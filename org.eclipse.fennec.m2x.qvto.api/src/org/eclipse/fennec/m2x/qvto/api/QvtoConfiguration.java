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
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;

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
	private final OclEngine oclEngine;
	private final EPackage.Registry packageRegistry;
	private final ResourceSet resourceSet;
	private final QvtoBlackboxRegistry blackboxRegistry;
	private final List<QvtoUnitResolver> unitResolvers;
	private final Executor parallelExecutor;
	private final boolean blackboxEnabled;
	private final Set<String> allowedBlackboxModules;
	private final boolean unitResolverEnabled;
	private final Set<String> allowedUnitModules;
	private final int maxBlackboxLibraries;
	private final int maxUnitResolvers;

	private QvtoConfiguration(Builder builder) {
		this.oclConfiguration = builder.oclConfiguration;
		this.oclEngine = builder.oclEngine;
		this.packageRegistry = builder.packageRegistry;
		this.resourceSet = builder.resourceSet;
		this.blackboxRegistry = builder.blackboxRegistry;
		this.unitResolvers = Collections.unmodifiableList(new ArrayList<>(builder.unitResolvers));
		this.parallelExecutor = builder.parallelExecutor;
		this.blackboxEnabled = builder.blackboxEnabled;
		this.allowedBlackboxModules = Set.copyOf(builder.allowedBlackboxModules);
		this.unitResolverEnabled = builder.unitResolverEnabled;
		this.allowedUnitModules = Set.copyOf(builder.allowedUnitModules);
		this.maxBlackboxLibraries = builder.maxBlackboxLibraries;
		this.maxUnitResolvers = builder.maxUnitResolvers;
	}

	/**
	 * Returns the OCL engine this configuration was given, or {@code null} if the engine
	 * is to be derived from {@link #oclConfiguration()}.
	 *
	 * <p>Supplying an engine is how QVT-O uses the one that was configured elsewhere — the
	 * injected service under OSGi, or one built with {@code OclEngines.create(...)} in
	 * plain Java — with its cache and its operation providers.
	 *
	 * @return the OCL engine, or {@code null}
	 */
	public OclEngine oclEngine() {
		return oclEngine;
	}

	public OclConfiguration oclConfiguration() {
		return oclConfiguration;
	}

	/**
	 * Returns the registry used for metamodel resolution, never {@code null}.
	 *
	 * <p>This is the single place where the fallback to the global registry is
	 * applied: unless a caller supplied one, this is {@link EPackage.Registry#INSTANCE}.
	 * Nothing below the engine reads the static registry on its own (D42).
	 *
	 * @return the package registry
	 */
	public EPackage.Registry packageRegistry() {
		if (packageRegistry != null) {
			return packageRegistry;
		}
		if (resourceSet != null) {
			return resourceSet.getPackageRegistry();
		}
		return EPackage.Registry.INSTANCE;
	}

	/**
	 * Returns the resource set this configuration was given, or {@code null}.
	 *
	 * <p>Only its {@linkplain ResourceSet#getPackageRegistry() package registry} is used
	 * for resolution today; nothing is loaded through it.
	 *
	 * @return the resource set, or {@code null}
	 */
	public ResourceSet resourceSet() {
		return resourceSet;
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

	/**
	 * Creates a builder that uses the given OCL engine.
	 *
	 * @param oclEngine the engine to evaluate OCL expressions with, must not be {@code null}
	 * @return a new builder
	 */
	public static Builder builder(OclEngine oclEngine) {
		return new Builder(Objects.requireNonNull(oclEngine, "oclEngine must not be null"));
	}

	public static Builder builder(OclConfiguration oclConfiguration) {
		return new Builder(oclConfiguration);
	}

	public static final class Builder {

		private final OclConfiguration oclConfiguration;
		private final OclEngine oclEngine;
		private EPackage.Registry packageRegistry;
		private ResourceSet resourceSet;
		private QvtoBlackboxRegistry blackboxRegistry;
		private final List<QvtoUnitResolver> unitResolvers = new ArrayList<>();
		private Executor parallelExecutor = Executors.newVirtualThreadPerTaskExecutor();
		private boolean blackboxEnabled;
		private Set<String> allowedBlackboxModules = Set.of();
		private boolean unitResolverEnabled;
		private Set<String> allowedUnitModules = Set.of();
		private int maxBlackboxLibraries = 10;
		private int maxUnitResolvers = 5;

		private Builder(OclConfiguration oclConfiguration) {
			this.oclConfiguration = Objects.requireNonNull(oclConfiguration, "oclConfiguration must not be null");
			this.oclEngine = null;
		}

		private Builder(OclEngine oclEngine) {
			this.oclConfiguration = null;
			this.oclEngine = oclEngine;
		}

		/**
		 * Sets the registry used to resolve {@code modeltype … uses '<nsURI>'}
		 * declarations and every metamodel type below them.
		 *
		 * <p>Defaults to {@link EPackage.Registry#INSTANCE}, which is the correct
		 * answer in plain Java, where no model version ambiguity exists. Supply your
		 * own registry under OSGi, or wherever two versions of one nsURI can coexist:
		 * the engine then resolves exactly the packages you resolved and verified
		 * yourself, and forms no opinion about which version an nsURI names (D42).
		 *
		 * @param registry the package registry, must not be {@code null}
		 * @return this builder
		 */
		/**
		 * Sets the resource set whose package registry resolves the transformation's modeltype declarations.
		 *
		 * <p>This is the form to reach for: a {@link ResourceSet} is what EMF hands
		 * around, and under OSGi it is what {@code emf.osgi} injects — a configured,
		 * isolated stack arrives as a resource set, not as a bare registry.
		 *
		 * <p>Only the resource set's package registry is used; nothing is loaded through
		 * it. If a registry is set as well, that registry wins — the more specific
		 * setting beats the more general one (D42).
		 *
		 * @param resourceSet the resource set, must not be {@code null}
		 * @return this builder
		 */
		public Builder resourceSet(ResourceSet resourceSet) {
			this.resourceSet = Objects.requireNonNull(resourceSet, "resourceSet must not be null");
			return this;
		}

		public Builder packageRegistry(EPackage.Registry registry) {
			this.packageRegistry = Objects.requireNonNull(registry, "registry must not be null");
			return this;
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

		public QvtoConfiguration build() {
			return new QvtoConfiguration(this);
		}
	}
}
