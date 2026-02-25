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

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Negative tests: OCL expressions that should fail to parse.
 * Verifies that the parser produces proper error diagnostics
 * rather than silently producing incorrect ASTs.
 */
class OclParseErrorTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Syntax errors ---

	@Test
	void missingEndif() {
		assertThrows(OclParseException.class,
				() -> eval("if true then 1 else 2", self));
	}

	@Test
	void missingThen() {
		assertThrows(OclParseException.class,
				() -> eval("if true 1 else 2 endif", self));
	}

	@Test
	void missingElse() {
		assertThrows(OclParseException.class,
				() -> eval("if true then 1 endif", self));
	}

	@Test
	void missingIn() {
		assertThrows(OclParseException.class,
				() -> eval("let x: Integer = 5 x + 1", self));
	}

	@Test
	void unclosedParenthesis() {
		assertThrows(OclParseException.class,
				() -> eval("(1 + 2", self));
	}

	@Test
	void unclosedString() {
		assertThrows(OclParseException.class,
				() -> eval("'hello", self));
	}

	@Test
	void emptyExpression() {
		assertThrows(OclParseException.class,
				() -> eval("", self));
	}

	@Test
	void doubleOperator() {
		assertThrows(OclParseException.class,
				() -> eval("1 + + 2", self));
	}

	// --- Collection literal errors ---

	@Test
	void unclosedSetLiteral() {
		assertThrows(OclParseException.class,
				() -> eval("Set{1, 2, 3", self));
	}

	@Test
	void unclosedSequenceLiteral() {
		assertThrows(OclParseException.class,
				() -> eval("Sequence{1, 2", self));
	}

	// --- Tuple errors ---

	@Test
	void tupleMissingEquals() {
		assertThrows(OclParseException.class,
				() -> eval("Tuple{a: Integer 3}", self));
	}

	@Test
	void tupleMissingClosingBrace() {
		assertThrows(OclParseException.class,
				() -> eval("Tuple{a: Integer = 3", self));
	}

	// --- Iterator errors ---

	@Test
	void iteratorMissingBar() {
		assertThrows(OclParseException.class,
				() -> eval("Sequence{1, 2}->select(i i > 1)", self));
	}

	@Test
	void iteratorMissingParen() {
		assertThrows(OclParseException.class,
				() -> eval("Sequence{1, 2}->select(i | i > 1", self));
	}
}
