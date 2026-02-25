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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for implicit collect (shorthand navigation).
 * In OCL, {@code self.employees.name} is equivalent to
 * {@code self.employees->collect(e | e.name)}.
 */
class OclImplicitCollectTest extends AbstractOclTest {

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

	// --- Basic implicit collect ---

	@Test
	void implicitCollect_name() throws OclParseException {
		Object result = eval("self.employees.name", company);
		assertInstanceOf(List.class, result);
		@SuppressWarnings("unchecked")
		List<String> names = (List<String>) result;
		assertEquals(3, names.size());
		assertTrue(names.contains("Alice"));
		assertTrue(names.contains("Bob"));
		assertTrue(names.contains("Carol"));
	}

	@Test
	void implicitCollect_age() throws OclParseException {
		Object result = eval("self.employees.age", company);
		assertInstanceOf(List.class, result);
		@SuppressWarnings("unchecked")
		List<Object> ages = (List<Object>) result;
		assertEquals(3, ages.size());
	}

	@Test
	void implicitCollect_salary() throws OclParseException {
		Object result = eval("self.employees.salary", company);
		assertInstanceOf(List.class, result);
	}

	@Test
	void implicitCollect_isMarried() throws OclParseException {
		Object result = eval("self.employees.isMarried", company);
		assertInstanceOf(List.class, result);
		assertEquals(List.of(true, false, true), result);
	}

	// --- Implicit collect then collection operation ---

	@Test
	void implicitCollect_thenSize() throws OclParseException {
		assertEquals(3, eval("self.employees.name->size()", company));
	}

	@Test
	void implicitCollect_thenIncludes() throws OclParseException {
		assertEquals(true, eval("self.employees.name->includes('Bob')", company));
	}

	@Test
	void implicitCollect_thenExcludes() throws OclParseException {
		assertEquals(true, eval("self.employees.name->excludes('Dave')", company));
	}

	@Test
	void implicitCollect_thenSum() throws OclParseException {
		// sum of ages: 30 + 25 + 45 = 100
		assertEquals(100, eval("self.employees.age->sum()", company));
	}

	// --- Implicit collect with sortedBy ---

	@Test
	void implicitCollect_sortedBy() throws OclParseException {
		Object result = eval("self.employees.name->sortedBy(n | n)", company);
		assertEquals(List.of("Alice", "Bob", "Carol"), result);
	}

	// --- Implicit collect with select ---

	@Test
	void implicitCollect_thenSelect() throws OclParseException {
		assertEquals(2, eval(
				"self.employees.name->select(n | n.size() > 3)->size()", company));
	}

	// --- Implicit collect with forAll ---

	@Test
	void implicitCollect_thenForAll() throws OclParseException {
		assertEquals(true, eval(
				"self.employees.name->forAll(n | n.size() > 0)", company));
	}

	// --- Implicit collect with exists ---

	@Test
	void implicitCollect_thenExists() throws OclParseException {
		assertEquals(true, eval(
				"self.employees.name->exists(n | n = 'Carol')", company));
	}

	// --- Explicit vs implicit comparison ---

	@Test
	void implicit_equalsExplicit_names() throws OclParseException {
		Object implicit = eval("self.employees.name", company);
		Object explicit = eval("self.employees->collect(e | e.name)", company);
		assertEquals(explicit, implicit);
	}

	@Test
	void implicit_equalsExplicit_ages() throws OclParseException {
		Object implicit = eval("self.employees.age", company);
		Object explicit = eval("self.employees->collect(e | e.age)", company);
		assertEquals(explicit, implicit);
	}

	// --- Empty collection ---

	@Test
	void implicitCollect_emptyCompany() throws OclParseException {
		EObject emptyCompany = createCompany("Empty");
		Object result = eval("self.employees.name", emptyCompany);
		assertInstanceOf(List.class, result);
		assertEquals(0, ((List<?>) result).size());
	}

	// --- Implicit collect on filtered result ---

	@Test
	void selectThenImplicit() throws OclParseException {
		Object result = eval(
				"self.employees->select(e | e.isMarried).name", company);
		assertInstanceOf(List.class, result);
		@SuppressWarnings("unchecked")
		List<String> names = (List<String>) result;
		assertEquals(2, names.size());
	}
}
