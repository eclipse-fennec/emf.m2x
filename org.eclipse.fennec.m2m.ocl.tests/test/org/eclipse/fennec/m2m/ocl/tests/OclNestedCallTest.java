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
 * Tests for nested operation calls, where the result of one
 * operation is used as an argument to another.
 */
class OclNestedCallTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Nested max/min ---

	@Test
	void nestedMax() throws OclParseException {
		// max(3, max(5, 2)) = max(3, 5) = 5
		assertEquals(5, eval("3.max(5.max(2))", self));
	}

	@Test
	void nestedMin() throws OclParseException {
		assertEquals(1, eval("3.min(1.min(5))", self));
	}

	@Test
	void maxOfMin() throws OclParseException {
		// max(min(3,5), min(7,2)) = max(3, 2) = 3
		assertEquals(3, eval("3.min(5).max(7.min(2))", self));
	}

	// --- Nested abs ---

	@Test
	void absOfNegation() throws OclParseException {
		assertEquals(5, eval("(-5).abs()", self));
	}

	@Test
	void negOfAbs() throws OclParseException {
		assertEquals(-5, eval("-(5.abs())", self));
	}

	// --- Nested string operations ---

	@Test
	void concat_of_substrings() throws OclParseException {
		// substring(1,3) = "hel", concat("lo") = "hello"
		assertEquals("hello", eval(
				"'hello'.substring(1, 3).concat('lo')", self));
	}

	@Test
	void size_of_toUpper() throws OclParseException {
		assertEquals(5, eval("'hello'.toUpperCase().size()", self));
	}

	@Test
	void toUpper_of_concat() throws OclParseException {
		assertEquals("HELLOWORLD", eval(
				"'hello'.concat('world').toUpperCase()", self));
	}

	@Test
	void substring_of_toUpper() throws OclParseException {
		assertEquals("HEL", eval("'hello'.toUpperCase().substring(1, 3)", self));
	}

	// --- Nested div/mod ---

	@Test
	void modOfDiv() throws OclParseException {
		// 17.div(5) = 3, 3.mod(2) = 1
		assertEquals(1, eval("17.div(5).mod(2)", self));
	}

	@Test
	void divOfMod() throws OclParseException {
		// 17.mod(5) = 2, 2.div(1) = 2
		assertEquals(2, eval("17.mod(5).div(1)", self));
	}

	// --- Nested floor/ceiling ---

	@Test
	void floorOfDivision() throws OclParseException {
		// 7/2 = 3.5, floor = 3
		assertEquals(3, eval("(7 / 2).floor()", self));
	}

	@Test
	void ceilingOfDivision() throws OclParseException {
		// 7/2 = 3.5, ceiling = 4
		assertEquals(4, eval("(7 / 2).ceiling()", self));
	}

	// --- Nested collection operations ---

	@Test
	void sizeOfSelect() throws OclParseException {
		assertEquals(3, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i > 2)->size()", self));
	}

	@Test
	void sumOfCollect() throws OclParseException {
		// collect doubles, sum = 2+4+6 = 12
		assertEquals(12, eval(
				"Sequence{1, 2, 3}->collect(i | i * 2)->sum()", self));
	}

	@Test
	void maxOfSelect() throws OclParseException {
		assertEquals(5, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i > 2)->max()", self));
	}

	@Test
	void firstOfSortedBy() throws OclParseException {
		assertEquals(1, eval(
				"Sequence{3, 1, 2}->sortedBy(i | i)->first()", self));
	}

	// --- Nested toString/toInteger ---

	@Test
	void toStringThenSize() throws OclParseException {
		assertEquals(5, eval("12345.toString().size()", self));
	}

	@Test
	void toIntegerThenArithmetic() throws OclParseException {
		assertEquals(52, eval("'42'.toInteger() + 10", self));
	}

	@Test
	void toStringThenConcat() throws OclParseException {
		assertEquals("age: 30", eval("'age: ' + self.age.toString()", self));
	}

	// --- Nested with property ---

	@Test
	void property_thenToString_thenSize() throws OclParseException {
		assertEquals(5, eval("self.name.size()", self));
	}

	@Test
	void property_thenMax() throws OclParseException {
		assertEquals(30, eval("self.age.max(25)", self));
	}

	@Test
	void property_thenMin() throws OclParseException {
		assertEquals(25, eval("self.age.min(25)", self));
	}

	// --- Deeply chained ---

	@Test
	void deepChain_stringOps() throws OclParseException {
		assertEquals("HEL", eval(
				"'  hello  '.trim().toUpperCase().substring(1, 3)", self));
	}

	@Test
	void deepChain_numericOps() throws OclParseException {
		// (-7).abs() = 7, 7.max(3) = 7, 7.min(10) = 7
		assertEquals(7, eval("(-7).abs().max(3).min(10)", self));
	}
}
