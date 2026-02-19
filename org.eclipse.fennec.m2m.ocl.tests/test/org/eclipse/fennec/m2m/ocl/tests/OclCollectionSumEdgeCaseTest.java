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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Edge case tests for collection sum(), including empty collections,
 * single elements, mixed int/real, and with model data.
 */
class OclCollectionSumEdgeCaseTest extends AbstractOclTest {

	static EObject self;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
		EObject bob = createPerson("Bob", 25, 40000.0, false);
		EObject carol = createPerson("Carol", 35, 60000.0, true);
		company = createCompany("ACME", self, bob, carol);
	}

	// --- Sum on integer sequences ---

	@Test
	void sum_integers() throws OclParseException {
		assertEquals(15, eval("Sequence{1, 2, 3, 4, 5}->sum()", self));
	}

	@Test
	void sum_singleInteger() throws OclParseException {
		assertEquals(42, eval("Sequence{42}->sum()", self));
	}

	@Test
	void sum_negativeIntegers() throws OclParseException {
		assertEquals(-6, eval("Sequence{-1, -2, -3}->sum()", self));
	}

	@Test
	void sum_mixedSignIntegers() throws OclParseException {
		assertEquals(0, eval("Sequence{-5, 0, 5}->sum()", self));
	}

	// --- Sum on real sequences ---

	@Test
	void sum_reals() throws OclParseException {
		Object result = eval("Sequence{1.5, 2.5, 3.0}->sum()", self);
		assertInstanceOf(Double.class, result);
		assertEquals(7.0, result);
	}

	@Test
	void sum_singleReal() throws OclParseException {
		assertEquals(3.14, eval("Sequence{3.14}->sum()", self));
	}

	// --- Sum on Set ---

	@Test
	void sum_set() throws OclParseException {
		assertEquals(6, eval("Set{1, 2, 3}->sum()", self));
	}

	// --- Sum on Bag ---

	@Test
	void sum_bag_withDuplicates() throws OclParseException {
		assertEquals(9, eval("Bag{1, 2, 3, 3}->sum()", self));
	}

	// --- Sum on range ---

	@Test
	void sum_range_1to10() throws OclParseException {
		assertEquals(55, eval("Sequence{1..10}->sum()", self));
	}

	@Test
	void sum_range_1to100() throws OclParseException {
		assertEquals(5050, eval("Sequence{1..100}->sum()", self));
	}

	// --- Sum with model data ---

	@Test
	void sum_salaries() throws OclParseException {
		assertEquals(150000.0, eval(
				"self.employees->collect(e | e.salary)->sum()", company));
	}

	@Test
	void sum_ages() throws OclParseException {
		assertEquals(90, eval(
				"self.employees->collect(e | e.age)->sum()", company));
	}

	// --- Sum after filter ---

	@Test
	void sum_afterSelect() throws OclParseException {
		// select(x | x > 2) → {3, 4, 5} → sum = 12
		assertEquals(12, eval("Sequence{1, 2, 3, 4, 5}->select(x | x > 2)->sum()", self));
	}

	@Test
	void sum_afterReject() throws OclParseException {
		assertEquals(6, eval("Sequence{1, 2, 3, 4, 5}->reject(x | x > 3)->sum()", self));
	}

	// --- Sum in let ---

	@Test
	void sum_inLet() throws OclParseException {
		assertEquals(true, eval(
				"let total : Integer = Sequence{1, 2, 3}->sum() in total = 6", self));
	}

	// --- Sum in comparison ---

	@Test
	void sum_greaterThan() throws OclParseException {
		assertEquals(true, eval("Sequence{10, 20, 30}->sum() > 50", self));
	}

	@Test
	void sum_equality() throws OclParseException {
		assertEquals(true, eval("Sequence{10, 20, 30}->sum() = 60", self));
	}

	// --- Sum with collect ---

	@Test
	void collect_thenSum() throws OclParseException {
		// Collect doubled values, then sum: 2+4+6 = 12
		assertEquals(12, eval(
				"Sequence{1, 2, 3}->collect(x | x * 2)->sum()", self));
	}

	// --- Large sum ---

	@Test
	void sum_largeValues() throws OclParseException {
		Object result = eval("Sequence{1000000, 2000000, 3000000}->sum()", self);
		assertEquals(6000000, result);
	}
}
