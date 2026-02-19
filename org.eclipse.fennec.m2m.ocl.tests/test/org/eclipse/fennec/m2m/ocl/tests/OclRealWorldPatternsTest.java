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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Real-world OCL patterns commonly found in model validation,
 * business rules, and model queries.
 */
class OclRealWorldPatternsTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject carol;
	static EObject dave;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 80000.0, true);
		bob = createPerson("Bob", 25, 40000.0, false);
		carol = createPerson("Carol", 45, 120000.0, true);
		dave = createPerson("Dave", 22, 35000.0, false);
		company = createCompany("TechCorp", alice, bob, carol, dave);
	}

	// --- Business rule: salary constraints ---

	@Test
	void allSalariesPositive() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e | e.salary > 0.0)", company));
	}

	@Test
	void noEmployeeUnderMinWage() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e | e.salary >= 30000.0)", company));
	}

	@Test
	void averageSalary() throws OclParseException {
		// (80000 + 40000 + 120000 + 35000) / 4 = 68750.0
		Object result = eval(
				"self.employees->collect(e | e.salary)->sum() / self.employees->size()",
				company);
		assertEquals(68750.0, result);
	}

	// --- Business rule: naming conventions ---

	@Test
	void allNamesNonEmpty() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e | e.name.size() > 0)", company));
	}

	@Test
	void uniqueNames() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->isUnique(e | e.name)", company));
	}

	// --- Query patterns ---

	@Test
	void findHighEarners() throws OclParseException {
		assertEquals(2, eval(
				"self.employees->select(e | e.salary > 50000.0)->size()", company));
	}

	@Test
	void findHighEarnerNames() throws OclParseException {
		Object result = eval(
				"self.employees->select(e | e.salary > 50000.0)->collect(e | e.name)->sortedBy(n | n)",
				company);
		assertEquals(List.of("Alice", "Carol"), result);
	}

	@Test
	void findYoungestEmployee() throws OclParseException {
		assertEquals("Dave", eval(
				"self.employees->sortedBy(e | e.age)->first().name", company));
	}

	@Test
	void findOldestEmployee() throws OclParseException {
		assertEquals("Carol", eval(
				"self.employees->sortedBy(e | e.age)->last().name", company));
	}

	@Test
	void findMaxSalary() throws OclParseException {
		assertEquals(120000.0, eval(
				"self.employees->collect(e | e.salary)->max()", company));
	}

	@Test
	void findMinSalary() throws OclParseException {
		assertEquals(35000.0, eval(
				"self.employees->collect(e | e.salary)->min()", company));
	}

	// --- Validation patterns ---

	@Test
	void companyHasEmployees() throws OclParseException {
		assertEquals(true, eval("self.employees->notEmpty()", company));
	}

	@Test
	void companyNameNotEmpty() throws OclParseException {
		assertEquals(true, eval("self.name.size() > 0", company));
	}

	@Test
	void atLeastOneMarried() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->exists(e | e.isMarried)", company));
	}

	@Test
	void notAllMarried() throws OclParseException {
		assertEquals(false, eval(
				"self.employees->forAll(e | e.isMarried)", company));
	}

	// --- Aggregate patterns ---

	@Test
	void countMarried() throws OclParseException {
		assertEquals(2, eval(
				"self.employees->select(e | e.isMarried)->size()", company));
	}

	@Test
	void countUnmarried() throws OclParseException {
		assertEquals(2, eval(
				"self.employees->reject(e | e.isMarried)->size()", company));
	}

	@Test
	void totalSalary() throws OclParseException {
		assertEquals(275000.0, eval(
				"self.employees->collect(e | e.salary)->sum()", company));
	}

	// --- Complex query: top earners who are young ---

	@Test
	void youngHighEarners() throws OclParseException {
		// Employees under 35 earning more than 50000
		assertEquals(1, eval(
				"self.employees->select(e | e.age < 35 and e.salary > 50000.0)->size()",
				company));
	}

	@Test
	void youngHighEarner_name() throws OclParseException {
		assertEquals("Alice", eval(
				"self.employees->select(e | e.age < 35 and e.salary > 50000.0)->first().name",
				company));
	}

	// --- Pattern: conditional string building ---

	@Test
	void employeeStatus_ifThenElse() throws OclParseException {
		assertEquals("senior", eval(
				"if self.age >= 30 then 'senior' else 'junior' endif", alice));
	}

	@Test
	void employeeStatus_junior() throws OclParseException {
		assertEquals("junior", eval(
				"if self.age >= 30 then 'senior' else 'junior' endif", dave));
	}

	// --- Pattern: derived values ---

	@Test
	void monthlyGross() throws OclParseException {
		// Annual salary / 12
		Object result = eval("self.salary / 12.0", alice);
		assertTrue(Math.abs((Double) result - 6666.67) < 1.0);
	}

	@Test
	void salaryRank() throws OclParseException {
		// How many earn more than Alice?
		assertEquals(1, eval(
				"self.employer.employees->select(e | e.salary > self.salary)->size()", alice));
	}

	// --- Pattern: grouped analysis ---

	@Test
	void marriedEmployeeAvgAge() throws OclParseException {
		// Married: Alice(30), Carol(45) → sum=75, size=2, 75/2 = 37.5
		assertEquals(37.5, eval(
				"let married : Sequence(Integer) = self.employees->select(e | e.isMarried)->collect(e | e.age) in " +
						"married->sum() / married->size()", company));
	}
}
