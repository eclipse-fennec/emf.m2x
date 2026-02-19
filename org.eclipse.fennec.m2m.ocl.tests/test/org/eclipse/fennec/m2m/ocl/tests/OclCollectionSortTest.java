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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL sortedBy operation across different scenarios.
 * Covers sorting by identity, by property, ascending/descending
 * (via reverse), and sorting different types.
 */
class OclCollectionSortTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject charlie;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 42, 95000.0, true);
		bob = createPerson("Bob", 28, 52000.0, false);
		charlie = createPerson("Charlie", 35, 78000.0, true);
		company = createCompany("ACME", alice, bob, charlie);
	}

	// --- Sort integers ---

	@Test
	void sortIntegers_ascending() throws OclParseException {
		assertEquals(List.of(1L, 2L, 3L, 4L, 5L),
				eval("Sequence{3, 1, 4, 5, 2}->sortedBy(i | i)", alice));
	}

	@Test
	void sortIntegers_alreadySorted() throws OclParseException {
		assertEquals(List.of(1L, 2L, 3L),
				eval("Sequence{1, 2, 3}->sortedBy(i | i)", alice));
	}

	@Test
	void sortIntegers_reverseSorted() throws OclParseException {
		assertEquals(List.of(1L, 2L, 3L),
				eval("Sequence{3, 2, 1}->sortedBy(i | i)", alice));
	}

	@Test
	void sortIntegers_withDuplicates() throws OclParseException {
		assertEquals(List.of(1L, 2L, 2L, 3L),
				eval("Sequence{2, 3, 1, 2}->sortedBy(i | i)", alice));
	}

	@Test
	void sortIntegers_singleton() throws OclParseException {
		assertEquals(List.of(42L),
				eval("Sequence{42}->sortedBy(i | i)", alice));
	}

	@Test
	void sortIntegers_negative() throws OclParseException {
		assertEquals(List.of(-3L, -1L, 0L, 2L, 5L),
				eval("Sequence{5, -1, 0, 2, -3}->sortedBy(i | i)", alice));
	}

	// --- Sort strings ---

	@Test
	void sortStrings() throws OclParseException {
		assertEquals(List.of("apple", "banana", "cherry"),
				eval("Sequence{'cherry', 'apple', 'banana'}->sortedBy(s | s)", alice));
	}

	@Test
	void sortStrings_byLength() throws OclParseException {
		Object result = eval(
				"Sequence{'bb', 'a', 'ccc'}->sortedBy(s | s.size())", alice);
		assertInstanceOf(List.class, result);
		List<?> sorted = (List<?>) result;
		assertEquals("a", sorted.get(0));
		assertEquals("bb", sorted.get(1));
		assertEquals("ccc", sorted.get(2));
	}

	// --- Sort + reverse for descending ---

	@Test
	void sortDescending() throws OclParseException {
		assertEquals(List.of(5L, 4L, 3L, 2L, 1L),
				eval("Sequence{3, 1, 4, 5, 2}->sortedBy(i | i)->reverse()", alice));
	}

	// --- Sort by model property ---

	@Test
	void sortByName() throws OclParseException {
		Object result = eval(
				"self.employees->sortedBy(e | e.name)->collect(e | e.name)", company);
		assertEquals(List.of("Alice", "Bob", "Charlie"), result);
	}

	@Test
	void sortByAge() throws OclParseException {
		Object result = eval(
				"self.employees->sortedBy(e | e.age)->collect(e | e.name)", company);
		assertEquals(List.of("Bob", "Charlie", "Alice"), result);
	}

	@Test
	void sortBySalary() throws OclParseException {
		Object result = eval(
				"self.employees->sortedBy(e | e.salary)->collect(e | e.name)", company);
		assertEquals(List.of("Bob", "Charlie", "Alice"), result);
	}

	@Test
	void sortBySalary_descending_last() throws OclParseException {
		// Highest salary is first after reverse
		Object result = eval(
				"let sorted: Person = self.employees->sortedBy(e | e.salary)->reverse()->first() " +
				"in sorted.name",
				company);
		assertEquals("Alice", result);
	}

	// --- Sort from Set (produces OrderedSet/Sequence) ---

	@Test
	void sortSet() throws OclParseException {
		Object result = eval("Set{5, 3, 1, 4, 2}->sortedBy(i | i)->first()", alice);
		assertEquals(1L, result);
	}

	@Test
	void sortSet_last() throws OclParseException {
		assertEquals(5L, eval("Set{5, 3, 1, 4, 2}->sortedBy(i | i)->last()", alice));
	}

	// --- Sort then access ---

	@Test
	void sort_thenFirst() throws OclParseException {
		assertEquals(1L, eval("Sequence{3, 1, 2}->sortedBy(i | i)->first()", alice));
	}

	@Test
	void sort_thenLast() throws OclParseException {
		assertEquals(3L, eval("Sequence{3, 1, 2}->sortedBy(i | i)->last()", alice));
	}

	@Test
	void sort_thenAt() throws OclParseException {
		assertEquals(2L, eval("Sequence{3, 1, 2}->sortedBy(i | i)->at(2)", alice));
	}

	// --- Sort by computed value ---

	@Test
	void sortByComputed() throws OclParseException {
		// Sort by absolute value (smallest abs first)
		Object result = eval(
				"Sequence{-3, 1, -2, 4}->sortedBy(i | i.abs())", alice);
		assertInstanceOf(List.class, result);
		List<?> sorted = (List<?>) result;
		assertEquals(1L, sorted.get(0));
	}

	// --- Sort + select ---

	@Test
	void select_thenSort() throws OclParseException {
		assertEquals(List.of(3L, 4L, 5L), eval(
				"Sequence{5, 1, 3, 2, 4}->select(i | i > 2)->sortedBy(i | i)", alice));
	}

	@Test
	void sort_thenSelect() throws OclParseException {
		assertEquals(List.of(3L, 4L, 5L), eval(
				"Sequence{5, 1, 3, 2, 4}->sortedBy(i | i)->select(i | i > 2)", alice));
	}
}
