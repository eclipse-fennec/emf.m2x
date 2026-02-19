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
 * Advanced tests for the OCL iterate expression with accumulator.
 * iterate(var; acc : Type = init | body)
 */
class OclIterateAdvancedTest extends AbstractOclTest {

	static EObject self;
	static EObject company;
	static EObject alice;
	static EObject bob;
	static EObject charlie;

	@BeforeAll
	static void setUp() {
		self = createPerson("Test", 25, 40000.0, false);
		alice = createPerson("Alice", 30, 60000.0, true);
		bob = createPerson("Bob", 25, 45000.0, false);
		charlie = createPerson("Charlie", 35, 80000.0, true);
		company = createCompany("ACME", alice, bob, charlie);
	}

	// --- Basic iterate patterns ---

	@Test
	void iterate_sum() throws OclParseException {
		assertEquals(15, eval(
				"Sequence{1, 2, 3, 4, 5}->iterate(i; acc : Integer = 0 | acc + i)", self));
	}

	@Test
	void iterate_product() throws OclParseException {
		assertEquals(120, eval(
				"Sequence{1, 2, 3, 4, 5}->iterate(i; acc : Integer = 1 | acc * i)", self));
	}

	@Test
	void iterate_max() throws OclParseException {
		assertEquals(5, eval(
				"Sequence{3, 1, 5, 2, 4}->iterate(i; acc : Integer = 0 | if i > acc then i else acc endif)", self));
	}

	@Test
	void iterate_min() throws OclParseException {
		assertEquals(1, eval(
				"Sequence{3, 1, 5, 2, 4}->iterate(i; acc : Integer = 999 | if i < acc then i else acc endif)", self));
	}

	// --- String accumulator ---

	@Test
	void iterate_joinWithSeparator() throws OclParseException {
		assertEquals("a-b-c", eval(
				"Sequence{'a', 'b', 'c'}->iterate(s; acc : String = '' | " +
				"if acc = '' then s else acc.concat('-').concat(s) endif)", self));
	}

	@Test
	void iterate_reverseString() throws OclParseException {
		assertEquals("cba", eval(
				"Sequence{'a', 'b', 'c'}->iterate(s; acc : String = '' | s.concat(acc))", self));
	}

	// --- Boolean accumulator ---

	@Test
	void iterate_allPositive() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->iterate(i; acc : Boolean = true | acc and (i > 0))", self));
	}

	@Test
	void iterate_anyNegative() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, -2, 3}->iterate(i; acc : Boolean = false | acc or (i < 0))", self));
	}

	// --- Counting with iterate ---

	@Test
	void iterate_countPositive() throws OclParseException {
		assertEquals(3, eval(
				"Sequence{-1, 2, 3, -4, 5}->iterate(i; acc : Integer = 0 | " +
				"if i > 0 then acc + 1 else acc endif)", self));
	}

	// --- Model-based iterate ---

	@Test
	void iterate_sumSalaries() throws OclParseException {
		assertEquals(185000.0, eval(
				"self.employees->iterate(e; acc : Real = 0.0 | acc + e.salary)", company));
	}

	@Test
	void iterate_concatNames() throws OclParseException {
		assertEquals("Alice, Bob, Charlie", eval(
				"self.employees->iterate(e; acc : String = '' | " +
				"if acc = '' then e.name else acc.concat(', ').concat(e.name) endif)", company));
	}

	// --- Empty collection ---

	@Test
	void iterate_emptyReturnsInit() throws OclParseException {
		assertEquals(42, eval(
				"Sequence{}->iterate(i; acc : Integer = 42 | acc + i)", self));
	}

	// --- Real accumulator ---

	@Test
	void iterate_average() throws OclParseException {
		// Manually compute average: sum / count
		assertEquals(3.0, eval(
				"let total: Real = Sequence{1, 2, 3, 4, 5}->iterate(i; acc : Real = 0.0 | acc + i) " +
				"in total / 5.0", self));
	}

	// --- Iterate on Set ---

	@Test
	void iterate_onSet() throws OclParseException {
		// Sum elements of a set
		assertEquals(6, eval(
				"Set{1, 2, 3}->iterate(i; acc : Integer = 0 | acc + i)", self));
	}

	// --- Single element ---

	@Test
	void iterate_singleElement() throws OclParseException {
		assertEquals(10, eval(
				"Sequence{10}->iterate(i; acc : Integer = 0 | acc + i)", self));
	}
}
