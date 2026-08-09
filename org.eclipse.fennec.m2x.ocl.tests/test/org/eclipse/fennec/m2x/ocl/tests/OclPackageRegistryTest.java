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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.ocl.Constraint;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.engine.OclEngineImpl;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

/**
 * Type resolution against an explicitly supplied {@link EPackage.Registry} (D42).
 *
 * <p>Two dynamic packages are used, neither of them registered globally unless a test
 * says so: {@code library} defines {@code Book}, {@code media} defines {@code Novel},
 * a subclass of {@code Book} living in the other package. A type name from that foreign
 * package can only resolve through a registry — the context type's own package does not
 * contain it.
 *
 * <p>The discriminating expression is {@code oclIsTypeOf}: with {@code Novel} resolved it
 * answers {@code true} for a {@code Novel} instance, while an unresolved name either
 * degrades to the context type {@code Book} (exact-type comparison fails) or resolves to
 * nothing at all — {@code false} either way.
 */
class OclPackageRegistryTest {

	private static final String LIBRARY_NS = "http://example.org/m2x/library/1.0";
	private static final String MEDIA_NS = "http://example.org/m2x/media/1.0";

	private EPackage libraryPackage;
	private EPackage mediaPackage;
	private EClass bookClass;
	private EClass novelClass;
	private EPackage.Registry registry;

	@BeforeEach
	void setUp() {
		libraryPackage = createPackage("library", LIBRARY_NS);
		bookClass = createClass(libraryPackage, "Book");
		addStringAttribute(bookClass, "title");

		mediaPackage = createPackage("media", MEDIA_NS);
		novelClass = createClass(mediaPackage, "Novel");
		novelClass.getESuperTypes().add(bookClass);

		registry = new EPackageRegistryImpl();
		registry.put(LIBRARY_NS, libraryPackage);
		registry.put(MEDIA_NS, mediaPackage);
	}

	@AfterEach
	void tearDown() {
		EPackage.Registry.INSTANCE.remove(LIBRARY_NS);
		EPackage.Registry.INSTANCE.remove(MEDIA_NS);
	}

	@Nested
	@DisplayName("Expressions")
	class Expressions {

		@Test
		@DisplayName("a type of a foreign package resolves through the supplied registry")
		void foreignPackageTypeResolves() throws OclParseException {
			OclParserSupport parser = new OclParserSupport(registry);
			OclEngineImpl engine = new OclEngineImpl(parser);

			OclExpression expression = parser.parse("self.oclIsTypeOf(Novel)", bookClass);

			assertEquals(Boolean.TRUE, engine.evaluate(expression, OclContext.of(newNovel())),
					"Novel lives in the other package and only resolves through the registry");
		}

		@Test
		@DisplayName("a type of the context type's own package resolves without any registry")
		void ownPackageTypeResolvesWithoutRegistry() throws OclParseException {
			OclParserSupport parser = new OclParserSupport();
			OclEngineImpl engine = new OclEngineImpl(parser);

			OclExpression expression = parser.parse("self.oclIsTypeOf(Book)", bookClass);

			assertEquals(Boolean.TRUE, engine.evaluate(expression, OclContext.of(newBook())));
		}

		@Test
		@DisplayName("a resource set is enough — its package registry is used")
		void resourceSetSuppliesTheRegistry() throws OclParseException {
			ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getPackageRegistry().put(LIBRARY_NS, libraryPackage);
			resourceSet.getPackageRegistry().put(MEDIA_NS, mediaPackage);

			OclParserSupport parser = new OclParserSupport(resourceSet);
			OclEngineImpl engine = new OclEngineImpl(parser);

			OclExpression expression = parser.parse("self.oclIsTypeOf(Novel)", bookClass);

			assertEquals(Boolean.TRUE, engine.evaluate(expression, OclContext.of(newNovel())));
		}

		@Test
		@DisplayName("without a configured registry the static one applies")
		void staticRegistryAppliesWhenNoneConfigured() throws OclParseException {
			EPackage.Registry.INSTANCE.put(MEDIA_NS, mediaPackage);

			OclParserSupport parser = new OclParserSupport();
			OclEngineImpl engine = new OclEngineImpl(parser);

			OclExpression expression = parser.parse("self.oclIsTypeOf(Novel)", bookClass);

			assertEquals(Boolean.TRUE, engine.evaluate(expression, OclContext.of(newNovel())));
		}
	}

	@Nested
	@DisplayName("Unresolvable names")
	class Unresolvable {

		@Test
		@DisplayName("a qualified type that resolves nowhere is reported instead of degrading")
		void unknownTypeIsReported() {
			OclParserSupport parser = new OclParserSupport();

			OclParseException failure = assertThrows(OclParseException.class,
					() -> parser.parse("self.oclIsTypeOf(nosuch::NoSuchType)", bookClass));

			assertEquals(1, failure.getErrors().size());
			assertTrue(failure.getErrors().get(0).getMessage()
					.contains("Unknown type (nosuch::NoSuchType)"),
					() -> failure.getErrors().get(0).getMessage());
		}

		@Test
		@DisplayName("several unknown names are reported together, not one at a time")
		void allUnknownNamesAreReported() {
			OclParserSupport parser = new OclParserSupport();

			OclParseException failure = assertThrows(OclParseException.class,
					() -> parser.parse(
							"self.oclIsTypeOf(nosuch::Missing1) or self.oclIsTypeOf(nosuch::Missing2)",
							bookClass));

			assertEquals(2, failure.getErrors().size(),
					() -> "diagnostics: " + failure.getErrors());
		}

		@Test
		@DisplayName("an unqualified unknown name stays an external variable — by design")
		void unqualifiedNameRemainsAnExternalVariable() throws OclParseException {
			// A bare name that resolves to no property and no classifier becomes an
			// external variable reference, which OclContext can bind at evaluation time.
			// Turning that into an error would break context variables, so a name in a
			// value position keeps this behaviour; only qualified names are rejected.
			OclParserSupport parser = new OclParserSupport();

			assertNotNull(parser.parse("threshold", bookClass));
		}
	}

	@Nested
	@DisplayName("Complete OCL documents")
	class Documents {

		@Test
		@DisplayName("the context classifier resolves through the supplied registry")
		void contextClassifierResolves() throws OclParseException {
			OclParserSupport parser = new OclParserSupport(registry);
			OclEngineImpl engine = new OclEngineImpl(parser);

			var constraints = engine.parseDocument("""
					context Novel
					  inv titleSet: self.title.size() > 0
					""");

			assertEquals(1, constraints.size());
			Constraint invariant = constraints.get(0);
			assertNotNull(invariant.getContextClassifier(),
					"context type must resolve from the supplied registry");
			assertEquals("Novel", invariant.getContextClassifier().getName());
		}
	}

	// --- helpers ---

	private EObject newBook() {
		EObject book = EcoreUtil.create(bookClass);
		book.eSet(bookClass.getEStructuralFeature("title"), "Moby Dick");
		return book;
	}

	private EObject newNovel() {
		EObject novel = EcoreUtil.create(novelClass);
		novel.eSet(novelClass.getEStructuralFeature("title"), "Moby Dick");
		return novel;
	}

	private static EPackage createPackage(String name, String nsURI) {
		EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
		ePackage.setName(name);
		ePackage.setNsURI(nsURI);
		ePackage.setNsPrefix(name);
		return ePackage;
	}

	private static EClass createClass(EPackage owner, String name) {
		EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		eClass.setName(name);
		owner.getEClassifiers().add(eClass);
		return eClass;
	}

	private static void addStringAttribute(EClass owner, String name) {
		EAttribute attribute = EcoreFactory.eINSTANCE.createEAttribute();
		attribute.setName(name);
		attribute.setEType(EcorePackage.Literals.ESTRING);
		owner.getEStructuralFeatures().add(attribute);
	}
}
