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
 * Advanced tests for OCL comparison operators.
 * Covers =, <>, <, >, <=, >= across different types,
 * string comparison, and comparison edge cases.
 */
class OclComparisonAdvancedTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Integer comparison ---

	@Test
	void int_equal() throws OclParseException {
		assertEquals(true, eval("5 = 5", self));
	}

	@Test
	void int_notEqual() throws OclParseException {
		assertEquals(true, eval("5 <> 6", self));
	}

	@Test
	void int_lessThan() throws OclParseException {
		assertEquals(true, eval("3 < 5", self));
	}

	@Test
	void int_greaterThan() throws OclParseException {
		assertEquals(true, eval("5 > 3", self));
	}

	@Test
	void int_lessOrEqual_equal() throws OclParseException {
		assertEquals(true, eval("5 <= 5", self));
	}

	@Test
	void int_lessOrEqual_less() throws OclParseException {
		assertEquals(true, eval("3 <= 5", self));
	}

	@Test
	void int_greaterOrEqual_equal() throws OclParseException {
		assertEquals(true, eval("5 >= 5", self));
	}

	@Test
	void int_greaterOrEqual_greater() throws OclParseException {
		assertEquals(true, eval("5 >= 3", self));
	}

	@Test
	void int_notLessThan() throws OclParseException {
		assertEquals(false, eval("5 < 3", self));
	}

	@Test
	void int_notGreaterThan() throws OclParseException {
		assertEquals(false, eval("3 > 5", self));
	}

	// --- Negative numbers ---

	@Test
	void negative_lessThanPositive() throws OclParseException {
		assertEquals(true, eval("-5 < 5", self));
	}

	@Test
	void negative_comparison() throws OclParseException {
		assertEquals(true, eval("-5 < -3", self));
	}

	@Test
	void zero_comparison() throws OclParseException {
		assertEquals(true, eval("0 <= 0", self));
	}

	// --- String comparison ---

	@Test
	void string_equal() throws OclParseException {
		assertEquals(true, eval("'hello' = 'hello'", self));
	}

	@Test
	void string_notEqual() throws OclParseException {
		assertEquals(true, eval("'hello' <> 'world'", self));
	}

	@Test
	void string_lessThan() throws OclParseException {
		assertEquals(true, eval("'apple' < 'banana'", self));
	}

	@Test
	void string_greaterThan() throws OclParseException {
		assertEquals(true, eval("'banana' > 'apple'", self));
	}

	@Test
	void string_caseSensitive() throws OclParseException {
		assertEquals(false, eval("'Hello' = 'hello'", self));
	}

	@Test
	void string_emptyEquality() throws OclParseException {
		assertEquals(true, eval("'' = ''", self));
	}

	@Test
	void string_emptyLessThan() throws OclParseException {
		assertEquals(true, eval("'' < 'a'", self));
	}

	// --- Boolean comparison ---

	@Test
	void bool_equal_true() throws OclParseException {
		assertEquals(true, eval("true = true", self));
	}

	@Test
	void bool_equal_false() throws OclParseException {
		assertEquals(true, eval("false = false", self));
	}

	@Test
	void bool_notEqual() throws OclParseException {
		assertEquals(true, eval("true <> false", self));
	}

	// --- Real comparison ---

	@Test
	void real_equal() throws OclParseException {
		assertEquals(true, eval("3.14 = 3.14", self));
	}

	@Test
	void real_lessThan() throws OclParseException {
		assertEquals(true, eval("3.14 < 3.15", self));
	}

	@Test
	void real_greaterOrEqual() throws OclParseException {
		assertEquals(true, eval("3.14 >= 3.14", self));
	}

	// --- Comparison with computed values ---

	@Test
	void computed_equal() throws OclParseException {
		assertEquals(true, eval("2 + 3 = 5", self));
	}

	@Test
	void computed_lessThan() throws OclParseException {
		assertEquals(true, eval("2 * 3 < 2 * 4", self));
	}

	@Test
	void computed_bothSides() throws OclParseException {
		assertEquals(true, eval("1 + 2 + 3 = 3 + 2 + 1", self));
	}

	// --- Comparison with properties ---

	@Test
	void property_equal() throws OclParseException {
		assertEquals(true, eval("self.name = 'Alice'", self));
	}

	@Test
	void property_greaterThan() throws OclParseException {
		assertEquals(true, eval("self.age > 18", self));
	}

	@Test
	void property_lessOrEqual() throws OclParseException {
		assertEquals(true, eval("self.salary <= 100000.0", self));
	}

	// --- Comparison in let ---

	@Test
	void comparison_inLet() throws OclParseException {
		assertEquals(true, eval(
				"let x: Integer = 10, y: Integer = 20 in x < y", self));
	}

	// --- Chained comparison (via boolean) ---

	@Test
	void chained_comparisons() throws OclParseException {
		// OCL doesn't support a < b < c directly, must use and
		assertEquals(true, eval("1 < 2 and 2 < 3", self));
	}
}
