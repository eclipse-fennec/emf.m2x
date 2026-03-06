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
package org.eclipse.fennec.m2x.qvtd.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;

/**
 * Immutable configuration for creating {@link QvtdEngine} instances.
 *
 * <p>Bundles the mandatory {@link OclConfiguration} for the underlying OCL
 * evaluator, plus optional blackbox registry and unit resolvers. Use the
 * {@link Builder} to construct instances:
 * <pre>
 * QvtdConfiguration config = QvtdConfiguration.builder(oclConfig)
 *     .blackboxRegistry(myRegistry)
 *     .addUnitResolver(myResolver)
 *     .build();
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class QvtdConfiguration {

	private final OclConfiguration oclConfiguration;
	private final QvtdBlackboxRegistry blackboxRegistry;
	private final List<QvtdUnitResolver> unitResolvers;
	private final boolean blackboxEnabled;
	private final Set<String> allowedBlackboxModules;
	private final boolean unitResolverEnabled;
	private final Set<String> allowedUnitModules;
	private final int maxBlackboxLibraries;
	private final int maxUnitResolvers;

	private QvtdConfiguration(Builder builder) {
		this.oclConfiguration = builder.oclConfiguration;
		this.blackboxRegistry = builder.blackboxRegistry;
		this.unitResolvers = Collections.unmodifiableList(new ArrayList<>(builder.unitResolvers));
		this.blackboxEnabled = builder.blackboxEnabled;
		this.allowedBlackboxModules = Set.copyOf(builder.allowedBlackboxModules);
		this.unitResolverEnabled = builder.unitResolverEnabled;
		this.allowedUnitModules = Set.copyOf(builder.allowedUnitModules);
		this.maxBlackboxLibraries = builder.maxBlackboxLibraries;
		this.maxUnitResolvers = builder.maxUnitResolvers;
	}

	public OclConfiguration oclConfiguration() {
		return oclConfiguration;
	}

	/**
	 * Returns the blackbox registry, or {@code null} if none configured.
	 */
	public QvtdBlackboxRegistry blackboxRegistry() {
		return blackboxRegistry;
	}

	public List<QvtdUnitResolver> unitResolvers() {
		return unitResolvers;
	}

	/**
	 * Returns whether blackbox library imports are enabled (D29).
	 * Defaults to {@code false}.
	 */
	public boolean blackboxEnabled() {
		return blackboxEnabled;
	}

	/**
	 * Returns the allow-list of blackbox module qualified names.
	 * Empty means all are allowed (when {@link #blackboxEnabled()} is {@code true}).
	 */
	public Set<String> allowedBlackboxModules() {
		return allowedBlackboxModules;
	}

	/**
	 * Returns whether unit resolver imports are enabled (D29).
	 * Defaults to {@code false}.
	 */
	public boolean unitResolverEnabled() {
		return unitResolverEnabled;
	}

	/**
	 * Returns the allow-list of unit module qualified names.
	 * Empty means all are allowed (when {@link #unitResolverEnabled()} is {@code true}).
	 */
	public Set<String> allowedUnitModules() {
		return allowedUnitModules;
	}

	/**
	 * Returns the maximum number of blackbox libraries (D29).
	 */
	public int maxBlackboxLibraries() {
		return maxBlackboxLibraries;
	}

	/**
	 * Returns the maximum number of unit resolvers (D29).
	 */
	public int maxUnitResolvers() {
		return maxUnitResolvers;
	}

	public static Builder builder(OclConfiguration oclConfiguration) {
		return new Builder(oclConfiguration);
	}

	public static final class Builder {

		private final OclConfiguration oclConfiguration;
		private QvtdBlackboxRegistry blackboxRegistry;
		private final List<QvtdUnitResolver> unitResolvers = new ArrayList<>();
		private boolean blackboxEnabled;
		private Set<String> allowedBlackboxModules = Set.of();
		private boolean unitResolverEnabled;
		private Set<String> allowedUnitModules = Set.of();
		private int maxBlackboxLibraries = 10;
		private int maxUnitResolvers = 5;

		private Builder(OclConfiguration oclConfiguration) {
			this.oclConfiguration = Objects.requireNonNull(oclConfiguration, "oclConfiguration must not be null");
		}

		/**
		 * Sets the blackbox registry for resolving blackbox libraries.
		 *
		 * @param registry the blackbox registry
		 * @return this builder
		 */
		public Builder blackboxRegistry(QvtdBlackboxRegistry registry) {
			this.blackboxRegistry = Objects.requireNonNull(registry, "registry must not be null");
			return this;
		}

		public Builder addUnitResolver(QvtdUnitResolver resolver) {
			this.unitResolvers.add(Objects.requireNonNull(resolver, "resolver must not be null"));
			return this;
		}

		public Builder unitResolvers(List<QvtdUnitResolver> resolvers) {
			this.unitResolvers.clear();
			this.unitResolvers.addAll(Objects.requireNonNull(resolvers, "resolvers must not be null"));
			return this;
		}

		/**
		 * Enables or disables blackbox library imports (D29).
		 * Defaults to {@code false}.
		 */
		public Builder blackboxEnabled(boolean enabled) {
			this.blackboxEnabled = enabled;
			return this;
		}

		/**
		 * Sets the allow-list of blackbox module qualified names.
		 * Empty means all are allowed when blackbox is enabled.
		 */
		public Builder allowedBlackboxModules(Set<String> modules) {
			this.allowedBlackboxModules = Objects.requireNonNull(modules, "modules must not be null");
			return this;
		}

		/**
		 * Enables or disables unit resolver imports (D29).
		 * Defaults to {@code false}.
		 */
		public Builder unitResolverEnabled(boolean enabled) {
			this.unitResolverEnabled = enabled;
			return this;
		}

		/**
		 * Sets the allow-list of unit module qualified names.
		 * Empty means all are allowed when unit resolver is enabled.
		 */
		public Builder allowedUnitModules(Set<String> modules) {
			this.allowedUnitModules = Objects.requireNonNull(modules, "modules must not be null");
			return this;
		}

		/**
		 * Sets the maximum number of blackbox libraries (D29).
		 * Defaults to 10.
		 */
		public Builder maxBlackboxLibraries(int max) {
			if (max <= 0) {
				throw new IllegalArgumentException("maxBlackboxLibraries must be positive: " + max);
			}
			this.maxBlackboxLibraries = max;
			return this;
		}

		/**
		 * Sets the maximum number of unit resolvers (D29).
		 * Defaults to 5.
		 */
		public Builder maxUnitResolvers(int max) {
			if (max <= 0) {
				throw new IllegalArgumentException("maxUnitResolvers must be positive: " + max);
			}
			this.maxUnitResolvers = max;
			return this;
		}

		public QvtdConfiguration build() {
			return new QvtdConfiguration(this);
		}
	}
}
