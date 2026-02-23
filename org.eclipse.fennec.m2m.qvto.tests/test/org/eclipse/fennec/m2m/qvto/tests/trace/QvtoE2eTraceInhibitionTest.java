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
package org.eclipse.fennec.m2m.qvto.tests.trace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.api.QvtoEvaluationOptions;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O implicit inhibition (trace caching).
 *
 * Spec references:
 * - §8.2.1.15 (p106): "If the guard succeeds, the relation trace is checked to find
 *   out whether the relation already holds. If so, the out parameters are populated
 *   using the corresponding trace tuples [...] Otherwise the body of the operation
 *   is executed."
 * - §8.1.5 (p106): Implicit inhibition — same input always maps to same output
 */
class QvtoE2eTraceInhibitionTest extends AbstractQvtoEngineTest {

	// §8.2.1.15: Same object mapped twice → body executes only once
	@Test
	void inhibition_bodyExecutesOnlyOnce() throws Exception {
		EObject src = createSourceElement("x", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := self.name;
				        value := self.value;
				        log('body-executed');
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget();
				            e.map toTarget();
				            e.map toTarget();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		// Body should execute only once — only 1 log entry
		long bodyCount = result.diagnostics().stream()
				.filter(d -> d.getMessage().contains("body-executed"))
				.count();
		assertEquals(1, bodyCount, "Body should execute only once due to implicit inhibition");
	}

	// §8.2.1.15: Same object mapped twice → same result returned
	@Test
	void inhibition_sameResultReturned() throws Exception {
		EObject src = createSourceElement("y", 2);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := self.name;
				        value := self.value;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            var r1 : TargetElement := e.map toTarget();
				            var r2 : TargetElement := e.map toTarget();
				            log('same:' + (r1 = r2).toString());
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "same:true");
		// Only 1 target element created (not 2)
		assertEquals(1, outExtent.getContents().size());
	}

	// §8.2.1.15: Different objects → different results (no inhibition)
	@Test
	void inhibition_differentObjects_differentResults() throws Exception {
		EObject src1 = createSourceElement("a", 1);
		EObject src2 = createSourceElement("b", 2);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1, src2);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := self.name;
				        value := self.value;
				        log('body:' + self.name);
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		// Both bodies should execute (different source objects)
		assertLogged(result, "body:a");
		assertLogged(result, "body:b");
		assertEquals(2, outExtent.getContents().size());
	}

	// §8.2.1.15: Same object, different mappings → no inhibition (separate caches)
	@Test
	void inhibition_differentMappings_noCacheConflict() throws Exception {
		EObject src = createSourceElement("z", 9);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := 'target-' + self.name;
				        value := self.value;
				        log('toTarget-body');
				    }
				    mapping SourceElement::toOther() : TargetElement {
				        name := 'other-' + self.name;
				        value := self.value + 100;
				        log('toOther-body');
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget();
				            e.map toOther();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "toTarget-body");
		assertLogged(result, "toOther-body");
		assertEquals(2, outExtent.getContents().size());
	}

	// §8.2.1.15: Inhibition across nested mapping calls
	@Test
	void inhibition_nestedMappings() throws Exception {
		EObject src = createSourceElement("n", 3);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::inner() : TargetElement {
				        name := 'inner-' + self.name;
				        value := self.value;
				        log('inner-body');
				    }
				    mapping SourceElement::outer() : TargetElement {
				        name := 'outer-' + self.name;
				        value := self.value;
				        log('outer-body');
				        -- call inner on same object
				        self.map inner();
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map outer();
				            -- call inner again: should be cached
				            e.map inner();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		// inner-body should execute once (from outer), second call cached
		long innerCount = result.diagnostics().stream()
				.filter(d -> d.getMessage().contains("inner-body"))
				.count();
		assertEquals(1, innerCount, "inner mapping body should execute only once");
		assertLogged(result, "outer-body");
	}

	// §8.2.1.15: Inhibition with resolveone — cached result findable
	@Test
	void inhibition_resolveFindsResult() throws Exception {
		EObject src = createSourceElement("r", 7);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := 'resolved-' + self.name;
				        value := self.value;
				    }
				    main() {
				        var src : SourceElement := null;
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget();
				            src := e;
				        };
				        -- resolve should find the mapped result
				        var found : TargetElement := src.resolveone(TargetElement);
				        log('found:' + found.name);
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "found:resolved-r");
	}

	// ---- P4-02: Inhibition deep-dive (§8.1.11.2) ----

	// §8.1.11.2: Same self + same in-param → inhibited
	@Test
	void inhibition_sameInParam_inhibited() throws Exception {
		EObject src = createSourceElement("x", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget(in label : String) : TargetElement {
				        name := label;
				        value := self.value;
				        log('body:' + label);
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget('same');
				            e.map toTarget('same');
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		long bodyCount = result.diagnostics().stream()
				.filter(d -> d.getMessage().contains("body:same"))
				.count();
		assertEquals(1, bodyCount,
				"Same self + same in-param should inhibit re-execution");
	}

	// §8.1.11.2: Same self + different in-param → NOT inhibited
	@Test
	void inhibition_differentInParam_notInhibited() throws Exception {
		EObject src = createSourceElement("x", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget(in label : String) : TargetElement {
				        name := label;
				        value := self.value;
				        log('body:' + label);
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget('first');
				            e.map toTarget('second');
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "body:first");
		assertLogged(result, "body:second");
		assertEquals(2, outExtent.getContents().size(),
				"Different in-params should produce separate results");
	}

	// §8.1.11.2: DataType deep-value equality — same String value inhibits
	@Test
	void inhibition_dataTypeDeepEquality() throws Exception {
		EObject src = createSourceElement("x", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget(in n : Integer) : TargetElement {
				        name := self.name;
				        value := n;
				        log('body:' + n.toString());
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget(42);
				            e.map toTarget(42);
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		long bodyCount = result.diagnostics().stream()
				.filter(d -> d.getMessage().contains("body:42"))
				.count();
		assertEquals(1, bodyCount,
				"Same DataType value (Integer 42) should inhibit via deep equality");
	}

	// §8.1.11.2: Mutable result after inhibition — changes visible
	@Test
	void inhibition_mutableResult() throws Exception {
		EObject src = createSourceElement("m", 5);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := 'original';
				        value := self.value;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            var r1 : TargetElement := e.map toTarget();
				            r1.name := 'mutated';
				            var r2 : TargetElement := e.map toTarget();
				            log('r2-name:' + r2.name);
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		// r2 is same object as r1, so mutation is visible
		assertLogged(result, "r2-name:mutated");
	}

	// §8.1.11.2: No new trace-record on inhibition
	@Test
	void inhibition_noNewTraceRecord() throws Exception {
		EObject src = createSourceElement("t", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoEvaluationOptions options = QvtoEvaluationOptions.defaults().withTracing(true);
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget();
				            e.map toTarget();
				            e.map toTarget();
				        };
				    }
				}
				""", options, inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, result.trace().getTraceRecords().size(),
				"Inhibited re-executions should not create new trace records");
	}

	// §8.1.11.1+2: when-guard failure prevents trace → no inhibition on next call
	@Test
	void inhibition_whenGuardFailure_noInhibitOnRetry() throws Exception {
		EObject src = createSourceElement("g", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toGuarded(in pass : Boolean) : TargetElement
				        when { pass } {
				        name := self.name;
				        log('guarded-body');
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toGuarded(false);
				            e.map toGuarded(true);
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		// First call: when { false } → skip, no trace → no inhibition
		// Second call: when { true } → executes body
		assertLogged(result, "guarded-body");
		assertEquals(1, outExtent.getContents().size());
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
