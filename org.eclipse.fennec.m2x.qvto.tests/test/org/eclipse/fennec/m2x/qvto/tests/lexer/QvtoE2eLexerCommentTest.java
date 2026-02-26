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
package org.eclipse.fennec.m2x.qvto.tests.lexer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for QVT-O comment syntax (GAP-23).
 *
 * <p>QVT-O v1.3 §8.4.2 defines three comment styles:
 * <ul>
 *   <li>{@code --} line comment (OCL style)</li>
 *   <li>{@code //} line comment (Java/C++ style)</li>
 *   <li>{@code /* ... * /} block comment</li>
 * </ul>
 *
 * <p>Eclipse reference test: {@code slashSingleLineComments_266478.qvto}
 *
 * <p><b>SPEC-FIRST:</b> Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eLexerCommentTest extends AbstractQvtoEngineTest {

	// ---- §8.4.2: '//' line comments ----

	@Test
	void comment_slashSlashLineComment() throws Exception {
		// Eclipse slashSingleLineComments_266478: // some thoughtful comment
		QvtoExecutionResult result = execute("""
				// top-level comment
				transformation test() {
				    // comment before main
				    main() {
				        // comment inside body
				        var x := 42; // end-of-line comment
				        log('val:' + x.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "val:42");
	}

	@Test
	void comment_slashSlashAndDashDashMixed() throws Exception {
		// Both comment styles in the same transformation
		QvtoExecutionResult result = execute("""
				// slash comment
				-- dash comment
				transformation test() {
				    main() {
				        var a := 1; // slash end-of-line
				        var b := 2; -- dash end-of-line
				        log('sum:' + (a + b).repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "sum:3");
	}

	@Test
	void comment_slashSlashDoesNotAffectStringLiterals() throws Exception {
		// '//' inside a string must NOT be treated as a comment
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var url := 'http://example.com';
				        log('val:' + url);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "val:http://example.com");
	}

	@Test
	void comment_slashSlashDoesNotAffectDoubleQuotedStrings() throws Exception {
		// '//' inside a double-quoted string must NOT be treated as a comment
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var url := "http://example.com";
				        log('val:' + url);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "val:http://example.com");
	}

	// ---- Helpers ----

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
