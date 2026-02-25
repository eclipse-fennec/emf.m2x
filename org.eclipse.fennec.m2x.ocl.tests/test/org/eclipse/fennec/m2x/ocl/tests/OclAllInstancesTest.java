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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclModelExtent;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL {@code allInstances()} operation per OCL v2.4 §11.2.
 *
 * <p>{@code Type.allInstances()} returns the Set of all instances of the
 * given Type within the model extent. Requires an {@link OclModelExtent}
 * to be provided in the evaluation context.
 */
class OclAllInstancesTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject charlie;
	static EObject company;
	static OclModelExtent extent;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 60000.0, true);
		bob = createPerson("Bob", 25, 45000.0, false);
		charlie = createPerson("Charlie", 35, 70000.0, true);
		company = createCompany("ACME", alice, bob, charlie);

		// Create extent that knows about all instances
		List<EObject> allObjects = new ArrayList<>();
		allObjects.add(company);
		allObjects.add(alice);
		allObjects.add(bob);
		allObjects.add(charlie);

		extent = eClass -> {
			List<EObject> result = new ArrayList<>();
			for (EObject obj : allObjects) {
				if (eClass.isInstance(obj)) {
					result.add(obj);
				}
			}
			return result;
		};
	}

	private Object evalWithExtent(String expression, EObject self) throws OclParseException {
		return engine.evaluate(expression, new OclContext(self, extent, java.util.Map.of()));
	}

	// === Basic allInstances ===

	@Test
	void allInstances_personType() throws OclParseException {
		Object result = evalWithExtent("Person.allInstances()", alice);
		assertTrue(result instanceof Collection<?>);
		assertEquals(3, ((Collection<?>) result).size());
	}

	@Test
	void allInstances_companyType() throws OclParseException {
		Object result = evalWithExtent("Company.allInstances()", alice);
		assertTrue(result instanceof Collection<?>);
		assertEquals(1, ((Collection<?>) result).size());
	}

	// === allInstances with no matching instances ===

	@Test
	void allInstances_noInstances() throws OclParseException {
		// Use an extent with no Company instances
		OclModelExtent emptyExtent = eClass -> List.of();
		Object result = engine.evaluate("Company.allInstances()",
				new OclContext(alice, emptyExtent, java.util.Map.of()));
		assertTrue(result instanceof Collection<?>);
		assertEquals(0, ((Collection<?>) result).size());
	}

	// === allInstances chained with operations ===

	@Test
	void allInstances_select() throws OclParseException {
		Object result = evalWithExtent(
				"Person.allInstances()->select(p | p.isMarried)", alice);
		assertTrue(result instanceof Collection<?>);
		assertEquals(2, ((Collection<?>) result).size()); // Alice and Charlie
	}

	@Test
	void allInstances_collect_names() throws OclParseException {
		Object result = evalWithExtent(
				"Person.allInstances()->collect(p | p.name)->size()", alice);
		assertEquals(3, result);
	}

	@Test
	void allInstances_forAll() throws OclParseException {
		// All persons have age > 0
		assertEquals(true, evalWithExtent(
				"Person.allInstances()->forAll(p | p.age > 0)", alice));
	}

	@Test
	void allInstances_exists() throws OclParseException {
		assertEquals(true, evalWithExtent(
				"Person.allInstances()->exists(p | p.name = 'Bob')", alice));
	}

	@Test
	void allInstances_size() throws OclParseException {
		assertEquals(3, evalWithExtent("Person.allInstances()->size()", alice));
	}

	// === allInstances without extent → invalid ===

	@Test
	void allInstances_noExtent_isInvalid() throws OclParseException {
		// When no extent is provided, allInstances should produce invalid
		assertInvalid("Person.allInstances()", alice);
	}

	// === allInstances returns a Set (no duplicates) ===

	@Test
	void allInstances_returnsSet() throws OclParseException {
		// Verify it's a Set-like collection (isUnique)
		assertEquals(true, evalWithExtent(
				"Person.allInstances()->isUnique(p | p)", alice));
	}
}
