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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.junit.jupiter.api.Test;

/**
 * Tests for QVT-O log() and assert() expressions.
 */
class QvtoLogAssertTest extends AbstractQvtoEngineTest {

	@Test
	void log_producesInfoDiagnostic() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() { log('hello world'); }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() == Diagnostic.INFO
						&& d.getMessage().contains("hello world")),
				() -> "Expected INFO diagnostic with 'hello world': " + result.diagnostics());
	}

	@Test
	void assertTrue_noDiagnostic() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() { assert fatal (true); }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	@Test
	void assertFalse_producesErrorDiagnostic() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() { assert error (false); }
				}
				""");
		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() == Diagnostic.ERROR),
				() -> "Expected ERROR diagnostic: " + result.diagnostics());
	}

	@Test
	void assertFalse_withWarning() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() { assert warning (1 = 2); }
				}
				""");
		assertNotNull(result);
		// Warning does not count as failure
		assertTrue(result.isSuccess());
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() == Diagnostic.WARNING),
				() -> "Expected WARNING diagnostic: " + result.diagnostics());
	}

	@Test
	void assertFalse_withLogMessage() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() { assert error (false) with log('expected failure'); }
				}
				""");
		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() == Diagnostic.ERROR
						&& d.getMessage().contains("expected failure")),
				() -> "Expected ERROR with message: " + result.diagnostics());
	}
}
