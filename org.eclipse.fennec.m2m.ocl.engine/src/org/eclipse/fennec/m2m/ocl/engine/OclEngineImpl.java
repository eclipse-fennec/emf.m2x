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
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.fennec.m2m.model.ocl.Constraint;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.ocl.api.CompleteOclContribution;
import org.eclipse.fennec.m2m.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2m.ocl.api.OclContext;
import org.eclipse.fennec.m2m.ocl.api.OclEngine;
import org.eclipse.fennec.m2m.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2m.ocl.api.OclExpressionCache;
import org.eclipse.fennec.m2m.ocl.api.OclExpressionParser;
import org.eclipse.fennec.m2m.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.eclipse.fennec.m2m.ocl.api.OclResult;
import org.eclipse.fennec.m2m.ocl.engine.internal.PropertyAccessorCache;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclBag;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclDelegateUtil;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclEvalEnvironment;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclEvaluator;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclInvocationDelegateFactory;
import org.eclipse.fennec.m2m.ocl.engine.internal.PreStateSnapshot;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclOrderedSet;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclSettingDelegateFactory;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclUnlimitedNatural;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclValidationDelegateFactory;

/**
 * Plain Java implementation of the {@link OclEngine} facade.
 *
 * <p>This class has no OSGi dependencies and can be instantiated directly:
 * <pre>
 * OclConfiguration config = OclConfiguration.builder(parser).build();
 * OclEngine engine = new OclEngineImpl(config);
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
	private final OclExpressionCache expressionCache;
	private final PropertyAccessorCache accessorCache;
	private final List<OclOperationProvider> operationProviders = new CopyOnWriteArrayList<>();
	private final List<CompleteOclContribution> oclContributions = new CopyOnWriteArrayList<>();
	private volatile OclEvaluationOptions delegateOptions = OclEvaluationOptions.strict();

	/**
	 * Creates a new engine from the given configuration.
	 *
	 * <p>The configuration bundles the parser, optional expression cache,
	 * and any pre-registered operation providers. Multiple engines can share
	 * the same configuration (and thus the same caches).
	 *
	 * @param config the engine configuration, must not be {@code null}
	 */
	public OclEngineImpl(OclConfiguration config) {
		Objects.requireNonNull(config, "config must not be null");
		this.parser = config.parser();
		this.expressionCache = config.expressionCache();
		this.accessorCache = new PropertyAccessorCache();
		this.operationProviders.addAll(config.operationProviders());
	}

	/**
	 * Creates a new engine with the given parser and no expression cache.
	 *
	 * @param parser the OCL expression parser, must not be {@code null}
	 */
	public OclEngineImpl(OclExpressionParser parser) {
		this(OclConfiguration.builder(parser).build());
	}

	/**
	 * Creates a new engine with the given parser and optional expression cache.
	 *
	 * <p>When a cache is provided, {@link #parse(String, EClassifier)} will
	 * check the cache before parsing and store results after parsing. The cache
	 * can be shared across multiple engine instances.
	 *
	 * @param parser the OCL expression parser, must not be {@code null}
	 * @param cache the expression cache, or {@code null} for no caching
	 */
	public OclEngineImpl(OclExpressionParser parser, OclExpressionCache cache) {
		this(OclConfiguration.builder(parser).expressionCache(cache).build());
	}

	// --- Parsing ---

	@Override
	public OclExpression parse(String expression, EClassifier contextType) throws OclParseException {
		if (expressionCache != null) {
			OclExpression cached = expressionCache.get(expression, contextType);
			if (cached != null) {
				return cached;
			}
		}
		OclExpression parsed = parser.parse(expression, contextType);
		if (expressionCache != null) {
			expressionCache.put(expression, contextType, parsed);
		}
		return parsed;
	}

	@Override
	public List<Constraint> parseDocument(String oclDocument) throws OclParseException {
		return parser.parseDocument(oclDocument);
	}

	@Override
	public List<Constraint> parseDocument(String oclDocument, ResourceSet resourceSet)
			throws OclParseException {
		return parser.parseDocument(oclDocument, resourceSet);
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
		EClassifier contextType = context.self() != null ? context.self().eClass() : null;
		OclExpression parsed = parse(expression, contextType);
		return evaluate(parsed, context);
	}


	@Override
	public OclResult evaluateWithDiagnostics(OclExpression expression, OclContext context,
			OclEvaluationOptions options) {
		Objects.requireNonNull(expression, "expression must not be null");
		Objects.requireNonNull(context, "context must not be null");
		Objects.requireNonNull(options, "options must not be null");

		OclEvalEnvironment env = OclEvalEnvironment.root(context);
		OclEvaluator evaluator = new OclEvaluator(env, options, getOperationProviders(), accessorCache);
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

	// --- Postcondition Evaluation ---

	/**
	 * Evaluates a postcondition expression with the given pre-state snapshot.
	 * Used by the invocation delegate for postcondition evaluation with
	 * {@code @pre} support.
	 */
	public Object evaluatePostcondition(OclExpression expression, OclContext context,
			PreStateSnapshot snapshot) {
		OclEvalEnvironment env = OclEvalEnvironment.root(context);
		OclEvaluator evaluator = new OclEvaluator(env, delegateOptions, getOperationProviders(), accessorCache);
		evaluator.setPreStateSnapshot(snapshot);
		OclResult result = evaluator.evaluate(expression);
		return narrowResult(result.value());
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

	// --- Warm-Up ---

	/**
	 * Pre-populates caches for the given EPackage, reducing first-access latency.
	 *
	 * <p>This method performs two kinds of warm-up:
	 * <ol>
	 *   <li><b>Property accessor cache:</b> For each non-abstract EClass, creates a
	 *       temporary instance and pre-populates this engine's {@link PropertyAccessorCache}
	 *       for all structural features (LambdaMetafactory accessors for generated models).</li>
	 *   <li><b>Expression parse cache:</b> If an {@link OclExpressionCache} is configured,
	 *       scans all EAnnotations with the Fennec OCL delegate URI and pre-parses
	 *       OCL expressions (derivation, initial, body, pre, post, constraint names).</li>
	 * </ol>
	 *
	 * <p>Parse errors during warm-up are silently ignored — they will be reported
	 * at evaluation time. This is intentional: warm-up should not prevent application
	 * startup.
	 *
	 * <p>Can be called multiple times for different packages. Thread-safe.
	 *
	 * @param ePackage the EPackage to warm up, must not be {@code null}
	 */
	public void warmUp(EPackage ePackage) {
		Objects.requireNonNull(ePackage, "ePackage must not be null");
		String delegateUri = OclDelegateUtil.DELEGATE_URI;

		for (EClassifier classifier : ePackage.getEClassifiers()) {
			if (!(classifier instanceof EClass eClass)) {
				continue;
			}

			// 1. Property accessor warm-up (skip abstract classes — no factory instance)
			if (!eClass.isAbstract() && !eClass.isInterface()) {
				try {
					EObject dummy = ePackage.getEFactoryInstance().create(eClass);
					accessorCache.warmUp(dummy);
				} catch (Exception e) {
					// Dynamic or broken classes — skip silently
				}
			}

			// 2. Expression parse warm-up (derivation, initial on features)
			if (expressionCache != null) {
				for (EStructuralFeature sf : eClass.getEStructuralFeatures()) {
					EAnnotation ann = sf.getEAnnotation(delegateUri);
					if (ann == null) {
						continue;
					}
					for (String key : List.of("derivation", "initial")) {
						String expr = ann.getDetails().get(key);
						if (expr != null) {
							tryParse(expr, eClass);
						}
					}
				}

				// 3. Expression parse warm-up (body, pre, post on operations)
				for (EOperation op : eClass.getEOperations()) {
					EAnnotation ann = op.getEAnnotation(delegateUri);
					if (ann == null) {
						continue;
					}
					for (String key : List.of("body", "pre", "post")) {
						String expr = ann.getDetails().get(key);
						if (expr != null) {
							tryParse(expr, eClass);
						}
					}
				}

				// 4. Expression parse warm-up (constraint expressions on class)
				EAnnotation classAnn = eClass.getEAnnotation(delegateUri);
				if (classAnn != null) {
					for (Map.Entry<String, String> detail : classAnn.getDetails().entrySet()) {
						tryParse(detail.getValue(), eClass);
					}
				}
			}
		}
	}

	/**
	 * Attempts to parse an expression, storing it in the cache on success.
	 * Silently ignores parse failures.
	 */
	private void tryParse(String expression, EClass contextType) {
		try {
			parse(expression, contextType);
		} catch (OclParseException e) {
			// Silently ignore — will be reported at evaluation time
		}
	}

	// --- Internal accessors for subclasses and evaluator ---

	/**
	 * Returns the registered operation providers.
	 *
	 * @return unmodifiable snapshot of operation providers
	 */
	public List<OclOperationProvider> getOperationProviders() {
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
	 * Returns the evaluation options used by EMF delegates.
	 *
	 * @return the current delegate options
	 */
	public OclEvaluationOptions getDelegateOptions() {
		return delegateOptions;
	}

	/**
	 * Sets the evaluation options used by EMF delegates.
	 *
	 * @param options the delegate options, must not be {@code null}
	 */
	public void setDelegateOptions(OclEvaluationOptions options) {
		this.delegateOptions = Objects.requireNonNull(options, "options must not be null");
	}

	/**
	 * Returns the expression cache, or {@code null} if no cache was configured.
	 *
	 * @return the expression cache, or {@code null}
	 */
	public OclExpressionCache getExpressionCache() {
		return expressionCache;
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
		if (value instanceof OclUnlimitedNatural) {
			return value; // preserve * sentinel as-is
		}
		if (value instanceof Long l) {
			if (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE) {
				return l.intValue();
			}
			return value;
		}
		if (value instanceof OclBag<?> bag) {
			OclBag<Object> narrowed = new OclBag<>();
			for (Object elem : bag) {
				narrowed.add(narrowResult(elem));
			}
			return narrowed;
		}
		if (value instanceof OclOrderedSet<?> oset) {
			OclOrderedSet<Object> narrowed = new OclOrderedSet<>();
			for (Object elem : oset) {
				narrowed.add(narrowResult(elem));
			}
			return narrowed;
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
