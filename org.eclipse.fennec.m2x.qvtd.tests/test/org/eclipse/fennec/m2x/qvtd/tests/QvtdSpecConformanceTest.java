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
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.BasicQvtdBlackboxRegistry;
import org.eclipse.fennec.m2x.qvtd.api.QvtdBlackboxLibrary;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Systematic spec-conformance tests for QVT-R v1.3.
 * Each nested class covers a specific spec section.
 *
 * <p>Tests marked {@code @Disabled} document known implementation gaps.
 */
@DisplayName("QVT-R Spec Conformance (v1.3)")
class QvtdSpecConformanceTest extends AbstractQvtdEngineTest {

	// ===== §7.2.1: When-clause Semantics =====

	@Nested
	@DisplayName("§7.2.1: When-clause Semantics")
	class WhenClauseSemantics {

		@Test
		@DisplayName("When-clause with true literal → relation executes")
		void whenTrueLiteral() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
							when { true; }
						}
					}
					""";

			QvtdModelExtent uml = QvtdModelExtent.of(createPackage("X"));
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess());
			assertEquals(1, rdbms.getContents().size());
		}

		@Test
		@DisplayName("When-clause with false literal → relation skipped")
		void whenFalseLiteral() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
							when { false; }
						}
					}
					""";

			QvtdModelExtent uml = QvtdModelExtent.of(createPackage("X"));
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess());
			assertTrue(rdbms.getContents().isEmpty(),
					"Relation should not execute when when-clause is false");
		}

		@Test
		@DisplayName("No when-clause → relation always executes")
		void noWhenClause() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
					}
					""";

			QvtdModelExtent uml = QvtdModelExtent.of(createPackage("X"));
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess());
			assertEquals(1, rdbms.getContents().size());
		}
	}

	// ===== §7.2.2: Top vs Non-top Execution Order =====

	@Nested
	@DisplayName("§7.2.2: Top vs Non-top Relations")
	class TopNonTopSemantics {

		@Test
		@DisplayName("Only top-level relations are executed directly")
		void onlyTopRelationsExecuted() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation TopR {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
						relation NonTopR {
							cn : String;
							checkonly domain uml c : Class { name = cn };
							enforce domain rdbms t : Table { name = cn };
						}
					}
					""";

			QvtdModelExtent uml = QvtdModelExtent.of(
					createPackage("Pkg"), createClass("Cls", null));
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess());

			Set<String> types = rdbms.getContents().stream()
					.map(o -> o.eClass().getName())
					.collect(Collectors.toSet());
			assertTrue(types.contains("Schema"), "Top relation should execute");
			assertFalse(types.contains("Table"),
					"Non-top relation should NOT execute directly");
		}

		@Test
		@DisplayName("Top-level flag correctly parsed")
		void topLevelFlagParsed() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation Alpha {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
						relation Beta {
							cn : String;
							checkonly domain uml c : Class { name = cn };
							enforce domain rdbms t : Table { name = cn };
						}
					}
					""";

			RelationalTransformation t = parse(source);
			List<Relation> relations = t.getRule().stream()
					.filter(Relation.class::isInstance)
					.map(Relation.class::cast)
					.toList();

			assertEquals(2, relations.size());
			Relation alpha = relations.stream()
					.filter(r -> "Alpha".equals(r.getName())).findFirst().orElseThrow();
			Relation beta = relations.stream()
					.filter(r -> "Beta".equals(r.getName())).findFirst().orElseThrow();

			assertTrue(alpha.isIsTopLevel(), "Alpha should be top-level");
			assertFalse(beta.isIsTopLevel(), "Beta should NOT be top-level");
		}
	}

	// ===== §7.3: Pattern Matching Semantics =====

	@Nested
	@DisplayName("§7.3: Pattern Matching")
	class PatternMatchingSemantics {

		@Test
		@DisplayName("Literal value filtering: only matching elements pass")
		void literalValueFiltering() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation PersistentClassToTable {
							cn : String;
							checkonly domain uml c : Class {
								name = cn,
								kind = 'Persistent'
							};
							enforce domain rdbms t : Table { name = cn };
						}
					}
					""";

			QvtdModelExtent uml = QvtdModelExtent.of(
					createClass("Good", "Persistent"),
					createClass("Bad", "Transient"),
					createClass("Also", "Persistent"));
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess());

			Set<String> tableNames = rdbms.getContents().stream()
					.map(o -> getName(o))
					.collect(Collectors.toSet());
			assertEquals(Set.of("Good", "Also"), tableNames,
					"Only Persistent classes should produce tables");
		}

		@Test
		@DisplayName("Free variable binding: variable bound from model value")
		void freeVariableBinding() throws QvtdParseException {
			// cn is free, gets bound to each Class's name
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							cn : String;
							checkonly domain uml c : Class { name = cn };
							enforce domain rdbms t : Table { name = cn };
						}
					}
					""";

			QvtdModelExtent uml = QvtdModelExtent.of(
					createClass("A", null), createClass("B", null));
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess());
			Set<String> names = rdbms.getContents().stream()
					.map(o -> getName(o))
					.collect(Collectors.toSet());
			assertEquals(Set.of("A", "B"), names,
					"Variable should bind to each element's name");
		}

		@Test
		@DisplayName("Nested pattern: multi-level template recursion")
		void nestedPatternRecursion() throws QvtdParseException {
			// Package → Class → Attribute: three-level nesting
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							cn : String;
							an : String;
							checkonly domain uml p : Package {
								name = pn,
								ownedClass = c : Class {
									name = cn,
									ownedAttribute = a : Attribute {
										name = an
									}
								}
							};
							enforce domain rdbms s : Schema { name = an };
						}
					}
					""";

			EObject pkg = createPackage("P");
			EObject cls = createClass("C", null);
			EObject attr = createAttribute("myAttr", "String");
			addAttributeToClass(cls, attr);
			addClassToPackage(pkg, cls);

			QvtdModelExtent uml = QvtdModelExtent.of(pkg);
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(1, rdbms.getContents().size());
			assertEquals("myAttr", getName(rdbms.getContents().get(0)),
					"Deeply nested variable should bind correctly");
		}

		@Test
		@DisplayName("Multi-valued reference produces multiple bindings")
		void multiValuedReferenceBindings() throws QvtdParseException {
			// Package with two classes → two separate matches
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							cn : String;
							checkonly domain uml p : Package {
								name = pn,
								ownedClass = c : Class { name = cn }
							};
							enforce domain rdbms s : Schema { name = cn };
						}
					}
					""";

			EObject pkg = createPackage("P");
			addClassToPackage(pkg, createClass("X", null));
			addClassToPackage(pkg, createClass("Y", null));

			QvtdModelExtent uml = QvtdModelExtent.of(pkg);
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			Set<String> names = rdbms.getContents().stream()
					.map(o -> getName(o))
					.collect(Collectors.toSet());
			assertEquals(Set.of("X", "Y"), names,
					"Each class in multi-valued ref should produce a separate match");
		}
	}

	// ===== §7.4: Key Declarations =====

	@Nested
	@DisplayName("§7.4: Key Declarations")
	class KeyDeclarationSemantics {

		@Test
		@DisplayName("Key prevents duplicate creation")
		void keyPreventsDuplicates() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						key simplerdbms::Table {name};
						top relation R {
							cn : String;
							checkonly domain uml c : Class { name = cn };
							enforce domain rdbms t : Table { name = cn };
						}
					}
					""";

			QvtdModelExtent uml = QvtdModelExtent.of(
					createClass("Same", null), createClass("Same", null));
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess());
			assertEquals(1, rdbms.getContents().size(),
					"Key should prevent duplicate Table with same name");
		}

		@Test
		@DisplayName("Without key: enforcement still finds existing match (§7.10.2)")
		void withoutKeyEnforcementFindsExisting() throws QvtdParseException {
			// Two source objects with same name. Without key, enforcement still
			// checks if the target pattern is satisfied before creating —
			// second source finds existing Table("Same") → no duplicate.
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							cn : String;
							checkonly domain uml c : Class { name = cn };
							enforce domain rdbms t : Table { name = cn };
						}
					}
					""";

			QvtdModelExtent uml = QvtdModelExtent.of(
					createClass("Same", null), createClass("Same", null));
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess());
			assertEquals(1, rdbms.getContents().size(),
					"Enforcement finds existing Table match for second binding (§7.10.2)");
		}
	}

	// ===== §7.10.1: Check Semantics =====

	@Nested
	@DisplayName("§7.10.1: Check Semantics")
	class CheckSemantics {

		@Test
		@DisplayName("Check-only: consistent models produce no errors")
		void checkOnlyConsistent() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							n : String;
							checkonly domain uml p : Package { name = n };
							checkonly domain rdbms s : Schema { name = n };
						}
					}
					""";

			QvtdModelExtent uml = QvtdModelExtent.of(createPackage("HR"));
			QvtdModelExtent rdbms = QvtdModelExtent.of(createSchema("HR"));

			QvtdExecutionResult result = executeCheckOnly(source,
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess());
		}

		@Test
		@DisplayName("Check-only: no modifications to models")
		void checkOnlyNoModification() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
					}
					""";

			QvtdModelExtent uml = QvtdModelExtent.of(createPackage("HR"));
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeCheckOnly(source,
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess());
			assertTrue(rdbms.getContents().isEmpty(),
					"Check-only should NOT create objects");
		}
	}

	// ===== §7.10.2: Enforcement Semantics =====

	@Nested
	@DisplayName("§7.10.2: Enforcement Semantics")
	class EnforcementSemantics {

		@Test
		@DisplayName("Enforce creates missing target objects")
		void enforceCreatesMissingTarget() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
					}
					""";

			QvtdModelExtent uml = QvtdModelExtent.of(createPackage("HR"));
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess());
			assertEquals(1, rdbms.getContents().size());
			assertEquals("HR", getName(rdbms.getContents().get(0)));
		}

		@Test
		@DisplayName("Enforce does not duplicate already-consistent target")
		void enforceDoesNotDuplicateConsistent() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
					}
					""";

			QvtdModelExtent uml = QvtdModelExtent.of(createPackage("HR"));
			EObject existingSchema = createSchema("HR");
			QvtdModelExtent rdbms = QvtdModelExtent.of(existingSchema);

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess());
			assertEquals(1, rdbms.getContents().size(),
					"Should not duplicate already-consistent Schema");
		}

		@Test
		@DisplayName("Enforce with nested pattern creates containment hierarchy")
		void enforceCreatesContainmentHierarchy() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							cn : String;
							checkonly domain uml p : Package {
								name = pn,
								ownedClass = c : Class { name = cn }
							};
							enforce domain rdbms s : Schema {
								name = pn,
								ownedTable = t : Table { name = cn }
							};
						}
					}
					""";

			EObject pkg = createPackage("HR");
			addClassToPackage(pkg, createClass("Employee", null));

			QvtdModelExtent uml = QvtdModelExtent.of(pkg);
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(1, rdbms.getContents().size(),
					"Only Schema should be root (Table contained)");

			EObject schema = rdbms.getContents().get(0);
			var tables = getTables(schema);
			assertEquals(1, tables.size());
			assertEquals("Employee", getName(tables.get(0)));
		}
	}

	// ===== §7.11.3.8: RelationCallExp =====

	@Nested
	@DisplayName("§7.11.3.8: RelationCallExp")
	class RelationCallExpSemantics {

		@Test
		@DisplayName("RelationCallExp argument count matches domain root variables")
		void argumentCountMatchesDomains() throws QvtdParseException {
			// PackageToSchema has 2 domains → RelationCallExp needs 2 arguments
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageToSchema {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
						top relation ClassToTable {
							cn : String;
							checkonly domain uml c : Class { name = cn };
							enforce domain rdbms t : Table { name = cn };
							when { PackageToSchema(p, s); }
						}
					}
					""";

			RelationalTransformation t = parse(source);
			Relation classToTable = t.getRule().stream()
					.filter(Relation.class::isInstance)
					.map(Relation.class::cast)
					.filter(r -> "ClassToTable".equals(r.getName()))
					.findFirst().orElseThrow();

			assertNotNull(classToTable.getWhen());
			assertFalse(classToTable.getWhen().getPredicate().isEmpty());
		}
	}

	// ===== Known Gaps (deferred features) =====

	@Nested
	@DisplayName("Known Gaps (deferred)")
	class KnownGaps {

		@Test
		@DisplayName("Primitive domain: variable domain for String parameter")
		void primitiveDomain() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
							where { Sub(p, s, pn); }
						}
						relation Sub {
							cn : String;
							checkonly domain uml p : Package {
								ownedClass = c : Class { name = cn }
							};
							enforce domain rdbms s : Schema {
								ownedTable = t : Table { name = cn }
							};
							primitive domain prefix : String;
						}
					}
					""";

			EObject pkg = createPackage("HR");
			addClassToPackage(pkg, createClass("Emp", null));
			QvtdModelExtent uml = QvtdModelExtent.of(pkg);
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(1, rdbms.getContents().size(), "One Schema");
			EObject schema = rdbms.getContents().get(0);
			var tables = getTables(schema);
			assertEquals(1, tables.size(), "Sub relation should create Table");
			assertEquals("Emp", getName(tables.get(0)));
		}

		@Test
		@DisplayName("Query invocation in where-clause")
		void queryInWhere() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						query TypeMap(umlType : String) : String {
							if umlType = 'Integer' then 'NUMBER'
							else 'VARCHAR'
							endif
						}

						top relation R {
							an : String;
							atype : String;
							sqltype : String;
							checkonly domain uml c : Class {
								ownedAttribute = a : Attribute {
									name = an,
									type = atype
								}
							};
							enforce domain rdbms t : Table {
								ownedColumn = col : Column {
									name = an,
									type = sqltype
								}
							};
							where {
								sqltype = TypeMap(atype);
							}
						}
					}
					""";

			EObject cls = createClass("Emp", null);
			addAttributeToClass(cls, createAttribute("salary", "Integer"));

			QvtdModelExtent uml = QvtdModelExtent.of(cls);
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess());
			EObject table = rdbms.getContents().get(0);
			var cols = getColumns(table);
			assertEquals("NUMBER", cols.get(0).eGet(
					cols.get(0).eClass().getEStructuralFeature("type")));
		}

		@Test
		@DisplayName("CollectionTemplateExp in domain pattern (§7.11.2.3)")
		void collectionTemplateExp() throws QvtdParseException {
			// CollectionTemplateExp with member ObjectTemplateExp + rest wildcard.
			// Each member match produces a separate binding (like nested template).
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						key simplerdbms::Table {name};
						top relation R {
							cn : String;
							checkonly domain uml p : Package {
								ownedClass = : Set(Class) { c : Class { name = cn, kind = 'Persistent' } ++ _ }
							};
							enforce domain rdbms t : Table { name = cn };
						}
					}
					""";

			EObject pkg = createPackage("HR");
			addClassToPackage(pkg, createClass("Employee", "Persistent"));
			addClassToPackage(pkg, createClass("TempData", "Transient"));
			addClassToPackage(pkg, createClass("Department", "Persistent"));

			QvtdModelExtent uml = QvtdModelExtent.of(pkg);
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess(), "" + result.diagnostics());

			Set<String> tableNames = rdbms.getContents().stream()
					.map(t -> getName(t))
					.collect(Collectors.toSet());
			assertEquals(Set.of("Employee", "Department"), tableNames,
					"Only Persistent classes matched by CollectionTemplateExp member");
		}

		@Test
		@DisplayName("Relation overrides base relation (§7.6)")
		void relationOverrides() throws QvtdParseException {
			// BaseR maps Package→Schema with name unchanged.
			// OverrideR overrides BaseR and uppercases the name.
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation BaseR {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
						top relation OverrideR overrides BaseR {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = 'OVERRIDDEN' };
						}
					}
					""";

			QvtdModelExtent uml = QvtdModelExtent.of(createPackage("test"));
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(1, rdbms.getContents().size());
			assertEquals("OVERRIDDEN", getName(rdbms.getContents().get(0)),
					"Override should execute instead of base relation");
		}
	}

	// ===== §7.11.3.7: Default Value Assignments =====

	@Nested
	@DisplayName("§7.11.3.7: Default Value Assignments")
	class DefaultValueAssignments {

		@Test
		@DisplayName("Default value used when variable is unbound")
		void defaultValueForUnboundVariable() throws QvtdParseException {
			// Attribute type is not mapped in pattern → default_values provides fallback
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							cn : String;
							coltype : String;
							checkonly domain uml c : Class {
								name = cn
							};
							enforce domain rdbms t : Table {
								ownedColumn = col : Column {
									name = cn,
									type = coltype
								}
							}
							default_values {
								coltype = 'VARCHAR';
							};
						}
					}
					""";

			QvtdModelExtent uml = QvtdModelExtent.of(createClass("Employee", null));
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			EObject table = rdbms.getContents().get(0);
			var cols = getColumns(table);
			assertEquals(1, cols.size());
			assertEquals("VARCHAR", getType(cols.get(0)),
					"Default value should be used for unbound coltype");
		}

		@Test
		@DisplayName("Default value NOT used when variable is already bound")
		void defaultValueNotUsedWhenBound() throws QvtdParseException {
			// atype is bound from source pattern → default should NOT override
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							cn : String;
							atype : String;
							checkonly domain uml c : Class {
								name = cn,
								ownedAttribute = a : Attribute {
									type = atype
								}
							};
							enforce domain rdbms t : Table {
								ownedColumn = col : Column {
									name = cn,
									type = atype
								}
							}
							default_values {
								atype = 'DEFAULT_TYPE';
							};
						}
					}
					""";

			EObject cls = createClass("Emp", null);
			addAttributeToClass(cls, createAttribute("id", "INTEGER"));

			QvtdModelExtent uml = QvtdModelExtent.of(cls);
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			EObject table = rdbms.getContents().get(0);
			var cols = getColumns(table);
			assertEquals("INTEGER", getType(cols.get(0)),
					"Default should NOT override already-bound value");
		}
	}

	// ===== §7.6: Abstract Relations =====

	@Nested
	@DisplayName("§7.6: Abstract Relations")
	class AbstractRelationSemantics {

		@Test
		@DisplayName("Abstract top relation is not executed directly")
		void abstractTopRelationSkipped() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top abstract relation AbstractR {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
					}
					""";

			QvtdModelExtent uml = QvtdModelExtent.of(createPackage("HR"));
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess());
			assertTrue(rdbms.getContents().isEmpty(),
					"Abstract relation should not execute directly");
		}

		@Test
		@DisplayName("Abstract relation overridden — override executes")
		void abstractRelationOverridden() throws QvtdParseException {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top abstract relation BaseR {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
						top relation ConcreteR overrides BaseR {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = 'CONCRETE' };
						}
					}
					""";

			QvtdModelExtent uml = QvtdModelExtent.of(createPackage("test"));
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(1, rdbms.getContents().size());
			assertEquals("CONCRETE", getName(rdbms.getContents().get(0)),
					"Override of abstract relation should execute");
		}
	}

	// ===== §7.11.2.4: Opposite Properties =====

	@Nested
	@DisplayName("§7.11.2.4: Opposite Properties")
	class OppositePropertySemantics {

		@Test
		@DisplayName("opposite(Class::prop) navigates property in reverse direction")
		void oppositePropertyNavigation() throws QvtdParseException {
			// opposite(Table::schema) in a Schema template finds Tables
			// whose 'schema' reference points back to the current Schema.
			// This is equivalent to navigating Schema.ownedTable (the eOpposite).
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							sn : String;
							tn : String;
							checkonly domain rdbms s : Schema {
								name = sn,
								opposite(Table::schema) = t : Table { name = tn }
							};
							enforce domain uml p : Package { name = tn };
						}
					}
					""";

			// Build RDBMS model: Schema "HR" containing Table "Employees"
			EObject schema = createSchema("HR");
			EObject table = createTable("Employees");
			addTableToSchema(schema, table);

			QvtdModelExtent rdbms = QvtdModelExtent.of(schema);
			QvtdModelExtent uml = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "uml",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(1, uml.getContents().size());
			assertEquals("Employees", getName(uml.getContents().get(0)),
					"Opposite property should navigate Schema→Table via Table::schema eOpposite");
		}

		@Test
		@DisplayName("opposite in enforce domain sets property on other object")
		void oppositePropertyEnforcement() throws QvtdParseException {
			// When enforcing opposite(Column::owningTable), the engine should
			// resolve the eOpposite (Table.ownedColumn) and add to it.
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							cn : String;
							colName : String;
							checkonly domain uml c : Class {
								name = cn,
								ownedAttribute = a : Attribute { name = colName }
							};
							enforce domain rdbms t : Table {
								name = cn,
								opposite(Column::owningTable) = col : Column { name = colName }
							};
						}
					}
					""";

			EObject cls = createClass("Employee", null);
			addAttributeToClass(cls, createAttribute("salary", null));

			QvtdModelExtent uml = QvtdModelExtent.of(cls);
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(1, rdbms.getContents().size());
			EObject table = rdbms.getContents().get(0);
			assertEquals("Employee", getName(table));
			var cols = getColumns(table);
			assertEquals(1, cols.size(),
					"Opposite enforcement should add Column to Table.ownedColumn");
			assertEquals("salary", getName(cols.get(0)));
		}
	}

	// ===== §7.11.2.3: CollectionTemplateExp Enforcement =====

	@Nested
	@DisplayName("§7.11.2.3: CollectionTemplateExp Enforcement")
	class CollectionTemplateEnforcement {

		@Test
		@DisplayName("CollectionTemplateExp in enforce domain creates collection members")
		void collectionTemplateInEnforceDomain() throws QvtdParseException {
			// CollectionTemplateExp with ObjectTemplateExp members in enforce domain
			// should create each member and add to the collection property.
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							cn : String;
							checkonly domain uml c : Class {
								name = cn,
								ownedAttribute = a : Attribute { name = cn }
							};
							enforce domain rdbms t : Table {
								name = cn,
								ownedColumn = : Set(Column) {
									col : Column { name = cn }
									++ _
								}
							};
						}
					}
					""";

			EObject cls = createClass("Employee", null);
			addAttributeToClass(cls, createAttribute("Employee", null));

			QvtdModelExtent uml = QvtdModelExtent.of(cls);
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(1, rdbms.getContents().size());
			EObject table = rdbms.getContents().get(0);
			assertEquals("Employee", getName(table));
			var cols = getColumns(table);
			assertEquals(1, cols.size(),
					"CollectionTemplateExp enforcement should create Column member");
			assertEquals("Employee", getName(cols.get(0)));
		}
	}

	// ===== §7.13.4: Optional Root Variables =====

	@Nested
	@DisplayName("Optional Root Variables [?] (Eclipse Extension)")
	class OptionalRootVariables {

		@Test
		@DisplayName("[?] optional root variable allows null binding when no match")
		void optionalRootVariableNullBinding() throws QvtdParseException {
			// Eclipse extension: 'c : Class[?] { ... }' allows c to be bound to null
			// when no matching instance exists, instead of failing the match.
			// This enables recursive patterns with null-to-null matching.
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation ClassToTable {
							cn : String;
							checkonly domain uml c : Class[?] {
								name = cn
							};
							enforce domain rdbms t : Table {
								name = cn
							};
						}
					}
					""";

			// No Class instances in the uml extent → [?] allows null binding
			QvtdModelExtent umlExtent = QvtdModelExtent.of();
			QvtdModelExtent rdbmsExtent = QvtdModelExtent.of();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			// With [?], the relation matches with null root variable,
			// but cn is unbound so enforcement cannot set table name
			// → no table created (null-to-null match is vacuously satisfied)
			assertEquals(0, rdbmsExtent.getContents().size(),
					"Optional [?] with no source match should not create target");
		}

		@Test
		@DisplayName("[?] optional root variable matches normally when instances exist")
		void optionalRootVariableNormalMatch() throws QvtdParseException {
			// When instances exist, [?] behaves like normal matching
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation ClassToTable {
							cn : String;
							checkonly domain uml c : Class[?] {
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
			QvtdModelExtent rdbmsExtent = QvtdModelExtent.of();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(1, rdbmsExtent.getContents().size(),
					"Optional [?] should match normally when instances exist");
			EObject table = rdbmsExtent.getContents().get(0);
			assertEquals("Employee",
					table.eGet(table.eClass().getEStructuralFeature("name")));
		}
	}

	// ===== §7.5: In-place Transformations =====

	@Nested
	@DisplayName("§7.5: In-place Transformations")
	class InPlaceTransformations {

		@Test
		@DisplayName("Same typed model as source and target modifies in-place")
		void inPlaceModification() throws QvtdParseException {
			// Both domains reference the same model. The checkonly domain reads,
			// the enforce domain writes to the same extent.
			// §7.7: "an implementation that operates in-place must take copies
			// of the old state to avoid confusion with updated new state"
			String source = """
					transformation T(uml : simpleuml) {
						top relation AddKind {
							cn : String;
							checkonly domain uml c : Class {
								name = cn
							};
							enforce domain uml c2 : Class {
								name = cn,
								kind = 'Persistent'
							};
						}
					}
					""";

			EObject cls = createClass("Employee", null);

			QvtdModelExtent uml = QvtdModelExtent.of(cls);

			QvtdExecutionResult result = executeEnforce(source, "uml",
					Map.of("uml", uml));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			// The class should now have kind='Persistent' set in-place
			assertEquals("Persistent",
					cls.eGet(cls.eClass().getEStructuralFeature("kind")),
					"In-place transformation should modify existing element");
		}
	}

	// ===== §7.8: Blackbox Queries =====

	@Nested
	@DisplayName("§7.8: Blackbox Queries")
	class BlackboxQueries {

		@Test
		@DisplayName("Query without body calls blackbox implementation")
		void blackboxQueryInvocation() throws QvtdParseException {
			// A query declared without body (just ';') delegates to blackbox registry
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						query TypeMap(umlType : String) : String;

						top relation R {
							an : String;
							atype : String;
							sqltype : String;
							checkonly domain uml c : Class {
								ownedAttribute = a : Attribute {
									name = an,
									type = atype
								}
							};
							enforce domain rdbms t : Table {
								ownedColumn = col : Column {
									name = an,
									type = sqltype
								}
							};
							where {
								sqltype = TypeMap(atype);
							}
						}
					}
					""";

			// Set up blackbox registry with TypeMap implementation
			BasicQvtdBlackboxRegistry registry = new BasicQvtdBlackboxRegistry();
			registry.register(new QvtdBlackboxLibrary() {
				@Override
				public String getModuleName() { return "helpers"; }
				@Override
				public String getUnitQualifiedName() { return "helpers"; }
				@Override
				public List<String> getUsedPackageURIs() { return List.of(); }
				@Override
				public List<String> getOperationNames() {
					return List.of("TypeMap");
				}

				@Override
				public Object invoke(String operationName, Object self, Object[] args) {
					if ("TypeMap".equals(operationName) && args.length == 1) {
						return "Integer".equals(args[0]) ? "NUMBER" : "VARCHAR";
					}
					return null;
				}
			});

			OclConfiguration oclConfig = OclConfiguration.builder(
					new OclParserSupport()).build();
			QvtdConfiguration bbConfig = QvtdConfiguration.builder(oclConfig)
					.blackboxRegistry(registry)
					.blackboxEnabled(true)
					.build();
			QvtdEngine bbEngine = QvtdEngines.create(bbConfig);

			EObject cls = createClass("Emp", null);
			addAttributeToClass(cls, createAttribute("salary", "Integer"));

			QvtdModelExtent uml = QvtdModelExtent.of(cls);
			QvtdModelExtent rdbms = emptyExtent();

			RelationalTransformation t = bbEngine.parse(source, "test");
			QvtdExecutionContext ctx = QvtdExecutionContext.enforce("rdbms",
					Map.of("uml", uml, "rdbms", rdbms));
			QvtdExecutionResult result = bbEngine.execute(t, ctx);

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			EObject table = rdbms.getContents().get(0);
			var cols = getColumns(table);
			assertEquals(1, cols.size());
			assertEquals("NUMBER", getType(cols.get(0)),
					"Blackbox TypeMap should map Integer → NUMBER");
		}
	}

	// ===== §7.13.1: Import/Multi-File =====

	@Nested
	@DisplayName("§7.13.1: Import/Multi-File")
	class ImportMultiFile {

		@Test
		@Disabled("GAP-10: Multi-file import — deferred to Phase 5 (Language Servers). "
				+ "§7.13.4 defines 'import' syntax for referencing external QVT-R units by URI. "
				+ "Requires: (1) UnitResolver infrastructure for QVT-R (analog to QvtoUnitResolver), "
				+ "(2) file lookup + external parsing + cross-unit linking in QvtrParserSupport, "
				+ "(3) relation/query sharing across compilation units. "
				+ "Currently all QVT-R transformations are single-file. Multi-file becomes relevant "
				+ "with Language Server support (live editing, workspace-wide resolution).")
		@DisplayName("import resolves external transformation unit")
		void importResolvesExternalUnit() throws QvtdParseException {
			// §7.13.4: 'import' [identifier ':'] URI ('::' identifier)* ['::*'] ';'
			//
			// What this gap requires:
			// 1. QvtdUnitResolver interface (analog to QvtoUnitResolver) — resolves
			//    unit names/URIs to source text or pre-parsed RelationalTransformations.
			// 2. QvtrParserSupport extended to handle multi-unit parsing — parse the
			//    imported unit, merge relations/queries into the importing transformation.
			// 3. Cross-unit relation calls — a relation in unit A can invoke a relation
			//    from imported unit B via 'where { B::SomeRelation(...); }'.
			// 4. QvtdConfiguration extended with unitResolvers (like QvtoConfiguration).
			//
			// Example (when implemented):
			//   // helpers.qvtr
			//   transformation Helpers(uml : simpleuml, rdbms : simplerdbms) {
			//       relation NameMapping { ... }
			//   }
			//   // main.qvtr
			//   import helpers;
			//   transformation Main(uml : simpleuml, rdbms : simplerdbms) {
			//       top relation ClassToTable {
			//           ...
			//           where { Helpers::NameMapping(cn, tn); }
			//       }
			//   }
		}
	}

	// ===== §7.6: Transformation Extends =====

	@Nested
	@DisplayName("§7.11.1.1: Transformation Extends")
	class TransformationExtends {

		@Test
		@DisplayName("Extending transformation inherits relations from base")
		void extendsInheritsRelations() throws QvtdParseException {
			// §7.11.1.1: "The rules of the extended transformation are included
			// in the extending transformation."
			// T1 has PackageToSchema, T2 extends T1 and adds ClassToTable.
			// Executing T2 should run both relations.
			String source = """
					transformation T1(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageToSchema {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
					}
					transformation T2(uml : simpleuml, rdbms : simplerdbms) extends T1 {
						top relation ClassToTable {
							cn : String;
							checkonly domain uml c : Class { name = cn };
							enforce domain rdbms t : Table { name = cn };
						}
					}
					""";

			EObject pkg = createPackage("MyPkg");
			EObject cls = createClass("Employee", null);

			QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg, cls);
			QvtdModelExtent rdbmsExtent = QvtdModelExtent.of();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			// Both inherited PackageToSchema and own ClassToTable should have run
			assertEquals(2, rdbmsExtent.getContents().size(),
					"Expected Schema (inherited) + Table (own)");
		}

		@Test
		@DisplayName("Extending transformation can override base relations")
		void extendsOverridesRelation() throws QvtdParseException {
			// Child relation with same name replaces base relation
			String source = """
					transformation T1(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageToSchema {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
					}
					transformation T2(uml : simpleuml, rdbms : simplerdbms) extends T1 {
						top relation PackageToSchema {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = 'OVERRIDDEN' };
						}
					}
					""";

			EObject pkg = createPackage("MyPkg");

			QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg);
			QvtdModelExtent rdbmsExtent = QvtdModelExtent.of();

			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(1, rdbmsExtent.getContents().size());
			EObject schema = rdbmsExtent.getContents().get(0);
			assertEquals("OVERRIDDEN",
					schema.eGet(schema.eClass().getEStructuralFeature("name")),
					"Child relation should override base");
		}
	}

	// ===== §7.2.6: implementedby =====

	@Nested
	@DisplayName("§7.11.3.6: implementedby Clause")
	class ImplementedByClause {

		@Test
		@DisplayName("implementedby delegates enforcement to operational implementation")
		void implementedbyDelegation() throws QvtdParseException {
			// §7.11.3.6: "The black-box operation is invoked when the relation
			// is executed in the direction of the typed model associated with
			// the enforced domain and the relation evaluates to false."
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation ClassToTable {
							cn : String;
							checkonly domain uml c : Class { name = cn };
							enforce domain rdbms t : Table { name = cn }
								implementedby createTableOp(cn);
						}
					}
					""";

			// Register blackbox that handles createTableOp
			BasicQvtdBlackboxRegistry registry = new BasicQvtdBlackboxRegistry();
			registry.register(new QvtdBlackboxLibrary() {
				EObject createdTable;
				@Override
				public String getModuleName() { return "ops"; }
				@Override
				public String getUnitQualifiedName() { return "ops"; }
				@Override
				public List<String> getUsedPackageURIs() { return List.of(); }
				@Override
				public List<String> getOperationNames() {
					return List.of("createTableOp");
				}

				@Override
				public Object invoke(String operationName, Object self, Object[] args) {
					if ("createTableOp".equals(operationName) && args.length == 1) {
						// Create a Table with name = arg and custom marker
						createdTable = createTable((String) args[0]);
						// Set a distinguishing marker to prove implementedby was called
						createdTable.eSet(
								createdTable.eClass().getEStructuralFeature("name"),
								"IMPL_" + args[0]);
						return createdTable;
					}
					return null;
				}
			});

			OclConfiguration oclConfig = OclConfiguration.builder(
					new OclParserSupport()).build();
			QvtdConfiguration config = QvtdConfiguration.builder(oclConfig)
					.blackboxRegistry(registry)
					.blackboxEnabled(true)
					.build();
			QvtdEngine implEngine = QvtdEngines.create(config);

			EObject cls = createClass("Employee", null);
			QvtdModelExtent uml = QvtdModelExtent.of(cls);
			QvtdModelExtent rdbms = emptyExtent();

			RelationalTransformation t = implEngine.parse(source, "test");
			QvtdExecutionContext ctx = QvtdExecutionContext.enforce("rdbms",
					Map.of("uml", uml, "rdbms", rdbms));
			QvtdExecutionResult result = implEngine.execute(t, ctx);

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(1, rdbms.getContents().size());
			EObject table = rdbms.getContents().get(0);
			assertEquals("IMPL_Employee",
					table.eGet(table.eClass().getEStructuralFeature("name")),
					"implementedby should delegate to blackbox operation");
		}
	}

	// ===== §7.10.4: Change Propagation =====

	@Nested
	@DisplayName("§7.10.4: Change Propagation")
	class ChangePropagation {

		@Test
		@Disabled("GAP-13: Incremental change propagation — deferred to Phase 5 (Language Servers). "
				+ "§7.10.4 describes incremental re-execution where only affected relations re-run "
				+ "after source model changes. Our engine currently performs full re-execution, which "
				+ "is semantically correct (produces identical results). Incremental execution is a "
				+ "performance optimization requiring: (1) dependency graph between relations, "
				+ "(2) change listeners on source model extents, (3) dirty-marking of affected traces. "
				+ "Becomes relevant with Language Server support (live preview, watch mode).")
		@DisplayName("Incremental execution propagates changes without full re-execution")
		void incrementalChangePropagation() throws QvtdParseException {
			// §7.10.4: Change propagation — incremental re-execution optimization.
			//
			// Current behavior: Full re-execution on every engine.execute() call.
			// This is CORRECT per spec — §7.6 states that change propagation is
			// "semantically equivalent to re-execution of the transformation".
			//
			// What this gap requires for INCREMENTAL execution:
			// 1. Dependency graph: track which relations depend on which model elements.
			//    When element E changes, only relations touching E need re-execution.
			// 2. Change listeners: EMF EContentAdapter or similar on source extents to
			//    detect modifications between execute() calls.
			// 3. Trace-based dirty marking: use QvtrTraceManager to identify which
			//    trace entries are invalidated by the change, re-execute only those.
			// 4. Target model update: merge incremental results into existing target
			//    (update/delete affected elements, keep unaffected ones).
			//
			// This is a pure performance optimization — the spec explicitly notes that
			// full re-execution produces correct results. Incremental mode becomes
			// valuable in IDE/LSP scenarios with live model editing.
		}
	}

	// ===== Helper =====

	private static String getType(EObject obj) {
		return (String) obj.eGet(obj.eClass().getEStructuralFeature("type"));
	}

	private static EObject createSchema(String name) {
		var cls = ecoreHelper.getEClass(rdbmsPackage, "Schema");
		EObject schema = org.eclipse.emf.ecore.util.EcoreUtil.create(cls);
		schema.eSet(cls.getEStructuralFeature("name"), name);
		return schema;
	}

	private static EObject createTable(String name) {
		var cls = ecoreHelper.getEClass(rdbmsPackage, "Table");
		EObject table = org.eclipse.emf.ecore.util.EcoreUtil.create(cls);
		table.eSet(cls.getEStructuralFeature("name"), name);
		return table;
	}

	@SuppressWarnings("unchecked")
	private static void addTableToSchema(EObject schema, EObject table) {
		((List<EObject>) schema.eGet(
				schema.eClass().getEStructuralFeature("ownedTable"))).add(table);
	}
}
