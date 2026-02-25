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
 * Tests for string operations: matches(), characters(),
 * and less commonly tested string functions.
 */
class OclStringMatchesCharsTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- matches() ---

	@Test
	void matches_simplePattern() throws OclParseException {
		assertEquals(true, eval("'hello'.matches('hello')", self));
	}

	@Test
	void matches_regex_letterPattern() throws OclParseException {
		assertEquals(true, eval("'Alice'.matches('[A-Z][a-z]+')", self));
	}

	@Test
	void matches_regex_noMatch() throws OclParseException {
		assertEquals(false, eval("'alice'.matches('[A-Z][a-z]+')", self));
	}

	@Test
	void matches_digits() throws OclParseException {
		assertEquals(true, eval("'12345'.matches('[0-9]+')", self));
	}

	@Test
	void matches_digits_noMatch() throws OclParseException {
		assertEquals(false, eval("'hello'.matches('[0-9]+')", self));
	}

	@Test
	void matches_dotStar() throws OclParseException {
		assertEquals(true, eval("'anything'.matches('.*')", self));
	}

	@Test
	void matches_emptyString() throws OclParseException {
		assertEquals(true, eval("''.matches('')", self));
	}

	@Test
	void matches_withProperty() throws OclParseException {
		assertEquals(true, eval("self.name.matches('[A-Z][a-z]+')", self));
	}

	// --- characters() ---

	@Test
	void characters_simple() throws OclParseException {
		Object result = eval("'abc'.characters()", self);
		assertEquals(List.of("a", "b", "c"), result);
	}

	@Test
	void characters_singleChar() throws OclParseException {
		assertEquals(List.of("x"), eval("'x'.characters()", self));
	}

	@Test
	void characters_empty() throws OclParseException {
		Object result = eval("''.characters()", self);
		assertInstanceOf(List.class, result);
		assertEquals(0, ((List<?>) result).size());
	}

	@Test
	void characters_withSpaces() throws OclParseException {
		assertEquals(List.of("a", " ", "b"), eval("'a b'.characters()", self));
	}

	@Test
	void characters_size() throws OclParseException {
		assertEquals(5, eval("'hello'.characters()->size()", self));
	}

	@Test
	void characters_first() throws OclParseException {
		assertEquals("h", eval("'hello'.characters()->first()", self));
	}

	@Test
	void characters_last() throws OclParseException {
		assertEquals("o", eval("'hello'.characters()->last()", self));
	}

	@Test
	void characters_includes() throws OclParseException {
		assertEquals(true, eval("'hello'.characters()->includes('l')", self));
	}

	@Test
	void characters_excludes() throws OclParseException {
		assertEquals(true, eval("'hello'.characters()->excludes('z')", self));
	}

	// --- characters with collection operations ---

	@Test
	void characters_asSet_removeDuplicates() throws OclParseException {
		// "hello" has duplicate 'l' → Set has 4 elements
		assertEquals(4, eval("'hello'.characters()->asSet()->size()", self));
	}

	@Test
	void characters_select_vowels() throws OclParseException {
		Object result = eval(
				"'hello'.characters()->select(c | c = 'e' or c = 'o')", self);
		assertEquals(List.of("e", "o"), result);
	}

	@Test
	void characters_count() throws OclParseException {
		assertEquals(2, eval("'hello'.characters()->count('l')", self));
	}

	// --- Property characters ---

	@Test
	void property_characters() throws OclParseException {
		assertEquals(List.of("A", "l", "i", "c", "e"),
				eval("self.name.characters()", self));
	}

	@Test
	void property_characters_first() throws OclParseException {
		assertEquals("A", eval("self.name.characters()->first()", self));
	}

	// --- matches in conditions ---

	@Test
	void matches_inIf() throws OclParseException {
		assertEquals("valid", eval(
				"if self.name.matches('[A-Z].*') then 'valid' else 'invalid' endif", self));
	}

	@Test
	void matches_inSelect() throws OclParseException {
		EObject bob = createPerson("Bob", 25, 40000.0, false);
		EObject carol = createPerson("carol", 35, 60000.0, true);
		EObject company = createCompany("ACME", self, bob, carol);
		// Select employees whose names start with uppercase
		assertEquals(2, eval(
				"self.employees->select(e | e.name.matches('[A-Z].*'))->size()", company));
	}
}
