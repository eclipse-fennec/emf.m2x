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
package org.eclipse.fennec.m2x.ocl.benchmark;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.engine.OclEngineImpl;
import org.eclipse.fennec.m2x.ocl.engine.OclLruExpressionCache;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.expressions.OCLExpression;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Performance benchmarks comparing Fennec OCL and Eclipse OCL Classic.
 *
 * <p>Three Fennec engine variants are measured against Eclipse OCL Classic:
 * <ol>
 *   <li><b>No cache</b> — plain {@code OclEngineImpl(parser)}, every call re-parses</li>
 *   <li><b>LRU cache</b> — {@code OclEngineImpl(parser, cache)}, repeated expressions hit cache</li>
 *   <li><b>LRU cache + warmUp</b> — additionally calls {@code warmUp(EPackage)} to prime accessor caches</li>
 * </ol>
 *
 * <p>All tests are tagged with {@code "perf"} and excluded from normal test runs.
 * Run with: {@code ./gradlew org.eclipse.fennec.m2x.ocl.benchmark:perfTest}
 */
@Tag("perf")
class OclPerformanceBenchmarkTest extends AbstractComparisonTest {

	private static final int JIT_WARMUP_ITERATIONS = 2_000;
	private static final int WARMUP_ITERATIONS = 500;
	private static final int MEASURE_ITERATIONS = 1_000;

	/** Fennec without cache (inherited from AbstractComparisonTest). */
	// fennecEngine is already set up in super class — no cache, no warmUp

	/** Fennec with LRU expression cache. */
	static OclEngineImpl fennecWithCache;

	/** Fennec with LRU expression cache + warmUp. */
	static OclEngineImpl fennecWithCacheAndWarmup;

	static EObject alice;
	static EObject acme;

	@BeforeAll
	static void setUpData() throws IOException {
		alice = createPerson("Alice", 30, 50000.0, true);
		EObject bob = createPerson("Bob", 25, 35000.0, false);
		EObject carol = createPerson("Carol", 45, 80000.0, true);
		acme = createCompany("ACME", alice, bob, carol);

		// Variant 2: with LRU cache
		OclParserSupport parser2 = new OclParserSupport();
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(1024);
		fennecWithCache = new OclEngineImpl(parser2, cache);

		// Variant 3: with LRU cache + warmUp
		OclParserSupport parser3 = new OclParserSupport();
		OclLruExpressionCache cache3 = OclLruExpressionCache.ofSize(1024);
		fennecWithCacheAndWarmup = new OclEngineImpl(parser3, cache3);
		fennecWithCacheAndWarmup.warmUp(companyPackage);

		// JIT warmup: exercise all engines and Eclipse thoroughly so the JVM
		// compiles all hot code paths before any measurement begins.
		// Without this, test execution order affects results.
		jitWarmup();
	}

	/**
	 * Runs all engines through representative expressions to trigger JIT compilation.
	 * This ensures stable results regardless of test execution order.
	 */
	private static void jitWarmup() {
		String[] warmupExprs = {
			"self.name",
			"self.employees->select(e | e.age > 25)->collect(e | e.name)",
			"let x : Integer = self.employees->size() in x * 2"
		};

		for (String expr : warmupExprs) {
			OclContext ctx = OclContext.of(acme);
			for (int i = 0; i < JIT_WARMUP_ITERATIONS; i++) {
				try {
					fennecEngine.evaluate(expr, ctx);
					fennecWithCache.evaluate(expr, ctx);
					fennecWithCacheAndWarmup.evaluate(expr, ctx);
				} catch (Exception e) {
					// ignore during warmup
				}
			}
			for (int i = 0; i < JIT_WARMUP_ITERATIONS; i++) {
				try {
					eclipseHelper.setContext(acme.eClass());
					OCLExpression<EClassifier> q = eclipseHelper.createQuery(expr);
					eclipseOcl.evaluate(acme, q);
				} catch (Exception e) {
					// ignore during warmup
				}
			}
		}
		// Clear caches after JIT warmup so benchmarks start clean
		fennecWithCache.getExpressionCache().invalidateAll();
		fennecWithCacheAndWarmup.getExpressionCache().invalidateAll();
	}

	// =====================================================================
	// Parse performance
	// =====================================================================

	@Test
	void parsePerformance() throws Exception {
		Map<String, String> expressions = standardExpressions();

		System.out.println();
		System.out.println("=== PARSE PERFORMANCE ===");
		System.out.println("(Fennec no-cache re-parses every time; with-cache hits after first parse)");
		printHeader4();

		for (var entry : expressions.entrySet()) {
			benchmarkParse(entry.getKey(), entry.getValue(), acme);
		}
	}

	// =====================================================================
	// Eval performance (pre-parsed)
	// =====================================================================

	@Test
	void evalPerformance() throws Exception {
		Map<String, String> expressions = standardExpressions();

		System.out.println();
		System.out.println("=== EVAL PERFORMANCE (pre-parsed) ===");
		System.out.println("(Cache irrelevant here — expression already parsed; warmUp primes accessor cache)");
		printHeaderEval();

		for (var entry : expressions.entrySet()) {
			benchmarkEval(entry.getKey(), entry.getValue(), acme);
		}
	}

	// =====================================================================
	// Parse + Eval combined
	// =====================================================================

	@Test
	void parseAndEvalPerformance() throws Exception {
		Map<String, String> expressions = standardExpressions();

		System.out.println();
		System.out.println("=== PARSE + EVAL PERFORMANCE ===");
		System.out.println("(Realistic workload: parse and evaluate in one call)");
		printHeader4();

		for (var entry : expressions.entrySet()) {
			benchmarkParseAndEval(entry.getKey(), entry.getValue(), acme);
		}
	}

	// =====================================================================
	// Standard expressions
	// =====================================================================

	private static Map<String, String> standardExpressions() {
		Map<String, String> expressions = new LinkedHashMap<>();
		expressions.put("simple", "self.name");
		expressions.put("medium", "self.employees->select(e | e.age > 25)->collect(e | e.name)");
		expressions.put("complex",
				"let total : Real = self.employees->collect(e | e.salary)"
						+ "->iterate(s : Real; acc : Real = 0.0 | acc + s) in "
						+ "if total > 100000.0 then 'high' else 'low' endif");
		return expressions;
	}

	// =====================================================================
	// Benchmark methods
	// =====================================================================

	private void benchmarkParse(String label, String expression, EObject self) throws Exception {
		// --- Warmup all engines ---
		for (int i = 0; i < WARMUP_ITERATIONS; i++) {
			fennecEngine.parse(expression, self.eClass());
		}
		// Clear cache so measurement starts fresh
		fennecWithCache.getExpressionCache().invalidateAll();
		for (int i = 0; i < WARMUP_ITERATIONS; i++) {
			fennecWithCache.parse(expression, self.eClass());
		}
		fennecWithCacheAndWarmup.getExpressionCache().invalidateAll();
		for (int i = 0; i < WARMUP_ITERATIONS; i++) {
			fennecWithCacheAndWarmup.parse(expression, self.eClass());
		}
		for (int i = 0; i < WARMUP_ITERATIONS; i++) {
			eclipseHelper.setContext(self.eClass());
			eclipseHelper.createQuery(expression);
		}

		// --- Measure: Fennec no cache ---
		long fennecNoCacheNanos = measureParseFennec(fennecEngine, expression, self);

		// --- Measure: Fennec with cache (first call fills, rest hit) ---
		fennecWithCache.getExpressionCache().invalidateAll();
		// Prime the cache with one parse
		fennecWithCache.parse(expression, self.eClass());
		long fennecCacheNanos = measureParseFennec(fennecWithCache, expression, self);

		// --- Measure: Fennec with cache + warmUp ---
		fennecWithCacheAndWarmup.getExpressionCache().invalidateAll();
		fennecWithCacheAndWarmup.parse(expression, self.eClass());
		long fennecWarmupNanos = measureParseFennec(fennecWithCacheAndWarmup, expression, self);

		// --- Measure: Eclipse ---
		long eclipseNanos = measureParseEclipse(expression, self);

		printRow4(label, fennecNoCacheNanos, fennecCacheNanos, fennecWarmupNanos, eclipseNanos);
	}

	private void benchmarkEval(String label, String expression, EObject self)
			throws OclParseException, ParserException {
		// Pre-parse on all engines
		OclExpression fennecParsed = fennecEngine.parse(expression, self.eClass());
		assertNotNull(fennecParsed);

		OclExpression fennecWarmupParsed = fennecWithCacheAndWarmup.parse(expression, self.eClass());
		assertNotNull(fennecWarmupParsed);

		eclipseHelper.setContext(self.eClass());
		OCLExpression<EClassifier> eclipseParsed = eclipseHelper.createQuery(expression);
		assertNotNull(eclipseParsed);

		// Warmup
		for (int i = 0; i < WARMUP_ITERATIONS; i++) {
			fennecEngine.evaluate(fennecParsed, OclContext.of(self));
		}
		for (int i = 0; i < WARMUP_ITERATIONS; i++) {
			fennecWithCacheAndWarmup.evaluate(fennecWarmupParsed, OclContext.of(self));
		}
		for (int i = 0; i < WARMUP_ITERATIONS; i++) {
			eclipseOcl.evaluate(self, eclipseParsed);
		}

		// Measure: Fennec no cache (eval only, no parse cache involved)
		long fennecStart = System.nanoTime();
		for (int i = 0; i < MEASURE_ITERATIONS; i++) {
			fennecEngine.evaluate(fennecParsed, OclContext.of(self));
		}
		long fennecNanos = System.nanoTime() - fennecStart;

		// Measure: Fennec with warmUp (accessor caches primed)
		long warmupStart = System.nanoTime();
		for (int i = 0; i < MEASURE_ITERATIONS; i++) {
			fennecWithCacheAndWarmup.evaluate(fennecWarmupParsed, OclContext.of(self));
		}
		long warmupNanos = System.nanoTime() - warmupStart;

		// Measure: Eclipse
		long eclipseStart = System.nanoTime();
		for (int i = 0; i < MEASURE_ITERATIONS; i++) {
			eclipseOcl.evaluate(self, eclipseParsed);
		}
		long eclipseNanos = System.nanoTime() - eclipseStart;

		printRowEval(label, fennecNanos, warmupNanos, eclipseNanos);
	}

	private void benchmarkParseAndEval(String label, String expression, EObject self) throws Exception {
		// --- Warmup all engines ---
		for (int i = 0; i < WARMUP_ITERATIONS; i++) {
			fennecEngine.evaluate(expression, OclContext.of(self));
		}
		fennecWithCache.getExpressionCache().invalidateAll();
		for (int i = 0; i < WARMUP_ITERATIONS; i++) {
			fennecWithCache.evaluate(expression, OclContext.of(self));
		}
		fennecWithCacheAndWarmup.getExpressionCache().invalidateAll();
		for (int i = 0; i < WARMUP_ITERATIONS; i++) {
			fennecWithCacheAndWarmup.evaluate(expression, OclContext.of(self));
		}
		for (int i = 0; i < WARMUP_ITERATIONS; i++) {
			eclipseHelper.setContext(self.eClass());
			OCLExpression<EClassifier> q = eclipseHelper.createQuery(expression);
			eclipseOcl.evaluate(self, q);
		}

		// --- Measure: Fennec no cache ---
		long fennecNoCacheStart = System.nanoTime();
		for (int i = 0; i < MEASURE_ITERATIONS; i++) {
			fennecEngine.evaluate(expression, OclContext.of(self));
		}
		long fennecNoCacheNanos = System.nanoTime() - fennecNoCacheStart;

		// --- Measure: Fennec with cache (already primed from warmup) ---
		// Cache is already filled from warmup — this measures cache-hit scenario
		long fennecCacheStart = System.nanoTime();
		for (int i = 0; i < MEASURE_ITERATIONS; i++) {
			fennecWithCache.evaluate(expression, OclContext.of(self));
		}
		long fennecCacheNanos = System.nanoTime() - fennecCacheStart;

		// --- Measure: Fennec with cache + warmUp ---
		long fennecWarmupStart = System.nanoTime();
		for (int i = 0; i < MEASURE_ITERATIONS; i++) {
			fennecWithCacheAndWarmup.evaluate(expression, OclContext.of(self));
		}
		long fennecWarmupNanos = System.nanoTime() - fennecWarmupStart;

		// --- Measure: Eclipse ---
		long eclipseStart = System.nanoTime();
		for (int i = 0; i < MEASURE_ITERATIONS; i++) {
			eclipseHelper.setContext(self.eClass());
			OCLExpression<EClassifier> q = eclipseHelper.createQuery(expression);
			eclipseOcl.evaluate(self, q);
		}
		long eclipseNanos = System.nanoTime() - eclipseStart;

		printRow4(label, fennecNoCacheNanos, fennecCacheNanos, fennecWarmupNanos, eclipseNanos);
	}

	// =====================================================================
	// Helper: measure a single engine's parse loop
	// =====================================================================

	private long measureParseFennec(OclEngineImpl engine, String expression, EObject self)
			throws OclParseException {
		long start = System.nanoTime();
		for (int i = 0; i < MEASURE_ITERATIONS; i++) {
			engine.parse(expression, self.eClass());
		}
		return System.nanoTime() - start;
	}

	private long measureParseEclipse(String expression, EObject self) throws ParserException {
		long start = System.nanoTime();
		for (int i = 0; i < MEASURE_ITERATIONS; i++) {
			eclipseHelper.setContext(self.eClass());
			eclipseHelper.createQuery(expression);
		}
		return System.nanoTime() - start;
	}

	// =====================================================================
	// Output formatting — 4-column (parse / parse+eval)
	// =====================================================================

	private static void printHeader4() {
		System.out.printf("%-12s %15s %15s %15s %15s%n",
				"Expression", "Fennec plain", "Fennec+cache", "Fennec+warmUp", "Eclipse");
		System.out.println("-".repeat(75));
	}

	private static void printRow4(String label,
			long noCacheNanos, long cacheNanos, long warmupNanos, long eclipseNanos) {
		double noCache = noCacheNanos / 1_000_000.0;
		double cache = cacheNanos / 1_000_000.0;
		double warmup = warmupNanos / 1_000_000.0;
		double eclipse = eclipseNanos / 1_000_000.0;
		System.out.printf("%-12s %12.2f ms %12.2f ms %12.2f ms %12.2f ms%n",
				label, noCache, cache, warmup, eclipse);
	}

	// =====================================================================
	// Output formatting — 3-column (eval only)
	// =====================================================================

	private static void printHeaderEval() {
		System.out.printf("%-12s %15s %15s %15s%n",
				"Expression", "Fennec plain", "Fennec+warmUp", "Eclipse");
		System.out.println("-".repeat(60));
	}

	private static void printRowEval(String label,
			long plainNanos, long warmupNanos, long eclipseNanos) {
		double plain = plainNanos / 1_000_000.0;
		double warmup = warmupNanos / 1_000_000.0;
		double eclipse = eclipseNanos / 1_000_000.0;
		System.out.printf("%-12s %12.2f ms %12.2f ms %12.2f ms%n",
				label, plain, warmup, eclipse);
	}
}
