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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for Bag and OrderedSet equality semantics.
 *
 * <p>Bag equality: same elements with same multiplicities (order irrelevant).
 * <p>OrderedSet equality: same elements in same order (no duplicates).
 */
class OclBagOrderedSetEqualityTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// === Bag equality ===

	@Test
	void bag_equalSameElements() throws OclParseException {
		assertEquals(true, eval("Bag{1, 2, 3} = Bag{1, 2, 3}", self));
	}

	@Test
	void bag_equalDifferentOrder() throws OclParseException {
		// Bags are unordered — same elements in different order are equal
		assertEquals(true, eval("Bag{1, 2, 3} = Bag{3, 1, 2}", self));
	}

	@Test
	void bag_equalWithDuplicates() throws OclParseException {
		assertEquals(true, eval("Bag{1, 1, 2} = Bag{1, 2, 1}", self));
	}

	@Test
	void bag_notEqual_differentMultiplicities() throws OclParseException {
		// Same elements but different counts
		assertEquals(false, eval("Bag{1, 1, 2} = Bag{1, 2, 2}", self));
	}

	@Test
	void bag_notEqual_differentSize() throws OclParseException {
		assertEquals(false, eval("Bag{1, 2} = Bag{1, 2, 3}", self));
	}

	@Test
	void bag_emptyEquals() throws OclParseException {
		assertEquals(true, eval("Bag{} = Bag{}", self));
	}

	@Test
	void bag_notEqual_operator() throws OclParseException {
		assertEquals(true, eval("Bag{1, 1} <> Bag{1}", self));
	}

	// === OrderedSet equality ===

	@Test
	void orderedSet_equalSameOrder() throws OclParseException {
		assertEquals(true, eval("OrderedSet{1, 2, 3} = OrderedSet{1, 2, 3}", self));
	}

	@Test
	void orderedSet_notEqual_differentOrder() throws OclParseException {
		// OrderedSet is ordered — different order means not equal
		assertEquals(false, eval("OrderedSet{1, 2, 3} = OrderedSet{3, 2, 1}", self));
	}

	@Test
	void orderedSet_equalWithDuplicatesRemoved() throws OclParseException {
		// OrderedSet removes duplicates
		assertEquals(true, eval("OrderedSet{1, 1, 2} = OrderedSet{1, 2}", self));
	}

	@Test
	void orderedSet_emptyEquals() throws OclParseException {
		assertEquals(true, eval("OrderedSet{} = OrderedSet{}", self));
	}

	@Test
	void orderedSet_notEqual_operator() throws OclParseException {
		assertEquals(true, eval("OrderedSet{1, 2} <> OrderedSet{2, 1}", self));
	}

	// === Bag with strings ===

	@Test
	void bag_strings_equalDifferentOrder() throws OclParseException {
		assertEquals(true, eval("Bag{'a', 'b', 'a'} = Bag{'a', 'a', 'b'}", self));
	}

	// === OrderedSet with strings ===

	@Test
	void orderedSet_strings_sameOrder() throws OclParseException {
		assertEquals(true, eval("OrderedSet{'a', 'b', 'c'} = OrderedSet{'a', 'b', 'c'}", self));
	}

	@Test
	void orderedSet_strings_differentOrder() throws OclParseException {
		assertEquals(false, eval("OrderedSet{'a', 'b'} = OrderedSet{'b', 'a'}", self));
	}

	// === Bag computed ===

	@Test
	void bag_computed_equality() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->collect(x | x)->asBag() = Bag{3, 2, 1}", self));
	}

	// === Single element ===

	@Test
	void bag_singleElement() throws OclParseException {
		assertEquals(true, eval("Bag{42} = Bag{42}", self));
	}

	@Test
	void orderedSet_singleElement() throws OclParseException {
		assertEquals(true, eval("OrderedSet{42} = OrderedSet{42}", self));
	}
}
