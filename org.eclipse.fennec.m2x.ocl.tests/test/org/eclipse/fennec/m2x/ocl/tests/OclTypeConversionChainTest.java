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
 * Tests for type conversion chains: toReal(), toInteger(), toString(),
 * and combinations of these conversions.
 */
class OclTypeConversionChainTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- toReal ---

	@Test
	void intToReal() throws OclParseException {
		Object result = eval("42.toReal()", self);
		assertInstanceOf(Double.class, result);
		assertEquals(42.0, result);
	}

	@Test
	void intToReal_zero() throws OclParseException {
		assertEquals(0.0, eval("0.toReal()", self));
	}

	@Test
	void intToReal_negative() throws OclParseException {
		assertEquals(-10.0, eval("(-10).toReal()", self));
	}

	@Test
	void propertyToReal() throws OclParseException {
		Object result = eval("self.age.toReal()", self);
		assertInstanceOf(Double.class, result);
		assertEquals(30.0, result);
	}

	// --- toInteger ---

	@Test
	void realToInteger_truncates() throws OclParseException {
		Object result = eval("3.7.toInteger()", self);
		assertInstanceOf(Number.class, result);
		assertEquals(3, ((Number) result).intValue());
	}

	@Test
	void realToInteger_exact() throws OclParseException {
		Object result = eval("5.0.toInteger()", self);
		assertInstanceOf(Number.class, result);
		assertEquals(5, ((Number) result).intValue());
	}

	@Test
	void realToInteger_negative() throws OclParseException {
		// OCL: toInteger() truncates toward zero
		Object result = eval("(-3.7).toInteger()", self);
		assertInstanceOf(Number.class, result);
		assertEquals(-3, ((Number) result).intValue());
	}

	@Test
	void realToInteger_zero() throws OclParseException {
		Object result = eval("0.0.toInteger()", self);
		assertInstanceOf(Number.class, result);
		assertEquals(0, ((Number) result).intValue());
	}

	// --- toString ---

	@Test
	void intToString() throws OclParseException {
		assertEquals("42", eval("42.toString()", self));
	}

	@Test
	void intToString_negative() throws OclParseException {
		assertEquals("-10", eval("(-10).toString()", self));
	}

	@Test
	void intToString_zero() throws OclParseException {
		assertEquals("0", eval("0.toString()", self));
	}

	@Test
	void boolToString_true() throws OclParseException {
		assertEquals("true", eval("true.toString()", self));
	}

	@Test
	void boolToString_false() throws OclParseException {
		assertEquals("false", eval("false.toString()", self));
	}

	@Test
	void realToString() throws OclParseException {
		String result = (String) eval("3.14.toString()", self);
		assertEquals("3.14", result);
	}

	@Test
	void propertyToString() throws OclParseException {
		assertEquals("30", eval("self.age.toString()", self));
	}

	// --- Conversion chains ---

	@Test
	void intToReal_thenArithmetic() throws OclParseException {
		Object result = eval("42.toReal() + 0.5", self);
		assertInstanceOf(Double.class, result);
		assertEquals(42.5, result);
	}

	@Test
	void intToReal_thenToInteger() throws OclParseException {
		Object result = eval("42.toReal().toInteger()", self);
		assertInstanceOf(Number.class, result);
		assertEquals(42, ((Number) result).intValue());
	}

	@Test
	void intToString_thenSize() throws OclParseException {
		assertEquals(2, eval("42.toString().size()", self));
	}

	@Test
	void intToString_thenConcat() throws OclParseException {
		assertEquals("age: 30", eval("'age: '.concat(self.age.toString())", self));
	}

	// --- String to number ---

	@Test
	void stringToInteger() throws OclParseException {
		Object result = eval("'42'.toInteger()", self);
		assertInstanceOf(Number.class, result);
		assertEquals(42, ((Number) result).intValue());
	}

	@Test
	void stringToReal() throws OclParseException {
		Object result = eval("'3.14'.toReal()", self);
		assertInstanceOf(Double.class, result);
		assertEquals(3.14, (Double) result, 0.001);
	}

	// --- Conversion in expressions ---

	@Test
	void toReal_inComparison() throws OclParseException {
		assertEquals(true, eval("self.age.toReal() > 29.5", self));
	}

	@Test
	void toString_inEquality() throws OclParseException {
		assertEquals(true, eval("self.age.toString() = '30'", self));
	}

	@Test
	void toString_inConcat() throws OclParseException {
		assertEquals("Alice is 30 years old", eval(
				"self.name.concat(' is ').concat(self.age.toString()).concat(' years old')", self));
	}

	// --- Conversion in iterators ---

	@Test
	void toReal_inCollect() throws OclParseException {
		assertEquals(java.util.List.of(1.0, 2.0, 3.0),
				eval("Sequence{1, 2, 3}->collect(x | x.toReal())", self));
	}

	@Test
	void toString_inCollect() throws OclParseException {
		assertEquals(java.util.List.of("1", "2", "3"),
				eval("Sequence{1, 2, 3}->collect(x | x.toString())", self));
	}

	// --- Conversion in let ---

	@Test
	void conversion_inLet() throws OclParseException {
		assertEquals(42.5, eval(
				"let x : Real = 42.toReal() in x + 0.5", self));
	}

	// --- Boolean conversion ---

	@Test
	void booleanToString_inIf() throws OclParseException {
		assertEquals("married: true", eval(
				"'married: '.concat(self.isMarried.toString())", self));
	}
}
