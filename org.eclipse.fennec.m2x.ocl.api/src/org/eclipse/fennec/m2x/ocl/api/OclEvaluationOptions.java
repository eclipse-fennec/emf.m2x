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
package org.eclipse.fennec.m2x.ocl.api;

import java.time.Duration;
import java.util.List;
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
		int maxRegexLength,
		boolean customOperationsEnabled,
		List<OclOperationProvider> additionalProviders,
		boolean useEMFTypes) {

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
	 * @param customOperationsEnabled whether config-registered custom operations are active
	 * @param additionalProviders per-evaluation providers, always active regardless of enable flag
	 * @param useEMFTypes when {@code true}, top-level {@link java.util.Collection} results are
	 *        returned as {@link org.eclipse.emf.common.util.EList} and top-level {@link java.util.Map}
	 *        results as {@link org.eclipse.emf.common.util.EMap}
	 *        (see <a href="https://github.com/eclipse-fennec/emf.m2x/issues/4">issue #4</a>)
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
		additionalProviders = List.copyOf(Objects.requireNonNull(additionalProviders,
				"additionalProviders must not be null"));
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
				DEFAULT_MAX_COLLECTION_SIZE, DEFAULT_MAX_CLOSURE_ITERATIONS, DEFAULT_MAX_REGEX_LENGTH,
				false, List.of(), false);
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
				DEFAULT_MAX_COLLECTION_SIZE, DEFAULT_MAX_CLOSURE_ITERATIONS, DEFAULT_MAX_REGEX_LENGTH,
				false, List.of(), false);
	}

	/**
	 * Returns a copy with the given maximum recursion depth.
	 *
	 * @param maxDepth maximum recursion depth (must be positive)
	 * @return new options with the given depth
	 */
	public OclEvaluationOptions withMaxDepth(int maxDepth) {
		return new OclEvaluationOptions(nullHandling, errorRecovery, maxDepth, timeout,
				maxCollectionSize, maxClosureIterations, maxRegexLength,
				customOperationsEnabled, additionalProviders, useEMFTypes);
	}

	/**
	 * Returns a copy with the given maximum collection size.
	 *
	 * @param maxCollectionSize maximum collection size (must be positive)
	 * @return new options with the given collection size limit
	 */
	public OclEvaluationOptions withMaxCollectionSize(int maxCollectionSize) {
		return new OclEvaluationOptions(nullHandling, errorRecovery, maxDepth, timeout,
				maxCollectionSize, maxClosureIterations, maxRegexLength,
				customOperationsEnabled, additionalProviders, useEMFTypes);
	}

	/**
	 * Returns a copy with the given maximum closure iterations.
	 *
	 * @param maxClosureIterations maximum closure iterations (must be positive)
	 * @return new options with the given closure iteration limit
	 */
	public OclEvaluationOptions withMaxClosureIterations(int maxClosureIterations) {
		return new OclEvaluationOptions(nullHandling, errorRecovery, maxDepth, timeout,
				maxCollectionSize, maxClosureIterations, maxRegexLength,
				customOperationsEnabled, additionalProviders, useEMFTypes);
	}

	/**
	 * Returns a copy with the given maximum regex pattern length.
	 *
	 * @param maxRegexLength maximum regex pattern length (must be positive)
	 * @return new options with the given regex length limit
	 */
	public OclEvaluationOptions withMaxRegexLength(int maxRegexLength) {
		return new OclEvaluationOptions(nullHandling, errorRecovery, maxDepth, timeout,
				maxCollectionSize, maxClosureIterations, maxRegexLength,
				customOperationsEnabled, additionalProviders, useEMFTypes);
	}

	/**
	 * Returns a copy with the given evaluation timeout.
	 *
	 * @param timeout evaluation timeout, or {@code null} for no timeout
	 * @return new options with the given timeout
	 */
	public OclEvaluationOptions withTimeout(Duration timeout) {
		return new OclEvaluationOptions(nullHandling, errorRecovery, maxDepth, timeout,
				maxCollectionSize, maxClosureIterations, maxRegexLength,
				customOperationsEnabled, additionalProviders, useEMFTypes);
	}

	/**
	 * Returns a copy with the given custom operations enabled flag.
	 *
	 * <p>When {@code true} AND the engine's {@link OclConfiguration} also has
	 * custom operations enabled, config-registered providers will be active.
	 *
	 * @param enabled whether to enable config-registered custom operations
	 * @return new options with the given flag
	 */
	public OclEvaluationOptions withCustomOperationsEnabled(boolean enabled) {
		return new OclEvaluationOptions(nullHandling, errorRecovery, maxDepth, timeout,
				maxCollectionSize, maxClosureIterations, maxRegexLength,
				enabled, additionalProviders, useEMFTypes);
	}

	/**
	 * Returns a copy with the given additional providers.
	 *
	 * <p>Additional providers are always active regardless of the
	 * {@code customOperationsEnabled} flag. This is used for per-evaluation
	 * providers such as the QVT-O↔OCL bridge.
	 *
	 * @param providers per-evaluation providers, must not be {@code null}
	 * @return new options with the given providers
	 */
	public OclEvaluationOptions withAdditionalProviders(List<OclOperationProvider> providers) {
		return new OclEvaluationOptions(nullHandling, errorRecovery, maxDepth, timeout,
				maxCollectionSize, maxClosureIterations, maxRegexLength,
				customOperationsEnabled, providers, useEMFTypes);
	}

	/**
	 * Returns a copy with the given {@code useEMFTypes} flag.
	 *
	 * <p>When {@code true}, top-level {@link java.util.Collection} results of
	 * {@code OclEngine.evaluate(...)} are wrapped as
	 * {@link org.eclipse.emf.common.util.EList} and top-level {@link java.util.Map}
	 * results as {@link org.eclipse.emf.common.util.EMap}. Nested collections
	 * are not rewrapped. Defaults to {@code false} (OCL-spec-native Java types).
	 *
	 * @param useEMFTypes whether to return EMF collection types at the top level
	 * @return new options with the given flag
	 *
	 * @see <a href="https://github.com/eclipse-fennec/emf.m2x/issues/4">issue #4</a>
	 */
	public OclEvaluationOptions withUseEMFTypes(boolean useEMFTypes) {
		return new OclEvaluationOptions(nullHandling, errorRecovery, maxDepth, timeout,
				maxCollectionSize, maxClosureIterations, maxRegexLength,
				customOperationsEnabled, additionalProviders, useEMFTypes);
	}
}
