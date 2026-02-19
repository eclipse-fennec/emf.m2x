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
 * Tests for OCL collection min/max/sum operations across
 * different collection types and element types.
 */
class OclCollectionMinMaxTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- min on integers ---

	@Test
	void min_sequence_integers() throws OclParseException {
		assertEquals(1L, eval("Sequence{3, 1, 4, 1, 5}->min()", self));
	}

	@Test
	void min_set_integers() throws OclParseException {
		assertEquals(1L, eval("Set{3, 1, 4, 5}->min()", self));
	}

	@Test
	void min_singleton() throws OclParseException {
		assertEquals(42L, eval("Sequence{42}->min()", self));
	}

	@Test
	void min_negative() throws OclParseException {
		assertEquals(-5L, eval("Sequence{3, -5, 1, -2}->min()", self));
	}

	@Test
	void min_allSame() throws OclParseException {
		assertEquals(7L, eval("Sequence{7, 7, 7}->min()", self));
	}

	// --- max on integers ---

	@Test
	void max_sequence_integers() throws OclParseException {
		assertEquals(5L, eval("Sequence{3, 1, 4, 1, 5}->max()", self));
	}

	@Test
	void max_set_integers() throws OclParseException {
		assertEquals(5L, eval("Set{3, 1, 4, 5}->max()", self));
	}

	@Test
	void max_singleton() throws OclParseException {
		assertEquals(42L, eval("Sequence{42}->max()", self));
	}

	@Test
	void max_negative() throws OclParseException {
		assertEquals(3L, eval("Sequence{3, -5, 1, -2}->max()", self));
	}

	@Test
	void max_allSame() throws OclParseException {
		assertEquals(7L, eval("Sequence{7, 7, 7}->max()", self));
	}

	// --- min/max on reals ---

	@Test
	void min_reals() throws OclParseException {
		assertEquals(1.1, eval("Sequence{3.3, 1.1, 2.2}->min()", self));
	}

	@Test
	void max_reals() throws OclParseException {
		assertEquals(3.3, eval("Sequence{3.3, 1.1, 2.2}->max()", self));
	}

	// --- min/max on strings ---

	@Test
	void min_strings() throws OclParseException {
		assertEquals("apple", eval("Sequence{'cherry', 'apple', 'banana'}->min()", self));
	}

	@Test
	void max_strings() throws OclParseException {
		assertEquals("cherry", eval("Sequence{'cherry', 'apple', 'banana'}->max()", self));
	}

	// --- sum ---

	@Test
	void sum_integers() throws OclParseException {
		assertEquals(15L, eval("Sequence{1, 2, 3, 4, 5}->sum()", self));
	}

	@Test
	void sum_reals() throws OclParseException {
		assertEquals(6.0, eval("Sequence{1.0, 2.0, 3.0}->sum()", self));
	}

	@Test
	void sum_set() throws OclParseException {
		assertEquals(6L, eval("Set{1, 2, 3}->sum()", self));
	}

	@Test
	void sum_bag_withDuplicates() throws OclParseException {
		assertEquals(9L, eval("Bag{1, 2, 3, 3}->sum()", self));
	}

	@Test
	void sum_singleton() throws OclParseException {
		assertEquals(42L, eval("Sequence{42}->sum()", self));
	}

	@Test
	void sum_empty() throws OclParseException {
		assertEquals(0L, eval("Sequence{}->sum()", self));
	}

	@Test
	void sum_negative() throws OclParseException {
		assertEquals(-3L, eval("Sequence{1, -2, -3, 1}->sum()", self));
	}

	// --- min/max after select ---

	@Test
	void min_afterSelect() throws OclParseException {
		assertEquals(3L, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i > 2)->min()", self));
	}

	@Test
	void max_afterSelect() throws OclParseException {
		assertEquals(5L, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i > 2)->max()", self));
	}

	// --- sum after collect ---

	@Test
	void sum_afterCollect() throws OclParseException {
		assertEquals(60L, eval(
				"Sequence{1, 2, 3}->collect(i | i * 10)->sum()", self));
	}

	// --- Chained min/max ---

	@Test
	void min_thenComparison() throws OclParseException {
		assertEquals(true, eval("Sequence{3, 1, 2}->min() = 1", self));
	}

	@Test
	void max_thenArithmetic() throws OclParseException {
		assertEquals(10L, eval("Sequence{3, 1, 5, 2}->max() * 2", self));
	}
}
