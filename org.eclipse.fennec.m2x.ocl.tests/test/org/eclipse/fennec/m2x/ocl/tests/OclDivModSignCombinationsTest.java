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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@code div()} and {@code mod()} with all sign combinations.
 *
 * <p>Spec: OCL v2.4 §11.5.2 (Integer):
 * <ul>
 *   <li>{@code div(i : Integer) : Integer} — "The number of times that i fits
 *       completely within self."
 *       <br>pre: {@code i <> 0}
 *       <br>post: {@code if self / i >= 0 then result = (self / i).floor()
 *       else result = -((-self/i).floor()) endif}</li>
 *   <li>{@code mod(i : Integer) : Integer} — "The result is self modulo i."
 *       <br>post: {@code result = self - (self.div(i) * i)}</li>
 * </ul>
 *
 * <p>Eclipse reference: {@code EvaluateNumericOperationsTest4#testNumberDiv()},
 * {@code testNumberMod()}.
 */
class OclDivModSignCombinationsTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// ========================================================================
	// div — all 4 sign combinations (§11.5.2)
	// ========================================================================

	@Nested
	class DivSignCombinations {

		@Test
		void positive_div_positive() throws OclParseException {
			// 3.div(2): self/i = 1.5 >= 0 → floor(1.5) = 1
			assertEquals(1, eval("3.div(2)", self));
		}

		@Test
		void negative_div_positive() throws OclParseException {
			// (-3).div(2): self/i = -1.5 < 0 → -(floor(3/2)) = -(1) = -1
			assertEquals(-1, eval("(-3).div(2)", self));
		}

		@Test
		void positive_div_negative() throws OclParseException {
			// 3.div(-2): self/i = -1.5 < 0 → -(floor(3/2)) = -(1) = -1
			assertEquals(-1, eval("3.div(-2)", self));
		}

		@Test
		void negative_div_negative() throws OclParseException {
			// (-3).div(-2): self/i = 1.5 >= 0 → floor(1.5) = 1
			assertEquals(1, eval("(-3).div(-2)", self));
		}

		@Test
		void larger_positive_div_positive() throws OclParseException {
			// 7.div(2) = floor(3.5) = 3
			assertEquals(3, eval("7.div(2)", self));
		}

		@Test
		void larger_negative_div_positive() throws OclParseException {
			// (-7).div(2): -(floor(7/2)) = -(3) = -3
			assertEquals(-3, eval("(-7).div(2)", self));
		}

		@Test
		void larger_positive_div_negative() throws OclParseException {
			// 7.div(-2): -(floor(7/2)) = -(3) = -3
			assertEquals(-3, eval("7.div(-2)", self));
		}

		@Test
		void larger_negative_div_negative() throws OclParseException {
			// (-7).div(-2): floor(7/2) = 3
			assertEquals(3, eval("(-7).div(-2)", self));
		}

		@Test
		void exact_division() throws OclParseException {
			assertEquals(5, eval("10.div(2)", self));
		}

		@Test
		void exact_division_negative() throws OclParseException {
			// (-10).div(2) = -(floor(10/2)) = -5
			assertEquals(-5, eval("(-10).div(2)", self));
		}

		@Test
		void div_by_one() throws OclParseException {
			assertEquals(42, eval("42.div(1)", self));
		}

		@Test
		void div_negative_by_one() throws OclParseException {
			assertEquals(-42, eval("(-42).div(1)", self));
		}

		@Test
		void div_by_negative_one() throws OclParseException {
			assertEquals(-42, eval("42.div(-1)", self));
		}

		@Test
		void div_self_equals_one() throws OclParseException {
			assertEquals(1, eval("17.div(17)", self));
		}

		@Test
		void div_by_zero_invalid() throws OclParseException {
			assertInvalid("5.div(0)", self);
		}

		@Test
		void div_zero_by_positive() throws OclParseException {
			// 0.div(5) = 0
			assertEquals(0, eval("0.div(5)", self));
		}

		@Test
		void div_zero_by_negative() throws OclParseException {
			// 0.div(-5) = 0
			assertEquals(0, eval("0.div(-5)", self));
		}
	}

	// ========================================================================
	// mod — all 4 sign combinations (§11.5.2)
	// post: result = self - (self.div(i) * i)
	// ========================================================================

	@Nested
	class ModSignCombinations {

		@Test
		void positive_mod_positive() throws OclParseException {
			// 3.mod(2) = 3 - (1 * 2) = 1
			assertEquals(1, eval("3.mod(2)", self));
		}

		@Test
		void negative_mod_positive() throws OclParseException {
			// (-3).mod(2) = -3 - (-1 * 2) = -3 + 2 = -1
			assertEquals(-1, eval("(-3).mod(2)", self));
		}

		@Test
		void positive_mod_negative() throws OclParseException {
			// 3.mod(-2) = 3 - (-1 * -2) = 3 - 2 = 1
			assertEquals(1, eval("3.mod(-2)", self));
		}

		@Test
		void negative_mod_negative() throws OclParseException {
			// (-3).mod(-2) = -3 - (1 * -2) = -3 + 2 = -1
			assertEquals(-1, eval("(-3).mod(-2)", self));
		}

		@Test
		void larger_positive_mod_positive() throws OclParseException {
			// 7.mod(2) = 7 - (3 * 2) = 1
			assertEquals(1, eval("7.mod(2)", self));
		}

		@Test
		void larger_negative_mod_positive() throws OclParseException {
			// (-7).mod(2) = -7 - (-3 * 2) = -7 + 6 = -1
			assertEquals(-1, eval("(-7).mod(2)", self));
		}

		@Test
		void larger_positive_mod_negative() throws OclParseException {
			// 7.mod(-2) = 7 - (-3 * -2) = 7 - 6 = 1
			assertEquals(1, eval("7.mod(-2)", self));
		}

		@Test
		void larger_negative_mod_negative() throws OclParseException {
			// (-7).mod(-2) = -7 - (3 * -2) = -7 + 6 = -1
			assertEquals(-1, eval("(-7).mod(-2)", self));
		}

		@Test
		void mod_no_remainder() throws OclParseException {
			assertEquals(0, eval("10.mod(5)", self));
		}

		@Test
		void mod_no_remainder_negative() throws OclParseException {
			// (-10).mod(5) = -10 - (-2 * 5) = 0
			assertEquals(0, eval("(-10).mod(5)", self));
		}

		@Test
		void mod_by_one() throws OclParseException {
			assertEquals(0, eval("42.mod(1)", self));
		}

		@Test
		void mod_by_negative_one() throws OclParseException {
			// 42.mod(-1) = 42 - (-42 * -1) = 42 - 42 = 0
			assertEquals(0, eval("42.mod(-1)", self));
		}

		@Test
		void mod_by_zero_invalid() throws OclParseException {
			assertInvalid("5.mod(0)", self);
		}

		@Test
		void mod_zero_by_positive() throws OclParseException {
			assertEquals(0, eval("0.mod(5)", self));
		}

		@Test
		void mod_zero_by_negative() throws OclParseException {
			assertEquals(0, eval("0.mod(-5)", self));
		}
	}

	// ========================================================================
	// div/mod relationship: a = (a.div(b) * b) + a.mod(b)
	// ========================================================================

	@Nested
	class DivModRelationship {

		@Test
		void relationship_positive_positive() throws OclParseException {
			// 17 = 17.div(5) * 5 + 17.mod(5) = 3*5 + 2 = 17
			assertEquals(17, eval("17.div(5) * 5 + 17.mod(5)", self));
		}

		@Test
		void relationship_negative_positive() throws OclParseException {
			// -17 = (-17).div(5) * 5 + (-17).mod(5) = -3*5 + (-2) = -17
			assertEquals(-17, eval("(-17).div(5) * 5 + (-17).mod(5)", self));
		}

		@Test
		void relationship_positive_negative() throws OclParseException {
			// 17 = 17.div(-5) * (-5) + 17.mod(-5) = (-3)*(-5) + 2 = 17
			assertEquals(17, eval("17.div(-5) * (-5) + 17.mod(-5)", self));
		}

		@Test
		void relationship_negative_negative() throws OclParseException {
			// -17 = (-17).div(-5) * (-5) + (-17).mod(-5) = 3*(-5) + (-2) = -17
			assertEquals(-17, eval("(-17).div(-5) * (-5) + (-17).mod(-5)", self));
		}
	}
}
