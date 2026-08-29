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
package org.eclipse.fennec.m2x.qvtd.engine.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.fennec.m2x.qvtd.api.QvtdUnit;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnitResolver;
import org.eclipse.fennec.m2x.unit.api.UnitNames;
import org.eclipse.fennec.m2x.unit.resolve.ResolutionPolicy;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * Looks a compilation unit up in the service registry, by the name the transformation gave.
 *
 * <p>Deliberately not a whiteboard. A transformation names what it imports, so the engine
 * knows at link time which name it needs and can ask for exactly that — while a declarative
 * reference would have to bind every resolver in the framework up front, and a static one
 * would restart the engine, dropping its caches, each time one came or went.
 *
 * <p>The name is a service property, so a bundle offering a unit publishes:
 *
 * <pre>
 * &#64;Component(property = QvtdServiceUnitResolver.UNIT_NAME + "=my.company.Utilities")
 * public class UtilitiesUnit implements QvtdUnitResolver { … }
 * </pre>
 *
 * <p>Its own registration carries {@link #RESOLVER_KIND}, and the lookup filter excludes
 * that, so discovery cannot find itself and recurse.
 *
 * <p>This widens who may answer, never which names may be asked: the engine checks the
 * allow-list before it gets here, and counts this as one resolver against the limit.
 *
 * @since 1.0
 */
@Component(name = "QvtdServiceUnitResolver", service = QvtdUnitResolver.class,
		property = QvtdServiceUnitResolver.RESOLVER_KIND + "=discovery")
public class QvtdServiceUnitResolver implements QvtdUnitResolver {

	/** Service property naming the unit a resolver can produce. */
	public static final String UNIT_NAME = "qvtd.unit.name";

	/** Service property marking this resolver, so the lookup can exclude itself. */
	public static final String RESOLVER_KIND = "qvtd.resolver.kind";

	private static final Logger LOG = Logger.getLogger(QvtdServiceUnitResolver.class.getName());

	private final BundleContext context;

	@Activate
	public QvtdServiceUnitResolver(BundleContext context) {
		this.context = context;
	}

	@Override
	public Optional<QvtdUnit> resolveUnit(String qualifiedName) {
		Collection<ServiceReference<QvtdUnitResolver>> candidates;
		try {
			candidates = context.getServiceReferences(QvtdUnitResolver.class,
					"(&(" + UNIT_NAME + "=" + UnitNames.escapeFilterValue(qualifiedName)
							+ ")(!(" + RESOLVER_KIND + "=discovery)))");
		} catch (InvalidSyntaxException malformed) {
			// The name comes out of a transformation, so it can contain anything the
			// grammar allows — which is not necessarily a valid LDAP filter value.
			LOG.log(Level.FINE, malformed,
					() -> "Not a usable service filter, so nothing can be registered under it: "
							+ qualifiedName);
			return Optional.empty();
		}
		// Ranking decides the order, highest first (#141): ServiceReference's natural order is
		// ascending by ranking and, for equal ranking, descending by id — reversed, the highest
		// ranking comes first and, among equals, the one registered first.
		List<ServiceReference<QvtdUnitResolver>> ordered = new ArrayList<>(candidates);
		ordered.sort(Comparator.reverseOrder());
		List<ResolutionPolicy.Source<QvtdUnit>> sources = new ArrayList<>();
		for (ServiceReference<QvtdUnitResolver> reference : ordered) {
			sources.add(ResolutionPolicy.Source.of(describe(reference), name -> {
				QvtdUnitResolver resolver = context.getService(reference);
				if (resolver == null) {
					return Optional.empty(); // gone between the lookup and now
				}
				try {
					return resolver.resolveUnit(name);
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
