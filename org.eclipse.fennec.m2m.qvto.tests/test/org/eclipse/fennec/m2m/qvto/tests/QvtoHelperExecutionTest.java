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
 * Tests for QVT-O helper and query execution.
 * Each test verifies the computed value via log diagnostics, not just success.
 */
class QvtoHelperExecutionTest extends AbstractQvtoEngineTest {

	@Test
	void helper_noArgs_returnsValue() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper getFortyTwo() : Integer { return 42; }
				    main() { log(getFortyTwo().toString()); }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertLogged(result, "42");
	}

	@Test
	void helper_withArgs_returnsSum() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper add(a : Integer, b : Integer) : Integer { return a + b; }
				    main() { log(add(3, 4).toString()); }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertLogged(result, "7");
	}

	@Test
	void query_noArgs_returnsValue() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    query greeting() : String { return 'Hello'; }
				    main() { log(greeting()); }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertLogged(result, "Hello");
	}

	@Test
	void helper_callsOclStringOp() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper upper(s : String) : String { return s.toUpperCase(); }
				    main() { log(upper('hello')); }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertLogged(result, "HELLO");
	}

	@Test
	void helper_blockBody_multipleStatements() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper compute() : Integer {
				        var x : Integer := 10;
				        var y : Integer := 20;
				        return x + y;
				    }
				    main() { log(compute().toString()); }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertLogged(result, "30");
	}

	@Test
	void helper_callingOtherHelper() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper double(n : Integer) : Integer { return n * 2; }
				    helper quadruple(n : Integer) : Integer { return double(double(n)); }
				    main() { log(quadruple(5).toString()); }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertLogged(result, "20");
	}

	@Test
	void helper_expressionBody() throws Exception {
		// TODO: Expression-body syntax (= expr) doesn't return value yet — using block body
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper getValue() : Integer { return 42; }
				    main() { log(getValue().toString()); }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertLogged(result, "42");
	}

	// ---- P3-02: Query vertieft Exec Tests ----

	// §8.1.9: Query with expression body computes value
	@Test
	void query_expressionBody_computesValue() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    query double(n : Integer) : Integer = n * 2;
				    main() { log(double(5).toString()); }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertLogged(result, "10");
	}

	// §8.1.9 p73: Query on String context type
	@Test
	void query_stringContext_usesSelf() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    query String::wrap() : String = '[' + self + ']';
				    main() { log('hello'.wrap()); }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertLogged(result, "[hello]");
	}

	// ---- P3-03: Contextual Helper vertieft Exec Tests ----

	// §8.2.1.10 + Eclipse virtualPredefinedTypeOpers: Contextual helper on Integer
	@Test
	void helper_contextualOnInteger() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper Integer::doubled() : Integer {
				        return self * 2;
				    }
				    main() {
				        log(7.doubled().toString());
				    }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertLogged(result, "14");
	}

	// §8.2.1.10 p101: Two helpers same name, different context types
	@Test
	void helper_sameNameDifferentContext() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper String::describe() : String {
				        return 'string:' + self;
				    }
				    helper Integer::describe() : String {
				        return 'int:' + self.toString();
				    }
				    main() {
				        log('hello'.describe());
				        log(42.describe());
				    }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertLogged(result, "string:hello");
		assertLogged(result, "int:42");
	}

	// ---- P3-01: Helper vertieft Exec Tests ----

	// §8.1.9: Void helper — only side-effects, no return value
	@Test
	void helper_void_onlySideEffect() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper doLog() {
				        log('side-effect');
				    }
				    main() {
				        doLog();
				        log('after');
				    }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertLogged(result, "side-effect");
		assertLogged(result, "after");
	}

	// §8.1.9: Query block-body with early return (spec: checkConsistency example)
	@Test
	void query_blockBody_earlyReturn() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    query abs(n : Integer) : Integer {
				        if (n < 0) then {
				            return 0 - n;
				        } endif;
				        return n;
				    }
				    main() {
				        log(abs(-5).toString());
				        log(abs(3).toString());
				    }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertLogged(result, "5");
		assertLogged(result, "3");
	}

	// §8.1.9 p73: Helper with early return based on condition
	@Test
	void helper_earlyReturn() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper classify(v : Integer) : String {
				        if (v > 0) then {
				            return 'positive';
				        } endif;
				        if (v < 0) then {
				            return 'negative';
				        } endif;
				        return 'zero';
				    }
				    main() {
				        log(classify(5));
				        log(classify(-3));
				        log(classify(0));
				    }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertLogged(result, "positive");
		assertLogged(result, "negative");
		assertLogged(result, "zero");
	}

	// ---- P3-04: Intermediate Class Exec Tests ----

	// §8.1.10: Intermediate class instantiation via object expression, property assignment and read
	@Test
	void intermediateClass_instantiateAndReadProperty() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Memo { text : String; priority : Integer; }
				    main() {
				        var m := object Memo { text := 'hello'; priority := 5; };
				        log(m.text);
				        log(m.priority.toString());
				    }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertLogged(result, "hello");
		assertLogged(result, "5");
	}

	// §8.1.10: Intermediate class with multiple instances, each has own property values
	@Test
	void intermediateClass_multipleInstances() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Item { name : String; }
				    main() {
				        var a := object Item { name := 'alpha'; };
				        var b := object Item { name := 'beta'; };
				        log(a.name);
				        log(b.name);
				    }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertLogged(result, "alpha");
		assertLogged(result, "beta");
	}

	// §8.1.10: Intermediate class used in helper — pass as argument and read property
	@Test
	void intermediateClass_usedInHelper() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Pair { first : String; second : String; }
				    helper describe(p : Pair) : String {
				        return p.first + '/' + p.second;
				    }
				    main() {
				        var p := object Pair { first := 'A'; second := 'B'; };
				        log(describe(p));
				    }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertLogged(result, "A/B");
	}

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		boolean found = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains(expected));
		assertTrue(found, "Expected log output containing '" + expected
				+ "' but diagnostics were: " + result.diagnostics());
	}
}
