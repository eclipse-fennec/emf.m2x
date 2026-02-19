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
 * Tests for nested iterators and iterator composition.
 * Exercises iterators within iterators, chained arrow operations,
 * and complex iterator patterns.
 */
class OclNestedIteratorTest extends AbstractOclTest {

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

	// --- Iterator within iterator ---

	@Test
	void nestedSelect_innerOuter() throws OclParseException {
		// Select elements from outer that satisfy an inner condition
		assertEquals(2L, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | Sequence{2, 4}->includes(i))->size()",
				alice));
	}

	@Test
	void nestedExists_inForAll() throws OclParseException {
		// For all elements, there exists a matching element in another collection
		assertEquals(true, eval(
				"Set{1, 2}->forAll(i | Set{1, 2, 3}->exists(j | j = i))",
				alice));
	}

	@Test
	void nestedForAll_false() throws OclParseException {
		assertEquals(false, eval(
				"Set{1, 2, 5}->forAll(i | Set{1, 2, 3}->exists(j | j = i))",
				alice));
	}

	@Test
	void nestedCollect_inSelect() throws OclParseException {
		// Select employees whose name size > 3
		Object result = eval(
				"self.employees->select(e | e.name.size() > 3)->collect(e | e.name)",
				company);
		assertInstanceOf(Collection.class, result);
		Collection<?> names = (Collection<?>) result;
		assertEquals(2, names.size()); // Alice(5), Charlie(7) > 3
	}

	// --- Chained arrow operations ---

	@Test
	void chainedSelect_twoFilters() throws OclParseException {
		assertEquals(1L, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i > 2)->select(i | i < 4)->size()",
				alice));
	}

	@Test
	void chainedCollect_select() throws OclParseException {
		// Collect then select
		Object result = eval(
				"Sequence{1, 2, 3}->collect(i | i * 2)->select(i | i > 3)",
				alice);
		assertInstanceOf(Collection.class, result);
		assertEquals(2, ((Collection<?>) result).size()); // 4, 6
	}

	@Test
	void chainedSelect_collect_size() throws OclParseException {
		assertEquals(2L, eval(
				"self.employees->select(e | e.isMarried)->collect(e | e.name)->size()",
				company));
	}

	@Test
	void chainedReject_select() throws OclParseException {
		// reject <3 → {3,4,5}, select <5 → {3,4} = 2
		assertEquals(2L, eval(
				"Sequence{1, 2, 3, 4, 5}->reject(i | i < 3)->select(i | i < 5)->size()",
				alice));
	}

	// --- select + forAll ---

	@Test
	void select_then_forAll() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->select(e | e.isMarried)->forAll(e | e.salary > 50000.0)",
				company));
	}

	// --- collect + sum ---

	@Test
	void collect_then_sum() throws OclParseException {
		assertEquals(6L, eval(
				"Sequence{1, 2, 3}->collect(i | i)->sum()",
				alice));
	}

	@Test
	void select_collect_sum() throws OclParseException {
		Object result = eval(
				"self.employees->select(e | e.isMarried)->collect(e | e.salary)->sum()",
				company);
		assertEquals(140000.0, result);
	}

	// --- any + property access ---

	@Test
	void any_thenProperty() throws OclParseException {
		// Using let to break the chain
		assertEquals(true, eval(
				"let e: Person = self.employees->any(e | e.name = 'Alice') in e.isMarried",
				company));
	}

	// --- Nested iterator variable names ---

	@Test
	void nestedIterators_distinctVarNames() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2}->forAll(a | Sequence{3, 4}->forAll(b | a < b))",
				alice));
	}

	@Test
	void nestedIterators_sameVarName() throws OclParseException {
		// Inner 'i' should shadow outer 'i'
		assertEquals(true, eval(
				"Sequence{1, 2}->forAll(i | Sequence{1, 2}->exists(i | i > 0))",
				alice));
	}

	// --- sortedBy + collect ---

	@Test
	void sortedBy_then_collect() throws OclParseException {
		Object result = eval(
				"self.employees->sortedBy(e | e.name)->collect(e | e.name)",
				company);
		assertInstanceOf(List.class, result);
		List<?> names = (List<?>) result;
		assertEquals("Alice", names.get(0));
		assertEquals("Bob", names.get(1));
		assertEquals("Charlie", names.get(2));
	}

	@Test
	void sortedBy_then_first() throws OclParseException {
		Object result = eval(
				"let first: Person = self.employees->sortedBy(e | e.age)->first() in first.name",
				company);
		assertEquals("Bob", result);
	}

	// --- Complex pipeline ---

	@Test
	void pipeline_selectCollectSortFirst() throws OclParseException {
		Object result = eval(
				"self.employees->select(e | e.salary > 40000.0)" +
				"->collect(e | e.name)->sortedBy(n | n)->first()",
				company);
		assertEquals("Alice", result);
	}

	@Test
	void pipeline_collectSelectSize() throws OclParseException {
		assertEquals(2L, eval(
				"Sequence{1, 2, 3, 4, 5}" +
				"->collect(i | i * i)" +        // {1, 4, 9, 16, 25}
				"->select(i | i > 10)" +         // {16, 25}
				"->size()",
				alice));
	}
}
