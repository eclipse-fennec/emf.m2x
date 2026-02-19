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
 * Tests for property access after first(), last(), at(), and any()
 * on model collections — verifying dynamic feature resolution.
 */
class OclFirstLastPropertyTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject carol;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 80000.0, true);
		bob = createPerson("Bob", 25, 40000.0, false);
		carol = createPerson("Carol", 45, 120000.0, true);
		company = createCompany("TechCorp", alice, bob, carol);
	}

	// --- first().property ---

	@Test
	void first_name() throws OclParseException {
		assertEquals("Alice", eval("self.employees->first().name", company));
	}

	@Test
	void first_age() throws OclParseException {
		assertEquals(30, eval("self.employees->first().age", company));
	}

	@Test
	void first_salary() throws OclParseException {
		assertEquals(80000.0, eval("self.employees->first().salary", company));
	}

	@Test
	void first_isMarried() throws OclParseException {
		assertEquals(true, eval("self.employees->first().isMarried", company));
	}

	// --- last().property ---

	@Test
	void last_name() throws OclParseException {
		assertEquals("Carol", eval("self.employees->last().name", company));
	}

	@Test
	void last_age() throws OclParseException {
		assertEquals(45, eval("self.employees->last().age", company));
	}

	@Test
	void last_salary() throws OclParseException {
		assertEquals(120000.0, eval("self.employees->last().salary", company));
	}

	// --- at().property ---

	@Test
	void at_name() throws OclParseException {
		assertEquals("Bob", eval("self.employees->at(2).name", company));
	}

	@Test
	void at_age() throws OclParseException {
		assertEquals(25, eval("self.employees->at(2).age", company));
	}

	// --- any().property ---

	@Test
	void any_name() throws OclParseException {
		assertEquals("Carol", eval(
				"self.employees->any(e | e.age > 40).name", company));
	}

	@Test
	void any_salary() throws OclParseException {
		assertEquals(120000.0, eval(
				"self.employees->any(e | e.salary > 100000.0).salary", company));
	}

	// --- sortedBy then first/last().property ---

	@Test
	void sortedBy_first_name() throws OclParseException {
		assertEquals("Bob", eval(
				"self.employees->sortedBy(e | e.age)->first().name", company));
	}

	@Test
	void sortedBy_last_name() throws OclParseException {
		assertEquals("Carol", eval(
				"self.employees->sortedBy(e | e.age)->last().name", company));
	}

	@Test
	void sortedBy_first_age() throws OclParseException {
		assertEquals(25, eval(
				"self.employees->sortedBy(e | e.age)->first().age", company));
	}

	// --- select then first().property ---

	@Test
	void select_first_name() throws OclParseException {
		assertEquals("Alice", eval(
				"self.employees->select(e | e.isMarried)->first().name", company));
	}

	@Test
	void select_last_name() throws OclParseException {
		assertEquals("Carol", eval(
				"self.employees->select(e | e.isMarried)->last().name", company));
	}

	// --- reject then first().property ---

	@Test
	void reject_first_name() throws OclParseException {
		assertEquals("Bob", eval(
				"self.employees->reject(e | e.isMarried)->first().name", company));
	}

	// --- Chained: first().employer.name ---

	@Test
	void first_employer_name() throws OclParseException {
		assertEquals("TechCorp", eval(
				"self.employees->first().employer.name", company));
	}

	// --- Comparison after first/last ---

	@Test
	void first_age_comparison() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->first().age >= 18", company));
	}

	@Test
	void last_salary_comparison() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->last().salary > 100000.0", company));
	}

	// --- first() identity from employee perspective ---

	@Test
	void first_identity() throws OclParseException {
		// From Alice's perspective: she is the first employee of her employer
		assertEquals(true, eval(
				"self.employer.employees->first() = self", alice));
	}
}
