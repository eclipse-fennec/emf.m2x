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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@code sum()} collection operation edge cases.
 *
 * <p>Spec references:
 * <ul>
 *   <li>§11.7.1: {@code sum() : T} — The addition of all elements in self.
 *       Elements must support the + operation.</li>
 *   <li>§11.7.1 Well-formedness [1]: A collection cannot contain invalid values.</li>
 *   <li>§11.2.3 OclVoid: null property/operation → invalid</li>
 * </ul>
 *
 * <p>Eclipse reference: {@code GenericEvaluationCollectionOperationTest#testCollectionSum()},
 * {@code testCollectionSumNullValue()}, {@code testCollectionSumInvalidValue()},
 * {@code CollectionsTest#test_sum()}, {@code test_sum_emptyIntegerCollection_204753()}
 *
 * <p>⚠️ SPEC-FIRST: Tests are written against the spec. If they fail,
 * the implementation has a gap — fix the implementation, NOT the test.
 */
class OclCollectionSumEdgeCaseTest extends AbstractOclTest {

	static EObject self;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
		EObject bob = createPerson("Bob", 25, 40000.0, false);
		EObject carol = createPerson("Carol", 35, 60000.0, true);
		company = createCompany("ACME", self, bob, carol);
	}

	// ========================================================================
	// Basic sum — all collection types
	// Eclipse: CollectionsTest#test_sum
	// ========================================================================

	@Nested
	class BasicSum {

		@Test
		void sum_sequence_withDuplicates() throws OclParseException {
			// Sequence{1, 2, 3, 3, 4, 5}->sum() = 18 (duplicates counted)
			assertEquals(18, eval("Sequence{1, 2, 3, 3, 4, 5}->sum()", self));
		}

		@Test
		void sum_set_deduplicates() throws OclParseException {
			// Set{1, 2, 3, 3, 4, 5}->sum() = 15 (duplicate 3 removed)
			assertEquals(15, eval("Set{1, 2, 3, 3, 4, 5}->sum()", self));
		}

		@Test
		void sum_bag_withDuplicates() throws OclParseException {
			// Bag{1, 2, 3, 3, 4, 5}->sum() = 18 (duplicates counted)
			assertEquals(18, eval("Bag{1, 2, 3, 3, 4, 5}->sum()", self));
		}

		@Test
		void sum_orderedSet_deduplicates() throws OclParseException {
			// OrderedSet{1, 2, 3, 3, 4, 5}->sum() = 15 (duplicate 3 removed)
			assertEquals(15, eval("OrderedSet{1, 2, 3, 3, 4, 5}->sum()", self));
		}

		@Test
		void sum_integers() throws OclParseException {
			assertEquals(15, eval("Sequence{1, 2, 3, 4, 5}->sum()", self));
		}

		@Test
		void sum_singleInteger() throws OclParseException {
			assertEquals(42, eval("Sequence{42}->sum()", self));
		}

		@Test
		void sum_negativeIntegers() throws OclParseException {
			assertEquals(-6, eval("Sequence{-1, -2, -3}->sum()", self));
		}

		@Test
		void sum_mixedSignIntegers() throws OclParseException {
			assertEquals(0, eval("Sequence{-5, 0, 5}->sum()", self));
		}
	}

	// ========================================================================
	// Sum with Real values
	// ========================================================================

	@Nested
	class RealSum {

		@Test
		void sum_reals() throws OclParseException {
			Object result = eval("Sequence{1.5, 2.5, 3.0}->sum()", self);
			assertInstanceOf(Double.class, result);
			assertEquals(7.0, result);
		}

		@Test
		void sum_reals_withDuplicates() throws OclParseException {
			// Sequence{4.0, 4.0, 5.0}->sum() = 13.0
			Object result = eval("Sequence{4.0, 4.0, 5.0}->sum()", self);
			assertInstanceOf(Double.class, result);
			assertEquals(13.0, result);
		}

		@Test
		void sum_singleReal() throws OclParseException {
			assertEquals(3.14, eval("Sequence{3.14}->sum()", self));
		}
	}

	// ========================================================================
	// Mixed Integer/Real — result should be Real
	// Eclipse: GenericEvaluationCollectionOperationTest#testCollectionSum
	// ========================================================================

	@Nested
	class MixedTypes {

		@Test
		void sum_pureInteger_returnsInteger() throws OclParseException {
			// Bag{4, 4, 5}->sum() = 13 (Integer)
			Object intResult = eval("Bag{4, 4, 5}->sum()", self);
			assertInstanceOf(Integer.class, intResult);
			assertEquals(13, intResult);
		}

		@Test
		void sum_pureReal_returnsReal() throws OclParseException {
			// Sequence{4.0, 4.0, 5.0}->sum() = 13.0 (Real)
			Object realResult = eval("Sequence{4.0, 4.0, 5.0}->sum()", self);
			assertInstanceOf(Double.class, realResult);
			assertEquals(13.0, realResult);
		}
	}

	// ========================================================================
	// Empty collection — returns 0 or 0.0
	// Eclipse: CollectionsTest#test_sum_emptyIntegerCollection_204753
	// ========================================================================

	@Nested
	class EmptyCollection {

		@Test
		void sum_emptySequence_returnsZero() throws OclParseException {
			assertEquals(0, eval("Sequence{}->sum()", self));
		}

		@Test
		void sum_emptySet_returnsZero() throws OclParseException {
			assertEquals(0, eval("Set{}->sum()", self));
		}

		@Test
		void sum_emptyBag_returnsZero() throws OclParseException {
			assertEquals(0, eval("Bag{}->sum()", self));
		}

		@Test
		void sum_emptyOrderedSet_returnsZero() throws OclParseException {
			assertEquals(0, eval("OrderedSet{}->sum()", self));
		}
	}

	// ========================================================================
	// Null elements — should return invalid (§11.2.3)
	// Eclipse: testCollectionSumNullValue
	// ========================================================================

	@Nested
	class NullElements {

		@Test
		void sum_sequence_withNull_isInvalid() throws OclParseException {
			// §11.2.3: null + anything → invalid
			assertInvalid("Sequence{4.0, null, 5.0}->sum()", self);
		}

		@Test
		void sum_bag_withNull_isInvalid() throws OclParseException {
			assertInvalid("Bag{4, null, 5}->sum()", self);
		}

		@Test
		void sum_set_withNull_isInvalid() throws OclParseException {
			assertInvalid("Set{4, null, 5}->sum()", self);
		}

		@Test
		void sum_orderedSet_withNull_isInvalid() throws OclParseException {
			assertInvalid("OrderedSet{4.0, null, 5.0}->sum()", self);
		}
	}

	// ========================================================================
	// Invalid elements — should return invalid (§11.7.1 WFR [1])
	// Eclipse: testCollectionSumInvalidValue
	// ========================================================================

	@Nested
	class InvalidElements {

		@Test
		void sum_sequence_withInvalid_isInvalid() throws OclParseException {
			assertInvalid("Sequence{4.0, invalid, 5.0}->sum()", self);
		}

		@Test
		void sum_bag_withInvalid_isInvalid() throws OclParseException {
			assertInvalid("Bag{4, invalid, 5}->sum()", self);
		}

		@Test
		void sum_set_withInvalid_isInvalid() throws OclParseException {
			assertInvalid("Set{4, invalid, 5}->sum()", self);
		}

		@Test
		void sum_orderedSet_withInvalid_isInvalid() throws OclParseException {
			assertInvalid("OrderedSet{4.0, invalid, 5.0}->sum()", self);
		}
	}

	// ========================================================================
	// Invalid/null source
	// Eclipse: testCollectionSumInvalid, testCollectionSumNull
	// ========================================================================

	@Nested
	class InvalidNullSource {

		@Test
		void sum_invalidSource_isInvalid() throws OclParseException {
			assertInvalid("invalid->sum()", self);
		}

		@Test
		void sum_nullSource_isEmpty() throws OclParseException {
			// §9.3.35[B]: null->sum() = Set{}->sum() = 0
			assertEquals(0, eval("null->sum()", self));
		}
	}

	// ========================================================================
	// Scalar sum — Arrow on non-collection (§9.3.35[B])
	// Eclipse: testCollectionSum — "4->sum()" = 4
	// ========================================================================

	@Nested
	class ScalarSum {

		@Test
		void integer_arrow_sum() throws OclParseException {
			// 4->sum() = Set{4}->sum() = 4
			assertEquals(4, eval("4->sum()", self));
		}

		@Test
		void real_arrow_sum() throws OclParseException {
			// 3.14->sum() = Set{3.14}->sum() = 3.14
			assertEquals(3.14, eval("3.14->sum()", self));
		}
	}

	// ========================================================================
	// Sum on ranges and with model data
	// ========================================================================

	@Nested
	class RangesAndModel {

		@Test
		void sum_range_1to10() throws OclParseException {
			assertEquals(55, eval("Sequence{1..10}->sum()", self));
		}

		@Test
		void sum_range_1to100() throws OclParseException {
			assertEquals(5050, eval("Sequence{1..100}->sum()", self));
		}

		@Test
		void sum_salaries() throws OclParseException {
			assertEquals(150000.0, eval(
					"self.employees->collect(e | e.salary)->sum()", company));
		}

		@Test
		void sum_ages() throws OclParseException {
			assertEquals(90, eval(
					"self.employees->collect(e | e.age)->sum()", company));
		}
	}

	// ========================================================================
	// Sum chained with other operations
	// ========================================================================

	@Nested
	class ChainedOps {

		@Test
		void sum_afterSelect() throws OclParseException {
			assertEquals(12, eval("Sequence{1, 2, 3, 4, 5}->select(x | x > 2)->sum()", self));
		}

		@Test
		void sum_afterReject() throws OclParseException {
			assertEquals(6, eval("Sequence{1, 2, 3, 4, 5}->reject(x | x > 3)->sum()", self));
		}

		@Test
		void collect_thenSum() throws OclParseException {
			assertEquals(12, eval("Sequence{1, 2, 3}->collect(x | x * 2)->sum()", self));
		}

		@Test
		void sum_inLet() throws OclParseException {
			assertEquals(true, eval(
					"let total : Integer = Sequence{1, 2, 3}->sum() in total = 6", self));
		}

		@Test
		void sum_equality() throws OclParseException {
			assertEquals(true, eval("Sequence{10, 20, 30}->sum() = 60", self));
		}
	}
}
