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

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclContext;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for includes/excludes/including/excluding on model collections
 * with EObject identity, and implicit collect in combination.
 */
class OclCollectionIncludesExcludesModelTest extends AbstractOclTest {

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

	// --- includes with EObject identity ---

	@Test
	void includes_self() throws OclParseException {
		assertEquals(true, eval("self.employer.employees->includes(self)", alice));
	}

	@Test
	void includes_first() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->includes(self.employees->first())", company));
	}

	@Test
	void excludes_notInCollection() throws OclParseException {
		EObject stranger = createPerson("Stranger", 99, 0.0, false);
		assertEquals(true, engine.evaluate(
				"self.employees->excludes(stranger)",
				OclContext.of(company, Map.of("stranger", stranger))));
	}

	// --- excluding with EObject ---

	@Test
	void excluding_self_size() throws OclParseException {
		assertEquals(2, eval(
				"self.employer.employees->excluding(self)->size()", alice));
	}

	@Test
	void excluding_self_names() throws OclParseException {
		Object result = eval(
				"self.employer.employees->excluding(self).name", alice);
		assertInstanceOf(List.class, result);
		@SuppressWarnings("unchecked")
		List<String> names = (List<String>) result;
		assertEquals(2, names.size());
		assertEquals(true, names.contains("Bob"));
		assertEquals(true, names.contains("Carol"));
	}

	@Test
	void excluding_first() throws OclParseException {
		assertEquals(2, eval(
				"self.employees->excluding(self.employees->first())->size()", company));
	}

	// --- including ---

	@Test
	void including_newPerson() throws OclParseException {
		// including adds to the collection (size + 1)
		assertEquals(4, eval(
				"self.employees->including(self.employees->first())->size()", company));
	}

	// --- includes on implicit collect result ---

	@Test
	void implicitCollect_includes_name() throws OclParseException {
		assertEquals(true, eval("self.employees.name->includes('Alice')", company));
	}

	@Test
	void implicitCollect_excludes_name() throws OclParseException {
		assertEquals(true, eval("self.employees.name->excludes('Dave')", company));
	}

	@Test
	void implicitCollect_includesAll() throws OclParseException {
		assertEquals(true, eval(
				"self.employees.name->includesAll(Sequence{'Alice', 'Bob'})", company));
	}

	@Test
	void implicitCollect_excludesAll() throws OclParseException {
		assertEquals(true, eval(
				"self.employees.name->excludesAll(Sequence{'Dave', 'Eve'})", company));
	}

	// --- excluding then implicit collect ---

	@Test
	void excluding_thenImplicit_names() throws OclParseException {
		Object result = eval(
				"self.employer.employees->excluding(self).name->sortedBy(n | n)", alice);
		assertEquals(List.of("Bob", "Carol"), result);
	}

	// --- select then excluding ---

	@Test
	void select_excluding_size() throws OclParseException {
		// married employees excluding alice → just Carol
		assertEquals(1, eval(
				"self.employees->select(e | e.isMarried)->excluding(self.employees->first())->size()",
				company));
	}

	// --- includesAll / excludesAll on model ---

	@Test
	void includesAll_employees() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->includesAll(self.employees->select(e | e.isMarried))", company));
	}

	@Test
	void excludesAll_disjoint() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->select(e | e.isMarried)->excludesAll(self.employees->reject(e | e.isMarried))",
				company));
	}
}
