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

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Two packages that both define a {@code Book} do not share a cached constraint.
 *
 * <p>The validation delegate keyed its cache by {@code contextType.getName() + "#" + expression}
 * — the simple class name, with no package. Two unrelated models that both define a class of the
 * same name, and both constrain it with the same text, therefore shared one entry, and whichever
 * was validated first decided which model the cached expression was compiled against.
 *
 * <p>It is invisible most of the time, which is what makes it worth a test: property access falls
 * back to resolving a feature by name on the runtime class
 * ({@code OclEvaluator.resolveFeature}), so a constraint that only navigates features keeps
 * working by accident. A constraint that names a <em>type</em> does not — the compiled expression
 * holds the other package's {@code EClass}, and {@code oclIsKindOf} then answers {@code false}
 * about an object that is very much of that kind. No error, no diagnostic: a valid object simply
 * reported as invalid.
 */
class OclValidationCacheKeyTest {

	private static final String DELEGATE_URI = "http://www.eclipse.org/fennec/m2x/ocl/1.0";

	/** The same text in both models — that is the point. */
	private static final String CONSTRAINT = "self.oclIsKindOf(Book)";

	private static EPackage libraryPackage;
	private static EPackage shopPackage;
	private static EClass libraryBook;
	private static EClass shopBook;

	private OclEngine engine;

	@BeforeAll
	static void registerPackages() {
		libraryPackage = bookPackage("library", "http://example.org/m2x/cache-key/library");
		shopPackage = bookPackage("shop", "http://example.org/m2x/cache-key/shop");
		libraryBook = (EClass) libraryPackage.getEClassifier("Book");
		shopBook = (EClass) shopPackage.getEClassifier("Book");

		EPackage.Registry.INSTANCE.put(libraryPackage.getNsURI(), libraryPackage);
		EPackage.Registry.INSTANCE.put(shopPackage.getNsURI(), shopPackage);
	}

	@AfterAll
	static void unregisterPackages() {
		EPackage.Registry.INSTANCE.remove(libraryPackage.getNsURI());
		EPackage.Registry.INSTANCE.remove(shopPackage.getNsURI());
	}

	/**
	 * A fresh engine per test, because the cache under test belongs to it — sharing one
	 * would let the first test decide the outcome of the second.
	 */
	@BeforeEach
	void freshEngine() {
		engine = OclEngines.create(new OclParserSupport());
		engine.installDelegates();
	}

	@AfterEach
	void removeDelegates() {
		engine.uninstallDelegates();
	}

	@Test
	@DisplayName("a book of the second model is not judged by the first model's constraint")
	void twoModelsDoNotShareOneCachedConstraint() {
		// Validated in this order on purpose: the first call is what fills the cache.
		assertTrue(isValid(libraryBook), "a library book satisfies its own constraint");
		assertTrue(isValid(shopBook),
				"a shop book satisfies the identical constraint of its own model — unless the "
						+ "cached expression came from the library model, whose Book it is not");
	}

	@Test
	@DisplayName("and the other way round, so neither order hides it")
	void theOrderDoesNotMatter() {
		assertTrue(isValid(shopBook));
		assertTrue(isValid(libraryBook));
	}

	// --- helpers ---

	/**
	 * A package with one {@code Book} carrying an invariant of the given text.
	 */
	private static EPackage bookPackage(String name, String nsURI) {
		EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
		ePackage.setName(name);
		ePackage.setNsURI(nsURI);
		ePackage.setNsPrefix(name);

		EAnnotation ecoreAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
		ecoreAnnotation.setSource("http://www.eclipse.org/emf/2002/Ecore");
		ecoreAnnotation.getDetails().put("validationDelegates", DELEGATE_URI);
		ePackage.getEAnnotations().add(ecoreAnnotation);

		EClass book = EcoreFactory.eINSTANCE.createEClass();
		book.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		book.getEStructuralFeatures().add(title);

		EAnnotation constraints = EcoreFactory.eINSTANCE.createEAnnotation();
		constraints.setSource("http://www.eclipse.org/emf/2002/Ecore");
		constraints.getDetails().put("constraints", "IsABook");
		book.getEAnnotations().add(constraints);

		EAnnotation body = EcoreFactory.eINSTANCE.createEAnnotation();
		body.setSource(DELEGATE_URI);
		body.getDetails().put("IsABook", CONSTRAINT);
		book.getEAnnotations().add(body);

		ePackage.getEClassifiers().add(book);
		return ePackage;
	}

	private boolean isValid(EClass bookClass) {
		EObject book = EcoreUtil.create(bookClass);
		book.eSet(bookClass.getEStructuralFeature("title"), "Moby Dick");
		return Diagnostician.INSTANCE.validate(book).getSeverity() == org.eclipse.emf.common.util.Diagnostic.OK;
	}
}
