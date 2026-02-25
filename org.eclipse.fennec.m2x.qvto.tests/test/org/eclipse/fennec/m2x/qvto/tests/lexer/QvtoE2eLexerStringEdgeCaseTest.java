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
 * End-to-end tests for lexer/string edge cases (P9-08).
 *
 * <p>Based on QVT-O v1.3 §8.4 (Concrete Syntax) and Eclipse reference tests:
 * <ul>
 *   <li>{@code escape_sequences_250630/escape_sequences_250630.qvto}</li>
 *   <li>{@code doubleQuoteStrings_262734/doubleQuoteStrings_262734.qvto}</li>
 *   <li>{@code multilineStrings_262733/multilineStrings_262733.qvto}</li>
 *   <li>{@code javakeywords/javakeywords.qvto}</li>
 * </ul>
 *
 * <p><b>SPEC-FIRST:</b> Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eLexerStringEdgeCaseTest extends AbstractQvtoEngineTest {

	// ==== P9-08: Lexer/String Edge Cases ====

	// ---- Basic escape sequences in single-quoted strings ----

	@Test
	void string_escapeBackslash() throws Exception {
		// Eclipse escape_sequences_250630: var g := '\\';
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s := '\\\\';
				        log('val:' + s);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "val:\\");
	}

	@Test
	void string_escapeSingleQuote() throws Exception {
		// Eclipse escape_sequences_250630: var f := '\\'\\'';
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s := '\\'\\'' ;
				        log('val:' + s);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "val:''");
	}

	@Test
	void string_escapeNewline() throws Exception {
		// Eclipse escape_sequences_250630: nsURI := '\\123t\\nri\\\\ng'
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s := 'line1\\nline2';
				        log('len:' + s.size().repr());
				    }
				}
				""");
		assertSuccess(result);
		// 'line1' + newline + 'line2' = 11 chars
		assertLogged(result, "len:11");
	}

	@Test
	void string_escapeTab() throws Exception {
		// Eclipse escape_sequences_250630: "str \\t \\n \\r \\" \\' \\\\ing"
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s := 'a\\tb';
				        log('len:' + s.size().repr());
				    }
				}
				""");
		assertSuccess(result);
		// 'a' + tab + 'b' = 3 chars
		assertLogged(result, "len:3");
	}

	@Test
	void string_escapeCarriageReturn() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s := 'a\\rb';
				        log('len:' + s.size().repr());
				    }
				}
				""");
		assertSuccess(result);
		// 'a' + CR + 'b' = 3 chars
		assertLogged(result, "len:3");
	}

	// ---- Octal escape sequences ----

	@Test
	void string_octalEscape() throws Exception {
		// Eclipse escape_sequences_250630: var k := '\\123'; → 'S' (ASCII 83)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s := '\\123';
				        log('val:' + s);
				        log('len:' + s.size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "val:S");
		assertLogged(result, "len:1");
	}

	// ---- Double-quoted strings as expressions ----

	@Test
	void string_doubleQuotedLiteral() throws Exception {
		// Eclipse doubleQuoteStrings_262734: nsPrefix := "a" + 'b';
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s := "hello";
				        log('val:' + s);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "val:hello");
	}

	@Test
	void string_doubleQuotedEscapeDoubleQuote() throws Exception {
		// Eclipse escape_sequences_250630: "str \\t \\n \\r \\" \\' \\\\ing"
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s := "say \\"hi\\"";
				        log('val:' + s);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "val:say \"hi\"");
	}

	// ---- Adjacent string concatenation (multiline strings) ----

	@Test
	void string_adjacentConcatenation() throws Exception {
		// Eclipse multilineStrings_262733: name := 'pack' '123' '456' '789';
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s := 'abc'
				        'def'
				        'ghi';
				        log('val:' + s);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "val:abcdefghi");
	}

	// ---- Java keyword identifiers ----

	@Test
	void string_javaKeywordAsIdentifier() throws Exception {
		// Eclipse javakeywords.qvto: var _class := 'class';
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var _class := 'myClass';
				        var _extends := _class;
				        log('val:' + _extends);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "val:myClass");
	}

	// ---- Combined escape sequences ----

	@Test
	void string_combinedEscapes() throws Exception {
		// Eclipse escape_sequences_250630: nsURI := '\\123t\\nri\\\\ng'
		// \\123 → 'S', t → 't', \\n → newline, r → 'r', i → 'i', \\\\ → '\\', n → 'n', g → 'g'
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s := 'a\\'b\\\\c\\nd';
				        log('len:' + s.size().repr());
				    }
				}
				""");
		assertSuccess(result);
		// a + ' + b + \ + c + newline + d = 7 chars
		assertLogged(result, "len:7");
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
