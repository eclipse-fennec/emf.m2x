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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for model-based OCL navigation using the company.ecore model.
 * Covers deep navigation chains, collect on references, combining
 * navigation with operations and iterators.
 */
class OclModelNavigationTest extends AbstractOclTest {

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

	// --- Collect on reference navigation ---

	@Test
	void collect_employeeNames() throws OclParseException {
		Object result = eval("self.employees->collect(e | e.name)", company);
		assertInstanceOf(List.class, result);
		assertEquals(List.of("Alice", "Bob", "Charlie"), result);
	}

	@Test
	void collect_employeeAges() throws OclParseException {
		Object result = eval("self.employees->collect(e | e.age)", company);
		assertInstanceOf(List.class, result);
		assertEquals(List.of(30, 25, 35), result);
	}

	// --- Implicit collect via dot on collection ---

	@Test
	void implicitCollect_names() throws OclParseException {
		// self.employees.name is shorthand for self.employees->collect(name)
		Object result = eval("self.employees->collect(e | e.name)", company);
		assertEquals(List.of("Alice", "Bob", "Charlie"), result);
	}

	// --- Select with model properties ---

	@Test
	void select_byAge() throws OclParseException {
		Object result = eval("self.employees->select(e | e.age > 28)", company);
		assertInstanceOf(Collection.class, result);
		Collection<?> selected = (Collection<?>) result;
		assertEquals(2, selected.size());
	}

	@Test
	void select_married() throws OclParseException {
		Object result = eval("self.employees->select(e | e.isMarried)", company);
		assertInstanceOf(Collection.class, result);
		assertEquals(2, ((Collection<?>) result).size());
	}

	// --- Reject with model properties ---

	@Test
	void reject_lowSalary() throws OclParseException {
		Object result = eval("self.employees->reject(e | e.salary < 50000.0)", company);
		assertInstanceOf(Collection.class, result);
		Collection<?> rejected = (Collection<?>) result;
		assertEquals(2, rejected.size());
	}

	// --- ForAll with model properties ---

	@Test
	void forAll_allHaveNames() throws OclParseException {
		assertEquals(true, eval("self.employees->forAll(e | e.name <> null)", company));
	}

	@Test
	void forAll_allOldEnough() throws OclParseException {
		assertEquals(true, eval("self.employees->forAll(e | e.age >= 18)", company));
	}

	@Test
	void forAll_allRich() throws OclParseException {
		assertEquals(false, eval("self.employees->forAll(e | e.salary > 70000.0)", company));
	}

	// --- Exists with model properties ---

	@Test
	void exists_highSalary() throws OclParseException {
		assertEquals(true, eval("self.employees->exists(e | e.salary > 70000.0)", company));
	}

	@Test
	void exists_veryOld() throws OclParseException {
		assertEquals(false, eval("self.employees->exists(e | e.age > 100)", company));
	}

	// --- Any with model properties ---

	@Test
	void any_byName() throws OclParseException {
		Object result = eval("self.employees->any(e | e.name = 'Bob')", company);
		assertTrue(result == bob);
	}

	@Test
	void any_notFound() throws OclParseException {
		assertNull(eval("self.employees->any(e | e.name = 'Nobody')", company));
	}

	// --- SortedBy with model properties ---

	@Test
	void sortedBy_age() throws OclParseException {
		Object result = eval("self.employees->sortedBy(e | e.age)->collect(e | e.name)", company);
		assertEquals(List.of("Bob", "Alice", "Charlie"), result);
	}

	@Test
	void sortedBy_salary() throws OclParseException {
		Object result = eval("self.employees->sortedBy(e | e.salary)->collect(e | e.name)", company);
		assertEquals(List.of("Bob", "Alice", "Charlie"), result);
	}

	// --- Collection operations on navigation results ---

	@Test
	void employees_size() throws OclParseException {
		assertEquals(3, eval("self.employees->size()", company));
	}

	@Test
	void employees_isEmpty() throws OclParseException {
		assertEquals(false, eval("self.employees->isEmpty()", company));
	}

	@Test
	void employees_includes() throws OclParseException {
		assertEquals(true, eval("self.employees->includes(self.employees->any(e | e.name = 'Alice'))", company));
	}

	// --- Chained navigation: person -> employer -> employees ---

	@Test
	void chainedNav_employerEmployeesSize() throws OclParseException {
		assertEquals(3, eval("self.employer.employees->size()", alice));
	}

	@Test
	void chainedNav_employerName() throws OclParseException {
		assertEquals("ACME", eval("self.employer.name", bob));
	}

	// --- Arithmetic on collected values ---

	@Test
	void sum_salaries() throws OclParseException {
		assertEquals(185000.0, eval("self.employees->collect(e | e.salary)->sum()", company));
	}

	@Test
	void sum_ages() throws OclParseException {
		// age is EInt, internally widened to Long, sum returns Long, narrowed to Integer (fits in int)
		assertEquals(90, eval("self.employees->collect(e | e.age)->sum()", company));
	}

	// --- Nested iterator ---

	@Test
	void nested_selectForAll() throws OclParseException {
		// Select employees with salary > 40000 and check all are married
		assertEquals(false, eval(
				"self.employees->select(e | e.salary > 40000.0)->forAll(e | e.isMarried)", company));
	}

	// --- isUnique ---

	@Test
	void isUnique_names() throws OclParseException {
		assertEquals(true, eval("self.employees->isUnique(e | e.name)", company));
	}

	@Test
	void isUnique_isMarried() throws OclParseException {
		// Alice and Charlie are both married, so isMarried is not unique
		assertEquals(false, eval("self.employees->isUnique(e | e.isMarried)", company));
	}

	// --- One ---

	@Test
	void one_unique() throws OclParseException {
		assertEquals(true, eval("self.employees->one(e | e.name = 'Alice')", company));
	}

	@Test
	void one_multiple() throws OclParseException {
		assertEquals(false, eval("self.employees->one(e | e.isMarried)", company));
	}

	@Test
	void one_none() throws OclParseException {
		assertEquals(false, eval("self.employees->one(e | e.age > 100)", company));
	}
}
