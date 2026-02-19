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
package org.eclipse.fennec.m2m.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL collection literals with computed elements,
 * mixed types, and expressions as elements.
 */
class OclCollectionLiteralAdvancedTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Computed elements ---

	@Test
	void sequence_computedElements() throws OclParseException {
		assertEquals(List.of(2, 4, 6),
				eval("Sequence{1 + 1, 2 + 2, 3 + 3}", self));
	}

	@Test
	void set_computedElements() throws OclParseException {
		assertEquals(3, eval("Set{1 * 1, 2 * 2, 3 * 3}->size()", self));
	}

	@Test
	void sequence_withPropertyAccess() throws OclParseException {
		// self.age returns Integer (EInt), collection elements may be Integer not Long
		assertEquals(3, eval(
				"Sequence{self.age, self.age + 1, self.age + 2}->size()", self));
	}

	@Test
	void set_withStringConcat() throws OclParseException {
		assertEquals(true, eval(
				"Set{'hello' + ' ' + 'world'}->includes('hello world')", self));
	}

	// --- Range literals ---

	@Test
	void range_basic() throws OclParseException {
		assertEquals(List.of(1, 2, 3, 4, 5),
				eval("Sequence{1..5}", self));
	}

	@Test
	void range_single() throws OclParseException {
		assertEquals(List.of(3), eval("Sequence{3..3}", self));
	}

	@Test
	void range_size() throws OclParseException {
		assertEquals(10, eval("Sequence{1..10}->size()", self));
	}

	@Test
	void range_sum() throws OclParseException {
		// 1+2+...+10 = 55
		assertEquals(55, eval("Sequence{1..10}->sum()", self));
	}

	@Test
	void range_withLiterals() throws OclParseException {
		assertEquals(20, eval("Sequence{1..20}->size()", self));
	}

	// --- Mixed ranges and elements ---

	@Test
	void sequence_rangeAndElements() throws OclParseException {
		assertEquals(7, eval("Sequence{1..5, 10, 20}->size()", self));
	}

	// --- Nested collections ---

	@Test
	void nestedSequence_size() throws OclParseException {
		assertEquals(2, eval("Sequence{Sequence{1, 2}, Sequence{3, 4}}->size()", self));
	}

	@Test
	void nestedSet_size() throws OclParseException {
		assertEquals(2, eval("Set{Set{1, 2}, Set{3, 4}}->size()", self));
	}

	// --- Boolean collection ---

	@Test
	void sequence_booleans() throws OclParseException {
		assertEquals(List.of(true, false, true),
				eval("Sequence{true, false, true}", self));
	}

	// --- String collection ---

	@Test
	void sequence_strings() throws OclParseException {
		assertEquals(List.of("a", "b", "c"),
				eval("Sequence{'a', 'b', 'c'}", self));
	}

	// --- Real collection ---

	@Test
	void sequence_reals() throws OclParseException {
		assertEquals(3, eval("Sequence{1.1, 2.2, 3.3}->size()", self));
	}

	// --- Empty with type ---

	@Test
	void emptySet() throws OclParseException {
		assertEquals(0, eval("Set{}->size()", self));
	}

	@Test
	void emptySequence() throws OclParseException {
		assertEquals(0, eval("Sequence{}->size()", self));
	}

	@Test
	void emptyBag() throws OclParseException {
		assertEquals(0, eval("Bag{}->size()", self));
	}

	@Test
	void emptyOrderedSet() throws OclParseException {
		assertEquals(0, eval("OrderedSet{}->size()", self));
	}

	// --- Large collection ---

	@Test
	void range_large() throws OclParseException {
		assertEquals(100, eval("Sequence{1..100}->size()", self));
	}

	@Test
	void range_large_sum() throws OclParseException {
		// 1+2+...+100 = 5050
		assertEquals(5050, eval("Sequence{1..100}->sum()", self));
	}

	@Test
	void range_large_max() throws OclParseException {
		assertEquals(100, eval("Sequence{1..100}->max()", self));
	}

	// --- Collection used immediately ---

	@Test
	void literal_immediateFirst() throws OclParseException {
		assertEquals(10, eval("Sequence{10, 20, 30}->first()", self));
	}

	@Test
	void literal_immediateLast() throws OclParseException {
		assertEquals(30, eval("Sequence{10, 20, 30}->last()", self));
	}

	@Test
	void literal_immediateAt() throws OclParseException {
		assertEquals(20, eval("Sequence{10, 20, 30}->at(2)", self));
	}

	// --- If expression as element ---

	@Test
	void sequence_withIfElement() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{if true then 1 else 2 endif}->includes(1)", self));
	}

	// --- Let expression as element ---

	@Test
	void sequence_withLetElement() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{let x: Integer = 42 in x}->includes(42)", self));
	}
}
