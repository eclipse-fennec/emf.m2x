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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.fennec.m2x.model.ocl.CompleteOclDocument;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclDocumentValidator;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * What a registered Complete OCL document puts into effect beyond {@code def:} (#204, decided
 * scope): {@code inv:} feeds the {@link OclDocumentValidator}, {@code derive:} and {@code body:}
 * become visible to OCL evaluation — and EMF's own {@code eGet}/{@code eInvoke} stay untouched,
 * that boundary belongs to the annotation-driven delegates.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class OclDocumentValidationTest {

	static OclEngine engine;
	static EcoreHelper ecoreHelper;
	static EPackage companyPackage;

	@BeforeAll
	static void setUp() throws IOException {
		engine = OclEngines.create(new OclParserSupport());
		ecoreHelper = new EcoreHelper(OclDocumentValidationTest.class);
		companyPackage = ecoreHelper.loadEcore("company.ecore");
	}

	@AfterAll
	static void tearDown() {
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
	}

	@Test
	void aDocumentInvariant_feedsTheValidator_andItsRemovalTakesItOut() throws Exception {
		OclEngine runtime = OclEngines.create(new OclParserSupport());
		CompleteOclDocument document = (CompleteOclDocument) runtime.compileDocument("company.rules", """
				package company
				context Person
				  inv agePositive: self.age >= 0
				endpackage
				""").getUnit();
		runtime.registerCompleteOclDocument(document);
		OclDocumentValidator validator = new OclDocumentValidator(runtime);

		BasicDiagnostic diagnostics = new BasicDiagnostic();
		assertFalse(validator.validate(person(-1), diagnostics, null));
		assertEquals(Diagnostic.ERROR, diagnostics.getSeverity());
		assertTrue(diagnostics.getChildren().get(0).getMessage().contains("agePositive"),
				diagnostics.getChildren().get(0).getMessage());
		assertTrue(validator.validate(person(21), new BasicDiagnostic(), null));

		runtime.unregisterCompleteOclDocument(document);
		assertTrue(validator.validate(person(-1), new BasicDiagnostic(), null),
				"what a registration put into effect, its removal takes back out");
	}

	@Test
	void aSupertypeInvariant_appliesToTheSubclass() throws Exception {
		EPackage shapes = shapes();
		OclEngine runtime = OclEngines.create(new OclParserSupport(registryOf(shapes)));
		runtime.loadDocument("""
				package shapes
				context Base
				  inv tagged: self.tag.size() > 0
				endpackage
				""");
		OclDocumentValidator validator = new OclDocumentValidator(runtime);

		EObject sub = instance(shapes, "Sub", "");
		assertFalse(validator.validate(sub, new BasicDiagnostic(), null),
				"the supertype's invariant reaches the subclass");
		assertTrue(validator.validate(instance(shapes, "Sub", "x"), new BasicDiagnostic(), null));
	}

	@Test
	void aDerivation_isVisibleToEvaluation_whileEGetStaysUntouched() throws Exception {
		OclEngine runtime = OclEngines.create(new OclParserSupport());
		runtime.loadDocument("""
				package company
				context Person::name : String
				  derive : 'dr ' + self.age.toString()
				endpackage
				""");

		EObject ada = person(48);
		ada.eSet(ada.eClass().getEStructuralFeature("name"), "ada");
		assertEquals("dr 48", runtime.evaluate("self.name", OclContext.of(ada)),
				"inside OCL evaluation the derivation wins");
		assertEquals("ada", ada.eGet(ada.eClass().getEStructuralFeature("name")),
				"EMF's eGet stays raw — that boundary belongs to the annotations");
	}

	@Test
	void anOperationBody_isCallableInEvaluation() throws Exception {
		EPackage shapes = shapes();
		OclEngine runtime = OclEngines.create(new OclParserSupport(registryOf(shapes)));
		runtime.loadDocument("""
				package shapes
				context Base::greet() : String
				  body : 'hi ' + self.tag
				endpackage
				""");

		assertEquals("hi x", runtime.evaluate("self.greet()", OclContext.of(instance(shapes, "Sub", "x"))));
	}

	// ==== helpers ====

	private static EObject person(int age) {
		EClass person = (EClass) companyPackage.getEClassifier("Person");
		EObject instance = companyPackage.getEFactoryInstance().create(person);
		instance.eSet(person.getEStructuralFeature("age"), age);
		return instance;
	}

	/** Base (tag : String, greet() : String) and Sub extends Base — inheritance and an operation. */
	private static EPackage shapes() {
		EPackage shapes = EcoreFactory.eINSTANCE.createEPackage();
		shapes.setName("shapes");
		shapes.setNsURI("http://example.org/m2x/document-validation/shapes/1.0");
		shapes.setNsPrefix("shapes");
		EClass base = EcoreFactory.eINSTANCE.createEClass();
		base.setName("Base");
		org.eclipse.emf.ecore.EAttribute tag = EcoreFactory.eINSTANCE.createEAttribute();
		tag.setName("tag");
		tag.setEType(EcorePackage.Literals.ESTRING);
		base.getEStructuralFeatures().add(tag);
		EOperation greet = EcoreFactory.eINSTANCE.createEOperation();
		greet.setName("greet");
		greet.setEType(EcorePackage.Literals.ESTRING);
		base.getEOperations().add(greet);
		EClass sub = EcoreFactory.eINSTANCE.createEClass();
		sub.setName("Sub");
		sub.getESuperTypes().add(base);
		shapes.getEClassifiers().add(base);
		shapes.getEClassifiers().add(sub);
		return shapes;
	}

	private static EObject instance(EPackage shapes, String className, String tag) {
		EClass eClass = (EClass) shapes.getEClassifier(className);
		EObject instance = shapes.getEFactoryInstance().create(eClass);
		instance.eSet(eClass.getEStructuralFeature("tag") == null
				? ((EClass) shapes.getEClassifier("Base")).getEStructuralFeature("tag")
				: eClass.getEStructuralFeature("tag"), tag);
		return instance;
	}

	private static org.eclipse.emf.ecore.EPackage.Registry registryOf(EPackage ePackage) {
		org.eclipse.emf.ecore.EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(ePackage.getNsURI(), ePackage);
		return registry;
	}
}
