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
package org.eclipse.fennec.m2x.unit.resolve;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.function.BiFunction;

import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitResolutionException;

/**
 * Asks the class path for a compilation unit, through {@link ServiceLoader}.
 *
 * <p>One implementation for all three languages (#192), which had one each, differing only in
 * the resolver type. What discovery means outside OSGi: it is one resolver from the engine's
 * point of view — it counts once towards the resolver limit, and the allow-list is checked
 * before it is ever asked, so discovery widens who may answer, never which names may be
 * resolved.
 *
 * <p>Declaration order decides, every provider is asked, a failing provider is an error and
 * answers have to agree (#141) — the same {@link ResolutionPolicy} the whiteboard and the
 * store go through. A provider that cannot even be instantiated is a failing source, not a
 * silent absence.
 *
 * <p>The lookup is bound to the class loader of the resolver interface rather than to the
 * thread context class loader, which is what the one-argument {@code ServiceLoader.load}
 * would use: outside OSGi the two are the same in practice, inside it the context loader is
 * whatever the caller happened to leave there, so the result would depend on who called the
 * engine. Under OSGi discovery goes through the service registry instead, which is why the
 * language bundles deliberately declare no {@code osgi.serviceloader} requirement — with one,
 * a Service Loader Mediator would weave this call and feed it from the same registry, and the
 * two mechanisms would answer the same import.
 *
 * @param <R> the language's resolver service type
 * @param <U> the language's unit type
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class ServiceLoaderUnitResolver<R, U extends Unit> {

	private final Class<R> resolverType;
	private final ServiceLoader<R> loader;
	private final BiFunction<R, String, Optional<U>> ask;
	private final Class<?> selfType;

	/**
	 * Creates a class-path resolver for one language.
	 *
	 * @param resolverType the language's resolver service type, must not be {@code null}
	 * @param ask how a resolver of that type is asked for a name
	 * @param selfType the language's own class-path resolver, so a provider that declared it
	 *            is skipped rather than allowed to recurse forever
	 */
	public ServiceLoaderUnitResolver(Class<R> resolverType, BiFunction<R, String, Optional<U>> ask,
			Class<?> selfType) {
		this(resolverType, ServiceLoader.load(resolverType, resolverType.getClassLoader()), ask, selfType);
	}

	/**
	 * Creates a class-path resolver over a given loader, for tests.
	 *
	 * @param resolverType the language's resolver service type
	 * @param loader the loader to ask
	 * @param ask how a resolver of that type is asked for a name
	 * @param selfType the language's own class-path resolver
	 */
	public ServiceLoaderUnitResolver(Class<R> resolverType, ServiceLoader<R> loader,
			BiFunction<R, String, Optional<U>> ask, Class<?> selfType) {
		this.resolverType = Objects.requireNonNull(resolverType, "resolverType must not be null");
		this.loader = Objects.requireNonNull(loader, "loader must not be null");
		this.ask = Objects.requireNonNull(ask, "ask must not be null");
		this.selfType = Objects.requireNonNull(selfType, "selfType must not be null");
	}

	/**
	 * The unit the class path offers under that name.
	 *
	 * @param qualifiedName the name a unit imported
	 * @return the unit, or empty if no provider offers it
	 * @throws UnitResolutionException if a provider cannot be loaded or cannot answer, or if
	 *             two of them disagree
	 */
	public Optional<U> resolveUnit(String qualifiedName) {
		List<ResolutionPolicy.Source<U>> sources = new ArrayList<>();
		try {
			for (R resolver : loader) {
				if (selfType.isInstance(resolver)) {
					continue;
				}
				sources.add(ResolutionPolicy.Source.of(
						"class-path provider " + resolver.getClass().getName(),
						name -> ask.apply(resolver, name)));
			}
		} catch (ServiceConfigurationError failure) {
			throw new UnitResolutionException("a class-path provider of " + resolverType.getSimpleName()
					+ " could not be loaded for '" + qualifiedName + "': " + failure.getMessage(), failure);
		}
		return ResolutionPolicy.resolve(qualifiedName, sources);
	}
}
