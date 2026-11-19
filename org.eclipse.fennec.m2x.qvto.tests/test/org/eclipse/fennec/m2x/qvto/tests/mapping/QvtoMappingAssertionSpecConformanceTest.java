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
package org.eclipse.fennec.m2x.qvto.tests.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * Pre- and post-conditions of mappings as assertions, against QVT v1.3 §8.2.1.15 and §8.2.1.21 (#335).
 */
class QvtoMappingAssertionSpecConformanceTest extends AbstractQvtoEngineTest {

	private static final String HEADER = """
			modeltype SRC uses 'http://test/source/1.0';
			modeltype TGT uses 'http://test/target/1.0';
			""";

	private static QvtoExecutionResult run(String body) throws Exception {
		return executeWithExtents(HEADER + body,
				new BasicQvtoModelExtent(createSourceElement("a", 1)), emptyExtent());
	}

	@Test
	void aViolatedWhereClause_endsTheTransformation() throws Exception {
		// §8.2.1.15: "pre-conditions and post-conditions are assertions; any execution failure is fatal"
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::m() : TargetElement where { result.name = 'y' } { name := 'x'; }
				    main() {
				        s.objectsOfType(SourceElement)->first().map m();
				        log('after');
				    }
				}
				""");
		assertFatal(result, "where-postcondition failed for mapping: m");
		assertNotLogged(result, "after");
	}

	@Test
	void aViolatedWhereClause_insideAnOclExpression_endsTheTransformation() throws Exception {
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::m() : TargetElement where { false } { name := 'x'; }
				    main() {
				        log('name:' + s.objectsOfType(SourceElement)->first().map m().name);
				        log('after');
				    }
				}
				""");
		assertFatal(result, "where-postcondition failed for mapping: m");
		assertNotLogged(result, "after");
	}

	@Test
	void aHoldingWhereClause_letsTheTransformationGoOn() throws Exception {
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::m() : TargetElement where { result.name = 'x' } { name := 'x'; }
				    main() {
				        s.objectsOfType(SourceElement)->first().map m();
				        log('after');
				    }
				}
				""");
		assertTrue(result.isSuccess(), () -> result.diagnostics().toString());
		assertLogged(result, "after");
	}

	@Test
	void aViolatedWhereClause_isCaughtAsAssertionFailed() throws Exception {
		// §8.2.2.20: a fatal assertion raises AssertionFailed, which a try can catch
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::m() : TargetElement where { false } { name := 'x'; }
				    main() {
				        try { s.objectsOfType(SourceElement)->first().map m(); }
				        except (AssertionFailed) { log('caught'); };
				        log('after');
				    }
				}
				""");
		assertTrue(result.isSuccess(), () -> result.diagnostics().toString());
		assertLogged(result, "caught");
		assertLogged(result, "after");
	}

	@Test
	void aFailedXmapPrecondition_endsTheTransformation() throws Exception {
		// §8.2.1.21: "In strict mode, failure to evaluate the when clause as a pre-condition causes
		// the mapping execution to fail"
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::m() : TargetElement when { false } { name := 'x'; }
				    main() {
				        s.objectsOfType(SourceElement)->first().xmap m();
				        log('after');
				    }
				}
				""");
		assertFatal(result, "xmap pre-condition failed for mapping: m");
		assertNotLogged(result, "after");
	}

	@Test
	void anXmapOnANonConformingSource_endsTheTransformation() throws Exception {
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SpecialSourceElement::m() : TargetElement { name := 'x'; }
				    main() {
				        s.objectsOfType(SourceElement)->first().xmap m();
				        log('after');
				    }
				}
				""");
		assertFatal(result, "xmap pre-condition failed for mapping: m");
		assertNotLogged(result, "after");
	}

	@Test
	void theXmapPrecondition_isEvaluatedOnce() throws Exception {
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    helper probe() : Boolean { log('when-evaluated'); return true; }
				    mapping SourceElement::m() : TargetElement when { probe() } { name := 'x'; }
				    main() {
				        var r := s.objectsOfType(SourceElement)->first().xmap m();
				        log('name:' + r.name);
				    }
				}
				""");
		assertTrue(result.isSuccess(), () -> result.diagnostics().toString());
		assertLogged(result, "name:x");
		assertEquals(1, result.diagnostics().stream().filter(d -> d.getMessage().contains("when-evaluated")).count(),
				() -> result.diagnostics().toString());
	}

	@Test
	void aMappingCallAsTheSourceOfANavigation_isEvaluatedOnce() throws Exception {
		// The property read on a mapping call evaluated its source twice — once to look for an
		// intermediate property, once in OCL — which ran the guard twice (#335)
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    helper probe() : Boolean { log('when-evaluated'); return true; }
				    mapping SourceElement::m() : TargetElement when { probe() } { name := 'x'; }
				    main() {
				        log('name:' + s.objectsOfType(SourceElement)->first().xmap m().name);
				    }
				}
				""");
		assertTrue(result.isSuccess(), () -> result.diagnostics().toString());
		assertLogged(result, "name:x");
		assertEquals(1, result.diagnostics().stream().filter(d -> d.getMessage().contains("when-evaluated")).count(),
				() -> result.diagnostics().toString());
	}

	private static void assertFatal(QvtoExecutionResult result, String message) {
		assertNotNull(result);
		assertFalse(result.isSuccess(), () -> result.diagnostics().toString());
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() == Diagnostic.ERROR && d.getMessage().contains("Assertion failed: " + message)),
				() -> "Expected the fatal assertion '" + message + "' but diagnostics were: " + result.diagnostics());
	}

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains(expected)),
				() -> "Expected log containing '" + expected + "' but diagnostics were: " + result.diagnostics());
	}

	private static void assertNotLogged(QvtoExecutionResult result, String unexpected) {
		assertTrue(result.diagnostics().stream().noneMatch(d -> d.getMessage().equals("log: " + unexpected)),
				() -> "Did not expect log '" + unexpected + "' but diagnostics were: " + result.diagnostics());
	}
}
