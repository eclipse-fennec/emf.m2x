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
 * Tests for select/reject with complex conditions on model data,
 * followed by property access, chaining, and implicit collect.
 */
class OclSelectRejectModelTest extends AbstractOclTest {

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

	// --- Select with property access on result ---

	@Test
	void select_married_names() throws OclParseException {
		assertEquals(List.of("Alice", "Carol"), eval(
				"self.employees->select(e | e.isMarried).name", company));
	}

	@Test
	void select_highEarner_names() throws OclParseException {
		assertEquals(List.of("Alice", "Carol"), eval(
				"self.employees->select(e | e.salary > 50000.0).name", company));
	}

	@Test
	void select_young_ages() throws OclParseException {
		assertEquals(List.of(25, 22), eval(
				"self.employees->select(e | e.age < 30).age", company));
	}

	// --- Reject with property access on result ---

	@Test
	void reject_married_names() throws OclParseException {
		assertEquals(List.of("Bob", "Dave"), eval(
				"self.employees->reject(e | e.isMarried).name", company));
	}

	@Test
	void reject_lowSalary_names() throws OclParseException {
		assertEquals(List.of("Alice", "Carol"), eval(
				"self.employees->reject(e | e.salary < 50000.0).name", company));
	}

	// --- Select then first().property ---

	@Test
	void select_first_name() throws OclParseException {
		assertEquals("Alice", eval(
				"self.employees->select(e | e.isMarried)->first().name", company));
	}

	@Test
	void select_last_name() throws OclParseException {
		assertEquals("Carol", eval(
				"self.employees->select(e | e.isMarried)->last().name", company));
	}

	// --- Complex conditions ---

	@Test
	void select_andCondition() throws OclParseException {
		// Married AND age > 35
		assertEquals(List.of("Carol"), eval(
				"self.employees->select(e | e.isMarried and e.age > 35).name", company));
	}

	@Test
	void select_orCondition() throws OclParseException {
		// Young OR high earner (age<25 or salary>100000)
		assertEquals(List.of("Carol", "Dave"), eval(
				"self.employees->select(e | e.age < 25 or e.salary > 100000.0).name", company));
	}

	@Test
	void select_notCondition() throws OclParseException {
		assertEquals(List.of("Bob", "Dave"), eval(
				"self.employees->select(e | not e.isMarried).name", company));
	}

	// --- Select then sortedBy ---

	@Test
	void select_sortedBy_name() throws OclParseException {
		assertEquals(List.of("Alice", "Carol"), eval(
				"self.employees->select(e | e.isMarried)->sortedBy(e | e.name)->collect(e | e.name)",
				company));
	}

	@Test
	void select_sortedBy_age_first_name() throws OclParseException {
		// Youngest married employee
		assertEquals("Alice", eval(
				"self.employees->select(e | e.isMarried)->sortedBy(e | e.age)->first().name",
				company));
	}

	// --- Chained select ---

	@Test
	void select_then_select() throws OclParseException {
		// First: salary > 40000 → Alice, Bob(40000 not >), Carol, Dave(35000)
		// Wait: Bob=40000 is NOT > 40000, so first select gives Alice, Carol
		// Second: age > 35 → Carol only
		assertEquals(1, eval(
				"self.employees->select(e | e.salary > 40000.0)->select(e | e.age > 35)->size()",
				company));
	}

	@Test
	void select_then_select_name() throws OclParseException {
		assertEquals("Carol", eval(
				"self.employees->select(e | e.salary > 40000.0)->select(e | e.age > 35)->first().name",
				company));
	}

	// --- Select + reject complement ---

	@Test
	void select_reject_complement() throws OclParseException {
		// select + reject should cover all employees
		Object selectSize = eval(
				"self.employees->select(e | e.isMarried)->size()", company);
		Object rejectSize = eval(
				"self.employees->reject(e | e.isMarried)->size()", company);
		assertEquals(4, ((Number) selectSize).intValue() + ((Number) rejectSize).intValue());
	}

	// --- Select with string condition ---

	@Test
	void select_nameLength() throws OclParseException {
		// Names with length > 3: Alice(5), Carol(5), Dave(4) → 3
		assertEquals(3, eval(
				"self.employees->select(e | e.name.size() > 3)->size()", company));
	}

	@Test
	void select_nameStartsWith() throws OclParseException {
		assertEquals(List.of("Alice"), eval(
				"self.employees->select(e | e.name.substring(1, 1) = 'A').name", company));
	}

	// --- Empty result ---

	@Test
	void select_noneMatch() throws OclParseException {
		assertEquals(0, eval(
				"self.employees->select(e | e.age > 100)->size()", company));
	}

	@Test
	void select_noneMatch_names() throws OclParseException {
		assertEquals(List.of(), eval(
				"self.employees->select(e | e.age > 100).name", company));
	}
}
