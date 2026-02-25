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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL {@code oclAsType} type cast operation.
 */
class OclOclAsTypeTest extends AbstractOclTest {

	static EObject person;
	static EObject company;

	@BeforeAll
	static void setUp() {
		person = createPerson("Alice", 30, 50000.0, true);
		company = createCompany("Acme", person);
	}

	// --- oclAsType to same type ---

	@Test
	void oclAsType_sameType() throws OclParseException {
		assertEquals("Alice", eval("self.oclAsType(Person).name", person));
	}

	@Test
	void oclAsType_company() throws OclParseException {
		assertEquals("Acme", eval("self.oclAsType(Company).name", company));
	}

	// --- oclAsType preserves identity ---

	@Test
	void oclAsType_preservesIdentity() throws OclParseException {
		assertEquals(true, eval("self = self.oclAsType(Person)", person));
	}

	@Test
	void oclAsType_companyIdentity() throws OclParseException {
		assertEquals(true, eval("self = self.oclAsType(Company)", company));
	}

	// --- oclAsType then property access ---

	@Test
	void oclAsType_thenName() throws OclParseException {
		assertEquals("Alice", eval("self.oclAsType(Person).name", person));
	}

	@Test
	void oclAsType_thenAge() throws OclParseException {
		assertEquals(30, eval("self.oclAsType(Person).age", person));
	}

	@Test
	void oclAsType_companyName() throws OclParseException {
		assertEquals("Acme", eval("self.oclAsType(Company).name", company));
	}

	// --- oclAsType in conditions ---

	@Test
	void oclAsType_typeCheckFirst() throws OclParseException {
		assertEquals(true, eval(
				"self.oclIsTypeOf(Person) and self.age > 0", person));
	}

	// --- oclAsType with not-null check ---

	@Test
	void oclAsType_notNull() throws OclParseException {
		assertEquals(false, eval(
				"self.oclAsType(Person).oclIsUndefined()", person));
	}

	@Test
	void oclAsType_notInvalid() throws OclParseException {
		assertEquals(false, eval(
				"self.oclAsType(Person).oclIsInvalid()", person));
	}

	// --- oclAsType type check round-trip ---

	@Test
	void oclAsType_isTypeOf() throws OclParseException {
		assertEquals(true, eval(
				"self.oclAsType(Person).oclIsTypeOf(Person)", person));
	}

	@Test
	void oclAsType_isKindOf() throws OclParseException {
		assertEquals(true, eval(
				"self.oclAsType(Person).oclIsKindOf(Person)", person));
	}
}
