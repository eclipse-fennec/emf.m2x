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
package org.eclipse.fennec.m2m.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.ocl.api.OclContext;
import org.eclipse.fennec.m2m.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2m.ocl.api.OclEvaluationOptions.ErrorRecovery;
import org.eclipse.fennec.m2m.ocl.api.OclEvaluationOptions.NullHandling;
import org.eclipse.fennec.m2m.ocl.api.OclInvalid;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.eclipse.fennec.m2m.ocl.api.OclResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OclEvaluationOptions} — strict vs lenient null handling,
 * {@link OclResult} diagnostics, and option factory methods.
 */
class OclEvaluationOptionsTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- OclEvaluationOptions factory methods ---

	@Test
	void strict_defaults() {
		OclEvaluationOptions opts = OclEvaluationOptions.strict();
		assertEquals(NullHandling.STRICT, opts.nullHandling());
		assertEquals(ErrorRecovery.FAIL_FAST, opts.errorRecovery());
		assertEquals(1000, opts.maxDepth());
	}

	@Test
	void lenient_defaults() {
		OclEvaluationOptions opts = OclEvaluationOptions.lenient();
		assertEquals(NullHandling.LENIENT, opts.nullHandling());
		assertEquals(ErrorRecovery.COLLECT_ERRORS, opts.errorRecovery());
		assertEquals(1000, opts.maxDepth());
	}

	// --- evaluateWithDiagnostics basic usage ---

	@Test
	void evaluateWithDiagnostics_success() throws OclParseException {
		OclExpression expr = engine.parse("self.name", personClass);
		OclResult result = engine.evaluateWithDiagnostics(expr, OclContext.of(self),
				OclEvaluationOptions.strict());
		assertTrue(result.isSuccess());
		assertFalse(result.isInvalid());
		assertFalse(result.isNull());
		assertTrue(result.hasValue());
		assertEquals("Alice", result.value());
	}

	@Test
	void evaluateWithDiagnostics_getValueAs() throws OclParseException {
		OclExpression expr = engine.parse("self.name", personClass);
		OclResult result = engine.evaluateWithDiagnostics(expr, OclContext.of(self),
				OclEvaluationOptions.strict());
		assertEquals("Alice", result.getValueAs(String.class));
	}

	@Test
	void evaluateWithDiagnostics_integerResult() throws OclParseException {
		OclExpression expr = engine.parse("self.age", personClass);
		OclResult result = engine.evaluateWithDiagnostics(expr, OclContext.of(self),
				OclEvaluationOptions.strict());
		assertTrue(result.isSuccess());
		assertTrue(result.hasValue());
		assertInstanceOf(Number.class, result.value());
	}

	@Test
	void evaluateWithDiagnostics_booleanResult() throws OclParseException {
		OclExpression expr = engine.parse("self.isMarried", personClass);
		OclResult result = engine.evaluateWithDiagnostics(expr, OclContext.of(self),
				OclEvaluationOptions.strict());
		assertTrue(result.isSuccess());
		assertEquals(true, result.value());
	}

	@Test
	void evaluateWithDiagnostics_diagnosticsNeverNull() throws OclParseException {
		OclExpression expr = engine.parse("self.name", personClass);
		OclResult result = engine.evaluateWithDiagnostics(expr, OclContext.of(self),
				OclEvaluationOptions.strict());
		assertNotNull(result.diagnostics());
	}

	@Test
	void evaluateWithDiagnostics_noDiagnosticsOnSuccess() throws OclParseException {
		OclExpression expr = engine.parse("42 + 1", personClass);
		OclResult result = engine.evaluateWithDiagnostics(expr, OclContext.of(self),
				OclEvaluationOptions.strict());
		assertTrue(result.diagnostics().isEmpty());
	}

	// --- OclResult null/invalid ---

	@Test
	void result_nullValue() throws OclParseException {
		OclExpression expr = engine.parse("null", personClass);
		OclResult result = engine.evaluateWithDiagnostics(expr, OclContext.of(self),
				OclEvaluationOptions.strict());
		assertTrue(result.isNull());
		assertFalse(result.hasValue());
		assertFalse(result.isInvalid());
	}

	@Test
	void result_invalidValue() throws OclParseException {
		OclExpression expr = engine.parse("invalid", personClass);
		OclResult result = engine.evaluateWithDiagnostics(expr, OclContext.of(self),
				OclEvaluationOptions.strict());
		assertTrue(result.isInvalid());
		assertFalse(result.hasValue());
		assertFalse(result.isNull());
		assertSame(OclInvalid.INSTANCE, result.value());
	}

	// --- Strict vs Lenient null handling ---

	@Test
	void strict_normalEvaluation() throws OclParseException {
		OclExpression expr = engine.parse("self.name.size()", personClass);
		OclResult result = engine.evaluateWithDiagnostics(expr, OclContext.of(self),
				OclEvaluationOptions.strict());
		assertTrue(result.isSuccess());
		// evaluateWithDiagnostics returns raw (Long), not narrowed
		assertInstanceOf(Number.class, result.value());
		assertEquals(5, ((Number) result.value()).intValue());
	}

	@Test
	void lenient_normalEvaluation() throws OclParseException {
		OclExpression expr = engine.parse("self.name.size()", personClass);
		OclResult result = engine.evaluateWithDiagnostics(expr, OclContext.of(self),
				OclEvaluationOptions.lenient());
		assertTrue(result.isSuccess());
		assertInstanceOf(Number.class, result.value());
		assertEquals(5, ((Number) result.value()).intValue());
	}

	// --- evaluate() convenience (uses strict by default) ---

	@Test
	void evaluate_usesStrictByDefault() throws OclParseException {
		OclExpression expr = engine.parse("self.name", personClass);
		Object result = engine.evaluate(expr, OclContext.of(self));
		assertEquals("Alice", result);
	}

	// --- Custom options ---

	@Test
	void customOptions_maxDepth() {
		OclEvaluationOptions opts = OclEvaluationOptions.strict().withMaxDepth(50);
		assertEquals(50, opts.maxDepth());
	}

	@Test
	void customOptions_withLenientAndCollectErrors() throws OclParseException {
		OclEvaluationOptions opts = OclEvaluationOptions.lenient().withMaxDepth(500);
		OclExpression expr = engine.parse("self.age + 1", personClass);
		OclResult result = engine.evaluateWithDiagnostics(expr, OclContext.of(self), opts);
		assertTrue(result.isSuccess());
		// evaluateWithDiagnostics returns raw Long, not narrowed
		assertEquals(31, ((Number) result.value()).intValue());
	}

	// --- evaluate with pre-parsed expression ---

	@Test
	void evaluate_preParsed_string() throws OclParseException {
		OclExpression expr = engine.parse("'hello'.toUpperCase()", personClass);
		Object result = engine.evaluate(expr, OclContext.of(self));
		assertEquals("HELLO", result);
	}

	@Test
	void evaluate_preParsed_arithmetic() throws OclParseException {
		OclExpression expr = engine.parse("(10 + 5) * 3", personClass);
		Object result = engine.evaluate(expr, OclContext.of(self));
		assertEquals(45, result);
	}

	@Test
	void evaluate_preParsed_collection() throws OclParseException {
		OclExpression expr = engine.parse("Sequence{1, 2, 3}->size()", personClass);
		Object result = engine.evaluate(expr, OclContext.of(self));
		assertEquals(3, result);
	}
}
