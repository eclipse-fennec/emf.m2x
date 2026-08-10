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
package org.eclipse.fennec.m2x.m2t.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@code allInstances()} inside a template.
 *
 * <p>The reference is what Acceleo does, which is what Eclipse OCL's extent map does: the extent
 * is the resource of the context object's root container, or that root's own containment tree
 * when it belongs to no resource. Subtypes count.
 *
 * <p>One thing goes beyond that reference on purpose and is pinned here: where {@code self} is
 * not an {@code EObject} — a template called with a string, which is ordinary in a generator —
 * Eclipse OCL hands out an empty extent, and {@code allInstances()} would answer zero in the
 * place a template author is most likely to write it. The generation's input elements are used
 * instead.
 */
class M2tAllInstancesTest {

	private static final String NS_URI = "http://example.org/m2x/library/1.0";

	private EPackage library;
	private EClass bookClass;
	private EClass novelClass;
	private EClass shelfClass;
	private EAttribute titleAttribute;
	private EReference booksReference;

	@BeforeEach
	void setUp() {
		library = EcoreFactory.eINSTANCE.createEPackage();
		library.setName("library");
		library.setNsPrefix("library");
		library.setNsURI(NS_URI);

		bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		titleAttribute = EcoreFactory.eINSTANCE.createEAttribute();
		titleAttribute.setName("title");
		titleAttribute.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(titleAttribute);

		// a subtype, because allInstances() answers for the kind, not for the exact type
		novelClass = EcoreFactory.eINSTANCE.createEClass();
		novelClass.setName("Novel");
		novelClass.getESuperTypes().add(bookClass);

		shelfClass = EcoreFactory.eINSTANCE.createEClass();
		shelfClass.setName("Shelf");
		booksReference = EcoreFactory.eINSTANCE.createEReference();
		booksReference.setName("books");
		booksReference.setEType(bookClass);
		booksReference.setContainment(true);
		booksReference.setUpperBound(-1);
		shelfClass.getEStructuralFeatures().add(booksReference);

		library.getEClassifiers().addAll(List.of(bookClass, novelClass, shelfClass));
	}

	@Test
	@DisplayName("a template sees the instances of the model it generates from")
	void allInstancesOverTheInputModel() throws Exception {
		EObject shelf = shelf(book("Dune"), novel("Emma"));
		inResource(shelf);

		assertEquals("2", generate("[Book.allInstances()->size()/]", shelf),
				"both books, the subtype instance among them");
	}

	@Test
	@DisplayName("the extent is the resource, not only what self contains")
	void theExtentSpansTheResource() throws Exception {
		EObject firstShelf = shelf(book("Dune"));
		EObject secondShelf = shelf(book("Emma"), book("Persuasion"));
		inResource(firstShelf, secondShelf);

		// self is the first shelf and contains one book; the extent is the whole resource,
		// which is the scope Eclipse OCL's extent map uses and Acceleo therefore inherits
		assertEquals("3", generate("[Book.allInstances()->size()/]", firstShelf));
	}

	@Test
	@DisplayName("a model in another resource is not in the extent")
	void anotherResourceIsNotInTheExtent() throws Exception {
		EObject shelf = shelf(book("Dune"));
		ResourceSet resourceSet = resourceSet();
		resourceIn(resourceSet, "memory://own.xmi", shelf);
		resourceIn(resourceSet, "memory://other.xmi", shelf(book("Emma"), book("Persuasion")));

		assertEquals("1", generate("[Book.allInstances()->size()/]", shelf),
				"a template generates from a model, not from everything that happens to be loaded");
	}

	@Test
	@DisplayName("without a resource, the root's containment tree is the extent")
	void aDetachedModelIsItsOwnExtent() throws Exception {
		EObject shelf = shelf(book("Dune"), book("Emma"));

		assertEquals("2", generate("[Book.allInstances()->size()/]", shelf),
				"a model built in memory and never saved still has an extent");
	}

	@Test
	@DisplayName("a template called with a string still sees the input model")
	void aNonEObjectContextFallsBackToTheInputElements() throws Exception {
		EObject shelf = shelf(book("Dune"), novel("Emma"));
		inResource(shelf);

		// inside 'count' self is a String: Eclipse OCL would hand out an empty extent, and
		// allInstances() would answer 0 — silently, which is the part that matters
		String body = "[for (b : Book | s.books)][count(b.title)/][/for]";
		String extra = """
				[template public count(t : String)][Book.allInstances()->size()/][/template]
				""";

		// whitespace between the two invocations is §8.4's business, not this test's
		assertEquals("22", generate(body, shelf, extra).replaceAll("\\s+", ""),
				"once per book, and the model is still the input model");
	}

	// --- helpers ---

	private EObject book(String title) {
		EObject book = EcoreUtil.create(bookClass);
		book.eSet(titleAttribute, title);
		return book;
	}

	private EObject novel(String title) {
		EObject novel = EcoreUtil.create(novelClass);
		novel.eSet(titleAttribute, title);
		return novel;
	}

	private EObject shelf(EObject... books) {
		EObject shelf = EcoreUtil.create(shelfClass);
		@SuppressWarnings("unchecked")
		List<EObject> contained = (List<EObject>) shelf.eGet(booksReference);
		contained.addAll(List.of(books));
		return shelf;
	}

	private ResourceSet resourceSet() {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(NS_URI, library);
		return resourceSet;
	}

	/** A resource without a factory registration — nothing here is ever saved or loaded. */
	private static Resource resourceIn(ResourceSet resourceSet, String uri, EObject... roots) {
		Resource resource = new ResourceImpl(URI.createURI(uri));
		resourceSet.getResources().add(resource);
		resource.getContents().addAll(List.of(roots));
		return resource;
	}

	private void inResource(EObject... roots) {
		resourceIn(resourceSet(), "memory://library.xmi", roots);
	}

	private String generate(String body, EObject input) throws Exception {
		return generate(body, input, "");
	}

	private String generate(String body, EObject input, String extraTemplates) throws Exception {
		String template = """
				[module extent(_'%s')/]
				[template public main(s : Shelf)]
				[file ('out.txt', false)]
				%s
				[/file]
				[/template]
				%s
				""".formatted(NS_URI, body, extraTemplates);

		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(NS_URI, library);
		// the M2T side resolves template parameter types through its own registry (D42)
		M2tEngine engine = M2tEngines.create(M2tConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport(registry)).build())
				.packageRegistry(registry)
				.build());

		Module module = engine.parse(template, "extent");
		engine.link(module);
		M2tResult result = engine.execute(module, M2tContext.of(input));
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		return result.generatedFiles().get("out.txt").strip();
	}
}
