/*
 * ******************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Data In Motion Consulting - initial implementation
 * ******************************************************************
 */
package org.eclipse.fennec.m2x.unit.osgi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitNames;
import org.eclipse.fennec.m2x.unit.resolve.ResolutionPolicy;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 * Looks a compilation unit up in the service registry, by the name a unit asked for.
 *
 * <p>One implementation for all three languages (#192). It was three, differing only in the
 * type of the resolver service and in two property names — and the difference cost twice
 * already: the whiteboard ranking of #141 and the filter escaping of #182 each had to be
 * written three times, which is the shape of mistake where the third one is forgotten.
 *
 * <p><b>Deliberately not a whiteboard reference.</b> A unit names what it imports, so the
 * engine knows at link time which name it needs and can ask for exactly that. A declarative
 * reference would have to bind every resolver in the framework up front, and a static one
 * would restart the engine — dropping its caches — each time one came or went.
 *
 * <p><b>What this widens is who may answer, never which names may be asked.</b> The engine
 * checks its allow-list before it gets here and counts this as one resolver against its
 * limit.
 *
 * @param <R> the language's resolver service type
 * @param <U> the language's unit type
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class OsgiServiceUnitResolver<R, U extends Unit> {

	private static final Logger LOG = Logger.getLogger(OsgiServiceUnitResolver.class.getName());

	/** The value of the resolver-kind property that marks a discovery resolver. */
	public static final String DISCOVERY = "discovery";

	private final BundleContext context;
	private final Class<R> resolverType;
	private final String unitNameProperty;
	private final String resolverKindProperty;
	private final BiFunction<R, String, Optional<U>> ask;

	/**
	 * Creates a discovery resolver for one language.
	 *
	 * @param context the bundle context to look services up in, must not be {@code null}
	 * @param resolverType the language's resolver service type, must not be {@code null}
	 * @param unitNameProperty the service property naming the unit a resolver serves,
	 *            e.g. {@code qvto.unit.name}
	 * @param resolverKindProperty the service property marking a discovery resolver, so the
	 *            lookup can exclude itself and cannot recurse
	 * @param ask how a resolver of that type is asked for a name
	 */
	public OsgiServiceUnitResolver(BundleContext context, Class<R> resolverType,
			String unitNameProperty, String resolverKindProperty,
			BiFunction<R, String, Optional<U>> ask) {
		this.context = Objects.requireNonNull(context, "context must not be null");
		this.resolverType = Objects.requireNonNull(resolverType, "resolverType must not be null");
		this.unitNameProperty = Objects.requireNonNull(unitNameProperty, "unitNameProperty must not be null");
		this.resolverKindProperty = Objects.requireNonNull(resolverKindProperty,
				"resolverKindProperty must not be null");
		this.ask = Objects.requireNonNull(ask, "ask must not be null");
	}

	/**
	 * The unit registered under that name, from the service with the highest ranking.
	 *
	 * @param qualifiedName the name a unit imported
	 * @return the unit, or empty if no service offers it
	 * @throws org.eclipse.fennec.m2x.unit.api.UnitResolutionException if a service that offers
	 *             the name cannot answer, or if two of them disagree about it
	 */
	public Optional<U> resolveUnit(String qualifiedName) {
		Collection<ServiceReference<R>> candidates;
		try {
			candidates = context.getServiceReferences(resolverType,
					"(&(" + unitNameProperty + "=" + UnitNames.escapeFilterValue(qualifiedName)
							+ ")(!(" + resolverKindProperty + "=" + DISCOVERY + ")))");
		} catch (InvalidSyntaxException malformed) {
			// The name comes out of a unit, so it can contain anything the grammar allows —
			// which is not necessarily a valid LDAP filter value. Escaped (#182), a name that
			// reads as a filter finds nothing rather than everything; one that cannot be
			// escaped into a filter at all cannot have been registered under either.
			LOG.log(Level.FINE, malformed,
					() -> "Not a usable service filter, so nothing can be registered under it: "
							+ qualifiedName);
			return Optional.empty();
		}
		// Ranking decides the order, highest first (#141): ServiceReference's natural order is
		// ascending by ranking and, for equal ranking, descending by id — reversed, the highest
		// ranking comes first and, among equals, the one registered first.
		List<ServiceReference<R>> ordered = new ArrayList<>(candidates);
		ordered.sort(Comparator.reverseOrder());
		List<ResolutionPolicy.Source<U>> sources = new ArrayList<>();
		for (ServiceReference<R> reference : ordered) {
			sources.add(ResolutionPolicy.Source.of(describe(reference), name -> {
				R resolver = context.getService(reference);
				if (resolver == null) {
					return Optional.empty(); // gone between the lookup and now
				}
				try {
					return ask.apply(resolver, name);
				} finally {
					context.ungetService(reference);
				}
			}));
		}
		// Every source is asked, a failing source is an error, answers have to agree — and a
		// failure travels to the caller as a UnitResolutionException, not as "not found"
		return ResolutionPolicy.resolve(qualifiedName, sources);
	}

	private static String describe(ServiceReference<?> reference) {
		Object ranking = reference.getProperty(Constants.SERVICE_RANKING);
		return "service " + reference.getProperty(Constants.SERVICE_ID) + " of bundle "
				+ (reference.getBundle() == null ? "?" : reference.getBundle().getSymbolicName())
				+ (ranking == null ? "" : " (ranking " + ranking + ")");
	}
}
