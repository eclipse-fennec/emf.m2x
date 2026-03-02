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

	private final OclExpressionParser parser;
	private final OclExpressionCache expressionCache;
	private final List<OclOperationProvider> operationProviders;
	private final boolean customOperationsEnabled;

	private OclConfiguration(Builder builder) {
		this.parser = builder.parser;
		this.expressionCache = builder.expressionCache;
		this.operationProviders = Collections.unmodifiableList(new ArrayList<>(builder.operationProviders));
		this.customOperationsEnabled = builder.customOperationsEnabled;
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
		 * Builds an immutable {@link OclConfiguration}.
		 *
		 * @return the configuration
		 */
		public OclConfiguration build() {
			return new OclConfiguration(this);
		}
	}
}
