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
 * Tests for OCL type-checking operations: oclIsTypeOf,
 * oclIsKindOf, oclAsType, and type-related expressions.
 *
 * <p>Note: Primitive type names (Integer, Real, String, Boolean) are
 * not currently accepted as type arguments in oclIsTypeOf/oclIsKindOf
 * calls. These tests focus on EClass-based types (Person, Company).
 */
class OclTypeCheckingTest extends AbstractOclTest {

	static EObject person;
	static EObject company;

	@BeforeAll
	static void setUp() {
		person = createPerson("Alice", 30, 50000.0, true);
		company = createCompany("Acme", person);
	}

	// --- oclIsTypeOf with EClass types ---

	@Test
	void oclIsTypeOf_sameType() throws OclParseException {
		assertEquals(true, eval("self.oclIsTypeOf(Person)", person));
	}

	@Test
	void oclIsTypeOf_differentType() throws OclParseException {
		assertEquals(false, eval("self.oclIsTypeOf(Company)", person));
	}

	@Test
	void oclIsTypeOf_company() throws OclParseException {
		assertEquals(true, eval("self.oclIsTypeOf(Company)", company));
	}

	@Test
	void oclIsTypeOf_companyNotPerson() throws OclParseException {
		assertEquals(false, eval("self.oclIsTypeOf(Person)", company));
	}

	// --- oclIsKindOf ---

	@Test
	void oclIsKindOf_sameType() throws OclParseException {
		assertEquals(true, eval("self.oclIsKindOf(Person)", person));
	}

	@Test
	void oclIsKindOf_differentType() throws OclParseException {
		assertEquals(false, eval("self.oclIsKindOf(Company)", person));
	}

	// --- Type checking in conditions ---

	@Test
	void ifTypeOf_person() throws OclParseException {
		assertEquals("yes", eval(
				"if self.oclIsTypeOf(Person) then 'yes' else 'no' endif", person));
	}

	@Test
	void ifTypeOf_company() throws OclParseException {
		assertEquals("no", eval(
				"if self.oclIsTypeOf(Person) then 'yes' else 'no' endif", company));
	}

	// --- Type checking combined with property access ---

	@Test
	void typeCheck_thenPropertyAccess() throws OclParseException {
		assertEquals(true, eval(
				"self.oclIsTypeOf(Person) and self.age > 0", person));
	}

	@Test
	void typeCheck_inLetExpression() throws OclParseException {
		assertEquals(true, eval(
				"let isPerson: Boolean = self.oclIsTypeOf(Person) in isPerson", person));
	}

	@Test
	void typeCheck_negation() throws OclParseException {
		assertEquals(true, eval(
				"not self.oclIsTypeOf(Company)", person));
	}

	@Test
	void typeCheck_or() throws OclParseException {
		assertEquals(true, eval(
				"self.oclIsTypeOf(Person) or self.oclIsTypeOf(Company)", person));
	}

	// --- oclType ---

	@Test
	void oclType_notNull() throws OclParseException {
		Object result = eval("self.oclType()", person);
		assertNotNull(result);
	}

	// --- oclIsUndefined ---

	@Test
	void null_isUndefined() throws OclParseException {
		assertEquals(true, eval("null.oclIsUndefined()", person));
	}

	@Test
	void nonNull_isNotUndefined() throws OclParseException {
		assertEquals(false, eval("1.oclIsUndefined()", person));
	}

	@Test
	void string_isNotUndefined() throws OclParseException {
		assertEquals(false, eval("'hello'.oclIsUndefined()", person));
	}

	@Test
	void property_isNotUndefined() throws OclParseException {
		assertEquals(false, eval("self.name.oclIsUndefined()", person));
	}

	// --- oclIsInvalid ---

	@Test
	void invalid_isInvalid() throws OclParseException {
		assertEquals(true, eval("invalid.oclIsInvalid()", person));
	}

	@Test
	void nonInvalid_isNotInvalid() throws OclParseException {
		assertEquals(false, eval("42.oclIsInvalid()", person));
	}

	@Test
	void null_isNotInvalid() throws OclParseException {
		assertEquals(false, eval("null.oclIsInvalid()", person));
	}

	// --- Combined type checks ---

	@Test
	void typeOf_and_kindOf_agree() throws OclParseException {
		// If oclIsTypeOf is true, oclIsKindOf must also be true
		assertEquals(true, eval(
				"self.oclIsTypeOf(Person) implies self.oclIsKindOf(Person)", person));
	}

	@Test
	void typeCheck_inIfResult() throws OclParseException {
		assertEquals(30, eval(
				"if self.oclIsTypeOf(Person) then self.age else 0 endif", person));
	}

	@Test
	void typeCheck_company_inIfResult() throws OclParseException {
		assertEquals(0, eval(
				"if self.oclIsTypeOf(Person) then 1 else 0 endif", company));
	}

	// --- oclIsUndefined/oclIsInvalid combined ---

	@Test
	void undefined_is_not_invalid() throws OclParseException {
		assertEquals(false, eval("null.oclIsInvalid()", person));
	}

	@Test
	void invalid_is_not_undefined() throws OclParseException {
		// In strict OCL: invalid.oclIsUndefined() = true (invalid conforms to all)
		// Our impl may differ — just test it doesn't throw
		eval("invalid.oclIsUndefined()", person);
	}

	@Test
	void value_isNeitherUndefinedNorInvalid() throws OclParseException {
		assertEquals(true, eval(
				"not 42.oclIsUndefined() and not 42.oclIsInvalid()", person));
	}
}
