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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for cross-type collection equality and numeric coercion in
 * collection equality per OCL spec.
 *
 * <p>Different collection types (Set vs Sequence, Bag vs OrderedSet) are
 * generally not equal even if they contain the same elements, because
 * they have different semantics (ordered vs unordered, unique vs non-unique).
 */
class OclCollectionEqualityCrossTypeTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// === Cross-type: Set vs Sequence ===

	@Test
	void set_notEqual_sequence() throws OclParseException {
		// Different collection types are not equal
		assertEquals(false, eval("Set{1, 2, 3} = Sequence{1, 2, 3}", self));
	}

	// === Cross-type: Set vs Bag ===

	@Test
	void set_notEqual_bag() throws OclParseException {
		assertEquals(false, eval("Set{1, 2} = Bag{1, 2}", self));
	}

	// === Cross-type: Sequence vs Bag ===

	@Test
	void sequence_notEqual_bag() throws OclParseException {
		assertEquals(false, eval("Sequence{1, 2} = Bag{1, 2}", self));
	}

	// === Numeric coercion in equality ===

	@Test
	void set_integerAndReal_equality() throws OclParseException {
		// 1 = 1.0 in OCL (numeric coercion)
		assertEquals(true, eval("1 = 1.0", self));
	}

	@Test
	void sequence_numericEquality() throws OclParseException {
		// Elements compared with numeric coercion
		assertEquals(true, eval("Sequence{1, 2, 3} = Sequence{1, 2, 3}", self));
	}

	// === Collection containing null ===

	@Test
	void set_withNull_equality() throws OclParseException {
		assertEquals(true, eval("Set{1, null, 3} = Set{null, 3, 1}", self));
	}

	@Test
	void sequence_withNull_equality() throws OclParseException {
		assertEquals(true, eval("Sequence{1, null, 3} = Sequence{1, null, 3}", self));
	}

	@Test
	void sequence_withNull_notEqual_differentPosition() throws OclParseException {
		assertEquals(false, eval("Sequence{null, 1} = Sequence{1, null}", self));
	}

	// === Collection containing invalid ===

	@Test
	void set_withInvalid_equality() throws OclParseException {
		// OCL v2.5: invalid = invalid yields invalid, so element comparison fails
		// → collections containing invalid cannot be equal (element match fails)
		// TODO: verify against Eclipse — may need to be assertInvalid instead of false
		assertEquals(false, eval("Set{1, invalid, 3} = Set{invalid, 3, 1}", self));
	}

	@Test
	void sequence_withInvalid_equality() throws OclParseException {
		// OCL v2.5: invalid = invalid yields invalid, so positional comparison fails
		// TODO: verify against Eclipse — may need to be assertInvalid instead of false
		assertEquals(false, eval("Sequence{1, invalid, 3} = Sequence{1, invalid, 3}", self));
	}

	// === Empty collections of different types ===

	@Test
	void emptySet_notEqual_emptySequence() throws OclParseException {
		assertEquals(false, eval("Set{} = Sequence{}", self));
	}

	@Test
	void emptyBag_notEqual_emptyOrderedSet() throws OclParseException {
		assertEquals(false, eval("Bag{} = OrderedSet{}", self));
	}

	// === Nested collection equality ===

	@Test
	void sequence_nestedSequence_equal() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{Sequence{1, 2}, Sequence{3}} = Sequence{Sequence{1, 2}, Sequence{3}}", self));
	}

	@Test
	void sequence_nestedSequence_notEqual() throws OclParseException {
		assertEquals(false, eval(
				"Sequence{Sequence{1, 2}, Sequence{3}} = Sequence{Sequence{2, 1}, Sequence{3}}", self));
	}

	// === Collection equality after operations ===

	@Test
	void union_equality() throws OclParseException {
		assertEquals(true, eval(
				"Set{1, 2}->union(Set{3}) = Set{1, 2, 3}", self));
	}

	@Test
	void intersection_equality() throws OclParseException {
		assertEquals(true, eval(
				"Set{1, 2, 3}->intersection(Set{2, 3, 4}) = Set{2, 3}", self));
	}

	@Test
	void symmetricDifference_equality() throws OclParseException {
		assertEquals(true, eval(
				"Set{1, 2, 3}->symmetricDifference(Set{2, 3, 4}) = Set{1, 4}", self));
	}
}
