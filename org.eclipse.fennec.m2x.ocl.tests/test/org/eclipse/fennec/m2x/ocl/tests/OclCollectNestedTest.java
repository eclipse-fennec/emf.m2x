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
 * Tests for the OCL {@code collectNested} iterator.
 * Unlike {@code collect}, which flattens one level,
 * {@code collectNested} preserves nested collection structure.
 *
 * <p>Also tests the difference between {@code collect} (which flattens)
 * and {@code collectNested} (which preserves nesting).
 */
class OclCollectNestedTest extends AbstractOclTest {

	static EObject self;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);

		EObject bob = createPerson("Bob", 25, 40000.0, false);
		EObject carol = createPerson("Carol", 35, 60000.0, true);
		company = createCompany("Acme", self, bob, carol);
	}

	// --- collectNested basic ---

	@Test
	void collectNested_identity() throws OclParseException {
		assertEquals(3, eval(
				"Sequence{1, 2, 3}->collectNested(i | i)->size()", self));
	}

	@Test
	void collectNested_transform() throws OclParseException {
		assertEquals(12, eval(
				"Sequence{1, 2, 3}->collectNested(i | i * 2)->sum()", self));
	}

	@Test
	void collectNested_strings() throws OclParseException {
		assertEquals(3, eval(
				"Sequence{'a', 'b', 'c'}->collectNested(s | s.toUpperCase())->size()", self));
	}

	// --- collectNested preserves nesting ---

	@Test
	void collectNested_producesNestedResult() throws OclParseException {
		// collectNested preserves nesting: 3 elements → outer size = 3
		assertEquals(3, eval(
				"Sequence{1, 2, 3}->collectNested(i | Sequence{i, i * 2})->size()", self));
	}

	@Test
	void collectNested_nestedSize() throws OclParseException {
		// 3 elements, each maps to a Sequence → outer size = 3
		assertEquals(3, eval(
				"Sequence{1, 2, 3}->collectNested(i | Sequence{i, i * 2})->size()", self));
	}

	// --- collect flattens, collectNested doesn't ---

	@Test
	void collect_flattens() throws OclParseException {
		// collect flattens one level: 3 * 2 elements = 6
		assertEquals(6, eval(
				"Sequence{1, 2, 3}->collect(i | Sequence{i, i * 2})->size()", self));
	}

	@Test
	void collectNested_doesNotFlatten() throws OclParseException {
		// collectNested does NOT flatten: 3 nested sequences = size 3
		assertEquals(3, eval(
				"Sequence{1, 2, 3}->collectNested(i | Sequence{i, i * 2})->size()", self));
	}

	// --- collectNested on model ---

	@Test
	void collectNested_names() throws OclParseException {
		assertEquals(3, eval(
				"self.employees->collectNested(e | e.name)->size()", company));
	}

	@Test
	void collectNested_ages() throws OclParseException {
		assertEquals(3, eval(
				"self.employees->collectNested(e | e.age)->size()", company));
	}

	// --- collectNested on empty collection ---

	@Test
	void collectNested_empty() throws OclParseException {
		assertEquals(0, eval(
				"Sequence{}->collectNested(i | i)->size()", self));
	}

	// --- collectNested on single element ---

	@Test
	void collectNested_singleElement() throws OclParseException {
		assertEquals(1, eval(
				"Sequence{42}->collectNested(i | i * 2)->size()", self));
	}

	// --- collectNested then flatten ---

	@Test
	void collectNested_thenFlatten() throws OclParseException {
		// collectNested preserves nesting, flatten removes it → same as collect
		assertEquals(6, eval(
				"Sequence{1, 2, 3}->collectNested(i | Sequence{i, i * 2})->flatten()->size()",
				self));
	}

	// --- collectNested with boolean body ---

	@Test
	void collectNested_booleans() throws OclParseException {
		assertEquals(3, eval(
				"Sequence{1, 2, 3}->collectNested(i | i > 1)->size()", self));
	}

	// --- collectNested chained ---

	@Test
	void collectNested_thenSelect() throws OclParseException {
		// collectNested doubles → {2,4,6}, select > 3 → {4,6}
		assertEquals(2, eval(
				"Sequence{1, 2, 3}->collectNested(i | i * 2)->select(i | i > 3)->size()",
				self));
	}

	@Test
	void collectNested_thenMax() throws OclParseException {
		assertEquals(6, eval(
				"Sequence{1, 2, 3}->collectNested(i | i * 2)->max()", self));
	}

	@Test
	void collectNested_thenMin() throws OclParseException {
		assertEquals(2, eval(
				"Sequence{1, 2, 3}->collectNested(i | i * 2)->min()", self));
	}

	// --- On Set ---

	@Test
	void collectNested_onSet() throws OclParseException {
		assertEquals(3, eval(
				"Set{1, 2, 3}->collectNested(i | i * 10)->size()", self));
	}

	// --- On Bag ---

	@Test
	void collectNested_onBag() throws OclParseException {
		assertEquals(4, eval(
				"Bag{1, 1, 2, 3}->collectNested(i | i * 10)->size()", self));
	}
}
