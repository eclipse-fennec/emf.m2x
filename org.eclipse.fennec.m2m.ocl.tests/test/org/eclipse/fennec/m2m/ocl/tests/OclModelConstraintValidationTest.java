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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests simulating OCL constraint validation on a model.
 * These represent invariants, pre/post-conditions, and
 * derived value queries that would appear in real EMF
 * model validation scenarios.
 */
class OclModelConstraintValidationTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject charlie;
	static EObject validCompany;
	static EObject emptyCompany;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 65000.0, true);
		bob = createPerson("Bob", 22, 42000.0, false);
		charlie = createPerson("Charlie", 45, 95000.0, true);
		validCompany = createCompany("ValidCorp", alice, bob, charlie);
		emptyCompany = createCompany("EmptyCorp");
	}

	// --- Person invariants ---

	@Test
	void personInv_nameNotEmpty() throws OclParseException {
		assertEquals(true, eval("self.name.size() > 0", alice));
	}

	@Test
	void personInv_agePositive() throws OclParseException {
		assertEquals(true, eval("self.age > 0", alice));
	}

	@Test
	void personInv_ageReasonable() throws OclParseException {
		assertEquals(true, eval("self.age >= 0 and self.age <= 150", alice));
	}

	@Test
	void personInv_salaryNonNegative() throws OclParseException {
		assertEquals(true, eval("self.salary >= 0.0", alice));
	}

	@Test
	void personInv_hasEmployer() throws OclParseException {
		assertEquals(true, eval("not self.employer.oclIsUndefined()", alice));
	}

	@Test
	void personInv_marriedImpliesAdult() throws OclParseException {
		assertEquals(true, eval("self.isMarried implies self.age >= 18", alice));
	}

	// --- Company invariants ---

	@Test
	void companyInv_nameNotEmpty() throws OclParseException {
		assertEquals(true, eval("self.name.size() > 0", validCompany));
	}

	@Test
	void companyInv_hasEmployees() throws OclParseException {
		assertEquals(true, eval("self.employees->notEmpty()", validCompany));
	}

	@Test
	void companyInv_emptyFails() throws OclParseException {
		assertEquals(false, eval("self.employees->notEmpty()", emptyCompany));
	}

	@Test
	void companyInv_allEmployeesAdult() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e | e.age >= 18)", validCompany));
	}

	@Test
	void companyInv_uniqueNames() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->isUnique(e | e.name)", validCompany));
	}

	@Test
	void companyInv_salaryBudget() throws OclParseException {
		// Total salary must be under 1 million
		assertEquals(true, eval(
				"self.employees->collect(e | e.salary)->sum() < 1000000.0",
				validCompany));
	}

	@Test
	void companyInv_maxEmployees() throws OclParseException {
		assertEquals(true, eval("self.employees->size() <= 100", validCompany));
	}

	// --- Derived values as constraints ---

	@Test
	void derived_avgSalaryAboveMinWage() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->collect(e | e.salary)->sum() / self.employees->size() > 30000.0",
				validCompany));
	}

	@Test
	void derived_youngestAbove18() throws OclParseException {
		Object result = eval(
				"self.employees->collect(e | e.age)->min()", validCompany);
		assertEquals(22, result);
	}

	@Test
	void derived_oldestBelow100() throws OclParseException {
		Object result = eval(
				"self.employees->collect(e | e.age)->max()", validCompany);
		assertEquals(45, result);
	}

	// --- Cross-constraint patterns ---

	@Test
	void crossConstraint_sameEmployerForAll() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e | e.employer = self)", validCompany));
	}

	@Test
	void crossConstraint_noSelfEmployment() throws OclParseException {
		// Company is not an employee of itself (different types, always true)
		assertEquals(true, eval(
				"self.employees->forAll(e | e <> self)", validCompany));
	}

	// --- Conditional constraints ---

	@Test
	void conditional_marriedHigherPay() throws OclParseException {
		// All married employees earn at least 50k (in our test data)
		assertEquals(true, eval(
				"self.employees->forAll(e | e.isMarried implies e.salary >= 50000.0)",
				validCompany));
	}

	@Test
	void conditional_seniorHigherPay() throws OclParseException {
		// Employees over 40 earn at least 60k (in our test data)
		assertEquals(true, eval(
				"self.employees->forAll(e | e.age > 40 implies e.salary >= 60000.0)",
				validCompany));
	}

	// --- Quantified constraints ---

	@Test
	void quantified_atLeastOneMarried() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->exists(e | e.isMarried)", validCompany));
	}

	@Test
	void quantified_exactlyOneOldest() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->one(e | e.age = self.employees->collect(e2 | e2.age)->max())",
				validCompany));
	}

	// --- Navigation constraint patterns ---

	@Test
	void navigation_backReference() throws OclParseException {
		// Every employee's employer is this company
		assertEquals(true, eval(
				"self.employees->forAll(e | e.employer.name = self.name)", validCompany));
	}

	@Test
	void navigation_colleagueCount() throws OclParseException {
		// Alice has 2 colleagues (excluding herself)
		assertEquals(2, eval(
				"self.employer.employees->select(e | e <> self)->size()", alice));
	}
}
