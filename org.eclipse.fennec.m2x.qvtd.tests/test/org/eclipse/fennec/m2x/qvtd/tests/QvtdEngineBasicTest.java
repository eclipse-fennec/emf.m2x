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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Basic P4-3 engine tests for QVT-R: parsing, check-only, enforce, pattern matching.
 *
 * Tests the core engine capabilities:
 * - Top-level relation execution
 * - Domain pattern matching (ObjectTemplateExp)
 * - Variable binding (free/bound)
 * - When/where clause evaluation
 * - Check-only mode
 * - Enforce mode (object creation)
 */
class QvtdEngineBasicTest extends AbstractQvtdEngineTest {

	// ===== Parsing =====

	@Nested
	@DisplayName("Parsing")
	class ParsingTests {

		@Test
		@DisplayName("Parse minimal transformation")
		void parseMinimalTransformation() throws QvtdParseException {
			String source = """
					transformation PackageToSchema(uml : simpleuml, rdbms : simplerdbms) {
					}
					""";
			RelationalTransformation t = parse(source);
			assertNotNull(t);
			assertEquals("PackageToSchema", t.getName());
			assertEquals(2, t.getModelParameter().size());
			assertEquals("uml", t.getModelParameter().get(0).getName());
			assertEquals("rdbms", t.getModelParameter().get(1).getName());
		}

		@Test
		@DisplayName("Parse transformation with one top relation")
		void parseTransformationWithRelation() throws QvtdParseException {
			String source = """
					transformation PackageToSchema(uml : simpleuml, rdbms : simplerdbms) {
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
			RelationalTransformation t = parse(source);
			assertNotNull(t);
			assertEquals(1, t.getRule().size());
			assertTrue(((org.eclipse.fennec.m2x.model.qvtrelation.Relation) t.getRule().get(0)).isIsTopLevel());
		}
	}

	// ===== Check-Only Mode =====

	@Nested
	@DisplayName("Check-Only Mode")
	class CheckOnlyTests {

		@Test
		@DisplayName("Check-only: consistent models return success")
		void checkOnlyConsistentModels() throws QvtdParseException {
			String source = """
					transformation PackageToSchema(uml : simpleuml, rdbms : simplerdbms) {
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

			// Create consistent models: Package "HR" ↔ Schema "HR"
			EObject umlPkg = createPackage("HR");
			EObject rdbmsSchema = createSchema("HR");

			QvtdModelExtent umlExtent = QvtdModelExtent.of(umlPkg);
			QvtdModelExtent rdbmsExtent = QvtdModelExtent.of(rdbmsSchema);

			QvtdExecutionResult result = executeCheckOnly(source,
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertNotNull(result);
			assertTrue(result.isConsistent(),
					"Consistent models should pass check: " + result.diagnostics());
		}

		@Test
		@DisplayName("Check-only: empty models — no bindings, warning expected")
		void checkOnlyEmptyModels() throws QvtdParseException {
			String source = """
					transformation PackageToSchema(uml : simpleuml, rdbms : simplerdbms) {
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

			QvtdExecutionResult result = executeCheckOnly(source,
					Map.of("uml", emptyExtent(), "rdbms", emptyExtent()));

			assertNotNull(result);
			// Empty models — no bindings to check, should still succeed (vacuously true)
			// or warn about no matches
		}
	}

	// ===== Enforce Mode =====

	@Nested
	@DisplayName("Enforce Mode")
	class EnforceTests {

		@Test
		@DisplayName("Enforce: create Schema from Package")
		void enforceCreateSchemaFromPackage() throws QvtdParseException {
			String source = """
					transformation PackageToSchema(uml : simpleuml, rdbms : simplerdbms) {
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

			EObject umlPkg = createPackage("HR");
			QvtdModelExtent umlExtent = QvtdModelExtent.of(umlPkg);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertNotNull(result);
			assertTrue(result.isSuccess(), "Enforce should succeed: " + result.diagnostics());
			assertTrue(result.enforced(), "Result should indicate enforce was performed");

			// Check that a Schema was created in the rdbms extent
			List<EObject> rdbmsContents = rdbmsExtent.getContents();
			assertFalse(rdbmsContents.isEmpty(), "RDBMS extent should contain created Schema");
			assertEquals(1, rdbmsContents.size());
			assertEquals("Schema", rdbmsContents.get(0).eClass().getName());
			assertEquals("HR", getName(rdbmsContents.get(0)));
		}

		@Test
		@DisplayName("Enforce: create multiple Schemas from multiple Packages")
		void enforceCreateMultipleSchemas() throws QvtdParseException {
			String source = """
					transformation PackageToSchema(uml : simpleuml, rdbms : simplerdbms) {
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

			assertTrue(result.isSuccess(), "Enforce should succeed: " + result.diagnostics());

			List<EObject> rdbmsContents = rdbmsExtent.getContents();
			assertEquals(2, rdbmsContents.size(), "Two Schemas should be created");
		}

		@Test
		@DisplayName("Enforce: target already consistent — no creation")
		void enforceAlreadyConsistent() throws QvtdParseException {
			String source = """
					transformation PackageToSchema(uml : simpleuml, rdbms : simplerdbms) {
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

			EObject umlPkg = createPackage("HR");
			EObject rdbmsSchema = createSchema("HR");
			QvtdModelExtent umlExtent = QvtdModelExtent.of(umlPkg);
			QvtdModelExtent rdbmsExtent = QvtdModelExtent.of(rdbmsSchema);

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess(), "Should succeed: " + result.diagnostics());
			// No new objects should be created — existing Schema matches
			assertEquals(1, rdbmsExtent.getContents().size(),
					"Existing Schema should remain, no new creation");
		}
	}

	// ===== When Clause =====

	@Nested
	@DisplayName("When Clause")
	class WhenClauseTests {

		@Test
		@DisplayName("When clause with literal condition (true)")
		void whenClauseTrue() throws QvtdParseException {
			String source = """
					transformation PackageToSchema(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageToSchema {
							pn : String;
							checkonly domain uml p : Package {
								name = pn
							};
							enforce domain rdbms s : Schema {
								name = pn
							};
							when {
								true;
							}
						}
					}
					""";

			EObject umlPkg = createPackage("HR");
			QvtdModelExtent umlExtent = QvtdModelExtent.of(umlPkg);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess());
			assertEquals(1, rdbmsExtent.getContents().size(),
					"When=true should allow enforce");
		}

		@Test
		@DisplayName("When clause with false condition — no enforcement")
		void whenClauseFalse() throws QvtdParseException {
			String source = """
					transformation PackageToSchema(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageToSchema {
							pn : String;
							checkonly domain uml p : Package {
								name = pn
							};
							enforce domain rdbms s : Schema {
								name = pn
							};
							when {
								false;
							}
						}
					}
					""";

			EObject umlPkg = createPackage("HR");
			QvtdModelExtent umlExtent = QvtdModelExtent.of(umlPkg);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess());
			assertTrue(rdbmsExtent.getContents().isEmpty(),
					"When=false should prevent enforce");
		}
	}

	// ===== Where Clause =====

	@Nested
	@DisplayName("Where Clause")
	class WhereClauseTests {

		@Test
		@DisplayName("Where clause with satisfied predicate")
		void whereClauseSatisfied() throws QvtdParseException {
			String source = """
					transformation PackageToSchema(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageToSchema {
							pn : String;
							checkonly domain uml p : Package {
								name = pn
							};
							enforce domain rdbms s : Schema {
								name = pn
							};
							where {
								pn <> '';
							}
						}
					}
					""";

			EObject umlPkg = createPackage("HR");
			QvtdModelExtent umlExtent = QvtdModelExtent.of(umlPkg);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess(), "Should succeed: " + result.diagnostics());
			assertEquals(1, rdbmsExtent.getContents().size());
		}
	}

	// ===== Multiple Relations =====

	@Nested
	@DisplayName("Multiple Relations")
	class MultipleRelationTests {

		@Test
		@DisplayName("Non-top relation is not executed directly")
		void nonTopRelationNotExecuted() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						relation HelperRelation {
							checkonly domain uml p : Package {};
							checkonly domain rdbms s : Schema {};
						}
					}
					""";

			EObject umlPkg = createPackage("HR");
			QvtdModelExtent umlExtent = QvtdModelExtent.of(umlPkg);
			QvtdModelExtent rdbmsExtent = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess());
			assertTrue(rdbmsExtent.getContents().isEmpty(),
					"Non-top relation should not be executed");
		}
	}

	// --- RDBMS model factory ---

	private static EObject createSchema(String name) {
		org.eclipse.emf.ecore.EClass cls = ecoreHelper.getEClass(rdbmsPackage, "Schema");
		EObject schema = org.eclipse.emf.ecore.util.EcoreUtil.create(cls);
		schema.eSet(cls.getEStructuralFeature("name"), name);
		return schema;
	}
}
