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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL collection operations (OCL v2.4 Section 11.7).
 *
 * <p>Ported from Eclipse OCL {@code EvaluateCollectionOperationsTest4}.
 */
class OclCollectionOperationsTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- size ---

	@Test
	void size_set() throws OclParseException {
		assertEquals(3L, eval("Set{1, 2, 3}->size()", self));
	}

	@Test
	void size_sequence() throws OclParseException {
		assertEquals(4L, eval("Sequence{1, 2, 2, 3}->size()", self));
	}

	@Test
	void size_emptySet() throws OclParseException {
		assertEquals(0L, eval("Set{}->size()", self));
	}

	@Test
	void size_emptySequence() throws OclParseException {
		assertEquals(0L, eval("Sequence{}->size()", self));
	}

	// --- isEmpty / notEmpty ---

	@Test
	void isEmpty_emptySet() throws OclParseException {
		assertEquals(true, eval("Set{}->isEmpty()", self));
	}

	@Test
	void isEmpty_nonEmptySet() throws OclParseException {
		assertEquals(false, eval("Set{1}->isEmpty()", self));
	}

	@Test
	void notEmpty_emptySequence() throws OclParseException {
		assertEquals(false, eval("Sequence{}->notEmpty()", self));
	}

	@Test
	void notEmpty_nonEmptySequence() throws OclParseException {
		assertEquals(true, eval("Sequence{1}->notEmpty()", self));
	}

	// --- includes / excludes ---

	@Test
	void includes_present() throws OclParseException {
		assertEquals(true, eval("Set{1, 2, 3}->includes(2)", self));
	}

	@Test
	void includes_absent() throws OclParseException {
		assertEquals(false, eval("Set{1, 2, 3}->includes(4)", self));
	}

	@Test
	void excludes_present() throws OclParseException {
		assertEquals(false, eval("Sequence{1, 2, 3}->excludes(2)", self));
	}

	@Test
	void excludes_absent() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3}->excludes(4)", self));
	}

	// --- includesAll / excludesAll ---

	@Test
	void includesAll_true() throws OclParseException {
		assertEquals(true, eval("Set{1, 2, 3}->includesAll(Set{1, 2})", self));
	}

	@Test
	void includesAll_false() throws OclParseException {
		assertEquals(false, eval("Set{1, 2, 3}->includesAll(Set{1, 4})", self));
	}

	@Test
	void excludesAll_true() throws OclParseException {
		assertEquals(true, eval("Set{1, 2, 3}->excludesAll(Set{4, 5})", self));
	}

	@Test
	void excludesAll_false() throws OclParseException {
		assertEquals(false, eval("Set{1, 2, 3}->excludesAll(Set{3, 4})", self));
	}

	// --- count ---

	@Test
	void count_sequence() throws OclParseException {
		assertEquals(2L, eval("Sequence{1, 2, 2, 3}->count(2)", self));
	}

	@Test
	void count_set() throws OclParseException {
		assertEquals(1L, eval("Set{1, 2, 3}->count(2)", self));
	}

	@Test
	void count_absent() throws OclParseException {
		assertEquals(0L, eval("Set{1, 2, 3}->count(4)", self));
	}

	// --- including / excluding ---

	@Test
	void including_set() throws OclParseException {
		Object result = eval("Set{1, 2}->including(3)", self);
		assertInstanceOf(Set.class, result);
		assertEquals(3, ((Collection<?>) result).size());
		assertTrue(((Collection<?>) result).contains(3L));
	}

	@Test
	void including_set_duplicate() throws OclParseException {
		Object result = eval("Set{1, 2, 3}->including(2)", self);
		assertEquals(3, ((Collection<?>) result).size());
	}

	@Test
	void including_sequence() throws OclParseException {
		Object result = eval("Sequence{1, 2}->including(3)", self);
		assertInstanceOf(List.class, result);
		assertEquals(List.of(1L, 2L, 3L), result);
	}

	@Test
	void excluding_set() throws OclParseException {
		Object result = eval("Set{1, 2, 3}->excluding(2)", self);
		assertInstanceOf(Set.class, result);
		assertEquals(2, ((Collection<?>) result).size());
	}

	@Test
	void excluding_sequence() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3, 2}->excluding(2)", self);
		assertInstanceOf(List.class, result);
		// excluding removes the first occurrence
		assertEquals(3, ((Collection<?>) result).size());
	}

	@Test
	void excluding_absent() throws OclParseException {
		Object result = eval("Set{1, 2, 3}->excluding(4)", self);
		assertEquals(3, ((Collection<?>) result).size());
	}

	// --- union ---

	@Test
	void union_sets() throws OclParseException {
		Object result = eval("Set{1, 2}->union(Set{2, 3})", self);
		assertInstanceOf(Set.class, result);
		@SuppressWarnings("unchecked")
		Set<Object> set = (Set<Object>) result;
		assertEquals(3, set.size());
		assertTrue(set.contains(1L));
		assertTrue(set.contains(2L));
		assertTrue(set.contains(3L));
	}

	@Test
	void union_sequences() throws OclParseException {
		Object result = eval("Sequence{1, 2}->union(Sequence{2, 3})", self);
		assertInstanceOf(List.class, result);
		assertEquals(List.of(1L, 2L, 2L, 3L), result);
	}

	// --- intersection ---

	@Test
	void intersection_sets() throws OclParseException {
		Object result = eval("Set{1, 2, 3}->intersection(Set{2, 3, 4})", self);
		assertInstanceOf(Set.class, result);
		@SuppressWarnings("unchecked")
		Set<Object> set = (Set<Object>) result;
		assertEquals(2, set.size());
		assertTrue(set.contains(2L));
		assertTrue(set.contains(3L));
	}

	@Test
	void intersection_disjoint() throws OclParseException {
		Object result = eval("Set{1, 2}->intersection(Set{3, 4})", self);
		assertTrue(((Collection<?>) result).isEmpty());
	}

	// --- set difference (minus) ---

	@Test
	void setMinus() throws OclParseException {
		Object result = eval("Set{1, 2, 3} - Set{2, 4}", self);
		assertInstanceOf(Set.class, result);
		@SuppressWarnings("unchecked")
		Set<Object> set = (Set<Object>) result;
		assertEquals(2, set.size());
		assertTrue(set.contains(1L));
		assertTrue(set.contains(3L));
	}

	// --- symmetricDifference ---

	@Test
	void symmetricDifference() throws OclParseException {
		Object result = eval("Set{1, 2, 3}->symmetricDifference(Set{2, 3, 4})", self);
		assertInstanceOf(Set.class, result);
		@SuppressWarnings("unchecked")
		Set<Object> set = (Set<Object>) result;
		assertEquals(2, set.size());
		assertTrue(set.contains(1L));
		assertTrue(set.contains(4L));
	}

	// --- flatten ---

	@Test
	void flatten_nestedSequence() throws OclParseException {
		Object result = eval("Sequence{Sequence{1, 2}, Sequence{3}}->flatten()", self);
		assertInstanceOf(List.class, result);
		assertEquals(List.of(1L, 2L, 3L), result);
	}

	@Test
	void flatten_alreadyFlat() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3}->flatten()", self);
		assertEquals(List.of(1L, 2L, 3L), result);
	}

	// --- conversion: asSet, asBag, asSequence, asOrderedSet ---

	@Test
	void asSet_fromSequence() throws OclParseException {
		Object result = eval("Sequence{1, 2, 2, 3}->asSet()", self);
		assertInstanceOf(LinkedHashSet.class, result);
		assertEquals(3, ((Collection<?>) result).size());
	}

	@Test
	void asSequence_fromSet() throws OclParseException {
		Object result = eval("Set{1, 2, 3}->asSequence()", self);
		assertInstanceOf(List.class, result);
		assertEquals(3, ((Collection<?>) result).size());
	}

	@Test
	void asOrderedSet_fromSequence() throws OclParseException {
		Object result = eval("Sequence{1, 2, 2, 3}->asOrderedSet()", self);
		assertInstanceOf(LinkedHashSet.class, result);
		assertEquals(3, ((Collection<?>) result).size());
	}

	@Test
	void asBag_fromSet() throws OclParseException {
		Object result = eval("Set{1, 2, 3}->asBag()", self);
		assertInstanceOf(List.class, result);
		assertEquals(3, ((Collection<?>) result).size());
	}

	// --- sum ---

	@Test
	void sum_integers() throws OclParseException {
		assertEquals(10L, eval("Sequence{1, 2, 3, 4}->sum()", self));
	}

	@Test
	void sum_reals() throws OclParseException {
		assertEquals(6.0, eval("Sequence{1.0, 2.0, 3.0}->sum()", self));
	}

	@Test
	void sum_empty() throws OclParseException {
		assertEquals(0L, eval("Sequence{}->sum()", self));
	}

	// --- max / min ---

	@Test
	void max_integers() throws OclParseException {
		assertEquals(4L, eval("Set{1, 4, 2, 3}->max()", self));
	}

	@Test
	void min_integers() throws OclParseException {
		assertEquals(1L, eval("Set{4, 1, 2, 3}->min()", self));
	}

	@Test
	void max_strings() throws OclParseException {
		assertEquals("c", eval("Set{'a', 'c', 'b'}->max()", self));
	}

	@Test
	void min_strings() throws OclParseException {
		assertEquals("a", eval("Set{'c', 'a', 'b'}->min()", self));
	}

	// --- product ---

	@Test
	@SuppressWarnings("unchecked")
	void product() throws OclParseException {
		Object result = eval("Set{1, 2}->product(Set{'a', 'b'})", self);
		assertInstanceOf(Set.class, result);
		Set<Map<String, Object>> tuples = (Set<Map<String, Object>>) result;
		assertEquals(4, tuples.size());
	}

	// --- Ordered collection: first / last ---

	@Test
	void first_sequence() throws OclParseException {
		assertEquals(1L, eval("Sequence{1, 2, 3}->first()", self));
	}

	@Test
	void last_sequence() throws OclParseException {
		assertEquals(3L, eval("Sequence{1, 2, 3}->last()", self));
	}

	@Test
	void first_emptySequence() throws OclParseException {
		assertInvalid("Sequence{}->first()", self);
	}

	@Test
	void last_emptySequence() throws OclParseException {
		assertInvalid("Sequence{}->last()", self);
	}

	// --- Ordered collection: at ---

	@Test
	void at_sequence() throws OclParseException {
		assertEquals(2L, eval("Sequence{1, 2, 3}->at(2)", self));
	}

	@Test
	void at_first() throws OclParseException {
		assertEquals(1L, eval("Sequence{1, 2, 3}->at(1)", self));
	}

	@Test
	void at_outOfBounds() throws OclParseException {
		assertInvalid("Sequence{1, 2, 3}->at(0)", self);
	}

	@Test
	void at_outOfBoundsHigh() throws OclParseException {
		assertInvalid("Sequence{1, 2, 3}->at(4)", self);
	}

	// --- Ordered collection: indexOf ---

	@Test
	void indexOf_found() throws OclParseException {
		assertEquals(2L, eval("Sequence{1, 2, 3}->indexOf(2)", self));
	}

	@Test
	void indexOf_notFound() throws OclParseException {
		assertEquals(0L, eval("Sequence{1, 2, 3}->indexOf(4)", self));
	}

	// --- Ordered collection: reverse ---

	@Test
	void reverse_sequence() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3}->reverse()", self);
		assertEquals(List.of(3L, 2L, 1L), result);
	}

	// --- Ordered collection: append / prepend ---

	@Test
	void append_sequence() throws OclParseException {
		Object result = eval("Sequence{1, 2}->append(3)", self);
		assertEquals(List.of(1L, 2L, 3L), result);
	}

	@Test
	void prepend_sequence() throws OclParseException {
		Object result = eval("Sequence{2, 3}->prepend(1)", self);
		assertEquals(List.of(1L, 2L, 3L), result);
	}

	// --- Ordered collection: insertAt ---

	@Test
	void insertAt_sequence() throws OclParseException {
		Object result = eval("Sequence{1, 3}->insertAt(2, 2)", self);
		assertEquals(List.of(1L, 2L, 3L), result);
	}

	@Test
	void insertAt_outOfBounds() throws OclParseException {
		assertInvalid("Sequence{1, 2}->insertAt(0, 3)", self);
	}

	// --- Ordered collection: subSequence ---

	@Test
	void subSequence() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3, 4, 5}->subSequence(2, 4)", self);
		assertEquals(List.of(2L, 3L, 4L), result);
	}

	@Test
	void subSequence_outOfBounds() throws OclParseException {
		assertInvalid("Sequence{1, 2, 3}->subSequence(0, 2)", self);
	}

	// --- Range literal ---

	@Test
	void range_sequence() throws OclParseException {
		Object result = eval("Sequence{1..5}", self);
		assertEquals(List.of(1L, 2L, 3L, 4L, 5L), result);
	}

	@Test
	void range_singleElement() throws OclParseException {
		Object result = eval("Sequence{3..3}", self);
		assertEquals(List.of(3L), result);
	}
}
