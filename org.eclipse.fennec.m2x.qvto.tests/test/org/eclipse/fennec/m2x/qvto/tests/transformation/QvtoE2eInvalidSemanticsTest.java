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
package org.eclipse.fennec.m2x.qvto.tests.transformation;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for {@code invalid} semantics in QVT-O (§8.1.20).
 *
 * <p>When an OCL evaluation fails it returns the {@code invalid} value rather
 * than raising an exception. Every use of {@code invalid} as a source value
 * in an imperative expression has an implicit assertion failure.
 * {@code oclIsInvalid()} can test for invalid without causing a failure.
 *
 * <p>Eclipse reference: {@code addundefined.qvto}, {@code equndefined.qvto},
 * {@code stdlibList.qvto}
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eInvalidSemanticsTest extends AbstractQvtoEngineTest {

	// ==== P7-05: OclInvalid in QVT-O (§8.1.20) ====

	@Test
	void invalid_oclIsInvalid_returnsTrue() throws Exception {
		// §8.1.20: oclIsInvalid() tests for invalid WITHOUT causing failure
		// Eclipse addundefined.qvto: value.oclIsUndefined() on invalid → true
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x := 1 / 0;
				        if (x.oclIsInvalid()) {
				            log('is_invalid');
				        } else {
				            log('not_invalid');
				        };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "is_invalid");
	}

	@Test
	void invalid_storeInVariable() throws Exception {
		// §8.1.20: invalid may be stored in a variable (assignment stores, not uses)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x := 1 / 0;
				        var isInv := x.oclIsInvalid();
				        if (isInv) {
				            log('stored_and_checked');
				        };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "stored_and_checked");
	}

	@Test
	void invalid_arithmeticProducesInvalid() throws Exception {
		// §8.1.20 + Eclipse addundefined.qvto: arithmetic with invalid → invalid
		// 1 + invalid, 1 - invalid, 1 * invalid → all invalid
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var inv := 1 / 0;
				        var sum := 1 + inv;
				        var diff := 1 - inv;
				        var prod := 1 * inv;
				        if (sum.oclIsUndefined()) {
				            log('sum_undef');
				        };
				        if (diff.oclIsUndefined()) {
				            log('diff_undef');
				        };
				        if (prod.oclIsUndefined()) {
				            log('prod_undef');
				        };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "sum_undef");
		assertLogged(result, "diff_undef");
		assertLogged(result, "prod_undef");
	}

	@Test
	void invalid_equalityWithInvalid() throws Exception {
		// §8.1.20 + Eclipse equndefined.qvto: equality with invalid → undefined
		// invalid = 1 → undefined, invalid <> 'x' → undefined
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var inv := 1 / 0;
				        var eq := (inv = 1);
				        var neq := (inv <> 'x');
				        if (eq.oclIsUndefined()) {
				            log('eq_undef');
				        };
				        if (neq.oclIsUndefined()) {
				            log('neq_undef');
				        };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "eq_undef");
		assertLogged(result, "neq_undef");
	}

	@Test
	void invalid_divideByZero() throws Exception {
		// §8.1.20: divide by zero produces invalid, not exception
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x := 10 / 0;
				        if (x.oclIsInvalid()) {
				            log('div_zero_invalid');
				        };
				        var y := 10.0 / 0.0;
				        if (y.oclIsInvalid()) {
				            log('real_div_zero_invalid');
				        };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "div_zero_invalid");
		assertLogged(result, "real_div_zero_invalid");
	}

	@Test
	void invalid_oclIsUndefinedOnInvalid() throws Exception {
		// OCL §11.2.2: invalid.oclIsUndefined() → true (invalid IS undefined)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x := 1 / 0;
				        if (x.oclIsUndefined()) {
				            log('invalid_is_undefined');
				        };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "invalid_is_undefined");
	}

	@Test
	void invalid_nullVsInvalidDistinction() throws Exception {
		// §8.1.20: null.oclIsInvalid() → false, invalid.oclIsInvalid() → true
		// null and invalid are both "undefined" but invalid is a stricter condition
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var n : String := null;
				        var inv := 1 / 0;
				        if (n.oclIsInvalid()) {
				            log('null_is_invalid');
				        } else {
				            log('null_not_invalid');
				        };
				        if (inv.oclIsInvalid()) {
				            log('inv_is_invalid');
				        };
				        if (n.oclIsUndefined()) {
				            log('null_is_undefined');
				        };
				        if (inv.oclIsUndefined()) {
				            log('inv_is_undefined');
				        };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "null_not_invalid");
		assertLogged(result, "inv_is_invalid");
		assertLogged(result, "null_is_undefined");
		assertLogged(result, "inv_is_undefined");
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
