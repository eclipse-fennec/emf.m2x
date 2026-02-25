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

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL Tuple operations.
 *
 * <p>Ported from Eclipse OCL {@code EvaluateTupleOperationsTest4}.
 */
class OclTupleOperationsTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Construction ---

	@Test
	void tupleLiteral_twoparts() throws OclParseException {
		Object result = eval("Tuple{a: Integer = 3, b: String = '4'}", self);
		assertInstanceOf(Map.class, result);
		@SuppressWarnings("unchecked")
		Map<String, Object> tuple = (Map<String, Object>) result;
		assertEquals(3, tuple.get("a"));
		assertEquals("4", tuple.get("b"));
	}

	@Test
	void tupleLiteral_singlePart() throws OclParseException {
		@SuppressWarnings("unchecked")
		Map<String, Object> tuple = (Map<String, Object>) eval("Tuple{x: Integer = 42}", self);
		assertEquals(42, tuple.get("x"));
	}

	// --- Part access ---

	@Test
	void partAccess_a() throws OclParseException {
		assertEquals(3, eval("Tuple{a: Integer = 3, b: String = '4'}.a", self));
	}

	@Test
	void partAccess_b() throws OclParseException {
		assertEquals("4", eval("Tuple{a: Integer = 3, b: String = '4'}.b", self));
	}

	@Test
	void partAccess_nested() throws OclParseException {
		assertEquals("3", eval("Tuple{a: Integer = 3, b = Tuple{a: String = '3', b: Real = 3.1}}.b.a", self));
	}

	@Test
	void partAccess_deeplyNested() throws OclParseException {
		assertEquals(3.1, eval("Tuple{a: Integer = 3, b = Tuple{a: String = '3', b = Tuple{a: Real = 3.1}}}.b.b.a", self));
	}

	// --- Equality ---

	@Test
	void equal_sameTuples() throws OclParseException {
		assertEquals(true, eval("Tuple{a: Integer = 3, b: String = '4'} = Tuple{a: Integer = 3, b: String = '4'}", self));
	}

	@Test
	void equal_differentValues() throws OclParseException {
		assertEquals(false, eval("Tuple{a: Integer = 3, b: String = '4'} = Tuple{a: Integer = 3, b: String = '5'}", self));
	}

	@Test
	void notEqual_same() throws OclParseException {
		assertEquals(false, eval("Tuple{a: Integer = 3, b: String = '4'} <> Tuple{a: Integer = 3, b: String = '4'}", self));
	}

	@Test
	void notEqual_different() throws OclParseException {
		assertEquals(true, eval("Tuple{a: Integer = 3, b: String = '4'} <> Tuple{a: Integer = 5, b: String = '4'}", self));
	}

	@Test
	void equal_nestedTuples() throws OclParseException {
		assertEquals(true, eval(
				"Tuple{a: Integer = 3, b = Tuple{x: String = 'hello'}} = " +
				"Tuple{a: Integer = 3, b = Tuple{x: String = 'hello'}}", self));
	}

	@Test
	void notEqual_nestedTuples() throws OclParseException {
		assertEquals(true, eval(
				"Tuple{a: Integer = 3, b = Tuple{x: String = 'hello'}} <> " +
				"Tuple{a: Integer = 3, b = Tuple{x: String = 'world'}}", self));
	}

	// --- Tuple in collections ---

	@Test
	void tupleInSequence() throws OclParseException {
		Object result = eval("Sequence{Tuple{x: Integer = 1}, Tuple{x: Integer = 2}}->size()", self);
		assertEquals(2, result);
	}

	@Test
	void tupleInSet() throws OclParseException {
		Object result = eval("Set{Tuple{x: Integer = 1}, Tuple{x: Integer = 2}, Tuple{x: Integer = 1}}->size()", self);
		assertEquals(2, result);
	}
}
