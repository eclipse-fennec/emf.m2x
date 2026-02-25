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
 * Tests for Integer/Real cross-type equality in collection operations.
 *
 * <p>Spec references:
 * <ul>
 *   <li>§11.4.2: Integer is a subclass of Real</li>
 *   <li>§11.5.1: Real {@code =(r:Real):Boolean} — Integer can be passed as Real parameter</li>
 *   <li>§11.7.1: {@code count} post: {@code if elem = object then acc + 1}</li>
 *   <li>§11.7.1: {@code includes} post: {@code self->count(object) > 0}</li>
 *   <li>§11.7.1: {@code excludes} post: {@code self->count(object) = 0}</li>
 * </ul>
 *
 * <p>Since Integer is a subclass of Real, {@code 4 = 4.0} must be {@code true}.
 * Therefore {@code count}, {@code includes}, and {@code excludes} must use
 * OCL numeric equality, not Java object equality.
 *
 * <p>⚠️ Note: Eclipse OCL has a <b>known bug</b> here — their {@code includes}/
 * {@code excludes} use Java equality, so {@code Sequence{3}->includes(3.0)} returns
 * {@code false}. This is spec-non-conformant. Fennec uses OCL equality (spec-correct).
 *
 * <p>Eclipse reference: {@code GenericEvaluationCollectionOperationTest#testCollectionCount()},
 * {@code testCollectionIncludes()}, {@code testCollectionExcludes()}
 *
 * <p>⚠️ SPEC-FIRST: Tests are written against the spec. If they fail,
 * the implementation has a gap — fix the implementation, NOT the test.
 */
class OclNumericCoercionCollectionTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// ========================================================================
	// Baseline: = operator has cross-type numeric equality (§11.5.1)
	// ========================================================================

	@Nested
	class EqualityBaseline {

		@Test
		void integer_equals_real_sameValue() throws OclParseException {
			assertEquals(true, eval("1 = 1.0", self));
		}

		@Test
		void real_equals_integer_sameValue() throws OclParseException {
			assertEquals(true, eval("1.0 = 1", self));
		}

		@Test
		void integer_notEquals_real_differentValue() throws OclParseException {
			assertEquals(false, eval("1 = 4.0", self));
		}

		@Test
		void integer_equals_integer() throws OclParseException {
			assertEquals(true, eval("4 = 4", self));
		}

		@Test
		void real_equals_real() throws OclParseException {
			assertEquals(true, eval("4.0 = 4.0", self));
		}
	}

	// ========================================================================
	// count — must use OCL equality (§11.7.1: if elem = object)
	// Eclipse: testCollectionCount — Sequence count(4.0) = 3 (matching 4.0, 4, 4.0)
	// ========================================================================

	@Nested
	class CountCoercion {

		@Test
		void sequence_count_realMatchesInteger() throws OclParseException {
			// Sequence{3, 'test', 4.0, 4, 4.0, 'test'}->count(4.0)
			// 4.0 matches: 4.0, 4, 4.0 → 3
			assertEquals(3, eval(
					"Sequence{3, 'test', 4.0, 4, 4.0, 'test'}->count(4.0)", self));
		}

		@Test
		void sequence_count_integerMatchesReal() throws OclParseException {
			// count(4) should also match 4.0 values
			assertEquals(3, eval(
					"Sequence{3, 'test', 4.0, 4, 4.0, 'test'}->count(4)", self));
		}

		@Test
		void sequence_count_noCoercionForString() throws OclParseException {
			// 'test' should only match 'test', not numbers
			assertEquals(2, eval(
					"Sequence{3, 'test', 4.0, 4, 4.0, 'test'}->count('test')", self));
		}

		@Test
		void sequence_count_integerOnly() throws OclParseException {
			// 3 appears once; 3.0 is not in the collection
			assertEquals(1, eval(
					"Sequence{3, 'test', 4.0, 4, 4.0, 'test'}->count(3)", self));
		}

		@Test
		void bag_count_realMatchesInteger() throws OclParseException {
			assertEquals(3, eval(
					"Bag{3, 'test', 4.0, 4, 4.0, 'test'}->count(4.0)", self));
		}

		@Test
		void set_count_crossType() throws OclParseException {
			// Set{3, 4.0, 4} — after deduplication, 4.0 and 4 are equal → Set{3, 4.0}
			// count(4.0) = 1
			assertEquals(1, eval("Set{3, 4.0, 4}->count(4.0)", self));
		}

		@Test
		void set_count_integerMatchesReal() throws OclParseException {
			// count(4) on Set{3, 4.0} → 1 (4 = 4.0)
			assertEquals(1, eval("Set{3, 4.0}->count(4)", self));
		}

		@Test
		void orderedSet_count_crossType() throws OclParseException {
			assertEquals(1, eval("OrderedSet{3, 4.0, 4}->count(4.0)", self));
		}

		@Test
		void count_notFound_crossType() throws OclParseException {
			assertEquals(0, eval(
					"Sequence{3, 'test', 4.0, 4, 4.0, 'test'}->count(0)", self));
		}
	}

	// ========================================================================
	// includes — must use OCL equality (§11.7.1: self->count(object) > 0)
	// ⚠️ Eclipse has a known bug: uses Java equality → spec-non-conformant
	// ========================================================================

	@Nested
	class IncludesCoercion {

		@Test
		void sequence_includes_realMatchesInteger() throws OclParseException {
			// Sequence{3, 4.0}->includes(3.0) = true (3 = 3.0)
			assertEquals(true, eval("Sequence{3, 4.0}->includes(3.0)", self));
		}

		@Test
		void sequence_includes_integerMatchesReal() throws OclParseException {
			// Sequence{3, 4.0}->includes(4) = true (4.0 = 4)
			assertEquals(true, eval("Sequence{3, 4.0}->includes(4)", self));
		}

		@Test
		void set_includes_crossType() throws OclParseException {
			assertEquals(true, eval("Set{3, 4.0}->includes(3.0)", self));
		}

		@Test
		void bag_includes_crossType() throws OclParseException {
			assertEquals(true, eval("Bag{3, 4.0}->includes(4)", self));
		}

		@Test
		void orderedSet_includes_crossType() throws OclParseException {
			assertEquals(true, eval("OrderedSet{3, 4.0}->includes(3.0)", self));
		}

		@Test
		void includes_noCoercionForString() throws OclParseException {
			// String '3' should NOT match Integer 3
			assertEquals(false, eval("Sequence{3, 4.0}->includes('3')", self));
		}

		@Test
		void includes_sameType() throws OclParseException {
			assertEquals(true, eval("Sequence{3, 4.0, 'test'}->includes(3)", self));
			assertEquals(true, eval("Sequence{3, 4.0, 'test'}->includes('test')", self));
		}
	}

	// ========================================================================
	// excludes — inverse of includes (§11.7.1: self->count(object) = 0)
	// ========================================================================

	@Nested
	class ExcludesCoercion {

		@Test
		void sequence_excludes_realMatchesInteger() throws OclParseException {
			// Sequence{3, 4.0}->excludes(3.0) = false (3 = 3.0 → included)
			assertEquals(false, eval("Sequence{3, 4.0}->excludes(3.0)", self));
		}

		@Test
		void sequence_excludes_integerMatchesReal() throws OclParseException {
			assertEquals(false, eval("Sequence{3, 4.0}->excludes(4)", self));
		}

		@Test
		void set_excludes_crossType() throws OclParseException {
			assertEquals(false, eval("Set{3, 4.0}->excludes(3.0)", self));
		}

		@Test
		void excludes_absent() throws OclParseException {
			assertEquals(true, eval("Sequence{3, 4.0}->excludes(5)", self));
		}

		@Test
		void excludes_noCoercionForString() throws OclParseException {
			assertEquals(true, eval("Sequence{3, 4.0}->excludes('3')", self));
		}
	}

	// ========================================================================
	// includesAll / excludesAll — cross-type
	// ========================================================================

	@Nested
	class IncludesAllExcludesAll {

		@Test
		void includesAll_crossType() throws OclParseException {
			// Set{1, 2, 3}->includesAll(Set{1.0, 2.0}) = true
			assertEquals(true, eval("Set{1, 2, 3}->includesAll(Set{1.0, 2.0})", self));
		}

		@Test
		void includesAll_crossType_missing() throws OclParseException {
			assertEquals(false, eval("Set{1, 2, 3}->includesAll(Set{1.0, 5.0})", self));
		}

		@Test
		void excludesAll_crossType() throws OclParseException {
			// Set{1, 2, 3}->excludesAll(Set{4.0, 5.0}) = true
			assertEquals(true, eval("Set{1, 2, 3}->excludesAll(Set{4.0, 5.0})", self));
		}

		@Test
		void excludesAll_crossType_overlap() throws OclParseException {
			// Set{1, 2, 3}->excludesAll(Set{2.0, 5.0}) = false (2.0 = 2)
			assertEquals(false, eval("Set{1, 2, 3}->excludesAll(Set{2.0, 5.0})", self));
		}
	}

	// ========================================================================
	// indexOf — cross-type (already tested in GAP-16, verify here)
	// ========================================================================

	@Nested
	class IndexOfCoercion {

		@Test
		void indexOf_integerMatchesReal() throws OclParseException {
			// Sequence{1, 2, 3}->indexOf(2.0) = 2
			assertEquals(2, eval("Sequence{1, 2, 3}->indexOf(2.0)", self));
		}

		@Test
		void indexOf_realMatchesInteger() throws OclParseException {
			// Sequence{1.0, 2.0, 3.0}->indexOf(2) = 2
			assertEquals(2, eval("Sequence{1.0, 2.0, 3.0}->indexOf(2)", self));
		}
	}
}
