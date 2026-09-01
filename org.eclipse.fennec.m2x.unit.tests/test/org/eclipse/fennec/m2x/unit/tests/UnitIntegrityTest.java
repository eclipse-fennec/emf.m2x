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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.fennec.emf.osgi.fingerprint.util.FingerprintHelper;
import org.eclipse.fennec.m2x.model.compiled.CompiledFactory;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.m2t.M2tFactory;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitBinder;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitMaterializeException;
import org.eclipse.fennec.m2x.unit.api.UnitResourceSet;
import org.eclipse.fennec.m2x.unit.api.UnitPrepareException;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.api.UnitStoreException;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.eclipse.fennec.m2x.unit.materialize.UnitMaterializer;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.eclipse.fennec.m2x.unit.validate.UnitValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * What a compiled unit <em>carries</em> is content of that unit: the copies of dynamic metamodels
 * it brings for a runtime that has none, and the units embedded under {@code embed} (#183).
 *
 * <p>The fingerprint used to cover the script and the names of the dependencies only, so a
 * tampered package copy or a swapped embedded library passed the validator and prepare while the
 * security analyses claimed integrity was checked. These tests fail against that.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class UnitIntegrityTest {

	private static final String NS_URI = "http://example.org/m2x/integrity/1.0";

	private EPackage shelf;
	private EClass bookClass;
	private EPackage.Registry registry;

	@BeforeEach
	void setUp() {
		shelf = EcoreFactory.eINSTANCE.createEPackage();
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

	// ==== the package copy a unit carries ====

	@Test
	void aChangedPackageCopy_failsTheIntegrityCheck() {
		CompiledUnit compiled = compiled("gen.Books");
		assertEquals(1, compiled.getPackages().size(), "the dynamic metamodel travels with the unit");

		// A copy is what the unit's types resolve to where the runtime has nothing — change it and
		// the unit means something else
		EClass copiedBook = (EClass) compiled.getPackages().get(0).getEClassifier("Book");
		copiedBook.getEStructuralFeatures().get(0).setName("renamedAfterSealing");

		List<String> findings = UnitValidator.defaults().validate(compiled);
		assertTrue(findings.stream().anyMatch(f -> f.contains("changed after it was sealed")), findings.toString());
	}

	@Test
	void theUnitFingerprintDeliberatelyDoesNotCoverTheCopy() {
		// The copy has its own recorded identity — the PackageEntry's fp1 value — and that is
		// what it is held to. Folding it into the unit fingerprint as well would make a compiled
		// document differ from the AST it was built from, a property the round-trip tests rely on
		// (QvtoUnitFingerprintTest.parsedAndCompiled_andAnotherEngine_agree).
		CompiledUnit compiled = compiled("gen.Books");
		String before = compiled.getManifest().getUnitFingerprint();
		((EClass) compiled.getPackages().get(0).getEClassifier("Book")).setAbstract(true);

		assertEquals(before, DefaultUnitFingerprintService.INSTANCE.fingerprint(compiled),
				"the unit says the same thing; what changed is the metamodel it carries");
		assertTrue(UnitValidator.defaults().validate(compiled).stream()
				.anyMatch(f -> f.contains("carried metamodel changed")),
				"and that is what the entry check is for");
	}

	// ==== the units a unit embeds ====

	@Test
	void aChangedEmbeddedUnit_isRejected() {
		CompiledUnit host = withEmbedded();
		Module embedded = (Module) host.getEmbedded().get(0).getUnit();
		embedded.setName("renamedAfterSealing");

		List<String> findings = UnitValidator.defaults().validate(host);
		assertTrue(findings.stream().anyMatch(f -> f.contains("embedded unit")), findings.toString());
		assertTrue(findings.stream().anyMatch(f -> f.contains("changed after it was sealed")), findings.toString());
	}

	@Test
	void anIntactEmbeddedUnit_passes() {
		assertEquals(List.of(), UnitValidator.defaults().validate(withEmbedded()));
	}

	// ==== prepare holds a carried copy to its entry ====

	@Test
	void aTamperedCopy_isRefusedWhenTheUnitIsMaterialized() throws Exception {
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		CompiledUnit compiled = compiled("gen.Books");
		((EClass) compiled.getPackages().get(0).getEClassifier("Book")).setAbstract(true);
		compiled.getManifest().setUnitFingerprint(DefaultUnitFingerprintService.INSTANCE.fingerprint(compiled));

		UnitKey key = store.put(compiled);
		PackagedUnit loaded = (PackagedUnit) store.get(key).orElseThrow();
		UnitMaterializeException failure = assertThrows(UnitMaterializeException.class,
				() -> UnitMaterializer.defaults().materialize(loaded, new UnitResourceSet(registry)),
				"the funnel sees it before any consumer does");
		assertTrue(failure.getMessage().contains(NS_URI), failure.getMessage());
	}

	@Test
	void prepare_refusesACarriedCopyThatDoesNotMatchItsEntry() throws Exception {
		// Past the funnel — a consumer that skips validation, which is what
		// UnitMaterializer.withoutValidation() is for — prepare still holds the copy to its entry
		InMemoryUnitStoreBackend backend = new InMemoryUnitStoreBackend();
		UnitStore writing = new DefaultUnitStore(backend);
		CompiledUnit compiled = compiled("gen.Books");
		((EClass) compiled.getPackages().get(0).getEClassifier("Book")).setAbstract(true);
		compiled.getManifest().setUnitFingerprint(DefaultUnitFingerprintService.INSTANCE.fingerprint(compiled));
		UnitKey key = writing.put(compiled);

		// A runtime that has no such metamodel: the carried copy is what would serve it
		UnitPreparer preparer = new UnitPreparer(new DefaultUnitStore(backend),
				FingerprintHelper.getDefaultFingerprintService(), List.of(noopBinder()),
				UnitMaterializer.withoutValidation());
		UnitPrepareException failure = assertThrows(UnitPrepareException.class, () -> preparer.prepare(key));
		assertTrue(failure.getMessage().contains(NS_URI), failure.getMessage());
		assertTrue(failure.getMessage().contains("the copy the unit carries"), failure.getMessage());
	}

	@Test
	void prepare_acceptsACarriedCopyThatMatches() throws Exception {
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		UnitKey key = store.put(compiled("gen.Books"));

		UnitPreparer preparer = new UnitPreparer(store, List.of(noopBinder()));
		assertEquals(1, preparer.prepare(key).units().size());
	}

	// ==== the store's parser ====

    @Test
	void aDoctypeInTheStoredBytes_isRefused() throws Exception {
		InMemoryUnitStoreBackend backend = new InMemoryUnitStoreBackend();
		UnitStore store = new DefaultUnitStore(backend);
		UnitKey key = store.put(compiled("gen.Books"));

		// What a tampered backend would hand back. Without disallow-doctype-decl the parser
		// resolves this before the validator ever sees the result.
		String xmi = new String(backend.get(key).orElseThrow(), StandardCharsets.UTF_8);
		String withDoctype = xmi.replaceFirst("<\\?xml[^?]*\\?>",
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE compiled:CompiledUnit ["
						+ "<!ENTITY xxe SYSTEM \"file:///etc/passwd\">]>");
		backend.put(key, withDoctype.getBytes(StandardCharsets.UTF_8));

		UnitStoreException failure = assertThrows(UnitStoreException.class, () -> store.get(key));
		assertTrue(failure.getMessage().contains("cannot read") || failure.getMessage().contains("rejected"),
				failure.getMessage());
	}

	@Test
	void garbageBytes_areAnError_notAnEmptyOptional() throws Exception {
		InMemoryUnitStoreBackend backend = new InMemoryUnitStoreBackend();
		UnitStore store = new DefaultUnitStore(backend);
		UnitKey key = store.put(compiled("gen.Books"));
		backend.put(key, "this is not XMI".getBytes(StandardCharsets.UTF_8));

		assertThrows(UnitStoreException.class, () -> store.get(key),
				"a store that cannot read what it holds says so; it does not report 'not found'");
	}

	// ==== what the store refuses (#174) ====

	@Test
	void anUnsealedDocument_isRefused_withTheAdviceToCompileFirst() {
		// A bare AST no longer fits through the API at all — put() takes the document form.
		// The document that LOOKS compiled but carries no manifest is the case that remains.
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		UnitStoreException failure = assertThrows(UnitStoreException.class,
				() -> store.put(CompiledFactory.eINSTANCE.createCompiledUnit()));
		assertTrue(failure.getMessage().contains("compile()"), failure.getMessage());
	}

	@Test
	void emptyContent_isAnError_notAnEmptyDocument() throws Exception {
		InMemoryUnitStoreBackend backend = new InMemoryUnitStoreBackend();
		UnitStore store = new DefaultUnitStore(backend);
		UnitKey key = store.put(compiled("gen.Books"));
		// Well-formed XMI holding nothing — a truncated write looks like this
		backend.put(key, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<empty/>"
				.getBytes(StandardCharsets.UTF_8));

		UnitStoreException failure = assertThrows(UnitStoreException.class, () -> store.get(key));

		assertTrue(failure.getMessage().contains("cannot read")
				|| failure.getMessage().contains("is empty")
				|| failure.getMessage().contains("did not come back"), failure.getMessage());
	}


	// ==== helpers ====

	private CompiledUnit compiled(String name) {
		Module module = M2tFactory.eINSTANCE.createModule();
		module.setName(name);
		module.setNsURI("http://example.org/m2x/integrity/module/" + name);
		Template template = M2tFactory.eINSTANCE.createTemplate();
		template.setName("book");
		Variable parameter = OclFactory.eINSTANCE.createVariable();
		parameter.setName("b");
		parameter.setType(bookClass);
		template.getParameter().add(parameter);
		module.getOwnedModuleElement().add(template);
		return UnitPackager.compile("m2t", name, module);
	}

	/** A host unit with one embedded unit, both sealed. */
	private CompiledUnit withEmbedded() {
		CompiledUnit embedded = compiled("gen.Lib");
		UnitPackager packager = UnitPackager.withDefaults();
		Module host = M2tFactory.eINSTANCE.createModule();
		host.setName("gen.Host");
		host.setNsURI("http://example.org/m2x/integrity/module/host");
		CompiledUnit document = packager.begin("m2t", "gen.Host", host, DependencyMode.EMBED, null);
		document.getEmbedded().add(embedded);
		var entry = CompiledFactory.eINSTANCE.createDependencyEntry();
		entry.setQualifiedName("gen.Lib");
		entry.setMode(DependencyMode.EMBED);
		entry.setFingerprint(embedded.getManifest().getUnitFingerprint());
		document.getManifest().getDependencyEntry().add(entry);
		return packager.seal(document);
	}

	private static UnitBinder noopBinder() {
		return new UnitBinder() {
			@Override
			public String language() {
				return "m2t";
			}

			@Override
			public void bind(CompiledUnit unit, Map<String, CompiledUnit> dependencies) {
			}

			@Override
			public void verifyBlackboxes(CompiledUnit unit) {
			}
		};
	}
}
