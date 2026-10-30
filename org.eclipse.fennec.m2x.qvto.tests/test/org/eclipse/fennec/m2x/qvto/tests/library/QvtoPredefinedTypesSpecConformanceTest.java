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
package org.eclipse.fennec.m2x.qvto.tests.library;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * Predefined types, repr() and log against QVT v1.3 §8.3.1-§8.3.4 and §8.2.2.19 (#345).
 */
class QvtoPredefinedTypesSpecConformanceTest extends AbstractQvtoEngineTest {

	private static QvtoExecutionResult run(String elements, String main) throws Exception {
		return executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(in s : SRC) {
				%s
				    main() {
				%s
				    }
				}
				""".formatted(elements, main), new BasicQvtoModelExtent(createSourceElement("a", 1)));
	}

	@Test
	void thePredefinedTypes_areTypeNames() throws Exception {
		// §8.3.1: Object, Transformation, Model, Status; §8.3.4: Element
		QvtoExecutionResult result = run("""
				    query describe(o : Object) : String { return o.repr(); }
				    query nameOf(e : Element) : String { return e.oclAsType(SourceElement).name; }
				    query Element::kind() : String { return 'element'; }
				""", """
				var m : Model := s;
				var e : Element := s.rootObjects()->first();
				var st : Status := null;
				var t : Transformation := null;
				log('object=' + describe(5) + ' element=' + nameOf(e) + ' kind=' + e.kind());
				log('model=' + m.rootObjects()->size().repr());
				""");
		assertSuccess(result);
		assertLogged(result, "object=5 element=a kind=element");
		assertLogged(result, "model=1");
	}

	@Test
	void theDeprecatedSynonyms_areSupported() throws Exception {
		// §8.3.2: Any, isKindOf, isTypeOf, asType
		QvtoExecutionResult result = run("", """
				var a : Any := 1;
				var e := s.rootObjects()->first();
				log('kind=' + e.isKindOf(SourceElement).repr() + ' type=' + e.isTypeOf(SourceElement).repr()
						+ ' as=' + e.asType(SourceElement).name);
				""");
		assertSuccess(result);
		assertLogged(result, "kind=true type=true as=a");
	}

	@Test
	void repr_ofAModelObject_isReadable() throws Exception {
		// §8.3.3 Object::repr()
		QvtoExecutionResult result = run("", """
				log('r=' + s.rootObjects()->first().repr());
				log('f=' + 'x %s'.format(s.rootObjects()->first()));
				""");
		assertSuccess(result);
		assertLogged(result, "r=SourceElement 'a'");
		assertLogged(result, "f=x SourceElement 'a'");
	}

	@Test
	void log_printsTheElementWithRepr_andCarriesTheLevel() throws Exception {
		// §8.2.2.19: "the second argument gives the model element to be print (using an implicit
		// call to the 'repr' operation) … the third argument gives a level number"
		QvtoExecutionResult result = run("", "log('found', s.rootObjects()->first(), 3);");
		assertSuccess(result);
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().equals("log: found SourceElement 'a'") && d.getCode() == 3),
				() -> result.diagnostics().toString());
	}

	@Test
	void log_withAFalseCondition_evaluatesNoArgument() throws Exception {
		// §8.2.2.19: "No log record is produced if it evaluates to false"
		QvtoExecutionResult result = run("", """
				var n : SourceElement := null;
				log('x', n.name) when false;
				log('done');
				""");
		assertSuccess(result);
		assertTrue(result.diagnostics().stream().allMatch(d -> d.getMessage().equals("log: done")),
				() -> result.diagnostics().toString());
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
