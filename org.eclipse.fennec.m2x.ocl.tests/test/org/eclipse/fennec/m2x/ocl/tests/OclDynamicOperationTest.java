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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.ecore.EAnnotation;
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
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.OclResult;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclDelegateUtil;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Calling an {@code EOperation} of a dynamic model.
 *
 * <p>A dynamic model has no generated {@code eInvoke} switch, so EMF asks an invocation
 * delegate — and with an OCL body annotation plus installed delegates there is one, so
 * the call works. The two tests here are the pair: with a body it computes, without one
 * there is nothing to invoke and the engine says so instead of letting EMF's
 * {@link UnsupportedOperationException} travel out past a result type that exists to
 * carry exactly this kind of problem.
 */
class OclDynamicOperationTest {

	private static final String NS_URI = "http://example.org/m2x/dynamic-operation/1.0";

	private EClass bookClass;
	private OclParserSupport parser;
	private OclEngine engine;

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
		engine = OclEngines.create(parser);
	}

	@Test
	@DisplayName("an operation with an OCL body is invoked on a dynamic model")
	void operationWithOclBodyIsInvoked() {
		// The delegate mechanism is what makes eInvoke work without generated code:
		// the package declares which delegate URI it uses, the operation carries the
		// body, and the engine registers the factory for that URI.
		EAnnotation packageAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
		packageAnnotation.setSource("http://www.eclipse.org/emf/2002/Ecore");
		packageAnnotation.getDetails().put("invocationDelegates", OclDelegateUtil.DELEGATE_URI);
		bookClass.getEPackage().getEAnnotations().add(packageAnnotation);

		EOperation shout = EcoreFactory.eINSTANCE.createEOperation();
		shout.setName("shout");
		shout.setEType(EcorePackage.Literals.ESTRING);
		EAnnotation body = EcoreFactory.eINSTANCE.createEAnnotation();
		body.setSource(OclDelegateUtil.DELEGATE_URI);
		body.getDetails().put("body", "self.title.toUpper()");
		shout.getEAnnotations().add(body);
		bookClass.getEOperations().add(shout);

		EPackage.Registry.INSTANCE.put(NS_URI, bookClass.getEPackage());
		engine.installDelegates();
		try {
			EObject book = EcoreUtil.create(bookClass);
			book.eSet(bookClass.getEStructuralFeature("title"), "Moby Dick");

			assertEquals("MOBY DICK", book.eInvoke(shout, ECollections.emptyEList()));
		} catch (InvocationTargetException e) {
			throw new AssertionError("the delegate must handle this operation", e);
		} finally {
			engine.uninstallDelegates();
			EPackage.Registry.INSTANCE.remove(NS_URI);
		}
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
