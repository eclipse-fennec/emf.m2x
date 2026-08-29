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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * One engine, several threads (#178).
 *
 * <p>An engine is published as an OSGi service and a caller may share one. Two of the things
 * it does on the way into a generation rewrite the caller's AST — linking and MOFM2T §8.4
 * whitespace normalization — and both are done once and then remembered, so without a lock a
 * second thread can evaluate a module while the first is still rewriting it (#184).
 *
 * <p>A concurrency test cannot prove the absence of a race; it can make a present one show up.
 * Both tests below fail loudly when the results differ between threads, which is what a
 * half-rewritten AST produces.
 */
class M2tConcurrentExecutionTest {

	private static final int THREADS = 8;
	private static final int RUNS_PER_THREAD = 25;

	private static final String TEMPLATE = """
			[module gen(_'http://www.eclipse.org/emf/2002/Ecore')/]
			[template public feature(f : EStructuralFeature)][f.name/];[/template]
			[template public main(c : EClass)]
			[file ('out.txt', false)]
			class [c.name/] {
			[for (f : EStructuralFeature | c.eStructuralFeatures)][feature(f)/][/for]
			}
			[/file]
			[/template]
			""";

	@Test
	@Timeout(value = 60, unit = TimeUnit.SECONDS)
	@DisplayName("the same module generated from several threads gives every thread the same text")
	void oneModuleFromSeveralThreads() throws Exception {
		M2tEngine engine = engine();
		Module module = engine.parse(TEMPLATE, "gen");
		String expected = generate(engine, module);

		List<String> results = inParallel(() -> generate(engine, module));

		assertEquals(THREADS * RUNS_PER_THREAD, results.size());
		assertTrue(results.stream().allMatch(expected::equals),
				() -> "every thread has to see the same document; got "
						+ results.stream().distinct().toList());
	}

	@Test
	@Timeout(value = 60, unit = TimeUnit.SECONDS)
	@DisplayName("threads that link and generate at the same time do not see a half-linked module")
	void linkingWhileGenerating() throws Exception {
		// The first execution of a module is the one that links and normalizes it. Starting all
		// the threads on an unlinked module is what puts them into that window together.
		M2tEngine engine = engine();
		Module module = engine.parse(TEMPLATE, "gen");

		List<String> results = inParallel(() -> generate(engine, module));

		assertEquals(1, results.stream().distinct().count(),
				() -> "one module, one document: " + results.stream().distinct().toList());
		assertTrue(results.get(0).contains("class Thing"), results.get(0));
	}

	// --- helpers ---

	private static M2tEngine engine() {
		return M2tEngines.create(M2tConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build()).build());
	}

	private static String generate(M2tEngine engine, Module module) {
		EClass type = EcoreFactory.eINSTANCE.createEClass();
		type.setName("Thing");
		M2tResult result = engine.execute(module, M2tContext.of(type));
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		return result.generatedFiles().get("out.txt");
	}

	private static List<String> inParallel(Callable<String> work) throws Exception {
		ExecutorService pool = Executors.newFixedThreadPool(THREADS);
		try {
			List<Callable<String>> jobs = new ArrayList<>();
			for (int i = 0; i < THREADS * RUNS_PER_THREAD; i++) {
				jobs.add(work);
			}
			List<String> results = new ArrayList<>();
			for (Future<String> future : pool.invokeAll(jobs)) {
				results.add(future.get(30, TimeUnit.SECONDS));
			}
			return results;
		} finally {
			pool.shutdownNow();
		}
	}
}
