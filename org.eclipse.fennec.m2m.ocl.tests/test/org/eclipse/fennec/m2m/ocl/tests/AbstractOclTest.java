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

import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2m.ocl.api.OclContext;
import org.eclipse.fennec.m2m.ocl.api.OclEngine;
import org.eclipse.fennec.m2m.ocl.api.OclInvalid;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.eclipse.fennec.m2m.ocl.engine.OclEngineImpl;
import org.eclipse.fennec.m2m.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2m.utils.EcoreHelper;
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

	static OclEngineImpl engine;
	static EcoreHelper ecoreHelper;
	static EPackage companyPackage;
	static EClass companyClass;
	static EClass personClass;

	@BeforeAll
	static void setUpEngine() throws IOException {
		engine = new OclEngineImpl(new OclParserSupport());
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
		var empList = (java.util.List<EObject>) company.eGet(companyClass.getEStructuralFeature("employees"));
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
}
