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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEvaluationOptions;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngineImpl;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 * Abstract base class for QVT-O engine tests.
 * Provides shared setup for QvtoEngine and convenience methods for parsing and executing.
 */
public abstract class AbstractQvtoEngineTest {

	protected static QvtoEngineImpl engine;
	protected static EcoreHelper ecoreHelper;
	protected static EPackage sourcePackage;
	protected static EPackage targetPackage;

	@BeforeAll
	static void setUpEngine() throws IOException {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		QvtoConfiguration config = QvtoConfiguration.builder(oclConfig).build();
		engine = new QvtoEngineImpl(config);
		ecoreHelper = new EcoreHelper(AbstractQvtoEngineTest.class);
		sourcePackage = ecoreHelper.loadEcore("source.ecore");
		targetPackage = ecoreHelper.loadEcore("target.ecore");
	}

	@AfterAll
	static void tearDownEngine() {
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
	}

	/**
	 * Parse a QVT-O source string.
	 */
	protected static OperationalTransformation parse(String source) throws QvtoParseException {
		return engine.parse(source, "test");
	}

	/**
	 * Parse and execute a QVT-O transformation with no model extents.
	 */
	protected static QvtoExecutionResult execute(String source) throws QvtoParseException {
		OperationalTransformation t = parse(source);
		return engine.execute(t, QvtoExecutionContext.of());
	}

	/**
	 * Parse and execute with a given context.
	 */
	protected static QvtoExecutionResult execute(String source, QvtoExecutionContext context)
			throws QvtoParseException {
		OperationalTransformation t = parse(source);
		return engine.execute(t, context);
	}

	/**
	 * Parse and execute with options.
	 */
	protected static QvtoExecutionResult execute(String source, QvtoExecutionContext context,
			QvtoEvaluationOptions options) throws QvtoParseException {
		OperationalTransformation t = parse(source);
		return engine.execute(t, context, options);
	}

	/**
	 * Parse and execute with model extents.
	 */
	protected static QvtoExecutionResult executeWithExtents(String source, QvtoModelExtent... extents)
			throws QvtoParseException {
		OperationalTransformation t = parse(source);
		return engine.execute(t, QvtoExecutionContext.of(extents));
	}

	/**
	 * Parse and execute with model extents and custom options.
	 */
	protected static QvtoExecutionResult executeWithExtents(String source, QvtoEvaluationOptions options,
			QvtoModelExtent... extents) throws QvtoParseException {
		OperationalTransformation t = parse(source);
		return engine.execute(t, QvtoExecutionContext.of(extents), options);
	}

	/**
	 * Creates a SourceElement EObject with the given name and value.
	 */
	protected static EObject createSourceElement(String name, int value) {
		EClass sourceElementClass = ecoreHelper.getEClass(sourcePackage, "SourceElement");
		EObject element = EcoreUtil.create(sourceElementClass);
		element.eSet(sourceElementClass.getEStructuralFeature("name"), name);
		element.eSet(sourceElementClass.getEStructuralFeature("value"), value);
		return element;
	}

	/**
	 * Creates a SpecialSourceElement EObject with the given name, value and tag.
	 */
	protected static EObject createSpecialSourceElement(String name, int value, String tag) {
		EClass specialClass = ecoreHelper.getEClass(sourcePackage, "SpecialSourceElement");
		EObject element = EcoreUtil.create(specialClass);
		element.eSet(specialClass.getEStructuralFeature("name"), name);
		element.eSet(specialClass.getEStructuralFeature("value"), value);
		element.eSet(specialClass.getEStructuralFeature("tag"), tag);
		return element;
	}

	/**
	 * Creates an empty model extent.
	 */
	protected static QvtoModelExtent emptyExtent() {
		return new BasicQvtoModelExtent();
	}
}
