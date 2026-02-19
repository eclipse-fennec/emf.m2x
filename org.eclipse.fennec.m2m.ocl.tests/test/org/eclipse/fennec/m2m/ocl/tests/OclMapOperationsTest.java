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

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL Map operations (OCL v2.5).
 *
 * <p>Ported from Eclipse OCL {@code EvaluateMapOperationsTest4}.
 */
class OclMapOperationsTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Construction ---

	@Test
	void mapLiteral_withKeyword() throws OclParseException {
		Object result = eval("Map{'a' with 1, 'b' with 2}", self);
		assertInstanceOf(Map.class, result);
		@SuppressWarnings("unchecked")
		Map<Object, Object> map = (Map<Object, Object>) result;
		assertEquals(1, map.get("a"));
		assertEquals(2, map.get("b"));
	}

	@Test
	void mapLiteral_withArrow() throws OclParseException {
		Object result = eval("Map{'x' <- 10, 'y' <- 20}", self);
		assertInstanceOf(Map.class, result);
		@SuppressWarnings("unchecked")
		Map<Object, Object> map = (Map<Object, Object>) result;
		assertEquals(10, map.get("x"));
		assertEquals(20, map.get("y"));
	}

	@Test
	void mapLiteral_empty() throws OclParseException {
		Object result = eval("Map{}", self);
		assertInstanceOf(Map.class, result);
		assertTrue(((Map<?, ?>) result).isEmpty());
	}

	// --- size ---

	@Test
	void size_nonEmpty() throws OclParseException {
		assertEquals(2, eval("Map{'a' with 1, 'b' with 2}->size()", self));
	}

	@Test
	void size_empty() throws OclParseException {
		assertEquals(0, eval("Map{}->size()", self));
	}

	// --- isEmpty / notEmpty ---

	@Test
	void isEmpty_empty() throws OclParseException {
		assertEquals(true, eval("Map{}->isEmpty()", self));
	}

	@Test
	void isEmpty_nonEmpty() throws OclParseException {
		assertEquals(false, eval("Map{'a' with 1}->isEmpty()", self));
	}

	@Test
	void notEmpty_empty() throws OclParseException {
		assertEquals(false, eval("Map{}->notEmpty()", self));
	}

	@Test
	void notEmpty_nonEmpty() throws OclParseException {
		assertEquals(true, eval("Map{'a' with 1}->notEmpty()", self));
	}

	// --- includes / excludes (key-based) ---

	@Test
	void includes_presentKey() throws OclParseException {
		assertEquals(true, eval("Map{'a' with 1, 'b' with 2}->includes('a')", self));
	}

	@Test
	void includes_absentKey() throws OclParseException {
		assertEquals(false, eval("Map{'a' with 1, 'b' with 2}->includes('c')", self));
	}

	@Test
	void includes_valueNotKey() throws OclParseException {
		// includes checks keys, not values
		assertEquals(false, eval("Map{'a' with 1, 'b' with 2}->includes(1)", self));
	}

	@Test
	void excludes_presentKey() throws OclParseException {
		assertEquals(false, eval("Map{'a' with 1}->excludes('a')", self));
	}

	@Test
	void excludes_absentKey() throws OclParseException {
		assertEquals(true, eval("Map{'a' with 1}->excludes('c')", self));
	}

	// --- includesValue / excludesValue ---

	@Test
	void includesValue_present() throws OclParseException {
		assertEquals(true, eval("Map{'a' with 1, 'b' with 2}->includesValue(1)", self));
	}

	@Test
	void includesValue_absent() throws OclParseException {
		assertEquals(false, eval("Map{'a' with 1, 'b' with 2}->includesValue(3)", self));
	}

	@Test
	void excludesValue_present() throws OclParseException {
		assertEquals(false, eval("Map{'a' with 1}->excludesValue(1)", self));
	}

	@Test
	void excludesValue_absent() throws OclParseException {
		assertEquals(true, eval("Map{'a' with 1}->excludesValue(3)", self));
	}

	// --- get (at) ---

	@Test
	void get_existingKey() throws OclParseException {
		assertEquals(2, eval("Map{'a' with 1, 'b' with 2}->get('b')", self));
	}

	@Test
	void get_missingKey() throws OclParseException {
		// get on missing key returns null
		assertEquals(null, eval("Map{'a' with 1}->get('z')", self));
	}

	// --- keys / values ---

	@Test
	void keys() throws OclParseException {
		Object result = eval("Map{'a' with 1, 'b' with 2}->keys()", self);
		assertInstanceOf(Set.class, result);
		@SuppressWarnings("unchecked")
		Set<Object> keys = (Set<Object>) result;
		assertEquals(2, keys.size());
		assertTrue(keys.contains("a"));
		assertTrue(keys.contains("b"));
	}

	@Test
	void values() throws OclParseException {
		Object result = eval("Map{'a' with 1, 'b' with 2}->values()", self);
		assertInstanceOf(List.class, result);
		@SuppressWarnings("unchecked")
		List<Object> vals = (List<Object>) result;
		assertEquals(2, vals.size());
		assertTrue(vals.contains(1));
		assertTrue(vals.contains(2));
	}

	// --- including / excluding ---

	@Test
	void including_newKey() throws OclParseException {
		@SuppressWarnings("unchecked")
		Map<Object, Object> result = (Map<Object, Object>) eval("Map{'a' with 1}->including('b', 2)", self);
		assertEquals(2, result.size());
		assertEquals(1, result.get("a"));
		assertEquals(2, result.get("b"));
	}

	@Test
	void including_existingKey_replaces() throws OclParseException {
		@SuppressWarnings("unchecked")
		Map<Object, Object> result = (Map<Object, Object>) eval("Map{'a' with 1}->including('a', 99)", self);
		assertEquals(1, result.size());
		assertEquals(99, result.get("a"));
	}

	@Test
	void excluding_existingKey() throws OclParseException {
		@SuppressWarnings("unchecked")
		Map<Object, Object> result = (Map<Object, Object>) eval("Map{'a' with 1, 'b' with 2}->excluding('a')", self);
		assertEquals(1, result.size());
		assertEquals(2, result.get("b"));
	}

	@Test
	void excluding_missingKey() throws OclParseException {
		@SuppressWarnings("unchecked")
		Map<Object, Object> result = (Map<Object, Object>) eval("Map{'a' with 1}->excluding('z')", self);
		assertEquals(1, result.size());
	}

	// --- Equality ---

	@Test
	void equal_sameMaps() throws OclParseException {
		assertEquals(true, eval("Map{'a' with 1, 'b' with 2} = Map{'a' with 1, 'b' with 2}", self));
	}

	@Test
	void equal_differentMaps() throws OclParseException {
		assertEquals(false, eval("Map{'a' with 1} = Map{'a' with 2}", self));
	}

	@Test
	void notEqual() throws OclParseException {
		assertEquals(true, eval("Map{'a' with 1} <> Map{'b' with 1}", self));
	}

	@Test
	void equal_emptyMaps() throws OclParseException {
		assertEquals(true, eval("Map{} = Map{}", self));
	}
}
