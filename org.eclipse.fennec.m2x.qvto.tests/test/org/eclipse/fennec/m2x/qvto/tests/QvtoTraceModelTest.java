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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.trace.Trace;
import org.eclipse.fennec.m2x.model.trace.TraceRecord;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoEvaluationOptions;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.junit.jupiter.api.Test;

/**
 * Tests for the EMF trace model export via QvtoExecutionResult.
 */
class QvtoTraceModelTest extends AbstractQvtoEngineTest {

	@Test
	void traceModel_populated() throws Exception {
		EObject src = createSourceElement("traced", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoEvaluationOptions options = QvtoEvaluationOptions.defaults().withTracing(true);
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", options, inExtent, outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		Trace trace = result.trace();
		assertNotNull(trace, "Trace should be non-null when tracing is enabled");
		assertEquals(1, trace.getTraceRecords().size(), "One mapping call = one trace record");
	}

	@Test
	void traceModel_hasMappingOperation() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoEvaluationOptions options = QvtoEvaluationOptions.defaults().withTracing(true);
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    mapping createElem() : r : TargetElement {
				        r.name := 'elem';
				    }
				    main() {
				        map createElem();
				    }
				}
				""", options, outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		Trace trace = result.trace();
		assertNotNull(trace);
		assertEquals(1, trace.getTraceRecords().size());
		TraceRecord record = trace.getTraceRecords().get(0);
		assertNotNull(record.getMappingOperation());
		assertEquals("createElem", record.getMappingOperation().getName());
	}

	@Test
	void traceModel_hasContextAndResult() throws Exception {
		EObject src = createSourceElement("ctx", 42);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoEvaluationOptions options = QvtoEvaluationOptions.defaults().withTracing(true);
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", options, inExtent, outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		TraceRecord record = result.trace().getTraceRecords().get(0);
		// Context
		assertNotNull(record.getContext());
		assertNotNull(record.getContext().getContext(), "Context should capture self");
		assertEquals("self", record.getContext().getContext().getName());
		assertNotNull(record.getContext().getContext().getValue());
		assertEquals(src, record.getContext().getContext().getValue().getModelElement());
		// Result
		assertNotNull(record.getResult());
		assertEquals(1, record.getResult().getResult().size());
		assertEquals("result", record.getResult().getResult().get(0).getName());
		EObject targetElem = outExtent.getContents().get(0);
		assertEquals(targetElem, record.getResult().getResult().get(0).getValue().getModelElement());
	}

	@Test
	void traceModel_nullWhenDisabled() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    main() {
				        log('no trace');
				    }
				}
				""", outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertNull(result.trace(), "Trace should be null when tracing is disabled (default)");
	}

	@Test
	void traceModel_multipleRecords() throws Exception {
		EObject src1 = createSourceElement("r1", 1);
		EObject src2 = createSourceElement("r2", 2);
		EObject src3 = createSourceElement("r3", 3);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1, src2, src3);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoEvaluationOptions options = QvtoEvaluationOptions.defaults().withTracing(true);
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", options, inExtent, outExtent);
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		Trace trace = result.trace();
		assertNotNull(trace);
		assertEquals(3, trace.getTraceRecords().size(),
				"Three mapping calls should produce three trace records");
	}
}
