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
package org.eclipse.fennec.m2x.qvto.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * One engine and one transformation, several threads (#178).
 *
 * <p>An engine is published as an OSGi service and a caller may share one. QVT-O links on
 * every {@code execute}, and linking rewrites the transformation in place — so two threads
 * running the same transformation are in that rewrite together.
 *
 * <p>A concurrency test cannot prove the absence of a race; it can make a present one show up.
 * This one fails when two threads see different output, or when one of them fails.
 */
class QvtoConcurrentExecutionTest extends AbstractQvtoEngineTest {

	private static final int THREADS = 8;
	private static final int RUNS_PER_THREAD = 20;

	private static final String TRANSFORMATION = """
			modeltype SRC uses 'http://test/source/1.0';
			modeltype TGT uses 'http://test/target/1.0';
			transformation concurrent(in s : SRC, out t : TGT) {
			    mapping SourceElement::toTarget() : r : TargetElement {
			        r.name := 'copy of ' + self.name;
			    }
			    main() {
			        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
			    }
			}
			""";

	@Test
	@Timeout(value = 60, unit = TimeUnit.SECONDS)
	@DisplayName("the same transformation run from several threads gives every thread the same model")
	void oneTransformationFromSeveralThreads() throws Exception {
		OperationalTransformation transformation = parse(TRANSFORMATION);

		List<String> results = inParallel(() -> runOnce(transformation));

		assertEquals(THREADS * RUNS_PER_THREAD, results.size());
		assertEquals(1, results.stream().distinct().count(),
				() -> "one transformation, one result: " + results.stream().distinct().toList());
		assertTrue(results.get(0).contains("copy of element"), results.get(0));
	}

	private static String runOnce(OperationalTransformation transformation) {
		QvtoModelExtent source = new BasicQvtoModelExtent(createSourceElement("element", 1));
		QvtoModelExtent target = new BasicQvtoModelExtent();
		QvtoExecutionResult result = engine.execute(transformation,
				QvtoExecutionContext.of(source, target));
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
