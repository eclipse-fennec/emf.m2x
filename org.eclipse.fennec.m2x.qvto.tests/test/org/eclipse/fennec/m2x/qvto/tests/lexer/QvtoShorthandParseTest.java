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
 * Parse-level tests for QVT-O v1.3 §8.4.4 shorthand operators.
 *
 * <p>GAP-19: {@code %} (format), GAP-20: {@code #}/{@code ##}/{@code *}, GAP-21: {@code !->}
 *
 * <p>Uses AbstractQvtoEngineTest to parse transformations and inspects the AST.
 *
 * <p><b>SPEC-FIRST:</b> Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoShorthandParseTest extends AbstractQvtoEngineTest {

	// ---- GAP-19: % as format operator ----

	@Test
	void formatOperator_parsesCorrectly() throws Exception {
		// §8.4.4 item 4: "hello %s" % name → parses without error
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var r := 'hello %s' % 'World';
				        log(r);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "hello World");
	}

	// ---- GAP-20: # / ## / * as unary operators ----

	@Test
	void hashOperator_parsesCorrectly() throws Exception {
		// §8.4.4 item 1: #Type — unary prefix, parses and evaluates in select context
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var items := Sequence{'hello', 42, true};
				        var r := items->select(#String);
				        log('count:' + r->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "count:1");
	}

	@Test
	void doubleHashOperator_parsesCorrectly() throws Exception {
		// §8.4.4 item 2: ##Type — unary prefix, parses and evaluates in select context
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var items := Sequence{'hello', 42, true};
				        var r := items->select(##String);
				        log('count:' + r->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "count:1");
	}

	@Test
	void unaryStarOperator_parsesCorrectly() throws Exception {
		// §8.4.4 item 3: *"stereo" → stereotypedBy — should parse
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var r := *'stereotype';
				        log('done');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "done");
	}

	// ---- GAP-21: !-> as not-arrow ----

	@Test
	void notArrowOperator_parsesCorrectly() throws Exception {
		// §8.4.4 item 5: list!->isEmpty() → not(list->isEmpty())
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var r := Sequence{1, 2}!->isEmpty();
				        log('r:' + r.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "r:true");
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
