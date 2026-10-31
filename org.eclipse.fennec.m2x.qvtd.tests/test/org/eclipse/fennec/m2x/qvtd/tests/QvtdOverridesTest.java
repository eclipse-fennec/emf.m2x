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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.qvtbase.Rule;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * §7.11.1.4: "A rule may conditionally override another rule. The overriding rule is executed in
 * place of the overridden rule when the overriding conditions are satisfied" — its when clause
 * and the patterns of its non-target domains (decided in #356); "An abstract rule is never
 * matched directly and so never executes directly".
 */
class QvtdOverridesTest extends AbstractQvtdEngineTest {

	private static List<String> schemas(String source, String... packages) throws QvtdParseException {
		QvtdModelExtent uml = QvtdModelExtent.of();
		for (String name : packages) {
			uml.getContents().add(createPackage(name));
		}
		QvtdModelExtent rdbms = emptyExtent();
		QvtdExecutionResult result = executeEnforce(source, "rdbms", Map.of("uml", uml, "rdbms", rdbms));
		assertTrue(result.isSuccess(), "" + result.diagnostics());
		return rdbms.getContents().stream().map(o -> getName(o)).sorted().toList();
	}

	private static Rule rule(RelationalTransformation t, String name) {
		return t.getRule().stream().filter(r -> name.equals(r.getName())).findFirst().orElseThrow();
	}

	@Test
	@DisplayName("where the overriding when clause is false, the overridden relation runs")
	void overrides_conditionFalse_baseRuns() throws QvtdParseException {
		assertEquals(List.of("HR", "OVER"), schemas("""
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					top relation Base {
						pn : String;
						checkonly domain uml p : Package { name = pn };
						enforce domain rdbms s : Schema { name = pn };
					}
					top relation Over overrides Base {
						pn : String;
						checkonly domain uml p : Package { name = pn };
						enforce domain rdbms s : Schema { name = 'OVER' };
						when { pn = 'Special'; }
					}
				}
				""", "HR", "Special"));
	}

	@Test
	@DisplayName("a stricter source pattern is an overriding condition too")
	void overrides_sourcePattern_isACondition() throws QvtdParseException {
		assertEquals(List.of("HR", "OVER"), schemas("""
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					top relation Base {
						pn : String;
						checkonly domain uml p : Package { name = pn };
						enforce domain rdbms s : Schema { name = pn };
					}
					top relation Over overrides Base {
						checkonly domain uml p : Package { name = 'Special' };
						enforce domain rdbms s : Schema { name = 'OVER' };
					}
				}
				""", "HR", "Special"));
	}

	private static String called(String sub, String over) {
		return """
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					top relation PackageToSchema {
						pn : String;
						checkonly domain uml p : Package { name = pn };
						enforce domain rdbms s : Schema { name = pn };
						where { Sub(p, s); }
					}
					%s
					%s
				}
				""".formatted(sub, over);
	}

	private static List<String> tables(String source, String packageName) throws QvtdParseException {
		QvtdModelExtent rdbms = emptyExtent();
		QvtdExecutionResult result = executeEnforce(source, "rdbms",
				Map.of("uml", QvtdModelExtent.of(createPackage(packageName)), "rdbms", rdbms));
		assertTrue(result.isSuccess(), "" + result.diagnostics());
		EObject schema = rdbms.getContents().get(0);
		return getTables(schema).stream().map(t -> getName(t)).toList();
	}

	private static String sub(String modifiers, String name, String table, String when) {
		return """
				%s relation %s {
					checkonly domain uml p : Package {};
					enforce domain rdbms s : Schema { ownedTable = t : Table { name = '%s' } };
					%s
				}
				""".formatted(modifiers, name, table, when);
	}

	@Test
	@DisplayName("a call from where reaches the overriding relation")
	void overrides_whereCall_dispatchesToOverriding() throws QvtdParseException {
		assertEquals(List.of("over"), tables(called(sub("", "Sub", "base", ""), sub("", "SubOver overrides Sub", "over", "")), "HR"));
	}

	@Test
	@DisplayName("a call of an abstract relation runs the relation overriding it")
	void abstract_calledWithOverride_runsOverriding() throws QvtdParseException {
		assertEquals(List.of("fromB"), tables(called(sub("abstract", "Sub", "fromAbstract", ""),
				sub("", "B overrides Sub", "fromB", "")), "HR"));
	}

	@Test
	@DisplayName("a call of an abstract relation that no override applies to is an error")
	void abstract_calledWithoutOverride_diagnostic() throws QvtdParseException {
		String source = called(sub("abstract", "Sub", "fromAbstract", ""),
				sub("", "B overrides Sub", "fromB", "when { p.name = 'X'; }"));
		QvtdModelExtent rdbms = emptyExtent();
		QvtdExecutionResult result = executeEnforce(source, "rdbms",
				Map.of("uml", QvtdModelExtent.of(createPackage("Y")), "rdbms", rdbms));
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getSeverity() == Diagnostic.ERROR
				&& d.getMessage().contains("Abstract relation 'Sub' is called, but no relation overriding it applies to p = Package 'Y'")),
				"" + result.diagnostics());
		assertEquals(List.of(), getTables(rdbms.getContents().get(0)), "the abstract relation did not run");
	}

	@Test
	@DisplayName("a relation may override a relation it inherits through extends")
	void overrides_acrossExtends() throws QvtdParseException {
		String source = """
				transformation Base(uml : simpleuml, rdbms : simplerdbms) {
					top relation R {
						pn : String;
						checkonly domain uml p : Package { name = pn };
						enforce domain rdbms s : Schema { name = pn };
					}
				}
				transformation T(uml : simpleuml, rdbms : simplerdbms) extends Base {
					top relation R2 overrides R {
						pn : String;
						checkonly domain uml p : Package { name = pn };
						enforce domain rdbms s : Schema { name = pn.toUpper() };
					}
				}
				""";
		RelationalTransformation t = parse(source);
		assertSame(rule(t, "R"), rule(t, "R2").getOverrides(), "the inherited R, in T");
		assertEquals(List.of("HR"), schemas(source, "hr"));
	}

	@Test
	@DisplayName("overriding a relation that does not exist is a positioned parse error")
	void overrides_unknownName_parseError() {
		QvtdParseException e = assertThrows(QvtdParseException.class, () -> parse("""
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					top relation R overrides Nope {
						checkonly domain uml p : Package {};
						enforce domain rdbms s : Schema {};
					}
				}
				"""));
		assertTrue(e.getMessage().contains("Relation 'R' overrides 'Nope', which is no relation of its transformation"),
				e.getMessage());
		assertTrue(e.getErrors().stream().anyMatch(d -> d.getLine() == 2), "" + e.getErrors());
	}
}
