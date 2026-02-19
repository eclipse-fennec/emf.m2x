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
 * Tests for Bag-specific union and intersection operations,
 * focusing on duplicate handling which distinguishes Bags from Sets.
 */
class OclBagUnionIntersectionTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Bag union ---

	@Test
	void bagUnion_noOverlap() throws OclParseException {
		assertEquals(4, eval("Bag{1, 2}->union(Bag{3, 4})->size()", self));
	}

	@Test
	void bagUnion_withOverlap() throws OclParseException {
		// Bags keep duplicates: {1,2} union {2,3} = {1,2,2,3}
		assertEquals(4, eval("Bag{1, 2}->union(Bag{2, 3})->size()", self));
	}

	@Test
	void bagUnion_duplicatesAdded() throws OclParseException {
		// {1,1} union {1} = {1,1,1}
		assertEquals(3, eval("Bag{1, 1}->union(Bag{1})->size()", self));
	}

	@Test
	void bagUnion_emptyBag() throws OclParseException {
		assertEquals(2, eval("Bag{1, 2}->union(Bag{})->size()", self));
	}

	@Test
	void bagUnion_bothEmpty() throws OclParseException {
		assertEquals(0, eval("Bag{}->union(Bag{})->size()", self));
	}

	@Test
	void bagUnion_thenIncludes() throws OclParseException {
		assertEquals(true, eval("Bag{1}->union(Bag{2})->includes(2)", self));
	}

	// --- Bag intersection ---

	@Test
	void bagIntersection_withOverlap() throws OclParseException {
		// Bag intersection: {1,2,3} intersect {2,3,4} = {2,3}
		assertEquals(2, eval("Bag{1, 2, 3}->intersection(Bag{2, 3, 4})->size()", self));
	}

	@Test
	void bagIntersection_noOverlap() throws OclParseException {
		assertEquals(0, eval("Bag{1, 2}->intersection(Bag{3, 4})->size()", self));
	}

	@Test
	void bagIntersection_identical() throws OclParseException {
		assertEquals(3, eval("Bag{1, 2, 3}->intersection(Bag{1, 2, 3})->size()", self));
	}

	@Test
	void bagIntersection_emptyResult() throws OclParseException {
		assertEquals(true, eval("Bag{1}->intersection(Bag{2})->isEmpty()", self));
	}

	// --- Set union ---

	@Test
	void setUnion_noOverlap() throws OclParseException {
		assertEquals(4, eval("Set{1, 2}->union(Set{3, 4})->size()", self));
	}

	@Test
	void setUnion_withOverlap() throws OclParseException {
		// Sets deduplicate: {1,2} union {2,3} = {1,2,3}
		assertEquals(3, eval("Set{1, 2}->union(Set{2, 3})->size()", self));
	}

	@Test
	void setUnion_empty() throws OclParseException {
		assertEquals(2, eval("Set{1, 2}->union(Set{})->size()", self));
	}

	// --- Set intersection ---

	@Test
	void setIntersection_withOverlap() throws OclParseException {
		assertEquals(2, eval("Set{1, 2, 3}->intersection(Set{2, 3, 4})->size()", self));
	}

	@Test
	void setIntersection_noOverlap() throws OclParseException {
		assertEquals(0, eval("Set{1, 2}->intersection(Set{3, 4})->size()", self));
	}

	@Test
	void setIntersection_includes() throws OclParseException {
		assertEquals(true,
				eval("Set{1, 2, 3}->intersection(Set{2, 3, 4})->includes(2)", self));
	}

	@Test
	void setIntersection_excludes() throws OclParseException {
		assertEquals(true,
				eval("Set{1, 2, 3}->intersection(Set{2, 3, 4})->excludes(1)", self));
	}

	// --- Set minus (difference) ---

	@Test
	void setDifference() throws OclParseException {
		// Operator precedence: -> binds tighter than -, so use parens
		assertEquals(1, eval("(Set{1, 2, 3} - Set{2, 3})->size()", self));
	}

	// --- Union then operations ---

	@Test
	void setUnion_thenSize() throws OclParseException {
		assertEquals(5, eval("Set{1, 2, 3}->union(Set{4, 5})->size()", self));
	}

	@Test
	void setUnion_thenForAll() throws OclParseException {
		assertEquals(true, eval(
				"Set{1, 2}->union(Set{3, 4})->forAll(x | x > 0)", self));
	}

	@Test
	void setUnion_thenSum() throws OclParseException {
		assertEquals(10, eval("Set{1, 2}->union(Set{3, 4})->sum()", self));
	}

	// --- String collections ---

	@Test
	void setUnion_strings() throws OclParseException {
		assertEquals(3, eval("Set{'a', 'b'}->union(Set{'b', 'c'})->size()", self));
	}

	@Test
	void setIntersection_strings() throws OclParseException {
		assertEquals(1, eval("Set{'a', 'b'}->intersection(Set{'b', 'c'})->size()", self));
	}

	// --- Chained operations ---

	@Test
	void union_thenIntersection() throws OclParseException {
		// {1,2} union {3,4} = {1,2,3,4}, intersect with {2,3} = {2,3}
		assertEquals(2, eval(
				"Set{1, 2}->union(Set{3, 4})->intersection(Set{2, 3})->size()", self));
	}

	@Test
	void intersection_thenUnion() throws OclParseException {
		// {1,2,3} intersect {2,3,4} = {2,3}, union {5} = {2,3,5}
		assertEquals(3, eval(
				"Set{1, 2, 3}->intersection(Set{2, 3, 4})->union(Set{5})->size()", self));
	}
}
