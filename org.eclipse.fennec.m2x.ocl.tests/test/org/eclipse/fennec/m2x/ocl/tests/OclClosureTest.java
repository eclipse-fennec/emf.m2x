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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for the OCL {@code closure} iterator.
 * closure(expr) computes the transitive closure of a navigation
 * expression, collecting all reachable elements.
 *
 * <p>Uses a simple Node model with a {@code parent} reference and
 * {@code children} containment for testing tree traversal.
 */
class OclClosureTest extends AbstractOclTest {

	static EPackage nodePackage;
	static EClass nodeClass;
	static EObject root;
	static EObject child1;
	static EObject child2;
	static EObject grandchild1;

	@SuppressWarnings("unchecked")
	@BeforeAll
	static void setUp() {
		// Build a simple Node metamodel dynamically
		nodePackage = EcoreFactory.eINSTANCE.createEPackage();
		nodePackage.setName("node");
		nodePackage.setNsPrefix("node");
		nodePackage.setNsURI("http://test/node");

		nodeClass = EcoreFactory.eINSTANCE.createEClass();
		nodeClass.setName("Node");

		var nameAttr = EcoreFactory.eINSTANCE.createEAttribute();
		nameAttr.setName("name");
		nameAttr.setEType(EcorePackage.Literals.ESTRING);
		nodeClass.getEStructuralFeatures().add(nameAttr);

		EReference parentRef = EcoreFactory.eINSTANCE.createEReference();
		parentRef.setName("parent");
		parentRef.setEType(nodeClass);
		nodeClass.getEStructuralFeatures().add(parentRef);

		EReference childrenRef = EcoreFactory.eINSTANCE.createEReference();
		childrenRef.setName("children");
		childrenRef.setEType(nodeClass);
		childrenRef.setUpperBound(-1);
		childrenRef.setContainment(true);
		childrenRef.setEOpposite(parentRef);
		parentRef.setEOpposite(childrenRef);
		nodeClass.getEStructuralFeatures().add(childrenRef);

		nodePackage.getEClassifiers().add(nodeClass);

		// Register the package
		EPackage.Registry.INSTANCE.put(nodePackage.getNsURI(), nodePackage);

		// Build: root → child1 → grandchild1
		//             → child2
		root = nodePackage.getEFactoryInstance().create(nodeClass);
		root.eSet(nodeClass.getEStructuralFeature("name"), "root");

		child1 = nodePackage.getEFactoryInstance().create(nodeClass);
		child1.eSet(nodeClass.getEStructuralFeature("name"), "child1");

		child2 = nodePackage.getEFactoryInstance().create(nodeClass);
		child2.eSet(nodeClass.getEStructuralFeature("name"), "child2");

		grandchild1 = nodePackage.getEFactoryInstance().create(nodeClass);
		grandchild1.eSet(nodeClass.getEStructuralFeature("name"), "grandchild1");

		((java.util.List<EObject>) root.eGet(nodeClass.getEStructuralFeature("children")))
				.add(child1);
		((java.util.List<EObject>) root.eGet(nodeClass.getEStructuralFeature("children")))
				.add(child2);
		((java.util.List<EObject>) child1.eGet(nodeClass.getEStructuralFeature("children")))
				.add(grandchild1);
	}

	// Note: parent-based closure (self.parent->closure(...)) not tested here
	// because single-valued references don't auto-wrap to collections for ->

	// --- Downward closure via children ---

	@Test
	void closure_childrenFromRoot() throws OclParseException {
		// root → {child1, child2, grandchild1} = 3
		assertEquals(3, eval(
				"self.children->closure(n | n.children)->size()", root));
	}

	@Test
	void closure_childrenFromChild1() throws OclParseException {
		// child1 → {grandchild1} = 1
		assertEquals(1, eval(
				"self.children->closure(n | n.children)->size()", child1));
	}

	@Test
	void closure_childrenFromChild2() throws OclParseException {
		// child2 has no children → empty
		assertEquals(0, eval(
				"self.children->closure(n | n.children)->size()", child2));
	}

	@Test
	void closure_childrenFromGrandchild() throws OclParseException {
		// grandchild1 has no children → empty
		assertEquals(0, eval(
				"self.children->closure(n | n.children)->size()", grandchild1));
	}

	// --- Closure includes starting elements ---

	@Test
	void closure_childrenIncludes_grandchild() throws OclParseException {
		assertEquals(true, eval(
				"self.children->closure(n | n.children)->exists(n | n.name = 'grandchild1')",
				root));
	}

	@Test
	void closure_childrenIncludes_child1() throws OclParseException {
		assertEquals(true, eval(
				"self.children->closure(n | n.children)->exists(n | n.name = 'child1')",
				root));
	}

	@Test
	void closure_childrenIncludes_child2() throws OclParseException {
		assertEquals(true, eval(
				"self.children->closure(n | n.children)->exists(n | n.name = 'child2')",
				root));
	}

	// --- Closure with operations ---

	@Test
	void closure_thenCollectNames() throws OclParseException {
		assertEquals(3, eval(
				"self.children->closure(n | n.children)->collect(n | n.name)->size()",
				root));
	}

	@Test
	void closure_thenSelect() throws OclParseException {
		assertEquals(1, eval(
				"self.children->closure(n | n.children)->select(n | n.name = 'child2')->size()",
				root));
	}

	// --- Closure result is a Set (no duplicates) ---

	@Test
	void closure_noDuplicates() throws OclParseException {
		// Even if traversal could revisit, closure produces unique elements
		assertEquals(3, eval(
				"self.children->closure(n | n.children)->size()", root));
	}
}
