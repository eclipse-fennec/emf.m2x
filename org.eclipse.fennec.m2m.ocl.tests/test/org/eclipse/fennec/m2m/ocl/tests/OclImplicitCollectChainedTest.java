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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for chained implicit collect and implicit collect combined
 * with arrow operations.
 */
class OclImplicitCollectChainedTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject carol;
	static EObject company1;
	static EObject company2;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 80000.0, true);
		bob = createPerson("Bob", 25, 40000.0, false);
		carol = createPerson("Carol", 45, 120000.0, true);
		company1 = createCompany("TechCorp", alice, bob, carol);
		// separate instances for company2 (EMF containment)
		company2 = createCompany("FinCorp",
				createPerson("Dave", 35, 90000.0, true),
				createPerson("Eve", 28, 55000.0, false));
	}

	// --- Implicit collect followed by arrow operation ---

	@Test
	void implicitCollect_thenArrowSelect() throws OclParseException {
		// self.employees.salary->select(s | s > 50000.0)->size()
		assertEquals(2, eval(
				"self.employees.salary->select(s | s > 50000.0)->size()", company1));
	}

	@Test
	void implicitCollect_thenArrowCollect() throws OclParseException {
		// self.employees.age->collect(a | a * 2)->sum()
		// ages: 30, 25, 45 → doubled: 60, 50, 90 → sum = 200
		assertEquals(200, eval(
				"self.employees.age->collect(a | a * 2)->sum()", company1));
	}

	@Test
	void implicitCollect_thenArrowReject() throws OclParseException {
		// reject names shorter than 4 chars → Alice, Carol
		assertEquals(2, eval(
				"self.employees.name->reject(n | n.size() < 4)->size()", company1));
	}

	@Test
	void implicitCollect_thenArrowExists() throws OclParseException {
		assertEquals(true, eval(
				"self.employees.name->exists(n | n = 'Bob')", company1));
	}

	@Test
	void implicitCollect_thenArrowForAll() throws OclParseException {
		assertEquals(true, eval(
				"self.employees.age->forAll(a | a > 20)", company1));
	}

	// --- Implicit collect with arithmetic result ---

	@Test
	void implicitCollect_salarySum() throws OclParseException {
		// sum of salaries via implicit collect
		assertEquals(240000.0, eval("self.employees.salary->sum()", company1));
	}

	@Test
	void implicitCollect_ageMin() throws OclParseException {
		assertEquals(25, eval("self.employees.age->min()", company1));
	}

	@Test
	void implicitCollect_ageMax() throws OclParseException {
		assertEquals(45, eval("self.employees.age->max()", company1));
	}

	// --- Implicit collect on select result ---

	@Test
	void selectThenImplicitCollect_names() throws OclParseException {
		Object result = eval(
				"self.employees->select(e | e.age > 28).name", company1);
		assertInstanceOf(List.class, result);
		@SuppressWarnings("unchecked")
		List<String> names = (List<String>) result;
		assertEquals(2, names.size());
		assertTrue(names.contains("Alice"));
		assertTrue(names.contains("Carol"));
	}

	@Test
	void selectThenImplicitCollect_ages() throws OclParseException {
		Object result = eval(
				"self.employees->select(e | e.isMarried).age", company1);
		assertInstanceOf(List.class, result);
		assertEquals(List.of(30, 45), result);
	}

	// --- Implicit collect with sortedBy ---

	@Test
	void implicitCollect_sortedNames() throws OclParseException {
		assertEquals(List.of("Alice", "Bob", "Carol"), eval(
				"self.employees.name->sortedBy(n | n)", company1));
	}

	// --- Implicit collect on company2 ---

	@Test
	void implicitCollect_company2Names() throws OclParseException {
		Object result = eval("self.employees.name", company2);
		assertInstanceOf(List.class, result);
		assertEquals(List.of("Dave", "Eve"), result);
	}

	@Test
	void implicitCollect_company2AgeSum() throws OclParseException {
		// Dave(35) + Eve(28) = 63
		assertEquals(63, eval("self.employees.age->sum()", company2));
	}

	// --- Implicit collect on empty ---

	@Test
	void implicitCollect_emptyEmployees() throws OclParseException {
		EObject empty = createCompany("Empty");
		Object result = eval("self.employees.name", empty);
		assertInstanceOf(List.class, result);
		assertEquals(0, ((List<?>) result).size());
	}

	@Test
	void implicitCollect_emptyThenSize() throws OclParseException {
		EObject empty = createCompany("Empty");
		assertEquals(0, eval("self.employees.name->size()", empty));
	}

	// --- Boolean implicit collect ---

	@Test
	void implicitCollect_booleanThenForAll() throws OclParseException {
		// Not all employees are married
		assertEquals(false, eval(
				"self.employees.isMarried->forAll(m | m)", company1));
	}

	@Test
	void implicitCollect_booleanThenExists() throws OclParseException {
		assertEquals(true, eval(
				"self.employees.isMarried->exists(m | m)", company1));
	}
}
