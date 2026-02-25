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

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclBag;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclOrderedSet;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@code excluding()} — verifies that ALL occurrences are removed.
 *
 * <p>Spec: OCL v2.4 §11.7.2 (Set), §11.7.4 (Bag), §11.7.5 (Sequence).
 * <ul>
 *   <li>§11.7.4 Bag: "all elements of self apart from all occurrences of object"</li>
 *   <li>§11.7.5 Sequence: post: result->includes(object) = false</li>
 * </ul>
 *
 * <p>Eclipse reference: {@code GenericEvaluationCollectionOperationTest#testCollectionExcluding()},
 * {@code CollectionsTest#test_excluding()}.
 */
class OclExcludingAllOccurrencesTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// ========================================================================
	// Sequence — ALL occurrences removed
	// ========================================================================

	@Nested
	class SequenceExcluding {

		@Test
		void removes_all_occurrences() throws OclParseException {
			// Sequence{'b','a','b','c'}->excluding('b') → Sequence{'a','c'}
			Object result = eval("Sequence{'b', 'a', 'b', 'c'}->excluding('b')", self);
			assertEquals(List.of("a", "c"), result);
		}

		@Test
		void removes_all_integer_occurrences() throws OclParseException {
			// Sequence{1,2,1,3,1}->excluding(1) → Sequence{2,3}
			Object result = eval("Sequence{1, 2, 1, 3, 1}->excluding(1)", self);
			assertEquals(List.of(2, 3), result);
		}

		@Test
		void element_not_present() throws OclParseException {
			Object result = eval("Sequence{1, 2, 3}->excluding(5)", self);
			assertEquals(List.of(1, 2, 3), result);
		}

		@Test
		void preserves_order() throws OclParseException {
			Object result = eval("Sequence{3, 1, 2, 1}->excluding(1)", self);
			assertEquals(List.of(3, 2), result);
		}

		@Test
		void single_element_removed() throws OclParseException {
			Object result = eval("Sequence{42}->excluding(42)", self);
			assertEquals(List.of(), result);
		}

		@Test
		void empty_sequence() throws OclParseException {
			Object result = eval("Sequence{}->excluding(1)", self);
			assertEquals(List.of(), result);
		}

		@Test
		void result_includes_false() throws OclParseException {
			// post: result->includes(object) = false
			assertEquals(false, eval(
					"Sequence{1, 2, 1, 3}->excluding(1)->includes(1)", self));
		}

		@Test
		void result_size() throws OclParseException {
			// 5 elements, 3 are 'b' → 2 remaining
			assertEquals(2, eval(
					"Sequence{'b', 'a', 'b', 'c', 'b'}->excluding('b')->size()", self));
		}
	}

	// ========================================================================
	// Bag — ALL occurrences removed
	// ========================================================================

	@Nested
	class BagExcluding {

		@Test
		void removes_all_occurrences() throws OclParseException {
			Object result = eval("Bag{1, 2, 1, 3, 1}->excluding(1)", self);
			assertInstanceOf(OclBag.class, result);
			assertEquals(2, ((Collection<?>) result).size());
			assertEquals(false, eval(
					"Bag{1, 2, 1, 3, 1}->excluding(1)->includes(1)", self));
		}

		@Test
		void element_not_present() throws OclParseException {
			Object result = eval("Bag{1, 2, 3}->excluding(5)", self);
			assertInstanceOf(OclBag.class, result);
			assertEquals(3, ((Collection<?>) result).size());
		}

		@Test
		void empty_bag() throws OclParseException {
			Object result = eval("Bag{}->excluding(1)", self);
			assertInstanceOf(OclBag.class, result);
			assertEquals(0, ((Collection<?>) result).size());
		}

		@Test
		void count_after_excluding() throws OclParseException {
			// Bag{1,1,1,2,3}->excluding(1)->count(1) = 0
			assertEquals(0, eval(
					"Bag{1, 1, 1, 2, 3}->excluding(1)->count(1)", self));
		}
	}

	// ========================================================================
	// Set — single element removed (unique)
	// ========================================================================

	@Nested
	class SetExcluding {

		@Test
		void removes_element() throws OclParseException {
			Object result = eval("Set{1, 2, 3}->excluding(2)", self);
			assertInstanceOf(Set.class, result);
			assertEquals(2, ((Collection<?>) result).size());
			assertEquals(false, eval("Set{1, 2, 3}->excluding(2)->includes(2)", self));
		}

		@Test
		void element_not_present() throws OclParseException {
			Object result = eval("Set{1, 2, 3}->excluding(5)", self);
			assertEquals(3, ((Collection<?>) result).size());
		}

		@Test
		void empty_set() throws OclParseException {
			Object result = eval("Set{}->excluding(1)", self);
			assertInstanceOf(OclSet.class, result);
			assertEquals(0, ((Collection<?>) result).size());
		}
	}

	// ========================================================================
	// OrderedSet — single element removed (unique, preserves order)
	// ========================================================================

	@Nested
	class OrderedSetExcluding {

		@Test
		void removes_element() throws OclParseException {
			Object result = eval("OrderedSet{1, 2, 3}->excluding(2)", self);
			assertInstanceOf(OclOrderedSet.class, result);
			assertEquals(2, ((Collection<?>) result).size());
			assertEquals(false, eval(
					"OrderedSet{1, 2, 3}->excluding(2)->includes(2)", self));
		}

		@Test
		void preserves_order() throws OclParseException {
			assertEquals(1, eval(
					"OrderedSet{3, 1, 2}->excluding(3)->first()", self));
		}
	}

	// ========================================================================
	// Cross-type numeric excluding (§11.5.1: 4 = 4.0)
	// ========================================================================

	@Nested
	class NumericCrossType {

		@Test
		void set_excluding_crossType() throws OclParseException {
			// Set{4.0, 5}->excluding(4) → should remove 4.0 (4 = 4.0)
			Object result = eval("Set{4.0, 5}->excluding(4)", self);
			assertInstanceOf(Set.class, result);
			assertEquals(1, ((Collection<?>) result).size());
		}

		@Test
		void sequence_excluding_crossType() throws OclParseException {
			// Sequence{4.0, 5, 4}->excluding(4) → should remove both 4.0 and 4
			Object result = eval("Sequence{4.0, 5, 4}->excluding(4)", self);
			assertEquals(1, ((Collection<?>) result).size());
		}

		@Test
		void bag_excluding_crossType() throws OclParseException {
			// Bag{4.0, 5, 4}->excluding(4) → should remove both 4.0 and 4
			Object result = eval("Bag{4.0, 5, 4}->excluding(4)", self);
			assertEquals(1, ((Collection<?>) result).size());
		}

		@Test
		void orderedSet_excluding_crossType() throws OclParseException {
			// OrderedSet{4.0, 5}->excluding(4) → should remove 4.0
			Object result = eval("OrderedSet{4.0, 5}->excluding(4)", self);
			assertEquals(1, ((Collection<?>) result).size());
		}
	}

	// ========================================================================
	// Null excluding
	// ========================================================================

	@Nested
	class NullExcluding {

		@Test
		void sequence_excluding_null() throws OclParseException {
			Object result = eval("Sequence{1, null, 2, null}->excluding(null)", self);
			assertEquals(List.of(1, 2), result);
		}

		@Test
		void bag_excluding_null() throws OclParseException {
			assertEquals(0, eval(
					"Bag{1, null, 2}->excluding(null)->count(null)", self));
		}
	}

	// ========================================================================
	// Return type preserved
	// ========================================================================

	@Nested
	class ReturnType {

		@Test
		void set_returns_set() throws OclParseException {
			assertInstanceOf(OclSet.class, eval("Set{1, 2}->excluding(1)", self));
		}

		@Test
		void bag_returns_bag() throws OclParseException {
			assertInstanceOf(OclBag.class, eval("Bag{1, 2}->excluding(1)", self));
		}

		@Test
		void sequence_returns_sequence() throws OclParseException {
			Object result = eval("Sequence{1, 2}->excluding(1)", self);
			assertInstanceOf(List.class, result);
		}

		@Test
		void orderedSet_returns_orderedSet() throws OclParseException {
			assertInstanceOf(OclOrderedSet.class,
					eval("OrderedSet{1, 2}->excluding(1)", self));
		}
	}
}
