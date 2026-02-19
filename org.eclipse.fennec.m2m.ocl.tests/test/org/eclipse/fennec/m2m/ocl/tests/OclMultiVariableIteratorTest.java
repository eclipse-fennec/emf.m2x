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
 * Tests for OCL multi-variable iterator syntax per OCL v2.4 §7.7.6.
 *
 * <p>OCL allows multiple iterator variables in iterators like
 * {@code forAll(a, b | ...)} and {@code exists(a, b | ...)}.
 * This is equivalent to Cartesian product iteration:
 * {@code forAll(a | forAll(b | ...))} but with a single iterator call.
 */
class OclMultiVariableIteratorTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject charlie;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 60000.0, true);
		bob = createPerson("Bob", 25, 45000.0, false);
		charlie = createPerson("Charlie", 35, 80000.0, true);
		company = createCompany("ACME", alice, bob, charlie);
	}

	// === forAll with two variables ===

	@Test
	void forAll_twoVars_allPairsPositiveSum() throws OclParseException {
		// forAll(a, b | a + b > 0) — all positive numbers
		assertEquals(true, eval(
				"Set{1, 2, 3}->forAll(a, b | a + b > 0)", alice));
	}

	@Test
	void forAll_twoVars_commutative() throws OclParseException {
		// forAll(a, b | a + b = b + a) — addition is commutative
		assertEquals(true, eval(
				"Set{1, 2, 3}->forAll(a, b | a + b = b + a)", alice));
	}

	@Test
	void forAll_twoVars_notAllEqual() throws OclParseException {
		// forAll(a, b | a = b) — NOT all pairs are equal
		assertEquals(false, eval(
				"Set{1, 2, 3}->forAll(a, b | a = b)", alice));
	}

	@Test
	void forAll_twoVars_singleElement() throws OclParseException {
		// With single element, only one pair (42, 42)
		assertEquals(true, eval(
				"Set{42}->forAll(a, b | a = b)", alice));
	}

	@Test
	void forAll_twoVars_emptySet() throws OclParseException {
		// Empty collection → forAll is vacuously true
		assertEquals(true, eval(
				"Set{}->forAll(a, b | false)", alice));
	}

	// === exists with two variables ===

	@Test
	void exists_twoVars_pairSumsToTarget() throws OclParseException {
		// exists(a, b | a + b = 5) — 2+3 = 5
		assertEquals(true, eval(
				"Set{1, 2, 3, 4}->exists(a, b | a + b = 5)", alice));
	}

	@Test
	void exists_twoVars_noPairSumsTo100() throws OclParseException {
		assertEquals(false, eval(
				"Set{1, 2, 3}->exists(a, b | a + b = 100)", alice));
	}

	@Test
	void exists_twoVars_emptySet() throws OclParseException {
		// Empty collection → exists is false
		assertEquals(false, eval(
				"Set{}->exists(a, b | true)", alice));
	}

	// === forAll with two variables on model ===

	@Test
	void forAll_twoVars_uniqueNames() throws OclParseException {
		// All pairs of employees: same person or different names
		assertEquals(true, eval(
				"self.employees->forAll(e1, e2 | e1 = e2 or e1.name <> e2.name)",
				company));
	}

	@Test
	void forAll_twoVars_ageRelation() throws OclParseException {
		// For all pairs: sum of ages > 0 (trivially true)
		assertEquals(true, eval(
				"self.employees->forAll(e1, e2 | e1.age + e2.age > 0)",
				company));
	}

	// === exists with two variables on model ===

	@Test
	void exists_twoVars_twoMarried() throws OclParseException {
		// Exists two different employees both married
		assertEquals(true, eval(
				"self.employees->exists(e1, e2 | e1 <> e2 and e1.isMarried and e2.isMarried)",
				company));
	}

	@Test
	void exists_twoVars_ageDiffGreaterThan5() throws OclParseException {
		// Exists a pair with age difference > 5
		assertEquals(true, eval(
				"self.employees->exists(e1, e2 | (e1.age - e2.age).abs() > 5)",
				company));
	}

	// === Sequence with two variables (order matters) ===

	@Test
	void forAll_twoVars_sequence() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->forAll(a, b | a * b >= 0)", alice));
	}

	@Test
	void exists_twoVars_sequence() throws OclParseException {
		// In Sequence, (2,3) and (3,2) are separate pairs
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->exists(a, b | a + b = 5)", alice));
	}

	// === Typed multi-variable iterators ===

	@Test
	void forAll_twoVars_typed() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e1 : Person, e2 : Person | e1.age > 0 and e2.age > 0)",
				company));
	}

	@Test
	void exists_twoVars_typed() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->exists(e1 : Person, e2 : Person | e1.name = 'Alice' and e2.name = 'Bob')",
				company));
	}
}
