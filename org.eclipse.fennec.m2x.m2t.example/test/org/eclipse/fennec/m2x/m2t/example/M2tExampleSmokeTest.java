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
package org.eclipse.fennec.m2x.m2t.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Keeps the example honest: both mains generate the same catalog — the zero-copies manuscript
 * dropped by the if block, the imported query formatting the lines, the log file appending, and
 * the protected area surviving a regeneration with an edit in between.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class M2tExampleSmokeTest {

	@Test
	void theAdHocExample_generates(@TempDir Path gen) throws Exception {
		AdHocExample.run(gen);
		assertCatalog(gen);
	}

	@Test
	void theCompiledUnitExample_generates(@TempDir Path gen) throws Exception {
		CompiledUnitExample.run(gen);
		assertCatalog(gen);
	}

	@Test
	void theProtectedArea_survivesRegeneration_andTheLogAppends(@TempDir Path gen) throws Exception {
		AdHocExample.run(gen);
		Path catalog = gen.resolve("catalog.md");
		String first = Files.readString(catalog);
		assertTrue(first.contains("_add curator notes here_"), "the default content of a fresh area");

		// The curator edits inside the protected area — regeneration must keep it
		Files.writeString(catalog, first.replace("_add curator notes here_", "shelf B is under repair"));
		AdHocExample.run(gen);
		String second = Files.readString(catalog);
		assertTrue(second.contains("shelf B is under repair"), "the edit survives the regeneration");
		assertFalse(second.contains("_add curator notes here_"), "and the default does not come back");

		assertEquals(2, Files.readString(gen.resolve("runs.log")).strip().split("\n").length,
				"the log file appends, one line per run");
	}

	private static void assertCatalog(Path gen) throws Exception {
		String catalog = Files.readString(gen.resolve("catalog.md"));
		assertTrue(catalog.contains("# City Library"));
		assertTrue(catalog.contains("- Melville: Moby Dick (1851)"), catalog);
		assertTrue(catalog.contains("- Abelson: Structure and Interpretation of Computer Programs (1985)"));
		assertFalse(catalog.contains("Lost Manuscript"), "the zero-copies manuscript is dropped by the if block");
		assertTrue(Files.exists(gen.resolve("runs.log")), "the append-mode file is written");
	}
}
