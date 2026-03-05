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
package org.eclipse.fennec.m2x.m2t.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests based on MOFM2T v1.0 specification §7 examples.
 *
 * <p>The spec uses UML (Class, Attribute, Operation). These tests
 * adapt the examples to Ecore (EClass, EAttribute, EOperation).
 */
class M2tSpecExampleTest {

	private M2tEngineImpl engine;
	private EClass employeeClass;
	private EPackage pkg;

	@BeforeEach
	void setUp() {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		M2tConfiguration config = M2tConfiguration.builder(oclConfig).build();
		engine = new M2tEngineImpl(config);

		// Model: EPackage "company" with EClass "Employee" having 2 EAttributes
		pkg = EcoreFactory.eINSTANCE.createEPackage();
		pkg.setName("company");
		pkg.setNsURI("http://company");

		employeeClass = EcoreFactory.eINSTANCE.createEClass();
		employeeClass.setName("Employee");
		pkg.getEClassifiers().add(employeeClass);

		var nameAttr = EcoreFactory.eINSTANCE.createEAttribute();
		nameAttr.setName("name");
		nameAttr.setEType(EcorePackage.Literals.ESTRING);
		employeeClass.getEStructuralFeatures().add(nameAttr);

		var salaryAttr = EcoreFactory.eINSTANCE.createEAttribute();
		salaryAttr.setName("salary");
		salaryAttr.setEType(EcorePackage.Literals.EDOUBLE);
		employeeClass.getEStructuralFeatures().add(salaryAttr);

		// Add an EOperation
		var computeOp = EcoreFactory.eINSTANCE.createEOperation();
		computeOp.setName("computeTax");
		computeOp.setEType(EcorePackage.Literals.EDOUBLE);
		employeeClass.getEOperations().add(computeOp);
	}

	private M2tResult executeOnClass(String mtlSource) throws M2tParseException {
		Module module = engine.parse(mtlSource, "spec_test");
		return engine.execute(module, M2tContext.of(employeeClass));
	}

	private M2tResult executeOnPackage(String mtlSource) throws M2tParseException {
		Module module = engine.parse(mtlSource, "spec_test");
		return engine.execute(module, M2tContext.of(pkg));
	}

	private String getFile(M2tResult result, String name) {
		Map<String, String> files = result.generatedFiles();
		assertNotNull(files.get(name), "File '" + name + "' not in: " + files.keySet());
		return files.get(name);
	}

	// ==================== §7.0 — Template Basics ====================

	@Nested
	@DisplayName("§7.0 — Template Basics")
	class TemplateBasics {

		@Test
		@DisplayName("simple class template — §7.0 Ex.1 (adapted to Ecore)")
		void classTemplate() throws M2tParseException {
			// Spec §7.0 Ex.1: template that generates a class definition
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(c : EClass)]\n" +
					"[file (c.name.concat('.java'), false)]\n" +
					"class [c.name/]\n" +
					"{\n" +
					"  // Constructor\n" +
					"  [c.name/]()\n" +
					"  {\n" +
					"  }\n" +
					"}\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeOnClass(mtl);
			assertTrue(result.isSuccess());
			String content = getFile(result, "Employee.java");
			assertEquals(
					"class Employee\n" +
					"{\n" +
					"  // Constructor\n" +
					"  Employee()\n" +
					"  {\n" +
					"  }\n" +
					"}", content);
		}

		@Test
		@DisplayName("for block with attributes — §7.0 Ex.3 (adapted to Ecore)")
		void forBlockWithAttributes() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(c : EClass)]\n" +
					"[file (c.name.concat('.java'), false)]\n" +
					"class [c.name/]\n" +
					"{\n" +
					"  // Attributes\n" +
					"[for (a : EAttribute | c.eAttributes)]\n" +
					"  [a.eType.name/] [a.name/];\n" +
					"[/for]\n" +
					"}\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeOnClass(mtl);
			String content = getFile(result, "Employee.java");
			// For body produces "  EString name;\n  EDouble salary;"
			// then "}" follows directly (no newline — [/for] standalone stripping consumed it)
			assertEquals(
					"class Employee\n" +
					"{\n" +
					"  // Attributes\n" +
					"  EString name;\n" +
					"  EDouble salary;}", content);
		}

		@Test
		@DisplayName("for block with separator — §7.0 (adapted)")
		void forBlockWithSeparator() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(c : EClass)]\n" +
					"[file ('attrs', false)]\n" +
					"[for (a : EAttribute | c.eAttributes) separator (', ')]\n" +
					"[a.name/]\n" +
					"[/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeOnClass(mtl);
			String content = getFile(result, "attrs");
			assertEquals("name, salary", content);
		}
	}

	// ==================== §7.0 — Template Invocation ====================

	@Nested
	@DisplayName("§7.0 — Template Invocation")
	class TemplateInvocationTests {

		@Test
		@DisplayName("template invoking another template — §7.0 (adapted)")
		void templateInvocation() throws M2tParseException {
			// Spec §7.0: classToJava invokes attributeToJava
			// Indent propagation: indent added after newlines, not on first line
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(c : EClass)]\n" +
					"[file ('out', false)]\n" +
					"class [c.name/] {\n" +
					"  [attrDecl(c)/]\n" +
					"}\n" +
					"[/file]\n" +
					"[/template]\n" +
					"[template public attrDecl(c : EClass)]\n" +
					"[for (a : EAttribute | c.eAttributes) separator ('\\n')]\n" +
					"[a.eType.name/] [a.name/];\n" +
					"[/for]\n" +
					"[/template]\n";
			M2tResult result = executeOnClass(mtl);
			String content = getFile(result, "out");
			// First line of invoked template not indented, subsequent lines get "  " indent
			assertEquals(
					"class Employee {\n" +
					"EString name;\n" +
					"  EDouble salary;\n" +
					"}", content);
		}
	}

	// ==================== §7.0 — Query ====================

	@Nested
	@DisplayName("§7.0 — Query")
	class QueryTests {

		@Test
		@DisplayName("query invocation — adapted allOperations")
		void queryInvocation() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[query public opCount(c : EClass) : Integer = c.eOperations->size()/]\n" +
					"[template public main(c : EClass)]\n" +
					"[file ('out', false)]\n" +
					"[c.name/] has [c.opCount()/] operations\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeOnClass(mtl);
			String content = getFile(result, "out");
			assertEquals("Employee has 1 operations", content);
		}
	}

	// ==================== §7.3 — File Block ====================

	@Nested
	@DisplayName("§7.3 — File Block")
	class FileBlockTests {

		@Test
		@DisplayName("file block with append mode — §7.3")
		void fileBlockAppend() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[for (c : EClassifier | p.eClassifiers)]\n" +
					"[file ('log.txt', true)]\n" +
					"processing [c.name/]\n" +
					"[/file]\n" +
					"[/for]\n" +
					"[/template]\n";
			M2tResult result = executeOnPackage(mtl);
			String content = getFile(result, "log.txt");
			assertEquals("processing Employee", content);
		}

		@Test
		@DisplayName("nested file blocks — main file + log")
		void nestedFileBlocks() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(c : EClass)]\n" +
					"[file (c.name.concat('.java'), false)]\n" +
					"[file ('log.txt', true)]generating [c.name/][/file]\n" +
					"class [c.name/] {}\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeOnClass(mtl);
			assertEquals("generating Employee", getFile(result, "log.txt"));
			assertEquals("class Employee {}", getFile(result, "Employee.java"));
		}
	}

	// ==================== §7.4 — Macro ====================

	@Nested
	@DisplayName("§7.4 — Macro")
	class MacroTests {

		@Test
		@DisplayName("simple macro invocation — §7.4 (adapted)")
		void simpleMacro() throws M2tParseException {
			// Simple macro (self-closing invocation, no Body param)
			String mtl =
					"[module m(Ecore)/]\n" +
					"[macro wrap(c : EClass)]<[c.name/]>[/macro]\n" +
					"[template public main(c : EClass)]\n" +
					"[file ('out', false)]\n" +
					"Result: [wrap(c)/]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeOnClass(mtl);
			String content = getFile(result, "out");
			assertEquals("Result: <Employee>", content);
		}
	}

	// ==================== §7.2 — Protected Area ====================

	@Nested
	@DisplayName("§7.2 — Protected Area")
	class ProtectedAreaTests {

		@Test
		@DisplayName("protected block with unique id — §7.2 (adapted)")
		void protectedArea() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(c : EClass)]\n" +
					"[file (c.name.concat('.java'), false)]\n" +
					"class [c.name/] {\n" +
					"  [c.name/]() {\n" +
					"    [protected ('user_code')]\n" +
					"    // user code here\n" +
					"    [/protected]\n" +
					"  }\n" +
					"}\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeOnClass(mtl);
			String content = getFile(result, "Employee.java");
			// Verify key structural elements
			assertTrue(content.contains("class Employee {"), "class declaration");
			assertTrue(content.contains("Employee() {"), "constructor");
			assertTrue(content.contains("[protected"), "protected area start marker");
			assertTrue(content.contains("user_code"), "protected area id");
			assertTrue(content.contains("[/protected]"), "protected area end marker");
			assertTrue(content.contains("// user code here"), "protected body content");
		}
	}
}
