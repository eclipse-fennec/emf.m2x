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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Edge case tests for the iterate() expression.
 * Covers various accumulator types, boundary conditions,
 * and complex iterate patterns.
 */
class OclIterateEdgeCaseTest extends AbstractOclTest {

	static EObject self;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
		EObject bob = createPerson("Bob", 25, 40000.0, false);
		EObject carol = createPerson("Carol", 35, 60000.0, true);
		company = createCompany("ACME", self, bob, carol);
	}

	// --- Empty collection iterate ---

	@Test
	void iterate_emptyCollection_returnsInit() throws OclParseException {
		assertEquals(0, eval(
				"Sequence{}->iterate(i; acc : Integer = 0 | acc + 1)", self));
	}

	@Test
	void iterate_emptyCollection_stringInit() throws OclParseException {
		assertEquals("default", eval(
				"Sequence{}->iterate(i; acc : String = 'default' | acc.concat(i.toString()))", self));
	}

	// --- Single element iterate ---

	@Test
	void iterate_singleElement() throws OclParseException {
		assertEquals(42, eval(
				"Sequence{42}->iterate(i; acc : Integer = 0 | acc + i)", self));
	}

	// --- Counting iterate ---

	@Test
	void iterate_count() throws OclParseException {
		assertEquals(5, eval(
				"Sequence{10, 20, 30, 40, 50}->iterate(i; acc : Integer = 0 | acc + 1)", self));
	}

	@Test
	void iterate_countFiltered() throws OclParseException {
		// Count elements > 25
		assertEquals(3, eval(
				"Sequence{10, 20, 30, 40, 50}->iterate(i; acc : Integer = 0 | " +
						"if i > 25 then acc + 1 else acc endif)", self));
	}

	// --- String accumulator ---

	@Test
	void iterate_joinWithSeparator() throws OclParseException {
		// Build comma-separated string (simplified - has trailing comma)
		String result = (String) eval(
				"Sequence{'a', 'b', 'c'}->iterate(s; acc : String = '' | acc.concat(s).concat(','))",
				self);
		assertEquals("a,b,c,", result);
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
	void iterate_allPositive_false() throws OclParseException {
		assertEquals(false, eval(
				"Sequence{1, -2, 3}->iterate(i; acc : Boolean = true | acc and (i > 0))", self));
	}

	@Test
	void iterate_anyNegative() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, -2, 3}->iterate(i; acc : Boolean = false | acc or (i < 0))", self));
	}

	// --- Real accumulator ---

	@Test
	void iterate_realSum() throws OclParseException {
		Object result = eval(
				"Sequence{1.5, 2.5, 3.0}->iterate(r; acc : Real = 0.0 | acc + r)", self);
		assertInstanceOf(Double.class, result);
		assertEquals(7.0, result);
	}

	@Test
	void iterate_realProduct() throws OclParseException {
		assertEquals(6.0, eval(
				"Sequence{1.0, 2.0, 3.0}->iterate(r; acc : Real = 1.0 | acc * r)", self));
	}

	// --- Iterate with model data ---

	@Test
	void iterate_sumAges() throws OclParseException {
		assertEquals(90, eval(
				"self.employees->iterate(e; acc : Integer = 0 | acc + e.age)", company));
	}

	@Test
	void iterate_concatNames() throws OclParseException {
		String result = (String) eval(
				"self.employees->iterate(e; acc : String = '' | " +
						"if acc = '' then e.name else acc.concat(', ').concat(e.name) endif)",
				company);
		assertEquals("Alice, Bob, Carol", result);
	}

	@Test
	void iterate_maxAge() throws OclParseException {
		assertEquals(35, eval(
				"self.employees->iterate(e; acc : Integer = 0 | " +
						"if e.age > acc then e.age else acc endif)", company));
	}

	// --- Set iterate ---

	@Test
	void iterate_set_sum() throws OclParseException {
		assertEquals(6, eval(
				"Set{1, 2, 3}->iterate(i; acc : Integer = 0 | acc + i)", self));
	}

	// --- Complex accumulator expressions ---

	@Test
	void iterate_conditional_accumulator() throws OclParseException {
		// Sum only even numbers
		assertEquals(6, eval(
				"Sequence{1, 2, 3, 4}->iterate(i; acc : Integer = 0 | " +
						"if i.mod(2) = 0 then acc + i else acc endif)", self));
	}

	@Test
	void iterate_largeCollection() throws OclParseException {
		// Sum 1..10 using range
		assertEquals(55, eval(
				"Sequence{1..10}->iterate(i; acc : Integer = 0 | acc + i)", self));
	}

	// --- Iterate result in further expression ---

	@Test
	void iterate_resultUsedInComparison() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->iterate(i; acc : Integer = 0 | acc + i) > 5", self));
	}

	@Test
	void iterate_resultUsedInArithmetic() throws OclParseException {
		// sum(1,2,3) * 2 = 12
		assertEquals(12, eval(
				"Sequence{1, 2, 3}->iterate(i; acc : Integer = 0 | acc + i) * 2", self));
	}

	// --- Iterate in let ---

	@Test
	void let_withIterate() throws OclParseException {
		assertEquals(15, eval(
				"let total : Integer = Sequence{1, 2, 3, 4, 5}->iterate(i; acc : Integer = 0 | acc + i) in total",
				self));
	}
}
