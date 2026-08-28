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
package org.eclipse.fennec.m2x.qvto.engine.internal;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.model.trace.Trace;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.RelationImplementationProvider;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxRegistry;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoEvaluationOptions;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;
import org.eclipse.fennec.m2x.qvto.parser.QvtoParserSupport;
import org.eclipse.fennec.m2x.ocl.api.SourcePosition;

/**
 * Plain Java implementation of the {@link QvtoEngine} facade.
 *
 * <p>This class has no OSGi dependencies and can be instantiated directly:
 * <pre>
 * OclConfiguration oclConfig = OclConfiguration.builder(parser).build();
 * QvtoConfiguration config = QvtoConfiguration.builder(oclConfig).build();
 * QvtoEngine engine = new QvtoEngineImpl(config);
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtoEngineImpl implements QvtoEngine, RelationImplementationProvider {

	/** Language tag of this engine's units, as used in store keys and manifests. */
	private static final String LANGUAGE = "qvto";

	private final QvtoParserSupport parserSupport;
	private final EPackage.Registry packageRegistry;
	/**
	 * Resolves {@code parse(URI)} through its {@link ResourceSet#getURIConverter()
	 * URIConverter}: the configured resource set, or a default one when none was given,
	 * so that every URI takes the same road (D42). Its package registry is deliberately
	 * not consulted for type resolution — that follows the configuration.
	 */
	private final ResourceSet resourceSet;
	private final OclEngine oclEngine;
	private final QvtoBlackboxRegistry blackboxRegistry;
	private final List<QvtoUnitResolver> unitResolvers;
	private final Executor parallelExecutor;
	private final boolean blackboxEnabled;
	private final Set<String> allowedBlackboxModules;
	private final boolean unitResolverEnabled;
	private final Set<String> allowedUnitModules;
	private final int maxUnitResolvers;
	private final boolean discoverUnitResolvers;
	private final int maxBlackboxLibraries;

	/** Loaded transformation for RelationImplementationProvider (D39, Phase 4b). */
	private volatile OperationalTransformation loadedTransformation;

	/**
	 * Creates a new engine from the given configuration.
	 *
	 * @param config the engine configuration
	 */
	public QvtoEngineImpl(QvtoConfiguration config) {
		Objects.requireNonNull(config, "config must not be null");
		this.parserSupport = new QvtoParserSupport();
		this.packageRegistry = config.packageRegistry();
		this.resourceSet = config.resourceSet() != null ? config.resourceSet() : new ResourceSetImpl();
		// The engine evaluates with the OCL engine it was given — its cache, its
		// providers; only when none was supplied is one built from the configuration.
		this.oclEngine = resolveOclEngine(config);
		this.blackboxRegistry = config.blackboxRegistry();
		this.unitResolvers = List.copyOf(config.unitResolvers());
		this.parallelExecutor = config.parallelExecutor();
		this.blackboxEnabled = config.blackboxEnabled();
		this.allowedBlackboxModules = config.allowedBlackboxModules();
		this.unitResolverEnabled = config.unitResolverEnabled();
		this.allowedUnitModules = config.allowedUnitModules();
		this.maxUnitResolvers = config.maxUnitResolvers();
		this.discoverUnitResolvers = config.discoverUnitResolvers();
		this.maxBlackboxLibraries = config.maxBlackboxLibraries();
	}

	// --- Parsing ---

	@Override
	public OperationalTransformation parse(URI transformationUri) throws QvtoParseException {
		Objects.requireNonNull(transformationUri, "transformationUri must not be null");
		try (InputStream in = resourceSet.getURIConverter().createInputStream(transformationUri)) {
			String source = new String(in.readAllBytes(), StandardCharsets.UTF_8);
			return parse(source, transformationUri.toString());
		} catch (IOException e) {
			throw new QvtoParseException("Failed to read transformation: " + transformationUri, e);
		}
	}

	@Override
	public OperationalTransformation parse(String source, String unitName) throws QvtoParseException {
		Objects.requireNonNull(source, "source must not be null");
		Objects.requireNonNull(unitName, "unitName must not be null");
		return parserSupport.parse(source, unitName, packageRegistry);
	}

	@Override
	public CompiledUnit compile(String source, String unitName) throws QvtoParseException {
		OperationalTransformation transformation = parse(source, unitName);
		try {
			return UnitPackager.compile(LANGUAGE, unitName, transformation);
		} catch (IllegalStateException | IllegalArgumentException e) {
			// A unit that is not self-contained is not storable; better said here, with the unit
			// name, than on the first save() somewhere else with a dangling reference (#137).
			throw new QvtoParseException("Cannot compile '" + unitName + "': " + e.getMessage(), e);
		}
	}

	@Override
	public CompiledUnit compile(URI transformationUri) throws QvtoParseException {
		Objects.requireNonNull(transformationUri, "transformationUri must not be null");
		try (InputStream in = resourceSet.getURIConverter().createInputStream(transformationUri)) {
			String source = new String(in.readAllBytes(), StandardCharsets.UTF_8);
			return compile(source, transformationUri.toString());
		} catch (IOException e) {
			throw new QvtoParseException("Failed to read transformation: " + transformationUri, e);
		}
	}

	/**
	 * Where an expression node stood in the unit it was parsed from, for a runtime diagnostic that
	 * knows the node and not the place (#116). Covers imported units too — they are parsed through
	 * the same support.
	 *
	 * @param node the node, may be {@code null}
	 * @return the position, or {@code null} when it is not known
	 */
	SourcePosition positionOf(EObject node) {
		return parserSupport.positionOf(node);
	}

	// --- Execution ---

	@Override
	public QvtoExecutionResult execute(OperationalTransformation transformation,
			QvtoExecutionContext context) {
		return execute(transformation, context, QvtoEvaluationOptions.defaults());
	}

	@Override
	public QvtoExecutionResult execute(OperationalTransformation transformation,
			QvtoExecutionContext context, QvtoEvaluationOptions options) {
		Objects.requireNonNull(transformation, "transformation must not be null");
		Objects.requireNonNull(context, "context must not be null");
		Objects.requireNonNull(options, "options must not be null");

		// §8.1.13: Link phase — resolve stub imports via unit resolvers and blackbox registry
		// D29: Enforce enable flags — only pass resolvers/registry when enabled
		// D29: the enable flag decides whether resolvers are reachable at all, the limit
		// how far one import may travel — every resolver is code asked to produce a unit
		// for a name the transformation chose. Applied here rather than at construction,
		// so it still holds once the set of resolvers can change between executions.
		List<QvtoUnitResolver> effectiveResolvers = unitResolverEnabled
				? Stream.concat(unitResolvers.stream(), discoveredResolvers())
						.limit(maxUnitResolvers)
						.toList()
				: List.of();
		QvtoBlackboxRegistry effectiveRegistry = blackboxEnabled ? blackboxRegistry : null;
		Set<String> effectiveBbAllow = blackboxEnabled ? allowedBlackboxModules : Set.of();
		Set<String> effectiveUrAllow = unitResolverEnabled ? allowedUnitModules : Set.of();

		// Always run the linker — it validates that all imports can be resolved.
		// When enable flags are false, no resolvers/registry are available,
		// so unresolved imports will correctly fail with "Cannot resolve import".
		try {
			QvtoLinker linker = new QvtoLinker(parserSupport, effectiveResolvers,
					packageRegistry, effectiveRegistry,
					effectiveBbAllow, effectiveUrAllow, maxBlackboxLibraries);
			linker.link(transformation);
		} catch (QvtoParseException e) {
			return new QvtoExecutionResult(
					List.of(new org.eclipse.emf.common.util.BasicDiagnostic(
							Diagnostic.ERROR, "org.eclipse.fennec.m2x.qvto.engine",
							0, "Link error: " + e.getMessage(), null)),
					null);
		}

		QvtoEvalEnvironment env = QvtoEvalEnvironment.forTransformation(context.configProperties());
		QvtoExtentManager extentManager = new QvtoExtentManager(transformation, context);

		QvtoEvaluator evaluator = new QvtoEvaluator(
				oclEngine, env, options, transformation, extentManager, this, packageRegistry);
		evaluator.setConfigProperties(context.configProperties());

		// D29: Pass QVT-O operations as additionalProviders — always active, no mutable registration
		QvtoOperationProvider qvtoProvider = new QvtoOperationProvider(
				transformation, evaluator, evaluator.getOperationResolver(), effectiveRegistry);
		evaluator.setAdditionalProviders(List.of(qvtoProvider));

		List<Diagnostic> diagnostics = evaluator.execute();
		Trace trace = options.tracingEnabled() ? evaluator.getTrace() : null;
		return new QvtoExecutionResult(diagnostics, trace);
	}

	/**
	 * Returns the underlying OCL engine (for testing and advanced use).
	 */
	@Override
	public OclEngine getOclEngine() {
		return oclEngine;
	}

	/**
	 * Returns the executor used for {@code parallelTransform()} (§8.3.6.2).
	 */
	public Executor getParallelExecutor() {
		return parallelExecutor;
	}

	/**
	 * Returns the blackbox registry, or {@code null} if none configured.
	 */
	public QvtoBlackboxRegistry getBlackboxRegistry() {
		return blackboxRegistry;
	}

	// --- RelationImplementationProvider (D39, Phase 4b) ---

	/**
	 * Loads a parsed QVT-O transformation so this engine can serve as a
	 * {@link RelationImplementationProvider} for QVT-R hybrid execution.
	 *
	 * @param transformation the parsed operational transformation
	 */
	@Override
	public void loadTransformation(OperationalTransformation transformation) {
		this.loadedTransformation = Objects.requireNonNull(transformation);
	}

	@Override
	public boolean canProvide(String relationQualifiedName) {
		OperationalTransformation ot = loadedTransformation;
		if (ot == null) {
			return false;
		}
		return findMappingByName(ot, relationQualifiedName) != null;
	}

	@Override
	public QvtdExecutionResult executeRelation(String relationQualifiedName,
			QvtdExecutionContext qvtdContext) {
		OperationalTransformation ot = loadedTransformation;
		if (ot == null) {
			return new QvtdExecutionResult(
					List.of(new org.eclipse.emf.common.util.BasicDiagnostic(
							Diagnostic.ERROR, "org.eclipse.fennec.m2x.qvto.engine", 0,
							"No QVT-O transformation loaded for relation: " + relationQualifiedName,
							null)),
					false);
		}

		// Bridge QvtdExecutionContext → QvtoExecutionContext
		// Map named extents to positional extents matching OT model parameters
		List<QvtoModelExtent> extents = new ArrayList<>();
		for (var modelParam : ot.getModelParameter()) {
			String paramName = modelParam.getName();
			QvtdModelExtent qvtdExtent = qvtdContext.modelExtents().get(paramName);
			if (qvtdExtent != null) {
				extents.add(bridgeExtent(qvtdExtent));
			} else {
				// Use empty extent for unmatched parameters
				extents.add(new BridgedModelExtent(new ArrayList<>()));
			}
		}

		QvtoExecutionContext qvtoCtx = QvtoExecutionContext.of(extents, Map.of());
		QvtoExecutionResult qvtoResult = execute(ot, qvtoCtx);

		// Bridge result back
		List<Diagnostic> diagnostics = qvtoResult.diagnostics();
		return new QvtdExecutionResult(diagnostics, !qvtdContext.checkOnly());
	}

	private static MappingOperation findMappingByName(OperationalTransformation ot, String name) {
		for (var classifier : ot.getEClassifiers()) {
			if (classifier instanceof EClass ec) {
				for (EOperation op : ec.getEOperations()) {
					if (op instanceof MappingOperation mo && name.equals(mo.getName())) {
						return mo;
					}
				}
			}
		}
		return null;
	}

	private static QvtoModelExtent bridgeExtent(QvtdModelExtent qvtdExtent) {
		return new BridgedModelExtent(qvtdExtent.getContents());
	}

	/**
	 * Bridges a QVT-R model extent to a QVT-O model extent. Both share the
	 * same underlying object list so mutations are visible across engines.
	 */
	private static final class BridgedModelExtent implements QvtoModelExtent {
		private List<EObject> contents;

		BridgedModelExtent(List<EObject> contents) {
			this.contents = new ArrayList<>(contents);
		}

		@Override
		public List<EObject> getContents() {
			return contents;
		}

		@Override
		public void setContents(List<? extends EObject> newContents) {
			contents = new ArrayList<>(newContents);
		}

		@Override
		public void add(EObject object) {
			contents.add(object);
		}
	}

	/**
	 * The discovery resolver, if this engine was told to discover — asked after the
	 * configured ones, and counting as one resolver towards the limit.
	 */
	private Stream<QvtoUnitResolver> discoveredResolvers() {
		return discoverUnitResolvers ? Stream.of(new ServiceLoaderUnitResolver()) : Stream.of();
	}

	/**
	 * Returns the OCL engine this engine evaluates with — the one it was given, one built
	 * from the OCL configuration it was given, or a default one.
	 *
	 * <p>The third case is what lets a caller configure Qvto without knowing anything about
	 * OCL ({@link QvtoConfiguration#builder()}). It is the single point where this engine falls back,
	 * so there is one place to look when the question is which OCL engine ran (D42).
	 */
	private static OclEngine resolveOclEngine(QvtoConfiguration config) {
		if (config.oclEngine() != null) {
			return config.oclEngine();
		}
		if (config.oclConfiguration() != null) {
			return OclEngines.create(config.oclConfiguration());
		}
		return OclEngines.create(new OclParserSupport());
	}
}
