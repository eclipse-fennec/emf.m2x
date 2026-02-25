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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests using Company as self context, verifying expressions
 * that navigate from Company to its employees and their properties.
 */
class OclCompanyContextTest extends AbstractOclTest {

	static EObject smallCompany;
	static EObject largeCompany;
	static EObject emptyCompany;

	@BeforeAll
	static void setUp() {
		// Each company needs its OWN Person instances (EMF containment)
		EObject sAlice = createPerson("Alice", 30, 80000.0, true);
		EObject sBob = createPerson("Bob", 25, 40000.0, false);
		smallCompany = createCompany("SmallCo", sAlice, sBob);

		EObject lAlice = createPerson("Alice", 30, 80000.0, true);
		EObject lBob = createPerson("Bob", 25, 40000.0, false);
		EObject lCarol = createPerson("Carol", 45, 120000.0, true);
		EObject lDave = createPerson("Dave", 22, 35000.0, false);
		EObject lEve = createPerson("Eve", 38, 95000.0, true);
		largeCompany = createCompany("LargeCo", lAlice, lBob, lCarol, lDave, lEve);

		emptyCompany = createCompany("EmptyCo");
	}

	// --- Self properties ---

	@Test
	void companyName() throws OclParseException {
		assertEquals("SmallCo", eval("self.name", smallCompany));
	}

	@Test
	void companyNameSize() throws OclParseException {
		assertEquals(7, eval("self.name.size()", smallCompany));
	}

	// --- Employee count ---

	@Test
	void employeeCount_small() throws OclParseException {
		assertEquals(2, eval("self.employees->size()", smallCompany));
	}

	@Test
	void employeeCount_large() throws OclParseException {
		assertEquals(5, eval("self.employees->size()", largeCompany));
	}

	@Test
	void employeeCount_empty() throws OclParseException {
		assertEquals(0, eval("self.employees->size()", emptyCompany));
	}

	// --- Empty/notEmpty ---

	@Test
	void employees_notEmpty() throws OclParseException {
		assertEquals(true, eval("self.employees->notEmpty()", smallCompany));
	}

	@Test
	void employees_isEmpty_emptyCompany() throws OclParseException {
		assertEquals(true, eval("self.employees->isEmpty()", emptyCompany));
	}

	// --- Employee names ---

	@Test
	void collectNames_small() throws OclParseException {
		Object result = eval("self.employees->collect(e | e.name)->sortedBy(n | n)", smallCompany);
		assertEquals(List.of("Alice", "Bob"), result);
	}

	// --- Salary operations ---

	@Test
	void totalSalary_small() throws OclParseException {
		assertEquals(120000.0, eval(
				"self.employees->collect(e | e.salary)->sum()", smallCompany));
	}

	@Test
	void maxSalary() throws OclParseException {
		assertEquals(120000.0, eval(
				"self.employees->collect(e | e.salary)->max()", largeCompany));
	}

	@Test
	void minSalary() throws OclParseException {
		assertEquals(35000.0, eval(
				"self.employees->collect(e | e.salary)->min()", largeCompany));
	}

	// --- Age operations ---

	@Test
	void maxAge() throws OclParseException {
		assertEquals(45, eval(
				"self.employees->collect(e | e.age)->max()", largeCompany));
	}

	@Test
	void minAge() throws OclParseException {
		assertEquals(22, eval(
				"self.employees->collect(e | e.age)->min()", largeCompany));
	}

	// --- Filtering ---

	@Test
	void married_count() throws OclParseException {
		assertEquals(3, eval(
				"self.employees->select(e | e.isMarried)->size()", largeCompany));
	}

	@Test
	void unmarried_count() throws OclParseException {
		assertEquals(2, eval(
				"self.employees->reject(e | e.isMarried)->size()", largeCompany));
	}

	@Test
	void highEarners() throws OclParseException {
		assertEquals(3, eval(
				"self.employees->select(e | e.salary >= 80000.0)->size()", largeCompany));
	}

	// --- ForAll / Exists ---

	@Test
	void forAll_positiveAge() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e | e.age > 0)", largeCompany));
	}

	@Test
	void exists_manager() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->exists(e | e.age >= 40)", largeCompany));
	}

	@Test
	void exists_teenager_false() throws OclParseException {
		assertEquals(false, eval(
				"self.employees->exists(e | e.age < 18)", largeCompany));
	}

	// --- Unique names ---

	@Test
	void isUnique_names() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->isUnique(e | e.name)", smallCompany));
	}

	// --- Sorted ---

	@Test
	void sortedByAge_ages() throws OclParseException {
		Object result = eval(
				"self.employees->sortedBy(e | e.age)->collect(e | e.age)", largeCompany);
		assertEquals(List.of(22, 25, 30, 38, 45), result);
	}

	// --- Complex queries ---

	@Test
	void averageSalary() throws OclParseException {
		Object result = eval(
				"self.employees->collect(e | e.salary)->sum() / self.employees->size()",
				smallCompany);
		assertEquals(60000.0, result);
	}

	@Test
	void employeesAboveAverage() throws OclParseException {
		// SmallCo average = 60000, Alice has 80000 → 1 above average
		assertEquals(1, eval(
				"let avg : Real = self.employees->collect(e | e.salary)->sum() / self.employees->size() in " +
						"self.employees->select(e | e.salary > avg)->size()",
				smallCompany));
	}

	// --- Empty company edge cases ---

	@Test
	void emptyCompany_forAll_vacuouslyTrue() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e | e.age > 100)", emptyCompany));
	}

	@Test
	void emptyCompany_exists_false() throws OclParseException {
		assertEquals(false, eval(
				"self.employees->exists(e | e.age > 0)", emptyCompany));
	}

	// --- Company name in expression ---

	@Test
	void companyNameConcat() throws OclParseException {
		assertEquals("Company: SmallCo", eval(
				"'Company: '.concat(self.name)", smallCompany));
	}

	@Test
	void companyNameUpperCase() throws OclParseException {
		assertEquals("SMALLCO", eval("self.name.toUpperCase()", smallCompany));
	}
}
