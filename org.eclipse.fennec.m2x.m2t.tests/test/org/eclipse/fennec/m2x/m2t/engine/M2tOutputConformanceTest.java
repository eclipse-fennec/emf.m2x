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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tGenerationStrategy;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.api.ProtectedAreaDelimiter;
import org.eclipse.fennec.m2x.m2t.api.WhitespaceMode;
import org.eclipse.fennec.m2x.m2t.engine.internal.M2tProtectedAreaMerger;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.OpenModeKind;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * What a generation writes (#367, #368, #374): protected areas and their markers, the file block
 * with the standard output, its append flag, unique id and charset, and the §8.4 whitespace of
 * CRLF templates and single-line stand-alone blocks.
 */
class M2tOutputConformanceTest {

	private static final String HEADER = "[module doc(ecore)/]\n";

	@TempDir
	Path outputDirectory;

	/** A generation strategy in memory: files, the content they had before, the lost areas, the ids. */
	private static final class MemoryStrategy implements M2tGenerationStrategy {

		final Map<String, String> files = new LinkedHashMap<>();
		final Map<String, Map<String, String>> lost = new LinkedHashMap<>();
		final Map<String, String> uniqueIds = new HashMap<>();
		final Map<String, OpenModeKind> modes = new HashMap<>();

		@Override
		public Writer createWriter(String filePath, OpenModeKind mode, Charset charset) {
			modes.put(filePath, mode);
			return new StringWriter() {
				@Override
				public void close() throws IOException {
					files.merge(filePath, toString(), (old, written) -> mode == OpenModeKind.APPEND ? old + written : written);
				}
			};
		}

		@Override
		public String readExistingContent(String filePath, Charset charset) {
			return files.get(filePath);
		}

		@Override
		public void lostContent(String filePath, Map<String, String> areas, Charset charset) {
			lost.put(filePath, areas);
		}

		@Override
		public String pathOfUniqueId(String uniqueId) {
			return uniqueIds.get(uniqueId);
		}

		@Override
		public void recordUniqueIds(Map<String, String> pathsByUniqueId) {
			uniqueIds.putAll(pathsByUniqueId);
		}
	}

	private static EClass employee() {
		EClass employee = EcoreFactory.eINSTANCE.createEClass();
		employee.setName("Employee");
		EPackage company = EcoreFactory.eINSTANCE.createEPackage();
		company.setName("company");
		company.getEClassifiers().add(employee);
		return employee;
	}

	private static M2tEngine engine(UnaryOperator<M2tConfiguration.Builder> configure) {
		return M2tEngines.create(configure.apply(M2tConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build())).build());
	}

	private static M2tResult run(M2tEngine engine, String main) throws M2tParseException {
		Module module = engine.parse(HEADER + "[template public main(c : EClass)]\n" + main + "\n[/template]\n", "doc");
		return engine.execute(module, M2tContext.of(employee()));
	}

	private static M2tResult run(M2tGenerationStrategy strategy, String main) throws M2tParseException {
		return run(engine(b -> b.generationStrategy(strategy)), main);
	}

	private static M2tResult run(String main) throws M2tParseException {
		return run(engine(UnaryOperator.identity()), main);
	}

	private static void assertSuccess(M2tResult result) {
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
	}

	private static void assertDiagnostic(M2tResult result, int severity, String message) {
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getSeverity() == severity && d.getMessage().contains(message)),
				() -> "expected '" + message + "' at severity " + severity + ": " + result.diagnostics());
	}

	@Nested
	@DisplayName("#367: protected areas (§7.2)")
	class ProtectedAreas {

		private static final String SPEC_EXAMPLE = """
				[file ('out.java', false)]
				{
				   [protected ('user_code')]
				   ; user code
				   [/protected]
				}
				[/file]""";

		@Test
		@DisplayName("the §7.2 example: both markers on lines of their own, indented like the tag")
		void specExampleLayout() throws M2tParseException {
			M2tResult result = run(SPEC_EXAMPLE);
			assertSuccess(result);
			String hash = M2tProtectedAreaMerger.contentHash("   ; user code\n   ");
			assertEquals("{\n   // [protected 'user_code' " + hash + "]\n   ; user code\n   // [/protected]\n}",
					result.generatedFiles().get("out.java"));
		}

		@Test
		@DisplayName("a regeneration keeps the user code of the §7.2 example")
		void regenerationKeepsUserCode() throws M2tParseException {
			MemoryStrategy strategy = new MemoryStrategy();
			assertSuccess(run(strategy, SPEC_EXAMPLE));
			strategy.files.put("out.java", strategy.files.get("out.java").replace("; user code", "int kept = 1;"));

			M2tResult again = run(strategy, SPEC_EXAMPLE);

			assertSuccess(again);
			assertTrue(strategy.files.get("out.java").contains("   int kept = 1;\n   // [/protected]"),
					strategy.files.get("out.java"));
		}

		@Test
		@DisplayName("an id with a quote survives the round trip")
		void idWithQuote() throws M2tParseException {
			MemoryStrategy strategy = new MemoryStrategy();
			String main = "[file ('out.java', false)]\n[protected ('it\\'s')]\ndefault\n[/protected]\n[/file]";
			assertSuccess(run(strategy, main));
			assertTrue(strategy.files.get("out.java").contains("[protected 'it\\'s' "), strategy.files.get("out.java"));
			strategy.files.put("out.java", strategy.files.get("out.java").replace("default", "USER"));

			assertSuccess(run(strategy, main));

			assertTrue(strategy.files.get("out.java").contains("USER"), strategy.files.get("out.java"));
		}

		@Test
		@DisplayName("the same id in two files is allowed")
		void sameIdInTwoFiles() throws M2tParseException {
			assertSuccess(run("[file ('a.java', false)][protected ('x')]A[/protected][/file]"
					+ "[file ('b.java', false)][protected ('x')]B[/protected][/file]"));
		}

		@Test
		@DisplayName("the same id twice in one file is an ERROR, and the second area is not written")
		void duplicateId() throws M2tParseException {
			M2tResult result = run("[file ('out.java', false)][protected ('x')]A[/protected][protected ('x')]B[/protected][/file]");
			assertDiagnostic(result, Diagnostic.ERROR, "Protected area id 'x' is used twice in 'out.java'");
			assertFalse(result.generatedFiles().get("out.java").contains("B"), result.generatedFiles().get("out.java"));
		}

		@Test
		@DisplayName("an existing file with an id twice is an ERROR, and that id is not merged")
		void duplicateIdOnDisk() throws M2tParseException {
			MemoryStrategy strategy = new MemoryStrategy();
			strategy.files.put("out.java", "// [protected 'x']\nONE\n// [/protected]\n// [protected 'x']\nTWO\n// [/protected]\n");
			M2tResult result = run(strategy, "[file ('out.java', false)][protected ('x')]A[/protected][/file]");
			assertDiagnostic(result, Diagnostic.ERROR, "holds protected area 'x' more than once");
			assertFalse(strategy.files.get("out.java").contains("ONE"), strategy.files.get("out.java"));
		}

		@Test
		@DisplayName("an empty or invalid id is an ERROR, and no marker is written")
		void emptyOrInvalidId() throws M2tParseException {
			M2tResult empty = run("[file ('out.java', false)]<[protected ('')]A[/protected]>[/file]");
			assertDiagnostic(empty, Diagnostic.ERROR, "The id of a protected area is empty");
			assertEquals("<>", empty.generatedFiles().get("out.java"));

			M2tResult invalid = run("[file ('out.java', false)]<[protected (c.eSuperTypes->first().name)]A[/protected]>[/file]");
			assertDiagnostic(invalid, Diagnostic.ERROR, "The id of a protected area is invalid");
			assertEquals("<>", invalid.generatedFiles().get("out.java"));
		}

		@Test
		@DisplayName("user code of an area no longer generated goes to the strategy, with a WARNING")
		void modifiedOrphan() throws M2tParseException {
			MemoryStrategy strategy = new MemoryStrategy();
			strategy.files.put("out.java", "// [protected 'gone']\nPRECIOUS USER CODE\n// [/protected]\n");
			M2tResult result = run(strategy, "[file ('out.java', false)]plain[/file]");
			assertDiagnostic(result, Diagnostic.WARNING, "[gone] of 'out.java' are no longer generated");
			assertEquals(Map.of("gone", "PRECIOUS USER CODE\n"), strategy.lost.get("out.java"));
		}

		@Test
		@DisplayName("an untouched area no longer generated goes without a word")
		void unmodifiedOrphan() throws M2tParseException {
			MemoryStrategy strategy = new MemoryStrategy();
			String body = "default\n";
			strategy.files.put("out.java", M2tProtectedAreaMerger.startMarker("gone", body) + "\n" + body + "// [/protected]\n");
			M2tResult result = run(strategy, "[file ('out.java', false)]plain[/file]");
			assertSuccess(result);
			assertTrue(result.diagnostics().isEmpty(), () -> "diagnostics: " + result.diagnostics());
			assertTrue(strategy.lost.isEmpty());
		}

		@Test
		@DisplayName("the file system keeps lost user code in <file>.lost")
		void lostFileOnDisk() throws Exception {
			Files.writeString(outputDirectory.resolve("out.java"), "// [protected 'gone']\nPRECIOUS\n// [/protected]\n");
			run(new FileSystemGenerationStrategy(outputDirectory), "[file ('out.java', false)]plain[/file]");
			assertEquals("[protected 'gone']\nPRECIOUS\n[/protected]\n", Files.readString(outputDirectory.resolve("out.java.lost")));
		}

		@Test
		@DisplayName("an XML file gets XML comments as delimiters")
		void xmlDelimiter() throws M2tParseException {
			String file = run("[file ('out.xml', false)][protected ('x')]A[/protected][/file]").generatedFiles().get("out.xml");
			assertTrue(file.startsWith("<!-- [protected 'x' ") && file.endsWith("<!-- [/protected] -->"), file);
		}

		@Test
		@DisplayName("a configured delimiter is used, and merged")
		void configuredDelimiter() throws M2tParseException {
			MemoryStrategy strategy = new MemoryStrategy();
			M2tEngine engine = engine(b -> b.generationStrategy(strategy).protectedAreaDelimiter("txt", ProtectedAreaDelimiter.HASH));
			String main = "[file ('out.txt', false)]\n[protected ('x')]\ndefault\n[/protected]\n[/file]";
			assertSuccess(run(engine, main));
			assertTrue(strategy.files.get("out.txt").startsWith("# [protected 'x' "), strategy.files.get("out.txt"));
			strategy.files.put("out.txt", strategy.files.get("out.txt").replace("default", "USER"));

			assertSuccess(run(engine, main));

			assertTrue(strategy.files.get("out.txt").contains("USER"), strategy.files.get("out.txt"));
		}
	}

	@Nested
	@DisplayName("#368: the file block (§7.3, §8.1.17, §8.2)")
	class FileBlock {

		@Test
		@DisplayName("[file (stdout)] goes to the standard output, not into a file")
		void stdoutKeyword() throws M2tParseException {
			M2tResult result = run("[file (stdout, false)]x[/file]");
			assertSuccess(result);
			assertEquals("x", result.standardOutput());
			assertTrue(result.generatedFiles().isEmpty());
		}

		@Test
		@DisplayName("the string 'stdout' is the standard output too")
		void stdoutString() throws M2tParseException {
			M2tResult result = run("[file ('stdout', false)]x[/file]");
			assertEquals("x", result.standardOutput());
			assertTrue(result.generatedFiles().isEmpty());
		}

		@Test
		@DisplayName("text outside a file block goes to the standard output, in order with stdout blocks")
		void textOutsideFileBlock() throws M2tParseException {
			M2tResult result = run("A[file (stdout)]B[/file]C[file ('f.txt', false)]F[/file]D");
			assertEquals("ABCD", result.standardOutput());
			assertEquals(Map.of("f.txt", "F"), result.generatedFiles());
		}

		@Test
		@DisplayName("the append flag is an expression")
		void appendFlagExpression() throws M2tParseException {
			M2tResult result = run("[file ('log', 1 = 1)]A[/file][file ('log', c.name <> '')]B[/file]");
			assertSuccess(result);
			assertEquals("AB", result.generatedFiles().get("log"));
		}

		@Test
		@DisplayName("an append flag that is no Boolean is an ERROR, and the block is skipped")
		void appendFlagNotBoolean() throws M2tParseException {
			M2tResult result = run("[file ('log', c.eSuperTypes->first())]A[/file]");
			assertDiagnostic(result, Diagnostic.ERROR, "append flag of [file] 'log'");
			assertTrue(result.generatedFiles().isEmpty());
		}

		@Test
		@DisplayName("an overwrite after an append overwrites the file on disk")
		void appendThenOverwrite() throws Exception {
			Files.writeString(outputDirectory.resolve("f.txt"), "OLD-");
			run(new FileSystemGenerationStrategy(outputDirectory), "[file ('f.txt', true)]A[/file][file ('f.txt', false)]B[/file]");
			assertEquals("B", Files.readString(outputDirectory.resolve("f.txt")));
		}

		@Test
		@DisplayName("an append after an overwrite extends what the run wrote")
		void overwriteThenAppend() throws Exception {
			Files.writeString(outputDirectory.resolve("f.txt"), "OLD-");
			run(new FileSystemGenerationStrategy(outputDirectory), "[file ('f.txt', false)]A[/file][file ('f.txt', true)]B[/file]");
			assertEquals("AB", Files.readString(outputDirectory.resolve("f.txt")));
		}

		@Test
		@DisplayName("a renamed file takes over the protected areas of the file of the same unique id")
		void uniqueIdRename() throws Exception {
			FileSystemGenerationStrategy strategy = new FileSystemGenerationStrategy(outputDirectory);
			String template = "[file (%s, false, 'id-42')]\n[protected ('spec')]\ndefault\n[/protected]\n[/file]";
			assertSuccess(run(strategy, template.formatted("'cust.java'")));
			Path old = outputDirectory.resolve("cust.java");
			Files.writeString(old, Files.readString(old).replace("default", "STORAGE SPEC"));

			M2tResult renamed = run(strategy, template.formatted("'customer.java'"));

			assertSuccess(renamed);
			assertTrue(Files.readString(outputDirectory.resolve("customer.java")).contains("STORAGE SPEC"));
			assertTrue(Files.exists(old), "the old file is kept");
			assertDiagnostic(renamed, Diagnostic.INFO, "'customer.java' takes over the protected areas of 'cust.java'");
		}

		@Test
		@DisplayName("a different unique id does not take over anything")
		void differentUniqueId() throws Exception {
			MemoryStrategy strategy = new MemoryStrategy();
			String template = "[file (%s, false, %s)]\n[protected ('spec')]\ndefault\n[/protected]\n[/file]";
			assertSuccess(run(strategy, template.formatted("'cust.java'", "'id-1'")));
			strategy.files.put("cust.java", strategy.files.get("cust.java").replace("default", "USER"));
			assertSuccess(run(strategy, template.formatted("'customer.java'", "'id-2'")));
			assertFalse(strategy.files.get("customer.java").contains("USER"));
		}

		@Test
		@DisplayName("a file without a charset is written in the configured default")
		void defaultCharset() throws Exception {
			M2tEngine engine = engine(b -> b.generationStrategy(new FileSystemGenerationStrategy(outputDirectory))
					.defaultCharset(StandardCharsets.ISO_8859_1));
			run(engine, "[file ('out.txt', false)]ä[/file]");
			assertArrayEquals(new byte[] { (byte) 0xE4 }, Files.readAllBytes(outputDirectory.resolve("out.txt")));
		}

		@Test
		@DisplayName("the charset is the fourth argument")
		void charsetFourthArgument() throws Exception {
			run(new FileSystemGenerationStrategy(outputDirectory), "[file ('out.txt', false, 'id', 'ISO-8859-1')]ä[/file]");
			assertArrayEquals(new byte[] { (byte) 0xE4 }, Files.readAllBytes(outputDirectory.resolve("out.txt")));
		}

		@Test
		@DisplayName("a charset name as third argument is a WARNING: the third is the unique id")
		void charsetAsThirdArgument() throws M2tParseException {
			M2tResult result = run("[file ('out.txt', false, 'UTF-8')]x[/file]");
			assertDiagnostic(result, Diagnostic.WARNING, "looks like a charset, which is the fourth argument");
			assertEquals(Map.of("out.txt", "UTF-8"), result.fileUniqueIds());
		}

		@Test
		@DisplayName("a file: URI is a path below the output directory")
		void fileUri() throws Exception {
			run(new FileSystemGenerationStrategy(outputDirectory), "[file ('file:\\\\' + c.name + '.java', false)]x[/file]");
			assertEquals("x", Files.readString(outputDirectory.resolve("Employee.java")));
		}

		@Test
		@DisplayName("an absolute file: URI outside the output directory is refused")
		void fileUriOutside() throws M2tParseException {
			M2tResult result = run(new FileSystemGenerationStrategy(outputDirectory), "[file ('file:///tmp/elsewhere.txt', false)]x[/file]");
			assertDiagnostic(result, Diagnostic.ERROR, "escapes the output directory");
		}
	}

	@Nested
	@DisplayName("#374: §8.4 whitespace")
	class Whitespace {

		@Test
		@DisplayName("a template with CRLF line ends leaves no stray carriage return")
		void crlfTemplate() throws M2tParseException {
			String source = (HEADER + "[template public main(c : EClass)]\n[file ('out', false)]\nA\n"
					+ "[for (n : String | Sequence{'x', 'y'})]\n  [n/]\n[/for]\nZ\n[/file]\n[/template]\n").replace("\n", "\r\n");
			M2tEngine engine = engine(UnaryOperator.identity());
			M2tResult result = engine.execute(engine.parse(source, "doc"), M2tContext.of(employee()));
			assertSuccess(result);
			assertEquals("A\n  x\n  y\nZ", result.generatedFiles().get("out"));
		}

		private String singleLineBlocks(WhitespaceMode mode) throws M2tParseException {
			M2tEngine engine = engine(b -> b.whitespaceMode(mode));
			return run(engine, "[file ('out', false)]\nA\n  [if (true)]X[/if]\nB\n  [if (false)]Y[/if]\nC\n[/file]")
					.generatedFiles().get("out");
		}

		@Test
		@DisplayName("a single-line stand-alone block keeps its line break when it wrote text, in both modes")
		void singleLineStandaloneBlock() throws M2tParseException {
			assertEquals("A\nX\nB\nC", singleLineBlocks(WhitespaceMode.SPEC));
			assertEquals("A\nX\nB\nC", singleLineBlocks(WhitespaceMode.ACCELEO));
		}

		@Test
		@DisplayName("^ removes only the whitespace the called template writes, not the caller's indentation")
		void bolUnderPropagatedIndentation() throws M2tParseException {
			M2tEngine engine = engine(b -> b.whitespaceMode(WhitespaceMode.SPEC));
			Module module = engine.parse(HEADER + "[template public t(c : EClass)]\nl1\n  ^l2\n[/template]\n"
					+ "[template public main(c : EClass)]\n[file ('out', false)]\n    [t(c)/]\n[/file]\n[/template]\n", "doc");
			assertEquals("    l1\n    l2", engine.execute(module, M2tContext.of(employee())).generatedFiles().get("out"));
		}
	}
}
