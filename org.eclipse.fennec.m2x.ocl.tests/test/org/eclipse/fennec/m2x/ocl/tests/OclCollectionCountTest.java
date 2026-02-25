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
 * Tests for OCL count operation and related counting patterns
 * across different collection types.
 */
class OclCollectionCountTest extends AbstractOclTest {

	static EObject self;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
		EObject bob = createPerson("Bob", 25, 45000.0, false);
		company = createCompany("ACME", self, bob);
	}

	// --- Sequence count ---

	@Test
	void sequence_count_single() throws OclParseException {
		assertEquals(1, eval("Sequence{1, 2, 3}->count(2)", self));
	}

	@Test
	void sequence_count_multiple() throws OclParseException {
		assertEquals(3, eval("Sequence{1, 2, 1, 3, 1}->count(1)", self));
	}

	@Test
	void sequence_count_absent() throws OclParseException {
		assertEquals(0, eval("Sequence{1, 2, 3}->count(5)", self));
	}

	@Test
	void sequence_count_string() throws OclParseException {
		assertEquals(2, eval("Sequence{'a', 'b', 'a', 'c'}->count('a')", self));
	}

	@Test
	void sequence_count_boolean() throws OclParseException {
		assertEquals(2, eval("Sequence{true, false, true}->count(true)", self));
	}

	// --- Set count ---

	@Test
	void set_count_present() throws OclParseException {
		assertEquals(1, eval("Set{1, 2, 3}->count(2)", self));
	}

	@Test
	void set_count_absent() throws OclParseException {
		assertEquals(0, eval("Set{1, 2, 3}->count(5)", self));
	}

	// --- Bag count ---

	@Test
	void bag_count_multiple() throws OclParseException {
		assertEquals(3, eval("Bag{1, 2, 1, 3, 1}->count(1)", self));
	}

	@Test
	void bag_count_single() throws OclParseException {
		assertEquals(1, eval("Bag{1, 2, 3}->count(2)", self));
	}

	@Test
	void bag_count_absent() throws OclParseException {
		assertEquals(0, eval("Bag{1, 2, 3}->count(5)", self));
	}

	// --- Empty collection count ---

	@Test
	void empty_count() throws OclParseException {
		assertEquals(0, eval("Sequence{}->count(1)", self));
	}

	// --- Count after operations ---

	@Test
	void count_afterIncluding() throws OclParseException {
		assertEquals(2, eval("Sequence{1, 2, 3}->including(2)->count(2)", self));
	}

	@Test
	void count_afterSelect() throws OclParseException {
		assertEquals(1, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i > 3)->count(4)", self));
	}

	// --- Counting patterns with size ---

	@Test
	void countViaSelect_size() throws OclParseException {
		// Alternative way to count: select + size
		assertEquals(3, eval(
				"Sequence{1, 2, 1, 3, 1}->select(i | i = 1)->size()", self));
	}

	@Test
	void countEmployees_married() throws OclParseException {
		assertEquals(1, eval(
				"self.employees->select(e | e.isMarried)->size()", company));
	}

	@Test
	void countEmployees_unmarried() throws OclParseException {
		assertEquals(1, eval(
				"self.employees->select(e | not e.isMarried)->size()", company));
	}

	// --- Count null ---

	@Test
	void count_null() throws OclParseException {
		assertEquals(2, eval("Sequence{1, null, 2, null}->count(null)", self));
	}

	// --- Count with real ---

	@Test
	void count_real() throws OclParseException {
		assertEquals(2, eval("Sequence{1.5, 2.5, 1.5}->count(1.5)", self));
	}
}
