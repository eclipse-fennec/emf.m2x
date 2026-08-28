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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.fennec.m2x.model.compiled.CompiledFactory;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.DependencyEntry;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.m2t.M2tFactory;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.api.UnitStoreException;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.eclipse.fennec.m2x.unit.validate.UnitValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A loaded unit bypasses the parser; the validator checks what the parser would have guaranteed
 * by construction, and the store rejects what fails (#142).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class UnitValidatorTest {

	private static final String NS_URI = "http://example.org/m2x/validate-test/1.0";

	private EClass bookClass;
	private EPackage.Registry registry;

	@BeforeEach
	void setUp() {
		EPackage shelf = EcoreFactory.eINSTANCE.createEPackage();
		shelf.setName("shelf");
		shelf.setNsURI(NS_URI);
		shelf.setNsPrefix("shelf");
		bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(title);
		shelf.getEClassifiers().add(bookClass);
		registry = new EPackageRegistryImpl();
		registry.put(NS_URI, shelf);
	}

	@Test
	void aSealedUnit_passes() {
		assertEquals(List.of(), UnitValidator.defaults().validate(compiled("gen.Books")));
	}

	@Test
	void aChangedDocument_failsTheIntegrityCheck() {
		CompiledUnit compiled = compiled("gen.Books");
		((Module) compiled.getUnit()).setName("renamed after sealing");
		List<String> findings = UnitValidator.defaults().validate(compiled);
		assertEquals(1, findings.size(), findings.toString());
		assertTrue(findings.get(0).contains("changed after it was sealed"), findings.get(0));
	}

	@Test
	void aMissingManifestField_isNamed() {
		CompiledUnit compiled = compiled("gen.Books");
		compiled.getManifest().setFormatVersion("9.9");
		compiled.getManifest().setLanguage(null);
		List<String> findings = UnitValidator.defaults().validate(compiled);
		assertTrue(findings.stream().anyMatch(f -> f.contains("format version")), findings.toString());
		assertTrue(findings.stream().anyMatch(f -> f.contains("no language")), findings.toString());
	}

	@Test
	void aPinWithoutFingerprint_isNamed() {
		CompiledUnit compiled = compiled("gen.Books");
		DependencyEntry entry = CompiledFactory.eINSTANCE.createDependencyEntry();
		entry.setQualifiedName("gen.Lib");
		entry.setMode(DependencyMode.PIN);
		compiled.getManifest().getDependencyEntry().add(entry);
		assertTrue(UnitValidator.defaults().validate(compiled).stream()
				.anyMatch(f -> f.contains("gen.Lib") && f.contains("no fingerprint")));
	}

	@Test
	void aReferenceToAForeignObject_isNamed() {
		CompiledUnit compiled = compiled("gen.Books");
		Variable smuggled = OclFactory.eINSTANCE.createVariable(); // uncontained, in no metamodel
		smuggled.setName("smuggled");
		compiled.getUnit().getEAnnotations().add(EcoreFactory.eINSTANCE.createEAnnotation());
		compiled.getUnit().getEAnnotations().get(0).getReferences().add(smuggled);
		List<String> findings = UnitValidator.defaults().validate(compiled);
		assertTrue(findings.stream().anyMatch(f -> f.contains("leaves the document")), findings.toString());
	}

	@Test
	void anEcoreViolation_isNamed() {
		CompiledUnit compiled = compiled("gen.Books");
		compiled.setId(null); // id is [1..1]
		List<String> findings = UnitValidator.defaults().validate(compiled);
		assertTrue(findings.stream().anyMatch(f -> f.toLowerCase().contains("id")), findings.toString());
	}

	@Test
	void aDocumentPastTheBounds_isRejectedWithoutBeingWalkedFurther() {
		CompiledUnit compiled = compiled("gen.Books");
		UnitValidator tiny = new UnitValidator(2, 1_000_000, DefaultUnitFingerprintService.INSTANCE);
		List<String> findings = tiny.validate(compiled);
		assertEquals(1, findings.size(), findings.toString());
		assertTrue(findings.get(0).contains("nested deeper than 2"), findings.get(0));
		UnitValidator few = new UnitValidator(1_000, 3, DefaultUnitFingerprintService.INSTANCE);
		assertTrue(few.validate(compiled).get(0).contains("more than 3 objects"));
	}

	@Test
	void theStore_rejectsAUnitThatFails_andCanBeToldNotToCheck() throws Exception {
		InMemoryUnitStoreBackend backend = new InMemoryUnitStoreBackend();
		UnitStore checking = new DefaultUnitStore(backend, registry);
		CompiledUnit compiled = compiled("gen.Books");
		UnitKey key = checking.store("m2t", new PackagedUnit(compiled));
		assertTrue(checking.load(key).isPresent(), "a sealed unit loads");

		// Corrupt the stored bytes the way a backend might: rename the module inside the XMI
		byte[] bytes = backend.get(key).orElseThrow();
		String xmi = new String(bytes, StandardCharsets.UTF_8).replace("gen.Books", "gen.Other");
		backend.put(key, xmi.getBytes(StandardCharsets.UTF_8));

		UnitStoreException failure = assertThrows(UnitStoreException.class, () -> checking.load(key));
		assertTrue(failure.getMessage().contains("rejected"), failure.getMessage());
		assertTrue(failure.getMessage().contains("changed after it was sealed"), failure.getMessage());

		UnitStore trusting = DefaultUnitStore.withoutValidation(backend, registry);
		assertFalse(trusting.load(key).isEmpty(), "without validation the same bytes load");
	}

	// ==== helpers ====

	private CompiledUnit compiled(String name) {
		Module module = M2tFactory.eINSTANCE.createModule();
		module.setName(name);
		module.setNsURI("http://example.org/m2x/validate-test/module/" + name);
		Template template = M2tFactory.eINSTANCE.createTemplate();
		template.setName("book");
		Variable parameter = OclFactory.eINSTANCE.createVariable();
		parameter.setName("b");
		parameter.setType(bookClass);
		template.getParameter().add(parameter);
		module.getOwnedModuleElement().add(template);
		return UnitPackager.compile("m2t", name, module);
	}
}
