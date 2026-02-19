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
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL {@code closure} iterator with cyclic graphs, self-references,
 * single-valued navigation, and deep chains.
 *
 * <p>Uses a dynamic GraphNode model with a multi-valued {@code neighbors}
 * reference that allows arbitrary graph topology including cycles.
 */
class OclClosureCycleTest extends AbstractOclTest {

	static EPackage graphPackage;
	static EClass graphNodeClass;
	static EReference neighborsRef;

	// Cycle: A → B → C → A
	static EObject nodeA;
	static EObject nodeB;
	static EObject nodeC;

	// Self-reference: D → D
	static EObject nodeD;

	// Linear chain: E → F → G → H → I (deep)
	static EObject nodeE;
	static EObject nodeF;
	static EObject nodeG;
	static EObject nodeH;
	static EObject nodeI;

	@BeforeAll
	static void setUp() {
		// Build GraphNode metamodel
		graphPackage = EcoreFactory.eINSTANCE.createEPackage();
		graphPackage.setName("graph");
		graphPackage.setNsPrefix("graph");
		graphPackage.setNsURI("http://test/graph");

		graphNodeClass = EcoreFactory.eINSTANCE.createEClass();
		graphNodeClass.setName("GraphNode");

		var nameAttr = EcoreFactory.eINSTANCE.createEAttribute();
		nameAttr.setName("name");
		nameAttr.setEType(EcorePackage.Literals.ESTRING);
		graphNodeClass.getEStructuralFeatures().add(nameAttr);

		neighborsRef = EcoreFactory.eINSTANCE.createEReference();
		neighborsRef.setName("neighbors");
		neighborsRef.setEType(graphNodeClass);
		neighborsRef.setUpperBound(-1);
		// NOT containment — allows cycles
		neighborsRef.setContainment(false);
		graphNodeClass.getEStructuralFeatures().add(neighborsRef);

		graphPackage.getEClassifiers().add(graphNodeClass);
		EPackage.Registry.INSTANCE.put(graphPackage.getNsURI(), graphPackage);

		// Build cycle: A → B → C → A
		nodeA = createNode("A");
		nodeB = createNode("B");
		nodeC = createNode("C");
		getNeighbors(nodeA).add(nodeB);
		getNeighbors(nodeB).add(nodeC);
		getNeighbors(nodeC).add(nodeA);

		// Self-reference: D → D
		nodeD = createNode("D");
		getNeighbors(nodeD).add(nodeD);

		// Linear chain: E → F → G → H → I
		nodeE = createNode("E");
		nodeF = createNode("F");
		nodeG = createNode("G");
		nodeH = createNode("H");
		nodeI = createNode("I");
		getNeighbors(nodeE).add(nodeF);
		getNeighbors(nodeF).add(nodeG);
		getNeighbors(nodeG).add(nodeH);
		getNeighbors(nodeH).add(nodeI);
	}

	private static EObject createNode(String name) {
		EObject node = graphPackage.getEFactoryInstance().create(graphNodeClass);
		node.eSet(graphNodeClass.getEStructuralFeature("name"), name);
		return node;
	}

	@SuppressWarnings("unchecked")
	private static List<EObject> getNeighbors(EObject node) {
		return (List<EObject>) node.eGet(neighborsRef);
	}

	// === Cyclic graph: A → B → C → A ===

	@Test
	void closure_cycle_terminates() throws OclParseException {
		// Starting from A: closure should visit B, C, then stop (A already seen)
		Object result = eval("self.neighbors->closure(n | n.neighbors)", nodeA);
		assertTrue(result instanceof Collection<?>);
		Collection<?> coll = (Collection<?>) result;
		// B, C, A (A is added because it's reachable, not because it's the start)
		assertEquals(3, coll.size());
	}

	@Test
	void closure_cycle_fromDifferentStart() throws OclParseException {
		// Starting from B: closure should visit C, A, then stop (B already seen)
		Object result = eval("self.neighbors->closure(n | n.neighbors)", nodeB);
		assertTrue(result instanceof Collection<?>);
		assertEquals(3, ((Collection<?>) result).size());
	}

	@Test
	void closure_cycle_containsAllNodes() throws OclParseException {
		// The closure from any node in the cycle should contain all 3 nodes
		assertEquals(true, eval(
				"self.neighbors->closure(n | n.neighbors)->size() = 3", nodeA));
	}

	// === Self-reference: D → D ===

	@Test
	void closure_selfReference_terminates() throws OclParseException {
		// D points to itself; closure should visit D and stop
		Object result = eval("self.neighbors->closure(n | n.neighbors)", nodeD);
		assertTrue(result instanceof Collection<?>);
		assertEquals(1, ((Collection<?>) result).size());
	}

	@Test
	void closure_selfReference_containsSelf() throws OclParseException {
		assertEquals(true, eval(
				"self.neighbors->closure(n | n.neighbors)->exists(n | n.name = 'D')", nodeD));
	}

	// === Deep linear chain: E → F → G → H → I ===

	@Test
	void closure_deepChain() throws OclParseException {
		// From E: closure traverses F, G, H, I
		Object result = eval("self.neighbors->closure(n | n.neighbors)", nodeE);
		assertTrue(result instanceof Collection<?>);
		assertEquals(4, ((Collection<?>) result).size());
	}

	@Test
	void closure_deepChain_reachesEnd() throws OclParseException {
		// I (the leaf) should be reachable from E
		assertEquals(true, eval(
				"self.neighbors->closure(n | n.neighbors)->exists(n | n.name = 'I')", nodeE));
	}

	@Test
	void closure_leaf_emptyResult() throws OclParseException {
		// I has no neighbors — closure yields empty set
		Object result = eval("self.neighbors->closure(n | n.neighbors)", nodeI);
		assertTrue(result instanceof Collection<?>);
		assertEquals(0, ((Collection<?>) result).size());
	}

	// === Closure result is always Set (no duplicates) ===

	@Test
	void closure_cycle_noDuplicates() throws OclParseException {
		// Even though there's a cycle, no node should appear twice
		assertEquals(true, eval(
				"self.neighbors->closure(n | n.neighbors)->isUnique(n | n)", nodeA));
	}

	// === Closure with integer-based bounded growth (regression) ===

	@Test
	void closure_integerBounded_terminates() throws OclParseException {
		// Integer-based: grow from 1 up to 5, stopping when guard is false
		Object result = eval(
				"Set{1}->closure(i | if i < 5 then Set{i + 1} else Set{} endif)", nodeA);
		assertTrue(result instanceof Collection<?>);
		assertEquals(5, ((Collection<?>) result).size());
	}
}
