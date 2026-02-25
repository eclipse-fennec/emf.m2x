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
import org.junit.jupiter.api.Test;

/**
 * Tests for chaining multiple collection operations together,
 * verifying that intermediate results are correctly passed along.
 */
class OclChainedCollectionOpTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Select → Collect ---

	@Test
	void selectThenCollect() throws OclParseException {
		// select > 2 → {3,4,5}, collect *10 → {30,40,50}, sum = 120
		assertEquals(120, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i > 2)->collect(i | i * 10)->sum()", self));
	}

	@Test
	void selectThenCollect_size() throws OclParseException {
		assertEquals(3, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i > 2)->collect(i | i * 2)->size()", self));
	}

	// --- Collect → Select ---

	@Test
	void collectThenSelect() throws OclParseException {
		// collect *2 → {2,4,6,8,10}, select > 5 → {6,8,10}, size = 3
		assertEquals(3, eval(
				"Sequence{1, 2, 3, 4, 5}->collect(i | i * 2)->select(i | i > 5)->size()", self));
	}

	// --- Select → Reject ---

	@Test
	void selectThenReject() throws OclParseException {
		// select > 1 → {2,3,4,5}, reject > 4 → {2,3,4}, size = 3
		assertEquals(3, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i > 1)->reject(i | i > 4)->size()", self));
	}

	// --- Reject → Select ---

	@Test
	void rejectThenSelect() throws OclParseException {
		// reject < 3 → {3,4,5}, select < 5 → {3,4}, size = 2
		assertEquals(2, eval(
				"Sequence{1, 2, 3, 4, 5}->reject(i | i < 3)->select(i | i < 5)->size()", self));
	}

	// --- Multiple selects ---

	@Test
	void doubleSelect() throws OclParseException {
		// select > 1 → {2,3,4,5}, select < 5 → {2,3,4}, size = 3
		assertEquals(3, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i > 1)->select(i | i < 5)->size()", self));
	}

	// --- Collect → SortedBy ---

	@Test
	void collectThenSortedBy() throws OclParseException {
		// collect: negate → {-3,-1,-2}, sortedBy identity → {-3,-2,-1}, first = -3
		assertEquals(-3, eval(
				"Sequence{3, 1, 2}->collect(i | 0 - i)->sortedBy(i | i)->first()", self));
	}

	// --- SortedBy → first/last ---

	@Test
	void sortedByThenFirst() throws OclParseException {
		assertEquals(1, eval(
				"Sequence{5, 3, 1, 4, 2}->sortedBy(i | i)->first()", self));
	}

	@Test
	void sortedByThenLast() throws OclParseException {
		assertEquals(5, eval(
				"Sequence{5, 3, 1, 4, 2}->sortedBy(i | i)->last()", self));
	}

	// --- Select → Size ---

	@Test
	void selectThenSize() throws OclParseException {
		assertEquals(2, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i > 3)->size()", self));
	}

	// --- Collect → Sum ---

	@Test
	void collectThenSum() throws OclParseException {
		assertEquals(30, eval(
				"Sequence{1, 2, 3, 4, 5}->collect(i | i * 2)->sum()", self));
	}

	// --- Select → forAll ---

	@Test
	void selectThenForAll() throws OclParseException {
		// select > 3 → {4,5}, forAll > 3 → true
		assertEquals(true, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i > 3)->forAll(i | i > 3)", self));
	}

	// --- Select → exists ---

	@Test
	void selectThenExists() throws OclParseException {
		// select > 3 → {4,5}, exists = 5 → true
		assertEquals(true, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i > 3)->exists(i | i = 5)", self));
	}

	// --- Collect → asSet → size (dedup) ---

	@Test
	void collectThenAsSetThenSize() throws OclParseException {
		// collect mod 2 → {1,0,1,0,1}, asSet → {0,1}, size = 2
		assertEquals(2, eval(
				"Sequence{1, 2, 3, 4, 5}->collect(i | i.mod(2))->asSet()->size()", self));
	}

	// --- Select → collect → sum ---

	@Test
	void selectCollectSum_pipeline() throws OclParseException {
		// select even → {2,4}, collect *3 → {6,12}, sum = 18
		assertEquals(18, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i.mod(2) = 0)->collect(i | i * 3)->sum()", self));
	}

	// --- Including → size ---

	@Test
	void includingThenSize() throws OclParseException {
		assertEquals(4, eval("Sequence{1, 2, 3}->including(4)->size()", self));
	}

	// --- Excluding → size ---

	@Test
	void excludingThenSize() throws OclParseException {
		assertEquals(2, eval("Sequence{1, 2, 3}->excluding(2)->size()", self));
	}

	// --- Append → last ---

	@Test
	void appendThenLast() throws OclParseException {
		assertEquals(99, eval("Sequence{1, 2, 3}->append(99)->last()", self));
	}

	// --- Prepend → first ---

	@Test
	void prependThenFirst() throws OclParseException {
		assertEquals(99, eval("Sequence{1, 2, 3}->prepend(99)->first()", self));
	}

	// --- Union → size ---

	@Test
	void unionThenSize() throws OclParseException {
		assertEquals(5, eval(
				"Sequence{1, 2}->union(Sequence{3, 4, 5})->size()", self));
	}

	// --- Triple chain ---

	@Test
	void tripleChain_selectRejectCollect() throws OclParseException {
		// select > 1 → {2,3,4,5}, reject > 4 → {2,3,4}, collect *10 → {20,30,40}, sum = 90
		assertEquals(90, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i > 1)->reject(i | i > 4)->collect(i | i * 10)->sum()",
				self));
	}

	// --- Select → isEmpty / notEmpty ---

	@Test
	void selectThenIsEmpty() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->select(i | i > 10)->isEmpty()", self));
	}

	@Test
	void selectThenNotEmpty() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->select(i | i > 1)->notEmpty()", self));
	}

	// --- Collect → max/min ---

	@Test
	void collectThenMax() throws OclParseException {
		assertEquals(10, eval(
				"Sequence{1, 2, 3, 4, 5}->collect(i | i * 2)->max()", self));
	}

	@Test
	void collectThenMin() throws OclParseException {
		assertEquals(2, eval(
				"Sequence{1, 2, 3, 4, 5}->collect(i | i * 2)->min()", self));
	}
}
