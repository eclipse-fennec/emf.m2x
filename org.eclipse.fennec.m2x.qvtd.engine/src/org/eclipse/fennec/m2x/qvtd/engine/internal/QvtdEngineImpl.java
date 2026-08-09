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
package org.eclipse.fennec.m2x.qvtd.engine.internal;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionException;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnitResolver;
import org.eclipse.fennec.m2x.qvtd.api.RelationImplementationProvider;
import org.eclipse.fennec.m2x.qvtd.parser.QvtrParserSupport;

/**
 * Plain Java implementation of the {@link QvtdEngine} facade.
 *
 * <p>This class has no OSGi dependencies and can be instantiated directly:
 * <pre>
 * OclConfiguration oclConfig = OclConfiguration.builder(parser).build();
 * QvtdConfiguration config = QvtdConfiguration.builder(oclConfig).build();
 * QvtdEngine engine = new QvtdEngineImpl(config);
 * </pre>
 *
 * <p>Thread-safety: the engine instance is safely shared across threads.
 * Each {@link #execute} call creates its own evaluator with no shared mutable state.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtdEngineImpl implements QvtdEngine {

	private final QvtrParserSupport parserSupport;
	/**
	 * Resolves {@code parse(URI)} through its {@link ResourceSet#getURIConverter()
	 * URIConverter}: the configured resource set, or a default one when none was given,
	 * so that every URI takes the same road (D42). Its package registry is deliberately
	 * not consulted for type resolution — that follows the configuration.
	 */
	private final ResourceSet resourceSet;
	private final OclEngine oclEngine;
	private final QvtdConfiguration config;
	private final List<RelationImplementationProvider> implementationProviders = new CopyOnWriteArrayList<>();

	/**
	 * Creates a new engine from the given configuration.
	 *
	 * @param config the engine configuration
	 */
	public QvtdEngineImpl(QvtdConfiguration config) {
		Objects.requireNonNull(config, "config must not be null");
		this.config = config;
		this.parserSupport = new QvtrParserSupport();
		this.resourceSet = config.resourceSet() != null ? config.resourceSet() : new ResourceSetImpl();
		// The engine evaluates with the OCL engine it was given — its cache, its
		// providers; only when none was supplied is one built from the configuration.
		this.oclEngine = resolveOclEngine(config);
	}

	// --- Parsing ---

	@Override
	public RelationalTransformation parse(URI transformationUri) throws QvtdParseException {
		Objects.requireNonNull(transformationUri, "transformationUri must not be null");
		try (InputStream in = resourceSet.getURIConverter().createInputStream(transformationUri)) {
			String source = new String(in.readAllBytes(), StandardCharsets.UTF_8);
			return parse(source, transformationUri.toString());
		} catch (IOException e) {
			throw new QvtdParseException("Failed to read transformation: " + transformationUri, e);
		}
	}

	/**
	 * The discovery resolver, if this engine was told to discover — asked after the
	 * configured ones, and counting as one resolver towards the limit.
	 */
	private Stream<QvtdUnitResolver> discoveredResolvers() {
		return config.discoverUnitResolvers()
				? Stream.of(new ServiceLoaderQvtdUnitResolver())
				: Stream.of();
	}

	@Override
	public RelationalTransformation parse(String source, String unitName) throws QvtdParseException {
		Objects.requireNonNull(source, "source must not be null");
		Objects.requireNonNull(unitName, "unitName must not be null");
		return parserSupport.parse(source, unitName, config.packageRegistry());
	}

	// --- Execution ---

	@Override
	public QvtdExecutionResult execute(RelationalTransformation transformation,
			QvtdExecutionContext context) {
		Objects.requireNonNull(transformation, "transformation must not be null");
		Objects.requireNonNull(context, "context must not be null");

		try {
			// §7.11.1: resolve what the transformation imports before anything runs. The
			// enable flag decides whether resolvers are reachable at all, the limit how far
			// one import may travel, and the allow-list which names may be asked for —
			// checked inside the linker, before a resolver is consulted.
			List<QvtdUnitResolver> effectiveResolvers = config.unitResolverEnabled()
					? Stream.concat(config.unitResolvers().stream(), discoveredResolvers())
							.limit(config.maxUnitResolvers())
							.toList()
					: List.of();
			new QvtdLinker(parserSupport, config.packageRegistry(), effectiveResolvers,
					config.unitResolverEnabled() ? config.allowedUnitModules() : Set.of())
					.link(transformation);

			QvtrEvalEnvironment env = new QvtrEvalEnvironment();
			QvtrExtentManager extentManager = new QvtrExtentManager(transformation, context);
			QvtrEvaluator evaluator = new QvtrEvaluator(
					oclEngine, env, transformation, extentManager, context,
					config, config.blackboxRegistry(), List.copyOf(implementationProviders));

			List<Diagnostic> diagnostics = evaluator.execute();
			return new QvtdExecutionResult(diagnostics, !context.checkOnly());
		} catch (QvtdExecutionException e) {
			throw e;
		} catch (Exception e) {
			return new QvtdExecutionResult(
					List.of(new BasicDiagnostic(
							Diagnostic.ERROR, "org.eclipse.fennec.m2x.qvtd.engine",
							0, "Execution error: " + e.getMessage(), null)),
					false);
		}
	}

	@Override
	public void registerImplementationProvider(RelationImplementationProvider provider) {
		Objects.requireNonNull(provider, "provider must not be null");
		implementationProviders.add(provider);
	}

	/**
	 * Returns the underlying OCL engine (for testing and advanced use).
	 */
	@Override
	public OclEngine getOclEngine() {
		return oclEngine;
	}

	/**
	 * Returns the OCL engine this engine evaluates with — the one it was given, one built
	 * from the OCL configuration it was given, or a default one.
	 *
	 * <p>The third case is what lets a caller configure Qvtd without knowing anything about
	 * OCL ({@link QvtdConfiguration#builder()}). It is the single point where this engine falls back,
	 * so there is one place to look when the question is which OCL engine ran (D42).
	 */
	private static OclEngine resolveOclEngine(QvtdConfiguration config) {
		if (config.oclEngine() != null) {
			return config.oclEngine();
		}
		if (config.oclConfiguration() != null) {
			return OclEngines.create(config.oclConfiguration());
		}
		return OclEngines.create(new OclParserSupport());
	}
}
