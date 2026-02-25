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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for bidirectional reference navigation (eOpposite) and containment.
 * Uses the company model where Company.employees &lt;-&gt; Person.employer.
 */
class OclBidirectionalNavTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject carol;
	static EObject company;
	static EObject standalone;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 50000.0, true);
		bob = createPerson("Bob", 25, 40000.0, false);
		carol = createPerson("Carol", 35, 60000.0, true);
		company = createCompany("ACME", alice, bob, carol);
		standalone = createPerson("Standalone", 20, 0.0, false);
	}

	// --- Person -> Company (employer) ---

	@Test
	void person_employer_name() throws OclParseException {
		assertEquals("ACME", eval("self.employer.name", alice));
	}

	@Test
	void person_employer_sameForAllEmployees() throws OclParseException {
		assertEquals(eval("self.employer.name", alice), eval("self.employer.name", bob));
	}

	@Test
	void standalone_noEmployer() throws OclParseException {
		assertNull(eval("self.employer", standalone));
	}

	@Test
	void standalone_employer_oclIsUndefined() throws OclParseException {
		assertEquals(true, eval("self.employer.oclIsUndefined()", standalone));
	}

	// --- Company -> Person (employees) -> Company (employer) round-trip ---

	@Test
	void roundTrip_employeesEmployer() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e | e.employer = self)", company));
	}

	@Test
	void roundTrip_employerEmployees() throws OclParseException {
		assertEquals(true, eval(
				"self.employer.employees->includes(self)", alice));
	}

	// --- Navigation through employer ---

	@Test
	void employer_employeeCount() throws OclParseException {
		assertEquals(3, eval("self.employer.employees->size()", alice));
	}

	@Test
	void employer_colleagues() throws OclParseException {
		// Colleagues: all employees of my employer except me
		assertEquals(2, eval(
				"self.employer.employees->excluding(self)->size()", alice));
	}

	@Test
	void employer_colleagueNames() throws OclParseException {
		assertEquals(true, eval(
				"self.employer.employees->excluding(self).name->includes('Bob')", alice));
	}

	// --- Company -> filter -> navigate back ---

	@Test
	void company_seniorEmployees_employer() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->select(e | e.age >= 30)->forAll(e | e.employer.name = 'ACME')",
				company));
	}

	// --- Chained navigation ---

	@Test
	void person_employer_name_size() throws OclParseException {
		assertEquals(4, eval("self.employer.name.size()", alice));
	}

	@Test
	void person_employer_employees_collect_name() throws OclParseException {
		Object result = eval("self.employer.employees->collect(e | e.name)", alice);
		assertTrue(result instanceof java.util.List);
		@SuppressWarnings("unchecked")
		java.util.List<String> names = (java.util.List<String>) result;
		assertEquals(3, names.size());
		assertTrue(names.contains("Alice"));
		assertTrue(names.contains("Bob"));
		assertTrue(names.contains("Carol"));
	}

	// --- Self comparison ---

	@Test
	void employee_isSelfInCompany() throws OclParseException {
		assertEquals(true, eval(
				"self.employer.employees->exists(e | e = self)", alice));
	}

	// --- Containment ---

	@Test
	void employeeCount_fromCompany() throws OclParseException {
		assertEquals(3, eval("self.employees->size()", company));
	}

	@Test
	void employeeNames_fromCompany() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->collect(e | e.name)->includes('Carol')", company));
	}

	// --- Null-safe navigation on opposite ---

	@Test
	void standalone_safeNav_employer() throws OclParseException {
		// safe navigation on null employer
		assertNull(eval("self.employer?.name", standalone));
	}
}
