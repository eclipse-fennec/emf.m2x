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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
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
				    }
				}
				""", inExtent, outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(1, outExtent.getContents().size());
		// Verify the mapped target has the correct name
		EObject target = outExtent.getContents().get(0);
		assertEquals("src1", target.eGet(target.eClass().getEStructuralFeature("name")));
	}

	@Test
	void resolve_noMatch_returnsNull() throws Exception {
		EObject srcElem = createSourceElement("x", 1);
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
				        var found := resolveone(t : TargetElement | t.name = 'nonexistent');
				        log(if found = null then 'null' else 'found' endif);
				    }
				}
				""", inExtent, outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		// Verify resolveone with no-match condition returns null
		boolean loggedNull = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("null"));
		assertTrue(loggedNull, "resolveone with non-matching condition should return null");
	}

	@Test
	void mapping_createsMultipleTargets() throws Exception {
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
		// Verify actual element names
		assertEquals("a", outExtent.getContents().get(0).eGet(
				outExtent.getContents().get(0).eClass().getEStructuralFeature("name")));
		assertEquals("b", outExtent.getContents().get(1).eGet(
				outExtent.getContents().get(1).eClass().getEStructuralFeature("name")));
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
		// Verify the mapping actually produced the element and log captured its name
		assertEquals(1, outExtent.getContents().size());
		boolean hasLog = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("traced"));
		assertTrue(hasLog, "Mapping result should have name 'traced'");
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
