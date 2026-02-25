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
 * Tests for OCL Boolean operations: not, and, or, xor, implies.
 */
class OclBooleanOperationsTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- not ---

	@Test
	void not_true() throws OclParseException {
		assertEquals(false, eval("not true", self));
	}

	@Test
	void not_false() throws OclParseException {
		assertEquals(true, eval("not false", self));
	}

	// --- and ---

	@Test
	void and_trueTrue() throws OclParseException {
		assertEquals(true, eval("true and true", self));
	}

	@Test
	void and_trueFalse() throws OclParseException {
		assertEquals(false, eval("true and false", self));
	}

	@Test
	void and_falseFalse() throws OclParseException {
		assertEquals(false, eval("false and false", self));
	}

	// --- or ---

	@Test
	void or_trueTrue() throws OclParseException {
		assertEquals(true, eval("true or true", self));
	}

	@Test
	void or_trueFalse() throws OclParseException {
		assertEquals(true, eval("true or false", self));
	}

	@Test
	void or_falseFalse() throws OclParseException {
		assertEquals(false, eval("false or false", self));
	}

	// --- xor ---

	@Test
	void xor_trueTrue() throws OclParseException {
		assertEquals(false, eval("true xor true", self));
	}

	@Test
	void xor_trueFalse() throws OclParseException {
		assertEquals(true, eval("true xor false", self));
	}

	@Test
	void xor_falseFalse() throws OclParseException {
		assertEquals(false, eval("false xor false", self));
	}

	// --- implies ---

	@Test
	void implies_trueTrue() throws OclParseException {
		assertEquals(true, eval("true implies true", self));
	}

	@Test
	void implies_trueFalse() throws OclParseException {
		assertEquals(false, eval("true implies false", self));
	}

	@Test
	void implies_falseTrue() throws OclParseException {
		assertEquals(true, eval("false implies true", self));
	}

	@Test
	void implies_falseFalse() throws OclParseException {
		assertEquals(true, eval("false implies false", self));
	}

	// --- Combined ---

	@Test
	void combined_notAndOr() throws OclParseException {
		assertEquals(true, eval("not false and (true or false)", self));
	}

	@Test
	void combined_impliesWithNot() throws OclParseException {
		assertEquals(true, eval("true implies not false", self));
	}
}
