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
 * Tests for unary minus edge cases: double negation, precedence
 * with method calls, parenthesized negation, and mixed expressions.
 */
class OclUnaryMinusEdgeCaseTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Simple negation ---

	@Test
	void negateInteger() throws OclParseException {
		assertEquals(-5, eval("-5", self));
	}

	@Test
	void negateReal() throws OclParseException {
		assertEquals(-3.14, eval("-3.14", self));
	}

	@Test
	void negateZero() throws OclParseException {
		assertEquals(0, eval("-0", self));
	}

	// --- Double negation ---

	@Test
	void doubleNegation() throws OclParseException {
		assertEquals(5, eval("-(-5)", self));
	}

	@Test
	void tripleNegation() throws OclParseException {
		assertEquals(-5, eval("-(-(- 5))", self));
	}

	// --- Negation with arithmetic ---

	@Test
	void negateAndAdd() throws OclParseException {
		assertEquals(-2, eval("-5 + 3", self));
	}

	@Test
	void negateAndSubtract() throws OclParseException {
		assertEquals(-8, eval("-5 - 3", self));
	}

	@Test
	void negateAndMultiply() throws OclParseException {
		assertEquals(-15, eval("-5 * 3", self));
	}

	@Test
	void addNegative() throws OclParseException {
		assertEquals(2, eval("5 + (-3)", self));
	}

	@Test
	void subtractNegative() throws OclParseException {
		assertEquals(8, eval("5 - (-3)", self));
	}

	@Test
	void multiplyNegatives() throws OclParseException {
		assertEquals(15, eval("(-5) * (-3)", self));
	}

	// --- Negation with method calls ---

	@Test
	void negateAbs() throws OclParseException {
		assertEquals(-5, eval("-(5.abs())", self));
	}

	@Test
	void absOfNegate() throws OclParseException {
		assertEquals(5, eval("(-5).abs()", self));
	}

	@Test
	void negateMax() throws OclParseException {
		assertEquals(-5, eval("-(3.max(5))", self));
	}

	@Test
	void maxOfNegate() throws OclParseException {
		assertEquals(-3, eval("(-5).max(-3)", self));
	}

	@Test
	void minOfNegatives() throws OclParseException {
		assertEquals(-5, eval("(-3).min(-5)", self));
	}

	// --- Negation with division ---

	@Test
	void negateDivision() throws OclParseException {
		assertEquals(-2.5, eval("-(5 / 2)", self));
	}

	@Test
	void divisionOfNegative() throws OclParseException {
		assertEquals(-2.5, eval("(-5) / 2", self));
	}

	// --- Negation with mod/div ---

	@Test
	void negateDiv() throws OclParseException {
		assertEquals(-2, eval("-(7.div(3))", self));
	}

	@Test
	void negateMod() throws OclParseException {
		assertEquals(-1, eval("-(7.mod(3))", self));
	}

	// --- Negation in collections ---

	@Test
	void negativeInSequence() throws OclParseException {
		assertEquals(3, eval("Sequence{-1, -2, -3}->size()", self));
	}

	@Test
	void sumOfNegatives() throws OclParseException {
		assertEquals(-6, eval("Sequence{-1, -2, -3}->sum()", self));
	}

	@Test
	void minOfNegativeCollection() throws OclParseException {
		assertEquals(-3, eval("Sequence{-1, -2, -3}->min()", self));
	}

	@Test
	void maxOfNegativeCollection() throws OclParseException {
		assertEquals(-1, eval("Sequence{-1, -2, -3}->max()", self));
	}

	// --- Negation in comparisons ---

	@Test
	void negativeCompare() throws OclParseException {
		assertEquals(true, eval("-5 < -3", self));
	}

	@Test
	void negativeEquals() throws OclParseException {
		assertEquals(true, eval("-5 = -5", self));
	}

	@Test
	void negativeNotEquals() throws OclParseException {
		assertEquals(true, eval("-5 <> -3", self));
	}

	// --- Negation in if ---

	@Test
	void negativeInIf() throws OclParseException {
		assertEquals(-1, eval("if true then -1 else 1 endif", self));
	}

	// --- Negation in let ---

	@Test
	void negativeInLet() throws OclParseException {
		assertEquals(-42, eval("let x: Integer = -42 in x", self));
	}

	@Test
	void negateLetVar() throws OclParseException {
		assertEquals(-42, eval("let x: Integer = 42 in -x", self));
	}

	// --- Negation with property ---

	@Test
	void negateProperty() throws OclParseException {
		assertEquals(-30, eval("-(self.age)", self));
	}
}
