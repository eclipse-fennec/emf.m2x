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
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for Integer/Long narrowing at the API boundary.
 *
 * <p>Internally the engine uses {@code Long} for all integer arithmetic.
 * At the API boundary ({@code evaluate()} return), values that fit in
 * {@code int} are narrowed to {@link Integer}. This test verifies:
 * <ul>
 *   <li>Simple integer literals return {@code Integer}</li>
 *   <li>Large values exceeding int range remain {@code Long}</li>
 *   <li>EMF EInt properties return {@code Integer}</li>
 *   <li>Collections, Sets, and Maps have their elements narrowed</li>
 *   <li>Doubles are not affected</li>
 * </ul>
 */
class OclNarrowingTest extends AbstractOclTest {

	static EObject self;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
		EObject bob = createPerson("Bob", 25, 40000.0, false);
		company = createCompany("ACME", self, bob);
	}

	// --- Simple integer literals ---

	@Test
	void integerLiteral_returnsInteger() throws OclParseException {
		Object result = eval("42", self);
		assertInstanceOf(Integer.class, result);
		assertEquals(42, result);
	}

	@Test
	void negativeLiteral_returnsInteger() throws OclParseException {
		Object result = eval("-10", self);
		assertInstanceOf(Integer.class, result);
		assertEquals(-10, result);
	}

	@Test
	void zero_returnsInteger() throws OclParseException {
		Object result = eval("0", self);
		assertInstanceOf(Integer.class, result);
		assertEquals(0, result);
	}

	@Test
	void maxInt_returnsInteger() throws OclParseException {
		Object result = eval("2147483647", self);
		assertInstanceOf(Integer.class, result);
		assertEquals(Integer.MAX_VALUE, result);
	}

	@Test
	void minInt_returnsInteger() throws OclParseException {
		Object result = eval("-2147483648", self);
		assertInstanceOf(Integer.class, result);
		assertEquals(Integer.MIN_VALUE, result);
	}

	// --- Large values stay Long ---

	@Test
	void beyondMaxInt_returnsLong() throws OclParseException {
		Object result = eval("2147483648", self);
		assertInstanceOf(Long.class, result);
		assertEquals(2147483648L, result);
	}

	@Test
	void beyondMinInt_returnsLong() throws OclParseException {
		Object result = eval("-2147483649", self);
		assertInstanceOf(Long.class, result);
		assertEquals(-2147483649L, result);
	}

	@Test
	void largeMultiplication_returnsLong() throws OclParseException {
		Object result = eval("1000000 * 1000000", self);
		assertInstanceOf(Long.class, result);
		assertEquals(1000000000000L, result);
	}

	// --- EMF EInt property ---

	@Test
	void eIntProperty_returnsInteger() throws OclParseException {
		Object result = eval("self.age", self);
		assertInstanceOf(Integer.class, result);
		assertEquals(30, result);
	}

	@Test
	void eIntProperty_arithmetic_returnsInteger() throws OclParseException {
		Object result = eval("self.age + 1", self);
		assertInstanceOf(Integer.class, result);
		assertEquals(31, result);
	}

	// --- Double not affected ---

	@Test
	void doubleLiteral_staysDouble() throws OclParseException {
		Object result = eval("3.14", self);
		assertInstanceOf(Double.class, result);
		assertEquals(3.14, result);
	}

	@Test
	void eSalary_staysDouble() throws OclParseException {
		Object result = eval("self.salary", self);
		assertInstanceOf(Double.class, result);
		assertEquals(50000.0, result);
	}

	@Test
	void division_alwaysDouble() throws OclParseException {
		Object result = eval("10 / 3", self);
		assertInstanceOf(Double.class, result);
	}

	// --- Collections narrowed ---

	@Test
	void sequenceLiteral_elementsNarrowed() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3}", self);
		assertInstanceOf(List.class, result);
		@SuppressWarnings("unchecked")
		List<Object> list = (List<Object>) result;
		assertEquals(3, list.size());
		for (Object elem : list) {
			assertInstanceOf(Integer.class, elem);
		}
		assertEquals(List.of(1, 2, 3), list);
	}

	@Test
	void setLiteral_elementsNarrowed() throws OclParseException {
		Object result = eval("Set{10, 20, 30}", self);
		assertInstanceOf(OclSet.class, result);
		@SuppressWarnings("unchecked")
		OclSet<Object> set = (OclSet<Object>) result;
		for (Object elem : set) {
			assertInstanceOf(Integer.class, elem);
		}
	}

	@Test
	void sequenceWithLargeValue_mixedTypes() throws OclParseException {
		// 3000000000 > Integer.MAX_VALUE → stays Long
		Object result = eval("Sequence{1, 3000000000}", self);
		assertInstanceOf(List.class, result);
		@SuppressWarnings("unchecked")
		List<Object> list = (List<Object>) result;
		assertInstanceOf(Integer.class, list.get(0));
		assertInstanceOf(Long.class, list.get(1));
	}

	// --- Map narrowed ---

	@Test
	void mapLiteral_valuesNarrowed() throws OclParseException {
		Object result = eval("Map{'a' with 1, 'b' with 2}", self);
		assertInstanceOf(Map.class, result);
		@SuppressWarnings("unchecked")
		Map<Object, Object> map = (Map<Object, Object>) result;
		for (Object val : map.values()) {
			assertInstanceOf(Integer.class, val);
		}
		assertEquals(1, map.get("a"));
		assertEquals(2, map.get("b"));
	}

	// --- Collected model properties narrowed ---

	@Test
	void collectAge_elementsNarrowed() throws OclParseException {
		Object result = eval("self.employees->collect(e | e.age)", company);
		assertInstanceOf(List.class, result);
		@SuppressWarnings("unchecked")
		List<Object> list = (List<Object>) result;
		for (Object elem : list) {
			assertInstanceOf(Integer.class, elem);
		}
	}

	@Test
	void sum_integerResult() throws OclParseException {
		// sum of ages: 30 + 25 = 55, fits in int
		Object result = eval("self.employees->collect(e | e.age)->sum()", company);
		assertInstanceOf(Integer.class, result);
		assertEquals(55, result);
	}

	// --- Arithmetic results ---

	@Test
	void addition_fitsInInt() throws OclParseException {
		Object result = eval("100 + 200", self);
		assertInstanceOf(Integer.class, result);
		assertEquals(300, result);
	}

	@Test
	void multiplication_fitsInInt() throws OclParseException {
		Object result = eval("100 * 100", self);
		assertInstanceOf(Integer.class, result);
		assertEquals(10000, result);
	}

	@Test
	void divMod_fitsInInt() throws OclParseException {
		Object result = eval("17.div(5)", self);
		assertInstanceOf(Integer.class, result);
		assertEquals(3, result);
	}

	@Test
	void mod_fitsInInt() throws OclParseException {
		Object result = eval("17.mod(5)", self);
		assertInstanceOf(Integer.class, result);
		assertEquals(2, result);
	}

	// --- String conversion ---

	@Test
	void toString_narrowedInteger() throws OclParseException {
		assertEquals("42", eval("42.toString()", self));
	}

	// --- Narrowing in if-then-else ---

	@Test
	void ifThenElse_integerBranch() throws OclParseException {
		Object result = eval("if true then 42 else 0 endif", self);
		assertInstanceOf(Integer.class, result);
		assertEquals(42, result);
	}

	// --- Narrowing in let ---

	@Test
	void let_integerValue() throws OclParseException {
		Object result = eval("let x : Integer = 99 in x + 1", self);
		assertInstanceOf(Integer.class, result);
		assertEquals(100, result);
	}
}
