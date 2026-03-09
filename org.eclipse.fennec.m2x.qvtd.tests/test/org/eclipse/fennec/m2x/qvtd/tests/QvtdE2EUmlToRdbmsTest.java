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
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * End-to-end test based on the canonical UML→RDBMS example from the QVT v1.3
 * specification (§7.1, §7.10), adapted for our simplified metamodels.
 *
 * <p>This is the primary spec-conformance E2E test for P4-6.
 *
 * <p>Transformation chain:
 * <pre>
 *   PackageToSchema (top)     : Package → Schema
 *     └─ ClassToTable (non-top)  : Class → Table  (via where)
 *         └─ AttributeToColumn (non-top): Attribute → Column (via where)
 * </pre>
 */
@DisplayName("E2E: UML→RDBMS (§7.1 spec example)")
class QvtdE2EUmlToRdbmsTest extends AbstractQvtdEngineTest {

	/**
	 * The simplified UML→RDBMS transformation adapted for our metamodels.
	 * Mirrors the spec example structure:
	 * - PackageToSchema: top, where invokes ClassToTable
	 * - ClassToTable: non-top, filters kind='Persistent', where invokes AttributeToColumn
	 * - AttributeToColumn: non-top, maps attribute name+type to column name+type
	 */
	private static final String UML_TO_RDBMS = """
			transformation UmlToRdbms(uml : simpleuml, rdbms : simplerdbms) {
				key simplerdbms::Table {name};
				key simplerdbms::Column {name};

				top relation PackageToSchema {
					pn : String;
					checkonly domain uml p : Package {
						name = pn
					};
					enforce domain rdbms s : Schema {
						name = pn
					};
					where {
						ClassToTable(p, s);
					}
				}

				relation ClassToTable {
					cn : String;
					checkonly domain uml p : Package {
						ownedClass = c : Class {
							name = cn,
							kind = 'Persistent'
						}
					};
					enforce domain rdbms s : Schema {
						ownedTable = t : Table {
							name = cn
						}
					};
					where {
						AttributeToColumn(c, t);
					}
				}

				relation AttributeToColumn {
					an : String;
					atype : String;
					checkonly domain uml c : Class {
						ownedAttribute = a : Attribute {
							name = an,
							type = atype
						}
					};
					enforce domain rdbms t : Table {
						ownedColumn = col : Column {
							name = an,
							type = atype
						}
					};
				}
			}
			""";

	// ===== Basic E2E =====

	@Test
	@DisplayName("Single package, one class, one attribute → full mapping")
	void singlePackageSingleClassSingleAttribute() throws QvtdParseException {
		EObject pkg = createPackage("HR");
		EObject cls = createClass("Employee", "Persistent");
		EObject attr = createAttribute("salary", "Integer");
		addAttributeToClass(cls, attr);
		addClassToPackage(pkg, cls);

		QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg);
		QvtdModelExtent rdbmsExtent = emptyExtent();

		QvtdExecutionResult result = executeEnforce(UML_TO_RDBMS, "rdbms",
				Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

		assertTrue(result.isSuccess(), "" + result.diagnostics());

		// Schema "HR" with Table "Employee" containing Column "salary"
		assertEquals(1, rdbmsExtent.getContents().size(), "One Schema root");
		EObject schema = rdbmsExtent.getContents().get(0);
		assertEquals("HR", getName(schema));

		var tables = getTables(schema);
		assertEquals(1, tables.size(), "One Table for Persistent class");
		EObject table = tables.get(0);
		assertEquals("Employee", getName(table));

		var columns = getColumns(table);
		assertEquals(1, columns.size(), "One Column for one Attribute");
		EObject col = columns.get(0);
		assertEquals("salary", getName(col));
		assertEquals("Integer", getType(col));
	}

	@Test
	@DisplayName("Class with multiple attributes → multiple columns")
	void multipleAttributes() throws QvtdParseException {
		EObject pkg = createPackage("HR");
		EObject cls = createClass("Employee", "Persistent");
		addAttributeToClass(cls, createAttribute("name", "String"));
		addAttributeToClass(cls, createAttribute("salary", "Integer"));
		addAttributeToClass(cls, createAttribute("active", "Boolean"));
		addClassToPackage(pkg, cls);

		QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg);
		QvtdModelExtent rdbmsExtent = emptyExtent();

		QvtdExecutionResult result = executeEnforce(UML_TO_RDBMS, "rdbms",
				Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

		assertTrue(result.isSuccess(), "" + result.diagnostics());

		EObject schema = rdbmsExtent.getContents().get(0);
		var tables = getTables(schema);
		assertEquals(1, tables.size());

		Set<String> colNames = getColumns(tables.get(0)).stream()
				.map(c -> getName(c))
				.collect(Collectors.toSet());
		assertEquals(Set.of("name", "salary", "active"), colNames,
				"Three columns for three attributes");
	}

	@Test
	@DisplayName("Multiple classes in one package → multiple tables")
	void multipleClasses() throws QvtdParseException {
		EObject pkg = createPackage("HR");
		EObject emp = createClass("Employee", "Persistent");
		addAttributeToClass(emp, createAttribute("name", "String"));
		EObject dept = createClass("Department", "Persistent");
		addAttributeToClass(dept, createAttribute("title", "String"));
		addClassToPackage(pkg, emp);
		addClassToPackage(pkg, dept);

		QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg);
		QvtdModelExtent rdbmsExtent = emptyExtent();

		QvtdExecutionResult result = executeEnforce(UML_TO_RDBMS, "rdbms",
				Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

		assertTrue(result.isSuccess(), "" + result.diagnostics());

		EObject schema = rdbmsExtent.getContents().get(0);
		Set<String> tableNames = getTables(schema).stream()
				.map(t -> getName(t))
				.collect(Collectors.toSet());
		assertEquals(Set.of("Employee", "Department"), tableNames);

		// Each table should have its own column
		for (EObject table : getTables(schema)) {
			assertEquals(1, getColumns(table).size(),
					"Table " + getName(table) + " should have 1 column");
		}
	}

	// ===== Filtering =====

	@Test
	@DisplayName("Non-persistent class filtered out (kind != 'Persistent')")
	void nonPersistentClassFiltered() throws QvtdParseException {
		EObject pkg = createPackage("Model");
		EObject persistent = createClass("Customer", "Persistent");
		addAttributeToClass(persistent, createAttribute("id", "Integer"));
		EObject transient_ = createClass("TempData", "Transient");
		addAttributeToClass(transient_, createAttribute("data", "String"));
		addClassToPackage(pkg, persistent);
		addClassToPackage(pkg, transient_);

		QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg);
		QvtdModelExtent rdbmsExtent = emptyExtent();

		QvtdExecutionResult result = executeEnforce(UML_TO_RDBMS, "rdbms",
				Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

		assertTrue(result.isSuccess(), "" + result.diagnostics());

		EObject schema = rdbmsExtent.getContents().get(0);
		var tables = getTables(schema);
		assertEquals(1, tables.size(),
				"Only Persistent class should produce a Table");
		assertEquals("Customer", getName(tables.get(0)));
	}

	@Test
	@DisplayName("Empty package → Schema but no tables")
	void emptyPackage() throws QvtdParseException {
		EObject pkg = createPackage("Empty");

		QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg);
		QvtdModelExtent rdbmsExtent = emptyExtent();

		QvtdExecutionResult result = executeEnforce(UML_TO_RDBMS, "rdbms",
				Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

		assertTrue(result.isSuccess(), "" + result.diagnostics());

		assertEquals(1, rdbmsExtent.getContents().size());
		EObject schema = rdbmsExtent.getContents().get(0);
		assertEquals("Empty", getName(schema));
		assertTrue(getTables(schema).isEmpty(), "No tables for empty package");
	}

	@Test
	@DisplayName("Class without attributes → Table but no columns")
	void classWithoutAttributes() throws QvtdParseException {
		EObject pkg = createPackage("Core");
		EObject cls = createClass("Marker", "Persistent");
		addClassToPackage(pkg, cls);

		QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg);
		QvtdModelExtent rdbmsExtent = emptyExtent();

		QvtdExecutionResult result = executeEnforce(UML_TO_RDBMS, "rdbms",
				Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

		assertTrue(result.isSuccess(), "" + result.diagnostics());

		EObject schema = rdbmsExtent.getContents().get(0);
		var tables = getTables(schema);
		assertEquals(1, tables.size());
		assertEquals("Marker", getName(tables.get(0)));
		assertTrue(getColumns(tables.get(0)).isEmpty(),
				"No columns for class without attributes");
	}

	// ===== Multiple Packages =====

	@Test
	@DisplayName("Multiple packages → multiple schemas with correct tables")
	void multiplePackages() throws QvtdParseException {
		EObject pkg1 = createPackage("HR");
		EObject emp = createClass("Employee", "Persistent");
		addAttributeToClass(emp, createAttribute("name", "String"));
		addClassToPackage(pkg1, emp);

		EObject pkg2 = createPackage("Finance");
		EObject acc = createClass("Account", "Persistent");
		addAttributeToClass(acc, createAttribute("balance", "Integer"));
		addClassToPackage(pkg2, acc);

		QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg1, pkg2);
		QvtdModelExtent rdbmsExtent = emptyExtent();

		QvtdExecutionResult result = executeEnforce(UML_TO_RDBMS, "rdbms",
				Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

		assertTrue(result.isSuccess(), "" + result.diagnostics());

		List<EObject> schemas = rdbmsExtent.getContents();
		assertEquals(2, schemas.size(), "Two schemas for two packages");

		Set<String> schemaNames = schemas.stream()
				.map(s -> getName(s))
				.collect(Collectors.toSet());
		assertEquals(Set.of("HR", "Finance"), schemaNames);

		// Each schema should have its table
		for (EObject schema : schemas) {
			var tables = getTables(schema);
			assertEquals(1, tables.size(),
					"Schema " + getName(schema) + " should have 1 table");
		}
	}

	// ===== Key-based Idempotency =====

	@Test
	@DisplayName("Re-execution is idempotent (key-based identity)")
	void idempotentReExecution() throws QvtdParseException {
		EObject pkg = createPackage("HR");
		EObject cls = createClass("Employee", "Persistent");
		addAttributeToClass(cls, createAttribute("salary", "Integer"));
		addClassToPackage(pkg, cls);

		QvtdModelExtent umlExtent = QvtdModelExtent.of(pkg);
		QvtdModelExtent rdbmsExtent = emptyExtent();

		// Execute twice
		QvtdExecutionResult r1 = executeEnforce(UML_TO_RDBMS, "rdbms",
				Map.of("uml", umlExtent, "rdbms", rdbmsExtent));
		assertTrue(r1.isSuccess(), "" + r1.diagnostics());

		QvtdExecutionResult r2 = executeEnforce(UML_TO_RDBMS, "rdbms",
				Map.of("uml", umlExtent, "rdbms", rdbmsExtent));
		assertTrue(r2.isSuccess(), "" + r2.diagnostics());

		// Should still have exactly 1 Schema, 1 Table (keys prevent duplicates)
		assertEquals(1, rdbmsExtent.getContents().size(),
				"Idempotent: still one Schema");
		EObject schema = rdbmsExtent.getContents().get(0);
		assertEquals(1, getTables(schema).size(),
				"Idempotent: still one Table (key-based identity)");
	}

	// ===== Helper =====

	private static String getType(EObject obj) {
		return (String) obj.eGet(obj.eClass().getEStructuralFeature("type"));
	}
}
