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
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.m2t.M2tFactory;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalFactory;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.api.UnitStoreException;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.eclipse.fennec.m2x.unit.satellite.SatelliteCollector;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.eclipse.fennec.m2x.unit.store.StoredSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The store holds sources and compiled units, tells them apart by kind, hands out independent
 * copies and says what it has when a version is missing (#139, concept §5.5).
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
		store = new DefaultUnitStore(backend, registry);
	}

	// ==== sources ====

	@Test
	void source_isStoredByItsFingerprint_andComesBackAsText() throws Exception {
		Unit.Source source = new QvtoUnit.SourceUnit("lib.Strings", URI.createURI("file:/lib/Strings.qvto"),
				"library Strings { }\n");
		UnitKey key = store.store("qvto", source);

		assertEquals(UnitKind.SOURCE, key.kind());
		assertEquals("lib.Strings", key.qualifiedName());
		assertEquals(DefaultUnitFingerprintService.INSTANCE.fingerprint(source), key.fingerprint().orElseThrow());
		Unit loaded = store.load(key).orElseThrow();
		StoredSource text = assertInstanceOf(StoredSource.class, loaded);
		assertEquals("library Strings { }\n", text.source());
		assertEquals(URI.createURI("file:/lib/Strings.qvto"), text.uri());
		assertEquals(UnitKind.SOURCE, text.kind());
	}

	@Test
	void sourceVersions_areListedNewestFirst_andTheUnpinnedKeyLoadsTheNewest() throws Exception {
		UnitKey v1 = store.store("qvto", source("lib.Strings", "library Strings { helper a() : String { return 'a'; } }"));
		UnitKey v2 = store.store("qvto", source("lib.Strings", "library Strings { helper a() : String { return 'b'; } }"));

		assertNotEquals(v1, v2);
		assertEquals(List.of(v2, v1), store.versions("qvto", "lib.Strings", UnitKind.SOURCE));
		StoredSource newest = (StoredSource) store.load(UnitKey.of("qvto", "lib.Strings", UnitKind.SOURCE)).orElseThrow();
		assertTrue(newest.source().contains("'b'"), "the unpinned key is whatever the store holds newest");
		assertTrue(((StoredSource) store.load(v1).orElseThrow()).source().contains("'a'"), "the pinned key is that version");
	}

	// ==== compiled units ====

	@Test
	void compiledUnit_isStoredByItsManifestFingerprint_andComesBackAsAnIndependentCopy() throws Exception {
		CompiledUnit compiled = compiledTemplate("gen.Books");
		UnitKey key = store.store("m2t", new PackagedUnit(compiled));

		assertEquals(UnitKind.COMPILED, key.kind());
		assertEquals(compiled.getManifest().getUnitFingerprint(), key.fingerprint().orElseThrow());
		assertNull(compiled.eResource(), "storing did not move the caller's document");

		PackagedUnit loaded = (PackagedUnit) store.load(key).orElseThrow();
		assertNotSame(compiled, loaded.document());
		assertEquals("gen.Books", loaded.qualifiedName());
		assertEquals("m2t", loaded.language());
		assertEquals(compiled.getId(), loaded.document().getId());
		assertEquals(List.of(), SatelliteCollector.find(loaded.document()), "the copy is self-contained");
		assertEquals(0, referencesInto(loaded.document(), compiled), "and points at nothing of the original");
	}

	@Test
	void loadedUnit_resolvesItsMetamodelInTheStoresRegistry() throws Exception {
		UnitKey key = store.store("m2t", new PackagedUnit(compiledTemplate("gen.Books")));
		PackagedUnit loaded = (PackagedUnit) store.load(key).orElseThrow();
		assertSame(bookClass, variableType(loaded.document()),
				"the registry knows the nsURI, so the type is the registry's very instance");
	}

	@Test
	void loadedUnit_isServedFromItsPackageCopy_whereTheRegistryHasNothing() throws Exception {
		UnitKey key = store.store("m2t", new PackagedUnit(compiledTemplate("gen.Books")));
		// A store over the same backend, but with a registry that never heard of the metamodel
		InMemoryUnitStoreBackend other = new InMemoryUnitStoreBackend();
		other.put(key, backend.get(key).orElseThrow());
		UnitStore elsewhere = new DefaultUnitStore(other, new EPackageRegistryImpl());

		PackagedUnit loaded = (PackagedUnit) elsewhere.load(key).orElseThrow();
		EClass type = variableType(loaded.document());
		assertEquals("Book", type.getName());
		assertNotSame(bookClass, type);
		assertTrue(EcoreUtil.isAncestor(loaded.document(), type), "the type is the copy the unit carries");
	}

	@Test
	void bareAst_isRefused_compileFirst() {
		QvtoUnit.CompiledUnit bare = new QvtoUnit.CompiledUnit("t",
				QvtOperationalFactory.eINSTANCE.createOperationalTransformation());
		UnitStoreException failure = assertThrows(UnitStoreException.class, () -> store.store("qvto", bare));
		assertTrue(failure.getMessage().contains("compile()"), failure.getMessage());
	}

	@Test
	void languageMismatch_isRefused() throws Exception {
		CompiledUnit compiled = compiledTemplate("gen.Books");
		UnitStoreException failure = assertThrows(UnitStoreException.class,
				() -> store.store("qvto", new PackagedUnit(compiled)));
		assertTrue(failure.getMessage().contains("m2t"), failure.getMessage());
	}

	// ==== kinds, versions, keys ====

	@Test
	void sourceAndCompiledUnit_ofOneName_coexistAndAreToldApartByKind() throws Exception {
		UnitKey sourceKey = store.store("m2t", source("gen.Books", "[module Books(Ecore)/]"));
		UnitKey compiledKey = store.store("m2t", new PackagedUnit(compiledTemplate("gen.Books")));

		assertEquals(List.of(sourceKey), store.versions("m2t", "gen.Books", UnitKind.SOURCE));
		assertEquals(List.of(compiledKey), store.versions("m2t", "gen.Books", UnitKind.COMPILED));
		assertInstanceOf(StoredSource.class, store.load(UnitKey.of("m2t", "gen.Books", UnitKind.SOURCE)).orElseThrow());
		assertInstanceOf(PackagedUnit.class, store.load(UnitKey.of("m2t", "gen.Books", UnitKind.COMPILED)).orElseThrow());
		assertTrue(store.contains(sourceKey));
		assertTrue(store.contains(UnitKey.of("m2t", "gen.Books", UnitKind.COMPILED)));
	}

	@Test
	void unknownName_isEmpty_butAMissingVersionOfAKnownNameIsAnError() throws Exception {
		assertEquals(Optional.empty(), store.load(UnitKey.of("qvto", "nobody.Home", UnitKind.SOURCE)));
		assertFalse(store.contains(UnitKey.of("qvto", "nobody.Home", UnitKind.SOURCE)));

		UnitKey present = store.store("qvto", source("lib.Strings", "library Strings { }"));
		UnitKey wrongVersion = UnitKey.pinned("qvto", "lib.Strings", UnitKind.SOURCE, "m2x1:0000");
		UnitStoreException failure = assertThrows(UnitStoreException.class, () -> store.load(wrongVersion));
		assertTrue(failure.getMessage().contains(present.fingerprint().orElseThrow()),
				"the message names the versions that are there: " + failure.getMessage());
	}

	@Test
	void remove_takesOneVersionOrAll() throws Exception {
		UnitKey v1 = store.store("qvto", source("lib.Strings", "library Strings { helper a() : String { return 'a'; } }"));
		UnitKey v2 = store.store("qvto", source("lib.Strings", "library Strings { helper a() : String { return 'b'; } }"));

		assertTrue(store.remove(v1));
		assertEquals(List.of(v2), store.versions("qvto", "lib.Strings", UnitKind.SOURCE));
		assertTrue(store.remove(UnitKey.of("qvto", "lib.Strings", UnitKind.SOURCE)));
		assertEquals(List.of(), store.versions("qvto", "lib.Strings", UnitKind.SOURCE));
		assertFalse(store.remove(v2));
	}

	@Test
	void storingTheSameContentTwice_isOneVersion() throws Exception {
		UnitKey first = store.store("qvto", source("lib.Strings", "library Strings { }"));
		UnitKey second = store.store("qvto", source("lib.Strings", "library Strings { }"));
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
