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
 * Advanced tests for OCL Set operations.
 * Covers set algebra, complex set operations, and set operations
 * on model elements.
 */
class OclSetAdvancedTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject charlie;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 60000.0, true);
		bob = createPerson("Bob", 25, 45000.0, false);
		charlie = createPerson("Charlie", 35, 80000.0, true);
		company = createCompany("ACME", alice, bob, charlie);
	}

	// --- Set algebra ---

	@Test
	void union_disjointSets() throws OclParseException {
		assertEquals(4L, eval("Set{1, 2}->union(Set{3, 4})->size()", alice));
	}

	@Test
	void union_overlappingSets() throws OclParseException {
		assertEquals(4L, eval("Set{1, 2, 3}->union(Set{2, 3, 4})->size()", alice));
	}

	@Test
	void union_identicalSets() throws OclParseException {
		assertEquals(3L, eval("Set{1, 2, 3}->union(Set{1, 2, 3})->size()", alice));
	}

	@Test
	void intersection_partial() throws OclParseException {
		assertEquals(2L, eval("Set{1, 2, 3}->intersection(Set{2, 3, 4})->size()", alice));
	}

	@Test
	void intersection_empty() throws OclParseException {
		assertEquals(0L, eval("Set{1, 2}->intersection(Set{3, 4})->size()", alice));
	}

	@Test
	void intersection_full() throws OclParseException {
		assertEquals(3L, eval("Set{1, 2, 3}->intersection(Set{1, 2, 3})->size()", alice));
	}

	@Test
	void difference() throws OclParseException {
		assertEquals(1L, eval("(Set{1, 2, 3} - Set{2, 3})->size()", alice));
	}

	@Test
	void difference_noOverlap() throws OclParseException {
		assertEquals(3L, eval("(Set{1, 2, 3} - Set{4, 5})->size()", alice));
	}

	@Test
	void difference_fullOverlap() throws OclParseException {
		assertEquals(0L, eval("(Set{1, 2, 3} - Set{1, 2, 3})->size()", alice));
	}

	@Test
	void symmetricDifference_basic() throws OclParseException {
		// {1,2,3} symDiff {2,3,4} = {1,4}
		assertEquals(2L, eval(
				"Set{1, 2, 3}->symmetricDifference(Set{2, 3, 4})->size()", alice));
	}

	@Test
	void symmetricDifference_disjoint() throws OclParseException {
		assertEquals(4L, eval(
				"Set{1, 2}->symmetricDifference(Set{3, 4})->size()", alice));
	}

	@Test
	void symmetricDifference_identical() throws OclParseException {
		assertEquals(0L, eval(
				"Set{1, 2, 3}->symmetricDifference(Set{1, 2, 3})->size()", alice));
	}

	// --- Set including/excluding chains ---

	@Test
	void including_chain() throws OclParseException {
		assertEquals(5L, eval(
				"Set{1, 2, 3}->including(4)->including(5)->size()", alice));
	}

	@Test
	void excluding_chain() throws OclParseException {
		assertEquals(1L, eval(
				"Set{1, 2, 3}->excluding(1)->excluding(2)->size()", alice));
	}

	@Test
	void including_existing() throws OclParseException {
		// Including existing element doesn't change set
		assertEquals(3L, eval("Set{1, 2, 3}->including(2)->size()", alice));
	}

	// --- count ---

	@Test
	void set_count_present() throws OclParseException {
		// In a set, count is always 0 or 1
		assertEquals(1L, eval("Set{1, 2, 3}->count(2)", alice));
	}

	@Test
	void set_count_absent() throws OclParseException {
		assertEquals(0L, eval("Set{1, 2, 3}->count(5)", alice));
	}

	// --- sum / min / max ---

	@Test
	void set_sum() throws OclParseException {
		assertEquals(6L, eval("Set{1, 2, 3}->sum()", alice));
	}

	@Test
	void set_min() throws OclParseException {
		assertEquals(1L, eval("Set{3, 1, 2}->min()", alice));
	}

	@Test
	void set_max() throws OclParseException {
		assertEquals(3L, eval("Set{3, 1, 2}->max()", alice));
	}

	// --- Set operations on model elements ---

	@Test
	void set_fromModel_size() throws OclParseException {
		assertEquals(3L, eval("self.employees->asSet()->size()", company));
	}

	@Test
	void set_fromModel_select() throws OclParseException {
		Object result = eval(
				"self.employees->select(e | e.isMarried)->asSet()", company);
		assertInstanceOf(Collection.class, result);
		assertEquals(2, ((Collection<?>) result).size());
	}

	@Test
	void set_fromModel_collect_asSet() throws OclParseException {
		// Collect names, then convert to set (removes duplicates if any)
		Object result = eval(
				"self.employees->collect(e | e.name)->asSet()", company);
		assertInstanceOf(Collection.class, result);
		assertEquals(3, ((Collection<?>) result).size());
	}

	// --- Set with strings ---

	@Test
	void set_strings_union() throws OclParseException {
		assertEquals(4L, eval(
				"Set{'a', 'b'}->union(Set{'c', 'd'})->size()", alice));
	}

	@Test
	void set_strings_includes() throws OclParseException {
		assertEquals(true, eval("Set{'hello', 'world'}->includes('hello')", alice));
	}

	// --- Set with booleans ---

	@Test
	void set_booleans() throws OclParseException {
		assertEquals(2L, eval("Set{true, false, true, false}->size()", alice));
	}

	// --- Chained set operations ---

	@Test
	void chain_unionIntersection() throws OclParseException {
		// {1,2} union {2,3} → {1,2,3}, then intersection {2,3,4} → {2,3}
		assertEquals(2L, eval(
				"Set{1, 2}->union(Set{2, 3})->intersection(Set{2, 3, 4})->size()",
				alice));
	}

	@Test
	void chain_selectIncludesAll() throws OclParseException {
		assertEquals(true, eval(
				"Set{1, 2, 3, 4, 5}->select(i | i > 2)->includesAll(Set{3, 4, 5})",
				alice));
	}
}
