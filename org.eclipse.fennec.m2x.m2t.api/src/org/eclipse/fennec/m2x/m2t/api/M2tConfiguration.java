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

	private final OclConfiguration oclConfiguration;
	private final M2tGenerationStrategy generationStrategy;
	private final Charset defaultCharset;
	private final WhitespaceMode whitespaceMode;

	private M2tConfiguration(Builder builder) {
		this.oclConfiguration = builder.oclConfiguration;
		this.generationStrategy = builder.generationStrategy;
		this.defaultCharset = builder.defaultCharset;
		this.whitespaceMode = builder.whitespaceMode;
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

	public static Builder builder(OclConfiguration oclConfiguration) {
		return new Builder(oclConfiguration);
	}

	public static final class Builder {

		private final OclConfiguration oclConfiguration;
		private M2tGenerationStrategy generationStrategy;
		private Charset defaultCharset = StandardCharsets.UTF_8;
		private WhitespaceMode whitespaceMode = WhitespaceMode.ACCELEO;

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

		public M2tConfiguration build() {
			return new M2tConfiguration(this);
		}
	}
}
