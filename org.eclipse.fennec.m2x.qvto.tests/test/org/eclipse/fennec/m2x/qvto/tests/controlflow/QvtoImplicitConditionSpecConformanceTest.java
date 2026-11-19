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
package org.eclipse.fennec.m2x.qvto.tests.controlflow;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * An element or a collection where a Boolean is expected, QVT v1.3 §8.4.5(2) (#338).
 */
class QvtoImplicitConditionSpecConformanceTest extends AbstractQvtoEngineTest {

	private static QvtoExecutionResult run(String body) throws Exception {
		return executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				%s
				}
				""".formatted(body), new BasicQvtoModelExtent(createSourceElement("a", 1)), emptyExtent());
	}

	@Test
	void anElementOrACollection_isComparedWithNullOrItsSize() throws Exception {
		// §8.4.5(2): "(i) if an Element instance is passed there is an implicit comparison with the
		// null value. (ii) if a collection value is passed there is an implicit comparison with the
		// size of the collection"
		QvtoExecutionResult result = run("""
				    main() {
				        var e : SourceElement := s.rootObjects()->first().oclAsType(SourceElement);
				        var none : SourceElement := null;
				        log('e=' + (if (e) then 'T' else 'F' endif));
				        log('null=' + (if (none) then 'T' else 'F' endif));
				        log('empty=' + (if (Sequence{}) then 'T' else 'F' endif));
				        log('one=' + (if (Sequence{1}) then 'T' else 'F' endif));
				        var n := 0;
				        while (Sequence{1}->select(i | n < 2)) { n := n + 1; };
				        log('n=' + n.repr());
				    }
				""");
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> result.diagnostics().toString());
		assertLogged(result, "e=T");
		assertLogged(result, "null=F");
		assertLogged(result, "empty=F");
		assertLogged(result, "one=T");
		assertLogged(result, "n=2");
	}

	@Test
	void anElementAsAGuard_selectsTheMapping() throws Exception {
		QvtoExecutionResult result = run("""
				    mapping SourceElement::m() : TargetElement when { self } { name := 'mapped'; }
				    main() { log('m=' + s.rootObjects()->first().oclAsType(SourceElement).map m().name); }
				""");
		assertTrue(result.isSuccess(), () -> result.diagnostics().toString());
		assertLogged(result, "m=mapped");
	}

	@Test
	void aString_staysAnUndefinedCondition() throws Exception {
		// Neither an element nor a collection: the error of #308/#310 stays
		QvtoExecutionResult result = run("""
				    mapping SourceElement::m() : TargetElement when { self.name } { name := 'mapped'; }
				    main() { s.rootObjects()->first().oclAsType(SourceElement).map m(); }
				""");
		assertFalse(result.isSuccess());
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getSeverity() == Diagnostic.ERROR
				&& d.getMessage().contains("when-condition must be Boolean")), () -> result.diagnostics().toString());
	}

	@Test
	void anObjectInferredIntoAnInModel_isAnError_notAnException() throws Exception {
		// The out model is not passed, so the only extent is the read-only in model
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(in s : SRC) {
				    main() { object SourceElement { name := 'new'; }; log('after'); }
				}
				""", new BasicQvtoModelExtent(createSourceElement("a", 1)));
		assertFalse(result.isSuccess());
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getSeverity() == Diagnostic.ERROR
				&& d.getMessage().contains("to an in model: it is read-only")), () -> result.diagnostics().toString());
		assertLogged(result, "after");
	}

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains(expected)),
				() -> "Expected log containing '" + expected + "' but diagnostics were: " + result.diagnostics());
	}
}
