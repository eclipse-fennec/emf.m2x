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
import static org.junit.jupiter.api.Assertions.assertSame;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclInvalid;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for the at() operation on ordered collections and strings.
 * OCL uses 1-based indexing.
 */
class OclCollectionAtTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Sequence at() ---

	@Test
	void sequence_atFirst() throws OclParseException {
		assertEquals(10, eval("Sequence{10, 20, 30}->at(1)", self));
	}

	@Test
	void sequence_atMiddle() throws OclParseException {
		assertEquals(20, eval("Sequence{10, 20, 30}->at(2)", self));
	}

	@Test
	void sequence_atLast() throws OclParseException {
		assertEquals(30, eval("Sequence{10, 20, 30}->at(3)", self));
	}

	@Test
	void sequence_atOutOfBounds_high() throws OclParseException {
		assertSame(OclInvalid.INSTANCE, eval("Sequence{1, 2, 3}->at(4)", self));
	}

	@Test
	void sequence_atOutOfBounds_zero() throws OclParseException {
		assertSame(OclInvalid.INSTANCE, eval("Sequence{1, 2, 3}->at(0)", self));
	}

	@Test
	void sequence_atOutOfBounds_negative() throws OclParseException {
		assertSame(OclInvalid.INSTANCE, eval("Sequence{1, 2, 3}->at(-1)", self));
	}

	// --- Sequence at() with strings ---

	@Test
	void sequence_string_atFirst() throws OclParseException {
		assertEquals("apple", eval("Sequence{'apple', 'banana', 'cherry'}->at(1)", self));
	}

	@Test
	void sequence_string_atLast() throws OclParseException {
		assertEquals("cherry", eval("Sequence{'apple', 'banana', 'cherry'}->at(3)", self));
	}

	// --- OrderedSet at() ---

	@Test
	void orderedSet_atFirst() throws OclParseException {
		assertEquals(10, eval("OrderedSet{10, 20, 30}->at(1)", self));
	}

	@Test
	void orderedSet_atLast() throws OclParseException {
		assertEquals(30, eval("OrderedSet{10, 20, 30}->at(3)", self));
	}

	// --- String at() ---

	@Test
	void string_atFirst() throws OclParseException {
		assertEquals("h", eval("'hello'.at(1)", self));
	}

	@Test
	void string_atMiddle() throws OclParseException {
		assertEquals("l", eval("'hello'.at(3)", self));
	}

	@Test
	void string_atLast() throws OclParseException {
		assertEquals("o", eval("'hello'.at(5)", self));
	}

	@Test
	void string_atOutOfBounds() throws OclParseException {
		assertSame(OclInvalid.INSTANCE, eval("'hello'.at(6)", self));
	}

	@Test
	void string_atZero() throws OclParseException {
		assertSame(OclInvalid.INSTANCE, eval("'hello'.at(0)", self));
	}

	// --- at() with model data ---

	@Test
	void property_stringAt() throws OclParseException {
		assertEquals("A", eval("self.name.at(1)", self));
	}

	@Test
	void property_stringAtLast() throws OclParseException {
		assertEquals("e", eval("self.name.at(self.name.size())", self));
	}

	// --- at() in expressions ---

	@Test
	void at_inComparison() throws OclParseException {
		assertEquals(true, eval("Sequence{10, 20, 30}->at(2) > 15", self));
	}

	@Test
	void at_inArithmetic() throws OclParseException {
		assertEquals(30, eval("Sequence{10, 20}->at(1) + Sequence{10, 20}->at(2)", self));
	}

	// --- at() after operations ---

	@Test
	void sortedBy_thenAt() throws OclParseException {
		assertEquals(1, eval("Sequence{3, 1, 2}->sortedBy(x | x)->at(1)", self));
	}

	@Test
	void append_thenAt() throws OclParseException {
		assertEquals(4, eval("Sequence{1, 2, 3}->append(4)->at(4)", self));
	}

	// --- Range at() ---

	@Test
	void range_at() throws OclParseException {
		assertEquals(5, eval("Sequence{1..10}->at(5)", self));
	}

	@Test
	void range_atFirst() throws OclParseException {
		assertEquals(1, eval("Sequence{1..10}->at(1)", self));
	}

	@Test
	void range_atLast() throws OclParseException {
		assertEquals(10, eval("Sequence{1..10}->at(10)", self));
	}
}
