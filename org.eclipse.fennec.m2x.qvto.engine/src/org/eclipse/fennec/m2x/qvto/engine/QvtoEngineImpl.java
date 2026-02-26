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
package org.eclipse.fennec.m2x.qvto.engine;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.model.trace.Trace;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.engine.OclEngineImpl;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxRegistry;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoEvaluationOptions;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;
import org.eclipse.fennec.m2x.qvto.engine.internal.QvtoEvalEnvironment;
import org.eclipse.fennec.m2x.qvto.engine.internal.QvtoEvaluator;
import org.eclipse.fennec.m2x.qvto.engine.internal.QvtoExtentManager;
import org.eclipse.fennec.m2x.qvto.engine.internal.QvtoLinker;
import org.eclipse.fennec.m2x.qvto.engine.internal.QvtoOperationProvider;
import org.eclipse.fennec.m2x.qvto.parser.QvtoParserSupport;

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
public class QvtoEngineImpl implements QvtoEngine {

	private final QvtoParserSupport parserSupport;
	private final OclEngineImpl oclEngine;
	private final QvtoBlackboxRegistry blackboxRegistry;
	private final List<QvtoUnitResolver> unitResolvers = new CopyOnWriteArrayList<>();
	private final Executor parallelExecutor;

	/**
	 * Creates a new engine from the given configuration.
	 *
	 * @param config the engine configuration
	 */
	public QvtoEngineImpl(QvtoConfiguration config) {
		Objects.requireNonNull(config, "config must not be null");
		this.parserSupport = new QvtoParserSupport();
		OclConfiguration oclConfig = config.oclConfiguration();
		this.oclEngine = new OclEngineImpl(oclConfig);
		this.blackboxRegistry = config.blackboxRegistry();
		this.unitResolvers.addAll(config.unitResolvers());
		this.parallelExecutor = config.parallelExecutor();
	}

	// --- Parsing ---

	@Override
	public OperationalTransformation parse(URI transformationUri) throws QvtoParseException {
		Objects.requireNonNull(transformationUri, "transformationUri must not be null");
		try {
			String source = Files.readString(Path.of(transformationUri));
			return parse(source, transformationUri.toString());
		} catch (IOException e) {
			throw new QvtoParseException("Failed to read transformation: " + transformationUri, e);
		}
	}

	@Override
	public OperationalTransformation parse(String source, String unitName) throws QvtoParseException {
		Objects.requireNonNull(source, "source must not be null");
		Objects.requireNonNull(unitName, "unitName must not be null");
		return parserSupport.parse(source, unitName, EPackage.Registry.INSTANCE);
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
		boolean hasResolvers = !unitResolvers.isEmpty() || blackboxRegistry != null;
		if (hasResolvers) {
			try {
				QvtoLinker linker = new QvtoLinker(parserSupport, unitResolvers,
						EPackage.Registry.INSTANCE, blackboxRegistry);
				linker.link(transformation);
			} catch (QvtoParseException e) {
				return new QvtoExecutionResult(
						List.of(new org.eclipse.emf.common.util.BasicDiagnostic(
								Diagnostic.ERROR, "org.eclipse.fennec.m2x.qvto.engine",
								0, "Link error: " + e.getMessage(), null)),
						null);
			}
		}

		QvtoEvalEnvironment env = QvtoEvalEnvironment.forTransformation(context.configProperties());
		QvtoExtentManager extentManager = new QvtoExtentManager(transformation, context);

		QvtoEvaluator evaluator = new QvtoEvaluator(
				oclEngine, env, options, transformation, extentManager, this);
		evaluator.setConfigProperties(context.configProperties());

		// Register QVT-O operations as OCL custom provider for mutual recursion
		QvtoOperationProvider qvtoProvider = new QvtoOperationProvider(
				transformation, evaluator, evaluator.getOperationResolver(), blackboxRegistry);
		oclEngine.registerOperations(qvtoProvider);
		try {
			List<Diagnostic> diagnostics = evaluator.execute();
			Trace trace = options.tracingEnabled() ? evaluator.getTrace() : null;
			return new QvtoExecutionResult(diagnostics, trace);
		} finally {
			oclEngine.unregisterOperations(qvtoProvider);
		}
	}

	// --- Extension Registration ---

	@Override
	public void registerUnitResolver(QvtoUnitResolver resolver) {
		Objects.requireNonNull(resolver, "resolver must not be null");
		unitResolvers.add(resolver);
	}

	@Override
	public void unregisterUnitResolver(QvtoUnitResolver resolver) {
		Objects.requireNonNull(resolver, "resolver must not be null");
		unitResolvers.remove(resolver);
	}

	/**
	 * Returns the underlying OCL engine (for testing and advanced use).
	 */
	public OclEngineImpl getOclEngine() {
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
}
