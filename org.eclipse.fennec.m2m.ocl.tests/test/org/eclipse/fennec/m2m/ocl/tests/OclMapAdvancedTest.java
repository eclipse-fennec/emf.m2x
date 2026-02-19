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
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Advanced tests for OCL Map operations: union, at, null/invalid edge cases,
 * chained operations, and count.
 */
class OclMapAdvancedTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// === at (alias for get) ===

	@Test
	void at_existingKey() throws OclParseException {
		assertEquals(2, eval("Map{'a' <- 1, 'b' <- 2}->at('b')", self));
	}

	@Test
	void at_missingKey() throws OclParseException {
		assertNull(eval("Map{'a' <- 1}->at('z')", self));
	}

	// === union ===

	@Test
	void union_twoMaps() throws OclParseException {
		@SuppressWarnings("unchecked")
		Map<Object, Object> result = (Map<Object, Object>) eval(
				"Map{'a' <- 1}->union(Map{'b' <- 2})", self);
		assertEquals(2, result.size());
		assertEquals(1, result.get("a"));
		assertEquals(2, result.get("b"));
	}

	@Test
	void union_overlappingKeys() throws OclParseException {
		// Second map's values should override first
		@SuppressWarnings("unchecked")
		Map<Object, Object> result = (Map<Object, Object>) eval(
				"Map{'a' <- 1, 'b' <- 2}->union(Map{'b' <- 99, 'c' <- 3})", self);
		assertEquals(3, result.size());
		assertEquals(99, result.get("b"));
	}

	@Test
	void union_withEmptyMap() throws OclParseException {
		@SuppressWarnings("unchecked")
		Map<Object, Object> result = (Map<Object, Object>) eval(
				"Map{'a' <- 1}->union(Map{})", self);
		assertEquals(1, result.size());
	}

	@Test
	void union_emptyWithNonEmpty() throws OclParseException {
		@SuppressWarnings("unchecked")
		Map<Object, Object> result = (Map<Object, Object>) eval(
				"Map{}->union(Map{'x' <- 42})", self);
		assertEquals(1, result.size());
		assertEquals(42, result.get("x"));
	}

	// === Chained Map operations ===

	@Test
	void chainedIncluding() throws OclParseException {
		@SuppressWarnings("unchecked")
		Map<Object, Object> result = (Map<Object, Object>) eval(
				"Map{}->including('a', 1)->including('b', 2)->including('c', 3)", self);
		assertEquals(3, result.size());
	}

	@Test
	void chainedExcluding() throws OclParseException {
		@SuppressWarnings("unchecked")
		Map<Object, Object> result = (Map<Object, Object>) eval(
				"Map{'a' <- 1, 'b' <- 2, 'c' <- 3}->excluding('a')->excluding('c')", self);
		assertEquals(1, result.size());
		assertEquals(2, result.get("b"));
	}

	@Test
	void keys_then_size() throws OclParseException {
		assertEquals(3, eval(
				"Map{'a' <- 1, 'b' <- 2, 'c' <- 3}->keys()->size()", self));
	}

	@Test
	void values_then_sum() throws OclParseException {
		assertEquals(6, eval(
				"Map{'a' <- 1, 'b' <- 2, 'c' <- 3}->values()->sum()", self));
	}

	// === Null values in maps ===

	@Test
	void map_nullValue() throws OclParseException {
		assertNull(eval("Map{'a' <- null}->get('a')", self));
	}

	@Test
	void map_nullValue_includesValue() throws OclParseException {
		assertEquals(true, eval("Map{'a' <- null}->includesValue(null)", self));
	}

	// === Invalid source ===

	@Test
	void invalid_map_size() throws OclParseException {
		assertInvalid("invalid->size()", self);
	}

	// === Integer keys ===

	@Test
	void map_integerKeys() throws OclParseException {
		@SuppressWarnings("unchecked")
		Map<Object, Object> result = (Map<Object, Object>) eval(
				"Map{1 <- 'one', 2 <- 'two', 3 <- 'three'}", self);
		assertEquals(3, result.size());
		// Verify via OCL get operation instead of Java Map.get
		assertEquals("one", eval("Map{1 <- 'one', 2 <- 'two', 3 <- 'three'}->get(1)", self));
	}

	@Test
	void map_integerKeys_get() throws OclParseException {
		assertEquals("two", eval("Map{1 <- 'one', 2 <- 'two'}->get(2)", self));
	}

	// === Map equality with computed maps ===

	@Test
	void map_equality_afterIncluding() throws OclParseException {
		assertEquals(true, eval(
				"Map{'a' <- 1}->including('b', 2) = Map{'a' <- 1, 'b' <- 2}", self));
	}

	@Test
	void map_equality_afterExcluding() throws OclParseException {
		assertEquals(true, eval(
				"Map{'a' <- 1, 'b' <- 2}->excluding('b') = Map{'a' <- 1}", self));
	}
}
