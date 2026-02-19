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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Advanced tests for OCL arithmetic operations.
 * Covers complex expressions, operator combinations,
 * div/mod edge cases, and arithmetic with model properties.
 */
class OclArithmeticAdvancedTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Complex arithmetic expressions ---

	@Test
	void complexExpression_1() throws OclParseException {
		// (2 + 3) * (4 - 1) = 5 * 3 = 15
		assertEquals(15, eval("(2 + 3) * (4 - 1)", self));
	}

	@Test
	void complexExpression_2() throws OclParseException {
		// 10 - 3 * 2 + 1 = 10 - 6 + 1 = 5
		assertEquals(5, eval("10 - 3 * 2 + 1", self));
	}

	@Test
	void complexExpression_3() throws OclParseException {
		// ((10 + 5) * 2) / 3 = 30 / 3 = 10.0
		assertEquals(10.0, eval("((10 + 5) * 2) / 3", self));
	}

	// --- div edge cases ---

	@Test
	void div_exact() throws OclParseException {
		assertEquals(5, eval("10.div(2)", self));
	}

	@Test
	void div_truncates() throws OclParseException {
		assertEquals(3, eval("10.div(3)", self));
	}

	@Test
	void div_negative_dividend() throws OclParseException {
		Object result = eval("(-10).div(3)", self);
		assertInstanceOf(Number.class, result);
	}

	@Test
	void div_one() throws OclParseException {
		assertEquals(42, eval("42.div(1)", self));
	}

	// --- mod edge cases ---

	@Test
	void mod_noRemainder() throws OclParseException {
		assertEquals(0, eval("10.mod(5)", self));
	}

	@Test
	void mod_withRemainder() throws OclParseException {
		assertEquals(1, eval("10.mod(3)", self));
	}

	@Test
	void mod_one() throws OclParseException {
		assertEquals(0, eval("42.mod(1)", self));
	}

	@Test
	void mod_twoDigit() throws OclParseException {
		assertEquals(7, eval("17.mod(10)", self));
	}

	// --- div/mod relationship: a = (a.div(b) * b) + a.mod(b) ---

	@Test
	void divMod_relationship() throws OclParseException {
		// 17.div(5) * 5 + 17.mod(5) = 3*5 + 2 = 17
		assertEquals(17, eval("17.div(5) * 5 + 17.mod(5)", self));
	}

	// --- Arithmetic with model properties ---

	@Test
	void property_addition() throws OclParseException {
		assertEquals(31, eval("self.age + 1", self));
	}

	@Test
	void property_multiplication() throws OclParseException {
		assertEquals(60, eval("self.age * 2", self));
	}

	@Test
	void property_realDivision() throws OclParseException {
		assertEquals(25000.0, eval("self.salary / 2.0", self));
	}

	@Test
	void property_comparison() throws OclParseException {
		assertEquals(true, eval("self.salary > self.age.toReal() * 1000.0", self));
	}

	// --- Integer overflow behavior ---

	@Test
	void largeMultiplication() throws OclParseException {
		Object result = eval("1000000 * 1000000", self);
		assertInstanceOf(Number.class, result);
		assertEquals(1000000000000L, result);
	}

	@Test
	void largeAddition() throws OclParseException {
		assertEquals(2000000000, eval("1000000000 + 1000000000", self));
	}

	// --- Unary minus combinations ---

	@Test
	void unaryMinus_beforeParens() throws OclParseException {
		assertEquals(-15, eval("-(3 * 5)", self));
	}

	@Test
	void unaryMinus_doubleNeg() throws OclParseException {
		assertEquals(42, eval("-(-42)", self));
	}

	@Test
	void unaryMinus_inExpression() throws OclParseException {
		assertEquals(-2, eval("3 + (-5)", self));
	}

	// --- Mixed int/real arithmetic ---

	@Test
	void mixed_addIntReal() throws OclParseException {
		Object result = eval("5 + 2.5", self);
		assertInstanceOf(Double.class, result);
		assertEquals(7.5, result);
	}

	@Test
	void mixed_multiplyIntReal() throws OclParseException {
		Object result = eval("4 * 2.5", self);
		assertInstanceOf(Double.class, result);
		assertEquals(10.0, result);
	}

	@Test
	void intDivAlwaysReal() throws OclParseException {
		Object result = eval("10 / 3", self);
		assertInstanceOf(Double.class, result);
	}

	// --- Zero arithmetic ---

	@Test
	void addZero() throws OclParseException {
		assertEquals(42, eval("42 + 0", self));
	}

	@Test
	void multiplyByZero() throws OclParseException {
		assertEquals(0, eval("42 * 0", self));
	}

	@Test
	void subtractSelf() throws OclParseException {
		assertEquals(0, eval("42 - 42", self));
	}

	@Test
	void multiplyByOne() throws OclParseException {
		assertEquals(42, eval("42 * 1", self));
	}
}
