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
 * Tests for OCL enum literal access and comparison.
 * Uses the {@code Status} enum from company.ecore (JUNIOR, SENIOR, MANAGER).
 */
class OclEnumTest extends AbstractOclTest {

	static EObject junior;
	static EObject senior;
	static EObject manager;
	static EObject company;

	@BeforeAll
	static void setUp() {
		junior = createPerson("Alice", 22, 35000.0, false);
		senior = createPerson("Bob", 35, 65000.0, true);
		manager = createPerson("Charlie", 45, 90000.0, true);

		// Set status on each person
		var statusFeature = personClass.getEStructuralFeature("status");
		var statusEnum = companyPackage.getEClassifier("Status");
		var juniorLit = ((org.eclipse.emf.ecore.EEnum) statusEnum).getEEnumLiteral("JUNIOR");
		var seniorLit = ((org.eclipse.emf.ecore.EEnum) statusEnum).getEEnumLiteral("SENIOR");
		var managerLit = ((org.eclipse.emf.ecore.EEnum) statusEnum).getEEnumLiteral("MANAGER");

		junior.eSet(statusFeature, juniorLit.getInstance());
		senior.eSet(statusFeature, seniorLit.getInstance());
		manager.eSet(statusFeature, managerLit.getInstance());

		company = createCompany("ACME", junior, senior, manager);
	}

	// --- Enum literal access ---

	@Test
	void enumLiteral_junior() throws OclParseException {
		assertEquals(true, eval("self.status = Status::JUNIOR", junior));
	}

	@Test
	void enumLiteral_senior() throws OclParseException {
		assertEquals(true, eval("self.status = Status::SENIOR", senior));
	}

	@Test
	void enumLiteral_manager() throws OclParseException {
		assertEquals(true, eval("self.status = Status::MANAGER", manager));
	}

	// --- Enum comparison ---

	@Test
	void enumNotEqual() throws OclParseException {
		assertEquals(true, eval("self.status <> Status::SENIOR", junior));
	}

	@Test
	void enumNotEqual_same() throws OclParseException {
		assertEquals(false, eval("self.status <> Status::JUNIOR", junior));
	}

	// --- Enum in collections / iterators ---

	@Test
	void select_byStatus() throws OclParseException {
		assertEquals(1, eval(
				"self.employees->select(e | e.status = Status::MANAGER)->size()", company));
	}

	@Test
	void reject_juniors() throws OclParseException {
		assertEquals(2, eval(
				"self.employees->reject(e | e.status = Status::JUNIOR)->size()", company));
	}

	@Test
	void exists_senior() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->exists(e | e.status = Status::SENIOR)", company));
	}

	@Test
	void forAll_notJunior() throws OclParseException {
		assertEquals(false, eval(
				"self.employees->forAll(e | e.status <> Status::JUNIOR)", company));
	}
}
