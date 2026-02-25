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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for multi-variable iterator validation.
 *
 * <p>Spec: OCL v2.5 §7.7.6 / §11.9 (iterator declarations in stdlib):
 * <ul>
 *   <li>{@code forAll} and {@code exists}: up to 3 iterator variables (Cartesian product)</li>
 *   <li>All other iterators ({@code collect}, {@code select}, {@code reject}, {@code any},
 *       {@code one}, {@code isUnique}, {@code sortedBy}, {@code closure}): exactly 1 variable</li>
 * </ul>
 *
 * <p>Eclipse reference: {@code GenericIteratorsTest#test_invalidMultipleIteratorVariables()},
 * {@code IteratorsTest4#test_invalidMultipleIteratorVariables()}.
 */
class OclMultiVarIteratorValidationTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// ========================================================================
	// forAll — multi-var is VALID (Cartesian product, up to 3 vars)
	// ========================================================================

	@Nested
	class ForAllMultiVar {

		@Test
		void twoVars_allPairsPositive() throws OclParseException {
			// Sequence{1,2,3,4}->forAll(e1, e2 | (e1 + e2) > e1) → true
			assertEquals(true, eval(
					"Sequence{1, 2, 3, 4}->forAll(e1, e2 | (e1 + e2) > e1)", self));
		}

		@Test
		void twoVars_notAllEqual() throws OclParseException {
			// Sequence{1,2,3,4}->forAll(e1, e2 | e1 = e2) → false
			assertEquals(false, eval(
					"Sequence{1, 2, 3, 4}->forAll(e1, e2 | e1 = e2)", self));
		}

		@Test
		void twoVars_emptyCollection() throws OclParseException {
			// Sequence{}->forAll(e1, e2 | e1 = e2) → true (vacuously)
			assertEquals(true, eval(
					"Sequence{}->forAll(e1, e2 | e1 = e2)", self));
		}

		@Test
		void threeVars_valid() throws OclParseException {
			// Sequence{1,2,3,4}->forAll(e1, e2, e3 | (e1 + e2 + e3) > e1) → true
			assertEquals(true, eval(
					"Sequence{1, 2, 3, 4}->forAll(e1, e2, e3 | (e1 + e2 + e3) > e1)", self));
		}

		@Test
		void threeVars_negativeResult() throws OclParseException {
			// Sequence{2,3,4,6,12}->forAll(e1, e2, e3 | e1 * e2 * e3 <> 72) → false
			assertEquals(false, eval(
					"Sequence{2, 3, 4, 6, 12}->forAll(e1, e2, e3 | e1 * e2 * e3 <> 72)", self));
		}
	}

	// ========================================================================
	// exists — multi-var is VALID (Cartesian product, up to 3 vars)
	// ========================================================================

	@Nested
	class ExistsMultiVar {

		@Test
		void twoVars_pairExists() throws OclParseException {
			// Sequence{1,2,3,4}->exists(e1, e2 | e1 = e2) → true
			assertEquals(true, eval(
					"Sequence{1, 2, 3, 4}->exists(e1, e2 | e1 = e2)", self));
		}

		@Test
		void twoVars_noPairExists() throws OclParseException {
			// Sequence{1,2,3,4}->exists(e1, e2 | (e1 + e2) = 0) → false
			assertEquals(false, eval(
					"Sequence{1, 2, 3, 4}->exists(e1, e2 | (e1 + e2) = 0)", self));
		}

		@Test
		void twoVars_emptyCollection() throws OclParseException {
			// Sequence{}->exists(e1, e2 | e1 = e2) → false
			assertEquals(false, eval(
					"Sequence{}->exists(e1, e2 | e1 = e2)", self));
		}

		@Test
		void threeVars_valid() throws OclParseException {
			// Sequence{1,2,3,4}->exists(e1, e2, e3 | (e1 + e2 + e3) > 2) → true
			assertEquals(true, eval(
					"Sequence{1, 2, 3, 4}->exists(e1, e2, e3 | (e1 + e2 + e3) > 2)", self));
		}

		@Test
		void threeVars_productCheck() throws OclParseException {
			// Sequence{2,3,4,6,12}->exists(e1, e2, e3 | e1 * e2 * e3 = 72) → true
			assertEquals(true, eval(
					"Sequence{2, 3, 4, 6, 12}->exists(e1, e2, e3 | e1 * e2 * e3 = 72)", self));
		}

		@Test
		void threeVars_noMatch() throws OclParseException {
			// Sequence{2,3,4,6,12}->exists(e1, e2, e3 | e1 * e2 * e3 = 73) → false
			assertEquals(false, eval(
					"Sequence{2, 3, 4, 6, 12}->exists(e1, e2, e3 | e1 * e2 * e3 = 73)", self));
		}
	}

	// ========================================================================
	// Single-var-only iterators — 2+ variables MUST fail
	// Eclipse: SemanticException / TooManyIteratorVariables_ERROR_
	// ========================================================================

	@Nested
	class CollectRejectsMultiVar {

		@Test
		void collect_twoVars_fails() {
			// collect allows exactly 1 variable
			assertThrows(OclParseException.class, () ->
					eval("Sequence{'a', 'b', 'c'}->collect(e1, e2 | e1)", self));
		}
	}

	@Nested
	class SelectRejectsMultiVar {

		@Test
		void select_twoVars_fails() {
			assertThrows(OclParseException.class, () ->
					eval("Sequence{1, 2, 3}->select(e1, e2 | e1 > 1)", self));
		}
	}

	@Nested
	class RejectRejectsMultiVar {

		@Test
		void reject_twoVars_fails() {
			assertThrows(OclParseException.class, () ->
					eval("Sequence{1, 2, 3}->reject(e1, e2 | e1 > 1)", self));
		}
	}

	@Nested
	class AnyRejectsMultiVar {

		@Test
		void any_twoVars_fails() {
			assertThrows(OclParseException.class, () ->
					eval("Sequence{1, 2, 3}->any(e1, e2 | e1 > 1)", self));
		}
	}

	@Nested
	class OneRejectsMultiVar {

		@Test
		void one_twoVars_fails() {
			assertThrows(OclParseException.class, () ->
					eval("Sequence{1, 2, 3}->one(e1, e2 | e1 > 2)", self));
		}
	}

	@Nested
	class IsUniqueRejectsMultiVar {

		@Test
		void isUnique_twoVars_fails() {
			assertThrows(OclParseException.class, () ->
					eval("Sequence{1, 2, 3}->isUnique(e1, e2 | e1)", self));
		}
	}

	@Nested
	class SortedByRejectsMultiVar {

		@Test
		void sortedBy_twoVars_fails() {
			assertThrows(OclParseException.class, () ->
					eval("Sequence{3, 1, 2}->sortedBy(e1, e2 | e1)", self));
		}
	}
}
