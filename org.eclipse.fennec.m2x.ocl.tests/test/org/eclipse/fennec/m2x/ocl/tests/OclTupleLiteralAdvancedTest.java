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
 * Advanced tests for OCL Tuple literals.
 * Covers computed parts, tuples in let, tuples in iterators,
 * tuple equality, and tuple part access patterns.
 */
class OclTupleLiteralAdvancedTest extends AbstractOclTest {

	static EObject self;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
		EObject bob = createPerson("Bob", 25, 45000.0, false);
		company = createCompany("ACME", self, bob);
	}

	// --- Basic tuple ---

	@Test
	void tuple_intPart() throws OclParseException {
		assertEquals(42, eval("Tuple{a: Integer = 42}.a", self));
	}

	@Test
	void tuple_stringPart() throws OclParseException {
		assertEquals("hello", eval("Tuple{s: String = 'hello'}.s", self));
	}

	@Test
	void tuple_boolPart() throws OclParseException {
		assertEquals(true, eval("Tuple{b: Boolean = true}.b", self));
	}

	@Test
	void tuple_realPart() throws OclParseException {
		assertEquals(3.14, eval("Tuple{pi: Real = 3.14}.pi", self));
	}

	// --- Multi-part tuple ---

	@Test
	void tuple_multiPart_accessFirst() throws OclParseException {
		assertEquals("Alice", eval(
				"Tuple{name: String = 'Alice', age: Integer = 30}.name", self));
	}

	@Test
	void tuple_multiPart_accessSecond() throws OclParseException {
		assertEquals(30, eval(
				"Tuple{name: String = 'Alice', age: Integer = 30}.age", self));
	}

	// --- Computed tuple parts ---

	@Test
	void tuple_computedPart() throws OclParseException {
		assertEquals(6, eval("Tuple{sum: Integer = 1 + 2 + 3}.sum", self));
	}

	@Test
	void tuple_partFromProperty() throws OclParseException {
		assertEquals("Alice", eval(
				"Tuple{n: String = self.name}.n", self));
	}

	@Test
	void tuple_partFromExpression() throws OclParseException {
		assertEquals("ALICE", eval(
				"Tuple{upper: String = self.name.toUpperCase()}.upper", self));
	}

	// --- Tuple in let ---

	@Test
	void tuple_inLet() throws OclParseException {
		assertEquals(42, eval(
				"let t: Tuple(x: Integer) = Tuple{x: Integer = 42} in t.x", self));
	}

	@Test
	void tuple_inLet_multiPart() throws OclParseException {
		assertEquals(true, eval(
				"let pair: Tuple(a: Integer, b: Integer) = Tuple{a: Integer = 10, b: Integer = 20} " +
				"in pair.a < pair.b", self));
	}

	// --- Tuple equality ---

	@Test
	void tuple_equal() throws OclParseException {
		assertEquals(true, eval(
				"Tuple{a: Integer = 1, b: Integer = 2} = Tuple{a: Integer = 1, b: Integer = 2}",
				self));
	}

	@Test
	void tuple_notEqual() throws OclParseException {
		assertEquals(false, eval(
				"Tuple{a: Integer = 1} = Tuple{a: Integer = 2}", self));
	}

	// --- Tuple in collection ---

	@Test
	void tuple_inSequence() throws OclParseException {
		assertEquals(2, eval(
				"Sequence{Tuple{x: Integer = 1}, Tuple{x: Integer = 2}}->size()", self));
	}

	// --- Tuple part access chained ---

	@Test
	void tuple_partThenMethod() throws OclParseException {
		assertEquals(5, eval(
				"Tuple{name: String = 'Alice'}.name.size()", self));
	}

	@Test
	void tuple_partThenArithmetic() throws OclParseException {
		assertEquals(11, eval(
				"Tuple{x: Integer = 5}.x + 6", self));
	}

	@Test
	void tuple_partThenComparison() throws OclParseException {
		assertEquals(true, eval(
				"Tuple{x: Integer = 42}.x > 10", self));
	}

	// --- Tuple as summary result ---

	@Test
	void tuple_modelSummary() throws OclParseException {
		// Create a summary tuple from model data
		assertEquals(true, eval(
				"let summary: Tuple(count: Integer, name: String) = " +
				"  Tuple{count: Integer = self.employees->size(), name: String = self.name} " +
				"in summary.count > 0 and summary.name = 'ACME'",
				company));
	}
}
