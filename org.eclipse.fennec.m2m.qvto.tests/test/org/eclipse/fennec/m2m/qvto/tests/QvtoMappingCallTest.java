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

import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;
import org.junit.jupiter.api.Test;

/**
 * Tests for QVT-O mapping call expressions (map/xmap).
 */
class QvtoMappingCallTest extends AbstractQvtoEngineTest {

	@Test
	void mapCall_basic() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createElem() : r : SourceElement {
				        r.name := 'mapped';
				    }
				    main() {
				        map createElem();
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(1, outExtent.getContents().size());
	}

	@Test
	void xmapCall_basic() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createElem() : r : SourceElement {
				        r.name := 'strict';
				    }
				    main() {
				        xmap createElem();
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(1, outExtent.getContents().size());
	}

	@Test
	void mapCall_withArguments() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createNamed(n : String) : r : SourceElement {
				        r.name := n;
				    }
				    main() {
				        map createNamed('argValue');
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(1, outExtent.getContents().size());
		assertEquals("argValue",
				outExtent.getContents().get(0).eGet(
						outExtent.getContents().get(0).eClass().getEStructuralFeature("name")));
	}

	@Test
	void mapCall_returnsResult() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createElem() : r : SourceElement {
				        r.name := 'returned';
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
				.anyMatch(d -> d.getMessage().contains("returned"));
		assertTrue(hasLog, "Mapping result should be returned to caller");
	}

	@Test
	void mapCall_whenGuardFails() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping guardedMapping() : r : SourceElement
				        when { false; } {
				        r.name := 'guarded';
				    }
				    main() {
				        var elem := map guardedMapping();
				        log('done');
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertTrue(outExtent.getContents().isEmpty(), "No elements when guard fails");
	}

	@Test
	void mapCall_chained() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createA() : r : SourceElement {
				        r.name := 'A';
				    }
				    mapping createB() : r : SourceElement {
				        r.name := 'B';
				    }
				    main() {
				        map createA();
				        map createB();
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(2, outExtent.getContents().size(), "Two chained mappings should create 2 elements");
	}
}
