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
package org.eclipse.fennec.m2x.unit.registry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistries;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistryEntry;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistryWriter;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.m2t.M2tFactory;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;
import org.eclipse.fennec.m2x.unit.api.UnitResourceSet;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.api.UnitStoreException;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.eclipse.fennec.m2x.unit.materialize.UnitMaterializer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.eclipse.fennec.m2x.unit.store.StoredSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The object medium keeps the store contract: what goes in is normalized into the transport
 * state, what comes out is an independent copy, and the same unit behaves the same over bytes
 * and over the registry (#213).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class RegistryUnitStoreTest {

	private static final String NS_URI = "http://example.org/m2x/registry-store-test/1.0";

	private EPackage metamodel;
	private EClass bookClass;
	private EPackage.Registry packages;
	private EObjectRegistryWriter writer;
	private RegistryUnitStore store;

	/** A source unit without any engine on the path. */
	private record TextSource(String qualifiedName, URI uri, String source) implements Unit.Source {}

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
		packages = new EPackageRegistryImpl();
		packages.put(NS_URI, metamodel);
		writer = EObjectRegistries.createRegistry("units-under-test");
		store = new RegistryUnitStore(writer);
	}

	// ==== the contract, medium-independent ====

	@Test
	void compiledUnit_roundTrips_withTheSameKeyAsTheByteMedium() throws Exception {
		CompiledUnit compiled = compiledTemplate("gen.Books");
		UnitKey overBytes = new DefaultUnitStore(new InMemoryUnitStoreBackend()).put(EcoreUtil.copy(compiled));
		UnitKey overObjects = store.put(compiled);

		assertEquals(overBytes, overObjects, "one unit, one key — whatever carries it");
		PackagedUnit loaded = (PackagedUnit) store.get(overObjects).orElseThrow();
		assertEquals("gen.Books", loaded.qualifiedName());
		assertEquals("m2t", loaded.language());
	}

	@Test
	void whatComesOut_isACopy_andTheRegistrysInstanceNeverLeaves() throws Exception {
		UnitKey key = store.put(compiledTemplate("gen.Books"));
		EObject inside = writer.getRegistry().getEntry(entryKeyOf(key)).orElseThrow().object();

		PackagedUnit first = (PackagedUnit) store.get(key).orElseThrow();
		PackagedUnit second = (PackagedUnit) store.get(key).orElseThrow();
		assertNotSame(inside, first.document());
		assertNotSame(first.document(), second.document());

		((Module) first.document().getUnit()).setName("mutated");
		assertEquals("gen.Books", ((Module) ((CompiledUnit) inside).getUnit()).getName(),
				"a caller's mutation cannot reach the store");
	}

	@Test
	void whatGoesIn_isNormalizedIntoTheTransportState() throws Exception {
		// The producer's document is live: its variable type IS the caller's very EClass
		CompiledUnit compiled = compiledTemplate("gen.Books");
		UnitKey key = store.put(compiled);

		EObject inside = writer.getRegistry().getEntry(entryKeyOf(key)).orElseThrow().object();
		Template template = (Template) ((Module) ((CompiledUnit) inside).getUnit()).getOwnedModuleElement().get(0);
		Object raw = template.getParameter().get(0).eGet(
				template.getParameter().get(0).eClass().getEStructuralFeature("type"), false);
		assertTrue(((EObject) raw).eIsProxy(),
				"the registry never carries the producer's bindings — proxies are the transport state");
	}

	@Test
	void materializedCopy_bindsInTheConsumersContext_notTheProducers() throws Exception {
		UnitKey key = store.put(compiledTemplate("gen.Books"));
		PackagedUnit loaded = (PackagedUnit) store.get(key).orElseThrow();
		UnitMaterializer.defaults().materialize(loaded, new UnitResourceSet(packages));
		assertSame(bookClass, variableType(loaded.document()),
				"the context knows the nsURI, so the type is the context's very instance");
	}

	@Test
	void sources_liveBesideCompiledUnits_toldApartByKind() throws Exception {
		UnitKey sourceKey = store.put("m2t", new TextSource("gen.Books",
				URI.createURI("mem:/gen/Books.mtl"), "[module Books(Ecore)/]"));
		UnitKey compiledKey = store.put(compiledTemplate("gen.Books"));

		assertEquals(List.of(sourceKey), store.versions("m2t", "gen.Books", UnitKind.SOURCE));
		assertEquals(List.of(compiledKey), store.versions("m2t", "gen.Books", UnitKind.COMPILED));
		StoredSource text = assertInstanceOf(StoredSource.class,
				store.get(UnitKey.of("m2t", "gen.Books", UnitKind.SOURCE)).orElseThrow());
		assertEquals("[module Books(Ecore)/]", text.source());
	}

	@Test
	void unknownName_isEmpty_butAMissingVersionOfAKnownNameIsAnError() throws Exception {
		assertEquals(Optional.empty(), store.get(UnitKey.of("m2t", "nobody.Home", UnitKind.COMPILED)));
		UnitKey present = store.put(compiledTemplate("gen.Books"));
		UnitKey wrongVersion = UnitKey.pinned("m2t", "gen.Books", UnitKind.COMPILED, "m2x1:0000");
		UnitStoreException failure = assertThrows(UnitStoreException.class, () -> store.get(wrongVersion));
		assertTrue(failure.getMessage().contains(present.fingerprint().orElseThrow()),
				"the message names the versions that are there: " + failure.getMessage());
	}

	@Test
	void remove_takesOneVersionOrAll() throws Exception {
		UnitKey v1 = store.put("m2t", new TextSource("gen.Books", URI.createURI("mem:/a"), "[module Books(Ecore)/]"));
		UnitKey v2 = store.put("m2t", new TextSource("gen.Books", URI.createURI("mem:/b"), "[module Books(Ecore)/] "));

		assertTrue(store.remove(v1));
		assertEquals(List.of(v2), store.versions("m2t", "gen.Books", UnitKind.SOURCE));
		assertTrue(store.remove(UnitKey.of("m2t", "gen.Books", UnitKind.SOURCE)));
		assertEquals(List.of(), store.versions("m2t", "gen.Books", UnitKind.SOURCE));
		assertFalse(store.remove(v2));
	}

	// ==== what only this medium has ====

	@Test
	void readOnlyStore_answersButRefusesToWrite() throws Exception {
		UnitKey key = store.put(compiledTemplate("gen.Books"));
		UnitStore readOnly = new RegistryUnitStore(writer.getRegistry());

		assertTrue(readOnly.contains(key));
		assertTrue(readOnly.get(key).isPresent());
		UnitStoreException failure = assertThrows(UnitStoreException.class,
				() -> readOnly.put(compiledTemplate("gen.Other")));
		assertTrue(failure.getMessage().contains("no write face"), failure.getMessage());
	}

	@Test
	void aForeignEntry_answersByItsManifest_withoutTheUnitProperties() throws Exception {
		// What a provider files: the document under its own key, no unit.* properties
		CompiledUnit document = providerDocument();
		writer.put("some-provider", "whatever/key", document, java.util.Map.of());

		UnitKey key = UnitKey.pinned("m2t", "gen.Provided", UnitKind.COMPILED,
				document.getManifest().getUnitFingerprint());
		assertTrue(store.contains(key), "the manifest names the key, the entry key does not matter");
		PackagedUnit loaded = (PackagedUnit) store.get(key).orElseThrow();
		assertEquals("gen.Provided", loaded.qualifiedName());
		assertEquals(List.of(key), store.versions("m2t", "gen.Provided", UnitKind.COMPILED));
	}

	@Test
	void entryProperties_carryTheKey_andNoEmfFingerprint() throws Exception {
		UnitKey key = store.put(compiledTemplate("gen.Books"));
		EObjectRegistryEntry entry = writer.getRegistry().getEntry(entryKeyOf(key)).orElseThrow();

		assertEquals("m2t", entry.properties().get(RegistryUnitStore.PROP_LANGUAGE));
		assertEquals("compiled", entry.properties().get(RegistryUnitStore.PROP_KIND));
		assertEquals("gen.Books", entry.properties().get(RegistryUnitStore.PROP_QUALIFIED_NAME));
		assertEquals(key.fingerprint().orElseThrow(), entry.properties().get(RegistryUnitStore.PROP_FINGERPRINT));
		assertEquals("transformation", entry.properties().get(RegistryUnitStore.PROP_NATURE),
				"the nature is filterable without opening a document (#224)");
		assertFalse(entry.properties().containsKey("emf.fingerprint"),
				"a unit references several packages; the model-fingerprint join key stays empty rather than wrong");
	}

	// ==== helpers ====

	private CompiledUnit providerDocument() {
		return compiledTemplate("gen.Provided");
	}

	/** A module with one template whose parameter is typed by the dynamic metamodel. */
	private CompiledUnit compiledTemplate(String name) {
		Module module = M2tFactory.eINSTANCE.createModule();
		module.setName(name);
		module.setNsURI("http://example.org/m2x/registry-store-test/module/" + name);
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

	private static String entryKeyOf(UnitKey key) {
		return key.language() + "/" + key.kind().tag() + "/" + key.qualifiedName() + "/"
				+ key.fingerprint().orElseThrow();
	}
}
