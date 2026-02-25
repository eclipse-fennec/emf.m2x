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

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL enum literal access and comparison.
 * Uses the Status enum from company.ecore (JUNIOR, SENIOR, MANAGER).
 */
class OclEnumLiteralTest extends AbstractOclTest {

	static EObject junior;
	static EObject senior;
	static EObject manager;
	static EObject company;

	@BeforeAll
	static void setUp() {
		EEnum statusEnum = (EEnum) companyPackage.getEClassifier("Status");

		junior = createPerson("Alice", 25, 30000.0, false);
		junior.eSet(personClass.getEStructuralFeature("status"),
				statusEnum.getEEnumLiteral("JUNIOR").getInstance());

		senior = createPerson("Bob", 35, 60000.0, true);
		senior.eSet(personClass.getEStructuralFeature("status"),
				statusEnum.getEEnumLiteral("SENIOR").getInstance());

		manager = createPerson("Carol", 45, 90000.0, true);
		manager.eSet(personClass.getEStructuralFeature("status"),
				statusEnum.getEEnumLiteral("MANAGER").getInstance());

		company = createCompany("Acme", junior, senior, manager);
	}

	// --- Direct property access ---

	@Test
	void status_junior() throws OclParseException {
		assertEquals(true, eval("self.status = Status::JUNIOR", junior));
	}

	@Test
	void status_senior() throws OclParseException {
		assertEquals(true, eval("self.status = Status::SENIOR", senior));
	}

	// --- Enum literal comparison ---

	@Test
	void status_equals_literal() throws OclParseException {
		assertEquals(true, eval("self.status = Status::JUNIOR", junior));
	}

	@Test
	void status_equals_senior() throws OclParseException {
		assertEquals(true, eval("self.status = Status::SENIOR", senior));
	}

	@Test
	void status_equals_manager() throws OclParseException {
		assertEquals(true, eval("self.status = Status::MANAGER", manager));
	}

	@Test
	void status_notEquals() throws OclParseException {
		assertEquals(true, eval("self.status <> Status::SENIOR", junior));
	}

	// --- Enum in if-then-else ---

	@Test
	void ifStatus_junior() throws OclParseException {
		assertEquals("junior", eval(
				"if self.status = Status::JUNIOR then 'junior' else 'other' endif",
				junior));
	}

	@Test
	void ifStatus_notJunior() throws OclParseException {
		assertEquals("other", eval(
				"if self.status = Status::JUNIOR then 'junior' else 'other' endif",
				senior));
	}

	// --- Enum in let ---

	@Test
	void let_enumComparison() throws OclParseException {
		assertEquals(true, eval(
				"let isManager: Boolean = self.status = Status::MANAGER in isManager",
				manager));
	}

	// --- Enum in collection select ---

	@Test
	void select_byStatus() throws OclParseException {
		assertEquals(1, eval(
				"self.employees->select(e | e.status = Status::MANAGER)->size()",
				company));
	}

	@Test
	void select_seniors() throws OclParseException {
		assertEquals(1, eval(
				"self.employees->select(e | e.status = Status::SENIOR)->size()",
				company));
	}

	@Test
	void select_juniors() throws OclParseException {
		assertEquals(1, eval(
				"self.employees->select(e | e.status = Status::JUNIOR)->size()",
				company));
	}

	// --- Enum in forAll/exists ---

	@Test
	void exists_manager() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->exists(e | e.status = Status::MANAGER)",
				company));
	}

	@Test
	void forAll_notJunior() throws OclParseException {
		assertEquals(false, eval(
				"self.employees->forAll(e | e.status <> Status::JUNIOR)",
				company));
	}

	// --- Enum in reject ---

	@Test
	void reject_juniors() throws OclParseException {
		assertEquals(2, eval(
				"self.employees->reject(e | e.status = Status::JUNIOR)->size()",
				company));
	}

	// --- Enum combined with other conditions ---

	@Test
	void status_and_salary() throws OclParseException {
		assertEquals(true, eval(
				"self.status = Status::MANAGER and self.salary > 80000.0",
				manager));
	}

	@Test
	void status_or_age() throws OclParseException {
		assertEquals(true, eval(
				"self.status = Status::JUNIOR or self.age > 40",
				junior));
	}

	// --- Count by status ---

	@Test
	void collect_status_size() throws OclParseException {
		assertEquals(3, eval(
				"self.employees->collect(e | e.status)->size()",
				company));
	}

	// --- Enum equality is reflexive ---

	@Test
	void enum_selfEquals() throws OclParseException {
		assertEquals(true, eval("self.status = self.status", junior));
	}

	// --- Enum in nested if ---

	@Test
	void nestedIf_enumStatus() throws OclParseException {
		assertEquals("manager", eval(
				"if self.status = Status::JUNIOR then 'junior' " +
				"else if self.status = Status::SENIOR then 'senior' " +
				"else 'manager' endif endif",
				manager));
	}
}
