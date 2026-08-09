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
package org.eclipse.fennec.m2x.qvtd.engine;

import java.util.Objects;
import java.util.Set;

import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;

/**
 * Translates the OSGi configuration of the QVT-R engine component into a
 * {@link QvtdConfiguration}.
 *
 * <p>Mirrors {@code OclConfigurationHelper}: translating configuration is the configurator's
 * job, not a constructor's.
 *
 * @since 1.0
 */
public final class QvtdConfigurationHelper {

	private QvtdConfigurationHelper() {
		// utility class
	}

	/**
	 * Creates a {@link QvtdConfiguration} that evaluates on the given OCL engine.
	 *
	 * @param config    the OSGi configuration annotation, must not be {@code null}
	 * @param oclEngine the engine to evaluate relation expressions with, must not be
	 *                  {@code null}
	 * @return the configuration
	 */
	public static QvtdConfiguration from(QvtdEngineConfiguration config, OclEngine oclEngine) {
		Objects.requireNonNull(config, "config must not be null");
		Objects.requireNonNull(oclEngine, "oclEngine must not be null");
		return QvtdConfiguration.builder(oclEngine)
				.blackboxEnabled(config.blackboxEnabled())
				.allowedBlackboxModules(Set.of(config.allowedBlackboxModules()))
				.unitResolverEnabled(config.unitResolverEnabled())
				.allowedUnitModules(Set.of(config.allowedUnitModules()))
				.maxBlackboxLibraries(config.maxBlackboxLibraries())
				.maxUnitResolvers(config.maxUnitResolvers())
				.maxRelationDepth(config.maxRelationDepth())
				.maxBindings(config.maxBindings())
				.timeoutMs(config.timeout())
				.maxTraceRecords(config.maxTraceRecords())
				.build();
	}
}
