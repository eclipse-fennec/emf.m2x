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

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Advanced tests for OclAny operations.
 * Covers oclIsUndefined, oclIsInvalid, oclIsKindOf, oclIsTypeOf,
 * oclAsType, oclAsSet, and toString in various contexts.
 */
class OclOclAnyAdvancedTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 60000.0, true);
		bob = createPerson("Bob", 25, 45000.0, false);
		company = createCompany("ACME", alice, bob);
	}

	// --- oclIsUndefined ---

	@Test
	void oclIsUndefined_integer() throws OclParseException {
		assertEquals(false, eval("42.oclIsUndefined()", alice));
	}

	@Test
	void oclIsUndefined_string() throws OclParseException {
		assertEquals(false, eval("'hello'.oclIsUndefined()", alice));
	}

	@Test
	void oclIsUndefined_boolean() throws OclParseException {
		assertEquals(false, eval("true.oclIsUndefined()", alice));
	}

	@Test
	void oclIsUndefined_null() throws OclParseException {
		assertEquals(true, eval("null.oclIsUndefined()", alice));
	}

	@Test
	void oclIsUndefined_invalid() throws OclParseException {
		assertEquals(true, eval("invalid.oclIsUndefined()", alice));
	}

	@Test
	void oclIsUndefined_modelObject() throws OclParseException {
		assertEquals(false, eval("self.oclIsUndefined()", alice));
	}

	// --- oclIsInvalid ---

	@Test
	void oclIsInvalid_integer() throws OclParseException {
		assertEquals(false, eval("42.oclIsInvalid()", alice));
	}

	@Test
	void oclIsInvalid_null() throws OclParseException {
		assertEquals(false, eval("null.oclIsInvalid()", alice));
	}

	@Test
	void oclIsInvalid_invalid() throws OclParseException {
		assertEquals(true, eval("invalid.oclIsInvalid()", alice));
	}

	@Test
	void oclIsInvalid_divByZero() throws OclParseException {
		assertEquals(true, eval("(1/0).oclIsInvalid()", alice));
	}

	// --- oclIsKindOf ---

	@Test
	void oclIsKindOf_person() throws OclParseException {
		assertEquals(true, eval("self.oclIsKindOf(Person)", alice));
	}

	@Test
	void oclIsKindOf_company() throws OclParseException {
		assertEquals(true, eval("self.oclIsKindOf(Company)", company));
	}

	@Test
	void oclIsKindOf_wrongType() throws OclParseException {
		assertEquals(false, eval("self.oclIsKindOf(Company)", alice));
	}

	// --- oclIsTypeOf ---

	@Test
	void oclIsTypeOf_person() throws OclParseException {
		assertEquals(true, eval("self.oclIsTypeOf(Person)", alice));
	}

	@Test
	void oclIsTypeOf_company() throws OclParseException {
		assertEquals(true, eval("self.oclIsTypeOf(Company)", company));
	}

	@Test
	void oclIsTypeOf_wrongType() throws OclParseException {
		assertEquals(false, eval("self.oclIsTypeOf(Company)", alice));
	}

	// --- oclAsSet ---

	@Test
	void oclAsSet_integer() throws OclParseException {
		assertEquals(1, eval("42.oclAsSet()->size()", alice));
	}

	@Test
	void oclAsSet_string() throws OclParseException {
		assertEquals(1, eval("'hello'.oclAsSet()->size()", alice));
	}

	@Test
	void oclAsSet_boolean() throws OclParseException {
		assertEquals(1, eval("true.oclAsSet()->size()", alice));
	}

	@Test
	void oclAsSet_null() throws OclParseException {
		assertEquals(0, eval("null.oclAsSet()->size()", alice));
	}

	@Test
	void oclAsSet_modelObject() throws OclParseException {
		Object result = eval("self.oclAsSet()", alice);
		assertInstanceOf(Collection.class, result);
		assertEquals(1, ((Collection<?>) result).size());
	}

	@Test
	void oclAsSet_includes() throws OclParseException {
		assertEquals(true, eval("42.oclAsSet()->includes(42)", alice));
	}

	// --- toString ---

	@Test
	void toString_integer() throws OclParseException {
		assertEquals("42", eval("42.toString()", alice));
	}

	@Test
	void toString_boolean() throws OclParseException {
		assertEquals("true", eval("true.toString()", alice));
	}

	@Test
	void toString_string() throws OclParseException {
		assertEquals("hello", eval("'hello'.toString()", alice));
	}

	@Test
	void toString_negative() throws OclParseException {
		assertEquals("-5", eval("(-5).toString()", alice));
	}

	@Test
	void toString_zero() throws OclParseException {
		assertEquals("0", eval("0.toString()", alice));
	}

	// --- OclAny in iterators ---

	@Test
	void oclIsUndefined_inForAll() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->forAll(i | not i.oclIsUndefined())", alice));
	}

	@Test
	void oclAsSet_inCollect() throws OclParseException {
		// Each element becomes a singleton set, then collect produces a sequence of sets
		Object result = eval(
				"Sequence{1, 2, 3}->collect(i | i.oclAsSet()->size())", alice);
		assertInstanceOf(Collection.class, result);
	}

	// --- OclAny on model in constraint ---

	@Test
	void employeesNotUndefined() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e | not e.oclIsUndefined())", company));
	}

	@Test
	void employeesArePerson() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e | e.oclIsKindOf(Person))", company));
	}

	@Test
	void employeesNotCompany() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e | not e.oclIsKindOf(Company))", company));
	}
}
