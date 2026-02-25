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
 * End-to-end tests for boxing / numeric widening (P9-05).
 *
 * <p>OCL v2.4 §11.5.1 (Real) / §11.5.2 (Integer): Integer is a subtype
 * of Real. Integer values are implicitly widened to Real when needed.
 *
 * <p>Eclipse reference: {@code boxing/boxing.qvto}
 *
 * <p><b>SPEC-FIRST:</b> Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eBoxingWideningTest extends AbstractQvtoEngineTest {

	// ==== P9-05: Boxing / Numeric Widening ====

	// ---- Integer arithmetic stays Integer ----

	@Test
	void boxing_integerPlusIntegerStaysInteger() throws Exception {
		// Eclipse boxing.qvto: var i2 := i1 + counter + 1; (all Integer)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property counter : Integer = 1;
				    main() {
				        var i1 := 1;
				        var i2 := i1 + counter + 1;
				        log('i2:' + i2.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "i2:3");
	}

	// ---- Integer literal widened to Real in property init ----

	@Test
	void boxing_integerLiteralInRealProperty() throws Exception {
		// Eclipse boxing.qvto: property ratio: Real = 1;
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property ratio : Real = 1;
				    main() {
				        log('ratio:' + ratio.repr());
				    }
				}
				""");
		assertSuccess(result);
		// Integer 1 widened to Real 1.0
		assertLogged(result, "ratio:1");
	}

	// ---- Query returns Integer but declared Real ----

	@Test
	void boxing_queryReturnsIntegerAsReal() throws Exception {
		// Eclipse boxing.qvto: query bar(): Real { return 1 }
		QvtoExecutionResult result = execute("""
				transformation test() {
				    query getReal() : Real {
				        return 1;
				    }
				    main() {
				        var r := getReal();
				        log('r:' + r.repr());
				        var sum := r + 0.5;
				        log('sum:' + sum.repr());
				    }
				}
				""");
		assertSuccess(result);
		// Integer 1 returned from Real-typed query — should work
		assertLogged(result, "r:1");
		assertLogged(result, "sum:1.5");
	}

	// ---- Mixed Integer + Real arithmetic ----

	@Test
	void boxing_mixedIntegerRealArithmetic() throws Exception {
		// Eclipse boxing.qvto: var f2 := f1 + ratio + bar() + 1;
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property ratio : Real = 1;
				    main() {
				        var f1 := 1.0;
				        var sum := f1 + ratio + 1;
				        log('sum:' + sum.repr());
				    }
				}
				""");
		assertSuccess(result);
		// 1.0 + 1 + 1 = 3.0
		assertLogged(result, "sum:3.0");
	}

	@Test
	void boxing_integerAddedToReal() throws Exception {
		// §11.5.1: Real + Integer → Real
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var r : Real := 2.5;
				        var i : Integer := 3;
				        var sum := r + i;
				        log('sum:' + sum.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "sum:5.5");
	}

	@Test
	void boxing_integerMultipliedByReal() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var product := 3 * 1.5;
				        log('product:' + product.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "product:4.5");
	}

	// ---- Integer division produces Real ----

	@Test
	void boxing_integerDivisionProducesReal() throws Exception {
		// §11.5.2: Integer::/(i: Integer) : Real — integer / integer = real
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var result := 7 / 2;
				        log('div:' + result.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "div:3.5");
	}

	// ---- Integer comparison with Real ----

	@Test
	void boxing_integerComparedWithReal() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('eq:' + (1 = 1.0).toString());
				        log('lt:' + (1 < 1.5).toString());
				        log('gt:' + (2 > 1.5).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "eq:true");
		assertLogged(result, "lt:true");
		assertLogged(result, "gt:true");
	}

	// ---- Real assigned to Integer variable ----

	@Test
	void boxing_realToIntegerTruncation() throws Exception {
		// Integer::round() / floor() for explicit conversion
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var r := 3.7;
				        var i := r.round();
				        log('round:' + i.repr());
				        var j := r.floor();
				        log('floor:' + j.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "round:4");
		assertLogged(result, "floor:3");
	}

	// ---- Module property with expression mixing types ----

	@Test
	void boxing_modulePropertyMixedExpression() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property base : Integer = 10;
				    property scale : Real = 1.5;
				    main() {
				        var result := base * scale;
				        log('result:' + result.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "result:15.0");
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
