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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclInvalid;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Edge case tests for arithmetic operations:
 * division by zero, boundary values, large numbers,
 * and precision edge cases.
 */
class OclArithmeticEdgeCaseTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Division by zero ---

	@Test
	void realDivisionByZero() throws OclParseException {
		Object result = eval("1.0 / 0.0", self);
		// IEEE 754: division by zero produces Infinity
		if (result instanceof Double d) {
			assertTrue(d.isInfinite());
		} else {
			assertSame(OclInvalid.INSTANCE, result);
		}
	}

	@Test
	void integerDivByZero() throws OclParseException {
		// 10 / 0 — OCL / always returns Real, so this is 10.0 / 0.0
		Object result = eval("10 / 0", self);
		if (result instanceof Double d) {
			assertTrue(d.isInfinite());
		} else {
			assertSame(OclInvalid.INSTANCE, result);
		}
	}

	@Test
	void divByZero_invalid() throws OclParseException {
		// div is integer division — should produce invalid
		Object result = eval("10.div(0)", self);
		assertSame(OclInvalid.INSTANCE, result);
	}

	@Test
	void modByZero_invalid() throws OclParseException {
		Object result = eval("10.mod(0)", self);
		assertSame(OclInvalid.INSTANCE, result);
	}

	// --- Integer boundary values ---

	@Test
	void maxLong() throws OclParseException {
		// 9223372036854775807 is Long.MAX_VALUE
		Object result = eval("9223372036854775807", self);
		assertInstanceOf(Long.class, result);
		assertEquals(Long.MAX_VALUE, result);
	}

	@Test
	void intMaxBoundary() throws OclParseException {
		// 2147483647 = Integer.MAX_VALUE → narrowed to Integer
		Object result = eval("2147483647", self);
		assertInstanceOf(Integer.class, result);
		assertEquals(Integer.MAX_VALUE, result);
	}

	@Test
	void intMaxPlusOne() throws OclParseException {
		// 2147483648 = Integer.MAX_VALUE + 1 → stays Long
		Object result = eval("2147483648", self);
		assertInstanceOf(Long.class, result);
		assertEquals(2147483648L, result);
	}

	@Test
	void intMinBoundary() throws OclParseException {
		Object result = eval("-2147483648", self);
		assertInstanceOf(Integer.class, result);
		assertEquals(Integer.MIN_VALUE, result);
	}

	@Test
	void intMinMinusOne() throws OclParseException {
		Object result = eval("-2147483649", self);
		assertInstanceOf(Long.class, result);
		assertEquals(-2147483649L, result);
	}

	// --- Precision edge cases ---

	@Test
	void realPrecision_smallDifference() throws OclParseException {
		Object result = eval("0.1 + 0.2", self);
		assertInstanceOf(Double.class, result);
		// IEEE 754: 0.1 + 0.2 is approximately 0.30000000000000004
		double d = (Double) result;
		assertTrue(Math.abs(d - 0.3) < 1e-10);
	}

	@Test
	void realPrecision_largeNumber() throws OclParseException {
		Object result = eval("1000000000.0 + 0.001", self);
		assertInstanceOf(Double.class, result);
	}

	// --- Negative number operations ---

	@Test
	void negative_times_negative() throws OclParseException {
		assertEquals(6, eval("(-2) * (-3)", self));
	}

	@Test
	void negative_times_positive() throws OclParseException {
		assertEquals(-6, eval("(-2) * 3", self));
	}

	@Test
	void negative_div() throws OclParseException {
		Object result = eval("(-7).div(2)", self);
		assertInstanceOf(Number.class, result);
	}

	@Test
	void negative_mod() throws OclParseException {
		Object result = eval("(-7).mod(2)", self);
		assertInstanceOf(Number.class, result);
	}

	// --- Abs edge cases ---

	@Test
	void abs_zero() throws OclParseException {
		assertEquals(0, eval("0.abs()", self));
	}

	@Test
	void abs_negative() throws OclParseException {
		assertEquals(42, eval("(-42).abs()", self));
	}

	@Test
	void abs_positive() throws OclParseException {
		assertEquals(42, eval("42.abs()", self));
	}

	@Test
	void abs_realNegative() throws OclParseException {
		assertEquals(3.14, eval("(-3.14).abs()", self));
	}

	// --- min/max edge cases ---

	@Test
	void min_sameValues() throws OclParseException {
		assertEquals(5, eval("5.min(5)", self));
	}

	@Test
	void max_sameValues() throws OclParseException {
		assertEquals(5, eval("5.max(5)", self));
	}

	@Test
	void min_negativeAndPositive() throws OclParseException {
		assertEquals(-10, eval("(-10).min(10)", self));
	}

	@Test
	void max_negativeAndPositive() throws OclParseException {
		assertEquals(10, eval("(-10).max(10)", self));
	}

	// --- Chained arithmetic ---

	@Test
	void chained_additions() throws OclParseException {
		assertEquals(55, eval("1+2+3+4+5+6+7+8+9+10", self));
	}

	@Test
	void chained_multiplications() throws OclParseException {
		assertEquals(120, eval("1*2*3*4*5", self));
	}

	@Test
	void mixed_precedence() throws OclParseException {
		// 2 + 3 * 4 - 1 = 2 + 12 - 1 = 13
		assertEquals(13, eval("2 + 3 * 4 - 1", self));
	}

	// --- toInteger / toReal conversions ---

	@Test
	void toReal_integer() throws OclParseException {
		Object result = eval("42.toReal()", self);
		assertInstanceOf(Double.class, result);
		assertEquals(42.0, result);
	}

	@Test
	void toInteger_real() throws OclParseException {
		Object result = eval("42.7.toInteger()", self);
		assertInstanceOf(Number.class, result);
		assertEquals(42, ((Number) result).intValue());
	}
}
