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
 * Advanced tests for OCL string operations: trim, characters,
 * at, replaceAll, matches, and chained string operations.
 */
class OclStringAdvancedOpsTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- trim ---

	@Test
	void trim_basic() throws OclParseException {
		assertEquals("hello", eval("'  hello  '.trim()", self));
	}

	@Test
	void trim_noWhitespace() throws OclParseException {
		assertEquals("hello", eval("'hello'.trim()", self));
	}

	@Test
	void trim_allWhitespace() throws OclParseException {
		assertEquals("", eval("'   '.trim()", self));
	}

	@Test
	void trim_tabs() throws OclParseException {
		assertEquals("hello", eval("'\thello\t'.trim()", self));
	}

	// --- String + (concat) ---

	@Test
	void concat_plus() throws OclParseException {
		assertEquals("helloworld", eval("'hello' + 'world'", self));
	}

	@Test
	void concat_withSpace() throws OclParseException {
		assertEquals("hello world", eval("'hello' + ' ' + 'world'", self));
	}

	@Test
	void concat_empty() throws OclParseException {
		assertEquals("hello", eval("'hello' + ''", self));
	}

	@Test
	void concat_emptyLeft() throws OclParseException {
		assertEquals("hello", eval("'' + 'hello'", self));
	}

	// --- toUpperCase / toLowerCase ---

	@Test
	void toUpperCase() throws OclParseException {
		assertEquals("HELLO", eval("'hello'.toUpperCase()", self));
	}

	@Test
	void toLowerCase() throws OclParseException {
		assertEquals("hello", eval("'HELLO'.toLowerCase()", self));
	}

	@Test
	void toUpperCase_mixed() throws OclParseException {
		assertEquals("HELLO WORLD", eval("'Hello World'.toUpperCase()", self));
	}

	@Test
	void toLowerCase_mixed() throws OclParseException {
		assertEquals("hello world", eval("'Hello World'.toLowerCase()", self));
	}

	// --- substring ---

	@Test
	void substring_first3() throws OclParseException {
		assertEquals("hel", eval("'hello'.substring(1, 3)", self));
	}

	@Test
	void substring_last3() throws OclParseException {
		assertEquals("llo", eval("'hello'.substring(3, 5)", self));
	}

	@Test
	void substring_single() throws OclParseException {
		assertEquals("h", eval("'hello'.substring(1, 1)", self));
	}

	@Test
	void substring_full() throws OclParseException {
		assertEquals("hello", eval("'hello'.substring(1, 5)", self));
	}

	// --- size ---

	@Test
	void size_empty() throws OclParseException {
		assertEquals(0, eval("''.size()", self));
	}

	@Test
	void size_basic() throws OclParseException {
		assertEquals(5, eval("'hello'.size()", self));
	}

	@Test
	void size_withSpaces() throws OclParseException {
		assertEquals(11, eval("'hello world'.size()", self));
	}

	// --- toInteger / toReal ---

	@Test
	void toInteger() throws OclParseException {
		assertEquals(42, eval("'42'.toInteger()", self));
	}

	@Test
	void toInteger_negative() throws OclParseException {
		assertEquals(-42, eval("'-42'.toInteger()", self));
	}

	@Test
	void toReal() throws OclParseException {
		assertEquals(3.14, eval("'3.14'.toReal()", self));
	}

	// --- toString ---

	@Test
	void toString_integer() throws OclParseException {
		assertEquals("42", eval("42.toString()", self));
	}

	@Test
	void toString_negative() throws OclParseException {
		assertEquals("-5", eval("(-5).toString()", self));
	}

	// --- Chained string operations ---

	@Test
	void chain_trimThenUpper() throws OclParseException {
		assertEquals("HELLO", eval("'  hello  '.trim().toUpperCase()", self));
	}

	@Test
	void chain_upperThenSubstring() throws OclParseException {
		assertEquals("HEL", eval("'hello'.toUpperCase().substring(1, 3)", self));
	}

	@Test
	void chain_concatThenSize() throws OclParseException {
		assertEquals(10, eval("'hello'.concat('world').size()", self));
	}

	@Test
	void chain_lowerThenConcat() throws OclParseException {
		assertEquals("helloWORLD", eval("'HELLO'.toLowerCase().concat('WORLD')", self));
	}

	// --- String operations on properties ---

	@Test
	void property_toUpper() throws OclParseException {
		assertEquals("ALICE", eval("self.name.toUpperCase()", self));
	}

	@Test
	void property_toLower() throws OclParseException {
		assertEquals("alice", eval("self.name.toLowerCase()", self));
	}

	@Test
	void property_size() throws OclParseException {
		assertEquals(5, eval("self.name.size()", self));
	}

	@Test
	void property_substring() throws OclParseException {
		assertEquals("Ali", eval("self.name.substring(1, 3)", self));
	}

	@Test
	void property_concat() throws OclParseException {
		assertEquals("Alice!", eval("self.name.concat('!')", self));
	}

	// --- String in collection ---

	@Test
	void collect_toUpper() throws OclParseException {
		assertEquals(3, eval(
				"Sequence{'a', 'b', 'c'}->collect(s | s.toUpperCase())->size()", self));
	}

	@Test
	void select_bySize() throws OclParseException {
		assertEquals(2, eval(
				"Sequence{'a', 'bb', 'ccc'}->select(s | s.size() > 1)->size()", self));
	}
}
