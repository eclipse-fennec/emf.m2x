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
 * Tests for ordered collection operations: append, prepend,
 * including, excluding on Sequence and OrderedSet.
 */
class OclCollectionAppendPrependTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Sequence append ---

	@Test
	void sequence_append_integer() throws OclParseException {
		assertEquals(List.of(1, 2, 3, 4), eval("Sequence{1, 2, 3}->append(4)", self));
	}

	@Test
	void sequence_append_toEmpty() throws OclParseException {
		assertEquals(List.of(1), eval("Sequence{}->append(1)", self));
	}

	@Test
	void sequence_append_string() throws OclParseException {
		assertEquals(List.of("a", "b", "c"), eval("Sequence{'a', 'b'}->append('c')", self));
	}

	@Test
	void sequence_append_duplicate() throws OclParseException {
		assertEquals(List.of(1, 2, 1), eval("Sequence{1, 2}->append(1)", self));
	}

	// --- Sequence prepend ---

	@Test
	void sequence_prepend_integer() throws OclParseException {
		assertEquals(List.of(0, 1, 2, 3), eval("Sequence{1, 2, 3}->prepend(0)", self));
	}

	@Test
	void sequence_prepend_toEmpty() throws OclParseException {
		assertEquals(List.of(1), eval("Sequence{}->prepend(1)", self));
	}

	@Test
	void sequence_prepend_string() throws OclParseException {
		assertEquals(List.of("z", "a", "b"), eval("Sequence{'a', 'b'}->prepend('z')", self));
	}

	// --- Chained append/prepend ---

	@Test
	void sequence_appendThenPrepend() throws OclParseException {
		assertEquals(List.of(0, 1, 2, 3, 4),
				eval("Sequence{1, 2, 3}->append(4)->prepend(0)", self));
	}

	@Test
	void sequence_multipleAppends() throws OclParseException {
		assertEquals(List.of(1, 2, 3),
				eval("Sequence{}->append(1)->append(2)->append(3)", self));
	}

	@Test
	void sequence_multiplePrepends() throws OclParseException {
		assertEquals(List.of(1, 2, 3),
				eval("Sequence{}->prepend(3)->prepend(2)->prepend(1)", self));
	}

	// --- OrderedSet append ---

	@Test
	void orderedSet_append_new() throws OclParseException {
		Object result = eval("OrderedSet{1, 2, 3}->append(4)->size()", self);
		assertEquals(4, result);
	}

	@Test
	void orderedSet_append_existing() throws OclParseException {
		// OCL §11.7.3: append on OrderedSet moves existing element to end (no duplicates)
		assertEquals(3, eval("OrderedSet{1, 2, 3}->append(2)->size()", self));
		// element 2 moved to end: last element is now 2
		assertEquals(2, eval("OrderedSet{1, 2, 3}->append(2)->last()", self));
		assertEquals(1, eval("OrderedSet{1, 2, 3}->append(2)->first()", self));
	}

	// --- OrderedSet prepend ---

	@Test
	void orderedSet_prepend_new() throws OclParseException {
		assertEquals(4, eval("OrderedSet{1, 2, 3}->prepend(0)->size()", self));
	}

	@Test
	void orderedSet_prepend_existing() throws OclParseException {
		// OCL §11.7.3: prepend on OrderedSet moves existing element to front (no duplicates)
		assertEquals(3, eval("OrderedSet{1, 2, 3}->prepend(2)->size()", self));
		// element 2 moved to front: first element is now 2
		assertEquals(2, eval("OrderedSet{1, 2, 3}->prepend(2)->first()", self));
		assertEquals(3, eval("OrderedSet{1, 2, 3}->prepend(2)->last()", self));
	}

	// --- Append/prepend with operations ---

	@Test
	void append_thenSize() throws OclParseException {
		assertEquals(4, eval("Sequence{1, 2, 3}->append(4)->size()", self));
	}

	@Test
	void prepend_thenFirst() throws OclParseException {
		assertEquals(0, eval("Sequence{1, 2, 3}->prepend(0)->first()", self));
	}

	@Test
	void append_thenLast() throws OclParseException {
		assertEquals(99, eval("Sequence{1, 2, 3}->append(99)->last()", self));
	}

	@Test
	void append_thenIncludes() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2}->append(3)->includes(3)", self));
	}

	@Test
	void prepend_thenSum() throws OclParseException {
		assertEquals(10, eval("Sequence{2, 3}->prepend(5)->sum()", self));
	}

	// --- With model data ---

	@Test
	void collect_thenAppend() throws OclParseException {
		EObject bob = createPerson("Bob", 25, 40000.0, false);
		EObject company = createCompany("ACME", self, bob);
		Object result = eval(
				"self.employees->collect(e | e.name)->append('Carol')", company);
		assertEquals(List.of("Alice", "Bob", "Carol"), result);
	}

	// --- Including on Set ---

	@Test
	void set_including_new() throws OclParseException {
		assertEquals(4, eval("Set{1, 2, 3}->including(4)->size()", self));
	}

	@Test
	void set_including_existing() throws OclParseException {
		assertEquals(3, eval("Set{1, 2, 3}->including(2)->size()", self));
	}

	// --- Excluding ---

	@Test
	void sequence_excluding() throws OclParseException {
		assertEquals(List.of(1, 3), eval("Sequence{1, 2, 3}->excluding(2)", self));
	}

	@Test
	void sequence_excluding_allOccurrences() throws OclParseException {
		// OCL v2.5 §11.7.2: excluding removes ALL occurrences
		assertEquals(List.of(1, 3), eval("Sequence{1, 2, 3, 2}->excluding(2)", self));
	}

	@Test
	void set_excluding() throws OclParseException {
		assertEquals(2, eval("Set{1, 2, 3}->excluding(2)->size()", self));
	}
}
