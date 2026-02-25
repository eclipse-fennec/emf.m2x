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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.junit.jupiter.api.Test;

/**
 * Tests for QVT-O mapping operation execution.
 */
class QvtoMappingExecutionTest extends AbstractQvtoEngineTest {

	@Test
	void mapping_basicExecution() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createElem() : r : SourceElement {
				        r.name := 'basic';
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
	void mapping_whenGuard_passing() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createWhenTrue() : r : SourceElement
				        when { true; } {
				        r.name := 'created';
				    }
				    main() {
				        map createWhenTrue();
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertFalse(outExtent.getContents().isEmpty(), "Output extent should have elements");
	}

	@Test
	void mapping_whenGuard_failing() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createWhenFalse() : r : SourceElement
				        when { false; } {
				        r.name := 'should-not-exist';
				    }
				    main() {
				        map createWhenFalse();
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertTrue(outExtent.getContents().isEmpty(), "Output extent should be empty when guard fails");
	}

	@Test
	void mapping_initAndEnd() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createWithSections() : r : SourceElement {
				        init {
				            log('init');
				        }
				        r.name := 'populated';
				        end {
				            log('end');
				        }
				    }
				    main() {
				        map createWithSections();
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		long logCount = result.diagnostics().stream()
				.filter(d -> d.getSeverity() == Diagnostic.INFO)
				.count();
		assertEquals(2, logCount, "Should have init and end log entries");
	}

	@Test
	void mapping_resultParameter() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createResult() : r : SourceElement {
				        r.name := 'result-obj';
				    }
				    main() {
				        var elem := map createResult();
				        log(elem.name);
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		boolean hasLog = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("result-obj"));
		assertTrue(hasLog, "Result object name should be accessible");
	}

	@Test
	void mapping_wherePostcondition_failing() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createWithWhere() : r : SourceElement
				        where { false; } {
				        r.name := 'test';
				    }
				    main() {
				        map createWithWhere();
				    }
				}
				""", outExtent);
		assertNotNull(result);
		boolean hasWarning = result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() == Diagnostic.WARNING
						&& d.getMessage().contains("where-postcondition"));
		assertTrue(hasWarning, "Should have where-postcondition warning");
	}

	@Test
	void mapping_callingHelper() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    helper computeName() : String { return 'computed'; }
				    mapping createFromHelper() : r : SourceElement {
				        r.name := computeName();
				    }
				    main() {
				        var elem := map createFromHelper();
				        log(elem.name);
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		boolean hasLog = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("computed"));
		assertTrue(hasLog, "Helper result should be used in mapping body");
	}

	@Test
	void mapping_callingOtherMapping() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping innerMapping() : r : SourceElement {
				        r.name := 'inner';
				    }
				    mapping outerMapping() : r : SourceElement {
				        r.name := 'outer';
				        var inner := map innerMapping();
				        log(inner.name);
				    }
				    main() {
				        map outerMapping();
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(2, outExtent.getContents().size(), "Should have 2 elements from nested mappings");
	}

	@Test
	void mapping_disjunct_firstPasses() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping first() : r : SourceElement
				        when { true; } {
				        r.name := 'first';
				    }
				    mapping second() : r : SourceElement
				        when { true; } {
				        r.name := 'second';
				    }
				    mapping disjunctMapping() : r : SourceElement
				        disjuncts first, second {}
				    main() {
				        var elem := map disjunctMapping();
				        log(elem.name);
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		boolean hasLog = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("first"));
		assertTrue(hasLog, "First disjunct should win when both pass");
	}

	@Test
	void mapping_disjunct_secondPasses() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping first() : r : SourceElement
				        when { false; } {
				        r.name := 'first';
				    }
				    mapping second() : r : SourceElement
				        when { true; } {
				        r.name := 'second';
				    }
				    mapping disjunctMapping() : r : SourceElement
				        disjuncts first, second {}
				    main() {
				        var elem := map disjunctMapping();
				        log(elem.name);
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		boolean hasLog = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("second"));
		assertTrue(hasLog, "Second disjunct should win when first fails");
	}
}
