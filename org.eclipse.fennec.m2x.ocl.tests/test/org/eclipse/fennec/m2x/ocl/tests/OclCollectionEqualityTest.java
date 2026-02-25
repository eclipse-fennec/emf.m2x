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
 * Tests for OCL collection equality semantics.
 * Sets are equal if they contain the same elements (order irrelevant).
 * Sequences are equal if they have the same elements in the same order.
 * Bags are equal if they have the same elements with the same multiplicities.
 */
class OclCollectionEqualityTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Set equality ---

	@Test
	void set_equalSameOrder() throws OclParseException {
		assertEquals(true, eval("Set{1, 2, 3} = Set{1, 2, 3}", self));
	}

	@Test
	void set_equalDifferentOrder() throws OclParseException {
		assertEquals(true, eval("Set{1, 2, 3} = Set{3, 1, 2}", self));
	}

	@Test
	void set_notEqual_differentSize() throws OclParseException {
		assertEquals(false, eval("Set{1, 2, 3} = Set{1, 2}", self));
	}

	@Test
	void set_notEqual_differentElements() throws OclParseException {
		assertEquals(false, eval("Set{1, 2, 3} = Set{1, 2, 4}", self));
	}

	@Test
	void set_equalWithDuplicates() throws OclParseException {
		// Duplicates are removed in sets
		assertEquals(true, eval("Set{1, 1, 2} = Set{1, 2}", self));
	}

	@Test
	void set_emptyEquals() throws OclParseException {
		assertEquals(true, eval("Set{} = Set{}", self));
	}

	// --- Sequence equality ---

	@Test
	void sequence_equalSameOrder() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3} = Sequence{1, 2, 3}", self));
	}

	@Test
	void sequence_notEqual_differentOrder() throws OclParseException {
		assertEquals(false, eval("Sequence{1, 2, 3} = Sequence{3, 2, 1}", self));
	}

	@Test
	void sequence_notEqual_differentSize() throws OclParseException {
		assertEquals(false, eval("Sequence{1, 2} = Sequence{1, 2, 3}", self));
	}

	@Test
	void sequence_equalWithDuplicates() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 1, 2} = Sequence{1, 1, 2}", self));
	}

	@Test
	void sequence_notEqual_differentDuplicates() throws OclParseException {
		assertEquals(false, eval("Sequence{1, 1, 2} = Sequence{1, 2, 2}", self));
	}

	@Test
	void sequence_emptyEquals() throws OclParseException {
		assertEquals(true, eval("Sequence{} = Sequence{}", self));
	}

	// --- <> operator ---

	@Test
	void set_notEqual_operator() throws OclParseException {
		assertEquals(true, eval("Set{1, 2} <> Set{1, 3}", self));
	}

	@Test
	void set_notNotEqual_operator() throws OclParseException {
		assertEquals(false, eval("Set{1, 2} <> Set{2, 1}", self));
	}

	@Test
	void sequence_notEqual_operator() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2} <> Sequence{2, 1}", self));
	}

	// --- String collections ---

	@Test
	void set_strings_equal() throws OclParseException {
		assertEquals(true, eval("Set{'a', 'b'} = Set{'b', 'a'}", self));
	}

	@Test
	void sequence_strings_equal() throws OclParseException {
		assertEquals(true, eval("Sequence{'a', 'b'} = Sequence{'a', 'b'}", self));
	}

	@Test
	void sequence_strings_notEqual() throws OclParseException {
		assertEquals(false, eval("Sequence{'a', 'b'} = Sequence{'b', 'a'}", self));
	}

	// --- Boolean collections ---

	@Test
	void set_booleans_equal() throws OclParseException {
		assertEquals(true, eval("Set{true, false} = Set{false, true}", self));
	}

	// --- Computed collections ---

	@Test
	void computed_set_equal() throws OclParseException {
		assertEquals(true, eval(
				"Set{1, 2, 3}->select(i | i > 1) = Set{2, 3}", self));
	}

	@Test
	void computed_sequence_equal() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->select(i | i > 1) = Sequence{2, 3}", self));
	}

	// --- Mixed empty ---

	@Test
	void emptySet_notEqual_nonEmpty() throws OclParseException {
		assertEquals(false, eval("Set{} = Set{1}", self));
	}

	@Test
	void emptySequence_notEqual_nonEmpty() throws OclParseException {
		assertEquals(false, eval("Sequence{} = Sequence{1}", self));
	}
}
