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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;
import org.junit.jupiter.api.Test;

/**
 * Tests for QVT-O resolve expressions (basic resolve, without late resolve).
 */
class QvtoResolveExpTest extends AbstractQvtoEngineTest {

	@Test
	void resolve_afterMapping() throws Exception {
		EObject srcElem = createSourceElement("src1", 10);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(srcElem);
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
				        log('mapped');
				    }
				}
				""", inExtent, outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(1, outExtent.getContents().size(), "Should have 1 target element");
	}

	@Test
	void resolve_mappingCreatesTarget() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createA() : r : SourceElement {
				        r.name := 'a';
				    }
				    mapping createB() : r : SourceElement {
				        r.name := 'b';
				    }
				    main() {
				        map createA();
				        map createB();
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(2, outExtent.getContents().size(), "Two mappings should produce 2 elements");
	}

	@Test
	void resolve_noMatch() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    main() {
				        log('no-match test');
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertTrue(outExtent.getContents().isEmpty());
	}

	@Test
	void mapping_producesTraceRecord() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createElem() : r : SourceElement {
				        r.name := 'traced';
				    }
				    main() {
				        var elem := map createElem();
				        log(elem.name);
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		boolean hasLog = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("traced"));
		assertTrue(hasLog, "Trace should record mapping result");
	}

	@Test
	void mapping_multipleInExtent() throws Exception {
		EObject src1 = createSourceElement("s1", 1);
		EObject src2 = createSourceElement("s2", 2);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1, src2);
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
				    }
				}
				""", inExtent, outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(2, outExtent.getContents().size(), "Each source should produce one target");
	}

	@Test
	void mapping_withValueAssignment() throws Exception {
		EObject srcElem = createSourceElement("vtest", 42);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(srcElem);
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
				    }
				}
				""", inExtent, outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(1, outExtent.getContents().size());
		EObject target = outExtent.getContents().get(0);
		assertEquals("vtest", target.eGet(target.eClass().getEStructuralFeature("name")));
		assertEquals(42, target.eGet(target.eClass().getEStructuralFeature("value")));
	}
}
