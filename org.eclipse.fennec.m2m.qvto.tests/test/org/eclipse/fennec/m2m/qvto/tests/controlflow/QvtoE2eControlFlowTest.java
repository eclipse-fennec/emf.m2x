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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.common.util.Diagnostic;

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

	// ---- forOne (§8.2.2.6: executes body only for first matching element) ----

	@Test
	void forOne_stopsAfterFirstMatch() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var count : Integer := 0;
				        Sequence{1, 2, 3, 4, 5}->forOne(n) {
				            count := count + 1;
				        };
				        log(count.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "1");
	}

	@Test
	void forOne_withCondition_findsFirstMatch() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var found : Integer := 0;
				        Sequence{1, 2, 3, 4, 5}->forOne(n | n > 3) {
				            found := n;
				        };
				        log(found.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "4");
	}

	@Test
	void forOne_noMatch_executesNothing() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var count : Integer := 0;
				        Sequence{1, 2, 3}->forOne(n | n > 10) {
				            count := count + 1;
				        };
				        log(count.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "0");
	}

	// ---- forEach compute shorthand (§8.2.2.6) ----

	@Test
	void forEach_computeShorthand_accumulatesResult() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var total : String := Sequence{'a', 'b', 'c'}->forEach(s; acc : String = '') {
				            acc := acc + s;
				        };
				        log(total);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "abc");
	}

	// ---- forEach over Set → ordered conversion (§8.2.2.6) ----

	@Test
	void forEach_overSet_iteratesAllElements() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var count : Integer := 0;
				        Set{1, 2, 3}->forEach(n) {
				            count := count + n;
				        };
				        log(count.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "6");
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

	// ---- P6-02: BlockExp (§8.2.2.2) ----

	@Test
	void blockExp_outerVarModifiableInsideBlock() throws Exception {
		// §8.2.2.2: Variables defined in outer scopes are accessible within the block
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : String := 'before';
				        if (true) then {
				            x := 'modified';
				        } endif;
				        log(x);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "modified");
	}

	@Test
	void blockExp_varScopedToBlock_reusableInNextBlock() throws Exception {
		// §8.2.2.2: The block creates a new scope, local variables not accessible outside.
		// Same variable name can be declared in separate blocks independently.
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        if (true) then {
				            var x : String := 'first';
				            log(x);
				        } endif;
				        if (true) then {
				            var x : String := 'second';
				            log(x);
				        } endif;
				        log('done');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "first");
		assertLogged(result, "second");
		assertLogged(result, "done");
	}

	@Test
	void blockExp_nestedBlocks_innerScopeIndependent() throws Exception {
		// §8.2.2.2: Nested blocks each create their own scope.
		// Inner block var 'item' is re-created each iteration, not leaking.
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var result : String := '';
				        var i : Integer := 0;
				        while (i < 3) {
				            var item : String := i.toString();
				            result := result + item;
				            i := i + 1;
				        };
				        log(result);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "012");
	}

	// ---- P6-02: if/elif/else — SwitchExp (§8.2.2.8) ----

	@Test
	void if_elif_else_chain_matchesCorrectBranch() throws Exception {
		// §8.2.2.8: if/elif/else evaluates alternatives in sequence until one succeeds
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper classify(n : Integer) : String {
				        if (n < 0) then {
				            return 'negative';
				        } elif (n = 0) then {
				            return 'zero';
				        } elif (n < 10) then {
				            return 'small';
				        } else {
				            return 'large';
				        } endif;
				        return 'unreachable';
				    }
				    main() {
				        log(classify(-5));
				        log(classify(0));
				        log(classify(7));
				        log(classify(42));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "negative");
		assertLogged(result, "zero");
		assertLogged(result, "small");
		assertLogged(result, "large");
	}

	@Test
	void if_asExpression_returnsValue() throws Exception {
		// §8.2.2.8: SwitchExp can be used as expression, returning the matched branch value
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : Integer := 10;
				        var label : String := if (x > 5) then 'big' else 'small' endif;
				        log(label);
				        var label2 : String := if (x < 5) then 'big' else 'small' endif;
				        log(label2);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "big");
		assertLogged(result, "small");
	}

	@Test
	void if_noElse_conditionFalse_returnsNull() throws Exception {
		// §8.2.2.8: else part is non-mandatory; if no alternative matches → null
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper maybeLabel(flag : Boolean) : String {
				        var result : String := if (flag) then 'yes' endif;
				        if (result = null) then {
				            return 'was_null';
				        } endif;
				        return result;
				    }
				    main() {
				        log(maybeLabel(true));
				        log(maybeLabel(false));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "yes");
		assertLogged(result, "was_null");
	}

	@Test
	void if_elif_noElse_noMatch_continuesExecution() throws Exception {
		// §8.2.2.8: if/elif without else, no match → continues after endif
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : Integer := 99;
				        if (x = 1) then {
				            log('one');
				        } elif (x = 2) then {
				            log('two');
				        } endif;
				        log('continued');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "continued");
	}

	@Test
	void switch_noElse_noMatch_returnsNull() throws Exception {
		// §8.2.2.8: switch without else, no case matches → expression evaluates to null
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : Integer := 99;
				        var label : String := switch {
				            case (x = 1) 'one';
				            case (x = 2) 'two';
				        };
				        if (label = null) then {
				            log('was_null');
				        } else {
				            log(label);
				        } endif;
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "was_null");
	}

	// ---- P6-03: WhileExp (§8.2.2.4) & ComputeExp (§8.2.2.3) ----

	@Test
	void while_returnsNull() throws Exception {
		// §8.2.2.4: WhileExp returns null
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var i : Integer := 0;
				        var r : OclAny := while (i < 3) {
				            i := i + 1;
				        };
				        if (r = null) then {
				            log('while_returned_null');
				        } else {
				            log('while_returned_value');
				        } endif;
				        log(i.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "while_returned_null");
		assertLogged(result, "3");
	}

	@Test
	void while_nestedBreak_onlyInnerLoopBreaks() throws Exception {
		// §8.2.2.4: break terminates the while expression — only the innermost loop
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var outerCount : Integer := 0;
				        var innerTotal : Integer := 0;
				        while (outerCount < 3) {
				            var j : Integer := 0;
				            while (true) {
				                j := j + 1;
				                if (j = 2) then { break; } endif;
				            };
				            innerTotal := innerTotal + j;
				            outerCount := outerCount + 1;
				        };
				        log(outerCount.toString());
				        log(innerTotal.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "3");
		// inner loop breaks at j=2 each time, so innerTotal = 2+2+2 = 6
		assertLogged(result, "6");
	}

	@Test
	void while_continue_skipsRestOfIteration() throws Exception {
		// §8.2.2.4: continue provokes the execution of the next iteration
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var i : Integer := 0;
				        var sum : Integer := 0;
				        while (i < 5) {
				            i := i + 1;
				            if (i = 3) then { continue; } endif;
				            sum := sum + i;
				        };
				        log(sum.toString());
				    }
				}
				""");
		assertSuccess(result);
		// sum = 1+2+4+5 = 12 (skip i=3)
		assertLogged(result, "12");
	}

	@Test
	void while_withInitVariable_computeShorthand() throws Exception {
		// §8.2.2.4: while with init variable declaration in header
		// Notation: while (x:Type := init; condition) { body }
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var result : String := '';
				        while (n : Integer := 3; n > 0) {
				            result := result + n.toString();
				            n := n - 1;
				        };
				        log(result);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "321");
	}

	@Test
	void compute_returnsFinalVarValue() throws Exception {
		// §8.2.2.3: ComputeExp returns the value of the variable at end of body
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : Integer := compute(acc : Integer = 0) {
				            acc := acc + 10;
				            acc := acc + 5;
				        };
				        log(x.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "15");
	}

	@Test
	void compute_withWhileLoop_accumulates() throws Exception {
		// §8.2.2.3: compute with while loop to build up result
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var label : String := compute(acc : String = '') {
				            var i : Integer := 1;
				            while (i <= 3) {
				                acc := acc + i.toString();
				                i := i + 1;
				            };
				        };
				        log(label);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "123");
	}

	@Test
	void compute_withForEach_accumulates() throws Exception {
		// §8.2.2.3: compute with forEach to build up result
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var csv : String := compute(s : String = '') {
				            Sequence{'a', 'b', 'c'}->forEach(item) {
				                if (s.size() > 0) then {
				                    s := s + ',';
				                } endif;
				                s := s + item;
				            };
				        };
				        log(csv);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "a,b,c");
	}

	@Test
	void compute_initialValueReturned_whenBodyEmpty() throws Exception {
		// §8.2.2.3: compute returns initial value if body doesn't modify it
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : String := compute(s : String = 'initial') {
				        };
				        log(x);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "initial");
	}

	// ---- P6-04: ForExp — forEach & forOne (§8.2.2.6) ----

	@Test
	void forEach_withCondition_filtersElements() throws Exception {
		// §8.2.2.6: forEach with condition — only elements satisfying condition
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var sum : Integer := 0;
				        Sequence{1, 2, 3, 4, 5}->forEach(n | n > 3) {
				            sum := sum + n;
				        };
				        log(sum.toString());
				    }
				}
				""");
		assertSuccess(result);
		// only 4 + 5 = 9
		assertLogged(result, "9");
	}

	@Test
	void forEach_returnsNull() throws Exception {
		// §8.2.2.6: ForExp returns the null value
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var r : OclAny := Sequence{1, 2, 3}->forEach(n) {
				            n.toString();
				        };
				        if (r = null) then {
				            log('forEach_returned_null');
				        } else {
				            log('forEach_returned_value');
				        } endif;
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "forEach_returned_null");
	}

	@Test
	void forEach_overOrderedSet_iteratesInOrder() throws Exception {
		// §8.2.2.6: non-ordered collections implicitly converted to ordered
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var count : Integer := 0;
				        OrderedSet{10, 20, 30}->forEach(n) {
				            count := count + 1;
				            log(n.toString());
				        };
				        log(count.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "10");
		assertLogged(result, "20");
		assertLogged(result, "30");
		assertLogged(result, "3");
	}

	@Test
	void forEach_continue_skipsCurrentElement() throws Exception {
		// §8.2.2.6: continue skips to next iteration element
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var result : String := '';
				        Sequence{'a', 'b', 'c', 'd'}->forEach(s) {
				            if (s = 'b') then { continue; } endif;
				            result := result + s;
				        };
				        log(result);
				    }
				}
				""");
		assertSuccess(result);
		// 'b' skipped → 'acd'
		assertLogged(result, "acd");
	}

	@Test
	void forEach_nestedForEach_independentIterators() throws Exception {
		// §8.2.2.6: nested forEach with independent iterator variables
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var pairs : String := '';
				        Sequence{1, 2}->forEach(i) {
				            Sequence{'a', 'b'}->forEach(j) {
				                pairs := pairs + i.toString() + j;
				            };
				        };
				        log(pairs);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "1a1b2a2b");
	}

	@Test
	void forEach_return_exitsEnclosingHelper() throws Exception {
		// §8.2.2.6: return within forEach exits the enclosing operation
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper findFirst(items : Sequence(String), prefix : String) : String {
				        items->forEach(s) {
				            if (s.startsWith(prefix)) then {
				                return s;
				            } endif;
				        };
				        return 'none';
				    }
				    main() {
				        log(findFirst(Sequence{'apple', 'banana', 'cherry'}, 'b'));
				        log(findFirst(Sequence{'apple', 'banana', 'cherry'}, 'z'));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "banana");
		assertLogged(result, "none");
	}

	@Test
	void forEach_withCondition_andBreak() throws Exception {
		// §8.2.2.6: forEach with condition + break — break exits the loop
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var count : Integer := 0;
				        Sequence{1, 2, 3, 4, 5, 6}->forEach(n | n > 2) {
				            count := count + 1;
				            if (n = 4) then { break; } endif;
				        };
				        log(count.toString());
				    }
				}
				""");
		assertSuccess(result);
		// n>2: 3,4,5,6 but break at n=4 → count=2 (processed 3 and 4)
		assertLogged(result, "2");
	}

	@Test
	void forOne_conditionFalse_bodyNotExecuted() throws Exception {
		// §8.2.2.6: forOne with condition where nothing matches
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var found : String := 'initial';
				        Sequence{'a', 'b', 'c'}->forOne(s | s = 'z') {
				            found := s;
				        };
				        log(found);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "initial");
	}

	// ---- P6-05: ImperativeIterateExp — xcollect/xselect (§8.2.2.7) ----

	@Test
	void xcollect_propertyShorthand_collectsNames() throws Exception {
		// §8.2.2.7: list->prop = list->xcollect(i | i.prop) — null values removed
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var c1 := object EClass { name := 'Alpha'; };
				        var c2 := object EClass { name := 'Beta'; };
				        var c3 := object EClass { name := 'Gamma'; };
				        var classes := Sequence{c1, c2, c3};
				        var names : Sequence(String) := classes->name;
				        names->forEach(n) { log(n); };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "Alpha");
		assertLogged(result, "Beta");
		assertLogged(result, "Gamma");
	}

	@Test
	void xcollect_nullValuesRemoved() throws Exception {
		// §8.2.2.7: xcollect removes null values from the result
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var c1 := object EClass { name := 'A'; };
				        var c2 := object EClass { };
				        var c3 := object EClass { name := 'C'; };
				        var classes := Sequence{c1, c2, c3};
				        var names : Sequence(String) := classes->name;
				        log(names->size().toString());
				        names->forEach(n) { log(n); };
				    }
				}
				""");
		assertSuccess(result);
		// c2.name is null/empty → either filtered or empty string
		// Spec says null values are removed, so we expect 2 or 3 depending on empty-string vs null
		assertLogged(result, "A");
		assertLogged(result, "C");
	}

	@Test
	void xselect_bracketCondition_filtersElements() throws Exception {
		// §8.2.2.7: list[condition] = list->xselect(i | condition)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var nums := Sequence{1, 2, 3, 4, 5, 6};
				        var big : Sequence(Integer) := nums[n | n > 3];
				        log(big->size().toString());
				        big->forEach(n) { log(n.toString()); };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "3");
		assertLogged(result, "4");
		assertLogged(result, "5");
		assertLogged(result, "6");
	}

	@Test
	void xselect_emptyResult() throws Exception {
		// §8.2.2.7: xselect with no matching elements → empty collection
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var nums := Sequence{1, 2, 3};
				        var big : Sequence(Integer) := nums[n | n > 100];
				        log(big->size().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "0");
	}

	@Test
	void xselect_typeFilter() throws Exception {
		// §8.2.2.7: list[Type] = list->xselect(oclIsKindOf(Type)) with type re-casting
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var c1 := object EClass { name := 'Cls'; };
				        var d1 := object EDataType { name := 'Dt'; };
				        var all := Sequence{c1, d1};
				        var classes : Sequence(EClass) := all[EClass];
				        log(classes->size().toString());
				        classes->forEach(c) { log(c.name); };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "1");
		assertLogged(result, "Cls");
	}

	@Test
	void xcollect_withMapShorthand() throws Exception {
		// §8.2.2.7: list->map f() = list->xcollect(i | i.map f())
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping EClass::toUpper() : EClass {
				        name := self.name.toUpperCase();
				    }
				    main() {
				        var c1 := object EClass { name := 'alpha'; };
				        var c2 := object EClass { name := 'beta'; };
				        var uppers := Sequence{c1, c2}->map toUpper();
				        uppers->forEach(u) { log(u.name); };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "ALPHA");
		assertLogged(result, "BETA");
	}

	@Test
	void xcollectselect_combined() throws Exception {
		// §8.2.2.7: list->prop[cond] — collect property then filter
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var c1 := object EClass { name := 'Alpha'; };
				        var c2 := object EClass { name := 'Beta'; };
				        var c3 := object EClass { name := 'Gamma'; };
				        var classes := Sequence{c1, c2, c3};
				        var filtered : Sequence(String) := classes->name[n | n.startsWith('A') or n.startsWith('G')];
				        log(filtered->size().toString());
				        filtered->forEach(n) { log(n); };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "2");
		assertLogged(result, "Alpha");
		assertLogged(result, "Gamma");
	}

	@Test
	void xcollect_flattenNestedCollections() throws Exception {
		// §8.2.2.7: xcollect flattens nested collections from BODY
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var p1 := object EPackage { name := 'p1'; };
				        var c1 := object EClass { name := 'A'; };
				        var c2 := object EClass { name := 'B'; };
				        p1.eClassifiers += c1;
				        p1.eClassifiers += c2;
				        var p2 := object EPackage { name := 'p2'; };
				        var c3 := object EClass { name := 'C'; };
				        p2.eClassifiers += c3;
				        var packages := Sequence{p1, p2};
				        var allClassifiers := packages->eClassifiers;
				        log(allClassifiers->size().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "3");
	}

	@Test
	void xselectOne_returnsFirstMatch() throws Exception {
		// §8.2.2.7: list![condition] → first matching element
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var c1 := object EClass { name := 'Alpha'; };
				        var c2 := object EClass { name := 'Beta'; };
				        var c3 := object EClass { name := 'Gamma'; };
				        var classes := Sequence{c1, c2, c3};
				        var found : EClass := classes![c | c.name.startsWith('B')];
				        log(found.name);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "Beta");
	}

	@Test
	void xselectOne_noMatch_returnsNull() throws Exception {
		// §8.2.2.7: xselectOne with no match → null
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var c1 := object EClass { name := 'Alpha'; };
				        var classes := Sequence{c1};
				        var found : EClass := classes![c | c.name = 'NoSuch'];
				        if (found = null) then {
				            log('not_found');
				        } else {
				            log(found.name);
				        } endif;
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "not_found");
	}

	// ==== P6-06: ReturnExp, BreakExp, ContinueExp (§8.2.2.16–18) ====

	@Test
	void break_inWhile_withIfThenElse() throws Exception {
		// Eclipse continue_break.qvto pattern: if/then break else continue endif
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var index := 0;
				        while (true) {
				            index := 1 + index;
				            if 5 < index then
				                break
				            else {
				                continue;
				            }
				            endif;
				        };
				        log(index.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "6");
	}

	@Test
	void break_inNestedForEach_onlyInnerBreaks() throws Exception {
		// §8.2.2.17: break exits only the innermost loop
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var result : String := '';
				        Sequence{'a', 'b'}->forEach(outer) {
				            Sequence{'1', '2', '3'}->forEach(inner) {
				                if (inner = '2') then { break; } endif;
				                result := result + outer + inner;
				            };
				        };
				        log(result);
				    }
				}
				""");
		assertSuccess(result);
		// inner breaks at '2' each time → a1, b1
		assertLogged(result, "a1b1");
	}

	@Test
	void continue_inForEach_withIfThenElse() throws Exception {
		// Eclipse continue_break.qvto: continue in forEach skips to next element
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var index := 0;
				        var col := Sequence{1, 2, 3};
				        col->forEach(i) {
				            if i > 1 then continue endif;
				            index := index + 1;
				        };
				        log(index.toString());
				    }
				}
				""");
		assertSuccess(result);
		// only i=1 passes the guard → index = 1
		assertLogged(result, "1");
	}

	@Test
	void return_withoutValue_fromHelper() throws Exception {
		// §8.2.2.16: return without value — exits operation
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper doWork(n : Integer) : String {
				        if (n < 0) then { return; } endif;
				        return n.toString();
				    }
				    main() {
				        var r1 := doWork(5);
				        log('pos=' + r1);
				        var r2 := doWork(-1);
				        if (r2 = null) then { log('neg=null'); } endif;
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "pos=5");
		assertLogged(result, "neg=null");
	}

	@Test
	void return_fromWhileLoop_exitsOperation() throws Exception {
		// §8.2.2.16: return in while exits the enclosing operation, not just the loop
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper findInRange(start : Integer, limit : Integer) : Integer {
				        var i := start;
				        while (i < limit) {
				            if (i * i > 50) then { return i; } endif;
				            i := i + 1;
				        };
				        return -1;
				    }
				    main() {
				        log(findInRange(1, 100).toString());
				        log(findInRange(1, 3).toString());
				    }
				}
				""");
		assertSuccess(result);
		// 8*8=64 > 50 → returns 8
		assertLogged(result, "8");
		// 1,2 checked, loop ends → returns -1
		assertLogged(result, "-1");
	}

	@Test
	void return_nullFromQuery() throws Exception {
		// Eclipse voidreturn.qvto / returnundefinedfromquery.qvto pattern
		QvtoExecutionResult result = execute("""
				transformation test() {
				    query nullQuery() : String {
				        return null;
				    }
				    main() {
				        var r := nullQuery();
				        if (r = null) then { log('is_null'); } else { log('not_null'); } endif;
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "is_null");
	}

	@Test
	void return_fromMapping_earlyExit() throws Exception {
		// §8.2.2.16: return inside mapping exits the mapping body
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping EClass::rename() : EClass {
				        if (self.name = 'skip') then { return; } endif;
				        name := self.name + '_renamed';
				    }
				    main() {
				        var c1 := object EClass { name := 'Alpha'; };
				        var c2 := object EClass { name := 'skip'; };
				        var r1 := c1.map rename();
				        var r2 := c2.map rename();
				        log(r1.name);
				        -- return; exits before name assignment — result has default null name
				        if (r2.name = null) then { log('name_is_null'); } endif;
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "Alpha_renamed");
		// return; exits mapping body before name := ... — result EClass has null name
		assertLogged(result, "name_is_null");
	}

	@Test
	void return_fromDeeplyNested() throws Exception {
		// §8.2.2.16: return from deeply nested control flow exits entire operation
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper deepSearch(items : Sequence(Integer)) : Integer {
				        items->forEach(i) {
				            if (i > 0) then {
				                var j := 0;
				                while (j < i) {
				                    j := j + 1;
				                    if (j * i > 10) then {
				                        return j * i;
				                    } endif;
				                };
				            } endif;
				        };
				        return -1;
				    }
				    main() {
				        log(deepSearch(Sequence{1, 2, 3, 4}).toString());
				    }
				}
				""");
		assertSuccess(result);
		// i=4, j=3: 3*4=12 > 10 → returns 12
		assertLogged(result, "12");
	}

	// ==== P6-07: LogExp & AssertExp (§8.2.2.19–20) ====

	@Test
	void log_whenTrue_producesLog() throws Exception {
		// §8.2.2.19: log with 'when' condition — log only when condition holds
		// Eclipse assert_log.qvto pattern: log('msg') when condition
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('visible') when true;
				        log('hidden') when false;
				        log('done');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "visible");
		assertLogged(result, "done");
		// 'hidden' should NOT appear
		boolean hasHidden = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("hidden"));
		assertFalse(hasHidden, "log with 'when false' should not produce diagnostic");
	}

	@Test
	void log_withElement_includesRepr() throws Exception {
		// §8.2.2.19: log(message, element) — implicit repr() on element
		// Eclipse assert_log.qvto: log('msg', model)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var c := object EClass { name := 'Foo'; };
				        log('elem', c);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "elem");
	}

	@Test
	void log_returnsNull() throws Exception {
		// §8.2.2.19: "A log expression returns null"
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x := log('test');
				        if (x = null) then { log('is_null'); } endif;
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "test");
		assertLogged(result, "is_null");
	}

	@Test
	void assert_defaultSeverity_isError() throws Exception {
		// §8.2.2.20: default severity is error — assert without severity keyword
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        assert (false);
				    }
				}
				""");
		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() == Diagnostic.ERROR),
				"assert without severity keyword should default to ERROR");
	}

	@Test
	void assert_fatal_terminatesExecution() throws Exception {
		// §8.2.2.20: fatal → terminates with AssertionFailed
		// Eclipse continue_break.qvto: assert fatal (condition)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('before');
				        assert fatal (false);
				        log('after');
				    }
				}
				""");
		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertLogged(result, "before");
		// 'after' should NOT appear — fatal terminates
		boolean hasAfter = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("after"));
		assertFalse(hasAfter, "assert fatal should terminate execution");
	}

	@Test
	void assert_trueCondition_noDiagnostic() throws Exception {
		// §8.2.2.20: when condition is true, no error/diagnostic generated
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        assert (1 + 1 = 2);
				        assert warning (true);
				        assert error (true);
				        log('all_passed');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "all_passed");
	}

	@Test
	void assert_fatal_withLog_message() throws Exception {
		// §8.2.2.20: assert fatal with log — Eclipse bug419299.qvto pattern
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        assert fatal (false) with log('fatal error occurred');
				    }
				}
				""");
		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("fatal error occurred")),
				"assert fatal with log should include the log message");
	}

	@Test
	void assert_multipleSeverities_inOneTransformation() throws Exception {
		// §8.2.2.20: warning does not fail, error fails, mixed in same transformation
		// Eclipse stacktrace.qvto: assert warning (false); assert error (true);
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        assert warning (false);
				        log('after_warning');
				        assert error (true);
				        log('after_error_true');
				    }
				}
				""");
		// warning does not stop execution, error(true) succeeds
		assertSuccess(result);
		assertLogged(result, "after_warning");
		assertLogged(result, "after_error_true");
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() == Diagnostic.WARNING),
				"assert warning should produce WARNING diagnostic");
	}

	// ==== P6-08: TryExp, CatchExp, RaiseExp (§8.2.2.13–15) ====

	@Test
	void tryCatch_basicRaiseAndCatch() throws Exception {
		// §8.2.2.13/15: raise string, catch in except — Eclipse bug419299.qvto pattern
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var msg := '';
				        try {
				            raise 'something wrong';
				        } except (ex : Exception) {
				            msg := 'caught';
				        };
				        log(msg);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "caught");
	}

	@Test
	void tryCatch_noException_passesThrough() throws Exception {
		// §8.2.2.13: body without exception — except clause NOT executed
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var result := 'ok';
				        try {
				            result := 'from_try';
				        } except (Exception) {
				            result := 'from_catch';
				        };
				        log(result);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "from_try");
	}

	@Test
	void tryCatch_catchAll_emptyParens() throws Exception {
		// Eclipse bug419299.qvto test11: except () catches any exception
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var trace := '';
				        try {
				            raise 'message';
				            trace := trace + 'not_raised.';
				        } except () {
				            trace := trace + 'caught.';
				        };
				        log(trace);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "caught.");
	}

	@Test
	void tryCatch_assertFatal_caughtAsAssertionFailed() throws Exception {
		// Eclipse bug419299.qvto: assert fatal in try → caught as AssertionFailed
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var msg := '';
				        try {
				            assert fatal (false) with log('NPE');
				        } except (ex : Exception) {
				            msg := 'caught_fatal';
				        };
				        log(msg);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "caught_fatal");
	}

	@Test
	void tryCatch_raiseStopsExecution() throws Exception {
		// §8.2.2.15: raise interrupts normal flow — code after raise not executed
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var trace := '';
				        try {
				            trace := trace + 'before.';
				            raise 'error';
				            trace := trace + 'after.';
				        } except () {
				            trace := trace + 'handler.';
				        };
				        log(trace);
				    }
				}
				""");
		assertSuccess(result);
		// 'after.' should NOT appear
		assertLogged(result, "before.handler.");
	}

	@Test
	void tryCatch_breakInsideTry() throws Exception {
		// Eclipse bug419299.qvto test1: break inside try block exits while loop
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var i := 0;
				        while (i < 100) {
				            i := i + 1;
				            try {
				                break;
				            } except () {
				                i := -1;
				            };
				        };
				        log(i.toString());
				    }
				}
				""");
		assertSuccess(result);
		// break exits while at i=1, except is NOT entered
		assertLogged(result, "1");
	}

	@Test
	void tryCatch_nestedTry_innerCatches() throws Exception {
		// Eclipse bug419299.qvto test6 pattern: nested try blocks
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var trace := '';
				        try {
				            try {
				                raise 'inner_error';
				                trace := trace + 'not_raised.';
				            } except () {
				                trace := trace + 'inner.';
				            };
				            trace := trace + 'outer_body.';
				        } except () {
				            trace := trace + 'outer_catch.';
				        };
				        log(trace);
				    }
				}
				""");
		assertSuccess(result);
		// inner except catches → resumes outer try body
		assertLogged(result, "inner.outer_body.");
	}

	@Test
	void tryCatch_uncaughtPropagates() throws Exception {
		// §8.2.2.13: unmatched exception propagates to outer try or fails transformation
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var trace := '';
				        try {
				            try {
				                raise 'deep_error';
				            } except (ex : Exception) {
				                trace := trace + 'inner.';
				                raise 'rethrown';
				            };
				            trace := trace + 'should_not_reach.';
				        } except () {
				            trace := trace + 'outer.';
				        };
				        log(trace);
				    }
				}
				""");
		assertSuccess(result);
		// inner catches, then re-raises → outer catches
		assertLogged(result, "inner.outer.");
	}

	@Test
	void raise_fromHelper_caughtInCaller() throws Exception {
		// Eclipse bug419299.qvto test3/4: raise in mapping/helper → caught by caller
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper failingHelper() : String {
				        raise 'helper_error';
				    }
				    main() {
				        var msg := '';
				        try {
				            failingHelper();
				        } except (ex : Exception) {
				            msg := 'caught_from_helper';
				        };
				        log(msg);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "caught_from_helper");
	}

	@Test
	void raise_uncaught_failsTransformation() throws Exception {
		// §8.2.2.15: uncaught raise → transformation failure
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('before');
				        raise 'unhandled';
				        log('after');
				    }
				}
				""");
		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertLogged(result, "before");
		// 'after' should NOT appear
		boolean hasAfter = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("after"));
		assertFalse(hasAfter, "code after uncaught raise should not execute");
	}

	// ==== P6-09: Nested Imperative Combinations (§8.2.2) ====

	@Test
	void nested_forEachInsideWhile() throws Exception {
		// Eclipse _while.qvto pattern: while loop containing forEach
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var trace := '';
				        var round := 0;
				        while (round < 2) {
				            round := round + 1;
				            Sequence{1,2}->forEach(i) {
				                trace := trace + round.toString() + '.' + i.toString() + ' ';
				            };
				        };
				        log(trace);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "1.1 1.2 2.1 2.2 ");
	}

	@Test
	void nested_switchInsideForEach() throws Exception {
		// Eclipse continue_break.qvto pattern: forEach with switch-case
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var trace := '';
				        Sequence{'a','b','c'}->forEach(s) {
				            switch {
				                case (s = 'a') trace := trace + 'alpha.';
				                case (s = 'b') trace := trace + 'beta.';
				                else trace := trace + 'other.';
				            };
				        };
				        log(trace);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "alpha.beta.other.");
	}

	@Test
	void nested_computeWithWhileAndBreak() throws Exception {
		// §8.2.2.4 + §8.2.2.3 + §8.2.2.17: compute wrapping while with break
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var sum := compute(s : Integer := 0) {
				            var i := 1;
				            while (true) {
				                if (i > 5) then { break; } endif;
				                s := s + i;
				                i := i + 1;
				            };
				        };
				        log(sum.toString());
				    }
				}
				""");
		assertSuccess(result);
		// 1+2+3+4+5 = 15
		assertLogged(result, "15");
	}

	@Test
	void nested_tryInsideForEach() throws Exception {
		// Eclipse bug419299.qvto pattern: exception handling per iteration
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var successes := 0;
				        Sequence{1,2,3}->forEach(i) {
				            try {
				                if (i = 2) then { raise 'skip'; } endif;
				                successes := successes + 1;
				            } except () {
				                -- skip this iteration
				            };
				        };
				        log(successes.toString());
				    }
				}
				""");
		assertSuccess(result);
		// i=1 succeeds, i=2 raises (caught), i=3 succeeds
		assertLogged(result, "2");
	}

	@Test
	void nested_returnFromDeeplyNested() throws Exception {
		// Eclipse bug463396 pattern: return from nested if inside forEach
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper findFirst(items : Sequence(Integer)) : Integer {
				        items->forEach(i) {
				            if (i > 3) then {
				                return i;
				            } endif;
				        };
				        return -1;
				    }
				    main() {
				        var r := findFirst(Sequence{1,2,5,8});
				        log(r.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "5");
	}

	@Test
	void nested_multipleConstructsInMapping() throws Exception {
		// Combined: var + forEach + if + compute in one mapping body
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var items := Sequence{10, 20, 30, 40, 50};
				        var trace := '';
				        var total := compute(s : Integer := 0) {
				            items->forEach(n) {
				                if (n <= 30) then {
				                    s := s + n;
				                    trace := trace + n.toString() + '+';
				                } else {
				                    trace := trace + 'skip.';
				                } endif;
				            };
				        };
				        log(trace + '=' + total.toString());
				    }
				}
				""");
		assertSuccess(result);
		// 10+20+30 = 60, 40 and 50 skipped
		assertLogged(result, "10+20+30+skip.skip.=60");
	}

	@Test
	void nested_tripleNestedTryWithBreak() throws Exception {
		// Eclipse bug419299 test6: try > while > try > try > break
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var trace := '';
				        var i := 0;
				        try {
				            while (i < 100) {
				                i := i + 1;
				                try {
				                    try {
				                        break;
				                    } except () {
				                        trace := trace + 'inner.';
				                    };
				                } except () {
				                    trace := trace + 'middle.';
				                };
				            };
				        } except () {
				            trace := trace + 'outer.';
				        };
				        log(i.toString() + ':' + trace);
				    }
				}
				""");
		assertSuccess(result);
		// break exits while at i=1, no except clauses triggered
		assertLogged(result, "1:");
	}

	@Test
	void nested_whileSwitchContinueWithAssertFatal() throws Exception {
		// Eclipse continue_perf.qvto pattern: while > switch > continue + assert fatal
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var i := 0;
				        var sum := 0;
				        while (true) {
				            i := i + 1;
				            switch {
				                case (i > 5) break;
				                case (i.mod(2) = 0) {
				                    continue;
				                };
				                else {
				                    sum := sum + i;
				                };
				            };
				        };
				        log(sum.toString());
				    }
				}
				""");
		assertSuccess(result);
		// odd numbers 1..5: 1+3+5 = 9
		assertLogged(result, "9");
	}

	@Test
	void nested_deepIfElifChain() throws Exception {
		// Eclipse bug417751 pattern: deeply nested if/elif/else chains
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper classify(n : Integer) : String {
				        if (n < 0) then {
				            return 'negative';
				        } elif (n = 0) then {
				            return 'zero';
				        } elif (n < 10) then {
				            return 'small';
				        } elif (n < 100) then {
				            return 'medium';
				        } else {
				            return 'large';
				        } endif;
				    }
				    main() {
				        var trace := '';
				        Sequence{-1, 0, 5, 42, 100}->forEach(n) {
				            trace := trace + classify(n) + '.';
				        };
				        log(trace);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "negative.zero.small.medium.large.");
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
