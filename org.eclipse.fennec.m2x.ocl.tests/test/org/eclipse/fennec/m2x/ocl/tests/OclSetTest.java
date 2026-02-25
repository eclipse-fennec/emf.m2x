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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.eclipse.fennec.m2x.ocl.engine.internal.OclSet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link OclSet}.
 *
 * <p>Verifies that OclSet uses OCL equality for deduplication,
 * contains, add, and remove — especially numeric cross-type equality
 * (§11.5.1: {@code 4 = 4.0}).
 */
class OclSetTest {

	// ========================================================================
	// Basic Set contract
	// ========================================================================

	@Nested
	class BasicContract {

		@Test
		void empty_set() {
			OclSet<Object> set = new OclSet<>();
			assertTrue(set.isEmpty());
			assertEquals(0, set.size());
		}

		@Test
		void add_single_element() {
			OclSet<Object> set = new OclSet<>();
			assertTrue(set.add(1));
			assertEquals(1, set.size());
			assertTrue(set.contains(1));
		}

		@Test
		void add_duplicate_rejected() {
			OclSet<Object> set = new OclSet<>();
			assertTrue(set.add(1));
			assertFalse(set.add(1));
			assertEquals(1, set.size());
		}

		@Test
		void add_different_elements() {
			OclSet<Object> set = new OclSet<>();
			set.add(1);
			set.add(2);
			set.add(3);
			assertEquals(3, set.size());
		}

		@Test
		void contains_present() {
			OclSet<Object> set = new OclSet<>(List.of(1, 2, 3));
			assertTrue(set.contains(2));
		}

		@Test
		void contains_absent() {
			OclSet<Object> set = new OclSet<>(List.of(1, 2, 3));
			assertFalse(set.contains(5));
		}

		@Test
		void remove_present() {
			OclSet<Object> set = new OclSet<>(List.of(1, 2, 3));
			assertTrue(set.remove(2));
			assertEquals(2, set.size());
			assertFalse(set.contains(2));
		}

		@Test
		void remove_absent() {
			OclSet<Object> set = new OclSet<>(List.of(1, 2, 3));
			assertFalse(set.remove(5));
			assertEquals(3, set.size());
		}

		@Test
		void clear() {
			OclSet<Object> set = new OclSet<>(List.of(1, 2, 3));
			set.clear();
			assertTrue(set.isEmpty());
		}

		@Test
		void implements_set_interface() {
			assertInstanceOf(Set.class, new OclSet<>());
		}
	}

	// ========================================================================
	// OCL numeric cross-type equality (§11.5.1)
	// ========================================================================

	@Nested
	class NumericCrossType {

		@Test
		void dedup_integer_and_double_sameValue() {
			// Set{4, 4.0} → should contain only 1 element
			OclSet<Object> set = new OclSet<>();
			set.add(4);
			assertFalse(set.add(4.0)); // rejected: 4 = 4.0
			assertEquals(1, set.size());
		}

		@Test
		void dedup_double_and_integer_sameValue() {
			OclSet<Object> set = new OclSet<>();
			set.add(4.0);
			assertFalse(set.add(4)); // rejected: 4.0 = 4
			assertEquals(1, set.size());
		}

		@Test
		void dedup_long_and_double() {
			OclSet<Object> set = new OclSet<>();
			set.add(4L);
			assertFalse(set.add(4.0));
			assertEquals(1, set.size());
		}

		@Test
		void contains_integer_finds_double() {
			OclSet<Object> set = new OclSet<>(List.of(4.0));
			assertTrue(set.contains(4));   // 4 = 4.0
			assertTrue(set.contains(4L));  // 4L = 4.0
		}

		@Test
		void contains_double_finds_integer() {
			OclSet<Object> set = new OclSet<>(List.of(4));
			assertTrue(set.contains(4.0)); // 4.0 = 4
		}

		@Test
		void remove_crossType() {
			OclSet<Object> set = new OclSet<>();
			set.add(4);
			assertTrue(set.remove(4.0)); // 4.0 = 4
			assertTrue(set.isEmpty());
		}

		@Test
		void different_numeric_values_kept() {
			OclSet<Object> set = new OclSet<>();
			set.add(3);
			set.add(4.0);
			assertEquals(2, set.size()); // 3 ≠ 4.0
		}

		@Test
		void constructor_dedup_crossType() {
			// new OclSet(List.of(3, 4.0, 4, 5)) → {3, 4.0, 5}
			OclSet<Object> set = new OclSet<>(List.of(3, 4.0, 4, 5));
			assertEquals(3, set.size());
			assertTrue(set.contains(3));
			assertTrue(set.contains(4.0));
			assertTrue(set.contains(4));  // finds 4.0
			assertTrue(set.contains(5));
		}
	}

	// ========================================================================
	// Null handling
	// ========================================================================

	@Nested
	class NullHandling {

		@Test
		void add_null() {
			OclSet<Object> set = new OclSet<>();
			assertTrue(set.add(null));
			assertEquals(1, set.size());
			assertTrue(set.contains(null));
		}

		@Test
		void add_null_duplicate_rejected() {
			OclSet<Object> set = new OclSet<>();
			set.add(null);
			assertFalse(set.add(null));
			assertEquals(1, set.size());
		}

		@Test
		void null_not_equals_zero() {
			OclSet<Object> set = new OclSet<>();
			set.add(null);
			set.add(0);
			assertEquals(2, set.size());
		}

		@Test
		void remove_null() {
			OclSet<Object> set = new OclSet<>();
			set.add(null);
			set.add(1);
			assertTrue(set.remove(null));
			assertEquals(1, set.size());
		}
	}

	// ========================================================================
	// Mixed types (no coercion between non-numeric types)
	// ========================================================================

	@Nested
	class MixedTypes {

		@Test
		void string_and_integer_are_different() {
			OclSet<Object> set = new OclSet<>();
			set.add("3");
			set.add(3);
			assertEquals(2, set.size());
		}

		@Test
		void string_integer_double_mixed() {
			// Set{'test', 3, 4.0, 4, 'test'} → {'test', 3, 4.0}
			OclSet<Object> set = new OclSet<>(List.of("test", 3, 4.0, 4, "test"));
			assertEquals(3, set.size());
			assertTrue(set.contains("test"));
			assertTrue(set.contains(3));
			assertTrue(set.contains(4.0));
		}

		@Test
		void boolean_and_integer_are_different() {
			OclSet<Object> set = new OclSet<>();
			set.add(true);
			set.add(1);
			assertEquals(2, set.size());
		}
	}

	// ========================================================================
	// Iteration order (insertion order preserved)
	// ========================================================================

	@Nested
	class IterationOrder {

		@Test
		void preserves_insertion_order() {
			OclSet<Object> set = new OclSet<>();
			set.add("c");
			set.add("a");
			set.add("b");
			List<Object> list = List.copyOf(set);
			assertEquals(List.of("c", "a", "b"), list);
		}

		@Test
		void dedup_preserves_first_occurrence() {
			// Adding 4 first, then 4.0 → 4.0 rejected, 4 stays
			OclSet<Object> set = new OclSet<>();
			set.add(4);
			set.add(4.0);
			Object first = set.iterator().next();
			assertEquals(4, first); // first occurrence preserved
		}
	}
}
