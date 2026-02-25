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
package org.eclipse.fennec.m2x.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@code iterate} expression, including typed iterator variables,
 * typed accumulators, and various accumulation patterns.
 *
 * <p>Covers branches in OclAstBuilder.createIterateExp (iterType, accType)
 * and OclEvaluator.caseIterateExp.
 */
class OclIterateExpressionTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject charlie;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 65000.0, true);
		bob = createPerson("Bob", 22, 42000.0, false);
		charlie = createPerson("Charlie", 45, 95000.0, true);
		company = createCompany("TestCorp", alice, bob, charlie);
	}

	// === Basic iterate: sum ===

	@Test
	void iterate_sumAges() throws OclParseException {
		// Sum ages using iterate with typed accumulator
		Object result = eval(
				"self.employees->iterate(e; acc : Integer = 0 | acc + e.age)",
				company);
		assertEquals(97, result);
	}

	@Test
	void iterate_sumSalaries() throws OclParseException {
		Object result = eval(
				"self.employees->iterate(e; acc : Real = 0.0 | acc + e.salary)",
				company);
		assertEquals(202000.0, result);
	}

	// === Iterate: count ===

	@Test
	void iterate_countMarried() throws OclParseException {
		Object result = eval(
				"self.employees->iterate(e; acc : Integer = 0 | if e.isMarried then acc + 1 else acc endif)",
				company);
		assertEquals(2, result);
	}

	// === Iterate: string concatenation ===

	@Test
	void iterate_concatNames() throws OclParseException {
		Object result = eval(
				"self.employees->iterate(e; acc : String = '' | acc.concat(e.name).concat(','))",
				company);
		assertEquals("Alice,Bob,Charlie,", result);
	}

	// === Iterate with typed iterator ===

	@Test
	void iterate_typedIterator_sum() throws OclParseException {
		// Explicitly typed iterator variable
		Object result = eval(
				"self.employees->iterate(e : Person; acc : Integer = 0 | acc + e.age)",
				company);
		assertEquals(97, result);
	}

	// === Iterate on literal collection ===

	@Test
	void iterate_sequenceLiteral_sum() throws OclParseException {
		Object result = eval(
				"Sequence{1, 2, 3, 4, 5}->iterate(x; acc : Integer = 0 | acc + x)",
				alice);
		assertEquals(15, result);
	}

	@Test
	void iterate_sequenceLiteral_product() throws OclParseException {
		Object result = eval(
				"Sequence{1, 2, 3, 4}->iterate(x; acc : Integer = 1 | acc * x)",
				alice);
		assertEquals(24, result);
	}

	@Test
	void iterate_setLiteral_concat() throws OclParseException {
		Object result = eval(
				"OrderedSet{'a', 'b', 'c'}->iterate(s; acc : String = '' | acc.concat(s))",
				alice);
		assertEquals("abc", result);
	}

	// === Iterate: max ===

	@Test
	void iterate_max() throws OclParseException {
		Object result = eval(
				"self.employees->iterate(e; acc : Integer = 0 | if e.age > acc then e.age else acc endif)",
				company);
		assertEquals(45, result);
	}

	// === Iterate: min ===

	@Test
	void iterate_min() throws OclParseException {
		Object result = eval(
				"self.employees->iterate(e; acc : Integer = 999 | if e.age < acc then e.age else acc endif)",
				company);
		assertEquals(22, result);
	}

	// === Iterate with boolean accumulator ===

	@Test
	void iterate_allPositive() throws OclParseException {
		Object result = eval(
				"self.employees->iterate(e; acc : Boolean = true | acc and (e.age > 0))",
				company);
		assertEquals(true, result);
	}

	@Test
	void iterate_anyNegative() throws OclParseException {
		Object result = eval(
				"self.employees->iterate(e; acc : Boolean = false | acc or (e.age < 0))",
				company);
		assertEquals(false, result);
	}

	// === Iterate: empty collection ===

	@Test
	void iterate_emptyCollection() throws OclParseException {
		Object result = eval(
				"Sequence{}->iterate(x; acc : Integer = 42 | acc + x)",
				alice);
		assertEquals(42, result);
	}

	// === Iterate on single element ===

	@Test
	void iterate_singleElement() throws OclParseException {
		Object result = eval(
				"Sequence{7}->iterate(x; acc : Integer = 0 | acc + x)",
				alice);
		assertEquals(7, result);
	}
}
