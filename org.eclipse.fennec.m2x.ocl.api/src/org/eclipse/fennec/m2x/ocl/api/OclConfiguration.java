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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions.ErrorRecovery;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions.NullHandling;

/**
 * Immutable configuration for creating {@link OclEngine} instances.
 *
 * <p>Bundles all dependencies and optional settings needed by the engine:
 * parser, expression cache, and custom operation providers. Use the
 * {@link Builder} to construct instances:
 * <pre>
 * OclConfiguration config = OclConfiguration.builder(parser)
 *     .expressionCache(new OclLruExpressionCache(2048))
 *     .addOperationProvider(myProvider)
 *     .build();
 * OclEngine engine = new OclEngineImpl(config);
 * </pre>
 *
 * <p>A single configuration can be shared across multiple engine instances,
 * allowing them to share the same expression cache and operation providers.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class OclConfiguration {

	private static final int DEFAULT_MAX_DEPTH = 1000;
	private static final int DEFAULT_MAX_COLLECTION_SIZE = 1_000_000;
	private static final int DEFAULT_MAX_CLOSURE_ITERATIONS = 100_000;
	private static final int DEFAULT_MAX_REGEX_LENGTH = 1000;

	private final OclExpressionParser parser;
	private final OclExpressionCache expressionCache;
	private final List<OclOperationProvider> operationProviders;
	private final boolean customOperationsEnabled;
	private final NullHandling nullHandling;
	private final ErrorRecovery errorRecovery;
	private final int maxDepth;
	private final long timeoutMs;
	private final int maxCollectionSize;
	private final int maxClosureIterations;
	private final int maxRegexLength;

	private OclConfiguration(Builder builder) {
		this.parser = builder.parser;
		this.expressionCache = builder.expressionCache;
		this.operationProviders = Collections.unmodifiableList(new ArrayList<>(builder.operationProviders));
		this.customOperationsEnabled = builder.customOperationsEnabled;
		this.nullHandling = builder.nullHandling;
		this.errorRecovery = builder.errorRecovery;
		this.maxDepth = builder.maxDepth;
		this.timeoutMs = builder.timeoutMs;
		this.maxCollectionSize = builder.maxCollectionSize;
		this.maxClosureIterations = builder.maxClosureIterations;
		this.maxRegexLength = builder.maxRegexLength;
	}

	/**
	 * Returns the OCL expression parser.
	 *
	 * @return the parser, never {@code null}
	 */
	public OclExpressionParser parser() {
		return parser;
	}

	/**
	 * Returns the expression cache, or {@code null} if no caching is configured.
	 *
	 * @return the expression cache, or {@code null}
	 */
	public OclExpressionCache expressionCache() {
		return expressionCache;
	}

	/**
	 * Returns the pre-configured operation providers.
	 *
	 * <p>These providers are only active when both
	 * {@link #customOperationsEnabled()} is {@code true} on the config AND
	 * {@link OclEvaluationOptions#customOperationsEnabled()} is {@code true}
	 * on the evaluation options.
	 *
	 * @return unmodifiable list of operation providers, never {@code null}
	 */
	public List<OclOperationProvider> operationProviders() {
		return operationProviders;
	}

	/**
	 * Returns whether config-registered custom operations are enabled.
	 * Defaults to {@code false} (D29: disabled by default).
	 *
	 * @return {@code true} if custom operations are enabled
	 */
	public boolean customOperationsEnabled() {
		return customOperationsEnabled;
	}

	/**
	 * Returns the null handling strategy. Defaults to {@link NullHandling#STRICT}.
	 *
	 * @return the null handling strategy, never {@code null}
	 */
	public NullHandling nullHandling() {
		return nullHandling;
	}

	/**
	 * Returns the error recovery strategy. Defaults to {@link ErrorRecovery#FAIL_FAST}.
	 *
	 * @return the error recovery strategy, never {@code null}
	 */
	public ErrorRecovery errorRecovery() {
		return errorRecovery;
	}

	/**
	 * Returns the maximum recursion depth. Defaults to 1000.
	 *
	 * @return the maximum recursion depth
	 */
	public int maxDepth() {
		return maxDepth;
	}

	/**
	 * Returns the evaluation timeout in milliseconds. 0 means no timeout.
	 *
	 * @return the timeout in milliseconds
	 */
	public long timeoutMs() {
		return timeoutMs;
	}

	/**
	 * Returns the maximum collection size. Defaults to 1,000,000.
	 *
	 * @return the maximum collection size
	 */
	public int maxCollectionSize() {
		return maxCollectionSize;
	}

	/**
	 * Returns the maximum closure iterations. Defaults to 100,000.
	 *
	 * @return the maximum closure iterations
	 */
	public int maxClosureIterations() {
		return maxClosureIterations;
	}

	/**
	 * Returns the maximum regex pattern length. Defaults to 1000.
	 *
	 * @return the maximum regex length
	 */
	public int maxRegexLength() {
		return maxRegexLength;
	}

	/**
	 * Creates a new builder with the given parser.
	 *
	 * @param parser the OCL expression parser, must not be {@code null}
	 * @return a new builder
	 */
	public static Builder builder(OclExpressionParser parser) {
		return new Builder(parser);
	}

	/**
	 * Builder for {@link OclConfiguration} instances.
	 */
	public static final class Builder {

		private final OclExpressionParser parser;
		private OclExpressionCache expressionCache;
		private final List<OclOperationProvider> operationProviders = new ArrayList<>();
		private boolean customOperationsEnabled;
		private NullHandling nullHandling = NullHandling.STRICT;
		private ErrorRecovery errorRecovery = ErrorRecovery.FAIL_FAST;
		private int maxDepth = DEFAULT_MAX_DEPTH;
		private long timeoutMs;
		private int maxCollectionSize = DEFAULT_MAX_COLLECTION_SIZE;
		private int maxClosureIterations = DEFAULT_MAX_CLOSURE_ITERATIONS;
		private int maxRegexLength = DEFAULT_MAX_REGEX_LENGTH;

		private Builder(OclExpressionParser parser) {
			this.parser = Objects.requireNonNull(parser, "parser must not be null");
		}

		/**
		 * Sets the expression cache. When set, parsed expressions are cached
		 * and reused across evaluations.
		 *
		 * @param cache the expression cache, or {@code null} for no caching
		 * @return this builder
		 */
		public Builder expressionCache(OclExpressionCache cache) {
			this.expressionCache = cache;
			return this;
		}

		/**
		 * Adds a custom operation provider.
		 *
		 * @param provider the operation provider to add, must not be {@code null}
		 * @return this builder
		 */
		public Builder addOperationProvider(OclOperationProvider provider) {
			this.operationProviders.add(Objects.requireNonNull(provider, "provider must not be null"));
			return this;
		}

		/**
		 * Enables or disables config-registered custom operations (D29).
		 * Defaults to {@code false}.
		 *
		 * @param enabled whether custom operations are enabled
		 * @return this builder
		 */
		public Builder customOperationsEnabled(boolean enabled) {
			this.customOperationsEnabled = enabled;
			return this;
		}

		/**
		 * Sets all operation providers, replacing any previously added.
		 *
		 * @param providers the operation providers, must not be {@code null}
		 * @return this builder
		 */
		public Builder operationProviders(List<OclOperationProvider> providers) {
			this.operationProviders.clear();
			this.operationProviders.addAll(Objects.requireNonNull(providers, "providers must not be null"));
			return this;
		}

		/**
		 * Sets the null handling strategy.
		 *
		 * @param nullHandling the strategy, must not be {@code null}
		 * @return this builder
		 */
		public Builder nullHandling(NullHandling nullHandling) {
			this.nullHandling = Objects.requireNonNull(nullHandling, "nullHandling must not be null");
			return this;
		}

		/**
		 * Sets the error recovery strategy.
		 *
		 * @param errorRecovery the strategy, must not be {@code null}
		 * @return this builder
		 */
		public Builder errorRecovery(ErrorRecovery errorRecovery) {
			this.errorRecovery = Objects.requireNonNull(errorRecovery, "errorRecovery must not be null");
			return this;
		}

		/**
		 * Sets the maximum recursion depth.
		 *
		 * @param maxDepth the maximum depth (must be positive)
		 * @return this builder
		 */
		public Builder maxDepth(int maxDepth) {
			if (maxDepth <= 0) {
				throw new IllegalArgumentException("maxDepth must be positive: " + maxDepth);
			}
			this.maxDepth = maxDepth;
			return this;
		}

		/**
		 * Sets the evaluation timeout in milliseconds. 0 means no timeout.
		 *
		 * @param timeoutMs the timeout in milliseconds (must be non-negative)
		 * @return this builder
		 */
		public Builder timeoutMs(long timeoutMs) {
			if (timeoutMs < 0) {
				throw new IllegalArgumentException("timeoutMs must be non-negative: " + timeoutMs);
			}
			this.timeoutMs = timeoutMs;
			return this;
		}

		/**
		 * Sets the maximum collection size.
		 *
		 * @param maxCollectionSize the maximum size (must be positive)
		 * @return this builder
		 */
		public Builder maxCollectionSize(int maxCollectionSize) {
			if (maxCollectionSize <= 0) {
				throw new IllegalArgumentException("maxCollectionSize must be positive: " + maxCollectionSize);
			}
			this.maxCollectionSize = maxCollectionSize;
			return this;
		}

		/**
		 * Sets the maximum closure iterations.
		 *
		 * @param maxClosureIterations the maximum iterations (must be positive)
		 * @return this builder
		 */
		public Builder maxClosureIterations(int maxClosureIterations) {
			if (maxClosureIterations <= 0) {
				throw new IllegalArgumentException("maxClosureIterations must be positive: " + maxClosureIterations);
			}
			this.maxClosureIterations = maxClosureIterations;
			return this;
		}

		/**
		 * Sets the maximum regex pattern length.
		 *
		 * @param maxRegexLength the maximum length (must be positive)
		 * @return this builder
		 */
		public Builder maxRegexLength(int maxRegexLength) {
			if (maxRegexLength <= 0) {
				throw new IllegalArgumentException("maxRegexLength must be positive: " + maxRegexLength);
			}
			this.maxRegexLength = maxRegexLength;
			return this;
		}

		/**
		 * Builds an immutable {@link OclConfiguration}.
		 *
		 * @return the configuration
		 */
		public OclConfiguration build() {
			return new OclConfiguration(this);
		}
	}
}
