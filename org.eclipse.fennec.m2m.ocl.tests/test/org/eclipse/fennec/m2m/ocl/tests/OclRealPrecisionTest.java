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
 * Tests for OCL Real (floating-point) precision and edge cases.
 * Verifies proper handling of decimal arithmetic, rounding,
 * and boundary values.
 */
class OclRealPrecisionTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Basic real arithmetic ---

	@Test
	void realAddition() throws OclParseException {
		// IEEE 754 floating-point: 1.1 + 2.2 ≈ 3.3000000000000003
		Object result = eval("1.1 + 2.2", self);
		assertInstanceOf(Double.class, result);
		assertEquals(3.3, (Double) result, 1e-10);
	}

	@Test
	void realSubtraction() throws OclParseException {
		assertEquals(0.5, eval("1.5 - 1.0", self));
	}

	@Test
	void realMultiplication() throws OclParseException {
		assertEquals(6.25, eval("2.5 * 2.5", self));
	}

	@Test
	void realDivision() throws OclParseException {
		assertEquals(2.5, eval("5.0 / 2.0", self));
	}

	// --- Small decimals ---

	@Test
	void smallDecimal() throws OclParseException {
		assertEquals(0.001, eval("0.001", self));
	}

	@Test
	void smallDecimalArithmetic() throws OclParseException {
		assertEquals(0.003, eval("0.001 + 0.002", self));
	}

	// --- Large reals ---

	@Test
	void largeReal() throws OclParseException {
		assertEquals(1000000.5, eval("1000000.5", self));
	}

	@Test
	void largeRealArithmetic() throws OclParseException {
		Object result = eval("999999.5 + 0.5", self);
		assertInstanceOf(Double.class, result);
		assertEquals(1000000.0, result);
	}

	// --- Negative reals ---

	@Test
	void negativeReal() throws OclParseException {
		assertEquals(-1.5, eval("-1.5", self));
	}

	@Test
	void negativeRealArithmetic() throws OclParseException {
		assertEquals(-3.0, eval("-1.5 + (-1.5)", self));
	}

	// --- Floor / Ceiling / Round ---

	@Test
	void floor_positive() throws OclParseException {
		assertEquals(3L, eval("(3.9).floor()", self));
	}

	@Test
	void floor_negative() throws OclParseException {
		assertEquals(-4L, eval("(-3.1).floor()", self));
	}

	@Test
	void ceiling_positive() throws OclParseException {
		assertEquals(4L, eval("(3.1).ceiling()", self));
	}

	@Test
	void ceiling_negative() throws OclParseException {
		assertEquals(-3L, eval("(-3.9).ceiling()", self));
	}

	@Test
	void round_halfUp() throws OclParseException {
		assertEquals(4L, eval("(3.5).round()", self));
	}

	@Test
	void round_halfDown() throws OclParseException {
		assertEquals(3L, eval("(3.4).round()", self));
	}

	@Test
	void round_exact() throws OclParseException {
		assertEquals(3L, eval("(3.0).round()", self));
	}

	// --- Abs ---

	@Test
	void abs_positiveReal() throws OclParseException {
		assertEquals(3.14, eval("(3.14).abs()", self));
	}

	@Test
	void abs_negativeReal() throws OclParseException {
		assertEquals(3.14, eval("(-3.14).abs()", self));
	}

	@Test
	void abs_zero() throws OclParseException {
		assertEquals(0.0, eval("(0.0).abs()", self));
	}

	// --- Max / Min ---

	@Test
	void max_reals() throws OclParseException {
		assertEquals(3.5, eval("(2.5).max(3.5)", self));
	}

	@Test
	void min_reals() throws OclParseException {
		assertEquals(2.5, eval("(2.5).min(3.5)", self));
	}

	@Test
	void max_negatives() throws OclParseException {
		assertEquals(-1.0, eval("(-1.0).max(-2.0)", self));
	}

	@Test
	void min_negatives() throws OclParseException {
		assertEquals(-2.0, eval("(-1.0).min(-2.0)", self));
	}

	// --- Real comparison ---

	@Test
	void realLessThan() throws OclParseException {
		assertEquals(true, eval("1.5 < 2.5", self));
	}

	@Test
	void realGreaterThan() throws OclParseException {
		assertEquals(true, eval("2.5 > 1.5", self));
	}

	@Test
	void realLessOrEqual() throws OclParseException {
		assertEquals(true, eval("1.5 <= 1.5", self));
	}

	@Test
	void realGreaterOrEqual() throws OclParseException {
		assertEquals(true, eval("2.5 >= 2.5", self));
	}

	@Test
	void realEqual() throws OclParseException {
		assertEquals(true, eval("1.5 = 1.5", self));
	}

	@Test
	void realNotEqual() throws OclParseException {
		assertEquals(true, eval("1.5 <> 2.5", self));
	}

	// --- Real in collections ---

	@Test
	void realSequence_sum() throws OclParseException {
		assertEquals(6.6, eval("Sequence{1.1, 2.2, 3.3}->sum()", self));
	}

	@Test
	void realSequence_min() throws OclParseException {
		assertEquals(1.1, eval("Sequence{3.3, 1.1, 2.2}->min()", self));
	}

	@Test
	void realSequence_max() throws OclParseException {
		assertEquals(3.3, eval("Sequence{3.3, 1.1, 2.2}->max()", self));
	}

	@Test
	void realSequence_sortedBy() throws OclParseException {
		assertEquals(1.1, eval(
				"Sequence{3.3, 1.1, 2.2}->sortedBy(r | r)->first()", self));
	}

	// --- toReal / toInteger ---

	@Test
	void intToReal() throws OclParseException {
		Object result = eval("42.toReal()", self);
		assertInstanceOf(Double.class, result);
		assertEquals(42.0, result);
	}

	@Test
	void realToInteger_truncates() throws OclParseException {
		Object result = eval("(3.7).toInteger()", self);
		assertInstanceOf(Long.class, result);
		assertEquals(3L, result);
	}
}
