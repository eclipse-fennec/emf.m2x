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
package org.eclipse.fennec.m2x.qvto.tests;

import java.io.IOException;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.parser.QvtoParserSupport;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 * Abstract base class for QVT-O parser tests.
 * Provides shared setup for QvtoParserSupport, EcoreHelper, and convenience methods.
 */
abstract class AbstractQvtoParserTest {

	static QvtoParserSupport parser;
	static EcoreHelper ecoreHelper;
	static EPackage sourcePackage;
	static EPackage targetPackage;

	@BeforeAll
	static void setUp() throws IOException {
		parser = new QvtoParserSupport();
		ecoreHelper = new EcoreHelper(AbstractQvtoParserTest.class);
		sourcePackage = ecoreHelper.loadEcore("source.ecore");
		targetPackage = ecoreHelper.loadEcore("target.ecore");
	}

	@AfterAll
	static void tearDown() {
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
	}

	/**
	 * Parse a QVT-O source string with the global package registry.
	 */
	static OperationalTransformation parse(String source) throws QvtoParseException {
		return parser.parse(source, "test", EPackage.Registry.INSTANCE);
	}

	/**
	 * Get the internal module EClass that holds operations.
	 */
	static EClass getModuleClass(OperationalTransformation t) {
		return t.getEClassifiers().stream()
				.filter(EClass.class::isInstance)
				.map(EClass.class::cast)
				.filter(c -> c.getName().equals(t.getName()))
				.findFirst()
				.orElseThrow(() -> new AssertionError("No module class found for " + t.getName()));
	}

	/**
	 * Get an EOperation from the module class by name.
	 */
	static EOperation getOperation(OperationalTransformation t, String name) {
		EClass moduleClass = getModuleClass(t);
		return moduleClass.getEOperations().stream()
				.filter(op -> name.equals(op.getName()))
				.findFirst()
				.orElseThrow(() -> new AssertionError("No operation '" + name + "' found"));
	}

	/**
	 * Get an EClassifier from the transformation by name.
	 */
	static EClassifier getClassifier(OperationalTransformation t, String name) {
		return t.getEClassifiers().stream()
				.filter(c -> c.getName().equals(name))
				.findFirst()
				.orElseThrow(() -> new AssertionError("No classifier '" + name + "' found"));
	}
}
