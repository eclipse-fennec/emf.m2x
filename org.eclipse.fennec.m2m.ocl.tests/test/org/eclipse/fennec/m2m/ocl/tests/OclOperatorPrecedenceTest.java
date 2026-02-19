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
 * Tests for OCL operator precedence.
 * Verifies that operators bind according to OCL spec precedence rules:
 * unary > * / > + - > comparison > not > and > or > xor > implies
 */
class OclOperatorPrecedenceTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Arithmetic precedence ---

	@Test
	void multiplyBeforeAdd() throws OclParseException {
		assertEquals(7, eval("1 + 2 * 3", self));
	}

	@Test
	void multiplyBeforeSubtract() throws OclParseException {
		assertEquals(-5, eval("1 - 2 * 3", self));
	}

	@Test
	void divideBeforeAdd() throws OclParseException {
		assertEquals(3.5, eval("1 + 5 / 2", self));
	}

	@Test
	void parensOverrideArithmetic() throws OclParseException {
		assertEquals(9, eval("(1 + 2) * 3", self));
	}

	@Test
	void leftAssociativity_addition() throws OclParseException {
		// (1 - 2) + 3 = 2, not 1 - (2 + 3) = -4
		assertEquals(2, eval("1 - 2 + 3", self));
	}

	@Test
	void leftAssociativity_multiplication() throws OclParseException {
		// (12 / 3) * 2 = 8.0, not 12 / (3 * 2) = 2.0
		assertEquals(8.0, eval("12 / 3 * 2", self));
	}

	// --- Comparison vs Arithmetic ---

	@Test
	void arithmeticBeforeComparison() throws OclParseException {
		// 1 + 2 > 2 → 3 > 2 → true
		assertEquals(true, eval("1 + 2 > 2", self));
	}

	@Test
	void arithmeticBeforeEquals() throws OclParseException {
		assertEquals(true, eval("2 + 3 = 5", self));
	}

	@Test
	void comparisonBothSides() throws OclParseException {
		// (1 + 2) < (3 + 1)
		assertEquals(true, eval("1 + 2 < 3 + 1", self));
	}

	// --- Boolean operator precedence ---

	@Test
	void notBeforeAnd() throws OclParseException {
		// not false and true → (not false) and true → true and true → true
		assertEquals(true, eval("not false and true", self));
	}

	@Test
	void andBeforeOr() throws OclParseException {
		// true or false and false → true or (false and false) → true or false → true
		assertEquals(true, eval("true or false and false", self));
	}

	@Test
	void andBeforeOr_false() throws OclParseException {
		// false and true or false → (false and true) or false → false or false → false
		assertEquals(false, eval("false and true or false", self));
	}

	@Test
	void orBeforeImplies() throws OclParseException {
		// false implies false or true → false implies (false or true) → false implies true → true
		assertEquals(true, eval("false implies false or true", self));
	}

	@Test
	void parensOverrideBoolean() throws OclParseException {
		// (true or false) and false → true and false → false
		assertEquals(false, eval("(true or false) and false", self));
	}

	// --- Comparison vs Boolean ---

	@Test
	void comparisonBeforeAnd() throws OclParseException {
		// 1 > 0 and 2 > 1 → true and true → true
		assertEquals(true, eval("1 > 0 and 2 > 1", self));
	}

	@Test
	void comparisonBeforeOr() throws OclParseException {
		// 1 > 2 or 3 > 2 → false or true → true
		assertEquals(true, eval("1 > 2 or 3 > 2", self));
	}

	@Test
	void comparisonBeforeImplies() throws OclParseException {
		// 1 > 0 implies 2 > 1 → true implies true → true
		assertEquals(true, eval("1 > 0 implies 2 > 1", self));
	}

	// --- Unary minus ---

	@Test
	void unaryMinusBeforeMultiply() throws OclParseException {
		// -2 * 3 → (-2) * 3 → -6
		assertEquals(-6, eval("-2 * 3", self));
	}

	@Test
	void unaryMinusBeforeAdd() throws OclParseException {
		// -2 + 3 → (-2) + 3 → 1
		assertEquals(1, eval("-2 + 3", self));
	}

	// --- xor precedence ---

	@Test
	void xor_trueXorTrue() throws OclParseException {
		assertEquals(false, eval("true xor true", self));
	}

	@Test
	void xor_trueXorFalse() throws OclParseException {
		assertEquals(true, eval("true xor false", self));
	}

	@Test
	void xor_withParens() throws OclParseException {
		// (true and false) xor true → false xor true → true
		assertEquals(true, eval("(true and false) xor true", self));
	}

	// --- Complex mixed expressions ---

	@Test
	void complex_arithmeticAndBoolean() throws OclParseException {
		// 1 + 2 > 2 and 3 * 2 = 6 → true and true → true
		assertEquals(true, eval("1 + 2 > 2 and 3 * 2 = 6", self));
	}

	@Test
	void complex_nestedArithmetic() throws OclParseException {
		assertEquals(14, eval("2 + 3 * 4", self));
	}

	@Test
	void complex_fullChain() throws OclParseException {
		// 1 + 1 = 2 and not false implies true
		// → (1+1 = 2) and (not false) implies true
		// → (true and true) implies true
		// → true implies true → true
		assertEquals(true, eval("1 + 1 = 2 and not false implies true", self));
	}
}
