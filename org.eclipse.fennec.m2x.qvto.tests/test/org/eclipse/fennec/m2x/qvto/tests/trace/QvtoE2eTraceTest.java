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
package org.eclipse.fennec.m2x.qvto.tests.trace;

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
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
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

	// ---- P4-01: Trace Record fields (§8.1.11.1) ----

	// §8.1.11.1: in-parameters are captured in trace record
	@Test
	void trace_hasInParameters() throws Exception {
		EObject src = createSourceElement("p", 42);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoEvaluationOptions options = QvtoEvaluationOptions.defaults().withTracing(true);
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget(in label : String) : r : TargetElement {
				        r.name := label;
				        r.value := self.value;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget('hello');
				        };
				    }
				}
				""", options, inExtent, outExtent);
		assertSuccess(result);
		Trace trace = result.trace();
		assertNotNull(trace);
		assertEquals(1, trace.getTraceRecords().size());
		TraceRecord record = trace.getTraceRecords().get(0);
		assertNotNull(record.getParameters());
		assertTrue(record.getParameters().getParameters().size() >= 1,
				"Trace should capture in-parameters; got: " + record.getParameters().getParameters());
	}

	// §8.1.11.1: when-guard failure → no trace record created
	@Test
	void trace_whenGuardFails_noTraceRecord() throws Exception {
		EObject src = createSourceElement("guarded", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoEvaluationOptions options = QvtoEvaluationOptions.defaults().withTracing(true);
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement
				        when { false } {
				        r.name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget();
				        };
				    }
				}
				""", options, inExtent, outExtent);
		assertSuccess(result);
		Trace trace = result.trace();
		assertNotNull(trace);
		assertEquals(0, trace.getTraceRecords().size(),
				"when { false } should prevent trace record creation");
	}

	// §8.1.11.1: No trace for object expressions — only mappings produce trace
	@Test
	void trace_objectExpression_noTraceRecord() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoEvaluationOptions options = QvtoEvaluationOptions.defaults().withTracing(true);
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    main() {
				        var elem := object TargetElement { name := 'obj'; };
				    }
				}
				""", options, outExtent);
		assertSuccess(result);
		Trace trace = result.trace();
		assertNotNull(trace);
		assertEquals(0, trace.getTraceRecords().size(),
				"object expressions should NOT produce trace records");
	}

	// §8.1.11.1: Trace records are in mapping execution order
	@Test
	void trace_executionOrder() throws Exception {
		EObject src1 = createSourceElement("first", 1);
		EObject src2 = createSourceElement("second", 2);
		EObject src3 = createSourceElement("third", 3);
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
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget();
				        };
				    }
				}
				""", options, inExtent, outExtent);
		assertSuccess(result);
		Trace trace = result.trace();
		assertEquals(3, trace.getTraceRecords().size());
		// Verify trace records are in execution order by checking context objects
		assertEquals(src1, trace.getTraceRecords().get(0).getContext().getContext().getValue().getModelElement());
		assertEquals(src2, trace.getTraceRecords().get(1).getContext().getContext().getValue().getModelElement());
		assertEquals(src3, trace.getTraceRecords().get(2).getContext().getContext().getValue().getModelElement());
	}

	// §8.1.11.1: inout parameters traced once as in-parameters
	@Test
	void trace_inoutParam_tracedAsIn() throws Exception {
		EObject src = createSourceElement("io", 5);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoEvaluationOptions options = QvtoEvaluationOptions.defaults().withTracing(true);
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget(inout count : Integer) : r : TargetElement {
				        count := count + 1;
				        r.name := self.name;
				        r.value := count;
				    }
				    main() {
				        var c : Integer := 0;
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget(c);
				        };
				    }
				}
				""", options, inExtent, outExtent);
		assertSuccess(result);
		TraceRecord record = result.trace().getTraceRecords().get(0);
		assertNotNull(record.getParameters());
		// inout parameter should appear in parameters (traced as in)
		assertTrue(record.getParameters().getParameters().size() >= 1,
				"inout parameter should be traced in parameters list");
	}

	// §8.1.11.1: Mapping without context (no self) — context handling
	@Test
	void trace_mappingWithoutContext() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoEvaluationOptions options = QvtoEvaluationOptions.defaults().withTracing(true);
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    mapping createElem() : r : TargetElement {
				        r.name := 'no-context';
				    }
				    main() {
				        map createElem();
				    }
				}
				""", options, outExtent);
		assertSuccess(result);
		Trace trace = result.trace();
		assertEquals(1, trace.getTraceRecords().size());
		TraceRecord record = trace.getTraceRecords().get(0);
		// Mapping without context type: context may be null or have null value
		assertNotNull(record.getMappingOperation());
		assertEquals("createElem", record.getMappingOperation().getName());
	}

	// ---- Helpers ----

	private static void assertSuccess(QvtoExecutionResult result) {
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}
}
