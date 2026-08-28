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

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.emf.osgi.fingerprint.util.FingerprintHelper;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnitResolver;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdStoreUnitResolver;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Execute a prepared QVT-R unit: the pinned import is merged at prepare time, no resolver is
 * asked at execute (#140).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtdPreparedExecutionTest {

	private static final String NS_URI = "http://example.org/m2x/qvtr-prepare/1.0";
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
	private QvtdEngine compiler;
	private QvtdEngine runner;

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
		compiler = QvtdEngines.create(QvtdConfiguration.builder(ocl).packageRegistry(registry)
				.addUnitResolver(new QvtdStoreUnitResolver(store)).unitResolverEnabled(true).build());
		QvtdUnitResolver forbidden = name -> {
			throw new AssertionError("Execute asked a resolver for '" + name + "'");
		};
		runner = QvtdEngines.create(QvtdConfiguration.builder(ocl).packageRegistry(registry)
				.addUnitResolver(forbidden).unitResolverEnabled(true).build());
	}

	@Test
	void preparedUnit_runsWithoutAskingAnyResolver() throws Exception {
		store.store("qvtr", new PackagedUnit(compiler.compile(LIBRARY, "shared.Library")));
		UnitKey key = store.store("qvtr", new PackagedUnit(compiler.compile(IMPORTER, "importer")));

		PreparedContext prepared = new UnitPreparer(store, registry, FingerprintHelper.getDefaultFingerprintService(),
				List.of(runner.unitBinder())).prepare(key);

		EObject book = EcoreUtil.create(bookClass);
		book.eSet(bookClass.getEStructuralFeature("title"), "Moby Dick");
		QvtdModelExtent target = QvtdModelExtent.of();
		QvtdExecutionResult result = runner.execute(prepared, "importer", QvtdExecutionContext.enforce("target",
				Map.of("source", QvtdModelExtent.of(book), "target", target)));
		assertTrue(result.isSuccess(), () -> "execution failed: " + result.diagnostics());
		assertEquals(1, target.getContents().size(), "the merged relation produced the Book");
		EObject produced = target.getContents().get(0);
		assertEquals("Moby Dick", produced.eGet(produced.eClass().getEStructuralFeature("title")));
	}
}
