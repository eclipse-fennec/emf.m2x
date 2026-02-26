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
package org.eclipse.fennec.m2x.qvto.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;

/**
 * Immutable configuration for creating {@link QvtoEngine} instances.
 *
 * <p>Bundles the mandatory {@link OclConfiguration} for the underlying OCL
 * evaluator, plus optional blackbox registry and unit resolvers. Use the
 * {@link Builder} to construct instances:
 * <pre>
 * QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
 *     .blackboxRegistry(myRegistry)
 *     .addUnitResolver(myResolver)
 *     .build();
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class QvtoConfiguration {

	private final OclConfiguration oclConfiguration;
	private final QvtoBlackboxRegistry blackboxRegistry;
	private final List<QvtoUnitResolver> unitResolvers;
	private final Executor parallelExecutor;

	private QvtoConfiguration(Builder builder) {
		this.oclConfiguration = builder.oclConfiguration;
		this.blackboxRegistry = builder.blackboxRegistry;
		this.unitResolvers = Collections.unmodifiableList(new ArrayList<>(builder.unitResolvers));
		this.parallelExecutor = builder.parallelExecutor;
	}

	public OclConfiguration oclConfiguration() {
		return oclConfiguration;
	}

	/**
	 * Returns the blackbox registry, or {@code null} if none configured.
	 */
	public QvtoBlackboxRegistry blackboxRegistry() {
		return blackboxRegistry;
	}

	public List<QvtoUnitResolver> unitResolvers() {
		return unitResolvers;
	}

	/**
	 * Returns the {@link Executor} used for {@code parallelTransform()} (§8.3.6.2).
	 * Defaults to {@link Executors#newVirtualThreadPerTaskExecutor()}.
	 */
	public Executor parallelExecutor() {
		return parallelExecutor;
	}

	public static Builder builder(OclConfiguration oclConfiguration) {
		return new Builder(oclConfiguration);
	}

	public static final class Builder {

		private final OclConfiguration oclConfiguration;
		private QvtoBlackboxRegistry blackboxRegistry;
		private final List<QvtoUnitResolver> unitResolvers = new ArrayList<>();
		private Executor parallelExecutor = Executors.newVirtualThreadPerTaskExecutor();

		private Builder(OclConfiguration oclConfiguration) {
			this.oclConfiguration = Objects.requireNonNull(oclConfiguration, "oclConfiguration must not be null");
		}

		/**
		 * Sets the blackbox registry for resolving blackbox libraries.
		 *
		 * @param registry the blackbox registry
		 * @return this builder
		 */
		public Builder blackboxRegistry(QvtoBlackboxRegistry registry) {
			this.blackboxRegistry = Objects.requireNonNull(registry, "registry must not be null");
			return this;
		}

		public Builder addUnitResolver(QvtoUnitResolver resolver) {
			this.unitResolvers.add(Objects.requireNonNull(resolver, "resolver must not be null"));
			return this;
		}

		public Builder unitResolvers(List<QvtoUnitResolver> resolvers) {
			this.unitResolvers.clear();
			this.unitResolvers.addAll(Objects.requireNonNull(resolvers, "resolvers must not be null"));
			return this;
		}

		/**
		 * Sets the {@link Executor} for {@code parallelTransform()} (§8.3.6.2).
		 * Defaults to {@link Executors#newVirtualThreadPerTaskExecutor()}.
		 *
		 * @param executor the executor to use for parallel transformation execution
		 * @return this builder
		 */
		public Builder parallelExecutor(Executor executor) {
			this.parallelExecutor = Objects.requireNonNull(executor, "executor must not be null");
			return this;
		}

		public QvtoConfiguration build() {
			return new QvtoConfiguration(this);
		}
	}
}
