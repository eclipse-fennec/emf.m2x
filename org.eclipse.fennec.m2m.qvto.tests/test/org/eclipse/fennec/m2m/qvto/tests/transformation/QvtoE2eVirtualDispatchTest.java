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
package org.eclipse.fennec.m2m.qvto.tests.transformation;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O virtual dispatch on predefined types.
 *
 * <p>QVT-O allows helpers/queries with context parameters on predefined
 * types (Integer, Real, String, Boolean, OclAny) and collection types.
 * Dispatch selects the most specific matching context type at runtime.
 *
 * <p>Based on Eclipse reference test: {@code virtualPredefinedTypeOpers.qvto}
 *
 * <p><b>SPEC-FIRST:</b> Tests written against QVT-O v1.3 §8.2.1.10
 * (OperationBody/context dispatch) and §8.1.7 (Helper operations).
 * Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eVirtualDispatchTest extends AbstractQvtoEngineTest {

	// ==== P9-01: Virtual Dispatch on Predefined Types ====

	// ---- Helper on Integer ----

	@Test
	void dispatch_helperOnInteger() throws Exception {
		// Eclipse: helper Integer::addOnInteger(i : Integer) : Integer
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper Integer::addOn(i : Integer) : Integer {
				        return self + i;
				    }
				    main() {
				        var i : Integer := 1;
				        log('result:' + i.addOn(10).repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "result:11");
	}

	// ---- Helper on Real ----

	@Test
	void dispatch_helperOnReal() throws Exception {
		// Eclipse: helper Real::addOnReal(r : Real) : Real
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper Real::addOn(r : Real) : Real {
				        return self + r;
				    }
				    main() {
				        var r : Real := 1.5;
				        log('result:' + r.addOn(1000.0).repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "result:1001.5");
	}

	// ---- Integer→Real widening in dispatch ----

	@Test
	void dispatch_integerConformsToReal() throws Exception {
		// Eclipse: i.addOnReal(100) works because Integer conforms to Real
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper Real::addOn(r : Real) : Real {
				        return self + r;
				    }
				    main() {
				        var i : Integer := 1;
				        log('result:' + i.addOn(100).repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "result:101");
	}

	// ---- Helper on OclAny — catches all types ----

	@Test
	void dispatch_helperOnOclAny() throws Exception {
		// Eclipse: helper OclAny::echoMe() : OclAny { return self; }
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper OclAny::echoMe() : OclAny {
				        return self;
				    }
				    main() {
				        log('bool:' + false.echoMe().repr());
				        log('real:' + 1.5.echoMe().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "bool:false");
		assertLogged(result, "real:1.5");
	}

	// ---- Virtual dispatch: most specific type wins ----

	@Test
	void dispatch_mostSpecificTypeWins() throws Exception {
		// Eclipse: String::virtOper vs OclAny::virtOper — String wins for strings
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper String::virtOper() : String {
				        return 'String::virtOper';
				    }
				    helper OclAny::virtOper() : String {
				        return 'OclAny::virtOper';
				    }
				    main() {
				        var a : OclAny := 'aString';
				        log('string:' + a.virtOper());
				        log('bool:' + true.virtOper());
				    }
				}
				""");
		assertSuccess(result);
		// Dynamic dispatch: 'aString' is a String at runtime → String::virtOper
		assertLogged(result, "string:String::virtOper");
		// Boolean has no specific helper → falls back to OclAny::virtOper
		assertLogged(result, "bool:OclAny::virtOper");
	}

	// ---- Same name, different context types (Integer vs String) ----

	@Test
	void dispatch_sameNameDifferentContextTypes() throws Exception {
		// Eclipse: two helpers 'describe' on different context types
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper String::describe() : String {
				        return 'str:' + self;
				    }
				    helper Integer::describe() : String {
				        return 'int:' + self.repr();
				    }
				    helper OclAny::describe() : String {
				        return 'any:' + self.repr();
				    }
				    main() {
				        log('hello'.describe());
				        log(42.describe());
				        log(true.describe());
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "str:hello", "int:42", "any:true");
	}

	// ---- Integer calls Real helper (widening dispatch) ----

	@Test
	void dispatch_integerCallsQueryOnReal() throws Exception {
		// Eclipse bug 244730: Integer::xone() calls Real::xtwo()
		QvtoExecutionResult result = execute("""
				transformation test() {
				    query Integer::xone() : Integer {
				        return self.xtwo();
				    }
				    query Real::xtwo() : Integer {
				        return 5;
				    }
				    main() {
				        log('result:' + 1.xone().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "result:5");
	}

	// ---- Helper on Boolean ----

	@Test
	void dispatch_helperOnBoolean() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper Boolean::asYesNo() : String {
				        if self then return 'YES' else return 'NO' endif;
				    }
				    main() {
				        log(true.asYesNo());
				        log(false.asYesNo());
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "YES", "NO");
	}

	// ---- Helper with parameter on String ----

	@Test
	void dispatch_helperOnStringWithParam() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper String::wrap(prefix : String, suffix : String) : String {
				        return prefix + self + suffix;
				    }
				    main() {
				        log('hello'.wrap('[', ']'));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "[hello]");
	}

	// ---- Non-contextual helper (no dispatch, module-level) ----

	@Test
	void dispatch_nonContextualFallback() throws Exception {
		// Non-contextual helpers are dispatched as module operations
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper format(x : OclAny) : String {
				        return '<' + x.repr() + '>';
				    }
				    main() {
				        log(format(42));
				        log(format('hi'));
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "<42>", "<hi>");
	}

	// ---- OclAny helper invoked on null context → null propagation (§8.1.19) ----

	@Test
	void dispatch_nullContextPropagatesNull() throws Exception {
		// §8.1.19: contextual operation on null → propagate null
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper String::greet() : String {
				        return 'hello ' + self;
				    }
				    main() {
				        var s : String := null;
				        var result := s.greet();
				        if result = null then
				            log('null-propagated')
				        else
				            log('unexpected:' + result)
				        endif;
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "null-propagated");
	}

	// ---- Query on Real returning Integer (implicit widening) ----

	@Test
	void dispatch_queryOnRealReturnsInteger() throws Exception {
		// Eclipse: query Real::xtwo() : Integer { return 5; }
		QvtoExecutionResult result = execute("""
				transformation test() {
				    query Real::doubled() : Real {
				        return self + self;
				    }
				    main() {
				        var r : Real := 3.5;
				        log('result:' + r.doubled().repr());
				        -- Integer conforms to Real, so this should also work:
				        log('int:' + 2.doubled().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "result:7.0", "int:4");
	}

	// ---- Multiple dispatch levels: Integer < Real < OclAny ----

	@Test
	void dispatch_threeLevel_Integer_Real_OclAny() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper Integer::info() : String {
				        return 'Integer';
				    }
				    helper Real::info() : String {
				        return 'Real';
				    }
				    helper OclAny::info() : String {
				        return 'OclAny';
				    }
				    main() {
				        log(42.info());
				        log(3.14.info());
				        log('text'.info());
				        log(true.info());
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "Integer", "Real", "OclAny", "OclAny");
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

	private static void assertLoggedInOrder(QvtoExecutionResult result, String... expected) {
		var messages = result.diagnostics().stream()
				.map(d -> d.getMessage())
				.toList();
		int lastIdx = -1;
		for (String exp : expected) {
			boolean found = false;
			for (int i = lastIdx + 1; i < messages.size(); i++) {
				if (messages.get(i).contains(exp)) {
					lastIdx = i;
					found = true;
					break;
				}
			}
			assertTrue(found, "Expected log containing '" + exp
					+ "' after index " + lastIdx + " but diagnostics were: " + messages);
		}
	}
}
