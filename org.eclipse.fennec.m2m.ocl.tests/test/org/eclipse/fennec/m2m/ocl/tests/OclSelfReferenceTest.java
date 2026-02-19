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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL 'self' reference in various contexts.
 * Verifies explicit self, implicit self, self in iterators,
 * and self with navigation chains.
 */
class OclSelfReferenceTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 60000.0, true);
		bob = createPerson("Bob", 25, 45000.0, false);
		company = createCompany("ACME", alice, bob);
	}

	// --- Explicit self ---

	@Test
	void explicitSelf_property() throws OclParseException {
		assertEquals("Alice", eval("self.name", alice));
	}

	@Test
	void explicitSelf_intProperty() throws OclParseException {
		assertEquals(30, eval("self.age", alice));
	}

	@Test
	void explicitSelf_boolProperty() throws OclParseException {
		assertEquals(true, eval("self.isMarried", alice));
	}

	@Test
	void explicitSelf_realProperty() throws OclParseException {
		assertEquals(60000.0, eval("self.salary", alice));
	}

	// --- Self with navigation ---

	@Test
	void self_navigation_employer() throws OclParseException {
		assertNotNull(eval("self.employer", alice));
	}

	@Test
	void self_navigation_chain() throws OclParseException {
		assertEquals("ACME", eval("self.employer.name", alice));
	}

	@Test
	void self_navigation_collection() throws OclParseException {
		assertEquals(2, eval("self.employees->size()", company));
	}

	// --- Self in conditions ---

	@Test
	void self_inComparison() throws OclParseException {
		assertEquals(true, eval("self.age >= 18", alice));
	}

	@Test
	void self_inComplexCondition() throws OclParseException {
		assertEquals(true, eval(
				"self.age > 18 and self.salary > 50000.0 and self.isMarried", alice));
	}

	// --- Self in let ---

	@Test
	void self_inLet() throws OclParseException {
		assertEquals("Alice", eval(
				"let n: String = self.name in n", alice));
	}

	@Test
	void self_inLetComputed() throws OclParseException {
		assertEquals(true, eval(
				"let senior: Boolean = self.age > 25 in senior", alice));
	}

	// --- Self in if ---

	@Test
	void self_inIfCondition() throws OclParseException {
		assertEquals("adult", eval(
				"if self.age >= 18 then 'adult' else 'minor' endif", alice));
	}

	@Test
	void self_inIfBranch() throws OclParseException {
		assertEquals("Alice", eval(
				"if true then self.name else 'unknown' endif", alice));
	}

	// --- Self in iterator (should still refer to original context) ---

	@Test
	void self_inIteratorBody() throws OclParseException {
		// self in iterator body still refers to evaluation context
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->forAll(i | self.age > i)", alice));
	}

	@Test
	void self_inModelIterator() throws OclParseException {
		// Iterate over employees, self is company
		assertEquals(true, eval(
				"self.employees->forAll(e | e.salary > 0.0)", company));
	}

	// --- Self equality ---

	@Test
	void self_equalsItself() throws OclParseException {
		assertEquals(true, eval("self = self", alice));
	}

	@Test
	void self_notEqualOther() throws OclParseException {
		// self (company) is not equal to any employee
		assertEquals(0, eval(
				"self.employees->select(e | e = self)->size()", company));
	}

	// --- Self with OclAny operations ---

	@Test
	void self_oclIsUndefined() throws OclParseException {
		assertEquals(false, eval("self.oclIsUndefined()", alice));
	}

	@Test
	void self_oclIsInvalid() throws OclParseException {
		assertEquals(false, eval("self.oclIsInvalid()", alice));
	}

	// --- Self with chained method calls ---

	@Test
	void self_propertyThenMethod() throws OclParseException {
		assertEquals("ALICE", eval("self.name.toUpperCase()", alice));
	}

	@Test
	void self_propertyThenSize() throws OclParseException {
		assertEquals(5, eval("self.name.size()", alice));
	}

	@Test
	void self_arithmeticOnProperty() throws OclParseException {
		assertEquals(31, eval("self.age + 1", alice));
	}

	// --- Different self types ---

	@Test
	void self_asPerson() throws OclParseException {
		assertEquals("Alice", eval("self.name", alice));
	}

	@Test
	void self_asCompany() throws OclParseException {
		assertEquals("ACME", eval("self.name", company));
	}

	@Test
	void self_personEmployer() throws OclParseException {
		assertEquals(2, eval("self.employer.employees->size()", alice));
	}
}
