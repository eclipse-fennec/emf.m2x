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
 * Edge case tests for string substring() operation.
 * OCL substring indices are 1-based (not 0-based).
 */
class OclSubstringEdgeCaseTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Basic substring ---

	@Test
	void substring_wholeString() throws OclParseException {
		assertEquals("hello", eval("'hello'.substring(1, 5)", self));
	}

	@Test
	void substring_firstChar() throws OclParseException {
		assertEquals("h", eval("'hello'.substring(1, 1)", self));
	}

	@Test
	void substring_lastChar() throws OclParseException {
		assertEquals("o", eval("'hello'.substring(5, 5)", self));
	}

	@Test
	void substring_middle() throws OclParseException {
		assertEquals("ell", eval("'hello'.substring(2, 4)", self));
	}

	@Test
	void substring_twoChars() throws OclParseException {
		assertEquals("he", eval("'hello'.substring(1, 2)", self));
	}

	@Test
	void substring_lastTwo() throws OclParseException {
		assertEquals("lo", eval("'hello'.substring(4, 5)", self));
	}

	// --- Substring with model data ---

	@Test
	void substring_property_firstThree() throws OclParseException {
		assertEquals("Ali", eval("self.name.substring(1, 3)", self));
	}

	@Test
	void substring_property_lastThree() throws OclParseException {
		assertEquals("ice", eval("self.name.substring(3, 5)", self));
	}

	// --- Substring then operations ---

	@Test
	void substring_thenSize() throws OclParseException {
		assertEquals(3, eval("'hello'.substring(1, 3).size()", self));
	}

	@Test
	void substring_thenUpperCase() throws OclParseException {
		assertEquals("HEL", eval("'hello'.substring(1, 3).toUpperCase()", self));
	}

	@Test
	void substring_thenConcat() throws OclParseException {
		assertEquals("helo", eval("'hello'.substring(1, 3).concat('o')", self));
	}

	@Test
	void substring_thenEquals() throws OclParseException {
		assertEquals(true, eval("'hello'.substring(1, 3) = 'hel'", self));
	}

	// --- Substring in expressions ---

	@Test
	void substring_inIf() throws OclParseException {
		assertEquals("starts with A", eval(
				"if self.name.substring(1, 1) = 'A' then 'starts with A' else 'other' endif",
				self));
	}

	@Test
	void substring_inLet() throws OclParseException {
		assertEquals("Ali", eval(
				"let prefix : String = self.name.substring(1, 3) in prefix", self));
	}

	// --- Substring with computed indices ---

	@Test
	void substring_computedEnd() throws OclParseException {
		// substring from 1 to name.size() = whole name
		assertEquals("Alice", eval("self.name.substring(1, self.name.size())", self));
	}

	// --- Single character string ---

	@Test
	void substring_singleCharString() throws OclParseException {
		assertEquals("x", eval("'x'.substring(1, 1)", self));
	}

	// --- Chained substrings ---

	@Test
	void substring_chained() throws OclParseException {
		// "hello".substring(2,5) = "ello", then .substring(1,3) = "ell"
		assertEquals("ell", eval("'hello'.substring(2, 5).substring(1, 3)", self));
	}

	// --- Substring in collection ---

	@Test
	void substring_inCollect() throws OclParseException {
		EObject bob = createPerson("Bob", 25, 40000.0, false);
		EObject company = createCompany("ACME", self, bob);
		Object result = eval(
				"self.employees->collect(e | e.name.substring(1, 1))", company);
		assertEquals(java.util.List.of("A", "B"), result);
	}

	// --- Substring comparison ---

	@Test
	void substring_equality() throws OclParseException {
		assertEquals(true, eval("'hello'.substring(1, 2) = 'he'", self));
	}

	@Test
	void substring_inequality() throws OclParseException {
		assertEquals(true, eval("'hello'.substring(1, 2) <> 'ho'", self));
	}
}
