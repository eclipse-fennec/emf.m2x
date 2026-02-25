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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL type widening: Integer/Real coercion in mixed-type
 * collections, if-then-else branches, and collection equality.
 *
 * <p>Per OCL spec, Integer conforms to Real. Operations on mixed
 * Integer/Real values should produce Real results where appropriate.
 */
class OclTypeWideningTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// === Mixed-type collection min/max ===

	@Test
	void mixed_collection_min() throws OclParseException {
		// Sequence{1, 2.5, 3}->min() → 1 (Integer, smaller than 2.5)
		Object result = eval("Sequence{1, 2.5, 3}->min()", self);
		// min should return 1 (as it's the smallest)
		assertTrue(result instanceof Number);
		assertEquals(1.0, ((Number) result).doubleValue(), 0.001);
	}

	@Test
	void mixed_collection_max() throws OclParseException {
		Object result = eval("Sequence{1, 2.5, 3}->max()", self);
		assertTrue(result instanceof Number);
		assertEquals(3.0, ((Number) result).doubleValue(), 0.001);
	}

	@Test
	void mixed_collection_sum() throws OclParseException {
		// sum of Integer + Real → Real
		Object result = eval("Sequence{1, 2.5, 3}->sum()", self);
		assertTrue(result instanceof Double);
		assertEquals(6.5, (Double) result, 0.001);
	}

	// === If-then-else with Integer/Real branches ===

	@Test
	void ifThenElse_integerAndReal() throws OclParseException {
		// true branch returns Integer, false branch returns Real
		Object result = eval("if true then 5 else 3.14 endif", self);
		// Should return Integer 5 (then-branch)
		assertTrue(result instanceof Number);
		assertEquals(5, ((Number) result).intValue());
	}

	@Test
	void ifThenElse_realBranch() throws OclParseException {
		Object result = eval("if false then 5 else 3.14 endif", self);
		assertTrue(result instanceof Double);
		assertEquals(3.14, (Double) result, 0.001);
	}

	// === Collection equality with numeric coercion ===

	@Test
	void set_integerAndReal_equality() throws OclParseException {
		// Set{1, 2} = Set{1.0, 2.0} — should be equal via numeric coercion
		assertEquals(true, eval("Set{1, 2} = Set{1.0, 2.0}", self));
	}

	@Test
	void sequence_integerAndReal_equality() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3} = Sequence{1.0, 2.0, 3.0}", self));
	}

	@Test
	void bag_integerAndReal_equality() throws OclParseException {
		assertEquals(true, eval("Bag{1, 2} = Bag{1.0, 2.0}", self));
	}

	// === Mixed-type sortedBy ===

	@Test
	void sortedBy_mixedIntegerReal() throws OclParseException {
		Object result = eval("Sequence{3, 1.5, 2}->sortedBy(x | x)", self);
		assertTrue(result instanceof Collection<?>);
		Collection<?> coll = (Collection<?>) result;
		assertEquals(3, coll.size());
		// First element should be 1.5
		Object first = coll.iterator().next();
		assertTrue(first instanceof Number);
		assertEquals(1.5, ((Number) first).doubleValue(), 0.001);
	}

	// === Arithmetic with mixed types ===

	@Test
	void sum_allIntegers_returnsInteger() throws OclParseException {
		// All integers → sum returns Integer
		Object result = eval("Sequence{1, 2, 3}->sum()", self);
		assertEquals(6, result);
	}

	@Test
	void sum_withOneReal_returnsReal() throws OclParseException {
		Object result = eval("Sequence{1, 2.0, 3}->sum()", self);
		assertTrue(result instanceof Double);
		assertEquals(6.0, (Double) result, 0.001);
	}

	// === Includes with numeric coercion ===

	@Test
	void set_includes_integerAsReal() throws OclParseException {
		// Set{1.0, 2.0}->includes(1) → true (1 = 1.0)
		assertEquals(true, eval("Set{1.0, 2.0}->includes(1)", self));
	}

	@Test
	void sequence_includes_realAsInteger() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3}->includes(2.0)", self));
	}

	// === Let with type coercion ===

	@Test
	void let_integerInRealContext() throws OclParseException {
		// let x = 5 in x + 1.5 → 6.5 (Integer promoted to Real)
		Object result = eval("let x = 5 in x + 1.5", self);
		assertTrue(result instanceof Double);
		assertEquals(6.5, (Double) result, 0.001);
	}

	// === Division always returns Real ===

	@Test
	void division_integerByInteger_isReal() throws OclParseException {
		// 7 / 2 → 3.5 (Real, not Integer division)
		Object result = eval("7 / 2", self);
		assertTrue(result instanceof Double);
		assertEquals(3.5, (Double) result, 0.001);
	}
}
