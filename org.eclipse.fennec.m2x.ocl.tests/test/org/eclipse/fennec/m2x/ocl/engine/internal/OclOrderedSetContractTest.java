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
package org.eclipse.fennec.m2x.ocl.engine.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * An OrderedSet holds a value once, whichever way it is filled (#187).
 *
 * <p>The class is an {@code ArrayList} that refuses duplicates in {@code add}. Every inherited
 * method that inserts without going through {@code add} was a way past that — {@code addAll}
 * copies straight into the backing array. No OCL expression was affected, because the stdlib
 * builds its result through the constructor, which does deduplicate; these tests hold the class
 * to its own contract so that the next caller of it does not have to know that.
 */
class OclOrderedSetContractTest {

	@Test
	@DisplayName("addAll keeps a value this set already has, once")
	void addAllDeduplicates() {
		OclOrderedSet<Object> set = new OclOrderedSet<>(List.of(1, 2, 3));

		assertTrue(set.addAll(List.of(3, 4)), "4 is new, so something changed");

		assertEquals(List.of(1, 2, 3, 4), set);
	}

	@Test
	@DisplayName("addAll of nothing new answers false")
	void addAllOfKnownValuesChangesNothing() {
		OclOrderedSet<Object> set = new OclOrderedSet<>(List.of(1, 2));

		assertFalse(set.addAll(List.of(1, 2)));

		assertEquals(List.of(1, 2), set);
	}

	@Test
	@DisplayName("add at an index keeps a value this set already has, once")
	void addAtIndexDeduplicates() {
		OclOrderedSet<Object> set = new OclOrderedSet<>(List.of(1, 2, 3));

		set.add(0, 3);

		assertEquals(List.of(1, 2, 3), set);
	}

	@Test
	@DisplayName("add at an index inserts a new value there")
	void addAtIndexInsertsNewValue() {
		OclOrderedSet<Object> set = new OclOrderedSet<>(List.of(1, 3));

		set.add(1, 2);

		assertEquals(List.of(1, 2, 3), set);
	}

	@Test
	@DisplayName("addAll at an index inserts what is new, in order")
	void addAllAtIndexInsertsNewValues() {
		OclOrderedSet<Object> set = new OclOrderedSet<>(List.of(1, 4));

		assertTrue(set.addAll(1, List.of(2, 1, 3)));

		assertEquals(List.of(1, 2, 3, 4), set);
	}
}
