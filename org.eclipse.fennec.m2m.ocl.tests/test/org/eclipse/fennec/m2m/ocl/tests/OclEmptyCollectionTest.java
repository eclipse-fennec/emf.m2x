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
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL operations on empty collections.
 * Verifies correct behavior of iterators, aggregation operations,
 * and accessors when applied to empty collections.
 */
class OclEmptyCollectionTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Size / isEmpty / notEmpty ---

	@Test
	void emptySet_size() throws OclParseException {
		assertEquals(0L, eval("Set{}->size()", self));
	}

	@Test
	void emptySequence_size() throws OclParseException {
		assertEquals(0L, eval("Sequence{}->size()", self));
	}

	@Test
	void emptyBag_size() throws OclParseException {
		assertEquals(0L, eval("Bag{}->size()", self));
	}

	@Test
	void emptyOrderedSet_size() throws OclParseException {
		assertEquals(0L, eval("OrderedSet{}->size()", self));
	}

	@Test
	void emptySet_isEmpty() throws OclParseException {
		assertEquals(true, eval("Set{}->isEmpty()", self));
	}

	@Test
	void emptySet_notEmpty() throws OclParseException {
		assertEquals(false, eval("Set{}->notEmpty()", self));
	}

	// --- forAll / exists on empty ---

	@Test
	void emptySet_forAll_vacuouslyTrue() throws OclParseException {
		assertEquals(true, eval("Set{}->forAll(i | false)", self));
	}

	@Test
	void emptySequence_forAll_vacuouslyTrue() throws OclParseException {
		assertEquals(true, eval("Sequence{}->forAll(i | false)", self));
	}

	@Test
	void emptySet_exists_false() throws OclParseException {
		assertEquals(false, eval("Set{}->exists(i | true)", self));
	}

	@Test
	void emptySequence_exists_false() throws OclParseException {
		assertEquals(false, eval("Sequence{}->exists(i | true)", self));
	}

	// --- select / reject on empty ---

	@Test
	void emptySet_select() throws OclParseException {
		Object result = eval("Set{}->select(i | true)", self);
		assertInstanceOf(Collection.class, result);
		assertEquals(0, ((Collection<?>) result).size());
	}

	@Test
	void emptySequence_reject() throws OclParseException {
		Object result = eval("Sequence{}->reject(i | false)", self);
		assertInstanceOf(Collection.class, result);
		assertEquals(0, ((Collection<?>) result).size());
	}

	// --- collect on empty ---

	@Test
	void emptySet_collect() throws OclParseException {
		Object result = eval("Set{}->collect(i | i)", self);
		assertInstanceOf(Collection.class, result);
		assertEquals(0, ((Collection<?>) result).size());
	}

	// --- any on empty ---

	@Test
	void emptySequence_any() throws OclParseException {
		assertNull(eval("Sequence{}->any(i | true)", self));
	}

	// --- first / last on empty ---

	@Test
	void emptySequence_first() throws OclParseException {
		// first() on empty produces OclInvalid
		assertInvalid("Sequence{}->first()", self);
	}

	@Test
	void emptySequence_last() throws OclParseException {
		// last() on empty produces OclInvalid
		assertInvalid("Sequence{}->last()", self);
	}

	// --- includes / excludes on empty ---

	@Test
	void emptySet_includes() throws OclParseException {
		assertEquals(false, eval("Set{}->includes(1)", self));
	}

	@Test
	void emptySet_excludes() throws OclParseException {
		assertEquals(true, eval("Set{}->excludes(1)", self));
	}

	@Test
	void emptySet_includesAll() throws OclParseException {
		// Empty includes all of empty → true
		assertEquals(true, eval("Set{}->includesAll(Set{})", self));
	}

	@Test
	void emptySet_excludesAll() throws OclParseException {
		assertEquals(true, eval("Set{}->excludesAll(Set{1, 2})", self));
	}

	// --- union / intersection with empty ---

	@Test
	void emptySet_union() throws OclParseException {
		assertEquals(3L, eval("Set{}->union(Set{1, 2, 3})->size()", self));
	}

	@Test
	void set_union_empty() throws OclParseException {
		assertEquals(3L, eval("Set{1, 2, 3}->union(Set{})->size()", self));
	}

	@Test
	void emptySet_intersection() throws OclParseException {
		assertEquals(0L, eval("Set{}->intersection(Set{1, 2, 3})->size()", self));
	}

	// --- flatten on empty ---

	@Test
	void emptySet_flatten() throws OclParseException {
		assertEquals(0L, eval("Set{}->flatten()->size()", self));
	}

	// --- one / isUnique on empty ---

	@Test
	void emptySet_one() throws OclParseException {
		assertEquals(false, eval("Set{}->one(i | true)", self));
	}

	@Test
	void emptySet_isUnique() throws OclParseException {
		// isUnique on empty is vacuously true
		assertEquals(true, eval("Set{}->isUnique(i | i)", self));
	}

	// --- sum on empty ---

	@Test
	void emptySequence_sum() throws OclParseException {
		assertEquals(0L, eval("Sequence{}->sum()", self));
	}

	// --- count on empty ---

	@Test
	void emptySequence_count() throws OclParseException {
		assertEquals(0L, eval("Sequence{}->count(1)", self));
	}

	// --- Chained operations on empty ---

	@Test
	void emptySet_chainedOps() throws OclParseException {
		assertEquals(0L, eval(
				"Set{}->including(1)->excluding(1)->size()", self));
	}

	@Test
	void emptySequence_appendSize() throws OclParseException {
		assertEquals(1L, eval("Sequence{}->append(42)->size()", self));
	}
}
