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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O §8.1.3/§8.1.6 @extentName extent routing
 * in object expressions and new expressions.
 */
class QvtoE2eExtentRoutingTest extends AbstractQvtoEngineTest {

	@Test
	void objectExp_atExtent_routesToNamedExtent() throws Exception {
		QvtoModelExtent xExtent = emptyExtent();
		QvtoModelExtent yExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation test(out x : ECORE, out y : ECORE) {
				    main() {
				        object EPackage@x {
				            name := 'routed';
				        };
				    }
				}
				""", xExtent, yExtent);
		assertSuccess(result);
		assertEquals(1, xExtent.getContents().size());
		assertEquals("routed", eGet(xExtent.getContents().get(0), "name"));
		assertTrue(yExtent.getContents().isEmpty());
	}

	@Test
	void objectExp_atExtent_secondExtent() throws Exception {
		QvtoModelExtent xExtent = emptyExtent();
		QvtoModelExtent yExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation test(out x : ECORE, out y : ECORE) {
				    main() {
				        object EPackage@y {
				            name := 'inY';
				        };
				    }
				}
				""", xExtent, yExtent);
		assertSuccess(result);
		assertTrue(xExtent.getContents().isEmpty());
		assertEquals(1, yExtent.getContents().size());
		assertEquals("inY", eGet(yExtent.getContents().get(0), "name"));
	}

	@Test
	void objectExp_atExtent_bothExtents() throws Exception {
		QvtoModelExtent xExtent = emptyExtent();
		QvtoModelExtent yExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation test(out x : ECORE, out y : ECORE) {
				    main() {
				        object EPackage@x { name := 'inX'; };
				        object EPackage@y { name := 'inY'; };
				    }
				}
				""", xExtent, yExtent);
		assertSuccess(result);
		assertEquals(1, xExtent.getContents().size());
		assertEquals("inX", eGet(xExtent.getContents().get(0), "name"));
		assertEquals(1, yExtent.getContents().size());
		assertEquals("inY", eGet(yExtent.getContents().get(0), "name"));
	}

	@Test
	void newExp_atExtent_routesToNamedExtent() throws Exception {
		QvtoModelExtent xExtent = emptyExtent();
		QvtoModelExtent yExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation test(out x : ECORE, out y : ECORE) {
				    main() {
				        var p := new EPackage@x();
				        p.name := 'newRouted';
				    }
				}
				""", xExtent, yExtent);
		assertSuccess(result);
		assertEquals(1, xExtent.getContents().size());
		assertEquals("newRouted", eGet(xExtent.getContents().get(0), "name"));
		assertTrue(yExtent.getContents().isEmpty());
	}

	@Test
	void objectExp_withoutAtExtent_usesDefault() throws Exception {
		QvtoModelExtent xExtent = emptyExtent();
		QvtoModelExtent yExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation test(out x : ECORE, out y : ECORE) {
				    main() {
				        object EPackage {
				            name := 'defaultRouted';
				        };
				    }
				}
				""", xExtent, yExtent);
		assertSuccess(result);
		// Without @extent, default routing applies (first out extent)
		int total = xExtent.getContents().size() + yExtent.getContents().size();
		assertEquals(1, total);
	}

	// ---- Helpers ----

	private static void assertSuccess(QvtoExecutionResult result) {
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	private static Object eGet(EObject obj, String featureName) {
		return obj.eGet(obj.eClass().getEStructuralFeature(featureName));
	}
}
