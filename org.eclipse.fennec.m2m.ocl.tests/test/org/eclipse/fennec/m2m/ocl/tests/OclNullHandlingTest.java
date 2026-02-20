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
 * Tests for OCL null (OclVoid) handling.
 * In OCL, null represents the OclVoid value.
 * Operations on null should follow OCL semantics.
 */
class OclNullHandlingTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- null literal ---

	@Test
	void null_literal() throws OclParseException {
		assertNull(eval("null", self));
	}

	// --- null equality ---

	@Test
	void null_equalsNull() throws OclParseException {
		assertEquals(true, eval("null = null", self));
	}

	@Test
	void null_notEqualsValue() throws OclParseException {
		assertEquals(false, eval("null = 1", self));
	}

	@Test
	void value_notEqualsNull() throws OclParseException {
		assertEquals(false, eval("1 = null", self));
	}

	@Test
	void null_notEqual_operator() throws OclParseException {
		assertEquals(true, eval("null <> 1", self));
	}

	@Test
	void null_notEqual_null() throws OclParseException {
		assertEquals(false, eval("null <> null", self));
	}

	// --- oclIsUndefined ---

	@Test
	void null_oclIsUndefined() throws OclParseException {
		assertEquals(true, eval("null.oclIsUndefined()", self));
	}

	@Test
	void value_oclIsUndefined() throws OclParseException {
		assertEquals(false, eval("42.oclIsUndefined()", self));
	}

	@Test
	void string_oclIsUndefined() throws OclParseException {
		assertEquals(false, eval("'hello'.oclIsUndefined()", self));
	}

	// --- oclIsInvalid on null ---

	@Test
	void null_isNotInvalid() throws OclParseException {
		assertEquals(false, eval("null.oclIsInvalid()", self));
	}

	// --- null in collections ---

	@Test
	void sequence_includesNull() throws OclParseException {
		assertEquals(true, eval("Sequence{1, null, 3}->includes(null)", self));
	}

	@Test
	void sequence_excludesNull() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3}->excludes(null)", self));
	}

	@Test
	void set_includesNull() throws OclParseException {
		assertEquals(true, eval("Set{1, null, 3}->includes(null)", self));
	}

	@Test
	void sequence_withNull_size() throws OclParseException {
		assertEquals(3, eval("Sequence{1, null, 3}->size()", self));
	}

	// --- null in boolean expressions ---

	@Test
	void null_oclIsUndefined_inIf() throws OclParseException {
		assertEquals("null", eval(
				"if null.oclIsUndefined() then 'null' else 'not null' endif", self));
	}

	// --- null in let ---

	@Test
	void let_nullValue() throws OclParseException {
		assertNull(eval("let x: OclVoid = null in x", self));
	}

	@Test
	void let_nullCheck() throws OclParseException {
		assertEquals(true, eval(
				"let x: OclVoid = null in x.oclIsUndefined()", self));
	}

	// --- null in if branches ---

	@Test
	void if_nullThen() throws OclParseException {
		assertNull(eval("if true then null else 42 endif", self));
	}

	@Test
	void if_nullElse() throws OclParseException {
		assertNull(eval("if false then 42 else null endif", self));
	}

	// --- oclAsSet on null ---

	@Test
	void null_oclAsSet() throws OclParseException {
		assertEquals(0, eval("null.oclAsSet()->size()", self));
	}

	// --- null toString ---

	@Test
	void null_toString() throws OclParseException {
		assertEquals("null", eval("null.toString()", self));
	}
}
