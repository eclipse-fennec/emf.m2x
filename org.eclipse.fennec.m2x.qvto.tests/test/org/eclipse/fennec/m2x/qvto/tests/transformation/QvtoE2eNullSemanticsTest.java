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
 * End-to-end tests for null semantics in QVT-O (§8.1.19).
 *
 * <p>{@code null} is a universal literal value that conforms to any type.
 * It can be explicitly or implicitly returned to mean "absence of value".
 *
 * <p>Eclipse reference: {@code calloclIsUndefinedforundefined.qvto},
 * {@code nullsource.qvto}, {@code callvirtforundefined.qvto}
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eNullSemanticsTest extends AbstractQvtoEngineTest {

	// ==== P7-04: Null Semantics (§8.1.19) ====

	@Test
	void null_conformsToAnyType() throws Exception {
		// §8.1.19: null conforms to any type — can be assigned to String, Integer, etc.
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s : String := null;
				        var i : Integer := null;
				        var b : Boolean := null;
				        log('ok');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "ok");
	}

	@Test
	void null_oclIsUndefined() throws Exception {
		// §8.1.19 + Eclipse calloclIsUndefinedforundefined.qvto:
		// null.oclIsUndefined() = true
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var p : String := null;
				        var msg := if p.oclIsUndefined() then 'undefined' else 'defined' endif;
				        log(msg);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "undefined");
	}

	@Test
	void null_equality() throws Exception {
		// §8.1.19: null = null is true, null <> 'x' is true
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var a : String := null;
				        var b : String := null;
				        if (a = b) {
				            log('null_eq_null');
				        };
				        if (a <> 'x') {
				            log('null_neq_x');
				        };
				        if (null = null) {
				            log('literal_eq');
				        };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "null_eq_null");
		assertLogged(result, "null_neq_x");
		assertLogged(result, "literal_eq");
	}

	@Test
	void null_methodCallOnNull() throws Exception {
		// §8.1.19 + Eclipse nullsource.qvto / callvirtforundefined.qvto:
		// Method call on null returns undefined (not an error)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper String::decorate() : String {
				        return 'decorated:' + self;
				    }
				    main() {
				        var s : String := null;
				        var decorated := s.decorate();
				        if (decorated.oclIsUndefined()) {
				            log('call_on_null_is_undefined');
				        } else {
				            log('call_on_null_returned:' + decorated);
				        };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "call_on_null_is_undefined");
	}

	@Test
	void null_inCollectionOperations() throws Exception {
		// §8.1.19: null in collections — should be handled gracefully
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s := Sequence{1, null, 3};
				        log(s->size().toString());
				        var nonNull := s->excluding(null);
				        log(nonNull->size().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "3");
		assertLogged(result, "2");
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
