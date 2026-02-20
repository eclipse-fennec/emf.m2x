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
 * Complex let expressions: model data in let variables, let with
 * collections, let with if-then-else, and multiple chained lets.
 */
class OclLetComplexTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject carol;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 80000.0, true);
		bob = createPerson("Bob", 25, 40000.0, false);
		carol = createPerson("Carol", 45, 120000.0, true);
		company = createCompany("TechCorp", alice, bob, carol);
	}

	// --- Let with model navigation ---

	@Test
	void let_firstEmployee_name() throws OclParseException {
		assertEquals("Alice", eval(
				"let first : Person = self.employees->first() in first.name", company));
	}

	@Test
	void let_firstEmployee_age() throws OclParseException {
		assertEquals(30, eval(
				"let first : Person = self.employees->first() in first.age", company));
	}

	// --- Let with collection computation ---

	@Test
	void let_totalSalary() throws OclParseException {
		assertEquals(240000.0, eval(
				"let total : Real = self.employees.salary->sum() in total", company));
	}

	@Test
	void let_avgAge() throws OclParseException {
		// (30 + 25 + 45) / 3 = 33.333...
		Object result = eval(
				"let ages : Sequence(Integer) = self.employees.age in " +
				"ages->sum() / ages->size()", company);
		assertEquals(33.333, ((Number) result).doubleValue(), 0.01);
	}

	// --- Let with conditional ---

	@Test
	void let_ifThenElse() throws OclParseException {
		assertEquals("large", eval(
				"let count : Integer = self.employees->size() in " +
				"if count > 2 then 'large' else 'small' endif", company));
	}

	@Test
	void let_ifThenElse_small() throws OclParseException {
		EObject small = createCompany("Small", createPerson("X", 20, 10000.0, false));
		assertEquals("small", eval(
				"let count : Integer = self.employees->size() in " +
				"if count > 2 then 'large' else 'small' endif", small));
	}

	// --- Chained let ---

	@Test
	void chainedLet_sumAndCount() throws OclParseException {
		assertEquals(true, eval(
				"let total : Real = self.employees.salary->sum() in " +
				"let count : Integer = self.employees->size() in " +
				"total / count > 50000.0", company));
	}

	@Test
	void chainedLet_minAndMax() throws OclParseException {
		assertEquals(80000.0, eval(
				"let maxSal : Real = self.employees.salary->max() in " +
				"let minSal : Real = self.employees.salary->min() in " +
				"maxSal - minSal", company));
	}

	// --- Let with select ---

	@Test
	void let_filteredCollection() throws OclParseException {
		assertEquals(2, eval(
				"let married : Sequence(Person) = self.employees->select(e | e.isMarried) in " +
				"married->size()", company));
	}

	@Test
	void let_filteredNames() throws OclParseException {
		Object result = eval(
				"let married : Sequence(Person) = self.employees->select(e | e.isMarried) in " +
				"married.name->sortedBy(n | n)", company);
		assertEquals(List.of("Alice", "Carol"), result);
	}

	// --- Let used multiple times in body ---

	@Test
	void let_usedTwice() throws OclParseException {
		// threshold used twice in body
		assertEquals(1, eval(
				"let threshold : Real = 50000.0 in " +
				"self.employees->select(e | e.salary > threshold and e.salary < threshold * 2)->size()",
				company));
	}

	// --- Let with string building ---

	@Test
	void let_stringConcat() throws OclParseException {
		assertEquals("TechCorp: 3 employees", eval(
				"let n : String = self.name in " +
				"let c : Integer = self.employees->size() in " +
				"n.concat(': ').concat(c.toString()).concat(' employees')", company));
	}

	// --- Let with boolean ---

	@Test
	void let_booleanCondition() throws OclParseException {
		assertEquals(true, eval(
				"let hasMany : Boolean = self.employees->size() > 2 in " +
				"hasMany and self.name = 'TechCorp'", company));
	}

	// --- Let shadowing outer scope ---

	@Test
	void let_shadowsSelf() throws OclParseException {
		// Inner let shadows the outer variable name
		assertEquals(10, eval(
				"let x : Integer = 5 in " +
				"let x : Integer = 10 in x", alice));
	}
}
