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

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.engine.M2tEngines;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.engine.internal.M2tWriterStack;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.OpenModeKind;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * The engine hands generated files to its {@link org.eclipse.fennec.m2x.m2t.api.M2tGenerationStrategy}.
 *
 * <p>Until now it never did: {@code createWriter} and {@code closeWriter} had no call site,
 * so a strategy written to produce files produced nothing — silently, with a successful
 * result. These tests pin the contract the SPI has documented since 1.0.
 */
class M2tFileOutputTest {

	@TempDir
	Path outputDirectory;

	@Test
	@DisplayName("generated files land on disk")
	void filesAreWritten() throws Exception {
		generate("""
				[module out(_'http://www.eclipse.org/emf/2002/Ecore')/]
				[template public main(c : EClass)]
				[file ('a.txt', false)]alpha[/file]
				[file ('sub/b.txt', false)]beta[/file]
				[/template]
				""");

		assertEquals("alpha", read("a.txt"));
		assertEquals("beta", read("sub/b.txt"));
	}

	@Test
	@DisplayName("overwrite replaces an existing file, append extends it")
	void overwriteAndAppend() throws Exception {
		Files.writeString(outputDirectory.resolve("over.txt"), "old");
		Files.writeString(outputDirectory.resolve("app.txt"), "old");

		generate("""
				[module out(_'http://www.eclipse.org/emf/2002/Ecore')/]
				[template public main(c : EClass)]
				[file ('over.txt', false)]new[/file]
				[file ('app.txt', true)]new[/file]
				[/template]
				""");

		assertEquals("new", read("over.txt"));
		assertEquals("oldnew", read("app.txt"));
	}

	@Test
	@DisplayName("the charset a file block was opened with reaches the strategy")
	void perFileCharsetIsCarriedThrough() {
		// MOFM2T §8.1.17: the third [file] argument is the unique id, not a charset,
		// so no template syntax reaches FileBlock.charset today. The evaluator computes
		// a charset per file all the same, and it must survive down to the writer.
		M2tWriterStack writers = new M2tWriterStack();
		writers.pushFileWriter("latin.txt", OpenModeKind.OVERWRITE,
				Charset.forName("ISO-8859-1"));
		writers.popWriter();

		assertEquals(Charset.forName("ISO-8859-1"), writers.getFileCharsets().get("latin.txt"));
		assertEquals(OpenModeKind.OVERWRITE, writers.getFileOpenModes().get("latin.txt"));
	}

	@Test
	@DisplayName("a file path escaping the output directory is rejected")
	void pathTraversalIsRejected() {
		FileSystemGenerationStrategy strategy =
				new FileSystemGenerationStrategy(outputDirectory);

		assertThrows(SecurityException.class,
				() -> strategy.createWriter("../escaped.txt", null, StandardCharsets.UTF_8));
		assertThrows(SecurityException.class,
				() -> strategy.createWriter("/etc/passwd", null, StandardCharsets.UTF_8));
	}

	@Test
	@DisplayName("a failing write is reported and the remaining files are still written")
	void writeFailureIsReportedAndDoesNotAbort() throws Exception {
		// A directory where the second file wants to put a regular file
		Files.createDirectories(outputDirectory.resolve("blocked.txt"));

		M2tResult result = generate("""
				[module out(_'http://www.eclipse.org/emf/2002/Ecore')/]
				[template public main(c : EClass)]
				[file ('blocked.txt', false)]nope[/file]
				[file ('fine.txt', false)]yes[/file]
				[/template]
				""");

		assertFalse(result.isSuccess(), "the failed write must be reported");
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("blocked.txt")),
				() -> "diagnostics: " + result.diagnostics());
		assertEquals("yes", read("fine.txt"), "the other file must still be written");
	}

	// --- helpers ---

	private M2tResult generate(String template) throws Exception {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		M2tEngine engine = M2tEngines.create(M2tConfiguration.builder(oclConfig)
				.generationStrategy(new FileSystemGenerationStrategy(outputDirectory))
				.build());

		Module module = engine.parse(template, "out");
		engine.link(module);
		return engine.execute(module, M2tContext.of(EcoreFactory.eINSTANCE.createEClass()));
	}

	private String read(String relativePath) throws IOException {
		return read(relativePath, StandardCharsets.UTF_8);
	}

	private String read(String relativePath, Charset charset) throws IOException {
		return Files.readString(outputDirectory.resolve(relativePath), charset);
	}
}
