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

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * What the collection operations promise at the expression level (#187).
 *
 * <p>These pin the behaviour a template or a constraint sees. The two classes underneath are
 * held to their own contracts by {@code OclOrderedSetContractTest} and
 * {@code OclCollectionUtilTest}, which is where the defects of #187 actually were.
 */
class OclCollectionContractTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	@Test
	@DisplayName("OrderedSet->union keeps a value once")
	void orderedSetUnionHasNoDuplicates() throws OclParseException {
		Object result = eval("OrderedSet{1, 2, 3}->union(OrderedSet{3, 4})", self);

		assertEquals(4, ((Collection<?>) result).size(), () -> "got " + result);
		assertEquals("[1, 2, 3, 4]", result.toString());
	}

	@Test
	@DisplayName("OrderedSet->union with a Sequence keeps a value once")
	void orderedSetUnionSequenceHasNoDuplicates() throws OclParseException {
		Object result = eval("OrderedSet{'a', 'b'}->union(Sequence{'b', 'b', 'c'})", self);

		assertEquals(3, ((Collection<?>) result).size(), () -> "got " + result);
	}

	@Test
	@DisplayName("OrderedSet->including an element it has changes nothing")
	void orderedSetIncludingKnownElement() throws OclParseException {
		Object result = eval("OrderedSet{1, 2}->including(2)", self);

		assertEquals(2, ((Collection<?>) result).size(), () -> "got " + result);
	}

	@Test
	@DisplayName("sortedBy over values of different types produces all of them")
	void sortedByOverMixedTypes() throws OclParseException {
		// The order between values of different types is arbitrary — OCL defines none — but it
		// has to be an order, or the sort refuses to run. OclCollectionUtilTest pins the
		// comparator itself; this pins that the expression works.
		Object result = eval("Sequence{'b', 1, 'a', true, 2, false}->sortedBy(e | e)", self);

		assertEquals(6, ((Collection<?>) result).size(), () -> "got " + result);
	}
}
