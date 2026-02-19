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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL comparison operations: =, <>, <, <=, >, >=
 * across Integer, Real, String, and Boolean types.
 */
class OclComparisonTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Equality (=) ---

	@Test
	void equals_integers() throws OclParseException {
		assertEquals(true, eval("1 = 1", self));
	}

	@Test
	void equals_integers_notEqual() throws OclParseException {
		assertEquals(false, eval("1 = 2", self));
	}

	@Test
	void equals_strings() throws OclParseException {
		assertEquals(true, eval("'hello' = 'hello'", self));
	}

	@Test
	void equals_strings_notEqual() throws OclParseException {
		assertEquals(false, eval("'hello' = 'world'", self));
	}

	@Test
	void equals_booleans() throws OclParseException {
		assertEquals(true, eval("true = true", self));
	}

	@Test
	void equals_nulls() throws OclParseException {
		assertEquals(true, eval("null = null", self));
	}

	@Test
	void equals_nullAndValue() throws OclParseException {
		assertEquals(false, eval("null = 1", self));
	}

	// --- Not Equal (<>) ---

	@Test
	void notEquals_integers() throws OclParseException {
		assertEquals(true, eval("1 <> 2", self));
	}

	@Test
	void notEquals_sameIntegers() throws OclParseException {
		assertEquals(false, eval("1 <> 1", self));
	}

	// --- Integer Comparisons ---

	@Test
	void lessThan_integers() throws OclParseException {
		assertEquals(true, eval("1 < 2", self));
	}

	@Test
	void lessThan_integers_false() throws OclParseException {
		assertEquals(false, eval("2 < 1", self));
	}

	@Test
	void lessThan_integers_equal() throws OclParseException {
		assertEquals(false, eval("2 < 2", self));
	}

	@Test
	void lessOrEqual_integers() throws OclParseException {
		assertEquals(true, eval("2 <= 2", self));
	}

	@Test
	void greaterThan_integers() throws OclParseException {
		assertEquals(true, eval("3 > 2", self));
	}

	@Test
	void greaterOrEqual_integers() throws OclParseException {
		assertEquals(true, eval("3 >= 3", self));
	}

	// --- Real Comparisons ---

	@Test
	void lessThan_reals() throws OclParseException {
		assertEquals(true, eval("1.5 < 2.5", self));
	}

	@Test
	void greaterThan_reals() throws OclParseException {
		assertEquals(true, eval("3.5 > 2.5", self));
	}

	// --- String Comparisons ---

	@Test
	void lessThan_strings() throws OclParseException {
		assertEquals(true, eval("'abc' < 'def'", self));
	}

	@Test
	void greaterThan_strings() throws OclParseException {
		assertEquals(true, eval("'def' > 'abc'", self));
	}
}
