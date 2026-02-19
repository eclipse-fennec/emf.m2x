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
 * Tests for the OCL {@code isUnique} and {@code one} iterators.
 * <ul>
 *   <li>{@code isUnique(expr)} — true iff expr produces distinct values for all elements</li>
 *   <li>{@code one(expr)} — true iff exactly one element satisfies expr</li>
 * </ul>
 */
class OclIsUniqueOneTest extends AbstractOclTest {

	static EObject self;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);

		EObject bob = createPerson("Bob", 25, 40000.0, false);
		EObject carol = createPerson("Carol", 35, 60000.0, true);
		company = createCompany("Acme", self, bob, carol);
	}

	// --- isUnique on literal collections ---

	@Test
	void isUnique_allDistinct() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->isUnique(i | i)", self));
	}

	@Test
	void isUnique_withDuplicates() throws OclParseException {
		assertEquals(false, eval(
				"Sequence{1, 2, 2, 3}->isUnique(i | i)", self));
	}

	@Test
	void isUnique_withExpression() throws OclParseException {
		// mod 3: 1→1, 2→2, 3→0, all distinct
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->isUnique(i | i.mod(3))", self));
	}

	@Test
	void isUnique_expressionNotUnique() throws OclParseException {
		// mod 2: 1→1, 2→0, 3→1 — not unique (1 appears twice)
		assertEquals(false, eval(
				"Sequence{1, 2, 3}->isUnique(i | i.mod(2))", self));
	}

	@Test
	void isUnique_singleElement() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{42}->isUnique(i | i)", self));
	}

	@Test
	void isUnique_emptyCollection() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{}->isUnique(i | i)", self));
	}

	@Test
	void isUnique_strings() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{'a', 'b', 'c'}->isUnique(s | s)", self));
	}

	@Test
	void isUnique_strings_notUnique() throws OclParseException {
		assertEquals(false, eval(
				"Sequence{'a', 'b', 'a'}->isUnique(s | s)", self));
	}

	@Test
	void isUnique_withTransform() throws OclParseException {
		// *2: 1→2, 2→4, 3→6, all distinct
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->isUnique(i | i * 2)", self));
	}

	@Test
	void isUnique_constantExpression() throws OclParseException {
		// All map to same value → not unique (unless single element)
		assertEquals(false, eval(
				"Sequence{1, 2, 3}->isUnique(i | 0)", self));
	}

	// --- isUnique on model ---

	@Test
	void isUnique_names() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->isUnique(e | e.name)", company));
	}

	@Test
	void isUnique_ages() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->isUnique(e | e.age)", company));
	}

	// --- one on literal collections ---

	@Test
	void one_exactlyOne() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->one(i | i = 2)", self));
	}

	@Test
	void one_none() throws OclParseException {
		assertEquals(false, eval(
				"Sequence{1, 2, 3}->one(i | i = 5)", self));
	}

	@Test
	void one_moreThanOne() throws OclParseException {
		assertEquals(false, eval(
				"Sequence{1, 2, 2, 3}->one(i | i = 2)", self));
	}

	@Test
	void one_allMatch() throws OclParseException {
		assertEquals(false, eval(
				"Sequence{1, 1, 1}->one(i | i = 1)", self));
	}

	@Test
	void one_singleElement_matches() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{42}->one(i | i = 42)", self));
	}

	@Test
	void one_singleElement_noMatch() throws OclParseException {
		assertEquals(false, eval(
				"Sequence{42}->one(i | i = 99)", self));
	}

	@Test
	void one_emptyCollection() throws OclParseException {
		assertEquals(false, eval(
				"Sequence{}->one(i | i = 1)", self));
	}

	@Test
	void one_withExpression() throws OclParseException {
		// Only 3 has mod 3 = 0
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->one(i | i.mod(3) = 0)", self));
	}

	@Test
	void one_strings() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{'hello', 'world', 'foo'}->one(s | s = 'world')", self));
	}

	// --- one on model ---

	@Test
	void one_employeeByName() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->one(e | e.name = 'Alice')", company));
	}

	@Test
	void one_employeeByAge() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->one(e | e.age > 30)", company));
	}

	@Test
	void one_noEmployeeMatches() throws OclParseException {
		assertEquals(false, eval(
				"self.employees->one(e | e.age > 100)", company));
	}

	// --- Combined with other operations ---

	@Test
	void isUnique_afterSelect() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i > 2)->isUnique(i | i)", self));
	}

	@Test
	void one_afterSelect() throws OclParseException {
		// select > 2 → {3,4,5}, one = 5 → true
		assertEquals(true, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i > 2)->one(i | i = 5)", self));
	}
}
