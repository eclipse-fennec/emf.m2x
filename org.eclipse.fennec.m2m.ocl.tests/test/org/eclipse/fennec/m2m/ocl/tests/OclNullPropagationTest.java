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
import static org.junit.jupiter.api.Assertions.assertNull;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for correct null (OclVoid) propagation through the evaluator.
 *
 * <p>The EMF-generated OclSwitch treats a Java {@code null} return from
 * {@code caseXxx} as "not handled" and falls through to {@code defaultCase}.
 * These tests verify that OCL null values are correctly propagated in all
 * expression types, not silently converted to OclInvalid.
 */
class OclNullPropagationTest extends AbstractOclTest {

	static EObject self;
	static EObject lonely;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
		lonely = createPerson("Lonely", 20, 30000.0, false);
	}

	// --- Null literal ---

	@Test
	void nullLiteral_returnsNull() throws OclParseException {
		assertNull(eval("null", self));
	}

	// --- Property returning null ---

	@Test
	void unsetReference_returnsNull() throws OclParseException {
		// Person not added to any company — employer is unset
		assertNull(eval("self.employer", lonely));
	}

	// --- If expression with null branches ---

	@Test
	void ifThenNull() throws OclParseException {
		assertNull(eval("if true then null else 42 endif", self));
	}

	@Test
	void ifElseNull() throws OclParseException {
		assertNull(eval("if false then 42 else null endif", self));
	}

	// --- Let expression with null ---

	@Test
	void letWithNullInit() throws OclParseException {
		assertNull(eval("let x : Integer = null in x", self));
	}

	@Test
	void letBodyReturnsNull() throws OclParseException {
		assertNull(eval("let x : Integer = 42 in null", self));
	}

	// --- Operations on null ---

	@Test
	void nullEqualsNull() throws OclParseException {
		assertEquals(true, eval("null = null", self));
	}

	@Test
	void nullNotEqualsValue() throws OclParseException {
		assertEquals(true, eval("null <> 42", self));
	}

	@Test
	void oclIsUndefined_onNull() throws OclParseException {
		assertEquals(true, eval("null.oclIsUndefined()", self));
	}

	@Test
	void oclIsUndefined_onInvalid() throws OclParseException {
		assertEquals(true, eval("invalid.oclIsUndefined()", self));
	}

	@Test
	void oclIsInvalid_onNull() throws OclParseException {
		assertEquals(false, eval("null.oclIsInvalid()", self));
	}

	// --- Null in collections ---

	@Test
	void sequenceContainingNull() throws OclParseException {
		Object result = eval("Sequence{1, null, 3}", self);
		assertNull(((java.util.List<?>) result).get(1));
	}

	// --- Nested null propagation ---

	@Test
	void nestedIfWithNull() throws OclParseException {
		assertNull(eval("if true then if true then null else 1 endif else 2 endif", self));
	}

	@Test
	void letWithNestedNull() throws OclParseException {
		assertNull(eval("let x : OclVoid = null in let y : OclVoid = x in y", self));
	}
}
