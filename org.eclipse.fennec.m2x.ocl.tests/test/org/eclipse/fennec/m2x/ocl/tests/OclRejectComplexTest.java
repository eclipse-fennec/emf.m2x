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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for reject() iterator with complex conditions,
 * combined with other iterators and collection operations.
 */
class OclRejectComplexTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject carol;
	static EObject dave;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 80000.0, true);
		bob = createPerson("Bob", 25, 40000.0, false);
		carol = createPerson("Carol", 45, 120000.0, true);
		dave = createPerson("Dave", 22, 35000.0, false);
		company = createCompany("TechCorp", alice, bob, carol, dave);
	}

	// --- Basic reject on literals ---

	@Test
	void reject_integers_greaterThan3() throws OclParseException {
		assertEquals(List.of(1, 2, 3), eval("Sequence{1, 2, 3, 4, 5}->reject(x | x > 3)", alice));
	}

	@Test
	void reject_strings_short() throws OclParseException {
		assertEquals(List.of("hello", "world"),
				eval("Sequence{'hi', 'hello', 'world', 'ok'}->reject(s | s.size() < 3)", alice));
	}

	@Test
	void reject_emptyResult() throws OclParseException {
		assertEquals(List.of(), eval("Sequence{1, 2, 3}->reject(x | x > 0)", alice));
	}

	@Test
	void reject_noneRejected() throws OclParseException {
		assertEquals(List.of(1, 2, 3), eval("Sequence{1, 2, 3}->reject(x | x > 10)", alice));
	}

	// --- Reject on model data ---

	@Test
	void reject_unmarried() throws OclParseException {
		assertEquals(2, eval(
				"self.employees->reject(e | e.isMarried)->size()", company));
	}

	@Test
	void reject_lowSalary() throws OclParseException {
		assertEquals(2, eval(
				"self.employees->reject(e | e.salary < 50000.0)->size()", company));
	}

	@Test
	void reject_young() throws OclParseException {
		assertEquals(2, eval(
				"self.employees->reject(e | e.age < 30)->size()", company));
	}

	// --- Reject with compound conditions ---

	@Test
	void reject_andCondition() throws OclParseException {
		// Reject young AND unmarried
		assertEquals(3, eval(
				"self.employees->reject(e | e.age < 25 and not e.isMarried)->size()", company));
	}

	@Test
	void reject_orCondition() throws OclParseException {
		// Reject young OR low salary: only Dave(22, 35000) matches → 3 remain
		assertEquals(3, eval(
				"self.employees->reject(e | e.age < 25 or e.salary < 40000.0)->size()", company));
	}

	// --- Reject then collect ---

	@Test
	void reject_thenCollect_names() throws OclParseException {
		Object result = eval(
				"self.employees->reject(e | e.isMarried)->collect(e | e.name)->sortedBy(n | n)",
				company);
		assertEquals(List.of("Bob", "Dave"), result);
	}

	@Test
	void reject_thenCollect_ages() throws OclParseException {
		Object result = eval(
				"self.employees->reject(e | e.salary > 100000.0)->collect(e | e.age)->sortedBy(a | a)",
				company);
		assertEquals(List.of(22, 25, 30), result);
	}

	// --- Reject then select (double filter) ---

	@Test
	void reject_thenSelect() throws OclParseException {
		// Reject unmarried, then select those with salary > 50000
		assertEquals(1, eval(
				"self.employees->reject(e | not e.isMarried)->select(e | e.salary > 100000.0)->size()",
				company));
	}

	// --- Reject then forAll/exists ---

	@Test
	void reject_thenForAll() throws OclParseException {
		// After rejecting young (<25), all remaining should be >= 25
		assertEquals(true, eval(
				"self.employees->reject(e | e.age < 25)->forAll(e | e.age >= 25)", company));
	}

	@Test
	void reject_thenExists() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->reject(e | e.isMarried)->exists(e | e.name = 'Bob')", company));
	}

	// --- Reject on Set ---

	@Test
	void reject_set_integers() throws OclParseException {
		assertEquals(2, eval("Set{1, 2, 3, 4, 5}->reject(x | x <= 3)->size()", alice));
	}

	// --- Reject preserves order ---

	@Test
	void reject_preservesOrder() throws OclParseException {
		assertEquals(List.of(2, 4), eval("Sequence{1, 2, 3, 4, 5}->reject(x | x.mod(2) <> 0)", alice));
	}

	// --- Reject with size check ---

	@Test
	void reject_result_isEmpty() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->reject(x | true)->isEmpty()", alice));
	}

	@Test
	void reject_result_notEmpty() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3}->reject(x | false)->notEmpty()", alice));
	}

	// --- Reject combined with sum ---

	@Test
	void reject_thenSum() throws OclParseException {
		// Reject values <= 2, sum remaining: 3+4+5 = 12
		assertEquals(12, eval("Sequence{1, 2, 3, 4, 5}->reject(x | x <= 2)->sum()", alice));
	}
}
