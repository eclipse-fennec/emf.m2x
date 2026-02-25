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
 * Tests for OCL {@code insertAt} and {@code indexOf} operations
 * on ordered collections (Sequence, OrderedSet).
 */
class OclInsertAtIndexOfTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- insertAt ---

	@Test
	void insertAt_beginning() throws OclParseException {
		assertEquals(List.of(0, 1, 2, 3),
				eval("Sequence{1, 2, 3}->insertAt(1, 0)", self));
	}

	@Test
	void insertAt_middle() throws OclParseException {
		assertEquals(List.of(1, 99, 2, 3),
				eval("Sequence{1, 2, 3}->insertAt(2, 99)", self));
	}

	@Test
	void insertAt_end() throws OclParseException {
		assertEquals(List.of(1, 2, 3, 99),
				eval("Sequence{1, 2, 3}->insertAt(4, 99)", self));
	}

	@Test
	void insertAt_singleElement() throws OclParseException {
		assertEquals(List.of(99, 1),
				eval("Sequence{1}->insertAt(1, 99)", self));
	}

	@Test
	void insertAt_thenSize() throws OclParseException {
		assertEquals(4,
				eval("Sequence{1, 2, 3}->insertAt(2, 99)->size()", self));
	}

	@Test
	void insertAt_thenFirst() throws OclParseException {
		assertEquals(99,
				eval("Sequence{1, 2, 3}->insertAt(1, 99)->first()", self));
	}

	@Test
	void insertAt_thenLast() throws OclParseException {
		assertEquals(99,
				eval("Sequence{1, 2, 3}->insertAt(4, 99)->last()", self));
	}

	@Test
	void insertAt_string() throws OclParseException {
		assertEquals(List.of("a", "x", "b", "c"),
				eval("Sequence{'a', 'b', 'c'}->insertAt(2, 'x')", self));
	}

	// --- indexOf ---

	@Test
	void indexOf_first() throws OclParseException {
		assertEquals(1, eval("Sequence{10, 20, 30}->indexOf(10)", self));
	}

	@Test
	void indexOf_middle() throws OclParseException {
		assertEquals(2, eval("Sequence{10, 20, 30}->indexOf(20)", self));
	}

	@Test
	void indexOf_last() throws OclParseException {
		assertEquals(3, eval("Sequence{10, 20, 30}->indexOf(30)", self));
	}

	@Test
	void indexOf_string() throws OclParseException {
		assertEquals(2, eval("Sequence{'a', 'b', 'c'}->indexOf('b')", self));
	}

	@Test
	void indexOf_firstOccurrence() throws OclParseException {
		// If duplicates, indexOf returns first occurrence
		assertEquals(1, eval("Sequence{1, 2, 1, 3}->indexOf(1)", self));
	}

	// --- indexOf on OrderedSet ---

	@Test
	void indexOf_orderedSet() throws OclParseException {
		assertEquals(2, eval("OrderedSet{10, 20, 30}->indexOf(20)", self));
	}

	// --- Combined ---

	@Test
	void insertAt_thenIndexOf() throws OclParseException {
		// insertAt(2, 99) → {1, 99, 2, 3}, indexOf(99) = 2
		assertEquals(2,
				eval("Sequence{1, 2, 3}->insertAt(2, 99)->indexOf(99)", self));
	}

	@Test
	void insertAt_thenAt() throws OclParseException {
		// insertAt(2, 99) → {1, 99, 2, 3}, at(2) = 99
		assertEquals(99,
				eval("Sequence{1, 2, 3}->insertAt(2, 99)->at(2)", self));
	}

	@Test
	void indexOf_thenUseResult() throws OclParseException {
		// indexOf(20) = 2, use in arithmetic
		assertEquals(4,
				eval("Sequence{10, 20, 30}->indexOf(20) * 2", self));
	}

	// --- insertAt multiple times ---

	@Test
	void insertAt_twice() throws OclParseException {
		// {1,2,3} → insertAt(1,0) → {0,1,2,3} → insertAt(5,4) → {0,1,2,3,4}
		assertEquals(5,
				eval("Sequence{1, 2, 3}->insertAt(1, 0)->insertAt(5, 4)->size()", self));
	}
}
