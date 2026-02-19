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
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL collection product, cross-collection operations,
 * and complex multi-collection patterns.
 */
class OclCollectionProductTest extends AbstractOclTest {

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

	// --- Cross-collection queries ---

	@Test
	void twoCollections_forAll_includes() throws OclParseException {
		// All elements of first are in second
		assertEquals(true, eval(
				"Set{1, 2}->forAll(i | Set{1, 2, 3}->includes(i))", alice));
	}

	@Test
	void twoCollections_exists_intersection() throws OclParseException {
		// There exists an element in both collections
		assertEquals(true, eval(
				"Set{1, 2, 3}->exists(i | Set{3, 4, 5}->includes(i))", alice));
	}

	@Test
	void twoCollections_noIntersection() throws OclParseException {
		assertEquals(false, eval(
				"Set{1, 2}->exists(i | Set{3, 4}->includes(i))", alice));
	}

	// --- Combining results from multiple operations ---

	@Test
	void combineResults_union() throws OclParseException {
		// Select even + select odd = all
		assertEquals(5L, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i.mod(2) = 0)" +
				"->union(Sequence{1, 2, 3, 4, 5}->select(i | i.mod(2) = 1))->size()",
				alice));
	}

	@Test
	void combineResults_intersection() throws OclParseException {
		// Elements > 2 AND elements < 5
		assertEquals(2L, eval(
				"Set{1, 2, 3, 4, 5}->select(i | i > 2)" +
				"->intersection(Set{1, 2, 3, 4, 5}->select(i | i < 5))->size()",
				alice));
	}

	// --- Nested collection expressions ---

	@Test
	void nestedCollectionLiteral_inIterator() throws OclParseException {
		// For each element, check against a literal collection
		assertEquals(2L, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | Set{2, 4}->includes(i))->size()",
				alice));
	}

	@Test
	void nestedCollectionLiteral_inLet() throws OclParseException {
		assertEquals(true, eval(
				"let allowed: Set(Integer) = Set{1, 2, 3} in allowed->includes(2)",
				alice));
	}

	// --- Model queries combining collections ---

	@Test
	void model_selectAndCount() throws OclParseException {
		// Number of married employees
		assertEquals(2L, eval(
				"self.employees->select(e | e.isMarried)->size()", company));
	}

	@Test
	void model_selectAndCollect() throws OclParseException {
		// Names of unmarried employees
		Object result = eval(
				"self.employees->select(e | not e.isMarried)->collect(e | e.name)",
				company);
		assertInstanceOf(Collection.class, result);
		Collection<?> names = (Collection<?>) result;
		assertEquals(1, names.size());
	}

	@Test
	void model_collectAndSort() throws OclParseException {
		// Sorted salaries
		Object result = eval(
				"self.employees->collect(e | e.salary)->sortedBy(s | s)",
				company);
		assertInstanceOf(List.class, result);
		List<?> salaries = (List<?>) result;
		assertEquals(45000.0, salaries.get(0));
		assertEquals(60000.0, salaries.get(1));
		assertEquals(80000.0, salaries.get(2));
	}

	@Test
	void model_collectAndMax() throws OclParseException {
		assertEquals(80000.0, eval(
				"self.employees->collect(e | e.salary)->max()", company));
	}

	@Test
	void model_collectAndMin() throws OclParseException {
		assertEquals(45000.0, eval(
				"self.employees->collect(e | e.salary)->min()", company));
	}

	// --- Using let with model collections ---

	@Test
	void model_let_withSelect() throws OclParseException {
		assertEquals(true, eval(
				"let married: Integer = self.employees->select(e | e.isMarried)->size() " +
				"in married > 0",
				company));
	}

	@Test
	void model_let_withCollect() throws OclParseException {
		Object result = eval(
				"let names: Sequence(String) = self.employees->collect(e | e.name) " +
				"in names->sortedBy(n | n)->first()",
				company);
		assertEquals("Alice", result);
	}

	// --- Collection operations chain ---

	@Test
	void chain_selectRejectSize() throws OclParseException {
		// Start with 1..10, select even, reject > 6
		assertEquals(3L, eval(
				"Sequence{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}" +
				"->select(i | i.mod(2) = 0)" +       // {2,4,6,8,10}
				"->reject(i | i > 6)" +               // {2,4,6}
				"->size()",
				alice));
	}

	@Test
	void chain_collectSelectForAll() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3, 4, 5}" +
				"->collect(i | i * 2)" +              // {2,4,6,8,10}
				"->select(i | i > 5)" +               // {6,8,10}
				"->forAll(i | i.mod(2) = 0)",         // all even → true
				alice));
	}

	// --- Collection iteration with arithmetic ---

	@Test
	void iterate_factorial() throws OclParseException {
		// 5! = 120
		assertEquals(120L, eval(
				"Sequence{1, 2, 3, 4, 5}->iterate(i; acc: Integer = 1 | acc * i)",
				alice));
	}

	@Test
	void iterate_maxManual() throws OclParseException {
		// Manual max via iterate
		assertEquals(5L, eval(
				"Sequence{3, 1, 5, 2, 4}->iterate(i; acc: Integer = 0 | if i > acc then i else acc endif)",
				alice));
	}
}
