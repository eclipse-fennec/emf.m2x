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
 * Tests for OCL {@code subSequence} and {@code subOrderedSet} operations.
 * These extract a contiguous sub-range from ordered collections.
 * OCL uses 1-based indexing.
 */
class OclSubSequenceSubOrderedSetTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- subSequence ---

	@Test
	void subSequence_full() throws OclParseException {
		assertEquals(List.of(1, 2, 3, 4, 5),
				eval("Sequence{1, 2, 3, 4, 5}->subSequence(1, 5)", self));
	}

	@Test
	void subSequence_first3() throws OclParseException {
		assertEquals(List.of(1, 2, 3),
				eval("Sequence{1, 2, 3, 4, 5}->subSequence(1, 3)", self));
	}

	@Test
	void subSequence_last3() throws OclParseException {
		assertEquals(List.of(3, 4, 5),
				eval("Sequence{1, 2, 3, 4, 5}->subSequence(3, 5)", self));
	}

	@Test
	void subSequence_middle() throws OclParseException {
		assertEquals(List.of(2, 3, 4),
				eval("Sequence{1, 2, 3, 4, 5}->subSequence(2, 4)", self));
	}

	@Test
	void subSequence_single() throws OclParseException {
		assertEquals(List.of(3),
				eval("Sequence{1, 2, 3, 4, 5}->subSequence(3, 3)", self));
	}

	@Test
	void subSequence_size() throws OclParseException {
		assertEquals(3,
				eval("Sequence{1, 2, 3, 4, 5}->subSequence(2, 4)->size()", self));
	}

	@Test
	void subSequence_thenSum() throws OclParseException {
		// 2 + 3 + 4 = 9
		assertEquals(9,
				eval("Sequence{1, 2, 3, 4, 5}->subSequence(2, 4)->sum()", self));
	}

	@Test
	void subSequence_thenFirst() throws OclParseException {
		assertEquals(2,
				eval("Sequence{1, 2, 3, 4, 5}->subSequence(2, 4)->first()", self));
	}

	@Test
	void subSequence_thenLast() throws OclParseException {
		assertEquals(4,
				eval("Sequence{1, 2, 3, 4, 5}->subSequence(2, 4)->last()", self));
	}

	@Test
	void subSequence_strings() throws OclParseException {
		assertEquals(List.of("b", "c"),
				eval("Sequence{'a', 'b', 'c', 'd'}->subSequence(2, 3)", self));
	}

	// --- subSequence on range ---

	@Test
	void subSequence_onRange() throws OclParseException {
		assertEquals(5,
				eval("Sequence{1..10}->subSequence(3, 7)->size()", self));
	}

	@Test
	void subSequence_onRange_first() throws OclParseException {
		assertEquals(3,
				eval("Sequence{1..10}->subSequence(3, 7)->first()", self));
	}

	// --- subOrderedSet ---

	@Test
	void subOrderedSet_full() throws OclParseException {
		assertEquals(3,
				eval("OrderedSet{1, 2, 3}->subOrderedSet(1, 3)->size()", self));
	}

	@Test
	void subOrderedSet_first2() throws OclParseException {
		assertEquals(2,
				eval("OrderedSet{1, 2, 3, 4}->subOrderedSet(1, 2)->size()", self));
	}

	@Test
	void subOrderedSet_last2() throws OclParseException {
		assertEquals(2,
				eval("OrderedSet{1, 2, 3, 4}->subOrderedSet(3, 4)->size()", self));
	}

	@Test
	void subOrderedSet_single() throws OclParseException {
		assertEquals(1,
				eval("OrderedSet{1, 2, 3}->subOrderedSet(2, 2)->size()", self));
	}

	@Test
	void subOrderedSet_thenFirst() throws OclParseException {
		assertEquals(2,
				eval("OrderedSet{1, 2, 3, 4}->subOrderedSet(2, 3)->first()", self));
	}

	@Test
	void subOrderedSet_thenLast() throws OclParseException {
		assertEquals(3,
				eval("OrderedSet{1, 2, 3, 4}->subOrderedSet(2, 3)->last()", self));
	}

	// --- Chained subSequence ---

	@Test
	void subSequence_chained() throws OclParseException {
		// subSequence(2,4) → {2,3,4}, subSequence(1,2) → {2,3}
		assertEquals(List.of(2, 3),
				eval("Sequence{1, 2, 3, 4, 5}->subSequence(2, 4)->subSequence(1, 2)", self));
	}

	// --- subSequence then select ---

	@Test
	void subSequence_thenSelect() throws OclParseException {
		// subSequence(1,4) → {1,2,3,4}, select > 2 → {3,4}
		assertEquals(2,
				eval("Sequence{1, 2, 3, 4, 5}->subSequence(1, 4)->select(i | i > 2)->size()", self));
	}

	// --- subSequence then collect ---

	@Test
	void subSequence_thenCollect() throws OclParseException {
		// subSequence(2,4) → {2,3,4}, collect *2 → {4,6,8}, sum = 18
		assertEquals(18,
				eval("Sequence{1, 2, 3, 4, 5}->subSequence(2, 4)->collect(i | i * 2)->sum()", self));
	}
}
