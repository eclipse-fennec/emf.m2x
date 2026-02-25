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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@code closure} return type and behavior (OCL v2.4 §11.9.1).
 *
 * <p>Spec reference: §11.9.1 defines closure return type based on source:
 * <ul>
 *   <li>Ordered source (Sequence, OrderedSet) → <b>OrderedSet</b></li>
 *   <li>Unordered source (Set, Bag) → <b>Set</b></li>
 * </ul>
 *
 * <p>Eclipse reference: {@code GenericIteratorsTest#test_closure()},
 * {@code test_closure_cycles()}, {@code IteratorsTest4#test_closure_recursions_401302()}.
 *
 * <p>⚠️ SPEC-FIRST: Tests are written against the spec. If they fail,
 * the implementation has a gap — fix the implementation, NOT the test.
 */
class OclClosureReturnTypeTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// ========================================================================
	// Return type depends on source collection type (§11.9.1)
	// ========================================================================

	@Test
	void closure_fromSet_returnsSet() throws OclParseException {
		// §11.9.1: Set (unordered) → Result = Set
		Object result = eval("Set{1}->closure(i | if i < 4 then Set{i + 1} else Set{} endif)", self);
		assertSet(result);
		assertEquals(4, ((Collection<?>) result).size()); // {1, 2, 3, 4}
	}

	@Test
	void closure_fromBag_returnsSet() throws OclParseException {
		// §11.9.1: Bag (unordered) → Result = Set
		Object result = eval("Bag{1}->closure(i | if i < 3 then Set{i + 1} else Set{} endif)", self);
		assertSet(result);
		assertEquals(3, ((Collection<?>) result).size()); // {1, 2, 3}
	}

	@Test
	void closure_fromSequence_returnsOrderedSet() throws OclParseException {
		// §11.9.1: Sequence (ordered) → Result = OrderedSet
		Object result = eval("Sequence{1}->closure(i | if i < 4 then Sequence{i + 1} else Sequence{} endif)", self);
		assertOrderedSet(result);
		assertEquals(4, ((Collection<?>) result).size()); // {1, 2, 3, 4}
	}

	@Test
	void closure_fromOrderedSet_returnsOrderedSet() throws OclParseException {
		// §11.9.1: OrderedSet (ordered) → Result = OrderedSet
		Object result = eval("OrderedSet{1}->closure(i | if i < 3 then OrderedSet{i + 1} else OrderedSet{} endif)", self);
		assertOrderedSet(result);
		assertEquals(3, ((Collection<?>) result).size()); // {1, 2, 3}
	}

	// ========================================================================
	// Cycle detection — closure terminates on already-visited elements
	// ========================================================================

	@Test
	void closure_cycleDetection_terminates() throws OclParseException {
		// Body always returns {1, 2} — but elements are already visited, so closure terminates
		Object result = eval("Set{1, 2}->closure(i | Set{1, 2})", self);
		assertSet(result);
		assertEquals(2, ((Collection<?>) result).size());
		assertTrue(((Collection<?>) result).contains(1));
		assertTrue(((Collection<?>) result).contains(2));
	}

	@Test
	void closure_cycle_singleElement() throws OclParseException {
		// Body returns the element itself — immediate cycle
		Object result = eval("Set{42}->closure(i | Set{i})", self);
		assertSet(result);
		assertEquals(1, ((Collection<?>) result).size());
		assertTrue(((Collection<?>) result).contains(42));
	}

	// ========================================================================
	// Source elements are included in result (§11.9.1: anonAcc->add(iterator))
	// ========================================================================

	@Test
	void closure_includesSourceElements() throws OclParseException {
		// Start with {1}, body produces {2} for 1, {3} for 2, {} for 3
		// Result should include source element 1 plus 2, 3
		Object result = eval(
				"Set{1}->closure(i | if i < 3 then Set{i + 1} else Set{} endif)", self);
		Collection<?> coll = (Collection<?>) result;
		assertEquals(3, coll.size());
		assertTrue(coll.contains(1)); // source element included
		assertTrue(coll.contains(2));
		assertTrue(coll.contains(3));
	}

	// ========================================================================
	// Empty source / empty body result
	// ========================================================================

	@Test
	void closure_emptySource_fromSet() throws OclParseException {
		Object result = eval("Set{}->closure(i | Set{i})", self);
		assertSet(result);
		assertTrue(((Collection<?>) result).isEmpty());
	}

	@Test
	void closure_emptySource_fromSequence() throws OclParseException {
		Object result = eval("Sequence{}->closure(i | Sequence{i})", self);
		assertOrderedSet(result);
		assertTrue(((Collection<?>) result).isEmpty());
	}

	@Test
	void closure_bodyReturnsEmpty() throws OclParseException {
		// Body always returns empty → only source elements in result
		Object result = eval("Set{1, 2, 3}->closure(i | Set{})", self);
		assertSet(result);
		assertEquals(3, ((Collection<?>) result).size());
	}

	// ========================================================================
	// Deduplication — closure removes duplicates (Eclipse bug 401302)
	// ========================================================================

	@Test
	void closure_deduplicates() throws OclParseException {
		// Multiple paths lead to same elements — result has no duplicates
		// Start: {1, 2}; body: 1→{2, 3}, 2→{1, 3}, 3→{1, 2}
		// All paths cycle back — result should be exactly {1, 2, 3}
		Object result = eval(
				"Set{1, 2}->closure(i | if i = 1 then Set{2, 3} else if i = 2 then Set{1, 3} else Set{1, 2} endif endif)",
				self);
		assertSet(result);
		assertEquals(3, ((Collection<?>) result).size());
		assertTrue(((Collection<?>) result).contains(1));
		assertTrue(((Collection<?>) result).contains(2));
		assertTrue(((Collection<?>) result).contains(3));
	}

	// ========================================================================
	// Invalid / null body handling
	// ========================================================================

	@Test
	void closure_bodyReturnsNull_skipsNull() throws OclParseException {
		// Body returns null for some elements — null should be skipped
		Object result = eval(
				"Set{1, 2}->closure(i | if i = 1 then Set{3} else null endif)", self);
		assertSet(result);
		// 1→{3}, 2→null(skip), 3→null(skip) → result = {1, 2, 3}
		assertEquals(3, ((Collection<?>) result).size());
	}

	// ========================================================================
	// Helpers
	// ========================================================================

	/**
	 * Asserts that the result is a Set (OclSet), not an OrderedSet.
	 */
	private static void assertSet(Object result) {
		assertInstanceOf(Set.class, result, "Closure from unordered source must return Set");
		assertNotEquals("OclOrderedSet", result.getClass().getSimpleName(),
				"Closure from unordered source must return Set, not OclOrderedSet");
	}

	/**
	 * Asserts that the result is an OrderedSet (OclOrderedSet).
	 */
	private static void assertOrderedSet(Object result) {
		assertInstanceOf(List.class, result, "Closure from ordered source must return OrderedSet");
		assertEquals("OclOrderedSet", result.getClass().getSimpleName(),
				"Expected OclOrderedSet but got " + result.getClass().getName());
	}
}
