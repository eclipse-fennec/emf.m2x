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
package org.eclipse.fennec.m2x.qvto.tests.controlflow;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * E2E tests for N7: var with parentheses (§8.4.7).
 * Spec EBNF: {@code <var_init_exp> ::= 'var' <declarator_list> | 'var' '(' <declarator_list> ')'}
 */
class QvtoE2eVarParenTest extends AbstractQvtoEngineTest {

	@Test
	void varWithParentheses_singleDeclarator_executesCorrectly() throws QvtoParseException {
		QvtoExecutionResult result = execute("""
				transformation test();
				main() {
				    var (x := 42);
				    assert fatal (x = 42) with log('expected 42, got: ' + x.repr());
				}
				""");
		assertTrue(result.isSuccess(), () -> "Transformation should succeed: " + result.diagnostics());
	}

	@Test
	void varWithParentheses_multipleDeclarators_executesCorrectly() throws QvtoParseException {
		QvtoExecutionResult result = execute("""
				transformation test();
				main() {
				    var (x := 10, y := 20);
				    assert fatal (x + y = 30) with log('expected 30, got: ' + (x + y).repr());
				}
				""");
		assertTrue(result.isSuccess(), () -> "Transformation should succeed: " + result.diagnostics());
	}

	@Test
	void varWithParentheses_withType_executesCorrectly() throws QvtoParseException {
		QvtoExecutionResult result = execute("""
				transformation test();
				main() {
				    var (x : Integer := 42);
				    assert fatal (x = 42) with log('expected 42, got: ' + x.repr());
				}
				""");
		assertTrue(result.isSuccess(), () -> "Transformation should succeed: " + result.diagnostics());
	}
}
