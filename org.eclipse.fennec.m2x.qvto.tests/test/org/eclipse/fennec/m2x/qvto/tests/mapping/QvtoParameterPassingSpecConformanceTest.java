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
package org.eclipse.fennec.m2x.qvto.tests.mapping;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * Parameter passing and result parameters against QVT v1.3 §8.1.18, §8.2.1.12, §8.2.1.15 (#336).
 */
class QvtoParameterPassingSpecConformanceTest extends AbstractQvtoEngineTest {

	private static final String HEADER = """
			modeltype SRC uses 'http://test/source/1.0';
			modeltype TGT uses 'http://test/target/1.0';
			""";

	private static QvtoExecutionResult run(String body) throws Exception {
		return executeWithExtents(HEADER + body,
				new BasicQvtoModelExtent(createSourceElement("a", 1)), emptyExtent());
	}

	@Test
	void outAndInoutParameters_areVisibleToTheCaller() throws Exception {
		// §8.2.1.12: "inout and out DataType parameters are passed by reference-to-variable …
		// These updates are visible to the caller"
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    helper setOut(out v : String) { v := 'new'; }
				    helper setInout(inout w : String) { w := w + '!'; }
				    main() {
				        var o := 'orig';
				        var io := 'io';
				        setOut(o);
				        setInout(io);
				        log('out=' + o);
				        log('inout=' + io);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "out=new");
		assertLogged(result, "inout=io!");
	}

	@Test
	void anOutParameter_startsNull() throws Exception {
		// §8.2.1.15: "The initial value of each out and result parameter is null"
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    helper probe(out v : String) { log('start=' + v.oclIsUndefined().repr()); }
				    main() {
				        var o := 'orig';
				        probe(o);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "start=true");
	}

	@Test
	void anInParameter_isNotWrittenBack() throws Exception {
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    helper change(v : String) { v := 'changed'; }
				    main() {
				        var o := 'orig';
				        change(o);
				        log('in=' + o);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "in=orig");
	}

	@Test
	void aMappingsOutParameter_isInstantiated_andHandedBack() throws Exception {
		// §8.2.1.15: "instantiation of all the out parameters that are object instances and that
		// still have a null value" … "The final value of each out … parameter is returned"
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::m(out x : TargetElement) : TargetElement {
				        x.name := 'side';
				        name := 'main';
				    }
				    main() {
				        var o2 : TargetElement := null;
				        var r := s.objectsOfType(SourceElement)->first().map m(o2);
				        log('result=' + r.name);
				        log('out=' + o2.name);
				        log('objects=' + t.objects()->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "result=main");
		assertLogged(result, "out=side");
		assertLogged(result, "objects=2");
	}

	@Test
	void anInhibitedMapping_handsItsOutParameterBackAgain() throws Exception {
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::m(out x : TargetElement) : TargetElement { x.name := 'side'; }
				    main() {
				        var e := s.objectsOfType(SourceElement)->first();
				        var first : TargetElement := null;
				        var second : TargetElement := null;
				        e.map m(first);
				        e.map m(second);
				        log('same=' + (first = second).repr());
				        log('objects=' + t.objects()->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "same=true");
		assertLogged(result, "objects=2");
	}

	@Test
	void multipleResults_bindResultAsATuple() throws Exception {
		// §8.1.18: "result … is a tuple when there are multiple result parameters"
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::two() : a : TargetElement, b : TargetElement {
				        result.a.name := 'A';
				        result.b.name := 'B';
				    }
				    main() {
				        var res := s.objectsOfType(SourceElement)->first().map two();
				        log('res=' + res.a.name + res.b.name);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "res=AB");
	}

	@Test
	void aValuelessReturn_answersTheResultParameters() throws Exception {
		// §8.2.2.14: a return without a value ends the operation with its result parameters
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    query two() : a : String, b : Integer { a := 'x'; b := 2; return; }
				    query one() : r : String { r := 'single'; return; }
				    main() {
				        var t2 := two();
				        log('two=' + t2.a + t2.b.repr());
				        log('one=' + one());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "two=x2");
		assertLogged(result, "one=single");
	}

	@Test
	void anOutArgumentOnAProperty_isAssignedToIt() throws Exception {
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    helper setOut(out v : String) { v := 'via-out'; }
				    main() {
				        var target := object TargetElement { name := 'before'; };
				        setOut(target.name);
				        log('prop=' + target.name);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "prop=via-out");
	}

	private static void assertSuccess(QvtoExecutionResult result) {
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains(expected)),
				() -> "Expected log containing '" + expected + "' but diagnostics were: " + result.diagnostics());
	}
}
