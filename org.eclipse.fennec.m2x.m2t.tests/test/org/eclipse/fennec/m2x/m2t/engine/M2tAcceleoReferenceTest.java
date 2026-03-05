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

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
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
 * (whitespace-stripped) against Acceleo 3.7 reference results. Whitespace
 * handling (P3-5) is not yet implemented, so only non-whitespace content
 * is compared — matching Acceleo's own {@code deleteWhitespaces()} strategy.
 *
 * <p>Input model: an EPackage with 3 classifiers (ClasseA, ClasseB, AbstractClass),
 * matching Acceleo's {@code data/target.ecore}.
 */
class M2tAcceleoReferenceTest {

	private M2tEngineImpl engine;
	private EPackage targetPackage;

	@BeforeEach
	void setUp() {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		M2tConfiguration config = M2tConfiguration.builder(oclConfig).build();
		engine = new M2tEngineImpl(config);

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

	/** Strip all whitespace for comparison (like Acceleo's deleteWhitespaces). */
	private String stripWhitespace(String s) {
		return s.replaceAll("\\s+", "");
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
			// Acceleo ref: ClasseA ClasseB AbstractClass (whitespace-stripped)
			assertEquals("ClasseAClasseBAbstractClass", stripWhitespace(content));
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
			assertEquals("ClasseA/ClasseB/AbstractClass", stripWhitespace(content));
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
			assertEquals("beforetestClasseAClasseBAbstractClass", stripWhitespace(content));
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
			assertEquals("ClasseAClasseBAbstractClassaftertest", stripWhitespace(content));
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
			assertEquals("ClasseAClasseB", stripWhitespace(content));
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
			// Acceleo ref: 123 (1-based)
			assertEquals("123", stripWhitespace(content));
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
			// Acceleo ref: yop2yop3yop (separator evaluated between iterations, i=2 and i=3)
			assertEquals("yop2yop3yop", stripWhitespace(content));
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
			assertEquals("MATCH", stripWhitespace(content));
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
			assertEquals("ClasseAClasseBAbstractClass", stripWhitespace(content));
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
			assertEquals("ABOTHER", stripWhitespace(content));
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
			assertEquals("constantoutput", stripWhitespace(content));
		}
	}
}
