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
 * Tests for long navigation chains combining dot access, arrow ops,
 * implicit collect, first/last, and property access in sequence.
 */
class OclNavigationChainTest extends AbstractOclTest {

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

	// --- Dot chain: scalar navigation ---

	@Test
	void dotChain_employerName() throws OclParseException {
		assertEquals("TechCorp", eval("self.employer.name", alice));
	}

	@Test
	void dotChain_employerNameSize() throws OclParseException {
		assertEquals(8, eval("self.employer.name.size()", alice));
	}

	@Test
	void dotChain_employerNameSubstring() throws OclParseException {
		assertEquals("Tech", eval("self.employer.name.substring(1, 4)", alice));
	}

	// --- Arrow chain: collection operations ---

	@Test
	void arrowChain_selectSortCollect() throws OclParseException {
		assertEquals(List.of("Alice", "Carol"), eval(
				"self.employees->select(e | e.isMarried)->sortedBy(e | e.name)->collect(e | e.name)",
				company));
	}

	@Test
	void arrowChain_selectSizeToString() throws OclParseException {
		assertEquals("2", eval(
				"self.employees->select(e | e.isMarried)->size().toString()", company));
	}

	// --- Mixed dot + arrow chains ---

	@Test
	void mixed_employerEmployeesSize() throws OclParseException {
		assertEquals(3, eval("self.employer.employees->size()", alice));
	}

	@Test
	void mixed_employerEmployeesFirstName() throws OclParseException {
		assertEquals("Alice", eval("self.employer.employees->first().name", alice));
	}

	@Test
	void mixed_employerEmployeesLastAge() throws OclParseException {
		assertEquals(45, eval("self.employer.employees->last().age", alice));
	}

	// --- Implicit collect in chain ---

	@Test
	void implicitCollect_namesSorted() throws OclParseException {
		assertEquals(List.of("Alice", "Bob", "Carol"), eval(
				"self.employer.employees.name->sortedBy(n | n)", alice));
	}

	@Test
	void implicitCollect_agesSum() throws OclParseException {
		assertEquals(100, eval("self.employer.employees.age->sum()", alice));
	}

	@Test
	void implicitCollect_salariesMax() throws OclParseException {
		assertEquals(120000.0, eval("self.employer.employees.salary->max()", alice));
	}

	// --- Round-trip navigation ---

	@Test
	void roundTrip_employerEmployeesExcludingSelf() throws OclParseException {
		assertEquals(2, eval(
				"self.employer.employees->excluding(self)->size()", alice));
	}

	@Test
	void roundTrip_employerEmployeesExcludingSelfNames() throws OclParseException {
		Object result = eval(
				"self.employer.employees->excluding(self).name->sortedBy(n | n)", alice);
		assertEquals(List.of("Bob", "Carol"), result);
	}

	// --- Long chain: select → sortedBy → first → property ---

	@Test
	void longChain_selectSortFirstName() throws OclParseException {
		assertEquals("Alice", eval(
				"self.employer.employees->select(e | e.isMarried)->sortedBy(e | e.age)->first().name",
				alice));
	}

	@Test
	void longChain_selectSortLastName() throws OclParseException {
		assertEquals("Carol", eval(
				"self.employer.employees->select(e | e.isMarried)->sortedBy(e | e.age)->last().name",
				alice));
	}

	// --- Chain with any ---

	@Test
	void chain_anyName() throws OclParseException {
		assertEquals("Bob", eval(
				"self.employer.employees->any(e | e.age < 30).name", alice));
	}

	// --- Chain with at ---

	@Test
	void chain_atName() throws OclParseException {
		assertEquals("Bob", eval(
				"self.employer.employees->at(2).name", alice));
	}

	// --- Chain ending in boolean ---

	@Test
	void chain_forAll() throws OclParseException {
		assertEquals(true, eval(
				"self.employer.employees.age->forAll(a | a > 18)", alice));
	}

	@Test
	void chain_exists() throws OclParseException {
		assertEquals(true, eval(
				"self.employer.employees.name->exists(n | n = 'Carol')", alice));
	}

	// --- Five-step chain ---

	@Test
	void fiveStepChain() throws OclParseException {
		// self → employer → employees → select → first → name
		assertEquals("Alice", eval(
				"self.employer.employees->select(e | e.salary > 50000.0)->first().name", alice));
	}
}
