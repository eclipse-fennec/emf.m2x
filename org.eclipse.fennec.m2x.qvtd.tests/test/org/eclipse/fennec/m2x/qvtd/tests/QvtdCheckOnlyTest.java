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

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
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
 * Checking a relation (#348): "the transformation simply checks whether the relations hold in all
 * directions, and reports errors when they do not" (§7.9), each direction k as §7.10.1 defines it.
 * A relation that does not hold is a WARNING with {@link QvtdExecutionResult#INCONSISTENCY}:
 * {@code isSuccess()} stays true, {@code isConsistent()} is false. An enforce run in the direction
 * of a domain that is only checked checks it (§7.2.3).
 */
class QvtdCheckOnlyTest extends AbstractQvtdEngineTest {

	private static final String PACKAGE_TO_SCHEMA = """
			transformation T(uml : simpleuml, rdbms : simplerdbms) {
				top relation PackageToSchema {
					pn : String;
					%s domain uml p : Package { name = pn };
					%s domain rdbms s : Schema { name = pn };
					%s
				}
			}
			""";

	private static String packageToSchema(String umlQualifier, String rdbmsQualifier, String where) {
		return PACKAGE_TO_SCHEMA.formatted(umlQualifier, rdbmsQualifier, where);
	}

	private static final String BOTH_CHECKONLY = packageToSchema("checkonly", "checkonly", "");

	private static QvtdModelExtent uml(String... packages) {
		QvtdModelExtent extent = QvtdModelExtent.of();
		for (String name : packages) {
			extent.getContents().add(createPackage(name));
		}
		return extent;
	}

	private static EObject create(String className, String name) {
		EClass eClass = ecoreHelper.getEClass(rdbmsPackage, className);
		EObject object = EcoreUtil.create(eClass);
		object.eSet(eClass.getEStructuralFeature("name"), name);
		return object;
	}

	private static QvtdModelExtent rdbms(String... schemas) {
		QvtdModelExtent extent = QvtdModelExtent.of();
		for (String name : schemas) {
			extent.getContents().add(create("Schema", name));
		}
		return extent;
	}

	private static QvtdExecutionResult check(String source, QvtdModelExtent uml, QvtdModelExtent rdbms)
			throws QvtdParseException {
		return executeCheckOnly(source, Map.of("uml", uml, "rdbms", rdbms));
	}

	private static List<String> inconsistencies(QvtdExecutionResult result) {
		return result.diagnostics().stream()
				.filter(d -> d.getCode() == QvtdExecutionResult.INCONSISTENCY)
				.map(Diagnostic::getMessage).toList();
	}

	private static void assertConsistent(QvtdExecutionResult result) {
		assertTrue(result.isSuccess(), "" + result.diagnostics());
		assertTrue(result.isConsistent(), "" + result.diagnostics());
		assertEquals(List.of(), result.diagnostics());
	}

	/** Inconsistent, not failed: one report per violation, each naming relation, direction and object. */
	private static void assertInconsistent(QvtdExecutionResult result, String... expected) {
		assertTrue(result.isSuccess(), "a check that finds a violation still ran: " + result.diagnostics());
		assertFalse(result.isConsistent());
		List<String> found = inconsistencies(result);
		assertEquals(expected.length, found.size(), "" + found);
		for (int i = 0; i < expected.length; i++) {
			assertTrue(found.get(i).contains(expected[i]), found.get(i) + " should contain " + expected[i]);
		}
	}

	// ── consistent ──

	@Test
	@DisplayName("matching pairs in both directions: consistent, nothing reported")
	void checkOnly_consistentPairs_noDiagnostic() throws QvtdParseException {
		assertConsistent(check(BOTH_CHECKONLY, uml("A", "B"), rdbms("A", "B")));
	}

	@Test
	@DisplayName("empty models: the relation holds vacuously, nothing reported")
	void checkOnly_emptyModels_noDiagnostic() throws QvtdParseException {
		assertConsistent(check(BOTH_CHECKONLY, uml(), rdbms()));
	}

	// ── inconsistent ──

	@Test
	@DisplayName("a Package without a Schema is reported in direction rdbms")
	void checkOnly_missingTarget_reported() throws QvtdParseException {
		assertInconsistent(check(BOTH_CHECKONLY, uml("A", "B"), rdbms("A")),
				"Relation 'PackageToSchema' does not hold in direction 'rdbms': no match for p = Package 'B'");
	}

	@Test
	@DisplayName("checkonly/enforce checked: the enforce domain is checked like any other")
	void checkOnly_enforceDomainChecked() throws QvtdParseException {
		// §7.9: "irrespective of whether the domains are marked checkonly or enforced"
		assertInconsistent(check(packageToSchema("checkonly", "enforce", ""), uml("A", "B"), rdbms("A")),
				"direction 'rdbms': no match for p = Package 'B'");
	}

	@Test
	@DisplayName("a Schema without a Package is reported in direction uml")
	void checkOnly_missingSource_reportedInDirectionUml() throws QvtdParseException {
		assertInconsistent(check(BOTH_CHECKONLY, uml("A"), rdbms("A", "Z")),
				"direction 'uml': no match for s = Schema 'Z'");
	}

	@Test
	@DisplayName("an empty target model is reported for each source object")
	void checkOnly_emptyTarget_reported() throws QvtdParseException {
		assertInconsistent(check(BOTH_CHECKONLY, uml("A"), rdbms()), "direction 'rdbms': no match for p = Package 'A'");
	}

	@Test
	@DisplayName("a where predicate that is false: the relation does not hold, in either direction")
	void checkOnly_whereFalse_reported() throws QvtdParseException {
		QvtdExecutionResult result = check(packageToSchema("checkonly", "checkonly", "where { pn <> 'A'; }"),
				uml("A"), rdbms("A"));
		assertInconsistent(result, "direction 'uml'", "direction 'rdbms'");
	}

	private static final String CLASS_TO_TABLE = """
			transformation T(uml : simpleuml, rdbms : simplerdbms) {
				top relation PackageToSchema {
					pn : String;
					checkonly domain uml p : Package { name = pn };
					checkonly domain rdbms s : Schema { name = pn };
				}
				top relation ClassToTable {
					cn : String;
					p : Package;
					s : Schema;
					checkonly domain uml c : Class { owningPackage = p, name = cn };
					checkonly domain rdbms t : Table { schema = s, name = cn };
					when { PackageToSchema(p, s); }
					%s
				}
				relation AttributeToColumn {
					an : String;
					checkonly domain uml a : Attribute { name = an };
					checkonly domain rdbms col : Column { name = an };
				}
			}
			""";

	/** P1{C1} on one side, Schema P1 on the other — with the Table C1 or without it. */
	private static QvtdExecutionResult checkClasses(boolean withTable, String where) throws QvtdParseException {
		EObject pkg = createPackage("P1");
		EObject cls = createClass("C1", null);
		addClassToPackage(pkg, cls);
		addAttributeToClass(cls, createAttribute("id", "Integer"));
		EObject schema = create("Schema", "P1");
		if (withTable) {
			EObject table = create("Table", "C1");
			getTables(schema).add(table);
		}
		return check(CLASS_TO_TABLE.formatted(where), QvtdModelExtent.of(pkg), QvtdModelExtent.of(schema));
	}

	@Test
	@DisplayName("a relation with a when call is checked: complete models are consistent")
	void checkOnly_whenRelationCall_consistent() throws QvtdParseException {
		assertConsistent(checkClasses(true, ""));
	}

	@Test
	@DisplayName("a relation with a when call is checked: the missing Table is reported")
	void checkOnly_whenRelationCall_missingTable_reported() throws QvtdParseException {
		// Before #348 the check recorded no trace, so ClassToTable's when call found nothing and
		// the relation was skipped without a word
		assertInconsistent(checkClasses(false, ""),
				"Relation 'ClassToTable' does not hold in direction 'rdbms': no match for c = Class 'C1'");
	}

	@Test
	@DisplayName("a relation called from where that does not hold makes the caller not hold")
	void checkOnly_whereRelationCallFails_reported() throws QvtdParseException {
		// ClassToTable's where needs a Column for the class's attribute; there is none
		String where = """
				where {
					AttributeToColumn(c.ownedAttribute->any(true), t.ownedColumn->any(true));
				}
				""";
		QvtdExecutionResult result = checkClasses(true, where);
		assertFalse(result.isConsistent(), "" + result.diagnostics());
		assertTrue(inconsistencies(result).stream().anyMatch(m -> m.contains("ClassToTable")), "" + result.diagnostics());
	}

	// ── enforce runs ──

	private static final String CHECK_UML_ENFORCE_RDBMS = packageToSchema("checkonly", "enforce", "");

	@Test
	@DisplayName("enforce towards a checkonly domain: consistent models, nothing reported")
	void enforceTowardsCheckonlyDomain_consistent_noDiagnostic() throws QvtdParseException {
		QvtdModelExtent uml = uml("Z");
		QvtdExecutionResult result = executeEnforce(CHECK_UML_ENFORCE_RDBMS, "uml",
				Map.of("uml", uml, "rdbms", rdbms("Z")));
		assertConsistent(result);
		assertEquals(1, uml.getContents().size());
	}

	@Test
	@DisplayName("enforce towards a checkonly domain reports what does not hold, and creates nothing")
	void enforceTowardsCheckonlyDomain_reportsInconsistency() throws QvtdParseException {
		// §7.2.3: "it is simply reported as an inconsistency - a package is not created because
		// the uml model is not enforced, it is only checked"
		QvtdModelExtent uml = uml("A");
		QvtdExecutionResult result = executeEnforce(CHECK_UML_ENFORCE_RDBMS, "uml",
				Map.of("uml", uml, "rdbms", rdbms("A", "X")));
		assertInconsistent(result, "Relation 'PackageToSchema' does not hold in direction 'uml': no match for s = Schema 'X'");
		assertEquals(1, uml.getContents().size(), "uml is only checked: no Package X");
	}

	@Test
	@DisplayName("an enforce run is consistent by default; the post-check finds what does not hold")
	void enforce_postCheck_findsWhatDoesNotHold() throws QvtdParseException {
		// By default an enforce run checks only the target model's checked domains (decided in
		// #348). The extra Schema X has no Package: kept with deleteUnmatched(false), only the
		// post-check in every direction sees it.
		QvtdEngine keeping = QvtdEngines.create(QvtdConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build())
				.packageRegistry(engineRegistry()).deleteUnmatched(false).build());
		QvtdModelExtent uml = uml("A");
		QvtdModelExtent rdbms = rdbms("X");
		QvtdExecutionResult plain = keeping.execute(keeping.parse(CHECK_UML_ENFORCE_RDBMS, "T"),
				QvtdExecutionContext.enforce("rdbms", Map.of("uml", uml, "rdbms", rdbms)));
		assertTrue(plain.isConsistent(), "" + plain.diagnostics());

		QvtdExecutionResult checked = keeping.execute(keeping.parse(CHECK_UML_ENFORCE_RDBMS, "T"),
				QvtdExecutionContext.enforce("rdbms", Map.of("uml", uml, "rdbms", rdbms)).withPostCheck());
		assertInconsistent(checked, "direction 'uml': no match for s = Schema 'X'");
	}

	@Test
	@DisplayName("by default the enforce run deletes the Schema without a Package, so the post-check finds nothing")
	void enforce_postCheck_afterDeletion_consistent() throws QvtdParseException {
		QvtdModelExtent rdbms = rdbms("X");
		QvtdExecutionResult result = engine.execute(parse(CHECK_UML_ENFORCE_RDBMS),
				QvtdExecutionContext.enforce("rdbms", Map.of("uml", uml("A"), "rdbms", rdbms)).withPostCheck());
		assertConsistent(result);
		assertEquals(List.of("A"), rdbms.getContents().stream().map(o -> getName(o)).toList());
	}

	private static EPackage.Registry engineRegistry() {
		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(umlPackage.getNsURI(), umlPackage);
		registry.put(rdbmsPackage.getNsURI(), rdbmsPackage);
		return registry;
	}
}
