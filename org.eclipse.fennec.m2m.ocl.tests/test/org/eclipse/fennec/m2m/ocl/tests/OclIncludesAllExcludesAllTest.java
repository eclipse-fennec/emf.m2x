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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL {@code includesAll} and {@code excludesAll} operations
 * on collections. These check subset/disjoint relationships.
 */
class OclIncludesAllExcludesAllTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- includesAll ---

	@Test
	void includesAll_subset() throws OclParseException {
		assertEquals(true, eval(
				"Set{1, 2, 3, 4, 5}->includesAll(Set{1, 2, 3})", self));
	}

	@Test
	void includesAll_equal() throws OclParseException {
		assertEquals(true, eval(
				"Set{1, 2, 3}->includesAll(Set{1, 2, 3})", self));
	}

	@Test
	void includesAll_notSubset() throws OclParseException {
		assertEquals(false, eval(
				"Set{1, 2, 3}->includesAll(Set{2, 3, 4})", self));
	}

	@Test
	void includesAll_emptyArg() throws OclParseException {
		// Empty set is subset of everything
		assertEquals(true, eval(
				"Set{1, 2, 3}->includesAll(Set{})", self));
	}

	@Test
	void includesAll_emptyReceiver() throws OclParseException {
		assertEquals(false, eval(
				"Set{}->includesAll(Set{1})", self));
	}

	@Test
	void includesAll_bothEmpty() throws OclParseException {
		assertEquals(true, eval(
				"Set{}->includesAll(Set{})", self));
	}

	@Test
	void includesAll_disjoint() throws OclParseException {
		assertEquals(false, eval(
				"Set{1, 2, 3}->includesAll(Set{4, 5})", self));
	}

	// --- includesAll on Sequence ---

	@Test
	void includesAll_sequence() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3, 4}->includesAll(Sequence{2, 3})", self));
	}

	@Test
	void includesAll_sequence_withDups() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 2, 3}->includesAll(Sequence{2})", self));
	}

	// --- excludesAll ---

	@Test
	void excludesAll_disjoint() throws OclParseException {
		assertEquals(true, eval(
				"Set{1, 2, 3}->excludesAll(Set{4, 5, 6})", self));
	}

	@Test
	void excludesAll_overlap() throws OclParseException {
		assertEquals(false, eval(
				"Set{1, 2, 3}->excludesAll(Set{3, 4, 5})", self));
	}

	@Test
	void excludesAll_subset() throws OclParseException {
		assertEquals(false, eval(
				"Set{1, 2, 3}->excludesAll(Set{1, 2})", self));
	}

	@Test
	void excludesAll_emptyArg() throws OclParseException {
		assertEquals(true, eval(
				"Set{1, 2, 3}->excludesAll(Set{})", self));
	}

	@Test
	void excludesAll_emptyReceiver() throws OclParseException {
		assertEquals(true, eval(
				"Set{}->excludesAll(Set{1, 2})", self));
	}

	@Test
	void excludesAll_bothEmpty() throws OclParseException {
		assertEquals(true, eval(
				"Set{}->excludesAll(Set{})", self));
	}

	// --- excludesAll on Sequence ---

	@Test
	void excludesAll_sequence_disjoint() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->excludesAll(Sequence{4, 5})", self));
	}

	@Test
	void excludesAll_sequence_overlap() throws OclParseException {
		assertEquals(false, eval(
				"Sequence{1, 2, 3}->excludesAll(Sequence{3, 4})", self));
	}

	// --- Combined with operations ---

	@Test
	void includesAll_afterSelect() throws OclParseException {
		// select > 2 → {3,4,5}, includesAll({3,4}) → true
		assertEquals(true, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i > 2)->includesAll(Sequence{3, 4})",
				self));
	}

	@Test
	void excludesAll_afterReject() throws OclParseException {
		// reject > 3 → {1,2,3}, excludesAll({4,5}) → true
		assertEquals(true, eval(
				"Sequence{1, 2, 3, 4, 5}->reject(i | i > 3)->excludesAll(Sequence{4, 5})",
				self));
	}

	// --- In conditions ---

	@Test
	void includesAll_inIf() throws OclParseException {
		assertEquals("yes", eval(
				"if Set{1, 2, 3}->includesAll(Set{1, 2}) then 'yes' else 'no' endif",
				self));
	}

	@Test
	void excludesAll_inIf() throws OclParseException {
		assertEquals("yes", eval(
				"if Set{1, 2, 3}->excludesAll(Set{4, 5}) then 'yes' else 'no' endif",
				self));
	}
}
