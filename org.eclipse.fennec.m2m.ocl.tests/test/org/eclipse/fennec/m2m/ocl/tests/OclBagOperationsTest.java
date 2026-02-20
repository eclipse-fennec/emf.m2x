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
 * Tests for OCL Bag operations.
 * A Bag is an unordered collection that allows duplicates.
 */
class OclBagOperationsTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Construction ---

	@Test
	void bag_literal() throws OclParseException {
		Object result = eval("Bag{1, 2, 3}", self);
		assertInstanceOf(Collection.class, result);
		Collection<?> bag = (Collection<?>) result;
		assertEquals(3, bag.size());
		assertEquals(true, bag.contains(1));
		assertEquals(true, bag.contains(2));
		assertEquals(true, bag.contains(3));
	}

	@Test
	void bag_allowsDuplicates() throws OclParseException {
		assertEquals(5, eval("Bag{1, 1, 2, 2, 3}->size()", self));
	}

	// --- count ---

	@Test
	void bag_count() throws OclParseException {
		assertEquals(3, eval("Bag{1, 1, 1, 2, 3}->count(1)", self));
	}

	@Test
	void bag_count_absent() throws OclParseException {
		assertEquals(0, eval("Bag{1, 2, 3}->count(5)", self));
	}

	// --- includes / excludes ---

	@Test
	void bag_includes() throws OclParseException {
		assertEquals(true, eval("Bag{1, 2, 3}->includes(2)", self));
	}

	@Test
	void bag_excludes() throws OclParseException {
		assertEquals(true, eval("Bag{1, 2, 3}->excludes(5)", self));
	}

	// --- including / excluding ---

	@Test
	void bag_including() throws OclParseException {
		assertEquals(4, eval("Bag{1, 2, 3}->including(4)->size()", self));
	}

	@Test
	void bag_excluding() throws OclParseException {
		assertEquals(2, eval("Bag{1, 2, 3}->excluding(1)->size()", self));
	}

	// --- union ---

	@Test
	void bag_union() throws OclParseException {
		assertEquals(5, eval("Bag{1, 2}->union(Bag{3, 4, 5})->size()", self));
	}

	@Test
	void bag_union_preservesDuplicates() throws OclParseException {
		assertEquals(4, eval("Bag{1, 2}->union(Bag{1, 2})->size()", self));
	}

	// --- intersection ---

	@Test
	void bag_intersection() throws OclParseException {
		assertEquals(true, eval("Bag{1, 2, 3}->intersection(Bag{2, 3, 4})->includes(2)", self));
	}

	// --- isEmpty / notEmpty ---

	@Test
	void bag_isEmpty() throws OclParseException {
		assertEquals(true, eval("Bag{}->isEmpty()", self));
	}

	@Test
	void bag_notEmpty() throws OclParseException {
		assertEquals(true, eval("Bag{1}->notEmpty()", self));
	}

	// --- sum ---

	@Test
	void bag_sum() throws OclParseException {
		assertEquals(9, eval("Bag{1, 2, 3, 3}->sum()", self));
	}

	// --- Iterators ---

	@Test
	void bag_select() throws OclParseException {
		assertEquals(2, eval("Bag{1, 2, 3, 4}->select(i | i > 2)->size()", self));
	}

	@Test
	void bag_exists() throws OclParseException {
		assertEquals(true, eval("Bag{1, 2, 3}->exists(i | i = 2)", self));
	}

	@Test
	void bag_forAll() throws OclParseException {
		assertEquals(true, eval("Bag{2, 4, 6}->forAll(i | i.mod(2) = 0)", self));
	}

	@Test
	void bag_collect() throws OclParseException {
		// collect on Bag produces Bag (preserves duplicates)
		assertEquals(6, eval("Bag{1, 1, 2, 2, 3, 3}->collect(i | i * 2)->size()", self));
	}

	// --- Conversion ---

	@Test
	void bag_asSet() throws OclParseException {
		assertEquals(3, eval("Bag{1, 1, 2, 2, 3}->asSet()->size()", self));
	}

	@Test
	void bag_asSequence() throws OclParseException {
		assertEquals(5, eval("Bag{1, 1, 2, 2, 3}->asSequence()->size()", self));
	}

	// --- flatten ---

	@Test
	void bag_flatten() throws OclParseException {
		assertEquals(3, eval("Bag{1, 2, 3}->flatten()->size()", self));
	}

	@Test
	void bag_excludingAllOccurrences() throws OclParseException {
		// OCL v2.5 §11.7.2: excluding removes ALL occurrences
		assertEquals(2, eval("Bag{1, 1, 2, 3}->excluding(1)->size()", self));
	}
}
