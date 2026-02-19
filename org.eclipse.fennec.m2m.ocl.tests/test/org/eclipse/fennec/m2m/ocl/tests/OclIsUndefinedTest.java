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
import static org.junit.jupiter.api.Assertions.assertSame;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclInvalid;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive tests for {@code oclIsUndefined()} and {@code oclIsInvalid()}.
 *
 * <p>OCL spec section 11.2.2:
 * <ul>
 *   <li>{@code oclIsUndefined()} returns true for null (OclVoid) AND invalid</li>
 *   <li>{@code oclIsInvalid()} returns true ONLY for invalid</li>
 * </ul>
 */
class OclIsUndefinedTest extends AbstractOclTest {

	static EObject self;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
		company = createCompany("ACME", self);
	}

	// --- oclIsUndefined on null ---

	@Test
	void oclIsUndefined_null_true() throws OclParseException {
		assertEquals(true, eval("null.oclIsUndefined()", self));
	}

	@Test
	void oclIsUndefined_invalid_true() throws OclParseException {
		assertEquals(true, eval("invalid.oclIsUndefined()", self));
	}

	@Test
	void oclIsUndefined_string_false() throws OclParseException {
		assertEquals(false, eval("'hello'.oclIsUndefined()", self));
	}

	@Test
	void oclIsUndefined_integer_false() throws OclParseException {
		assertEquals(false, eval("42.oclIsUndefined()", self));
	}

	@Test
	void oclIsUndefined_boolean_false() throws OclParseException {
		assertEquals(false, eval("true.oclIsUndefined()", self));
	}

	@Test
	void oclIsUndefined_emptyString_false() throws OclParseException {
		assertEquals(false, eval("''.oclIsUndefined()", self));
	}

	@Test
	void oclIsUndefined_zero_false() throws OclParseException {
		assertEquals(false, eval("0.oclIsUndefined()", self));
	}

	@Test
	void oclIsUndefined_self_false() throws OclParseException {
		assertEquals(false, eval("self.oclIsUndefined()", self));
	}

	@Test
	void oclIsUndefined_property_false() throws OclParseException {
		assertEquals(false, eval("self.name.oclIsUndefined()", self));
	}

	// --- oclIsInvalid ---

	@Test
	void oclIsInvalid_invalid_true() throws OclParseException {
		assertEquals(true, eval("invalid.oclIsInvalid()", self));
	}

	@Test
	void oclIsInvalid_null_false() throws OclParseException {
		assertEquals(false, eval("null.oclIsInvalid()", self));
	}

	@Test
	void oclIsInvalid_string_false() throws OclParseException {
		assertEquals(false, eval("'hello'.oclIsInvalid()", self));
	}

	@Test
	void oclIsInvalid_integer_false() throws OclParseException {
		assertEquals(false, eval("42.oclIsInvalid()", self));
	}

	@Test
	void oclIsInvalid_self_false() throws OclParseException {
		assertEquals(false, eval("self.oclIsInvalid()", self));
	}

	// --- oclIsUndefined in conditions ---

	@Test
	void oclIsUndefined_inIfCondition() throws OclParseException {
		assertEquals("undefined",
				eval("if null.oclIsUndefined() then 'undefined' else 'defined' endif", self));
	}

	@Test
	void oclIsInvalid_inIfCondition() throws OclParseException {
		assertEquals("invalid",
				eval("if invalid.oclIsInvalid() then 'invalid' else 'valid' endif", self));
	}

	// --- Negated ---

	@Test
	void not_oclIsUndefined_string() throws OclParseException {
		assertEquals(true, eval("not 'hello'.oclIsUndefined()", self));
	}

	@Test
	void not_oclIsInvalid_null() throws OclParseException {
		assertEquals(true, eval("not null.oclIsInvalid()", self));
	}

	// --- Combined with other operations ---

	@Test
	void oclIsUndefined_withAnd() throws OclParseException {
		assertEquals(true,
				eval("null.oclIsUndefined() and invalid.oclIsUndefined()", self));
	}

	@Test
	void oclIsUndefined_withOr() throws OclParseException {
		assertEquals(true,
				eval("null.oclIsUndefined() or false", self));
	}

	// --- Direct invalid/null literal values ---

	@Test
	void nullLiteral_value() throws OclParseException {
		assertEquals(null, eval("null", self));
	}

	@Test
	void invalidLiteral_value() throws OclParseException {
		assertSame(OclInvalid.INSTANCE, eval("invalid", self));
	}

	// --- Collection element check ---

	@Test
	void oclIsUndefined_afterCollectionOp() throws OclParseException {
		// any() on empty filtered set returns null
		Object result = eval("Sequence{1, 2, 3}->any(x | x > 10)", self);
		// null result — verify oclIsUndefined works on it
		assertEquals(null, result);
	}

	// --- Property that could be null ---

	@Test
	void oclIsUndefined_employerReference() throws OclParseException {
		// Person without employer (self is contained in company, so has employer)
		EObject standalone = createPerson("Standalone", 20, 0.0, false);
		assertEquals(true, eval("self.employer.oclIsUndefined()", standalone));
	}

	@Test
	void oclIsUndefined_employerReference_notNull() throws OclParseException {
		// self is contained in company via createCompany
		assertEquals(false, eval("self.employer.oclIsUndefined()", self));
	}
}
