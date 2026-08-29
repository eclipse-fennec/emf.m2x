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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.emf.osgi.fingerprint.FingerprintService;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.compiled.PackageEntry;
import org.eclipse.fennec.m2x.model.m2t.M2tFactory;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * The unit bundle under OSGi (#178).
 *
 * <p>It had no OSGi test at all, while it is the bundle every engine reaches through to
 * package a compiled unit — and packaging asks a {@code FingerprintService} for the identity
 * of each metamodel a unit carries. On a flat class path that service comes from
 * {@code FingerprintHelper}; in a framework it is a service, and which of the two answers had
 * never been checked from inside one.
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
class UnitFingerprintServiceOSGiTest {

	private static final String NS_URI = "http://example.org/m2x/unit-osgi/1.0";

	@Test
	@DisplayName("the fingerprint service is there as a service")
	void fingerprintServiceIsRegistered(
			@InjectService(timeout = 5000) FingerprintService fingerprints) {
		assertNotNull(fingerprints, "packaging a unit needs it, so a framework has to offer it");
	}

	@Test
	@DisplayName("a packaged unit records the fingerprint of the metamodel it carries")
	void packagingRecordsThePackageFingerprint(
			@InjectService(timeout = 5000) FingerprintService fingerprints) {
		// The same value from the service and from the packager: what a unit records is what the
		// runtime will later compare against, and prepare refuses the unit if the two differ
		EPackage shelf = shelfPackage();
		UnitPackager packager = new UnitPackager(fingerprints);
		CompiledUnit compiled = packager.seal(packager.begin("m2t", "gen.Books",
				moduleFor(shelf), DependencyMode.PIN, null));

		PackageEntry entry = compiled.getManifest().getPackageEntry().stream()
				.filter(e -> NS_URI.equals(e.getNsURI()))
				.findFirst()
				.orElseThrow(() -> new AssertionError("no entry for the carried metamodel: "
						+ compiled.getManifest().getPackageEntry()));

		assertEquals(fingerprints.fingerprint(shelf), entry.getFingerprint(),
				"the recorded identity is the service's answer, not something computed twice");
		assertTrue(entry.getFingerprint().startsWith("fp1:"), entry.getFingerprint());
	}

	// --- helpers ---

	private static EPackage shelfPackage() {
		EPackage shelf = EcoreFactory.eINSTANCE.createEPackage();
		shelf.setName("shelf");
		shelf.setNsURI(NS_URI);
		shelf.setNsPrefix("shelf");
		EClass book = EcoreFactory.eINSTANCE.createEClass();
		book.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		book.getEStructuralFeatures().add(title);
		shelf.getEClassifiers().add(book);
		return shelf;
	}

	private static Module moduleFor(EPackage shelf) {
		Module module = M2tFactory.eINSTANCE.createModule();
		module.setName("gen.Books");
		module.setNsURI("http://example.org/m2x/unit-osgi/module");
		Template template = M2tFactory.eINSTANCE.createTemplate();
		template.setName("book");
		Variable parameter = OclFactory.eINSTANCE.createVariable();
		parameter.setName("b");
		parameter.setType(shelf.getEClassifier("Book"));
		template.getParameter().add(parameter);
		module.getOwnedModuleElement().add(template);
		return module;
	}
}
