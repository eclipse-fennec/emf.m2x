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
package org.eclipse.fennec.m2m.qvto.tests.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O object expressions and instantiation.
 * Tests cover simple object creation, nested objects, property assignment,
 * extent assignment, object update, and collection features.
 */
class QvtoE2eObjectExpTest extends AbstractQvtoEngineTest {

	// ---- Simple object creation ----

	@Test
	void object_simpleCreation() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    main() {
				        object TargetElement {
				            name := 'simple';
				        };
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		assertEquals("simple", eGet(outExtent.getContents().get(0), "name"));
	}

	@Test
	void object_withNameAndValue() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    main() {
				        object TargetElement {
				            name := 'elem';
				            value := 42;
				        };
				    }
				}
				""", outExtent);
		assertSuccess(result);
		EObject elem = outExtent.getContents().get(0);
		assertEquals("elem", eGet(elem, "name"));
		assertEquals(42, eGet(elem, "value"));
	}

	// ---- Nested objects ----

	@Test
	void object_nestedContainerWithChildren() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    main() {
				        object TargetContainer {
				            name := 'root';
				            elements += object TargetElement { name := 'child1'; };
				            elements += object TargetElement { name := 'child2'; };
				        };
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertFalse(outExtent.getContents().isEmpty());
		EObject container = outExtent.getContents().stream()
				.filter(e -> e.eClass().getName().equals("TargetContainer"))
				.findFirst().orElse(null);
		assertNotNull(container);
		assertEquals("root", eGet(container, "name"));
	}

	@Test
	void object_deeplyNested() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    main() {
				        object SourceContainer {
				            name := 'outer';
				            elements += object SourceElement {
				                name := 'inner';
				                value := 99;
				            };
				        };
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertFalse(outExtent.getContents().isEmpty());
	}

	// ---- Object assigned to variable ----

	@Test
	void object_assignedToVariable_accessible() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    main() {
				        var elem := object TargetElement {
				            name := 'var-assigned';
				            value := 7;
				        };
				        log(elem.name + ':' + elem.value.toString());
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertLogged(result, "var-assigned:7");
	}

	// ---- Object as mapping result ----

	@Test
	void object_asMappingResult() throws Exception {
		EObject src = createSourceElement("src", 5);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				        r.ref := object TargetElement {
				            name := 'ref-' + self.name;
				        };
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertFalse(outExtent.getContents().isEmpty());
	}

	// ---- Multiple objects ----

	@Test
	void object_multipleInMain() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    main() {
				        object TargetElement { name := 'first'; };
				        object TargetElement { name := 'second'; };
				        object TargetElement { name := 'third'; };
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertEquals(3, outExtent.getContents().size());
	}

	// ---- Object with collection features (+=) ----

	@Test
	void object_addMultipleChildrenViaLoop() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    main() {
				        var container := object SourceContainer {
				            name := 'container';
				        };
				        Sequence{1, 2, 3}->forEach(i) {
				            container.elements += object SourceElement {
				                name := 'item-' + i.toString();
				                value := i;
				            };
				        };
				        log(container.elements->size().toString());
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertLogged(result, "3");
	}

	// ---- Object using self ----

	@Test
	void object_selfReferenceInBody() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    main() {
				        object TargetElement {
				            self.name := 'self-ref';
				            self.value := 123;
				        };
				    }
				}
				""", outExtent);
		assertSuccess(result);
		EObject elem = outExtent.getContents().get(0);
		assertEquals("self-ref", eGet(elem, "name"));
		assertEquals(123, eGet(elem, "value"));
	}

	// ---- Object with computed values ----

	@Test
	void object_computedPropertyValues() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    helper computeName() : String { return 'computed'; }
				    helper computeValue() : Integer { return 3 * 7; }
				    main() {
				        object TargetElement {
				            name := computeName();
				            value := computeValue();
				        };
				    }
				}
				""", outExtent);
		assertSuccess(result);
		EObject elem = outExtent.getContents().get(0);
		assertEquals("computed", eGet(elem, "name"));
		assertEquals(21, eGet(elem, "value"));
	}

	// ---- new expression ----

	@Test
	void new_createsObjectInExtent() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    main() {
				        new TargetElement();
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
	}

	// ---- Object in loop ----

	@Test
	void object_createdInLoop() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    main() {
				        var i : Integer := 0;
				        while (i < 5) {
				            object TargetElement {
				                name := 'item-' + i.toString();
				                value := i;
				            };
				            i := i + 1;
				        };
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertEquals(5, outExtent.getContents().size());
	}

	// ---- Object cross-reference ----

	@Test
	void object_crossReference_between_objects() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    main() {
				        var target := object TargetElement { name := 'target'; };
				        object TargetElement {
				            name := 'referrer';
				            ref := target;
				        };
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertEquals(2, outExtent.getContents().size());
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
