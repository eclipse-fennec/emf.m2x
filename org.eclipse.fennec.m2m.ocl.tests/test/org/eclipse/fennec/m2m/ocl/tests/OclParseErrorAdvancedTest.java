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

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Additional negative parse tests covering more complex
 * syntax error scenarios.
 */
class OclParseErrorAdvancedTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Operator errors ---

	@Test
	void trailingOperator() {
		assertThrows(OclParseException.class,
				() -> eval("1 +", self));
	}

	@Test
	void leadingOperator() {
		assertThrows(OclParseException.class,
				() -> eval("* 2", self));
	}

	@Test
	void doubleComparison() {
		assertThrows(OclParseException.class,
				() -> eval("1 < > 2", self));
	}

	// --- Keyword errors ---

	@Test
	void endifWithoutIf() {
		assertThrows(OclParseException.class,
				() -> eval("endif", self));
	}

	@Test
	void thenWithoutIf() {
		assertThrows(OclParseException.class,
				() -> eval("then 1", self));
	}

	@Test
	void inWithoutLet() {
		assertThrows(OclParseException.class,
				() -> eval("in x", self));
	}

	// --- Let syntax errors ---

	@Test
	void let_missingEquals() {
		assertThrows(OclParseException.class,
				() -> eval("let x: Integer 5 in x", self));
	}

	@Test
	void let_missingExpression() {
		assertThrows(OclParseException.class,
				() -> eval("let x: Integer = in x", self));
	}

	// --- Collection syntax errors ---

	@Test
	void unknownCollectionType() {
		assertThrows(OclParseException.class,
				() -> eval("List{1, 2, 3}", self));
	}

	@Test
	void collection_trailingComma() {
		assertThrows(OclParseException.class,
				() -> eval("Sequence{1, 2, }", self));
	}

	// --- Arrow errors ---

	@Test
	void arrowWithoutOperation() {
		assertThrows(OclParseException.class,
				() -> eval("Sequence{1, 2}->", self));
	}

	// --- Parenthesis errors ---

	@Test
	void extraClosingParen() {
		assertThrows(OclParseException.class,
				() -> eval("(1 + 2))", self));
	}

	@Test
	void mismatchedParens() {
		assertThrows(OclParseException.class,
				() -> eval("((1 + 2)", self));
	}

	// --- Tuple errors ---

	@Test
	void tuple_unclosedBrace() {
		assertThrows(OclParseException.class,
				() -> eval("Tuple{a: Integer = 1", self));
	}

	// --- String errors ---

	@Test
	void unterminatedString() {
		assertThrows(OclParseException.class,
				() -> eval("'hello", self));
	}

	@Test
	void unterminatedString_withContent() {
		assertThrows(OclParseException.class,
				() -> eval("'hello world", self));
	}

	// --- Multiple statements ---

	@Test
	void multipleExpressions() {
		assertThrows(OclParseException.class,
				() -> eval("1 2", self));
	}

	// --- Invalid dot access ---

	@Test
	void dotWithoutProperty() {
		assertThrows(OclParseException.class,
				() -> eval("self.", self));
	}

	@Test
	void doubleDot() {
		assertThrows(OclParseException.class,
				() -> eval("self..name", self));
	}

	// --- Range errors ---

	@Test
	void rangeMissingEnd() {
		assertThrows(OclParseException.class,
				() -> eval("Sequence{1..}", self));
	}

	@Test
	void rangeMissingStart() {
		assertThrows(OclParseException.class,
				() -> eval("Sequence{..5}", self));
	}
}
