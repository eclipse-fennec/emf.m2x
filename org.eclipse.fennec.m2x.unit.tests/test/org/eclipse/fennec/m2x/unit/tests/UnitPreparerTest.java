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

import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.emf.osgi.fingerprint.util.FingerprintHelper;
import org.eclipse.fennec.m2x.model.compiled.CompiledFactory;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.DependencyEntry;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.m2t.M2tFactory;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitBinder;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;
import org.eclipse.fennec.m2x.unit.api.UnitPrepareException;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Prepare loads a unit and its closure into one context, verifies what it was built against, and
 * hands binding to the language (#140). Language-neutral: the units are built from the factories,
 * the binder records what it was asked to bind.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class UnitPreparerTest {

	private static final String NS_URI = "http://example.org/m2x/prepare-test/1.0";

	private EPackage metamodel;
	private EClass bookClass;
	private EPackage.Registry registry;
	private UnitStore store;
	private RecordingBinder binder;

	@BeforeEach
	void setUp() {
		metamodel = shelf(false);
		bookClass = (EClass) metamodel.getEClassifier("Book");
		registry = new EPackageRegistryImpl();
		registry.put(NS_URI, metamodel);
		store = new DefaultUnitStore(new InMemoryUnitStoreBackend(), registry);
		binder = new RecordingBinder();
	}

	// ==== the happy path ====

	@Test
	void prepare_loadsTheUnitAndItsPinnedDependency_andBindsThem() throws Exception {
		CompiledUnit library = compiled("gen.Lib");
		UnitKey libraryKey = store.store("m2t", new PackagedUnit(library));
		CompiledUnit main = compiled("gen.Main", pin("gen.Lib", libraryKey.fingerprint().orElseThrow()));
		UnitKey mainKey = store.store("m2t", new PackagedUnit(main));

		PreparedContext prepared = preparer(registry).prepare(mainKey);

		assertEquals(2, prepared.units().size(), "the unit and its dependency");
		assertTrue(prepared.unit("gen.Main").isPresent());
		assertTrue(prepared.unit("gen.Lib").isPresent());
		assertEquals(List.of("gen.Lib", "gen.Main"), binder.bound.keySet().stream().sorted().toList());
		assertEquals(List.of("gen.Lib"), binder.bound.get("gen.Main"), "main was bound to its dependency");
		assertEquals(List.of(), binder.bound.get("gen.Lib"));
		assertEquals(List.of("gen.Lib", "gen.Main"), binder.verified.stream().sorted().toList());
	}

	@Test
	void prepare_resolvesTheMetamodelToTheRuntimeInstance_whereTheFingerprintMatches() throws Exception {
		UnitKey key = store.store("m2t", new PackagedUnit(compiled("gen.Main")));
		PreparedContext prepared = preparer(registry).prepare(key);
		assertSame(bookClass, variableType(prepared, "gen.Main"), "the runtime instance wins on equality");
		assertSame(metamodel, prepared.packageRegistry().getEPackage(NS_URI));
	}

	@Test
	void prepare_servesTheMetamodelFromTheCopy_whereTheRuntimeHasNone() throws Exception {
		UnitKey key = store.store("m2t", new PackagedUnit(compiled("gen.Main")));
		PreparedContext prepared = preparer(new EPackageRegistryImpl()).prepare(key);
		EClass type = variableType(prepared, "gen.Main");
		assertEquals("Book", type.getName());
		assertTrue(EcoreUtil.isAncestor(((PackagedUnit) prepared.unit("gen.Main").orElseThrow()).document(), type),
				"the type is the copy the unit carries");
	}

	// ==== hard failures ====

	@Test
	void prepare_failsOnAMetamodelWithADifferingFingerprint_namingBothValues() throws Exception {
		UnitKey key = store.store("m2t", new PackagedUnit(compiled("gen.Main")));
		EPackage.Registry other = new EPackageRegistryImpl();
		EPackage changed = shelf(true);
		other.put(NS_URI, changed);

		UnitPrepareException failure = assertThrows(UnitPrepareException.class, () -> preparer(other).prepare(key));
		String expected = FingerprintHelper.fingerprint(metamodel);
		String actual = FingerprintHelper.fingerprint(changed);
		assertTrue(failure.getMessage().contains(NS_URI), failure.getMessage());
		assertTrue(failure.getMessage().contains(expected) && failure.getMessage().contains(actual),
				"both values are named: " + failure.getMessage());
	}

	@Test
	void prepare_failsOnAPinnedVersionTheStoreNoLongerHas() throws Exception {
		CompiledUnit library = compiled("gen.Lib");
		store.store("m2t", new PackagedUnit(library));
		CompiledUnit main = compiled("gen.Main", pin("gen.Lib", "m2x1:0000"));
		UnitKey mainKey = store.store("m2t", new PackagedUnit(main));

		UnitPrepareException failure = assertThrows(UnitPrepareException.class, () -> preparer(registry).prepare(mainKey));
		assertTrue(failure.getMessage().contains("gen.Lib"), failure.getMessage());
		assertTrue(failure.getMessage().contains(library.getManifest().getUnitFingerprint()),
				"the versions the store has are named: " + failure.getMessage());
	}

	@Test
	void prepare_failsWhenTwoUnitsPinDifferentVersionsOfOneName() throws Exception {
		UnitKey v1 = store.store("m2t", new PackagedUnit(compiled("gen.Lib")));
		UnitKey v2 = store.store("m2t", new PackagedUnit(compiled("gen.Lib", extraTemplate())));
		UnitKey a = store.store("m2t", new PackagedUnit(compiled("gen.A", pin("gen.Lib", v1.fingerprint().orElseThrow()))));
		UnitKey b = store.store("m2t", new PackagedUnit(compiled("gen.B", pin("gen.Lib", v2.fingerprint().orElseThrow()))));

		UnitPrepareException failure = assertThrows(UnitPrepareException.class, () -> preparer(registry).prepare(a, b));
		assertTrue(failure.getMessage().contains("different versions"), failure.getMessage());
	}

	@Test
	void prepare_refusesASource_andAMissingUnit_andALanguageWithoutBinder() throws Exception {
		UnitKey sourceKey = store.store("m2t", new StoredSourceUnit("gen.Src", "[module x(Ecore)/]"));
		assertTrue(assertThrows(UnitPrepareException.class, () -> preparer(registry).prepare(sourceKey))
				.getMessage().contains("compile()"));
		assertTrue(assertThrows(UnitPrepareException.class,
				() -> preparer(registry).prepare(UnitKey.of("m2t", "nobody.Home", UnitKind.COMPILED)))
				.getMessage().contains("nobody.Home"));
		UnitKey key = store.store("m2t", new PackagedUnit(compiled("gen.Main")));
		UnitPreparer noBinder = new UnitPreparer(store, registry, FingerprintHelper.getDefaultFingerprintService(), List.of());
		assertTrue(assertThrows(UnitPrepareException.class, () -> noBinder.prepare(key))
				.getMessage().contains("no binder for language 'm2t'"));
	}

	// ==== rebind ====

	@Test
	void rebind_takesTheNewestVersion_andRecordsWhatWasBound() throws Exception {
		store.store("m2t", new PackagedUnit(compiled("gen.Lib")));
		UnitKey newest = store.store("m2t", new PackagedUnit(compiled("gen.Lib", extraTemplate())));
		UnitKey mainKey = store.store("m2t", new PackagedUnit(compiled("gen.Main", rebind("gen.Lib"))));

		PreparedContext prepared = preparer(registry).prepare(mainKey);

		CompiledUnit main = ((PackagedUnit) prepared.unit("gen.Main").orElseThrow()).document();
		assertEquals(1, main.getManifest().getResolvedClosure().size());
		assertEquals("gen.Lib", main.getManifest().getResolvedClosure().get(0).getQualifiedName());
		assertEquals(newest.fingerprint().orElseThrow(), main.getManifest().getResolvedClosure().get(0).getFingerprint(),
				"the record names the version that was actually bound");
		assertEquals("store", main.getManifest().getResolvedClosure().get(0).getSource());
	}

	// ==== paths that had no test (#174) ====

	@Test
	@DisplayName("an embedded dependency is not looked for in the store")
	void prepare_takesAnEmbeddedDependencyFromTheUnitItself() throws Exception {
		// EMBED means the dependency travels inside the unit, bound when it was compiled. A
		// store that does not have it at all is the point: nothing may be looked up.
		CompiledUnit library = compiled("gen.Lib");
		CompiledUnit main = compiled("gen.Main", embed("gen.Lib",
				library.getManifest().getUnitFingerprint()));
		main.getEmbedded().add(library);
		UnitKey mainKey = store.store("m2t", new PackagedUnit(reseal(main)));

		PreparedContext prepared = preparer(registry).prepare(mainKey);

		assertEquals(1, prepared.units().size(),
				"the embedded unit is content of the host, not a second unit of the run");
		assertEquals(List.of("gen.Main"), binder.bound.keySet().stream().toList());
	}

	@Test
	@DisplayName("what the binder rejects ends the prepare, before anything is bound")
	void prepare_propagatesWhatTheBinderRefuses() throws Exception {
		UnitKey key = store.store("m2t", new PackagedUnit(compiled("gen.Main")));
		RecordingBinder refusing = new RecordingBinder();
		refusing.refuse = true;

		UnitPrepareException failure = assertThrows(UnitPrepareException.class,
				() -> new UnitPreparer(store, registry,
						FingerprintHelper.getDefaultFingerprintService(), List.of(refusing))
						.prepare(key));

		assertTrue(failure.getMessage().contains("the language says no"), failure::getMessage);
		assertEquals(Map.of(), refusing.bound,
				"validate runs for every unit before the first one is bound (#142)");
	}

	@Test
	@DisplayName("units that rebind each other terminate")
	void prepare_terminatesOnACycleOfRebindingUnits() throws Exception {
		// A rebinds B and B rebinds A. The map of what is already loaded is what ends this;
		// without it prepare would follow the two entries forever.
		//
		// A cycle of *pinned* units cannot be built at all: a pin names the fingerprint of the
		// other unit, and each fingerprint would then have to be part of the other's content.
		// Rebind names only the qualified name, which is what makes the cycle possible.
		store.store("m2t", new PackagedUnit(compiled("gen.A", rebind("gen.B"))));
		store.store("m2t", new PackagedUnit(compiled("gen.B", rebind("gen.A"))));
		UnitKey aKey = store.store("m2t", new PackagedUnit(compiled("gen.A", rebind("gen.B"))));

		PreparedContext prepared = assertTimeoutPreemptively(Duration.ofSeconds(10),
				() -> preparer(registry).prepare(aKey));

		assertTrue(prepared.unit("gen.A").isPresent());
		assertTrue(prepared.unit("gen.B").isPresent());
		assertEquals(2, prepared.units().size(), "each of the two loaded once");
	}

	private static DependencyEntry embed(String name, String fingerprint) {
		DependencyEntry entry = CompiledFactory.eINSTANCE.createDependencyEntry();
		entry.setQualifiedName(name);
		entry.setMode(DependencyMode.EMBED);
		entry.setFingerprint(fingerprint);
		return entry;
	}

	/** Seals again after content was added, so the fingerprint covers what the unit carries. */
	private static CompiledUnit reseal(CompiledUnit document) {
		document.getManifest().setUnitFingerprint(
				org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService.INSTANCE
						.fingerprint(document));
		return document;
	}

	// ==== helpers ====

	private UnitPreparer preparer(EPackage.Registry runtime) {
		return new UnitPreparer(store, runtime, FingerprintHelper.getDefaultFingerprintService(), List.of(binder));
	}

	private static EPackage shelf(boolean withAuthor) {
		EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
		ePackage.setName("shelf");
		ePackage.setNsURI(NS_URI);
		ePackage.setNsPrefix("shelf");
		EClass book = EcoreFactory.eINSTANCE.createEClass();
		book.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		book.getEStructuralFeatures().add(title);
		if (withAuthor) {
			EAttribute author = EcoreFactory.eINSTANCE.createEAttribute();
			author.setName("author");
			author.setEType(EcorePackage.Literals.ESTRING);
			book.getEStructuralFeatures().add(author);
		}
		ePackage.getEClassifiers().add(book);
		return ePackage;
	}

	/** A module with one template typed by the dynamic metamodel, plus the given dependency entries. */
	private CompiledUnit compiled(String name, DependencyEntry... dependencies) {
		return compiled(name, null, dependencies);
	}

	private CompiledUnit compiled(String name, Template extra, DependencyEntry... dependencies) {
		Module module = M2tFactory.eINSTANCE.createModule();
		module.setName(name);
		module.setNsURI("http://example.org/m2x/prepare-test/module/" + name);
		Template template = M2tFactory.eINSTANCE.createTemplate();
		template.setName("book");
		Variable parameter = OclFactory.eINSTANCE.createVariable();
		parameter.setName("b");
		parameter.setType(bookClass);
		template.getParameter().add(parameter);
		module.getOwnedModuleElement().add(template);
		if (extra != null) {
			module.getOwnedModuleElement().add(extra);
		}
		UnitPackager packager = UnitPackager.withDefaults();
		CompiledUnit document = packager.begin("m2t", name, module, DependencyMode.PIN, null);
		for (DependencyEntry dependency : dependencies) {
			document.getManifest().getDependencyEntry().add(dependency);
		}
		return packager.seal(document);
	}

	private static Template extraTemplate() {
		Template template = M2tFactory.eINSTANCE.createTemplate();
		template.setName("extra");
		return template;
	}

	private static DependencyEntry pin(String name, String fingerprint) {
		DependencyEntry entry = CompiledFactory.eINSTANCE.createDependencyEntry();
		entry.setQualifiedName(name);
		entry.setMode(DependencyMode.PIN);
		entry.setFingerprint(fingerprint);
		return entry;
	}

	private static DependencyEntry rebind(String name) {
		DependencyEntry entry = CompiledFactory.eINSTANCE.createDependencyEntry();
		entry.setQualifiedName(name);
		entry.setMode(DependencyMode.REBIND);
		return entry;
	}

	private static EClass variableType(PreparedContext prepared, String name) {
		Module module = (Module) ((PackagedUnit) prepared.unit(name).orElseThrow()).document().getUnit();
		Template template = (Template) module.getOwnedModuleElement().get(0);
		return (EClass) template.getParameter().get(0).getType();
	}

	/** Records what it was asked to bind and verify. */
	static final class RecordingBinder implements UnitBinder {
		final Map<String, List<String>> bound = new LinkedHashMap<>();
		final List<String> verified = new ArrayList<>();
		/** Set to have {@code validate} refuse, as a language rejecting a unit would. */
		boolean refuse;

		@Override
		public void validate(CompiledUnit unit) throws UnitPrepareException {
			if (refuse) {
				throw new UnitPrepareException("the language says no");
			}
		}

		@Override
		public String language() {
			return "m2t";
		}

		@Override
		public void bind(CompiledUnit unit, Map<String, CompiledUnit> dependencies) {
			bound.put(unit.getManifest().getQualifiedName(), new ArrayList<>(dependencies.keySet()));
		}

		@Override
		public void verifyBlackboxes(CompiledUnit unit) {
			verified.add(unit.getManifest().getQualifiedName());
		}
	}

	/** A source as a caller would hand it to a store. */
	record StoredSourceUnit(String qualifiedName, String source) implements Unit.Source {
		@Override
		public URI uri() {
			return URI.createURI("mem:/" + qualifiedName);
		}
	}
}
