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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for the OCL {@code any} iterator.
 * {@code any(expr)} returns an arbitrary element satisfying the condition,
 * or null/invalid if none matches.
 */
class OclAnyTest extends AbstractOclTest {

	static EObject self;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
		EObject bob = createPerson("Bob", 25, 40000.0, false);
		EObject carol = createPerson("Carol", 35, 60000.0, true);
		company = createCompany("Acme", self, bob, carol);
	}

	// --- any on literal collections ---

	@Test
	void any_singleMatch() throws OclParseException {
		assertEquals(3, eval("Sequence{1, 2, 3}->any(i | i = 3)", self));
	}

	@Test
	void any_firstMatch() throws OclParseException {
		// any returns some matching element
		Object result = eval("Sequence{1, 2, 3}->any(i | i > 1)", self);
		assertNotNull(result);
	}

	@Test
	void any_noMatch() throws OclParseException {
		// any with no match returns null (OclVoid)
		assertNull(eval("Sequence{1, 2, 3}->any(i | i > 10)", self));
	}

	@Test
	void any_allMatch() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3}->any(i | i > 0)", self);
		assertNotNull(result);
	}

	@Test
	void any_singleElement() throws OclParseException {
		assertEquals(42, eval("Sequence{42}->any(i | i = 42)", self));
	}

	@Test
	void any_emptyCollection() throws OclParseException {
		assertNull(eval("Sequence{}->any(i | true)", self));
	}

	// --- any on strings ---

	@Test
	void any_string() throws OclParseException {
		assertEquals("hello", eval(
				"Sequence{'hello', 'world'}->any(s | s = 'hello')", self));
	}

	@Test
	void any_stringBySize() throws OclParseException {
		Object result = eval(
				"Sequence{'a', 'bb', 'ccc'}->any(s | s.size() > 1)", self);
		assertNotNull(result);
	}

	// --- any on Set ---

	@Test
	void any_onSet() throws OclParseException {
		Object result = eval("Set{1, 2, 3}->any(i | i > 0)", self);
		assertNotNull(result);
	}

	// --- any on Bag ---

	@Test
	void any_onBag() throws OclParseException {
		Object result = eval("Bag{1, 2, 3}->any(i | i = 2)", self);
		assertEquals(2, result);
	}

	// --- any on OrderedSet ---

	@Test
	void any_onOrderedSet() throws OclParseException {
		Object result = eval("OrderedSet{1, 2, 3}->any(i | i > 0)", self);
		assertNotNull(result);
	}

	// --- any on model ---

	@Test
	void any_employeeByName() throws OclParseException {
		assertEquals("Bob", eval(
				"self.employees->any(e | e.name = 'Bob').name", company));
	}

	@Test
	void any_employeeNotFound() throws OclParseException {
		assertNull(eval(
				"self.employees->any(e | e.name = 'Nobody')", company));
	}

	@Test
	void any_employeeBySalary() throws OclParseException {
		assertEquals("Carol", eval(
				"self.employees->any(e | e.salary > 50000.0).name", company));
	}

	// --- any combined with operations ---

	@Test
	void any_thenIsUndefined() throws OclParseException {
		assertEquals(false, eval(
				"Sequence{1, 2, 3}->any(i | i = 2).oclIsUndefined()", self));
	}

	@Test
	void any_noMatch_thenIsUndefined() throws OclParseException {
		// any returns null when no match, null.oclIsUndefined() = true
		Object result = eval(
				"Sequence{1, 2, 3}->any(i | i > 10).oclIsUndefined()", self);
		assertEquals(true, result);
	}

	// --- any with boolean body ---

	@Test
	void any_trueCondition() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3}->any(i | true)", self);
		assertNotNull(result);
	}

	@Test
	void any_falseCondition() throws OclParseException {
		assertNull(eval("Sequence{1, 2, 3}->any(i | false)", self));
	}

	// --- any with modular condition ---

	@Test
	void any_modCondition() throws OclParseException {
		// Find element divisible by 3
		assertEquals(3, eval(
				"Sequence{1, 2, 3, 4, 5}->any(i | i.mod(3) = 0)", self));
	}
}
