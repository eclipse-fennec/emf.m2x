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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclOrderedSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Boundary tests for {@code indexOf('')}, {@code subSequence}, and {@code subOrderedSet}.
 *
 * <p>Spec: OCL v2.4:
 * <ul>
 *   <li>§11.5.3 String {@code indexOf(s)}: "The empty string is a substring of every string
 *       at index 1 (and also at all other indexes)."
 *       <br>post: {@code self.size() = 0 implies result = 0}
 *       <br>post: {@code s.size() = 0 implies result = 1}</li>
 *   <li>§11.7.5 Sequence {@code subSequence(lower, upper)}:
 *       <br>pre: {@code 1 <= lower}, {@code lower <= upper}, {@code upper <= self->size()}</li>
 *   <li>§11.7.3 OrderedSet {@code subOrderedSet(lower, upper)}:
 *       <br>pre: {@code 1 <= lower}, {@code lower <= upper}, {@code upper <= self->size()}</li>
 * </ul>
 *
 * <p>Eclipse reference: {@code GenericEvaluationStringOperationTest#test_indexOf()},
 * {@code GenericEvaluationCollectionOperationTest#testCollectionSubSequence()},
 * {@code testCollectionSubOrderedSet()}.
 */
class OclIndexOfSubSequenceBoundaryTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// ========================================================================
	// String indexOf — empty string boundary (§11.5.3)
	// ========================================================================

	@Nested
	class StringIndexOfBoundary {

		@Test
		void indexOf_emptyString_returnsOne() throws OclParseException {
			// §11.5.3: "The empty string is a substring of every string at index 1"
			// post: s.size() = 0 implies result = 1
			assertEquals(1, eval("'test'.indexOf('')", self));
		}

		@Test
		void emptyString_indexOf_emptyString() throws OclParseException {
			// post: s.size() = 0 implies result = 1 (even for empty self)
			assertEquals(1, eval("''.indexOf('')", self));
		}

		@Test
		void emptyString_indexOf_nonEmpty_returnsZero() throws OclParseException {
			// post: self.size() = 0 implies result = 0
			assertEquals(0, eval("''.indexOf('t')", self));
		}

		@Test
		void indexOf_normal() throws OclParseException {
			// 'test'.indexOf('es') = 2
			assertEquals(2, eval("'test'.indexOf('es')", self));
		}

		@Test
		void indexOf_notFound() throws OclParseException {
			// 'test'.indexOf('xyz') = 0
			assertEquals(0, eval("'test'.indexOf('xyz')", self));
		}

		@Test
		void indexOf_atStart() throws OclParseException {
			assertEquals(1, eval("'test'.indexOf('t')", self));
		}

		@Test
		void indexOf_atEnd() throws OclParseException {
			// 'test'.indexOf('st') = 3
			assertEquals(3, eval("'test'.indexOf('st')", self));
		}
	}

	// ========================================================================
	// subSequence — boundary cases (§11.7.5)
	// pre: 1 <= lower, lower <= upper, upper <= self->size()
	// ========================================================================

	@Nested
	class SubSequenceBoundary {

		@Test
		void valid_singleElement() throws OclParseException {
			// subSequence(1,1) → first element only
			assertEquals(List.of("a"), eval(
					"Sequence{'a', 'b', 'c', 'd'}->subSequence(1, 1)", self));
		}

		@Test
		void valid_middleRange() throws OclParseException {
			assertEquals(List.of("b", "c"), eval(
					"Sequence{'a', 'b', 'c', 'd'}->subSequence(2, 3)", self));
		}

		@Test
		void valid_lastElement() throws OclParseException {
			assertEquals(List.of("d"), eval(
					"Sequence{'a', 'b', 'c', 'd'}->subSequence(4, 4)", self));
		}

		@Test
		void valid_fullRange() throws OclParseException {
			assertEquals(List.of("a", "b", "c", "d"), eval(
					"Sequence{'a', 'b', 'c', 'd'}->subSequence(1, 4)", self));
		}

		@Test
		void lower_greaterThan_upper_invalid() throws OclParseException {
			// pre: lower <= upper violated
			assertInvalid("Sequence{'a', 'b', 'c', 'd'}->subSequence(2, 1)", self);
		}

		@Test
		void lower_zero_invalid() throws OclParseException {
			// pre: 1 <= lower violated
			assertInvalid("Sequence{'a', 'b', 'c', 'd'}->subSequence(0, 1)", self);
		}

		@Test
		void upper_exceedsSize_invalid() throws OclParseException {
			// pre: upper <= self->size() violated
			assertInvalid("Sequence{'a', 'b', 'c', 'd'}->subSequence(4, 5)", self);
		}

		@Test
		void lower_exceedsSize_invalid() throws OclParseException {
			assertInvalid("Sequence{'a', 'b', 'c', 'd'}->subSequence(5, 5)", self);
		}

		@Test
		void negative_lower_invalid() throws OclParseException {
			assertInvalid("Sequence{'a', 'b'}->subSequence(-1, 1)", self);
		}
	}

	// ========================================================================
	// subOrderedSet — boundary cases (§11.7.3)
	// pre: 1 <= lower, lower <= upper, upper <= self->size()
	// ========================================================================

	@Nested
	class SubOrderedSetBoundary {

		@Test
		void valid_singleElement() throws OclParseException {
			Object result = eval("OrderedSet{'a', 'b', 'c', 'd'}->subOrderedSet(1, 1)", self);
			assertInstanceOf(OclOrderedSet.class, result);
			assertEquals(1, ((java.util.Collection<?>) result).size());
		}

		@Test
		void valid_middleRange() throws OclParseException {
			Object result = eval("OrderedSet{'a', 'b', 'c', 'd'}->subOrderedSet(2, 3)", self);
			assertInstanceOf(OclOrderedSet.class, result);
			assertEquals(2, ((java.util.Collection<?>) result).size());
		}

		@Test
		void valid_lastElement() throws OclParseException {
			Object result = eval("OrderedSet{'a', 'b', 'c', 'd'}->subOrderedSet(4, 4)", self);
			assertInstanceOf(OclOrderedSet.class, result);
			assertEquals(1, ((java.util.Collection<?>) result).size());
		}

		@Test
		void lower_greaterThan_upper_invalid() throws OclParseException {
			// pre: lower <= upper violated
			assertInvalid("OrderedSet{'a', 'b', 'c', 'd'}->subOrderedSet(2, 1)", self);
		}

		@Test
		void lower_zero_invalid() throws OclParseException {
			// pre: 1 <= lower violated
			assertInvalid("OrderedSet{'a', 'b', 'c', 'd'}->subOrderedSet(0, 1)", self);
		}

		@Test
		void upper_exceedsSize_invalid() throws OclParseException {
			// pre: upper <= self->size() violated
			assertInvalid("OrderedSet{'a', 'b', 'c', 'd'}->subOrderedSet(4, 5)", self);
		}

		@Test
		void lower_exceedsSize_invalid() throws OclParseException {
			assertInvalid("OrderedSet{'a', 'b', 'c', 'd'}->subOrderedSet(5, 5)", self);
		}

		@Test
		void negative_lower_invalid() throws OclParseException {
			assertInvalid("OrderedSet{'a', 'b'}->subOrderedSet(-1, 1)", self);
		}
	}

	// ========================================================================
	// Collection indexOf — element not found (§11.7.3, §11.7.5)
	// ========================================================================

	@Nested
	class CollectionIndexOfBoundary {

		@Test
		void sequence_indexOf_notFound_invalid() throws OclParseException {
			// Spec: indexOf on Sequence/OrderedSet when element not present → invalid
			// (precondition violated: "obj must be contained in the source")
			assertInvalid("Sequence{1, 2, 3}->indexOf(5)", self);
		}

		@Test
		void orderedSet_indexOf_notFound_invalid() throws OclParseException {
			assertInvalid("OrderedSet{1, 2, 3}->indexOf(5)", self);
		}

		@Test
		void sequence_indexOf_found() throws OclParseException {
			assertEquals(2, eval("Sequence{'a', 'b', 'c'}->indexOf('b')", self));
		}

		@Test
		void orderedSet_indexOf_found() throws OclParseException {
			assertEquals(3, eval("OrderedSet{'a', 'b', 'c'}->indexOf('c')", self));
		}

		@Test
		void sequence_indexOf_first() throws OclParseException {
			assertEquals(1, eval("Sequence{'a', 'b', 'c'}->indexOf('a')", self));
		}

		@Test
		void sequence_indexOf_crossType() throws OclParseException {
			// §11.5.1: 4 = 4.0 → indexOf should find via OCL equality
			assertEquals(1, eval("Sequence{4.0, 5}->indexOf(4)", self));
		}
	}
}
