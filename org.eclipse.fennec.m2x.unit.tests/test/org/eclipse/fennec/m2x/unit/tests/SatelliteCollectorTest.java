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
package org.eclipse.fennec.m2x.unit.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.fennec.m2x.unit.satellite.SatelliteCollector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The collector finds what a tree references without containing, and nothing else.
 *
 * <p>Built from plain Ecore objects so that the rules are tested without any language in
 * play: an {@code EReference.eType} pointing at a free-floating {@code EClass} is exactly the
 * shape a {@code VariableExp.referredVariable} pointing at a parser-created {@code Variable}
 * has, and it needs no parser to set up.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class SatelliteCollectorTest {

	private static final EcoreFactory F = EcoreFactory.eINSTANCE;

	// ==== What is a satellite ====

	@Test
	void freeFloatingReferenceTarget_isFound() {
		EPackage root = F.createEPackage();
		EClass owner = F.createEClass();
		root.getEClassifiers().add(owner);
		EClass floating = F.createEClass();
		floating.setName("floating");
		owner.getESuperTypes().add(floating);

		assertEquals(List.of(floating), SatelliteCollector.find(root));
	}

	@Test
	void eachObject_reportedOnce() {
		EPackage root = F.createEPackage();
		EClass a = F.createEClass();
		EClass b = F.createEClass();
		root.getEClassifiers().add(a);
		root.getEClassifiers().add(b);
		EClass floating = F.createEClass();
		a.getESuperTypes().add(floating);
		b.getESuperTypes().add(floating);

		assertEquals(1, SatelliteCollector.find(root).size());
	}

	@Test
	void orderIsFirstEncounter() {
		EPackage root = F.createEPackage();
		EClass owner = F.createEClass();
		root.getEClassifiers().add(owner);
		EClass first = F.createEClass();
		EClass second = F.createEClass();
		owner.getESuperTypes().add(first);
		owner.getESuperTypes().add(second);

		assertEquals(List.of(first, second), SatelliteCollector.find(root));
	}

	// ==== What is not a satellite ====

	// A reference into a loaded metamodel is a cross-document reference, and must stay one:
	// a unit lends its types, it does not own them.
	@Test
	void targetInAResource_isNotFound() {
		EPackage root = F.createEPackage();
		EClass owner = F.createEClass();
		root.getEClassifiers().add(owner);
		EReference ref = F.createEReference();
		ref.setEType(EcorePackage.Literals.ESTRING);
		owner.getEStructuralFeatures().add(ref);

		assertEquals(List.of(), SatelliteCollector.find(root));
	}

	@Test
	void targetInTheSameTree_isNotFound() {
		EPackage root = F.createEPackage();
		EClass a = F.createEClass();
		EClass b = F.createEClass();
		root.getEClassifiers().add(a);
		root.getEClassifiers().add(b);
		a.getESuperTypes().add(b);

		assertEquals(List.of(), SatelliteCollector.find(root));
	}

	@Test
	void containmentTargets_areNotSatellites() {
		EPackage root = F.createEPackage();
		EClass owner = F.createEClass();
		root.getEClassifiers().add(owner);
		EAnnotation annotation = F.createEAnnotation();
		owner.getEAnnotations().add(annotation);

		assertEquals(List.of(), SatelliteCollector.find(root));
	}

	// Every EPackage carries an EFactory, created lazily by EMF and linked back to the
	// package through a one-to-one pair. It is infrastructure, not a parser leftover, and it
	// stays with its package.
	@Test
	void theFactoryOfAPackage_isNotASatellite() {
		EPackage root = F.createEPackage();
		root.getEFactoryInstance();

		assertEquals(List.of(), SatelliteCollector.find(root));
	}

	// find() is asked about a subtree but answers for the whole document: a satellite is a
	// property of the document, and the tree it is reached from is an accident.
	@Test
	void find_looksAtTheWholeDocument() {
		EPackage root = F.createEPackage();
		EClass a = F.createEClass();
		EClass b = F.createEClass();
		root.getEClassifiers().add(a);
		root.getEClassifiers().add(b);
		EClass floating = F.createEClass();
		b.getESuperTypes().add(floating);

		assertEquals(List.of(floating), SatelliteCollector.find(a));
	}

	// ==== contain ====

	@Test
	void contain_placesEverythingAndLeavesNothing() {
		EPackage root = F.createEPackage();
		EClass owner = F.createEClass();
		root.getEClassifiers().add(owner);
		EClass floating = F.createEClass();
		owner.getESuperTypes().add(floating);
		EAnnotation home = F.createEAnnotation();
		root.getEAnnotations().add(home);

		int placed = SatelliteCollector.contain(root, home.getContents());

		assertEquals(1, placed);
		assertEquals(List.of(floating), home.getContents());
		assertEquals(List.of(), SatelliteCollector.find(root));
		assertSame(root, org.eclipse.emf.ecore.util.EcoreUtil.getRootContainer(floating));
	}

	// A satellite may reference a further uncontained object, reachable only once the first
	// one has a root. contain() iterates until nothing is left.
	@Test
	void contain_reachesSatellitesOfSatellites() {
		EPackage root = F.createEPackage();
		EClass owner = F.createEClass();
		root.getEClassifiers().add(owner);
		EClass first = F.createEClass();
		EClass behindFirst = F.createEClass();
		first.getESuperTypes().add(behindFirst);
		owner.getESuperTypes().add(first);
		EAnnotation home = F.createEAnnotation();
		root.getEAnnotations().add(home);

		assertEquals(List.of(first), SatelliteCollector.find(root),
				"before placing, the second is not reachable");
		int placed = SatelliteCollector.contain(root, home.getContents());
		assertEquals(2, placed);
		assertEquals(List.of(first, behindFirst), home.getContents());
		assertEquals(List.of(), SatelliteCollector.find(root));
	}

	@Test
	@DisplayName("a chain longer than MAX_ROUNDS is refused, not walked forever")
	void contain_givesUpOnAnEndlessChain() {
		// Each round places what the previous one exposed, so a chain of N satellites needs N
		// rounds. Past the ceiling the collector says so instead of running on — the ceiling
		// had no test, and a document that never becomes self-contained is what it is for.
		EPackage root = F.createEPackage();
		EClass owner = F.createEClass();
		root.getEClassifiers().add(owner);
		// One longer than the ceiling of 32 rounds, which is package-private
		EClass previous = owner;
		for (int i = 0; i <= 32; i++) {
			EClass next = F.createEClass();
			next.setName("Link" + i);
			previous.getESuperTypes().add(next);
			previous = next;
		}
		EAnnotation home = F.createEAnnotation();
		root.getEAnnotations().add(home);

		IllegalStateException failure = assertThrows(IllegalStateException.class,
				() -> SatelliteCollector.contain(root, home.getContents()));

		assertTrue(failure.getMessage().contains("did not become self-contained"),
				failure::getMessage);
		assertTrue(failure.getMessage().contains("rounds"), failure::getMessage);
	}

	@Test
	void contain_onASelfContainedTree_placesNothing() {
		EPackage root = F.createEPackage();
		root.getEClassifiers().add(F.createEClass());
		EAnnotation home = F.createEAnnotation();
		root.getEAnnotations().add(home);

		assertEquals(0, SatelliteCollector.contain(root, home.getContents()));
		assertTrue(home.getContents().isEmpty());
	}

	// Placing into a list that is not part of the document only moves the dangling reference
	// one step; the collector refuses instead of pretending.
	@Test
	void contain_rejectsAContainerOutsideTheDocument() {
		EPackage root = F.createEPackage();
		EClass owner = F.createEClass();
		root.getEClassifiers().add(owner);
		owner.getESuperTypes().add(F.createEClass());
		List<EObject> elsewhere = new ArrayList<>();

		assertThrows(IllegalArgumentException.class,
				() -> SatelliteCollector.contain(root, elsewhere));
	}

	// ==== The point of it all ====

	@Test
	void afterContain_theDocumentSaves() throws Exception {
		EPackage root = F.createEPackage();
		root.setName("p");
		root.setNsURI("http://p");
		EClass owner = F.createEClass();
		owner.setName("Owner");
		root.getEClassifiers().add(owner);
		EClass floating = F.createEClass();
		floating.setName("Floating");
		owner.getESuperTypes().add(floating);
		EAnnotation home = F.createEAnnotation();
		home.setSource("satellites");
		root.getEAnnotations().add(home);
		SatelliteCollector.contain(root, home.getContents());

		ResourceSetImpl resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("*", new XMIResourceFactoryImpl());
		Resource resource = resourceSet.createResource(URI.createURI("unit.xmi"));
		resource.getContents().add(root);
		resource.save(new java.io.ByteArrayOutputStream(), null);
	}
}
