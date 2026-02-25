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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.ocl.engine.internal.OclEqualityUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link OclEqualityUtil}.
 *
 * <p>Spec: §11.4.2 Integer is subclass of Real → {@code 4 = 4.0} is true.
 */
class OclEqualityUtilTest {

	// ========================================================================
	// Null handling
	// ========================================================================

	@Nested
	class NullHandling {

		@Test
		void null_equals_null() {
			assertTrue(OclEqualityUtil.oclEquals(null, null));
		}

		@Test
		void null_notEquals_nonNull() {
			assertFalse(OclEqualityUtil.oclEquals(null, 1));
		}

		@Test
		void nonNull_notEquals_null() {
			assertFalse(OclEqualityUtil.oclEquals(1, null));
		}
	}

	// ========================================================================
	// Same reference
	// ========================================================================

	@Nested
	class SameReference {

		@Test
		void sameObject_isEqual() {
			Object obj = "hello";
			assertTrue(OclEqualityUtil.oclEquals(obj, obj));
		}
	}

	// ========================================================================
	// Integer equality
	// ========================================================================

	@Nested
	class IntegerEquality {

		@Test
		void int_equals_int() {
			assertTrue(OclEqualityUtil.oclEquals(4, 4));
		}

		@Test
		void int_notEquals_int() {
			assertFalse(OclEqualityUtil.oclEquals(4, 5));
		}

		@Test
		void long_equals_long() {
			assertTrue(OclEqualityUtil.oclEquals(4L, 4L));
		}

		@Test
		void int_equals_long_sameValue() {
			assertTrue(OclEqualityUtil.oclEquals(4, 4L));
		}

		@Test
		void long_equals_int_sameValue() {
			assertTrue(OclEqualityUtil.oclEquals(4L, 4));
		}

		@Test
		void zero_equals_zero() {
			assertTrue(OclEqualityUtil.oclEquals(0, 0));
		}

		@Test
		void negative_equals_negative() {
			assertTrue(OclEqualityUtil.oclEquals(-3, -3));
		}

		@Test
		void negative_notEquals_positive() {
			assertFalse(OclEqualityUtil.oclEquals(-3, 3));
		}
	}

	// ========================================================================
	// Cross-type numeric equality (§11.4.2, §11.5.1)
	// ========================================================================

	@Nested
	class CrossTypeNumeric {

		@Test
		void int_equals_double_sameValue() {
			// §11.5.1: Integer is subclass of Real → 4 = 4.0
			assertTrue(OclEqualityUtil.oclEquals(4, 4.0));
		}

		@Test
		void double_equals_int_sameValue() {
			assertTrue(OclEqualityUtil.oclEquals(4.0, 4));
		}

		@Test
		void long_equals_double_sameValue() {
			assertTrue(OclEqualityUtil.oclEquals(4L, 4.0));
		}

		@Test
		void double_equals_long_sameValue() {
			assertTrue(OclEqualityUtil.oclEquals(4.0, 4L));
		}

		@Test
		void int_notEquals_double_differentValue() {
			assertFalse(OclEqualityUtil.oclEquals(4, 4.5));
		}

		@Test
		void double_notEquals_int_differentValue() {
			assertFalse(OclEqualityUtil.oclEquals(4.5, 4));
		}

		@Test
		void zero_int_equals_zero_double() {
			assertTrue(OclEqualityUtil.oclEquals(0, 0.0));
		}

		@Test
		void negative_int_equals_negative_double() {
			assertTrue(OclEqualityUtil.oclEquals(-3, -3.0));
		}

		@Test
		void one_equals_one_point_zero() {
			assertTrue(OclEqualityUtil.oclEquals(1, 1.0));
		}
	}

	// ========================================================================
	// Double equality
	// ========================================================================

	@Nested
	class DoubleEquality {

		@Test
		void double_equals_double() {
			assertTrue(OclEqualityUtil.oclEquals(3.14, 3.14));
		}

		@Test
		void double_notEquals_double() {
			assertFalse(OclEqualityUtil.oclEquals(3.14, 2.71));
		}
	}

	// ========================================================================
	// String equality
	// ========================================================================

	@Nested
	class StringEquality {

		@Test
		void string_equals_string() {
			assertTrue(OclEqualityUtil.oclEquals("hello", "hello"));
		}

		@Test
		void string_notEquals_string() {
			assertFalse(OclEqualityUtil.oclEquals("hello", "world"));
		}

		@Test
		void string_notEquals_number() {
			assertFalse(OclEqualityUtil.oclEquals("3", 3));
		}

		@Test
		void number_notEquals_string() {
			assertFalse(OclEqualityUtil.oclEquals(3, "3"));
		}
	}

	// ========================================================================
	// Boolean equality
	// ========================================================================

	@Nested
	class BooleanEquality {

		@Test
		void true_equals_true() {
			assertTrue(OclEqualityUtil.oclEquals(true, true));
		}

		@Test
		void false_equals_false() {
			assertTrue(OclEqualityUtil.oclEquals(false, false));
		}

		@Test
		void true_notEquals_false() {
			assertFalse(OclEqualityUtil.oclEquals(true, false));
		}

		@Test
		void boolean_notEquals_number() {
			assertFalse(OclEqualityUtil.oclEquals(true, 1));
		}
	}

	// ========================================================================
	// Mixed types — no coercion
	// ========================================================================

	@Nested
	class MixedTypes {

		@Test
		void string_notEquals_boolean() {
			assertFalse(OclEqualityUtil.oclEquals("true", true));
		}

		@Test
		void int_notEquals_string() {
			assertFalse(OclEqualityUtil.oclEquals(42, "42"));
		}

		@Test
		void null_notEquals_zero() {
			assertFalse(OclEqualityUtil.oclEquals(null, 0));
		}

		@Test
		void null_notEquals_emptyString() {
			assertFalse(OclEqualityUtil.oclEquals(null, ""));
		}

		@Test
		void null_notEquals_false() {
			assertFalse(OclEqualityUtil.oclEquals(null, false));
		}
	}
}
