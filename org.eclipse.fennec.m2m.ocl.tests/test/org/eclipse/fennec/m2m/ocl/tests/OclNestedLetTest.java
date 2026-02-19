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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for nested and complex let-in expressions.
 * Verifies variable scoping, shadowing, and combinations
 * with other OCL features.
 */
class OclNestedLetTest extends AbstractOclTest {

	static EObject self;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
		EObject bob = createPerson("Bob", 25, 40000.0, false);
		EObject carol = createPerson("Carol", 35, 60000.0, true);
		company = createCompany("ACME", self, bob, carol);
	}

	// --- Simple nested let ---

	@Test
	void nestedLet_twoVariables() throws OclParseException {
		assertEquals(30, eval(
				"let x : Integer = 10 in let y : Integer = 20 in x + y", self));
	}

	@Test
	void nestedLet_threeVariables() throws OclParseException {
		assertEquals(60, eval(
				"let a : Integer = 10 in let b : Integer = 20 in let c : Integer = 30 in a + b + c",
				self));
	}

	// --- Let with reference to outer variable ---

	@Test
	void nestedLet_innerUsesOuter() throws OclParseException {
		assertEquals(20, eval(
				"let x : Integer = 10 in let y : Integer = x * 2 in y", self));
	}

	@Test
	void nestedLet_chainedDependency() throws OclParseException {
		assertEquals(80, eval(
				"let a : Integer = 10 in let b : Integer = a * 2 in let c : Integer = b * 2 in c * 2",
				self));
	}

	// --- Let with model properties ---

	@Test
	void let_withPropertyAccess() throws OclParseException {
		assertEquals(31, eval(
				"let nextAge : Integer = self.age + 1 in nextAge", self));
	}

	@Test
	void let_withStringProperty() throws OclParseException {
		assertEquals("Hello, Alice", eval(
				"let greeting : String = 'Hello, ' in greeting.concat(self.name)", self));
	}

	@Test
	void nestedLet_withProperties() throws OclParseException {
		assertEquals(true, eval(
				"let n : String = self.name in let len : Integer = n.size() in len > 3", self));
	}

	// --- Let with string operations ---

	@Test
	void let_stringConcat() throws OclParseException {
		assertEquals("Alice is 30", eval(
				"let n : String = self.name in let a : String = self.age.toString() in n.concat(' is ').concat(a)",
				self));
	}

	// --- Let in collection operations ---

	@Test
	void let_inSelect() throws OclParseException {
		assertEquals(2, eval(
				"let threshold : Integer = 28 in self.employees->select(e | e.age > threshold)->size()",
				company));
	}

	@Test
	void let_inCollect() throws OclParseException {
		Object result = eval(
				"let bonus : Integer = 1000 in self.employees->collect(e | e.salary + bonus)",
				company);
		assertEquals(List.of(51000.0, 41000.0, 61000.0), result);
	}

	@Test
	void let_inForAll() throws OclParseException {
		assertEquals(true, eval(
				"let minAge : Integer = 18 in self.employees->forAll(e | e.age >= minAge)",
				company));
	}

	@Test
	void let_inExists() throws OclParseException {
		assertEquals(true, eval(
				"let target : String = 'Bob' in self.employees->exists(e | e.name = target)",
				company));
	}

	// --- Let with if-then-else ---

	@Test
	void let_inIfCondition() throws OclParseException {
		assertEquals("senior", eval(
				"let age : Integer = self.age in if age >= 30 then 'senior' else 'junior' endif",
				self));
	}

	@Test
	void let_inIfBranches() throws OclParseException {
		assertEquals(50, eval(
				"let x : Integer = 10 in if true then let y : Integer = 50 in y else 0 endif",
				self));
	}

	// --- Let with arithmetic ---

	@Test
	void let_complexArithmetic() throws OclParseException {
		assertEquals(150, eval(
				"let base : Integer = 100 in let factor : Integer = 3 in let offset : Integer = base.div(2) in offset * factor",
				self));
	}

	@Test
	void let_realArithmetic() throws OclParseException {
		assertEquals(15.0, eval(
				"let pi : Real = 3.0 in let r : Real = 5.0 in pi * r", self));
	}

	// --- Let with boolean logic ---

	@Test
	void let_booleanCombination() throws OclParseException {
		assertEquals(true, eval(
				"let isOld : Boolean = self.age >= 30 in let isRich : Boolean = self.salary > 40000.0 in isOld and isRich",
				self));
	}

	@Test
	void let_negation() throws OclParseException {
		assertEquals(false, eval(
				"let flag : Boolean = true in not flag", self));
	}

	// --- Let with collection literal ---

	@Test
	void let_withSequence() throws OclParseException {
		assertEquals(6, eval(
				"let nums : Sequence(Integer) = Sequence{1, 2, 3} in nums->sum()", self));
	}

	@Test
	void let_withSet() throws OclParseException {
		assertEquals(3, eval(
				"let items : Set(Integer) = Set{1, 2, 3} in items->size()", self));
	}

	// --- Scoping ---

	@Test
	void let_selfStillAccessible() throws OclParseException {
		assertEquals("Alice", eval(
				"let x : Integer = 42 in self.name", self));
	}

	@Test
	void let_outerVariable_notShadowed() throws OclParseException {
		assertEquals(52, eval(
				"let x : Integer = 10 in let y : Integer = 42 in x + y", self));
	}

	// --- Let result types ---

	@Test
	void let_returnsString() throws OclParseException {
		assertEquals("result", eval(
				"let x : String = 'result' in x", self));
	}

	@Test
	void let_returnsBoolean() throws OclParseException {
		assertEquals(true, eval(
				"let x : Boolean = true in x", self));
	}

	@Test
	void let_returnsReal() throws OclParseException {
		assertEquals(3.14, eval(
				"let x : Real = 3.14 in x", self));
	}
}
