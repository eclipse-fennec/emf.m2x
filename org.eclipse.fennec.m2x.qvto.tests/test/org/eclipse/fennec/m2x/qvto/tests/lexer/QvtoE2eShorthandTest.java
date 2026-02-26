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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * E2E tests for QVT-O v1.3 §8.4.4 shorthand operators.
 *
 * <p>GAP-19: {@code %} (format), GAP-20: {@code #}/{@code ##}/{@code *}, GAP-21: {@code !->}
 *
 * <p><b>SPEC-FIRST:</b> Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eShorthandTest extends AbstractQvtoEngineTest {

	// ---- GAP-19: % format operator ----

	@Test
	void formatOperator_formatsString() throws Exception {
		// §8.4.4 item 4: "Hello %s" % 'World' → "Hello World"
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var msg := 'Hello %s' % 'World';
				        log(msg);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "Hello World");
	}

	@Test
	void formatOperator_multipleArgs() throws Exception {
		// §8.4.4: "%s=%d" % Sequence{'x', 42} → "x=42"
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var msg := '%s=%d' % Sequence{'x', 42};
				        log(msg);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "x=42");
	}

	@Test
	void formatOperator_calledAsMethod() throws Exception {
		// format() as method call (existing QVT-O §8.3.16.1 operation)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var msg := 'Hello %s'.format('World');
				        log(msg);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "Hello World");
	}

	// ---- GAP-20: # / ## / * unary operators ----

	@Test
	void hashOperator_inSelect_primitiveTypes() throws Exception {
		// §8.4.4 item 1: #Type → oclIsKindOf — filter heterogeneous collection
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var items := Sequence{'hello', 42, true, 'world'};
				        var strings := items->select(#String);
				        log('count:' + strings->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "count:2");
	}

	@Test
	void hashOperator_inSelect_ecoreTypes() throws Exception {
		// §8.4.4 item 1: list->select(#SpecialSourceElement) filters by oclIsKindOf
		EObject se1 = createSourceElement("a", 1);
		EObject se2 = createSpecialSourceElement("b", 2, "tag");
		var inExtent = new BasicQvtoModelExtent(se1, se2);

		QvtoExecutionResult result = execute("""
				modeltype src uses source('http://test/source');
				transformation test(in s : src) {
				    main() {
				        var roots := s.rootObjects();
				        var specials := roots->select(#SpecialSourceElement);
				        log('count:' + specials->size().repr());
				    }
				}
				""", QvtoExecutionContext.of(inExtent));
		assertSuccess(result);
		assertLogged(result, "count:1");
	}

	@Test
	void doubleHashOperator_inSelect_primitiveTypes() throws Exception {
		// §8.4.4 item 2: ##Type → oclIsTypeOf — filter heterogeneous collection
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var items := Sequence{'hello', 42, true, 3.14};
				        var reals := items->select(##Real);
				        log('count:' + reals->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "count:1");
	}

	@Test
	void doubleHashOperator_inSelect_ecoreTypes() throws Exception {
		// §8.4.4 item 2: list->select(##SourceElement) filters by oclIsTypeOf (exact)
		EObject se1 = createSourceElement("a", 1);
		EObject se2 = createSpecialSourceElement("b", 2, "tag");
		var inExtent = new BasicQvtoModelExtent(se1, se2);

		QvtoExecutionResult result = execute("""
				modeltype src uses source('http://test/source');
				transformation test(in s : src) {
				    main() {
				        var roots := s.rootObjects();
				        var exactOnly := roots->select(##SourceElement);
				        log('count:' + exactOnly->size().repr());
				    }
				}
				""", QvtoExecutionContext.of(inExtent));
		assertSuccess(result);
		// Only se1 is exactly SourceElement, se2 is SpecialSourceElement
		assertLogged(result, "count:1");
	}

	@Test
	void unaryStarOperator_unsupported() throws Exception {
		// §8.4.4 item 3: *"stereo" → stereotypedBy (UML-specific, P10-07)
		// Should parse but evaluate to OclInvalid
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var r := *'myStereotype';
				        log('r:' + r.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "r:OclInvalid");
	}

	// ---- GAP-21: !-> not-arrow operator ----

	@Test
	void notArrow_negatesBoolean() throws Exception {
		// §8.4.4: list!->isEmpty() → not(list->isEmpty()) → true when list is not empty
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var items := Sequence{1, 2, 3};
				        var r := items!->isEmpty();
				        log('notEmpty:' + r.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "notEmpty:true");
	}

	@Test
	void notArrow_emptyCollection() throws Exception {
		// §8.4.4: empty!->isEmpty() → not(true) → false
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var items := Sequence{};
				        var r := items!->isEmpty();
				        log('notEmpty:' + r.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "notEmpty:false");
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
