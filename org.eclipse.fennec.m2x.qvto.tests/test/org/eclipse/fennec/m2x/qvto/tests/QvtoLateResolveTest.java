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
package org.eclipse.fennec.m2x.qvto.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.junit.jupiter.api.Test;

/**
 * Tests for QVT-O late (deferred) resolve expressions.
 */
class QvtoLateResolveTest extends AbstractQvtoEngineTest {

	@Test
	void lateResolve_assignsAfterMappingsComplete() throws Exception {
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
				        r.ref := self.late resolveone(t : TargetElement);
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(2, outExtent.getContents().size());
		// §8.1.11.3+7: self.late resolveone — explicit source filters trace to self's record.
		// Each target's ref should point to itself (target mapped from its own source).
		for (EObject target : outExtent.getContents()) {
			Object ref = target.eGet(target.eClass().getEStructuralFeature("ref"));
			assertNotNull(ref, "Late resolve should have assigned ref");
			assertEquals(target, ref, "self.resolveone should find the target mapped from self");
		}
	}

	@Test
	void lateResolveone_singleResult() throws Exception {
		EObject src = createSourceElement("single", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
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
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(1, outExtent.getContents().size());
		EObject target = outExtent.getContents().get(0);
		Object ref = target.eGet(target.eClass().getEStructuralFeature("ref"));
		assertNotNull(ref, "Late resolveone should have assigned ref");
		assertEquals(target, ref);
	}

	@Test
	void lateResolve_noMatch_leavesNull() throws Exception {
		// Use a condition that is always false — no trace result passes the filter.
		EObject src = createSourceElement("src", 1);
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
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(1, outExtent.getContents().size());
		EObject target = outExtent.getContents().get(0);
		Object ref = target.eGet(target.eClass().getEStructuralFeature("ref"));
		assertNull(ref, "Late resolve with no-match condition should leave ref null");
	}

	@Test
	void lateResolve_withCondition() throws Exception {
		EObject src1 = createSourceElement("x", 10);
		EObject src2 = createSourceElement("y", 20);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1, src2);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				        r.value := self.value;
				        r.ref := self.late resolveone(t : TargetElement | t.value > 15);
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(2, outExtent.getContents().size());
		// §8.1.11.3+7: self.late resolveone with condition and explicit source.
		// src1 (value=10) → self.resolveone finds target1 (value=10) → doesn't pass >15 → null
		// src2 (value=20) → self.resolveone finds target2 (value=20) → passes >15 → target2
		EObject target1 = outExtent.getContents().stream()
				.filter(e -> "x".equals(e.eGet(e.eClass().getEStructuralFeature("name"))))
				.findFirst().orElse(null);
		EObject target2 = outExtent.getContents().stream()
				.filter(e -> "y".equals(e.eGet(e.eClass().getEStructuralFeature("name"))))
				.findFirst().orElse(null);
		assertNotNull(target1);
		assertNotNull(target2);
		assertNull(target1.eGet(target1.eClass().getEStructuralFeature("ref")),
				"self=src1 → target1(value=10) doesn't match >15 → null");
		assertEquals(target2, target2.eGet(target2.eClass().getEStructuralFeature("ref")),
				"self=src2 → target2(value=20) matches >15 → target2");
	}

	@Test
	void lateInvresolve_basic() throws Exception {
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
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(1, outExtent.getContents().size());
		// invresolve(self) looks in trace for records where result==self and returns the source.
		// self is a SourceElement. No trace has a SourceElement as result → null
		EObject target = outExtent.getContents().get(0);
		Object ref = target.eGet(target.eClass().getEStructuralFeature("ref"));
		assertNull(ref, "invresolve should return null when no trace result matches self");
	}

	@Test
	void lateResolve_multipleDeferred() throws Exception {
		EObject src1 = createSourceElement("m1", 1);
		EObject src2 = createSourceElement("m2", 2);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1, src2);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				        r.ref := self.late resolveone(t : TargetElement);
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(2, outExtent.getContents().size());
		// §8.1.11.3+7: self.late resolveone — each deferred resolve should execute after all mappings.
		// With explicit self, each target's ref points to itself.
		for (EObject target : outExtent.getContents()) {
			Object ref = target.eGet(target.eClass().getEStructuralFeature("ref"));
			assertNotNull(ref, "Each late resolveone should have been executed");
			assertEquals(target, ref, "self.resolveone should find target mapped from self");
		}
	}
}
