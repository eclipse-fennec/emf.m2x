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
package org.eclipse.fennec.m2x.qvto.tests.transformation;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * Model and element operations against QVT v1.3 §8.3.4, §8.3.5, §8.3.18.1 and §8.2.2.12 (#342).
 */
class QvtoModelElementSpecConformanceTest extends AbstractQvtoEngineTest {

	@SuppressWarnings("unchecked")
	private static EObject container(String name, EObject... children) {
		EClass containerClass = ecoreHelper.getEClass(sourcePackage, "SourceContainer");
		EObject container = EcoreUtil.create(containerClass);
		container.eSet(containerClass.getEStructuralFeature("name"), name);
		((List<EObject>) container.eGet(containerClass.getEStructuralFeature("elements"))).addAll(List.of(children));
		return container;
	}

	@Test
	void allInstances_rangesOverAllModels() throws Exception {
		// §8.3.18.1: "Instances are returned from all the model extents for input, inout and output models"
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        object TargetElement { name := 'x'; };
				        log('src:' + SourceElement.allInstances()->size().repr());
				        log('tgt:' + TargetElement.allInstances()->size().repr());
				    }
				}
				""", new BasicQvtoModelExtent(container("c", createSourceElement("a", 1), createSourceElement("b", 2))),
				emptyExtent());
		assertSuccess(result);
		assertLogged(result, "src:2");
		assertLogged(result, "tgt:1");
	}

	@Test
	void clone_andDeepclone_areAddedToTheSourceModel() throws Exception {
		// §8.3.4.10/11: "The returned object is added to the root of the model containing the source element"
		QvtoModelExtent model = new BasicQvtoModelExtent(container("c", createSourceElement("a", 1)));
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(inout m : SRC) {
				    main() {
				        var c := m.rootObjects()->first();
				        c.clone();
				        c.deepclone();
				        log('roots:' + m.rootObjects()->size().repr());
				    }
				}
				""", model);
		assertSuccess(result);
		assertLogged(result, "roots:3");
	}

	@Test
	void addElement_displacesFromTheContainer() throws Exception {
		// §8.3.5.5: "first displaced from any usage by setting its container to null"
		EObject child = createSourceElement("a", 1);
		QvtoModelExtent model = new BasicQvtoModelExtent(container("c", child));
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(inout m : SRC) {
				    main() {
				        var c : SourceContainer := m.rootObjects()->first().oclAsType(SourceContainer);
				        m.addElement(c.elements->first());
				        log('elements:' + c.elements->size().repr());
				        log('roots:' + m.rootObjects()->size().repr());
				    }
				}
				""", model);
		assertSuccess(result);
		assertLogged(result, "elements:0");
		assertLogged(result, "roots:2");
		assertNull(child.eContainer());
	}

	@Test
	void removeElement_removesANestedElement() throws Exception {
		// §8.3.5.6: "All references to or from the object are eliminated"
		EObject child = createSourceElement("a", 1);
		QvtoModelExtent model = new BasicQvtoModelExtent(container("c", child, createSourceElement("b", 2)));
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(inout m : SRC) {
				    main() {
				        m.removeElement(m.objectsOfType(SourceElement)->first());
				        log('objects:' + m.objects()->size().repr());
				    }
				}
				""", model);
		assertSuccess(result);
		assertLogged(result, "objects:2");
		assertNull(child.eContainer());
	}

	@Test
	void removeElement_clearsIncomingReferences_andCopyRewiresThem() throws Exception {
		QvtoModelExtent model = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    main() {
				        var t2 := object TargetElement { name := 't2'; };
				        var t1 := object TargetElement { name := 't1'; ref := t2; };
				        var copied := t.copy();
				        var c1 := copied.objectsOfType(TargetElement)->select(e | e.name = 't1')->first();
				        log('copy-ref-is-copy:' + (c1.ref <> t2).repr());
				        t.removeElement(t2);
				        log('ref-after-remove:' + t1.ref.oclIsUndefined().repr());
				    }
				}
				""", model);
		assertSuccess(result);
		// §8.3.5.8 copy: "a deep copy of a model" — references between roots point into the copy
		assertLogged(result, "copy-ref-is-copy:true");
		assertLogged(result, "ref-after-remove:true");
	}

	@Test
	void unlink_containment_makesTheChildARoot() throws Exception {
		// §8.2.2.12: "the no longer contained object becomes a root object of its model"
		EObject child = createSourceElement("a", 1);
		EObject root = container("c", child, createSourceElement("b", 2));
		QvtoModelExtent model = new BasicQvtoModelExtent(root);
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(inout m : SRC) {
				    main() {
				        var c : SourceContainer := m.rootObjects()->first().oclAsType(SourceContainer);
				        c.elements.unlink(c.elements->first());
				        log('roots:' + m.rootObjects()->size().repr());
				        log('objects:' + m.objects()->size().repr());
				    }
				}
				""", model);
		assertSuccess(result);
		assertLogged(result, "roots:2");
		assertLogged(result, "objects:3");
		assertSame(model.getContents().get(1), child);
	}

	@Test
	void objectsOfType_aDataType_isEmpty() throws Exception {
		// §8.3.5.2/3: the objects of the given type — no model object is a String
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(in s : SRC) {
				    main() {
				        log('strings:' + s.objectsOfType(String)->size().repr());
				        log('kinds:' + s.objectsOfKind(Integer)->size().repr());
				        log('children:' + s.rootObjects()->first().subobjectsOfType(String)->size().repr());
				    }
				}
				""", new BasicQvtoModelExtent(container("c", createSourceElement("a", 1))));
		assertSuccess(result);
		assertLogged(result, "strings:0");
		assertLogged(result, "kinds:0");
		assertLogged(result, "children:0");
	}

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
