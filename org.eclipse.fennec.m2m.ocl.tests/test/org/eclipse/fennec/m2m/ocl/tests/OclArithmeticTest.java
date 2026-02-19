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
 * Tests for OCL arithmetic operations on Integer and Real types.
 */
class OclArithmeticTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Integer Arithmetic ---

	@Test
	void integerAddition() throws OclParseException {
		assertEquals(3L, eval("1 + 2", self));
	}

	@Test
	void integerSubtraction() throws OclParseException {
		assertEquals(5L, eval("10 - 5", self));
	}

	@Test
	void integerMultiplication() throws OclParseException {
		assertEquals(12L, eval("3 * 4", self));
	}

	@Test
	void integerDivision_returnsReal() throws OclParseException {
		assertEquals(2.5, eval("5 / 2", self));
	}

	@Test
	void integerDiv_integerDivision() throws OclParseException {
		assertEquals(2L, eval("5.div(2)", self));
	}

	@Test
	void integerMod() throws OclParseException {
		assertEquals(1L, eval("5.mod(2)", self));
	}

	@Test
	void integerAbs_positive() throws OclParseException {
		assertEquals(5L, eval("5.abs()", self));
	}

	@Test
	void integerAbs_negative() throws OclParseException {
		assertEquals(5L, eval("(-5).abs()", self));
	}

	@Test
	void integerMax() throws OclParseException {
		assertEquals(5L, eval("3.max(5)", self));
	}

	@Test
	void integerMin() throws OclParseException {
		assertEquals(3L, eval("3.min(5)", self));
	}

	@Test
	void integerToReal() throws OclParseException {
		assertEquals(42.0, eval("42.toReal()", self));
	}

	@Test
	void divisionByZero_returnsInvalid() throws OclParseException {
		assertInvalid("1 / 0", self);
	}

	@Test
	void integerDivByZero_returnsInvalid() throws OclParseException {
		assertInvalid("5.div(0)", self);
	}

	@Test
	void integerModByZero_returnsInvalid() throws OclParseException {
		assertInvalid("5.mod(0)", self);
	}

	// --- Real Arithmetic ---

	@Test
	void realAddition() throws OclParseException {
		assertEquals(5.5, eval("2.5 + 3.0", self));
	}

	@Test
	void realSubtraction() throws OclParseException {
		assertEquals(1.5, eval("4.0 - 2.5", self));
	}

	@Test
	void realMultiplication() throws OclParseException {
		assertEquals(7.5, eval("2.5 * 3.0", self));
	}

	@Test
	void realDivision() throws OclParseException {
		assertEquals(2.5, eval("5.0 / 2.0", self));
	}

	@Test
	void realAbs() throws OclParseException {
		assertEquals(3.14, eval("(-3.14).abs()", self));
	}

	@Test
	void realFloor() throws OclParseException {
		assertEquals(3L, eval("3.7.floor()", self));
	}

	@Test
	void realCeiling() throws OclParseException {
		assertEquals(4L, eval("3.2.ceiling()", self));
	}

	@Test
	void realRound() throws OclParseException {
		assertEquals(4L, eval("3.5.round()", self));
	}

	@Test
	void realRound_down() throws OclParseException {
		assertEquals(3L, eval("3.4.round()", self));
	}

	@Test
	void realMax() throws OclParseException {
		assertEquals(5.5, eval("3.2.max(5.5)", self));
	}

	@Test
	void realMin() throws OclParseException {
		assertEquals(3.2, eval("3.2.min(5.5)", self));
	}

	// --- Mixed Integer/Real ---

	@Test
	void mixedAddition_intPlusReal() throws OclParseException {
		assertEquals(5.5, eval("2 + 3.5", self));
	}

	@Test
	void mixedMultiplication() throws OclParseException {
		assertEquals(7.5, eval("3 * 2.5", self));
	}

	// --- Operator Precedence ---

	@Test
	void precedence_multiplicationBeforeAddition() throws OclParseException {
		assertEquals(7L, eval("1 + 2 * 3", self));
	}

	@Test
	void precedence_parentheses() throws OclParseException {
		assertEquals(9L, eval("(1 + 2) * 3", self));
	}

	@Test
	void precedence_unaryMinus() throws OclParseException {
		assertEquals(-6L, eval("-(2 * 3)", self));
	}
}
