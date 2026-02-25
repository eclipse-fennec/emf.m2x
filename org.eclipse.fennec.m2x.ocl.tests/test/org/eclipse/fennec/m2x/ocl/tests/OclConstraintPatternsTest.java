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

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for real-world OCL constraint patterns commonly found in
 * EMF models and UML profiles. These patterns exercise multiple
 * features together as they would appear in actual model constraints.
 */
class OclConstraintPatternsTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject charlie;
	static EObject dave;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 60000.0, true);
		bob = createPerson("Bob", 25, 45000.0, false);
		charlie = createPerson("Charlie", 55, 120000.0, true);
		dave = createPerson("Dave", 18, 30000.0, false);
		company = createCompany("ACME", alice, bob, charlie, dave);
	}

	// --- Invariant patterns ---

	@Test
	void invariant_nameNotEmpty() throws OclParseException {
		assertEquals(true, eval(
				"self.name <> '' and self.name.size() > 0", alice));
	}

	@Test
	void invariant_ageRange() throws OclParseException {
		assertEquals(true, eval(
				"self.age >= 18 and self.age <= 100", alice));
	}

	@Test
	void invariant_salaryPositive() throws OclParseException {
		assertEquals(true, eval(
				"self.salary > 0.0 implies self.age >= 18", alice));
	}

	@Test
	void invariant_allEmployeesOldEnough() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e | e.age >= 18)", company));
	}

	@Test
	void invariant_uniqueEmployeeNames() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->isUnique(e | e.name)", company));
	}

	@Test
	void invariant_atLeastOneEmployee() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->notEmpty()", company));
	}

	// --- Implies patterns ---

	@Test
	void implies_marriedAndSenior() throws OclParseException {
		// If married, salary must be > 40000
		assertEquals(true, eval(
				"self.isMarried implies self.salary > 40000.0", alice));
	}

	@Test
	void implies_notMarried() throws OclParseException {
		// For non-married: implies is vacuously true
		assertEquals(true, eval(
				"self.isMarried implies self.salary > 100000.0", bob));
	}

	// --- Derive patterns ---

	@Test
	void derive_seniorCount() throws OclParseException {
		// Count employees over 40
		assertEquals(1, eval(
				"self.employees->select(e | e.age > 40)->size()", company));
	}

	@Test
	void derive_averageSalary() throws OclParseException {
		// Sum of salaries / count
		Object result = eval(
				"self.employees->collect(e | e.salary)->sum() / self.employees->size()", company);
		assertInstanceOf(Double.class, result);
	}

	@Test
	void derive_maxSalary() throws OclParseException {
		assertEquals(120000.0, eval(
				"self.employees->collect(e | e.salary)->max()", company));
	}

	@Test
	void derive_minAge() throws OclParseException {
		// eGet returns Integer for EInt, which needs special handling
		Object result = eval(
				"self.employees->collect(e | e.age)->min()", company);
		assertEquals(18, result);
	}

	// --- Complex query patterns ---

	@Test
	void query_highEarners() throws OclParseException {
		// Names of employees earning more than 50000
		Object result = eval(
				"self.employees->select(e | e.salary > 50000.0)->collect(e | e.name)",
				company);
		assertInstanceOf(Collection.class, result);
		Collection<?> names = (Collection<?>) result;
		assertEquals(2, names.size());
	}

	@Test
	void query_youngAndUnmarried() throws OclParseException {
		// Employees under 30 who are not married
		Object result = eval(
				"self.employees->select(e | e.age < 30 and not e.isMarried)",
				company);
		assertInstanceOf(Collection.class, result);
		assertEquals(2, ((Collection<?>) result).size());
	}

	@Test
	void query_existsHighEarner() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->exists(e | e.salary > 100000.0)", company));
	}

	@Test
	void query_noChildLabor() throws OclParseException {
		assertEquals(true, eval(
				"not self.employees->exists(e | e.age < 16)", company));
	}

	// --- Let + complex expressions ---

	@Test
	void let_totalSalary() throws OclParseException {
		Object result = eval(
				"let total: Real = self.employees->collect(e | e.salary)->sum() in total > 0.0",
				company);
		assertEquals(true, result);
	}

	@Test
	void let_highestPaidName() throws OclParseException {
		// Find the name of highest-paid employee via select + collect
		Object result = eval(
				"let maxSal: Real = self.employees->collect(e | e.salary)->max() " +
				"in self.employees->select(e | e.salary = maxSal)->collect(e | e.name)->first()",
				company);
		assertEquals("Charlie", result);
	}

	// --- If-then-else in constraints ---

	@Test
	void if_salaryCategory() throws OclParseException {
		assertEquals("high", eval(
				"if self.salary > 50000.0 then 'high' else 'low' endif", alice));
	}

	@Test
	void if_nestedCategory() throws OclParseException {
		assertEquals("mid", eval(
				"if self.salary > 100000.0 then 'high' " +
				"else if self.salary > 30000.0 then 'mid' " +
				"else 'low' endif endif", alice));
	}

	// --- Combining model navigation with arithmetic ---

	@Test
	void combined_salaryDifference() throws OclParseException {
		Object result = eval(
				"let maxSal: Real = self.employees->collect(e | e.salary)->max(), " +
				"    minSal: Real = self.employees->collect(e | e.salary)->min() " +
				"in maxSal - minSal",
				company);
		assertEquals(90000.0, result);
	}

	@Test
	void combined_salaryAboveAverage() throws OclParseException {
		Object result = eval(
				"let avg: Real = self.employees->collect(e | e.salary)->sum() / self.employees->size() " +
				"in self.employees->select(e | e.salary > avg)->size()",
				company);
		assertInstanceOf(Number.class, result);
	}

	// --- Collection equality ---

	@Test
	void collectionEquality_sets() throws OclParseException {
		assertEquals(true, eval("Set{1, 2, 3} = Set{3, 2, 1}", alice));
	}

	@Test
	void collectionEquality_sequences() throws OclParseException {
		assertEquals(false, eval("Sequence{1, 2, 3} = Sequence{3, 2, 1}", alice));
	}

	@Test
	void collectionEquality_sameSequence() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3} = Sequence{1, 2, 3}", alice));
	}

	// --- includesAll / excludesAll ---

	@Test
	void includesAll() throws OclParseException {
		assertEquals(true, eval("Set{1, 2, 3, 4}->includesAll(Set{2, 3})", alice));
	}

	@Test
	void includesAll_false() throws OclParseException {
		assertEquals(false, eval("Set{1, 2, 3}->includesAll(Set{2, 5})", alice));
	}

	@Test
	void excludesAll() throws OclParseException {
		assertEquals(true, eval("Set{1, 2, 3}->excludesAll(Set{4, 5})", alice));
	}

	@Test
	void excludesAll_false() throws OclParseException {
		assertEquals(false, eval("Set{1, 2, 3}->excludesAll(Set{3, 4})", alice));
	}

	// --- symmetricDifference ---

	@Test
	void symmetricDifference() throws OclParseException {
		// {1,2,3} symmetricDifference {2,3,4} = {1,4}
		assertEquals(2, eval(
				"Set{1, 2, 3}->symmetricDifference(Set{2, 3, 4})->size()", alice));
	}
}
