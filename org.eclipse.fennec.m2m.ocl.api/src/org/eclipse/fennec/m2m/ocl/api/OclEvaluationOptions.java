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
package org.eclipse.fennec.m2m.ocl.api;

import java.time.Duration;
import java.util.Objects;

/**
 * Immutable configuration for OCL expression evaluation.
 *
 * <p>Controls null handling semantics, error recovery strategy,
 * maximum recursion depth, and evaluation timeout.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record OclEvaluationOptions(
		NullHandling nullHandling,
		ErrorRecovery errorRecovery,
		int maxDepth,
		Duration timeout) {

	/**
	 * Controls how {@code null} values are handled during navigation.
	 */
	public enum NullHandling {
		/** Null property access produces {@link OclInvalid#INSTANCE}. */
		STRICT,
		/** Null property access produces {@code null}. */
		LENIENT
	}

	/**
	 * Controls how evaluation errors are handled.
	 */
	public enum ErrorRecovery {
		/** Stop evaluation on the first error. */
		FAIL_FAST,
		/** Collect all errors and continue where possible. */
		COLLECT_ERRORS
	}

	private static final int DEFAULT_MAX_DEPTH = 1000;

	/**
	 * Canonical constructor with validation.
	 *
	 * @param nullHandling null handling strategy
	 * @param errorRecovery error recovery strategy
	 * @param maxDepth maximum recursion depth (must be positive)
	 * @param timeout evaluation timeout, or {@code null} for no timeout
	 */
	public OclEvaluationOptions {
		Objects.requireNonNull(nullHandling, "nullHandling must not be null");
		Objects.requireNonNull(errorRecovery, "errorRecovery must not be null");
		if (maxDepth <= 0) {
			throw new IllegalArgumentException("maxDepth must be positive: " + maxDepth);
		}
	}

	/**
	 * Returns strict evaluation options: null access produces {@code OclInvalid},
	 * evaluation stops on first error, default depth, no timeout.
	 *
	 * @return strict options
	 */
	public static OclEvaluationOptions strict() {
		return new OclEvaluationOptions(NullHandling.STRICT, ErrorRecovery.FAIL_FAST,
				DEFAULT_MAX_DEPTH, null);
	}

	/**
	 * Returns lenient evaluation options: null access produces {@code null},
	 * errors are collected, default depth, no timeout.
	 *
	 * @return lenient options
	 */
	public static OclEvaluationOptions lenient() {
		return new OclEvaluationOptions(NullHandling.LENIENT, ErrorRecovery.COLLECT_ERRORS,
				DEFAULT_MAX_DEPTH, null);
	}
}
