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
package org.eclipse.fennec.m2x.m2t.engine.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The scope rules of {@link M2tModelExtent}, at the level where they are decided.
 *
 * <p>{@link org.eclipse.fennec.m2x.m2t.engine.M2tAllInstancesTest} pins the same rules as a
 * template author meets them; this pins them directly, including the one promise a template
 * cannot observe: instances are collected per class once and kept, so asking in a loop does not
 * rescan the model per pass.
 */
class M2tModelExtentTest {

	private EPackage library;
	private EClass bookClass;
	private EClass novelClass;
	private EClass shelfClass;
	private EReference booksReference;

	@BeforeEach
	void setUp() {
		library = EcoreFactory.eINSTANCE.createEPackage();
		library.setName("library");
		library.setNsPrefix("library");
		library.setNsURI("http://example.org/m2x/extent/1.0");

		bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
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
	@DisplayName("subtype instances are part of a type's extent")
	void subtypesCount() {
		EObject shelf = shelf(book(), novel());

		Collection<EObject> books = M2tModelExtent.aroundSelf(shelf).getAllInstances(bookClass);

		assertEquals(2, books.size());
		assertEquals(1, M2tModelExtent.aroundSelf(shelf).getAllInstances(novelClass).size(),
				"and the subtype's own extent holds only its instances");
	}

	@Test
	@DisplayName("the root element itself belongs to its extent")
	void theRootIsInItsOwnExtent() {
		EObject shelf = shelf(book());

		assertEquals(List.of(shelf), M2tModelExtent.aroundSelf(shelf).getAllInstances(shelfClass));
	}

	@Test
	@DisplayName("in a resource the extent is the resource, reached from any element")
	void theResourceIsTheScope() {
		EObject firstShelf = shelf(book());
		EObject secondShelf = shelf(book(), book());
		inResource(firstShelf, secondShelf);
		EObject deepInside = (EObject) ((List<?>) firstShelf.eGet(booksReference)).get(0);

		assertEquals(3, M2tModelExtent.aroundSelf(deepInside).getAllInstances(bookClass).size(),
				"the scope is found from the root container, not from where the ask happened");
	}

	@Test
	@DisplayName("without a resource the extent is the root's tree")
	void withoutAResourceTheTreeIsTheScope() {
		EObject shelf = shelf(book(), book());
		EObject unrelated = shelf(book());

		assertEquals(2, M2tModelExtent.aroundSelf(shelf).getAllInstances(bookClass).size());
		assertEquals(1, M2tModelExtent.aroundSelf(unrelated).getAllInstances(bookClass).size(),
				"two detached trees are two extents");
	}

	@Test
	@DisplayName("input elements of one resource do not count their content twice")
	void inputElementsOfOneResourceAreMergedByIdentity() {
		EObject firstShelf = shelf(book());
		EObject secondShelf = shelf(book());
		inResource(firstShelf, secondShelf);

		Collection<EObject> books = M2tModelExtent.aroundAll(List.of(firstShelf, secondShelf))
				.getAllInstances(bookClass);

		assertEquals(2, books.size(), "two shelves of one resource, two books — not four");
	}

	@Test
	@DisplayName("input elements from different resources are all in scope")
	void inputElementsFromSeveralResourcesAreUnited() {
		EObject own = shelf(book());
		EObject other = shelf(book(), book());
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceIn(resourceSet, "memory://own.xmi", own);
		resourceIn(resourceSet, "memory://other.xmi", other);

		assertEquals(3, M2tModelExtent.aroundAll(List.of(own, other))
				.getAllInstances(bookClass).size());
	}

	@Test
	@DisplayName("an extent collects a class once and keeps it")
	void collectingHappensOncePerClass() {
		EObject shelf = shelf(book(), book());
		M2tModelExtent extent = M2tModelExtent.aroundSelf(shelf);

		Collection<EObject> first = extent.getAllInstances(bookClass);
		Collection<EObject> second = extent.getAllInstances(bookClass);

		assertSame(first, second, "the second ask must not walk the model again");
	}

	@Test
	@DisplayName("no class, no instances")
	void aMissingClassIsAnsweredEmpty() {
		assertTrue(M2tModelExtent.aroundSelf(shelf()).getAllInstances(null).isEmpty());
		assertTrue(M2tModelExtent.aroundAll(List.of()).getAllInstances(bookClass).isEmpty());
	}

	// --- helpers ---

	private EObject book() {
		return EcoreUtil.create(bookClass);
	}

	private EObject novel() {
		return EcoreUtil.create(novelClass);
	}

	private EObject shelf(EObject... books) {
		EObject shelf = EcoreUtil.create(shelfClass);
		@SuppressWarnings("unchecked")
		List<EObject> contained = (List<EObject>) shelf.eGet(booksReference);
		contained.addAll(List.of(books));
		return shelf;
	}

	private void inResource(EObject... roots) {
		resourceIn(new ResourceSetImpl(), "memory://extent.xmi", roots);
	}

	/** A resource without a factory registration — nothing here is ever saved or loaded. */
	private static Resource resourceIn(ResourceSet resourceSet, String uri, EObject... roots) {
		Resource resource = new ResourceImpl(URI.createURI(uri));
		resourceSet.getResources().add(resource);
		resource.getContents().addAll(List.of(roots));
		return resource;
	}
}
