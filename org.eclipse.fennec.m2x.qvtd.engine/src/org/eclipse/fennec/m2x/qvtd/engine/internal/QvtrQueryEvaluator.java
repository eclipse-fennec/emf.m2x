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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.model.qvtbase.Function;
import org.eclipse.fennec.m2x.model.qvtbase.Pattern;
import org.eclipse.fennec.m2x.model.qvtbase.Predicate;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationCallExp;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;

/**
 * Evaluates QVT-R query functions (§7.11.4) and pre-computes where-clause
 * variable bindings.
 *
 * <p>Extracted from {@link QvtrEvaluator} for testability. Handles query
 * resolution from the transformation's synthetic {@code _queries} EClass,
 * query body evaluation, and blackbox query delegation.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtrQueryEvaluator {

	private final RelationalTransformation transformation;
	private final QvtrOclCallback oclCallback;
	private final QvtrBlackboxBridge blackboxBridge;

	public QvtrQueryEvaluator(RelationalTransformation transformation,
			QvtrOclCallback oclCallback, QvtrBlackboxBridge blackboxBridge) {
		this.transformation = transformation;
		this.oclCallback = oclCallback;
		this.blackboxBridge = blackboxBridge;
	}

	/**
	 * Pre-computes where-clause variable bindings that can be resolved before
	 * enforcement. Handles predicates of the form {@code var = expr} where
	 * {@code var} is unbound and {@code expr} can be evaluated (including
	 * query calls, §7.11.4).
	 */
	public void preComputeWhereBindings(Relation relation, Map<String, Object> bindings) {
		Pattern where = relation.getWhere();
		if (where == null) {
			return;
		}

		for (Predicate predicate : where.getPredicate()) {
			OclExpression expr = predicate.getConditionExpression();
			if (expr instanceof RelationCallExp) {
				continue; // Handled in evaluateWhereClause
			}
			if (!(expr instanceof OperationCallExp eqOp) || !"=".equals(eqOp.getName())) {
				continue;
			}

			// Check if left side (source) is an unbound VariableExp
			OclExpression source = eqOp.getOwnedSource();
			if (source instanceof VariableExp varExp) {
				Variable ref = varExp.getReferredVariable();
				if (ref != null && !bindings.containsKey(ref.getName())
						&& !eqOp.getOwnedArguments().isEmpty()) {
					Object value = evaluateExprWithQueries(
							eqOp.getOwnedArguments().get(0), bindings);
					if (value != null) {
						bindings.put(ref.getName(), value);
					}
					continue;
				}
			}

			// Check if right side (argument) is an unbound VariableExp
			if (!eqOp.getOwnedArguments().isEmpty()) {
				OclExpression arg = eqOp.getOwnedArguments().get(0);
				if (arg instanceof VariableExp varExp) {
					Variable ref = varExp.getReferredVariable();
					if (ref != null && !bindings.containsKey(ref.getName())) {
						Object value = evaluateExprWithQueries(source, bindings);
						if (value != null) {
							bindings.put(ref.getName(), value);
						}
					}
				}
			}
		}
	}

	/**
	 * Evaluates an expression, resolving query calls (§7.11.4) from the
	 * transformation's Function definitions.
	 */
	public Object evaluateExprWithQueries(OclExpression expr, Map<String, Object> bindings) {
		if (expr instanceof OperationCallExp opCall) {
			Function query = resolveQuery(opCall.getName());
			if (query != null) {
				return evaluateQueryCall(query, opCall, bindings);
			}
		}
		return oclCallback.evaluate(expr, bindings);
	}

	/**
	 * Resolves a query Function by name from the transformation's synthetic
	 * {@code _queries} EClass.
	 */
	public Function resolveQuery(String name) {
		if (name == null) {
			return null;
		}
		for (EClassifier classifier : transformation.getEClassifiers()) {
			if ("_queries".equals(classifier.getName()) && classifier instanceof EClass queriesClass) {
				for (EOperation op : queriesClass.getEOperations()) {
					if (op instanceof Function f && name.equals(f.getName())) {
						return f;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Evaluates a query Function call (§7.11.4). Binds the query's parameters
	 * to the argument values and evaluates the queryExpression.
	 */
	public Object evaluateQueryCall(Function query, OperationCallExp callExpr,
			Map<String, Object> callerBindings) {
		OclExpression queryBody = query.getQueryExpression();
		if (queryBody == null) {
			// Blackbox query (§7.8): delegate to registered blackbox library
			return blackboxBridge.evaluateBlackboxQuery(query, callExpr, callerBindings);
		}

		// Build parameter bindings
		Map<String, Object> paramBindings = new HashMap<>();
		EList<EParameter> params = query.getEParameters();
		List<OclExpression> args = callExpr.getOwnedArguments();
		for (int i = 0; i < Math.min(params.size(), args.size()); i++) {
			Object argValue = oclCallback.evaluate(args.get(i), callerBindings);
			paramBindings.put(params.get(i).getName(), argValue);
		}

		// Evaluate query body with parameter bindings
		return oclCallback.evaluate(queryBody, paramBindings);
	}
}
