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

import java.util.Optional;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnit;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnitResolver;
import org.eclipse.fennec.m2x.unit.osgi.OsgiServiceUnitResolver;
import org.osgi.framework.BundleContext;
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

	private final OsgiServiceUnitResolver<QvtdUnitResolver, QvtdUnit> discovery;

	/**
	 * @param context the bundle context, injected by the component runtime
	 */
	@Activate
	public QvtdServiceUnitResolver(BundleContext context) {
		this.discovery = new OsgiServiceUnitResolver<>(context, QvtdUnitResolver.class,
				UNIT_NAME, RESOLVER_KIND, QvtdUnitResolver::resolveUnit);
	}

	@Override
	public Optional<QvtdUnit> resolveUnit(String qualifiedName) {
		return discovery.resolveUnit(qualifiedName);
	}

}
