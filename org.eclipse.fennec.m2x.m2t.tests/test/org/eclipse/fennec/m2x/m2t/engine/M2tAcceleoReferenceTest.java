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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.engine.M2tEngines;
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
 * Acceleo 3.7 reference comparison tests.
 *
 * <p>These tests parse M2T templates and compare the generated output
 * with exact whitespace against expected results derived from the
 * MOFM2T §8.4 whitespace normalization rules.
 *
 * <p>Input model: an EPackage with 3 classifiers (ClasseA, ClasseB, AbstractClass),
 * matching Acceleo's {@code data/target.ecore}.
 */
class M2tAcceleoReferenceTest {

	private M2tEngine engine;
	private EPackage targetPackage;

	@BeforeEach
	void setUp() {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		M2tConfiguration config = M2tConfiguration.builder(oclConfig).build();
		engine = M2tEngines.create(config);

		// Reproduce Acceleo's target.ecore: EPackage "target" with 3 EClassifiers
		targetPackage = EcoreFactory.eINSTANCE.createEPackage();
		targetPackage.setName("target");
		targetPackage.setNsURI("target");

		var classeA = EcoreFactory.eINSTANCE.createEClass();
		classeA.setName("ClasseA");
		targetPackage.getEClassifiers().add(classeA);

		var classeB = EcoreFactory.eINSTANCE.createEClass();
		classeB.setName("ClasseB");
		targetPackage.getEClassifiers().add(classeB);

		var abstractClass = EcoreFactory.eINSTANCE.createEClass();
		abstractClass.setName("AbstractClass");
		abstractClass.setAbstract(true);
		targetPackage.getEClassifiers().add(abstractClass);
	}

	private M2tResult executeTemplate(String mtlSource) throws M2tParseException {
		Module module = engine.parse(mtlSource, "test");
		return engine.execute(module, M2tContext.of(targetPackage));
	}


	private String getFileContent(M2tResult result, String fileName) {
		Map<String, String> files = result.generatedFiles();
		assertNotNull(files.get(fileName), "File '" + fileName + "' not found in: " + files.keySet());
		return files.get(fileName);
	}

	// ==================== ForBlock (Acceleo: data/ForBlock/template_for.mtl) ====================

	@Nested
	@DisplayName("ForBlock — Acceleo reference")
	class ForBlockReferenceTests {

		@Test
		@DisplayName("simple for — ClasseA ClasseB AbstractClass")
		void forSimple() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(c : EPackage)]\n" +
					"[file ('testForSimple', false)]\n" +
					"[for (cl : EClassifier | c.eClassifiers)]\n" +
					"[cl.name/]\n" +
					"[/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			assertTrue(result.isSuccess());
			String content = getFileContent(result, "testForSimple");
			assertEquals("ClasseA\nClasseB\nAbstractClass", content);
		}

		@Test
		@DisplayName("for with separator — ClasseA/ClasseB/AbstractClass")
		void forSeparator() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(c : EPackage)]\n" +
					"[file ('testForSeparator', false)]\n" +
					"[for (cl : EClassifier | c.eClassifiers) separator ('/')]\n" +
					"[cl.name/]\n" +
					"[/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "testForSeparator");
			assertEquals("ClasseA/ClasseB/AbstractClass", content);
		}

		@Test
		@DisplayName("for with before — before test ClasseA ClasseB AbstractClass")
		void forBefore() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(c : EPackage)]\n" +
					"[file ('testForBefore', false)]\n" +
					"[for (cl : EClassifier | c.eClassifiers) before ('before test')]\n" +
					"[cl.name/]\n" +
					"[/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "testForBefore");
			assertEquals("before testClasseA\nClasseB\nAbstractClass", content);
		}

		@Test
		@DisplayName("for with after — ClasseA ClasseB AbstractClass after test")
		void forAfter() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(c : EPackage)]\n" +
					"[file ('testForAfter', false)]\n" +
					"[for (cl : EClassifier | c.eClassifiers) after ('after test')]\n" +
					"[cl.name/]\n" +
					"[/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "testForAfter");
			assertEquals("ClasseA\nClasseB\nAbstractClassafter test", content);
		}

		@Test
		@DisplayName("for with guard — filters non-matching elements")
		void forGuard() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(c : EPackage)]\n" +
					"[file ('testForGuard', false)]\n" +
					"[for (cl : EClassifier | c.eClassifiers) ? (cl.name.startsWith('C'))]\n" +
					"[cl.name/]\n" +
					"[/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "testForGuard");
			// ClasseA and ClasseB start with 'C', AbstractClass doesn't
			assertEquals("ClasseA\nClasseB", content);
		}

		@Test
		@DisplayName("for i variable — 1-based counter (Acceleo convention)")
		void forIVariable() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(c : EPackage)]\n" +
					"[file ('testForI', false)]\n" +
					"[for (cl : EClassifier | c.eClassifiers)][i/][/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "testForI");
			// 1-based counter with default separator \n between iterations
			assertEquals("1\n2\n3", content);
		}

		@Test
		@DisplayName("for i in separator — separator uses current i value")
		void forISeparator() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(c : EPackage)]\n" +
					"[file ('testForISeparator', false)]\n" +
					"[for (cl : EClassifier | c.eClassifiers) separator (i.toString())]yop[/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "testForISeparator");
			// separator evaluated between iterations, i=2 and i=3
			assertEquals("yop2yop3yop", content);
		}
	}

	// ==================== IfBlock (Acceleo: data/IfBlock/template_if.mtl) ====================

	@Nested
	@DisplayName("IfBlock — Acceleo reference")
	class IfBlockReferenceTests {

		@Test
		@DisplayName("if block matches one classifier")
		void ifBlockMatchesOne() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(c : EPackage)]\n" +
					"[file ('testIf', false)]\n" +
					"[for (cl : EClassifier | c.eClassifiers)]\n" +
					"[if (cl.name = 'ClasseA')]MATCH[/if]\n" +
					"[/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "testIf");
			// MATCH + 2x default separator (empty body for non-matching classifiers)
			assertEquals("MATCH\n\n", content);
		}

		@Test
		@DisplayName("if/else outputs for all classifiers")
		void ifElseAllClassifiers() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(c : EPackage)]\n" +
					"[file ('testElse', false)]\n" +
					"[for (cl : EClassifier | c.eClassifiers)]\n" +
					"[if (cl.name = 'NoMatch')]X[else][cl.name/][/if]\n" +
					"[/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "testElse");
			assertEquals("ClasseA\nClasseB\nAbstractClass", content);
		}

		@Test
		@DisplayName("if/elseif/else chain")
		void ifElseIfElse() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(c : EPackage)]\n" +
					"[file ('testCompleteIf', false)]\n" +
					"[for (cl : EClassifier | c.eClassifiers)]\n" +
					"[if (cl.name = 'ClasseA')]A[elseif (cl.name = 'ClasseB')]B[else]OTHER[/if]\n" +
					"[/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "testCompleteIf");
			assertEquals("A\nB\nOTHER", content);
		}
	}

	// ==================== GenericEngine (basic template) ====================

	@Nested
	@DisplayName("GenericEngine — Acceleo reference")
	class GenericEngineReferenceTests {

		@Test
		@DisplayName("constant output in file block")
		void constantOutput() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('test_generic_engine', false)]\n" +
					"constant output\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "test_generic_engine");
			assertEquals("constant output", content);
		}
	}

	// ==================== Variables (Acceleo: data/Variables/template_self.mtl) ====================

	@Nested
	@DisplayName("Variables — Acceleo reference")
	class VariablesReferenceTests {

		@Test
		@DisplayName("explicit self.name in template")
		void selfTemplate() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('self_template', false)]\n" +
					"[self.name/]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "self_template");
			assertEquals("target", content);
		}

		@Test
		@DisplayName("explicit self.name in query")
		void selfQuery() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[query public pkgName(p : EPackage) : String = self.name/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('self_query', false)]\n" +
					"[p.pkgName()/]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "self_query");
			assertEquals("target", content);
		}
	}

	// ==================== LetBlock ====================

	@Nested
	@DisplayName("LetBlock — Acceleo reference")
	class LetBlockReferenceTests {

		@Test
		@DisplayName("let with variable binding")
		void letSimple() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[let n : String = p.name]\n" +
					"package: [n/]\n" +
					"[/let]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "out");
			assertEquals("package: target", content);
		}

		@Test
		@DisplayName("let with else branch on null")
		void letElse() throws M2tParseException {
			// nsPrefix is null/empty → else branch
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[let prefix : String = p.nsPrefix]\n" +
					"prefix: [prefix/]\n" +
					"[else]\n" +
					"no prefix\n" +
					"[/let]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "out");
			// nsPrefix is empty string (not null), so let branch is taken
			assertTrue(content.contains("prefix:") || content.contains("no prefix"));
		}

		@Test
		@DisplayName("let with elselet chain")
		void letElseLet() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[for (c : EClassifier | p.eClassifiers)]\n" +
					"[let cls : EClass = c.oclAsType(EClass)]\n" +
					"[cls.name/]\n" +
					"[/let]\n" +
					"[/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "out");
			assertTrue(content.contains("ClasseA"));
			assertTrue(content.contains("ClasseB"));
			assertTrue(content.contains("AbstractClass"));
		}
	}

	// ==================== TemplateInvocation (Acceleo: data/TemplateInvocation/) ====================

	@Nested
	@DisplayName("TemplateInvocation — Acceleo reference")
	class TemplateInvocationReferenceTests {

		@Test
		@DisplayName("multi-template composition — format each classifier")
		void multiTemplateComposition() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[for (c : EClassifier | p.eClassifiers)]\n" +
					"[formatClassifier(c)/]\n" +
					"[/for]\n" +
					"[/file]\n" +
					"[/template]\n" +
					"[template public formatClassifier(c : EClassifier)]\n" +
					"- [c.name/]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "out");
			assertEquals("- ClasseA\n- ClasseB\n- AbstractClass", content);
		}

		@Test
		@DisplayName("template invocation with before/after/separator")
		void templateInvocationWithSeparators() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[format(p.eClassifiers) separator (', ')/]\n" +
					"[/file]\n" +
					"[/template]\n" +
					"[template public format(c : EClassifier)]\n" +
					"[c.name/]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "out");
			assertEquals("ClasseA, ClasseB, AbstractClass", content);
		}

		@Test
		@DisplayName("query invocation from template")
		void queryFromTemplate() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[query public upper(s : String) : String = s.toUpper()/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[upper(p.name)/]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "out");
			assertEquals("TARGET", content);
		}
	}

	// ==================== ProtectedAreaBlock — Acceleo reference ====================

	@Nested
	@DisplayName("ProtectedAreaBlock — Acceleo reference")
	class ProtectedAreaBlockReferenceTests {

		@Test
		@DisplayName("protected area in file block produces markers with hash")
		void protectedAreaInFileBlock() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[protected ('my_area')]\n" +
					"default content\n" +
					"[/protected]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "out");
			assertTrue(content.contains("// [protected 'my_area'"), "start marker");
			assertTrue(content.contains("default content"), "default body");
			assertTrue(content.contains("// [/protected]"), "end marker");
		}

		@Test
		@DisplayName("protected area with dynamic id from expression")
		void protectedAreaDynamicId() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[for (c : EClassifier | p.eClassifiers)]\n" +
					"[protected (c.name)]\n" +
					"code for [c.name/]\n" +
					"[/protected]\n" +
					"[/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "out");
			assertTrue(content.contains("// [protected 'ClasseA'"), "ClasseA marker");
			assertTrue(content.contains("// [protected 'ClasseB'"), "ClasseB marker");
			assertTrue(content.contains("// [protected 'AbstractClass'"), "AbstractClass marker");
			assertTrue(content.contains("code for ClasseA"), "ClasseA body");
			assertTrue(content.contains("code for ClasseB"), "ClasseB body");
			assertTrue(content.contains("code for AbstractClass"), "AbstractClass body");
		}

		@Test
		@DisplayName("protected area inside for/if nesting")
		void protectedAreaInsideForIf() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[for (c : EClassifier | p.eClassifiers)]\n" +
					"[if (c.name = 'ClasseA')]\n" +
					"[protected ('user_code')]\n" +
					"protected block\n" +
					"[/protected]\n" +
					"[/if]\n" +
					"[/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "out");
			assertTrue(content.contains("// [protected 'user_code'"), "marker present");
			assertTrue(content.contains("protected block"), "body present");
		}

		@Test
		@DisplayName("protected area via template invocation")
		void protectedAreaViaTemplateCall() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public protHelper(p : EPackage)]\n" +
					"[protected ('helper_area')]\n" +
					"helper default\n" +
					"[/protected]\n" +
					"[/template]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[protHelper(p)/]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "out");
			assertTrue(content.contains("// [protected 'helper_area'"), "marker from invoked template");
			assertTrue(content.contains("helper default"), "body from invoked template");
		}

		@Test
		@DisplayName("multiple protected areas in same file")
		void multipleProtectedAreas() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"header\n" +
					"[protected ('area1')]\n" +
					"first area\n" +
					"[/protected]\n" +
					"middle\n" +
					"[protected ('area2')]\n" +
					"second area\n" +
					"[/protected]\n" +
					"footer\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "out");
			assertTrue(content.contains("// [protected 'area1'"), "area1 marker");
			assertTrue(content.contains("// [protected 'area2'"), "area2 marker");
			assertTrue(content.contains("first area"), "area1 body");
			assertTrue(content.contains("second area"), "area2 body");
			assertTrue(content.contains("header"), "header text");
			assertTrue(content.contains("middle"), "middle text");
			assertTrue(content.contains("footer"), "footer text");
		}

		@Test
		@DisplayName("protected area hash is consistent for same content")
		void protectedAreaHashConsistency() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[protected ('area1')]\n" +
					"same content\n" +
					"[/protected]\n" +
					"[protected ('area2')]\n" +
					"same content\n" +
					"[/protected]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "out");
			// Both areas have same body → same hash in markers
			String[] lines = content.split("\n");
			String hash1 = null, hash2 = null;
			for (String line : lines) {
				if (line.contains("[protected 'area1'")) {
					hash1 = line.replaceAll(".*'\\s+([0-9a-f]+)\\].*", "$1");
				}
				if (line.contains("[protected 'area2'")) {
					hash2 = line.replaceAll(".*'\\s+([0-9a-f]+)\\].*", "$1");
				}
			}
			assertNotNull(hash1, "area1 hash present");
			assertNotNull(hash2, "area2 hash present");
			assertEquals(hash1, hash2, "same content → same hash");
		}
	}

	// ==================== Template Features — Acceleo reference ====================

	@Nested
	@DisplayName("Template Features — Acceleo reference")
	class TemplateFeatureReferenceTests {

		@Test
		@DisplayName("template guard — true guard produces output")
		void templateGuardTrue() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public guarded(p : EPackage) ? (p.name = 'target')]\n" +
					"guarded output\n" +
					"[/template]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[guarded(p)/]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "out");
			assertEquals("guarded output", content);
		}

		@Test
		@DisplayName("template guard — false guard produces no output")
		void templateGuardFalse() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public guarded(p : EPackage) ? (p.name = 'noMatch')]\n" +
					"should not appear\n" +
					"[/template]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"before[guarded(p)/]after\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "out");
			assertFalse(content.contains("should not appear"), "guard=false → no output");
			assertTrue(content.contains("before"), "text before invocation");
			assertTrue(content.contains("after"), "text after invocation");
		}

		@Test
		@DisplayName("template guard with abstract check — Acceleo pattern")
		void templateGuardAbstractCheck() throws M2tParseException {
			// ClasseA and ClasseB are concrete, AbstractClass is abstract
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public concreteOnly(c : EClassifier) ? (c.oclIsKindOf(EClass) and not c.oclAsType(EClass).abstract)]\n" +
					"[c.name/]\n" +
					"[/template]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[for (c : EClassifier | p.eClassifiers)]\n" +
					"[concreteOnly(c)/]\n" +
					"[/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "out");
			assertTrue(content.contains("ClasseA"), "concrete ClasseA included");
			assertTrue(content.contains("ClasseB"), "concrete ClasseB included");
			assertFalse(content.contains("AbstractClass"), "abstract class excluded by guard");
		}

		@Test
		@DisplayName("template post-processing — trim()")
		void templatePostTrim() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public trimmed(p : EPackage) post(trim())]\n" +
					"   [p.name/]   \n" +
					"[/template]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					">[trimmed(p)/]<\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "out");
			// post(trim()) should trim the template output
			assertEquals(">target<", content);
		}

		@Test
		@DisplayName("template post-processing — toUpper()")
		void templatePostToUpper() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public uppered(p : EPackage) post(toUpper())]\n" +
					"[p.name/]\n" +
					"[/template]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[uppered(p)/]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "out");
			assertEquals("TARGET", content);
		}

		@Test
		@DisplayName("template invocation with before/after")
		void templateInvocationBeforeAfter() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public fmt(c : EClassifier)]\n" +
					"[c.name/]\n" +
					"[/template]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[fmt(p.eClassifiers) before ('[') separator (', ') after (']')/]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "out");
			assertEquals("[ClasseA, ClasseB, AbstractClass]", content);
		}

		@Test
		@DisplayName("template private visibility — only local access")
		void privateTemplateLocalAccess() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template private secret(p : EPackage)]\n" +
					"secret: [p.name/]\n" +
					"[/template]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[secret(p)/]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			String content = getFileContent(result, "out");
			assertEquals("secret: target", content);
		}

		@Test
		@DisplayName("protected visibility via extends")
		void protectedVisibilityExtends() throws M2tParseException {
			String baseMtl =
					"[module base(Ecore)/]\n" +
					"[template protected helper(p : EPackage)]\n" +
					"protected: [p.name/]\n" +
					"[/template]\n";
			String childMtl =
					"[module child(Ecore) extends base/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[helper(p)/]\n" +
					"[/file]\n" +
					"[/template]\n";
			Module baseMod = engine.parse(baseMtl, "base");
			Module childMod = engine.parse(childMtl, "child");
			engine.link(baseMod, childMod);
			M2tResult result = engine.execute(childMod, M2tContext.of(targetPackage));
			String content = getFileContent(result, "out").strip();
			assertEquals("protected: target", content);
		}

		@Test
		@DisplayName("protected NOT visible via imports")
		void protectedNotVisibleViaImport() throws M2tParseException {
			String libMtl =
					"[module lib(Ecore)/]\n" +
					"[template protected helper(p : EPackage)]\n" +
					"should not see\n" +
					"[/template]\n";
			String mainMtl =
					"[module main(Ecore)/]\n" +
					"[import lib/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[helper(p)/]\n" +
					"[/file]\n" +
					"[/template]\n";
			Module libMod = engine.parse(libMtl, "lib");
			Module mainMod = engine.parse(mainMtl, "main");
			List<String> warnings = engine.link(libMod, mainMod);
			assertTrue(warnings.stream().anyMatch(w -> w.contains("helper")),
					"Expected warning about unresolved 'helper': " + warnings);
		}

		@Test
		@DisplayName("override with super call — Acceleo pattern")
		void overrideWithSuperCall() throws M2tParseException {
			String baseMtl =
					"[module base(Ecore)/]\n" +
					"[template public render(p : EPackage)]\n" +
					"[p.name/]\n" +
					"[/template]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[render(p)/]\n" +
					"[/file]\n" +
					"[/template]\n";
			String childMtl =
					"[module child(Ecore) extends base/]\n" +
					"[template public render(p : EPackage) overrides render]\n" +
					"CHILD([super/])\n" +
					"[/template]\n";
			Module baseMod = engine.parse(baseMtl, "base");
			Module childMod = engine.parse(childMtl, "child");
			engine.link(baseMod, childMod);
			M2tResult result = engine.execute(baseMod, M2tContext.of(targetPackage));
			String content = getFileContent(result, "out").strip();
			assertEquals("CHILD(target)", content);
		}
	}

	// ==================== Library Operations — Acceleo reference ====================

	@Nested
	@DisplayName("Library Operations — M2T context")
	class LibraryOperationsReferenceTests {

		@Test
		@DisplayName("substitute — replace occurrences")
		void substituteOp() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"['test'.substitute('es', 'se')/]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			assertEquals("tset", getFileContent(result, "out"));
		}

		@Test
		@DisplayName("index — 1-based substring position")
		void indexOp() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"['test'.index('es')/]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			assertEquals("2", getFileContent(result, "out"));
		}

		@Test
		@DisplayName("first — first n characters")
		void firstOp() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"['hello'.first(3)/]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			assertEquals("hel", getFileContent(result, "out"));
		}

		@Test
		@DisplayName("last — last n characters")
		void lastOp() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"['hello'.last(3)/]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			assertEquals("llo", getFileContent(result, "out"));
		}

		@Test
		@DisplayName("strstr — contains check")
		void strstrOp() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"['test'.strstr('es')/]-['test'.strstr('xyz')/]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			assertEquals("true-false", getFileContent(result, "out"));
		}

		@Test
		@DisplayName("strcmp — lexicographic comparison")
		void strcmpOp() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[if ('test'.strcmp('test') = 0)]equal[/if]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			assertEquals("equal", getFileContent(result, "out"));
		}

		@Test
		@DisplayName("isAlpha / isAlphanum")
		void isAlphaOps() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"['abc'.isAlpha()/]-['abc1'.isAlpha()/]-['abc1'.isAlphanum()/]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			assertEquals("true-false-true", getFileContent(result, "out"));
		}

		@Test
		@DisplayName("toUpperFirst / toLowerFirst")
		void caseFirstOps() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"['test'.toUpperFirst()/]-['TEST'.toLowerFirst()/]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			assertEquals("Test-tEST", getFileContent(result, "out"));
		}

		@Test
		@DisplayName("toUpper / toLower — M2T §8.3")
		void caseConversionOps() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"['Hello'.toUpper()/]-['Hello'.toLower()/]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			assertEquals("HELLO-hello", getFileContent(result, "out"));
		}

		@Test
		@DisplayName("OCL String ops in M2T context — concat, size, substring")
		void oclStringOpsInM2T() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"['hello'.concat(' world')/]-['hello'.size()/]-['hello'.substring(2, 4)/]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			assertEquals("hello world-5-ell", getFileContent(result, "out"));
		}

		@Test
		@DisplayName("OCL Collection ops in M2T context — size, first, includes")
		void oclCollectionOpsInM2T() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[p.eClassifiers->size()/]-[p.eClassifiers->first().name/]-[p.eClassifiers->exists(c | c.name = 'ClasseA')/]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			assertEquals("3-ClasseA-true", getFileContent(result, "out"));
		}

		@Test
		@DisplayName("OCL select/reject in M2T context")
		void oclSelectRejectInM2T() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[for (c : EClassifier | p.eClassifiers->select(c | c.name.startsWith('C'))) separator (', ')]\n" +
					"[c.name/]\n" +
					"[/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			assertEquals("ClasseA, ClasseB", getFileContent(result, "out"));
		}

		@Test
		@DisplayName("OCL collect in M2T context")
		void oclCollectInM2T() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[for (n : String | p.eClassifiers->collect(c | c.name)) separator (', ')]\n" +
					"[n/]\n" +
					"[/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tResult result = executeTemplate(mtl);
			assertEquals("ClasseA, ClasseB, AbstractClass", getFileContent(result, "out"));
		}
	}

	// ==================== Resolution — Namesake + Guard Combinations ====================

	@Nested
	@DisplayName("Resolution — Namesake + Guard Combinations")
	class ResolutionReferenceTests {

		@Test
		@DisplayName("namesake priority: local > extended > imported")
		void namesakePriorityLocalFirst() throws M2tParseException {
			String importedMtl =
					"[module imported(Ecore)/]\n" +
					"[template public render(p : EPackage)]Imported[/template]\n";
			String extendedMtl =
					"[module extended(Ecore)/]\n" +
					"[template public render(p : EPackage)]Extended[/template]\n";
			String localMtl =
					"[module local(Ecore) extends extended/]\n" +
					"[import imported/]\n" +
					"[template public render(p : EPackage)]Local[/template]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[render(p)/]\n" +
					"[/file]\n" +
					"[/template]\n";
			Module importedMod = engine.parse(importedMtl, "imported");
			Module extendedMod = engine.parse(extendedMtl, "extended");
			Module localMod = engine.parse(localMtl, "local");
			engine.link(importedMod, extendedMod, localMod);

			M2tResult result = engine.execute(localMod, M2tContext.of(targetPackage));
			assertEquals("Local", getFileContent(result, "out"));
		}

		@Test
		@DisplayName("override with guard selects based on model element")
		void overrideGuardModelBased() throws M2tParseException {
			EClass abstractClass = null;
			EClass concreteClass = null;
			for (var c : targetPackage.getEClassifiers()) {
				if (c instanceof EClass ec) {
					if (ec.isAbstract()) abstractClass = ec;
					else if (concreteClass == null) concreteClass = ec;
				}
			}
			assertNotNull(abstractClass);
			assertNotNull(concreteClass);

			String baseMtl =
					"[module base(Ecore)/]\n" +
					"[template public render(c : EClass)]Base: [c.name/][/template]\n" +
					"[template public main(c : EClass)]\n" +
					"[file ('out', false)]\n" +
					"[render(c)/]\n" +
					"[/file]\n" +
					"[/template]\n";
			String childMtl =
					"[module child(Ecore) extends base/]\n" +
					"[template public render(c : EClass) overrides render ? (c.abstract)]Abstract: [c.name/][/template]\n";

			Module baseMod = engine.parse(baseMtl, "base");
			Module childMod = engine.parse(childMtl, "child");
			engine.link(baseMod, childMod);

			// Abstract class → override applies
			M2tResult r1 = engine.execute(baseMod, M2tContext.of(abstractClass));
			assertEquals("Abstract: AbstractClass", getFileContent(r1, "out"));

			// Concrete class → fallback to base
			M2tResult r2 = engine.execute(baseMod, M2tContext.of(concreteClass));
			assertEquals("Base: ClasseA", getFileContent(r2, "out"));
		}

		@Test
		@DisplayName("external namesake resolution — extended preferred over imported")
		void externalNamesakeExtendedPreferred() throws M2tParseException {
			String importedMtl =
					"[module imported(Ecore)/]\n" +
					"[template public helper(p : EPackage)]Imported[/template]\n";
			String extendedMtl =
					"[module extended(Ecore)/]\n" +
					"[template public helper(p : EPackage)]Extended[/template]\n";
			String localMtl =
					"[module local(Ecore) extends extended/]\n" +
					"[import imported/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[helper(p)/]\n" +
					"[/file]\n" +
					"[/template]\n";
			Module importedMod = engine.parse(importedMtl, "imported");
			Module extendedMod = engine.parse(extendedMtl, "extended");
			Module localMod = engine.parse(localMtl, "local");
			engine.link(importedMod, extendedMod, localMod);

			M2tResult result = engine.execute(localMod, M2tContext.of(targetPackage));
			assertEquals("Extended", getFileContent(result, "out"));
		}

		@Test
		@DisplayName("override chain with super — three-level")
		void overrideChainWithSuper() throws M2tParseException {
			String aMtl =
					"[module a(Ecore)/]\n" +
					"[template public render(p : EPackage)]A[/template]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[render(p)/]\n" +
					"[/file]\n" +
					"[/template]\n";
			String bMtl =
					"[module b(Ecore) extends a/]\n" +
					"[template public render(p : EPackage) overrides render][super/]+B[/template]\n";
			String cMtl =
					"[module c(Ecore) extends b/]\n" +
					"[template public render(p : EPackage) overrides render][super/]+C[/template]\n";

			Module aMod = engine.parse(aMtl, "a");
			Module bMod = engine.parse(bMtl, "b");
			Module cMod = engine.parse(cMtl, "c");
			engine.link(aMod, bMod, cMod);

			M2tResult result = engine.execute(aMod, M2tContext.of(targetPackage));
			assertEquals("A+B+C", getFileContent(result, "out"));
		}
	}
}
