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
 * Tests for new string operations: replaceAll, replaceFirst,
 * equalsIgnoreCase, startsWith, endsWith.
 */
class OclStringNewOpsTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- replaceAll ---

	@Test
	void replaceAll_singleOccurrence() throws OclParseException {
		assertEquals("Hello World", eval("'Hello OCL'.replaceAll('OCL', 'World')", self));
	}

	@Test
	void replaceAll_multipleOccurrences() throws OclParseException {
		assertEquals("b-b-b", eval("'a-a-a'.replaceAll('a', 'b')", self));
	}

	@Test
	void replaceAll_noMatch() throws OclParseException {
		assertEquals("hello", eval("'hello'.replaceAll('xyz', 'abc')", self));
	}

	@Test
	void replaceAll_emptyReplacement() throws OclParseException {
		assertEquals("hll", eval("'hello'.replaceAll('e|o', '')", self));
	}

	@Test
	void replaceAll_regex() throws OclParseException {
		assertEquals("X-X-X", eval("'1-2-3'.replaceAll('[0-9]', 'X')", self));
	}

	// --- replaceFirst ---

	@Test
	void replaceFirst_replacesOnlyFirst() throws OclParseException {
		assertEquals("b-a-a", eval("'a-a-a'.replaceFirst('a', 'b')", self));
	}

	@Test
	void replaceFirst_noMatch() throws OclParseException {
		assertEquals("hello", eval("'hello'.replaceFirst('xyz', 'abc')", self));
	}

	@Test
	void replaceFirst_regex() throws OclParseException {
		assertEquals("X-2-3", eval("'1-2-3'.replaceFirst('[0-9]', 'X')", self));
	}

	// --- equalsIgnoreCase ---

	@Test
	void equalsIgnoreCase_sameCaseTrue() throws OclParseException {
		assertEquals(true, eval("'hello'.equalsIgnoreCase('hello')", self));
	}

	@Test
	void equalsIgnoreCase_differentCaseTrue() throws OclParseException {
		assertEquals(true, eval("'Hello'.equalsIgnoreCase('hELLO')", self));
	}

	@Test
	void equalsIgnoreCase_false() throws OclParseException {
		assertEquals(false, eval("'Hello'.equalsIgnoreCase('World')", self));
	}

	@Test
	void equalsIgnoreCase_empty() throws OclParseException {
		assertEquals(true, eval("''.equalsIgnoreCase('')", self));
	}

	// --- startsWith ---

	@Test
	void startsWith_true() throws OclParseException {
		assertEquals(true, eval("'Hello World'.startsWith('Hello')", self));
	}

	@Test
	void startsWith_false() throws OclParseException {
		assertEquals(false, eval("'Hello World'.startsWith('World')", self));
	}

	@Test
	void startsWith_emptyPrefix() throws OclParseException {
		assertEquals(true, eval("'Hello'.startsWith('')", self));
	}

	@Test
	void startsWith_exactMatch() throws OclParseException {
		assertEquals(true, eval("'Hello'.startsWith('Hello')", self));
	}

	@Test
	void startsWith_longerPrefix() throws OclParseException {
		assertEquals(false, eval("'Hi'.startsWith('Hello')", self));
	}

	// --- endsWith ---

	@Test
	void endsWith_true() throws OclParseException {
		assertEquals(true, eval("'Hello World'.endsWith('World')", self));
	}

	@Test
	void endsWith_false() throws OclParseException {
		assertEquals(false, eval("'Hello World'.endsWith('Hello')", self));
	}

	@Test
	void endsWith_emptySuffix() throws OclParseException {
		assertEquals(true, eval("'Hello'.endsWith('')", self));
	}

	@Test
	void endsWith_exactMatch() throws OclParseException {
		assertEquals(true, eval("'Hello'.endsWith('Hello')", self));
	}

	@Test
	void endsWith_longerSuffix() throws OclParseException {
		assertEquals(false, eval("'Hi'.endsWith('Hello')", self));
	}

	// --- Combined with model properties ---

	@Test
	void startsWith_onProperty() throws OclParseException {
		assertEquals(true, eval("self.name.startsWith('Al')", self));
	}

	@Test
	void endsWith_onProperty() throws OclParseException {
		assertEquals(true, eval("self.name.endsWith('ice')", self));
	}

	@Test
	void replaceAll_onProperty() throws OclParseException {
		assertEquals("Alyce", eval("self.name.replaceAll('ic', 'yc')", self));
	}

	@Test
	void equalsIgnoreCase_onProperty() throws OclParseException {
		assertEquals(true, eval("self.name.equalsIgnoreCase('ALICE')", self));
	}
}
