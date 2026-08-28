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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.model.ocl.Constraint;
import org.eclipse.fennec.m2x.model.ocl.ConstraintKind;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for Complete OCL {@code def:} expression runtime support (S-02).
 *
 * <p>Tests that def-properties and def-operations defined in Complete OCL
 * documents are available during expression evaluation.
 */
class OclDefExpressionTest {

	static OclEngine engine;
	static EcoreHelper ecoreHelper;
	static EPackage companyPackage;
	static EClass personClass;
	static EClass companyClass;

	@BeforeAll
	static void setUp() throws IOException {
		engine = OclEngines.create(new OclParserSupport());
		ecoreHelper = new EcoreHelper(OclDefExpressionTest.class);
		companyPackage = ecoreHelper.loadEcore("company.ecore");
		personClass = (EClass) companyPackage.getEClassifier("Person");
		companyClass = (EClass) companyPackage.getEClassifier("Company");
	}

	@AfterAll
	static void tearDown() {
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
	}

	private EObject createPerson(String name, int age) {
		EObject person = companyPackage.getEFactoryInstance().create(personClass);
		person.eSet(personClass.getEStructuralFeature("name"), name);
		person.eSet(personClass.getEStructuralFeature("age"), age);
		return person;
	}

	private EObject createPerson(String name, int age, double salary) {
		EObject person = createPerson(name, age);
		person.eSet(personClass.getEStructuralFeature("salary"), salary);
		return person;
	}

	// ==================== Parser: Feature Name Extraction ====================

	@Test
	void defConstraint_featureNameExtracted() throws OclParseException {
		String doc = """
				package company
				context Person
				  def : isAdult : Boolean = self.age >= 18
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(1, constraints.size());
		Constraint def = constraints.get(0);
		assertEquals(ConstraintKind.DEF, def.getKind());
		assertEquals("isAdult", def.getName());
		assertNotNull(def.getSpecification());
		assertNotNull(def.getContextClassifier());
	}

	@Test
	void defConstraint_withLabel_featureNameIsLast() throws OclParseException {
		String doc = """
				package company
				context Person
				  def myLabel : isAdult : Boolean = self.age >= 18
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(1, constraints.size());
		// Feature name should be "isAdult", not "myLabel"
		assertEquals("isAdult", constraints.get(0).getName());
	}

	// ==================== Def-Property: Evaluation ====================

	@Test
	void defProperty_booleanExpression() throws OclParseException {
		OclEngine eng = OclEngines.create(new OclParserSupport());
		eng.loadDocument("""
				package company
				context Person
				  def : isAdult : Boolean = self.age >= 18
				endpackage
				""");
		EObject adult = createPerson("Alice", 25);
		EObject child = createPerson("Bob", 10);

		Object resultAdult = eng.evaluate("self.isAdult", OclContext.of(adult));
		assertEquals(true, resultAdult);

		Object resultChild = eng.evaluate("self.isAdult", OclContext.of(child));
		assertEquals(false, resultChild);
	}

	@Test
	void defProperty_stringConcat() throws OclParseException {
		OclEngine eng = OclEngines.create(new OclParserSupport());
		eng.loadDocument("""
				package company
				context Person
				  def : greeting : String = 'Hello, '.concat(self.name)
				endpackage
				""");
		EObject person = createPerson("Alice", 30);

		Object result = eng.evaluate("self.greeting", OclContext.of(person));
		assertEquals("Hello, Alice", result);
	}

	@Test
	void defProperty_arithmeticExpression() throws OclParseException {
		OclEngine eng = OclEngines.create(new OclParserSupport());
		eng.loadDocument("""
				package company
				context Person
				  def : birthYear : Integer = 2026 - self.age
				endpackage
				""");
		EObject person = createPerson("Alice", 30);

		Object result = eng.evaluate("self.birthYear", OclContext.of(person));
		assertEquals(1996, result);
	}

	@Test
	void defProperty_usedInInvariant() throws OclParseException {
		OclEngine eng = OclEngines.create(new OclParserSupport());
		List<Constraint> constraints = eng.loadDocument("""
				package company
				context Person
				  def : isAdult : Boolean = self.age >= 18
				  inv adultCheck: self.isAdult
				endpackage
				""");

		// Find the invariant
		Constraint inv = constraints.stream()
				.filter(c -> c.getKind() == ConstraintKind.INV)
				.findFirst().orElseThrow();

		EObject adult = createPerson("Alice", 25);
		Object result = eng.evaluate(inv.getSpecification(), OclContext.of(adult));
		assertEquals(true, result);

		EObject child = createPerson("Bob", 10);
		result = eng.evaluate(inv.getSpecification(), OclContext.of(child));
		assertEquals(false, result);
	}

	@Test
	void defProperty_chainedDefs() throws OclParseException {
		OclEngine eng = OclEngines.create(new OclParserSupport());
		eng.loadDocument("""
				package company
				context Person
				  def : isAdult : Boolean = self.age >= 18
				  def : isWorkingAdult : Boolean = self.isAdult and self.salary > 0.0
				endpackage
				""");
		EObject workingAdult = createPerson("Alice", 30, 5000.0);
		EObject unemployedAdult = createPerson("Bob", 25, 0.0);
		EObject child = createPerson("Charlie", 10, 0.0);

		assertEquals(true, eng.evaluate("self.isWorkingAdult", OclContext.of(workingAdult)));
		assertEquals(false, eng.evaluate("self.isWorkingAdult", OclContext.of(unemployedAdult)));
		assertEquals(false, eng.evaluate("self.isWorkingAdult", OclContext.of(child)));
	}

	@Test
	void defProperty_realPropertyNotOverridden() throws OclParseException {
		OclEngine eng = OclEngines.create(new OclParserSupport());
		eng.loadDocument("""
				package company
				context Person
				  def : name : String = 'OVERRIDDEN'
				endpackage
				""");
		EObject person = createPerson("Alice", 30);

		// Real EAttribute 'name' should take precedence over def
		Object result = eng.evaluate("self.name", OclContext.of(person));
		assertEquals("Alice", result);
	}

	@Test
	void defProperty_multipleContexts() throws OclParseException {
		OclEngine eng = OclEngines.create(new OclParserSupport());
		eng.loadDocument("""
				package company
				context Person
				  def : isAdult : Boolean = self.age >= 18

				context Company
				  def : headcount : Integer = self.employees->size()
				endpackage
				""");

		EObject person = createPerson("Alice", 25);
		assertEquals(true, eng.evaluate("self.isAdult", OclContext.of(person)));

		EObject company = companyPackage.getEFactoryInstance().create(companyClass);
		company.eSet(companyClass.getEStructuralFeature("name"), "ACME");
		assertEquals(0, eng.evaluate("self.headcount", OclContext.of(company)));
	}

	// ==================== Def-Operation: Evaluation ====================

	@Test
	void defOperation_withParameter() throws OclParseException {
		OclEngine eng = OclEngines.create(new OclParserSupport());
		eng.loadDocument("""
				package company
				context Person
				  def : isOlderThan(threshold : Integer) : Boolean = self.age > threshold
				endpackage
				""");
		EObject person = createPerson("Alice", 30);

		assertEquals(true, eng.evaluate("self.isOlderThan(20)", OclContext.of(person)));
		assertEquals(false, eng.evaluate("self.isOlderThan(40)", OclContext.of(person)));
	}

	@Test
	void defOperation_noParameters() throws OclParseException {
		OclEngine eng = OclEngines.create(new OclParserSupport());
		eng.loadDocument("""
				package company
				context Person
				  def : doubleAge() : Integer = self.age * 2
				endpackage
				""");
		EObject person = createPerson("Alice", 20);

		assertEquals(40, eng.evaluate("self.doubleAge()", OclContext.of(person)));
	}

	// ==================== loadDocument returns constraints ====================

	@Test
	void loadDocument_returnsAllConstraints() throws OclParseException {
		OclEngine eng = OclEngines.create(new OclParserSupport());
		List<Constraint> constraints = eng.loadDocument("""
				package company
				context Person
				  def : isAdult : Boolean = self.age >= 18
				  inv agePositive: self.age >= 0
				endpackage
				""");
		assertEquals(2, constraints.size());
		assertTrue(constraints.stream().anyMatch(c -> c.getKind() == ConstraintKind.DEF));
		assertTrue(constraints.stream().anyMatch(c -> c.getKind() == ConstraintKind.INV));
	}

	// ==================== Static Def: Parsing ====================

	@Test
	void staticDefPropertyIsParsed() throws OclParseException {
		String doc = """
				package company
				context Person
				  static def : foo : Integer = 42
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(1, constraints.size());
		Constraint def = constraints.get(0);
		assertEquals(ConstraintKind.DEF, def.getKind());
		assertEquals("foo", def.getName());
		assertTrue(def.isIsStatic());
	}

	@Test
	void staticDefOperationIsParsed() throws OclParseException {
		String doc = """
				package company
				context Person
				  static def : bar() : Integer = 42
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(1, constraints.size());
		Constraint def = constraints.get(0);
		assertEquals(ConstraintKind.DEF, def.getKind());
		assertEquals("bar", def.getName());
		assertTrue(def.isIsStatic());
	}

	@Test
	void nonStaticDefHasIsStaticFalse() throws OclParseException {
		String doc = """
				package company
				context Person
				  def : isAdult : Boolean = self.age >= 18
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(1, constraints.size());
		assertFalse(constraints.get(0).isIsStatic());
	}

	// ==================== Static Def: Evaluation ====================

	@Test
	void staticDefPropertyIsEvaluated() throws OclParseException {
		OclEngine eng = OclEngines.create(new OclParserSupport());
		eng.loadDocument("""
				package company
				context Person
				  static def : magicNumber : Integer = 42
				endpackage
				""");
		EObject person = createPerson("Alice", 25);

		Object result = eng.evaluate("self.magicNumber", OclContext.of(person));
		assertEquals(42, result);
	}

	@Test
	void staticDefOperationIsEvaluated() throws OclParseException {
		OclEngine eng = OclEngines.create(new OclParserSupport());
		eng.loadDocument("""
				package company
				context Person
				  static def : doubleVal(x : Integer) : Integer = x * 2
				endpackage
				""");
		EObject person = createPerson("Alice", 25);

		Object result = eng.evaluate("self.doubleVal(21)", OclContext.of(person));
		assertEquals(42, result);
	}
}
