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
 * Tests for Cartesian-product style iterator patterns using
 * nested single-variable iterators. These simulate the
 * multi-variable forAll/exists semantics.
 */
class OclMultiIteratorVarTest extends AbstractOclTest {

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

	// --- Nested forAll (simulates forAll with two variables) ---

	@Test
	void nestedForAll_allPairsPositiveSum() throws OclParseException {
		assertEquals(true, eval(
				"Set{1, 2, 3}->forAll(i | Set{1, 2, 3}->forAll(j | i + j > 0))",
				alice));
	}

	@Test
	void nestedForAll_commutative() throws OclParseException {
		assertEquals(true, eval(
				"Set{1, 2, 3}->forAll(i | Set{1, 2, 3}->forAll(j | i + j = j + i))",
				alice));
	}

	@Test
	void nestedForAll_notAllEqual() throws OclParseException {
		assertEquals(false, eval(
				"Set{1, 2, 3}->forAll(i | Set{1, 2, 3}->forAll(j | i = j))",
				alice));
	}

	// --- Nested exists (simulates exists with two variables) ---

	@Test
	void nestedExists_pairSumsTo5() throws OclParseException {
		assertEquals(true, eval(
				"Set{1, 2, 3, 4}->exists(i | Set{1, 2, 3, 4}->exists(j | i + j = 5))",
				alice));
	}

	@Test
	void nestedExists_noPairSumsTo100() throws OclParseException {
		assertEquals(false, eval(
				"Set{1, 2, 3}->exists(i | Set{1, 2, 3}->exists(j | i + j = 100))",
				alice));
	}

	// --- Model: nested forAll for uniqueness check ---

	@Test
	void model_nestedForAll_uniqueNames() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e1 | " +
				"  self.employees->forAll(e2 | e1 = e2 or e1.name <> e2.name))",
				company));
	}

	@Test
	void model_nestedExists_twoMarried() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->exists(e1 | " +
				"  self.employees->exists(e2 | e1 <> e2 and e1.isMarried and e2.isMarried))",
				company));
	}

	// --- Singleton nested ---

	@Test
	void singleton_nestedForAll() throws OclParseException {
		assertEquals(true, eval(
				"Set{42}->forAll(i | Set{42}->forAll(j | i = j))", alice));
	}

	@Test
	void singleton_nestedExists() throws OclParseException {
		assertEquals(true, eval(
				"Set{42}->exists(i | Set{42}->exists(j | i = j))", alice));
	}

	// --- Empty collection nested ---

	@Test
	void empty_nestedForAll() throws OclParseException {
		assertEquals(true, eval(
				"Set{}->forAll(i | Set{}->forAll(j | false))", alice));
	}

	@Test
	void empty_nestedExists() throws OclParseException {
		assertEquals(false, eval(
				"Set{}->exists(i | Set{}->exists(j | true))", alice));
	}

	// --- Mixed forAll/exists ---

	@Test
	void forAll_inner_exists() throws OclParseException {
		// For every element, there exists another element that sums to > 3
		assertEquals(true, eval(
				"Set{1, 2, 3}->forAll(i | Set{1, 2, 3}->exists(j | i + j > 3))",
				alice));
	}

	@Test
	void exists_inner_forAll() throws OclParseException {
		// There exists an element where all others are less than it + 10
		assertEquals(true, eval(
				"Set{1, 2, 3}->exists(i | Set{1, 2, 3}->forAll(j | j < i + 10))",
				alice));
	}

	// --- Sequence nested (order matters) ---

	@Test
	void sequence_nestedForAll() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->forAll(i | Sequence{1, 2, 3}->forAll(j | i * j >= 0))",
				alice));
	}
}
