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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL if/then/else/endif and let/in expressions.
 */
class OclIfLetTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- If/Then/Else ---

	@Test
	void ifTrue() throws OclParseException {
		assertEquals(1, eval("if true then 1 else 2 endif", self));
	}

	@Test
	void ifFalse() throws OclParseException {
		assertEquals(2, eval("if false then 1 else 2 endif", self));
	}

	@Test
	void ifWithExpression() throws OclParseException {
		assertEquals("yes", eval("if 1 < 2 then 'yes' else 'no' endif", self));
	}

	@Test
	void ifWithPropertyAccess() throws OclParseException {
		assertEquals("married", eval(
				"if self.isMarried then 'married' else 'single' endif", self));
	}

	@Test
	void ifNested() throws OclParseException {
		assertEquals(3, eval(
				"if false then 1 else if false then 2 else 3 endif endif", self));
	}

	@Test
	void ifInvalid_condition_returnsInvalid() throws OclParseException {
		assertInvalid("if invalid then 1 else 2 endif", self);
	}

	// --- Let/In ---

	@Test
	void letSimple() throws OclParseException {
		assertEquals(10, eval("let x : Integer = 5 in x + x", self));
	}

	@Test
	void letWithString() throws OclParseException {
		assertEquals("hello world", eval("let s : String = 'hello' in s.concat(' world')", self));
	}

	@Test
	void letNested() throws OclParseException {
		assertEquals(8, eval(
				"let x : Integer = 3 in let y : Integer = 5 in x + y", self));
	}

	@Test
	void letShadowing() throws OclParseException {
		// Inner let shadows outer
		assertEquals(10, eval(
				"let x : Integer = 5 in let x : Integer = 10 in x", self));
	}

	@Test
	void letWithPropertyAccess() throws OclParseException {
		assertEquals("Alice Smith", eval(
				"let first : String = self.name in first.concat(' Smith')", self));
	}

	// --- Combined ---

	@Test
	void letInIf() throws OclParseException {
		assertEquals("high", eval(
				"let threshold : Integer = 40000 in " +
				"if self.salary > threshold then 'high' else 'low' endif", self));
	}
}
