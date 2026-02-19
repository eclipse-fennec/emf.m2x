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
 * Tests for OCL operations that combine or compare different
 * collection types: union of Set+Bag, conversion chains,
 * and operations across collection type boundaries.
 */
class OclCrossCollectionOpsTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Conversion chains ---

	@Test
	void sequenceToSetToSequence() throws OclParseException {
		// Sequence with dups → Set (removes dups) → Sequence
		assertEquals(3, eval(
				"Sequence{1, 2, 2, 3, 3}->asSet()->asSequence()->size()", self));
	}

	@Test
	void setToBagToSet() throws OclParseException {
		assertEquals(3, eval(
				"Set{1, 2, 3}->asBag()->asSet()->size()", self));
	}

	@Test
	void sequenceToOrderedSet() throws OclParseException {
		// Removes duplicates, preserves first-seen order
		assertEquals(3, eval(
				"Sequence{1, 2, 1, 3, 2}->asOrderedSet()->size()", self));
	}

	@Test
	void orderedSetToSequence() throws OclParseException {
		assertEquals(3, eval(
				"OrderedSet{1, 2, 3}->asSequence()->size()", self));
	}

	@Test
	void bagToSequence() throws OclParseException {
		assertEquals(5, eval(
				"Bag{1, 1, 2, 2, 3}->asSequence()->size()", self));
	}

	@Test
	void bagToSet() throws OclParseException {
		assertEquals(3, eval(
				"Bag{1, 1, 2, 2, 3}->asSet()->size()", self));
	}

	// --- Operations after conversion ---

	@Test
	void setToSequence_first() throws OclParseException {
		// Set has no guaranteed order, but converting to sequence should work
		Object result = eval("Set{42}->asSequence()->first()", self);
		assertEquals(42, result);
	}

	@Test
	void sequenceToSet_includes() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->asSet()->includes(2)", self));
	}

	@Test
	void bagToSet_excludesDuplicates() throws OclParseException {
		assertEquals(3, eval(
				"Bag{1, 1, 1, 2, 2, 3}->asSet()->size()", self));
	}

	// --- Union across types ---

	@Test
	void set_union_bag() throws OclParseException {
		Object result = eval("Set{1, 2}->union(Bag{3, 4})", self);
		assertInstanceOf(Collection.class, result);
	}

	@Test
	void sequence_union_sequence() throws OclParseException {
		assertEquals(6, eval(
				"Sequence{1, 2, 3}->union(Sequence{4, 5, 6})->size()", self));
	}

	@Test
	void bag_union_bag() throws OclParseException {
		assertEquals(6, eval(
				"Bag{1, 2, 3}->union(Bag{1, 2, 3})->size()", self));
	}

	// --- Intersection ---

	@Test
	void set_intersection_set() throws OclParseException {
		assertEquals(2, eval(
				"Set{1, 2, 3}->intersection(Set{2, 3, 4})->size()", self));
	}

	// --- Select then convert ---

	@Test
	void selectThenAsSet() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 2, 3, 3}->select(i | i > 1)->asSet()->size() = 2",
				self));
	}

	@Test
	void selectThenAsBag() throws OclParseException {
		assertEquals(4, eval(
				"Sequence{1, 2, 2, 3, 3}->select(i | i > 1)->asBag()->size()",
				self));
	}

	// --- Collect then convert ---

	@Test
	void collectThenAsSet() throws OclParseException {
		// collect doubles: {2,4,6}, no dups so set size = 3
		assertEquals(3, eval(
				"Sequence{1, 2, 3}->collect(i | i * 2)->asSet()->size()", self));
	}

	@Test
	void collectThenAsSet_withDups() throws OclParseException {
		// collect mod 2: {1,0,1,0,1} → set {0,1} size=2
		assertEquals(2, eval(
				"Sequence{1, 2, 3, 4, 5}->collect(i | i.mod(2))->asSet()->size()", self));
	}

	// --- SortedBy on different source types ---

	@Test
	void set_sortedBy() throws OclParseException {
		assertEquals(1, eval("Set{3, 1, 2}->sortedBy(i | i)->first()", self));
	}

	@Test
	void bag_sortedBy() throws OclParseException {
		assertEquals(1, eval("Bag{3, 1, 2}->sortedBy(i | i)->first()", self));
	}

	@Test
	void orderedSet_sortedBy() throws OclParseException {
		assertEquals(1, eval("OrderedSet{3, 1, 2}->sortedBy(i | i)->first()", self));
	}

	// --- Flatten across types ---

	@Test
	void flatten_sequence() throws OclParseException {
		assertEquals(4, eval(
				"Sequence{Sequence{1, 2}, Sequence{3, 4}}->flatten()->size()", self));
	}

	@Test
	void flatten_set() throws OclParseException {
		assertEquals(true, eval(
				"Set{Set{1, 2}, Set{3, 4}}->flatten()->includes(3)", self));
	}

	// --- Complex conversion pipeline ---

	@Test
	void pipeline_removeDuplicatesAndSort() throws OclParseException {
		assertEquals(1, eval(
				"Sequence{3, 1, 2, 1, 3}->asSet()->sortedBy(i | i)->first()", self));
	}

	@Test
	void pipeline_bagCountAfterCollect() throws OclParseException {
		// collect mod 3 → Bag, count occurrences of 0
		assertEquals(1, eval(
				"Sequence{1, 2, 3, 4, 5}->collect(i | i.mod(3))->asBag()->count(0)",
				self));
	}
}
