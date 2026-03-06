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
package org.eclipse.fennec.m2x.m2t.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.api.WhitespaceMode;
import org.eclipse.fennec.m2x.m2t.engine.M2tEngineImpl;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for MOFM2T §8.4 whitespace handling.
 *
 * <p>Default mode is {@link WhitespaceMode#ACCELEO}. SPEC mode tests are
 * in their own nested class where behavior differs.
 */
class M2tWhitespaceTest {

	private M2tEngineImpl engine;
	private EPackage inputPackage;

	@BeforeEach
	void setUp() {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		M2tConfiguration config = M2tConfiguration.builder(oclConfig).build(); // default = ACCELEO
		engine = new M2tEngineImpl(config);

		inputPackage = EcoreFactory.eINSTANCE.createEPackage();
		inputPackage.setName("test");
		EClass classA = EcoreFactory.eINSTANCE.createEClass();
		classA.setName("ClassA");
		inputPackage.getEClassifiers().add(classA);
		EClass classB = EcoreFactory.eINSTANCE.createEClass();
		classB.setName("ClassB");
		inputPackage.getEClassifiers().add(classB);
	}

	private String execute(String mtlSource) throws M2tParseException {
		return execute(engine, mtlSource);
	}

	private String execute(M2tEngineImpl eng, String mtlSource) throws M2tParseException {
		Module module = eng.parse(mtlSource, "test");
		M2tResult result = eng.execute(module, M2tContext.of(inputPackage));
		assertTrue(result.isSuccess(), "Execution should succeed: " + result.diagnostics());
		return result.generatedFiles().values().iterator().next();
	}

	private M2tEngineImpl engineWithMode(WhitespaceMode mode) {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		M2tConfiguration config = M2tConfiguration.builder(oclConfig)
				.whitespaceMode(mode)
				.build();
		return new M2tEngineImpl(config);
	}

	// ==================== Body Trimming (§8.4 Rule 1+2) ====================

	@Nested
	@DisplayName("Body Trimming")
	class BodyTrimmingTests {

		@Test
		@DisplayName("template body trims leading newline after head")
		void templateBodyTrimsLeadingNewline() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"Hello\n" +
					"[/file]\n" +
					"[/template]\n";
			assertEquals("Hello", execute(mtl));
		}

		@Test
		@DisplayName("template body trims trailing newline before tail")
		void templateBodyTrimsTrailingNewline() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"Hello\n" +
					"World\n" +
					"[/file]\n" +
					"[/template]\n";
			assertEquals("Hello\nWorld", execute(mtl));
		}

		@Test
		@DisplayName("file block body is trimmed too")
		void fileBlockBodyTrimmed() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"content\n" +
					"[/file]\n" +
					"[/template]\n";
			assertEquals("content", execute(mtl));
		}
	}

	// ==================== Standalone Block Detection (§8.4 Rule 3) ====================

	@Nested
	@DisplayName("Standalone Block Detection")
	class StandaloneBlockTests {

		@Test
		@DisplayName("standalone for strips leading whitespace and trailing newline")
		void standaloneForStripsLeadingWhitespace() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"  [for (c : EClassifier | p.eClassifiers)]\n" +
					"[c.name/]\n" +
					"  [/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			// ACCELEO mode: no default separator injection, newline comes from body text
			assertEquals("ClassA\nClassB", execute(mtl));
		}

		@Test
		@DisplayName("standalone if strips leading whitespace and trailing newline")
		void standaloneIfStripsLeadingWhitespace() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"  [if (true)]\n" +
					"Yes\n" +
					"  [/if]\n" +
					"[/file]\n" +
					"[/template]\n";
			assertEquals("Yes", execute(mtl));
		}

		@Test
		@DisplayName("standalone let strips leading whitespace and trailing newline")
		void standaloneLetStripsLeadingWhitespace() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"  [let name : String = p.name]\n" +
					"[name/]\n" +
					"  [/let]\n" +
					"[/file]\n" +
					"[/template]\n";
			assertEquals("test", execute(mtl));
		}

		@Test
		@DisplayName("embedded block preserves whitespace")
		void embeddedBlockPreservesWhitespace() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"text[if (true)]yes[/if]\n" +
					"[/file]\n" +
					"[/template]\n";
			assertEquals("textyes", execute(mtl));
		}

		@Test
		@DisplayName("standalone for with explicit separator keeps it")
		void standaloneForExplicitSeparatorKept() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[for (c : EClassifier | p.eClassifiers) separator(', ')]\n" +
					"[c.name/]\n" +
					"[/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			assertEquals("ClassA, ClassB", execute(mtl));
		}

		@Test
		@DisplayName("nested standalone blocks each normalized independently")
		void nestedStandaloneBlocks() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[for (c : EClassifier | p.eClassifiers) separator(', ')]\n" +
					"  [if (true)]\n" +
					"[c.name/]\n" +
					"  [/if]\n" +
					"[/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			assertEquals("ClassA, ClassB", execute(mtl));
		}
	}

	// ==================== BOL Indicator (§8.4 Rule 5) ====================

	@Nested
	@DisplayName("BOL Indicator — SPEC mode only")
	class BolIndicatorTests {

		@Test
		@DisplayName("SPEC: BOL indicator strips leading whitespace on line")
		void bolIndicatorStripsLeadingWhitespace() throws M2tParseException {
			M2tEngineImpl specEngine = engineWithMode(WhitespaceMode.SPEC);
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"    ^content\n" +
					"[/file]\n" +
					"[/template]\n";
			assertEquals("content", execute(specEngine, mtl));
		}

		@Test
		@DisplayName("SPEC: BOL indicator mid-line")
		void bolIndicatorMidLine() throws M2tParseException {
			M2tEngineImpl specEngine = engineWithMode(WhitespaceMode.SPEC);
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"line1\n" +
					"    ^line2\n" +
					"[/file]\n" +
					"[/template]\n";
			assertEquals("line1\nline2", execute(specEngine, mtl));
		}

		@Test
		@DisplayName("ACCELEO: caret is passed through as literal text")
		void acceleoCaretPassedThrough() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"    ^content\n" +
					"[/file]\n" +
					"[/template]\n";
			// ACCELEO mode (default): ^ is not processed, kept as literal text
			assertEquals("    ^content", execute(mtl));
		}
	}

	// ==================== Indent Propagation (§8.4 Rule 4) ====================

	@Nested
	@DisplayName("Indent Propagation")
	class IndentPropagationTests {

		@Test
		@DisplayName("standalone invocation propagates indent to every line")
		void standaloneInvocationIndentPropagation() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"  [helper(p)/]\n" +
					"[/file]\n" +
					"[/template]\n" +
					"[template public helper(p : EPackage)]\n" +
					"line1\n" +
					"line2\n" +
					"line3\n" +
					"[/template]\n";
			assertEquals("line1\n  line2\n  line3", execute(mtl));
		}

		@Test
		@DisplayName("embedded invocation does not propagate indent")
		void embeddedInvocationNoIndent() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"text[helper(p)/]\n" +
					"[/file]\n" +
					"[/template]\n" +
					"[template public helper(p : EPackage)]\n" +
					"A\n" +
					"B\n" +
					"[/template]\n";
			assertEquals("textA\nB", execute(mtl));
		}

		@Test
		@DisplayName("indent propagation does not indent first line")
		void indentPropagationFirstLineNotIndented() throws M2tParseException {
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"before\n" +
					"    [helper(p)/]\n" +
					"after\n" +
					"[/file]\n" +
					"[/template]\n" +
					"[template public helper(p : EPackage)]\n" +
					"first\n" +
					"second\n" +
					"[/template]\n";
			assertEquals("before\nfirst\n    second\nafter", execute(mtl));
		}
	}

	// ==================== SPEC Mode (§8.4 strict) ====================

	@Nested
	@DisplayName("SPEC Mode — default separator injection")
	class SpecModeTests {

		@Test
		@DisplayName("SPEC mode injects default newline separator for standalone for")
		void specModeDefaultNewlineSeparator() throws M2tParseException {
			M2tEngineImpl specEngine = engineWithMode(WhitespaceMode.SPEC);
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"[for (c : EClassifier | p.eClassifiers)]\n" +
					"[c.name/]\n" +
					"[/for]\n" +
					"[/file]\n" +
					"[/template]\n";
			// SPEC mode: body is trimmed to [c.name/], default \n separator injected
			assertEquals("ClassA\nClassB", execute(specEngine, mtl));
		}
	}

	// ==================== NONE Mode ====================

	@Nested
	@DisplayName("NONE Mode — no normalization")
	class NoneModeTests {

		@Test
		@DisplayName("no normalization when NONE mode")
		void noNormalizationWhenNoneMode() throws M2tParseException {
			M2tEngineImpl noneEngine = engineWithMode(WhitespaceMode.NONE);
			String mtl =
					"[module m(Ecore)/]\n" +
					"[template public main(p : EPackage)]\n" +
					"[file ('out', false)]\n" +
					"Hello\n" +
					"[/file]\n" +
					"[/template]\n";
			Module module = noneEngine.parse(mtl, "test");
			M2tResult result = noneEngine.execute(module, M2tContext.of(inputPackage));
			String content = result.generatedFiles().get("out");
			// Without normalization, the raw body text includes leading/trailing newlines
			assertTrue(content.contains("\nHello\n"), "Content should contain raw newlines: '" + content + "'");
		}
	}
}
