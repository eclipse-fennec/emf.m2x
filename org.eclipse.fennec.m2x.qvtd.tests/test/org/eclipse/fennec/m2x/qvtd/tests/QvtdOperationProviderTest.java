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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.BasicQvtdBlackboxRegistry;
import org.eclipse.fennec.m2x.qvtd.api.QvtdBlackboxLibrary;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The OCL engine can resolve what a QVT-R transformation defines (#118).
 *
 * <p>Before this, QVT-R never handed OCL its queries and blackboxes — the top-level interception
 * in the query evaluator caught a query called as the whole expression, but a query <em>nested</em>
 * inside one went to OCL, which reported {@code Unknown operation} for something the
 * transformation defines. That false error is also why OCL's diagnostics could not be forwarded:
 * adopting them would have failed working transformations. Both halves are pinned here — the
 * nested call works and reports nothing, and a genuinely unknown operation fails the execution
 * loudly, which it silently did not before.
 */
class QvtdOperationProviderTest extends AbstractQvtdEngineTest {

	@Test
	@DisplayName("a blackbox query nested inside an expression resolves and reports nothing")
	void nestedBlackboxQueryResolves() throws QvtdParseException {
		// TypeMap(atype) is an argument of '+', so the top-level interception never sees it —
		// only OCL does, and OCL needs the provider to answer it.
		QvtdExecutionResult result = execute("""
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					query TypeMap(umlType : String) : String;

					top relation R {
						an : String; atype : String; sqltype : String;
						checkonly domain uml c : Class {
							ownedAttribute = a : Attribute { name = an, type = atype }
						};
						enforce domain rdbms t : Table {
							ownedColumn = col : Column { name = an, type = sqltype }
						};
						where {
							sqltype = 'SQL_' + TypeMap(atype);
						}
					}
				}
				""");

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertFalse(result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("Unknown operation")),
				() -> "the transformation defines TypeMap, so nothing may report it as unknown: "
						+ result.diagnostics());
		assertEquals("SQL_NUMBER", getType(getColumns(targetContents().get(0)).get(0)),
				"and the nested call really ran");
	}

	@Test
	@DisplayName("a query with a body is resolvable inside an expression too")
	void nestedBodyQueryResolves() throws QvtdParseException {
		QvtdExecutionResult result = execute("""
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					query Prefix(t : String) : String {
						'P_' + t
					}

					top relation R {
						an : String; atype : String; sqltype : String;
						checkonly domain uml c : Class {
							ownedAttribute = a : Attribute { name = an, type = atype }
						};
						enforce domain rdbms t : Table {
							ownedColumn = col : Column { name = an, type = sqltype }
						};
						where {
							sqltype = Prefix(atype) + '!';
						}
					}
				}
				""");

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertEquals("P_Integer!", getType(getColumns(targetContents().get(0)).get(0)));
	}

	@Test
	@DisplayName("a genuinely unknown operation fails the execution, loudly")
	void genuinelyUnknownOperationIsAnError() throws QvtdParseException {
		// Before #118 every OCL error was dropped (and then deliberately filtered), so this
		// misspelling produced an empty where-result and a "predicate not satisfied" warning —
		// with no hint that the predicate could never be evaluated.
		QvtdExecutionResult result = execute("""
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					query TypeMap(umlType : String) : String;

					top relation R {
						an : String; atype : String; sqltype : String;
						checkonly domain uml c : Class {
							ownedAttribute = a : Attribute { name = an, type = atype }
						};
						enforce domain rdbms t : Table {
							ownedColumn = col : Column { name = an, type = sqltype }
						};
						where {
							sqltype = TypeMapp(atype);
						}
					}
				}
				""");

		assertFalse(result.isSuccess(), "a predicate that cannot be evaluated is not a success");
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("Unknown operation")
						&& d.getMessage().contains("TypeMapp")),
				() -> "and the diagnostic names the misspelt operation: " + result.diagnostics());
	}

	// --- helpers ---

	private static String getType(EObject obj) {
		return (String) obj.eGet(obj.eClass().getEStructuralFeature("type"));
	}

	private QvtdModelExtent rdbms;

	private QvtdExecutionResult execute(String source) throws QvtdParseException {
		BasicQvtdBlackboxRegistry registry = new BasicQvtdBlackboxRegistry();
		registry.register(new QvtdBlackboxLibrary() {
			@Override
			public String getModuleName() { return "helpers"; }
			@Override
			public String getUnitQualifiedName() { return "helpers"; }
			@Override
			public List<String> getUsedPackageURIs() { return List.of(); }
			@Override
			public Object invoke(String operationName, Object self, Object[] args) {
				if ("TypeMap".equals(operationName) && args.length == 1) {
					return "Integer".equals(args[0]) ? "NUMBER" : "VARCHAR";
				}
				return null;
			}
		});

		QvtdConfiguration config = QvtdConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build())
				.blackboxRegistry(registry)
				.blackboxEnabled(true)
				.build();
		QvtdEngine blackboxEngine = QvtdEngines.create(config);

		EObject cls = createClass("Emp", null);
		addAttributeToClass(cls, createAttribute("salary", "Integer"));
		QvtdModelExtent uml = QvtdModelExtent.of(cls);
		rdbms = emptyExtent();

		RelationalTransformation transformation = blackboxEngine.parse(source, "test");
		return blackboxEngine.execute(transformation, QvtdExecutionContext.enforce("rdbms",
				Map.of("uml", uml, "rdbms", rdbms)));
	}

	private List<EObject> targetContents() {
		return rdbms.getContents();
	}
}
