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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.compiled.CompiledFactory;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.m2t.M2tFactory;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;
import org.eclipse.fennec.m2x.unit.api.UnitResourceSet;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.api.UnitStoreException;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.eclipse.fennec.m2x.unit.materialize.UnitMaterializer;
import org.eclipse.fennec.m2x.unit.satellite.SatelliteCollector;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.eclipse.fennec.m2x.unit.store.StoredSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The store holds sources and compiled units, tells them apart by kind, hands out independent
 * copies with their references unresolved — proxies are the transport state, materializing binds
 * them in a consumer's context — and says what it has when a version is missing (#139, #211,
 * concept §5.5).
 *
 * <p>Language-neutral: the units here are built from the factories, not parsed. A template with a
 * variable typed by a dynamic metamodel is enough to exercise satellites, package copies and the
 * metamodel resolution on load.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class DefaultUnitStoreTest {

	private static final String NS_URI = "http://example.org/m2x/store-test/1.0";

	private EPackage metamodel;
	private EClass bookClass;
	private EPackage.Registry registry;
	private InMemoryUnitStoreBackend backend;
	private UnitStore store;

	@BeforeEach
	void setUp() {
		metamodel = EcoreFactory.eINSTANCE.createEPackage();
		metamodel.setName("shelf");
		metamodel.setNsURI(NS_URI);
		metamodel.setNsPrefix("shelf");
		bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(title);
		metamodel.getEClassifiers().add(bookClass);
		registry = new EPackageRegistryImpl();
		registry.put(NS_URI, metamodel);
		backend = new InMemoryUnitStoreBackend();
		store = new DefaultUnitStore(backend);
	}

	// ==== sources ====

	@Test
	void source_isStoredByItsFingerprint_andComesBackAsText() throws Exception {
		Unit.Source source = new QvtoUnit.SourceUnit("lib.Strings", URI.createURI("file:/lib/Strings.qvto"),
				"library Strings { }\n");
		UnitKey key = store.put("qvto", source);

		assertEquals(UnitKind.SOURCE, key.kind());
		assertEquals("lib.Strings", key.qualifiedName());
		assertEquals(DefaultUnitFingerprintService.INSTANCE.fingerprint(source), key.fingerprint().orElseThrow());
		Unit loaded = store.get(key).orElseThrow();
		StoredSource text = assertInstanceOf(StoredSource.class, loaded);
		assertEquals("library Strings { }\n", text.source());
		assertEquals(URI.createURI("file:/lib/Strings.qvto"), text.uri());
		assertEquals(UnitKind.SOURCE, text.kind());
	}

	@Test
	void sourceVersions_areListedNewestFirst_andTheUnpinnedKeyLoadsTheNewest() throws Exception {
		UnitKey v1 = store.put("qvto", source("lib.Strings", "library Strings { helper a() : String { return 'a'; } }"));
		UnitKey v2 = store.put("qvto", source("lib.Strings", "library Strings { helper a() : String { return 'b'; } }"));

		assertNotEquals(v1, v2);
		assertEquals(List.of(v2, v1), store.versions("qvto", "lib.Strings", UnitKind.SOURCE));
		StoredSource newest = (StoredSource) store.get(UnitKey.of("qvto", "lib.Strings", UnitKind.SOURCE)).orElseThrow();
		assertTrue(newest.source().contains("'b'"), "the unpinned key is whatever the store holds newest");
		assertTrue(((StoredSource) store.get(v1).orElseThrow()).source().contains("'a'"), "the pinned key is that version");
	}

	// ==== compiled units ====

	@Test
	void compiledUnit_isStoredByItsManifestFingerprint_andComesBackAsAnIndependentCopy() throws Exception {
		CompiledUnit compiled = compiledTemplate("gen.Books");
		UnitKey key = store.put(compiled);

		assertEquals(UnitKind.COMPILED, key.kind());
		assertEquals(compiled.getManifest().getUnitFingerprint(), key.fingerprint().orElseThrow());
		assertNull(compiled.eResource(), "storing did not move the caller's document");

		PackagedUnit loaded = (PackagedUnit) store.get(key).orElseThrow();
		assertNotSame(compiled, loaded.document());
		assertEquals("gen.Books", loaded.qualifiedName());
		assertEquals("m2t", loaded.language());
		assertEquals(compiled.getId(), loaded.document().getId());
		UnitMaterializer.defaults().materialize(loaded, new UnitResourceSet(registry));
		assertEquals(List.of(), SatelliteCollector.find(loaded.document()), "the copy is self-contained");
		assertEquals(0, referencesInto(loaded.document(), compiled), "and points at nothing of the original");
	}

	@Test
	void storedUnit_comesBackUnresolved_untilMaterialized() throws Exception {
		UnitKey key = store.put(compiledTemplate("gen.Books"));
		PackagedUnit loaded = (PackagedUnit) store.get(key).orElseThrow();
		Module module = (Module) loaded.document().getUnit();
		Template template = (Template) module.getOwnedModuleElement().get(0);
		Object raw = template.getParameter().get(0).eGet(
				template.getParameter().get(0).eClass().getEStructuralFeature("type"), false);
		assertTrue(((org.eclipse.emf.ecore.EObject) raw).eIsProxy(),
				"a document leaves the store with its references unresolved — the transport state");
	}

	@Test
	void materializedUnit_resolvesItsMetamodelInTheConsumersContext() throws Exception {
		UnitKey key = store.put(compiledTemplate("gen.Books"));
		PackagedUnit loaded = (PackagedUnit) store.get(key).orElseThrow();
		UnitMaterializer.defaults().materialize(loaded, new UnitResourceSet(registry));
		assertSame(bookClass, variableType(loaded.document()),
				"the context knows the nsURI, so the type is the context's very instance");
	}

	@Test
	void materializedUnit_isServedFromItsPackageCopy_whereTheContextHasNothing() throws Exception {
		UnitKey key = store.put(compiledTemplate("gen.Books"));
		// The same bytes on another machine, materialized in a context that never heard of the metamodel
		InMemoryUnitStoreBackend other = new InMemoryUnitStoreBackend();
		other.put(key, backend.get(key).orElseThrow());
		UnitStore elsewhere = new DefaultUnitStore(other);

		PackagedUnit loaded = (PackagedUnit) elsewhere.get(key).orElseThrow();
		UnitMaterializer.defaults().materialize(loaded, new UnitResourceSet(new EPackageRegistryImpl()));
		EClass type = variableType(loaded.document());
		assertEquals("Book", type.getName());
		assertNotSame(bookClass, type);
		assertTrue(EcoreUtil.isAncestor(loaded.document(), type), "the type is the copy the unit carries");
	}

	@Test
	void documentWithoutManifest_isRefused_compileFirst() {
		CompiledUnit unsealed = CompiledFactory.eINSTANCE.createCompiledUnit();
		UnitStoreException failure = assertThrows(UnitStoreException.class, () -> store.put(unsealed));
		assertTrue(failure.getMessage().contains("compile()"), failure.getMessage());
	}

	@Test
	void documentWithoutLanguage_isRefused() throws Exception {
		CompiledUnit compiled = compiledTemplate("gen.Books");
		compiled.getManifest().setLanguage(null);
		UnitStoreException failure = assertThrows(UnitStoreException.class, () -> store.put(compiled));
		assertTrue(failure.getMessage().contains("language"), failure.getMessage());
	}

	// ==== kinds, versions, keys ====

	@Test
	void sourceAndCompiledUnit_ofOneName_coexistAndAreToldApartByKind() throws Exception {
		UnitKey sourceKey = store.put("m2t", source("gen.Books", "[module Books(Ecore)/]"));
		UnitKey compiledKey = store.put(compiledTemplate("gen.Books"));

		assertEquals(List.of(sourceKey), store.versions("m2t", "gen.Books", UnitKind.SOURCE));
		assertEquals(List.of(compiledKey), store.versions("m2t", "gen.Books", UnitKind.COMPILED));
		assertInstanceOf(StoredSource.class, store.get(UnitKey.of("m2t", "gen.Books", UnitKind.SOURCE)).orElseThrow());
		assertInstanceOf(PackagedUnit.class, store.get(UnitKey.of("m2t", "gen.Books", UnitKind.COMPILED)).orElseThrow());
		assertTrue(store.contains(sourceKey));
		assertTrue(store.contains(UnitKey.of("m2t", "gen.Books", UnitKind.COMPILED)));
	}

	@Test
	void unknownName_isEmpty_butAMissingVersionOfAKnownNameIsAnError() throws Exception {
		assertEquals(Optional.empty(), store.get(UnitKey.of("qvto", "nobody.Home", UnitKind.SOURCE)));
		assertFalse(store.contains(UnitKey.of("qvto", "nobody.Home", UnitKind.SOURCE)));

		UnitKey present = store.put("qvto", source("lib.Strings", "library Strings { }"));
		UnitKey wrongVersion = UnitKey.pinned("qvto", "lib.Strings", UnitKind.SOURCE, "m2x1:0000");
		UnitStoreException failure = assertThrows(UnitStoreException.class, () -> store.get(wrongVersion));
		assertTrue(failure.getMessage().contains(present.fingerprint().orElseThrow()),
				"the message names the versions that are there: " + failure.getMessage());
	}

	@Test
	void remove_takesOneVersionOrAll() throws Exception {
		UnitKey v1 = store.put("qvto", source("lib.Strings", "library Strings { helper a() : String { return 'a'; } }"));
		UnitKey v2 = store.put("qvto", source("lib.Strings", "library Strings { helper a() : String { return 'b'; } }"));

		assertTrue(store.remove(v1));
		assertEquals(List.of(v2), store.versions("qvto", "lib.Strings", UnitKind.SOURCE));
		assertTrue(store.remove(UnitKey.of("qvto", "lib.Strings", UnitKind.SOURCE)));
		assertEquals(List.of(), store.versions("qvto", "lib.Strings", UnitKind.SOURCE));
		assertFalse(store.remove(v2));
	}

	@Test
	void storingTheSameContentTwice_isOneVersion() throws Exception {
		UnitKey first = store.put("qvto", source("lib.Strings", "library Strings { }"));
		UnitKey second = store.put("qvto", source("lib.Strings", "library Strings { }"));
		assertEquals(first, second);
		assertEquals(List.of(first), store.versions("qvto", "lib.Strings", UnitKind.SOURCE));
	}

	// ==== helpers ====

	private static Unit.Source source(String name, String text) {
		return new QvtoUnit.SourceUnit(name, URI.createURI("mem:/" + name), text);
	}

	/** A module with one template whose parameter is typed by the dynamic metamodel. */
	private CompiledUnit compiledTemplate(String name) {
		Module module = M2tFactory.eINSTANCE.createModule();
		module.setName(name);
		module.setNsURI("http://example.org/m2x/store-test/module/" + name);
		Template template = M2tFactory.eINSTANCE.createTemplate();
		template.setName("book");
		Variable parameter = OclFactory.eINSTANCE.createVariable();
		parameter.setName("b");
		parameter.setType(bookClass);
		template.getParameter().add(parameter);
		module.getOwnedModuleElement().add(template);
		return UnitPackager.compile("m2t", name, module);
	}

	private static EClass variableType(CompiledUnit document) {
		Module module = (Module) document.getUnit();
		Template template = (Template) module.getOwnedModuleElement().get(0);
		return (EClass) template.getParameter().get(0).getType();
	}

	private static int referencesInto(CompiledUnit copy, CompiledUnit original) {
		int count = 0;
		for (var entry : EcoreUtil.ExternalCrossReferencer.find(copy).entrySet()) {
			if (EcoreUtil.isAncestor(original, entry.getKey())) {
				count += entry.getValue().size();
			}
		}
		return count;
	}
}
