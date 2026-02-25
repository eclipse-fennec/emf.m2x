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
 * Tests for OCL string indexing and search operations.
 * OCL uses 1-based indexing for strings.
 */
class OclStringIndexTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- substring (1-based, inclusive) ---

	@Test
	void substring_middle() throws OclParseException {
		assertEquals("bcd", eval("'abcdef'.substring(2, 4)", self));
	}

	@Test
	void substring_firstChar() throws OclParseException {
		assertEquals("a", eval("'abcdef'.substring(1, 1)", self));
	}

	@Test
	void substring_lastChar() throws OclParseException {
		assertEquals("f", eval("'abcdef'.substring(6, 6)", self));
	}

	@Test
	void substring_whole() throws OclParseException {
		assertEquals("abc", eval("'abc'.substring(1, 3)", self));
	}

	// --- indexOf ---

	@Test
	void indexOf_found() throws OclParseException {
		assertEquals(3, eval("'abcdef'.indexOf('cd')", self));
	}

	@Test
	void indexOf_first() throws OclParseException {
		assertEquals(1, eval("'abcdef'.indexOf('a')", self));
	}

	@Test
	void indexOf_last() throws OclParseException {
		assertEquals(6, eval("'abcdef'.indexOf('f')", self));
	}

	@Test
	void indexOf_notFound() throws OclParseException {
		// OCL v2.4 §11.5.3: "or zero if s is not a substring of self"
		assertEquals(0, eval("'abcdef'.indexOf('xyz')", self));
	}

	@Test
	void indexOf_multiChar() throws OclParseException {
		assertEquals(2, eval("'hello world'.indexOf('ello')", self));
	}

	// --- lastIndexOf (Eclipse extension, not in OCL v2.4 spec) ---

	@Test
	void lastIndexOf_lastOccurrence() throws OclParseException {
		assertEquals(4, eval("'test'.lastIndexOf('t')", self));
	}

	@Test
	void lastIndexOf_firstOccurrence() throws OclParseException {
		assertEquals(1, eval("'test'.lastIndexOf('te')", self));
	}

	@Test
	void lastIndexOf_middle() throws OclParseException {
		assertEquals(2, eval("'test'.lastIndexOf('es')", self));
	}

	@Test
	void lastIndexOf_suffix() throws OclParseException {
		assertEquals(3, eval("'test'.lastIndexOf('st')", self));
	}

	@Test
	void lastIndexOf_singleChar() throws OclParseException {
		assertEquals(5, eval("'tesla'.lastIndexOf('a')", self));
	}

	@Test
	void lastIndexOf_notFound() throws OclParseException {
		assertEquals(0, eval("'test'.lastIndexOf('xyzzy')", self));
	}

	@Test
	void lastIndexOf_notFound_partial() throws OclParseException {
		assertEquals(0, eval("'test'.lastIndexOf('est2')", self));
	}

	@Test
	void lastIndexOf_emptySubstring() throws OclParseException {
		// Empty string is found at end: size + 1 in 1-based
		assertEquals(5, eval("'test'.lastIndexOf('')", self));
	}

	@Test
	void lastIndexOf_emptyInEmpty() throws OclParseException {
		assertEquals(1, eval("''.lastIndexOf('')", self));
	}

	@Test
	void lastIndexOf_notFoundInEmpty() throws OclParseException {
		assertEquals(0, eval("''.lastIndexOf('t')", self));
	}

	// --- characters ---

	@Test
	void characters_size() throws OclParseException {
		assertEquals(3, eval("'abc'.characters()->size()", self));
	}

	@Test
	void characters_first() throws OclParseException {
		assertEquals("a", eval("'abc'.characters()->first()", self));
	}

	@Test
	void characters_last() throws OclParseException {
		assertEquals("c", eval("'abc'.characters()->last()", self));
	}

	@Test
	void characters_at() throws OclParseException {
		assertEquals("b", eval("'abc'.characters()->at(2)", self));
	}

	// --- String + operator ---

	@Test
	void stringPlus_concat() throws OclParseException {
		assertEquals("helloworld", eval("'hello' + 'world'", self));
	}

	@Test
	void stringPlus_withSpace() throws OclParseException {
		assertEquals("hello world", eval("'hello' + ' ' + 'world'", self));
	}

	// --- Chained string operations ---

	@Test
	void chainedOps_substringThenSize() throws OclParseException {
		assertEquals(3, eval("'hello'.substring(1, 3).size()", self));
	}

	@Test
	void chainedOps_concatThenSubstring() throws OclParseException {
		assertEquals("low", eval("'helloworld'.substring(4, 6)", self));
	}

	@Test
	void chainedOps_upperThenSubstring() throws OclParseException {
		assertEquals("HEL", eval("'hello'.toUpperCase().substring(1, 3)", self));
	}

	// --- matches ---

	@Test
	void matches_true() throws OclParseException {
		assertEquals(true, eval("'hello123'.matches('[a-z]+[0-9]+')", self));
	}

	@Test
	void matches_false() throws OclParseException {
		assertEquals(false, eval("'hello'.matches('[0-9]+')", self));
	}

	@Test
	void matches_fullMatch() throws OclParseException {
		assertEquals(true, eval("'42'.matches('[0-9]+')", self));
	}

	// --- Comparison with property access ---

	@Test
	void stringFromProperty_size() throws OclParseException {
		assertEquals(5, eval("self.name.size()", self));
	}

	@Test
	void stringFromProperty_toUpper() throws OclParseException {
		assertEquals("ALICE", eval("self.name.toUpperCase()", self));
	}

	@Test
	void stringFromProperty_substring() throws OclParseException {
		assertEquals("Ali", eval("self.name.substring(1, 3)", self));
	}
}
