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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.fennec.m2m.ocl.engine.internal.OclOrderedSet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link OclOrderedSet} data structure.
 *
 * <p>Verifies that OclOrderedSet uses OCL equality for deduplication
 * and contains — especially numeric cross-type equality
 * (§11.5.1: {@code 4 = 4.0}).
 */
class OclOrderedSetUnitTest {

	// ========================================================================
	// Basic OrderedSet contract
	// ========================================================================

	@Nested
	class BasicContract {

		@Test
		void empty_orderedSet() {
			OclOrderedSet<Object> oset = new OclOrderedSet<>();
			assertTrue(oset.isEmpty());
			assertEquals(0, oset.size());
		}

		@Test
		void add_single_element() {
			OclOrderedSet<Object> oset = new OclOrderedSet<>();
			assertTrue(oset.add(1));
			assertEquals(1, oset.size());
			assertTrue(oset.contains(1));
		}

		@Test
		void add_duplicate_rejected() {
			OclOrderedSet<Object> oset = new OclOrderedSet<>();
			assertTrue(oset.add(1));
			assertFalse(oset.add(1));
			assertEquals(1, oset.size());
		}

		@Test
		void preserves_insertion_order() {
			OclOrderedSet<Object> oset = new OclOrderedSet<>(List.of(3, 1, 2));
			assertEquals(List.of(3, 1, 2), List.copyOf(oset));
		}

		@Test
		void extends_list() {
			assertInstanceOf(List.class, new OclOrderedSet<>());
		}

		@Test
		void constructor_dedup_sameType() {
			OclOrderedSet<Object> oset = new OclOrderedSet<>(List.of(1, 2, 2, 3));
			assertEquals(3, oset.size());
			assertEquals(List.of(1, 2, 3), List.copyOf(oset));
		}
	}

	// ========================================================================
	// OCL numeric cross-type equality (§11.5.1)
	// ========================================================================

	@Nested
	class NumericCrossType {

		@Test
		void dedup_integer_and_double_sameValue() {
			OclOrderedSet<Object> oset = new OclOrderedSet<>();
			oset.add(4);
			assertFalse(oset.add(4.0)); // rejected: 4 = 4.0
			assertEquals(1, oset.size());
		}

		@Test
		void dedup_double_and_integer_sameValue() {
			OclOrderedSet<Object> oset = new OclOrderedSet<>();
			oset.add(4.0);
			assertFalse(oset.add(4)); // rejected: 4.0 = 4
			assertEquals(1, oset.size());
		}

		@Test
		void contains_integer_finds_double() {
			OclOrderedSet<Object> oset = new OclOrderedSet<>(List.of(4.0));
			assertTrue(oset.contains(4));
		}

		@Test
		void contains_double_finds_integer() {
			OclOrderedSet<Object> oset = new OclOrderedSet<>(List.of(4));
			assertTrue(oset.contains(4.0));
		}

		@Test
		void constructor_dedup_crossType() {
			// OrderedSet{3, 4.0, 4, 5} → {3, 4.0, 5}
			OclOrderedSet<Object> oset = new OclOrderedSet<>(List.of(3, 4.0, 4, 5));
			assertEquals(3, oset.size());
			assertEquals(3, oset.get(0));
			assertEquals(4.0, oset.get(1));
			assertEquals(5, oset.get(2));
		}

		@Test
		void different_numeric_values_kept() {
			OclOrderedSet<Object> oset = new OclOrderedSet<>(List.of(3, 4.0));
			assertEquals(2, oset.size());
		}
	}

	// ========================================================================
	// Mixed types
	// ========================================================================

	@Nested
	class MixedTypes {

		@Test
		void string_and_integer_are_different() {
			OclOrderedSet<Object> oset = new OclOrderedSet<>(List.of("3", 3));
			assertEquals(2, oset.size());
		}

		@Test
		void string_integer_double_mixed() {
			// OrderedSet{'test', 3, 4.0, 4, 'test'} → {'test', 3, 4.0}
			OclOrderedSet<Object> oset = new OclOrderedSet<>(
					List.of("test", 3, 4.0, 4, "test"));
			assertEquals(3, oset.size());
		}
	}

	// ========================================================================
	// Null handling
	// ========================================================================

	@Nested
	class NullHandling {

		@Test
		void add_null() {
			OclOrderedSet<Object> oset = new OclOrderedSet<>();
			assertTrue(oset.add(null));
			assertEquals(1, oset.size());
			assertTrue(oset.contains(null));
		}

		@Test
		void add_null_duplicate_rejected() {
			OclOrderedSet<Object> oset = new OclOrderedSet<>();
			oset.add(null);
			assertFalse(oset.add(null));
			assertEquals(1, oset.size());
		}
	}
}
