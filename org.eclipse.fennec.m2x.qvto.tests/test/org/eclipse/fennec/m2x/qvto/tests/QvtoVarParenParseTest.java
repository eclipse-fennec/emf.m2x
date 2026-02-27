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
package org.eclipse.fennec.m2x.qvto.tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Test;

/**
 * Tests for N7: var with parentheses (§8.4.7).
 * Spec EBNF: {@code <var_init_exp> ::= 'var' <declarator_list> | 'var' '(' <declarator_list> ')'}
 */
class QvtoVarParenParseTest extends AbstractQvtoParserTest {

	@Test
	void varWithParentheses_singleDeclarator_parsesSuccessfully() throws QvtoParseException {
		// §8.4.7: var '(' declarator ')'
		OperationalTransformation t = parse("""
				transformation test();
				main() {
				    var (x := 42);
				    log(x.repr());
				}
				""");
		assertNotNull(t);
	}

	@Test
	void varWithParentheses_multipleDeclarators_parsesSuccessfully() throws QvtoParseException {
		// §8.4.7: var '(' declarator_list ')'
		OperationalTransformation t = parse("""
				transformation test();
				main() {
				    var (x := 1, y := 2);
				    log((x + y).repr());
				}
				""");
		assertNotNull(t);
	}

	@Test
	void varWithParentheses_withType_parsesSuccessfully() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation test();
				main() {
				    var (x : Integer := 42);
				    log(x.repr());
				}
				""");
		assertNotNull(t);
	}

	@Test
	void varWithoutParentheses_stillWorks() throws QvtoParseException {
		// Regression: existing form must still work
		OperationalTransformation t = parse("""
				transformation test();
				main() {
				    var x := 42;
				    log(x.repr());
				}
				""");
		assertNotNull(t);
	}
}
