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
package org.eclipse.fennec.m2x.qvtd.tests;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.BasicQvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 * Abstract base class for QVT-R engine tests.
 * Provides shared setup for QvtdEngine and convenience methods.
 */
public abstract class AbstractQvtdEngineTest {

	protected static QvtdEngine engine;
	protected static EcoreHelper ecoreHelper;
	protected static EPackage umlPackage;
	protected static EPackage rdbmsPackage;

	@BeforeAll
	static void setUpEngine() throws IOException {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		QvtdConfiguration config = QvtdConfiguration.builder(oclConfig).build();
		engine = QvtdEngines.create(config);
		ecoreHelper = new EcoreHelper(AbstractQvtdEngineTest.class);
		umlPackage = ecoreHelper.loadEcore("simpleuml.ecore");
		rdbmsPackage = ecoreHelper.loadEcore("simplerdbms.ecore");
	}

	@AfterAll
	static void tearDownEngine() {
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
	}

	/**
	 * Parse a QVT-R source string.
	 */
	protected static RelationalTransformation parse(String source) throws QvtdParseException {
		return engine.parse(source, "test");
	}

	/**
	 * Parse and execute a QVT-R transformation in enforce mode.
	 */
	protected static QvtdExecutionResult executeEnforce(String source,
			String targetModelName, Map<String, QvtdModelExtent> extents)
			throws QvtdParseException {
		RelationalTransformation t = parse(source);
		QvtdExecutionContext ctx = QvtdExecutionContext.enforce(targetModelName, extents);
		return engine.execute(t, ctx);
	}

	/**
	 * Parse and execute a QVT-R transformation in check-only mode.
	 */
	protected static QvtdExecutionResult executeCheckOnly(String source,
			Map<String, QvtdModelExtent> extents) throws QvtdParseException {
		RelationalTransformation t = parse(source);
		QvtdExecutionContext ctx = QvtdExecutionContext.checkOnly(extents);
		return engine.execute(t, ctx);
	}

	// --- UML model factory methods ---

	protected static EObject createPackage(String name) {
		EClass cls = ecoreHelper.getEClass(umlPackage, "Package");
		EObject pkg = EcoreUtil.create(cls);
		pkg.eSet(cls.getEStructuralFeature("name"), name);
		return pkg;
	}

	protected static EObject createClass(String name, String kind) {
		EClass cls = ecoreHelper.getEClass(umlPackage, "Class");
		EObject clazz = EcoreUtil.create(cls);
		clazz.eSet(cls.getEStructuralFeature("name"), name);
		if (kind != null) {
			clazz.eSet(cls.getEStructuralFeature("kind"), kind);
		}
		return clazz;
	}

	protected static EObject createAttribute(String name, String type) {
		EClass cls = ecoreHelper.getEClass(umlPackage, "Attribute");
		EObject attr = EcoreUtil.create(cls);
		attr.eSet(cls.getEStructuralFeature("name"), name);
		if (type != null) {
			attr.eSet(cls.getEStructuralFeature("type"), type);
		}
		return attr;
	}

	@SuppressWarnings("unchecked")
	protected static void addClassToPackage(EObject pkg, EObject clazz) {
		((List<EObject>) pkg.eGet(pkg.eClass().getEStructuralFeature("ownedClass"))).add(clazz);
	}

	@SuppressWarnings("unchecked")
	protected static void addAttributeToClass(EObject clazz, EObject attr) {
		((List<EObject>) clazz.eGet(clazz.eClass().getEStructuralFeature("ownedAttribute"))).add(attr);
	}

	// --- RDBMS model inspection methods ---

	protected static String getName(EObject obj) {
		return (String) obj.eGet(obj.eClass().getEStructuralFeature("name"));
	}

	@SuppressWarnings("unchecked")
	protected static List<EObject> getTables(EObject schema) {
		return (List<EObject>) schema.eGet(schema.eClass().getEStructuralFeature("ownedTable"));
	}

	@SuppressWarnings("unchecked")
	protected static List<EObject> getColumns(EObject table) {
		return (List<EObject>) table.eGet(table.eClass().getEStructuralFeature("ownedColumn"));
	}

	protected static QvtdModelExtent emptyExtent() {
		return new BasicQvtdModelExtent();
	}
}
