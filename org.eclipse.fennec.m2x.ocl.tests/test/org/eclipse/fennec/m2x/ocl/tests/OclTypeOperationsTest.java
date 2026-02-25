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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL type operations: oclIsKindOf, oclIsTypeOf, oclAsType,
 * oclAsSet, and type-related expressions.
 *
 * <p>Ported from Eclipse OCL {@code EvaluateOclAnyOperationsTest4}.
 */
class OclTypeOperationsTest extends AbstractOclTest {

	static EObject alice;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 60000.0, true);
		company = createCompany("ACME", alice);
	}

	// --- oclIsKindOf ---

	@Test
	void oclIsKindOf_sameType() throws OclParseException {
		assertEquals(true, eval("self.oclIsKindOf(Person)", alice));
	}

	@Test
	void oclIsKindOf_differentType() throws OclParseException {
		assertEquals(false, eval("self.oclIsKindOf(Company)", alice));
	}

	@Test
	void oclIsKindOf_companyOnPerson() throws OclParseException {
		assertEquals(false, eval("self.oclIsKindOf(Person)", company));
	}

	@Test
	void oclIsKindOf_companyOnCompany() throws OclParseException {
		assertEquals(true, eval("self.oclIsKindOf(Company)", company));
	}

	// --- oclIsTypeOf ---

	@Test
	void oclIsTypeOf_sameType() throws OclParseException {
		assertEquals(true, eval("self.oclIsTypeOf(Person)", alice));
	}

	@Test
	void oclIsTypeOf_differentType() throws OclParseException {
		assertEquals(false, eval("self.oclIsTypeOf(Company)", alice));
	}

	// --- oclAsType ---

	@Test
	void oclAsType_sameType() throws OclParseException {
		Object result = eval("self.oclAsType(Person)", alice);
		assertTrue(result == alice);
	}

	@Test
	void oclAsType_wrongType() throws OclParseException {
		assertInvalid("self.oclAsType(Company)", alice);
	}

	// --- oclAsSet ---

	@Test
	void oclAsSet_eobject() throws OclParseException {
		Object result = eval("self.oclAsSet()", alice);
		assertInstanceOf(OclSet.class, result);
		@SuppressWarnings("unchecked")
		OclSet<Object> set = (OclSet<Object>) result;
		assertEquals(1, set.size());
		assertTrue(set.contains(alice));
	}

	@Test
	void oclAsSet_null() throws OclParseException {
		Object result = eval("null.oclAsSet()", alice);
		assertInstanceOf(OclSet.class, result);
		assertTrue(((OclSet<?>) result).isEmpty());
	}

	// --- Combined type + property tests ---

	@Test
	void oclIsKindOf_afterNavigation() throws OclParseException {
		assertEquals(true, eval("self.employer.oclIsKindOf(Company)", alice));
	}

	@Test
	void oclIsKindOf_inCollection() throws OclParseException {
		// All employees are Persons
		assertEquals(true, eval("self.employees->forAll(e | e.oclIsKindOf(Person))", company));
	}

	@Test
	void oclIsTypeOf_inSelect() throws OclParseException {
		// Select all employees that are Persons (should be all)
		assertEquals(1, eval("self.employees->select(e | e.oclIsTypeOf(Person))->size()", company));
	}

	// --- toString on various types ---

	@Test
	void toString_real() throws OclParseException {
		assertEquals("3.14", eval("(3.14).toString()", alice));
	}

	@Test
	void toString_null() throws OclParseException {
		// Spec §11.2.3: toString() not defined on OclVoid → invalid (general rule).
		assertInvalid("null.toString()", alice);
	}
}
