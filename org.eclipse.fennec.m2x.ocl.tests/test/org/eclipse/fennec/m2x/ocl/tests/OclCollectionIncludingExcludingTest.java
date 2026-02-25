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
 * Tests for OCL including/excluding operations across all
 * collection types, and their interaction with other operations.
 */
class OclCollectionIncludingExcludingTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Set including ---

	@Test
	void set_including_newElement() throws OclParseException {
		assertEquals(4, eval("Set{1, 2, 3}->including(4)->size()", self));
	}

	@Test
	void set_including_existingElement() throws OclParseException {
		assertEquals(3, eval("Set{1, 2, 3}->including(2)->size()", self));
	}

	@Test
	void set_including_null() throws OclParseException {
		assertEquals(4, eval("Set{1, 2, 3}->including(null)->size()", self));
	}

	@Test
	void set_including_chain() throws OclParseException {
		assertEquals(5, eval("Set{1}->including(2)->including(3)->including(4)->including(5)->size()", self));
	}

	// --- Set excluding ---

	@Test
	void set_excluding_existing() throws OclParseException {
		assertEquals(2, eval("Set{1, 2, 3}->excluding(2)->size()", self));
	}

	@Test
	void set_excluding_nonExisting() throws OclParseException {
		assertEquals(3, eval("Set{1, 2, 3}->excluding(5)->size()", self));
	}

	@Test
	void set_excluding_includes() throws OclParseException {
		assertEquals(false, eval("Set{1, 2, 3}->excluding(2)->includes(2)", self));
	}

	// --- Sequence including ---

	@Test
	void sequence_including() throws OclParseException {
		assertEquals(4, eval("Sequence{1, 2, 3}->including(4)->size()", self));
	}

	@Test
	void sequence_including_duplicate() throws OclParseException {
		assertEquals(4, eval("Sequence{1, 2, 3}->including(2)->size()", self));
	}

	// --- Sequence excluding ---

	@Test
	void sequence_excluding() throws OclParseException {
		assertEquals(2, eval("Sequence{1, 2, 3}->excluding(2)->size()", self));
	}

	// --- Bag including ---

	@Test
	void bag_including() throws OclParseException {
		assertEquals(4, eval("Bag{1, 2, 3}->including(4)->size()", self));
	}

	@Test
	void bag_including_duplicate() throws OclParseException {
		assertEquals(4, eval("Bag{1, 2, 3}->including(2)->size()", self));
	}

	// --- Bag excluding ---

	@Test
	void bag_excluding() throws OclParseException {
		assertEquals(2, eval("Bag{1, 2, 3}->excluding(2)->size()", self));
	}

	// --- OrderedSet including ---

	@Test
	void orderedSet_including_new() throws OclParseException {
		assertEquals(4, eval("OrderedSet{1, 2, 3}->including(4)->size()", self));
	}

	@Test
	void orderedSet_including_existing() throws OclParseException {
		// OrderedSet deduplicates
		assertEquals(3, eval("OrderedSet{1, 2, 3}->including(2)->size()", self));
	}

	// --- OrderedSet excluding ---

	@Test
	void orderedSet_excluding() throws OclParseException {
		assertEquals(2, eval("OrderedSet{1, 2, 3}->excluding(2)->size()", self));
	}

	// --- Including/excluding with strings ---

	@Test
	void set_including_string() throws OclParseException {
		assertEquals(true, eval("Set{'a', 'b'}->including('c')->includes('c')", self));
	}

	@Test
	void set_excluding_string() throws OclParseException {
		assertEquals(false, eval("Set{'a', 'b', 'c'}->excluding('b')->includes('b')", self));
	}

	// --- Including/excluding then forAll ---

	@Test
	void including_then_forAll() throws OclParseException {
		assertEquals(true, eval(
				"Set{2, 4}->including(6)->forAll(i | i.mod(2) = 0)", self));
	}

	@Test
	void excluding_then_forAll() throws OclParseException {
		assertEquals(true, eval(
				"Set{1, 2, 4, 6}->excluding(1)->forAll(i | i.mod(2) = 0)", self));
	}

	// --- Round-trip: including then excluding ---

	@Test
	void including_excluding_roundtrip() throws OclParseException {
		assertEquals(3, eval(
				"Set{1, 2, 3}->including(4)->excluding(4)->size()", self));
	}

	@Test
	void excluding_including_roundtrip() throws OclParseException {
		assertEquals(3, eval(
				"Set{1, 2, 3}->excluding(2)->including(2)->size()", self));
	}
}
