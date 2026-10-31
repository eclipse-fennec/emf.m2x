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
import java.util.Optional;
import java.util.TreeMap;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
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
 * When clauses and the order of top relations (#349): a when call binds every trace that
 * matches (§7.2.1, §7.3, §7.10.2), the result does not depend on the order the relations are
 * declared in (§7.2.2), and only a relation that holds for a binding leaves a trace.
 */
class QvtdWhenOrderTest extends AbstractQvtdEngineTest {

	private static final String PACKAGE_TO_SCHEMA = """
			top relation PackageToSchema {
				pn : String;
				checkonly domain uml p : Package { name = pn };
				enforce domain rdbms s : Schema { name = pn };
			}
			""";

	/** §7.3's style: the when call binds p and s, the domains are restricted to them. */
	private static final String CLASS_TO_TABLE = """
			top relation ClassToTable {
				cn : String;
				p : Package;
				s : Schema;
				checkonly domain uml c : Class { owningPackage = p, name = cn };
				enforce domain rdbms t : Table { schema = s, name = cn };
				when { PackageToSchema(p, s); }
			}
			""";

	private static final String ATTRIBUTE_TO_COLUMN = """
			top relation AttributeToColumn {
				an : String;
				c : Class;
				t : Table;
				checkonly domain uml a : Attribute { owningClass = c, name = an };
				enforce domain rdbms col : Column { owningTable = t, name = an };
				when { ClassToTable(c, t); }
			}
			""";

	private static String transformation(String... relations) {
		return "transformation T(uml : simpleuml, rdbms : simplerdbms) {\n" + String.join("\n", relations) + "}\n";
	}

	/** Packages with their classes: name → class names. */
	private static QvtdModelExtent uml(Map<String, List<String>> packages) {
		QvtdModelExtent extent = QvtdModelExtent.of();
		packages.forEach((pkgName, classes) -> {
			EObject pkg = createPackage(pkgName);
			extent.getContents().add(pkg);
			for (String className : classes) {
				addClassToPackage(pkg, createClass(className, null));
			}
		});
		return extent;
	}

	private static Map<String, List<String>> layout(QvtdModelExtent rdbms) {
		Map<String, List<String>> layout = new TreeMap<>();
		for (EObject schema : rdbms.getContents()) {
			if ("Schema".equals(schema.eClass().getName())) {
				layout.put(getName(schema), getTables(schema).stream().map(t -> getName(t)).sorted().toList());
			}
		}
		return layout;
	}

	private static Map<String, List<String>> run(String source, QvtdModelExtent uml) throws QvtdParseException {
		QvtdModelExtent rdbms = emptyExtent();
		QvtdExecutionResult result = executeEnforce(source, "rdbms", Map.of("uml", uml, "rdbms", rdbms));
		assertTrue(result.isSuccess(), "" + result.diagnostics());
		return layout(rdbms);
	}

	// ── a when call binds every trace ──

	@Test
	@DisplayName("a when call binds every matching trace, not the first")
	void whenCall_bindsEveryTrace() throws QvtdParseException {
		assertEquals(Map.of("P1", List.of("C1"), "P2", List.of("C2")),
				run(transformation(PACKAGE_TO_SCHEMA, CLASS_TO_TABLE), uml(Map.of("P1", List.of("C1"), "P2", List.of("C2")))));
	}

	@Test
	@DisplayName("the first package without classes does not take the tables of the next")
	void whenCall_firstPackageEmpty() throws QvtdParseException {
		assertEquals(Map.of("P1", List.of(), "P2", List.of("C1", "C2")),
				run(transformation(PACKAGE_TO_SCHEMA, CLASS_TO_TABLE), uml(Map.of("P1", List.of(), "P2", List.of("C1", "C2")))));
	}

	@Test
	@DisplayName("three packages, the middle one empty")
	void whenCall_threePackagesMiddleEmpty() throws QvtdParseException {
		assertEquals(Map.of("P1", List.of("C1"), "P2", List.of(), "P3", List.of("C3")),
				run(transformation(PACKAGE_TO_SCHEMA, CLASS_TO_TABLE),
						uml(Map.of("P1", List.of("C1"), "P2", List.of(), "P3", List.of("C3")))));
	}

	@Test
	@DisplayName("a class without a package gets no table, and that is no error")
	void whenCall_classWithoutPackage_noTable() throws QvtdParseException {
		QvtdModelExtent uml = uml(Map.of("P1", List.of("C1")));
		uml.getContents().add(createClass("Loose", null));
		assertEquals(Map.of("P1", List.of("C1")), run(transformation(PACKAGE_TO_SCHEMA, CLASS_TO_TABLE), uml));
	}

	@Test
	@DisplayName("a package PackageToSchema does not hold for leaves its classes without tables")
	void whenCall_packageWithoutTrace_noTable() throws QvtdParseException {
		String guarded = PACKAGE_TO_SCHEMA.replace("enforce domain rdbms s : Schema { name = pn };",
				"enforce domain rdbms s : Schema { name = pn };\n\t\t\t\twhen { pn <> 'P2'; }");
		assertEquals(Map.of("P1", List.of("C1")),
				run(transformation(guarded, CLASS_TO_TABLE), uml(Map.of("P1", List.of("C1"), "P2", List.of("C2")))));
	}

	// ── the order of declaration does not matter ──

	@Test
	@DisplayName("a relation declared before the one its when clause calls gives the same result")
	void topRelations_reverseDeclarationOrder() throws QvtdParseException {
		assertEquals(Map.of("P1", List.of("C1"), "P2", List.of("C2")),
				run(transformation(CLASS_TO_TABLE, PACKAGE_TO_SCHEMA), uml(Map.of("P1", List.of("C1"), "P2", List.of("C2")))));
	}

	@Test
	@DisplayName("three levels of when dependencies, declared in reverse")
	void topRelations_threeLevelsReverseOrder() throws QvtdParseException {
		QvtdModelExtent uml = uml(Map.of("P1", List.of("C1")));
		EObject c1 = ((List<?>) uml.getContents().get(0).eGet(
				uml.getContents().get(0).eClass().getEStructuralFeature("ownedClass"))).stream()
				.map(EObject.class::cast).findFirst().orElseThrow();
		addAttributeToClass(c1, createAttribute("id", "Integer"));
		QvtdModelExtent rdbms = emptyExtent();

		QvtdExecutionResult result = executeEnforce(transformation(ATTRIBUTE_TO_COLUMN, CLASS_TO_TABLE, PACKAGE_TO_SCHEMA),
				"rdbms", Map.of("uml", uml, "rdbms", rdbms));

		assertTrue(result.isSuccess(), "" + result.diagnostics());
		EObject table = getTables(rdbms.getContents().get(0)).get(0);
		assertEquals(List.of("id"), getColumns(table).stream().map(col -> getName(col)).toList());
	}

	@Test
	@DisplayName("relations whose when clauses call each other end without a start binding")
	void whenCall_cyclicDependency_terminates() throws QvtdParseException {
		String cycle = transformation("""
				top relation A {
					pn : String;
					s : Schema;
					checkonly domain uml p : Package { name = pn };
					enforce domain rdbms s2 : Schema { name = pn };
					when { B(p, s); }
				}
				""", """
				top relation B {
					pn : String;
					s : Schema;
					checkonly domain uml p : Package { name = pn };
					enforce domain rdbms s2 : Schema { name = pn };
					when { A(p, s); }
				}
				""");
		assertEquals(Map.of(), run(cycle, uml(Map.of("P1", List.of("C1")))));
	}

	// ── a trace only for a relation that holds ──

	@Test
	@DisplayName("[?] without a match: the relation holds, its trace lets the dependent relation run")
	void optionalNullBinding_recordsATrace() throws QvtdParseException {
		// Decided for #349: a [?] root bound to null makes the relation hold for that binding,
		// for top relations as for non-top ones, and when clauses that depend on it find it
		String source = transformation("""
				top relation MaybeClass {
					cn : String;
					checkonly domain uml c : Class[?] { name = cn };
					enforce domain rdbms t : Table { name = cn };
				}
				""", """
				top relation PackageAfterClass {
					pn : String;
					c : Class;
					t : Table;
					checkonly domain uml p : Package { name = pn };
					enforce domain rdbms s : Schema { name = pn };
					when { MaybeClass(c, t); }
				}
				""");
		QvtdModelExtent uml = QvtdModelExtent.of(createPackage("P1"));
		assertEquals(Map.of("P1", List.of()), run(source, uml));
	}

	@Test
	@DisplayName("a binding whose target could not be enforced leaves no trace")
	void failedEnforce_noTrace_dependentRelationSkipped() throws QvtdParseException {
		// A metamodel whose Holder is abstract: PackageToHolder cannot create one, so it does not
		// hold, and ClassToItem — which needs it — does not run for the package's class
		EPackage mm = EcoreFactory.eINSTANCE.createEPackage();
		mm.setName("holders");
		mm.setNsURI("http://example.org/m2x/qvtr-holders/1.0");
		mm.setNsPrefix("holders");
		EClass holder = eClass(mm, "Holder", true);
		eClass(mm, "Item", false);
		assertTrue(holder.isAbstract());

		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(mm.getNsURI(), mm);
		registry.put(umlPackage.getNsURI(), umlPackage);
		QvtdEngine holdersEngine = QvtdEngines.create(QvtdConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build()).packageRegistry(registry).build());
		String source = """
				transformation T(uml : simpleuml, out : holders) {
					top relation PackageToHolder {
						pn : String;
						checkonly domain uml p : Package { name = pn };
						enforce domain out h : Holder { name = pn };
					}
					top relation ClassToItem {
						cn : String;
						p : Package;
						h : Holder;
						checkonly domain uml c : Class { owningPackage = p, name = cn };
						enforce domain out i : Item { name = cn };
						when { PackageToHolder(p, h); }
					}
				}
				""";
		EObject pkg = createPackage("P1");
		addClassToPackage(pkg, createClass("C1", null));
		QvtdModelExtent out = QvtdModelExtent.of();

		QvtdExecutionResult result = holdersEngine.execute(holdersEngine.parse(source, "T"),
				QvtdExecutionContext.enforce("out", Map.of("uml", QvtdModelExtent.of(pkg), "out", out)));

		Optional<Diagnostic> error = result.diagnostics().stream()
				.filter(d -> d.getSeverity() == Diagnostic.ERROR).findFirst();
		assertTrue(error.isPresent() && error.get().getMessage().contains("abstract"), "" + result.diagnostics());
		assertEquals(List.of(), out.getContents(), "no Holder, and no Item for the package's class");
	}

	private static EClass eClass(EPackage pkg, String name, boolean isAbstract) {
		EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		eClass.setName(name);
		eClass.setAbstract(isAbstract);
		EAttribute nameAttr = EcoreFactory.eINSTANCE.createEAttribute();
		nameAttr.setName("name");
		nameAttr.setEType(EcorePackage.Literals.ESTRING);
		eClass.getEStructuralFeatures().add(nameAttr);
		pkg.getEClassifiers().add(eClass);
		return eClass;
	}
}
