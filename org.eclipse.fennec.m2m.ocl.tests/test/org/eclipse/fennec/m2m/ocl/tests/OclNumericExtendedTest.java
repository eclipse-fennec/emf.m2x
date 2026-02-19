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
 * Extended tests for OCL numeric operations: abs, floor, ceiling, round,
 * min, max, div, mod, toReal, toInteger.
 *
 * <p>Ported from Eclipse OCL {@code EvaluateNumericOperationsTest4}.
 */
class OclNumericExtendedTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- abs ---

	@Test
	void abs_positiveInteger() throws OclParseException {
		assertEquals(3L, eval("3.abs()", self));
	}

	@Test
	void abs_negativeInteger() throws OclParseException {
		assertEquals(3L, eval("(-3).abs()", self));
	}

	@Test
	void abs_zero() throws OclParseException {
		assertEquals(0L, eval("0.abs()", self));
	}

	@Test
	void abs_positiveReal() throws OclParseException {
		assertEquals(3.14, eval("(3.14).abs()", self));
	}

	@Test
	void abs_negativeReal() throws OclParseException {
		assertEquals(3.14, eval("(-3.14).abs()", self));
	}

	// --- floor ---

	@Test
	void floor_positiveReal() throws OclParseException {
		assertEquals(3L, eval("(3.7).floor()", self));
	}

	@Test
	void floor_negativeReal() throws OclParseException {
		assertEquals(-2L, eval("(-1.5).floor()", self));
	}

	@Test
	void floor_integer() throws OclParseException {
		// floor on integer is not a standard op, but let's see if it works as toInteger
		assertEquals(3L, eval("(3.0).floor()", self));
	}

	// --- ceiling ---

	@Test
	void ceiling_positiveReal() throws OclParseException {
		assertEquals(4L, eval("(3.2).ceiling()", self));
	}

	@Test
	void ceiling_negativeReal() throws OclParseException {
		assertEquals(-1L, eval("(-1.5).ceiling()", self));
	}

	@Test
	void ceiling_exact() throws OclParseException {
		assertEquals(3L, eval("(3.0).ceiling()", self));
	}

	// --- round ---

	@Test
	void round_up() throws OclParseException {
		assertEquals(4L, eval("(3.5).round()", self));
	}

	@Test
	void round_down() throws OclParseException {
		assertEquals(3L, eval("(3.4).round()", self));
	}

	@Test
	void round_negative() throws OclParseException {
		assertEquals(-1L, eval("(-1.5).round()", self));
	}

	// --- min / max (Integer) ---

	@Test
	void max_integers() throws OclParseException {
		assertEquals(5L, eval("3.max(5)", self));
	}

	@Test
	void max_integers_firstLarger() throws OclParseException {
		assertEquals(5L, eval("5.max(3)", self));
	}

	@Test
	void min_integers() throws OclParseException {
		assertEquals(3L, eval("3.min(5)", self));
	}

	@Test
	void min_integers_firstSmaller() throws OclParseException {
		assertEquals(3L, eval("5.min(3)", self));
	}

	// --- min / max (Real) ---

	@Test
	void max_reals() throws OclParseException {
		assertEquals(5.5, eval("(3.2).max(5.5)", self));
	}

	@Test
	void min_reals() throws OclParseException {
		assertEquals(3.2, eval("(3.2).min(5.5)", self));
	}

	// --- min / max (mixed Integer/Real) ---

	@Test
	void max_integerReal() throws OclParseException {
		assertEquals(5.5, eval("3.max(5.5)", self));
	}

	@Test
	void min_integerReal() throws OclParseException {
		assertEquals(3.0, eval("3.min(5.5)", self));
	}

	// --- div / mod ---

	@Test
	void div_positive() throws OclParseException {
		assertEquals(1L, eval("3.div(2)", self));
	}

	@Test
	void div_negative() throws OclParseException {
		assertEquals(-1L, eval("(-3).div(2)", self));
	}

	@Test
	void div_byZero() throws OclParseException {
		assertInvalid("1.div(0)", self);
	}

	@Test
	void mod_positive() throws OclParseException {
		assertEquals(1L, eval("7.mod(3)", self));
	}

	@Test
	void mod_zero() throws OclParseException {
		assertEquals(0L, eval("6.mod(3)", self));
	}

	@Test
	void mod_byZero() throws OclParseException {
		assertInvalid("1.mod(0)", self);
	}

	// --- toReal / toInteger ---

	@Test
	void toReal_fromInteger() throws OclParseException {
		assertEquals(3.0, eval("3.toReal()", self));
	}

	@Test
	void toInteger_fromReal() throws OclParseException {
		assertEquals(3L, eval("(3.7).toInteger()", self));
	}

	// --- Division always returns Real ---

	@Test
	void divide_intByInt_returnsReal() throws OclParseException {
		assertEquals(1.0, eval("1 / 1", self));
	}

	@Test
	void divide_intByInt_fraction() throws OclParseException {
		assertEquals(-0.25, eval("1 / -4", self));
	}

	@Test
	void divide_byZero() throws OclParseException {
		assertInvalid("1 / 0", self);
	}

	@Test
	void divide_realByZero() throws OclParseException {
		assertInvalid("1.0 / 0.0", self);
	}
}
