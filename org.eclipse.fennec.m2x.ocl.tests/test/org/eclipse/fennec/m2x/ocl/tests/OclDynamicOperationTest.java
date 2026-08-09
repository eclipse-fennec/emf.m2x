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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.OclResult;
import org.eclipse.fennec.m2x.ocl.engine.OclEngineImpl;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Calling an {@code EOperation} of a dynamic model.
 *
 * <p>An operation declared in an {@code .ecore} that is loaded at runtime has no
 * implementation: there is no generated {@code eInvoke} switch, and EMF refuses with an
 * {@link UnsupportedOperationException} unless an invocation delegate is registered.
 * That exception used to travel straight out of the engine, past a result type that
 * exists to carry exactly this kind of problem.
 */
class OclDynamicOperationTest {

	private static final String NS_URI = "http://example.org/m2x/dynamic-operation/1.0";

	private EClass bookClass;
	private OclParserSupport parser;
	private OclEngineImpl engine;

	@BeforeEach
	void setUp() {
		EcoreFactory factory = EcoreFactory.eINSTANCE;
		EPackage ePackage = factory.createEPackage();
		ePackage.setName("bookshelf");
		ePackage.setNsURI(NS_URI);
		ePackage.setNsPrefix("bookshelf");

		bookClass = factory.createEClass();
		bookClass.setName("Book");
		EAttribute title = factory.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(title);

		EOperation displayLabel = factory.createEOperation();
		displayLabel.setName("displayLabel");
		displayLabel.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEOperations().add(displayLabel);

		ePackage.getEClassifiers().add(bookClass);

		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(NS_URI, ePackage);

		parser = new OclParserSupport(registry);
		engine = new OclEngineImpl(parser);
	}

	@Test
	@DisplayName("an operation without implementation is reported, not thrown out of the engine")
	void unimplementedOperationIsReported() throws OclParseException {
		EObject book = EcoreUtil.create(bookClass);
		book.eSet(bookClass.getEStructuralFeature("title"), "Moby Dick");

		OclResult result = engine.evaluateWithDiagnostics(
				parser.parse("self.displayLabel()", bookClass), OclContext.of(book),
				OclEvaluationOptions.strict());

		assertFalse(result.isSuccess(), () -> "the call cannot succeed: " + result.value());
		assertTrue(result.isInvalid(), () -> "expected OclInvalid, got " + result.value());
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("displayLabel")
						&& d.getMessage().contains("no implementation")),
				() -> "diagnostics: " + result.diagnostics());
	}
}
