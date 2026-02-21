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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclBag;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for cross-type union and intersection semantics.
 *
 * <p>Spec: OCL v2.4 §11.7.2 (Set), §11.7.4 (Bag), §11.7.5 (Sequence).
 *
 * <p>Return type rules:
 * <ul>
 *   <li>{@code Set->union(Set)} → Set</li>
 *   <li>{@code Set->union(Bag)} → Bag</li>
 *   <li>{@code Bag->union(Set)} → Bag</li>
 *   <li>{@code Bag->union(Bag)} → Bag</li>
 *   <li>{@code Sequence->union(Sequence)} → Sequence</li>
 *   <li>{@code Set->intersection(Set)} → Set</li>
 *   <li>{@code Set->intersection(Bag)} → Set</li>
 *   <li>{@code Bag->intersection(Set)} → Set</li>
 *   <li>{@code Bag->intersection(Bag)} → Bag</li>
 * </ul>
 *
 * <p>Eclipse reference: {@code GenericEvaluationCollectionOperationTest#testCollectionUnionDuplicates()},
 * {@code testCollectionUnionNoDuplicates()}, {@code testCollectionIntersectionDuplicates()}.
 */
class OclUnionIntersectionCrossTypeTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// ========================================================================
	// Union — Return Type (§11.7.2, §11.7.4)
	// ========================================================================

	@Nested
	class UnionReturnType {

		@Test
		void set_union_set_returnsSet() throws OclParseException {
			Object result = eval("Set{'a', 'b'}->union(Set{'c', 'd'})", self);
			assertInstanceOf(Set.class, result);
			assertEquals(4, ((Collection<?>) result).size());
		}

		@Test
		void set_union_bag_returnsBag() throws OclParseException {
			// §11.7.2: union(bag : Bag(T)) : Bag(T)
			Object result = eval("Set{'a', 'b'}->union(Bag{'c', 'd'})", self);
			assertInstanceOf(OclBag.class, result);
			assertEquals(4, ((Collection<?>) result).size());
		}

		@Test
		void bag_union_set_returnsBag() throws OclParseException {
			// §11.7.4: union(set : Set(T)) : Bag(T)
			Object result = eval("Bag{'a', 'b'}->union(Set{'c', 'd'})", self);
			assertInstanceOf(OclBag.class, result);
			assertEquals(4, ((Collection<?>) result).size());
		}

		@Test
		void bag_union_bag_returnsBag() throws OclParseException {
			Object result = eval("Bag{'a', 'b'}->union(Bag{'c', 'd'})", self);
			assertInstanceOf(OclBag.class, result);
			assertEquals(4, ((Collection<?>) result).size());
		}

		@Test
		void sequence_union_sequence_returnsSequence() throws OclParseException {
			Object result = eval("Sequence{'a', 'b'}->union(Sequence{'c', 'd'})", self);
			assertInstanceOf(List.class, result);
			assertEquals(4, ((Collection<?>) result).size());
		}
	}

	// ========================================================================
	// Union — Duplicate Semantics
	// ========================================================================

	@Nested
	class UnionDuplicates {

		@Test
		void set_union_set_deduplicates() throws OclParseException {
			// Set{a,b,a}->union(Set{b,c}) → Set{a,b,c}
			Object result = eval("Set{1, 2}->union(Set{2, 3})", self);
			assertInstanceOf(Set.class, result);
			assertEquals(3, ((Collection<?>) result).size());
		}

		@Test
		void set_union_bag_preservesDuplicates() throws OclParseException {
			// Set{a,b}->union(Bag{b,c}) → Bag{a,b,b,c}
			Object result = eval("Set{1, 2}->union(Bag{2, 3})", self);
			assertInstanceOf(OclBag.class, result);
			assertEquals(4, ((Collection<?>) result).size());
		}

		@Test
		void bag_union_set_preservesDuplicates() throws OclParseException {
			// Bag{a,b,a}->union(Set{b,c}) → Bag{a,b,a,b,c}
			Object result = eval("Bag{1, 2, 1}->union(Set{2, 3})", self);
			assertInstanceOf(OclBag.class, result);
			assertEquals(5, ((Collection<?>) result).size());
		}

		@Test
		void bag_union_bag_allDuplicatesKept() throws OclParseException {
			// Bag{a,b,a}->union(Bag{b,c}) → Bag{a,b,a,b,c}
			Object result = eval("Bag{1, 2, 1}->union(Bag{2, 3})", self);
			assertInstanceOf(OclBag.class, result);
			assertEquals(5, ((Collection<?>) result).size());
		}

		@Test
		void sequence_union_preservesAll() throws OclParseException {
			// Sequence{a,b,a}->union(Sequence{b,c}) → Sequence{a,b,a,b,c}
			Object result = eval("Sequence{1, 2, 1}->union(Sequence{2, 3})", self);
			assertEquals(List.of(1, 2, 1, 2, 3), result);
		}
	}

	// ========================================================================
	// Union — Empty Collections
	// ========================================================================

	@Nested
	class UnionEmpty {

		@Test
		void set_union_emptyBag_returnsBag() throws OclParseException {
			// §11.7.2: Set->union(Bag) → Bag even when Bag is empty
			Object result = eval("Set{3, 4}->union(Bag{})", self);
			assertInstanceOf(OclBag.class, result);
			assertEquals(2, ((Collection<?>) result).size());
		}

		@Test
		void bag_union_emptySet_returnsBag() throws OclParseException {
			Object result = eval("Bag{3, 4}->union(Set{})", self);
			assertInstanceOf(OclBag.class, result);
			assertEquals(2, ((Collection<?>) result).size());
		}

		@Test
		void emptySet_union_bag_returnsBag() throws OclParseException {
			Object result = eval("Set{}->union(Bag{3, 4})", self);
			assertInstanceOf(OclBag.class, result);
			assertEquals(2, ((Collection<?>) result).size());
		}

		@Test
		void emptyBag_union_set_returnsBag() throws OclParseException {
			Object result = eval("Bag{}->union(Set{3, 4})", self);
			assertInstanceOf(OclBag.class, result);
			assertEquals(2, ((Collection<?>) result).size());
		}
	}

	// ========================================================================
	// Intersection — Return Type (§11.7.2, §11.7.4)
	// ========================================================================

	@Nested
	class IntersectionReturnType {

		@Test
		void set_intersection_set_returnsSet() throws OclParseException {
			Object result = eval("Set{1, 2, 3}->intersection(Set{2, 3, 4})", self);
			assertInstanceOf(Set.class, result);
			assertEquals(2, ((Collection<?>) result).size());
		}

		@Test
		void set_intersection_bag_returnsSet() throws OclParseException {
			// §11.7.2: intersection(bag : Bag(T)) : Set(T)
			Object result = eval("Set{1, 2, 3}->intersection(Bag{2, 3, 4})", self);
			assertInstanceOf(OclSet.class, result);
			assertEquals(2, ((Collection<?>) result).size());
		}

		@Test
		void bag_intersection_set_returnsSet() throws OclParseException {
			// §11.7.4: intersection(set : Set(T)) : Set(T)
			Object result = eval("Bag{1, 2, 3}->intersection(Set{2, 3, 4})", self);
			assertInstanceOf(OclSet.class, result);
			assertEquals(2, ((Collection<?>) result).size());
		}

		@Test
		void bag_intersection_bag_returnsBag() throws OclParseException {
			Object result = eval("Bag{1, 2, 3}->intersection(Bag{2, 3, 4})", self);
			assertInstanceOf(OclBag.class, result);
			assertEquals(2, ((Collection<?>) result).size());
		}
	}

	// ========================================================================
	// Intersection — Duplicate/Frequency Semantics
	// ========================================================================

	@Nested
	class IntersectionDuplicates {

		@Test
		void set_intersection_bag_noDuplicatesInResult() throws OclParseException {
			// Set{a,b}->intersection(Bag{a,b,b,c}) → Set{a,b}
			Object result = eval("Set{1, 2}->intersection(Bag{1, 2, 2, 3})", self);
			assertInstanceOf(OclSet.class, result);
			assertEquals(2, ((Collection<?>) result).size());
		}

		@Test
		void bag_intersection_set_noDuplicatesInResult() throws OclParseException {
			// Bag{a,b,a}->intersection(Set{a,b,c}) → Set{a,b}
			Object result = eval("Bag{1, 2, 1}->intersection(Set{1, 2, 3})", self);
			assertInstanceOf(OclSet.class, result);
			assertEquals(2, ((Collection<?>) result).size());
		}

		@Test
		void bag_intersection_bag_minFrequency() throws OclParseException {
			// Bag{a,b,a,b}->intersection(Bag{a,b,b}) → Bag{a,b,b}
			// min(count_self, count_other): a: min(2,1)=1, b: min(2,2)=2
			Object result = eval(
					"Bag{1, 2, 1, 2}->intersection(Bag{1, 2, 2})", self);
			assertInstanceOf(OclBag.class, result);
			assertEquals(3, ((Collection<?>) result).size());
		}

		@Test
		void bag_intersection_bag_minFrequency_reversed() throws OclParseException {
			// Bag{a,b,a}->intersection(Bag{a,b}) → Bag{a,b}
			Object result = eval(
					"Bag{1, 2, 1}->intersection(Bag{1, 2})", self);
			assertInstanceOf(OclBag.class, result);
			assertEquals(2, ((Collection<?>) result).size());
		}
	}

	// ========================================================================
	// Intersection — Empty Collections
	// ========================================================================

	@Nested
	class IntersectionEmpty {

		@Test
		void set_intersection_emptyBag_returnsEmptySet() throws OclParseException {
			Object result = eval("Set{3, 4}->intersection(Bag{})", self);
			assertInstanceOf(OclSet.class, result);
			assertTrue(((Collection<?>) result).isEmpty());
		}

		@Test
		void bag_intersection_emptySet_returnsEmptySet() throws OclParseException {
			Object result = eval("Bag{3, 4}->intersection(Set{})", self);
			assertInstanceOf(OclSet.class, result);
			assertTrue(((Collection<?>) result).isEmpty());
		}

		@Test
		void emptyBag_intersection_set_returnsEmptySet() throws OclParseException {
			Object result = eval("Bag{}->intersection(Set{3, 4})", self);
			assertInstanceOf(OclSet.class, result);
			assertTrue(((Collection<?>) result).isEmpty());
		}
	}

	// ========================================================================
	// Union — Content Verification (Eclipse patterns)
	// ========================================================================

	@Nested
	class UnionContent {

		@Test
		void set_union_bag_containsAll() throws OclParseException {
			// Set{1,2}->union(Bag{2,3}) should contain 1,2,2,3
			assertEquals(true, eval(
					"Set{1, 2}->union(Bag{2, 3})->includes(1)", self));
			assertEquals(true, eval(
					"Set{1, 2}->union(Bag{2, 3})->includes(2)", self));
			assertEquals(true, eval(
					"Set{1, 2}->union(Bag{2, 3})->includes(3)", self));
		}

		@Test
		void set_union_bag_count() throws OclParseException {
			// In Bag result, 2 appears once from Set + once from Bag = 2 times
			assertEquals(2, eval(
					"Set{1, 2}->union(Bag{2, 3})->count(2)", self));
		}

		@Test
		void bag_union_set_count() throws OclParseException {
			// Bag{1,1,2}->union(Set{2,3}) → count(1)=2, count(2)=2, count(3)=1
			assertEquals(2, eval(
					"Bag{1, 1, 2}->union(Set{2, 3})->count(1)", self));
			assertEquals(2, eval(
					"Bag{1, 1, 2}->union(Set{2, 3})->count(2)", self));
			assertEquals(1, eval(
					"Bag{1, 1, 2}->union(Set{2, 3})->count(3)", self));
		}
	}

	// ========================================================================
	// Intersection — Content Verification
	// ========================================================================

	@Nested
	class IntersectionContent {

		@Test
		void set_intersection_bag_elements() throws OclParseException {
			// Set{1,2,3}->intersection(Bag{2,3,4}) → Set{2,3}
			assertEquals(true, eval(
					"Set{1, 2, 3}->intersection(Bag{2, 3, 4})->includes(2)", self));
			assertEquals(true, eval(
					"Set{1, 2, 3}->intersection(Bag{2, 3, 4})->includes(3)", self));
			assertEquals(false, eval(
					"Set{1, 2, 3}->intersection(Bag{2, 3, 4})->includes(1)", self));
		}

		@Test
		void bag_intersection_set_elements() throws OclParseException {
			// Bag{1,2,2,3,3,3}->intersection(Set{3,4,5}) → Set{3}
			Object result = eval(
					"Bag{1, 2, 2, 3, 3, 3}->intersection(Set{3, 4, 5})", self);
			assertInstanceOf(OclSet.class, result);
			assertEquals(1, ((Collection<?>) result).size());
		}
	}

	// ========================================================================
	// Union/Intersection — Null handling
	// ========================================================================

	@Nested
	class NullHandling {

		@Test
		void set_union_bag_withNull() throws OclParseException {
			// Set{1, null}->union(Bag{2, null}) → Bag with both nulls
			Object result = eval(
					"Set{1, null}->union(Bag{2, null})", self);
			assertInstanceOf(OclBag.class, result);
			assertEquals(4, ((Collection<?>) result).size());
		}

		@Test
		void bag_intersection_set_withNull() throws OclParseException {
			// Bag{2, 3, null}->intersection(Set{2, 4, null}) → Set{2, null}
			Object result = eval(
					"Bag{2, 3, null}->intersection(Set{2, 4, null})", self);
			assertInstanceOf(OclSet.class, result);
			assertEquals(2, ((Collection<?>) result).size());
		}
	}

	// ========================================================================
	// Chained cross-type operations
	// ========================================================================

	@Nested
	class Chaining {

		@Test
		void set_union_bag_then_intersection() throws OclParseException {
			// Set{1,2}->union(Bag{3,4}) results in Bag{1,2,3,4}
			// then ->intersection(Bag{2,3}) → Bag{2,3}
			Object result = eval(
					"Set{1, 2}->union(Bag{3, 4})->intersection(Bag{2, 3})", self);
			assertInstanceOf(OclBag.class, result);
			assertEquals(2, ((Collection<?>) result).size());
		}

		@Test
		void bag_intersection_set_then_size() throws OclParseException {
			assertEquals(2, eval(
					"Bag{1, 2, 2, 3}->intersection(Set{2, 3, 4})->size()", self));
		}
	}
}
