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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL property access (PropertyCallExp):
 * self.property, chained navigation, cross-references, collections.
 */
class OclPropertyAccessTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 60000.0, true);
		bob = createPerson("Bob", 25, 45000.0, false);
		company = createCompany("ACME", alice, bob);
	}

	// --- Simple Attribute Access ---

	@Test
	void selfName() throws OclParseException {
		assertEquals("Alice", eval("self.name", alice));
	}

	@Test
	void selfAge() throws OclParseException {
		assertEquals(30, eval("self.age", alice));
	}

	@Test
	void selfSalary() throws OclParseException {
		assertEquals(60000.0, eval("self.salary", alice));
	}

	@Test
	void selfIsMarried() throws OclParseException {
		assertEquals(true, eval("self.isMarried", alice));
	}

	// --- Cross-Reference Navigation ---

	@Test
	void personEmployer() throws OclParseException {
		assertEquals("ACME", eval("self.employer.name", alice));
	}

	// --- Collection Navigation ---

	@Test
	void companyEmployees() throws OclParseException {
		Object result = eval("self.employees", company);
		assertInstanceOf(Collection.class, result);
		assertEquals(2, ((Collection<?>) result).size());
	}

	@Test
	void companyName() throws OclParseException {
		assertEquals("ACME", eval("self.name", company));
	}

	// --- Chained Navigation ---

	@Test
	void chainedNavigation() throws OclParseException {
		assertEquals("ACME", eval("self.employer.name", bob));
	}

	// --- Null Navigation ---

	@Test
	void nullReference_strict_returnsInvalid() throws OclParseException {
		// Person without employer (not added to company)
		EObject lonely = createPerson("Lonely", 20, 30000.0, false);
		assertNull(eval("self.employer", lonely));
	}

	// --- Self Expression ---

	@Test
	void selfExpression_returnsEObject() throws OclParseException {
		Object result = eval("self", alice);
		assertTrue(result == alice);
	}
}
