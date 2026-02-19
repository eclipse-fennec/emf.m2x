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
package org.eclipse.fennec.m2m.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for the OCL closure iterator.
 * closure(body) recursively applies the body expression and collects
 * all reachable elements (breadth-first, stops on cycles).
 */
class OclClosureIteratorTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Closure on flat collections (identity) ---

	@Test
	void closure_identity() throws OclParseException {
		// closure on a non-collection body element stops after one level
		Object result = eval("Set{1, 2, 3}->closure(i | i)", self);
		assertInstanceOf(Set.class, result);
		@SuppressWarnings("unchecked")
		Set<Object> set = (Set<Object>) result;
		assertEquals(3, set.size());
		assertTrue(set.contains(1));
		assertTrue(set.contains(2));
		assertTrue(set.contains(3));
	}

	// --- Closure on nested sets ---

	@Test
	void closure_nestedSets() throws OclParseException {
		// closure flattens nested collections recursively
		Object result = eval("Set{Set{1, 2}, Set{3, 4}}->closure(s | s)", self);
		assertInstanceOf(Set.class, result);
		@SuppressWarnings("unchecked")
		Set<Object> set = (Set<Object>) result;
		// Should contain: Set{1,2}, Set{3,4}, 1, 2, 3, 4
		assertTrue(set.contains(1));
		assertTrue(set.contains(2));
		assertTrue(set.contains(3));
		assertTrue(set.contains(4));
	}

	// --- Closure on empty collection ---

	@Test
	void closure_empty() throws OclParseException {
		Object result = eval("Set{}->closure(i | i)", self);
		assertInstanceOf(Set.class, result);
		assertTrue(((Set<?>) result).isEmpty());
	}

	// --- Closure result is always a Set ---

	@Test
	void closure_returnsSet() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3}->closure(i | i)", self);
		assertInstanceOf(Set.class, result);
	}

	// --- Closure with computation ---

	@Test
	void closure_multiplication() throws OclParseException {
		// Start with {2}, closure(i | i * 2) → {2, 4, 8, 16, ...} but we need termination
		// Since we only have integers and closure stops on duplicates, this would be infinite
		// Let's test with a bounded example instead
		Object result = eval("Set{1}->closure(i | if i < 4 then Set{i + 1} else Set{} endif)", self);
		assertInstanceOf(Set.class, result);
		@SuppressWarnings("unchecked")
		Set<Object> set = (Set<Object>) result;
		assertTrue(set.contains(1));
		assertTrue(set.contains(2));
		assertTrue(set.contains(3));
		assertTrue(set.contains(4));
	}
}
