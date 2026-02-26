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
 * E2E tests for QVT-O v1.3 §8.3.9.13 — {@code List(T)::deepclone()}.
 *
 * <p>Returns a new list where each EObject element is deep-cloned via
 * {@code EcoreUtil.copy()}. Primitives are immutable and copied as-is.
 *
 * <p><b>SPEC-FIRST:</b> Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eListDeepcloneTest extends AbstractQvtoEngineTest {

	// ---- §8.3.9.13: List(T)::deepclone() ----

	@Test
	void deepclone_listOfEObjects() throws Exception {
		// deepclone a list of EObjects — elements are independent copies
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
				        var original := c.elements;
				        var cloned := original.deepclone();
				        log('origSize:' + original->size().repr());
				        log('cloneSize:' + cloned->size().repr());
				        -- modify cloned element name
				        cloned->at(1).oclAsType(SourceElement).name := 'modified';
				        -- original should be unaffected
				        log('origName:' + original->at(1).oclAsType(SourceElement).name);
				    }
				}
				""", inoutExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "origSize:2");
		assertLogged(result, "cloneSize:2");
		assertLogged(result, "origName:a");
	}

	@Test
	void deepclone_emptyList() throws Exception {
		// deepclone on empty list → empty list
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var empty : List(Integer) := List{};
				        var cloned := empty.deepclone();
				        log('size:' + cloned->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:0");
	}

	@Test
	void deepclone_listOfPrimitives() throws Exception {
		// deepclone on list of primitives — values copied as-is (immutable)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var nums : List(Integer) := List{1, 2, 3};
				        var cloned := nums.deepclone();
				        log('size:' + cloned->size().repr());
				        log('sum:' + (cloned->at(1) + cloned->at(2) + cloned->at(3)).repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:3");
		assertLogged(result, "sum:6");
	}

	@Test
	void deepclone_isNewList() throws Exception {
		// deepclone returns a NEW list — modifying it does not affect original
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var orig : List(Integer) := List{10, 20};
				        var cloned := orig.deepclone();
				        cloned->add(30);
				        log('origSize:' + orig->size().repr());
				        log('cloneSize:' + cloned->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "origSize:2");
		assertLogged(result, "cloneSize:3");
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
