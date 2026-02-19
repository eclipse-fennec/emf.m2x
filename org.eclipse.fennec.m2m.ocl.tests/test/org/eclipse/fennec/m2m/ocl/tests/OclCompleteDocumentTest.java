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
package org.eclipse.fennec.m2m.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.fennec.m2m.model.ocl.Constraint;
import org.eclipse.fennec.m2m.model.ocl.ConstraintKind;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.eclipse.fennec.m2m.ocl.engine.OclEngineImpl;
import org.eclipse.fennec.m2m.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2m.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for Complete OCL document parsing (GAP-1).
 */
class OclCompleteDocumentTest {

	static OclEngineImpl engine;
	static EcoreHelper ecoreHelper;
	static EPackage companyPackage;

	@BeforeAll
	static void setUp() throws IOException {
		engine = new OclEngineImpl(new OclParserSupport());
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
}
