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
package org.eclipse.fennec.m2m.qvto.tests.transformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O Model operations (§8.3.5).
 *
 * <p>QVT-O defines operations on Model (extent) objects:
 * objects, objectsOfKind, objectsOfType, rootObjects,
 * addElement, removeElement, copy, createEmptyModel, asTransformation.
 *
 * <p><b>SPEC-FIRST:</b> Tests written against QVT-O v1.3 §8.3.5.
 * Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eModelOpsTest extends AbstractQvtoEngineTest {

	// ==== P8-04: Model Operations §8.3.5 ====

	// ---- §8.3.5.1: objects() : Set(Element) ----

	@Test
	void model_objects() throws Exception {
		// §8.3.5.1: returns the set of all objects in the model
		EObject src1 = createSourceElement("a", 1);
		EObject src2 = createSourceElement("b", 2);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1, src2);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        log('count:' + s.objects()->size().repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "count:2");
	}

	// ---- §8.3.5.2: objectsOfKind(type) : Set(T) ----

	@Test
	void model_objectsOfKind() throws Exception {
		// §8.3.5.2: returns objects that have the type given (includes subtypes)
		EObject src1 = createSourceElement("a", 1);
		EObject src2 = createSpecialSourceElement("b", 2, "special");
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1, src2);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var kindCount := s.objectsOfKind(SourceElement)->size();
				        log('kind:' + kindCount.repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		// Both SourceElement and SpecialSourceElement match (SpecialSourceElement extends SourceElement)
		assertLogged(result, "kind:2");
	}

	@Test
	void model_objectsOfKindExactSubtype() throws Exception {
		// §8.3.5.2: objectsOfKind with subtype returns only subtypes
		EObject src1 = createSourceElement("a", 1);
		EObject src2 = createSpecialSourceElement("b", 2, "special");
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1, src2);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var kindCount := s.objectsOfKind(SpecialSourceElement)->size();
				        log('kind:' + kindCount.repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		// Only SpecialSourceElement matches
		assertLogged(result, "kind:1");
	}

	// ---- §8.3.5.3: objectsOfType(type) : Set(T) ----

	@Test
	void model_objectsOfType() throws Exception {
		// §8.3.5.3: returns objects with EXACT type (not subtypes)
		EObject src1 = createSourceElement("a", 1);
		EObject src2 = createSpecialSourceElement("b", 2, "special");
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1, src2);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var typeCount := s.objectsOfType(SourceElement)->size();
				        log('type:' + typeCount.repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		// Only SourceElement matches, NOT SpecialSourceElement (exact type only)
		assertLogged(result, "type:1");
	}

	@Test
	void model_objectsOfTypeSubtype() throws Exception {
		// §8.3.5.3: objectsOfType with subtype returns only exact matches
		EObject src1 = createSourceElement("a", 1);
		EObject src2 = createSpecialSourceElement("b", 2, "special");
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1, src2);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var typeCount := s.objectsOfType(SpecialSourceElement)->size();
				        log('type:' + typeCount.repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "type:1");
	}

	// ---- §8.3.5.4: rootObjects() : Set(Element) ----

	@Test
	void model_rootObjects() throws Exception {
		// §8.3.5.4: returns objects not contained by other objects
		EObject src1 = createSourceElement("a", 1);
		EObject src2 = createSourceElement("b", 2);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1, src2);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        log('roots:' + s.rootObjects()->size().repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "roots:2");
	}

	// ---- §8.3.5.5: addElement(anObject) : Void ----

	@Test
	void model_addElement() throws Exception {
		// §8.3.5.5: adds element from one extent to another
		EObject src1 = createSourceElement("a", 1);
		EObject src2 = createSourceElement("b", 2);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1, src2);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(inout s : SRC, out t : TGT) {
				    main() {
				        var elem := s.objectsOfKind(SourceElement)->asSequence()->first();
				        t.addElement(elem);
				        log('out:' + t.objects()->size().repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "out:1");
		assertFalse(outExtent.getContents().isEmpty(), "Output extent should contain the added element");
	}

	@Test
	void model_addElementMultiple() throws Exception {
		// §8.3.5.5: add multiple elements from one extent to another
		EObject src1 = createSourceElement("a", 1);
		EObject src2 = createSourceElement("b", 2);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1, src2);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(inout s : SRC, out t : TGT) {
				    main() {
				        s.objectsOfKind(SourceElement)->forEach(e) {
				            t.addElement(e);
				        };
				        log('out:' + t.objects()->size().repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "out:2");
		assertEquals(2, outExtent.getContents().size());
	}

	// ---- §8.3.5.6: removeElement(Element) : Void ----

	@Test
	void model_removeElement() throws Exception {
		// §8.3.5.6: removes an object from the model
		EObject src1 = createSourceElement("a", 1);
		EObject src2 = createSourceElement("b", 2);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1, src2);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(inout s : SRC, out t : TGT) {
				    main() {
				        var first := s.objectsOfKind(SourceElement)->asSequence()->first();
				        s.removeElement(first);
				        log('remaining:' + s.objects()->size().repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "remaining:1");
	}

	// ---- §8.3.5.8: copy() : Model ----

	@Test
	void model_copy() throws Exception {
		// §8.3.5.8: deep copy of model and its extent
		EObject src1 = createSourceElement("a", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var copied := s.copy();
				        log('original:' + s.objects()->size().repr());
				        log('copy:' + copied.objects()->size().repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLoggedInOrder(result, "original:1", "copy:1");
	}

	// ---- §8.3.5.9: createEmptyModel() : Model ----

	@Test
	void model_createEmptyModel() throws Exception {
		// §8.3.5.9: creates a new empty model extent
		EObject src1 = createSourceElement("a", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var empty := s.createEmptyModel();
				        log('empty:' + empty.objects()->size().repr());
				        log('original:' + s.objects()->size().repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLoggedInOrder(result, "empty:0", "original:1");
	}

	@Test
	void model_createEmptyModelAddElement() throws Exception {
		// §8.3.5.9: createEmptyModel returns a usable model extent
		EObject src1 = createSourceElement("a", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var empty := s.createEmptyModel();
				        var elem := s.objectsOfKind(SourceElement)->asSequence()->first();
				        empty.addElement(elem);
				        log('size:' + empty.objects()->size().repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "size:1");
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

	private static void assertLoggedInOrder(QvtoExecutionResult result, String... expected) {
		var messages = result.diagnostics().stream()
				.map(d -> d.getMessage())
				.toList();
		int lastIdx = -1;
		for (String exp : expected) {
			boolean found = false;
			for (int i = lastIdx + 1; i < messages.size(); i++) {
				if (messages.get(i).contains(exp)) {
					lastIdx = i;
					found = true;
					break;
				}
			}
			assertTrue(found, "Expected log containing '" + exp
					+ "' after index " + lastIdx + " but diagnostics were: " + messages);
		}
	}
}
