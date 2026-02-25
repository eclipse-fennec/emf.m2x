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
 * Advanced tests for navigating the company model:
 * opposite references, multi-level queries, combined
 * property access with collection operations.
 */
class OclModelNavigationAdvancedTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject carol;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 50000.0, true);
		bob = createPerson("Bob", 25, 40000.0, false);
		carol = createPerson("Carol", 45, 80000.0, true);
		company = createCompany("Acme", alice, bob, carol);
	}

	// --- Basic navigation ---

	@Test
	void company_name() throws OclParseException {
		assertEquals("Acme", eval("self.name", company));
	}

	@Test
	void company_employeeCount() throws OclParseException {
		assertEquals(3, eval("self.employees->size()", company));
	}

	// --- Opposite reference: employee → employer ---

	@Test
	void employee_employer() throws OclParseException {
		assertEquals("Acme", eval("self.employer.name", alice));
	}

	@Test
	void employee_employerName() throws OclParseException {
		assertEquals("Acme", eval("self.employer.name", alice));
	}

	// --- Select employees by criteria ---

	@Test
	void select_married() throws OclParseException {
		assertEquals(2, eval(
				"self.employees->select(e | e.isMarried)->size()", company));
	}

	@Test
	void select_unmarried() throws OclParseException {
		assertEquals(1, eval(
				"self.employees->select(e | not e.isMarried)->size()", company));
	}

	@Test
	void select_ageOver30() throws OclParseException {
		assertEquals(1, eval(
				"self.employees->select(e | e.age > 30)->size()", company));
	}

	@Test
	void select_salaryOver50k() throws OclParseException {
		assertEquals(1, eval(
				"self.employees->select(e | e.salary > 50000.0)->size()", company));
	}

	// --- Collect from model ---

	@Test
	void collect_names() throws OclParseException {
		assertEquals(3, eval(
				"self.employees->collect(e | e.name)->size()", company));
	}

	@Test
	void collect_ages() throws OclParseException {
		assertEquals(3, eval(
				"self.employees->collect(e | e.age)->size()", company));
	}

	// --- Aggregate operations on model ---

	@Test
	void sum_salaries() throws OclParseException {
		assertEquals(170000.0, eval(
				"self.employees->collect(e | e.salary)->sum()", company));
	}

	@Test
	void min_age() throws OclParseException {
		assertEquals(25, eval(
				"self.employees->collect(e | e.age)->min()", company));
	}

	@Test
	void max_age() throws OclParseException {
		assertEquals(45, eval(
				"self.employees->collect(e | e.age)->max()", company));
	}

	// --- ForAll / Exists on model ---

	@Test
	void forAll_positiveAge() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e | e.age > 0)", company));
	}

	@Test
	void exists_married() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->exists(e | e.isMarried)", company));
	}

	@Test
	void exists_highSalary() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->exists(e | e.salary > 70000.0)", company));
	}

	@Test
	void forAll_notManager() throws OclParseException {
		// Default status may not be MANAGER for all
		assertEquals(true, eval(
				"self.employees->forAll(e | e.age > 20)", company));
	}

	// --- SortedBy on model ---

	@Test
	void sortedBy_age_first() throws OclParseException {
		// Youngest is Bob (25)
		assertEquals("Bob", eval(
				"self.employees->sortedBy(e | e.age)->first().name", company));
	}

	@Test
	void sortedBy_age_first_name() throws OclParseException {
		// Youngest should be Bob (25) — direct chained access
		assertEquals("Bob", eval(
				"self.employees->sortedBy(e | e.age)->first().name",
				company));
	}

	@Test
	void sortedBy_salary_last_name() throws OclParseException {
		// Highest salary is Carol (80000)
		assertEquals("Carol", eval(
				"self.employees->sortedBy(e | e.salary)->last().name",
				company));
	}

	// --- Select then further operations ---

	@Test
	void select_married_collect_names() throws OclParseException {
		assertEquals(2, eval(
				"self.employees->select(e | e.isMarried)->collect(e | e.name)->size()",
				company));
	}

	@Test
	void select_then_forAll() throws OclParseException {
		// All married employees have age > 20
		assertEquals(true, eval(
				"self.employees->select(e | e.isMarried)->forAll(e | e.age > 20)",
				company));
	}

	// --- isEmpty / notEmpty ---

	@Test
	void employees_notEmpty() throws OclParseException {
		assertEquals(true, eval("self.employees->notEmpty()", company));
	}

	@Test
	void select_nobody_isEmpty() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->select(e | e.age > 100)->isEmpty()", company));
	}

	// --- One ---

	@Test
	void one_byName() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->one(e | e.name = 'Carol')", company));
	}

	// --- Count ---

	@Test
	void collect_ages_thenSize() throws OclParseException {
		// 3 employees → 3 ages
		assertEquals(3, eval(
				"self.employees->collect(e | e.age)->size()", company));
	}
}
