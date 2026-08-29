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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.io.TempDir;

/**
 * M2T guards that exist in production and that no test would have missed (#177, part of #188).
 *
 * <p>The depth guard for queries, the path containment seen from the template rather than from
 * the strategy, the protected-area switch in its off position, and a cycle in the override
 * chain: each one is a mitigation the M2T security analysis names.
 */
class M2tGuardCoverageTest {

	@TempDir
	Path outputDirectory;

	@Nested
	@DisplayName("T-1: depth")
	class Depth {

		@Test
		@Timeout(value = 15, unit = TimeUnit.SECONDS)
		@DisplayName("a query that calls itself ends in a diagnostic, not in a StackOverflowError")
		void selfRecursiveQueryTerminates() throws Exception {
			// The template depth guard is tested; queries have their own counter and their own
			// way in. Without it this is an Error, which no catch in the engine turns into a
			// diagnostic — the generation would take the calling thread with it.
			M2tResult result = generate("""
					[module gen(_'http://www.eclipse.org/emf/2002/Ecore')/]
					[query public loop(c : EClass) : String = loop(c)/]
					[template public main(c : EClass)]
					[file ('out.txt', false)][loop(c)/][/file]
					[/template]
					""", builder -> builder.maxTemplateDepth(20));

			assertFalse(result.isSuccess(), "a runaway query is a failure, not an output");
			assertTrue(result.diagnostics().stream()
					.anyMatch(d -> d.getMessage().contains("depth")),
					() -> "diagnostics: " + result.diagnostics());
		}
	}

	@Nested
	@DisplayName("T-1: the stack, under the default limit")
	class DefaultDepth {

		@Test
		@Timeout(value = 30, unit = TimeUnit.SECONDS)
		@DisplayName("a recursion the stack cannot take is a diagnostic, not an Error")
		void aRecursionDeeperThanTheStackIsReported() throws Exception {
			// Measured on this machine: at maxTemplateDepth 500 the depth guard reports, at the
			// default of 1000 the stack runs out first and the generation used to die with a
			// StackOverflowError — an Error, so the caller got no result at all (#178). The
			// default is not lowered: how deep a stack goes differs per platform, and catching
			// it does not have to guess.
			M2tResult result = generate("""
					[module gen(_'http://www.eclipse.org/emf/2002/Ecore')/]
					[template public loop(c : EClass)][loop(c)/][/template]
					[template public main(c : EClass)][loop(c)/][/template]
					""", UnaryOperator.identity());

			assertFalse(result.isSuccess(), "a runaway recursion is a failure, whichever limit stops it");
			assertTrue(result.diagnostics().stream()
					.anyMatch(d -> d.getMessage().contains("stack ran out")
							|| d.getMessage().contains("depth exceeded")),
					() -> "diagnostics: " + result.diagnostics());
		}
	}

	@Nested
	@DisplayName("T-4: where a template may write")
	class PathContainment {

		@Test
		@DisplayName("a path escaping the output directory is reported and nothing is written")
		void traversalThroughTheEngineIsReportedNotWritten() throws Exception {
			// The strategy refuses the path; this is the same thing seen from the template,
			// which is where a generator author would meet it
			Path outside = outputDirectory.resolve("..").resolve("escaped.txt").normalize();
			Files.deleteIfExists(outside);

			M2tResult result = generate("""
					[module gen(_'http://www.eclipse.org/emf/2002/Ecore')/]
					[template public main(c : EClass)]
					[file ('../escaped.txt', false)]escaped[/file]
					[file ('fine.txt', false)]fine[/file]
					[/template]
					""", builder -> builder
							.generationStrategy(new FileSystemGenerationStrategy(outputDirectory)));

			assertFalse(result.isSuccess(), "the escape must be reported");
			assertFalse(Files.exists(outside), "and nothing may be written outside");
			assertEquals("fine", Files.readString(outputDirectory.resolve("fine.txt")),
					"while the rest of the generation still runs");
		}
	}

	@Nested
	@DisplayName("T-6: the protected-area switch")
	class ProtectedAreas {

		private static final String TEMPLATE = """
				[module gen(_'http://www.eclipse.org/emf/2002/Ecore')/]
				[template public main(c : EClass)]
				[file ('out.txt', false)]
				[protected ('body')]
				default
				[/protected]
				[/file]
				[/template]
				""";

		@Test
		@DisplayName("switched off, what the last generation left in a protected area is overwritten")
		void disabledDoesNotMerge() throws Exception {
			// The real flow: generate once, a developer edits inside the marker, generate again
			editInsideTheProtectedArea(handWritten(true));

			generate(TEMPLATE, builder -> builder
					.generationStrategy(new FileSystemGenerationStrategy(outputDirectory))
					.protectedAreaEnabled(false));

			assertFalse(Files.readString(outputDirectory.resolve("out.txt")).contains("hand-written"),
					"with protected areas off the file is regenerated, so the switch is real");
		}

		@Test
		@DisplayName("switched on, it survives")
		void enabledMerges() throws Exception {
			editInsideTheProtectedArea(handWritten(true));

			generate(TEMPLATE, builder -> builder
					.generationStrategy(new FileSystemGenerationStrategy(outputDirectory))
					.protectedAreaEnabled(true));

			assertTrue(Files.readString(outputDirectory.resolve("out.txt")).contains("hand-written"),
					"this is what the disabled case is being compared against (D31)");
		}

		/** Generates once, so the file carries the markers this engine writes. */
		private String handWritten(boolean enabled) throws Exception {
			generate(TEMPLATE, builder -> builder
					.generationStrategy(new FileSystemGenerationStrategy(outputDirectory))
					.protectedAreaEnabled(enabled));
			return Files.readString(outputDirectory.resolve("out.txt"));
		}

		private void editInsideTheProtectedArea(String generated) throws Exception {
			assertTrue(generated.contains("default"), () -> "the area has content: " + generated);
			Files.writeString(outputDirectory.resolve("out.txt"),
					generated.replace("default", "hand-written"));
		}
	}

	@Nested
	@DisplayName("entry points")
	class EntryPoints {

		@Test
		@DisplayName("a module without a main template says so")
		void moduleWithoutMainTemplate() throws Exception {
			M2tEngine engine = engine(UnaryOperator.identity());
			Module module = engine.parse("""
					[module gen(_'http://www.eclipse.org/emf/2002/Ecore')/]
					[template public helper(c : EClass)]nothing[/template]
					""", "gen");

			IllegalStateException failure = assertThrows(IllegalStateException.class,
					() -> engine.execute(module,
							M2tContext.of(EcoreFactory.eINSTANCE.createEClass())));

			assertTrue(failure.getMessage().contains("main"), failure::getMessage);
		}
	}

	// --- helpers ---

	private M2tEngine engine(UnaryOperator<M2tConfiguration.Builder> configure) {
		return M2tEngines.create(configure.apply(M2tConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build())).build());
	}

	private M2tResult generate(String template, UnaryOperator<M2tConfiguration.Builder> configure)
			throws M2tParseException {
		M2tEngine engine = engine(configure);
		Module module = engine.parse(template, "gen");
		engine.link(module);
		return engine.execute(module, M2tContext.of(EcoreFactory.eINSTANCE.createEClass()));
	}
}
