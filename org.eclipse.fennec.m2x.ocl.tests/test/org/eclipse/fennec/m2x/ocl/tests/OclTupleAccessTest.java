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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for tuple creation and field access patterns.
 * Covers single-field, multi-field, nested tuples,
 * tuples in collections, and tuples with model data.
 */
class OclTupleAccessTest extends AbstractOclTest {

	static EObject self;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
		EObject bob = createPerson("Bob", 25, 40000.0, false);
		company = createCompany("ACME", self, bob);
	}

	// --- Single field ---

	@Test
	void tuple_singleStringField() throws OclParseException {
		assertEquals("hello", eval("Tuple{name : String = 'hello'}.name", self));
	}

	@Test
	void tuple_singleIntField() throws OclParseException {
		assertEquals(42, eval("Tuple{value : Integer = 42}.value", self));
	}

	@Test
	void tuple_singleBoolField() throws OclParseException {
		assertEquals(true, eval("Tuple{flag : Boolean = true}.flag", self));
	}

	@Test
	void tuple_singleRealField() throws OclParseException {
		assertEquals(3.14, eval("Tuple{pi : Real = 3.14}.pi", self));
	}

	// --- Multi field ---

	@Test
	void tuple_twoFields_accessFirst() throws OclParseException {
		assertEquals("Alice",
				eval("Tuple{name : String = 'Alice', age : Integer = 30}.name", self));
	}

	@Test
	void tuple_twoFields_accessSecond() throws OclParseException {
		assertEquals(30,
				eval("Tuple{name : String = 'Alice', age : Integer = 30}.age", self));
	}

	@Test
	void tuple_threeFields() throws OclParseException {
		assertEquals(true,
				eval("Tuple{a : Integer = 1, b : String = 'x', c : Boolean = true}.c", self));
	}

	// --- Tuple with model data ---

	@Test
	void tuple_withPropertyValues() throws OclParseException {
		assertEquals("Alice",
				eval("Tuple{n : String = self.name, a : Integer = self.age}.n", self));
	}

	@Test
	void tuple_withPropertyValues_secondField() throws OclParseException {
		assertEquals(30,
				eval("Tuple{n : String = self.name, a : Integer = self.age}.a", self));
	}

	// --- Tuple field access in expressions ---

	@Test
	void tuple_fieldInArithmetic() throws OclParseException {
		assertEquals(11,
				eval("Tuple{x : Integer = 5, y : Integer = 6}.x + Tuple{x : Integer = 5, y : Integer = 6}.y",
						self));
	}

	@Test
	void tuple_fieldInComparison() throws OclParseException {
		assertEquals(true,
				eval("Tuple{age : Integer = 30}.age >= 18", self));
	}

	@Test
	void tuple_fieldInStringOp() throws OclParseException {
		assertEquals(5,
				eval("Tuple{name : String = 'Alice'}.name.size()", self));
	}

	// --- Tuple in let ---

	@Test
	void let_tuple_access() throws OclParseException {
		assertEquals("Alice", eval(
				"let t : Tuple(name : String, age : Integer) = Tuple{name : String = 'Alice', age : Integer = 30} in t.name",
				self));
	}

	// --- Tuple equality ---

	@Test
	void tuple_equality_same() throws OclParseException {
		assertEquals(true, eval(
				"Tuple{x : Integer = 1, y : Integer = 2} = Tuple{x : Integer = 1, y : Integer = 2}",
				self));
	}

	@Test
	void tuple_equality_different() throws OclParseException {
		assertEquals(false, eval(
				"Tuple{x : Integer = 1} = Tuple{x : Integer = 2}",
				self));
	}

	@Test
	void tuple_inequality() throws OclParseException {
		assertEquals(true, eval(
				"Tuple{x : Integer = 1} <> Tuple{x : Integer = 2}",
				self));
	}

	// --- Tuple in if-then-else ---

	@Test
	void tuple_inIfThenElse() throws OclParseException {
		assertEquals("yes", eval(
				"if true then Tuple{r : String = 'yes'}.r else Tuple{r : String = 'no'}.r endif",
				self));
	}

	// --- Collection of tuples ---

	@Test
	void tupleSequence_collect() throws OclParseException {
		Object result = eval(
				"Sequence{Tuple{v : Integer = 1}, Tuple{v : Integer = 2}, Tuple{v : Integer = 3}}->collect(t | t.v)",
				self);
		assertEquals(List.of(1, 2, 3), result);
	}

	@Test
	void tupleSequence_select() throws OclParseException {
		Object result = eval(
				"Sequence{Tuple{v : Integer = 1}, Tuple{v : Integer = 5}, Tuple{v : Integer = 3}}->select(t | t.v > 2)->size()",
				self);
		assertEquals(2, result);
	}

	// --- Tuple as map-like structure ---

	@Test
	void tuple_asKeyValue() throws OclParseException {
		assertEquals("Alice", eval(
				"Tuple{key : String = 'name', value : String = 'Alice'}.value", self));
	}

	// --- Tuple oclIsUndefined ---

	@Test
	void tuple_notUndefined() throws OclParseException {
		assertEquals(false, eval(
				"Tuple{x : Integer = 1}.oclIsUndefined()", self));
	}
}
