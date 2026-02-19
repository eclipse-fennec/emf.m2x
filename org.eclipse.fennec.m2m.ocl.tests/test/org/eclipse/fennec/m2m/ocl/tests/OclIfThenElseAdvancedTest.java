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

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Advanced tests for OCL if-then-else expressions.
 * Covers nested if, if in iterators, if in let, if with
 * model properties, and if producing different types.
 */
class OclIfThenElseAdvancedTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 60000.0, true);
		bob = createPerson("Bob", 25, 45000.0, false);
		company = createCompany("ACME", alice, bob);
	}

	// --- Nested if-then-else (cascading) ---

	@Test
	void nestedIf_threeWay() throws OclParseException {
		assertEquals("medium", eval(
				"if self.age > 50 then 'old' " +
				"else if self.age > 18 then 'medium' " +
				"else 'young' endif endif", alice));
	}

	@Test
	void nestedIf_fourWay() throws OclParseException {
		assertEquals("B", eval(
				"if self.salary > 100000.0 then 'A' " +
				"else if self.salary > 50000.0 then 'B' " +
				"else if self.salary > 30000.0 then 'C' " +
				"else 'D' endif endif endif", alice));
	}

	@Test
	void nestedIf_inThen() throws OclParseException {
		assertEquals("married adult", eval(
				"if self.age >= 18 then " +
				"  if self.isMarried then 'married adult' else 'single adult' endif " +
				"else 'minor' endif", alice));
	}

	// --- If in let ---

	@Test
	void if_inLetBody() throws OclParseException {
		assertEquals("senior", eval(
				"let age: Integer = self.age in " +
				"if age >= 30 then 'senior' else 'junior' endif", alice));
	}

	@Test
	void if_inLetInit() throws OclParseException {
		assertEquals(true, eval(
				"let label: String = if self.isMarried then 'M' else 'S' endif " +
				"in label = 'M'", alice));
	}

	// --- If in iterator ---

	@Test
	void if_inCollect() throws OclParseException {
		Object result = eval(
				"self.employees->collect(e | " +
				"  if e.isMarried then 'married' else 'single' endif)",
				company);
		assertInstanceOf(Collection.class, result);
		Collection<?> labels = (Collection<?>) result;
		assertEquals(2, labels.size());
	}

	@Test
	void if_inSelect_condition() throws OclParseException {
		// Select based on conditional expression
		assertEquals(1L, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | " +
				"  if i.mod(2) = 0 then i > 3 else false endif)->size()",
				alice)); // only 4 matches
	}

	@Test
	void if_inForAll() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e | " +
				"  if e.isMarried then e.salary > 50000.0 else true endif)",
				company));
	}

	// --- If producing integers ---

	@Test
	void if_integerResult() throws OclParseException {
		assertEquals(1L, eval("if true then 1 else 2 endif", alice));
	}

	@Test
	void if_integerResultUsedInArithmetic() throws OclParseException {
		assertEquals(11L, eval(
				"(if self.isMarried then 1 else 0 endif) + 10", alice));
	}

	// --- If producing collections ---

	@Test
	void if_collectionResult() throws OclParseException {
		assertEquals(3L, eval(
				"(if true then Sequence{1, 2, 3} else Sequence{4, 5} endif)->size()",
				alice));
	}

	// --- If with boolean conditions ---

	@Test
	void if_andCondition() throws OclParseException {
		assertEquals("yes", eval(
				"if self.age > 18 and self.isMarried then 'yes' else 'no' endif", alice));
	}

	@Test
	void if_orCondition() throws OclParseException {
		assertEquals("yes", eval(
				"if self.age > 100 or self.isMarried then 'yes' else 'no' endif", alice));
	}

	@Test
	void if_notCondition() throws OclParseException {
		assertEquals("yes", eval(
				"if not (self.age < 18) then 'yes' else 'no' endif", alice));
	}

	@Test
	void if_impliesCondition() throws OclParseException {
		assertEquals("yes", eval(
				"if self.isMarried implies self.age >= 18 then 'yes' else 'no' endif", alice));
	}

	// --- If with comparison in both branches ---

	@Test
	void if_comparisonBranches() throws OclParseException {
		assertEquals(true, eval(
				"if self.salary > 50000.0 then self.age >= 25 else self.age < 25 endif",
				alice));
	}

	// --- If with string concat ---

	@Test
	void if_stringConcat() throws OclParseException {
		assertEquals("Hello Alice", eval(
				"'Hello ' + (if self.isMarried then self.name else 'stranger' endif)",
				alice));
	}

	// --- Deeply nested ---

	@Test
	void deeplyNestedIf_5levels() throws OclParseException {
		assertEquals("e", eval(
				"if false then 'a' " +
				"else if false then 'b' " +
				"else if false then 'c' " +
				"else if false then 'd' " +
				"else 'e' endif endif endif endif", alice));
	}

	// --- If in iterate ---

	@Test
	void if_inIterate() throws OclParseException {
		assertEquals(6L, eval(
				"Sequence{1, 2, 3, 4, 5}->iterate(i; acc: Integer = 0 | " +
				"  acc + (if i.mod(2) = 0 then i else 0 endif))",
				alice)); // 0+2+0+4+0 = 6
	}
}
