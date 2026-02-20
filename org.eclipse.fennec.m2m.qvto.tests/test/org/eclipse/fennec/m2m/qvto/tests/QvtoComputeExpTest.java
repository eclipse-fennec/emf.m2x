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
 * Tests for QVT-O compute expression.
 */
class QvtoComputeExpTest extends AbstractQvtoEngineTest {

	@Test
	void compute_basic() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : Integer := compute (r) { r := 42; };
				        log(x.toString());
				    }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	@Test
	void compute_nested() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : Integer := compute (outer) {
				            var y : Integer := compute (inner) {
				                inner := 10;
				            };
				            outer := y + 5;
				        };
				        log(x.toString());
				    }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	@Test
	void compute_withLoop() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var sum : Integer := compute (r) {
				            r := 0;
				            var i : Integer := 1;
				            while (i <= 5) {
				                r := r + i;
				                i := i + 1;
				            };
				        };
				        log(sum.toString());
				    }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}
}
