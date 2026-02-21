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
package org.eclipse.fennec.m2m.qvto.tests.resolve;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O resolve expressions.
 * Tests cover forward/inverse resolve, resolveone, resolveIn, conditions,
 * late resolve, and implicit source.
 */
class QvtoE2eResolveTest extends AbstractQvtoEngineTest {

	// ---- resolveone — forward, single result ----

	@Test
	void resolveone_afterMapping_findsTarget() throws Exception {
		EObject src = createSourceElement("r1", 10);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				        var found := s.objectsOfType(SourceElement)->asSequence()->first().resolveone(TargetElement);
				        log(if found = null then 'null' else found.name endif);
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "r1");
	}

	// ---- resolveone — no match returns null ----

	@Test
	void resolveone_noMatch_returnsNull() throws Exception {
		EObject src = createSourceElement("x", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				        var found := resolveone(t : TargetElement | t.name = 'nonexistent');
				        log(if found = null then 'null' else 'found' endif);
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "null");
	}

	// ---- Resolve with condition ----

	@Test
	void resolveone_withCondition_findsMatchingTarget() throws Exception {
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(
				createSourceElement("alpha", 1),
				createSourceElement("beta", 2));
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				        r.value := self.value;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				        var found := resolveone(t : TargetElement | t.name = 'beta');
				        log(if found = null then 'null' else found.value.toString() endif);
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "2");
	}

	// ---- Late resolve — deferred execution ----

	@Test
	void lateResolveone_assignsAfterAllMappings() throws Exception {
		EObject src1 = createSourceElement("a", 1);
		EObject src2 = createSourceElement("b", 2);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1, src2);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				        r.ref := late resolveone(t : TargetElement);
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(2, outExtent.getContents().size());
		for (EObject target : outExtent.getContents()) {
			Object ref = eGet(target, "ref");
			assertNotNull(ref, "Late resolve should have assigned ref");
		}
	}

	@Test
	void lateResolveone_noMatch_leavesNull() throws Exception {
		EObject src = createSourceElement("lonely", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				        r.ref := late resolveone(x : TargetElement | x.name = 'nonexistent');
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		EObject target = outExtent.getContents().get(0);
		assertNull(eGet(target, "ref"), "No match → ref should be null");
	}

	@Test
	void lateResolve_withCondition_selectsMatchingTarget() throws Exception {
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(
				createSourceElement("x", 10),
				createSourceElement("y", 20));
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				        r.value := self.value;
				        r.ref := late resolveone(t : TargetElement | t.value > 15);
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(2, outExtent.getContents().size());
		EObject target1 = outExtent.getContents().stream()
				.filter(e -> "x".equals(eGet(e, "name")))
				.findFirst().orElse(null);
		EObject target2 = outExtent.getContents().stream()
				.filter(e -> "y".equals(eGet(e, "name")))
				.findFirst().orElse(null);
		assertNotNull(target1);
		assertNotNull(target2);
		// target1 resolve(self=src1) → target1 (value=10) doesn't pass >15
		assertNull(eGet(target1, "ref"), "value=10 should not pass >15");
		// target2 resolve(self=src2) → target2 (value=20) passes >15
		assertEquals(target2, eGet(target2, "ref"), "value=20 should pass >15");
	}

	// ---- invresolve — reverse lookup ----

	@Test
	void lateInvresolveone_noTraceForResult_returnsNull() throws Exception {
		EObject src = createSourceElement("inv", 5);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				        r.ref := late invresolveone(t : TargetElement);
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		EObject target = outExtent.getContents().get(0);
		// invresolve(self): self is a SourceElement, no trace result is a SourceElement → null
		assertNull(eGet(target, "ref"));
	}

	// ---- Resolve with empty trace ----

	@Test
	void resolveone_emptyTrace_returnsNull() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    main() {
				        var found := resolveone(TargetElement);
				        log(if found = null then 'null' else 'found' endif);
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertLogged(result, "null");
	}

	// ---- Resolve with multiple mappings for same source type ----

	@Test
	void resolveone_multipleMappingsForSameType() throws Exception {
		EObject src = createSourceElement("multi", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTargetA() : r : TargetElement {
				        r.name := 'A-' + self.name;
				    }
				    mapping SourceElement::toTargetB() : r : TargetElement {
				        r.name := 'B-' + self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTargetA();
				            e.map toTargetB();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(2, outExtent.getContents().size());
	}

	// ---- Late resolve — multiple deferred tasks ----

	@Test
	void lateResolve_multipleDeferred_allResolved() throws Exception {
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(
				createSourceElement("d1", 1),
				createSourceElement("d2", 2),
				createSourceElement("d3", 3));
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				        r.ref := late resolveone(t : TargetElement);
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(3, outExtent.getContents().size());
		for (EObject target : outExtent.getContents()) {
			assertNotNull(eGet(target, "ref"), "All late resolves should be executed");
		}
	}

	// ---- Resolve with mapping that produces container ----

	@Test
	void resolve_afterContainerMapping() throws Exception {
		EObject src = createSourceElement("cont", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				        var count := outExtent.getContents().size();
				        log('created');
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
	}

	// ---- Late resolve — implicit source (self) ----

	@Test
	void lateResolve_implicitSource_usesSelf() throws Exception {
		EObject src = createSourceElement("impl", 7);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				        r.ref := late resolveone(TargetElement);
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		EObject target = outExtent.getContents().get(0);
		assertNotNull(eGet(target, "ref"), "Implicit source should use self");
		assertEquals(target, eGet(target, "ref"));
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

	private static Object eGet(EObject obj, String featureName) {
		return obj.eGet(obj.eClass().getEStructuralFeature(featureName));
	}
}
