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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OclInvalid propagation through OCL expressions.
 * Per OCL spec, invalid propagates through most operations
 * except oclIsInvalid() and oclIsUndefined().
 */
class OclInvalidPropagationTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Division by zero produces invalid ---

	@Test
	void divByZero_integer() throws OclParseException {
		assertInvalid("1 / 0", self);
	}

	@Test
	void divByZero_real() throws OclParseException {
		assertInvalid("1.0 / 0.0", self);
	}

	@Test
	void divByZero_integerDiv() throws OclParseException {
		assertInvalid("1.div(0)", self);
	}

	@Test
	void modByZero() throws OclParseException {
		assertInvalid("1.mod(0)", self);
	}

	// --- Invalid propagation through arithmetic ---

	@Test
	void invalidPlusInt() throws OclParseException {
		assertInvalid("(1 / 0) + 1", self);
	}

	@Test
	void intPlusInvalid() throws OclParseException {
		assertInvalid("1 + (1 / 0)", self);
	}

	@Test
	void invalidTimesInt() throws OclParseException {
		assertInvalid("(1 / 0) * 2", self);
	}

	// --- Invalid in boolean context ---

	@Test
	void invalidAndTrue() throws OclParseException {
		// (1/0).oclIsInvalid() is true, true = false is false, false and true is false
		assertEquals(false, eval("(1 / 0).oclIsInvalid() = false and true", self));
	}

	// --- Invalid in if condition ---

	@Test
	void invalidInIfCondition() throws OclParseException {
		// if invalid then ... — should be invalid
		assertInvalid("if (1 / 0) > 0 then 'yes' else 'no' endif", self);
	}

	// --- Invalid in let ---

	@Test
	void invalidInLetInit() throws OclParseException {
		assertInvalid("let x: Integer = 1 / 0 in x + 1", self);
	}

	// --- oclIsInvalid detects invalid ---

	@Test
	void oclIsInvalid_onDivByZero() throws OclParseException {
		assertEquals(true, eval("(1 / 0).oclIsInvalid()", self));
	}

	@Test
	void oclIsInvalid_onValidValue() throws OclParseException {
		assertEquals(false, eval("42.oclIsInvalid()", self));
	}

	// --- oclIsUndefined detects invalid too ---

	@Test
	void oclIsUndefined_onInvalid() throws OclParseException {
		assertEquals(true, eval("(1 / 0).oclIsUndefined()", self));
	}

	// --- Invalid in collections ---

	@Test
	void invalidInSequence() throws OclParseException {
		// Sequence containing invalid — size should still work
		assertEquals(3, eval("Sequence{1, 1 / 0, 3}->size()", self));
	}

	// --- Invalid comparison ---

	@Test
	void invalidEqualsInvalid() throws OclParseException {
		// Two invalids are equal per OCL spec (both are the singleton OclInvalid)
		assertEquals(true, eval("(1 / 0) = (2 / 0)", self));
	}

	@Test
	void invalidNotEqualsValue() throws OclParseException {
		assertEquals(true, eval("(1 / 0) <> 42", self));
	}

	// --- String operations producing invalid ---

	@Test
	void substring_outOfBounds() throws OclParseException {
		assertInvalid("'hello'.substring(1, 10)", self);
	}

	@Test
	void at_outOfBounds() throws OclParseException {
		assertInvalid("'hello'.at(0)", self);
	}

	@Test
	void toInteger_badFormat() throws OclParseException {
		assertInvalid("'abc'.toInteger()", self);
	}

	@Test
	void toReal_badFormat() throws OclParseException {
		assertInvalid("'abc'.toReal()", self);
	}

	@Test
	void toBoolean_badFormat() throws OclParseException {
		assertInvalid("'maybe'.toBoolean()", self);
	}

	// --- Collection operations producing invalid ---

	@Test
	void first_emptySequence() throws OclParseException {
		assertInvalid("Sequence{}->first()", self);
	}

	@Test
	void last_emptySequence() throws OclParseException {
		assertInvalid("Sequence{}->last()", self);
	}

	@Test
	void at_outOfBounds_collection() throws OclParseException {
		assertInvalid("Sequence{1, 2, 3}->at(0)", self);
	}

	@Test
	void at_outOfBounds_high_collection() throws OclParseException {
		assertInvalid("Sequence{1, 2, 3}->at(4)", self);
	}
}
