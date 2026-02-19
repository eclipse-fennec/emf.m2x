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
package org.eclipse.fennec.m2m.ocl.engine.internal;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.fennec.m2m.model.ocl.BooleanLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.CollectionItem;
import org.eclipse.fennec.m2m.model.ocl.CollectionKind;
import org.eclipse.fennec.m2m.model.ocl.CollectionLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.CollectionLiteralPart;
import org.eclipse.fennec.m2m.model.ocl.CollectionRange;
import org.eclipse.fennec.m2m.model.ocl.EnumLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.IfExp;
import org.eclipse.fennec.m2m.model.ocl.IntegerLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.InvalidLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.IterateExp;
import org.eclipse.fennec.m2m.model.ocl.IteratorExp;
import org.eclipse.fennec.m2m.model.ocl.LetExp;
import org.eclipse.fennec.m2m.model.ocl.MapLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.MapLiteralPart;
import org.eclipse.fennec.m2m.model.ocl.NullLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2m.model.ocl.PropertyCallExp;
import org.eclipse.fennec.m2m.model.ocl.RealLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.StringLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.TupleLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.TupleLiteralPart;
import org.eclipse.fennec.m2m.model.ocl.TypeExp;
import org.eclipse.fennec.m2m.model.ocl.UnlimitedNaturalLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.Variable;
import org.eclipse.fennec.m2m.model.ocl.VariableExp;
import org.eclipse.fennec.m2m.model.ocl.util.OclSwitch;
import org.eclipse.fennec.m2m.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2m.ocl.api.OclEvaluationOptions.NullHandling;
import org.eclipse.fennec.m2m.ocl.api.OclInvalid;
import org.eclipse.fennec.m2m.ocl.api.OclOperation;
import org.eclipse.fennec.m2m.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2m.ocl.api.OclResult;

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

	private static final String SOURCE_ID = "org.eclipse.fennec.m2m.ocl.engine";

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
	private final List<Diagnostic> diagnostics = new ArrayList<>();
	private OclEvalEnvironment env;

	/**
	 * Creates a new evaluator.
	 *
	 * @param env the root evaluation environment
	 * @param options evaluation options
	 * @param customProviders registered custom operation providers
	 */
	public OclEvaluator(OclEvalEnvironment env, OclEvaluationOptions options,
			List<OclOperationProvider> customProviders) {
		this.env = env;
		this.options = options;
		this.customProviders = customProviders;
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
		Object result = doSwitch(expression);
		return result == OCL_NULL ? null : result;
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
		return exp.getUnlimitedNaturalSymbol();
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

		// Tuple part access (tuples are represented as Map<String, Object>)
		if (source instanceof Map<?, ?> map) {
			Object value = map.get(sf.getName());
			return value == null ? OCL_NULL : value;
		}

		if (!(source instanceof EObject eo)) {
			return addError("Property access requires an EObject or Tuple, got: "
					+ source.getClass().getSimpleName());
		}

		Object value = eo.eGet(sf);
		return value == null ? OCL_NULL : value;
	}

	// --- Operation Call ---

	@Override
	public Object caseOperationCallExp(OperationCallExp exp) {
		Object source = eval(exp.getOwnedSource());
		String opName = exp.getName();

		// Safe navigation
		if (source == null && exp.isIsSafe()) {
			return OCL_NULL;
		}

		// OclAny null/invalid-safe operations (must work on null/invalid source)
		if (isNullSafeOperation(opName)) {
			Object[] args = evaluateArguments(exp.getOwnedArguments());
			return wrapNull(OclStdlib.dispatch(opName, source, args));
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

		// 1. Try Ecore model operation (referredOperation set by parser)
		if (exp.getReferredOperation() != null && source instanceof EObject eo) {
			try {
				EList<Object> eArgs = args.length > 0
						? new BasicEList<>(List.of(args))
						: ECollections.emptyEList();
				return wrapNull(eo.eInvoke(exp.getReferredOperation(), eArgs));
			} catch (InvocationTargetException e) {
				return addError("Operation invocation failed: " + opName + " - " + e.getCause().getMessage());
			}
		}

		// 2. Try standard library
		Object result = OclStdlib.dispatch(opName, source, args);
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
					return addError("Collection range requires Integer bounds");
				}
				for (long i = f; i <= l; i++) {
					elements.add(i);
				}
			}
		}

		return switch (kind) {
			case SET -> new LinkedHashSet<>(elements);
			case ORDERED_SET -> new LinkedHashSet<>(elements);
			case SEQUENCE -> new ArrayList<>(elements);
			case BAG -> new ArrayList<>(elements);
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
		Object nullCheck = checkNullInvalid(source, "iterator '" + exp.getName() + "'");
		if (nullCheck != null) {
			return nullCheck;
		}
		if (!(source instanceof Collection<?> coll)) {
			return addError("Iterator source must be a Collection, got: "
					+ source.getClass().getSimpleName());
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
		Object nullCheck = checkNullInvalid(source, "iterate");
		if (nullCheck != null) {
			return nullCheck;
		}
		if (!(source instanceof Collection<?> coll)) {
			return addError("Iterate source must be a Collection, got: "
					+ source.getClass().getSimpleName());
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
				if (Boolean.TRUE.equals(bodyResult)) {
					result.add(element);
				}
			}
			return source instanceof Set<?> ? new LinkedHashSet<>(result) : result;
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
				if (!Boolean.TRUE.equals(bodyResult)) {
					result.add(element);
				}
			}
			return source instanceof Set<?> ? new LinkedHashSet<>(result) : result;
		} finally {
			env = previousEnv;
		}
	}

	private Object iteratorCollect(Collection<?> source, List<Variable> iterVars,
			OclExpression body) {
		List<Object> result = new ArrayList<>();
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
			return result; // collect always yields a Bag (List)
		} finally {
			env = previousEnv;
		}
	}

	private Object iteratorCollectNested(Collection<?> source, List<Variable> iterVars,
			OclExpression body) {
		List<Object> result = new ArrayList<>();
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
		OclEvalEnvironment previousEnv = env;
		try {
			for (Object element : source) {
				env = previousEnv.nested(iterVars.get(0).getName(), element);
				Object bodyResult = eval(body);
				if (!Boolean.TRUE.equals(bodyResult)) {
					return false; // short-circuit
				}
			}
			return true;
		} finally {
			env = previousEnv;
		}
	}

	private Object iteratorExists(Collection<?> source, List<Variable> iterVars,
			OclExpression body) {
		OclEvalEnvironment previousEnv = env;
		try {
			for (Object element : source) {
				env = previousEnv.nested(iterVars.get(0).getName(), element);
				Object bodyResult = eval(body);
				if (Boolean.TRUE.equals(bodyResult)) {
					return true; // short-circuit
				}
			}
			return false;
		} finally {
			env = previousEnv;
		}
	}

	private Object iteratorAny(Collection<?> source, List<Variable> iterVars,
			OclExpression body) {
		OclEvalEnvironment previousEnv = env;
		try {
			for (Object element : source) {
				env = previousEnv.nested(iterVars.get(0).getName(), element);
				Object bodyResult = eval(body);
				if (Boolean.TRUE.equals(bodyResult)) {
					return element;
				}
			}
			return null; // OclVoid — no element matched
		} finally {
			env = previousEnv;
		}
	}

	private Object iteratorOne(Collection<?> source, List<Variable> iterVars,
			OclExpression body) {
		boolean foundOne = false;
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
				}
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
				if (!seen.add(bodyResult)) {
					return false; // duplicate
				}
			}
			return true;
		} finally {
			env = previousEnv;
		}
	}

	@SuppressWarnings("unchecked")
	private Object iteratorSortedBy(Collection<?> source, List<Variable> iterVars,
			OclExpression body) {
		List<Object> elements = new ArrayList<>(source);
		OclEvalEnvironment previousEnv = env;
		try {
			elements.sort((a, b) -> {
				env = previousEnv.nested(iterVars.get(0).getName(), a);
				Object keyA = eval(body);
				env = previousEnv.nested(iterVars.get(0).getName(), b);
				Object keyB = eval(body);
				if (keyA instanceof Comparable ca && keyB instanceof Comparable) {
					return ca.compareTo(keyB);
				}
				return 0;
			});
			return elements; // sortedBy yields a Sequence
		} finally {
			env = previousEnv;
		}
	}

	private Object iteratorClosure(Collection<?> source, List<Variable> iterVars,
			OclExpression body) {
		Set<Object> result = new LinkedHashSet<>();
		List<Object> workList = new ArrayList<>(source);
		OclEvalEnvironment previousEnv = env;
		try {
			while (!workList.isEmpty()) {
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
				 "oclIsKindOf", "oclIsTypeOf", "oclAsType", "oclAsSet", "toString" -> true;
			default -> false;
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
		// Source is valid — return a non-null sentinel to indicate "proceed"
		return null;
	}

	private Object dispatchCustomOperation(String opName, Object source, Object[] args) {
		for (OclOperationProvider provider : customProviders) {
			for (OclOperation op : provider.getOperations()) {
				if (opName.equals(op.name())) {
					return op.implementation().apply(source, args);
				}
			}
		}
		return OclStdlib.NOT_FOUND;
	}

	private Object addError(String message) {
		diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, SOURCE_ID, 0, message, null));
		return OclInvalid.INSTANCE;
	}
}
