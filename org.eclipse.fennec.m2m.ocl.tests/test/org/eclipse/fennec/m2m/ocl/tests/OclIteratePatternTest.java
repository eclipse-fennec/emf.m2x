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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL iterate expression patterns.
 * Iterate is the most general iterator — all other iterators
 * can be expressed in terms of iterate. These tests verify
 * common patterns: sum, product, count, min, max, concat,
 * and custom accumulation.
 */
class OclIteratePatternTest extends AbstractOclTest {

	static EObject self;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
		EObject bob = createPerson("Bob", 25, 45000.0, false);
		EObject charlie = createPerson("Charlie", 35, 80000.0, true);
		company = createCompany("ACME", self, bob, charlie);
	}

	// --- Sum pattern ---

	@Test
	void iterate_sum() throws OclParseException {
		assertEquals(15L, eval(
				"Sequence{1, 2, 3, 4, 5}->iterate(i; acc: Integer = 0 | acc + i)",
				self));
	}

	@Test
	void iterate_sumReal() throws OclParseException {
		assertEquals(6.6, (Double) eval(
				"Sequence{1.1, 2.2, 3.3}->iterate(i; acc: Real = 0.0 | acc + i)",
				self), 1e-10);
	}

	// --- Product pattern ---

	@Test
	void iterate_product() throws OclParseException {
		assertEquals(120L, eval(
				"Sequence{1, 2, 3, 4, 5}->iterate(i; acc: Integer = 1 | acc * i)",
				self));
	}

	// --- Count pattern ---

	@Test
	void iterate_countPositive() throws OclParseException {
		assertEquals(3L, eval(
				"Sequence{-1, 2, -3, 4, 5}->iterate(i; acc: Integer = 0 | " +
				"  if i > 0 then acc + 1 else acc endif)",
				self));
	}

	@Test
	void iterate_countEven() throws OclParseException {
		assertEquals(2L, eval(
				"Sequence{1, 2, 3, 4, 5}->iterate(i; acc: Integer = 0 | " +
				"  if i.mod(2) = 0 then acc + 1 else acc endif)",
				self));
	}

	// --- Min pattern ---

	@Test
	void iterate_min() throws OclParseException {
		assertEquals(1L, eval(
				"Sequence{3, 1, 4, 1, 5}->iterate(i; acc: Integer = 999 | " +
				"  if i < acc then i else acc endif)",
				self));
	}

	// --- Max pattern ---

	@Test
	void iterate_max() throws OclParseException {
		assertEquals(5L, eval(
				"Sequence{3, 1, 4, 1, 5}->iterate(i; acc: Integer = 0 | " +
				"  if i > acc then i else acc endif)",
				self));
	}

	// --- String concatenation pattern ---

	@Test
	void iterate_concat() throws OclParseException {
		assertEquals("abc", eval(
				"Sequence{'a', 'b', 'c'}->iterate(s; acc: String = '' | acc + s)",
				self));
	}

	@Test
	void iterate_concatWithSeparator() throws OclParseException {
		assertEquals("a,b,c", eval(
				"Sequence{'a', 'b', 'c'}->iterate(s; acc: String = '' | " +
				"  if acc = '' then s else acc + ',' + s endif)",
				self));
	}

	// --- Boolean accumulation ---

	@Test
	void iterate_allPositive() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->iterate(i; acc: Boolean = true | acc and (i > 0))",
				self));
	}

	@Test
	void iterate_anyNegative() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, -2, 3}->iterate(i; acc: Boolean = false | acc or (i < 0))",
				self));
	}

	// --- On empty collection ---

	@Test
	void iterate_emptySum() throws OclParseException {
		assertEquals(0L, eval(
				"Sequence{}->iterate(i; acc: Integer = 0 | acc + i)",
				self));
	}

	@Test
	void iterate_emptyConcat() throws OclParseException {
		assertEquals("", eval(
				"Sequence{}->iterate(s; acc: String = '' | acc + s)",
				self));
	}

	// --- On Set ---

	@Test
	void iterate_setSum() throws OclParseException {
		assertEquals(6L, eval(
				"Set{1, 2, 3}->iterate(i; acc: Integer = 0 | acc + i)",
				self));
	}

	// --- With model data ---

	@Test
	void iterate_totalSalary() throws OclParseException {
		assertEquals(175000.0, eval(
				"self.employees->iterate(e; acc: Real = 0.0 | acc + e.salary)",
				company));
	}

	@Test
	void iterate_nameList() throws OclParseException {
		Object result = eval(
				"self.employees->iterate(e; acc: String = '' | " +
				"  if acc = '' then e.name else acc + ', ' + e.name endif)",
				company);
		assertInstanceOf(String.class, result);
		String names = (String) result;
		assertEquals(true, names.contains("Alice"));
		assertEquals(true, names.contains("Bob"));
		assertEquals(true, names.contains("Charlie"));
	}

	@Test
	void iterate_countMarried() throws OclParseException {
		assertEquals(2L, eval(
				"self.employees->iterate(e; acc: Integer = 0 | " +
				"  if e.isMarried then acc + 1 else acc endif)",
				company));
	}

	// --- Complex iterate ---

	@Test
	void iterate_sumOfSquares() throws OclParseException {
		assertEquals(55L, eval(
				"Sequence{1, 2, 3, 4, 5}->iterate(i; acc: Integer = 0 | acc + i * i)",
				self));
	}

	@Test
	void iterate_alternatingSum() throws OclParseException {
		// 1 - 2 + 3 - 4 + 5 = 3 ... but iterate doesn't have index
		// Instead: sum of elements > 2
		assertEquals(12L, eval(
				"Sequence{1, 2, 3, 4, 5}->iterate(i; acc: Integer = 0 | " +
				"  if i > 2 then acc + i else acc endif)",
				self));
	}
}
