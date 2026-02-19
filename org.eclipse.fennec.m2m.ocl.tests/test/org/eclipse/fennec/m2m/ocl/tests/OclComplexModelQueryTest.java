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
 * Complex model queries combining multiple OCL features:
 * navigation, iterators, let, if, string ops, arithmetic,
 * and collection operations together.
 */
class OclComplexModelQueryTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject carol;
	static EObject dave;
	static EObject eve;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 80000.0, true);
		bob = createPerson("Bob", 25, 40000.0, false);
		carol = createPerson("Carol", 45, 120000.0, true);
		dave = createPerson("Dave", 22, 35000.0, false);
		eve = createPerson("Eve", 38, 95000.0, true);
		company = createCompany("TechCorp", alice, bob, carol, dave, eve);
	}

	// --- Multi-step queries ---

	@Test
	void seniorHighEarners() throws OclParseException {
		// Employees over 35 earning over 90000
		assertEquals(2, eval(
				"self.employees->select(e | e.age > 35 and e.salary > 90000.0)->size()",
				company));
	}

	@Test
	void youngestHighEarner() throws OclParseException {
		// Among employees earning > 75000, find the youngest — should be Alice(30)
		assertEquals("Alice", eval(
				"self.employees->select(e | e.salary > 75000.0)->sortedBy(e | e.age)->first().name",
				company));
	}

	@Test
	void salarySpread() throws OclParseException {
		// max salary - min salary
		assertEquals(85000.0, eval(
				"self.employees->collect(e | e.salary)->max() - self.employees->collect(e | e.salary)->min()",
				company));
	}

	// --- Categorization with if-then-else ---

	@Test
	void categorizeEmployees() throws OclParseException {
		Object result = eval(
				"self.employees->collect(e | " +
						"if e.salary > 100000.0 then 'top' " +
						"else if e.salary > 50000.0 then 'mid' " +
						"else 'entry' endif endif)",
				company);
		assertEquals(List.of("mid", "entry", "top", "entry", "mid"), result);
	}

	// --- Aggregate with let ---

	@Test
	void salaryBudgetPercentage() throws OclParseException {
		// Alice's salary as percentage of total
		assertEquals(true, eval(
				"let total : Real = self.employer.employees->collect(e | e.salary)->sum() in " +
						"self.salary / total > 0.2", alice));
	}

	// --- String building from model data ---

	@Test
	void employeeSummary() throws OclParseException {
		assertEquals("Alice (30)", eval(
				"self.name.concat(' (').concat(self.age.toString()).concat(')')", alice));
	}

	// --- Complex boolean logic ---

	@Test
	void eligibleForPromotion() throws OclParseException {
		// Age >= 25 AND salary < 100000 AND married
		assertEquals(2, eval(
				"self.employees->select(e | " +
						"e.age >= 25 and e.salary < 100000.0 and e.isMarried)->size()",
				company));
	}

	@Test
	void notEligible() throws OclParseException {
		// Not (age >= 25 AND married) = young OR unmarried
		assertEquals(2, eval(
				"self.employees->reject(e | e.age >= 25 and e.isMarried)->size()",
				company));
	}

	// --- Nested collection operations ---

	@Test
	void namesSortedBySalaryDesc() throws OclParseException {
		// Sort employees by salary desc and get names — use negation for desc
		Object result = eval(
				"self.employees->sortedBy(e | 0 - e.salary)->collect(e | e.name)",
				company);
		assertInstanceOf(List.class, result);
		@SuppressWarnings("unchecked")
		List<String> names = (List<String>) result;
		assertEquals("Carol", names.get(0)); // 120000
		assertEquals("Dave", names.get(names.size() - 1)); // 35000
	}

	// --- iterate for complex aggregation ---

	@Test
	void totalSalaryOfMarried() throws OclParseException {
		// Sum salaries of married employees using iterate
		assertEquals(295000.0, eval(
				"self.employees->iterate(e; acc : Real = 0.0 | " +
						"if e.isMarried then acc + e.salary else acc endif)",
				company));
	}

	@Test
	void countByCondition() throws OclParseException {
		// Count employees with name length > 3
		assertEquals(3, eval(
				"self.employees->iterate(e; acc : Integer = 0 | " +
						"if e.name.size() > 3 then acc + 1 else acc endif)",
				company));
	}

	// --- Combined let + select + collect ---

	@Test
	void aboveMedianAge() throws OclParseException {
		// Use let to compute a threshold, then filter
		assertEquals(2, eval(
				"let ages : Sequence(Integer) = self.employees->collect(e | e.age)->sortedBy(a | a) in " +
						"let medianAge : Integer = ages->at(3) in " +
						"self.employees->select(e | e.age > medianAge)->size()",
				company));
	}

	// --- forAll with complex body ---

	@Test
	void allNamesStartWithLetter() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e | e.name.matches('[A-Z].*'))", company));
	}

	// --- Exists with navigation ---

	@Test
	void existsMarriedHighEarner() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->exists(e | e.isMarried and e.salary > 100000.0)", company));
	}

	@Test
	void existsYoungMarried() throws OclParseException {
		assertEquals(false, eval(
				"self.employees->exists(e | e.age < 25 and e.isMarried)", company));
	}

	// --- One complex expression ---

	@Test
	void fullCompanySummary() throws OclParseException {
		// Company name + employee count
		assertEquals("TechCorp has 5 employees", eval(
				"self.name.concat(' has ').concat(self.employees->size().toString()).concat(' employees')",
				company));
	}

	// --- isUnique with expression ---

	@Test
	void uniqueAges() throws OclParseException {
		assertEquals(true, eval("self.employees->isUnique(e | e.age)", company));
	}

	@Test
	void uniqueMaritalStatus() throws OclParseException {
		// isMarried is not unique (multiple true and false)
		assertEquals(false, eval("self.employees->isUnique(e | e.isMarried)", company));
	}

	// --- Collection of tuples ---

	@Test
	void employeeTuples() throws OclParseException {
		Object result = eval(
				"self.employees->collect(e | " +
						"Tuple{n : String = e.name, a : Integer = e.age})",
				company);
		assertInstanceOf(List.class, result);
		assertEquals(5, ((List<?>) result).size());
	}
}
