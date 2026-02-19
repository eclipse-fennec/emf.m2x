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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

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
import org.eclipse.fennec.m2m.ocl.api.OclContext;
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
 * its child expressions recursively via {@link #doSwitch(EObject)}.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class OclEvaluator extends OclSwitch<Object> {

	private static final String SOURCE_ID = "org.eclipse.fennec.m2m.ocl.engine";

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
		Object result = doSwitch(expression);
		return new OclResult(result, diagnostics);
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
		return null;
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
			return env.lookup(name);
		}
		return addError("Unresolved variable: " + name);
	}

	@Override
	public Object caseIfExp(IfExp exp) {
		Object condition = doSwitch(exp.getOwnedCondition());
		if (condition == OclInvalid.INSTANCE) {
			return OclInvalid.INSTANCE;
		}
		if (!(condition instanceof Boolean)) {
			return addError("if-condition must be Boolean, got: "
					+ (condition == null ? "null" : condition.getClass().getSimpleName()));
		}
		return (Boolean) condition
				? doSwitch(exp.getOwnedThen())
				: doSwitch(exp.getOwnedElse());
	}

	@Override
	public Object caseLetExp(LetExp exp) {
		Variable variable = exp.getOwnedVariable();
		Object value = null;
		if (variable.getOwnedInit() != null) {
			value = doSwitch(variable.getOwnedInit());
		}
		OclEvalEnvironment previousEnv = env;
		try {
			env = env.nested(variable.getName(), value);
			return doSwitch(exp.getOwnedIn());
		} finally {
			env = previousEnv;
		}
	}

	// --- Property Access ---

	@Override
	public Object casePropertyCallExp(PropertyCallExp exp) {
		Object source = doSwitch(exp.getOwnedSource());

		// Safe navigation
		if (source == null && exp.isIsSafe()) {
			return null;
		}

		// Null/Invalid handling
		Object nullCheck = checkNullInvalid(source, "property '" + exp.getReferredProperty().getName() + "'");
		if (nullCheck != null) {
			return nullCheck;
		}

		if (!(source instanceof EObject eo)) {
			return addError("Property access requires an EObject, got: "
					+ source.getClass().getSimpleName());
		}

		EStructuralFeature sf = exp.getReferredProperty();
		return eo.eGet(sf);
	}

	// --- Operation Call ---

	@Override
	public Object caseOperationCallExp(OperationCallExp exp) {
		Object source = doSwitch(exp.getOwnedSource());
		String opName = exp.getName();

		// Safe navigation
		if (source == null && exp.isIsSafe()) {
			return null;
		}

		// OclAny null/invalid-safe operations (must work on null/invalid source)
		if (isNullSafeOperation(opName)) {
			Object[] args = evaluateArguments(exp.getOwnedArguments());
			return OclStdlib.dispatch(opName, source, args);
		}

		// Null/Invalid handling for other operations
		Object nullCheck = checkNullInvalid(source, "operation '" + opName + "'");
		if (nullCheck != null) {
			return nullCheck;
		}

		// Evaluate arguments
		Object[] args = evaluateArguments(exp.getOwnedArguments());

		// 1. Try Ecore model operation (referredOperation set by parser)
		if (exp.getReferredOperation() != null && source instanceof EObject eo) {
			try {
				EList<Object> eArgs = args.length > 0
						? new BasicEList<>(List.of(args))
						: ECollections.emptyEList();
				return eo.eInvoke(exp.getReferredOperation(), eArgs);
			} catch (InvocationTargetException e) {
				return addError("Operation invocation failed: " + opName + " - " + e.getCause().getMessage());
			}
		}

		// 2. Try standard library
		Object result = OclStdlib.dispatch(opName, source, args);
		if (result != OclStdlib.NOT_FOUND) {
			return result;
		}

		// 3. Try custom operation providers
		result = dispatchCustomOperation(opName, source, args);
		if (result != OclStdlib.NOT_FOUND) {
			return result;
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
				elements.add(doSwitch(item.getOwnedItem()));
			} else if (part instanceof CollectionRange range) {
				Object first = doSwitch(range.getOwnedFirst());
				Object last = doSwitch(range.getOwnedLast());
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
				value = doSwitch(part.getOwnedInit());
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
			Object key = doSwitch(part.getOwnedKey());
			Object value = doSwitch(part.getOwnedValue());
			map.put(key, value);
		}
		return map;
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
			args[i] = doSwitch(argExps.get(i));
		}
		return args;
	}

	private boolean isNullSafeOperation(String opName) {
		return switch (opName) {
			case "oclIsUndefined", "oclIsInvalid", "=", "<>" -> true;
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
