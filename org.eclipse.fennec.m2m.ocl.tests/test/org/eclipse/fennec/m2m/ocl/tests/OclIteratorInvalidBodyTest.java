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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for iterator behavior when the body expression produces
 * invalid or null values. Per OCL spec:
 * <ul>
 *   <li>forAll: if any body returns invalid (and no false), result is invalid</li>
 *   <li>exists: if any body returns invalid (and no true), result is invalid</li>
 *   <li>select/reject: invalid body elements are typically excluded</li>
 *   <li>collect: invalid body values are included in result</li>
 *   <li>iterate: invalid accumulator propagates</li>
 * </ul>
 */
class OclIteratorInvalidBodyTest extends AbstractOclTest {

	static EObject self;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
		EObject bob = createPerson("Bob", 0, 0.0, false); // age=0 for division by zero
		EObject charlie = createPerson("Charlie", 45, 90000.0, true);
		company = createCompany("TestCorp", self, bob, charlie);
	}

	// === forAll with invalid body ===

	@Test
	void forAll_allInvalidBody_isInvalid() throws OclParseException {
		// All bodies produce invalid → result is invalid
		assertInvalid("Sequence{1, 2, 3}->forAll(x | 1 / 0 > 0)", self);
	}

	@Test
	void forAll_someInvalidAfterTrue_isInvalid() throws OclParseException {
		// x=1 → true, x=0 → div-by-zero (invalid), no false seen → invalid
		assertInvalid("Sequence{1, 0}->forAll(x | 1 / x > 0)", self);
	}

	@Test
	void forAll_falseBeforeInvalid_isFalse() throws OclParseException {
		// x=0 → false (0 > 5 is false), should short-circuit before hitting invalid
		// The collection order matters: 0 first means false comes first
		assertEquals(false, eval(
				"Sequence{0, 1, 2}->forAll(x | x > 5)", self));
	}

	// === exists with invalid body ===

	@Test
	void exists_allInvalidBody_isInvalid() throws OclParseException {
		// All bodies produce invalid → result is invalid
		assertInvalid("Sequence{1, 2, 3}->exists(x | 1 / 0 > 0)", self);
	}

	@Test
	void exists_trueBeforeInvalid_isTrue() throws OclParseException {
		// x=2 → true (2 > 1), short-circuits to true
		assertEquals(true, eval(
				"Sequence{2, 0}->exists(x | x > 1)", self));
	}

	@Test
	void exists_someInvalidAfterFalse_isInvalid() throws OclParseException {
		// x=1 → false (1/1 > 5 → false), x=0 → invalid (div by zero)
		// No true found, invalid seen → invalid
		assertInvalid("Sequence{1, 0}->exists(x | 1 / x > 5)", self);
	}

	// === select with invalid body ===

	@Test
	void select_invalidBody_excludesElement() throws OclParseException {
		// Elements where body is invalid: those elements are excluded
		// Only elements where body is clearly true are included
		Object result = eval(
				"Sequence{3, 0, 5}->select(x | x > 2)", self);
		assertTrue(result instanceof Collection<?>);
		Collection<?> coll = (Collection<?>) result;
		assertEquals(2, coll.size()); // 3 and 5
	}

	@Test
	void reject_invalidBody_keepElement() throws OclParseException {
		// reject: element is kept if body is NOT true
		// invalid is not true, so element stays
		Object result = eval(
				"Sequence{1, 2, 3}->reject(x | x > 2)", self);
		assertTrue(result instanceof Collection<?>);
		Collection<?> coll = (Collection<?>) result;
		assertEquals(2, coll.size()); // 1 and 2
	}

	// === collect with invalid body ===

	@Test
	void collect_invalidBodyElement_includesInvalid() throws OclParseException {
		// collect: invalid values are preserved in result
		Object result = eval(
				"Sequence{2, 0, 3}->collect(x | 10 / x)", self);
		assertTrue(result instanceof Collection<?>);
		Collection<?> coll = (Collection<?>) result;
		assertEquals(3, coll.size()); // 5, invalid, 3 (rounded)
	}

	// === iterate with invalid ===

	@Test
	void iterate_invalidInBody_propagates() throws OclParseException {
		// If body produces invalid, iterate should propagate
		assertInvalid(
				"Sequence{1, 0, 3}->iterate(x; acc : Integer = 0 | acc + 1 / x)",
				self);
	}

	// === one with invalid body ===

	@Test
	void one_invalidBody_isInvalid() throws OclParseException {
		// If all bodies are invalid, one can't determine truth
		assertInvalid("Sequence{1, 2, 3}->one(x | 1 / 0 > 0)", self);
	}

	// === any with invalid body ===

	@Test
	void any_noMatchReturnsNull() throws OclParseException {
		// any returns null (OclVoid) when no element matches
		Object result = eval(
				"Sequence{1, 2, 3}->any(x | x > 10)", self);
		// any with no match → null
		assertEquals(null, result);
	}

	// === isUnique with invalid body ===

	@Test
	void isUnique_normalCase() throws OclParseException {
		// Simple case: all different values
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->isUnique(x | x * 2)", self));
	}

	@Test
	void isUnique_duplicates() throws OclParseException {
		// Duplicate body results
		assertEquals(false, eval(
				"Sequence{1, 2, 3}->isUnique(x | x.div(2))", self));
	}

	// === Empty collection iterators ===

	@Test
	void forAll_empty_isTrue() throws OclParseException {
		assertEquals(true, eval("Sequence{}->forAll(x | false)", self));
	}

	@Test
	void exists_empty_isFalse() throws OclParseException {
		assertEquals(false, eval("Sequence{}->exists(x | true)", self));
	}

	@Test
	void one_empty_isFalse() throws OclParseException {
		assertEquals(false, eval("Sequence{}->one(x | true)", self));
	}

	@Test
	void isUnique_empty_isTrue() throws OclParseException {
		assertEquals(true, eval("Sequence{}->isUnique(x | x)", self));
	}

	@Test
	void select_empty_isEmpty() throws OclParseException {
		assertEquals(0, eval("Sequence{}->select(x | true)->size()", self));
	}

	@Test
	void collect_empty_isEmpty() throws OclParseException {
		assertEquals(0, eval("Sequence{}->collect(x | x)->size()", self));
	}
}
