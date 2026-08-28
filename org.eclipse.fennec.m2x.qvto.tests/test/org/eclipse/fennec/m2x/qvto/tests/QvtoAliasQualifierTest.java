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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The qualifier of a {@code tag "alias"} target decides which metamodel is meant.
 *
 * <p>QVT v1.3 §8.3.19 writes the target as a qualified path — {@code RDBMS::Table::key_}
 * — so the qualifier names the model type the class belongs to. The registry used to
 * drop it and look the class up by simple name across every package it could reach,
 * which resolved by installation accident when two metamodels declare the same name.
 */
class QvtoAliasQualifierTest {

	private static final String LEFT_NS = "http://example.org/m2x/left/1.0";
	private static final String RIGHT_NS = "http://example.org/m2x/right/1.0";

	private EPackage left;
	private EPackage right;
	private EPackage.Registry registry;

	@BeforeEach
	void setUp() {
		left = packageWith("left", LEFT_NS, "title");
		right = packageWith("right", RIGHT_NS, "label");

		registry = new EPackageRegistryImpl();
		registry.put(LEFT_NS, left);
		registry.put(RIGHT_NS, right);
	}

	@Test
	@DisplayName("the qualifier selects the metamodel, even when both declare the class")
	void qualifierSelectsTheMetamodel() throws Exception {
		// Both packages declare Book; only right::Book has 'label'. Without the
		// qualifier the lookup would take whichever package came first.
		QvtoExecutionResult result = run("""
				modeltype RIGHT uses '%s';
				transformation aliasTest(inout m : RIGHT) {
				    tag "alias" right::Book::caption = 'label';
				    main() {
				        log('done');
				    }
				}
				""".formatted(RIGHT_NS), right);

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertEquals(0, warnings(result).size(),
				() -> "a qualified target must not warn: " + warnings(result));
	}

	@Test
	@DisplayName("an unqualified, ambiguous class name is reported as a warning")
	void ambiguousUnqualifiedTargetWarns() throws Exception {
		// The extent holds an unrelated object, so neither it nor its package answers
		// the lookup and the search falls through to the registry — where both
		// metamodels offer a class of that name.
		QvtoExecutionResult result = runWith("""
				modeltype RIGHT uses '%s';
				transformation aliasTest(inout m : RIGHT) {
				    tag "alias" Magazine::caption = 'label';
				    main() {
				        log('done');
				    }
				}
				""".formatted(RIGHT_NS), EcoreFactory.eINSTANCE.createEPackage());

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertTrue(warnings(result).stream()
				.anyMatch(message -> message.contains("ambiguous")),
				() -> "expected an ambiguity warning, got: " + warnings(result));
	}

	// --- helpers ---

	private List<String> warnings(QvtoExecutionResult result) {
		return result.diagnostics().stream()
				.filter(d -> d.getSeverity() == org.eclipse.emf.common.util.Diagnostic.WARNING)
				.map(org.eclipse.emf.common.util.Diagnostic::getMessage)
				.toList();
	}

	private QvtoExecutionResult run(String source, EPackage instancePackage) throws Exception {
		EClass bookClass = (EClass) instancePackage.getEClassifier("Book");
		return runWith(source, EcoreUtil.create(bookClass));
	}

	private QvtoExecutionResult runWith(String source, EObject content) throws Exception {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		QvtoEngine engine = QvtoEngines.create(QvtoConfiguration.builder(oclConfig)
				.packageRegistry(registry)
				.build());

		OperationalTransformation t = engine.parse(source, "aliasTest");
		QvtoModelExtent extent = new BasicQvtoModelExtent(content);

		return engine.execute(t, QvtoExecutionContext.of(extent));
	}

	private static EPackage packageWith(String name, String nsURI, String attributeName) {
		EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
		ePackage.setName(name);
		ePackage.setNsURI(nsURI);
		ePackage.setNsPrefix(name);

		ePackage.getEClassifiers().add(classWith("Book", attributeName));
		ePackage.getEClassifiers().add(classWith("Magazine", attributeName));
		return ePackage;
	}

	private static EClass classWith(String className, String attributeName) {
		EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		eClass.setName(className);
		EAttribute attribute = EcoreFactory.eINSTANCE.createEAttribute();
		attribute.setName(attributeName);
		attribute.setEType(EcorePackage.Literals.ESTRING);
		eClass.getEStructuralFeatures().add(attribute);
		return eClass;
	}
}
