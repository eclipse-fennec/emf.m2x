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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoEvaluationOptions;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * Performance benchmarks for the Fennec QVT-O engine (Q-P1 to Q-P8).
 *
 * <p>Measures parse throughput, mapping execution, trace lookup degradation,
 * large model scale, deep call stacks, collection resolve, helper/query
 * throughput, and trace recording overhead.
 *
 * <p>Each test isolates what it measures: model creation is always outside
 * the timed section, and execution tests run multiple iterations with fresh
 * extents to get stable per-operation numbers.
 *
 * <p>Run via: {@code ./gradlew org.eclipse.fennec.m2x.qvto.tests:perfTest}
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Tag("perf")
class QvtoPerformanceTest extends AbstractQvtoEngineTest {

	private static final int WARMUP = 50;
	private static final int PARSE_ITERATIONS = 5_000;
	private static final int EXEC_ITERATIONS = 200;
	private static final int MODEL_SIZE = 200;

	// ==================== Q-P1: Parse Throughput ====================

	@Test
	void qp1_parseThroughput(TestInfo info) throws Exception {
		String source = """
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				helper SourceElement::label() : String { return 'T_' + self.name; }
				mapping SourceElement::toTarget() : TargetElement {
				    name := self.name;
				    value := self.value * 2;
				}
				main() { s.objectsOfType(SourceElement)->map toTarget(); }
				""";

		// Warmup
		for (int i = 0; i < WARMUP; i++) {
			parse(source);
		}

		long start = System.nanoTime();
		for (int i = 0; i < PARSE_ITERATIONS; i++) {
			parse(source);
		}
		long elapsed = System.nanoTime() - start;

		long nsPerOp = elapsed / PARSE_ITERATIONS;
		printResult(info, PARSE_ITERATIONS, nsPerOp);
		assertTrue(nsPerOp > 0);
	}

	// ==================== Q-P2: Simple Mapping (Baseline) ====================

	@Test
	void qp2_simpleMappingThroughput(TestInfo info) throws Exception {
		String source = """
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				mapping SourceElement::toTarget() : TargetElement {
				    name := self.name;
				    value := self.value * 2;
				}
				main() { s.objectsOfType(SourceElement)->map toTarget(); }
				""";

		OperationalTransformation t = parse(source);
		List<EObject> elements = createSourceElements(MODEL_SIZE);

		// Warmup — fresh extents each iteration
		for (int i = 0; i < WARMUP; i++) {
			engine.execute(t, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(elements), emptyExtent()));
		}

		// Timed: N iterations × MODEL_SIZE mappings
		long start = System.nanoTime();
		for (int i = 0; i < EXEC_ITERATIONS; i++) {
			QvtoExecutionResult result = engine.execute(t, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(elements), emptyExtent()));
			assertTrue(result.isSuccess());
		}
		long elapsed = System.nanoTime() - start;

		long totalMappings = (long) EXEC_ITERATIONS * MODEL_SIZE;
		long nsPerMapping = elapsed / totalMappings;
		long nsPerExec = elapsed / EXEC_ITERATIONS;
		printResult(info, "mapping", totalMappings, nsPerMapping);
		printResult(info, "execution", EXEC_ITERATIONS, nsPerExec);
	}

	// ==================== Q-P3: Trace Lookup Degradation ====================

	@Test
	void qp3_traceLookupDegradation(TestInfo info) throws Exception {
		String source = """
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				mapping SourceElement::toTarget() : TargetElement {
				    name := self.name;
				    value := self.value;
				    ref := self.resolveone(TargetElement);
				}
				main() { s.objectsOfType(SourceElement)->map toTarget(); }
				""";

		OperationalTransformation t = parse(source);
		QvtoEvaluationOptions opts = QvtoEvaluationOptions.defaults().withTracing(true);

		int[] sizes = {100, 500, 2000};
		int iterations = 50;
		for (int size : sizes) {
			List<EObject> elements = createSourceElements(size);

			// Warmup
			for (int w = 0; w < 10; w++) {
				engine.execute(t, QvtoExecutionContext.of(
						new BasicQvtoModelExtent(elements), emptyExtent()), opts);
			}

			// Timed: multiple iterations per size
			long start = System.nanoTime();
			for (int i = 0; i < iterations; i++) {
				QvtoExecutionResult result = engine.execute(t, QvtoExecutionContext.of(
						new BasicQvtoModelExtent(elements), emptyExtent()), opts);
				assertTrue(result.isSuccess());
			}
			long elapsed = System.nanoTime() - start;

			long totalMappings = (long) iterations * size;
			long nsPerMapping = elapsed / totalMappings;
			long nsPerExec = elapsed / iterations;
			System.out.printf("[%s] size=%,d: %,.0f ns/mapping, %,.0f ns/exec (%d iterations, %.1f ms total)%n",
					info.getDisplayName(), size, (double) nsPerMapping, (double) nsPerExec,
					iterations, elapsed / 1_000_000.0);
		}
	}

	// ==================== Q-P4: Large Model Scale ====================

	@Test
	void qp4_largeModelScale(TestInfo info) throws Exception {
		String source = """
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				mapping SourceElement::toTarget() : TargetElement {
				    name := self.name;
				    value := self.value * 2;
				}
				main() { s.objectsOfType(SourceElement)->map toTarget(); }
				""";

		OperationalTransformation t = parse(source);

		// Compare different model sizes to show scaling behavior
		int[] sizes = {200, 1_000, 5_000};
		int iterations = 20;
		for (int size : sizes) {
			List<EObject> elements = createSourceElements(size);

			// Warmup
			for (int w = 0; w < 5; w++) {
				engine.execute(t, QvtoExecutionContext.of(
						new BasicQvtoModelExtent(elements), emptyExtent()));
			}

			// Timed
			long start = System.nanoTime();
			for (int i = 0; i < iterations; i++) {
				QvtoExecutionResult result = engine.execute(t, QvtoExecutionContext.of(
						new BasicQvtoModelExtent(elements), emptyExtent()));
				assertTrue(result.isSuccess());
			}
			long elapsed = System.nanoTime() - start;

			long totalElements = (long) iterations * size;
			long nsPerElement = elapsed / totalElements;
			long nsPerExec = elapsed / iterations;
			System.out.printf("[%s] size=%,d: %,.0f ns/element, %,.0f ns/exec (%d iterations, %.1f ms total)%n",
					info.getDisplayName(), size, (double) nsPerElement, (double) nsPerExec,
					iterations, elapsed / 1_000_000.0);
		}
	}

	// ==================== Q-P5: Deep Mapping Call Stack ====================

	@Test
	void qp5_deepCallStack(TestInfo info) throws Exception {
		// Baseline: mapping without recursion
		String baselineSource = """
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				mapping SourceElement::toTarget() : TargetElement {
				    name := self.name;
				    value := self.value;
				}
				main() { s.objectsOfType(SourceElement)->map toTarget(); }
				""";

		// Deep: mapping + depth-10 recursive helper
		String deepSource = """
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				helper recurse(n : Integer) : Integer {
				    return if n <= 0 then 0 else n + recurse(n - 1) endif;
				}
				mapping SourceElement::toTarget() : TargetElement {
				    name := self.name;
				    value := recurse(10);
				}
				main() { s.objectsOfType(SourceElement)->map toTarget(); }
				""";

		OperationalTransformation tBaseline = parse(baselineSource);
		OperationalTransformation tDeep = parse(deepSource);
		List<EObject> elements = createSourceElements(MODEL_SIZE);

		// Warmup both
		for (int w = 0; w < WARMUP; w++) {
			engine.execute(tBaseline, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(elements), emptyExtent()));
			engine.execute(tDeep, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(elements), emptyExtent()));
		}

		// Timed: baseline
		long startBase = System.nanoTime();
		for (int i = 0; i < EXEC_ITERATIONS; i++) {
			engine.execute(tBaseline, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(elements), emptyExtent()));
		}
		long elapsedBase = System.nanoTime() - startBase;

		// Timed: deep
		long startDeep = System.nanoTime();
		for (int i = 0; i < EXEC_ITERATIONS; i++) {
			engine.execute(tDeep, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(elements), emptyExtent()));
		}
		long elapsedDeep = System.nanoTime() - startDeep;

		long totalMappings = (long) EXEC_ITERATIONS * MODEL_SIZE;
		long nsBase = elapsedBase / totalMappings;
		long nsDeep = elapsedDeep / totalMappings;
		double overhead = (double) nsDeep / nsBase;

		System.out.printf("[%s] baseline (no recursion): %,.0f ns/mapping%n",
				info.getDisplayName(), (double) nsBase);
		System.out.printf("[%s] deep (depth-10 recurse): %,.0f ns/mapping%n",
				info.getDisplayName(), (double) nsDeep);
		System.out.printf("[%s] call-stack overhead: %.2fx%n",
				info.getDisplayName(), overhead);
	}

	// ==================== Q-P6: Resolve auf Collection ====================

	@Test
	void qp6_resolveCollection(TestInfo info) throws Exception {
		// Baseline: only mapping, no resolve
		String baselineSource = """
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				mapping SourceElement::toTarget() : TargetElement {
				    name := self.name;
				    value := self.value;
				}
				main() { s.objectsOfType(SourceElement)->map toTarget(); }
				""";

		// With collection resolve after mapping
		String resolveSource = """
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				mapping SourceElement::toTarget() : TargetElement {
				    name := self.name;
				    value := self.value;
				}
				main() {
				    s.objectsOfType(SourceElement)->map toTarget();
				    var resolved := s.objectsOfType(SourceElement)->resolve(TargetElement);
				    log('resolved: ' + resolved->size().repr());
				}
				""";

		OperationalTransformation tBase = parse(baselineSource);
		OperationalTransformation tResolve = parse(resolveSource);

		int[] sizes = {100, 500};
		int iterations = 50;
		for (int size : sizes) {
			List<EObject> elements = createSourceElements(size);

			// Warmup both
			for (int w = 0; w < 10; w++) {
				engine.execute(tBase, QvtoExecutionContext.of(
						new BasicQvtoModelExtent(elements), emptyExtent()));
				engine.execute(tResolve, QvtoExecutionContext.of(
						new BasicQvtoModelExtent(elements), emptyExtent()));
			}

			// Timed: baseline
			long startBase = System.nanoTime();
			for (int i = 0; i < iterations; i++) {
				engine.execute(tBase, QvtoExecutionContext.of(
						new BasicQvtoModelExtent(elements), emptyExtent()));
			}
			long elapsedBase = System.nanoTime() - startBase;

			// Timed: with resolve
			long startResolve = System.nanoTime();
			for (int i = 0; i < iterations; i++) {
				engine.execute(tResolve, QvtoExecutionContext.of(
						new BasicQvtoModelExtent(elements), emptyExtent()));
			}
			long elapsedResolve = System.nanoTime() - startResolve;

			long nsBase = elapsedBase / iterations;
			long nsResolve = elapsedResolve / iterations;
			long resolveOverhead = nsResolve - nsBase;
			System.out.printf("[%s] size=%d: baseline=%,.0f ns/exec, with-resolve=%,.0f ns/exec, resolve-cost=%,.0f ns (%.2fx)%n",
					info.getDisplayName(), size, (double) nsBase, (double) nsResolve,
					(double) resolveOverhead, (double) nsResolve / nsBase);
		}
	}

	// ==================== Q-P7: Helper/Query Throughput ====================

	@Test
	void qp7_helperQueryThroughput(TestInfo info) throws Exception {
		// Baseline: mapping only (no query call)
		String baselineSource = """
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				mapping SourceElement::toTarget() : TargetElement {
				    name := self.name;
				    value := self.value;
				}
				main() { s.objectsOfType(SourceElement)->map toTarget(); }
				""";

		// With query: each mapping calls a query
		String querySource = """
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				query SourceElement::nameLen() : Integer = self.name.size();
				mapping SourceElement::toTarget() : TargetElement {
				    name := self.name;
				    value := self.nameLen();
				}
				main() { s.objectsOfType(SourceElement)->map toTarget(); }
				""";

		OperationalTransformation tBase = parse(baselineSource);
		OperationalTransformation tQuery = parse(querySource);
		List<EObject> elements = createSourceElements(MODEL_SIZE);

		// Warmup both
		for (int w = 0; w < WARMUP; w++) {
			engine.execute(tBase, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(elements), emptyExtent()));
			engine.execute(tQuery, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(elements), emptyExtent()));
		}

		// Timed: baseline
		long startBase = System.nanoTime();
		for (int i = 0; i < EXEC_ITERATIONS; i++) {
			engine.execute(tBase, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(elements), emptyExtent()));
		}
		long elapsedBase = System.nanoTime() - startBase;

		// Timed: with query
		long startQuery = System.nanoTime();
		for (int i = 0; i < EXEC_ITERATIONS; i++) {
			engine.execute(tQuery, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(elements), emptyExtent()));
		}
		long elapsedQuery = System.nanoTime() - startQuery;

		long totalMappings = (long) EXEC_ITERATIONS * MODEL_SIZE;
		long nsBase = elapsedBase / totalMappings;
		long nsQuery = elapsedQuery / totalMappings;
		long queryOverhead = nsQuery - nsBase;

		System.out.printf("[%s] baseline (no query):  %,.0f ns/mapping (%,d total)%n",
				info.getDisplayName(), (double) nsBase, totalMappings);
		System.out.printf("[%s] with query dispatch:  %,.0f ns/mapping (%,d total)%n",
				info.getDisplayName(), (double) nsQuery, totalMappings);
		System.out.printf("[%s] query dispatch cost:  %,.0f ns/call (%.2fx overhead)%n",
				info.getDisplayName(), (double) queryOverhead, (double) nsQuery / nsBase);
	}

	// ==================== Q-P8: Trace Recording Overhead ====================

	@Test
	void qp8_traceRecordingOverhead(TestInfo info) throws Exception {
		String source = """
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				mapping SourceElement::toTarget() : TargetElement {
				    name := self.name;
				    value := self.value * 2;
				}
				main() { s.objectsOfType(SourceElement)->map toTarget(); }
				""";

		OperationalTransformation t = parse(source);
		List<EObject> elements = createSourceElements(MODEL_SIZE);

		QvtoEvaluationOptions optsNoTrace = QvtoEvaluationOptions.defaults().withTracing(false);
		QvtoEvaluationOptions optsTrace = QvtoEvaluationOptions.defaults().withTracing(true);

		// Warmup both paths
		for (int w = 0; w < WARMUP; w++) {
			engine.execute(t, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(elements), emptyExtent()), optsNoTrace);
			engine.execute(t, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(elements), emptyExtent()), optsTrace);
		}

		// Timed: without tracing
		long startNoTrace = System.nanoTime();
		for (int i = 0; i < EXEC_ITERATIONS; i++) {
			engine.execute(t, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(elements), emptyExtent()), optsNoTrace);
		}
		long elapsedNoTrace = System.nanoTime() - startNoTrace;

		// Timed: with tracing
		long startTrace = System.nanoTime();
		for (int i = 0; i < EXEC_ITERATIONS; i++) {
			engine.execute(t, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(elements), emptyExtent()), optsTrace);
		}
		long elapsedTrace = System.nanoTime() - startTrace;

		long totalMappings = (long) EXEC_ITERATIONS * MODEL_SIZE;
		long nsNoTrace = elapsedNoTrace / totalMappings;
		long nsTrace = elapsedTrace / totalMappings;
		double overhead = (double) nsTrace / nsNoTrace;

		System.out.printf("[%s] no-trace: %,.0f ns/mapping (%,d mappings, %.1f ms)%n",
				info.getDisplayName(), (double) nsNoTrace, totalMappings,
				elapsedNoTrace / 1_000_000.0);
		System.out.printf("[%s] tracing:  %,.0f ns/mapping (%,d mappings, %.1f ms)%n",
				info.getDisplayName(), (double) nsTrace, totalMappings,
				elapsedTrace / 1_000_000.0);
		System.out.printf("[%s] trace overhead: %.2fx%n",
				info.getDisplayName(), overhead);
	}

	// ==================== Helpers ====================

	private static List<EObject> createSourceElements(int count) {
		List<EObject> elements = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			elements.add(createSourceElement("elem_" + i, i));
		}
		return elements;
	}

	private static void printResult(TestInfo info, long iterations, long nsPerOp) {
		double usPerOp = nsPerOp / 1_000.0;
		double opsPerSec = nsPerOp > 0 ? 1_000_000_000.0 / nsPerOp : 0;
		System.out.printf("[%s] %,d iterations: %,.0f ns/op (%.1f µs/op, %,.0f ops/sec)%n",
				info.getDisplayName(), iterations, (double) nsPerOp, usPerOp, opsPerSec);
	}

	private static void printResult(TestInfo info, String label, long iterations, long nsPerOp) {
		double usPerOp = nsPerOp / 1_000.0;
		double opsPerSec = nsPerOp > 0 ? 1_000_000_000.0 / nsPerOp : 0;
		System.out.printf("[%s] %s: %,d iterations: %,.0f ns/op (%.1f µs/op, %,.0f ops/sec)%n",
				info.getDisplayName(), label, iterations, (double) nsPerOp, usPerOp, opsPerSec);
	}
}
