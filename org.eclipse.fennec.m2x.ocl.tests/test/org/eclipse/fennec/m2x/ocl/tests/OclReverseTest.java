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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL {@code reverse()} on Sequence and OrderedSet.
 */
class OclReverseTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Sequence reverse ---

	@Test
	void reverse_sequence() throws OclParseException {
		assertEquals(List.of(3, 2, 1),
				eval("Sequence{1, 2, 3}->reverse()", self));
	}

	@Test
	void reverse_singleElement() throws OclParseException {
		assertEquals(List.of(42),
				eval("Sequence{42}->reverse()", self));
	}

	@Test
	void reverse_twoElements() throws OclParseException {
		assertEquals(List.of(2, 1),
				eval("Sequence{1, 2}->reverse()", self));
	}

	@Test
	void reverse_strings() throws OclParseException {
		assertEquals(List.of("c", "b", "a"),
				eval("Sequence{'a', 'b', 'c'}->reverse()", self));
	}

	@Test
	void reverse_preservesDuplicates() throws OclParseException {
		assertEquals(List.of(1, 2, 1),
				eval("Sequence{1, 2, 1}->reverse()", self));
	}

	// --- Reverse then operations ---

	@Test
	void reverse_thenFirst() throws OclParseException {
		assertEquals(5,
				eval("Sequence{1, 2, 3, 4, 5}->reverse()->first()", self));
	}

	@Test
	void reverse_thenLast() throws OclParseException {
		assertEquals(1,
				eval("Sequence{1, 2, 3, 4, 5}->reverse()->last()", self));
	}

	@Test
	void reverse_thenAt() throws OclParseException {
		assertEquals(4,
				eval("Sequence{1, 2, 3, 4, 5}->reverse()->at(2)", self));
	}

	@Test
	void reverse_thenSize() throws OclParseException {
		assertEquals(5,
				eval("Sequence{1, 2, 3, 4, 5}->reverse()->size()", self));
	}

	// --- Double reverse = identity ---

	@Test
	void reverse_twice() throws OclParseException {
		assertEquals(List.of(1, 2, 3),
				eval("Sequence{1, 2, 3}->reverse()->reverse()", self));
	}

	// --- Reverse on range ---

	@Test
	void reverse_range() throws OclParseException {
		assertEquals(5,
				eval("Sequence{1..5}->reverse()->first()", self));
	}

	// --- Reverse after sortedBy ---

	@Test
	void sortedBy_thenReverse_first() throws OclParseException {
		assertEquals(5,
				eval("Sequence{3, 1, 5, 2, 4}->sortedBy(i | i)->reverse()->first()", self));
	}

	@Test
	void sortedBy_thenReverse_last() throws OclParseException {
		assertEquals(1,
				eval("Sequence{3, 1, 5, 2, 4}->sortedBy(i | i)->reverse()->last()", self));
	}

	// --- Reverse on OrderedSet ---

	@Test
	void reverse_orderedSet_first() throws OclParseException {
		assertEquals(3,
				eval("OrderedSet{1, 2, 3}->reverse()->first()", self));
	}

	@Test
	void reverse_orderedSet_last() throws OclParseException {
		assertEquals(1,
				eval("OrderedSet{1, 2, 3}->reverse()->last()", self));
	}

	// --- Reverse empty ---

	@Test
	void reverse_empty() throws OclParseException {
		assertEquals(0,
				eval("Sequence{}->reverse()->size()", self));
	}

	// --- Reverse then sum (order doesn't affect sum) ---

	@Test
	void reverse_thenSum() throws OclParseException {
		assertEquals(15,
				eval("Sequence{1, 2, 3, 4, 5}->reverse()->sum()", self));
	}
}
