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
package org.eclipse.fennec.m2x.m2t.api;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;

/**
 * Immutable configuration for creating {@link M2tEngine} instances.
 *
 * <p>Bundles the mandatory {@link OclConfiguration} for the underlying OCL
 * evaluator, plus optional generation strategy and charset. Use the
 * {@link Builder} to construct instances:
 * <pre>
 * M2tConfiguration config = M2tConfiguration.builder(oclConfig)
 *     .defaultCharset(StandardCharsets.UTF_8)
 *     .generationStrategy(myStrategy)
 *     .build();
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class M2tConfiguration {

	/** Default maximum number of diagnostic entries before truncation. */
	public static final int DEFAULT_MAX_DIAGNOSTICS = 10_000;
	/** Default maximum template invocation depth (T-1 protection). */
	public static final int DEFAULT_MAX_TEMPLATE_DEPTH = 1_000;
	/** Default maximum for-block iterations (T-2 protection). */
	public static final int DEFAULT_MAX_FOR_ITERATIONS = 1_000_000;
	/** Default maximum cross-product size for set-argument invocations (T-3 protection). */
	public static final int DEFAULT_MAX_CROSS_PRODUCT_SIZE = 1_000_000;
	/** Default maximum total output size in characters (T-7 protection). 10 MB ~ 10,000,000 chars. */
	public static final long DEFAULT_MAX_OUTPUT_SIZE = 10_000_000L;

	private final OclConfiguration oclConfiguration;
	private final M2tGenerationStrategy generationStrategy;
	private final Charset defaultCharset;
	private final WhitespaceMode whitespaceMode;
	private final int maxDiagnostics;
	private final int maxTemplateDepth;
	private final int maxForIterations;
	private final int maxCrossProductSize;
	private final long maxOutputSize;
	private final boolean protectedAreaEnabled;

	private M2tConfiguration(Builder builder) {
		this.oclConfiguration = builder.oclConfiguration;
		this.generationStrategy = builder.generationStrategy;
		this.defaultCharset = builder.defaultCharset;
		this.whitespaceMode = builder.whitespaceMode;
		this.maxDiagnostics = builder.maxDiagnostics;
		this.maxTemplateDepth = builder.maxTemplateDepth;
		this.maxForIterations = builder.maxForIterations;
		this.maxCrossProductSize = builder.maxCrossProductSize;
		this.maxOutputSize = builder.maxOutputSize;
		this.protectedAreaEnabled = builder.protectedAreaEnabled;
	}

	public OclConfiguration oclConfiguration() {
		return oclConfiguration;
	}

	/**
	 * Returns the generation strategy, or {@code null} if none configured.
	 * When {@code null}, the engine uses an in-memory string writer.
	 */
	public M2tGenerationStrategy generationStrategy() {
		return generationStrategy;
	}

	/**
	 * Returns the default charset for file output. Defaults to UTF-8.
	 */
	public Charset defaultCharset() {
		return defaultCharset;
	}

	/**
	 * Returns the whitespace handling mode.
	 * Defaults to {@link WhitespaceMode#ACCELEO}.
	 *
	 * @see WhitespaceMode
	 */
	public WhitespaceMode whitespaceMode() {
		return whitespaceMode;
	}

	/**
	 * Returns the maximum number of diagnostic entries before truncation.
	 * Defaults to {@value #DEFAULT_MAX_DIAGNOSTICS}.
	 */
	public int maxDiagnostics() {
		return maxDiagnostics;
	}

	/**
	 * Returns the maximum template invocation depth.
	 * Protects against T-1 (template recursion stack overflow).
	 * Defaults to {@value #DEFAULT_MAX_TEMPLATE_DEPTH}.
	 */
	public int maxTemplateDepth() {
		return maxTemplateDepth;
	}

	/**
	 * Returns the maximum number of for-block iterations.
	 * Protects against T-2 (for-block iteration exhaustion).
	 * Defaults to {@value #DEFAULT_MAX_FOR_ITERATIONS}.
	 */
	public int maxForIterations() {
		return maxForIterations;
	}

	/**
	 * Returns the maximum cross-product size for set-argument invocations.
	 * Protects against T-3 (cross-product explosion).
	 * Defaults to {@value #DEFAULT_MAX_CROSS_PRODUCT_SIZE}.
	 */
	public int maxCrossProductSize() {
		return maxCrossProductSize;
	}

	/**
	 * Returns the maximum total output size in characters.
	 * Protects against T-7 (output size exhaustion).
	 * Defaults to {@value #DEFAULT_MAX_OUTPUT_SIZE} (~10 MB).
	 * A value of {@code 0} means unlimited.
	 */
	public long maxOutputSize() {
		return maxOutputSize;
	}

	/**
	 * Returns whether protected area support is enabled.
	 * When disabled, {@code [protected]} blocks emit their body content
	 * without markers, and no merge with existing content is performed.
	 * Defaults to {@code true}.
	 *
	 * <p>Disabling protected areas mitigates T-6 (protected area marker injection).
	 */
	public boolean protectedAreaEnabled() {
		return protectedAreaEnabled;
	}

	public static Builder builder(OclConfiguration oclConfiguration) {
		return new Builder(oclConfiguration);
	}

	public static final class Builder {

		private final OclConfiguration oclConfiguration;
		private M2tGenerationStrategy generationStrategy;
		private Charset defaultCharset = StandardCharsets.UTF_8;
		private WhitespaceMode whitespaceMode = WhitespaceMode.ACCELEO;
		private int maxDiagnostics = DEFAULT_MAX_DIAGNOSTICS;
		private int maxTemplateDepth = DEFAULT_MAX_TEMPLATE_DEPTH;
		private int maxForIterations = DEFAULT_MAX_FOR_ITERATIONS;
		private int maxCrossProductSize = DEFAULT_MAX_CROSS_PRODUCT_SIZE;
		private long maxOutputSize = DEFAULT_MAX_OUTPUT_SIZE;
		private boolean protectedAreaEnabled = true;

		private Builder(OclConfiguration oclConfiguration) {
			this.oclConfiguration = Objects.requireNonNull(oclConfiguration, "oclConfiguration must not be null");
		}

		/**
		 * Sets the generation strategy for file output.
		 *
		 * @param strategy the generation strategy
		 * @return this builder
		 */
		public Builder generationStrategy(M2tGenerationStrategy strategy) {
			this.generationStrategy = Objects.requireNonNull(strategy, "strategy must not be null");
			return this;
		}

		/**
		 * Sets the default charset for file output.
		 *
		 * @param charset the charset (default: UTF-8)
		 * @return this builder
		 */
		public Builder defaultCharset(Charset charset) {
			this.defaultCharset = Objects.requireNonNull(charset, "charset must not be null");
			return this;
		}

		/**
		 * Sets the whitespace handling mode.
		 *
		 * @param mode the whitespace mode (default: {@link WhitespaceMode#ACCELEO})
		 * @return this builder
		 */
		public Builder whitespaceMode(WhitespaceMode mode) {
			this.whitespaceMode = Objects.requireNonNull(mode, "mode must not be null");
			return this;
		}

		/**
		 * Sets the maximum number of diagnostic entries before truncation.
		 *
		 * @param max the maximum (must be positive, default: {@value M2tConfiguration#DEFAULT_MAX_DIAGNOSTICS})
		 * @return this builder
		 */
		public Builder maxDiagnostics(int max) {
			if (max <= 0) {
				throw new IllegalArgumentException("maxDiagnostics must be positive: " + max);
			}
			this.maxDiagnostics = max;
			return this;
		}

		/**
		 * Sets the maximum template invocation depth (T-1 protection).
		 *
		 * @param max the maximum (must be positive, default: {@value M2tConfiguration#DEFAULT_MAX_TEMPLATE_DEPTH})
		 * @return this builder
		 */
		public Builder maxTemplateDepth(int max) {
			if (max <= 0) {
				throw new IllegalArgumentException("maxTemplateDepth must be positive: " + max);
			}
			this.maxTemplateDepth = max;
			return this;
		}

		/**
		 * Sets the maximum number of for-block iterations (T-2 protection).
		 *
		 * @param max the maximum (must be positive, default: {@value M2tConfiguration#DEFAULT_MAX_FOR_ITERATIONS})
		 * @return this builder
		 */
		public Builder maxForIterations(int max) {
			if (max <= 0) {
				throw new IllegalArgumentException("maxForIterations must be positive: " + max);
			}
			this.maxForIterations = max;
			return this;
		}

		/**
		 * Sets the maximum cross-product size for set-argument invocations (T-3 protection).
		 *
		 * @param max the maximum (must be positive, default: {@value M2tConfiguration#DEFAULT_MAX_CROSS_PRODUCT_SIZE})
		 * @return this builder
		 */
		public Builder maxCrossProductSize(int max) {
			if (max <= 0) {
				throw new IllegalArgumentException("maxCrossProductSize must be positive: " + max);
			}
			this.maxCrossProductSize = max;
			return this;
		}

		/**
		 * Sets the maximum total output size in characters (T-7 protection).
		 * Use {@code 0} for unlimited output.
		 *
		 * @param max the maximum (must be non-negative, default: {@value M2tConfiguration#DEFAULT_MAX_OUTPUT_SIZE})
		 * @return this builder
		 */
		public Builder maxOutputSize(long max) {
			if (max < 0) {
				throw new IllegalArgumentException("maxOutputSize must be non-negative: " + max);
			}
			this.maxOutputSize = max;
			return this;
		}

		/**
		 * Enables or disables protected area support (T-6 mitigation).
		 * When disabled, {@code [protected]} blocks emit plain body content
		 * without markers, and no merge with existing content is performed.
		 *
		 * @param enabled {@code true} to enable (default), {@code false} to disable
		 * @return this builder
		 */
		public Builder protectedAreaEnabled(boolean enabled) {
			this.protectedAreaEnabled = enabled;
			return this;
		}

		public M2tConfiguration build() {
			return new M2tConfiguration(this);
		}
	}
}
