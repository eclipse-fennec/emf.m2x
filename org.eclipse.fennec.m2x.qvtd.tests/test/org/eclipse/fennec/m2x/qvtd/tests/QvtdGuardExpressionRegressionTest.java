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
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.qvtbase.Rule;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.junit.jupiter.api.Test;

/**
 * Regression guards for QVT-R {@code when} / {@code where} clauses that read
 * something other than a literal.
 *
 * <p>The QVT-O counterpart is {@code QvtoE2eGuardPropertyAccessTest}: the review
 * of PR #128 showed that guards take a different code path than bodies for
 * parser-created satellites, and that no test noticed because every guard in the
 * suites was {@code true} or {@code false}. QVT-R had the same blind spot —
 * every {@code when} clause in the engine suites was a literal.
 *
 * <p>Since the satellite mechanism of #135/#137 is language-neutral by design,
 * QVT-R needs the same guard: a {@code when} / {@code where} clause that reads a
 * relation variable, a domain property or a query must keep working.
 *
 * <p><b>SPEC-FIRST:</b> Tests written against QVT v1.3 §7.10.2 (relation
 * execution), §7.11.4 (queries). Failures are implementation gaps, NOT test
 * errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtdGuardExpressionRegressionTest extends AbstractQvtdEngineTest {

	// ==== when clause reading a relation variable ====

	// §7.10.2: a when clause reading the variable bound by the checkonly domain
	@Test
	void whenClause_readsRelationVariable_matches() throws QvtdParseException {
		QvtdModelExtent uml = QvtdModelExtent.of(createPackage("HR"));
		QvtdModelExtent rdbms = emptyExtent();
		QvtdExecutionResult result = executeEnforce(guarded("pn.size() > 1"), "rdbms",
				Map.of("uml", uml, "rdbms", rdbms));
		assertSuccess(result);
		assertEquals(1, rdbms.getContents().size(),
				"when clause reading 'pn' must see the bound value 'HR'");
	}

	// §7.10.2: the same clause with a condition the bound value does not satisfy
	@Test
	void whenClause_readsRelationVariable_filtersOut() throws QvtdParseException {
		QvtdModelExtent uml = QvtdModelExtent.of(createPackage("HR"));
		QvtdModelExtent rdbms = emptyExtent();
		QvtdExecutionResult result = executeEnforce(guarded("pn.size() > 10"), "rdbms",
				Map.of("uml", uml, "rdbms", rdbms));
		assertSuccess(result);
		assertEquals(0, rdbms.getContents().size(),
				"when clause must be evaluated against the bound value, not against null");
	}

	// §7.10.2: a when clause comparing the relation variable to a literal
	@Test
	void whenClause_comparesRelationVariable() throws QvtdParseException {
		QvtdModelExtent uml = QvtdModelExtent.of(createPackage("HR"));
		QvtdModelExtent rdbms = emptyExtent();
		QvtdExecutionResult result = executeEnforce(guarded("pn = 'HR'"), "rdbms",
				Map.of("uml", uml, "rdbms", rdbms));
		assertSuccess(result);
		assertEquals(1, rdbms.getContents().size());
	}

	// §7.10.2: a when clause navigating the domain variable
	@Test
	void whenClause_readsDomainVariableProperty() throws QvtdParseException {
		QvtdModelExtent uml = QvtdModelExtent.of(createPackage("HR"));
		QvtdModelExtent rdbms = emptyExtent();
		QvtdExecutionResult result = executeEnforce(guarded("p.name <> null"), "rdbms",
				Map.of("uml", uml, "rdbms", rdbms));
		assertSuccess(result);
		assertEquals(1, rdbms.getContents().size(),
				"when clause reading 'p.name' must see the matched Package");
	}

	// §7.10.2: the domain variable navigated to a value that does not match
	@Test
	void whenClause_readsDomainVariableProperty_filtersOut() throws QvtdParseException {
		QvtdModelExtent uml = QvtdModelExtent.of(createPackage("HR"));
		QvtdModelExtent rdbms = emptyExtent();
		QvtdExecutionResult result = executeEnforce(guarded("p.name = 'OTHER'"), "rdbms",
				Map.of("uml", uml, "rdbms", rdbms));
		assertSuccess(result);
		assertEquals(0, rdbms.getContents().size());
	}

	// ==== when clause calling a query ====

	// §7.11.4: a query invoked from a when clause, returning true
	@Test
	void whenClause_callsQuery_matches() throws QvtdParseException {
		QvtdModelExtent uml = QvtdModelExtent.of(createPackage("HR"));
		QvtdModelExtent rdbms = emptyExtent();
		QvtdExecutionResult result = executeEnforce("""
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					query IsExported(n : String) : Boolean {
						n.size() > 1
					}

					top relation PackageToSchema {
						pn : String;
						checkonly domain uml p : Package {
							name = pn
						};
						enforce domain rdbms s : Schema {
							name = pn
						};
						when {
							IsExported(pn);
						}
					}
				}
				""", "rdbms", Map.of("uml", uml, "rdbms", rdbms));
		assertSuccess(result);
		assertEquals(1, rdbms.getContents().size(),
				"query called from a when clause must see the bound argument");
	}

	// §7.11.4: the same query returning false
	@Test
	void whenClause_callsQuery_filtersOut() throws QvtdParseException {
		QvtdModelExtent uml = QvtdModelExtent.of(createPackage("HR"));
		QvtdModelExtent rdbms = emptyExtent();
		QvtdExecutionResult result = executeEnforce("""
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					query IsExported(n : String) : Boolean {
						n.size() > 10
					}

					top relation PackageToSchema {
						pn : String;
						checkonly domain uml p : Package {
							name = pn
						};
						enforce domain rdbms s : Schema {
							name = pn
						};
						when {
							IsExported(pn);
						}
					}
				}
				""", "rdbms", Map.of("uml", uml, "rdbms", rdbms));
		assertSuccess(result);
		assertEquals(0, rdbms.getContents().size());
	}

	// ==== where clause ====

	// §7.10.2: a where clause reading the relation variable
	@Test
	void whereClause_readsRelationVariable() throws QvtdParseException {
		QvtdModelExtent uml = QvtdModelExtent.of(createPackage("HR"));
		QvtdModelExtent rdbms = emptyExtent();
		QvtdExecutionResult result = executeEnforce("""
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					top relation PackageToSchema {
						pn : String;
						checkonly domain uml p : Package {
							name = pn
						};
						enforce domain rdbms s : Schema {
							name = pn
						};
						where {
							pn.size() > 1;
						}
					}
				}
				""", "rdbms", Map.of("uml", uml, "rdbms", rdbms));
		assertSuccess(result);
		assertEquals(1, rdbms.getContents().size());
		assertEquals("HR", getName(rdbms.getContents().get(0)));
	}

	// ==== Guard evaluated repeatedly ====

	// §7.10.2: the guard is evaluated per match and keeps seeing the right binding
	@Test
	void whenClause_evaluatedPerMatch() throws QvtdParseException {
		EObject hr = createPackage("HR");
		EObject x = createPackage("X");
		QvtdModelExtent uml = QvtdModelExtent.of(List.of(hr, x));
		QvtdModelExtent rdbms = emptyExtent();
		QvtdExecutionResult result = executeEnforce(guarded("pn.size() > 1"), "rdbms",
				Map.of("uml", uml, "rdbms", rdbms));
		assertSuccess(result);
		assertEquals(1, rdbms.getContents().size(),
				"only the Package whose name passes the guard may produce a Schema");
		assertEquals("HR", getName(rdbms.getContents().get(0)));
	}

	// ==== Structural guard ====

	// §7.10.1: after parsing, a relation owns its declared and its domain
	// variables — and no parser satellites such as self / this / result
	@Test
	void relationVariables_containNoSatellites() throws QvtdParseException {
		RelationalTransformation t = parse(guarded("pn.size() > 1"));
		Relation relation = firstRelation(t);
		List<String> names = relation.getVariable().stream().map(Variable::getName).toList();
		assertTrue(names.contains("pn"), "declared variable 'pn' missing: " + names);
		for (String satellite : List.of("self", "this", "result")) {
			assertTrue(!names.contains(satellite),
					"'" + satellite + "' must not appear in Relation::variable, but it holds " + names);
		}
	}

	// ---- Helpers ----

	private static String guarded(String whenExpression) {
		return """
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					top relation PackageToSchema {
						pn : String;
						checkonly domain uml p : Package {
							name = pn
						};
						enforce domain rdbms s : Schema {
							name = pn
						};
						when {
							%s;
						}
					}
				}
				""".formatted(whenExpression);
	}

	private static Relation firstRelation(RelationalTransformation t) {
		for (Rule rule : t.getRule()) {
			if (rule instanceof Relation relation) {
				return relation;
			}
		}
		throw new AssertionError("transformation has no relation");
	}

	private static void assertSuccess(QvtdExecutionResult result) {
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}
}
