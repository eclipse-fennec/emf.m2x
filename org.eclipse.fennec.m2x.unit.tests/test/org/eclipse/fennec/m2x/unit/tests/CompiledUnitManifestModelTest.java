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

import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The compiled-unit manifest metamodel loads, and its nsURI is the neutral one.
 *
 * <p>The nsURI test is not ceremony. Moving this Ecore to another bundle later is
 * cheap; changing its nsURI is not — every unit already stored under the old one
 * becomes unreadable. Hence the neutral {@code …/m2x/compiled/1.0}, which names
 * neither a language nor a bundle, and a test that says so out loud.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class CompiledUnitManifestModelTest {

	private static final String NS_URI = "http://www.eclipse.org/fennec/m2x/compiled/1.0";

	private static EPackage manifestPackage;

	@BeforeAll
	static void loadModel() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("ecore", new XMIResourceFactoryImpl());
		Resource resource = resourceSet.createResource(URI.createURI("compiledunit.ecore"));
		try (InputStream in = CompiledUnitManifestModelTest.class
				.getResourceAsStream("/model/compiledunit.ecore")) {
			assertNotNull(in, "model/compiledunit.ecore must be included in the bundle");
			resource.load(in, null);
		}
		manifestPackage = (EPackage) resource.getContents().get(0);
	}

	@Test
	void nsUri_isNeutralAndVersioned() {
		assertEquals(NS_URI, manifestPackage.getNsURI(),
				"changing this nsURI makes every stored unit unreadable");
		assertEquals("compiled", manifestPackage.getName());
		assertEquals("compiled", manifestPackage.getNsPrefix());
	}

	@Test
	void manifest_carriesTheEntriesPrepareNeeds() {
		EClass manifest = classifier("CompiledUnitManifest");
		assertFeatures(manifest, "formatVersion", "producedBy", "language", "qualifiedName",
				"unitFingerprint", "dependencyMode", "packageEntry", "dependencyEntry",
				"blackboxRequirement", "resolvedClosure");
	}

	@Test
	void packageEntry_carriesFingerprintAndScheme() {
		assertFeatures(classifier("PackageEntry"), "nsURI", "fingerprint", "scheme", "role");
	}

	@Test
	void dependencyEntry_carriesModeAndFingerprint() {
		assertFeatures(classifier("DependencyEntry"), "qualifiedName", "mode", "fingerprint");
	}

	@Test
	void blackboxRequirement_carriesTheDeclaration() {
		assertFeatures(classifier("BlackboxRequirement"), "name", "signatureFingerprint", "provider");
	}

	@Test
	void resolvedDependency_recordsWhatPrepareBound() {
		assertFeatures(classifier("ResolvedDependency"), "qualifiedName", "fingerprint", "source");
	}

	@Test
	void dependencyMode_hasTheThreeModes() {
		EEnum mode = (EEnum) manifestPackage.getEClassifier("DependencyMode");
		assertNotNull(mode);
		assertEquals(List.of("embed", "pin", "rebind"),
				mode.getELiterals().stream().map(literal -> literal.getName()).toList());
	}

	@Test
	void packageRole_distinguishesReferencedFromEmbedded() {
		EEnum role = (EEnum) manifestPackage.getEClassifier("PackageRole");
		assertNotNull(role);
		assertEquals(List.of("referenced", "embedded"),
				role.getELiterals().stream().map(literal -> literal.getName()).toList());
	}

	@Test
	void everyEntryIsContained() {
		EClass manifest = classifier("CompiledUnitManifest");
		for (String name : List.of("packageEntry", "dependencyEntry", "blackboxRequirement",
				"resolvedClosure")) {
			EStructuralFeature feature = manifest.getEStructuralFeature(name);
			assertTrue(feature instanceof EReference reference && reference.isContainment(),
					"'" + name + "' must be a containment — a manifest is one document");
		}
	}

	// ---- Helpers ----

	private static EClass classifier(String name) {
		var classifier = manifestPackage.getEClassifier(name);
		assertNotNull(classifier, "missing classifier: " + name);
		return (EClass) classifier;
	}

	private static void assertFeatures(EClass owner, String... expected) {
		assertEquals(List.of(expected),
				owner.getEStructuralFeatures().stream().map(EStructuralFeature::getName).toList());
	}
}
