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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EValidator;
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
import org.eclipse.fennec.m2m.ocl.engine.internal.OclDelegateUtil;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclEvalEnvironment;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclEvaluator;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclInvocationDelegateFactory;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclSettingDelegateFactory;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclValidationDelegateFactory;

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
		return narrowResult(result.value());
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

	// --- EMF Delegate Registration ---

	/**
	 * Registers this engine as EMF delegate for invocation, setting, and
	 * validation under the Fennec OCL delegate URI
	 * ({@value OclDelegateUtil#DELEGATE_URI}).
	 *
	 * <p>After calling this method, EOperations, EStructuralFeatures, and
	 * EClasses annotated with the Fennec delegate URI will be evaluated
	 * using this engine.
	 *
	 * <p>For standalone (non-OSGi) usage:
	 * <pre>
	 * OclEngine engine = new OclEngineImpl(parser);
	 * engine.installDelegates();
	 * </pre>
	 *
	 * @see #uninstallDelegates()
	 */
	public void installDelegates() {
		String uri = OclDelegateUtil.DELEGATE_URI;

		EOperation.Internal.InvocationDelegate.Factory.Registry.INSTANCE
				.put(uri, new OclInvocationDelegateFactory(this));

		EStructuralFeature.Internal.SettingDelegate.Factory.Registry.INSTANCE
				.put(uri, new OclSettingDelegateFactory(this));

		EValidator.ValidationDelegate.Registry.INSTANCE
				.put(uri, new OclValidationDelegateFactory(this));
	}

	/**
	 * Removes the Fennec OCL delegate registrations from the global EMF registries.
	 *
	 * @see #installDelegates()
	 */
	public void uninstallDelegates() {
		String uri = OclDelegateUtil.DELEGATE_URI;

		EOperation.Internal.InvocationDelegate.Factory.Registry.INSTANCE.remove(uri);
		EStructuralFeature.Internal.SettingDelegate.Factory.Registry.INSTANCE.remove(uri);
		EValidator.ValidationDelegate.Registry.INSTANCE.remove(uri);
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

	/**
	 * Narrows the evaluation result for the caller: Long values that fit
	 * in {@code int} are returned as {@link Integer}. Collection elements
	 * are narrowed recursively.
	 *
	 * <p>Internally the engine uses {@code Long} for all integer arithmetic.
	 * This method aligns the return type with what EMF users expect: EInt
	 * features return {@code Integer}, and OCL integer literals that fit
	 * in 32 bits are also returned as {@code Integer}.
	 */
	private static Object narrowResult(Object value) {
		if (value instanceof Long l) {
			if (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE) {
				return l.intValue();
			}
			return value;
		}
		if (value instanceof List<?> list) {
			List<Object> narrowed = new ArrayList<>(list.size());
			for (Object elem : list) {
				narrowed.add(narrowResult(elem));
			}
			return narrowed;
		}
		if (value instanceof LinkedHashSet<?> set) {
			LinkedHashSet<Object> narrowed = new LinkedHashSet<>(set.size());
			for (Object elem : set) {
				narrowed.add(narrowResult(elem));
			}
			return narrowed;
		}
		if (value instanceof Collection<?> coll) {
			List<Object> narrowed = new ArrayList<>(coll.size());
			for (Object elem : coll) {
				narrowed.add(narrowResult(elem));
			}
			return narrowed;
		}
		if (value instanceof Map<?, ?> map) {
			Map<Object, Object> narrowed = new LinkedHashMap<>(map.size());
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				narrowed.put(narrowResult(entry.getKey()), narrowResult(entry.getValue()));
			}
			return narrowed;
		}
		return value;
	}
}
