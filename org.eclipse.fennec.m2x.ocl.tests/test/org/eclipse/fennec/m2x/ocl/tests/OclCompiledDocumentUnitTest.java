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
package org.eclipse.fennec.m2x.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.UnitNature;
import org.eclipse.fennec.m2x.model.ocl.CompleteOclDocument;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitPrepareException;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Complete OCL documents as compiled units (#209): compiled once, stored, prepared — and then
 * <em>registered</em>, because a document is installed into an engine, never executed. The
 * runtime that registers needs no parser for the document.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class OclCompiledDocumentUnitTest {

	private static final String DOCUMENT = """
			package company
			context Person
			  def : isAdult : Boolean = self.age >= 18
			endpackage
			""";

	static OclEngine engine;
	static EcoreHelper ecoreHelper;
	static EPackage companyPackage;

	@BeforeAll
	static void setUp() throws IOException {
		engine = OclEngines.create(new OclParserSupport());
		ecoreHelper = new EcoreHelper(OclCompiledDocumentUnitTest.class);
		companyPackage = ecoreHelper.loadEcore("company.ecore");
	}

	@AfterAll
	static void tearDown() {
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
	}

	@Test
	void compileDocument_sealsALibraryUnitUnderTheOclTag() throws Exception {
		CompiledUnit unit = engine.compileDocument("company.rules", DOCUMENT);

		assertEquals("ocl", unit.getManifest().getLanguage());
		assertEquals("company.rules", unit.getManifest().getQualifiedName());
		assertEquals(UnitNature.LIBRARY, unit.getManifest().getNature(),
				"a document is installed, never started");
		assertTrue(unit.getManifest().getUnitFingerprint().startsWith("m2x1:"));
		assertEquals(List.of(), unit.getManifest().getDependencyEntry(), "no unit imports");
		CompleteOclDocument document = assertInstanceOf(CompleteOclDocument.class, unit.getUnit());
		assertEquals(1, document.getConstraints().size());
	}

	@Test
	void storedPreparedRegistered_theDefApplies_withoutParsingTheDocument() throws Exception {
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		UnitKey key = store.put(engine.compileDocument("company.rules", DOCUMENT));

		// The runtime side: its own engine, the store, the metamodel as a contribution —
		// and no document text anywhere
		OclEngine runtime = OclEngines.create(new OclParserSupport());
		PreparedContext prepared = new UnitPreparer(store, List.of(runtime.unitBinder()))
				.registerPackage(companyPackage).prepare(key);
		runtime.registerCompleteOclDocument(prepared, "company.rules");

		assertEquals(Boolean.TRUE, runtime.evaluate("self.isAdult", contextOf(21)));
		assertEquals(Boolean.FALSE, runtime.evaluate("self.isAdult", contextOf(12)));
	}

	@Test
	void theAstOverload_registersAndUnregisters() throws Exception {
		CompleteOclDocument document =
				(CompleteOclDocument) engine.compileDocument("company.rules", DOCUMENT).getUnit();
		OclEngine runtime = OclEngines.create(new OclParserSupport());

		runtime.registerCompleteOclDocument(document);
		assertEquals(Boolean.TRUE, runtime.evaluate("self.isAdult", contextOf(21)));

		runtime.unregisterCompleteOclDocument(document);
		Exception failure = assertThrows(Exception.class,
				() -> runtime.evaluate("self.isAdult", contextOf(21)),
				"what a registration put into effect, its removal takes back out");
		assertTrue(failure.getMessage().contains("isAdult"), failure.getMessage());
	}

	@Test
	void aPreparedContextWithoutTheName_isRefused() throws Exception {
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		store.put(engine.compileDocument("company.rules", DOCUMENT));
		PreparedContext prepared = new UnitPreparer(store, List.of(engine.unitBinder()))
				.registerPackage(companyPackage)
				.prepare(store.versions("ocl", "company.rules",
						org.eclipse.fennec.m2x.unit.api.UnitKind.COMPILED).get(0));

		IllegalArgumentException failure = assertThrows(IllegalArgumentException.class,
				() -> engine.registerCompleteOclDocument(prepared, "nobody.Home"));
		assertTrue(failure.getMessage().contains("nobody.Home"), failure.getMessage());
	}

	@Test
	void theBinder_refusesAForeignRoot() {
		CompiledUnit foreign = UnitPackager.compile("ocl", "not.Ocl", EcoreFactory.eINSTANCE.createEPackage());
		UnitPrepareException failure = assertThrows(UnitPrepareException.class,
				() -> engine.unitBinder().validate(foreign));
		assertTrue(failure.getMessage().contains("CompleteOclDocument"), failure.getMessage());
	}

	// ==== helpers ====

	private static OclContext contextOf(int age) {
		EClass person = (EClass) companyPackage.getEClassifier("Person");
		EObject instance = companyPackage.getEFactoryInstance().create(person);
		instance.eSet(person.getEStructuralFeature("age"), age);
		return OclContext.of(instance);
	}
}
