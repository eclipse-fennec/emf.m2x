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
package org.eclipse.fennec.m2x.m2t.engine;

import java.nio.charset.Charset;
import java.util.Objects;

import java.util.Set;

import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tUnitResolver;
import org.eclipse.fennec.m2x.m2t.api.WhitespaceMode;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;

/**
 * Translates the OSGi configuration of the M2T engine component into an
 * {@link M2tConfiguration}.
 *
 * <p>Mirrors {@code OclConfigurationHelper}: translating configuration is the configurator's
 * job, not a constructor's.
 *
 * @since 1.0
 */
public final class M2tConfigurationHelper {

	private M2tConfigurationHelper() {
		// utility class
	}

	/**
	 * Creates an {@link M2tConfiguration} that evaluates on the given OCL engine.
	 *
	 * @param config    the OSGi configuration annotation, must not be {@code null}
	 * @param oclEngine the engine to evaluate template expressions with, must not be
	 *                  {@code null}
	 * @return the configuration
	 */
	public static M2tConfiguration from(M2tEngineConfiguration config, OclEngine oclEngine) {
		return from(config, oclEngine, null);
	}

	/**
	 * Creates an {@link M2tConfiguration} that evaluates on the given OCL engine and, when
	 * the configuration asks for discovery, resolves modules through the given resolver.
	 *
	 * @param config          the OSGi configuration annotation, must not be {@code null}
	 * @param oclEngine       the engine to evaluate with, must not be {@code null}
	 * @param serviceResolver the resolver that looks modules up in the service registry, or
	 *                        {@code null} when there is none
	 * @return the configuration
	 */
	public static M2tConfiguration from(M2tEngineConfiguration config, OclEngine oclEngine,
			M2tUnitResolver serviceResolver) {
		Objects.requireNonNull(config, "config must not be null");
		Objects.requireNonNull(oclEngine, "oclEngine must not be null");
		M2tConfiguration.Builder builder = M2tConfiguration.builder(oclEngine)
				.defaultCharset(Charset.forName(config.defaultCharset()))
				.whitespaceMode(WhitespaceMode.valueOf(config.whitespaceMode()))
				.maxDiagnostics(config.maxDiagnostics())
				.maxTemplateDepth(config.maxTemplateDepth())
				.maxForIterations(config.maxForIterations())
				.maxCrossProductSize(config.maxCrossProductSize())
				.maxOutputSize(config.maxOutputSize())
				.protectedAreaEnabled(config.protectedAreaEnabled())
				.unitResolverEnabled(config.unitResolverEnabled())
				.allowedUnitModules(Set.of(config.allowedUnitModules()))
				.maxUnitResolvers(config.maxUnitResolvers());
		if (config.discoverUnitResolvers() && serviceResolver != null) {
			builder.addUnitResolver(serviceResolver);
		}
		return builder.build();
	}
}
