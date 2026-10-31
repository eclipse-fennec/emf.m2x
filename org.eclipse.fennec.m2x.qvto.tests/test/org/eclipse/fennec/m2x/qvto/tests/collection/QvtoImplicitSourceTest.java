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
package org.eclipse.fennec.m2x.qvto.tests.collection;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * Named iterator variables as implicit sources, OCL v2.4 §9.3.25 [A], §9.4.1 [10], §7.8 — as the
 * OCL parser has done since #320.
 */
class QvtoImplicitSourceTest extends AbstractQvtoEngineTest {

	private static QvtoExecutionResult run(String main) throws Exception {
		return executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(in s : SRC) {
				    main() {
				        var objs : Sequence(SourceElement) := s.objectsOfType(SourceElement)->sortedBy(value)->asSequence();
				%s
				    }
				}
				""".formatted(main), new BasicQvtoModelExtent(createSourceElement("a", 1), createSourceElement("b", 2)));
	}

	@Test
	void aBareName_isThePropertyOfTheNamedIterator() throws Exception {
		QvtoExecutionResult result = run("""
				log('collect=' + objs->collect(p | name)->joinfields(',', '', ''));
				log('xcollect=' + objs->xcollect(p | name)->joinfields(',', '', ''));
				log('iterate=' + objs->iterate(e; acc : String = '' | acc + name));
				""");
		assertSuccess(result);
		assertLogged(result, "collect=a,b");
		assertLogged(result, "xcollect=a,b");
		assertLogged(result, "iterate=ab");
	}

	@Test
	void theInnermostSource_answers() throws Exception {
		// §7.8: "the most inner scope"
		QvtoExecutionResult result = run("""
				log('inner=' + objs->collect(o | objs->select(i | value > 1)->size())->sum().repr());
				""");
		assertSuccess(result);
		assertLogged(result, "inner=2");
	}

	@Test
	void aVariableInScope_comesFirst() throws Exception {
		QvtoExecutionResult result = run("""
				var name := 'outer';
				log('var=' + objs->collect(p | name)->joinfields(',', '', ''));
				""");
		assertSuccess(result);
		assertLogged(result, "var=outer,outer");
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
