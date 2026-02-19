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

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL OrderedSet operations.
 * OrderedSet is a Set (no duplicates) that maintains insertion order.
 */
class OclOrderedSetTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Construction ---

	@Test
	void orderedSet_literal() throws OclParseException {
		Object result = eval("OrderedSet{1, 2, 3}", self);
		assertInstanceOf(Collection.class, result);
		assertEquals(3, ((Collection<?>) result).size());
	}

	@Test
	void orderedSet_removeDuplicates() throws OclParseException {
		assertEquals(3, eval("OrderedSet{1, 2, 2, 3, 3}->size()", self));
	}

	// --- Ordering ---

	@Test
	void orderedSet_first() throws OclParseException {
		assertEquals(1, eval("OrderedSet{1, 2, 3}->first()", self));
	}

	@Test
	void orderedSet_last() throws OclParseException {
		assertEquals(3, eval("OrderedSet{1, 2, 3}->last()", self));
	}

	@Test
	void orderedSet_at() throws OclParseException {
		assertEquals(2, eval("OrderedSet{1, 2, 3}->at(2)", self));
	}

	// --- Set operations on OrderedSet ---

	@Test
	void orderedSet_includes() throws OclParseException {
		assertEquals(true, eval("OrderedSet{1, 2, 3}->includes(2)", self));
	}

	@Test
	void orderedSet_excludes() throws OclParseException {
		assertEquals(true, eval("OrderedSet{1, 2, 3}->excludes(5)", self));
	}

	@Test
	void orderedSet_isEmpty() throws OclParseException {
		assertEquals(true, eval("OrderedSet{}->isEmpty()", self));
	}

	@Test
	void orderedSet_notEmpty() throws OclParseException {
		assertEquals(true, eval("OrderedSet{1}->notEmpty()", self));
	}

	// --- indexOf ---

	@Test
	void orderedSet_indexOf() throws OclParseException {
		assertEquals(2, eval("OrderedSet{10, 20, 30}->indexOf(20)", self));
	}

	// --- append / prepend ---

	@Test
	void orderedSet_append() throws OclParseException {
		assertEquals(4, eval("OrderedSet{1, 2, 3}->append(4)->last()", self));
	}

	@Test
	void orderedSet_prepend() throws OclParseException {
		assertEquals(0, eval("OrderedSet{1, 2, 3}->prepend(0)->first()", self));
	}

	@Test
	void orderedSet_append_newElement() throws OclParseException {
		assertEquals(4, eval("OrderedSet{1, 2, 3}->append(4)->size()", self));
	}

	// --- reverse ---

	@Test
	void orderedSet_reverse() throws OclParseException {
		assertEquals(3, eval("OrderedSet{1, 2, 3}->reverse()->first()", self));
	}

	// --- including / excluding ---

	@Test
	void orderedSet_including() throws OclParseException {
		assertEquals(4, eval("OrderedSet{1, 2, 3}->including(4)->size()", self));
	}

	@Test
	void orderedSet_excluding() throws OclParseException {
		assertEquals(2, eval("OrderedSet{1, 2, 3}->excluding(2)->size()", self));
	}

	// --- Iterators on OrderedSet ---

	@Test
	void orderedSet_select() throws OclParseException {
		assertEquals(2, eval("OrderedSet{1, 2, 3, 4}->select(i | i > 2)->size()", self));
	}

	@Test
	void orderedSet_collect() throws OclParseException {
		Object result = eval("OrderedSet{1, 2, 3}->collect(i | i * 2)", self);
		assertInstanceOf(Collection.class, result);
	}

	@Test
	void orderedSet_forAll() throws OclParseException {
		assertEquals(true, eval("OrderedSet{2, 4, 6}->forAll(i | i.mod(2) = 0)", self));
	}

	// --- Conversion ---

	@Test
	void orderedSet_asSequence() throws OclParseException {
		assertEquals(1, eval("OrderedSet{1, 2, 3}->asSequence()->first()", self));
	}

	@Test
	void orderedSet_asSet() throws OclParseException {
		assertEquals(3, eval("OrderedSet{1, 2, 3}->asSet()->size()", self));
	}

	// --- Flatten ---

	@Test
	void orderedSet_flatten() throws OclParseException {
		// Nested collection should be flattened
		assertEquals(true, eval("OrderedSet{1, 2, 3}->flatten()->includes(1)", self));
	}
}
