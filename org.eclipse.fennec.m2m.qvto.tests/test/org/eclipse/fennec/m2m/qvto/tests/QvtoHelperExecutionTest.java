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
package org.eclipse.fennec.m2m.qvto.tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.junit.jupiter.api.Test;

/**
 * Tests for QVT-O helper and query execution.
 */
class QvtoHelperExecutionTest extends AbstractQvtoEngineTest {

	@Test
	void helper_noArgs_returnsValue() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper getFortyTwo() : Integer { return 42; }
				    main() { getFortyTwo(); }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	@Test
	void helper_withArgs_returnsSum() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper add(a : Integer, b : Integer) : Integer { return a + b; }
				    main() { add(3, 4); }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	@Test
	void query_noArgs_returnsValue() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    query greeting() : String = 'Hello';
				    main() { greeting(); }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	@Test
	void helper_callsOclStringOp() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper upper(s : String) : String { return s.toUpperCase(); }
				    main() { upper('hello'); }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	@Test
	void helper_blockBody_multipleStatements() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper compute() : Integer {
				        var x : Integer := 10;
				        var y : Integer := 20;
				        return x + y;
				    }
				    main() { compute(); }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	@Test
	void helper_callingOtherHelper() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper double(n : Integer) : Integer { return n * 2; }
				    helper quadruple(n : Integer) : Integer { return double(double(n)); }
				    main() { quadruple(5); }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	@Test
	void helper_expressionBody() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper getValue() : Integer = 42;
				    main() { getValue(); }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}
}
