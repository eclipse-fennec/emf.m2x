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
package org.eclipse.fennec.m2x.qvtd.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdStoreUnitResolver;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * compile → store → load → execute for QVT-R, with a dynamic metamodel that lives in no resource:
 * the store has to give it one to write the document, and the loaded unit has to find it again
 * through the store's registry (#139, acceptance).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtdStoreRoundTripTest {

	private static final String NS_URI = "http://example.org/m2x/qvtr-store/1.0";
	private static final String LIBRARY = """
			transformation shared(source : bookshelf, target : bookshelf) {
			    top relation CopyTitle {
			        t : String;
			        checkonly domain source b1 : Book { title = t };
			        enforce domain target b2 : Book { title = t };
			    }
			}
			""";
	private static final String IMPORTER = """
			import shared.Library;
			transformation importer(source : bookshelf, target : bookshelf) {
			}
			""";

	private EClass bookClass;
	private EPackage.Registry registry;
	private UnitStore store;
	private QvtdEngine engine;
	private QvtdEngine bare;

	@BeforeEach
	void setUp() {
		EPackage bookshelf = EcoreFactory.eINSTANCE.createEPackage();
		bookshelf.setName("bookshelf");
		bookshelf.setNsURI(NS_URI);
		bookshelf.setNsPrefix("bookshelf");
		bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(title);
		bookshelf.getEClassifiers().add(bookClass);
		registry = new EPackageRegistryImpl();
		registry.put(NS_URI, bookshelf);
		store = new DefaultUnitStore(new InMemoryUnitStoreBackend(), registry);
		OclConfiguration ocl = OclConfiguration.builder(new OclParserSupport()).build();
		engine = QvtdEngines.create(QvtdConfiguration.builder(ocl).packageRegistry(registry)
				.addUnitResolver(new QvtdStoreUnitResolver(store)).unitResolverEnabled(true).build());
		bare = QvtdEngines.create(QvtdConfiguration.builder(ocl).packageRegistry(registry).build());
	}

	@Test
	void pinnedFromTheStore_roundTrips_andRunsWhereTheStoreIs() throws Exception {
		UnitKey libraryKey = store.store("qvtr", new PackagedUnit(bare.compile(LIBRARY, "shared.Library")));
		CompiledUnit importer = engine.compile(IMPORTER, "importer");
		assertEquals(libraryKey.fingerprint().orElseThrow(),
				importer.getManifest().getDependencyEntry().get(0).getFingerprint());

		PackagedUnit loaded = (PackagedUnit) store.load(store.store("qvtr", new PackagedUnit(importer))).orElseThrow();
		assertEquals("Moby Dick", runOn(engine, (RelationalTransformation) loaded.document().getUnit()));
	}

	@Test
	void embeddedFromTheStore_runsWithoutTheStore() throws Exception {
		store.store("qvtr", new PackagedUnit(bare.compile(LIBRARY, "shared.Library")));
		CompiledUnit importer = engine.compile(IMPORTER, "importer", UnitCompileOptions.of(DependencyMode.EMBED));
		PackagedUnit loaded = (PackagedUnit) store.load(store.store("qvtr", new PackagedUnit(importer))).orElseThrow();
		assertEquals("Moby Dick", runOn(bare, (RelationalTransformation) loaded.document().getUnit()));
	}

	private String runOn(QvtdEngine target, RelationalTransformation transformation) {
		EObject book = EcoreUtil.create(bookClass);
		book.eSet(bookClass.getEStructuralFeature("title"), "Moby Dick");
		QvtdModelExtent targetExtent = QvtdModelExtent.of();
		QvtdExecutionResult result = target.execute(transformation, QvtdExecutionContext.enforce("target",
				Map.of("source", QvtdModelExtent.of(book), "target", targetExtent)));
		assertTrue(result.isSuccess(), () -> "execution failed: " + result.diagnostics());
		assertEquals(1, targetExtent.getContents().size(), "one Book produced");
		EObject produced = targetExtent.getContents().get(0);
		return (String) produced.eGet(produced.eClass().getEStructuralFeature("title"));
	}
}
