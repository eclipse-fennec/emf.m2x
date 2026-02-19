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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
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
		assertEquals(5L, eval("'hello'.size()", self));
	}

	@Test
	void size_empty() throws OclParseException {
		assertEquals(0L, eval("''.size()", self));
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
	void trim() throws OclParseException {
		assertEquals("hello", eval("'  hello  '.trim()", self));
	}

	@Test
	void indexOf() throws OclParseException {
		// OCL indexOf returns 1-based position, 0 if not found
		assertEquals(3L, eval("'hello'.indexOf('llo')", self));
	}

	@Test
	void indexOf_notFound() throws OclParseException {
		assertEquals(0L, eval("'hello'.indexOf('xyz')", self));
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
		assertEquals(42L, eval("'42'.toInteger()", self));
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
}
