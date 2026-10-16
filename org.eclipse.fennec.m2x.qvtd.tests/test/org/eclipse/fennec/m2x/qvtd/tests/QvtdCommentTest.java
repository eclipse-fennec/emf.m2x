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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Code inside a comment is never executed (QVT v1.3 §7.13.3: {@code --} to the end of the line
 * and {@code /* … *}{@code /}, the comment forms QVT-R takes from OCL).
 *
 * <p>Each case runs the same transformation twice: once with the code active, to show that it
 * has an effect one could observe, and once with it commented out, where that effect must be
 * absent. Without the first run a passing second run would prove nothing.
 */
@DisplayName("§7.13.3: Comments are never executed")
class QvtdCommentTest extends AbstractQvtdEngineTest {

	/** {@code %s} is where the relation call stands: active, or commented out. */
	private static final String WHERE_CALL = """
			transformation T(uml : simpleuml, rdbms : simplerdbms) {
				top relation PackageToSchema {
					pn : String;
					checkonly domain uml p : Package { name = pn };
					enforce domain rdbms s : Schema { name = pn };
					where {
						%s
					}
				}
				relation Leak {
					checkonly domain uml p : Package {};
					enforce domain rdbms s : Schema { ownedTable = t : Table { name = 'leak' } };
				}
			}
			""";

	@Test
	@DisplayName("A relation call commented out in the where-clause is not invoked")
	void whereCallInCommentNotInvoked() throws QvtdParseException {
		assertEquals(List.of("leak"), tablesOf(run(WHERE_CALL.formatted("Leak(p, s);"))),
				"control: the active call creates the table");

		assertEquals(List.of(), tablesOf(run(WHERE_CALL.formatted("-- Leak(p, s);"))));
		assertEquals(List.of(), tablesOf(run(WHERE_CALL.formatted("/* Leak(p, s); */"))));
		assertEquals(List.of(), tablesOf(run(WHERE_CALL.formatted("""
				/*
							Leak(p, s);
						*/"""))));
	}

	/** {@code %s} follows {@code name = pn} in the enforced template. */
	private static final String TEMPLATE_PART = """
			transformation T(uml : simpleuml, rdbms : simplerdbms) {
				top relation PackageToSchema {
					pn : String;
					checkonly domain uml p : Package { name = pn };
					enforce domain rdbms s : Schema {
						name = pn%s
					};
				}
			}
			""";

	@Test
	@DisplayName("A property template commented out is not enforced")
	void templatePartInCommentNotEnforced() throws QvtdParseException {
		String part = ", ownedTable = t : Table { name = 'leak' }";
		assertEquals(List.of("leak"), tablesOf(run(TEMPLATE_PART.formatted(part))),
				"control: the active part creates the table");

		assertEquals(List.of(), tablesOf(run(TEMPLATE_PART.formatted(" -- " + part + "\n"))));
		assertEquals(List.of(), tablesOf(run(TEMPLATE_PART.formatted(" /* " + part + " */"))));
	}

	/** {@code %s} is the when-clause's only content besides {@code true}. */
	private static final String WHEN_GUARD = """
			transformation T(uml : simpleuml, rdbms : simplerdbms) {
				top relation PackageToSchema {
					pn : String;
					checkonly domain uml p : Package { name = pn };
					enforce domain rdbms s : Schema { name = pn };
					when {
						true;
						%s
					}
				}
			}
			""";

	@Test
	@DisplayName("A guard commented out in the when-clause does not block the relation")
	void whenGuardInCommentNotEvaluated() throws QvtdParseException {
		assertEquals(0, run(WHEN_GUARD.formatted("false;")).size(),
				"control: the active guard blocks the relation");

		assertEquals(1, run(WHEN_GUARD.formatted("-- false;")).size());
		assertEquals(1, run(WHEN_GUARD.formatted("/* false; */")).size());
	}

	/** Enforces rdbms from one Package("HR") and returns the rdbms extent's contents. */
	private List<EObject> run(String source) throws QvtdParseException {
		QvtdModelExtent rdbms = emptyExtent();
		QvtdExecutionResult result = executeEnforce(source, "rdbms",
				Map.of("uml", QvtdModelExtent.of(createPackage("HR")), "rdbms", rdbms));
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		return rdbms.getContents();
	}

	/** The names of the tables of the one schema in the contents. */
	private static List<String> tablesOf(List<EObject> contents) {
		assertEquals(1, contents.size(), () -> "one schema expected: " + contents);
		return getTables(contents.get(0)).stream().map(AbstractQvtdEngineTest::getName).toList();
	}
}
