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
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * §7.7: "Execution proceeds as if the source and target models are distinct with an atomic update
 * of non-distinct models occurring on completion of the transformation [...] Execution of a
 * transformation should return a Boolean status to indicate whether any changes were made to
 * target models" (#353).
 */
class QvtdInPlaceTest extends AbstractQvtdEngineTest {

	@Test
	@DisplayName("in-place: a relation matches the state before the run, not what another relation created")
	void inPlace_doesNotSeeOwnOutputAsSource() throws QvtdParseException {
		// Without a snapshot ClassToPackage, running after Version, matched Employee_v2 too
		String source = """
				transformation T(m : simpleuml) {
					top relation Version {
						cn : String;
						checkonly domain m c1 : Class { name = cn };
						enforce domain m c2 : Class { name = cn.concat('_v2') };
					}
					top relation ClassToPackage {
						n : String;
						checkonly domain m c : Class { name = n };
						enforce domain m p : Package { name = n };
					}
				}
				""";
		QvtdModelExtent m = QvtdModelExtent.of(createClass("Employee", null));
		QvtdExecutionResult result = executeEnforce(source, "m", Map.of("m", m));

		assertTrue(result.isSuccess(), "" + result.diagnostics());
		assertTrue(result.modelChanged());
		List<String> packages = m.getContents().stream().filter(o -> "Package".equals(o.eClass().getName()))
				.map(o -> getName(o)).toList();
		assertEquals(List.of("Employee"), packages);
	}

	private static final String MARK_PERSISTENT = """
			transformation T(m : simpleuml) {
				key Class {name};
				top relation MarkPersistent {
					cn : String;
					checkonly domain m c1 : Class { name = cn };
					enforce domain m c2 : Class { name = cn, kind = 'Persistent' };
				}
			}
			""";

	@Test
	@DisplayName("in-place: a run that reaches its fixpoint reports a change, the next one does not")
	void inPlace_secondRun_changedFalse() throws QvtdParseException {
		EObject employee = createClass("Employee", null);
		QvtdModelExtent m = QvtdModelExtent.of(employee);

		QvtdExecutionResult first = executeEnforce(MARK_PERSISTENT, "m", Map.of("m", m));
		assertTrue(first.isSuccess(), "" + first.diagnostics());
		assertTrue(first.modelChanged(), "kind was set");
		assertEquals(List.of(employee), m.getContents(), "the class is updated, not copied");

		QvtdExecutionResult second = executeEnforce(MARK_PERSISTENT, "m", Map.of("m", m));
		assertTrue(second.isSuccess(), "" + second.diagnostics());
		assertFalse(second.modelChanged(), "nothing left to change");
	}

	private static final String SCHEMAS = """
			transformation T(uml : simpleuml, rdbms : simplerdbms) {
				key Schema {name};
				top relation PackageToSchema {
					pn : String;
					checkonly domain uml p : Package { name = pn };
					enforce domain rdbms s : Schema { name = pn };
				}
			}
			""";

	@Test
	@DisplayName("creating an object is a change; running again on the consistent target is none")
	void enforce_createsObject_thenConsistent() throws QvtdParseException {
		QvtdModelExtent uml = QvtdModelExtent.of(createPackage("HR"));
		QvtdModelExtent rdbms = emptyExtent();
		assertTrue(executeEnforce(SCHEMAS, "rdbms", Map.of("uml", uml, "rdbms", rdbms)).modelChanged());
		assertFalse(executeEnforce(SCHEMAS, "rdbms", Map.of("uml", uml, "rdbms", rdbms)).modelChanged());
	}

	@Test
	@DisplayName("modifying a property of an existing object is a change")
	void enforce_modifiesObject_changedTrue() throws QvtdParseException {
		EObject employee = createClass("Employee", null);
		QvtdModelExtent rdbms = emptyExtent();
		String source = """
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					key Table {name};
					top relation ClassToTable {
						cn : String;
						checkonly domain uml c : Class { name = cn };
						enforce domain rdbms t : Table { name = cn, type = 'TABLE' };
					}
				}
				""";
		executeEnforce(source, "rdbms", Map.of("uml", QvtdModelExtent.of(employee), "rdbms", rdbms));
		EObject table = rdbms.getContents().get(0);
		table.eSet(table.eClass().getEStructuralFeature("type"), "VIEW");

		QvtdExecutionResult again = executeEnforce(source, "rdbms", Map.of("uml", QvtdModelExtent.of(employee), "rdbms", rdbms));
		assertTrue(again.modelChanged());
		assertEquals("TABLE", table.eGet(table.eClass().getEStructuralFeature("type")));
	}

	@Test
	@DisplayName("deleting an object is a change")
	void enforce_deletesObject_changedTrue() throws QvtdParseException {
		EObject stale = EcoreUtil.create(ecoreHelper.getEClass(rdbmsPackage, "Schema"));
		stale.eSet(stale.eClass().getEStructuralFeature("name"), "Stale");
		QvtdModelExtent rdbms = QvtdModelExtent.of(stale);
		QvtdExecutionResult result = executeEnforce(SCHEMAS, "rdbms", Map.of("uml", QvtdModelExtent.of(), "rdbms", rdbms));
		assertTrue(result.modelChanged());
		assertEquals(List.of(), rdbms.getContents());
	}

	@Test
	@DisplayName("a check-only run changes nothing")
	void checkOnly_changedFalse() throws QvtdParseException {
		QvtdExecutionResult result = executeCheckOnly(SCHEMAS,
				Map.of("uml", QvtdModelExtent.of(createPackage("HR")), "rdbms", emptyExtent()));
		assertFalse(result.modelChanged());
	}
}
