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
 * maximum recursion depth, evaluation timeout, and security limits
 * for resource-bounded evaluation.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 *
 * @see <a href="../../../../../docs/security-hardening.md">Security Hardening</a>
 */
public record OclEvaluationOptions(
		NullHandling nullHandling,
		ErrorRecovery errorRecovery,
		int maxDepth,
		Duration timeout,
		int maxCollectionSize,
		int maxClosureIterations,
		int maxRegexLength) {

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
	private static final int DEFAULT_MAX_COLLECTION_SIZE = 1_000_000;
	private static final int DEFAULT_MAX_CLOSURE_ITERATIONS = 100_000;
	private static final int DEFAULT_MAX_REGEX_LENGTH = 1000;

	/**
	 * Canonical constructor with validation.
	 *
	 * @param nullHandling null handling strategy
	 * @param errorRecovery error recovery strategy
	 * @param maxDepth maximum recursion depth (must be positive)
	 * @param timeout evaluation timeout, or {@code null} for no timeout
	 * @param maxCollectionSize maximum collection size for ranges, products, allInstances (must be positive)
	 * @param maxClosureIterations maximum iterations for closure (must be positive)
	 * @param maxRegexLength maximum regex pattern length for matches/replaceAll/replaceFirst (must be positive)
	 */
	public OclEvaluationOptions {
		Objects.requireNonNull(nullHandling, "nullHandling must not be null");
		Objects.requireNonNull(errorRecovery, "errorRecovery must not be null");
		if (maxDepth <= 0) {
			throw new IllegalArgumentException("maxDepth must be positive: " + maxDepth);
		}
		if (maxCollectionSize <= 0) {
			throw new IllegalArgumentException("maxCollectionSize must be positive: " + maxCollectionSize);
		}
		if (maxClosureIterations <= 0) {
			throw new IllegalArgumentException("maxClosureIterations must be positive: " + maxClosureIterations);
		}
		if (maxRegexLength <= 0) {
			throw new IllegalArgumentException("maxRegexLength must be positive: " + maxRegexLength);
		}
	}

	/**
	 * Returns strict evaluation options: null access produces {@code OclInvalid},
	 * evaluation stops on first error, default depth, no timeout, default security limits.
	 *
	 * @return strict options
	 */
	public static OclEvaluationOptions strict() {
		return new OclEvaluationOptions(NullHandling.STRICT, ErrorRecovery.FAIL_FAST,
				DEFAULT_MAX_DEPTH, null,
				DEFAULT_MAX_COLLECTION_SIZE, DEFAULT_MAX_CLOSURE_ITERATIONS, DEFAULT_MAX_REGEX_LENGTH);
	}

	/**
	 * Returns lenient evaluation options: null access produces {@code null},
	 * errors are collected, default depth, no timeout, default security limits.
	 *
	 * @return lenient options
	 */
	public static OclEvaluationOptions lenient() {
		return new OclEvaluationOptions(NullHandling.LENIENT, ErrorRecovery.COLLECT_ERRORS,
				DEFAULT_MAX_DEPTH, null,
				DEFAULT_MAX_COLLECTION_SIZE, DEFAULT_MAX_CLOSURE_ITERATIONS, DEFAULT_MAX_REGEX_LENGTH);
	}

	/**
	 * Returns a copy with the given maximum recursion depth.
	 *
	 * @param maxDepth maximum recursion depth (must be positive)
	 * @return new options with the given depth
	 */
	public OclEvaluationOptions withMaxDepth(int maxDepth) {
		return new OclEvaluationOptions(nullHandling, errorRecovery, maxDepth, timeout,
				maxCollectionSize, maxClosureIterations, maxRegexLength);
	}

	/**
	 * Returns a copy with the given maximum collection size.
	 *
	 * @param maxCollectionSize maximum collection size (must be positive)
	 * @return new options with the given collection size limit
	 */
	public OclEvaluationOptions withMaxCollectionSize(int maxCollectionSize) {
		return new OclEvaluationOptions(nullHandling, errorRecovery, maxDepth, timeout,
				maxCollectionSize, maxClosureIterations, maxRegexLength);
	}

	/**
	 * Returns a copy with the given maximum closure iterations.
	 *
	 * @param maxClosureIterations maximum closure iterations (must be positive)
	 * @return new options with the given closure iteration limit
	 */
	public OclEvaluationOptions withMaxClosureIterations(int maxClosureIterations) {
		return new OclEvaluationOptions(nullHandling, errorRecovery, maxDepth, timeout,
				maxCollectionSize, maxClosureIterations, maxRegexLength);
	}

	/**
	 * Returns a copy with the given maximum regex pattern length.
	 *
	 * @param maxRegexLength maximum regex pattern length (must be positive)
	 * @return new options with the given regex length limit
	 */
	public OclEvaluationOptions withMaxRegexLength(int maxRegexLength) {
		return new OclEvaluationOptions(nullHandling, errorRecovery, maxDepth, timeout,
				maxCollectionSize, maxClosureIterations, maxRegexLength);
	}

	/**
	 * Returns a copy with the given evaluation timeout.
	 *
	 * @param timeout evaluation timeout, or {@code null} for no timeout
	 * @return new options with the given timeout
	 */
	public OclEvaluationOptions withTimeout(Duration timeout) {
		return new OclEvaluationOptions(nullHandling, errorRecovery, maxDepth, timeout,
				maxCollectionSize, maxClosureIterations, maxRegexLength);
	}
}
