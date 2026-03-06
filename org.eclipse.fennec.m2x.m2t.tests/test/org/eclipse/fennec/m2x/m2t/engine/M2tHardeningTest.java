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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.api.WhitespaceMode;
import org.eclipse.fennec.m2x.m2t.engine.internal.M2tProtectedAreaMerger;
import org.eclipse.fennec.m2x.m2t.engine.internal.M2tStandardLibrary;
import org.eclipse.fennec.m2x.m2t.parser.M2tParseResult;
import org.eclipse.fennec.m2x.m2t.parser.M2tParserSupport;
import org.eclipse.fennec.m2x.m2t.parser.M2tWhitespaceNormalizer;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclOperation;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Hardening tests for null-safety, thread-safety edge cases, and error recovery.
 */
class M2tHardeningTest {

	private M2tEngineImpl engine;
	private EClass testClass;

	@BeforeEach
	void setUp() {
		OclParserSupport parser = new OclParserSupport();
		OclConfiguration oclConfig = OclConfiguration.builder(parser).build();
		M2tConfiguration config = M2tConfiguration.builder(oclConfig).build();
		engine = new M2tEngineImpl(config);

		EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
		pkg.setName("test");
		pkg.setNsURI("http://test");
		testClass = EcoreFactory.eINSTANCE.createEClass();
		testClass.setName("TestClass");
		pkg.getEClassifiers().add(testClass);
	}

	@Nested
	@DisplayName("Null-safety on public API")
	class NullSafety {

		@Test
		@DisplayName("parse(String, String) — null source throws NPE")
		void parseNullSource() {
			assertThrows(NullPointerException.class, () -> engine.parse(null, "test"));
		}

		@Test
		@DisplayName("parse(String, String) — null unitName throws NPE")
		void parseNullUnitName() {
			assertThrows(NullPointerException.class,
					() -> engine.parse("[module test('http://test')/][template public main(e : EObject)]hello[/template]", null));
		}

		@Test
		@DisplayName("parse(URI) — null URI throws NPE")
		void parseNullUri() {
			assertThrows(NullPointerException.class, () -> engine.parse(null));
		}

		@Test
		@DisplayName("execute — null module throws NPE")
		void executeNullModule() {
			assertThrows(NullPointerException.class,
					() -> engine.execute(null, M2tContext.of(testClass)));
		}

		@Test
		@DisplayName("execute — null context throws NPE")
		void executeNullContext() throws M2tParseException {
			String src = "[module m(Ecore)/]\n"
					+ "[template public main(e : EClass)]\n"
					+ "hello\n"
					+ "[/template]\n";
			Module m = engine.parse(src, "test");
			assertThrows(NullPointerException.class, () -> engine.execute(m, null));
		}

		@Test
		@DisplayName("link — null modules array throws NPE")
		void linkNullArray() {
			assertThrows(NullPointerException.class, () -> engine.link((Module[]) null));
		}

		@Test
		@DisplayName("link — null module element throws NPE")
		void linkNullElement() {
			assertThrows(NullPointerException.class, () -> engine.link(new Module[] { null }));
		}
	}

	@Nested
	@DisplayName("Parser null-safety")
	class ParserNullSafety {

		@Test
		@DisplayName("buildModule — null unitName throws NPE")
		void buildModuleNullUnitName() {
			M2tParserSupport support = new M2tParserSupport();
			assertThrows(NullPointerException.class,
					() -> support.buildModule("[module t('http://test')/]", null));
		}

		@Test
		@DisplayName("buildModuleWithPending — null unitName throws NPE")
		void buildModuleWithPendingNullUnitName() {
			M2tParserSupport support = new M2tParserSupport();
			assertThrows(NullPointerException.class,
					() -> support.buildModuleWithPending("[module t('http://test')/]", null));
		}
	}

	@Nested
	@DisplayName("M2tParseException cause preservation")
	class ExceptionCausePreservation {

		@Test
		@DisplayName("parse(URI) — IOException cause is preserved")
		void parseUriIoExceptionCause() {
			java.net.URI nonExistent = java.net.URI.create("file:///nonexistent/template.mtl");
			M2tParseException ex = assertThrows(M2tParseException.class,
					() -> engine.parse(nonExistent));
			assertNotNull(ex.getCause(), "IOException cause should be preserved");
		}
	}

	@Nested
	@DisplayName("Standard library null-safety")
	class StandardLibraryNullSafety {

		@SuppressWarnings("restriction")
		@Test
		@DisplayName("Operations return non-null for null self")
		void operationsHandleNullSelf() {
			M2tStandardLibrary lib = new M2tStandardLibrary();
			List<OclOperation> ops = lib.getOperations();
			OclOperation substitute = ops.stream()
					.filter(op -> "substitute".equals(op.name()))
					.findFirst().orElseThrow();
			Object result = substitute.implementation().apply(null, new Object[] { "a", "b" });
			assertEquals("", result);
		}

		@SuppressWarnings("restriction")
		@Test
		@DisplayName("first(n) with negative n returns empty string")
		void firstNegativeN() {
			M2tStandardLibrary lib = new M2tStandardLibrary();
			OclOperation first = lib.getOperations().stream()
					.filter(op -> "first".equals(op.name()))
					.findFirst().orElseThrow();
			Object result = first.implementation().apply("hello", new Object[] { -1 });
			assertEquals("", result);
		}

		@SuppressWarnings("restriction")
		@Test
		@DisplayName("last(n) with negative n returns empty string")
		void lastNegativeN() {
			M2tStandardLibrary lib = new M2tStandardLibrary();
			OclOperation last = lib.getOperations().stream()
					.filter(op -> "last".equals(op.name()))
					.findFirst().orElseThrow();
			Object result = last.implementation().apply("hello", new Object[] { -1 });
			assertEquals("", result);
		}
	}

	@Nested
	@DisplayName("ProtectedAreaMerger null-safety")
	class MergerNullSafety {

		@SuppressWarnings("restriction")
		@Test
		@DisplayName("merge — null newContent throws NPE")
		void mergeNullNewContent() {
			M2tProtectedAreaMerger merger = new M2tProtectedAreaMerger();
			assertThrows(NullPointerException.class,
					() -> merger.merge(null, "existing"));
		}

		@SuppressWarnings("restriction")
		@Test
		@DisplayName("merge — null existingContent returns newContent")
		void mergeNullExistingContent() {
			M2tProtectedAreaMerger merger = new M2tProtectedAreaMerger();
			assertEquals("new", merger.merge("new", null));
		}

		@SuppressWarnings("restriction")
		@Test
		@DisplayName("startMarker — null markerId throws NPE")
		void startMarkerNullId() {
			assertThrows(NullPointerException.class,
					() -> M2tProtectedAreaMerger.startMarker(null, "content"));
		}
	}

	@Nested
	@DisplayName("WhitespaceNormalizer null-safety")
	class NormalizerNullSafety {

		@Test
		@DisplayName("normalize — null module throws NPE")
		void normalizeNullModule() {
			M2tWhitespaceNormalizer normalizer = new M2tWhitespaceNormalizer(WhitespaceMode.SPEC);
			assertThrows(NullPointerException.class, () -> normalizer.normalize(null));
		}

		@Test
		@DisplayName("constructor — null mode throws NPE")
		void constructorNullMode() {
			assertThrows(NullPointerException.class, () -> new M2tWhitespaceNormalizer(null));
		}
	}

	@Nested
	@DisplayName("M2tParseResult null-safety")
	class ParseResultNullSafety {

		@Test
		@DisplayName("null module throws NPE")
		void nullModule() {
			assertThrows(NullPointerException.class,
					() -> new M2tParseResult(null, List.of(), List.of(),
							java.util.Map.of(), java.util.Map.of()));
		}
	}

	@Nested
	@DisplayName("Error recovery in template evaluation")
	class ErrorRecovery {

		@Test
		@DisplayName("OCL evaluation error produces ERROR diagnostic, not exception")
		void oclErrorProducesErrorDiagnostic() throws M2tParseException {
			String src = "[module m(Ecore)/]\n"
					+ "[template public main(c : EClass)]\n"
					+ "[c.nonExistentProperty/]\n"
					+ "[/template]\n";
			Module m = engine.parse(src, "test");
			M2tResult result = assertDoesNotThrow(
					() -> engine.execute(m, M2tContext.of(testClass)));
			assertNotNull(result);
			// OCL evaluation error should be ERROR severity
			assertTrue(result.diagnostics().stream()
					.anyMatch(d -> d.getSeverity() == Diagnostic.ERROR
							&& d.getMessage().contains("OCL evaluation error")));
			assertFalse(result.isSuccess(), "OCL error should mark result as not successful");
		}
	}

	@Nested
	@DisplayName("Diagnostic severity levels")
	class DiagnosticSeverity {

		@Test
		@DisplayName("SOURCE_ID is 'm2t.engine' on all diagnostics")
		void diagnosticsHaveSourceId() throws M2tParseException {
			String src = "[module m(Ecore)/]\n"
					+ "[template public main(c : EClass)]\n"
					+ "[c.nonExistentProperty/]\n"
					+ "[/template]\n";
			Module m = engine.parse(src, "test");
			M2tResult result = engine.execute(m, M2tContext.of(testClass));
			assertFalse(result.diagnostics().isEmpty());
			assertTrue(result.diagnostics().stream()
					.allMatch(d -> "m2t.engine".equals(d.getSource())));
		}

		@Test
		@DisplayName("maxDiagnostics limits diagnostic output")
		void maxDiagnosticsLimit() throws M2tParseException {
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			M2tConfiguration limitConfig = M2tConfiguration.builder(oclConfig)
					.maxDiagnostics(3)
					.build();
			M2tEngineImpl limitedEngine = new M2tEngineImpl(limitConfig);

			// Template with many errors — each OCL expression fails
			String src = "[module m(Ecore)/]\n"
					+ "[template public main(c : EClass)]\n"
					+ "[c.a/][c.b/][c.c/][c.d/][c.e/]\n"
					+ "[/template]\n";
			Module m = limitedEngine.parse(src, "test");
			M2tResult result = limitedEngine.execute(m, M2tContext.of(testClass));
			// Should have at most 3 + 1 (truncation message) diagnostics
			assertTrue(result.diagnostics().size() <= 4,
					"Expected max 4 diagnostics (3 + truncation), got: " + result.diagnostics().size());
			// Last one should be the truncation warning
			assertTrue(result.diagnostics().stream()
					.anyMatch(d -> d.getMessage().contains("truncated")));
		}
	}
}
