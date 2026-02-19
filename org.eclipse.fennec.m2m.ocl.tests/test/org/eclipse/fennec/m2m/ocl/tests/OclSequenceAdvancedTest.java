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
 * Advanced tests for OCL Sequence operations.
 * Covers insertAt, subSequence, indexOf, count, append, prepend,
 * and complex Sequence manipulation patterns.
 */
class OclSequenceAdvancedTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- insertAt ---

	@Test
	void insertAt_beginning() throws OclParseException {
		assertEquals(0L, eval("Sequence{1, 2, 3}->insertAt(1, 0)->first()", self));
	}

	@Test
	void insertAt_middle() throws OclParseException {
		assertEquals(4L, eval("Sequence{1, 2, 3}->insertAt(2, 99)->size()", self));
	}

	@Test
	void insertAt_end() throws OclParseException {
		assertEquals(99L, eval("Sequence{1, 2, 3}->insertAt(4, 99)->last()", self));
	}

	// --- subSequence ---

	@Test
	void subSequence_middle() throws OclParseException {
		Object result = eval("Sequence{10, 20, 30, 40, 50}->subSequence(2, 4)", self);
		assertEquals(List.of(20L, 30L, 40L), result);
	}

	@Test
	void subSequence_first() throws OclParseException {
		Object result = eval("Sequence{10, 20, 30}->subSequence(1, 2)", self);
		assertEquals(List.of(10L, 20L), result);
	}

	@Test
	void subSequence_single() throws OclParseException {
		Object result = eval("Sequence{10, 20, 30}->subSequence(2, 2)", self);
		assertEquals(List.of(20L), result);
	}

	@Test
	void subSequence_all() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3}->subSequence(1, 3)", self);
		assertEquals(List.of(1L, 2L, 3L), result);
	}

	// --- indexOf ---

	@Test
	void indexOf_found() throws OclParseException {
		assertEquals(2L, eval("Sequence{10, 20, 30}->indexOf(20)", self));
	}

	@Test
	void indexOf_first() throws OclParseException {
		assertEquals(1L, eval("Sequence{10, 20, 30}->indexOf(10)", self));
	}

	@Test
	void indexOf_duplicate_returnsFirst() throws OclParseException {
		assertEquals(1L, eval("Sequence{10, 20, 10, 30}->indexOf(10)", self));
	}

	// --- count ---

	@Test
	void count_single() throws OclParseException {
		assertEquals(1L, eval("Sequence{1, 2, 3}->count(2)", self));
	}

	@Test
	void count_multiple() throws OclParseException {
		assertEquals(3L, eval("Sequence{1, 2, 1, 3, 1}->count(1)", self));
	}

	@Test
	void count_absent() throws OclParseException {
		assertEquals(0L, eval("Sequence{1, 2, 3}->count(5)", self));
	}

	// --- append / prepend ---

	@Test
	void append_element() throws OclParseException {
		assertEquals(99L, eval("Sequence{1, 2, 3}->append(99)->last()", self));
	}

	@Test
	void append_size() throws OclParseException {
		assertEquals(4L, eval("Sequence{1, 2, 3}->append(4)->size()", self));
	}

	@Test
	void prepend_element() throws OclParseException {
		assertEquals(99L, eval("Sequence{1, 2, 3}->prepend(99)->first()", self));
	}

	@Test
	void prepend_size() throws OclParseException {
		assertEquals(4L, eval("Sequence{1, 2, 3}->prepend(0)->size()", self));
	}

	// --- including / excluding ---

	@Test
	void including_adds() throws OclParseException {
		assertEquals(4L, eval("Sequence{1, 2, 3}->including(4)->size()", self));
	}

	@Test
	void excluding_removes() throws OclParseException {
		assertEquals(2L, eval("Sequence{1, 2, 3}->excluding(2)->size()", self));
	}

	@Test
	void excluding_removesOne() throws OclParseException {
		// excluding removes one occurrence
		assertEquals(3L, eval("Sequence{1, 2, 1, 3}->excluding(1)->size()", self));
	}

	// --- reverse ---

	@Test
	void reverse_sequence() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3}->reverse()", self);
		assertEquals(List.of(3L, 2L, 1L), result);
	}

	@Test
	void reverse_preservesDuplicates() throws OclParseException {
		Object result = eval("Sequence{1, 2, 2, 3}->reverse()", self);
		assertEquals(List.of(3L, 2L, 2L, 1L), result);
	}

	// --- Chained Sequence operations ---

	@Test
	void chain_appendPrependSize() throws OclParseException {
		assertEquals(5L, eval(
				"Sequence{1, 2, 3}->append(4)->prepend(0)->size()", self));
	}

	@Test
	void chain_reverseFirst() throws OclParseException {
		assertEquals(3L, eval("Sequence{1, 2, 3}->reverse()->first()", self));
	}

	@Test
	void chain_sortReverseLast() throws OclParseException {
		assertEquals(1L, eval(
				"Sequence{3, 1, 2}->sortedBy(i | i)->reverse()->last()", self));
	}

	// --- Sequence with strings ---

	@Test
	void sequence_strings_includes() throws OclParseException {
		assertEquals(true, eval("Sequence{'a', 'b', 'c'}->includes('b')", self));
	}

	@Test
	void sequence_strings_first() throws OclParseException {
		assertEquals("a", eval("Sequence{'a', 'b', 'c'}->first()", self));
	}

	@Test
	void sequence_strings_sortedBy() throws OclParseException {
		assertEquals("a", eval(
				"Sequence{'c', 'a', 'b'}->sortedBy(s | s)->first()", self));
	}
}
