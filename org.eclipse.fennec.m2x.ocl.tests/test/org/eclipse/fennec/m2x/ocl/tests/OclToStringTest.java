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
 * Tests for the toString() operation on various types.
 */
class OclToStringTest extends AbstractOclTest {

	static EObject self;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
		company = createCompany("ACME", self);
	}

	// --- Integer toString ---

	@Test
	void intToString() throws OclParseException {
		assertEquals("42", eval("42.toString()", self));
	}

	@Test
	void intToString_zero() throws OclParseException {
		assertEquals("0", eval("0.toString()", self));
	}

	@Test
	void intToString_negative() throws OclParseException {
		assertEquals("-5", eval("(-5).toString()", self));
	}

	// --- Real toString ---

	@Test
	void realToString() throws OclParseException {
		assertEquals("3.14", eval("3.14.toString()", self));
	}

	@Test
	void realToString_wholeNumber() throws OclParseException {
		assertEquals("5.0", eval("5.0.toString()", self));
	}

	// --- Boolean toString ---

	@Test
	void boolToString_true() throws OclParseException {
		assertEquals("true", eval("true.toString()", self));
	}

	@Test
	void boolToString_false() throws OclParseException {
		assertEquals("false", eval("false.toString()", self));
	}

	// --- String toString ---

	@Test
	void stringToString() throws OclParseException {
		assertEquals("hello", eval("'hello'.toString()", self));
	}

	// --- Property then toString ---

	@Test
	void ageToString() throws OclParseException {
		assertEquals("30", eval("self.age.toString()", self));
	}

	@Test
	void nameToString() throws OclParseException {
		assertEquals("Alice", eval("self.name.toString()", self));
	}

	// --- toString in concat ---

	@Test
	void toStringInConcat() throws OclParseException {
		assertEquals("Age: 30", eval(
				"'Age: '.concat(self.age.toString())", self));
	}

	@Test
	void toStringInConcat_salary() throws OclParseException {
		assertEquals("Salary: 50000.0", eval(
				"'Salary: '.concat(self.salary.toString())", self));
	}

	// --- toString on computed values ---

	@Test
	void toStringOnArithmetic() throws OclParseException {
		assertEquals("15", eval("(3 * 5).toString()", self));
	}

	@Test
	void toStringOnCollectionSize() throws OclParseException {
		assertEquals("1", eval("self.employees->size().toString()", company));
	}

	// --- toString in collect ---

	@Test
	void collectToString() throws OclParseException {
		assertEquals(java.util.List.of("Alice"), eval(
				"self.employees->collect(e | e.name.toString())", company));
	}

	// --- Comparison with toString result ---

	@Test
	void toStringEquality() throws OclParseException {
		assertEquals(true, eval("42.toString() = '42'", self));
	}

	@Test
	void toStringNotEqual() throws OclParseException {
		assertEquals(true, eval("42.toString() <> '43'", self));
	}
}
