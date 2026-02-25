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
 * Tests for OCL Set {@code symmetricDifference} operation.
 * symmetricDifference(s2) returns elements in either set but not both.
 * Also tests {@code -} (set minus / difference) more thoroughly.
 */
class OclSymmetricDifferenceTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- symmetricDifference ---

	@Test
	void symDiff_disjoint() throws OclParseException {
		// {1,2} symDiff {3,4} = {1,2,3,4}
		assertEquals(4, eval(
				"Set{1, 2}->symmetricDifference(Set{3, 4})->size()", self));
	}

	@Test
	void symDiff_overlap() throws OclParseException {
		// {1,2,3} symDiff {2,3,4} = {1,4}
		assertEquals(2, eval(
				"Set{1, 2, 3}->symmetricDifference(Set{2, 3, 4})->size()", self));
	}

	@Test
	void symDiff_identical() throws OclParseException {
		// {1,2,3} symDiff {1,2,3} = {}
		assertEquals(0, eval(
				"Set{1, 2, 3}->symmetricDifference(Set{1, 2, 3})->size()", self));
	}

	@Test
	void symDiff_emptyLeft() throws OclParseException {
		assertEquals(3, eval(
				"Set{}->symmetricDifference(Set{1, 2, 3})->size()", self));
	}

	@Test
	void symDiff_emptyRight() throws OclParseException {
		assertEquals(3, eval(
				"Set{1, 2, 3}->symmetricDifference(Set{})->size()", self));
	}

	@Test
	void symDiff_bothEmpty() throws OclParseException {
		assertEquals(0, eval(
				"Set{}->symmetricDifference(Set{})->size()", self));
	}

	@Test
	void symDiff_singleOverlap() throws OclParseException {
		// {1,2,3} symDiff {3,4,5} = {1,2,4,5}
		assertEquals(4, eval(
				"Set{1, 2, 3}->symmetricDifference(Set{3, 4, 5})->size()", self));
	}

	@Test
	void symDiff_includes() throws OclParseException {
		assertEquals(true, eval(
				"Set{1, 2, 3}->symmetricDifference(Set{2, 3, 4})->includes(1)", self));
	}

	@Test
	void symDiff_excludesCommon() throws OclParseException {
		assertEquals(false, eval(
				"Set{1, 2, 3}->symmetricDifference(Set{2, 3, 4})->includes(2)", self));
	}

	@Test
	void symDiff_isCommutative() throws OclParseException {
		// A symDiff B = B symDiff A
		assertEquals(
				eval("Set{1, 2, 3}->symmetricDifference(Set{3, 4, 5})->size()", self),
				eval("Set{3, 4, 5}->symmetricDifference(Set{1, 2, 3})->size()", self));
	}

	// --- Set minus / difference ---

	@Test
	void setMinus_basic() throws OclParseException {
		// {1,2,3} - {2,3} = {1}
		assertEquals(1, eval(
				"(Set{1, 2, 3} - Set{2, 3})->size()", self));
	}

	@Test
	void setMinus_disjoint() throws OclParseException {
		// {1,2,3} - {4,5} = {1,2,3}
		assertEquals(3, eval(
				"(Set{1, 2, 3} - Set{4, 5})->size()", self));
	}

	@Test
	void setMinus_identical() throws OclParseException {
		assertEquals(0, eval(
				"(Set{1, 2, 3} - Set{1, 2, 3})->size()", self));
	}

	@Test
	void setMinus_emptyRight() throws OclParseException {
		assertEquals(3, eval(
				"(Set{1, 2, 3} - Set{})->size()", self));
	}

	@Test
	void setMinus_emptyLeft() throws OclParseException {
		assertEquals(0, eval(
				"(Set{} - Set{1, 2, 3})->size()", self));
	}

	@Test
	void setMinus_superset() throws OclParseException {
		// {1,2} - {1,2,3,4} = {}
		assertEquals(0, eval(
				"(Set{1, 2} - Set{1, 2, 3, 4})->size()", self));
	}

	@Test
	void setMinus_includes() throws OclParseException {
		assertEquals(true, eval(
				"(Set{1, 2, 3} - Set{2})->includes(1)", self));
	}

	@Test
	void setMinus_excludesRemoved() throws OclParseException {
		assertEquals(false, eval(
				"(Set{1, 2, 3} - Set{2})->includes(2)", self));
	}

	// --- Set minus is not commutative ---

	@Test
	void setMinus_notCommutative() throws OclParseException {
		// {1,2,3} - {3,4} = {1,2} (size 2)
		// {3,4} - {1,2,3} = {4} (size 1)
		assertEquals(2, eval("(Set{1, 2, 3} - Set{3, 4})->size()", self));
		assertEquals(1, eval("(Set{3, 4} - Set{1, 2, 3})->size()", self));
	}

	// --- Combined ---

	@Test
	void symDiff_thenSize_thenCompare() throws OclParseException {
		assertEquals(true, eval(
				"Set{1, 2, 3}->symmetricDifference(Set{2, 3, 4})->size() = 2", self));
	}

	@Test
	void setMinus_thenForAll() throws OclParseException {
		// {1,2,3} - {3} = {1,2}, forAll < 3 → true
		assertEquals(true, eval(
				"(Set{1, 2, 3} - Set{3})->forAll(i | i < 3)", self));
	}
}
