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

import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
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
		Objects.requireNonNull(config, "config must not be null");
		Objects.requireNonNull(oclEngine, "oclEngine must not be null");
		return M2tConfiguration.builder(oclEngine)
				.defaultCharset(Charset.forName(config.defaultCharset()))
				.whitespaceMode(WhitespaceMode.valueOf(config.whitespaceMode()))
				.maxDiagnostics(config.maxDiagnostics())
				.maxTemplateDepth(config.maxTemplateDepth())
				.maxForIterations(config.maxForIterations())
				.maxCrossProductSize(config.maxCrossProductSize())
				.maxOutputSize(config.maxOutputSize())
				.protectedAreaEnabled(config.protectedAreaEnabled())
				.build();
	}
}
