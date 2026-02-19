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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.model.ocl.BooleanLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.EnumLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.IfExp;
import org.eclipse.fennec.m2m.model.ocl.IntegerLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.InvalidLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.LetExp;
import org.eclipse.fennec.m2m.model.ocl.NullLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.model.ocl.RealLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.StringLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.UnlimitedNaturalLiteralExp;
import org.eclipse.fennec.m2m.model.ocl.Variable;
import org.eclipse.fennec.m2m.model.ocl.VariableExp;
import org.eclipse.fennec.m2m.model.ocl.util.OclSwitch;
import org.eclipse.fennec.m2m.ocl.api.OclContext;
import org.eclipse.fennec.m2m.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2m.ocl.api.OclInvalid;
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
	private final List<Diagnostic> diagnostics = new ArrayList<>();
	private OclEvalEnvironment env;

	/**
	 * Creates a new evaluator.
	 *
	 * @param env the root evaluation environment
	 * @param options evaluation options
	 */
	OclEvaluator(OclEvalEnvironment env, OclEvaluationOptions options) {
		this.env = env;
		this.options = options;
	}

	/**
	 * Evaluates an expression and returns the result with diagnostics.
	 *
	 * @param expression the parsed OCL expression
	 * @param context the evaluation context
	 * @param options evaluation options
	 * @return the evaluation result with diagnostics
	 */
	public static OclResult evaluate(OclExpression expression, OclContext context,
			OclEvaluationOptions options) {
		OclEvalEnvironment env = OclEvalEnvironment.root(context);
		OclEvaluator evaluator = new OclEvaluator(env, options);
		Object result = evaluator.doSwitch(expression);
		return new OclResult(result, evaluator.diagnostics);
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

	// --- Fallback ---

	@Override
	public Object defaultCase(EObject object) {
		return addError("Unsupported expression type: " + object.eClass().getName());
	}

	// --- Diagnostics ---

	private Object addError(String message) {
		diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, SOURCE_ID, 0, message, null));
		return OclInvalid.INSTANCE;
	}
}
