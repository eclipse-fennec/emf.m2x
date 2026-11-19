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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Integer and Real are the mathematical concepts (OCL v2.4 §11.4.1, §11.4.2, #322): Integer
 * continues beyond 64 bits, Real is decimal, and both compare exactly (#317).
 */
class OclNumberSemanticsTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 0.1, true);
	}

	/** §11.4.2: Integer is unbounded. */
	@Nested
	class UnboundedInteger {

		@Test
		void overflow_continuesExactly() throws OclParseException {
			assertEquals(new BigInteger("9223372036854775808"), eval("9223372036854775807 + 1", self));
			assertEquals(new BigInteger("18446744073709551614"), eval("9223372036854775807 * 2", self));
			assertEquals(new BigInteger("9223372036854775808"), eval("(-9223372036854775807 - 1).abs()", self));
			assertEquals(new BigInteger("9223372036854775808"), eval("(-9223372036854775807 - 1).div(-1)", self));
		}

		@Test
		void bigResults_comeBackToLongWhenTheyFit() throws OclParseException {
			assertEquals(9223372036854775807L, eval("9223372036854775807 + 1 - 1", self));
		}

		@Test
		void comparison_isExactAbove2pow53() throws OclParseException {
			// #317: compared as doubles these two were equal
			assertEquals(false, eval("9007199254740993 = 9007199254740992", self));
			assertEquals(true, eval("9007199254740993 > 9007199254740992", self));
			assertEquals(2, eval("Set{9007199254740993, 9007199254740992}->size()", self));
		}

		@Test
		void stringConversion_beyond64Bits() throws OclParseException {
			assertEquals(new BigInteger("123456789012345678901234567890"),
					eval("'123456789012345678901234567890'.toInteger()", self));
		}
	}

	/** §11.4.1: Real is decimal — 0.1 + 0.2 = 0.3. */
	@Nested
	class DecimalReal {

		@Test
		void decimalArithmetic_isExact() throws OclParseException {
			assertEquals(true, eval("0.1 + 0.2 = 0.3", self));
			assertEquals(true, eval("1.1 * 3 = 3.3", self));
			assertEquals(true, eval("self.salary + 0.2 = 0.3", self)); // an EDouble 0.1 from the model
		}

		@Test
		void division_hasAtLeast32FractionDigits() throws OclParseException {
			assertEquals("0.33333333333333333333333333333333", eval("(1 / 3).toString()", self));
			assertEquals("0.5", eval("(1 / 2).toString()", self));
			assertEquals(true, eval("1 / 4 = 0.25", self));
		}

		@Test
		void evaluate_returnsReals_asDouble() throws OclParseException {
			assertEquals(0.3, eval("0.1 + 0.2", self));
			assertEquals(3.5, eval("1 + 2.5", self));
		}

		@Test
		void evaluateWithDiagnostics_keepsTheExactValue() throws OclParseException {
			Object value = evalWithDiagnostics("0.1 + 0.2", self).value();
			assertTrue(value instanceof BigDecimal, String.valueOf(value));
			assertEquals(0, new BigDecimal("0.3").compareTo((BigDecimal) value));
		}

		@Test
		void integerAndReal_areOneValue() throws OclParseException {
			assertEquals(true, eval("1 = 1.0", self));
			assertEquals(true, eval("Set{1, 1.0}->size() = 1", self));
			assertEquals(true, eval("2.0.oclIsKindOf(Real)", self));
		}
	}

	/** §11.5.1 toString: plain decimal notation. */
	@Nested
	class RealText {

		@Test
		void toString_isPlainDecimal() throws OclParseException {
			assertEquals("3.0", eval("3.0.toString()", self));
			assertEquals("100000000000000000000.0", eval("100000000000000000000.0.toString()", self));
			assertEquals("0.00001", eval("0.00001.toString()", self));
			assertEquals("0.3", eval("(0.1 + 0.2).toString()", self));
			assertEquals("5.0", eval("5.oclAsType(Real).toString()", self));
		}
	}

	/** §11.5.3 toReal: decimal notation only (#322). */
	@Nested
	class ToReal {

		@Test
		void javaOnlySpellings_areInvalid() throws OclParseException {
			assertInvalid("'NaN'.toReal()", self);
			assertInvalid("'Infinity'.toReal()", self);
			assertInvalid("'1f'.toReal()", self);
			assertInvalid("'0x1p3'.toReal()", self);
			assertInvalid("'abc'.toReal()", self);
		}

		@Test
		void decimalNotations_areReals() throws OclParseException {
			assertEquals(1000.0, eval("'1e3'.toReal()", self));
			assertEquals(-4.25, eval("'-4.25'.toReal()", self));
		}
	}

	/** §11.5.1 round: "When there are two such integers, the largest one." */
	@Nested
	class Rounding {

		@Test
		void ties_roundTowardsPositiveInfinity() throws OclParseException {
			assertEquals(3, eval("(2.5).round()", self));
			assertEquals(-2, eval("(-2.5).round()", self));
			assertEquals(0, eval("(-0.5).round()", self));
			assertEquals(1, eval("(0.5).round()", self));
			assertEquals(-3, eval("(-3.5).round()", self));
		}

		@Test
		void floorAndToInteger() throws OclParseException {
			assertEquals(-3, eval("(-2.5).floor()", self));
			assertEquals(2, eval("2.9.floor()", self));
		}
	}

	/** §11.7.1 max/min/sum over numbers, UnlimitedNatural, Strings and Booleans (#322). */
	@Nested
	class CollectionAggregates {

		@Test
		void maxMin_withUnlimited() throws OclParseException {
			assertEquals(true, eval("Sequence{1, *}->max() = *", self));
			assertEquals(1, eval("Sequence{1, *}->min()", self));
			assertEquals(3, eval("Sequence{3, 1, 2}->max()", self));
		}

		@Test
		void maxMin_onBooleans_areInvalid() throws OclParseException {
			assertInvalid("Sequence{true, false}->max()", self);
			assertInvalid("Sequence{true, false}->min()", self);
		}

		@Test
		void sum_ofStrings_concatenates() throws OclParseException {
			assertEquals("ab", eval("Sequence{'a', 'b'}->sum()", self));
		}

		@Test
		void sum_isExact() throws OclParseException {
			assertEquals(true, eval("Sequence{0.1, 0.2}->sum() = 0.3", self));
			assertEquals(new BigInteger("18446744073709551614"),
					eval("Sequence{9223372036854775807, 9223372036854775807}->sum()", self));
		}

		@Test
		void emptyMaxMin_areInvalid() throws OclParseException {
			assertInvalid("Sequence{}->max()", self);
			assertInvalid("Set{}->min()", self);
		}
	}
}
