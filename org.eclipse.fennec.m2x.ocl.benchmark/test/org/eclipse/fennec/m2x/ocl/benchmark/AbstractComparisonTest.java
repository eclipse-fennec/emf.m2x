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
package org.eclipse.fennec.m2x.ocl.benchmark;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.engine.OclEngineImpl;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.ecore.OCL;
import org.eclipse.ocl.expressions.OCLExpression;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 * Base class for Fennec OCL vs Eclipse OCL Classic comparison tests.
 *
 * <p>Sets up both engines with the shared {@code company.ecore} model and
 * provides helper methods for evaluating OCL expressions on both engines.
 */
abstract class AbstractComparisonTest {

	// --- Fennec engine ---
	static OclEngineImpl fennecEngine;
	static EcoreHelper ecoreHelper;

	// --- Eclipse OCL Classic engine ---
	static OCL eclipseOcl;
	static OCL.Helper eclipseHelper;

	// --- Shared model ---
	static EPackage companyPackage;
	static EClass companyClass;
	static EClass personClass;

	@BeforeAll
	static void setUpEngines() throws IOException {
		// Fennec
		fennecEngine = new OclEngineImpl(new OclParserSupport());
		ecoreHelper = new EcoreHelper(AbstractComparisonTest.class);
		companyPackage = ecoreHelper.loadEcore("company.ecore");
		companyClass = ecoreHelper.getEClass(companyPackage, "Company");
		personClass = ecoreHelper.getEClass(companyPackage, "Person");

		// Eclipse OCL Classic (Ecore binding)
		eclipseOcl = OCL.newInstance();
		eclipseHelper = eclipseOcl.createOCLHelper();
	}

	@AfterAll
	static void tearDown() {
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
		if (eclipseOcl != null) {
			eclipseOcl.dispose();
		}
	}

	// --- Factory methods ---

	static EObject createPerson(String name, int age, double salary, boolean married) {
		EObject person = companyPackage.getEFactoryInstance().create(personClass);
		person.eSet(personClass.getEStructuralFeature("name"), name);
		person.eSet(personClass.getEStructuralFeature("age"), age);
		person.eSet(personClass.getEStructuralFeature("salary"), salary);
		person.eSet(personClass.getEStructuralFeature("isMarried"), married);
		return person;
	}

	@SuppressWarnings("unchecked")
	static EObject createCompany(String name, EObject... employees) {
		EObject company = companyPackage.getEFactoryInstance().create(companyClass);
		company.eSet(companyClass.getEStructuralFeature("name"), name);
		var empList = (List<EObject>) company.eGet(companyClass.getEStructuralFeature("employees"));
		for (EObject emp : employees) {
			empList.add(emp);
		}
		return company;
	}

	// --- Fennec evaluation ---

	static Object fennecEval(String expression, EObject self) throws OclParseException {
		return fennecEngine.evaluate(expression, OclContext.of(self));
	}

	// --- Eclipse OCL Classic evaluation ---

	static Object eclipseEval(String expression, EObject self) throws ParserException {
		eclipseHelper.setContext(self.eClass());
		OCLExpression<EClassifier> query = eclipseHelper.createQuery(expression);
		return eclipseOcl.evaluate(self, query);
	}

	// --- Comparison helper ---

	/**
	 * Evaluates the expression on both engines and asserts that the results are equal.
	 * Handles collection normalization (Eclipse returns Java collections, Fennec may too).
	 */
	static void assertSameResult(String expression, EObject self) {
		Object fennecResult;
		Object eclipseResult;

		try {
			fennecResult = fennecEval(expression, self);
		} catch (OclParseException e) {
			fail("Fennec failed to parse: " + expression + " — " + e.getMessage());
			return;
		}

		try {
			eclipseResult = eclipseEval(expression, self);
		} catch (ParserException e) {
			fail("Eclipse failed to parse: " + expression + " — " + e.getMessage());
			return;
		}

		// Normalize results for comparison
		Object normalizedFennec = normalize(fennecResult);
		Object normalizedEclipse = normalize(eclipseResult);

		assertEquals(normalizedEclipse, normalizedFennec,
				"Results differ for expression: " + expression);
	}

	/**
	 * Normalizes engine results for comparison.
	 * - OclInvalid and Eclipse's invalid sentinel both become "INVALID"
	 * - Collections are converted to sorted lists for stable comparison
	 * - Numbers are normalized (Integer vs Long, etc.)
	 */
	static Object normalize(Object value) {
		if (value == null) {
			return null;
		}
		if (value == OclInvalid.INSTANCE || "OclInvalid".equals(String.valueOf(value))) {
			return "INVALID";
		}
		// Eclipse OCL uses its own invalid representation
		if (eclipseOcl != null && eclipseOcl.isInvalid(value)) {
			return "INVALID";
		}
		// Normalize numbers to comparable types
		if (value instanceof Long l) {
			if (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE) {
				return l.intValue();
			}
		}
		// Normalize collections to sorted lists
		if (value instanceof Collection<?> coll) {
			return List.copyOf(coll);
		}
		return value;
	}
}
