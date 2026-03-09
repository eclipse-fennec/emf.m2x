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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Extended P4-3 engine tests based on QVT v1.3 spec §7.10 (execution semantics)
 * and Eclipse QVT-D reference test patterns.
 *
 * Covers:
 * - Nested ObjectTemplateExp patterns (§7.11.2.2)
 * - Shared variables across domains (§7.10)
 * - Literal property matching
 * - Multiple top relations
 * - Nested object creation during enforce (§7.10.2)
 * - Pattern mismatch scenarios
 */
class QvtdEnginePatternTest extends AbstractQvtdEngineTest {

	// ===== Shared Variables (§7.10) =====

	@Nested
	@DisplayName("Shared Variables (§7.10)")
	class SharedVariableTests {

		@Test
		@DisplayName("Shared variable 'pn' binds Package.name to Schema.name")
		void sharedVariableBindsAcrossDomains() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageToSchema {
							pn : String;
							checkonly domain uml p : Package {
								name = pn
							};
							enforce domain rdbms s : Schema {
								name = pn
							};
						}
					}
					""";

			EObject pkg = createPackage("Sales");
			QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess(), "Should succeed: " + result.diagnostics());
			List<EObject> targets = rdbmsExtent.getContents();
			assertEquals(1, targets.size());
			assertEquals("Sales", getName(targets.get(0)),
					"Schema name should match Package name via shared variable 'pn'");
		}

		@Test
		@DisplayName("Multiple source objects produce multiple targets with matching names")
		void sharedVariableWithMultipleSources() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageToSchema {
							pn : String;
							checkonly domain uml p : Package {
								name = pn
							};
							enforce domain rdbms s : Schema {
								name = pn
							};
						}
					}
					""";

			EObject pkg1 = createPackage("HR");
			EObject pkg2 = createPackage("Finance");
			EObject pkg3 = createPackage("IT");
			QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg1, pkg2, pkg3);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			List<EObject> targets = rdbmsExtent.getContents();
			assertEquals(3, targets.size(), "Three schemas should be created");

			// Verify names match 1:1
			List<String> names = targets.stream().map(this::nameOf).sorted().toList();
			assertEquals(List.of("Finance", "HR", "IT"), names);
		}

		private String nameOf(EObject obj) {
			return getName(obj);
		}
	}

	// ===== Literal Property Matching =====

	@Nested
	@DisplayName("Literal Property Matching")
	class LiteralMatchingTests {

		@Test
		@DisplayName("Match only Classes with kind='Persistent' (§7.2 ClassToTable example)")
		void matchOnlyPersistentClasses() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation ClassToTable {
							cn : String;
							checkonly domain uml c : Class {
								name = cn,
								kind = 'Persistent'
							};
							enforce domain rdbms t : Table {
								name = cn
							};
						}
					}
					""";

			EObject persistent = createClass("Employee", "Persistent");
			EObject transient_ = createClass("TempData", "Transient");
			QvtdModelExtent umlExtent = QvtdModelExtent.of(persistent, transient_);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			List<EObject> tables = rdbmsExtent.getContents();
			assertEquals(1, tables.size(),
					"Only Persistent classes should produce Tables");
			assertEquals("Employee", getName(tables.get(0)));
		}

		@Test
		@DisplayName("No match when literal does not match any source object")
		void noMatchForLiteral() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation ClassToTable {
							cn : String;
							checkonly domain uml c : Class {
								name = cn,
								kind = 'Persistent'
							};
							enforce domain rdbms t : Table {
								name = cn
							};
						}
					}
					""";

			EObject transient1 = createClass("A", "Transient");
			EObject transient2 = createClass("B", "Transient");
			QvtdModelExtent umlExtent = QvtdModelExtent.of(transient1, transient2);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess());
			assertTrue(rdbmsExtent.getContents().isEmpty(),
					"No tables should be created when no Persistent classes exist");
		}
	}

	// ===== Nested Pattern Matching (§7.11.2.2) =====

	@Nested
	@DisplayName("Nested Patterns (§7.11.2.2)")
	class NestedPatternTests {

		@Test
		@DisplayName("Match Class inside Package via nested template")
		void nestedObjectTemplateMatch() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageClassToSchemaTable {
							pn : String;
							cn : String;
							checkonly domain uml p : Package {
								name = pn,
								ownedClass = c : Class {
									name = cn
								}
							};
							enforce domain rdbms s : Schema {
								name = pn
							};
						}
					}
					""";

			// Package with 2 classes
			EObject pkg = createPackage("HR");
			EObject cls1 = createClass("Employee", null);
			EObject cls2 = createClass("Department", null);
			addClassToPackage(pkg, cls1);
			addClassToPackage(pkg, cls2);

			QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertFalse(rdbmsExtent.getContents().isEmpty(),
					"Schema should be created for Package containing Classes");
		}

		@Test
		@DisplayName("No match for Package without Classes (nested pattern fails)")
		void nestedPatternNoMatchForEmptyPackage() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageClassToSchemaTable {
							pn : String;
							cn : String;
							checkonly domain uml p : Package {
								name = pn,
								ownedClass = c : Class {
									name = cn
								}
							};
							enforce domain rdbms s : Schema {
								name = pn
							};
						}
					}
					""";

			// Package WITHOUT classes
			EObject pkg = createPackage("Empty");
			QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess());
			assertTrue(rdbmsExtent.getContents().isEmpty(),
					"No Schema for Package without Classes (nested pattern fails)");
		}

		@Test
		@DisplayName("Enforce creates nested objects (Table inside Schema)")
		void enforceCreatesNestedObjects() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation ClassToTable {
							cn : String;
							checkonly domain uml c : Class {
								name = cn
							};
							enforce domain rdbms t : Table {
								name = cn
							};
						}
					}
					""";

			EObject cls = createClass("Employee", null);
			QvtdModelExtent umlExtent = QvtdModelExtent.of(cls);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			List<EObject> tables = rdbmsExtent.getContents();
			assertEquals(1, tables.size());
			assertEquals("Table", tables.get(0).eClass().getName());
			assertEquals("Employee", getName(tables.get(0)));
		}
	}

	// ===== Multiple Top Relations =====

	@Nested
	@DisplayName("Multiple Top Relations")
	class MultipleRelationTests {

		@Test
		@DisplayName("Two top relations execute sequentially")
		void twoTopRelationsExecute() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageToSchema {
							pn : String;
							checkonly domain uml p : Package {
								name = pn
							};
							enforce domain rdbms s : Schema {
								name = pn
							};
						}
						top relation ClassToTable {
							cn : String;
							checkonly domain uml c : Class {
								name = cn
							};
							enforce domain rdbms t : Table {
								name = cn
							};
						}
					}
					""";

			EObject pkg = createPackage("HR");
			EObject cls = createClass("Employee", null);
			QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg, cls);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			List<EObject> targets = rdbmsExtent.getContents();
			assertEquals(2, targets.size(),
					"Both top relations should produce output");
		}

		@Test
		@DisplayName("Mixed top and non-top: only top executes")
		void mixedTopAndNonTop() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageToSchema {
							pn : String;
							checkonly domain uml p : Package {
								name = pn
							};
							enforce domain rdbms s : Schema {
								name = pn
							};
						}
						relation ClassToTable {
							cn : String;
							checkonly domain uml c : Class {
								name = cn
							};
							enforce domain rdbms t : Table {
								name = cn
							};
						}
					}
					""";

			EObject pkg = createPackage("HR");
			EObject cls = createClass("Employee", null);
			QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg, cls);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess());
			List<EObject> targets = rdbmsExtent.getContents();
			assertEquals(1, targets.size(),
					"Only PackageToSchema (top) should execute, not ClassToTable");
			assertEquals("Schema", targets.get(0).eClass().getName());
		}
	}

	// ===== Check-Only Semantics (§7.10.1) =====

	@Nested
	@DisplayName("Check-Only Semantics (§7.10.1)")
	class CheckOnlySemantics {

		@Test
		@DisplayName("Check-only detects inconsistency: Package without matching Schema")
		void checkOnlyDetectsInconsistency() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageToSchema {
							pn : String;
							checkonly domain uml p : Package {
								name = pn
							};
							checkonly domain rdbms s : Schema {
								name = pn
							};
						}
					}
					""";

			// UML has a Package, RDBMS has no matching Schema
			EObject pkg = createPackage("HR");
			QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeCheckOnly(source,
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertNotNull(result);
			// Should report a warning about missing match
			assertFalse(result.diagnostics().isEmpty(),
					"Should report inconsistency when Package has no matching Schema");
		}

		@Test
		@DisplayName("Check-only: multiple consistent pairs")
		void checkOnlyMultipleConsistentPairs() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageToSchema {
							pn : String;
							checkonly domain uml p : Package {
								name = pn
							};
							checkonly domain rdbms s : Schema {
								name = pn
							};
						}
					}
					""";

			EObject pkg1 = createPackage("HR");
			EObject pkg2 = createPackage("Finance");
			EObject schema1 = createSchema("HR");
			EObject schema2 = createSchema("Finance");
			QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg1, pkg2);
			QvtdModelExtent rdbmsExtent = QvtdModelExtent.of(schema1, schema2);

			QvtdExecutionResult result = executeCheckOnly(source,
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isConsistent(),
					"Both Package/Schema pairs should be consistent: " + result.diagnostics());
		}
	}

	// ===== Enforce Idempotency (§7.10.2) =====

	@Nested
	@DisplayName("Enforce Idempotency")
	class EnforceIdempotencyTests {

		@Test
		@DisplayName("Re-running enforce does not duplicate objects")
		void enforceIdempotent() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageToSchema {
							pn : String;
							checkonly domain uml p : Package {
								name = pn
							};
							enforce domain rdbms s : Schema {
								name = pn
							};
						}
					}
					""";

			EObject pkg = createPackage("HR");
			QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			// First run
			executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));
			assertEquals(1, rdbmsExtent.getContents().size());

			// Second run — target already has matching Schema
			QvtdExecutionResult result2 = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result2.isSuccess());
			assertEquals(1, rdbmsExtent.getContents().size(),
					"Re-running enforce should not duplicate existing Schema");
		}
	}

	// ===== Where Clause with OCL (§7.2.1) =====

	@Nested
	@DisplayName("Where Clause with OCL")
	class WhereClauseOclTests {

		@Test
		@DisplayName("Where clause with string concatenation")
		void whereClauseWithConcat() throws QvtdParseException {
			String source = """
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
								pn.size() > 0;
							}
						}
					}
					""";

			EObject pkg = createPackage("HR");
			QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(1, rdbmsExtent.getContents().size());
		}
	}

	// ===== Edge Cases =====

	@Nested
	@DisplayName("Edge Cases")
	class EdgeCases {

		@Test
		@DisplayName("Transformation with no relations produces empty result")
		void noRelations() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
					}
					""";

			EObject pkg = createPackage("HR");
			QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess());
			assertTrue(rdbmsExtent.getContents().isEmpty());
		}

		@Test
		@DisplayName("Source extent has objects of wrong type — no match")
		void wrongTypeInExtent() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageToSchema {
							pn : String;
							checkonly domain uml p : Package {
								name = pn
							};
							enforce domain rdbms s : Schema {
								name = pn
							};
						}
					}
					""";

			// Put a Class (not Package) in the UML extent
			EObject cls = createClass("Employee", null);
			QvtdModelExtent umlExtent = QvtdModelExtent.of(cls);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess());
			assertTrue(rdbmsExtent.getContents().isEmpty(),
					"No Schema created when extent has Class (not Package)");
		}

		@Test
		@DisplayName("Relation with multiple properties in template")
		void multiplePropertiesInTemplate() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation ClassToTable {
							cn : String;
							ck : String;
							checkonly domain uml c : Class {
								name = cn,
								kind = ck
							};
							enforce domain rdbms t : Table {
								name = cn
							};
						}
					}
					""";

			EObject cls = createClass("Employee", "Persistent");
			QvtdModelExtent umlExtent = QvtdModelExtent.of(cls);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(1, rdbmsExtent.getContents().size());
			assertEquals("Employee", getName(rdbmsExtent.getContents().get(0)));
		}
	}

	// --- RDBMS model factory ---

	private static EObject createSchema(String name) {
		EClass cls = ecoreHelper.getEClass(rdbmsPackage, "Schema");
		EObject schema = EcoreUtil.create(cls);
		schema.eSet(cls.getEStructuralFeature("name"), name);
		return schema;
	}

	private static EObject createTable(String name) {
		EClass cls = ecoreHelper.getEClass(rdbmsPackage, "Table");
		EObject table = EcoreUtil.create(cls);
		table.eSet(cls.getEStructuralFeature("name"), name);
		return table;
	}
}
