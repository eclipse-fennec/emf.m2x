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
package org.eclipse.fennec.m2x.qvto.engine;

import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;

/**
 * Translates the OSGi configuration of the QVT-O engine component into a
 * {@link QvtoConfiguration}.
 *
 * <p>Mirrors {@code OclConfigurationHelper}: translating configuration is the configurator's
 * job, not a constructor's.
 *
 * @since 1.0
 */
public final class QvtoConfigurationHelper {

	private QvtoConfigurationHelper() {
		// utility class
	}

	/**
	 * Creates a {@link QvtoConfiguration} that evaluates on the given OCL engine.
	 *
	 * @param config    the OSGi configuration annotation, must not be {@code null}
	 * @param oclEngine the engine to evaluate transformation expressions with, must not be
	 *                  {@code null}
	 * @return the configuration
	 */
	public static QvtoConfiguration from(QvtoEngineConfiguration config, OclEngine oclEngine) {
		return from(config, oclEngine, null);
	}

	/**
	 * Creates a {@link QvtoConfiguration} that evaluates on the given OCL engine and, when
	 * the configuration asks for discovery, resolves units through the given resolver.
	 *
	 * <p>Under OSGi discovery means the service registry, so the resolver that performs the
	 * lookup is handed in rather than found: {@code QvtoConfiguration.discoverUnitResolvers}
	 * stays off, because that flag means the {@code ServiceLoader} of plain Java, and only
	 * one of the two should ever be in play.
	 *
	 * @param config          the OSGi configuration annotation, must not be {@code null}
	 * @param oclEngine       the engine to evaluate with, must not be {@code null}
	 * @param serviceResolver the resolver that looks units up in the service registry, or
	 *                        {@code null} when there is none
	 * @return the configuration
	 */
	public static QvtoConfiguration from(QvtoEngineConfiguration config, OclEngine oclEngine,
			QvtoUnitResolver serviceResolver) {
		return from(config, oclEngine, serviceResolver, null);
	}

	/**
	 * Creates a {@link QvtoConfiguration} that evaluates on the given OCL engine, resolves
	 * metamodels through the given resource set and, when the configuration asks for
	 * discovery, resolves units through the given resolver.
	 *
	 * <p>The resource set is the seam the DS component uses (#245): under {@code emf.osgi} it
	 * is the injected, configured stack, and its package registry is where a dynamically
	 * registered metamodel lives. Only that registry is used; nothing is loaded through the
	 * resource set here (D42).
	 *
	 * @param config          the OSGi configuration annotation, must not be {@code null}
	 * @param oclEngine       the engine to evaluate with, must not be {@code null}
	 * @param serviceResolver the resolver that looks units up in the service registry, or
	 *                        {@code null} when there is none
	 * @param resourceSet     the resource set whose package registry resolves metamodels, or
	 *                        {@code null} to keep the plain-Java fallback to the global registry
	 * @return the configuration
	 */
	public static QvtoConfiguration from(QvtoEngineConfiguration config, OclEngine oclEngine,
			QvtoUnitResolver serviceResolver, ResourceSet resourceSet) {
		Objects.requireNonNull(config, "config must not be null");
		Objects.requireNonNull(oclEngine, "oclEngine must not be null");
		QvtoConfiguration.Builder builder = QvtoConfiguration.builder(oclEngine)
				.blackboxEnabled(config.blackboxEnabled())
				.allowedBlackboxModules(Set.of(config.allowedBlackboxModules()))
				.unitResolverEnabled(config.unitResolverEnabled())
				.allowedUnitModules(Set.of(config.allowedUnitModules()))
				.maxBlackboxLibraries(config.maxBlackboxLibraries())
				.maxUnitResolvers(config.maxUnitResolvers());
		if (resourceSet != null) {
			builder.resourceSet(resourceSet);
		}
		if (config.discoverUnitResolvers() && serviceResolver != null) {
			builder.addUnitResolver(serviceResolver);
		}
		return builder.build();
	}
}
