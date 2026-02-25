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
import org.junit.jupiter.api.Test;

/**
 * Tests for Bag frequency semantics (intersection/subtraction),
 * Real.toReal() passthrough, Set->collect returns Bag (allows duplicates),
 * and sum() precision with large integers.
 */
class OclBagCollectPrecisionTest extends AbstractOclTest {

	static EObject self;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 80000.0, true);
		EObject bob = createPerson("Bob", 30, 40000.0, false);
		EObject carol = createPerson("Carol", 30, 120000.0, true);
		company = createCompany("TechCorp", self, bob, carol);
	}

	// === Gap 10: Bag intersection min-frequency ===

	@Test
	void bagIntersection_minFrequency() throws OclParseException {
		// Bag{1,1,2} ∩ Bag{1,2,2} → min(2,1)=1 for 1, min(1,2)=1 for 2 → Bag{1,2}
		assertEquals(2, eval("Bag{1, 1, 2}->intersection(Bag{1, 2, 2})->size()", self));
	}

	@Test
	void bagIntersection_minFrequency_count() throws OclParseException {
		// Bag{1,1,1} ∩ Bag{1,1} → min(3,2) = 2 copies of 1
		assertEquals(2, eval("Bag{1, 1, 1}->intersection(Bag{1, 1})->size()", self));
	}

	@Test
	void bagIntersection_minFrequency_includes() throws OclParseException {
		// Result should contain element 1 and 2
		assertEquals(true, eval(
				"Bag{1, 1, 2}->intersection(Bag{1, 2, 2})->includes(1)", self));
		assertEquals(true, eval(
				"Bag{1, 1, 2}->intersection(Bag{1, 2, 2})->includes(2)", self));
	}

	@Test
	void bagIntersection_disjoint() throws OclParseException {
		assertEquals(0, eval("Bag{1, 1}->intersection(Bag{2, 2})->size()", self));
	}

	// === Gap 10: Bag subtraction per-occurrence ===

	@Test
	void bagSubtraction_perOccurrence() throws OclParseException {
		// Bag{1,1,2,3} - Bag{1,2} → remove one 1, one 2 → Bag{1,3}
		assertEquals(2, eval("(Bag{1, 1, 2, 3} - Bag{1, 2})->size()", self));
	}

	@Test
	void bagSubtraction_removeAll_copies() throws OclParseException {
		// Bag{1,1,1} - Bag{1,1} → remove two copies → Bag{1}
		assertEquals(1, eval("(Bag{1, 1, 1} - Bag{1, 1})->size()", self));
	}

	@Test
	void bagSubtraction_nothingToRemove() throws OclParseException {
		// Bag{1,2} - Bag{3,4} → no change → Bag{1,2}
		assertEquals(2, eval("(Bag{1, 2} - Bag{3, 4})->size()", self));
	}

	@Test
	void bagSubtraction_removeMore_thanExists() throws OclParseException {
		// Bag{1} - Bag{1,1,1} → remove all → Bag{}
		assertEquals(0, eval("(Bag{1} - Bag{1, 1, 1})->size()", self));
	}

	@Test
	void bagSubtraction_includes() throws OclParseException {
		// Bag{1,1,2,3} - Bag{1} → still has one 1
		assertEquals(true, eval("(Bag{1, 1, 2, 3} - Bag{1})->includes(1)", self));
		assertEquals(true, eval("(Bag{1, 1, 2, 3} - Bag{1})->includes(3)", self));
	}

	// === Gap 11: Real.toReal() passthrough ===

	@Test
	void toReal_onReal_passthrough() throws OclParseException {
		assertEquals(3.14, eval("3.14.toReal()", self));
	}

	@Test
	void toReal_onReal_negative() throws OclParseException {
		assertEquals(-2.5, eval("(-2.5).toReal()", self));
	}

	@Test
	void toReal_onReal_zero() throws OclParseException {
		assertEquals(0.0, eval("0.0.toReal()", self));
	}

	@Test
	void toReal_onReal_property() throws OclParseException {
		assertEquals(80000.0, eval("self.salary.toReal()", self));
	}

	// === Gap 13: Set->collect returns Bag (allows duplicates) ===

	@Test
	void setCollect_allowsDuplicates() throws OclParseException {
		// Set{1,2,3}->collect(i | i.div(2)) can produce duplicates: 0, 1, 1
		// If collect returns a Bag, size = 3 (with duplicates preserved)
		Object result = eval("Set{1, 2, 3}->collect(i | i.div(2))->size()", self);
		assertEquals(3, result);
	}

	@Test
	void setCollect_duplicateCount() throws OclParseException {
		// All three employees have age=30, so collect(e | e.age) produces Bag{30,30,30}
		assertEquals(3, eval(
				"self.employees->asSet()->collect(e | e.age)->size()", company));
	}

	@Test
	void setCollect_resultNotUnique() throws OclParseException {
		// Set{1,2,3,4}->collect(i | i > 2) → Bag{false,false,true,true}
		// count(true) should be 2
		assertEquals(2, eval(
				"Set{1, 2, 3, 4}->collect(i | i > 2)->count(true)", self));
	}

	@Test
	void setCollect_vs_sequenceCollect() throws OclParseException {
		// Both should preserve duplicates from the body expression
		Object setResult = eval("Set{1, 2, 3}->collect(i | 1)->size()", self);
		Object seqResult = eval("Sequence{1, 2, 3}->collect(i | 1)->size()", self);
		assertEquals(3, setResult);
		assertEquals(3, seqResult);
	}

	// === Gap 14: sum() precision — large integers ===

	@Test
	void sum_exceedsIntMaxValue() throws OclParseException {
		// 2000000000 + 2000000000 = 4000000000 — exceeds Integer.MAX_VALUE (2147483647)
		Object result = eval("Sequence{2000000000, 2000000000}->sum()", self);
		assertInstanceOf(Number.class, result);
		assertEquals(4000000000L, ((Number) result).longValue());
	}

	@Test
	void sum_largeNegative() throws OclParseException {
		// -2000000000 + -2000000000 = -4000000000
		Object result = eval("Sequence{-2000000000, -2000000000}->sum()", self);
		assertInstanceOf(Number.class, result);
		assertEquals(-4000000000L, ((Number) result).longValue());
	}

	@Test
	void sum_mixedLargeValues() throws OclParseException {
		// Sum of large values that fits in long but not int
		Object result = eval(
				"Sequence{1000000000, 1000000000, 1000000000}->sum()", self);
		assertInstanceOf(Number.class, result);
		assertEquals(3000000000L, ((Number) result).longValue());
	}

	@Test
	void sum_normalIntegers_stillPrecise() throws OclParseException {
		// Normal-sized integers should still work
		assertEquals(15, eval("Sequence{1, 2, 3, 4, 5}->sum()", self));
	}

	@Test
	void sum_realValues_unaffected() throws OclParseException {
		// Real sum is not affected by the long accumulator change
		assertEquals(6.0, eval("Sequence{1.0, 2.0, 3.0}->sum()", self));
	}

	// --- Combined: collect on Set then sum ---

	@Test
	void setCollect_thenSum() throws OclParseException {
		// Set{1,2,3}->collect(i | i * 10) returns a Bag → sum should work
		assertEquals(60, eval("Set{1, 2, 3}->collect(i | i * 10)->sum()", self));
	}

	// --- Bag intersection then size ---

	@Test
	void bagIntersection_frequency_thenCollect() throws OclParseException {
		// Bag{1,1,2,2,3}->intersection(Bag{1,2,2,4}) = Bag{1,2,2} (freq: min)
		// size = 3
		assertEquals(3, eval(
				"Bag{1, 1, 2, 2, 3}->intersection(Bag{1, 2, 2, 4})->size()", self));
	}

	@Test
	void bagIntersection_frequency_detailed() throws OclParseException {
		// Bag{1,1,2,2,3}->intersection(Bag{1,2,2,4})
		// count(1) = min(2,1) = 1, count(2) = min(2,2) = 2
		assertEquals(1, eval(
				"Bag{1, 1, 2, 2, 3}->intersection(Bag{1, 2, 2, 4})->count(1)", self));
		assertEquals(2, eval(
				"Bag{1, 1, 2, 2, 3}->intersection(Bag{1, 2, 2, 4})->count(2)", self));
	}
}
