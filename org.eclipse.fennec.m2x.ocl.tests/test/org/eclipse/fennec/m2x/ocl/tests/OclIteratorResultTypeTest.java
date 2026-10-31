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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.ocl.CollectionKind;
import org.eclipse.fennec.m2x.model.ocl.CollectionType;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.junit.jupiter.api.Test;

/**
 * Result types of the predefined iterators, OCL v2.4 §11.9 (#378), and what they make possible:
 * a dot after a selecting iterator is an implicit collect (§7.6.2, #320).
 */
class OclIteratorResultTypeTest extends AbstractOclTest {

	private static CollectionType collectionTypeOf(String expression) throws Exception {
		OclExpression ast = engine.parse(expression, companyClass);
		return assertInstanceOf(CollectionType.class, ast.getType(), () -> expression + " : " + ast.getType());
	}

	@Test
	void select_keepsTheSourceKind_andTheElementType() throws Exception {
		CollectionType type = collectionTypeOf("self.employees->select(p | p.age > 30)");
		assertSame(personClass, type.getElementType());
		CollectionType sourceType = (CollectionType) engine.parse("self.employees", companyClass).getType();
		assertEquals(sourceType.getKind(), type.getKind());
	}

	@Test
	void collect_isASequenceOrABag_ofTheFlattenedBodyType() throws Exception {
		CollectionType ordered = collectionTypeOf("self.employees->collect(p | p.name)");
		assertEquals(CollectionKind.SEQUENCE, ordered.getKind());
		assertEquals("EString", ordered.getElementType().getName());
		assertEquals(CollectionKind.BAG, collectionTypeOf("Set{1, 2}->collect(i | i * 2)").getKind());
	}

	@Test
	void sortedByAndClosure_answerOrderedAndUniqueKinds() throws Exception {
		assertEquals(CollectionKind.ORDERED_SET, collectionTypeOf("Set{3, 1}->sortedBy(i | i)").getKind());
		assertEquals(CollectionKind.SEQUENCE, collectionTypeOf("Bag{3, 1}->sortedBy(i | i)").getKind());
		assertEquals(CollectionKind.SET, collectionTypeOf("Set{1}->closure(i | Set{})").getKind());
	}

	@Test
	void anyAndTheQuantifiers_answerTheElementTypeAndBoolean() throws Exception {
		assertSame(personClass, engine.parse("self.employees->any(p | p.age > 30)", companyClass).getType());
		assertEquals("Boolean", engine.parse("self.employees->forAll(p | p.age > 30)", companyClass).getType().getName());
	}

	@Test
	void aDotAfterSelect_collects() throws Exception {
		// §7.6.2: collection.prop = collection->collect(prop) — before #378 select was typed with the
		// element type, so the dot navigated one element instead of collecting
		EObject company = createCompany("C", createPerson("Ann", 40, 1.0, false), createPerson("Bob", 20, 1.0, false),
				createPerson("Cid", 50, 1.0, false));
		assertEquals(List.of("Ann", "Cid"), List.copyOf((Collection<?>) eval("self.employees->select(p | p.age > 30).name", company)));
	}

	@Test
	void anIteratorOverElementsOfUnknownType_isUntyped() throws Exception {
		// A tuple literal is untyped, so collect answers Sequence(?) — its iterator is no collection
		OclExpression ast = engine.parse("self.employees->collect(p | Tuple{n = p.name})->select(t | t.n <> '')", companyClass);
		CollectionType type = assertInstanceOf(CollectionType.class, ast.getType());
		assertNull(type.getElementType());
		EObject company = createCompany("C", createPerson("Ann", 40, 1.0, false));
		assertEquals(1L, ((Number) eval("self.employees->collect(p | Tuple{n = p.name})->select(t | t.n <> '')->size()", company)).longValue());
	}
}
