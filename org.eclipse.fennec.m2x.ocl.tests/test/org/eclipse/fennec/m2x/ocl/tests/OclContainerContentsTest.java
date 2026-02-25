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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for oclContainer() and oclContents() — OCL §11.3.1 (OclAny).
 *
 * <p>Spec semantics:
 * <ul>
 *   <li>{@code oclContainer()} returns the object for which self is a composed
 *       content, or null if there is no such object (root element).</li>
 *   <li>{@code oclContents()} returns the composed contents of self as a Set.</li>
 * </ul>
 *
 * <p>Eclipse OCL reference: ClassifierOclContainerOperation,
 * ClassifierOclContentsOperation, OclElementOclContainerProperty,
 * OclElementOclContentsProperty.
 */
class OclContainerContentsTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject charlie;
	static EObject company;
	static EObject standalonePerson;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 50000.0, true);
		bob = createPerson("Bob", 25, 40000.0, false);
		charlie = createPerson("Charlie", 35, 60000.0, true);
		company = createCompany("ACME", alice, bob, charlie);
		standalonePerson = createPerson("Standalone", 40, 70000.0, false);
	}

	// --- oclContainer() ---

	@Test
	void oclContainer_returnsParent() throws OclParseException {
		// alice is contained in company via employees reference
		Object result = eval("self.oclContainer()", alice);
		assertSame(company, result);
	}

	@Test
	void oclContainer_rootReturnsNull() throws OclParseException {
		// company has no container — it's a root element
		Object result = eval("self.oclContainer()", company);
		assertNull(result);
	}

	@Test
	void oclContainer_standaloneReturnsNull() throws OclParseException {
		// standalone person has no container
		Object result = eval("self.oclContainer()", standalonePerson);
		assertNull(result);
	}

	@Test
	void oclContainer_nullReturnsInvalid() throws OclParseException {
		assertInvalid("null.oclContainer()", alice);
	}

	@Test
	void oclContainer_invalidReturnsInvalid() throws OclParseException {
		assertInvalid("invalid.oclContainer()", alice);
	}

	@Test
	void oclContainer_chainNavigation() throws OclParseException {
		// alice.oclContainer() is the company, then access its name
		assertEquals("ACME", eval("self.oclContainer().name", alice));
	}

	@Test
	void oclContainer_allEmployeesHaveSameContainer() throws OclParseException {
		// All employees should have the same container (the company)
		assertEquals(true, eval(
				"self.employees->forAll(e | e.oclContainer() = self)", company));
	}

	// --- oclContents() ---

	@Test
	void oclContents_returnsChildren() throws OclParseException {
		Object result = eval("self.oclContents()", company);
		assertInstanceOf(Set.class, result);
		@SuppressWarnings("unchecked")
		Set<Object> contents = (Set<Object>) result;
		assertEquals(3, contents.size());
		assertTrue(contents.contains(alice));
		assertTrue(contents.contains(bob));
		assertTrue(contents.contains(charlie));
	}

	@Test
	void oclContents_leafReturnsEmptySet() throws OclParseException {
		// alice has no contained children
		Object result = eval("self.oclContents()", alice);
		assertInstanceOf(OclSet.class, result);
		assertEquals(0, ((Set<?>) result).size());
	}

	@Test
	void oclContents_standaloneLeafReturnsEmptySet() throws OclParseException {
		Object result = eval("self.oclContents()", standalonePerson);
		assertInstanceOf(OclSet.class, result);
		assertTrue(((Set<?>) result).isEmpty());
	}

	@Test
	void oclContents_nullReturnsInvalid() throws OclParseException {
		assertInvalid("null.oclContents()", alice);
	}

	@Test
	void oclContents_invalidReturnsInvalid() throws OclParseException {
		assertInvalid("invalid.oclContents()", alice);
	}

	@Test
	void oclContents_size() throws OclParseException {
		// size() returns Long in OCL
		Object result = eval("self.oclContents()->size()", company);
		assertEquals(3, ((Number) result).intValue());
	}

	@Test
	void oclContents_isEmpty() throws OclParseException {
		assertEquals(true, eval("self.oclContents()->isEmpty()", alice));
	}

	@Test
	void oclContents_notEmpty() throws OclParseException {
		assertEquals(true, eval("self.oclContents()->notEmpty()", company));
	}

	// --- Combined oclContainer + oclContents ---

	@Test
	void oclContents_containsChild() throws OclParseException {
		// company.oclContents() includes alice
		assertEquals(true, eval(
				"self.oclContents()->includes(self.employees->first())", company));
	}

	@Test
	void oclContainer_oclContents_roundtrip() throws OclParseException {
		// alice's container's contents should include alice
		assertEquals(true, eval(
				"self.oclContainer().oclContents()->includes(self)", alice));
	}

	@Test
	void oclContents_forAll() throws OclParseException {
		// All contents of company are persons (oclIsKindOf)
		assertEquals(true, eval(
				"self.oclContents()->forAll(c | c.oclIsKindOf(Person))", company));
	}
}
