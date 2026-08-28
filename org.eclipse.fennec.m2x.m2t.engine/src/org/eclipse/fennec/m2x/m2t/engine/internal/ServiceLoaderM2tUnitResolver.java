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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import org.eclipse.fennec.m2x.m2t.api.M2tUnit;
import org.eclipse.fennec.m2x.m2t.api.M2tUnitResolver;
import org.eclipse.fennec.m2x.unit.api.UnitResolutionException;
import org.eclipse.fennec.m2x.unit.resolve.ResolutionPolicy;

/**
 * Asks every {@link M2tUnitResolver} on the class path, in the order
 * {@link ServiceLoader} presents them.
 *
 * <p>This is what discovery means outside OSGi. It is one resolver from the engine's point
 * of view — it counts once towards the resolver limit, and the allow-list is checked before
 * it is ever asked, so discovery cannot widen which names may be resolved, only who may
 * answer for them.
 *
 * <p>A provider that fails to load is skipped rather than allowed to abort the link: one
 * broken jar on the class path should not decide whether an unrelated import resolves.
 *
 * <p>The lookup is bound to the class loader of {@link M2tUnitResolver} rather than to the
 * thread context class loader, which is what the one-argument {@code ServiceLoader.load}
 * would use. Outside OSGi the two are the same in practice; inside it the context loader is
 * whatever the caller happened to leave there, so the result would depend on who called the
 * engine. Under OSGi discovery goes through {@link M2tServiceUnitResolver} and the service
 * registry instead, which is why this bundle deliberately declares no
 * {@code osgi.serviceloader} requirement: with one, a Service Loader Mediator such as Aries
 * SPI Fly would weave this call and feed it from the same registry, and the two mechanisms
 * would answer the same import.
 *
 * @since 1.0
 */
public final class ServiceLoaderM2tUnitResolver implements M2tUnitResolver {


	private final ServiceLoader<M2tUnitResolver> loader;

	public ServiceLoaderM2tUnitResolver() {
		this(ServiceLoader.load(M2tUnitResolver.class, M2tUnitResolver.class.getClassLoader()));
	}

	ServiceLoaderM2tUnitResolver(ServiceLoader<M2tUnitResolver> loader) {
		this.loader = loader;
	}

	@Override
	public Optional<M2tUnit> resolveUnit(String qualifiedName) {
		// Declaration order decides; every provider is asked, a failing one is an error, answers
		// have to agree (#141). A provider that cannot even be instantiated is a failing source.
		List<ResolutionPolicy.Source<M2tUnit>> sources = new ArrayList<>();
		try {
			for (M2tUnitResolver resolver : loader) {
				// A provider that declared itself would otherwise recurse forever.
				if (resolver instanceof ServiceLoaderM2tUnitResolver) {
					continue;
				}
				sources.add(ResolutionPolicy.Source.of("class-path provider " + resolver.getClass().getName(),
						resolver::resolveUnit));
			}
		} catch (ServiceConfigurationError failure) {
			throw new UnitResolutionException("a class-path provider of " + M2tUnitResolver.class.getSimpleName()
					+ " could not be loaded for '" + qualifiedName + "': " + failure.getMessage(), failure);
		}
		return ResolutionPolicy.resolve(qualifiedName, sources);
	}
}
