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
 * Advanced operator precedence tests covering arithmetic,
 * comparison, boolean, and collection operator interactions.
 */
class OclOperatorPrecedenceAdvancedTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Arithmetic precedence ---

	@Test
	void multiplyBeforeAdd() throws OclParseException {
		assertEquals(14, eval("2 + 3 * 4", self));
	}

	@Test
	void multiplyBeforeSubtract() throws OclParseException {
		assertEquals(-10, eval("2 - 3 * 4", self));
	}

	@Test
	void divideBeforeAdd() throws OclParseException {
		assertEquals(5.0, eval("2 + 12 / 4", self));
	}

	@Test
	void parenthesesOverride() throws OclParseException {
		assertEquals(20, eval("(2 + 3) * 4", self));
	}

	@Test
	void nestedParentheses() throws OclParseException {
		assertEquals(14, eval("(2 + (3 * 4))", self));
	}

	@Test
	void unaryMinusPrecedence() throws OclParseException {
		assertEquals(-6, eval("-2 * 3", self));
	}

	@Test
	void unaryMinusWithAdd() throws OclParseException {
		assertEquals(1, eval("-2 + 3", self));
	}

	// --- Comparison + Boolean precedence ---

	@Test
	void comparisonBeforeAnd() throws OclParseException {
		// 3 > 2 and 4 > 3 → true and true → true
		assertEquals(true, eval("3 > 2 and 4 > 3", self));
	}

	@Test
	void comparisonBeforeOr() throws OclParseException {
		assertEquals(true, eval("3 > 2 or 4 < 3", self));
	}

	@Test
	void andBeforeOr() throws OclParseException {
		// false and true or true → (false and true) or true → false or true → true
		assertEquals(true, eval("false and true or true", self));
	}

	@Test
	void andBeforeOr_reverse() throws OclParseException {
		// true or true and false → true or (true and false) → true or false → true
		assertEquals(true, eval("true or true and false", self));
	}

	@Test
	void notPrecedence() throws OclParseException {
		assertEquals(false, eval("not true", self));
	}

	@Test
	void notBeforeAnd() throws OclParseException {
		// not true and false → (not true) and false → false and false → false
		assertEquals(false, eval("not true and false", self));
	}

	@Test
	void notWithParentheses() throws OclParseException {
		// not (true and false) → not false → true
		assertEquals(true, eval("not (true and false)", self));
	}

	// --- Implies precedence ---

	@Test
	void impliesPrecedence() throws OclParseException {
		// true and false implies true → (true and false) implies true → false implies true → true
		assertEquals(true, eval("true and false implies true", self));
	}

	@Test
	void impliesRightAssociation() throws OclParseException {
		// true implies true implies false
		// If right-assoc: true implies (true implies false) = true implies false = false
		// If left-assoc:  (true implies true) implies false = true implies false = false
		assertEquals(false, eval("true implies true implies false", self));
	}

	// --- Arithmetic + Comparison ---

	@Test
	void arithmeticThenComparison() throws OclParseException {
		// 2 + 3 > 4 → 5 > 4 → true
		assertEquals(true, eval("2 + 3 > 4", self));
	}

	@Test
	void comparisonBothSidesArithmetic() throws OclParseException {
		assertEquals(true, eval("2 * 3 > 1 + 4", self));
	}

	@Test
	void comparisonEquality() throws OclParseException {
		assertEquals(true, eval("2 + 3 = 5", self));
	}

	@Test
	void comparisonNotEqual() throws OclParseException {
		assertEquals(true, eval("2 + 3 <> 6", self));
	}

	// --- Mixed complex expressions ---

	@Test
	void complexMixed() throws OclParseException {
		// (2 + 3) * 4 > 15 and 10 / 2 = 5
		assertEquals(true, eval("(2 + 3) * 4 > 15 and 10 / 2 = 5", self));
	}

	@Test
	void complexWithNot() throws OclParseException {
		// not (2 > 3) and 4 > 1
		assertEquals(true, eval("not (2 > 3) and 4 > 1", self));
	}

	// --- Arrow vs dot vs minus ---

	@Test
	void arrowBindsTighterThanMinus() throws OclParseException {
		// Sequence{1,2,3}->size() - 1 → 3 - 1 → 2
		assertEquals(2, eval("Sequence{1, 2, 3}->size() - 1", self));
	}

	@Test
	void dotBindsTighterThanArrow() throws OclParseException {
		// self.name.size() → 5 (Alice)
		assertEquals(5, eval("self.name.size()", self));
	}

	// --- Parenthesized collection expressions ---

	@Test
	void parenCollectionDifference() throws OclParseException {
		assertEquals(1, eval("(Set{1, 2, 3} - Set{2, 3})->size()", self));
	}

	@Test
	void collectionSizePlusLiteral() throws OclParseException {
		assertEquals(13, eval("Sequence{1, 2, 3}->size() + 10", self));
	}
}
