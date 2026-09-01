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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.model.qvtbase.Domain;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.BasicQvtdBlackboxRegistry;
import org.eclipse.fennec.m2x.qvtd.api.QvtdBlackboxLibrary;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitPrepareException;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.api.UnitMaterializeException;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A manipulated QVT-R unit is rejected when it is loaded or prepared (#176, part of #188).
 *
 * <p>The third of the three: `QvtoLoadValidationTest` had these checks, M2T got them with
 * #177, and QVT-R had none while the security analysis lists them (M-13) as the load-time
 * defence. A stored unit is executed without being parsed again, so what a store hands back
 * is input.
 */
class QvtdLoadValidationTest {

	private static final String NS_URI = "http://example.org/m2x/qvtr-validate/1.0";

	private static final String WITH_BLACKBOX = """
			transformation boxed(source : bookshelf, target : bookshelf) {
			    query Shout(s : String) : String;
			    top relation CopyTitle {
			        t : String;
			        checkonly domain source b1 : Book { title = t };
			        enforce domain target b2 : Book { title = Shout(t) };
			    }
			}
			""";

	private static final String TRANSFORMATION = """
			transformation copy(source : bookshelf, target : bookshelf) {
			    top relation CopyTitle {
			        t : String;
			        checkonly domain source b1 : Book { title = t };
			        enforce domain target b2 : Book { title = t };
			    }
			}
			""";

	private EPackage.Registry registry;
	private QvtdEngine engine;

	@BeforeEach
	void setUp() {
		EPackage bookshelf = EcoreFactory.eINSTANCE.createEPackage();
		bookshelf.setName("bookshelf");
		bookshelf.setNsURI(NS_URI);
		bookshelf.setNsPrefix("bookshelf");
		EClass bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(title);
		bookshelf.getEClassifiers().add(bookClass);
		registry = new EPackageRegistryImpl();
		registry.put(NS_URI, bookshelf);
		engine = QvtdEngines.create(QvtdConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build())
				.packageRegistry(registry).build());
	}

	@Test
	@DisplayName("a unit of another language under the qvtr tag is rejected")
	void aUnitOfAnotherLanguage_isRejected() throws Exception {
		CompiledUnit compiled = engine.compile(TRANSFORMATION, "copy");
		compiled.setUnit(EcoreFactory.eINSTANCE.createEPackage());

		UnitPrepareException failure = assertThrows(UnitPrepareException.class,
				() -> engine.unitBinder().validate(compiled));

		assertTrue(failure.getMessage().contains("declared a QVT-R unit"), failure::getMessage);
	}

	@Test
	@DisplayName("a domain that names no typed model is rejected")
	void aDomainWithoutATypedModel_isRejected() throws Exception {
		// The parser binds every domain to one of the transformation's typed models, so a domain
		// without one did not come from a parser
		CompiledUnit compiled = engine.compile(TRANSFORMATION, "copy");
		firstDomain(compiled).setTypedModel(null);

		UnitPrepareException failure = assertThrows(UnitPrepareException.class,
				() -> engine.unitBinder().validate(compiled));

		assertTrue(failure.getMessage().contains("names no typed model"), failure::getMessage);
	}

	@Test
	@DisplayName("a domain of a typed model this transformation does not declare is rejected")
	void aDomainOfAnUndeclaredTypedModel_isRejected() throws Exception {
		// A domain pointing at a typed model from somewhere else would read and write extents
		// this transformation never declared
		CompiledUnit compiled = engine.compile(TRANSFORMATION, "copy");
		RelationalTransformation other = (RelationalTransformation) engine
				.compile(TRANSFORMATION, "other").getUnit();
		firstDomain(compiled).setTypedModel(other.getModelParameter().get(0));

		UnitPrepareException failure = assertThrows(UnitPrepareException.class,
				() -> engine.unitBinder().validate(compiled));

		assertTrue(failure.getMessage().contains("does not declare"), failure::getMessage);
	}

	@Test
	@DisplayName("a variable use whose declaration left the document is rejected")
	void aVariableUseWhoseDeclarationLeftTheDocument_isRejected() throws Exception {
		CompiledUnit compiled = engine.compile(TRANSFORMATION, "copy");
		VariableExp use = firstVariableUse(compiled);
		Variable elsewhere = OclFactory.eINSTANCE.createVariable();
		elsewhere.setName("t");
		use.setReferredVariable(elsewhere);

		UnitPrepareException failure = assertThrows(UnitPrepareException.class,
				() -> engine.unitBinder().validate(compiled));

		assertTrue(failure.getMessage().contains("declared outside the document"),
				failure::getMessage);
	}

	@Test
	@DisplayName("a tampered document is rejected by the materializer, before prepare hands it on")
	void aTamperedDocument_isRejectedByTheStore() throws Exception {
		InMemoryUnitStoreBackend backend = new InMemoryUnitStoreBackend();
		UnitStore store = new DefaultUnitStore(backend);
		UnitKey key = store.put(engine.compile(TRANSFORMATION, "copy"));

		byte[] bytes = backend.get(key).orElseThrow();
		String original = new String(bytes, StandardCharsets.UTF_8);
		String tampered = original.replace("CopyTitle", "CopyTitl3");
		assertTrue(!tampered.equals(original), "the tampering changed something");
		backend.put(key, tampered.getBytes(StandardCharsets.UTF_8));

		UnitPreparer preparer = UnitPreparer.withDefaults(store, engine.unitBinder());
		UnitPrepareException failure = assertThrows(UnitPrepareException.class,
				() -> preparer.prepare(key));

		assertTrue(failure.getCause() instanceof UnitMaterializeException, String.valueOf(failure.getCause()));
		assertTrue(failure.getMessage().contains("changed after it was sealed"), failure::getMessage);
	}

	// ==== the blackbox gate at prepare time (#176) ====

	@Test
	@DisplayName("a unit requiring a blackbox is refused where blackboxes are off")
	void aBlackboxRequirement_isRefusedWhereBlackboxesAreOff() throws Exception {
		// The requirement was recorded when the unit was compiled. A runtime that does not offer
		// blackboxes at all must say so at prepare rather than at the call, which would be
		// mid-transformation and after the target model has been half written
		CompiledUnit compiled = engine.compile(WITH_BLACKBOX, "boxed");

		UnitPrepareException failure = assertThrows(UnitPrepareException.class,
				() -> engine.unitBinder().verifyBlackboxes(compiled));

		assertTrue(failure.getMessage().contains("Shout"), failure::getMessage);
		assertTrue(failure.getMessage().contains("not enabled"), failure::getMessage);
	}

	@Test
	@DisplayName("a unit requiring a blackbox no library serves is refused")
	void aBlackboxRequirement_isRefusedWhereNoLibraryServesIt() throws Exception {
		CompiledUnit compiled = engine.compile(WITH_BLACKBOX, "boxed");
		QvtdEngine withOtherLibrary = engineWith(library("helpers", "SomethingElse"));

		UnitPrepareException failure = assertThrows(UnitPrepareException.class,
				() -> withOtherLibrary.unitBinder().verifyBlackboxes(compiled));

		assertTrue(failure.getMessage().contains("Shout"), failure::getMessage);
	}

	@Test
	@DisplayName("a unit requiring a blackbox a library declares is accepted")
	void aBlackboxRequirement_isAcceptedWhereALibraryServesIt() throws Exception {
		CompiledUnit compiled = engine.compile(WITH_BLACKBOX, "boxed");
		QvtdEngine withTheLibrary = engineWith(library("helpers", "Shout"));

		assertDoesNotThrow(() -> withTheLibrary.unitBinder().verifyBlackboxes(compiled),
				"this is what the two refusals are being compared against");
	}

	private QvtdEngine engineWith(QvtdBlackboxLibrary library) {
		BasicQvtdBlackboxRegistry blackboxes = new BasicQvtdBlackboxRegistry();
		blackboxes.register(library);
		return QvtdEngines.create(QvtdConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build())
				.packageRegistry(registry)
				.blackboxRegistry(blackboxes)
				.blackboxEnabled(true)
				.build());
	}

	private static QvtdBlackboxLibrary library(String moduleName, String operation) {
		return new QvtdBlackboxLibrary() {

			@Override
			public String getModuleName() {
				return moduleName;
			}

			@Override
			public String getUnitQualifiedName() {
				return moduleName;
			}

			@Override
			public java.util.List<String> getUsedPackageURIs() {
				return java.util.List.of();
			}

			@Override
			public java.util.List<String> getOperationNames() {
				return java.util.List.of(operation);
			}

			@Override
			public Object invoke(String operationName, Object self, Object[] args) {
				return "!";
			}
		};
	}

	private static Domain firstDomain(CompiledUnit compiled) {
		RelationalTransformation transformation = (RelationalTransformation) compiled.getUnit();
		return transformation.getRule().get(0).getDomain().get(0);
	}

	private static VariableExp firstVariableUse(CompiledUnit compiled) {
		for (Iterator<EObject> it = compiled.eAllContents(); it.hasNext();) {
			if (it.next() instanceof VariableExp use) {
				return use;
			}
		}
		throw new AssertionError("no variable use in the compiled transformation");
	}
}
