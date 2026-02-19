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
package org.eclipse.fennec.m2m.ocl.engine;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.fennec.m2m.model.ocl.Constraint;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.ocl.api.CompleteOclContribution;
import org.eclipse.fennec.m2m.ocl.api.OclContext;
import org.eclipse.fennec.m2m.ocl.api.OclEngine;
import org.eclipse.fennec.m2m.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2m.ocl.api.OclExpressionParser;
import org.eclipse.fennec.m2m.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.eclipse.fennec.m2m.ocl.api.OclResult;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclEvalEnvironment;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclEvaluator;

/**
 * Plain Java implementation of the {@link OclEngine} facade.
 *
 * <p>This class has no OSGi dependencies and can be instantiated directly:
 * <pre>
 * OclExpressionParser parser = new OclParserSupport();
 * OclEngine engine = new OclEngineImpl(parser);
 * Object result = engine.evaluate("self.name", OclContext.of(myEObject));
 * </pre>
 *
 * <p>In an OSGi environment, use the {@code OclEngineComponent} DS adapter
 * that extends this class and injects dependencies via {@code @Reference}.
 *
 * <p>Thread safety: this class is thread-safe. The parser and extension lists
 * are safely shared, and each {@code evaluate()} call creates its own
 * evaluation environment (no shared mutable state during evaluation).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class OclEngineImpl implements OclEngine {

	private final OclExpressionParser parser;
	private final List<OclOperationProvider> operationProviders = new CopyOnWriteArrayList<>();
	private final List<CompleteOclContribution> oclContributions = new CopyOnWriteArrayList<>();

	/**
	 * Creates a new engine with the given parser.
	 *
	 * @param parser the OCL expression parser, must not be {@code null}
	 */
	public OclEngineImpl(OclExpressionParser parser) {
		this.parser = Objects.requireNonNull(parser, "parser must not be null");
	}

	// --- Parsing ---

	@Override
	public OclExpression parse(String expression, EClassifier contextType) throws OclParseException {
		return parser.parse(expression, contextType);
	}

	@Override
	public List<Constraint> parseDocument(String oclDocument) throws OclParseException {
		return parser.parseDocument(oclDocument);
	}

	// --- Evaluation ---

	@Override
	public Object evaluate(OclExpression expression, OclContext context) {
		return evaluate(expression, context, OclEvaluationOptions.strict());
	}

	@Override
	public Object evaluate(OclExpression expression, OclContext context, OclEvaluationOptions options) {
		OclResult result = evaluateWithDiagnostics(expression, context, options);
		return result.value();
	}

	@Override
	public Object evaluate(String expression, OclContext context) throws OclParseException {
		OclExpression parsed = parse(expression, context.self().eClass());
		return evaluate(parsed, context);
	}

	@Override
	public OclResult evaluateWithDiagnostics(OclExpression expression, OclContext context,
			OclEvaluationOptions options) {
		Objects.requireNonNull(expression, "expression must not be null");
		Objects.requireNonNull(context, "context must not be null");
		Objects.requireNonNull(options, "options must not be null");

		OclEvalEnvironment env = OclEvalEnvironment.root(context);
		OclEvaluator evaluator = new OclEvaluator(env, options, getOperationProviders());
		return evaluator.evaluate(expression);
	}

	// --- Validation ---

	@Override
	public List<Diagnostic> validate(OclExpression expression, EClassifier contextType) {
		Objects.requireNonNull(expression, "expression must not be null");
		Objects.requireNonNull(contextType, "contextType must not be null");

		// TODO: implement expression validation (step 5+)
		return List.of();
	}

	// --- Extension: Custom Operations ---

	@Override
	public void registerOperations(OclOperationProvider provider) {
		Objects.requireNonNull(provider, "provider must not be null");
		operationProviders.add(provider);
	}

	@Override
	public void unregisterOperations(OclOperationProvider provider) {
		Objects.requireNonNull(provider, "provider must not be null");
		operationProviders.remove(provider);
	}

	// --- Extension: Complete OCL Documents ---

	@Override
	public void registerCompleteOclDocument(CompleteOclContribution contribution) {
		Objects.requireNonNull(contribution, "contribution must not be null");
		oclContributions.add(contribution);
	}

	@Override
	public void unregisterCompleteOclDocument(CompleteOclContribution contribution) {
		Objects.requireNonNull(contribution, "contribution must not be null");
		oclContributions.remove(contribution);
	}

	// --- Internal accessors for subclasses and evaluator ---

	/**
	 * Returns the registered operation providers.
	 *
	 * @return unmodifiable snapshot of operation providers
	 */
	protected List<OclOperationProvider> getOperationProviders() {
		return List.copyOf(operationProviders);
	}

	/**
	 * Returns the registered Complete OCL contributions.
	 *
	 * @return unmodifiable snapshot of OCL contributions
	 */
	protected List<CompleteOclContribution> getOclContributions() {
		return List.copyOf(oclContributions);
	}
}
