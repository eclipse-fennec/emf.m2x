/*
 * ******************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Data In Motion Consulting - initial implementation
 * ******************************************************************
 */
package org.eclipse.fennec.m2x.m2t.engine.internal;

import java.util.Objects;

import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;

/**
 * What one generation is allowed to do: the ceilings of the M2T threat model (T-1 to T-6).
 *
 * <p>They travel together because they are decided together — one configuration sets all of
 * them — and passing them one by one is what made the evaluator's constructor twelve
 * parameters wide, four of them {@code int} and unlabelled at the call site (#185).
 *
 * @param maxDiagnostics maximum diagnostic entries before truncation
 * @param maxTemplateDepth maximum template invocation depth (T-1)
 * @param maxForIterations maximum for-block iterations (T-2)
 * @param maxCrossProductSize maximum cross-product size (T-3)
 * @param protectedAreaEnabled whether protected area markers are honoured (T-6)
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record M2tLimits(
		int maxDiagnostics,
		int maxTemplateDepth,
		int maxForIterations,
		int maxCrossProductSize,
		boolean protectedAreaEnabled) {

	/**
	 * The limits of an engine configuration.
	 *
	 * @param config the configuration, must not be {@code null}
	 * @return its limits
	 */
	public static M2tLimits of(M2tConfiguration config) {
		Objects.requireNonNull(config, "config must not be null");
		return new M2tLimits(config.maxDiagnostics(), config.maxTemplateDepth(),
				config.maxForIterations(), config.maxCrossProductSize(),
				config.protectedAreaEnabled());
	}

	/**
	 * The limits an evaluator built directly on an AST runs under.
	 *
	 * @return the default limits
	 */
	public static M2tLimits defaults() {
		return new M2tLimits(10_000, 1_000, 1_000_000, 1_000_000, true);
	}
}
