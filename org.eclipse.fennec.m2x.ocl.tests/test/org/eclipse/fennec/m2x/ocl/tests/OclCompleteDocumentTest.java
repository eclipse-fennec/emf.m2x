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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.fennec.m2x.model.ocl.Constraint;
import org.eclipse.fennec.m2x.model.ocl.ConstraintKind;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for Complete OCL document parsing (GAP-1).
 */
class OclCompleteDocumentTest {

	static OclEngine engine;
	static EcoreHelper ecoreHelper;
	static EPackage companyPackage;

	@BeforeAll
	static void setUp() throws IOException {
		engine = OclEngines.create(new OclParserSupport());
		ecoreHelper = new EcoreHelper(OclCompleteDocumentTest.class);
		companyPackage = ecoreHelper.loadEcore("company.ecore");
	}

	@AfterAll
	static void tearDown() {
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
	}

	@Test
	void invariantWithPackageWrapper() throws OclParseException {
		String doc = """
				package company
				context Person
				  inv nameNotEmpty: self.name.size() > 0
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(1, constraints.size());
		Constraint inv = constraints.get(0);
		assertEquals(ConstraintKind.INV, inv.getKind());
		assertEquals("nameNotEmpty", inv.getName());
		assertNotNull(inv.getContextClassifier());
		assertEquals("Person", inv.getContextClassifier().getName());
		assertNotNull(inv.getSpecification());
	}

	@Test
	void invariantWithoutName() throws OclParseException {
		String doc = """
				package company
				context Person
				  inv : self.age >= 0
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(1, constraints.size());
		Constraint inv = constraints.get(0);
		assertEquals(ConstraintKind.INV, inv.getKind());
		assertNull(inv.getName());
		assertNotNull(inv.getSpecification());
	}

	@Test
	void multipleInvariants() throws OclParseException {
		String doc = """
				package company
				context Person
				  inv agePositive: self.age >= 0
				  inv nameSet: self.name.size() > 0
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(2, constraints.size());
		assertEquals(ConstraintKind.INV, constraints.get(0).getKind());
		assertEquals("agePositive", constraints.get(0).getName());
		assertEquals(ConstraintKind.INV, constraints.get(1).getKind());
		assertEquals("nameSet", constraints.get(1).getName());
		// Both should reference Person
		assertEquals(constraints.get(0).getContextClassifier(),
				constraints.get(1).getContextClassifier());
	}

	@Test
	void definitionConstraint() throws OclParseException {
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
		assertNotNull(def.getSpecification());
		assertNotNull(def.getContextClassifier());
	}

	@Test
	void topLevelContextWithoutPackage() throws OclParseException {
		String doc = """
				context Person
				  inv ageCheck: self.age >= 0
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(1, constraints.size());
		assertEquals(ConstraintKind.INV, constraints.get(0).getKind());
		assertEquals("Person", constraints.get(0).getContextClassifier().getName());
	}

	@Test
	void propertyContextInitAndDerive() throws OclParseException {
		String doc = """
				package company
				context Person::salary : Real
				  init : 0.0
				  derive : self.age * 1000.0
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(2, constraints.size());

		Constraint init = constraints.get(0);
		assertEquals(ConstraintKind.INIT, init.getKind());
		assertNotNull(init.getContextProperty());
		assertEquals("salary", init.getContextProperty().getName());
		assertNotNull(init.getContextClassifier());

		Constraint derive = constraints.get(1);
		assertEquals(ConstraintKind.DERIVE, derive.getKind());
		assertNotNull(derive.getContextProperty());
		assertEquals("salary", derive.getContextProperty().getName());
	}

	@Test
	void importSkippedWithContext() throws OclParseException {
		String doc = """
				import company : company
				context Person
				  inv : self.age >= 0
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(1, constraints.size());
		assertEquals(ConstraintKind.INV, constraints.get(0).getKind());
	}

	@Test
	void unresolvedClassifierSkipped() throws OclParseException {
		String doc = """
				context NonExistent
				  inv : self.name.size() > 0
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(0, constraints.size());
	}

	@Test
	void syntaxErrorThrowsParseException() {
		String doc = """
				context Person
				  inv : self.age >=
				""";
		assertThrows(OclParseException.class, () -> engine.parseDocument(doc));
	}

	@Test
	void resourceSetOverload() throws OclParseException {
		// Remove package from global registry temporarily
		String nsURI = companyPackage.getNsURI();
		Object saved = EPackage.Registry.INSTANCE.remove(nsURI);
		try {
			// Without global registry, unresolved
			List<Constraint> withoutRs = engine.parseDocument(
					"context Person inv : self.age >= 0");
			assertEquals(0, withoutRs.size());

			// With local ResourceSet registry, resolved
			ResourceSet rs = new ResourceSetImpl();
			rs.getPackageRegistry().put(nsURI, companyPackage);
			List<Constraint> withRs = engine.parseDocument(
					"context Person inv : self.age >= 0", rs);
			assertEquals(1, withRs.size());
			assertEquals("Person", withRs.get(0).getContextClassifier().getName());
		} finally {
			// Restore
			if (saved != null) {
				EPackage.Registry.INSTANCE.put(nsURI, saved);
			}
		}
	}

	@Test
	void emptyDocument() throws OclParseException {
		List<Constraint> constraints = engine.parseDocument("");
		assertEquals(0, constraints.size());
	}

	// ==================== Operation Context ====================

	@Test
	void operationContext_bodyExpression() throws OclParseException {
		String doc = """
				package company
				context Person::getAge() : Integer
				  body : self.age
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(1, constraints.size());
		Constraint body = constraints.get(0);
		assertEquals(ConstraintKind.BODY, body.getKind());
		assertNotNull(body.getSpecification());
		assertNotNull(body.getContextClassifier());
		assertEquals("Person", body.getContextClassifier().getName());
	}

	@Test
	void operationContext_preCondition() throws OclParseException {
		String doc = """
				package company
				context Person::setAge(newAge : Integer) : Integer
				  pre validAge: newAge >= 0
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(1, constraints.size());
		Constraint pre = constraints.get(0);
		assertEquals(ConstraintKind.PRE, pre.getKind());
		assertEquals("validAge", pre.getName());
		assertNotNull(pre.getSpecification());
		assertNotNull(pre.getContextClassifier());
	}

	@Test
	void operationContext_postCondition() throws OclParseException {
		String doc = """
				package company
				context Person::setAge(newAge : Integer) : Integer
				  post ageUpdated: self.age = newAge
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(1, constraints.size());
		Constraint post = constraints.get(0);
		assertEquals(ConstraintKind.POST, post.getKind());
		assertEquals("ageUpdated", post.getName());
		assertNotNull(post.getSpecification());
		assertNotNull(post.getContextClassifier());
	}

	@Test
	void operationContext_preAndPostAndBody() throws OclParseException {
		String doc = """
				package company
				context Person::setAge(newAge : Integer) : Integer
				  pre validAge: newAge >= 0
				  body : self.age
				  post ageSet: self.age = newAge
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(3, constraints.size());

		Constraint pre = constraints.get(0);
		assertEquals(ConstraintKind.PRE, pre.getKind());
		assertEquals("validAge", pre.getName());

		Constraint body = constraints.get(1);
		assertEquals(ConstraintKind.BODY, body.getKind());

		Constraint post = constraints.get(2);
		assertEquals(ConstraintKind.POST, post.getKind());
		assertEquals("ageSet", post.getName());

		// All reference the same classifier
		assertEquals(pre.getContextClassifier(), body.getContextClassifier());
		assertEquals(body.getContextClassifier(), post.getContextClassifier());
	}

	@Test
	void operationContext_withoutName() throws OclParseException {
		String doc = """
				package company
				context Person::setAge(newAge : Integer) : Integer
				  pre : newAge >= 0
				  post : self.age >= 0
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(2, constraints.size());
		assertNull(constraints.get(0).getName());
		assertNull(constraints.get(1).getName());
		assertEquals(ConstraintKind.PRE, constraints.get(0).getKind());
		assertEquals(ConstraintKind.POST, constraints.get(1).getKind());
	}

	@Test
	void operationContext_noParams() throws OclParseException {
		String doc = """
				package company
				context Person::getAge() : Integer
				  body : self.age
				  post positiveResult: result >= 0
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(2, constraints.size());
		assertEquals(ConstraintKind.BODY, constraints.get(0).getKind());
		assertEquals(ConstraintKind.POST, constraints.get(1).getKind());
		assertEquals("positiveResult", constraints.get(1).getName());
	}

	@Test
	void operationContext_multipleParams() throws OclParseException {
		String doc = """
				package company
				context Person::transfer(target : Person, amount : Real) : Boolean
				  pre validAmount: amount > 0.0
				  pre hasFunds: self.salary >= amount
				  post salaryReduced: self.salary < self.salary
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(3, constraints.size());
		assertEquals(ConstraintKind.PRE, constraints.get(0).getKind());
		assertEquals("validAmount", constraints.get(0).getName());
		assertEquals(ConstraintKind.PRE, constraints.get(1).getKind());
		assertEquals("hasFunds", constraints.get(1).getName());
		assertEquals(ConstraintKind.POST, constraints.get(2).getKind());
		assertEquals("salaryReduced", constraints.get(2).getName());
	}

	@Test
	void operationContext_topLevelWithoutPackage() throws OclParseException {
		String doc = """
				context Person::getAge() : Integer
				  body : self.age
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(1, constraints.size());
		assertEquals(ConstraintKind.BODY, constraints.get(0).getKind());
		assertEquals("Person", constraints.get(0).getContextClassifier().getName());
	}

	@Test
	void operationContext_unresolvedClassifier() throws OclParseException {
		String doc = """
				context NonExistent::doSomething() : Boolean
				  pre : true
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(0, constraints.size());
	}

	@Test
	void mixedClassifierAndOperationContext() throws OclParseException {
		String doc = """
				package company
				context Person
				  inv agePositive: self.age >= 0

				context Person::getAge() : Integer
				  body : self.age
				  post : result >= 0
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(3, constraints.size());
		assertEquals(ConstraintKind.INV, constraints.get(0).getKind());
		assertEquals(ConstraintKind.BODY, constraints.get(1).getKind());
		assertEquals(ConstraintKind.POST, constraints.get(2).getKind());
	}
}
