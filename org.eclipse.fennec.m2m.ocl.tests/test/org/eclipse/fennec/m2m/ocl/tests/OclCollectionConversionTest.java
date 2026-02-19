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

import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL collection type conversions: asSet, asBag, asSequence,
 * asOrderedSet, and behavior differences between collection types.
 */
class OclCollectionConversionTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Sequence → other types ---

	@Test
	void sequence_asSet() throws OclParseException {
		Object result = eval("Sequence{1, 2, 2, 3}->asSet()", self);
		assertInstanceOf(Set.class, result);
		assertEquals(3, ((Set<?>) result).size());
	}

	@Test
	void sequence_asSet_removeDuplicates() throws OclParseException {
		assertEquals(3, eval("Sequence{1, 2, 2, 3}->asSet()->size()", self));
	}

	@Test
	void sequence_asOrderedSet() throws OclParseException {
		Object result = eval("Sequence{1, 2, 2, 3}->asOrderedSet()", self);
		assertInstanceOf(List.class, result);
		assertEquals(3, ((List<?>) result).size()); // duplicates removed
	}

	@Test
	void sequence_asBag() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3}->asBag()", self);
		assertInstanceOf(List.class, result);
		assertEquals(3, ((List<?>) result).size());
	}

	// --- Set → other types ---

	@Test
	void set_asSequence() throws OclParseException {
		Object result = eval("Set{1, 2, 3}->asSequence()", self);
		assertInstanceOf(List.class, result);
		assertEquals(3, ((List<?>) result).size());
	}

	@Test
	void set_asBag() throws OclParseException {
		Object result = eval("Set{1, 2, 3}->asBag()", self);
		assertInstanceOf(List.class, result);
		assertEquals(3, ((List<?>) result).size());
	}

	@Test
	void set_asOrderedSet() throws OclParseException {
		Object result = eval("Set{1, 2, 3}->asOrderedSet()", self);
		assertInstanceOf(List.class, result);
		assertEquals(3, ((List<?>) result).size());
	}

	// --- Set uniqueness ---

	@Test
	void set_noDuplicates() throws OclParseException {
		assertEquals(3, eval("Set{1, 2, 3, 2, 1}->size()", self));
	}

	// --- OrderedSet operations ---

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

	@Test
	void orderedSet_noDuplicates() throws OclParseException {
		assertEquals(3, eval("OrderedSet{1, 2, 3, 2, 1}->size()", self));
	}

	// --- Bag operations ---

	@Test
	void bag_allowsDuplicates() throws OclParseException {
		assertEquals(5, eval("Bag{1, 2, 3, 2, 1}->size()", self));
	}

	@Test
	void bag_count() throws OclParseException {
		assertEquals(2, eval("Bag{1, 2, 3, 2, 1}->count(2)", self));
	}

	// --- Chained conversions ---

	@Test
	void sequence_asSet_asSequence() throws OclParseException {
		Object result = eval("Sequence{1, 2, 2, 3}->asSet()->asSequence()", self);
		assertInstanceOf(List.class, result);
		assertEquals(3, ((List<?>) result).size());
	}

	@Test
	void set_asSequence_reverse() throws OclParseException {
		// Converting to sequence enables ordered operations
		Object result = eval("Set{1, 2, 3}->asSequence()->reverse()", self);
		assertInstanceOf(List.class, result);
		assertEquals(3, ((List<?>) result).size());
	}

	// --- Empty collection conversions ---

	@Test
	void emptySequence_asSet() throws OclParseException {
		assertEquals(0, eval("Sequence{}->asSet()->size()", self));
	}

	@Test
	void emptySet_asSequence() throws OclParseException {
		assertEquals(0, eval("Set{}->asSequence()->size()", self));
	}

	// --- Including/excluding with type conversions ---

	@Test
	void set_including_chain() throws OclParseException {
		assertEquals(4, eval("Set{1, 2, 3}->including(4)->size()", self));
	}

	@Test
	void set_including_existing() throws OclParseException {
		// Including existing element in set — no change
		assertEquals(3, eval("Set{1, 2, 3}->including(2)->size()", self));
	}

	@Test
	void sequence_including_existing() throws OclParseException {
		// Including in sequence always adds
		assertEquals(4, eval("Sequence{1, 2, 3}->including(2)->size()", self));
	}
}
