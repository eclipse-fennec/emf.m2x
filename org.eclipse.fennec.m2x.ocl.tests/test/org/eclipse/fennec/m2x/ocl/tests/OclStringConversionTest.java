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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL string/number/boolean conversion operations.
 * Covers toString(), toInteger(), toReal(), toBoolean() and
 * their edge cases.
 */
class OclStringConversionTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- toString ---

	@Test
	void integerToString() throws OclParseException {
		assertEquals("42", eval("42.toString()", self));
	}

	@Test
	void negativeIntegerToString() throws OclParseException {
		assertEquals("-42", eval("(-42).toString()", self));
	}

	@Test
	void zeroToString() throws OclParseException {
		assertEquals("0", eval("0.toString()", self));
	}

	@Test
	void realToString() throws OclParseException {
		Object result = eval("(3.14).toString()", self);
		assertInstanceOf(String.class, result);
		// Just verify it's a string representation of the number
		assertEquals(true, ((String) result).contains("3.14"));
	}

	@Test
	void booleanTrueToString() throws OclParseException {
		assertEquals("true", eval("true.toString()", self));
	}

	@Test
	void booleanFalseToString() throws OclParseException {
		assertEquals("false", eval("false.toString()", self));
	}

	@Test
	void stringToString() throws OclParseException {
		assertEquals("hello", eval("'hello'.toString()", self));
	}

	// --- toInteger ---

	@Test
	void stringToInteger() throws OclParseException {
		Object result = eval("'42'.toInteger()", self);
		assertInstanceOf(Number.class, result);
		assertEquals(42, result);
	}

	@Test
	void negativeStringToInteger() throws OclParseException {
		Object result = eval("'-42'.toInteger()", self);
		assertInstanceOf(Number.class, result);
		assertEquals(-42, result);
	}

	@Test
	void zeroStringToInteger() throws OclParseException {
		Object result = eval("'0'.toInteger()", self);
		assertInstanceOf(Number.class, result);
		assertEquals(0, result);
	}

	@Test
	void realToInteger_truncates() throws OclParseException {
		Object result = eval("(3.7).toInteger()", self);
		assertInstanceOf(Number.class, result);
		assertEquals(3, result);
	}

	@Test
	void negativeRealToInteger_truncates() throws OclParseException {
		Object result = eval("(-3.7).toInteger()", self);
		assertInstanceOf(Number.class, result);
		assertEquals(-3, result);
	}

	// --- toReal ---

	@Test
	void stringToReal() throws OclParseException {
		Object result = eval("'3.14'.toReal()", self);
		assertInstanceOf(Double.class, result);
		assertEquals(3.14, result);
	}

	@Test
	void integerStringToReal() throws OclParseException {
		Object result = eval("'42'.toReal()", self);
		assertInstanceOf(Double.class, result);
		assertEquals(42.0, result);
	}

	@Test
	void integerToReal() throws OclParseException {
		Object result = eval("42.toReal()", self);
		assertInstanceOf(Double.class, result);
		assertEquals(42.0, result);
	}

	@Test
	void negativeStringToReal() throws OclParseException {
		Object result = eval("'-2.5'.toReal()", self);
		assertInstanceOf(Double.class, result);
		assertEquals(-2.5, result);
	}

	// --- toBoolean ---

	@Test
	void stringToBoolean_true() throws OclParseException {
		assertEquals(true, eval("'true'.toBoolean()", self));
	}

	@Test
	void stringToBoolean_false() throws OclParseException {
		assertEquals(false, eval("'false'.toBoolean()", self));
	}

	// --- Chained conversions ---

	@Test
	void intToString_thenSize() throws OclParseException {
		assertEquals(3, eval("123.toString().size()", self));
	}

	@Test
	void intToString_thenConcat() throws OclParseException {
		assertEquals("Value: 42", eval("'Value: ' + 42.toString()", self));
	}

	@Test
	void stringToInt_thenArithmetic() throws OclParseException {
		assertEquals(52, eval("'42'.toInteger() + 10", self));
	}

	@Test
	void stringToReal_thenArithmetic() throws OclParseException {
		assertEquals(13.14, eval("'3.14'.toReal() + 10.0", self));
	}

	// --- Conversion in collection context ---

	@Test
	void collect_toString() throws OclParseException {
		Object result = eval(
				"Sequence{1, 2, 3}->collect(i | i.toString())", self);
		assertInstanceOf(java.util.List.class, result);
		java.util.List<?> list = (java.util.List<?>) result;
		assertEquals("1", list.get(0));
		assertEquals("2", list.get(1));
		assertEquals("3", list.get(2));
	}

	@Test
	void collect_toStringConcat() throws OclParseException {
		assertEquals("1,2,3", eval(
				"Sequence{1, 2, 3}->iterate(i; acc: String = '' | " +
				"if acc = '' then i.toString() else acc + ',' + i.toString() endif)",
				self));
	}

	// --- Property to string ---

	@Test
	void propertyToString() throws OclParseException {
		assertEquals("30", eval("self.age.toString()", self));
	}

	@Test
	void propertyConcat() throws OclParseException {
		assertEquals("Alice is 30", eval(
				"self.name + ' is ' + self.age.toString()", self));
	}
}
