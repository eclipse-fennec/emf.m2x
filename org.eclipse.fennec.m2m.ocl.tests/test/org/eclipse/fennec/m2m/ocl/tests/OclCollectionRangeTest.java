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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL collection ranges: Sequence{1..10}, Set{1..5}.
 * Ranges generate integer sequences from lower to upper bound (inclusive).
 */
class OclCollectionRangeTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Basic ranges ---

	@Test
	void sequence_range() throws OclParseException {
		Object result = eval("Sequence{1..5}", self);
		assertEquals(List.of(1, 2, 3, 4, 5), result);
	}

	@Test
	void sequence_range_size() throws OclParseException {
		assertEquals(5, eval("Sequence{1..5}->size()", self));
	}

	@Test
	void sequence_range_sum() throws OclParseException {
		assertEquals(15, eval("Sequence{1..5}->sum()", self));
	}

	@Test
	void sequence_range_first() throws OclParseException {
		assertEquals(1, eval("Sequence{1..5}->first()", self));
	}

	@Test
	void sequence_range_last() throws OclParseException {
		assertEquals(5, eval("Sequence{1..5}->last()", self));
	}

	// --- Range with iterators ---

	@Test
	void range_select() throws OclParseException {
		Object result = eval("Sequence{1..10}->select(i | i > 7)", self);
		assertEquals(List.of(8, 9, 10), result);
	}

	@Test
	void range_collect() throws OclParseException {
		Object result = eval("Sequence{1..5}->collect(i | i * i)", self);
		assertEquals(List.of(1, 4, 9, 16, 25), result);
	}

	@Test
	void range_forAll() throws OclParseException {
		assertEquals(true, eval("Sequence{1..10}->forAll(i | i > 0)", self));
	}

	@Test
	void range_exists() throws OclParseException {
		assertEquals(true, eval("Sequence{1..10}->exists(i | i = 5)", self));
	}

	// --- Single element range ---

	@Test
	void range_singleElement() throws OclParseException {
		Object result = eval("Sequence{3..3}", self);
		assertEquals(List.of(3), result);
	}

	// --- Empty range (lower > upper) ---

	@Test
	void range_empty() throws OclParseException {
		Object result = eval("Sequence{5..3}", self);
		assertInstanceOf(Collection.class, result);
		assertTrue(((Collection<?>) result).isEmpty());
	}

	// --- Range in Set ---

	@Test
	void set_range() throws OclParseException {
		assertEquals(5, eval("Set{1..5}->size()", self));
	}

	// --- Range mixed with explicit elements ---

	@Test
	void sequence_rangeWithElements() throws OclParseException {
		Object result = eval("Sequence{1..3, 10}", self);
		assertEquals(List.of(1, 2, 3, 10), result);
	}

	// --- Range with computation ---

	@Test
	void range_iterate_factorial() throws OclParseException {
		// 5! = 120
		assertEquals(120, eval(
				"Sequence{1..5}->iterate(i; acc : Integer = 1 | acc * i)", self));
	}

	@Test
	void range_average() throws OclParseException {
		// Average of 1..10 = 5.5
		assertEquals(5.5, eval(
				"Sequence{1..10}->sum() / Sequence{1..10}->size()", self));
	}
}
