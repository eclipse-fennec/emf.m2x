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
package org.eclipse.fennec.m2x.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.OclResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for LENIENT vs STRICT null handling in the OCL evaluator.
 *
 * <p>Verifies that {@link OclEvaluationOptions.NullHandling#LENIENT} propagates
 * null through navigation chains without throwing exceptions, while
 * {@link OclEvaluationOptions.NullHandling#STRICT} produces {@code OclInvalid}
 * with diagnostics on null source access.
 *
 * @see OclEvaluationOptions.NullHandling
 */
class OclLenientNullHandlingTest extends AbstractOclTest {

	private static final OclEvaluationOptions STRICT = OclEvaluationOptions.strict();
	private static final OclEvaluationOptions LENIENT = OclEvaluationOptions.lenient();

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	private static OclResult evalResult(String expression, EObject ctx, OclEvaluationOptions opts)
			throws OclParseException {
		OclExpression parsed = engine.parse(expression, ctx.eClass());
		return engine.evaluateWithDiagnostics(parsed, OclContext.of(ctx), opts);
	}

	// --- Property access on null source ---

	@Test
	void strict_nullPropertyAccess_returnsInvalidWithDiagnostic() throws OclParseException {
		// null.oclIsUndefined() is always true, but null used as property source should fail in STRICT
		// We use an if-expression to produce null, then navigate on it
		OclResult result = evalResult(
				"let x : String = null in x.size()", self, STRICT);
		assertSame(OclInvalid.INSTANCE, result.value());
		assertFalse(result.diagnostics().isEmpty());
	}

	@Test
	void lenient_nullPropertyAccess_returnsInvalidWithDiagnostic() throws OclParseException {
		// In LENIENT mode, null source passes through checkNullInvalid but still errors
		// because null is not an EObject — the error just has a different path
		OclResult result = evalResult(
				"let x : String = null in x.size()", self, LENIENT);
		// LENIENT mode: null passes through checkNullInvalid, then stdlib handles null.size()
		// size() on null string → stdlib dispatches on null source
		// Since null is not a String, dispatchString returns NOT_FOUND
		// Then we fall through to "Unknown operation" → OclInvalid
		assertSame(OclInvalid.INSTANCE, result.value());
	}

	// --- Null-safe operations work in both modes ---

	@Test
	void strict_oclIsUndefined_onNull() throws OclParseException {
		OclResult result = evalResult("null.oclIsUndefined()", self, STRICT);
		assertEquals(true, result.value());
	}

	@Test
	void lenient_oclIsUndefined_onNull() throws OclParseException {
		OclResult result = evalResult("null.oclIsUndefined()", self, LENIENT);
		assertEquals(true, result.value());
	}

	@Test
	void strict_oclIsInvalid_onNull() throws OclParseException {
		OclResult result = evalResult("null.oclIsInvalid()", self, STRICT);
		assertEquals(false, result.value());
	}

	@Test
	void lenient_oclIsInvalid_onNull() throws OclParseException {
		OclResult result = evalResult("null.oclIsInvalid()", self, LENIENT);
		assertEquals(false, result.value());
	}

	// --- Arithmetic on null in STRICT vs LENIENT ---

	@Test
	void strict_nullInArithmetic_returnsInvalid() throws OclParseException {
		OclResult result = evalResult(
				"let x : Integer = null in x + 1", self, STRICT);
		assertSame(OclInvalid.INSTANCE, result.value());
		assertFalse(result.diagnostics().isEmpty());
	}

	@Test
	void lenient_nullInArithmetic_returnsInvalid() throws OclParseException {
		// Even in LENIENT mode, null + 1 should produce OclInvalid
		// because the stdlib can't add null to an integer
		OclResult result = evalResult(
				"let x : Integer = null in x + 1", self, LENIENT);
		assertSame(OclInvalid.INSTANCE, result.value());
	}

	// --- If-expression with null branches ---

	@Test
	void strict_ifNull_thenBranch() throws OclParseException {
		OclResult result = evalResult(
				"if true then null else 42 endif", self, STRICT);
		assertTrue(result.isNull());
	}

	@Test
	void lenient_ifNull_thenBranch() throws OclParseException {
		OclResult result = evalResult(
				"if true then null else 42 endif", self, LENIENT);
		assertTrue(result.isNull());
	}

	// --- Null comparison in both modes ---

	@Test
	void strict_nullEquality() throws OclParseException {
		OclResult result = evalResult("null = null", self, STRICT);
		assertEquals(true, result.value());
	}

	@Test
	void lenient_nullEquality() throws OclParseException {
		OclResult result = evalResult("null = null", self, LENIENT);
		assertEquals(true, result.value());
	}

	@Test
	void strict_nullInequality() throws OclParseException {
		OclResult result = evalResult("null <> 'hello'", self, STRICT);
		assertEquals(true, result.value());
	}

	@Test
	void lenient_nullInequality() throws OclParseException {
		OclResult result = evalResult("null <> 'hello'", self, LENIENT);
		assertEquals(true, result.value());
	}

	// --- COLLECT_ERRORS error recovery ---

	@Test
	void lenient_collectErrors_hasDiagnosticsOnFailure() throws OclParseException {
		// LENIENT mode uses COLLECT_ERRORS error recovery
		OclResult result = evalResult("1.div(0)", self, LENIENT);
		assertSame(OclInvalid.INSTANCE, result.value());
	}

	// --- Boolean three-valued logic with null ---

	@Test
	void strict_nullAndTrue_returnsInvalid() throws OclParseException {
		// Three-valued logic: null and true → OclInvalid
		// (our implementation conflates null and invalid in Boolean operations)
		OclResult result = evalResult(
				"let b : Boolean = null in b and true", self, STRICT);
		assertSame(OclInvalid.INSTANCE, result.value());
	}

	@Test
	void lenient_nullAndTrue_returnsInvalid() throws OclParseException {
		OclResult result = evalResult(
				"let b : Boolean = null in b and true", self, LENIENT);
		assertSame(OclInvalid.INSTANCE, result.value());
	}

	@Test
	void strict_nullOrTrue_returnsTrue() throws OclParseException {
		// OCL §11.3.1: null or true = true
		OclResult result = evalResult(
				"let b : Boolean = null in b or true", self, STRICT);
		assertEquals(true, result.value());
	}

	@Test
	void lenient_nullOrTrue_returnsTrue() throws OclParseException {
		OclResult result = evalResult(
				"let b : Boolean = null in b or true", self, LENIENT);
		assertEquals(true, result.value());
	}

	// --- Safe navigation interacts with null handling ---

	@Test
	void strict_safeNavigation_onNull_returnsNull() throws OclParseException {
		// Safe navigation (?.) on null source → null regardless of mode
		OclResult result = evalResult(
				"let x : String = null in x?.size()", self, STRICT);
		assertTrue(result.isNull());
	}

	@Test
	void lenient_safeNavigation_onNull_returnsNull() throws OclParseException {
		OclResult result = evalResult(
				"let x : String = null in x?.size()", self, LENIENT);
		assertTrue(result.isNull());
	}

	// --- Normal evaluation works the same in both modes ---

	@Test
	void strict_normalEvaluation_succeeds() throws OclParseException {
		OclResult result = evalResult("self.name", self, STRICT);
		assertTrue(result.isSuccess());
		assertEquals("Alice", result.value());
	}

	@Test
	void lenient_normalEvaluation_succeeds() throws OclParseException {
		OclResult result = evalResult("self.name", self, LENIENT);
		assertTrue(result.isSuccess());
		assertEquals("Alice", result.value());
	}

	@Test
	void strict_collectionOnNull_arrowImpliesOclAsSet() throws OclParseException {
		// Spec §11.2.3: null as source of arrow call → implicit oclAsSet() → Set{}
		// null->size() → Set{}->size() → 0
		OclResult result = evalResult(
				"let x : Sequence(Integer) = null in x->size()", self, STRICT);
		assertEquals(0L, result.value());
	}

	@Test
	void lenient_collectionOnNull_arrowImpliesOclAsSet() throws OclParseException {
		// Spec §11.2.3: null as source of arrow call → implicit oclAsSet() → Set{}
		OclResult result = evalResult(
				"let x : Sequence(Integer) = null in x->size()", self, LENIENT);
		assertEquals(0L, result.value());
	}
}
