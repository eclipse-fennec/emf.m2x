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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.OclResult;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 * Base class for OCL integration tests.
 *
 * <p>Loads the {@code company.ecore} model dynamically and provides a shared
 * {@link OclEngine} instance plus convenience methods for evaluating OCL
 * expressions.
 */
abstract class AbstractOclTest {

	static OclEngine engine;
	static EcoreHelper ecoreHelper;
	static EPackage companyPackage;
	static EClass companyClass;
	static EClass personClass;

	@BeforeAll
	static void setUpEngine() throws IOException {
		engine = OclEngines.create(new OclParserSupport());
		ecoreHelper = new EcoreHelper(AbstractOclTest.class);
		companyPackage = ecoreHelper.loadEcore("company.ecore");
		companyClass = ecoreHelper.getEClass(companyPackage, "Company");
		personClass = ecoreHelper.getEClass(companyPackage, "Person");
	}

	@AfterAll
	static void tearDown() {
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
	}

	/**
	 * Evaluates an OCL expression with the given EObject as self.
	 */
	static Object eval(String expression, EObject self) throws OclParseException {
		return engine.evaluate(expression, OclContext.of(self));
	}

	/**
	 * Creates a new Person instance with the given name.
	 */
	static EObject createPerson(String name, int age, double salary, boolean married) {
		EObject person = companyPackage.getEFactoryInstance().create(personClass);
		person.eSet(personClass.getEStructuralFeature("name"), name);
		person.eSet(personClass.getEStructuralFeature("age"), age);
		person.eSet(personClass.getEStructuralFeature("salary"), salary);
		person.eSet(personClass.getEStructuralFeature("isMarried"), married);
		return person;
	}

	/**
	 * Creates a new Company instance with the given name and employees.
	 */
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

	/**
	 * Asserts that the expression evaluates to OclInvalid.
	 */
	static void assertInvalid(String expression, EObject self) throws OclParseException {
		assertSame(OclInvalid.INSTANCE, eval(expression, self));
	}

	/**
	 * Evaluates an OCL expression with the engine's default options and returns value and diagnostics.
	 */
	static OclResult evalWithDiagnostics(String expression, EObject self) throws OclParseException {
		return engine.evaluateWithDiagnostics(engine.parse(expression, self.eClass()), OclContext.of(self),
				engine.getDefaultOptions());
	}

	/**
	 * Asserts that the expression is a parse error because its types do not fit (GAP-3, #378), and
	 * that the message contains the fragment.
	 */
	static OclParseException assertTypeError(String expression, EObject self, String messageFragment) {
		OclParseException failure = assertThrows(OclParseException.class, () -> engine.parse(expression, self.eClass()),
				() -> "expected a type error for " + expression);
		assertTrue(failure.getErrors().stream().anyMatch(d -> d.getMessage().contains(messageFragment)),
				() -> "expected a type error containing '" + messageFragment + "' for " + expression + ", got "
						+ failure.getErrors().stream().map(Resource.Diagnostic::getMessage).toList());
		return failure;
	}

	/**
	 * Evaluates an expression with external variables. A variable bound only at evaluation has no
	 * type when the expression is parsed — the way to reach what evaluation does with a value the
	 * type checker could not see (#378).
	 */
	static Object evalWith(String expression, EObject self, Map<String, Object> variables) throws OclParseException {
		return engine.evaluate(expression, new OclContext(self, null, variables, null));
	}

	/** {@link #evalWithDiagnostics} with external variables, see {@link #evalWith}. */
	static OclResult evalWithDiagnosticsWith(String expression, EObject self, Map<String, Object> variables)
			throws OclParseException {
		return engine.evaluateWithDiagnostics(engine.parse(expression, self.eClass()),
				new OclContext(self, null, variables, null), engine.getDefaultOptions());
	}

	/**
	 * Asserts that evaluating the expression reports an ERROR diagnostic whose message contains the fragment.
	 */
	static void assertHasError(String expression, EObject self, String messageFragment) throws OclParseException {
		OclResult result = evalWithDiagnostics(expression, self);
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() == Diagnostic.ERROR && d.getMessage().contains(messageFragment)),
				() -> "expected an ERROR containing '" + messageFragment + "' for " + expression + ", got "
						+ result.diagnostics().stream().map(Diagnostic::getMessage).toList());
	}
}
