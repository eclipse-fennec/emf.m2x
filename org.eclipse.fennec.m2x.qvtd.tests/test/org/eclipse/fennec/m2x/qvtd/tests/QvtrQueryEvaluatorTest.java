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
package org.eclipse.fennec.m2x.qvtd.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.model.ocl.StringLiteralExp;
import org.eclipse.fennec.m2x.model.qvtbase.Function;
import org.eclipse.fennec.m2x.model.qvtbase.QvtbaseFactory;
import org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationFactory;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.qvtd.api.BasicQvtdBlackboxRegistry;
import org.eclipse.fennec.m2x.qvtd.api.QvtdBlackboxLibrary;
import org.eclipse.fennec.m2x.qvtd.engine.internal.QvtrBlackboxBridge;
import org.eclipse.fennec.m2x.qvtd.engine.internal.QvtrQueryEvaluator;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link QvtrQueryEvaluator}.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtrQueryEvaluatorTest {

	// ── resolveQuery ─────────────────────────────────────────────────

	@Test
	void resolveQuery_existingQuery_returnsFunction() {
		RelationalTransformation t = createTransformationWithQuery("TypeMap");
		QvtrQueryEvaluator evaluator = createEvaluator(t);

		Function result = evaluator.resolveQuery("TypeMap");
		assertNotNull(result, "Should find query by name");
		assertEquals("TypeMap", result.getName());
	}

	@Test
	void resolveQuery_unknownName_returnsNull() {
		RelationalTransformation t = createTransformationWithQuery("TypeMap");
		QvtrQueryEvaluator evaluator = createEvaluator(t);

		assertNull(evaluator.resolveQuery("Unknown"));
	}

	@Test
	void resolveQuery_nullName_returnsNull() {
		RelationalTransformation t = createTransformationWithQuery("TypeMap");
		QvtrQueryEvaluator evaluator = createEvaluator(t);

		assertNull(evaluator.resolveQuery(null));
	}

	// ── evaluateQueryCall ────────────────────────────────────────────

	@Test
	void evaluateQueryCall_withBody_evaluatesBody() {
		// Query with body: returns what the OCL callback evaluates
		Function query = QvtbaseFactory.eINSTANCE.createFunction();
		query.setName("computeName");

		StringLiteralExp body = OclFactory.eINSTANCE.createStringLiteralExp();
		body.setStringSymbol("computed");
		query.setQueryExpression(body);

		OperationCallExp callExpr = OclFactory.eINSTANCE.createOperationCallExp();
		callExpr.setName("computeName");

		// OCL callback returns the string symbol directly
		QvtrQueryEvaluator evaluator = new QvtrQueryEvaluator(
				QvtrelationFactory.eINSTANCE.createRelationalTransformation(),
				(expr, bindings) -> {
					if (expr instanceof StringLiteralExp sle) {
						return sle.getStringSymbol();
					}
					return null;
				},
				null); // no blackbox bridge needed

		Object result = evaluator.evaluateQueryCall(query, callExpr, Map.of());
		assertEquals("computed", result);
	}

	@Test
	void evaluateQueryCall_withoutBody_delegatesToBlackbox() {
		// Query without body = blackbox query
		Function query = QvtbaseFactory.eINSTANCE.createFunction();
		query.setName("TypeMap");
		// No queryExpression → blackbox

		OperationCallExp callExpr = OclFactory.eINSTANCE.createOperationCallExp();
		callExpr.setName("TypeMap");

		// Real blackbox bridge with enabled blackbox + real registry
		BasicQvtdBlackboxRegistry registry = new BasicQvtdBlackboxRegistry();
		registry.register(new QvtdBlackboxLibrary() {
			@Override
			public String getModuleName() { return "helpers"; }
			@Override
			public String getUnitQualifiedName() { return "helpers"; }
			@Override
			public java.util.List<String> getUsedPackageURIs() { return java.util.List.of(); }
			@Override
			public Object invoke(String operationName, Object self, Object[] args) {
				return "TypeMap".equals(operationName) ? "BB_RESULT" : null;
			}
		});

		QvtrBlackboxBridge bridge = new QvtrBlackboxBridge(
				registry, createConfig(true), List.of(),
				null, null, new ArrayList<>(), (expr, bindings) -> null);

		QvtrQueryEvaluator evaluator = new QvtrQueryEvaluator(
				QvtrelationFactory.eINSTANCE.createRelationalTransformation(),
				(expr, bindings) -> null,
				bridge);

		Object result = evaluator.evaluateQueryCall(query, callExpr, Map.of());
		assertEquals("BB_RESULT", result);
	}

	// ── evaluateExprWithQueries ──────────────────────────────────────

	@Test
	void evaluateExprWithQueries_nonQueryExpr_delegatesToOcl() {
		RelationalTransformation t = QvtrelationFactory.eINSTANCE.createRelationalTransformation();
		StringLiteralExp literal = OclFactory.eINSTANCE.createStringLiteralExp();
		literal.setStringSymbol("hello");

		QvtrQueryEvaluator evaluator = new QvtrQueryEvaluator(t,
				(expr, bindings) -> "OCL_RESULT",
				null);

		Object result = evaluator.evaluateExprWithQueries(literal, Map.of());
		assertEquals("OCL_RESULT", result);
	}

	@Test
	void evaluateExprWithQueries_queryCall_resolvesAndEvaluates() {
		RelationalTransformation t = createTransformationWithQueryAndBody("Helper", "resolved_value");
		QvtrQueryEvaluator evaluator = new QvtrQueryEvaluator(t,
				(expr, bindings) -> {
					if (expr instanceof StringLiteralExp sle) {
						return sle.getStringSymbol();
					}
					return null;
				},
				null);

		OperationCallExp opCall = OclFactory.eINSTANCE.createOperationCallExp();
		opCall.setName("Helper");

		Object result = evaluator.evaluateExprWithQueries(opCall, Map.of());
		assertEquals("resolved_value", result);
	}

	// ── preComputeWhereBindings ──────────────────────────────────────

	@Test
	void preComputeWhereBindings_noWhereClause_doesNothing() {
		RelationalTransformation t = QvtrelationFactory.eINSTANCE.createRelationalTransformation();
		QvtrQueryEvaluator evaluator = createEvaluator(t);

		var relation = QvtrelationFactory.eINSTANCE.createRelation();
		// No where clause
		Map<String, Object> bindings = new HashMap<>();
		evaluator.preComputeWhereBindings(relation, bindings);
		assertTrue(bindings.isEmpty());
	}

	// ── Helpers ──────────────────────────────────────────────────────

	private QvtrQueryEvaluator createEvaluator(RelationalTransformation t) {
		return new QvtrQueryEvaluator(t, (expr, bindings) -> null, null);
	}

	private RelationalTransformation createTransformationWithQuery(String queryName) {
		RelationalTransformation t = QvtrelationFactory.eINSTANCE.createRelationalTransformation();

		EClass queriesClass = EcoreFactory.eINSTANCE.createEClass();
		queriesClass.setName("_queries");

		Function func = QvtbaseFactory.eINSTANCE.createFunction();
		func.setName(queryName);
		queriesClass.getEOperations().add(func);

		t.getEClassifiers().add(queriesClass);
		return t;
	}

	private RelationalTransformation createTransformationWithQueryAndBody(
			String queryName, String bodyValue) {
		RelationalTransformation t = QvtrelationFactory.eINSTANCE.createRelationalTransformation();

		EClass queriesClass = EcoreFactory.eINSTANCE.createEClass();
		queriesClass.setName("_queries");

		Function func = QvtbaseFactory.eINSTANCE.createFunction();
		func.setName(queryName);
		StringLiteralExp body = OclFactory.eINSTANCE.createStringLiteralExp();
		body.setStringSymbol(bodyValue);
		func.setQueryExpression(body);
		queriesClass.getEOperations().add(func);

		t.getEClassifiers().add(queriesClass);
		return t;
	}

	private org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration createConfig(boolean blackboxEnabled) {
		return org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration.builder(
				org.eclipse.fennec.m2x.ocl.api.OclConfiguration.builder(
						new org.eclipse.fennec.m2x.ocl.parser.OclParserSupport()).build())
				.blackboxEnabled(blackboxEnabled)
				.build();
	}

	private static void assertTrue(boolean condition) {
		org.junit.jupiter.api.Assertions.assertTrue(condition);
	}
}
