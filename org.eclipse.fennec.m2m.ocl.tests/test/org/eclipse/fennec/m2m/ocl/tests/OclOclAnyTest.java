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

import java.util.LinkedHashSet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OclAny operations: oclIsUndefined, oclIsInvalid,
 * oclIsKindOf, oclIsTypeOf, oclAsType, oclAsSet, toString.
 */
class OclOclAnyTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- oclIsUndefined ---

	@Test
	void oclIsUndefined_null() throws OclParseException {
		assertEquals(true, eval("null.oclIsUndefined()", self));
	}

	@Test
	void oclIsUndefined_invalid() throws OclParseException {
		assertEquals(true, eval("invalid.oclIsUndefined()", self));
	}

	@Test
	void oclIsUndefined_value() throws OclParseException {
		assertEquals(false, eval("1.oclIsUndefined()", self));
	}

	@Test
	void oclIsUndefined_string() throws OclParseException {
		assertEquals(false, eval("'hello'.oclIsUndefined()", self));
	}

	// --- oclIsInvalid ---

	@Test
	void oclIsInvalid_invalid() throws OclParseException {
		assertEquals(true, eval("invalid.oclIsInvalid()", self));
	}

	@Test
	void oclIsInvalid_null() throws OclParseException {
		assertEquals(false, eval("null.oclIsInvalid()", self));
	}

	@Test
	void oclIsInvalid_value() throws OclParseException {
		assertEquals(false, eval("42.oclIsInvalid()", self));
	}

	// --- oclAsSet ---

	@Test
	void oclAsSet_integer() throws OclParseException {
		Object result = eval("42.oclAsSet()", self);
		assertInstanceOf(LinkedHashSet.class, result);
		@SuppressWarnings("unchecked")
		LinkedHashSet<Object> set = (LinkedHashSet<Object>) result;
		assertEquals(1, set.size());
		assertEquals(true, set.contains(42L));
	}

	@Test
	void oclAsSet_string() throws OclParseException {
		Object result = eval("'hello'.oclAsSet()", self);
		assertInstanceOf(LinkedHashSet.class, result);
		assertEquals(1, ((LinkedHashSet<?>) result).size());
	}

	// --- toString ---

	@Test
	void toString_integer() throws OclParseException {
		assertEquals("42", eval("42.toString()", self));
	}

	@Test
	void toString_boolean() throws OclParseException {
		assertEquals("true", eval("true.toString()", self));
	}

	@Test
	void toString_string() throws OclParseException {
		assertEquals("hello", eval("'hello'.toString()", self));
	}
}
