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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for complex model query patterns that combine multiple
 * OCL features. These represent realistic queries one might
 * write against an EMF model.
 */
class OclModelQueryPatternsTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject charlie;
	static EObject dave;
	static EObject eve;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 42, 95000.0, true);
		bob = createPerson("Bob", 28, 52000.0, false);
		charlie = createPerson("Charlie", 35, 78000.0, true);
		dave = createPerson("Dave", 55, 120000.0, true);
		eve = createPerson("Eve", 23, 45000.0, false);
		company = createCompany("TechCorp", alice, bob, charlie, dave, eve);
	}

	// --- Aggregation queries ---

	@Test
	void query_totalPayroll() throws OclParseException {
		assertEquals(390000.0, eval(
				"self.employees->collect(e | e.salary)->sum()", company));
	}

	@Test
	void query_employeeCount() throws OclParseException {
		assertEquals(5, eval("self.employees->size()", company));
	}

	@Test
	void query_marriedCount() throws OclParseException {
		assertEquals(3, eval(
				"self.employees->select(e | e.isMarried)->size()", company));
	}

	@Test
	void query_unmarriedCount() throws OclParseException {
		assertEquals(2, eval(
				"self.employees->reject(e | e.isMarried)->size()", company));
	}

	// --- Filtering queries ---

	@Test
	void query_seniorEmployees() throws OclParseException {
		// Employees over 40
		Object result = eval(
				"self.employees->select(e | e.age > 40)->collect(e | e.name)",
				company);
		assertInstanceOf(Collection.class, result);
		Collection<?> names = (Collection<?>) result;
		assertEquals(2, names.size());
		assertTrue(names.contains("Alice"));
		assertTrue(names.contains("Dave"));
	}

	@Test
	void query_juniorEmployees() throws OclParseException {
		// Employees under 30
		assertEquals(2, eval(
				"self.employees->select(e | e.age < 30)->size()", company));
	}

	@Test
	void query_highEarners() throws OclParseException {
		// Employees earning more than average
		Object result = eval(
				"let avg: Real = self.employees->collect(e | e.salary)->sum() / self.employees->size() " +
				"in self.employees->select(e | e.salary > avg)->collect(e | e.name)",
				company);
		assertInstanceOf(Collection.class, result);
		Collection<?> names = (Collection<?>) result;
		assertTrue(names.contains("Alice"));
		assertTrue(names.contains("Dave"));
	}

	// --- Sorting queries ---

	@Test
	void query_sortByAge() throws OclParseException {
		Object result = eval(
				"self.employees->sortedBy(e | e.age)->collect(e | e.name)",
				company);
		assertInstanceOf(List.class, result);
		List<?> names = (List<?>) result;
		assertEquals("Eve", names.get(0));      // 23
		assertEquals("Bob", names.get(1));      // 28
		assertEquals("Charlie", names.get(2));  // 35
		assertEquals("Alice", names.get(3));    // 42
		assertEquals("Dave", names.get(4));     // 55
	}

	@Test
	void query_sortBySalary_first() throws OclParseException {
		Object result = eval(
				"let lowest: Person = self.employees->sortedBy(e | e.salary)->first() " +
				"in lowest.name",
				company);
		assertEquals("Eve", result);
	}

	@Test
	void query_sortBySalary_last() throws OclParseException {
		Object result = eval(
				"let highest: Person = self.employees->sortedBy(e | e.salary)->last() " +
				"in highest.name",
				company);
		assertEquals("Dave", result);
	}

	// --- Existence checks ---

	@Test
	void query_anyoneOver50() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->exists(e | e.age > 50)", company));
	}

	@Test
	void query_anyoneUnder20() throws OclParseException {
		assertEquals(false, eval(
				"self.employees->exists(e | e.age < 20)", company));
	}

	@Test
	void query_allAdults() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e | e.age >= 18)", company));
	}

	@Test
	void query_exactlyOneOver100k() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->one(e | e.salary > 100000.0)", company));
	}

	// --- Derived value queries ---

	@Test
	void query_salaryRange() throws OclParseException {
		Object result = eval(
				"let maxSal: Real = self.employees->collect(e | e.salary)->max(), " +
				"    minSal: Real = self.employees->collect(e | e.salary)->min() " +
				"in maxSal - minSal",
				company);
		assertEquals(75000.0, result);
	}

	@Test
	void query_nameList_sorted() throws OclParseException {
		Object result = eval(
				"self.employees->collect(e | e.name)->sortedBy(n | n)",
				company);
		assertInstanceOf(List.class, result);
		List<?> sorted = (List<?>) result;
		assertEquals("Alice", sorted.get(0));
		assertEquals("Bob", sorted.get(1));
		assertEquals("Charlie", sorted.get(2));
		assertEquals("Dave", sorted.get(3));
		assertEquals("Eve", sorted.get(4));
	}

	// --- Complex constraint patterns ---

	@Test
	void constraint_marriedEarnMoreThan40k() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e | e.isMarried implies e.salary > 40000.0)",
				company));
	}

	@Test
	void constraint_uniqueNames() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->isUnique(e | e.name)", company));
	}

	@Test
	void constraint_companyNameNotEmpty() throws OclParseException {
		assertEquals(true, eval(
				"self.name.size() > 0 and self.employees->notEmpty()", company));
	}

	// --- Navigate back to employer ---

	@Test
	void navigate_employerName() throws OclParseException {
		assertEquals("TechCorp", eval("self.employer.name", alice));
	}

	@Test
	void navigate_colleagueCount() throws OclParseException {
		// All employees of my employer (including myself)
		assertEquals(5, eval("self.employer.employees->size()", alice));
	}

	@Test
	void navigate_otherColleagues() throws OclParseException {
		// Colleagues excluding self
		assertEquals(4, eval(
				"self.employer.employees->select(e | e <> self)->size()", alice));
	}

	// --- Iterate on model ---

	@Test
	void iterate_totalSalary() throws OclParseException {
		assertEquals(390000.0, eval(
				"self.employees->iterate(e; acc: Real = 0.0 | acc + e.salary)",
				company));
	}

	@Test
	void iterate_oldestAge() throws OclParseException {
		Object result = eval(
				"self.employees->iterate(e; acc: Integer = 0 | if e.age > acc then e.age else acc endif)",
				company);
		assertEquals(55, result);
	}
}
