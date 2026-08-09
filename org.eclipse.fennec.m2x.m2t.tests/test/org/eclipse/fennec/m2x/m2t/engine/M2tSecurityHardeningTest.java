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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.common.util.Diagnostic;
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
 * Security hardening tests for the MOFM2T Engine.
 *
 * <p>Tests verify that configurable limits are enforced to prevent
 * denial-of-service attacks via malicious templates (T-1 through T-9).
 *
 * @see <a href="../docs/m2t-security-analysis.md">M2T Security Analysis</a>
 */
class M2tSecurityHardeningTest {

	private EClass testClass;

	@BeforeEach
	void setUp() {
		EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
		pkg.setName("test");
		pkg.setNsURI("http://test");
		testClass = EcoreFactory.eINSTANCE.createEClass();
		testClass.setName("TestClass");
		pkg.getEClassifiers().add(testClass);
	}

	private M2tEngine createEngine(M2tConfiguration config) {
		return M2tEngines.create(config);
	}

	private M2tConfiguration defaultConfig() {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		return M2tConfiguration.builder(oclConfig).build();
	}

	@Nested
	@DisplayName("T-1: Template Recursion Depth Limit")
	class TemplateRecursionDepth {

		@Test
		@DisplayName("t1_selfRecursiveTemplate_exceedingDepth_terminates")
		void t1_selfRecursiveTemplate_exceedingDepth_terminates() throws M2tParseException {
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			M2tConfiguration config = M2tConfiguration.builder(oclConfig)
					.maxTemplateDepth(5)
					.build();
			M2tEngine engine = createEngine(config);

			// Template 'main' calls 'recurse', which calls itself
			String src = "[module m(Ecore)/]\n"
					+ "[template public recurse(c : EClass)]\n"
					+ "X[recurse(c)/]\n"
					+ "[/template]\n"
					+ "[template public main(c : EClass)]\n"
					+ "[recurse(c)/]\n"
					+ "[/template]\n";
			Module m = engine.parse(src, "test");
			M2tResult result = engine.execute(m, M2tContext.of(testClass));

			assertNotNull(result);
			assertFalse(result.isSuccess(), "Recursive template should produce errors");
			assertTrue(result.diagnostics().stream()
					.anyMatch(d -> d.getSeverity() == Diagnostic.ERROR
							&& d.getMessage().contains("Maximum template depth exceeded")),
					"Should have template depth exceeded error");
		}

		@Test
		@DisplayName("t1_normalDepth_succeeds")
		void t1_normalDepth_succeeds() throws M2tParseException {
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			M2tConfiguration config = M2tConfiguration.builder(oclConfig)
					.maxTemplateDepth(100)
					.build();
			M2tEngine engine = createEngine(config);

			// Non-recursive template chain: main -> helper
			String src = "[module m(Ecore)/]\n"
					+ "[template public helper(c : EClass)]hello[/template]\n"
					+ "[template public main(c : EClass)]\n"
					+ "[helper(c)/]\n"
					+ "[/template]\n";
			Module m = engine.parse(src, "test");
			M2tResult result = engine.execute(m, M2tContext.of(testClass));

			assertTrue(result.isSuccess(), "Normal depth should succeed");
		}
	}

	@Nested
	@DisplayName("T-2: For-Block Iteration Limit")
	class ForBlockIterationLimit {

		@Test
		@DisplayName("t2_forLoop_exceedingLimit_terminates")
		void t2_forLoop_exceedingLimit_terminates() throws M2tParseException {
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			M2tConfiguration config = M2tConfiguration.builder(oclConfig)
					.maxForIterations(5)
					.build();
			M2tEngine engine = createEngine(config);

			// For-loop over a sequence larger than the limit
			String src = "[module m(Ecore)/]\n"
					+ "[template public main(c : EClass)]\n"
					+ "[for (i : Integer | Sequence{1, 2, 3, 4, 5, 6, 7, 8, 9, 10})]\n"
					+ "X\n"
					+ "[/for]\n"
					+ "[/template]\n";
			Module m = engine.parse(src, "test");
			M2tResult result = engine.execute(m, M2tContext.of(testClass));

			assertNotNull(result);
			assertFalse(result.isSuccess(), "For-loop exceeding limit should produce errors");
			assertTrue(result.diagnostics().stream()
					.anyMatch(d -> d.getSeverity() == Diagnostic.ERROR
							&& d.getMessage().contains("Maximum for-block iterations exceeded")),
					"Should have for-block iteration exceeded error");
		}

		@Test
		@DisplayName("t2_forLoop_withinLimit_succeeds")
		void t2_forLoop_withinLimit_succeeds() throws M2tParseException {
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			M2tConfiguration config = M2tConfiguration.builder(oclConfig)
					.maxForIterations(100)
					.build();
			M2tEngine engine = createEngine(config);

			String src = "[module m(Ecore)/]\n"
					+ "[template public main(c : EClass)]\n"
					+ "[for (i : Integer | Sequence{1, 2, 3})]\n"
					+ "X\n"
					+ "[/for]\n"
					+ "[/template]\n";
			Module m = engine.parse(src, "test");
			M2tResult result = engine.execute(m, M2tContext.of(testClass));

			assertTrue(result.isSuccess(), "For-loop within limit should succeed");
		}
	}

	@Nested
	@DisplayName("T-3: Cross-Product Size Limit")
	class CrossProductSizeLimit {

		@Test
		@DisplayName("t3_crossProduct_exceedingLimit_terminates")
		void t3_crossProduct_exceedingLimit_terminates() throws M2tParseException {
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			M2tConfiguration config = M2tConfiguration.builder(oclConfig)
					.maxCrossProductSize(5)
					.build();
			M2tEngine engine = createEngine(config);

			// Template invocation with Set arguments — cross-product 3*3 = 9 > 5
			String src = "[module m(Ecore)/]\n"
					+ "[template public helper(a : Integer, b : Integer)]([a/],[b/])[/template]\n"
					+ "[template public main(c : EClass)]\n"
					+ "[helper(Sequence{1, 2, 3}, Sequence{4, 5, 6})/]\n"
					+ "[/template]\n";
			Module m = engine.parse(src, "test");
			M2tResult result = engine.execute(m, M2tContext.of(testClass));

			assertNotNull(result);
			assertFalse(result.isSuccess(), "Cross-product exceeding limit should produce errors");
			assertTrue(result.diagnostics().stream()
					.anyMatch(d -> d.getSeverity() == Diagnostic.ERROR
							&& d.getMessage().contains("Cross-product size exceeds limit")),
					"Should have cross-product size exceeded error");
		}

		@Test
		@DisplayName("t3_crossProduct_withinLimit_succeeds")
		void t3_crossProduct_withinLimit_succeeds() throws M2tParseException {
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			M2tConfiguration config = M2tConfiguration.builder(oclConfig)
					.maxCrossProductSize(100)
					.build();
			M2tEngine engine = createEngine(config);

			String src = "[module m(Ecore)/]\n"
					+ "[template public helper(a : Integer, b : Integer)]([a/],[b/])[/template]\n"
					+ "[template public main(c : EClass)]\n"
					+ "[helper(Sequence{1, 2}, Sequence{3, 4})/]\n"
					+ "[/template]\n";
			Module m = engine.parse(src, "test");
			M2tResult result = engine.execute(m, M2tContext.of(testClass));

			assertTrue(result.isSuccess(), "Cross-product within limit should succeed");
		}
	}

	@Nested
	@DisplayName("T-6: Protected Area Marker Injection")
	class ProtectedAreaDisable {

		@Test
		@DisplayName("t6_protectedAreaDisabled_noMarkersInOutput")
		void t6_protectedAreaDisabled_noMarkersInOutput() throws M2tParseException {
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			M2tConfiguration config = M2tConfiguration.builder(oclConfig)
					.protectedAreaEnabled(false)
					.build();
			M2tEngine engine = createEngine(config);

			String src = "[module m(Ecore)/]\n"
					+ "[template public main(c : EClass)]\n"
					+ "[file ('out.txt', false, 'UTF-8')]\n"
					+ "before\n"
					+ "[protected ('myId')]\n"
					+ "default content\n"
					+ "[/protected]\n"
					+ "after\n"
					+ "[/file]\n"
					+ "[/template]\n";
			Module m = engine.parse(src, "test");
			M2tResult result = engine.execute(m, M2tContext.of(testClass));

			assertTrue(result.isSuccess());
			String content = result.generatedFiles().get("out.txt");
			assertNotNull(content);
			assertFalse(content.contains("// [protected"), "No marker should be emitted when disabled");
			assertFalse(content.contains("// [/protected]"), "No end marker should be emitted when disabled");
			assertTrue(content.contains("default content"), "Body content should still be emitted");
		}

		@Test
		@DisplayName("t6_protectedAreaEnabled_markersInOutput")
		void t6_protectedAreaEnabled_markersInOutput() throws M2tParseException {
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			M2tConfiguration config = M2tConfiguration.builder(oclConfig)
					.protectedAreaEnabled(true)
					.build();
			M2tEngine engine = createEngine(config);

			String src = "[module m(Ecore)/]\n"
					+ "[template public main(c : EClass)]\n"
					+ "[file ('out.txt', false, 'UTF-8')]\n"
					+ "[protected ('myId')]\n"
					+ "default content\n"
					+ "[/protected]\n"
					+ "[/file]\n"
					+ "[/template]\n";
			Module m = engine.parse(src, "test");
			M2tResult result = engine.execute(m, M2tContext.of(testClass));

			assertTrue(result.isSuccess());
			String content = result.generatedFiles().get("out.txt");
			assertNotNull(content);
			assertTrue(content.contains("// [protected 'myId'"), "Markers should be emitted when enabled");
			assertTrue(content.contains("// [/protected]"), "End marker should be emitted when enabled");
		}

		@Test
		@DisplayName("t6_defaultIsEnabled")
		void t6_defaultIsEnabled() {
			M2tConfiguration config = defaultConfig();
			assertTrue(config.protectedAreaEnabled(), "Protected areas should be enabled by default");
		}
	}

	@Nested
	@DisplayName("T-7: Output Size Limit")
	class OutputSizeLimit {

		@Test
		@DisplayName("t7_outputExceedingLimit_producesError")
		void t7_outputExceedingLimit_producesError() throws M2tParseException {
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			M2tConfiguration config = M2tConfiguration.builder(oclConfig)
					.maxOutputSize(50)
					.maxForIterations(1_000_000)
					.build();
			M2tEngine engine = createEngine(config);

			// Each iteration emits "ABCDEFGHIJ" (10 chars), 50 char limit → exceeded after 5+ iterations
			String src = "[module m(Ecore)/]\n"
					+ "[template public main(c : EClass)]\n"
					+ "[for (i : Integer | Sequence{1, 2, 3, 4, 5, 6, 7, 8, 9, 10})]\n"
					+ "ABCDEFGHIJ\n"
					+ "[/for]\n"
					+ "[/template]\n";
			Module m = engine.parse(src, "test");
			M2tResult result = engine.execute(m, M2tContext.of(testClass));

			assertNotNull(result);
			assertFalse(result.isSuccess(), "Output exceeding limit should produce errors");
			assertTrue(result.diagnostics().stream()
					.anyMatch(d -> d.getSeverity() == Diagnostic.ERROR
							&& d.getMessage().contains("Maximum output size exceeded")),
					"Should have output size exceeded error");
		}

		@Test
		@DisplayName("t7_outputWithinLimit_succeeds")
		void t7_outputWithinLimit_succeeds() throws M2tParseException {
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			M2tConfiguration config = M2tConfiguration.builder(oclConfig)
					.maxOutputSize(10_000)
					.build();
			M2tEngine engine = createEngine(config);

			String src = "[module m(Ecore)/]\n"
					+ "[template public main(c : EClass)]\n"
					+ "hello world\n"
					+ "[/template]\n";
			Module m = engine.parse(src, "test");
			M2tResult result = engine.execute(m, M2tContext.of(testClass));

			assertTrue(result.isSuccess(), "Output within limit should succeed");
		}

		@Test
		@DisplayName("t7_unlimitedOutput_succeeds")
		void t7_unlimitedOutput_succeeds() throws M2tParseException {
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			M2tConfiguration config = M2tConfiguration.builder(oclConfig)
					.maxOutputSize(0)  // unlimited
					.build();
			M2tEngine engine = createEngine(config);

			String src = "[module m(Ecore)/]\n"
					+ "[template public main(c : EClass)]\n"
					+ "hello world\n"
					+ "[/template]\n";
			Module m = engine.parse(src, "test");
			M2tResult result = engine.execute(m, M2tContext.of(testClass));

			assertTrue(result.isSuccess(), "Unlimited output should succeed");
		}
	}

	@Nested
	@DisplayName("Configuration Defaults")
	class ConfigurationDefaults {

		@Test
		@DisplayName("defaults_haveCorrectValues")
		void defaults_haveCorrectValues() {
			M2tConfiguration config = defaultConfig();
			assertEquals(1_000, config.maxTemplateDepth(), "maxTemplateDepth default");
			assertEquals(1_000_000, config.maxForIterations(), "maxForIterations default");
			assertEquals(1_000_000, config.maxCrossProductSize(), "maxCrossProductSize default");
			assertEquals(10_000_000L, config.maxOutputSize(), "maxOutputSize default");
			assertEquals(10_000, config.maxDiagnostics(), "maxDiagnostics default");
			assertTrue(config.protectedAreaEnabled(), "protectedAreaEnabled default");
		}

		@Test
		@DisplayName("builder_rejectsInvalidValues")
		void builder_rejectsInvalidValues() {
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();

			assertThrows(IllegalArgumentException.class,
					() -> M2tConfiguration.builder(oclConfig).maxTemplateDepth(0));
			assertThrows(IllegalArgumentException.class,
					() -> M2tConfiguration.builder(oclConfig).maxForIterations(-1));
			assertThrows(IllegalArgumentException.class,
					() -> M2tConfiguration.builder(oclConfig).maxCrossProductSize(0));
			assertThrows(IllegalArgumentException.class,
					() -> M2tConfiguration.builder(oclConfig).maxDiagnostics(-5));
			assertThrows(IllegalArgumentException.class,
					() -> M2tConfiguration.builder(oclConfig).maxOutputSize(-1));
		}
	}
}
