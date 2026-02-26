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
package org.eclipse.fennec.m2x.qvto.tests.collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
 * E2E tests for QVT-O v1.3 §8.2.2.12 — {@code UnlinkExp}.
 *
 * <p>{@code feature.unlink(item)} removes an item from a multivalued property.
 * The residence of the removed object is unaffected (spec: if containment,
 * the object becomes a root object of its model).
 *
 * <p>Eclipse QVT-O has only a stub implementation (evaluator returns null).
 * Fennec implements this fully per spec.
 *
 * <p><b>SPEC-FIRST:</b> Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eUnlinkTest extends AbstractQvtoEngineTest {

	// ---- §8.2.2.12: feature.unlink(item) ----

	@Test
	void unlink_removesFromMultiValued() throws Exception {
		// Unlink one child from container.elements — size goes from 3 to 2
		EObject container = createSourceContainer("parent",
				createSourceElement("a", 1),
				createSourceElement("b", 2),
				createSourceElement("c", 3));
		QvtoModelExtent inoutExtent = new BasicQvtoModelExtent(container);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(inout s : SRC, out t : TGT) {
				    main() {
				        var c := s.objectsOfKind(SourceContainer)->asSequence()->first();
				        var child := c.elements->at(2);
				        log('before:' + c.elements->size().repr());
				        c.elements.unlink(child);
				        log('after:' + c.elements->size().repr());
				    }
				}
				""", inoutExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "before:3");
		assertLogged(result, "after:2");
	}

	@Test
	void unlink_singleElement() throws Exception {
		// Unlink the only child → empty list
		EObject container = createSourceContainer("parent",
				createSourceElement("only", 1));
		QvtoModelExtent inoutExtent = new BasicQvtoModelExtent(container);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(inout s : SRC, out t : TGT) {
				    main() {
				        var c := s.objectsOfKind(SourceContainer)->asSequence()->first();
				        var child := c.elements->first();
				        c.elements.unlink(child);
				        log('after:' + c.elements->size().repr());
				    }
				}
				""", inoutExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "after:0");
	}

	@Test
	void unlink_nonExistent() throws Exception {
		// Unlink an element not in the list → no change
		EObject container = createSourceContainer("parent",
				createSourceElement("a", 1),
				createSourceElement("b", 2));
		// Create a detached element not in the container
		EObject detached = createSourceElement("detached", 99);
		QvtoModelExtent inoutExtent = new BasicQvtoModelExtent(container, detached);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(inout s : SRC, out t : TGT) {
				    main() {
				        var c := s.objectsOfKind(SourceContainer)->asSequence()->first();
				        var det := s.objectsOfType(SourceElement)->select(e | e.name = 'detached')->first();
				        log('before:' + c.elements->size().repr());
				        c.elements.unlink(det);
				        log('after:' + c.elements->size().repr());
				    }
				}
				""", inoutExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "before:2");
		assertLogged(result, "after:2");
	}

	@Test
	void unlink_containment_childBecomesDetached() throws Exception {
		// §8.2.2.12: residence unaffected — after unlink from containment,
		// the child's container becomes null
		EObject container = createSourceContainer("parent",
				createSourceElement("child", 1));
		QvtoModelExtent inoutExtent = new BasicQvtoModelExtent(container);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(inout s : SRC, out t : TGT) {
				    main() {
				        var c := s.objectsOfKind(SourceContainer)->asSequence()->first();
				        var child := c.elements->first();
				        log('name:' + child.name);
				        c.elements.unlink(child);
				        log('detached:' + child.container().oclIsUndefined().repr());
				    }
				}
				""", inoutExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "name:child");
		assertLogged(result, "detached:true");
	}

	@Test
	void unlink_returnValue() throws Exception {
		// unlink returns the modified collection (self)
		EObject container = createSourceContainer("parent",
				createSourceElement("a", 1),
				createSourceElement("b", 2));
		QvtoModelExtent inoutExtent = new BasicQvtoModelExtent(container);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(inout s : SRC, out t : TGT) {
				    main() {
				        var c := s.objectsOfKind(SourceContainer)->asSequence()->first();
				        var child := c.elements->first();
				        var remaining := c.elements.unlink(child);
				        log('remaining:' + remaining->size().repr());
				    }
				}
				""", inoutExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "remaining:1");
	}

	// ---- Helpers ----

	@SuppressWarnings("unchecked")
	private static EObject createSourceContainer(String name, EObject... children) {
		EClass containerClass = ecoreHelper.getEClass(sourcePackage, "SourceContainer");
		EObject container = EcoreUtil.create(containerClass);
		container.eSet(containerClass.getEStructuralFeature("name"), name);
		List<EObject> elements = (List<EObject>) container
				.eGet(containerClass.getEStructuralFeature("elements"));
		for (EObject child : children) {
			elements.add(child);
		}
		return container;
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
