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

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The runtime collection kind of a navigation follows the feature's {@code unique} and
 * {@code ordered} flags (OCL v2.4 §7.5.3, §7.5.11, #319): unique+ordered is an OrderedSet,
 * unique is a Set, ordered is a Sequence, neither is a Bag.
 */
class OclNavigationKindTest extends AbstractOclTest {

	static EObject company;
	static EObject holder;

	@BeforeAll
	static void setUp() {
		company = createCompany("ACME", createPerson("Alice", 30, 1.0, true), createPerson("Bob", 25, 1.0, false),
				createPerson("Carl", 40, 1.0, false));
		holder = createHolder();
	}

	/** A class with one many-valued Integer attribute per collection kind. */
	@SuppressWarnings("unchecked")
	static EObject createHolder() {
		EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
		pkg.setName("kinds");
		pkg.setNsURI("http://test/kinds");
		pkg.setNsPrefix("kinds");
		EClass holderClass = EcoreFactory.eINSTANCE.createEClass();
		holderClass.setName("Holder");
		pkg.getEClassifiers().add(holderClass);
		holderClass.getEStructuralFeatures().add(attribute("oset", true, true));
		holderClass.getEStructuralFeatures().add(attribute("set", true, false));
		holderClass.getEStructuralFeatures().add(attribute("seq", false, true));
		holderClass.getEStructuralFeatures().add(attribute("bag", false, false));
		EObject instance = pkg.getEFactoryInstance().create(holderClass);
		for (String name : List.of("oset", "set", "seq", "bag")) {
			List<Object> values = (List<Object>) instance.eGet(holderClass.getEStructuralFeature(name));
			values.addAll(List.of(1, 2));
			if (name.equals("seq") || name.equals("bag")) {
				values.add(1);
			}
		}
		return instance;
	}

	static EAttribute attribute(String name, boolean unique, boolean ordered) {
		EAttribute attribute = EcoreFactory.eINSTANCE.createEAttribute();
		attribute.setName(name);
		attribute.setEType(EcorePackage.Literals.EINT);
		attribute.setUpperBound(-1);
		attribute.setUnique(unique);
		attribute.setOrdered(ordered);
		return attribute;
	}

	@Test
	void orderedUniqueReference_isAnOrderedSet() throws OclParseException {
		assertEquals(3, eval("self.employees->including(self.employees->first())->size()", company));
		assertEquals(true, eval("self.employees = self.employees->asOrderedSet()", company));
		assertEquals(false, eval("self.employees = self.employees->asSequence()", company));
		assertEquals(true, eval("self.employees->oclIsKindOf(OrderedSet(Person))", company));
	}

	@Test
	void eachKind_followsTheFlags() throws OclParseException {
		assertEquals(true, eval("self.oset->oclIsTypeOf(OrderedSet(Integer))", holder));
		assertEquals(true, eval("self.set->oclIsTypeOf(Set(Integer))", holder));
		assertEquals(true, eval("self.seq->oclIsTypeOf(Sequence(Integer))", holder));
		assertEquals(true, eval("self.bag->oclIsTypeOf(Bag(Integer))", holder));
	}

	@Test
	void kindDecidesTheCollectionOperations() throws OclParseException {
		assertEquals(2, eval("self.set->including(1)->size()", holder));
		assertEquals(4, eval("self.seq->including(1)->size()", holder));
		assertEquals(2, eval("self.bag->count(1)", holder));
		assertEquals(true, eval("self.set->collect(x | x)->oclIsKindOf(Bag(Integer))", holder));
		assertEquals(true, eval("self.seq->collect(x | x)->oclIsKindOf(Sequence(Integer))", holder));
	}

	@Test
	void modelOrderIsKept() throws OclParseException {
		assertEquals(List.of("Alice", "Bob", "Carl"), eval("self.employees->collect(name)", company));
	}

	@Test
	void collectionOperations_doNotWriteIntoTheModel() throws OclParseException {
		eval("self.employees->including(self.employees->first())", company);
		assertEquals(3, eval("self.employees->size()", company));
		// #182: mutating operations on a navigated feature stay refused
		assertInvalid("self.employees->add(self.employees->first())", company);
		assertEquals(3, eval("self.employees->size()", company));
	}
}
