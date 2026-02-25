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

import static org.junit.jupiter.api.Assertions.assertSame;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL evaluation errors that should produce OclInvalid.
 * According to the OCL spec, runtime errors result in the invalid
 * value rather than throwing exceptions.
 */
class OclEvaluationErrorTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Division by zero ---

	@Test
	void divisionByZero_integer() throws OclParseException {
		assertInvalid("1 / 0", self);
	}

	@Test
	void divisionByZero_real() throws OclParseException {
		assertInvalid("1.0 / 0.0", self);
	}

	@Test
	void divByZero_integer() throws OclParseException {
		assertInvalid("5.div(0)", self);
	}

	@Test
	void modByZero() throws OclParseException {
		assertInvalid("5.mod(0)", self);
	}

	// --- Invalid property access ---
	// Note: nonExistent properties are caught at parse time as synthetic
	// attributes; the evaluator throws IllegalArgumentException from EMF.
	// This is acceptable behavior for our implementation.

	// --- Invalid on invalid ---

	@Test
	void invalid_oclIsInvalid() throws OclParseException {
		assertSame(true, eval("invalid.oclIsInvalid()", self));
	}

	@Test
	void invalid_oclIsUndefined() throws OclParseException {
		assertSame(true, eval("invalid.oclIsUndefined()", self));
	}

	@Test
	void invalid_literal() throws OclParseException {
		assertInvalid("invalid", self);
	}

	// --- Arithmetic with invalid ---

	@Test
	void invalid_plus() throws OclParseException {
		assertInvalid("invalid + 1", self);
	}

	@Test
	void invalid_times() throws OclParseException {
		assertInvalid("invalid * 2", self);
	}

	@Test
	void invalid_negate() throws OclParseException {
		assertInvalid("-invalid", self);
	}

	// --- Invalid in boolean context ---

	@Test
	void invalid_and_true() throws OclParseException {
		assertInvalid("invalid and true", self);
	}

	@Test
	void invalid_or_false() throws OclParseException {
		assertInvalid("invalid or false", self);
	}

	// --- Invalid in collections ---

	@Test
	void invalid_inSequence() throws OclParseException {
		// Collection containing invalid
		assertInvalid("Sequence{1, 1/0, 3}->sum()", self);
	}

	// --- Substring out of bounds ---

	@Test
	void substring_outOfBounds() throws OclParseException {
		assertInvalid("'abc'.substring(1, 10)", self);
	}

	@Test
	void substring_negativeIndex() throws OclParseException {
		assertInvalid("'abc'.substring(-1, 2)", self);
	}

	// --- Sequence at out of bounds ---

	@Test
	void sequence_at_outOfBounds() throws OclParseException {
		assertInvalid("Sequence{1, 2, 3}->at(5)", self);
	}

	@Test
	void sequence_at_zero() throws OclParseException {
		// OCL is 1-based, 0 is invalid
		assertInvalid("Sequence{1, 2, 3}->at(0)", self);
	}

	// --- Invalid propagation in if ---

	@Test
	void if_invalidCondition() throws OclParseException {
		assertInvalid("if invalid then 1 else 2 endif", self);
	}

	@Test
	void if_invalidThen_selected() throws OclParseException {
		assertInvalid("if true then invalid else 2 endif", self);
	}

	@Test
	void if_invalidElse_notSelected() throws OclParseException {
		// Invalid in else branch should not propagate when condition is true
		Object result = eval("if true then 1 else invalid endif", self);
		assertSame(1, result);
	}

	// --- Invalid equality ---

	@Test
	void invalid_equalsInvalid() throws OclParseException {
		// OCL v2.5: any operation on invalid (except oclIsInvalid/oclIsUndefined) yields invalid
		assertInvalid("invalid = invalid", self);
	}

	@Test
	void invalid_notEqualsValue() throws OclParseException {
		// OCL v2.5: any operation on invalid (except oclIsInvalid/oclIsUndefined) yields invalid
		assertInvalid("invalid <> 1", self);
	}
}
