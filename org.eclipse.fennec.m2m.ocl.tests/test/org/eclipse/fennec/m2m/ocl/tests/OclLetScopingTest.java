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
 * Tests for OCL let expression scoping rules.
 * Verifies nested lets, variable shadowing, multi-variable lets,
 * and interaction with other expression forms.
 */
class OclLetScopingTest extends AbstractOclTest {

	static EObject self;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
		company = createCompany("ACME", self);
	}

	// --- Basic let ---

	@Test
	void let_simpleBinding() throws OclParseException {
		assertEquals(42, eval("let x: Integer = 42 in x", self));
	}

	@Test
	void let_stringBinding() throws OclParseException {
		assertEquals("hello", eval("let s: String = 'hello' in s", self));
	}

	@Test
	void let_booleanBinding() throws OclParseException {
		assertEquals(true, eval("let b: Boolean = true in b", self));
	}

	@Test
	void let_realBinding() throws OclParseException {
		assertEquals(3.14, eval("let pi: Real = 3.14 in pi", self));
	}

	// --- Let with computation ---

	@Test
	void let_computedValue() throws OclParseException {
		assertEquals(15, eval("let x: Integer = 5 * 3 in x", self));
	}

	@Test
	void let_usedInExpression() throws OclParseException {
		assertEquals(10, eval("let x: Integer = 5 in x + x", self));
	}

	@Test
	void let_usedInComparison() throws OclParseException {
		assertEquals(true, eval("let x: Integer = 5 in x > 3", self));
	}

	// --- Nested lets ---

	@Test
	void nestedLet_twoLevels() throws OclParseException {
		assertEquals(3, eval(
				"let x: Integer = 1 in let y: Integer = 2 in x + y", self));
	}

	@Test
	void nestedLet_threeLevels() throws OclParseException {
		assertEquals(6, eval(
				"let a: Integer = 1 in let b: Integer = 2 in let c: Integer = 3 in a + b + c",
				self));
	}

	@Test
	void nestedLet_innerUsesOuter() throws OclParseException {
		assertEquals(10, eval(
				"let x: Integer = 5 in let y: Integer = x * 2 in y", self));
	}

	@Test
	void nestedLet_outerNotAffectedByInner() throws OclParseException {
		// This tests that the outer 'x' is still accessible after inner let
		assertEquals(7, eval(
				"let x: Integer = 3 in let y: Integer = 4 in x + y", self));
	}

	// --- Variable shadowing ---

	@Test
	void let_shadowsOuterVariable() throws OclParseException {
		// Inner 'x' should shadow outer 'x'
		assertEquals(10, eval(
				"let x: Integer = 5 in let x: Integer = 10 in x", self));
	}

	@Test
	void let_shadowDoesNotAffectOuter() throws OclParseException {
		// After inner scope, outer x should still be accessible
		// But in OCL, let defines a single in-body, so we can't test "after"
		// Instead test that outer is still used correctly before shadowing
		assertEquals(15, eval(
				"let x: Integer = 5 in x + (let x: Integer = 10 in x)", self));
	}

	// --- Multi-variable let (comma-separated) ---

	@Test
	void let_multiVariable() throws OclParseException {
		assertEquals(30, eval(
				"let x: Integer = 10, y: Integer = 20 in x + y", self));
	}

	@Test
	void let_multiVariable_dependsOnPrevious() throws OclParseException {
		// Second variable depends on first
		assertEquals(25, eval(
				"let x: Integer = 5, y: Integer = x * 5 in y", self));
	}

	@Test
	void let_multiVariable_threeVars() throws OclParseException {
		assertEquals(60, eval(
				"let a: Integer = 10, b: Integer = 20, c: Integer = 30 in a + b + c",
				self));
	}

	// --- Let with model access ---

	@Test
	void let_withPropertyAccess() throws OclParseException {
		assertEquals(true, eval(
				"let n: String = self.name in n = 'Alice'", self));
	}

	@Test
	void let_withSelfNavigation() throws OclParseException {
		assertEquals(true, eval(
				"let age: Integer = self.age in age >= 18", self));
	}

	// --- Let in if-then-else ---

	@Test
	void let_inIfCondition() throws OclParseException {
		assertEquals("adult", eval(
				"let age: Integer = self.age in if age >= 18 then 'adult' else 'minor' endif",
				self));
	}

	@Test
	void let_inIfBranches() throws OclParseException {
		assertEquals(true, eval(
				"if true then let x: Integer = 1 in x = 1 else false endif",
				self));
	}

	// --- Let in iterator ---

	@Test
	void let_inIteratorBody() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->forAll(i | let doubled: Integer = i * 2 in doubled > 0)",
				self));
	}

	@Test
	void let_outsideIterator() throws OclParseException {
		assertEquals(true, eval(
				"let threshold: Integer = 2 in Sequence{3, 4, 5}->forAll(i | i > threshold)",
				self));
	}

	// --- Let with collection expressions ---

	@Test
	void let_collectionInLet() throws OclParseException {
		assertEquals(3, eval(
				"let nums: Sequence(Integer) = Sequence{1, 2, 3} in nums->size()",
				self));
	}

	@Test
	void let_collectionSumInLet() throws OclParseException {
		assertEquals(6, eval(
				"let nums: Sequence(Integer) = Sequence{1, 2, 3} in nums->sum()",
				self));
	}
}
