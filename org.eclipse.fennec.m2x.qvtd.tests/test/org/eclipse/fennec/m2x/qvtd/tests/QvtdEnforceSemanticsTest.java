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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * What enforcing does with the target model (§7.4, §7.10.2): which object a template selects
 * (#350), when two assignments clash and where created objects go (#351), and which objects are
 * deleted (#352).
 */
class QvtdEnforceSemanticsTest extends AbstractQvtdEngineTest {

	private static final String PACKAGE_TO_SCHEMA = """
			top relation PackageToSchema {
				pn : String;
				checkonly domain uml p : Package { name = pn };
				enforce domain rdbms s : Schema { name = pn };
			}
			""";

	/** §7.4's own example: the table of a class lives in the schema of the class's package. */
	private static final String CLASS_TO_TABLE = """
			top relation ClassToTable {
				cn : String;
				p : Package;
				s : Schema;
				checkonly domain uml c : Class { owningPackage = p, name = cn };
				enforce domain rdbms t : Table { schema = s : Schema {}, name = cn };
				when { PackageToSchema(p, s); }
			}
			""";

	private static String transformation(String keys, String... relations) {
		return "transformation T(uml : simpleuml, rdbms : simplerdbms) {\n" + keys + "\n"
				+ String.join("\n", relations) + "}\n";
	}

	/** An engine that keeps what no binding requires, so a test sees only the selection. */
	private static QvtdEngine keeping() {
		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(umlPackage.getNsURI(), umlPackage);
		registry.put(rdbmsPackage.getNsURI(), rdbmsPackage);
		return QvtdEngines.create(QvtdConfiguration.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.packageRegistry(registry).deleteUnmatched(false).build());
	}

	private static QvtdExecutionResult enforce(QvtdEngine on, String source, QvtdModelExtent uml,
			QvtdModelExtent rdbms) throws QvtdParseException {
		return on.execute(on.parse(source, "T"),
				QvtdExecutionContext.enforce("rdbms", Map.of("uml", uml, "rdbms", rdbms)));
	}

	private static EObject rdbms(String className, String name) {
		EClass eClass = ecoreHelper.getEClass(rdbmsPackage, className);
		EObject object = EcoreUtil.create(eClass);
		object.eSet(eClass.getEStructuralFeature("name"), name);
		return object;
	}

	private static EObject table(EObject schema, String name) {
		EObject table = rdbms("Table", name);
		getTables(schema).add(table);
		return table;
	}

	private static EObject column(EObject table, String name) {
		EObject column = rdbms("Column", name);
		getColumns(table).add(column);
		return column;
	}

	private static EObject pkg(QvtdModelExtent uml, String name, String... classes) {
		EObject pkg = createPackage(name);
		uml.getContents().add(pkg);
		for (String className : classes) {
			addClassToPackage(pkg, createClass(className, null));
		}
		return pkg;
	}

	private static Object get(EObject object, String feature) {
		return object.eGet(object.eClass().getEStructuralFeature(feature));
	}

	/** Schema name → table names, of every Schema root of the extent. */
	private static Map<String, List<String>> layout(QvtdModelExtent rdbms) {
		Map<String, List<String>> layout = new TreeMap<>();
		for (EObject root : rdbms.getContents()) {
			if ("Schema".equals(root.eClass().getName())) {
				layout.put(getName(root), getTables(root).stream().map(t -> getName(t)).toList());
			}
		}
		return layout;
	}

	private static List<String> errors(QvtdExecutionResult result) {
		return result.diagnostics().stream().filter(d -> d.getSeverity() == Diagnostic.ERROR)
				.map(Diagnostic::getMessage).toList();
	}

	@Nested
	@DisplayName("#350: object selection through keys (§7.4)")
	class Keys {

		@Test
		@DisplayName("key Table {schema, name}: the table of the class's own schema is found, the namesake elsewhere stays")
		void key_templatePart_specExample() throws QvtdParseException {
			QvtdModelExtent uml = QvtdModelExtent.of();
			pkg(uml, "HR", "Foo");
			pkg(uml, "Other");
			EObject other = rdbms("Schema", "Other");
			EObject otherFoo = table(other, "Foo");
			EObject hr = rdbms("Schema", "HR");
			EObject hrFoo = table(hr, "Foo");
			QvtdModelExtent rdbms = QvtdModelExtent.of(other, hr);
			String source = transformation("key Schema {name};\nkey Table {schema, name};", PACKAGE_TO_SCHEMA, CLASS_TO_TABLE);

			for (int run = 1; run <= 2; run++) {
				QvtdExecutionResult result = enforce(keeping(), source, uml, rdbms);
				assertEquals(List.of(), errors(result), "run " + run);
				assertEquals(Map.of("HR", List.of("Foo"), "Other", List.of("Foo")), layout(rdbms), "run " + run);
				assertSame(hrFoo, getTables(hr).get(0), "the existing table is updated, not replaced");
				assertSame(otherFoo, getTables(other).get(0), "the namesake in Other is not moved");
			}
		}

		@Test
		@DisplayName("key Table {schema, name}: the update goes to the table of the right schema")
		void key_templatePart_updatesRightTable() throws QvtdParseException {
			QvtdModelExtent uml = QvtdModelExtent.of();
			pkg(uml, "S2", "T");
			pkg(uml, "S1");
			EObject s1 = rdbms("Schema", "S1");
			EObject t1 = table(s1, "T");
			t1.eSet(t1.eClass().getEStructuralFeature("type"), "old");
			EObject s2 = rdbms("Schema", "S2");
			EObject t2 = table(s2, "T");
			t2.eSet(t2.eClass().getEStructuralFeature("type"), "old");
			String withType = CLASS_TO_TABLE.replace("schema = s : Schema {}, name = cn };",
					"schema = s : Schema {}, name = cn, type = 'new' };");

			QvtdExecutionResult result = enforce(keeping(),
					transformation("key Schema {name};\nkey Table {schema, name};", PACKAGE_TO_SCHEMA, withType),
					uml, QvtdModelExtent.of(s1, s2));

			assertEquals(List.of(), errors(result));
			assertEquals("new", get(t2, "type"));
			assertEquals("old", get(t1, "type"));
			assertSame(s1, t1.eContainer(), "S1's table stays in S1");
		}

		@Test
		@DisplayName("key Column {name, opposite(Table::ownedColumn)}: each table keeps its own column")
		void key_oppositePart_columnPerTable() throws QvtdParseException {
			QvtdModelExtent uml = QvtdModelExtent.of();
			EObject p = pkg(uml, "P", "A", "B");
			for (Object cls : (List<?>) get(p, "ownedClass")) {
				addAttributeToClass((EObject) cls, createAttribute("id", "Integer"));
			}
			EObject schema = rdbms("Schema", "P");
			EObject a = table(schema, "A");
			EObject aId = column(a, "id");
			EObject b = table(schema, "B");
			String attributeToColumn = """
					top relation AttributeToColumn {
						an : String;
						c : Class;
						t : Table;
						checkonly domain uml at : Attribute { owningClass = c, name = an };
						enforce domain rdbms col : Column { owningTable = t, name = an };
						when { ClassToTable(c, t); }
					}
					""";

			QvtdExecutionResult result = enforce(keeping(), transformation(
					"key Schema {name};\nkey Table {schema, name};\nkey Column {name, opposite(Table::ownedColumn)};",
					PACKAGE_TO_SCHEMA, CLASS_TO_TABLE, attributeToColumn), uml, QvtdModelExtent.of(schema));

			assertEquals(List.of(), errors(result));
			assertEquals(List.of(aId), getColumns(a), "A keeps its column");
			assertEquals(1, getColumns(b).size(), "B gets a column of its own");
		}

		@Test
		@DisplayName("a class may have several keys: the second one locates the object")
		void key_secondKeyLocatesObject() throws QvtdParseException {
			EObject existing = rdbms("Table", "X");
			existing.eSet(existing.eClass().getEStructuralFeature("type"), "T1");
			QvtdModelExtent rdbms = QvtdModelExtent.of(existing);
			String classToTable = """
					top relation ClassToTable {
						cn : String;
						checkonly domain uml c : Class { name = cn };
						enforce domain rdbms t : Table { name = cn, type = 'T1' };
					}
					""";

			QvtdExecutionResult result = enforce(keeping(), transformation("key Table {name};\nkey Table {type};", classToTable),
					QvtdModelExtent.of(createClass("Y", null)), rdbms);

			assertEquals(List.of(), errors(result));
			assertEquals(List.of(existing), rdbms.getContents());
			assertEquals("Y", getName(existing));
		}

		@Test
		@DisplayName("a key of a superclass identifies objects of its subclasses")
		void key_ofSuperclass_appliesToSubclass() throws QvtdParseException {
			EPackage mm = EcoreFactory.eINSTANCE.createEPackage();
			mm.setName("named");
			mm.setNsURI("http://example.org/m2x/qvtr-named/1.0");
			mm.setNsPrefix("named");
			EClass named = EcoreFactory.eINSTANCE.createEClass();
			named.setName("NamedElement");
			named.setAbstract(true);
			EAttribute name = EcoreFactory.eINSTANCE.createEAttribute();
			name.setName("name");
			name.setEType(EcorePackage.Literals.ESTRING);
			named.getEStructuralFeatures().add(name);
			EClass item = EcoreFactory.eINSTANCE.createEClass();
			item.setName("Item");
			item.getESuperTypes().add(named);
			mm.getEClassifiers().add(named);
			mm.getEClassifiers().add(item);
			EPackage.Registry registry = new EPackageRegistryImpl();
			registry.put(mm.getNsURI(), mm);
			registry.put(umlPackage.getNsURI(), umlPackage);
			QvtdEngine on = QvtdEngines.create(QvtdConfiguration.builder(OclConfiguration.builder(new OclParserSupport()).build())
					.packageRegistry(registry).build());
			EObject existing = EcoreUtil.create(item);
			existing.eSet(name, "A");
			QvtdModelExtent out = QvtdModelExtent.of(existing);

			QvtdExecutionResult result = on.execute(on.parse("""
					transformation T(uml : simpleuml, out : named) {
						key NamedElement {name};
						top relation PackageToItem {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain out i : Item { name = pn };
						}
					}
					""", "T"), QvtdExecutionContext.enforce("out", Map.of("uml", QvtdModelExtent.of(createPackage("A")), "out", out)));

			assertEquals(List.of(), errors(result));
			assertEquals(List.of(existing), out.getContents(), "the superclass key found the existing Item");
		}

		@Test
		@DisplayName("without a key a namesake in another schema is not taken over")
		void noKey_foreignTableUntouched() throws QvtdParseException {
			QvtdModelExtent uml = QvtdModelExtent.of();
			pkg(uml, "HR", "Foo");
			pkg(uml, "Other");
			EObject other = rdbms("Schema", "Other");
			EObject foreign = table(other, "Foo");
			EObject hr = rdbms("Schema", "HR");

			QvtdExecutionResult result = enforce(keeping(), transformation("key Schema {name};", PACKAGE_TO_SCHEMA, CLASS_TO_TABLE),
					uml, QvtdModelExtent.of(other, hr));

			assertEquals(List.of(), errors(result));
			assertEquals(Map.of("HR", List.of("Foo"), "Other", List.of("Foo")), layout(QvtdModelExtent.of(other, hr)));
			assertSame(other, foreign.eContainer(), "Other's table stays where it is");
		}

		@Test
		@DisplayName("a key of an unknown property is a parse error")
		void keyDecl_unknownProperty_parseError() {
			QvtdParseException e = assertThrows(QvtdParseException.class,
					() -> parse(transformation("key simplerdbms::Table {nam};")));
			assertTrue(e.getMessage().contains("Unknown key property (Table::nam)"), e.getMessage());
		}

		@Test
		@DisplayName("a key of an unknown class is a parse error")
		void keyDecl_unknownClass_parseError() {
			QvtdParseException e = assertThrows(QvtdParseException.class,
					() -> parse(transformation("key simplerdbms::Tabel {name};")));
			assertTrue(e.getMessage().contains("Unknown key class (simplerdbms::Tabel)"), e.getMessage());
		}
	}

	@Nested
	@DisplayName("#351: clashes, created objects, where (§7.10.2)")
	class Assignments {

		private static String twoRelations(String typeOfFirst, String typeOfSecond) {
			return transformation("key Table {name};", """
					top relation R1 {
						checkonly domain uml p : Package {};
						enforce domain rdbms t : Table { name = 'X', type = '%s' };
					}
					""".formatted(typeOfFirst), """
					top relation R2 {
						checkonly domain uml p : Package {};
						enforce domain rdbms t : Table { name = 'X', type = '%s' };
					}
					""".formatted(typeOfSecond));
		}

		@Test
		@DisplayName("two relations setting different values: an error, the first value stays")
		void clash_primitiveValue_error() throws QvtdParseException {
			QvtdModelExtent rdbms = QvtdModelExtent.of();
			QvtdExecutionResult result = enforce(keeping(), twoRelations("A", "B"), QvtdModelExtent.of(createPackage("P")), rdbms);

			assertFalse(result.isSuccess());
			assertTrue(errors(result).stream().anyMatch(m -> m.contains("Relation 'R2' assigns 'B' to 'type'")
					&& m.contains("relation 'R1' set to 'A'") && m.contains("clash")), "" + errors(result));
			assertEquals("A", get(rdbms.getContents().get(0), "type"));
		}

		@Test
		@DisplayName("two relations setting the same value: no clash")
		void clash_sameValueTwice_noError() throws QvtdParseException {
			QvtdExecutionResult result = enforce(keeping(), twoRelations("A", "A"), QvtdModelExtent.of(createPackage("P")),
					QvtdModelExtent.of());
			assertEquals(List.of(), errors(result));
		}

		@Test
		@DisplayName("a value the object had before the run is modified, not a clash")
		void clash_preexistingValueModified_noError() throws QvtdParseException {
			EObject existing = rdbms("Table", "X");
			existing.eSet(existing.eClass().getEStructuralFeature("type"), "old");
			QvtdExecutionResult result = enforce(keeping(), twoRelations("A", "A"), QvtdModelExtent.of(createPackage("P")),
					QvtdModelExtent.of(existing));
			assertEquals(List.of(), errors(result));
			assertEquals("A", get(existing, "type"));
		}

		@Test
		@DisplayName("a single-valued reference set to two objects: an error")
		void clash_singleValuedReference_error() throws QvtdParseException {
			String source = transformation("key Table {name};\nkey Schema {name};", """
					top relation R1 {
						checkonly domain uml p : Package {};
						enforce domain rdbms t : Table { name = 'X', schema = s : Schema { name = 'S1' } };
					}
					""", """
					top relation R2 {
						checkonly domain uml p : Package {};
						enforce domain rdbms t : Table { name = 'X', schema = s : Schema { name = 'S2' } };
					}
					""");
			QvtdExecutionResult result = enforce(keeping(), source, QvtdModelExtent.of(createPackage("P")), QvtdModelExtent.of());
			assertTrue(errors(result).stream().anyMatch(m -> m.contains("to 'schema'") && m.contains("clash")),
					"" + result.diagnostics());
		}

		@Test
		@DisplayName("a container created through the opposite of a reference is a root of the target model")
		void enforce_containerViaOpposite_inExtent() throws QvtdParseException {
			QvtdModelExtent rdbms = QvtdModelExtent.of();
			QvtdExecutionResult result = enforce(keeping(), transformation("", """
					top relation ClassToTable {
						cn : String;
						checkonly domain uml c : Class { name = cn };
						enforce domain rdbms t : Table { name = cn, schema = s1 : Schema { name = 'S1' } };
					}
					"""), QvtdModelExtent.of(createClass("X", null)), rdbms);

			assertEquals(List.of(), errors(result));
			assertEquals(Map.of("S1", List.of("X")), layout(rdbms));
			assertEquals(1, rdbms.getContents().size(), "the Table is held by its Schema, not also a root");
		}

		@Test
		@DisplayName("a where predicate on the source bindings that is false: nothing is created, an error")
		void enforce_whereFalse_error() throws QvtdParseException {
			QvtdModelExtent rdbms = QvtdModelExtent.of();
			String source = transformation("", PACKAGE_TO_SCHEMA.replace("enforce domain rdbms s : Schema { name = pn };",
					"enforce domain rdbms s : Schema { name = pn };\n\t\t\t\twhere { pn <> 'A'; }"));
			QvtdExecutionResult result = enforce(keeping(), source, QvtdModelExtent.of(createPackage("A")), rdbms);

			assertTrue(errors(result).stream().anyMatch(m -> m.contains("Relation 'PackageToSchema' cannot hold")),
					"" + result.diagnostics());
			assertEquals(List.of(), rdbms.getContents());
		}

		@Test
		@DisplayName("a where predicate on the target that is false after enforcing: an error")
		void enforce_whereFalseAfterEnforcing_error() throws QvtdParseException {
			String source = transformation("", PACKAGE_TO_SCHEMA.replace("enforce domain rdbms s : Schema { name = pn };",
					"enforce domain rdbms s : Schema { name = pn };\n\t\t\t\twhere { s.name.size() > 3; }"));
			QvtdExecutionResult result = enforce(keeping(), source, QvtdModelExtent.of(createPackage("A")), QvtdModelExtent.of());

			assertTrue(errors(result).stream().anyMatch(m -> m.contains("Relation 'PackageToSchema' does not hold")),
					"" + result.diagnostics());
		}

		@Test
		@DisplayName("of two existing candidates the one that satisfies where is selected, nothing is created")
		void enforce_whereSelectsSecondCandidate() throws QvtdParseException {
			EObject first = rdbms("Table", "C");
			first.eSet(first.eClass().getEStructuralFeature("type"), "A");
			EObject second = rdbms("Table", "C");
			second.eSet(second.eClass().getEStructuralFeature("type"), "B");
			QvtdModelExtent rdbms = QvtdModelExtent.of(first, second);
			String source = transformation("", """
					top relation ClassToTable {
						cn : String;
						checkonly domain uml c : Class { name = cn };
						enforce domain rdbms t : Table { name = cn };
						where { t.type = 'B'; }
					}
					""");

			QvtdExecutionResult result = enforce(keeping(), source, QvtdModelExtent.of(createClass("C", null)), rdbms);

			assertEquals(List.of(), errors(result));
			assertEquals(List.of(first, second), rdbms.getContents(), "nothing created");
		}
	}

	@Nested
	@DisplayName("#352: deletion (§7.2.3, §7.10.2)")
	class Deletion {

		private static final String SCHEMAS = transformation("key Schema {name};", PACKAGE_TO_SCHEMA);

		private static List<String> names(QvtdModelExtent extent) {
			return extent.getContents().stream().map(o -> getName(o)).toList();
		}

		@Test
		@DisplayName("a Schema without a Package is deleted")
		void enforce_deletesSchemaWithoutPackage() throws QvtdParseException {
			QvtdModelExtent rdbms = QvtdModelExtent.of(rdbms("Schema", "A"), rdbms("Schema", "Z"));
			QvtdExecutionResult result = executeEnforce(SCHEMAS, "rdbms",
					Map.of("uml", QvtdModelExtent.of(createPackage("A")), "rdbms", rdbms));
			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(List.of("A"), names(rdbms));
		}

		@Test
		@DisplayName("a stale Table in a Schema that holds is deleted, the Schema stays")
		void enforce_deletesNestedStaleTable() throws QvtdParseException {
			QvtdModelExtent uml = QvtdModelExtent.of();
			pkg(uml, "A", "C");
			EObject schema = rdbms("Schema", "A");
			table(schema, "Stale");
			QvtdModelExtent rdbms = QvtdModelExtent.of(schema);

			QvtdExecutionResult result = executeEnforce(transformation("key Schema {name};\nkey Table {schema, name};",
					PACKAGE_TO_SCHEMA, CLASS_TO_TABLE), "rdbms", Map.of("uml", uml, "rdbms", rdbms));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(Map.of("A", List.of("C")), layout(rdbms));
		}

		@Test
		@DisplayName("an object another relation requires is not deleted")
		void enforce_keepsObjectRequiredByOtherRelation() throws QvtdParseException {
			String source = transformation("key Schema {name};", PACKAGE_TO_SCHEMA, """
					top relation ClassToSchema {
						cn : String;
						checkonly domain uml c : Class { name = cn };
						enforce domain rdbms s : Schema { name = cn };
					}
					""");
			QvtdModelExtent rdbms = QvtdModelExtent.of();
			QvtdExecutionResult result = executeEnforce(source, "rdbms",
					Map.of("uml", QvtdModelExtent.of(createPackage("A"), createClass("B", null)), "rdbms", rdbms));
			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(List.of("A", "B"), names(rdbms), "Schema B has no Package, but ClassToSchema requires it");
		}

		@Test
		@DisplayName("the source model is never touched")
		void enforce_keepsSourceObjects() throws QvtdParseException {
			QvtdModelExtent uml = QvtdModelExtent.of(createPackage("A"));
			executeEnforce(SCHEMAS, "rdbms", Map.of("uml", uml, "rdbms", QvtdModelExtent.of(rdbms("Schema", "Z"))));
			assertEquals(List.of("A"), names(uml));
		}

		@Test
		@DisplayName("a check-only run never deletes")
		void checkOnly_neverDeletes() throws QvtdParseException {
			QvtdModelExtent rdbms = QvtdModelExtent.of(rdbms("Schema", "A"), rdbms("Schema", "Z"));
			executeCheckOnly(SCHEMAS, Map.of("uml", QvtdModelExtent.of(createPackage("A")), "rdbms", rdbms));
			assertEquals(List.of("A", "Z"), names(rdbms));
		}

		@Test
		@DisplayName("an enforce run towards a checkonly domain never deletes")
		void enforceTowardsCheckonlyDomain_neverDeletes() throws QvtdParseException {
			QvtdModelExtent uml = QvtdModelExtent.of(createPackage("A"), createPackage("X"));
			executeEnforce(SCHEMAS, "uml", Map.of("uml", uml, "rdbms", QvtdModelExtent.of(rdbms("Schema", "A"))));
			assertEquals(List.of("A", "X"), names(uml));
		}

		@Test
		@DisplayName("deleteUnmatched(false) keeps what no binding requires")
		void deleteUnmatched_false_keeps() throws QvtdParseException {
			QvtdModelExtent rdbms = QvtdModelExtent.of(rdbms("Schema", "A"), rdbms("Schema", "Z"));
			enforce(keeping(), SCHEMAS, QvtdModelExtent.of(createPackage("A")), rdbms);
			assertEquals(List.of("A", "Z"), names(rdbms));
		}
	}
}
