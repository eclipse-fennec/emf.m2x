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
package org.eclipse.fennec.m2x.qvto.tests.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
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
		EObject container = outExtent.getContents().stream()
				.filter(e -> e.eClass().getName().equals("TargetContainer"))
				.findFirst().orElse(null);
		assertNotNull(container, "Container should be in out extent");
		assertEquals("root", eGet(container, "name"));
		@SuppressWarnings("unchecked")
		List<EObject> children = (List<EObject>) container.eGet(
				container.eClass().getEStructuralFeature("elements"));
		assertEquals(2, children.size(), "Container should have 2 children");
		assertEquals("child1", eGet(children.get(0), "name"));
		assertEquals("child2", eGet(children.get(1), "name"));
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
		EObject container = outExtent.getContents().stream()
				.filter(e -> e.eClass().getName().equals("SourceContainer"))
				.findFirst().orElse(null);
		assertNotNull(container, "SourceContainer should be in out extent");
		assertEquals("outer", eGet(container, "name"));
		@SuppressWarnings("unchecked")
		List<EObject> children = (List<EObject>) container.eGet(
				container.eClass().getEStructuralFeature("elements"));
		assertEquals(1, children.size(), "Container should have 1 child");
		assertEquals("inner", eGet(children.get(0), "name"));
		assertEquals(99, eGet(children.get(0), "value"));
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
		// Mapping result + nested object expression → at least the mapping result in extent
		EObject mapped = outExtent.getContents().stream()
				.filter(e -> "src".equals(eGet(e, "name")))
				.findFirst().orElse(null);
		assertNotNull(mapped, "Mapped TargetElement 'src' should be in out extent");
		EObject ref = (EObject) eGet(mapped, "ref");
		assertNotNull(ref, "ref should be set by nested object expression");
		assertEquals("ref-src", eGet(ref, "name"), "Nested object should have name 'ref-src'");
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

	// ---- Object with named variable ----

	@Test
	void object_namedVariable_accessibleInBody() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    main() {
				        var elem := object TargetElement {
				            name := 'direct-access';
				            value := 123;
				        };
				        log(elem.name + ':' + elem.value.toString());
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertLogged(result, "direct-access:123");
		EObject elem = outExtent.getContents().get(0);
		assertEquals("direct-access", eGet(elem, "name"));
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

	// ---- P2-08: Object Update Semantics (§8.1.6 p70, §8.2.1.24 p116) ----

	// §8.2.1.24: referredObject is null → create new object
	@Test
	void object_creation_whenVariableNull() throws Exception {
		EObject src = createSourceElement("c", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        var x : TargetElement := null;
				        object x : TargetElement { name := 'created'; value := 42; };
				        log('name:' + x.name);
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) { e.map toTarget(); };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "name:created");
	}

	// §8.2.1.24: referredObject is non-null → update existing (no new creation)
	@Test
	void object_update_existingObject() throws Exception {
		EObject src = createSourceElement("u", 5);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := 'original';
				        r.value := 1;
				        -- update existing object (r is non-null)
				        object r : TargetElement { value := 99; };
				        log('name:' + r.name + ',value:' + r.value.toString());
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) { e.map toTarget(); };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		// name should still be 'original' (not reset), value updated to 99
		assertLogged(result, "name:original,value:99");
		// Only 1 object in extent (update, not new creation)
		assertEquals(1, outExtent.getContents().size(),
				"Object update should not create a new object");
	}

	// §8.2.1.24: object without variable → always create
	@Test
	void object_noVariable_alwaysCreates() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    main() {
				        object TargetElement { name := 'first'; };
				        object TargetElement { name := 'second'; };
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertEquals(2, outExtent.getContents().size(),
				"object without variable should always create new");
	}

	// §8.1.6: Object update does not add duplicate to extent
	@Test
	void object_update_noExtentDuplicate() throws Exception {
		EObject src = createSourceElement("d", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := 'orig';
				        -- update r three times
				        object r : TargetElement { value := 1; };
				        object r : TargetElement { value := 2; };
				        object r : TargetElement { value := 3; };
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) { e.map toTarget(); };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		// Only 1 object despite 3 updates
		assertEquals(1, outExtent.getContents().size(),
				"Updates should not add duplicates to extent");
		EObject target = outExtent.getContents().get(0);
		assertEquals("orig", eGet(target, "name"));
		assertEquals(3, eGet(target, "value"), "Last update wins");
	}

	// §8.1.6 p70: Object update in mapping end section
	@Test
	void object_update_inEndSection() throws Exception {
		EObject src = createSourceElement("e", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := 'pop';
				        r.value := 10;
				        end {
				            object r : TargetElement { name := 'end-updated'; };
				        }
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) { e.map toTarget(); };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		EObject target = outExtent.getContents().get(0);
		assertEquals("end-updated", eGet(target, "name"));
		assertEquals(10, eGet(target, "value"), "Value unchanged by end update");
	}

	// §8.2.1.24: Object with conditional body
	@Test
	void object_conditionalBody() throws Exception {
		EObject src = createSourceElement("cond", 5);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        object TargetElement {
				            if (self.value > 3) {
				                name := 'high';
				            } else {
				                name := 'low';
				            };
				        };
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) { e.map toTarget(); };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		// 2 objects: mapping result + object expression
		boolean found = outExtent.getContents().stream()
				.anyMatch(o -> "high".equals(eGet(o, "name")));
		assertTrue(found, "Conditional body should set name='high' for value>3");
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
