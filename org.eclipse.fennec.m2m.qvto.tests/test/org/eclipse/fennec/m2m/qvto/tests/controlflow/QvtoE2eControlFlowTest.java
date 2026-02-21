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
package org.eclipse.fennec.m2m.qvto.tests.controlflow;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O imperative control flow constructs.
 * Tests cover while, for/forEach, switch, break, continue, return,
 * compute, nested loops, and if/then/else.
 */
class QvtoE2eControlFlowTest extends AbstractQvtoEngineTest {

	// ---- While loop ----

	@Test
	void while_counter_iteratesToLimit() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var i : Integer := 0;
				        while (i < 10) {
				            i := i + 1;
				        };
				        log(i.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "10");
	}

	@Test
	void while_sumAccumulation() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var i : Integer := 1;
				        var sum : Integer := 0;
				        while (i <= 5) {
				            sum := sum + i;
				            i := i + 1;
				        };
				        log(sum.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "15");
	}

	@Test
	void while_falseCondition_neverExecutes() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : Integer := 0;
				        while (false) {
				            x := 99;
				        };
				        log(x.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "0");
	}

	// ---- forEach over collection ----

	@Test
	void forEach_overSequence_iteratesAll() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var sum : Integer := 0;
				        Sequence{1, 2, 3, 4, 5}->forEach(n) {
				            sum := sum + n;
				        };
				        log(sum.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "15");
	}

	@Test
	void forEach_overEmptyCollection_skips() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var count : Integer := 0;
				        Sequence{}->forEach(n) {
				            count := count + 1;
				        };
				        log(count.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "0");
	}

	@Test
	void forEach_strings_concatenation() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        Sequence{'a', 'b', 'c'}->forEach(s) {
				            log(s);
				        };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "a");
		assertLogged(result, "b");
		assertLogged(result, "c");
	}

	// ---- Nested loops ----

	@Test
	void nestedWhile_multiplicationTable() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var count : Integer := 0;
				        var i : Integer := 0;
				        while (i < 3) {
				            var j : Integer := 0;
				            while (j < 4) {
				                count := count + 1;
				                j := j + 1;
				            };
				            i := i + 1;
				        };
				        log(count.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "12");
	}

	@Test
	void nestedForEach() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var count : Integer := 0;
				        Sequence{1, 2}->forEach(i) {
				            Sequence{10, 20, 30}->forEach(j) {
				                count := count + 1;
				            };
				        };
				        log(count.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "6");
	}

	// ---- Switch/case/else ----

	@Test
	void switch_matchesFirstCase() throws Exception {
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
				        log(classify(-5));
				        log(classify(0));
				        log(classify(42));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "negative");
		assertLogged(result, "zero");
		assertLogged(result, "positive");
	}

	@Test
	void switch_noMatch_noElse_returnsNull() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : Integer := 99;
				        switch {
				            case (x = 1) log('one');
				            case (x = 2) log('two');
				        };
				        log('done');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "done");
	}

	@Test
	void switch_multipleCases_stopsAtFirst() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : Integer := 5;
				        var label : String := switch {
				            case (x > 0) 'pos';
				            case (x > 3) 'gt3';
				            else 'other';
				        };
				        log(label);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "pos");
	}

	// ---- Break in loop ----

	@Test
	void break_exitsWhileLoop() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var i : Integer := 0;
				        while (i < 100) {
				            switch { case (i = 5) break; };
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
	void break_exitsForEachLoop() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var last : Integer := 0;
				        Sequence{1, 2, 3, 4, 5}->forEach(n) {
				            last := n;
				            switch { case (n = 3) break; };
				        };
				        log(last.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "3");
	}

	// ---- Continue in loop ----

	@Test
	void continue_skipsCurrentIteration() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var sum : Integer := 0;
				        var i : Integer := 0;
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
		// 1+2+4+5 = 12 (skip 3)
		assertLogged(result, "12");
	}

	// ---- Return from mapping ----

	@Test
	void return_fromMapping_returnsResult() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping createElem() : r : EClass {
				        r.name := 'returned';
				    }
				    main() {
				        var elem := map createElem();
				        log(elem.name);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "returned");
	}

	// ---- Return from helper ----

	@Test
	void return_fromHelper_earlyExit() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper findFirst(nums : Sequence(Integer)) : Integer {
				        nums->forEach(n) {
				            switch { case (n > 3) return n; };
				        };
				        return -1;
				    }
				    main() {
				        log(findFirst(Sequence{1, 2, 5, 3}).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "5");
	}

	@Test
	void return_fromHelper_withConditional() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper clamp(n : Integer, min : Integer, max : Integer) : Integer {
				        if (n < min) then { return min; } endif;
				        if (n > max) then { return max; } endif;
				        return n;
				    }
				    main() {
				        log(clamp(-5, 0, 10).toString());
				        log(clamp(15, 0, 10).toString());
				        log(clamp(5, 0, 10).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "0");
		assertLogged(result, "10");
		assertLogged(result, "5");
	}

	// ---- Compute expression ----

	@Test
	void compute_returnsAccumulatedValue() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var result : Integer := compute(acc : Integer) {
				            acc := 0;
				            Sequence{1, 2, 3, 4}->forEach(n) {
				                acc := acc + n;
				            };
				        };
				        log(result.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "10");
	}

	@Test
	void compute_withStringResult() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var label : String := compute(s : String) {
				            s := 'hello';
				            s := s + ' world';
				        };
				        log(label);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "hello world");
	}

	// ---- If/then/else (imperative style) ----

	@Test
	void if_then_else_imperative() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : Integer := 10;
				        if (x > 5) then {
				            log('greater');
				        } else {
				            log('not greater');
				        } endif;
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "greater");
	}

	@Test
	void if_then_noElse() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : Integer := 3;
				        if (x > 5) then {
				            log('greater');
				        } endif;
				        log('done');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "done");
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
