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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.model.m2t.FileBlock;
import org.eclipse.fennec.m2x.model.m2t.M2tFactory;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.OpenModeKind;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.m2t.VisibilityKind;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.StringLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for MOFM2T v1.0 §8.3 Standard Library operations.
 *
 * <p>Uses {@link M2tEngine} to ensure the library is auto-registered
 * and operations are accessible from template expressions.
 */
class M2tStandardLibraryTest {

	private M2tEngine engine;
	private EClass input;

	@BeforeEach
	void setUp() {
		OclParserSupport parser = new OclParserSupport();
		OclConfiguration oclConfig = OclConfiguration.builder(parser).build();
		M2tConfiguration config = M2tConfiguration.builder(oclConfig).build();
		engine = M2tEngines.create(config);
		input = EcoreFactory.eINSTANCE.createEClass();
		input.setName("TestClass");
	}

	/**
	 * Evaluates an OCL expression via M2T template and returns the output string.
	 * The expression is embedded in a file block so we get exact output.
	 */
	private String eval(String oclExpr) {
		M2tFactory m2t = M2tFactory.eINSTANCE;
		OclFactory ocl = OclFactory.eINSTANCE;

		Module module = m2t.createModule();
		module.setName("test");

		Template t = m2t.createTemplate();
		t.setName("main");
		t.setMain(true);
		t.setVisibility(VisibilityKind.PUBLIC);

		Variable param = ocl.createVariable();
		param.setName("c");
		t.getParameter().add(param);

		FileBlock fb = m2t.createFileBlock();
		fb.setFileUrl(stringLit("out.txt"));
		fb.setOpenMode(OpenModeKind.OVERWRITE);

		// Parse the OCL expression and wrap in an inline let block
		try {
			@SuppressWarnings("unused")
			OclExpression expr = engine.parse("[module m(UML)/]\n[template public main(c : EClass)]\n[file ('out.txt', false, 'UTF-8')]\n[" + oclExpr + "/]\n[/file]\n[/template]\n", "test")
					.getOwnedModuleElement().stream()
					.filter(e -> e instanceof Template)
					.map(e -> (Template) e)
					.findFirst().orElseThrow()
					.getBody().get(0) // FileBlock
					.eContents().stream() // FileBlock body
					.findFirst().orElse(null) != null ? null : null;
		} catch (Exception e) {
			// ignore
		}

		// Simpler approach: use the parser to build the full module
		try {
			Module m = engine.parse(
					"[module m(UML)/]\n" +
					"[template public main(c : EClass)]\n" +
					"[file ('out.txt', false, 'UTF-8')]\n" +
					"[" + oclExpr + "/]\n" +
					"[/file]\n" +
					"[/template]\n",
					"test");
			M2tResult result = engine.execute(m, M2tContext.of(input));
			String fileContent = result.generatedFiles().get("out.txt");
			return fileContent != null ? fileContent.trim() : "";
		} catch (Exception e) {
			fail("Failed to evaluate: " + oclExpr + " — " + e.getMessage());
			return null;
		}
	}

	private StringLiteralExp stringLit(String value) {
		StringLiteralExp lit = OclFactory.eINSTANCE.createStringLiteralExp();
		lit.setStringSymbol(value);
		return lit;
	}

	// ==================== substitute ====================

	@Nested
	@DisplayName("substitute(r, t)")
	class SubstituteTests {

		@Test
		@DisplayName("replaces substring")
		void replacesSubstring() {
			assertEquals("Hello World", eval("'Hello OCL'.substitute('OCL', 'World')"));
		}

		@Test
		@DisplayName("no match returns original")
		void noMatchReturnsOriginal() {
			assertEquals("abc", eval("'abc'.substitute('xyz', '123')"));
		}

		@Test
		@DisplayName("replaces all occurrences")
		void replacesAllOccurrences() {
			assertEquals("b-b-b", eval("'a-a-a'.substitute('a', 'b')"));
		}
	}

	// ==================== index ====================

	@Nested
	@DisplayName("index(r)")
	class IndexTests {

		@Test
		@DisplayName("1-based index")
		void oneBasedIndex() {
			assertEquals("1", eval("'hello'.index('h')"));
		}

		@Test
		@DisplayName("substring index")
		void substringIndex() {
			assertEquals("3", eval("'hello'.index('llo')"));
		}

		@Test
		@DisplayName("not found returns -1")
		void notFoundReturnsMinusOne() {
			assertEquals("-1", eval("'hello'.index('xyz')"));
		}
	}

	// ==================== first ====================

	@Nested
	@DisplayName("first(n)")
	class FirstTests {

		@Test
		@DisplayName("first 3 characters")
		void first3() {
			assertEquals("hel", eval("'hello'.first(3)"));
		}

		@Test
		@DisplayName("first with n > length returns whole string")
		void firstExceedsLength() {
			assertEquals("hi", eval("'hi'.first(10)"));
		}
	}

	// ==================== last ====================

	@Nested
	@DisplayName("last(n)")
	class LastTests {

		@Test
		@DisplayName("last 3 characters")
		void last3() {
			assertEquals("llo", eval("'hello'.last(3)"));
		}

		@Test
		@DisplayName("last with n > length returns whole string")
		void lastExceedsLength() {
			assertEquals("hi", eval("'hi'.last(10)"));
		}
	}

	// ==================== strstr ====================

	@Nested
	@DisplayName("strstr(r)")
	class StrstrTests {

		@Test
		@DisplayName("found returns true")
		void foundReturnsTrue() {
			assertEquals("true", eval("'hello world'.strstr('world')"));
		}

		@Test
		@DisplayName("not found returns false")
		void notFoundReturnsFalse() {
			assertEquals("false", eval("'hello'.strstr('xyz')"));
		}
	}

	// ==================== toUpper / toLower ====================

	@Nested
	@DisplayName("toUpper / toLower")
	class CaseTests {

		@Test
		@DisplayName("toUpper")
		void toUpper() {
			assertEquals("HELLO", eval("'hello'.toUpper()"));
		}

		@Test
		@DisplayName("toLower")
		void toLower() {
			assertEquals("hello", eval("'HELLO'.toLower()"));
		}
	}

	// ==================== toUpperFirst / toLowerFirst ====================

	@Nested
	@DisplayName("toUpperFirst / toLowerFirst")
	class FirstCaseTests {

		@Test
		@DisplayName("toUpperFirst")
		void toUpperFirst() {
			assertEquals("Hello", eval("'hello'.toUpperFirst()"));
		}

		@Test
		@DisplayName("toLowerFirst")
		void toLowerFirst() {
			assertEquals("hELLO", eval("'HELLO'.toLowerFirst()"));
		}
	}

	// ==================== strcmp ====================

	@Nested
	@DisplayName("strcmp(s1)")
	class StrcmpTests {

		@Test
		@DisplayName("equal strings return 0")
		void equalStrings() {
			assertEquals("0", eval("'abc'.strcmp('abc')"));
		}

		@Test
		@DisplayName("self < s1 returns negative")
		void lessThan() {
			String result = eval("'abc'.strcmp('xyz')");
			assertTrue(Long.parseLong(result) < 0, "Expected negative, got: " + result);
		}

		@Test
		@DisplayName("self > s1 returns positive")
		void greaterThan() {
			String result = eval("'xyz'.strcmp('abc')");
			assertTrue(Long.parseLong(result) > 0, "Expected positive, got: " + result);
		}
	}

	// ==================== isAlpha / isAlphanum ====================

	@Nested
	@DisplayName("isAlpha / isAlphanum")
	class AlphaTests {

		@Test
		@DisplayName("isAlpha true for letters")
		void isAlphaTrue() {
			assertEquals("true", eval("'Hello'.isAlpha()"));
		}

		@Test
		@DisplayName("isAlpha false for digits")
		void isAlphaFalse() {
			assertEquals("false", eval("'Hello123'.isAlpha()"));
		}

		@Test
		@DisplayName("isAlphanum true for letters and digits")
		void isAlphanumTrue() {
			assertEquals("true", eval("'Hello123'.isAlphanum()"));
		}

		@Test
		@DisplayName("isAlphanum false for special chars")
		void isAlphanumFalse() {
			assertEquals("false", eval("'Hello!'.isAlphanum()"));
		}
	}

	// ==================== strtok ====================

	@Nested
	@DisplayName("strtok(s1, flag)")
	class StrtokTests {

		@Test
		@DisplayName("first token")
		void firstToken() {
			assertEquals("Hello", eval("'Hello World Foo'.strtok(' ', 0)"));
		}
	}
}
