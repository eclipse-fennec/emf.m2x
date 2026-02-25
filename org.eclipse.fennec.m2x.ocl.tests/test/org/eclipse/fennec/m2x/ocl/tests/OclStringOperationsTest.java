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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL String operations.
 * Note: OCL uses 1-based indexing for String operations.
 */
class OclStringOperationsTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	@Test
	void size() throws OclParseException {
		assertEquals(5, eval("'hello'.size()", self));
	}

	@Test
	void size_empty() throws OclParseException {
		assertEquals(0, eval("''.size()", self));
	}

	@Test
	void concat() throws OclParseException {
		assertEquals("helloworld", eval("'hello'.concat('world')", self));
	}

	@Test
	void concat_operator() throws OclParseException {
		assertEquals("helloworld", eval("'hello' + 'world'", self));
	}

	@Test
	void substring() throws OclParseException {
		// OCL substring is 1-based, inclusive on both ends
		assertEquals("ell", eval("'hello'.substring(2, 4)", self));
	}

	@Test
	void substring_single() throws OclParseException {
		assertEquals("h", eval("'hello'.substring(1, 1)", self));
	}

	@Test
	void substring_outOfBounds_returnsInvalid() throws OclParseException {
		assertInvalid("'hello'.substring(1, 10)", self);
	}

	@Test
	void toUpperCase() throws OclParseException {
		assertEquals("HELLO", eval("'hello'.toUpperCase()", self));
	}

	@Test
	void toLowerCase() throws OclParseException {
		assertEquals("hello", eval("'HELLO'.toLowerCase()", self));
	}

	@Test
	void toUpper_synonym() throws OclParseException {
		assertEquals("HELLO", eval("'hello'.toUpper()", self));
	}

	@Test
	void toLower_synonym() throws OclParseException {
		assertEquals("hello", eval("'HELLO'.toLower()", self));
	}

	@Test
	void trim() throws OclParseException {
		assertEquals("hello", eval("'  hello  '.trim()", self));
	}

	@Test
	void indexOf() throws OclParseException {
		// OCL indexOf returns 1-based position, invalid if not found
		assertEquals(3, eval("'hello'.indexOf('llo')", self));
	}

	@Test
	void indexOf_notFound() throws OclParseException {
		// OCL v2.4 §11.5.3: "or zero if s is not a substring of self"
		assertEquals(0, eval("'hello'.indexOf('xyz')", self));
	}

	@Test
	void at() throws OclParseException {
		// OCL at() is 1-based
		assertEquals("h", eval("'hello'.at(1)", self));
	}

	@Test
	void at_last() throws OclParseException {
		assertEquals("o", eval("'hello'.at(5)", self));
	}

	@Test
	void at_outOfBounds_returnsInvalid() throws OclParseException {
		assertInvalid("'hello'.at(0)", self);
	}

	@Test
	void characters() throws OclParseException {
		Object result = eval("'abc'.characters()", self);
		assertInstanceOf(List.class, result);
		assertEquals(List.of("a", "b", "c"), result);
	}

	@Test
	void toInteger() throws OclParseException {
		assertEquals(42, eval("'42'.toInteger()", self));
	}

	@Test
	void toReal() throws OclParseException {
		assertEquals(3.14, eval("'3.14'.toReal()", self));
	}

	@Test
	void toBoolean_true() throws OclParseException {
		assertEquals(true, eval("'true'.toBoolean()", self));
	}

	@Test
	void toBoolean_false() throws OclParseException {
		assertEquals(false, eval("'false'.toBoolean()", self));
	}

	@Test
	void matches() throws OclParseException {
		assertEquals(true, eval("'hello123'.matches('.*[0-9]+')", self));
	}

	@Test
	void matches_noMatch() throws OclParseException {
		assertEquals(false, eval("'hello'.matches('[0-9]+')", self));
	}

	@Test
	void matches_invalidRegex_returnsInvalid() throws OclParseException {
		assertInvalid("'hello'.matches('[invalid')", self);
	}

	// --- substituteAll (Eclipse extension, literal non-regex replacement) ---

	@Test
	void substituteAll_basic() throws OclParseException {
		assertEquals("subsTiTuTeAll operaTion",
				eval("'substituteAll operation'.substituteAll('t', 'T')", self));
	}

	@Test
	void substituteAll_notFound() throws OclParseException {
		// Target not found → original string returned
		assertEquals("hello", eval("'hello'.substituteAll('xyz', 'abc')", self));
	}

	@Test
	void substituteAll_regexTreatedAsLiteral() throws OclParseException {
		// Regex metacharacters treated as literal text
		assertEquals("repla ce operation",
				eval("'repla ce operation'.substituteAll('(\\\\w+)\\\\s*', '')", self));
	}

	@Test
	void substituteAll_emptyTarget() throws OclParseException {
		// Empty target inserts replacement between every character
		assertEquals("xxrxxexxpxx", eval("'rep'.substituteAll('', 'xx')", self));
	}

	@Test
	void substituteAll_emptyReplacement() throws OclParseException {
		assertEquals("hll", eval("'hello'.substituteAll('e', '').substituteAll('o', '')", self));
	}

	// --- substituteFirst (Eclipse extension, literal non-regex replacement) ---

	@Test
	void substituteFirst_basic() throws OclParseException {
		assertEquals("Hello world",
				eval("'hello world'.substituteFirst('h', 'H')", self));
	}

	@Test
	void substituteFirst_onlyFirst() throws OclParseException {
		// Only replaces FIRST occurrence
		assertEquals("Test", eval("'test'.substituteFirst('t', 'T')", self));
	}

	@Test
	void substituteFirst_multiChar() throws OclParseException {
		assertEquals("hXYZlo", eval("'hello'.substituteFirst('el', 'XYZ')", self));
	}

	@Test
	void substituteFirst_notFound() throws OclParseException {
		// Target not found → invalid
		assertInvalid("'hello'.substituteFirst('xyz', 'abc')", self);
	}

	@Test
	void substituteFirst_emptyTarget() throws OclParseException {
		// Empty target found at start → inserts replacement at beginning
		assertEquals("xxhello", eval("'hello'.substituteFirst('', 'xx')", self));
	}

	// --- tokenize (Eclipse extension, StringTokenizer-like) ---

	@Test
	void tokenize_defaultDelimiters() throws OclParseException {
		// Default: whitespace delimiters (space, tab, newline, CR, FF)
		assertEquals(List.of("a", "b", "c", "d"),
				eval("'\\na b\\tc\\fd\\r'.tokenize()", self));
	}

	@Test
	void tokenize_defaultDelimiters_leadingTrailing() throws OclParseException {
		assertEquals(List.of("a", "b", "c", "d"),
				eval("' \\t\\n\\r\\fa b\\tc\\fd \\t\\n\\r\\f'.tokenize()", self));
	}

	@Test
	void tokenize_customDelimiter_spaceOnly() throws OclParseException {
		// Only space as delimiter — newlines/tabs stay in tokens
		assertEquals(List.of("\na", "b\tc\fd\r"),
				eval("'\\na b\\tc\\fd\\r'.tokenize(' ')", self));
	}

	@Test
	void tokenize_dotDelimiter() throws OclParseException {
		assertEquals(List.of("1", "2", "3", "4"),
				eval("'1.2.3.4'.tokenize('.')", self));
	}

	@Test
	void tokenize_returnDelims_true() throws OclParseException {
		// returnDelims=true includes delimiter tokens in result
		assertEquals(List.of("\na", " ", "b\tc\fd\r"),
				eval("'\\na b\\tc\\fd\\r'.tokenize(' ', true)", self));
	}

	@Test
	void tokenize_emptyString() throws OclParseException {
		assertEquals(List.of(), eval("''.tokenize(' ', true)", self));
	}

	@Test
	void tokenize_emptyDelimiter() throws OclParseException {
		// Empty delimiter: entire string is one token
		assertEquals(List.of(" \t\n\r\f"),
				eval("' \\t\\n\\r\\f'.tokenize('', true)", self));
	}

	@Test
	void tokenize_emptyBoth() throws OclParseException {
		assertEquals(List.of(), eval("''.tokenize('', true)", self));
	}
}
