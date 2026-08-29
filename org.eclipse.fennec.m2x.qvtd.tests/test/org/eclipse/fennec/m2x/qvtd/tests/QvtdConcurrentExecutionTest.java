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
package org.eclipse.fennec.m2x.qvtd.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.qvtd.api.BasicQvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * One engine and one transformation, several threads (#178).
 *
 * <p>The third of the three transformation engines. An engine is published as an OSGi service
 * and a caller may share one; QVT-R links imports into the transformation before it runs, so
 * two threads on the same transformation meet in that merge.
 *
 * <p>A concurrency test cannot prove the absence of a race; it can make a present one show up.
 * This one fails when two threads produce different target models, or when one of them fails.
 */
class QvtdConcurrentExecutionTest extends AbstractQvtdEngineTest {

	private static final int THREADS = 8;
	private static final int RUNS_PER_THREAD = 15;

	private static final String TRANSFORMATION = """
			transformation concurrent(uml : simpleuml, rdbms : simplerdbms) {
			    top relation PackageToSchema {
			        n : String;
			        checkonly domain uml p : Package { name = n };
			        enforce domain rdbms s : Schema { name = n };
			    }
			}
			""";

	@Test
	@Timeout(value = 60, unit = TimeUnit.SECONDS)
	@DisplayName("the same transformation run from several threads gives every thread the same model")
	void oneTransformationFromSeveralThreads() throws Exception {
		RelationalTransformation transformation = engine.parse(TRANSFORMATION, "concurrent");

		List<String> results = inParallel(() -> runOnce(transformation));

		assertEquals(THREADS * RUNS_PER_THREAD, results.size());
		assertEquals(1, results.stream().distinct().count(),
				() -> "one transformation, one result: " + results.stream().distinct().toList());
		assertTrue(results.get(0).contains("Library"), results.get(0));
	}

	private String runOnce(RelationalTransformation transformation) {
		QvtdModelExtent target = new BasicQvtdModelExtent();
		QvtdExecutionResult result = engine.execute(transformation,
				QvtdExecutionContext.enforce("rdbms",
						Map.of("uml", QvtdModelExtent.of(createPackage("Library")), "rdbms", target)));
		if (!result.isSuccess()) {
			return "failed: " + result.diagnostics();
		}
		List<String> names = new ArrayList<>();
		for (EObject produced : target.getContents()) {
			names.add(String.valueOf(
					produced.eGet(produced.eClass().getEStructuralFeature("name"))));
		}
		return names.toString();
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
