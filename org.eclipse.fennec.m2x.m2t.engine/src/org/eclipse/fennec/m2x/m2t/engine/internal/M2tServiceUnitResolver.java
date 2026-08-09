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
package org.eclipse.fennec.m2x.m2t.engine.internal;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.fennec.m2x.m2t.api.M2tUnit;
import org.eclipse.fennec.m2x.m2t.api.M2tUnitResolver;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * Looks a compilation unit up in the service registry, by the name the template gave.
 *
 * <p>Deliberately not a whiteboard. A template names what it imports, so the engine
 * knows at link time which name it needs and can ask for exactly that — while a declarative
 * reference would have to bind every resolver in the framework up front, and a static one
 * would restart the engine, dropping its caches, each time one came or went.
 *
 * <p>The name is a service property, so a bundle offering a unit publishes:
 *
 * <pre>
 * &#64;Component(property = M2tServiceUnitResolver.UNIT_NAME + "=my.company.Utilities")
 * public class UtilitiesUnit implements M2tUnitResolver { … }
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
@Component(name = "M2tServiceUnitResolver", service = M2tUnitResolver.class,
		property = M2tServiceUnitResolver.RESOLVER_KIND + "=discovery")
public class M2tServiceUnitResolver implements M2tUnitResolver {

	/** Service property naming the unit a resolver can produce. */
	public static final String UNIT_NAME = "m2t.unit.name";

	/** Service property marking this resolver, so the lookup can exclude itself. */
	public static final String RESOLVER_KIND = "m2t.resolver.kind";

	private static final Logger LOG = Logger.getLogger(M2tServiceUnitResolver.class.getName());

	private final BundleContext context;

	@Activate
	public M2tServiceUnitResolver(BundleContext context) {
		this.context = context;
	}

	@Override
	public Optional<M2tUnit> resolveUnit(String qualifiedName) {
		Collection<ServiceReference<M2tUnitResolver>> candidates;
		try {
			candidates = context.getServiceReferences(M2tUnitResolver.class,
					"(&(" + UNIT_NAME + "=" + qualifiedName + ")(!(" + RESOLVER_KIND + "=discovery)))");
		} catch (InvalidSyntaxException malformed) {
			// The name comes out of a template, so it can contain anything the
			// grammar allows — which is not necessarily a valid LDAP filter value.
			LOG.log(Level.FINE, malformed,
					() -> "Not a usable service filter, so nothing can be registered under it: "
							+ qualifiedName);
			return Optional.empty();
		}
		for (ServiceReference<M2tUnitResolver> reference : candidates) {
			M2tUnitResolver resolver = context.getService(reference);
			if (resolver == null) {
				continue; // gone between the lookup and now
			}
			try {
				Optional<M2tUnit> unit = resolver.resolveUnit(qualifiedName);
				if (unit.isPresent()) {
					return unit;
				}
			} catch (RuntimeException failure) {
				LOG.log(Level.WARNING, failure,
						() -> "Registered unit resolver failed for '" + qualifiedName
								+ "', skipping it: " + resolver.getClass().getName());
			} finally {
				context.ungetService(reference);
			}
		}
		return Optional.empty();
	}
}
