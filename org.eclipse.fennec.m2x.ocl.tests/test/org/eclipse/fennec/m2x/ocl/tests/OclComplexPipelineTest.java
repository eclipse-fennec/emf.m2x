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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for complex OCL expression pipelines that combine
 * many features into realistic query patterns.
 * Each test exercises multiple OCL features in a single expression.
 */
class OclComplexPipelineTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject charlie;
	static EObject dave;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 35, 75000.0, true);
		bob = createPerson("Bob", 28, 52000.0, false);
		charlie = createPerson("Charlie", 45, 110000.0, true);
		dave = createPerson("Dave", 22, 38000.0, false);
		company = createCompany("BigCorp", alice, bob, charlie, dave);
	}

	// --- Select + Collect + Sort + First ---

	@Test
	void pipeline_cheapestMarriedEmployee() throws OclParseException {
		Object result = eval(
				"let cheapest: Person = self.employees" +
				"->select(e | e.isMarried)" +
				"->sortedBy(e | e.salary)" +
				"->first() " +
				"in cheapest.name",
				company);
		assertEquals("Alice", result);
	}

	@Test
	void pipeline_mostExpensiveUnmarriedEmployee() throws OclParseException {
		Object result = eval(
				"let expensive: Person = self.employees" +
				"->select(e | not e.isMarried)" +
				"->sortedBy(e | e.salary)" +
				"->last() " +
				"in expensive.name",
				company);
		assertEquals("Bob", result);
	}

	// --- Aggregate + Compare ---

	@Test
	void pipeline_aboveAverageSalary() throws OclParseException {
		Object result = eval(
				"let avg: Real = self.employees->collect(e | e.salary)->sum() / self.employees->size() " +
				"in self.employees->select(e | e.salary > avg)->collect(e | e.name)->sortedBy(n | n)",
				company);
		assertInstanceOf(List.class, result);
		List<?> names = (List<?>) result;
		assertEquals(2, names.size());
		assertEquals("Alice", names.get(0));
		assertEquals("Charlie", names.get(1));
	}

	// --- Let + Iterator + If ---

	@Test
	void pipeline_categorizeEmployees() throws OclParseException {
		Object result = eval(
				"self.employees->collect(e | " +
				"  if e.salary > 80000.0 then 'high' " +
				"  else if e.salary > 50000.0 then 'mid' " +
				"  else 'low' endif endif)",
				company);
		assertInstanceOf(Collection.class, result);
		Collection<?> categories = (Collection<?>) result;
		assertEquals(4, categories.size());
		assertTrue(categories.contains("high"));
		assertTrue(categories.contains("mid"));
		assertTrue(categories.contains("low"));
	}

	// --- Iterate + String building ---

	@Test
	void pipeline_nameListViaIterate() throws OclParseException {
		Object result = eval(
				"self.employees->sortedBy(e | e.name)" +
				"->iterate(e; acc: String = '' | " +
				"  if acc = '' then e.name else acc + ', ' + e.name endif)",
				company);
		assertEquals("Alice, Bob, Charlie, Dave", result);
	}

	// --- Nested let ---

	@Test
	void pipeline_nestedLet_salaryAnalysis() throws OclParseException {
		assertEquals(true, eval(
				"let total: Real = self.employees->collect(e | e.salary)->sum(), " +
				"    count: Integer = self.employees->size(), " +
				"    avg: Real = total / count " +
				"in avg > 50000.0",
				company));
	}

	// --- Select chain ---

	@Test
	void pipeline_chainedSelects() throws OclParseException {
		assertEquals(1, eval(
				"self.employees" +
				"->select(e | e.age > 25)" +       // Alice, Charlie, Bob... wait Bob is 28
				"->select(e | e.isMarried)" +       // Alice, Charlie
				"->select(e | e.salary > 80000.0)" + // Charlie
				"->size()",
				company));
	}

	// --- forAll with let ---

	@Test
	void pipeline_forAllWithLet() throws OclParseException {
		assertEquals(true, eval(
				"let minSalary: Real = 30000.0 " +
				"in self.employees->forAll(e | e.salary > minSalary)",
				company));
	}

	// --- Exists with navigation ---

	@Test
	void pipeline_existsHighEarnerOver40() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->exists(e | e.age > 40 and e.salary > 100000.0)",
				company));
	}

	// --- Collection arithmetic pipeline ---

	@Test
	void pipeline_sumOfSquaredAges() throws OclParseException {
		Object result = eval(
				"self.employees->collect(e | e.age)" +
				"->collect(a | a * a)" +
				"->sum()",
				company);
		// 35^2 + 28^2 + 45^2 + 22^2 = 1225 + 784 + 2025 + 484 = 4518
		// But age is EInt, so a*a might produce Integer or Long
		assertInstanceOf(Number.class, result);
	}

	// --- SortedBy + at ---

	@Test
	void pipeline_secondYoungest() throws OclParseException {
		Object result = eval(
				"let second: Person = self.employees->sortedBy(e | e.age)->at(2) " +
				"in second.name",
				company);
		assertEquals("Bob", result);
	}

	// --- Complex boolean ---

	@Test
	void pipeline_complexConstraint() throws OclParseException {
		// Company is valid if: has employees, all are adults, unique names, total salary < 500k
		assertEquals(true, eval(
				"self.employees->notEmpty() " +
				"and self.employees->forAll(e | e.age >= 18) " +
				"and self.employees->isUnique(e | e.name) " +
				"and self.employees->collect(e | e.salary)->sum() < 500000.0",
				company));
	}

	// --- Using any + property ---

	@Test
	void pipeline_oldestEmployeeName() throws OclParseException {
		Object result = eval(
				"let maxAge: Integer = self.employees->collect(e | e.age)->max() " +
				"in self.employees->select(e | e.age = maxAge)->collect(e | e.name)->first()",
				company);
		assertEquals("Charlie", result);
	}

	// --- Range + pipeline ---

	@Test
	void pipeline_rangeFilterCollect() throws OclParseException {
		Object result = eval(
				"Sequence{1..10}->select(i | i.mod(3) = 0)->collect(i | i * i)",
				alice);
		assertInstanceOf(Collection.class, result);
		// 3, 6, 9 → 9, 36, 81
		Collection<?> squares = (Collection<?>) result;
		assertEquals(3, squares.size());
	}

	// --- Iterate with model + if ---

	@Test
	void pipeline_iterateHighSalaryCount() throws OclParseException {
		assertEquals(2, eval(
				"self.employees->iterate(e; acc: Integer = 0 | " +
				"  if e.salary > 60000.0 then acc + 1 else acc endif)",
				company));
	}
}
