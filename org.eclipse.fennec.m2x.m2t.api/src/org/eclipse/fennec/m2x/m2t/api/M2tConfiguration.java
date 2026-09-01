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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Objects;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;

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
	private final OclEngine oclEngine;
	private final EPackage.Registry packageRegistry;
	private final ResourceSet resourceSet;
	private final M2tGenerationStrategy generationStrategy;
	private final Charset defaultCharset;
	private final WhitespaceMode whitespaceMode;
	private final int maxDiagnostics;
	private final int maxTemplateDepth;
	private final int maxForIterations;
	private final int maxCrossProductSize;
	private final long maxOutputSize;
	private final boolean protectedAreaEnabled;
	private final List<M2tUnitResolver> unitResolvers;
	private final boolean unitResolverEnabled;
	private final boolean discoverUnitResolvers;
	private final Set<String> allowedUnitModules;
	private final int maxUnitResolvers;

	private final UnresolvedReferenceMode unresolvedReferenceMode;

	private M2tConfiguration(Builder builder) {
		this.oclConfiguration = builder.oclConfiguration;
		this.oclEngine = builder.oclEngine;
		this.packageRegistry = withContributions(builder);
		this.resourceSet = builder.resourceSet;
		this.generationStrategy = builder.generationStrategy;
		this.defaultCharset = builder.defaultCharset;
		this.whitespaceMode = builder.whitespaceMode;
		this.unresolvedReferenceMode = builder.unresolvedReferenceMode;
		this.maxDiagnostics = builder.maxDiagnostics;
		this.maxTemplateDepth = builder.maxTemplateDepth;
		this.maxForIterations = builder.maxForIterations;
		this.maxCrossProductSize = builder.maxCrossProductSize;
		this.maxOutputSize = builder.maxOutputSize;
		this.protectedAreaEnabled = builder.protectedAreaEnabled;
		this.unitResolvers = List.copyOf(builder.unitResolvers);
		this.unitResolverEnabled = builder.unitResolverEnabled;
		this.discoverUnitResolvers = builder.discoverUnitResolvers;
		this.allowedUnitModules = Set.copyOf(builder.allowedUnitModules);
		this.maxUnitResolvers = builder.maxUnitResolvers;
	}

	/**
	 * Returns the OCL engine this configuration was given, or {@code null} if the engine
	 * is to be derived from {@link #oclConfiguration()}.
	 *
	 * <p>Supplying an engine is how M2T uses the one that was configured elsewhere — the
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
	 * Returns the registry used to resolve metamodel type names appearing in
	 * template sources, never {@code null}.
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
	 * Returns how the generation reacts to a reference the linker cannot resolve.
	 * Defaults to {@link UnresolvedReferenceMode#FAIL}.
	 *
	 * @see UnresolvedReferenceMode
	 */
	public UnresolvedReferenceMode unresolvedReferenceMode() {
		return unresolvedReferenceMode;
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

	/**
	 * Creates a builder that uses the given OCL engine.
	 *
	 * @param oclEngine the engine to evaluate template expressions with, must not be
	 *        {@code null}
	 * @return a new builder
	 */
	/**
	 * The resolvers that can produce a module this generation imports or extends by name.
	 *
	 * @return the resolvers, never {@code null}
	 */
	public List<M2tUnitResolver> unitResolvers() {
		return unitResolvers;
	}

	/**
	 * Whether modules may be reached beyond the ones handed to {@code link}.
	 *
	 * <p>Off by default: a resolver produces template source from a name a template chose,
	 * which is a way out of the set of modules the caller decided to generate from.
	 *
	 * @return {@code true} if resolvers are consulted
	 */
	public boolean unitResolverEnabled() {
		return unitResolverEnabled;
	}

	/**
	 * Whether the engine also asks {@link M2tUnitResolver} implementations it finds itself.
	 *
	 * <p>Off by default, and a decision of its own: with an empty
	 * {@link #allowedUnitModules()} every name is permitted, so discovery there would let
	 * anything on the class path answer an import.
	 *
	 * @return {@code true} if implementations are discovered
	 */
	public boolean discoverUnitResolvers() {
		return discoverUnitResolvers;
	}

	/**
	 * The module names that may be resolved; empty puts no restriction on the names.
	 *
	 * @return the allowed names, never {@code null}
	 */
	public Set<String> allowedUnitModules() {
		return allowedUnitModules;
	}

	/**
	 * How many resolvers one generation may consult.
	 *
	 * @return the limit
	 */
	public int maxUnitResolvers() {
		return maxUnitResolvers;
	}

	/**
	 * Creates a builder that needs no OCL knowledge.
	 *
	 * <p>The engine built from this configuration evaluates template expressions with a default
	 * OCL engine that the factory creates. Use {@link #builder(OclEngine)} to run on an
	 * engine that already exists — the injected service under OSGi — or
	 * {@link #builder(OclConfiguration)} to configure the OCL side as well.
	 *
	 * @return a new builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	public static Builder builder(OclEngine oclEngine) {
		return new Builder(Objects.requireNonNull(oclEngine, "oclEngine must not be null"));
	}

	public static Builder builder(OclConfiguration oclConfiguration) {
		return new Builder(oclConfiguration);
	}


	/**
	 * The registry the builder's contributions make effective: a local registry holding the
	 * contributed packages, falling back to what would have applied without them — the explicit
	 * registry, the resource set's, the global one (#212).
	 */
	private static EPackage.Registry withContributions(Builder builder) {
		if (builder.contributedPackages.isEmpty()) {
			return builder.packageRegistry;
		}
		EPackage.Registry base = builder.packageRegistry != null ? builder.packageRegistry
				: builder.resourceSet != null ? builder.resourceSet.getPackageRegistry()
						: EPackage.Registry.INSTANCE;
		EPackage.Registry local = new EPackageRegistryImpl(base);
		for (EPackage contributed : builder.contributedPackages) {
			local.put(contributed.getNsURI(), contributed);
		}
		return local;
	}

	public static final class Builder {

		private final OclConfiguration oclConfiguration;
		private final OclEngine oclEngine;
		private EPackage.Registry packageRegistry;
		private final List<EPackage> contributedPackages = new ArrayList<>();
		private ResourceSet resourceSet;
		private M2tGenerationStrategy generationStrategy;
		private Charset defaultCharset = StandardCharsets.UTF_8;
		private WhitespaceMode whitespaceMode = WhitespaceMode.ACCELEO;
		private UnresolvedReferenceMode unresolvedReferenceMode = UnresolvedReferenceMode.FAIL;
		private int maxDiagnostics = DEFAULT_MAX_DIAGNOSTICS;
		private int maxTemplateDepth = DEFAULT_MAX_TEMPLATE_DEPTH;
		private int maxForIterations = DEFAULT_MAX_FOR_ITERATIONS;
		private int maxCrossProductSize = DEFAULT_MAX_CROSS_PRODUCT_SIZE;
		private long maxOutputSize = DEFAULT_MAX_OUTPUT_SIZE;
		private boolean protectedAreaEnabled = true;
		private final List<M2tUnitResolver> unitResolvers = new ArrayList<>();
		private boolean unitResolverEnabled;
		private boolean discoverUnitResolvers;
		private Set<String> allowedUnitModules = Set.of();
		private int maxUnitResolvers = 5;

		private Builder(OclConfiguration oclConfiguration) {
			this.oclConfiguration = Objects.requireNonNull(oclConfiguration, "oclConfiguration must not be null");
			this.oclEngine = null;
		}

		private Builder() {
			this.oclConfiguration = null;
			this.oclEngine = null;
		}

		private Builder(OclEngine oclEngine) {
			this.oclConfiguration = null;
			this.oclEngine = oclEngine;
		}

		/**
		 * Sets the registry used to resolve metamodel type names in template sources.
		 *
		 * <p>Template type references such as the {@code Book} in
		 * {@code [template public main(b : Book)]}, in {@code oclIsKindOf(Book)} or in an
		 * {@code overrides} declaration are resolved against this registry at parse time.
		 *
		 * <p>Defaults to {@link EPackage.Registry#INSTANCE}, which is the correct answer in
		 * plain Java, where no model version ambiguity exists. Supply your own registry under
		 * OSGi, or wherever two versions of one nsURI can coexist: the engine then resolves
		 * exactly the packages you resolved and verified yourself (D42).
		 *
		 * @param registry the package registry, must not be {@code null}
		 * @return this builder
		 */
		/**
		 * Sets the resource set whose package registry resolves metamodel type names appearing in template sources.
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
		 * Contributes a metamodel: it answers for its nsURI ahead of whatever registry applies —
		 * local to this configuration, nothing leaks into the global registry (#212).
		 * {@link #packageRegistry(EPackage.Registry)} and the resource set stay the injection
		 * points for a managed registry.
		 *
		 * @param ePackage the metamodel, must carry an nsURI
		 * @return this builder
		 */
		public Builder registerPackage(EPackage ePackage) {
			Objects.requireNonNull(ePackage, "ePackage must not be null");
			Objects.requireNonNull(ePackage.getNsURI(), "the package carries no nsURI");
			this.contributedPackages.add(ePackage);
			return this;
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
		 * Sets how the generation reacts to a reference the linker cannot resolve.
		 *
		 * @param mode the mode (default: {@link UnresolvedReferenceMode#FAIL})
		 * @return this builder
		 */
		public Builder unresolvedReferenceMode(UnresolvedReferenceMode mode) {
			this.unresolvedReferenceMode = Objects.requireNonNull(mode, "mode must not be null");
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
		/**
		 * Adds a resolver that can produce a module by the name a template names it with.
		 *
		 * <p>Needs {@link #unitResolverEnabled(boolean)}; without it the resolvers are not
		 * consulted, which is the default.
		 *
		 * @param resolver the resolver, must not be {@code null}
		 * @return this builder
		 */
		public Builder addUnitResolver(M2tUnitResolver resolver) {
			this.unitResolvers.add(Objects.requireNonNull(resolver, "resolver must not be null"));
			return this;
		}

		/**
		 * Lets modules be reached beyond the ones handed to {@code link}.
		 *
		 * @param enabled whether resolvers are consulted
		 * @return this builder
		 */
		public Builder unitResolverEnabled(boolean enabled) {
			this.unitResolverEnabled = enabled;
			return this;
		}

		/**
		 * Lets the engine ask {@link M2tUnitResolver} implementations it discovers, on top
		 * of the ones named here — {@link java.util.ServiceLoader} in plain Java, the
		 * service registry under OSGi.
		 *
		 * @param discover whether to discover implementations
		 * @return this builder
		 */
		public Builder discoverUnitResolvers(boolean discover) {
			this.discoverUnitResolvers = discover;
			return this;
		}

		/**
		 * Narrows which module names may be resolved. Empty puts no restriction on them —
		 * the enable flag is the gate, this is what narrows once it is open.
		 *
		 * @param modules the allowed names, must not be {@code null}
		 * @return this builder
		 */
		public Builder allowedUnitModules(Set<String> modules) {
			this.allowedUnitModules = Objects.requireNonNull(modules, "modules must not be null");
			return this;
		}

		/**
		 * Bounds how many resolvers one generation may consult.
		 *
		 * @param max the limit
		 * @return this builder
		 */
		public Builder maxUnitResolvers(int max) {
			this.maxUnitResolvers = max;
			return this;
		}

		public Builder protectedAreaEnabled(boolean enabled) {
			this.protectedAreaEnabled = enabled;
			return this;
		}

		public M2tConfiguration build() {
			return new M2tConfiguration(this);
		}
	}
}
