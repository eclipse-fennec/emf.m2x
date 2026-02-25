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
 * Tests for string comparison and ordering operations.
 * OCL spec section 11.5: String has {@code <, >, <=, >=} based
 * on lexicographic ordering.
 */
class OclStringComparisonTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Equality ---

	@Test
	void equal_sameStrings() throws OclParseException {
		assertEquals(true, eval("'hello' = 'hello'", self));
	}

	@Test
	void equal_differentStrings() throws OclParseException {
		assertEquals(false, eval("'hello' = 'world'", self));
	}

	@Test
	void notEqual_differentStrings() throws OclParseException {
		assertEquals(true, eval("'hello' <> 'world'", self));
	}

	@Test
	void notEqual_sameStrings() throws OclParseException {
		assertEquals(false, eval("'hello' <> 'hello'", self));
	}

	// --- Lexicographic < ---

	@Test
	void lessThan_abc_xyz() throws OclParseException {
		assertEquals(true, eval("'abc' < 'xyz'", self));
	}

	@Test
	void lessThan_abc_abc() throws OclParseException {
		assertEquals(false, eval("'abc' < 'abc'", self));
	}

	@Test
	void lessThan_abc_abd() throws OclParseException {
		assertEquals(true, eval("'abc' < 'abd'", self));
	}

	@Test
	void lessThan_a_aa() throws OclParseException {
		assertEquals(true, eval("'a' < 'aa'", self));
	}

	// --- Lexicographic > ---

	@Test
	void greaterThan_xyz_abc() throws OclParseException {
		assertEquals(true, eval("'xyz' > 'abc'", self));
	}

	@Test
	void greaterThan_abc_abc() throws OclParseException {
		assertEquals(false, eval("'abc' > 'abc'", self));
	}

	@Test
	void greaterThan_b_a() throws OclParseException {
		assertEquals(true, eval("'b' > 'a'", self));
	}

	// --- Lexicographic <= ---

	@Test
	void lessOrEqual_abc_xyz() throws OclParseException {
		assertEquals(true, eval("'abc' <= 'xyz'", self));
	}

	@Test
	void lessOrEqual_same() throws OclParseException {
		assertEquals(true, eval("'abc' <= 'abc'", self));
	}

	@Test
	void lessOrEqual_greater() throws OclParseException {
		assertEquals(false, eval("'xyz' <= 'abc'", self));
	}

	// --- Lexicographic >= ---

	@Test
	void greaterOrEqual_xyz_abc() throws OclParseException {
		assertEquals(true, eval("'xyz' >= 'abc'", self));
	}

	@Test
	void greaterOrEqual_same() throws OclParseException {
		assertEquals(true, eval("'abc' >= 'abc'", self));
	}

	@Test
	void greaterOrEqual_less() throws OclParseException {
		assertEquals(false, eval("'abc' >= 'xyz'", self));
	}

	// --- Case sensitivity ---

	@Test
	void lessThan_caseSensitive_upperLower() throws OclParseException {
		// 'A' (65) < 'a' (97) in standard comparison
		assertEquals(true, eval("'A' < 'a'", self));
	}

	@Test
	void greaterThan_caseSensitive() throws OclParseException {
		assertEquals(true, eval("'a' > 'A'", self));
	}

	// --- Empty string ---

	@Test
	void empty_lessThan_any() throws OclParseException {
		assertEquals(true, eval("'' < 'a'", self));
	}

	@Test
	void empty_equals_empty() throws OclParseException {
		assertEquals(true, eval("'' = ''", self));
	}

	@Test
	void empty_lessOrEqual_empty() throws OclParseException {
		assertEquals(true, eval("'' <= ''", self));
	}

	// --- With model data ---

	@Test
	void property_stringComparison() throws OclParseException {
		assertEquals(true, eval("self.name > 'AAA'", self));
	}

	@Test
	void property_stringEquality() throws OclParseException {
		assertEquals(true, eval("self.name = 'Alice'", self));
	}

	// --- String comparison in collections ---

	@Test
	void sortedBy_strings() throws OclParseException {
		Object result = eval("Sequence{'cherry', 'apple', 'banana'}->sortedBy(s | s)", self);
		assertEquals(java.util.List.of("apple", "banana", "cherry"), result);
	}

	@Test
	void select_byStringComparison() throws OclParseException {
		assertEquals(2, eval(
				"Sequence{'apple', 'banana', 'cherry'}->select(s | s >= 'banana')->size()", self));
	}
}
