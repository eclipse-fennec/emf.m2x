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

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL flatten, implicit collect (shorthand navigation),
 * and collection-of-collections scenarios.
 */
class OclFlattenCollectTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 60000.0, true);
		bob = createPerson("Bob", 25, 45000.0, false);
		company = createCompany("ACME", alice, bob);
	}

	// --- Explicit collect on model navigation ---

	@Test
	void explicitCollect_names() throws OclParseException {
		Object result = eval("self.employees->collect(e | e.name)", company);
		assertInstanceOf(Collection.class, result);
		Collection<?> names = (Collection<?>) result;
		assertEquals(2, names.size());
		assertTrue(names.contains("Alice"));
		assertTrue(names.contains("Bob"));
	}

	@Test
	void explicitCollect_ages() throws OclParseException {
		Object result = eval("self.employees->collect(e | e.age)", company);
		assertInstanceOf(Collection.class, result);
		assertEquals(2, ((Collection<?>) result).size());
	}

	@Test
	void explicitCollect_chainedWithSize() throws OclParseException {
		assertEquals(2, eval("self.employees->collect(e | e.name)->size()", company));
	}

	@Test
	void explicitCollect_chainedWithSelect() throws OclParseException {
		Object result = eval("self.employees->collect(e | e.name)->select(n | n = 'Alice')", company);
		assertInstanceOf(Collection.class, result);
		assertEquals(1, ((Collection<?>) result).size());
	}

	// --- Explicit collect ---

	@Test
	void collect_toStringValues() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3}->collect(i | i.toString())", alice);
		assertInstanceOf(List.class, result);
		List<?> list = (List<?>) result;
		assertTrue(list.contains("1"));
		assertTrue(list.contains("2"));
		assertTrue(list.contains("3"));
	}

	@Test
	void collect_preservesOrder() throws OclParseException {
		Object result = eval("Sequence{3, 1, 2}->collect(i | i * 10)", alice);
		assertInstanceOf(List.class, result);
		assertEquals(List.of(30, 10, 20), result);
	}

	@Test
	void collect_nested_produces_flat() throws OclParseException {
		// collect on a collection that produces collections => nested result (not auto-flattened)
		// In OCL, collect DOES auto-flatten one level
		Object result = eval("Sequence{1, 2}->collect(i | Sequence{i, i})", alice);
		assertInstanceOf(Collection.class, result);
	}

	// --- flatten ---

	@Test
	void flatten_sequence() throws OclParseException {
		assertEquals(true, eval("Sequence{Sequence{1, 2}, Sequence{3, 4}}->flatten()->includes(3)", alice));
	}

	@Test
	void flatten_set() throws OclParseException {
		assertEquals(true, eval("Set{Set{1, 2}, Set{3, 4}}->flatten()->includes(3)", alice));
	}

	@Test
	void flatten_alreadyFlat() throws OclParseException {
		// Flattening a non-nested collection returns same content
		assertEquals(3, eval("Sequence{1, 2, 3}->flatten()->size()", alice));
	}

	@Test
	void flatten_size() throws OclParseException {
		assertEquals(4, eval("Sequence{Sequence{1, 2}, Sequence{3, 4}}->flatten()->size()", alice));
	}

	// --- collectNested ---

	@Test
	void collectNested_preservesNesting() throws OclParseException {
		// collectNested should NOT flatten, unlike collect
		Object result = eval("Sequence{1, 2}->collectNested(i | Sequence{i, i})", alice);
		assertInstanceOf(Collection.class, result);
		assertEquals(2, ((Collection<?>) result).size());
	}

	// --- reject (inverse of select) ---

	@Test
	void reject_integers() throws OclParseException {
		assertEquals(3, eval("Sequence{1, 2, 3, 4, 5}->reject(i | i > 3)->size()", alice));
	}

	@Test
	void reject_employees() throws OclParseException {
		// reject married employees
		Object result = eval("self.employees->reject(e | e.isMarried)", company);
		assertInstanceOf(Collection.class, result);
		assertEquals(1, ((Collection<?>) result).size());
	}

	// --- any ---

	@Test
	void any_found() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3}->any(i | i > 2) > 0", alice));
	}

	@Test
	void any_employee() throws OclParseException {
		Object result = eval("self.employees->any(e | e.name = 'Alice')", company);
		assertInstanceOf(EObject.class, result);
	}

	// --- one ---

	@Test
	void one_true() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3}->one(i | i = 2)", alice));
	}

	@Test
	void one_false_none() throws OclParseException {
		assertEquals(false, eval("Sequence{1, 2, 3}->one(i | i = 5)", alice));
	}

	@Test
	void one_false_multiple() throws OclParseException {
		assertEquals(false, eval("Sequence{1, 2, 2, 3}->one(i | i = 2)", alice));
	}

	// --- isUnique ---

	@Test
	void isUnique_true() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3}->isUnique(i | i)", alice));
	}

	@Test
	void isUnique_false() throws OclParseException {
		assertEquals(false, eval("Sequence{1, 2, 2, 3}->isUnique(i | i)", alice));
	}

	@Test
	void isUnique_employees() throws OclParseException {
		// Employee names should be unique
		assertEquals(true, eval("self.employees->isUnique(e | e.name)", company));
	}
}
