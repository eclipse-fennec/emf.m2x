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

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for implicit iterator variable shorthand syntax (OCL v2.4 §7.5.5).
 *
 * <p>OCL allows three forms of iterator syntax:
 * <ol>
 *   <li>Complete: {@code ->select(p : Person | p.isMarried)}</li>
 *   <li>Shorter: {@code ->select(p | p.isMarried)}</li>
 *   <li>Shortest (implicit): {@code ->select(isMarried)}</li>
 * </ol>
 *
 * <p>The shortest form resolves unqualified names against the iterator element type.
 *
 * <p>Spec reference: Eclipse OCL {@code GenericIteratorsTest} tests all three forms.
 */
class OclImplicitIteratorTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject charlie;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 60000.0, true);
		bob = createPerson("Bob", 25, 45000.0, false);
		charlie = createPerson("Charlie", 35, 80000.0, true);
		company = createCompany("ACME", alice, bob, charlie);
	}

	// --- select() shorthand ---

	@Test
	void select_constant_true() throws OclParseException {
		// ->select(true) selects all elements
		assertEquals(3, eval("Set{1, 2, 3}->select(true)->size()", alice));
	}

	@Test
	void select_property_shorthand() throws OclParseException {
		// ->select(isMarried) = ->select(e | e.isMarried)
		assertEquals(2, eval("self.employees->select(isMarried)->size()", company));
	}

	@Test
	void select_equivalentToExplicit() throws OclParseException {
		// Shorthand and explicit form must produce the same result
		Object shorthand = eval("self.employees->select(isMarried)->size()", company);
		Object explicit = eval("self.employees->select(e | e.isMarried)->size()", company);
		assertEquals(explicit, shorthand);
	}

	// --- reject() shorthand ---

	@Test
	void reject_constant_true() throws OclParseException {
		// ->reject(true) rejects all elements
		assertEquals(0, eval("Set{1, 2, 3}->reject(true)->size()", alice));
	}

	@Test
	void reject_property_shorthand() throws OclParseException {
		// ->reject(isMarried) = ->reject(e | e.isMarried)
		assertEquals(1, eval("self.employees->reject(isMarried)->size()", company));
	}

	// --- collect() shorthand ---

	@Test
	@SuppressWarnings("unchecked")
	void collect_property_shorthand() throws OclParseException {
		// ->collect(name) = ->collect(e | e.name)
		Object result = eval("self.employees->collect(name)", company);
		assertInstanceOf(Collection.class, result);
		Collection<String> names = (Collection<String>) result;
		assertEquals(3, names.size());
	}

	@Test
	void collect_equivalentToExplicit() throws OclParseException {
		Object shorthand = eval("self.employees->collect(name)->sortedBy(n | n)->first()", company);
		Object explicit = eval("self.employees->collect(e | e.name)->sortedBy(n | n)->first()", company);
		assertEquals(explicit, shorthand);
	}

	// --- forAll() shorthand ---

	@Test
	void forAll_constant_true() throws OclParseException {
		assertEquals(true, eval("Set{1, 2, 3}->forAll(true)", alice));
	}

	@Test
	void forAll_property_shorthand() throws OclParseException {
		// Not all employees are married → false
		assertEquals(false, eval("self.employees->forAll(isMarried)", company));
	}

	// --- exists() shorthand ---

	@Test
	void exists_constant_true() throws OclParseException {
		assertEquals(true, eval("Set{1, 2, 3}->exists(true)", alice));
	}

	@Test
	void exists_property_shorthand() throws OclParseException {
		// At least one employee is married → true
		assertEquals(true, eval("self.employees->exists(isMarried)", company));
	}

	// --- any() shorthand ---

	@Test
	void any_constant_true() throws OclParseException {
		// ->any(true) returns first element
		Object result = eval("Set{1, 2, 3}->any(true)", alice);
		assertInstanceOf(Integer.class, result);
	}

	@Test
	void any_property_shorthand() throws OclParseException {
		// ->any(isMarried) returns a married person
		assertEquals(true, eval("self.employees->any(isMarried).isMarried", company));
	}

	// --- one() shorthand ---

	@Test
	void one_constant_true_singleElement() throws OclParseException {
		assertEquals(true, eval("Set{1}->one(true)", alice));
	}

	@Test
	void one_constant_true_multipleElements() throws OclParseException {
		assertEquals(false, eval("Set{1, 2}->one(true)", alice));
	}

	// --- isUnique() shorthand ---

	@Test
	void isUnique_property_shorthand() throws OclParseException {
		// All employee names are unique
		assertEquals(true, eval("self.employees->isUnique(name)", company));
	}

	@Test
	void isUnique_constant_allSame() throws OclParseException {
		// All elements map to same value → not unique
		assertEquals(false, eval("Set{1, 2}->isUnique(true)", alice));
	}

	// --- sortedBy() shorthand ---

	@Test
	void sortedBy_property_shorthand() throws OclParseException {
		// Sort by name → first is Alice
		Object result = eval("self.employees->sortedBy(name)->first().name", company);
		assertEquals("Alice", result);
	}

	@Test
	void sortedBy_equivalentToExplicit() throws OclParseException {
		Object shorthand = eval("self.employees->sortedBy(name)->last().name", company);
		Object explicit = eval("self.employees->sortedBy(e | e.name)->last().name", company);
		assertEquals(explicit, shorthand);
	}

	// --- Mixed: shorthand in chain ---

	@Test
	void chain_selectCollect_shorthand() throws OclParseException {
		// Select married, collect names
		Object result = eval(
				"self.employees->select(isMarried)->collect(name)->sortedBy(n | n)->first()",
				company);
		assertEquals("Alice", result);
	}

	// --- Edge cases ---

	@Test
	void shorthand_emptyCollection() throws OclParseException {
		assertEquals(0, eval("Sequence{}->select(true)->size()", alice));
	}

	@Test
	void shorthand_exists_false() throws OclParseException {
		assertEquals(false, eval("Set{1, 2, 3}->exists(false)", alice));
	}

	@Test
	void shorthand_forAll_false() throws OclParseException {
		assertEquals(false, eval("Set{1, 2, 3}->forAll(false)", alice));
	}
}
