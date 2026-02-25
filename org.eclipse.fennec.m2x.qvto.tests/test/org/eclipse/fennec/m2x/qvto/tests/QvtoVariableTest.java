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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.junit.jupiter.api.Test;

/**
 * Tests for QVT-O variable initialization and assignment.
 */
class QvtoVariableTest extends AbstractQvtoEngineTest {

	@Test
	void varInit_withType() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : Integer := 42;
				        log(x.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "42");
	}

	@Test
	void varAssign_simpleReplace() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : Integer := 10;
				        x := 20;
				        log(x.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "20");
	}

	@Test
	void varScope_innerShadowsOuter() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : Integer := 10;
				        var y : Integer := compute (r) {
				            var x : Integer := 20;
				            r := x;
				        };
				        log(y.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "20");
	}

	@Test
	void varInit_stringValue() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var name : String := 'hello';
				        log(name);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "hello");
	}

	@Test
	void varInit_booleanValue() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var flag : Boolean := true;
				        log(flag.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "true");
	}

	@Test
	void varInit_inferredType() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x := 42;
				        log(x.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "42");
	}

	private static void assertSuccess(QvtoExecutionResult result) {
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		boolean found = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains(expected));
		assertTrue(found, "Expected log containing '" + expected
				+ "' but diagnostics were: " + result.diagnostics());
	}
}
