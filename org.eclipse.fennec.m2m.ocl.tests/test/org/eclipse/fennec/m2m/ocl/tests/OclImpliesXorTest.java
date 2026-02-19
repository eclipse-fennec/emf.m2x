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
 * Tests for OCL {@code implies} and {@code xor} boolean operators.
 * Truth table coverage for all combinations.
 */
class OclImpliesXorTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- implies truth table ---

	@Test
	void implies_TT() throws OclParseException {
		assertEquals(true, eval("true implies true", self));
	}

	@Test
	void implies_TF() throws OclParseException {
		assertEquals(false, eval("true implies false", self));
	}

	@Test
	void implies_FT() throws OclParseException {
		assertEquals(true, eval("false implies true", self));
	}

	@Test
	void implies_FF() throws OclParseException {
		assertEquals(true, eval("false implies false", self));
	}

	// --- implies with expressions ---

	@Test
	void implies_comparison() throws OclParseException {
		// 5 > 3 implies 10 > 1 → true implies true → true
		assertEquals(true, eval("5 > 3 implies 10 > 1", self));
	}

	@Test
	void implies_falseAntecedent() throws OclParseException {
		// 1 > 5 implies false → false implies false → true
		assertEquals(true, eval("1 > 5 implies false", self));
	}

	@Test
	void implies_trueImpliesFalse() throws OclParseException {
		assertEquals(false, eval("1 < 5 implies 1 > 5", self));
	}

	// --- implies with property ---

	@Test
	void implies_property() throws OclParseException {
		// isMarried implies age > 0 → true implies true → true
		assertEquals(true, eval("self.isMarried implies self.age > 0", self));
	}

	// --- implies chained ---

	@Test
	void implies_chained() throws OclParseException {
		// (true implies true) implies true → true implies true → true
		assertEquals(true, eval("(true implies true) implies true", self));
	}

	@Test
	void implies_chainedWithFalse() throws OclParseException {
		// (true implies false) implies true → false implies true → true
		assertEquals(true, eval("(true implies false) implies true", self));
	}

	// --- implies in let ---

	@Test
	void implies_inLet() throws OclParseException {
		assertEquals(true, eval(
				"let x: Boolean = true in x implies x", self));
	}

	// --- implies in forAll ---

	@Test
	void implies_inForAll() throws OclParseException {
		// For all i: i > 3 implies i > 0 → always true
		assertEquals(true, eval(
				"Sequence{1, 2, 3, 4, 5}->forAll(i | i > 3 implies i > 0)", self));
	}

	// --- xor truth table ---

	@Test
	void xor_TT() throws OclParseException {
		assertEquals(false, eval("true xor true", self));
	}

	@Test
	void xor_TF() throws OclParseException {
		assertEquals(true, eval("true xor false", self));
	}

	@Test
	void xor_FT() throws OclParseException {
		assertEquals(true, eval("false xor true", self));
	}

	@Test
	void xor_FF() throws OclParseException {
		assertEquals(false, eval("false xor false", self));
	}

	// --- xor with expressions ---

	@Test
	void xor_sameTrue() throws OclParseException {
		assertEquals(false, eval("1 < 2 xor 3 < 4", self));
	}

	@Test
	void xor_different() throws OclParseException {
		assertEquals(true, eval("1 < 2 xor 3 > 4", self));
	}

	@Test
	void xor_sameFalse() throws OclParseException {
		assertEquals(false, eval("1 > 2 xor 3 > 4", self));
	}

	// --- xor with properties ---

	@Test
	void xor_properties() throws OclParseException {
		// isMarried=true xor age>0=true → false
		assertEquals(false, eval("self.isMarried xor self.age > 0", self));
	}

	// --- xor in let ---

	@Test
	void xor_inLet() throws OclParseException {
		assertEquals(false, eval(
				"let x: Boolean = true in x xor x", self));
	}

	// --- xor combined with and/or ---

	@Test
	void xor_withAnd() throws OclParseException {
		// (true xor false) and true → true and true → true
		assertEquals(true, eval("(true xor false) and true", self));
	}

	@Test
	void xor_withOr() throws OclParseException {
		// (false xor false) or true → false or true → true
		assertEquals(true, eval("(false xor false) or true", self));
	}

	// --- implies and xor combined ---

	@Test
	void implies_xor_combined() throws OclParseException {
		// (true xor false) implies true → true implies true → true
		assertEquals(true, eval("(true xor false) implies true", self));
	}

	@Test
	void xor_implies_combined() throws OclParseException {
		// true xor (true implies false) → true xor false → true
		assertEquals(true, eval("true xor (true implies false)", self));
	}

	// --- In collection expressions ---

	@Test
	void xor_inSelect() throws OclParseException {
		// Elements where (i > 2) xor (i > 4): true for 3,4 (>2 but not >4)
		// i=1: F xor F = F; i=2: F xor F = F; i=3: T xor F = T;
		// i=4: T xor F = T; i=5: T xor T = F
		assertEquals(2, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | (i > 2) xor (i > 4))->size()",
				self));
	}
}
