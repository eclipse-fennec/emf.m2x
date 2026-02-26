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
 * Tests for '!=' as synonym for '<>' (GAP-18).
 *
 * <p>QVT-O v1.3 §8.4.4 item 6: "The binary operator '!=' can be used instead of
 * the '<>' comparison operator. Both alternatives should be available."
 *
 * <p>Eclipse reference: {@code bug274105_274505.qvto} uses {@code name != "aaa"}.
 *
 * <p><b>SPEC-FIRST:</b> Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eLexerNotEqualTest extends AbstractQvtoEngineTest {

	// ---- §8.4.4: '!=' as synonym for '<>' ----

	@Test
	void notEqual_basicComparison() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var a := 1;
				        var b := 2;
				        if (a != b) then { log('diff:true'); } else { log('diff:false'); } endif;
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "diff:true");
	}

	@Test
	void notEqual_equalValues() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var a := 'hello';
				        var b := 'hello';
				        if (a != b) then { log('diff:true'); } else { log('diff:false'); } endif;
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "diff:false");
	}

	@Test
	void notEqual_mixedWithDiamondOperator() throws Exception {
		// Both '!=' and '<>' should work in the same transformation
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var a := 1;
				        var b := 2;
				        var c := 3;
				        var r1 := a <> b;
				        var r2 := a != c;
				        if (r1 and r2) then { log('both:true'); } endif;
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "both:true");
	}

	@Test
	void notEqual_withNull() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s := 'hello';
				        if (s != null) then { log('notNull:true'); } else { log('notNull:false'); } endif;
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "notNull:true");
	}

	@Test
	void notEqual_doesNotBreakXselectOne() throws Exception {
		// Ensure '!' '[' (xselectOne) still works — no lexer conflict with '!='
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var items := List{1, 2, 3};
				        var found := items![e | e = 2];
				        log('found:' + found.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "found:2");
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
