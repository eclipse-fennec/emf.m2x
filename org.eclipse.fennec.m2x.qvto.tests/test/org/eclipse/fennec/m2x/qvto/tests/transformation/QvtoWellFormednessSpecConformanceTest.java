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
package org.eclipse.fennec.m2x.qvto.tests.transformation;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * Well-formedness rules of QVT v1.3 §8.1.9, §8.1.11.2, §8.2.1.1, §8.2.1.10-12, §8.2.1.15 (#347).
 */
class QvtoWellFormednessSpecConformanceTest extends AbstractQvtoEngineTest {

	private static final String HEADER = """
			modeltype SRC uses 'http://test/source/1.0';
			modeltype TGT uses 'http://test/target/1.0';
			""";

	private static void assertRejected(String transformation, String message) {
		QvtoParseException failure = assertThrows(QvtoParseException.class, () -> parse(HEADER + transformation));
		assertTrue(failure.getErrors().stream().anyMatch(d -> d.getMessage().contains(message)),
				() -> "Expected '" + message + "' but got " + failure.getErrors());
	}

	private static QvtoExecutionResult run(String transformation) throws Exception {
		return executeWithExtents(HEADER + transformation,
				new BasicQvtoModelExtent(createSourceElement("a", 1)), emptyExtent());
	}

	@Test
	void twoEntryOperations_areAnError() {
		// §8.2.1.11: "no more than an entry operation"
		assertRejected("""
				transformation test(in s : SRC, out t : TGT) {
				    main() { log('one'); }
				    main() { log('two'); }
				}
				""", "more than one entry operation");
	}

	@Test
	void anEntryWithParameters_isAnError() {
		// §8.2.1.11: the entry operation "has no parameters"
		assertRejected("""
				transformation test(in s : SRC, out t : TGT) {
				    main(x : String) { log(x); }
				}
				""", "the entry operation has no parameters");
	}

	@Test
	void anAbstractTransformationWithAnEntry_isAnError() {
		// §8.2.1.1: "No entry operation should be defined"
		assertRejected("""
				abstract transformation test(in s : SRC, out t : TGT) {
				    main() { }
				}
				""", "an abstract transformation defines no entry operation");
	}

	@Test
	void aDisjunctionWithABody_isAnError() {
		// §8.2.1.15: disjunct->notEmpty() implies body = null
		assertRejected("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::a() : TargetElement { }
				    mapping SourceElement::d() : TargetElement disjuncts SourceElement::a { name := 'ignored'; }
				    main() { }
				}
				""", "has disjuncts and a body");
	}

	@Test
	void twoContextualOperationsOfOneSignature_areAnError() {
		// §8.2.1.10
		assertRejected("""
				transformation test(in s : SRC, out t : TGT) {
				    query SourceElement::q(x : String) : String { return x; }
				    query SourceElement::q(y : String) : String { return y; }
				    main() { }
				}
				""", "operation 'q' is defined twice");
	}

	@Test
	void wellFormedOperations_pass() throws Exception {
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    query SourceElement::q(x : String) : String { return x; }
				    query SourceElement::q(x : Integer) : String { return 'int'; }
				    mapping SourceElement::a() : TargetElement { name := 'a'; }
				    mapping SourceElement::d() : TargetElement disjuncts SourceElement::a;
				    main() {
				        var e := s.rootObjects()->first().oclAsType(SourceElement);
				        log('q=' + e.q('s') + ' d=' + e.map d().name);
				    }
				}
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> result.diagnostics().toString());
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains("q=s d=a")),
				() -> result.diagnostics().toString());
	}

	@Test
	void aQueryWithASideEffect_isReported() throws Exception {
		// §8.2.1.12: "A query has no side-effects"
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    property counter : Integer = 0;
				    query count() : Integer { this.counter := 5; return counter; }
				    query pure() : Integer { var local := 1; return local; }
				    main() { log('c=' + count().repr() + ' p=' + pure().repr()); }
				}
				""");
		assertWarning(result, "query 'count' assigns 'counter': a query has no side effects");
		assertTrue(result.diagnostics().stream().noneMatch(d -> d.getMessage().contains("query 'pure'")),
				() -> result.diagnostics().toString());
	}

	@Test
	void changingAnInoutParameterInInit_isReported() throws Exception {
		// §8.1.11.2: "inout parameters may not be changed during the initialization section"
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping m(inout x : TargetElement) : TargetElement {
				        init { x.name := 'changed'; }
				        name := 'r';
				    }
				    main() {
				        var x := object TargetElement { name := 'orig'; };
				        map m(x);
				    }
				}
				""");
		assertWarning(result, "mapping 'm' changes the inout parameter's 'name' in its init section");
	}

	private static void assertWarning(QvtoExecutionResult result, String expected) {
		assertNotNull(result);
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() == Diagnostic.WARNING && d.getMessage().contains(expected)),
				() -> "Expected the warning '" + expected + "' but diagnostics were: " + result.diagnostics());
	}
}
