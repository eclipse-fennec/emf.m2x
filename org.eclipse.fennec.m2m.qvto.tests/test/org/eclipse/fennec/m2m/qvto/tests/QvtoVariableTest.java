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
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
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
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	@Test
	void varScope_innerShadowsOuter() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : Integer := 10;
				        compute (r) {
				            var x : Integer := 20;
				            r := x;
				        };
				    }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
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
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
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
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
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
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}
}
