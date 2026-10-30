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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.fennec.m2x.model.ocl.Constraint;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.OclResult;
import org.eclipse.fennec.m2x.ocl.engine.OclDocumentValidator;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Complete OCL documents and the EMF delegates against OCL v2.4 Chapter 12 (#323, #324).
 */
class OclCompleteOclConformanceTest {

	static OclEngine engine;
	static EcoreHelper ecoreHelper;
	static EPackage companyPackage;
	static EClass personClass;
	static EPackage shelfPackage;
	static EClass shelfClass;
	static EClass bookClass;

	@BeforeAll
	static void setUp() throws IOException {
		engine = OclEngines.create(new OclParserSupport());
		ecoreHelper = new EcoreHelper(OclCompleteOclConformanceTest.class);
		companyPackage = ecoreHelper.loadEcore("company.ecore");
		personClass = ecoreHelper.getEClass(companyPackage, "Person");
		shelfPackage = ecoreHelper.loadEcore("shelf.ecore");
		shelfClass = ecoreHelper.getEClass(shelfPackage, "Shelf");
		bookClass = ecoreHelper.getEClass(shelfPackage, "Book");
		engine.installDelegates();
	}

	@AfterAll
	static void tearDown() {
		engine.uninstallDelegates();
		ecoreHelper.releaseAll();
	}

	static EObject person(String name, int age) {
		EObject person = companyPackage.getEFactoryInstance().create(personClass);
		person.eSet(personClass.getEStructuralFeature("name"), name);
		person.eSet(personClass.getEStructuralFeature("age"), age);
		return person;
	}

	@SuppressWarnings("unchecked")
	static EObject shelf(String name, String... authors) {
		EObject shelf = shelfPackage.getEFactoryInstance().create(shelfClass);
		shelf.eSet(shelfClass.getEStructuralFeature("name"), name);
		List<EObject> books = (List<EObject>) shelf.eGet(shelfClass.getEStructuralFeature("books"));
		for (String author : authors) {
			EObject book = shelfPackage.getEFactoryInstance().create(bookClass);
			book.eSet(bookClass.getEStructuralFeature("author"), author);
			book.eSet(bookClass.getEStructuralFeature("title"), "by " + author);
			books.add(book);
		}
		return shelf;
	}

	static EOperation operation(String name) {
		return shelfClass.getEOperations().stream().filter(op -> name.equals(op.getName())).findFirst().orElseThrow();
	}

	static void assertParseError(String document, String fragment) {
		OclParseException failure = assertThrows(OclParseException.class, () -> engine.parseDocument(document));
		assertTrue(failure.getMessage().contains(fragment), failure::getMessage);
	}

	/** §12.12.5 [B]: context p : Person — p is self under another name. */
	@Nested
	class ContextVariable {

		@Test
		void explicitContextVariable_isBoundAtEvaluation() throws OclParseException {
			List<Constraint> constraints = engine.parseDocument("""
					context p : company::Person
					inv adult: p.age >= 18
					""");
			assertEquals(true, engine.evaluate(constraints.get(0).getSpecification(), OclContext.of(person("Ann", 30))));
			assertEquals(false, engine.evaluate(constraints.get(0).getSpecification(), OclContext.of(person("Kid", 7))));
		}
	}

	/** §12.2.1: parameters and result are variables of the expression. */
	@Nested
	class ParametersAndResult {

		@Test
		void parameter_winsOverAPropertyOfANamedIterator() throws InvocationTargetException {
			EObject shelf = shelf("S", "Melville", "Austen", "Melville");
			Object count = ((InternalEObject) shelf).eInvoke(operation("countBy"), ECollections.asEList("Melville"));
			assertEquals(2, count);
		}

		@Test
		void documentBody_seesItsParameters() throws OclParseException {
			List<Constraint> constraints = engine.parseDocument("""
					context company::Person::setAge(newAge : Integer) : Integer
					pre positive: newAge >= 0
					body: newAge
					post: result = newAge
					""");
			assertEquals(3, constraints.size());
		}

		@Test
		void parametersAreTypedVariables_inADefOperation() throws OclParseException {
			OclEngine local = OclEngines.create(new OclParserSupport());
			local.loadDocument("""
					context company::Person
					def: older(age : Integer) : Boolean = self.age > age
					def: older(a : Integer, b : Integer) : Boolean = self.age > a + b
					""");
			EObject ann = person("Ann", 30);
			assertEquals(true, local.evaluate("self.older(20)", OclContext.of(ann)));
			assertEquals(false, local.evaluate("self.older(20, 20)", OclContext.of(ann)));
		}
	}

	/** §12.7–§12.10: the operation or property a context declaration names must exist. */
	@Nested
	class UnknownContextTargets {

		@Test
		void unknownOperation_isAParseError() {
			assertParseError("""
					context company::Person::noSuchOp() : Integer
					body: 1
					""", "Unknown operation 'noSuchOp'");
		}

		@Test
		void unknownProperty_isAParseError() {
			assertParseError("""
					context company::Person::noSuchProp : Integer
					derive: 1
					""", "Unknown property 'noSuchProp'");
		}
	}

	/** §7.4.4: a definition may not repeat a feature of the class or another definition. */
	@Nested
	class DefinitionConflicts {

		@Test
		void defRepeatingAProperty_isAParseError() {
			assertParseError("""
					context company::Person
					def: name : String = 'x'
					""", "conflicts with the property Person::name");
		}

		@Test
		void defTwice_isAParseError() {
			assertParseError("""
					context company::Person
					def: x : Integer = 1
					def: x : Integer = 2
					""", "'x' is defined twice");
		}
	}

	/** §12.6.1 [1], A.3.1.5: an invariant must be true; anything else violates it. */
	@Nested
	class Invariants {

		@Test
		void validationDelegate_nonBooleanResult_isAViolation() {
			EObject shelf = shelf("S", "Austen");
			Diagnostic diagnostics = Diagnostician.INSTANCE.validate(shelf);
			assertTrue(diagnostics.getSeverity() >= Diagnostic.ERROR, diagnostics::toString);
		}

		@Test
		void documentValidator_nonBooleanResult_isAViolation() throws OclParseException {
			OclEngine local = OclEngines.create(new OclParserSupport());
			local.loadDocument("""
					context company::Person
					inv notBoolean: self.age
					""");
			BasicDiagnostic diagnostics = new BasicDiagnostic();
			assertFalse(new OclDocumentValidator(local).validate(person("Ann", 30), diagnostics, null));
		}
	}

	/** §7.5.14, §12.5.1 [3] …: @pre and oclIsNew() belong to postconditions. */
	@Nested
	class PreState {

		@Test
		void atPre_inAnInvariant_isAParseError() {
			assertParseError("""
					context company::Person
					inv noPre: self.age@pre > 0
					""", "@pre is allowed in postconditions only");
		}

		@Test
		void oclIsNew_inADef_isAParseError() {
			assertParseError("""
					context company::Person
					def: fresh : Boolean = self.oclIsNew()
					""", "oclIsNew() is allowed in postconditions only");
		}

		@Test
		void adHocAtPre_readsTheCurrentValue_withAWarning() throws OclParseException {
			OclResult result = engine.evaluateWithDiagnostics(engine.parse("self.age@pre", personClass),
					OclContext.of(person("Ann", 30)), engine.getDefaultOptions());
			assertEquals(30L, result.value());
			assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains("without a pre-state")));
		}

		@Test
		void adHocOclIsNew_isInvalidWithAnError() throws OclParseException {
			OclResult result = engine.evaluateWithDiagnostics(engine.parse("self.oclIsNew()", personClass),
					OclContext.of(person("Ann", 30)), engine.getDefaultOptions());
			assertSame(OclInvalid.INSTANCE, result.value());
		}

		@Test
		void atPre_onAnImplicitCollect_readsEachElement() throws InvocationTargetException {
			// post: self.books.title@pre = self.books.title — @pre inside the implicit collect
			// used to read null for every element and fail the postcondition (#323)
			EObject shelf = shelf("S", "Austen", "Melville");
			assertEquals(1, ((InternalEObject) shelf).eInvoke(operation("countBy"), ECollections.asEList("Austen")));
		}
	}

	/** §12.7: a document's pre- and postconditions are checked where the operation runs. */
	@Nested
	class DocumentConditions {

		@Test
		void documentPostcondition_isCheckedByTheInvocationDelegate() throws Exception {
			engine.loadDocument("""
					context shelf::Shelf::label() : String
					post longLabel: result.size() > 3
					""");
			EObject shortName = shelf("abc");
			InvocationTargetException failure = assertThrows(InvocationTargetException.class,
					() -> ((InternalEObject) shortName).eInvoke(operation("label"), null));
			assertTrue(String.valueOf(failure.getCause()).contains("Postcondition violated"), failure::toString);
			assertEquals("long enough", ((InternalEObject) shelf("long enough")).eInvoke(operation("label"), null));
		}
	}
}
