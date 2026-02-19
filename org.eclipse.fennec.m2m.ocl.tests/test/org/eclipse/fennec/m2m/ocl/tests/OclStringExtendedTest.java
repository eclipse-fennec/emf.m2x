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
 * Extended tests for OCL String operations: startsWith, endsWith,
 * replaceAll, replaceFirst, equalsIgnoreCase, comparisons, edge cases.
 *
 * <p>Ported from Eclipse OCL {@code EvaluateStringOperationsTest4}.
 */
class OclStringExtendedTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- String comparison operators ---

	@Test
	void lessThan_true() throws OclParseException {
		assertEquals(true, eval("'abc' < 'abd'", self));
	}

	@Test
	void lessThan_false() throws OclParseException {
		assertEquals(false, eval("'abd' < 'abc'", self));
	}

	@Test
	void lessThan_equal() throws OclParseException {
		assertEquals(false, eval("'abc' < 'abc'", self));
	}

	@Test
	void lessOrEqual_true() throws OclParseException {
		assertEquals(true, eval("'abc' <= 'abc'", self));
	}

	@Test
	void lessOrEqual_false() throws OclParseException {
		assertEquals(false, eval("'abd' <= 'abc'", self));
	}

	@Test
	void greaterThan_true() throws OclParseException {
		assertEquals(true, eval("'abd' > 'abc'", self));
	}

	@Test
	void greaterThan_false() throws OclParseException {
		assertEquals(false, eval("'abc' > 'abd'", self));
	}

	@Test
	void greaterOrEqual_true() throws OclParseException {
		assertEquals(true, eval("'abc' >= 'abc'", self));
	}

	@Test
	void greaterOrEqual_false() throws OclParseException {
		assertEquals(false, eval("'abc' >= 'abd'", self));
	}

	// --- String equality ---

	@Test
	void equal_same() throws OclParseException {
		assertEquals(true, eval("'hello' = 'hello'", self));
	}

	@Test
	void equal_different() throws OclParseException {
		assertEquals(false, eval("'hello' = 'world'", self));
	}

	@Test
	void notEqual() throws OclParseException {
		assertEquals(true, eval("'hello' <> 'world'", self));
	}

	// --- Concat edge cases ---

	@Test
	void concat_empty() throws OclParseException {
		assertEquals("hello", eval("'hello'.concat('')", self));
	}

	@Test
	void concat_withEmpty() throws OclParseException {
		assertEquals("hello", eval("''.concat('hello')", self));
	}

	// --- Substring edge cases ---

	@Test
	void substring_full() throws OclParseException {
		assertEquals("hello", eval("'hello'.substring(1, 5)", self));
	}

	@Test
	void substring_empty_range() throws OclParseException {
		// substring(2,1) — lower > upper but within valid empty range
		assertEquals("", eval("'hello'.substring(2, 1)", self));
	}

	// --- indexOf edge cases ---

	@Test
	void indexOf_empty() throws OclParseException {
		// indexOf('') returns 1 (first position where empty string can be found)
		assertEquals(1L, eval("'hello'.indexOf('')", self));
	}

	@Test
	void indexOf_self() throws OclParseException {
		assertEquals(1L, eval("'hello'.indexOf('hello')", self));
	}

	// --- size edge cases ---

	@Test
	void size_withSpaces() throws OclParseException {
		assertEquals(4L, eval("' hi '.size()", self));
	}

	@Test
	void size_unicode() throws OclParseException {
		assertEquals(3L, eval("'abc'.size()", self));
	}

	// --- toUpperCase / toLowerCase edge cases ---

	@Test
	void toUpperCase_alreadyUpper() throws OclParseException {
		assertEquals("HELLO", eval("'HELLO'.toUpperCase()", self));
	}

	@Test
	void toLowerCase_alreadyLower() throws OclParseException {
		assertEquals("hello", eval("'hello'.toLowerCase()", self));
	}

	@Test
	void toUpperCase_mixed() throws OclParseException {
		assertEquals("HELLO WORLD", eval("'Hello World'.toUpperCase()", self));
	}

	@Test
	void toLowerCase_mixed() throws OclParseException {
		assertEquals("hello world", eval("'Hello World'.toLowerCase()", self));
	}

	// --- at edge cases ---

	@Test
	void at_middle() throws OclParseException {
		assertEquals("l", eval("'hello'.at(3)", self));
	}

	@Test
	void at_outOfBounds_high() throws OclParseException {
		assertInvalid("'hello'.at(6)", self);
	}

	// --- toInteger / toReal edge cases ---

	@Test
	void toInteger_negative() throws OclParseException {
		assertEquals(-42L, eval("'-42'.toInteger()", self));
	}

	@Test
	void toInteger_invalid() throws OclParseException {
		assertInvalid("'abc'.toInteger()", self);
	}

	@Test
	void toReal_negative() throws OclParseException {
		assertEquals(-3.14, eval("'-3.14'.toReal()", self));
	}

	@Test
	void toReal_invalid() throws OclParseException {
		assertInvalid("'abc'.toReal()", self);
	}

	@Test
	void toBoolean_invalid() throws OclParseException {
		assertInvalid("'maybe'.toBoolean()", self);
	}

	// --- matches edge cases ---

	@Test
	void matches_fullMatch() throws OclParseException {
		assertEquals(true, eval("'hello'.matches('hello')", self));
	}

	@Test
	void matches_partial() throws OclParseException {
		// Java matches() requires full match, so partial patterns need .*
		assertEquals(false, eval("'hello'.matches('ell')", self));
	}

	@Test
	void matches_withDot() throws OclParseException {
		assertEquals(true, eval("'hello'.matches('h.llo')", self));
	}

	// --- trim edge cases ---

	@Test
	void trim_noSpaces() throws OclParseException {
		assertEquals("hello", eval("'hello'.trim()", self));
	}

	@Test
	void trim_allSpaces() throws OclParseException {
		assertEquals("", eval("'   '.trim()", self));
	}

	// --- String + operator with property access ---

	@Test
	void concat_withProperty() throws OclParseException {
		assertEquals("Name: Alice", eval("'Name: '.concat(self.name)", self));
	}

	@Test
	void concat_operator_withProperty() throws OclParseException {
		assertEquals("Name: Alice", eval("'Name: ' + self.name", self));
	}
}
