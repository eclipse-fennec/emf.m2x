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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

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
import org.eclipse.fennec.m2x.model.ocl.OclPackage;
import org.eclipse.fennec.m2x.model.qvtoperational.ModelType;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoBlackboxRegistry;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.qvto.engine.QvtoStoreUnitResolver;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.UnitBinder;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;
import org.eclipse.fennec.m2x.unit.api.UnitPrepareException;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Execute from a prepared context: no resolver is asked, the metamodel is the runtime's instance
 * where the fingerprint matches, a pipeline shares one context, and a model loaded in the context
 * carries the context's types (#140, acceptance).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoPreparedExecutionTest {

	private static final String NS_URI = "http://example.org/m2x/qvto-prepare/1.0";
	private static final String HELPER_LIB = """
			library HelperLib {
			    helper title() : String {
			        return 'prepared';
			    }
			}
			""";
	/** T1: every Book of the input gets its title from the library. */
	private static final String RENAME = """
			modeltype SHELF uses 'http://example.org/m2x/qvto-prepare/1.0';
			import HelperLib;
			transformation Rename(inout m : SHELF) {
			    main() {
			        m.objectsOfType(Book)->forEach(b) {
			            b.title := title();
			        };
			    }
			}
			""";
	/** T2: a second Book for every Book in the input, into the same extent. */
	private static final String DUPLICATE = """
			modeltype SHELF uses 'http://example.org/m2x/qvto-prepare/1.0';
			transformation Duplicate(inout m : SHELF) {
			    mapping Book::copy() : Book {
			        title := self.title + ' (copy)';
			    }
			    main() {
			        m.objectsOfType(Book)->map copy();
			    }
			}
			""";
	private static final String OVER_OCL = """
			modeltype OCL uses 'http://www.eclipse.org/fennec/m2x/ocl/1.0';
			transformation Count(in m : OCL) {
			    main() {
			        log('expressions: ' + m.objectsOfType(OclExpression)->size().repr());
			    }
			}
			""";
	private static final String WITH_BLACKBOX = """
			modeltype SHELF uses 'http://example.org/m2x/qvto-prepare/1.0';
			import mylib;
			transformation Boxed(inout m : SHELF) {
			    main() {
			        m.objectsOfType(Book)->forEach(b) {
			            b.title := trimAll('  boxed  ');
			        };
			    }
			}
			""";

	private EPackage shelf;
	private EClass bookClass;
	private EPackage.Registry registry;
	private UnitStore store;
	/** The engine that compiles: resolves from the store. */
	private QvtoEngine compiler;
	/** The engine that executes: its only resolver throws — Execute must never reach it. */
	private QvtoEngine runner;

	@BeforeEach
	void setUp() {
		shelf = shelf(false);
		bookClass = (EClass) shelf.getEClassifier("Book");
		registry = new EPackageRegistryImpl();
		registry.put(NS_URI, shelf);
		store = new DefaultUnitStore(new InMemoryUnitStoreBackend(), registry);
		compiler = QvtoEngines.create(QvtoConfiguration.builder(oclConfig()).packageRegistry(registry)
				.addUnitResolver(new QvtoStoreUnitResolver(store)).unitResolverEnabled(true).build());
		QvtoUnitResolver forbidden = name -> {
			throw new AssertionError("Execute asked a resolver for '" + name + "'");
		};
		runner = QvtoEngines.create(QvtoConfiguration.builder(oclConfig()).packageRegistry(registry)
				.addUnitResolver(forbidden).unitResolverEnabled(true).build());
	}

	@Test
	void preparedUnit_runsWithoutAskingAnyResolver() throws Exception {
		store.store("qvto", new PackagedUnit(compiler.compile(HELPER_LIB, "HelperLib")));
		UnitKey key = store.store("qvto", new PackagedUnit(compiler.compile(RENAME, "Rename")));

		PreparedContext prepared = preparer(registry, runner.unitBinder()).prepare(key);
		EObject book = book("Moby Dick");
		QvtoExecutionResult result = runner.execute(prepared, "Rename", QvtoExecutionContext.of(new BasicQvtoModelExtent(book)));

		assertTrue(result.isSuccess(), () -> "execution failed: " + result.diagnostics());
		assertEquals("prepared", title(book));
	}

	@Test
	void oneContext_manyExecutions() throws Exception {
		store.store("qvto", new PackagedUnit(compiler.compile(HELPER_LIB, "HelperLib")));
		UnitKey key = store.store("qvto", new PackagedUnit(compiler.compile(RENAME, "Rename")));
		PreparedContext prepared = preparer(registry, runner.unitBinder()).prepare(key);
		for (int i = 0; i < 3; i++) {
			EObject book = book("run " + i);
			assertTrue(runner.execute(prepared, "Rename", QvtoExecutionContext.of(new BasicQvtoModelExtent(book))).isSuccess());
			assertEquals("prepared", title(book));
		}
	}

	@Test
	void pipeline_sharesOneContext_andTheRuntimeMetamodelWinsOnEquality() throws Exception {
		store.store("qvto", new PackagedUnit(compiler.compile(HELPER_LIB, "HelperLib")));
		UnitKey rename = store.store("qvto", new PackagedUnit(compiler.compile(RENAME, "Rename")));
		UnitKey duplicate = store.store("qvto", new PackagedUnit(compiler.compile(DUPLICATE, "Duplicate")));

		PreparedContext prepared = preparer(registry, runner.unitBinder()).prepare(rename, duplicate);
		OperationalTransformation duplicateAst = (OperationalTransformation) ((PackagedUnit) prepared.unit("Duplicate")
				.orElseThrow()).document().getUnit();
		ModelType modelType = duplicateAst.getModelParameter().get(0).getEType() instanceof ModelType mt ? mt : null;
		assertSame(shelf, modelType.getMetamodel().get(0), "same fingerprint: the prepared unit refers to the runtime instance");

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent(book("Moby Dick"));
		assertTrue(runner.execute(prepared, "Rename", QvtoExecutionContext.of(extent)).isSuccess());
		assertTrue(runner.execute(prepared, "Duplicate", QvtoExecutionContext.of(extent)).isSuccess());

		assertEquals(2, extent.getContents().size(), "T2 saw T1's output as its input");
		assertEquals(List.of("prepared", "prepared (copy)"), extent.getContents().stream().map(this::title).toList());
		assertSame(bookClass, extent.getContents().get(1).eClass(), "T2 created the copy as an instance of the runtime's Book");
	}

	@Test
	void modelLoadedInTheContext_carriesTheContextsTypes(@TempDir Path dir) throws Exception {
		store.store("qvto", new PackagedUnit(compiler.compile(HELPER_LIB, "HelperLib")));
		UnitKey key = store.store("qvto", new PackagedUnit(compiler.compile(RENAME, "Rename")));
		Path file = dir.resolve("shelf.xmi");
		saveBooks(file, "Moby Dick");

		PreparedContext prepared = preparer(registry, runner.unitBinder()).prepare(key);
		List<EObject> roots = prepared.contents(URI.createFileURI(file.toString()));
		assertEquals(1, roots.size());
		assertSame(bookClass, roots.get(0).eClass(), "loaded in the context, the model has the context's types");
		assertSame(roots, prepared.contents(URI.createFileURI(file.toString())), "loaded once");

		assertTrue(runner.execute(prepared, "Rename", QvtoExecutionContext.of(new BasicQvtoModelExtent(roots))).isSuccess());
		assertEquals("prepared", title(roots.get(0)));
	}

	@Test
	void generatedMetamodel_resolvesToTheGeneratedInstance() throws Exception {
		// A generated package enters the global registry when its class initializes — in a plain
		// JVM that is the consumer's doing, here the test's
		OclPackage.eINSTANCE.getNsURI();
		QvtoEngine plain = QvtoEngines.create(QvtoConfiguration.builder(oclConfig()).build());
		UnitStore global = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		UnitKey key = global.store("qvto", new PackagedUnit(plain.compile(OVER_OCL, "Count")));

		PreparedContext prepared = UnitPreparer.withDefaults(global, plain.unitBinder()).prepare(key);
		OperationalTransformation ast = (OperationalTransformation) ((PackagedUnit) prepared.unit("Count").orElseThrow())
				.document().getUnit();
		ModelType modelType = (ModelType) ast.getModelParameter().get(0).getEType();
		assertSame(OclPackage.eINSTANCE, modelType.getMetamodel().get(0),
				"a metamodel available as generated code is used as such");
	}

	// ==== failures at prepare, not mid-run ====

	@Test
	void differingMetamodel_failsAtPrepare_namingTheNsUriAndBothValues() throws Exception {
		UnitKey key = store.store("qvto", new PackagedUnit(compiler.compile(DUPLICATE, "Duplicate")));
		EPackage changed = shelf(true);
		EPackage.Registry other = new EPackageRegistryImpl();
		other.put(NS_URI, changed);

		UnitPrepareException failure = assertThrows(UnitPrepareException.class,
				() -> preparer(other, runner.unitBinder()).prepare(key));
		assertTrue(failure.getMessage().contains(NS_URI), failure.getMessage());
		assertTrue(failure.getMessage().contains(FingerprintHelper.fingerprint(shelf)), failure.getMessage());
		assertTrue(failure.getMessage().contains(FingerprintHelper.fingerprint(changed)), failure.getMessage());
	}

	@Test
	void missingBlackbox_failsAtPrepare() throws Exception {
		BasicQvtoBlackboxRegistry blackboxes = new BasicQvtoBlackboxRegistry();
		blackboxes.register(new QvtoCompiledUnitDependencyTest.TrimLibrary());
		QvtoEngine withBlackbox = QvtoEngines.create(QvtoConfiguration.builder(oclConfig()).packageRegistry(registry)
				.blackboxRegistry(blackboxes).blackboxEnabled(true).build());
		UnitKey key = store.store("qvto", new PackagedUnit(withBlackbox.compile(WITH_BLACKBOX, "Boxed")));

		UnitPrepareException failure = assertThrows(UnitPrepareException.class,
				() -> preparer(registry, runner.unitBinder()).prepare(key));
		assertTrue(failure.getMessage().contains("mylib"), failure.getMessage());

		PreparedContext prepared = preparer(registry, withBlackbox.unitBinder()).prepare(key);
		EObject book = book("x");
		assertTrue(withBlackbox.execute(prepared, "Boxed", QvtoExecutionContext.of(new BasicQvtoModelExtent(book))).isSuccess());
		assertEquals("boxed", title(book));
	}

	@Test
	void unboundUnit_isRefusedByExecute() throws Exception {
		store.store("qvto", new PackagedUnit(compiler.compile(HELPER_LIB, "HelperLib")));
		UnitKey key = store.store("qvto", new PackagedUnit(compiler.compile(RENAME, "Rename")));
		UnitBinder doesNothing = new UnitBinder() {
			@Override
			public String language() {
				return "qvto";
			}

			@Override
			public void bind(CompiledUnit unit, Map<String, CompiledUnit> dependencies) {
			}

			@Override
			public void verifyBlackboxes(CompiledUnit unit) {
			}
		};
		PreparedContext prepared = preparer(registry, doesNothing).prepare(key);
		IllegalArgumentException failure = assertThrows(IllegalArgumentException.class,
				() -> runner.execute(prepared, "Rename", QvtoExecutionContext.of(new BasicQvtoModelExtent(book("x")))));
		assertTrue(failure.getMessage().contains("HelperLib"), failure.getMessage());
		assertThrows(IllegalArgumentException.class,
				() -> runner.execute(prepared, "Nobody", QvtoExecutionContext.of(new BasicQvtoModelExtent())));
	}

	// ==== helpers ====

	private UnitPreparer preparer(EPackage.Registry runtime, UnitBinder binder) {
		return new UnitPreparer(store, runtime, FingerprintHelper.getDefaultFingerprintService(), List.of(binder));
	}

	private static OclConfiguration oclConfig() {
		return OclConfiguration.builder(new OclParserSupport()).build();
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

	private EObject book(String title) {
		EObject book = EcoreUtil.create(bookClass);
		book.eSet(bookClass.getEStructuralFeature("title"), title);
		return book;
	}

	private String title(EObject book) {
		return (String) book.eGet(book.eClass().getEStructuralFeature("title"));
	}

	private void saveBooks(Path file, String... titles) throws Exception {
		ResourceSet rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		rs.getPackageRegistry().put(NS_URI, shelf);
		if (shelf.eResource() == null) {
			rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION,
					new XMIResourceFactoryImpl());
			rs.createResource(URI.createURI(NS_URI)).getContents().add(shelf);
		}
		Resource resource = rs.createResource(URI.createFileURI(file.toString()));
		for (String title : titles) {
			resource.getContents().add(book(title));
		}
		resource.save(null);
		assertTrue(Files.size(file) > 0);
	}

}
