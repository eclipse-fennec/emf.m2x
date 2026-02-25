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

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for collect with complex body expressions on model data:
 * tuples, if-then-else, string building, arithmetic in collect body.
 */
class OclCollectNestedModelTest extends AbstractOclTest {

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

	// --- Collect with arithmetic body ---

	@Test
	void collect_doubleAge() throws OclParseException {
		assertEquals(List.of(60, 50, 90), eval(
				"self.employees->collect(e | e.age * 2)", company));
	}

	@Test
	void collect_monthlySalary() throws OclParseException {
		Object result = eval(
				"self.employees->collect(e | e.salary / 12.0)", company);
		assertInstanceOf(List.class, result);
		assertEquals(3, ((List<?>) result).size());
	}

	// --- Collect with string body ---

	@Test
	void collect_nameUpperCase() throws OclParseException {
		assertEquals(List.of("ALICE", "BOB", "CAROL"), eval(
				"self.employees->collect(e | e.name.toUpperCase())", company));
	}

	@Test
	void collect_nameConcat() throws OclParseException {
		assertEquals(List.of("Alice!", "Bob!", "Carol!"), eval(
				"self.employees->collect(e | e.name.concat('!'))", company));
	}

	@Test
	void collect_nameSizes() throws OclParseException {
		assertEquals(List.of(5, 3, 5), eval(
				"self.employees->collect(e | e.name.size())", company));
	}

	// --- Collect with if-then-else body ---

	@Test
	void collect_ifSenior() throws OclParseException {
		assertEquals(List.of("junior", "junior", "senior"), eval(
				"self.employees->collect(e | if e.age >= 35 then 'senior' else 'junior' endif)",
				company));
	}

	@Test
	void collect_ifHighEarner() throws OclParseException {
		assertEquals(List.of(true, false, true), eval(
				"self.employees->collect(e | e.salary > 50000.0)", company));
	}

	// --- Collect with tuple body ---

	@Test
	void collect_tuples() throws OclParseException {
		Object result = eval(
				"self.employees->collect(e | Tuple{n : String = e.name, a : Integer = e.age})",
				company);
		assertInstanceOf(List.class, result);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> tuples = (List<Map<String, Object>>) result;
		assertEquals(3, tuples.size());
		assertEquals("Alice", tuples.get(0).get("n"));
		assertEquals(30, tuples.get(0).get("a"));
	}

	// --- Collect then further operations ---

	@Test
	void collect_thenSum() throws OclParseException {
		// Double ages then sum: 60+50+90 = 200
		assertEquals(200, eval(
				"self.employees->collect(e | e.age * 2)->sum()", company));
	}

	@Test
	void collect_thenMax() throws OclParseException {
		assertEquals(90, eval(
				"self.employees->collect(e | e.age * 2)->max()", company));
	}

	@Test
	void collect_thenSelect() throws OclParseException {
		// Collect ages * 2, select those > 55
		assertEquals(2, eval(
				"self.employees->collect(e | e.age * 2)->select(a | a > 55)->size()", company));
	}

	// --- Nested collect ---

	@Test
	void collect_nameChars_size() throws OclParseException {
		// Collect name sizes
		assertEquals(List.of(5, 3, 5), eval(
				"self.employees.name->collect(n | n.size())", company));
	}

	// --- Collect with let in body ---

	@Test
	void collect_letInBody() throws OclParseException {
		assertEquals(List.of("Alice:30", "Bob:25", "Carol:45"), eval(
				"self.employees->collect(e | " +
				"let label : String = e.name.concat(':').concat(e.age.toString()) in label)",
				company));
	}
}
