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
package org.eclipse.fennec.m2m.qvto.tests.trace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.model.trace.Trace;
import org.eclipse.fennec.m2m.model.trace.TraceRecord;
import org.eclipse.fennec.m2m.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.api.QvtoEvaluationOptions;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O trace model.
 * Tests cover trace creation, trace content verification, nested mappings,
 * multiple mappings, and enable/disable tracing.
 */
class QvtoE2eTraceTest extends AbstractQvtoEngineTest {

	// ---- Trace after simple mapping ----

	@Test
	void trace_simpleMapping_createsOneRecord() throws Exception {
		EObject src = createSourceElement("t1", 1);
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
		assertSuccess(result);
		Trace trace = result.trace();
		assertNotNull(trace);
		assertEquals(1, trace.getTraceRecords().size());
	}

	// ---- Trace after nested mappings ----

	@Test
	void trace_nestedMappings_createsMultipleRecords() throws Exception {
		EObject src = createSourceElement("nested", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoEvaluationOptions options = QvtoEvaluationOptions.defaults().withTracing(true);
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				        r.ref := map createRef();
				    }
				    mapping createRef() : r : TargetElement {
				        r.name := 'ref';
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", options, inExtent, outExtent);
		assertSuccess(result);
		Trace trace = result.trace();
		assertNotNull(trace);
		assertEquals(2, trace.getTraceRecords().size(), "Nested mapping should also produce trace record");
	}

	// ---- Trace disabled ----

	@Test
	void trace_disabled_returnsNull() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    main() {
				        log('no-trace');
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertNull(result.trace(), "Trace should be null when tracing is disabled");
	}

	// ---- Trace content: MappingOperation name ----

	@Test
	void trace_hasMappingOperationName() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoEvaluationOptions options = QvtoEvaluationOptions.defaults().withTracing(true);
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    mapping createElem() : r : TargetElement {
				        r.name := 'traced';
				    }
				    main() {
				        map createElem();
				    }
				}
				""", options, outExtent);
		assertSuccess(result);
		TraceRecord record = result.trace().getTraceRecords().get(0);
		assertNotNull(record.getMappingOperation());
		assertEquals("createElem", record.getMappingOperation().getName());
	}

	// ---- Trace content: Context ----

	@Test
	void trace_hasContext() throws Exception {
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
		assertSuccess(result);
		TraceRecord record = result.trace().getTraceRecords().get(0);
		assertNotNull(record.getContext());
		assertNotNull(record.getContext().getContext(), "Context should capture self");
		assertEquals("self", record.getContext().getContext().getName());
		assertEquals(src, record.getContext().getContext().getValue().getModelElement());
	}

	// ---- Trace content: Result ----

	@Test
	void trace_hasResult() throws Exception {
		EObject src = createSourceElement("res", 1);
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
		assertSuccess(result);
		TraceRecord record = result.trace().getTraceRecords().get(0);
		assertNotNull(record.getResult());
		assertEquals(1, record.getResult().getResult().size());
		EObject targetElem = outExtent.getContents().get(0);
		assertEquals(targetElem, record.getResult().getResult().get(0).getValue().getModelElement());
	}

	// ---- Multiple mappings same element ----

	@Test
	void trace_multipleMappingsSameElement() throws Exception {
		EObject src = createSourceElement("multi", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoEvaluationOptions options = QvtoEvaluationOptions.defaults().withTracing(true);
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTargetA() : r : TargetElement {
				        r.name := 'A-' + self.name;
				    }
				    mapping SourceElement::toTargetB() : r : TargetElement {
				        r.name := 'B-' + self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTargetA();
				            e.map toTargetB();
				        };
				    }
				}
				""", options, inExtent, outExtent);
		assertSuccess(result);
		Trace trace = result.trace();
		assertNotNull(trace);
		assertEquals(2, trace.getTraceRecords().size(),
				"Two different mappings on same element → two trace records");
	}

	// ---- Trace with multiple source elements ----

	@Test
	void trace_multipleSourceElements() throws Exception {
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(
				createSourceElement("s1", 1),
				createSourceElement("s2", 2),
				createSourceElement("s3", 3));
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
		assertSuccess(result);
		assertEquals(3, result.trace().getTraceRecords().size());
	}

	// ---- Helpers ----

	private static void assertSuccess(QvtoExecutionResult result) {
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}
}
