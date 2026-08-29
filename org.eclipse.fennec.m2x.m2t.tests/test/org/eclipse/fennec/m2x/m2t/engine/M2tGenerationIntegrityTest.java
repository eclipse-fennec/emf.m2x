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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tGenerationStrategy;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.api.M2tUnit;
import org.eclipse.fennec.m2x.m2t.api.M2tUnitResolver;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.OpenModeKind;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * What a generation writes, and what it is allowed to see (#184).
 *
 * <p>Each test here stands for one way a generation used to be able to go wrong quietly:
 * output lost on a failing close, a template of an unrelated module taking over, a symbolic
 * link followed out of the output directory, a target directory that nobody read. None of
 * them raised anything at the time — the result said success — which is what makes them
 * worth a test that fails loudly if the guard is ever taken back out.
 */
class M2tGenerationIntegrityTest {

	@TempDir
	Path outputDirectory;

	@Nested
	@DisplayName("writing")
	class Writing {

		@Test
		@DisplayName("a close that fails is reported, not swallowed")
		void closeFailureIsReported() throws Exception {
			// A buffered writer writes most of a file inside close(). Silence there means a
			// disk-full or quota error is reported as a successful generation over a truncated
			// file — the one failure that has no other way of reaching the caller.
			M2tResult result = generate(new FailingCloseStrategy(), """
					[module out(_'http://www.eclipse.org/emf/2002/Ecore')/]
					[template public main(c : EClass)]
					[file ('a.txt', false)]alpha[/file]
					[/template]
					""");

			assertFalse(result.isSuccess(), "a failing close must fail the generation");
			assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains("a.txt")),
					() -> "diagnostics: " + result.diagnostics());
		}

		@Test
		@DisplayName("a symbolic link where the file goes is refused, not followed")
		void symbolicLinkIsNotFollowed() throws Exception {
			Path outside = Files.createTempDirectory("m2t-outside").resolve("victim.txt");
			Files.writeString(outside, "untouched");
			Path link = outputDirectory.resolve("link.txt");
			try {
				Files.createSymbolicLink(link, outside);
			} catch (IOException | UnsupportedOperationException unsupported) {
				assumeTrue(false, "symbolic links are not available here");
			}

			M2tResult result = generate(new FileSystemGenerationStrategy(outputDirectory), """
					[module out(_'http://www.eclipse.org/emf/2002/Ecore')/]
					[template public main(c : EClass)]
					[file ('link.txt', false)]overwritten[/file]
					[/template]
					""");

			assertEquals("untouched", Files.readString(outside),
					"a link planted in the output directory must not reach outside it");
			assertFalse(result.isSuccess(), "the refused write must be reported");
		}

		@Test
		@DisplayName("the directory a diagnostic names is the one the template named")
		void diagnosticsDoNotLeakTheOutputDirectory() throws Exception {
			Files.createDirectories(outputDirectory.resolve("blocked.txt"));

			M2tResult result = generate(new FileSystemGenerationStrategy(outputDirectory), """
					[module out(_'http://www.eclipse.org/emf/2002/Ecore')/]
					[template public main(c : EClass)]
					[file ('blocked.txt', false)]nope[/file]
					[/template]
					""");

			assertTrue(result.diagnostics().stream().noneMatch(
					d -> d.getMessage().contains(outputDirectory.toString())),
					() -> "the absolute output directory is not the template's business: "
							+ result.diagnostics());
		}

		@Test
		@DisplayName("the target directory of the context is where files go")
		void contextTargetDirectoryIsUsed() throws Exception {
			// M2tContext documents targetDirectory as "the base directory for generated files".
			// It was read by nobody: without a configured strategy the engine wrote nothing.
			M2tEngine engine = M2tEngines.create(M2tConfiguration.builder(
					OclConfiguration.builder(new OclParserSupport()).build()).build());
			Module module = engine.parse("""
					[module out(_'http://www.eclipse.org/emf/2002/Ecore')/]
					[template public main(c : EClass)]
					[file ('a.txt', false)]alpha[/file]
					[/template]
					""", "out");

			M2tResult result = engine.execute(module, M2tContext.of(
					EcoreFactory.eINSTANCE.createEClass(), outputDirectory));

			assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
			assertEquals("alpha", Files.readString(outputDirectory.resolve("a.txt")));
		}
	}

	@Nested
	@DisplayName("what a generation may see")
	class Visibility {

		private static final String BASE = """
				[module base(_'http://www.eclipse.org/emf/2002/Ecore')/]
				[template public greet(c : EClass)]from the base[/template]
				[template public main(c : EClass)]
				[file ('out.txt', false)][greet(c)/][/file]
				[/template]
				""";

		private static final String INTRUDER = """
				[module intruder(_'http://www.eclipse.org/emf/2002/Ecore') extends base/]
				[template public greet(c : EClass) overrides greet]from the intruder[/template]
				[template public main(c : EClass)]
				[file ('out.txt', false)][greet(c)/][/file]
				[/template]
				""";

		@Test
		@DisplayName("a module linked later cannot override the templates of an earlier generation")
		void anUnrelatedModuleDoesNotOverride() throws Exception {
			// The override index used to be built from every module the engine had ever
			// linked. An engine is a long-lived service — under OSGi one instance serves
			// every caller — so any module linked afterwards, for anybody, could take over
			// a template of a generation that never asked for it.
			Module[] parsedBase = new Module[1];
			M2tUnitResolver resolver = name -> "base".equals(name) && parsedBase[0] != null
					? Optional.of(new M2tUnit.CompiledUnit("base", parsedBase[0]))
					: Optional.empty();
			M2tEngine engine = M2tEngines.create(M2tConfiguration.builder(
					OclConfiguration.builder(new OclParserSupport()).build())
					.addUnitResolver(resolver)
					.unitResolverEnabled(true)
					.build());

			Module base = engine.parse(BASE, "base");
			engine.link(base);
			parsedBase[0] = base;

			// The intruder extends and overrides the very same base module, in its own link
			Module intruder = engine.parse(INTRUDER, "intruder");
			engine.link(intruder);

			assertEquals("from the intruder", text(engine, intruder),
					"the override itself has to work, or this test proves nothing");
			assertEquals("from the base", text(engine, base),
					"a generation started from the base sees the base");
		}

		private String text(M2tEngine engine, Module module) {
			M2tResult result = engine.execute(module,
					M2tContext.of(EcoreFactory.eINSTANCE.createEClass()));
			assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
			return result.generatedFiles().get("out.txt");
		}
	}

	// --- helpers ---

	private M2tResult generate(M2tGenerationStrategy strategy, String template) throws Exception {
		M2tEngine engine = M2tEngines.create(M2tConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build())
				.generationStrategy(strategy)
				.build());
		Module module = engine.parse(template, "out");
		engine.link(module);
		return engine.execute(module, M2tContext.of(EcoreFactory.eINSTANCE.createEClass()));
	}

	/** A strategy whose writer keeps everything until close, and then fails — a full disk. */
	private static final class FailingCloseStrategy implements M2tGenerationStrategy {

		@Override
		public Writer createWriter(String filePath, OpenModeKind mode, Charset charset) {
			return new Writer() {

				@Override
				public void write(char[] buffer, int offset, int length) {
					// buffered: nothing reaches the file yet
				}

				@Override
				public void flush() {
					// still nothing
				}

				@Override
				public void close() throws IOException {
					throw new IOException("No space left on device");
				}
			};
		}

		@Override
		public String readExistingContent(String filePath, Charset charset) {
			return null;
		}
	}
}
