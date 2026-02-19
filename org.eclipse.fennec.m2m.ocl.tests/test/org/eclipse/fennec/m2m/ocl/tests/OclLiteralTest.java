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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclInvalid;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL literal expressions: Integer, Real, String, Boolean,
 * null, invalid, collection literals, tuple literals, and map literals.
 */
class OclLiteralTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Integer Literals ---

	@Test
	void integerLiteral_zero() throws OclParseException {
		assertEquals(0L, eval("0", self));
	}

	@Test
	void integerLiteral_positive() throws OclParseException {
		assertEquals(42L, eval("42", self));
	}

	@Test
	void integerLiteral_negative() throws OclParseException {
		assertEquals(-1L, eval("-1", self));
	}

	@Test
	void integerLiteral_large() throws OclParseException {
		assertEquals(1000000L, eval("1000000", self));
	}

	// --- Real Literals ---

	@Test
	void realLiteral_simple() throws OclParseException {
		assertEquals(3.14, eval("3.14", self));
	}

	@Test
	void realLiteral_zero() throws OclParseException {
		assertEquals(0.0, eval("0.0", self));
	}

	@Test
	void realLiteral_negative() throws OclParseException {
		assertEquals(-2.5, eval("-2.5", self));
	}

	// --- String Literals ---

	@Test
	void stringLiteral_simple() throws OclParseException {
		assertEquals("hello", eval("'hello'", self));
	}

	@Test
	void stringLiteral_empty() throws OclParseException {
		assertEquals("", eval("''", self));
	}

	@Test
	void stringLiteral_withSpaces() throws OclParseException {
		assertEquals("hello world", eval("'hello world'", self));
	}

	// --- Boolean Literals ---

	@Test
	void booleanLiteral_true() throws OclParseException {
		assertEquals(true, eval("true", self));
	}

	@Test
	void booleanLiteral_false() throws OclParseException {
		assertEquals(false, eval("false", self));
	}

	// --- Null and Invalid ---

	@Test
	void nullLiteral() throws OclParseException {
		assertNull(eval("null", self));
	}

	@Test
	void invalidLiteral() throws OclParseException {
		assertSame(OclInvalid.INSTANCE, eval("invalid", self));
	}

	// --- Collection Literals ---

	@Test
	void setLiteral_integers() throws OclParseException {
		Object result = eval("Set{1, 2, 3}", self);
		assertInstanceOf(LinkedHashSet.class, result);
		@SuppressWarnings("unchecked")
		LinkedHashSet<Object> set = (LinkedHashSet<Object>) result;
		assertEquals(3, set.size());
		assertTrue(set.contains(1L));
		assertTrue(set.contains(2L));
		assertTrue(set.contains(3L));
	}

	@Test
	void setLiteral_duplicatesRemoved() throws OclParseException {
		Object result = eval("Set{1, 2, 2, 3}", self);
		assertInstanceOf(LinkedHashSet.class, result);
		assertEquals(3, ((Collection<?>) result).size());
	}

	@Test
	void sequenceLiteral_integers() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3}", self);
		assertInstanceOf(List.class, result);
		assertEquals(List.of(1L, 2L, 3L), result);
	}

	@Test
	void sequenceLiteral_preservesDuplicates() throws OclParseException {
		Object result = eval("Sequence{1, 2, 2, 3}", self);
		assertEquals(List.of(1L, 2L, 2L, 3L), result);
	}

	@Test
	void sequenceLiteral_range() throws OclParseException {
		Object result = eval("Sequence{1..5}", self);
		assertEquals(List.of(1L, 2L, 3L, 4L, 5L), result);
	}

	@Test
	void setLiteral_empty() throws OclParseException {
		Object result = eval("Set{}", self);
		assertInstanceOf(LinkedHashSet.class, result);
		assertTrue(((Collection<?>) result).isEmpty());
	}

	@Test
	void sequenceLiteral_empty() throws OclParseException {
		Object result = eval("Sequence{}", self);
		assertInstanceOf(List.class, result);
		assertTrue(((Collection<?>) result).isEmpty());
	}

	// --- Tuple Literals ---

	@Test
	void tupleLiteral_simple() throws OclParseException {
		Object result = eval("Tuple{name: String = 'Alice', age: Integer = 30}", self);
		assertInstanceOf(Map.class, result);
		@SuppressWarnings("unchecked")
		Map<String, Object> tuple = (Map<String, Object>) result;
		assertEquals("Alice", tuple.get("name"));
		assertEquals(30L, tuple.get("age"));
	}

	// --- Map Literals (v2.5) ---

	@Test
	void mapLiteral_simple() throws OclParseException {
		Object result = eval("Map{'a' with 1, 'b' with 2}", self);
		assertInstanceOf(Map.class, result);
		@SuppressWarnings("unchecked")
		Map<Object, Object> map = (Map<Object, Object>) result;
		assertEquals(1L, map.get("a"));
		assertEquals(2L, map.get("b"));
	}

	@Test
	void mapLiteral_empty() throws OclParseException {
		Object result = eval("Map{}", self);
		assertInstanceOf(Map.class, result);
		assertTrue(((Map<?, ?>) result).isEmpty());
	}
}
