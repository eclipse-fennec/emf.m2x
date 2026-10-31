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
package org.eclipse.fennec.m2x.qvtd.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A template as a when or where predicate (§7.13.5 {@code <OclExpressionCS> ::= … | <template>},
 * §7.13.7 ExtOclExpressionCS, #361): it holds when some object of the run's models matches it.
 * Eclipse leaves this out and §7.10 gives it no semantics of its own; it was decided to implement
 * it as that Boolean.
 */
class QvtdTemplatePredicateTest extends AbstractQvtdEngineTest {

	/** Schemas named after the packages, with {@code %s} as when and {@code %s} as where clause. */
	private static String packageToSchema(String when, String where) {
		return """
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
				    top relation R {
				        pn : String;
				        checkonly domain uml p : Package { name = pn };
				        enforce domain rdbms s : Schema { name = pn };
				        %s
				        %s
				    }
				}
				""".formatted(when, where);
	}

	/** Packages of the given names; each name ending in '+' gets one class, without the '+'. */
	private static QvtdModelExtent packages(String... names) {
		List<EObject> roots = new ArrayList<>();
		for (String name : names) {
			EObject pkg = createPackage(name.replace("+", ""));
			if (name.endsWith("+")) {
				addClassToPackage(pkg, createClass("C", null));
			}
			roots.add(pkg);
		}
		return QvtdModelExtent.of(roots.toArray(EObject[]::new));
	}

	private static QvtdExecutionResult run(String source, QvtdModelExtent uml, QvtdModelExtent rdbms)
			throws QvtdParseException {
		return executeEnforce(source, "rdbms", Map.of("uml", uml, "rdbms", rdbms));
	}

	private static List<String> names(QvtdModelExtent extent) {
		return extent.getContents().stream().map(o -> getName(o)).sorted().toList();
	}

	@Test
	@DisplayName("when: the relation applies where a match of the template exists")
	void aWhenTemplate_holdsWhereAMatchExists() throws QvtdParseException {
		QvtdModelExtent rdbms = emptyExtent();

		QvtdExecutionResult result = run(packageToSchema("when { c : Class { owningPackage = p }; }", ""),
				packages("A+", "B"), rdbms);

		assertTrue(result.isSuccess(), "" + result.diagnostics());
		assertEquals(List.of("A"), names(rdbms));
	}

	@Test
	@DisplayName("when: a template whose root is bound matches that object only")
	void aWhenTemplateWithABoundRoot_matchesThatObject() throws QvtdParseException {
		QvtdModelExtent rdbms = emptyExtent();

		run(packageToSchema("when { p : Package { name = 'B' }; }", ""), packages("A", "B"), rdbms);

		assertEquals(List.of("B"), names(rdbms));
	}

	@Test
	@DisplayName("where: a template that matches lets the relation hold")
	void aWhereTemplateThatMatches_holds() throws QvtdParseException {
		QvtdModelExtent rdbms = emptyExtent();

		QvtdExecutionResult result = run(packageToSchema("", "where { c : Class { owningPackage = p }; }"),
				packages("A+"), rdbms);

		assertTrue(result.isSuccess(), "" + result.diagnostics());
		assertEquals(List.of("A"), names(rdbms));
	}

	@Test
	@DisplayName("where: a template without a match is a relation that cannot hold, an ERROR")
	void aWhereTemplateWithoutAMatch_doesNotHold() throws QvtdParseException {
		QvtdExecutionResult result = run(packageToSchema("", "where { c : Class { owningPackage = p }; }"),
				packages("B"), emptyExtent());

		assertFalse(result.isSuccess());
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getSeverity() == Diagnostic.ERROR
				&& d.getMessage().contains("Relation 'R' cannot hold")), "" + result.diagnostics());
	}

	@Test
	@DisplayName("a collection template as a predicate is a positioned error")
	void aCollectionTemplatePredicate_isAnError() {
		QvtdParseException e = assertThrows(QvtdParseException.class,
				() -> parse(packageToSchema("when { cs : Set(Class) { _ ++ _ }; }", "")));

		assertTrue(e.getErrors().stream().anyMatch(d -> d.getMessage().contains("A collection template cannot be a predicate")
				&& d.getLine() == 6), () -> "" + e.getErrors());
	}
}
