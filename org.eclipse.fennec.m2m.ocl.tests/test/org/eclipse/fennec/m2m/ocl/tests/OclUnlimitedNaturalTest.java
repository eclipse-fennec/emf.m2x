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
import static org.junit.jupiter.api.Assertions.assertSame;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclUnlimitedNatural;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive tests for OCL UnlimitedNatural operations.
 * Based on OCL v2.4 Spec §11.5.5 and Eclipse OCL reference tests.
 */
class OclUnlimitedNaturalTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// === Literal ===

	@Nested
	class Literal {

		@Test
		void unlimitedLiteral() throws OclParseException {
			assertSame(OclUnlimitedNatural.INSTANCE, eval("*", self));
		}

		@Test
		void finiteLiteral_isInteger() throws OclParseException {
			// Finite values are just Integers
			assertEquals(42, eval("42", self));
		}
	}

	// === Comparison: * vs finite (Spec §11.5.5) ===

	@Nested
	class ComparisonUnlimitedVsFinite {

		@Test
		void unlimited_lessThan_finite() throws OclParseException {
			// * is NOT less than any finite value
			assertEquals(false, eval("* < 999999", self));
		}

		@Test
		void unlimited_lessThanOrEqual_finite() throws OclParseException {
			assertEquals(false, eval("* <= 999999", self));
		}

		@Test
		void unlimited_greaterThan_finite() throws OclParseException {
			assertEquals(true, eval("* > 999999", self));
		}

		@Test
		void unlimited_greaterThanOrEqual_finite() throws OclParseException {
			assertEquals(true, eval("* >= 999999", self));
		}
	}

	// === Comparison: finite vs * ===

	@Nested
	class ComparisonFiniteVsUnlimited {

		@Test
		void finite_lessThan_unlimited() throws OclParseException {
			assertEquals(true, eval("5 < *", self));
		}

		@Test
		void finite_lessThanOrEqual_unlimited() throws OclParseException {
			assertEquals(true, eval("5 <= *", self));
		}

		@Test
		void finite_greaterThan_unlimited() throws OclParseException {
			assertEquals(false, eval("5 > *", self));
		}

		@Test
		void finite_greaterThanOrEqual_unlimited() throws OclParseException {
			assertEquals(false, eval("5 >= *", self));
		}
	}

	// === Comparison: * vs * ===

	@Nested
	class ComparisonUnlimitedVsUnlimited {

		@Test
		void unlimited_lessThan_unlimited() throws OclParseException {
			// Spec: if self = * then result = false
			assertEquals(false, eval("* < *", self));
		}

		@Test
		void unlimited_lessThanOrEqual_unlimited() throws OclParseException {
			// Spec: if u = * then result = true
			assertEquals(true, eval("* <= *", self));
		}

		@Test
		void unlimited_greaterThan_unlimited() throws OclParseException {
			// Spec: if u = * then result = false
			assertEquals(false, eval("* > *", self));
		}

		@Test
		void unlimited_greaterThanOrEqual_unlimited() throws OclParseException {
			// Spec: if self = * then result = true
			assertEquals(true, eval("* >= *", self));
		}
	}

	// === Equality ===

	@Nested
	class Equality {

		@Test
		void unlimited_equals_unlimited() throws OclParseException {
			assertEquals(true, eval("* = *", self));
		}

		@Test
		void unlimited_notEquals_unlimited() throws OclParseException {
			assertEquals(false, eval("* <> *", self));
		}

		@Test
		void unlimited_equals_integer() throws OclParseException {
			assertEquals(false, eval("* = 1", self));
		}

		@Test
		void integer_equals_unlimited() throws OclParseException {
			assertEquals(false, eval("1 = *", self));
		}

		@Test
		void unlimited_notEquals_integer() throws OclParseException {
			assertEquals(true, eval("* <> 1", self));
		}

		@Test
		void integer_notEquals_unlimited() throws OclParseException {
			assertEquals(true, eval("1 <> *", self));
		}
	}

	// === Arithmetic on * — all invalid per Spec §11.5.5 ===

	@Nested
	class ArithmeticUnlimited {

		@Test
		void unlimited_plus_integer() throws OclParseException {
			assertInvalid("* + 1", self);
		}

		@Test
		void integer_plus_unlimited() throws OclParseException {
			assertInvalid("1 + *", self);
		}

		@Test
		void unlimited_plus_unlimited() throws OclParseException {
			assertInvalid("* + *", self);
		}

		@Test
		void unlimited_minus_integer() throws OclParseException {
			assertInvalid("* - 1", self);
		}

		@Test
		void integer_minus_unlimited() throws OclParseException {
			assertInvalid("1 - *", self);
		}

		@Test
		void unlimited_minus_unlimited() throws OclParseException {
			assertInvalid("* - *", self);
		}

		@Test
		void unlimited_times_integer() throws OclParseException {
			assertInvalid("* * 1", self);
		}

		@Test
		void integer_times_unlimited() throws OclParseException {
			assertInvalid("1 * *", self);
		}

		@Test
		void unlimited_times_unlimited() throws OclParseException {
			assertInvalid("* * *", self);
		}

		@Test
		void unlimited_divide_integer() throws OclParseException {
			assertInvalid("* / 1", self);
		}

		@Test
		void integer_divide_unlimited() throws OclParseException {
			assertInvalid("1 / *", self);
		}

		@Test
		void unlimited_divide_unlimited() throws OclParseException {
			assertInvalid("* / *", self);
		}

		@Test
		void unlimited_div_integer() throws OclParseException {
			assertInvalid("*.div(1)", self);
		}

		@Test
		void integer_div_unlimited() throws OclParseException {
			assertInvalid("1.div(*)", self);
		}

		@Test
		void unlimited_mod_integer() throws OclParseException {
			assertInvalid("*.mod(1)", self);
		}

		@Test
		void integer_mod_unlimited() throws OclParseException {
			assertInvalid("1.mod(*)", self);
		}

		@Test
		void unlimited_unaryMinus() throws OclParseException {
			assertInvalid("-*", self);
		}

		@Test
		void unlimited_abs() throws OclParseException {
			assertInvalid("*.abs()", self);
		}

		@Test
		void unlimited_floor() throws OclParseException {
			assertInvalid("*.floor()", self);
		}

		@Test
		void unlimited_round() throws OclParseException {
			assertInvalid("*.round()", self);
		}
	}

	// === max/min — Spec §11.5.5 defines these for UnlimitedNatural ===

	@Nested
	class MaxMin {

		@Test
		void unlimited_max_unlimited() throws OclParseException {
			// Spec §11.5.5: post: if self = * or u = * then result = *
			assertSame(OclUnlimitedNatural.INSTANCE, eval("*.max(*)", self));
		}

		@Test
		void unlimited_max_finite() throws OclParseException {
			// Spec: if self = * then result = *
			assertSame(OclUnlimitedNatural.INSTANCE, eval("*.max(1)", self));
		}

		@Test
		void finite_max_unlimited() throws OclParseException {
			// finite.max(*) — * is greater, so result = *
			assertSame(OclUnlimitedNatural.INSTANCE, eval("1.max(*)", self));
		}

		@Test
		void unlimited_min_unlimited() throws OclParseException {
			// Spec: if self = * then result = u; u = * → result = *
			assertSame(OclUnlimitedNatural.INSTANCE, eval("*.min(*)", self));
		}

		@Test
		void unlimited_min_finite() throws OclParseException {
			// Spec: if self = * then result = u → result = 1
			assertEquals(1, eval("*.min(1)", self));
		}

		@Test
		void finite_min_unlimited() throws OclParseException {
			// finite.min(*) — finite is smaller, result = finite
			assertEquals(1, eval("1.min(*)", self));
		}
	}

	// === Type conversion ===

	@Nested
	class TypeConversion {

		@Test
		void unlimited_toString() throws OclParseException {
			assertEquals("*", eval("*.toString()", self));
		}

		@Test
		void unlimited_toInteger() throws OclParseException {
			// Spec §11.5.5: if self = * then result = invalid
			assertInvalid("*.toInteger()", self);
		}
	}

	// === Type operations ===

	@Nested
	class TypeOperations {

		@Test
		void unlimited_oclIsUndefined() throws OclParseException {
			assertEquals(false, eval("*.oclIsUndefined()", self));
		}

		@Test
		void unlimited_oclIsInvalid() throws OclParseException {
			assertEquals(false, eval("*.oclIsInvalid()", self));
		}

		@Test
		void unlimited_oclIsTypeOf_UnlimitedNatural() throws OclParseException {
			assertEquals(true, eval("*.oclIsTypeOf(UnlimitedNatural)", self));
		}

		@Test
		void unlimited_oclIsTypeOf_Integer() throws OclParseException {
			assertEquals(false, eval("*.oclIsTypeOf(Integer)", self));
		}

		@Test
		void unlimited_oclIsKindOf_UnlimitedNatural() throws OclParseException {
			assertEquals(true, eval("*.oclIsKindOf(UnlimitedNatural)", self));
		}

		@Test
		void unlimited_oclIsKindOf_Integer() throws OclParseException {
			// Spec: UnlimitedNatural conforms to Integer
			assertEquals(true, eval("*.oclIsKindOf(Integer)", self));
		}

		@Test
		void unlimited_oclIsKindOf_Real() throws OclParseException {
			// UnlimitedNatural → Integer → Real
			assertEquals(true, eval("*.oclIsKindOf(Real)", self));
		}

		@Test
		void unlimited_oclIsKindOf_String() throws OclParseException {
			assertEquals(false, eval("*.oclIsKindOf(String)", self));
		}
	}

	// === Arithmetic: Real vs * ===

	@Nested
	class ArithmeticRealVsUnlimited {

		@Test
		void real_plus_unlimited() throws OclParseException {
			assertInvalid("1.0 + *", self);
		}

		@Test
		void unlimited_plus_real() throws OclParseException {
			assertInvalid("* + 1.0", self);
		}

		@Test
		void real_minus_unlimited() throws OclParseException {
			assertInvalid("1.0 - *", self);
		}

		@Test
		void real_times_unlimited() throws OclParseException {
			assertInvalid("1.0 * *", self);
		}

		@Test
		void real_divide_unlimited() throws OclParseException {
			assertInvalid("1.0 / *", self);
		}
	}

	// === Edge cases ===

	@Nested
	class EdgeCases {

		@Test
		void unlimited_divideByZero() throws OclParseException {
			assertInvalid("* / 0", self);
		}

		@Test
		void unlimited_in_sequence() throws OclParseException {
			// * can appear as collection element
			assertEquals(true, eval("Sequence{1, *, 3}->includes(*)", self));
		}

		@Test
		void unlimited_equals_zero() throws OclParseException {
			assertEquals(false, eval("* = 0", self));
		}

		@Test
		void unlimited_notEquals_zero() throws OclParseException {
			assertEquals(true, eval("* <> 0", self));
		}
	}
}
