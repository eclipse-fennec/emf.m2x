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

import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * What the engine remembers about a module, and how a caller gets rid of it.
 *
 * <p>The engine caches parse results, link and normalization state and the indentation
 * of template invocations. Those caches used to be keyed by identity and never emptied:
 * a long-lived engine held every module it had ever parsed. They are keyed weakly now,
 * and {@link M2tEngineImpl#release(Module)} makes the release deterministic — garbage
 * collection is a safety net, not a schedule, so the contract that is tested here is
 * the explicit one.
 */
class M2tEngineRetentionTest {

	private static final String BASE = """
			[module base(_'http://www.eclipse.org/emf/2002/Ecore')/]
			[template public greet(c : EClass)]hello[/template]
			""";

	private static final String CHILD = """
			[module child(_'http://www.eclipse.org/emf/2002/Ecore') extends base/]
			[template public greet(c : EClass) overrides greet]hi[/template]
			""";

	private static final String TEMPLATE = """
			[module doc(_'http://www.eclipse.org/emf/2002/Ecore')/]
			[template public main(c : EClass)]
			[file ('out.txt', false)]done[/file]
			[/template]
			""";

	@Test
	@DisplayName("a released module is unknown to the linker")
	void releasedModuleIsForgotten() throws Exception {
		M2tEngineImpl engine = engine();
		Module base = engine.parse(BASE, "base");
		Module child = engine.parse(CHILD, "child");

		// Linking resolves the child's extends against the cached parse result of base
		assertTrue(engine.link(base, child).isEmpty(),
				() -> "extends must resolve while both modules are known");

		engine.release(base);

		// With base forgotten, its parse result is gone and the extends cannot resolve
		assertFalse(engine.link(base, child).isEmpty(),
				"after release the linker no longer knows base");
	}

	@Test
	@DisplayName("clearCaches forgets every module")
	void clearCachesForgetsEverything() throws Exception {
		M2tEngineImpl engine = engine();
		Module base = engine.parse(BASE, "base");
		Module child = engine.parse(CHILD, "child");
		assertTrue(engine.link(base, child).isEmpty());

		engine.clearCaches();

		assertFalse(engine.link(base, child).isEmpty(),
				"after clearCaches no module is known any more");
	}

	@Test
	@DisplayName("a module survives repeated execution — releasing is the caller's choice")
	void moduleStaysUsableUntilReleased() throws Exception {
		M2tEngineImpl engine = engine();
		Module module = engine.parse(TEMPLATE, "doc");
		engine.link(module);

		for (int i = 0; i < 3; i++) {
			assertEquals("done", execute(engine, module));
		}
	}

	// --- helpers ---

	private M2tEngineImpl engine() {
		return new M2tEngineImpl(M2tConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build()).build());
	}

	private String execute(M2tEngineImpl engine, Module module) {
		M2tResult result = engine.execute(module, M2tContext.of(EcoreFactory.eINSTANCE.createEClass()));
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		return result.generatedFiles().get("out.txt");
	}
}
