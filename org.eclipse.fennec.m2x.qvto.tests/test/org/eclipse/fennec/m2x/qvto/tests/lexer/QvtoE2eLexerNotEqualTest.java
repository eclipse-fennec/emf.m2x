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
 * Tests for '!=' and '==' as synonyms for '<>' and '=' (§8.4.4 items 5+6).
 *
 * <p>QVT-O v1.3 §8.4.4 item 5: "The binary operator '==' can replace the '=' comparison operator."
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

	// ---- §8.4.4: '==' as synonym for '=' (Q-1) ----

	@Test
	void doubleEqual_basicComparison() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var a := 42;
				        var b := 42;
				        if (a == b) then { log('eq:true'); } else { log('eq:false'); } endif;
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "eq:true");
	}

	@Test
	void doubleEqual_unequalValues() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var a := 'hello';
				        var b := 'world';
				        if (a == b) then { log('eq:true'); } else { log('eq:false'); } endif;
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "eq:false");
	}

	@Test
	void doubleEqual_mixedWithSingleEqual() throws Exception {
		// Both '==' and '=' should work in the same transformation
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var a := 1;
				        var b := 1;
				        var c := 2;
				        var r1 := a = b;
				        var r2 := a == b;
				        var r3 := a == c;
				        if (r1 and r2 and not r3) then { log('all:true'); } endif;
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "all:true");
	}

	@Test
	void doubleEqual_withNull() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s : String := null;
				        if (s == null) then { log('isNull:true'); } else { log('isNull:false'); } endif;
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "isNull:true");
	}

	// ---- Q-3: Soft keywords 'class', 'default', 'refines' as identifiers (§8.4.7) ----

	@Test
	void softKeyword_class_asVariable() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var class := 'MyClass';
				        log('class:' + class);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "class:MyClass");
	}

	@Test
	void softKeyword_default_asVariable() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var default := 10;
				        log('default:' + default.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "default:10");
	}

	@Test
	void softKeyword_refines_asVariable() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var refines := true;
				        log('refines:' + refines.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "refines:true");
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
