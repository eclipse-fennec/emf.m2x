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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.fennec.m2x.model.ocl.AnyType;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.qvtbase.Function;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclOperation;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;

/**
 * Hands the OCL engine what a QVT-R transformation defines: its queries (§7.11.4) and, where a
 * query has no body, the blackbox implementations behind them (§7.8).
 *
 * <p>Without this, OCL cannot resolve a query it meets <em>inside</em> an expression — the
 * top-level interception in {@link QvtrQueryEvaluator} never sees a nested call — and reports
 * {@code Unknown operation} for something the transformation defines perfectly well. That false
 * error is why the evaluator could not forward OCL's diagnostics (#118): adopting them would have
 * failed working transformations.
 *
 * <p>QVT-O has had this shape from the start ({@code QvtoOperationProvider}); this is the QVT-R
 * counterpart, deliberately small. Queries take no receiver, so every operation is owned by
 * {@code AnyType} and dispatches on name and arity.
 *
 * @since 1.0
 */
class QvtrOperationProvider implements OclOperationProvider {

	private final RelationalTransformation transformation;
	private final QvtrBlackboxBridge blackboxBridge;
	private final BiFunction<OclExpression, Map<String, Object>, Object> evaluator;

	/**
	 * @param transformation the transformation whose queries become operations
	 * @param blackboxBridge answers for body-less queries, carrying the configuration's gates and
	 *                       allow-list — one place for those rules, not two
	 * @param evaluator      evaluates a query body with its parameter bindings — the evaluator's
	 *                       own OCL path, so nested queries recurse naturally
	 */
	QvtrOperationProvider(RelationalTransformation transformation,
			QvtrBlackboxBridge blackboxBridge,
			BiFunction<OclExpression, Map<String, Object>, Object> evaluator) {
		this.transformation = Objects.requireNonNull(transformation, "transformation must not be null");
		this.blackboxBridge = blackboxBridge;
		this.evaluator = Objects.requireNonNull(evaluator, "evaluator must not be null");
	}

	@Override
	public List<OclOperation> getOperations() {
		List<OclOperation> operations = new ArrayList<>();
		AnyType any = OclStandardLibrary.INSTANCE.oclAny();
		for (EClassifier classifier : transformation.getEClassifiers()) {
			if (!"_queries".equals(classifier.getName()) || !(classifier instanceof EClass queries)) {
				continue;
			}
			for (EOperation operation : queries.getEOperations()) {
				if (operation instanceof Function query) {
					operations.add(toOperation(query, any));
				}
			}
		}
		return operations;
	}

	private OclOperation toOperation(Function query, AnyType any) {
		List<EClassifier> parameterTypes =
				new ArrayList<>(Collections.nCopies(query.getEParameters().size(),
						(EClassifier) any));
		return new OclOperation(query.getName(), any, parameterTypes, any,
				(self, args) -> invoke(query, args));
	}

	private Object invoke(Function query, Object[] args) {
		OclExpression body = query.getQueryExpression();
		if (body != null) {
			Map<String, Object> bindings = new HashMap<>();
			List<EParameter> parameters = query.getEParameters();
			for (int i = 0; i < Math.min(parameters.size(), args.length); i++) {
				bindings.put(parameters.get(i).getName(), args[i]);
			}
			return evaluator.apply(body, bindings);
		}
		// Body-less query: the bridge answers, behind its own gates and allow-list (§7.8). It
		// wants the call expression to evaluate arguments itself, so the already-evaluated values
		// are wrapped as bindings of a synthetic call — see evaluateBlackboxQuery.
		if (blackboxBridge == null) {
			return null;
		}
		OperationCallExp call = OclFactory.eINSTANCE.createOperationCallExp();
		call.setName(query.getName());
		Map<String, Object> bindings = new HashMap<>();
		List<EParameter> parameters = query.getEParameters();
		for (int i = 0; i < Math.min(parameters.size(), args.length); i++) {
			org.eclipse.fennec.m2x.model.ocl.Variable variable =
					OclFactory.eINSTANCE.createVariable();
			variable.setName(parameters.get(i).getName());
			org.eclipse.fennec.m2x.model.ocl.VariableExp variableExp =
					OclFactory.eINSTANCE.createVariableExp();
			variableExp.setReferredVariable(variable);
			call.getOwnedArguments().add(variableExp);
			bindings.put(parameters.get(i).getName(), args[i]);
		}
		return blackboxBridge.evaluateBlackboxQuery(query, call, bindings);
	}
}
