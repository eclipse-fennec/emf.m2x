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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;
import org.junit.jupiter.api.Test;

/**
 * Tests for QVT-O object expression and instantiation expression.
 */
class QvtoObjectExpTest extends AbstractQvtoEngineTest {

	@Test
	void objectExp_createsEObject() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    main() {
				        object SourceElement {};
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(1, outExtent.getContents().size(), "Extent should have 1 element");
	}

	@Test
	void objectExp_propertyAssignment() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    main() {
				        object SourceElement {
				            name := 'test';
				        };
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertFalse(outExtent.getContents().isEmpty());
		EObject elem = outExtent.getContents().get(0);
		assertEquals("test", elem.eGet(elem.eClass().getEStructuralFeature("name")));
	}

	@Test
	void objectExp_namedVariable() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    main() {
				        var elem := object SourceElement {
				            name := 'named';
				        };
				        log(elem.name);
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		boolean hasLog = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("named"));
		assertTrue(hasLog, "Named variable should be accessible after object expression");
	}

	@Test
	void objectExp_multipleAssignments() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    main() {
				        object SourceElement {
				            name := 'multi';
				            value := 42;
				        };
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		EObject elem = outExtent.getContents().get(0);
		assertEquals("multi", elem.eGet(elem.eClass().getEStructuralFeature("name")));
		assertEquals(42, elem.eGet(elem.eClass().getEStructuralFeature("value")));
	}

	@Test
	void instantiationExp_new() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    main() {
				        new SourceElement();
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(1, outExtent.getContents().size(), "new should create 1 element");
	}

	@Test
	void objectExp_addsToOutputExtent() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		assertTrue(outExtent.getContents().isEmpty(), "Extent starts empty");
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    main() {
				        object SourceElement { name := 'a'; };
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(1, outExtent.getContents().size(), "Extent should grow by 1");
	}

	@Test
	void objectExp_multiple() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    main() {
				        object SourceElement { name := 'first'; };
				        object SourceElement { name := 'second'; };
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(2, outExtent.getContents().size(), "Two objects should be created");
	}

	@Test
	void objectExp_nested() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    main() {
				        object SourceContainer {
				            name := 'container';
				            elements += object SourceElement { name := 'child'; };
				        };
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertTrue(outExtent.getContents().size() >= 1, "At least container should be in extent");
	}
}
