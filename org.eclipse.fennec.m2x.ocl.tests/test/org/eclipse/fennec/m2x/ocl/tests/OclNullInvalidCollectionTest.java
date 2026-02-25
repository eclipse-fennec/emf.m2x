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
 * Null/invalid propagation tests for Collection operations.
 *
 * <p>Per OCL v2.5: any operation on invalid yields invalid.
 * Null as collection receiver (via arrow) gets wrapped into an empty Set,
 * but null.operation() propagates invalid. Invalid receiver always
 * propagates invalid.
 *
 * <p>Also tests: invalid/null as collection elements in operations
 * like sum, includes, count, indexOf.
 */
class OclNullInvalidCollectionTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// === Invalid receiver — all should return invalid ===

	@Nested
	class InvalidReceiver {

		@Test
		void invalid_size() throws OclParseException {
			assertInvalid("invalid->size()", self);
		}

		@Test
		void invalid_isEmpty() throws OclParseException {
			assertInvalid("invalid->isEmpty()", self);
		}

		@Test
		void invalid_notEmpty() throws OclParseException {
			assertInvalid("invalid->notEmpty()", self);
		}

		@Test
		void invalid_includes() throws OclParseException {
			assertInvalid("invalid->includes(1)", self);
		}

		@Test
		void invalid_excludes() throws OclParseException {
			assertInvalid("invalid->excludes(1)", self);
		}

		@Test
		void invalid_includesAll() throws OclParseException {
			assertInvalid("invalid->includesAll(Set{1})", self);
		}

		@Test
		void invalid_excludesAll() throws OclParseException {
			assertInvalid("invalid->excludesAll(Set{1})", self);
		}

		@Test
		void invalid_count() throws OclParseException {
			assertInvalid("invalid->count(1)", self);
		}

		@Test
		void invalid_first() throws OclParseException {
			assertInvalid("invalid->first()", self);
		}

		@Test
		void invalid_last() throws OclParseException {
			assertInvalid("invalid->last()", self);
		}

		@Test
		void invalid_sum() throws OclParseException {
			assertInvalid("invalid->sum()", self);
		}

		@Test
		void invalid_flatten() throws OclParseException {
			assertInvalid("invalid->flatten()", self);
		}

		@Test
		void invalid_at() throws OclParseException {
			assertInvalid("invalid->at(1)", self);
		}

		@Test
		void invalid_indexOf() throws OclParseException {
			assertInvalid("invalid->indexOf(1)", self);
		}

		@Test
		void invalid_including() throws OclParseException {
			assertInvalid("invalid->including(1)", self);
		}

		@Test
		void invalid_excluding() throws OclParseException {
			assertInvalid("invalid->excluding(1)", self);
		}

		@Test
		void invalid_append() throws OclParseException {
			assertInvalid("invalid->append(1)", self);
		}

		@Test
		void invalid_prepend() throws OclParseException {
			assertInvalid("invalid->prepend(1)", self);
		}

		@Test
		void invalid_union() throws OclParseException {
			assertInvalid("invalid->union(Set{1})", self);
		}

		@Test
		void invalid_intersection() throws OclParseException {
			assertInvalid("invalid->intersection(Set{1})", self);
		}

		@Test
		void invalid_select() throws OclParseException {
			assertInvalid("invalid->select(x | true)", self);
		}

		@Test
		void invalid_reject() throws OclParseException {
			assertInvalid("invalid->reject(x | true)", self);
		}

		@Test
		void invalid_collect() throws OclParseException {
			assertInvalid("invalid->collect(x | x)", self);
		}

		@Test
		void invalid_forAll() throws OclParseException {
			assertInvalid("invalid->forAll(x | true)", self);
		}

		@Test
		void invalid_exists() throws OclParseException {
			assertInvalid("invalid->exists(x | true)", self);
		}

		@Test
		void invalid_any() throws OclParseException {
			assertInvalid("invalid->any(x | true)", self);
		}

		@Test
		void invalid_one() throws OclParseException {
			assertInvalid("invalid->one(x | true)", self);
		}

		@Test
		void invalid_isUnique() throws OclParseException {
			assertInvalid("invalid->isUnique(x | x)", self);
		}

		@Test
		void invalid_sortedBy() throws OclParseException {
			assertInvalid("invalid->sortedBy(x | x)", self);
		}

		@Test
		void invalid_iterate() throws OclParseException {
			assertInvalid("invalid->iterate(x; acc : Integer = 0 | acc + x)", self);
		}

		@Test
		void invalid_asSet() throws OclParseException {
			assertInvalid("invalid->asSet()", self);
		}

		@Test
		void invalid_asSequence() throws OclParseException {
			assertInvalid("invalid->asSequence()", self);
		}

		@Test
		void invalid_asBag() throws OclParseException {
			assertInvalid("invalid->asBag()", self);
		}

		@Test
		void invalid_asOrderedSet() throws OclParseException {
			assertInvalid("invalid->asOrderedSet()", self);
		}
	}

	// === Null as element in collection operations ===

	@Nested
	class NullAsElement {

		@Test
		void sum_withNull_isInvalid() throws OclParseException {
			// Eclipse: Sequence{4.0, null, 5.0}->sum() = invalid
			assertInvalid("Sequence{4, null, 5}->sum()", self);
		}

		@Test
		void includes_null() throws OclParseException {
			assertEquals(true, eval("Sequence{1, null, 3}->includes(null)", self));
		}

		@Test
		void excludes_null() throws OclParseException {
			assertEquals(false, eval("Sequence{1, null, 3}->excludes(null)", self));
		}

		@Test
		void count_null() throws OclParseException {
			assertEquals(2, eval("Sequence{1, null, 3, null}->count(null)", self));
		}

		@Test
		void indexOf_null() throws OclParseException {
			// Sequence contains null at position 2
			assertEquals(2, eval("Sequence{1, null, 3}->indexOf(null)", self));
		}
	}

	// === Null as argument to collection operations ===

	@Nested
	class NullArgument {

		@Test
		void including_null() throws OclParseException {
			assertEquals(4, eval("Sequence{1, 2, 3}->including(null)->size()", self));
		}

		@Test
		void excluding_null() throws OclParseException {
			assertEquals(2, eval("Sequence{1, null, 3}->excluding(null)->size()", self));
		}

		@Test
		void append_null() throws OclParseException {
			assertEquals(4, eval("Sequence{1, 2, 3}->append(null)->size()", self));
		}

		@Test
		void prepend_null() throws OclParseException {
			assertEquals(4, eval("Sequence{1, 2, 3}->prepend(null)->size()", self));
		}
	}

	// === Invalid as argument to collection operations ===

	@Nested
	class InvalidArgument {

		@Test
		void includes_invalid() throws OclParseException {
			// includes with invalid arg — invalid propagates
			assertInvalid("Sequence{1, 2, 3}->includes(invalid)", self);
		}

		@Test
		void excludes_invalid() throws OclParseException {
			assertInvalid("Sequence{1, 2, 3}->excludes(invalid)", self);
		}

		@Test
		void count_invalid() throws OclParseException {
			assertInvalid("Sequence{1, 2, 3}->count(invalid)", self);
		}

		@Test
		void indexOf_invalid() throws OclParseException {
			assertInvalid("Sequence{1, 2, 3}->indexOf(invalid)", self);
		}

		@Test
		void at_invalid() throws OclParseException {
			assertInvalid("Sequence{1, 2, 3}->at(invalid)", self);
		}
	}

	// Note: null->operation() (arrow-based) is T-6, not tested here.
	// The implicit wrapping of null to empty Set via -> is a separate concern.

	// === Union/intersection with invalid operand ===

	@Nested
	class SetOpsWithInvalid {

		@Test
		void set_union_invalid() throws OclParseException {
			assertInvalid("Set{1, 2}->union(invalid)", self);
		}

		@Test
		void set_intersection_invalid() throws OclParseException {
			assertInvalid("Set{1, 2}->intersection(invalid)", self);
		}

		@Test
		void set_minus_invalid() throws OclParseException {
			assertInvalid("Set{1, 2} - invalid", self);
		}

		@Test
		void set_symmetricDifference_invalid() throws OclParseException {
			assertInvalid("Set{1, 2}->symmetricDifference(invalid)", self);
		}

		@Test
		void set_includesAll_invalid() throws OclParseException {
			assertInvalid("Set{1, 2}->includesAll(invalid)", self);
		}

		@Test
		void set_excludesAll_invalid() throws OclParseException {
			assertInvalid("Set{1, 2}->excludesAll(invalid)", self);
		}
	}
}
