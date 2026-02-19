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
 * Tests for numeric cross-type equality and comparison.
 * In OCL, Integer and Real are comparable: {@code 5 = 5.0} is true.
 */
class OclNumericEqualityTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Integer = Real ---

	@Test
	void intEqualReal() throws OclParseException {
		assertEquals(true, eval("5 = 5.0", self));
	}

	@Test
	void realEqualInt() throws OclParseException {
		assertEquals(true, eval("5.0 = 5", self));
	}

	@Test
	void intNotEqualReal() throws OclParseException {
		assertEquals(true, eval("5 <> 5.1", self));
	}

	@Test
	void zeroIntEqualZeroReal() throws OclParseException {
		assertEquals(true, eval("0 = 0.0", self));
	}

	@Test
	void negativeIntEqualNegativeReal() throws OclParseException {
		assertEquals(true, eval("-3 = -3.0", self));
	}

	// --- Division result equality ---

	@Test
	void divisionResultEqualInt() throws OclParseException {
		// 10 / 2 yields 5.0 (Real), should equal 5
		assertEquals(true, eval("10 / 2 = 5", self));
	}

	@Test
	void divisionResultEqualReal() throws OclParseException {
		assertEquals(true, eval("10 / 2 = 5.0", self));
	}

	@Test
	void divisionResultNotEqual() throws OclParseException {
		assertEquals(true, eval("10 / 3 <> 3", self));
	}

	// --- Cross-type comparison ---

	@Test
	void intGreaterThanReal() throws OclParseException {
		assertEquals(true, eval("6 > 5.5", self));
	}

	@Test
	void realLessThanInt() throws OclParseException {
		assertEquals(true, eval("4.5 < 5", self));
	}

	@Test
	void intGreaterEqualReal() throws OclParseException {
		assertEquals(true, eval("5 >= 5.0", self));
	}

	@Test
	void realLessEqualInt() throws OclParseException {
		assertEquals(true, eval("5.0 <= 5", self));
	}

	// --- Arithmetic mixed types ---

	@Test
	void intPlusRealEqualResult() throws OclParseException {
		assertEquals(true, eval("2 + 3.0 = 5.0", self));
	}

	@Test
	void realPlusIntEqualResult() throws OclParseException {
		assertEquals(true, eval("2.0 + 3 = 5.0", self));
	}

	@Test
	void intTimesRealEqualResult() throws OclParseException {
		assertEquals(true, eval("2 * 2.5 = 5.0", self));
	}

	// --- In collection context ---

	@Test
	void sequenceIncludesIntAsReal() throws OclParseException {
		// Sequence{1.0, 2.0, 3.0}->includes(2) — cross-type includes
		assertEquals(true, eval("Sequence{1.0, 2.0, 3.0}->includes(2)", self));
	}

	@Test
	void sequenceIncludesRealAsInt() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3}->includes(2.0)", self));
	}

	// --- Property comparison with literal ---

	@Test
	void propertyIntEqualReal() throws OclParseException {
		// age is Integer (30), compare with 30.0
		assertEquals(true, eval("self.age = 30.0", self));
	}

	@Test
	void propertySalaryEqualInt() throws OclParseException {
		// salary is Real (50000.0), compare with 50000
		assertEquals(true, eval("self.salary = 50000", self));
	}
}
