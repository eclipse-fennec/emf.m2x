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
 * Tests for QVT-O control flow: while, break, continue, return, switch.
 */
class QvtoControlFlowTest extends AbstractQvtoEngineTest {

	@Test
	void whileLoop_basic() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var i : Integer := 0;
				        while (i < 5) {
				            i := i + 1;
				        };
				        log(i.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "5");
	}

	@Test
	void whileLoop_break() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var i : Integer := 0;
				        while (i < 100) {
				            switch { case (i = 3) break; };
				            i := i + 1;
				        };
				        log(i.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "3");
	}

	@Test
	void whileLoop_continue() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var i : Integer := 0;
				        var sum : Integer := 0;
				        while (i < 5) {
				            i := i + 1;
				            switch { case (i = 3) continue; };
				            sum := sum + i;
				        };
				        log(sum.toString());
				    }
				}
				""");
		assertSuccess(result);
		// sum = 1 + 2 + 4 + 5 = 12 (skip i=3)
		assertLogged(result, "12");
	}

	@Test
	void returnExp_withValue() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper earlyReturn(n : Integer) : Integer {
				        return if n < 0 then 0 else n * 2 endif;
				    }
				    main() {
				        log(earlyReturn(-1).toString());
				        log(earlyReturn(5).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "0");
		assertLogged(result, "10");
	}

	@Test
	void switchExp_basic() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper classify(n : Integer) : String {
				        return switch {
				            case (n < 0) 'negative';
				            case (n = 0) 'zero';
				            else 'positive';
				        };
				    }
				    main() {
				        log(classify(-1));
				        log(classify(0));
				        log(classify(5));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "negative");
		assertLogged(result, "zero");
		assertLogged(result, "positive");
	}

	@Test
	void switchExp_noElse_noMatch() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : Integer := 42;
				        switch {
				            case (x = 1) log('one');
				            case (x = 2) log('two');
				        };
				        log('done');
				    }
				}
				""");
		assertSuccess(result);
		// No case matched, so 'one' and 'two' should NOT appear, but 'done' should
		assertLogged(result, "done");
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
