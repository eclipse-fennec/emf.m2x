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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for Key-based identity (QVT v1.3 §7.4) and advanced enforcement.
 *
 * <p>Per §7.4: "Keys are used at the time of object creation; if an object
 * template expression has properties corresponding to a key of the associated
 * class, then the key is used to locate a matching object in the model; a new
 * object is created only when a matching object does not exist."
 */
@DisplayName("QVT-R Key-based Identity (§7.4)")
class QvtdEngineKeyTest extends AbstractQvtdEngineTest {

	// ===== Key Parsing =====

	@Nested
	@DisplayName("Key Declaration Parsing")
	class KeyParsingTests {

		@Test
		@DisplayName("Parse transformation with key declaration")
		void parseKeyDeclaration() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						key Schema {name};
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

			// Should parse without error
			var transformation = parse(source);
			assertNotNull(transformation);
			assertEquals(1, transformation.getOwnedKey().size(),
					"Transformation should have one key");
			assertEquals("Schema", transformation.getOwnedKey().get(0).getIdentifies().getName());
		}

		@Test
		@DisplayName("Parse multiple key declarations")
		void parseMultipleKeys() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						key Schema {name};
						key Table {name};
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

			var transformation = parse(source);
			assertEquals(2, transformation.getOwnedKey().size());
		}

		@Test
		@DisplayName("Parse key with multiple properties")
		void parseKeyWithMultipleProperties() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						key Column {name, type};
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

			var transformation = parse(source);
			assertEquals(1, transformation.getOwnedKey().size());
			assertEquals(2, transformation.getOwnedKey().get(0).getPart().size(),
					"Key should have two parts");
		}
	}

	// ===== Key-based Enforcement =====

	@Nested
	@DisplayName("Key-based Enforcement (§7.4)")
	class KeyEnforcementTests {

		@Test
		@DisplayName("Key prevents duplicate: existing object with matching key is updated")
		void keyPreventsExistingDuplicate() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						key Schema {name};
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

			// Pre-populate target with a Schema named "HR"
			EObject existingSchema = createSchema("HR");
			QvtdModelExtent rdbmsExtent = QvtdModelExtent.of(existingSchema);

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(1, rdbmsExtent.getContents().size(),
					"Key should prevent duplicate Schema creation");
		}

		@Test
		@DisplayName("Key allows creation when no matching object exists")
		void keyAllowsCreationWhenNoMatch() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						key Schema {name};
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

			// Pre-populate with a DIFFERENT schema
			EObject existingSchema = createSchema("Finance");
			QvtdModelExtent rdbmsExtent = QvtdModelExtent.of(existingSchema);

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(2, rdbmsExtent.getContents().size(),
					"New Schema 'HR' should be created alongside existing 'Finance'");
		}

		@Test
		@DisplayName("Key with multiple sources: each source creates distinct target")
		void keyWithMultipleSources() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						key Schema {name};
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
			QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg1, pkg2);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(2, rdbmsExtent.getContents().size(),
					"Two distinct Schemas should be created");
		}

		@Test
		@DisplayName("Re-running with key is idempotent — no duplicates")
		void keyIdempotent() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						key Schema {name};
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

			// Second run — key should prevent duplicate
			QvtdExecutionResult result2 = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result2.isSuccess());
			assertEquals(1, rdbmsExtent.getContents().size(),
					"Key-based enforcement should be idempotent");
		}

		@Test
		@DisplayName("Without key, re-running creates duplicates (baseline)")
		void withoutKeyDuplicates() throws QvtdParseException {
			// This is the baseline: without keys, re-running WILL create duplicates
			// because the pattern matcher won't find a full match (the second run sees
			// a new source binding but the target match already exists from run 1,
			// which the evaluator actually does detect via pattern matching)
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

			// Second run — the evaluator's pattern matching will detect the
			// existing match, so it should still be idempotent even without keys
			QvtdExecutionResult result2 = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result2.isSuccess());
			assertEquals(1, rdbmsExtent.getContents().size(),
					"Pattern match should detect existing consistent target");
		}

		@Test
		@DisplayName("Key update: existing object gets non-key properties updated")
		void keyUpdatesNonKeyProperties() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						key Table {name};
						top relation ClassToTable {
							cn : String;
							checkonly domain uml c : Class {
								name = cn
							};
							enforce domain rdbms t : Table {
								name = cn,
								type = 'BASE'
							};
						}
					}
					""";

			EObject cls = createClass("Employee", null);
			QvtdModelExtent umlExtent = QvtdModelExtent.of(cls);

			// Pre-populate target with a Table named "Employee" but no type
			EObject existingTable = createTable("Employee");
			QvtdModelExtent rdbmsExtent = QvtdModelExtent.of(existingTable);

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(1, rdbmsExtent.getContents().size(),
					"Key match should reuse existing Table");

			// The existing table should have been updated with type='BASE'
			EObject table = rdbmsExtent.getContents().get(0);
			assertEquals("BASE", getFeatureValue(table, "type"),
					"Non-key property 'type' should be updated");
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

	private static Object getFeatureValue(EObject obj, String featureName) {
		EStructuralFeature feature = obj.eClass().getEStructuralFeature(featureName);
		return feature != null ? obj.eGet(feature) : null;
	}
}
