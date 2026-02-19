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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for the any() iterator operation.
 * {@code any(expr)} returns an arbitrary element satisfying the condition,
 * or null if none found.
 */
class OclAnyOperationsTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject carol;
	static EObject company;
	static EObject self;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 80000.0, true);
		bob = createPerson("Bob", 25, 40000.0, false);
		carol = createPerson("Carol", 45, 120000.0, true);
		company = createCompany("TechCorp", alice, bob, carol);
		self = createPerson("Test", 20, 30000.0, false);
	}

	// --- any() on literal collections ---

	@Test
	void any_sequenceFound() throws OclParseException {
		// any element > 3 in {1,2,3,4,5}
		Object result = eval("Sequence{1, 2, 3, 4, 5}->any(x | x > 3)", self);
		// Should be 4 (first matching element)
		assertEquals(4, result);
	}

	@Test
	void any_sequenceNotFound() throws OclParseException {
		// No element > 10
		assertEquals(null, eval("Sequence{1, 2, 3}->any(x | x > 10)", self));
	}

	@Test
	void any_setFound() throws OclParseException {
		// Any element > 15, should be one of 20 or 30
		assertEquals(true, eval("Set{10, 20, 30}->any(x | x > 15) > 15", self));
	}

	@Test
	void any_emptyCollection() throws OclParseException {
		assertEquals(null, eval("Sequence{}->any(x | true)", self));
	}

	// --- any() with strings ---

	@Test
	void any_stringStartsWith() throws OclParseException {
		assertEquals("apple", eval(
				"Sequence{'apple', 'banana', 'cherry'}->any(s | s.substring(1, 1) = 'a')", self));
	}

	@Test
	void any_stringLengthCondition() throws OclParseException {
		// First string with size > 5
		assertEquals("banana", eval(
				"Sequence{'apple', 'banana', 'cherry'}->any(s | s.size() > 5)", self));
	}

	// --- any() with model data ---

	@Test
	void any_employeeByName() throws OclParseException {
		assertEquals("Bob", eval(
				"self.employees->any(e | e.name = 'Bob').name", company));
	}

	@Test
	void any_employeeByAge() throws OclParseException {
		assertEquals("Carol", eval(
				"self.employees->any(e | e.age > 40).name", company));
	}

	@Test
	void any_employeeNotFound() throws OclParseException {
		assertEquals(null, eval(
				"self.employees->any(e | e.name = 'Zara')", company));
	}

	@Test
	void any_marriedEmployee() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->any(e | e.isMarried).isMarried", company));
	}

	// --- any() with boolean body ---

	@Test
	void any_allTrue() throws OclParseException {
		// All elements satisfy, return first
		assertEquals(1, eval("Sequence{1, 2, 3}->any(x | x > 0)", self));
	}

	// --- any() combined with other operations ---

	@Test
	void any_thenProperty() throws OclParseException {
		assertEquals("Carol", eval(
				"self.employees->any(e | e.salary > 100000.0).name", company));
	}

	@Test
	void any_thenComparison() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->any(e | e.name = 'Alice').age = 30", company));
	}
}
