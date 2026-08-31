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
package org.eclipse.fennec.m2x.qvto.tests.regression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.fennec.emf.osgi.fingerprint.util.FingerprintHelper;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest;
import org.eclipse.fennec.m2x.model.compiled.DependencyEntry;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.compiled.PackageEntry;
import org.eclipse.fennec.m2x.model.compiled.PackageRole;
import org.eclipse.fennec.m2x.model.ocl.OclPackage;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.qvto.engine.QvtoStoreUnitResolver;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitBinder;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;
import org.eclipse.fennec.m2x.unit.api.UnitPrepareException;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * The examples of {@code docs/compiled-units-guide.md}, executed. A guide whose code does not
 * compile is worse than none, and the shapes it shows — compile with a mode, store, prepare,
 * execute, the manifest accessors, resolving from a store — are exactly what breaks silently when
 * an API is renamed.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class CompiledUnitsGuideExamplesTest {

	private static final String LIB_SOURCE = """
			library HelperLib {
			    helper greet() : String {
			        return 'hello';
			    }
			}
			""";
	private static final String MAIN = """
			modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
			import HelperLib;
			transformation Main(inout m : ECORE) {
			    main() {
			        m.objectsOfType(EPackage)->forEach(p) {
			            p.name := greet();
			        };
			    }
			}
			""";

	/** §7 — the metamodel of the worked examples, dynamic: built here, no generated code. */
	private static final String SHELF_NS = "http://example.org/m2x/guide/shelf/1.0";
	private static final String CASE_LIB = """
			library text.Case {
			    helper shout(s : String) : String {
			        return s.toUpperCase() + '!';
			    }
			}
			""";
	private static final String TITLES_LIB = """
			library shelf.Titles {
			    helper prefix(s : String) : String {
			        return 'now reading: ' + s;
			    }
			}
			""";
	private static final String ANNOUNCE = """
			modeltype SHELF uses 'http://example.org/m2x/guide/shelf/1.0';
			import shelf.Titles;
			import text.Case;
			transformation Announce(inout m : SHELF) {
			    main() {
			        m.objectsOfType(Book)->forEach(b) {
			            b.title := shout(prefix(b.title));
			        };
			    }
			}
			""";
	/** The same, without any import — example A. */
	private static final String UPPERCASE = """
			modeltype SHELF uses 'http://example.org/m2x/guide/shelf/1.0';
			transformation Uppercase(inout m : SHELF) {
			    main() {
			        m.objectsOfType(Book)->forEach(b) {
			            b.title := b.title.toUpperCase();
			        };
			    }
			}
			""";

	/** §3 — compile() beside parse(), and what the document holds. */
	@Test
	void section3_compileBesideParse() throws Exception {
		QvtoEngine engine = engineWithLibrary();

		OperationalTransformation ast = engine.parse(MAIN, "Main");
		CompiledUnit compiled = engine.compile(MAIN, "Main");
		OperationalTransformation script = (OperationalTransformation) compiled.getUnit();

		assertNotNull(ast);
		assertNotNull(compiled.getId());
		CompiledUnitManifest m = compiled.getManifest();
		assertEquals("qvto", m.getLanguage());
		assertEquals("Main", m.getQualifiedName());
		assertTrue(m.getUnitFingerprint().startsWith("m2x1:"));
		assertTrue(m.getSourceFingerprint().startsWith("m2x1:"));
		assertEquals(DependencyMode.PIN, m.getDependencyMode());
		assertEquals("hello", runOn(engine, script));
	}

	/** §4 — embed carries the library; the unit runs where no resolver exists. */
	@Test
	void section4_embedRunsWithoutAResolver() throws Exception {
		CompiledUnit unit = engineWithLibrary()
				.compile(MAIN, "Main", UnitCompileOptions.of(DependencyMode.EMBED));

		assertEquals("HelperLib", unit.getEmbedded().get(0).getManifest().getQualifiedName());
		QvtoEngine bare = QvtoEngines.create(QvtoConfiguration.builder(oclConfig()).build());
		assertEquals("hello", runOn(bare, (OperationalTransformation) unit.getUnit()));
	}

	/**
	 * §5 — store, versions, kinds; §6 — prepare and execute. Note what prepare needs: the pinned
	 * dependency has to be in the store as a <em>compiled</em> unit. A source there serves the
	 * compiler, which parses it; prepare loads documents by key and has nothing to parse with.
	 */
	@Test
	void section5and6_storePrepareExecute() throws Exception {
		QvtoEngine engine = engineWithLibrary();
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());

		UnitKey key = store.store("qvto", new PackagedUnit(engine.compile(MAIN, "Main")));
		PackagedUnit loaded = (PackagedUnit) store.load(key).orElseThrow();
		assertEquals("Main", loaded.qualifiedName());

		// a source and a compiled unit of one name live side by side, told apart by kind
		store.store("qvto", new QvtoUnit.SourceUnit("HelperLib", URI.createURI("mem:/lib.qvto"), LIB_SOURCE));
		store.store("qvto", new PackagedUnit(engine.compile(LIB_SOURCE, "HelperLib")));
		assertEquals(1, store.versions("qvto", "HelperLib", UnitKind.SOURCE).size());
		assertEquals(1, store.versions("qvto", "HelperLib", UnitKind.COMPILED).size());
		assertTrue(store.load(UnitKey.of("qvto", "HelperLib", UnitKind.SOURCE)).isPresent());

		QvtoEngine overStore = QvtoEngines.create(QvtoConfiguration.builder(oclConfig())
				.addUnitResolver(new QvtoStoreUnitResolver(store))
				.unitResolverEnabled(true).build());
		UnitKey pinned = store.store("qvto", new PackagedUnit(overStore.compile(MAIN, "Main")));

		PreparedContext prepared = UnitPreparer.withDefaults(store, overStore.unitBinder()).prepare(pinned);
		EObject pkg = ecorePackage("Original");
		QvtoExecutionResult result = overStore.execute(prepared, "Main",
				QvtoExecutionContext.of(new BasicQvtoModelExtent(List.of(pkg))));
		assertTrue(result.isSuccess(), () -> "execution failed: " + result.diagnostics());
		assertEquals("hello", name(pkg));
	}

	/** §6 — one context, many executions. */
	@Test
	void section6_oneContextManyExecutions() throws Exception {
		QvtoEngine engine = engineWithLibrary();
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		store.store("qvto", new PackagedUnit(engine.compile(LIB_SOURCE, "HelperLib")));
		QvtoEngine overStore = QvtoEngines.create(QvtoConfiguration.builder(oclConfig())
				.addUnitResolver(new QvtoStoreUnitResolver(store)).unitResolverEnabled(true).build());
		UnitKey key = store.store("qvto", new PackagedUnit(overStore.compile(MAIN, "Main")));

		PreparedContext prepared = UnitPreparer.withDefaults(store, overStore.unitBinder()).prepare(key);
		for (int i = 0; i < 3; i++) {
			EObject pkg = ecorePackage("run " + i);
			assertTrue(overStore.execute(prepared, "Main",
					QvtoExecutionContext.of(new BasicQvtoModelExtent(List.of(pkg)))).isSuccess());
			assertEquals("hello", name(pkg));
		}
	}

	/** §6 — under rebind, prepare records what it bound. */
	@Test
	void section6_rebindRecordsTheClosure() throws Exception {
		QvtoEngine engine = engineWithLibrary();
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		store.store("qvto", new PackagedUnit(engine.compile(LIB_SOURCE, "HelperLib")));
		QvtoEngine overStore = QvtoEngines.create(QvtoConfiguration.builder(oclConfig())
				.addUnitResolver(new QvtoStoreUnitResolver(store)).unitResolverEnabled(true).build());
		UnitKey key = store.store("qvto", new PackagedUnit(
				overStore.compile(MAIN, "Main", UnitCompileOptions.of(DependencyMode.REBIND))));

		PreparedContext prepared = UnitPreparer.withDefaults(store, overStore.unitBinder()).prepare(key);
		CompiledUnit ran = ((PackagedUnit) prepared.unit("Main").orElseThrow()).document();
		assertEquals("HelperLib", ran.getManifest().getResolvedClosure().get(0).getQualifiedName());
		assertEquals("store", ran.getManifest().getResolvedClosure().get(0).getSource());
	}

	// ==== §7: the two worked examples ====

	/**
	 * §7.1 — one script, no imports: compile it, store it, and run it from the store on an engine
	 * whose only resolver would fail if prepare had left anything to resolve.
	 */
	@Test
	void section7_exampleA_oneScriptBecomesAStoredUnit() throws Exception {
		EPackage shelf = shelf();
		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(SHELF_NS, shelf);
		QvtoEngine engine = engineOver(registry);

		CompiledUnit unit = engine.compile(UPPERCASE, "Uppercase");

		assertEquals("Uppercase", unit.getManifest().getQualifiedName());
		assertTrue(unit.getManifest().getUnitFingerprint().startsWith("m2x1:"));
		assertEquals(List.of(), unit.getManifest().getDependencyEntry(), "nothing imported");
		PackageEntry entry = unit.getManifest().getPackageEntry().get(0);
		assertEquals(SHELF_NS, entry.getNsURI());
		assertEquals(PackageRole.EMBEDDED, entry.getRole(), "dynamic metamodel: the copy travels");
		assertTrue(entry.getFingerprint().startsWith("fp1:"));
		assertEquals(1, unit.getPackages().size());

		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend(), registry);
		UnitKey key = store.store("qvto", new PackagedUnit(unit));
		assertEquals(unit.getManifest().getUnitFingerprint(), key.fingerprint().orElseThrow());

		QvtoEngine runner = engineOver(registry, forbiddenResolver());
		PreparedContext prepared = preparer(store, registry, runner.unitBinder()).prepare(key);
		EObject book = book(shelf, "moby dick");
		QvtoExecutionResult run = runner.execute(prepared, "Uppercase",
				QvtoExecutionContext.of(new BasicQvtoModelExtent(List.of(book))));

		assertTrue(run.isSuccess(), () -> "execution failed: " + run.diagnostics());
		assertEquals("MOBY DICK", title(book));
	}

	/**
	 * §7.2 — three scripts under {@code embed}: one self-contained document. It runs where neither
	 * the libraries nor the metamodel are known, because both travel inside it.
	 */
	@Test
	void section7_exampleB_embedCarriesTheLibrariesAndTheMetamodel() throws Exception {
		EPackage shelf = shelf();
		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(SHELF_NS, shelf);
		QvtoEngine compiler = engineOver(registry, sourceResolver());

		CompiledUnit archive = compiler.compile(ANNOUNCE, "Announce",
				UnitCompileOptions.of(DependencyMode.EMBED));

		assertEquals(List.of("shelf.Titles", "text.Case"),
				archive.getEmbedded().stream().map(u -> u.getManifest().getQualifiedName()).sorted().toList());
		assertEquals(1, archive.getPackages().size(), "the dynamic metamodel travels too");

		// Machine B: an empty registry, no resolver, no source of either library anywhere
		EPackage.Registry nothing = new EPackageRegistryImpl();
		UnitStore transported = new DefaultUnitStore(new InMemoryUnitStoreBackend(), nothing);
		UnitKey key = transported.store("qvto", new PackagedUnit(archive));
		QvtoEngine runner = engineOver(nothing, forbiddenResolver());

		PreparedContext prepared = preparer(transported, nothing, runner.unitBinder()).prepare(key);
		assertEquals(List.of("Announce"), prepared.units().stream().map(Unit::qualifiedName).toList(),
				"the libraries are inside the unit, not units of the context");

		// The metamodel this run has is the copy the unit carries — that is what its types resolve
		// to, so that is what the input model has to be built with
		EPackage carried = ((PackagedUnit) prepared.unit("Announce").orElseThrow()).document().getPackages().get(0);
		assertEquals(SHELF_NS, carried.getNsURI());
		EObject book = book(carried, "moby dick");
		QvtoExecutionResult run = runner.execute(prepared, "Announce",
				QvtoExecutionContext.of(new BasicQvtoModelExtent(List.of(book))));

		assertTrue(run.isSuccess(), () -> "execution failed: " + run.diagnostics());
		assertEquals("NOW READING: MOBY DICK!", title(book));
	}

	/**
	 * §7.2 — a carried metamodel serves the unit's own references, not the package registry: a
	 * model in a file whose metamodel only exists as a copy cannot be loaded through the context.
	 * Register the metamodel on the executing side if that is what you need.
	 */
	@Test
	void section7_exampleB_aCarriedMetamodelIsNotInTheRegistry(@TempDir Path dir) throws Exception {
		EPackage shelf = shelf();
		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(SHELF_NS, shelf);
		Path file = dir.resolve("shelf.xmi");
		saveBook(file, shelf, "moby dick");

		CompiledUnit archive = engineOver(registry, sourceResolver())
				.compile(ANNOUNCE, "Announce", UnitCompileOptions.of(DependencyMode.EMBED));
		EPackage.Registry nothing = new EPackageRegistryImpl();
		UnitStore transported = new DefaultUnitStore(new InMemoryUnitStoreBackend(), nothing);
		UnitKey key = transported.store("qvto", new PackagedUnit(archive));
		QvtoEngine runner = engineOver(nothing, forbiddenResolver());
		PreparedContext prepared = preparer(transported, nothing, runner.unitBinder()).prepare(key);

		UnitPrepareException failure = assertThrows(UnitPrepareException.class,
				() -> prepared.contents(URI.createFileURI(file.toString())));
		assertTrue(failure.getMessage().contains("Package with uri '" + SHELF_NS + "' not found"),
				failure.getMessage());
	}

	/**
	 * §7.2 — the same three scripts under {@code pin}: links instead of copies. Each unit is in the
	 * store once, and prepare needs the ones the manifest names.
	 */
	@Test
	void section7_exampleB_pinKeepsLinks_andPrepareNeedsTheClosure() throws Exception {
		EPackage shelf = shelf();
		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(SHELF_NS, shelf);
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend(), registry);
		QvtoEngine compiler = engineOver(registry, sourceResolver());

		CompiledUnit main = compiler.compile(ANNOUNCE, "Announce");   // pin is the default

		assertEquals(List.of(), main.getEmbedded(), "nothing carried");
		assertEquals(List.of("shelf.Titles", "text.Case"), main.getManifest().getDependencyEntry().stream()
				.map(DependencyEntry::getQualifiedName).sorted().toList());
		DependencyEntry dependency = main.getManifest().getDependencyEntry().get(0);
		assertEquals(DependencyMode.PIN, dependency.getMode());
		assertTrue(dependency.getFingerprint().startsWith("m2x1:"),
				"named with the exact version it was built against");

		// prepare loads documents by key, so every pinned dependency has to be in the store as a
		// compiled unit — a source there serves the compiler, which parses it
		UnitKey key = store.store("qvto", new PackagedUnit(main));
		QvtoEngine runner = engineOver(registry, forbiddenResolver());
		UnitPrepareException missing = assertThrows(UnitPrepareException.class,
				() -> preparer(store, registry, runner.unitBinder()).prepare(key));
		assertTrue(missing.getMessage().contains("no compiled unit 'shelf.Titles'")
				|| missing.getMessage().contains("no compiled unit 'text.Case'"), missing.getMessage());

		store.store("qvto", new PackagedUnit(compiler.compile(CASE_LIB, "text.Case")));
		store.store("qvto", new PackagedUnit(compiler.compile(TITLES_LIB, "shelf.Titles")));
		PreparedContext prepared = preparer(store, registry, runner.unitBinder()).prepare(key);
		assertEquals(List.of("Announce", "shelf.Titles", "text.Case"),
				prepared.units().stream().map(Unit::qualifiedName).sorted().toList(),
				"the closure is loaded, each unit once");

		EObject book = book(shelf, "moby dick");
		QvtoExecutionResult run = runner.execute(prepared, "Announce",
				QvtoExecutionContext.of(new BasicQvtoModelExtent(List.of(book))));
		assertTrue(run.isSuccess(), () -> "execution failed: " + run.diagnostics());
		assertEquals("NOW READING: MOBY DICK!", title(book));
	}

	/**
	 * §7.3 — a metamodel with generated code is only named, never copied: the runtime has it, and
	 * the entry says which version was expected.
	 */
	@Test
	void section7_generatedMetamodel_isALinkNotACopy() throws Exception {
		OclPackage.eINSTANCE.getNsURI();   // the generated package registers itself
		QvtoEngine plain = QvtoEngines.create(QvtoConfiguration.builder(oclConfig()).build());

		CompiledUnit unit = plain.compile("""
				modeltype OCL uses 'http://www.eclipse.org/fennec/m2x/ocl/1.0';
				transformation Count(in m : OCL) {
				    main() { log('expressions: ' + m.objectsOfType(OclExpression)->size().repr()); }
				}
				""", "Count");

		PackageEntry entry = unit.getManifest().getPackageEntry().get(0);
		assertEquals(OclPackage.eNS_URI, entry.getNsURI());
		assertEquals(PackageRole.REFERENCED, entry.getRole());
		assertTrue(entry.getFingerprint().startsWith("fp1:"));
		assertEquals(List.of(), unit.getPackages(), "no copy of a metamodel the runtime brings");
	}

	// ==== helpers ====

	/** The dynamic metamodel of §7: no generated code, so a unit carries a copy of it. */
	private static EPackage shelf() {
		EPackage shelf = EcoreFactory.eINSTANCE.createEPackage();
		shelf.setName("shelf");
		shelf.setNsURI(SHELF_NS);
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

	private static EObject book(EPackage shelf, String title) {
		EClass book = (EClass) shelf.getEClassifier("Book");
		EObject instance = shelf.getEFactoryInstance().create(book);
		instance.eSet(book.getEStructuralFeature("title"), title);
		return instance;
	}

	private static String title(EObject book) {
		return (String) book.eGet(book.eClass().getEStructuralFeature("title"));
	}

	private static void saveBook(Path file, EPackage shelf, String title) throws Exception {
		ResourceSet set = new ResourceSetImpl();
		set.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		set.getPackageRegistry().put(SHELF_NS, shelf);
		Resource resource = set.createResource(URI.createFileURI(file.toString()));
		resource.getContents().add(book(shelf, title));
		resource.save(Map.of());
	}

	private static QvtoEngine engineOver(EPackage.Registry registry, QvtoUnitResolver... resolvers) {
		QvtoConfiguration.Builder builder = QvtoConfiguration.builder(oclConfig()).packageRegistry(registry);
		for (QvtoUnitResolver resolver : resolvers) {
			builder.addUnitResolver(resolver).unitResolverEnabled(true);
		}
		return QvtoEngines.create(builder.build());
	}

	/** The two libraries of §7 as sources — what a compile is allowed to ask for. */
	private static QvtoUnitResolver sourceResolver() {
		Map<String, String> sources = Map.of("text.Case", CASE_LIB, "shelf.Titles", TITLES_LIB);
		return name -> Optional.ofNullable(sources.get(name))
				.map(source -> new QvtoUnit.SourceUnit(name, URI.createURI("mem:/" + name + ".qvto"), source));
	}

	/** What execute must never reach. */
	private static QvtoUnitResolver forbiddenResolver() {
		return name -> {
			throw new AssertionError("a resolver was asked for '" + name + "' after prepare");
		};
	}

	private static UnitPreparer preparer(UnitStore store, EPackage.Registry runtime, UnitBinder binder) {
		return new UnitPreparer(store, runtime, FingerprintHelper.getDefaultFingerprintService(), List.of(binder));
	}

	private static OclConfiguration oclConfig() {
		return OclConfiguration.builder(new OclParserSupport()).build();
	}

	private static QvtoEngine engineWithLibrary() {
		QvtoUnitResolver lib = name -> "HelperLib".equals(name)
				? Optional.of(new QvtoUnit.SourceUnit(name, URI.createURI("mem:/HelperLib.qvto"), LIB_SOURCE))
				: Optional.empty();
		return QvtoEngines.create(QvtoConfiguration.builder(oclConfig())
				.addUnitResolver(lib).unitResolverEnabled(true).build());
	}

	private static EObject ecorePackage(String name) {
		EObject pkg = EcoreUtil.create(EcorePackage.Literals.EPACKAGE);
		pkg.eSet(EcorePackage.Literals.ENAMED_ELEMENT__NAME, name);
		return pkg;
	}

	private static String name(EObject pkg) {
		return (String) pkg.eGet(EcorePackage.Literals.ENAMED_ELEMENT__NAME);
	}

	private static String runOn(QvtoEngine engine, OperationalTransformation transformation) {
		EObject pkg = ecorePackage("Original");
		QvtoExecutionResult result = engine.execute(transformation,
				QvtoExecutionContext.of(new BasicQvtoModelExtent(List.of(pkg))));
		assertTrue(result.isSuccess(), () -> "execution failed: " + result.diagnostics());
		return name(pkg);
	}
}
