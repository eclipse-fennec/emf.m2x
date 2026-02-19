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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL {@code selectByKind} and {@code selectByType} collection
 * operations per OCL v2.4 §11.9.1.
 *
 * <p>{@code selectByKind(type)} returns elements that are instances of the
 * given type (including subtypes). {@code selectByType(type)} returns only
 * exact type matches.
 */
class OclSelectByKindTypeTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 60000.0, true);
		bob = createPerson("Bob", 25, 45000.0, false);
		company = createCompany("ACME", alice, bob);
	}

	// === selectByKind with model types ===

	@Test
	void selectByKind_allMatchingType() throws OclParseException {
		// All employees are Persons
		Object result = eval(
				"self.employees->selectByKind(Person)", company);
		assertTrue(result instanceof Collection<?>);
		assertEquals(2, ((Collection<?>) result).size());
	}

	@Test
	void selectByKind_noMatchingType() throws OclParseException {
		// No employees are Companies
		Object result = eval(
				"self.employees->selectByKind(Company)", company);
		assertTrue(result instanceof Collection<?>);
		assertEquals(0, ((Collection<?>) result).size());
	}

	// === selectByType with model types ===

	@Test
	void selectByType_exactMatch() throws OclParseException {
		// All employees are exactly Person (no subtypes in our model)
		Object result = eval(
				"self.employees->selectByType(Person)", company);
		assertTrue(result instanceof Collection<?>);
		assertEquals(2, ((Collection<?>) result).size());
	}

	@Test
	void selectByType_noMatch() throws OclParseException {
		Object result = eval(
				"self.employees->selectByType(Company)", company);
		assertTrue(result instanceof Collection<?>);
		assertEquals(0, ((Collection<?>) result).size());
	}

	// === selectByKind with primitive types on heterogeneous collections ===

	@Test
	void selectByKind_integersFromMixed() throws OclParseException {
		// Mixed collection: select only integers
		Object result = eval(
				"Sequence{1, 'hello', 2, true, 3}->selectByKind(Integer)", alice);
		assertTrue(result instanceof Collection<?>);
		assertEquals(3, ((Collection<?>) result).size());
	}

	@Test
	void selectByKind_stringsFromMixed() throws OclParseException {
		Object result = eval(
				"Sequence{1, 'hello', 2, 'world', 3}->selectByKind(String)", alice);
		assertTrue(result instanceof Collection<?>);
		assertEquals(2, ((Collection<?>) result).size());
	}

	@Test
	void selectByKind_booleansFromMixed() throws OclParseException {
		Object result = eval(
				"Sequence{1, true, 'x', false}->selectByKind(Boolean)", alice);
		assertTrue(result instanceof Collection<?>);
		assertEquals(2, ((Collection<?>) result).size());
	}

	// === selectByKind on empty collection ===

	@Test
	void selectByKind_emptyCollection() throws OclParseException {
		Object result = eval("Sequence{}->selectByKind(Integer)", alice);
		assertTrue(result instanceof Collection<?>);
		assertEquals(0, ((Collection<?>) result).size());
	}

	// === selectByKind preserves collection kind ===

	@Test
	void selectByKind_setResult() throws OclParseException {
		Object result = eval(
				"Set{1, 'a', 2, 'b'}->selectByKind(Integer)->size()", alice);
		assertEquals(2, result);
	}

	// === selectByKind on invalid source ===

	@Test
	void selectByKind_invalidSource() throws OclParseException {
		assertInvalid("invalid->selectByKind(Integer)", alice);
	}

	// === Chained selectByKind ===

	@Test
	void selectByKind_chained() throws OclParseException {
		// selectByKind then size
		assertEquals(3, eval(
				"Sequence{1, 'a', 2, 'b', 3}->selectByKind(Integer)->size()", alice));
	}

	@Test
	void selectByKind_chainedWithCollect() throws OclParseException {
		// selectByKind then operations on the filtered result
		assertEquals(6, eval(
				"Sequence{1, 'a', 2, 'b', 3}->selectByKind(Integer)->sum()", alice));
	}
}
