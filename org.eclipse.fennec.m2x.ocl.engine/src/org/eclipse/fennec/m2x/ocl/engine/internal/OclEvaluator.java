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
package org.eclipse.fennec.m2x.ocl.engine.internal;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.fennec.m2x.model.ocl.AnyType;
import org.eclipse.fennec.m2x.model.ocl.BooleanLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.ClassifierType;
import org.eclipse.fennec.m2x.model.ocl.CollectionType;
import org.eclipse.fennec.m2x.model.ocl.CollectionItem;
import org.eclipse.fennec.m2x.model.ocl.CollectionKind;
import org.eclipse.fennec.m2x.model.ocl.CollectionLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.CollectionLiteralPart;
import org.eclipse.fennec.m2x.model.ocl.CollectionRange;
import org.eclipse.fennec.m2x.model.ocl.EnumLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.IfExp;
import org.eclipse.fennec.m2x.model.ocl.IntegerLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.InvalidLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.IterateExp;
import org.eclipse.fennec.m2x.model.ocl.IteratorExp;
import org.eclipse.fennec.m2x.model.ocl.LetExp;
import org.eclipse.fennec.m2x.model.ocl.MapLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.MapLiteralPart;
import org.eclipse.fennec.m2x.model.ocl.MessageExp;
import org.eclipse.fennec.m2x.model.ocl.NullLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclType;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.model.ocl.PropertyCallExp;
import org.eclipse.fennec.m2x.model.ocl.RealLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.StringLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.TupleLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.TupleLiteralPart;
import org.eclipse.fennec.m2x.model.ocl.TypeExp;
import org.eclipse.fennec.m2x.model.ocl.UnlimitedNaturalLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.model.ocl.util.OclSwitch;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions.NullHandling;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclOperation;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.ocl.api.OclResult;

/**
 * Core OCL expression evaluator using the EMF-generated {@link OclSwitch}
 * for dispatch by AST node type.
 *
 * <p>Each {@code evaluate()} call creates a fresh evaluator instance with its own
 * {@link OclEvalEnvironment} — no shared mutable state, thread-safe by design.
 *
 * <p>The evaluator is a recursive interpreter: each {@code caseXxx} method evaluates
 * its child expressions recursively via {@link #eval(EObject)}.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class OclEvaluator extends OclSwitch<Object> {

	private static final String SOURCE_ID = "org.eclipse.fennec.m2x.ocl.engine";

	/**
	 * Internal sentinel for OCL null (OclVoid). The EMF-generated OclSwitch
	 * treats a Java {@code null} return from {@code caseXxx} as "not handled"
	 * and falls through to the next case. We use this sentinel to distinguish
	 * "OCL null" from "case not handled", and unwrap it in {@link #evaluate}.
	 */
	private static final Object OCL_NULL = new Object() {
		@Override
		public String toString() {
			return "OCL_NULL";
		}
	};

	private final OclEvaluationOptions options;
	private final List<OclOperationProvider> customProviders;
	private final PropertyAccessorCache accessorCache;
	private final List<Diagnostic> diagnostics = new ArrayList<>();
	private OclEvalEnvironment env;
	private PreStateSnapshot preStateSnapshot = PreStateSnapshot.EMPTY;
	private Map<DefRegistry.DefKey, DefRegistry.DefEntry> defProperties = Map.of();
	private int depth;

	/*
	 * 1-entry feature resolution cache ("last resolved").
	 *
	 * resolveFeature() maps (EClass, EStructuralFeature) → resolved feature.
	 * In iterator bodies (select, collect, forAll, ...) the same feature is
	 * accessed on many objects of the same EClass — hundreds or thousands of
	 * times. The 1-entry cache turns all but the first resolveFeature() call
	 * into a simple reference comparison (two == checks, ~1 ns), eliminating
	 * the getFeatureID() call (~5–10 ns) on every subsequent iteration.
	 *
	 * Thread-safety: OclEvaluator is per-evaluation (not shared), so plain
	 * fields are safe — no synchronization needed.
	 */
	private EClass lastResolvedClass;
	private EStructuralFeature lastResolvedInput;
	private EStructuralFeature lastResolvedOutput;

	/**
	 * Creates a new evaluator.
	 *
	 * @param env the root evaluation environment
	 * @param options evaluation options
	 * @param customProviders registered custom operation providers
	 * @param accessorCache the property accessor cache to use
	 */
	public OclEvaluator(OclEvalEnvironment env, OclEvaluationOptions options,
			List<OclOperationProvider> customProviders, PropertyAccessorCache accessorCache) {
		this.env = Objects.requireNonNull(env, "env must not be null");
		this.options = Objects.requireNonNull(options, "options must not be null");
		this.customProviders = Objects.requireNonNull(customProviders, "customProviders must not be null");
		this.accessorCache = Objects.requireNonNull(accessorCache, "accessorCache must not be null");
	}

	/**
	 * Sets the pre-state snapshot for postcondition evaluation.
	 * Must be called before {@link #evaluate(OclExpression)}.
	 */
	public void setPreStateSnapshot(PreStateSnapshot snapshot) {
		this.preStateSnapshot = Objects.requireNonNull(snapshot, "snapshot must not be null");
	}

	/**
	 * Sets the def-property registry for Complete OCL {@code def:} expressions.
	 * Must be called before {@link #evaluate(OclExpression)}.
	 */
	public void setDefProperties(Map<DefRegistry.DefKey, DefRegistry.DefEntry> defProperties) {
		this.defProperties = Objects.requireNonNull(defProperties, "defProperties must not be null");
	}

	/**
	 * Evaluates an expression and returns the result with diagnostics.
	 *
	 * @param expression the parsed OCL expression
	 * @return the evaluation result with diagnostics
	 */
	public OclResult evaluate(OclExpression expression) {
		Object result = eval(expression);
		return new OclResult(result, diagnostics);
	}

	/**
	 * Internal evaluation entry point. Calls the EMF switch and unwraps
	 * the {@link #OCL_NULL} sentinel to Java {@code null}.
	 *
	 * <p>All recursive evaluation within {@code caseXxx} methods must use
	 * this method instead of {@code doSwitch} directly, so that OCL null
	 * values are correctly propagated through the expression tree.
	 */
	private Object eval(OclExpression expression) {
		if (expression == null) {
			return null; // null source (e.g. module-level QVT-O calls)
		}
		if (++depth > options.maxDepth()) {
			--depth;
			return addError("Maximum evaluation depth exceeded: " + options.maxDepth());
		}
		try {
			Object result = doSwitch(expression);
			return result == OCL_NULL ? null : result;
		} finally {
			--depth;
		}
	}

	/**
	 * Wraps a value for return from {@code caseXxx} methods: replaces
	 * Java {@code null} with {@link #OCL_NULL} so the EMF Switch does not
	 * treat it as "not handled".
	 */
	private static Object wrapNull(Object value) {
		return value == null ? OCL_NULL : value;
	}

	// --- Literal Expressions ---

	@Override
	public Object caseIntegerLiteralExp(IntegerLiteralExp exp) {
		return exp.getIntegerSymbol();
	}

	@Override
	public Object caseRealLiteralExp(RealLiteralExp exp) {
		return exp.getRealSymbol();
	}

	@Override
	public Object caseStringLiteralExp(StringLiteralExp exp) {
		return exp.getStringSymbol();
	}

	@Override
	public Object caseBooleanLiteralExp(BooleanLiteralExp exp) {
		return exp.isBooleanSymbol();
	}

	@Override
	public Object caseNullLiteralExp(NullLiteralExp exp) {
		return OCL_NULL;
	}

	@Override
	public Object caseInvalidLiteralExp(InvalidLiteralExp exp) {
		return OclInvalid.INSTANCE;
	}

	@Override
	public Object caseUnlimitedNaturalLiteralExp(UnlimitedNaturalLiteralExp exp) {
		long value = exp.getUnlimitedNaturalSymbol();
		if (value == -1L) {
			return OclUnlimitedNatural.INSTANCE;
		}
		return value;
	}

	@Override
	public Object caseEnumLiteralExp(EnumLiteralExp exp) {
		return exp.getReferredLiteral();
	}

	@Override
	public Object caseTypeExp(TypeExp exp) {
		return exp.getReferredType();
	}

	// --- Structural Expressions ---

	@Override
	public Object caseVariableExp(VariableExp exp) {
		Variable variable = exp.getReferredVariable();
		String name = variable.getName();
		if (env.contains(name)) {
			return wrapNull(env.lookup(name));
		}
		// §7.5.6 / §8.1.18: implicit self — bare name resolves to self.featureName
		if (env.contains("self")) {
			Object self = env.lookup("self");
			if (self instanceof EObject eObj) {
				EStructuralFeature sf = eObj.eClass().getEStructuralFeature(name);
				if (sf != null) {
					return wrapNull(eObj.eGet(sf));
				}
			}
		}
		return addError("Unresolved variable: " + name);
	}

	@Override
	public Object caseIfExp(IfExp exp) {
		Object condition = eval(exp.getOwnedCondition());
		if (condition == OclInvalid.INSTANCE) {
			return OclInvalid.INSTANCE;
		}
		if (!(condition instanceof Boolean)) {
			return addError("if-condition must be Boolean, got: "
					+ (condition == null ? "null" : condition.getClass().getSimpleName()));
		}
		return wrapNull((Boolean) condition
				? eval(exp.getOwnedThen())
				: eval(exp.getOwnedElse()));
	}

	@Override
	public Object caseLetExp(LetExp exp) {
		Variable variable = exp.getOwnedVariable();
		Object value = null;
		if (variable.getOwnedInit() != null) {
			value = eval(variable.getOwnedInit());
		}
		OclEvalEnvironment previousEnv = env;
		try {
			env = env.nested(variable.getName(), value);
			return wrapNull(eval(exp.getOwnedIn()));
		} finally {
			env = previousEnv;
		}
	}

	// --- Property Access ---

	@Override
	public Object casePropertyCallExp(PropertyCallExp exp) {
		EStructuralFeature sf = exp.getReferredProperty();
		if (sf == null) {
			return addError("Unresolved property on " + exp.eClass().getName());
		}

		Object source = eval(exp.getOwnedSource());

		// Safe navigation
		if (source == null && exp.isIsSafe()) {
			return OCL_NULL;
		}

		// Null/Invalid handling
		Object nullCheck = checkNullInvalid(source, "property '" + sf.getName() + "'");
		if (nullCheck != null) {
			return nullCheck;
		}

		// oclLocale: OclAny property (OCL v2.4 §11.2.1)
		if ("oclLocale".equals(sf.getName())) {
			Object localeValue = env.lookup("oclLocale");
			return localeValue == null ? OclEvalEnvironment.DEFAULT_OCL_LOCALE : localeValue;
		}

		// Tuple part access (tuples are represented as Map<String, Object>)
		if (source instanceof Map<?, ?> map) {
			Object value = map.get(sf.getName());
			return value == null ? OCL_NULL : value;
		}

		// Implicit collect: source is a collection, apply property to each element
		if (source instanceof Collection<?> col) {
			List<Object> result = new ArrayList<>(col.size());
			for (Object elem : col) {
				if (elem instanceof EObject elemObj) {
					Object intercepted = tryPropertyInterceptor(elemObj, sf.getName());
					if (intercepted != OclContext.PROPERTY_NOT_HANDLED) {
						result.add(intercepted == null ? OCL_NULL : widenInteger(intercepted));
					} else {
						// Check for def-property if real feature doesn't exist
						EStructuralFeature realFeat = elemObj.eClass().getEStructuralFeature(sf.getName());
						if (realFeat == null && !defProperties.isEmpty()) {
							DefRegistry.DefEntry defEntry = lookupDefProperty(elemObj.eClass(), sf.getName());
							if (defEntry != null && !defEntry.isOperation()) {
								Object defResult = evaluateDefBody(defEntry.body(), elemObj);
								result.add(defResult == null ? OCL_NULL : widenInteger(defResult));
								continue;
							}
						}
						EStructuralFeature actual = resolveFeature(elemObj, sf);
						Object value = getProperty(elemObj, actual);
						result.add(value == null ? OCL_NULL : widenInteger(value));
					}
				} else {
					result.add(OclInvalid.INSTANCE);
				}
			}
			return result;
		}

		if (!(source instanceof EObject eo)) {
			return addError("Property access requires an EObject or Tuple, got: "
					+ (source == null ? "null" : source.getClass().getSimpleName()));
		}

		// @pre support: return captured pre-state value
		if (exp.isIsPre() && preStateSnapshot.hasPreValue(exp)) {
			return wrapNull(widenInteger(preStateSnapshot.getPreValue(exp)));
		}

		// Property interceptor (e.g., QVT-O intermediate properties)
		Object intercepted = tryPropertyInterceptor(eo, sf.getName());
		if (intercepted != OclContext.PROPERTY_NOT_HANDLED) {
			return intercepted == null ? OCL_NULL : widenInteger(intercepted);
		}

		// Check if the feature actually exists on the runtime EClass
		// Real features have priority over def-properties
		EStructuralFeature realFeature = eo.eClass().getEStructuralFeature(sf.getName());
		if (realFeature != null) {
			sf = resolveFeature(eo, sf);
			Object value = getProperty(eo, sf);
			return value == null ? OCL_NULL : widenInteger(value);
		}

		// Def-property evaluation (Complete OCL def: constraints)
		if (!defProperties.isEmpty()) {
			DefRegistry.DefEntry defEntry = lookupDefProperty(eo.eClass(), sf.getName());
			if (defEntry != null && !defEntry.isOperation()) {
				Object defResult = evaluateDefBody(defEntry.body(), eo);
				return defResult == null ? OCL_NULL : widenInteger(defResult);
			}
		}

		// Fallback: try resolveFeature (may work for cross-type features)
		sf = resolveFeature(eo, sf);
		Object value = getProperty(eo, sf);
		return value == null ? OCL_NULL : widenInteger(value);
	}

	// --- Operation Call ---

	@Override
	public Object caseOperationCallExp(OperationCallExp exp) {
		Object source = eval(exp.getOwnedSource());
		String opName = exp.getName();

		// @pre support: return captured pre-state value
		if (exp.isIsPre() && preStateSnapshot.hasPreValue(exp)) {
			return wrapNull(preStateSnapshot.getPreValue(exp));
		}

		// oclIsNew(): check if object existed before the operation
		if ("oclIsNew".equals(opName)) {
			return source instanceof EObject eo ? !preStateSnapshot.existedBefore(eo) : OclInvalid.INSTANCE;
		}

		// Safe navigation
		if (source == null && exp.isIsSafe()) {
			return OCL_NULL;
		}

		// Spec §9.3.35[B]: Arrow on non-collection → implicit oclAsSet()
		// §11.3.2: null.oclAsSet() = Set{}, §11.3.3: invalid.oclAsSet() = invalid
		// §11.3.1: value.oclAsSet() = Set{value}
		// Must be applied BEFORE null-safe operations so null->oclIsUndefined() works correctly
		if (isArrowCall(exp) && !(source instanceof Collection<?>) && !(source instanceof Map<?, ?>)) {
			if (source == null) {
				source = new OclSet<>();
			} else if (source == OclInvalid.INSTANCE) {
				// invalid.oclAsSet() = invalid → propagates through all operations
				// (but oclIsUndefined/oclIsInvalid are exception ops, handled below)
			} else {
				OclSet<Object> wrapped = new OclSet<>();
				wrapped.add(source);
				source = wrapped;
			}
		}

		// allInstances() — Type.allInstances() returns Set of all instances
		if ("allInstances".equals(opName) && source instanceof OclType) {
			return wrapNull(evaluateAllInstances(source));
		}

		// OclAny null/invalid-safe operations (must work on null/invalid source)
		if (isNullSafeOperation(opName)) {
			Object[] args = evaluateArguments(exp.getOwnedArguments());
			return wrapNull(OclStdlib.dispatch(opName, source, args, options, resolveOclLocale()));
		}

		// Three-valued boolean logic (OCL v2.4 §11.3.1)
		// Must be handled before generic null/invalid checks to support short-circuit
		if (isBooleanThreeValuedOp(opName)
				&& (source instanceof Boolean || source == OclInvalid.INSTANCE || source == null)) {
			Object[] args = evaluateArguments(exp.getOwnedArguments());
			return evaluateThreeValuedBoolean(opName, source, args);
		}

		// Null/Invalid handling for other operations
		Object nullCheck = checkNullInvalid(source, "operation '" + opName + "'");
		if (nullCheck != null) {
			return nullCheck;
		}

		// Evaluate arguments
		Object[] args = evaluateArguments(exp.getOwnedArguments());

		// Check for OclInvalid in arguments — propagate invalid
		for (Object arg : args) {
			if (arg == OclInvalid.INSTANCE) {
				return OclInvalid.INSTANCE;
			}
		}

		// Check for null arguments in non-collection operations — null in arithmetic/string context is invalid
		if (!(source instanceof Collection<?>) && !(source instanceof Map<?, ?>)) {
			for (Object arg : args) {
				if (arg == null) {
					return OclInvalid.INSTANCE;
				}
			}
		}

		// 1. Try Ecore model operation (referredOperation set by parser)
		if (exp.getReferredOperation() != null && source instanceof EObject eo) {
			try {
				EList<Object> eArgs = args.length > 0
						? new BasicEList<>(List.of(args))
						: ECollections.emptyEList();
				return wrapNull(eo.eInvoke(exp.getReferredOperation(), eArgs));
			} catch (InvocationTargetException e) {
				Throwable cause = e.getCause();
				return addError("Operation invocation failed: " + opName + " - " + (cause != null ? cause.getMessage() : e.getMessage()));
			}
		}

		// 2. Try standard library
		Object result = OclStdlib.dispatch(opName, source, args, options, resolveOclLocale());
		if (result != OclStdlib.NOT_FOUND) {
			return wrapNull(result);
		}

		// 3. Try custom operation providers
		result = dispatchCustomOperation(opName, source, args);
		if (result != OclStdlib.NOT_FOUND) {
			return wrapNull(result);
		}

		return addError("Unknown operation: " + opName + " on "
				+ (source == null ? "null" : source.getClass().getSimpleName()));
	}

	// --- Collection Literals ---

	@Override
	public Object caseCollectionLiteralExp(CollectionLiteralExp exp) {
		CollectionKind kind = exp.getKind();
		List<Object> elements = new ArrayList<>();

		for (CollectionLiteralPart part : exp.getOwnedParts()) {
			if (part instanceof CollectionItem item) {
				elements.add(eval(item.getOwnedItem()));
			} else if (part instanceof CollectionRange range) {
				Object first = eval(range.getOwnedFirst());
				Object last = eval(range.getOwnedLast());
				if (!(first instanceof Long f) || !(last instanceof Long l)) {
					// Eclipse bug415661: invalid/null bounds → silently return invalid
					if (first == OclInvalid.INSTANCE || last == OclInvalid.INSTANCE
							|| first == null || last == null) {
						return OclInvalid.INSTANCE;
					}
					return addError("Collection range requires Integer bounds");
				}
				long rangeSize = l - f + 1;
				if (rangeSize > options.maxCollectionSize()) {
					return addError("Collection range size " + rangeSize
							+ " exceeds maximum allowed size: " + options.maxCollectionSize());
				}
				for (long i = f; i <= l; i++) {
					elements.add(i);
				}
			}
			if (elements.size() > options.maxCollectionSize()) {
				return addError("Collection size " + elements.size()
						+ " exceeds maximum allowed size: " + options.maxCollectionSize());
			}
		}

		return switch (kind) {
			case SET -> new OclSet<>(elements);
			case ORDERED_SET -> new OclOrderedSet<>(elements);
			case SEQUENCE -> new ArrayList<>(elements);
			case BAG -> new OclBag<>(elements);
			case COLLECTION -> new ArrayList<>(elements);
		};
	}

	// --- Tuple Literals ---

	@Override
	public Object caseTupleLiteralExp(TupleLiteralExp exp) {
		Map<String, Object> tuple = new LinkedHashMap<>();
		for (TupleLiteralPart part : exp.getOwnedParts()) {
			Object value = null;
			if (part.getOwnedInit() != null) {
				value = eval(part.getOwnedInit());
			}
			tuple.put(part.getName(), value);
		}
		return tuple;
	}

	// --- Map Literals (v2.5) ---

	@Override
	public Object caseMapLiteralExp(MapLiteralExp exp) {
		Map<Object, Object> map = new LinkedHashMap<>();
		for (MapLiteralPart part : exp.getOwnedParts()) {
			Object key = eval(part.getOwnedKey());
			Object value = eval(part.getOwnedValue());
			map.put(key, value);
		}
		return map;
	}

	// --- Iterator Expressions (OCL v2.4 Section 11.9) ---

	@Override
	public Object caseIteratorExp(IteratorExp exp) {
		Object source = eval(exp.getOwnedSource());

		if (source == null && exp.isIsSafe()) {
			return OCL_NULL;
		}
		// Spec §9.3.35[B]: Arrow on non-collection → implicit oclAsSet()
		if (isArrowCall(exp) && !(source instanceof Collection<?>) && !(source instanceof Map<?, ?>)) {
			if (source == null) {
				source = new OclSet<>();
			} else if (source == OclInvalid.INSTANCE) {
				// invalid.oclAsSet() = invalid → propagates
			} else {
				OclSet<Object> wrapped = new OclSet<>();
				wrapped.add(source);
				source = wrapped;
			}
		}
		Object nullCheck = checkNullInvalid(source, "iterator '" + exp.getName() + "'");
		if (nullCheck != null) {
			return nullCheck;
		}
		if (!(source instanceof Collection<?> coll)) {
			return addError("Iterator source must be a Collection, got: "
					+ (source == null ? "null" : source.getClass().getSimpleName()));
		}

		List<Variable> iterVars = exp.getOwnedIterators();
		String iterName = exp.getName();

		return wrapNull(switch (iterName) {
			case "select" -> iteratorSelect(coll, iterVars, exp.getOwnedBody());
			case "reject" -> iteratorReject(coll, iterVars, exp.getOwnedBody());
			case "collect" -> iteratorCollect(coll, iterVars, exp.getOwnedBody());
			case "collectNested" -> iteratorCollectNested(coll, iterVars, exp.getOwnedBody());
			case "forAll" -> iteratorForAll(coll, iterVars, exp.getOwnedBody());
			case "exists" -> iteratorExists(coll, iterVars, exp.getOwnedBody());
			case "any" -> iteratorAny(coll, iterVars, exp.getOwnedBody());
			case "one" -> iteratorOne(coll, iterVars, exp.getOwnedBody());
			case "isUnique" -> iteratorIsUnique(coll, iterVars, exp.getOwnedBody());
			case "sortedBy" -> iteratorSortedBy(coll, iterVars, exp.getOwnedBody());
			case "closure" -> iteratorClosure(coll, iterVars, exp.getOwnedBody());
			default -> addError("Unknown iterator: " + iterName);
		});
	}

	@Override
	public Object caseIterateExp(IterateExp exp) {
		Object source = eval(exp.getOwnedSource());

		if (source == null && exp.isIsSafe()) {
			return OCL_NULL;
		}
		// Spec §9.3.35[B]: Arrow on non-collection → implicit oclAsSet()
		if (isArrowCall(exp) && !(source instanceof Collection<?>) && !(source instanceof Map<?, ?>)) {
			if (source == null) {
				source = new OclSet<>();
			} else if (source == OclInvalid.INSTANCE) {
				// invalid.oclAsSet() = invalid → propagates
			} else {
				OclSet<Object> wrapped = new OclSet<>();
				wrapped.add(source);
				source = wrapped;
			}
		}
		Object nullCheck = checkNullInvalid(source, "iterate");
		if (nullCheck != null) {
			return nullCheck;
		}
		if (!(source instanceof Collection<?> coll)) {
			return addError("Iterate source must be a Collection, got: "
					+ (source == null ? "null" : source.getClass().getSimpleName()));
		}

		Variable accVar = exp.getOwnedResult();
		Object accValue = accVar.getOwnedInit() != null ? eval(accVar.getOwnedInit()) : null;
		List<Variable> iterVars = exp.getOwnedIterators();

		OclEvalEnvironment previousEnv = env;
		try {
			for (Object element : coll) {
				env = previousEnv.nested(iterVars.get(0).getName(), element);
				env = env.nested(accVar.getName(), accValue);
				accValue = eval(exp.getOwnedBody());
				if (accValue == OclInvalid.INSTANCE) {
					return OclInvalid.INSTANCE;
				}
			}
			return wrapNull(accValue);
		} finally {
			env = previousEnv;
		}
	}

	// --- Iterator implementations ---

	private Object iteratorSelect(Collection<?> source, List<Variable> iterVars,
			OclExpression body) {
		List<Object> result = new ArrayList<>();
		OclEvalEnvironment previousEnv = env;
		try {
			for (Object element : source) {
				env = previousEnv.nested(iterVars.get(0).getName(), element);
				Object bodyResult = eval(body);
				if (bodyResult == OclInvalid.INSTANCE || bodyResult == null) {
					return OclInvalid.INSTANCE;
				}
				if (Boolean.TRUE.equals(bodyResult)) {
					result.add(element);
				}
			}
			return preserveKind(source, result);
		} finally {
			env = previousEnv;
		}
	}

	private Object iteratorReject(Collection<?> source, List<Variable> iterVars,
			OclExpression body) {
		List<Object> result = new ArrayList<>();
		OclEvalEnvironment previousEnv = env;
		try {
			for (Object element : source) {
				env = previousEnv.nested(iterVars.get(0).getName(), element);
				Object bodyResult = eval(body);
				if (bodyResult == OclInvalid.INSTANCE || bodyResult == null) {
					return OclInvalid.INSTANCE;
				}
				if (!Boolean.TRUE.equals(bodyResult)) {
					result.add(element);
				}
			}
			return preserveKind(source, result);
		} finally {
			env = previousEnv;
		}
	}

	private Object iteratorCollect(Collection<?> source, List<Variable> iterVars,
			OclExpression body) {
		// Set/Bag source → Bag result; Sequence/OrderedSet source → Sequence result
		boolean isBagResult = source instanceof Set<?> || source instanceof OclBag<?>;
		List<Object> result = isBagResult ? new OclBag<>() : new ArrayList<>();
		OclEvalEnvironment previousEnv = env;
		try {
			for (Object element : source) {
				env = previousEnv.nested(iterVars.get(0).getName(), element);
				Object bodyResult = eval(body);
				// collect flattens one level
				if (bodyResult instanceof Collection<?> nested) {
					result.addAll(nested);
				} else {
					result.add(bodyResult);
				}
			}
			return result;
		} finally {
			env = previousEnv;
		}
	}

	private Object iteratorCollectNested(Collection<?> source, List<Variable> iterVars,
			OclExpression body) {
		// §11.9.2/§11.9.3: Set/Bag → Bag; §11.9.4/§11.9.5: Sequence/OrderedSet → Sequence
		boolean isBagResult = source instanceof Set<?> || source instanceof OclBag<?>;
		List<Object> result = isBagResult ? new OclBag<>() : new ArrayList<>();
		OclEvalEnvironment previousEnv = env;
		try {
			for (Object element : source) {
				env = previousEnv.nested(iterVars.get(0).getName(), element);
				result.add(eval(body));
			}
			return result; // collectNested does NOT flatten
		} finally {
			env = previousEnv;
		}
	}

	private Object iteratorForAll(Collection<?> source, List<Variable> iterVars,
			OclExpression body) {
		if (iterVars.size() > 1) {
			return iteratorForAllMulti(source, iterVars, body, 0);
		}
		// Spec §11.9.1: false > invalid > null > true
		boolean hasInvalid = false;
		boolean hasNull = false;
		OclEvalEnvironment previousEnv = env;
		try {
			for (Object element : source) {
				env = previousEnv.nested(iterVars.get(0).getName(), element);
				Object bodyResult = eval(body);
				if (Boolean.FALSE.equals(bodyResult)) {
					return false;
				}
				if (bodyResult == OclInvalid.INSTANCE) {
					hasInvalid = true;
				} else if (!Boolean.TRUE.equals(bodyResult)) {
					hasNull = true;
				}
			}
			return hasInvalid ? OclInvalid.INSTANCE : hasNull ? null : true;
		} finally {
			env = previousEnv;
		}
	}

	private Object iteratorForAllMulti(Collection<?> source, List<Variable> iterVars,
			OclExpression body, int depth) {
		// Spec §11.9.1: false > invalid > null > true
		boolean hasInvalid = false;
		boolean hasNull = false;
		OclEvalEnvironment previousEnv = env;
		try {
			for (Object element : source) {
				env = previousEnv.nested(iterVars.get(depth).getName(), element);
				Object result;
				if (depth + 1 < iterVars.size()) {
					result = iteratorForAllMulti(source, iterVars, body, depth + 1);
				} else {
					result = eval(body);
				}
				if (Boolean.FALSE.equals(result)) {
					return false;
				}
				if (result == OclInvalid.INSTANCE) {
					hasInvalid = true;
				} else if (!Boolean.TRUE.equals(result)) {
					hasNull = true;
				}
			}
			return hasInvalid ? OclInvalid.INSTANCE : hasNull ? null : true;
		} finally {
			env = previousEnv;
		}
	}

	private Object iteratorExists(Collection<?> source, List<Variable> iterVars,
			OclExpression body) {
		if (iterVars.size() > 1) {
			return iteratorExistsMulti(source, iterVars, body, 0);
		}
		// Spec §11.9.1: true > invalid > null > false
		boolean hasInvalid = false;
		boolean hasNull = false;
		OclEvalEnvironment previousEnv = env;
		try {
			for (Object element : source) {
				env = previousEnv.nested(iterVars.get(0).getName(), element);
				Object bodyResult = eval(body);
				if (Boolean.TRUE.equals(bodyResult)) {
					return true;
				}
				if (bodyResult == OclInvalid.INSTANCE) {
					hasInvalid = true;
				} else if (!Boolean.FALSE.equals(bodyResult)) {
					hasNull = true;
				}
			}
			return hasInvalid ? OclInvalid.INSTANCE : hasNull ? null : false;
		} finally {
			env = previousEnv;
		}
	}

	private Object iteratorExistsMulti(Collection<?> source, List<Variable> iterVars,
			OclExpression body, int depth) {
		// Spec §11.9.1: true > invalid > null > false
		boolean hasInvalid = false;
		boolean hasNull = false;
		OclEvalEnvironment previousEnv = env;
		try {
			for (Object element : source) {
				env = previousEnv.nested(iterVars.get(depth).getName(), element);
				Object result;
				if (depth + 1 < iterVars.size()) {
					result = iteratorExistsMulti(source, iterVars, body, depth + 1);
				} else {
					result = eval(body);
				}
				if (Boolean.TRUE.equals(result)) {
					return true;
				}
				if (result == OclInvalid.INSTANCE) {
					hasInvalid = true;
				} else if (!Boolean.FALSE.equals(result)) {
					hasNull = true;
				}
			}
			return hasInvalid ? OclInvalid.INSTANCE : hasNull ? null : false;
		} finally {
			env = previousEnv;
		}
	}

	private Object iteratorAny(Collection<?> source, List<Variable> iterVars,
			OclExpression body) {
		boolean hasInvalid = false;
		OclEvalEnvironment previousEnv = env;
		try {
			for (Object element : source) {
				env = previousEnv.nested(iterVars.get(0).getName(), element);
				Object bodyResult = eval(body);
				if (Boolean.TRUE.equals(bodyResult)) {
					return element;
				}
				if (bodyResult == OclInvalid.INSTANCE) {
					hasInvalid = true;
				}
			}
			// Eclipse: if any body was invalid and no match found → invalid
			return hasInvalid ? OclInvalid.INSTANCE : null;
		} finally {
			env = previousEnv;
		}
	}

	private Object iteratorOne(Collection<?> source, List<Variable> iterVars,
			OclExpression body) {
		boolean foundOne = false;
		boolean hasInvalid = false;
		OclEvalEnvironment previousEnv = env;
		try {
			for (Object element : source) {
				env = previousEnv.nested(iterVars.get(0).getName(), element);
				Object bodyResult = eval(body);
				if (Boolean.TRUE.equals(bodyResult)) {
					if (foundOne) {
						return false; // more than one
					}
					foundOne = true;
				} else if (!Boolean.FALSE.equals(bodyResult)) {
					hasInvalid = true; // invalid/null body
				}
			}
			if (hasInvalid) {
				return OclInvalid.INSTANCE;
			}
			return foundOne;
		} finally {
			env = previousEnv;
		}
	}

	private Object iteratorIsUnique(Collection<?> source, List<Variable> iterVars,
			OclExpression body) {
		Set<Object> seen = new HashSet<>();
		OclEvalEnvironment previousEnv = env;
		try {
			for (Object element : source) {
				env = previousEnv.nested(iterVars.get(0).getName(), element);
				Object bodyResult = eval(body);
				// OCL v2.4 §11.9.1: "Results in invalid if body evaluates to invalid"
				if (bodyResult == OclInvalid.INSTANCE) {
					return OclInvalid.INSTANCE;
				}
				if (!seen.add(bodyResult)) {
					return false; // duplicate
				}
			}
			return true;
		} finally {
			env = previousEnv;
		}
	}

	private Object iteratorSortedBy(Collection<?> source, List<Variable> iterVars,
			OclExpression body) {
		List<Object> elements = new ArrayList<>(source);
		// Pre-compute sort keys and check for invalid
		List<Object> keys = new ArrayList<>(elements.size());
		OclEvalEnvironment previousEnv = env;
		try {
			for (Object element : elements) {
				env = previousEnv.nested(iterVars.get(0).getName(), element);
				Object key = eval(body);
				if (key == OclInvalid.INSTANCE) {
					return OclInvalid.INSTANCE;
				}
				keys.add(key);
			}
			// Sort by pre-computed keys using cross-type numeric comparison
			Integer[] indices = new Integer[elements.size()];
			for (int i = 0; i < indices.length; i++) indices[i] = i;
			Arrays.sort(indices, (i, j) -> compareOcl(keys.get(i), keys.get(j)));
			// sortedBy on Set/OrderedSet → OclOrderedSet; on Sequence → Sequence
			List<Object> sorted;
			if (source instanceof Set<?> || source instanceof OclOrderedSet<?>) {
				sorted = new OclOrderedSet<>(elements.size());
			} else {
				sorted = new ArrayList<>(elements.size());
			}
			for (int idx : indices) sorted.add(elements.get(idx));
			return sorted;
		} finally {
			env = previousEnv;
		}
	}

	private static int compareOcl(Object a, Object b) {
		return OclCollectionUtil.compareOcl(a, b);
	}

	private Object iteratorClosure(Collection<?> source, List<Variable> iterVars,
			OclExpression body) {
		// §11.9.1: Result = OrderedSet if source is ordered, Set otherwise
		// OclBag extends ArrayList but is unordered — check before List
		boolean isOrdered = source instanceof List<?> && !(source instanceof OclBag<?>);
		Collection<Object> result = isOrdered
				? new OclOrderedSet<>()
				: new OclSet<>();
		List<Object> workList = new ArrayList<>(source);
		int iterations = 0;
		OclEvalEnvironment previousEnv = env;
		try {
			while (!workList.isEmpty()) {
				if (++iterations > options.maxClosureIterations()) {
					addError("Closure iteration limit exceeded: " + options.maxClosureIterations());
					return OclInvalid.INSTANCE;
				}
				Object element = workList.remove(0);
				if (!result.add(element)) {
					continue; // already visited
				}
				env = previousEnv.nested(iterVars.get(0).getName(), element);
				Object bodyResult = eval(body);
				if (bodyResult instanceof Collection<?> nested) {
					workList.addAll(nested);
				} else if (bodyResult != null && bodyResult != OclInvalid.INSTANCE) {
					workList.add(bodyResult);
				}
			}
			return result;
		} finally {
			env = previousEnv;
		}
	}

	// --- Message Expression (GAP-6) ---

	@Override
	public Object caseMessageExp(MessageExp exp) {
		return addError("MessageExp (^^ operator) is not yet supported — "
				+ "requires an OclMessageProvider extension point (see design decisions)");
	}

	// --- Fallback ---

	@Override
	public Object defaultCase(EObject object) {
		return addError("Unsupported expression type: " + object.eClass().getName());
	}

	// --- Internal helpers ---

	private Object[] evaluateArguments(List<OclExpression> argExps) {
		Object[] args = new Object[argExps.size()];
		for (int i = 0; i < argExps.size(); i++) {
			args[i] = eval(argExps.get(i));
		}
		return args;
	}

	private boolean isNullSafeOperation(String opName) {
		return switch (opName) {
			case "oclIsUndefined", "oclIsInvalid", "=", "<>",
				 "oclIsKindOf", "oclIsTypeOf", "oclAsType", "oclAsSet", "oclType", "toString" -> true;
			default -> false;
		};
	}

	private boolean isBooleanThreeValuedOp(String opName) {
		return switch (opName) {
			case "and", "or", "implies", "xor", "not" -> true;
			default -> false;
		};
	}

	/**
	 * Evaluates boolean operations with three-valued logic per OCL v2.4 §11.3.1.
	 * Handles short-circuit: {@code false and invalid = false},
	 * {@code true or invalid = true}, {@code false implies invalid = true}.
	 */
	private Object evaluateThreeValuedBoolean(String opName, Object source, Object[] args) {
		Boolean src = source instanceof Boolean b ? b : null;
		Boolean arg = args.length > 0 && args[0] instanceof Boolean b ? b : null;

		return switch (opName) {
			case "not" -> src != null ? !src : OclInvalid.INSTANCE;
			case "and" -> {
				if (Boolean.FALSE.equals(src) || Boolean.FALSE.equals(arg)) yield false;
				if (Boolean.TRUE.equals(src) && Boolean.TRUE.equals(arg)) yield true;
				yield OclInvalid.INSTANCE;
			}
			case "or" -> {
				if (Boolean.TRUE.equals(src) || Boolean.TRUE.equals(arg)) yield true;
				if (Boolean.FALSE.equals(src) && Boolean.FALSE.equals(arg)) yield false;
				yield OclInvalid.INSTANCE;
			}
			case "implies" -> {
				if (Boolean.FALSE.equals(src) || Boolean.TRUE.equals(arg)) yield true;
				if (Boolean.TRUE.equals(src) && Boolean.FALSE.equals(arg)) yield false;
				yield OclInvalid.INSTANCE;
			}
			case "xor" -> {
				if (src != null && arg != null) yield src ^ arg;
				yield OclInvalid.INSTANCE;
			}
			default -> OclInvalid.INSTANCE;
		};
	}

	/**
	 * Checks null/invalid source values and returns the appropriate result
	 * based on {@link OclEvaluationOptions#nullHandling()}.
	 *
	 * @return the error value to return, or {@code null} if evaluation should proceed
	 */
	private Object checkNullInvalid(Object source, String context) {
		if (source == OclInvalid.INSTANCE) {
			return OclInvalid.INSTANCE;
		}
		if (source == null) {
			if (options.nullHandling() == NullHandling.STRICT) {
				return addError("Null source for " + context);
			}
			// LENIENT: propagate null
			return null;
		}
		// Source is valid — return null to indicate "proceed with evaluation"
		return null;
	}

	private Object evaluateAllInstances(Object typeObj) {
		OclContext ctx = env.getContext();
		if (ctx == null || ctx.extent() == null) {
			return addError("allInstances() requires a model extent in the evaluation context");
		}
		EClassifier classifier = null;
		if (typeObj instanceof ClassifierType ct) {
			classifier = ct.getReferredClassifier();
		}
		if (classifier instanceof EClass eClass) {
			Collection<EObject> instances = ctx.extent().getAllInstances(eClass);
			if (instances.size() > options.maxCollectionSize()) {
				return addError("allInstances() result size " + instances.size()
						+ " exceeds maximum allowed size: " + options.maxCollectionSize());
			}
			return new OclSet<>(instances);
		}
		return addError("allInstances() requires an EClass type argument, got: " + typeObj);
	}

	private Object dispatchCustomOperation(String opName, Object source, Object[] args) {
		// §8.1.14.3: most-specific-type-first dispatch
		// Priority: exact match > widening (Integer→Real) > OclAny/AnyType catch-all
		// OCL §7.4.7: null (OclVoid) conforms to all types → first named match wins
		OclOperation fallback = null;
		OclOperation wideningMatch = null;
		for (OclOperationProvider provider : customProviders) {
			for (OclOperation op : provider.getOperations()) {
				if (!opName.equals(op.name())) {
					continue;
				}
				// Null source: OclVoid conforms to all → first matching op wins
				if (source == null) {
					return op.implementation().apply(source, args);
				}
				OclType ownerType = op.ownerType();
				// Exact primitive type match
				if (ownerType instanceof PrimitiveType pt) {
					String typeName = pt.getName();
					if (OclStdlib.matchesPrimitiveType(source, typeName)) {
						return op.implementation().apply(source, args);
					}
					// Widening: Integer→Real
					if ("Real".equals(typeName)
							&& (source instanceof Long || source instanceof Integer)) {
						wideningMatch = op;
						continue;
					}
					// OclAny catches all
					if ("OclAny".equals(typeName)) {
						fallback = op;
						continue;
					}
					continue;
				}
				// AnyType catches all
				if (ownerType instanceof AnyType) {
					fallback = op;
					continue;
				}
				// Other types: use isCompatibleOwner
				if (isCompatibleOwner(ownerType, source)) {
					return op.implementation().apply(source, args);
				}
			}
		}
		// Widening match before catch-all
		if (wideningMatch != null) {
			return wideningMatch.implementation().apply(source, args);
		}
		if (fallback != null) {
			return fallback.implementation().apply(source, args);
		}
		return OclStdlib.NOT_FOUND;
	}

	private static boolean isCompatibleOwner(OclType ownerType, Object source) {
		if (ownerType instanceof AnyType) {
			return true;
		}
		// OCL §7.4.7: OclVoid (null) conforms to all types
		if (source == null) {
			return true;
		}
		if (ownerType instanceof PrimitiveType pt) {
			String typeName = pt.getName();
			// §8.2.1.10: OclAny matches everything (catch-all dispatch)
			if ("OclAny".equals(typeName)) {
				return true;
			}
			// Exact type match first
			if (OclStdlib.matchesPrimitiveType(source, typeName)) {
				return true;
			}
			// OCL §11.5.1: Integer conforms to Real (widening)
			if ("Real".equals(typeName) && (source instanceof Long || source instanceof Integer)) {
				return true;
			}
			return false;
		}
		if (ownerType instanceof ClassifierType ct) {
			EClassifier ec = ct.getReferredClassifier();
			return ec != null && ec.isInstance(source);
		}
		if (ownerType instanceof CollectionType) {
			return source instanceof Collection<?>;
		}
		return true; // unknown type → allow (backward compat)
	}

	private Object addError(String message) {
		diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, SOURCE_ID, 0, message, null));
		return OclInvalid.INSTANCE;
	}

	/**
	 * Resolves a structural feature against the runtime EClass with 1-entry caching.
	 *
	 * <p>At parse time, feature references are resolved against the <em>static</em> type.
	 * At runtime, the object may be a different concrete type (e.g. after {@code any()},
	 * {@code first()}, or in iterator bodies). When the static feature doesn't belong
	 * to the runtime EClass, we fall back to name-based lookup.
	 *
	 * <p><b>1-entry cache optimization:</b> In iterator bodies ({@code select}, {@code collect},
	 * {@code forAll}, ...), the same feature is accessed on many objects of the <em>same</em>
	 * EClass — often hundreds or thousands of times. The cache stores the last
	 * (EClass, input feature) → output feature mapping, turning repeated lookups into
	 * two reference comparisons (~1 ns) instead of {@code getFeatureID()} (~5–10 ns each).
	 *
	 * <p>Example: {@code self.employees->collect(e | e.name)} with 1000 employees:
	 * <ul>
	 *   <li>Iteration 1: cache miss → {@code getFeatureID()} + resolve → cache filled</li>
	 *   <li>Iterations 2–1000: cache hit → two {@code ==} checks → return cached feature</li>
	 * </ul>
	 *
	 * <p>Uses {@code getFeatureID(sf)} which is O(1) for generated models (compiled
	 * feature ID constants) and O(n) only for dynamic models where n is the number
	 * of features.
	 *
	 * @param eo the runtime EObject
	 * @param sf the statically resolved feature from the AST
	 * @return the feature valid for {@code eo.eGet()}, possibly name-resolved
	 */
	private EStructuralFeature resolveFeature(EObject eo, EStructuralFeature sf) {
		EClass eClass = eo.eClass();

		// 1-entry cache: hit when same (EClass, feature) as last call
		if (eClass == lastResolvedClass && sf == lastResolvedInput) {
			return lastResolvedOutput;
		}

		// Cache miss — perform actual resolution
		EStructuralFeature resolved = sf;
		if (eClass.getFeatureID(sf) == -1) {
			EStructuralFeature byName = eClass.getEStructuralFeature(sf.getName());
			if (byName != null) {
				resolved = byName;
			}
		}

		// Update cache
		lastResolvedClass = eClass;
		lastResolvedInput = sf;
		lastResolvedOutput = resolved;

		return resolved;
	}

	/**
	 * Reads a property value using the accessor cache for generated models,
	 * falling back to eGet for dynamic models.
	 */
	private Object getProperty(EObject eo, EStructuralFeature sf) {
		PropertyAccessor accessor = accessorCache.getAccessor(eo, sf);
		if (accessor != null) {
			return accessor.get(eo);
		}
		return eo.eGet(sf);
	}

	/**
	 * Tries the property interceptor from the OclContext, if present.
	 *
	 * @return the intercepted value, or {@link OclContext#PROPERTY_NOT_HANDLED} if not intercepted
	 */
	private Object tryPropertyInterceptor(EObject target, String propName) {
		OclContext ctx = env.getContext();
		if (ctx != null && ctx.propertyInterceptor() != null) {
			return ctx.propertyInterceptor().apply(target, propName);
		}
		return OclContext.PROPERTY_NOT_HANDLED;
	}

	private static Collection<Object> preserveKind(Collection<?> source, List<Object> elements) {
		return OclCollectionUtil.preserveCollectionKind(source, elements);
	}

	/**
	 * Widens Integer to Long for internal consistency.
	 * EMF eGet() returns Integer for EInt attributes, but OCL
	 * operates on Long internally. This avoids Long/Integer
	 * mismatch in collection operations (includes, count, etc.).
	 */
	private static Object widenInteger(Object value) {
		if (value instanceof Integer i) {
			return (long) i;
		}
		return value;
	}

	/**
	 * Checks whether an AST node was created from an arrow call ({@code ->}).
	 * The parser marks these nodes with an {@code ArrowCallMarker} adapter.
	 */
	private static boolean isArrowCall(EObject exp) {
		return exp.eAdapters().stream()
				.anyMatch(a -> a.getClass().getSimpleName().equals("ArrowCallMarker"));
	}

	// --- oclLocale Support (OCL v2.4 §11.2.1) ---

	/**
	 * Resolves the prevailing OCL locale from the environment.
	 * Parses the {@code oclLocale} variable (format: {@code "language_country"})
	 * into a {@link Locale}. Falls back to {@link Locale#US} if not set or malformed.
	 */
	private Locale resolveOclLocale() {
		Object localeValue = env.lookup("oclLocale");
		if (localeValue instanceof String localeStr && !localeStr.isEmpty()) {
			return parseOclLocale(localeStr);
		}
		return Locale.US;
	}

	/**
	 * Parses an OCL locale string (e.g. {@code "en_us"}, {@code "fr_CA"}, {@code "de"})
	 * into a {@link Locale}.
	 */
	static Locale parseOclLocale(String localeStr) {
		String[] parts = localeStr.split("_");
		return switch (parts.length) {
			case 1 -> Locale.of(parts[0]);
			case 2 -> Locale.of(parts[0], parts[1]);
			default -> Locale.of(parts[0], parts[1], parts[2]);
		};
	}

	// --- Def-Property Support (Complete OCL def: constraints) ---

	/**
	 * Looks up a def-property entry for the given EClass and feature name,
	 * including supertypes.
	 */
	private DefRegistry.DefEntry lookupDefProperty(EClass eClass, String featureName) {
		DefRegistry.DefEntry entry = defProperties.get(new DefRegistry.DefKey(eClass, featureName));
		if (entry != null) {
			return entry;
		}
		for (EClass superType : eClass.getEAllSuperTypes()) {
			entry = defProperties.get(new DefRegistry.DefKey(superType, featureName));
			if (entry != null) {
				return entry;
			}
		}
		return null;
	}

	/**
	 * Evaluates a def-property body expression with {@code self} bound to the target object.
	 */
	private Object evaluateDefBody(OclExpression body, EObject target) {
		OclEvalEnvironment previousEnv = env;
		OclContext defContext = OclContext.of(target);
		env = OclEvalEnvironment.root(defContext);
		try {
			return eval(body);
		} finally {
			env = previousEnv;
		}
	}
}
