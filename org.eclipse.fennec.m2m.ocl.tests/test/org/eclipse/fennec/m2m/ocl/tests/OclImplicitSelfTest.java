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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for implicit self references.
 * In OCL, property access without explicit 'self.' prefix
 * implicitly refers to the context object.
 */
class OclImplicitSelfTest extends AbstractOclTest {

	static EObject alice;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 60000.0, true);
		company = createCompany("ACME", alice);
	}

	// --- Implicit self on attributes ---

	@Test
	void implicitSelf_name() throws OclParseException {
		assertEquals("Alice", eval("name", alice));
	}

	@Test
	void implicitSelf_age() throws OclParseException {
		assertEquals(30, eval("age", alice));
	}

	@Test
	void implicitSelf_salary() throws OclParseException {
		assertEquals(60000.0, eval("salary", alice));
	}

	@Test
	void implicitSelf_isMarried() throws OclParseException {
		assertEquals(true, eval("isMarried", alice));
	}

	// --- Implicit self on references ---

	@Test
	void implicitSelf_employer() throws OclParseException {
		Object result = eval("employer", alice);
		assertTrue(result == company);
	}

	@Test
	void implicitSelf_employees() throws OclParseException {
		Object result = eval("employees", company);
		assertInstanceOf(Collection.class, result);
		assertEquals(1, ((Collection<?>) result).size());
	}

	// --- Implicit self in expressions ---

	@Test
	void implicitSelf_inComparison() throws OclParseException {
		assertEquals(true, eval("age > 25", alice));
	}

	@Test
	void implicitSelf_inConcat() throws OclParseException {
		assertEquals("Hello Alice", eval("'Hello '.concat(name)", alice));
	}

	@Test
	void implicitSelf_inIf() throws OclParseException {
		assertEquals("married", eval(
				"if isMarried then 'married' else 'single' endif", alice));
	}

	@Test
	void implicitSelf_inLet() throws OclParseException {
		assertEquals(65000.0, eval(
				"let bonus: Real = 5000.0 in salary + bonus", alice));
	}

	// --- Implicit self with chained navigation ---

	@Test
	void implicitSelf_chainedNavigation() throws OclParseException {
		assertEquals("ACME", eval("employer.name", alice));
	}

	// --- Implicit self with collection operations ---

	@Test
	void implicitSelf_collectionSize() throws OclParseException {
		assertEquals(1, eval("employees->size()", company));
	}

	@Test
	void implicitSelf_collectionForAll() throws OclParseException {
		assertEquals(true, eval("employees->forAll(e | e.age > 20)", company));
	}

	// --- Explicit self vs implicit self equivalence ---

	@Test
	void explicitEqualsImplicit_name() throws OclParseException {
		Object explicit = eval("self.name", alice);
		Object implicit = eval("name", alice);
		assertEquals(explicit, implicit);
	}

	@Test
	void explicitEqualsImplicit_age() throws OclParseException {
		Object explicit = eval("self.age", alice);
		Object implicit = eval("age", alice);
		assertEquals(explicit, implicit);
	}
}
