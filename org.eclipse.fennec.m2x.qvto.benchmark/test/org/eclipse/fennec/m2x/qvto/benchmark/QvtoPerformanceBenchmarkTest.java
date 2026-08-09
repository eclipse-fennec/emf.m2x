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
package org.eclipse.fennec.m2x.qvto.benchmark;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.engine.OclLruExpressionCache;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.eclipse.m2m.qvt.oml.BasicModelExtent;
import org.eclipse.m2m.qvt.oml.ExecutionContextImpl;
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic;
import org.eclipse.m2m.qvt.oml.TransformationExecutor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Performance benchmarks comparing Fennec QVT-O and Eclipse QVT-O.
 *
 * <p>Three Fennec variants are measured:
 * <ol>
 *   <li><b>Plain</b> — no OCL expression cache, no warmUp</li>
 *   <li><b>Cache</b> — LRU expression cache (1024 entries)</li>
 *   <li><b>Cache+WarmUp</b> — LRU cache + PropertyAccessorCache warmUp for source/target packages</li>
 * </ol>
 *
 * <p>All tests are tagged with {@code "perf"}.
 * Run with: {@code ./gradlew org.eclipse.fennec.m2x.qvto.benchmark:perfTest}
 */
@Tag("perf")
class QvtoPerformanceBenchmarkTest {

	private static final int JIT_WARMUP = 500;
	private static final int ITERATIONS = 200;
	private static final int MODEL_SIZE = 100;

	// Three Fennec engine variants
	static QvtoEngine fennecPlain;
	static QvtoEngine fennecWithCache;
	static QvtoEngine fennecWithCacheAndWarmup;

	static EcoreHelper ecoreHelper;
	static EPackage sourcePackage;
	static EPackage targetPackage;
	static EClass sourceElementClass;

	// Eclipse QVT-O
	static Path tempQvtoFile;

	// Shared transformation source
	static final String TRANSFORM_SOURCE = """
			modeltype SRC uses 'http://test/source/1.0';
			modeltype TGT uses 'http://test/target/1.0';
			transformation SimpleMapping(in s : SRC, out t : TGT);
			main() {
				s.objectsOfType(SourceElement)->map toTarget();
			}
			mapping SourceElement::toTarget() : TargetElement {
				name := self.name;
				value := self.value * 2;
			}
			""";

	@BeforeAll
	static void setUp() throws IOException {
		// Load metamodels
		ecoreHelper = new EcoreHelper(QvtoPerformanceBenchmarkTest.class);
		sourcePackage = ecoreHelper.loadEcore("source.ecore");
		targetPackage = ecoreHelper.loadEcore("target.ecore");
		sourceElementClass = ecoreHelper.getEClass(sourcePackage, "SourceElement");

		// Register packages for Eclipse QVT-O
		EPackage.Registry.INSTANCE.put(sourcePackage.getNsURI(), sourcePackage);
		EPackage.Registry.INSTANCE.put(targetPackage.getNsURI(), targetPackage);

		// Variant 1: plain (no cache, no warmUp)
		OclConfiguration oclPlain = OclConfiguration.builder(new OclParserSupport()).build();
		fennecPlain = QvtoEngines.create(QvtoConfiguration.builder(oclPlain).build());

		// Variant 2: with LRU expression cache
		OclConfiguration oclCached = OclConfiguration.builder(new OclParserSupport())
				.expressionCache(OclLruExpressionCache.ofSize(1024))
				.build();
		fennecWithCache = QvtoEngines.create(QvtoConfiguration.builder(oclCached).build());

		// Variant 3: with LRU cache + warmUp
		OclConfiguration oclCachedWarm = OclConfiguration.builder(new OclParserSupport())
				.expressionCache(OclLruExpressionCache.ofSize(1024))
				.build();
		fennecWithCacheAndWarmup = QvtoEngines.create(QvtoConfiguration.builder(oclCachedWarm).build());
		fennecWithCacheAndWarmup.getOclEngine().warmUp(sourcePackage);
		fennecWithCacheAndWarmup.getOclEngine().warmUp(targetPackage);

		// Write .qvto file for Eclipse QVT-O (needs a URI)
		tempQvtoFile = Files.createTempFile("benchmark", ".qvto");
		Files.writeString(tempQvtoFile, TRANSFORM_SOURCE);
	}

	@AfterAll
	static void tearDown() throws IOException {
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
		if (tempQvtoFile != null) {
			Files.deleteIfExists(tempQvtoFile);
		}
		EPackage.Registry.INSTANCE.remove("http://test/source/1.0");
		EPackage.Registry.INSTANCE.remove("http://test/target/1.0");
	}

	// --- Test data ---

	static List<EObject> createSourceElements(int count) {
		List<EObject> elements = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			EObject e = EcoreUtil.create(sourceElementClass);
			e.eSet(sourceElementClass.getEStructuralFeature("name"), "elem_" + i);
			e.eSet(sourceElementClass.getEStructuralFeature("value"), i);
			elements.add(e);
		}
		return elements;
	}

	// --- JIT warmup across all engines ---

	private void jitWarmup(List<EObject> sourceElements) throws Exception {
		URI eclipseUri = URI.createFileURI(tempQvtoFile.toAbsolutePath().toString());

		for (int i = 0; i < JIT_WARMUP; i++) {
			// Fennec plain
			OperationalTransformation t1 = fennecPlain.parse(TRANSFORM_SOURCE, "bench.qvto");
			fennecPlain.execute(t1, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(sourceElements), new BasicQvtoModelExtent()));

			// Fennec with cache
			OperationalTransformation t2 = fennecWithCache.parse(TRANSFORM_SOURCE, "bench.qvto");
			fennecWithCache.execute(t2, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(sourceElements), new BasicQvtoModelExtent()));

			// Fennec with cache+warmup
			OperationalTransformation t3 = fennecWithCacheAndWarmup.parse(TRANSFORM_SOURCE, "bench.qvto");
			fennecWithCacheAndWarmup.execute(t3, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(sourceElements), new BasicQvtoModelExtent()));

			// Eclipse
			TransformationExecutor ex = new TransformationExecutor(eclipseUri);
			ex.execute(new ExecutionContextImpl(),
					new BasicModelExtent(sourceElements), new BasicModelExtent());
			ex.cleanup();
		}
	}

	// =====================================================================
	// Parse performance
	// =====================================================================

	@Test
	void parsePerformance() throws Exception {
		List<EObject> sourceElements = createSourceElements(MODEL_SIZE);
		jitWarmup(sourceElements);

		URI eclipseUri = URI.createFileURI(tempQvtoFile.toAbsolutePath().toString());

		// Measure Fennec plain
		long plainStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			fennecPlain.parse(TRANSFORM_SOURCE, "bench.qvto");
		}
		long plainNanos = System.nanoTime() - plainStart;

		// Measure Fennec with cache
		long cacheStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			fennecWithCache.parse(TRANSFORM_SOURCE, "bench.qvto");
		}
		long cacheNanos = System.nanoTime() - cacheStart;

		// Measure Fennec with cache+warmup
		long warmStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			fennecWithCacheAndWarmup.parse(TRANSFORM_SOURCE, "bench.qvto");
		}
		long warmNanos = System.nanoTime() - warmStart;

		// Measure Eclipse
		long eclipseStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			TransformationExecutor executor = new TransformationExecutor(eclipseUri);
			executor.loadTransformation();
			executor.cleanup();
		}
		long eclipseNanos = System.nanoTime() - eclipseStart;

		System.out.println();
		System.out.println("=== QVT-O PARSE PERFORMANCE (" + ITERATIONS + " iterations) ===");
		printRow("parse", plainNanos, cacheNanos, warmNanos, eclipseNanos);
	}

	// =====================================================================
	// Execution performance
	// =====================================================================

	@Test
	void executionPerformance() throws Exception {
		List<EObject> sourceElements = createSourceElements(MODEL_SIZE);
		jitWarmup(sourceElements);

		// Pre-parse all Fennec variants
		OperationalTransformation tPlain = fennecPlain.parse(TRANSFORM_SOURCE, "bench.qvto");
		OperationalTransformation tCache = fennecWithCache.parse(TRANSFORM_SOURCE, "bench.qvto");
		OperationalTransformation tWarm = fennecWithCacheAndWarmup.parse(TRANSFORM_SOURCE, "bench.qvto");
		assertNotNull(tPlain);

		// Pre-load Eclipse
		URI eclipseUri = URI.createFileURI(tempQvtoFile.toAbsolutePath().toString());
		TransformationExecutor eclipseExecutor = new TransformationExecutor(eclipseUri);
		Diagnostic loadDiag = eclipseExecutor.loadTransformation();
		assertTrue(loadDiag.getSeverity() <= Diagnostic.WARNING,
				"Eclipse QVT-O load failed: " + loadDiag.getMessage());

		// Measure Fennec plain
		long plainStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			QvtoModelExtent in = new BasicQvtoModelExtent(sourceElements);
			QvtoModelExtent out = new BasicQvtoModelExtent();
			QvtoExecutionResult result = fennecPlain.execute(tPlain, QvtoExecutionContext.of(in, out));
			assertTrue(result.isSuccess());
		}
		long plainNanos = System.nanoTime() - plainStart;

		// Measure Fennec with cache
		long cacheStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			QvtoModelExtent in = new BasicQvtoModelExtent(sourceElements);
			QvtoModelExtent out = new BasicQvtoModelExtent();
			QvtoExecutionResult result = fennecWithCache.execute(tCache, QvtoExecutionContext.of(in, out));
			assertTrue(result.isSuccess());
		}
		long cacheNanos = System.nanoTime() - cacheStart;

		// Measure Fennec with cache+warmup
		long warmStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			QvtoModelExtent in = new BasicQvtoModelExtent(sourceElements);
			QvtoModelExtent out = new BasicQvtoModelExtent();
			QvtoExecutionResult result = fennecWithCacheAndWarmup.execute(tWarm, QvtoExecutionContext.of(in, out));
			assertTrue(result.isSuccess());
		}
		long warmNanos = System.nanoTime() - warmStart;

		// Measure Eclipse
		long eclipseStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			BasicModelExtent in = new BasicModelExtent(sourceElements);
			BasicModelExtent out = new BasicModelExtent();
			ExecutionDiagnostic result = eclipseExecutor.execute(new ExecutionContextImpl(), in, out);
			assertEquals(Diagnostic.OK, result.getSeverity());
		}
		long eclipseNanos = System.nanoTime() - eclipseStart;

		System.out.println();
		System.out.println("=== QVT-O EXECUTION PERFORMANCE (" + MODEL_SIZE + " elements, " + ITERATIONS + " iterations) ===");
		printRow("execute", plainNanos, cacheNanos, warmNanos, eclipseNanos);

		long totalMappings = (long) ITERATIONS * MODEL_SIZE;
		System.out.printf("  Plain:          %,.0f ns/mapping%n", plainNanos / (double) totalMappings);
		System.out.printf("  Cache:          %,.0f ns/mapping%n", cacheNanos / (double) totalMappings);
		System.out.printf("  Cache+WarmUp:   %,.0f ns/mapping%n", warmNanos / (double) totalMappings);
		System.out.printf("  Eclipse:        %,.0f ns/mapping%n", eclipseNanos / (double) totalMappings);
	}

	// =====================================================================
	// Parse + Execute combined
	// =====================================================================

	@Test
	void parseAndExecutePerformance() throws Exception {
		List<EObject> sourceElements = createSourceElements(MODEL_SIZE);
		jitWarmup(sourceElements);

		URI eclipseUri = URI.createFileURI(tempQvtoFile.toAbsolutePath().toString());

		// Measure Fennec plain
		long plainStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			OperationalTransformation t = fennecPlain.parse(TRANSFORM_SOURCE, "bench.qvto");
			QvtoExecutionResult result = fennecPlain.execute(t, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(sourceElements), new BasicQvtoModelExtent()));
			assertTrue(result.isSuccess());
		}
		long plainNanos = System.nanoTime() - plainStart;

		// Measure Fennec with cache
		long cacheStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			OperationalTransformation t = fennecWithCache.parse(TRANSFORM_SOURCE, "bench.qvto");
			QvtoExecutionResult result = fennecWithCache.execute(t, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(sourceElements), new BasicQvtoModelExtent()));
			assertTrue(result.isSuccess());
		}
		long cacheNanos = System.nanoTime() - cacheStart;

		// Measure Fennec with cache+warmup
		long warmStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			OperationalTransformation t = fennecWithCacheAndWarmup.parse(TRANSFORM_SOURCE, "bench.qvto");
			QvtoExecutionResult result = fennecWithCacheAndWarmup.execute(t, QvtoExecutionContext.of(
					new BasicQvtoModelExtent(sourceElements), new BasicQvtoModelExtent()));
			assertTrue(result.isSuccess());
		}
		long warmNanos = System.nanoTime() - warmStart;

		// Measure Eclipse
		long eclipseStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			TransformationExecutor ex = new TransformationExecutor(eclipseUri);
			ExecutionDiagnostic result = ex.execute(new ExecutionContextImpl(),
					new BasicModelExtent(sourceElements), new BasicModelExtent());
			assertEquals(Diagnostic.OK, result.getSeverity());
			ex.cleanup();
		}
		long eclipseNanos = System.nanoTime() - eclipseStart;

		System.out.println();
		System.out.println("=== QVT-O PARSE + EXECUTE PERFORMANCE (" + MODEL_SIZE + " elements, " + ITERATIONS + " iterations) ===");
		printRow("parse+exec", plainNanos, cacheNanos, warmNanos, eclipseNanos);
	}

	// --- Output ---

	private static void printRow(String label, long plainNanos, long cacheNanos, long warmNanos, long eclipseNanos) {
		double plainMs = plainNanos / 1_000_000.0;
		double cacheMs = cacheNanos / 1_000_000.0;
		double warmMs = warmNanos / 1_000_000.0;
		double eclipseMs = eclipseNanos / 1_000_000.0;
		System.out.printf("%-12s  Plain: %10.2f ms   Cache: %10.2f ms   Cache+WU: %10.2f ms   Eclipse: %10.2f ms%n",
				label, plainMs, cacheMs, warmMs, eclipseMs);
		System.out.printf("%-12s  vs Eclipse:  Plain %.2fx   Cache %.2fx   Cache+WU %.2fx%n",
				"", plainMs / eclipseMs, cacheMs / eclipseMs, warmMs / eclipseMs);
	}
}
