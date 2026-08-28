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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * What the compiled-unit path costs and what it saves, measured against {@code parse()} and
 * {@code execute(transformation)} on the same transformation and model as
 * {@link QvtoPerformanceBenchmarkTest}.
 *
 * <p>Four questions: what does {@code compile()} cost over {@code parse()} (satellites,
 * package entries, fingerprint); what does a store round trip cost; what does a repeated
 * execution from a prepared context save over a repeated {@code execute(ast)} — the latter
 * re-runs the link phase on every call, the former ran it once in prepare; and how do the two
 * ends compare over a whole run of N executions, parse once versus load + prepare once.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Tag("benchmark")
class QvtoCompiledUnitBenchmarkTest {

	private static final int JIT_WARMUP = 500;
	private static final int ITERATIONS = 200;
	private static final int MODEL_SIZE = 100;

	static QvtoEngine engine;
	static EcoreHelper ecoreHelper;
	static EPackage sourcePackage;
	static EPackage targetPackage;
	static EClass sourceElementClass;

	static final String TRANSFORM_SOURCE = QvtoPerformanceBenchmarkTest.TRANSFORM_SOURCE;

	@BeforeAll
	static void setUp() throws IOException {
		ecoreHelper = new EcoreHelper(QvtoCompiledUnitBenchmarkTest.class);
		sourcePackage = ecoreHelper.loadEcore("source.ecore");
		targetPackage = ecoreHelper.loadEcore("target.ecore");
		sourceElementClass = ecoreHelper.getEClass(sourcePackage, "SourceElement");
		EPackage.Registry.INSTANCE.put(sourcePackage.getNsURI(), sourcePackage);
		EPackage.Registry.INSTANCE.put(targetPackage.getNsURI(), targetPackage);
		OclConfiguration ocl = OclConfiguration.builder(new OclParserSupport()).build();
		engine = QvtoEngines.create(QvtoConfiguration.builder(ocl).build());
	}

	@AfterAll
	static void tearDown() {
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
		EPackage.Registry.INSTANCE.remove("http://test/source/1.0");
		EPackage.Registry.INSTANCE.remove("http://test/target/1.0");
	}

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

	private static QvtoExecutionContext context(List<EObject> source) {
		return QvtoExecutionContext.of(new BasicQvtoModelExtent(source), new BasicQvtoModelExtent());
	}

	private void jitWarmup(List<EObject> source) throws Exception {
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		for (int i = 0; i < JIT_WARMUP; i++) {
			OperationalTransformation t = engine.parse(TRANSFORM_SOURCE, "bench.qvto");
			assertTrue(engine.execute(t, context(source)).isSuccess());
			CompiledUnit c = engine.compile(TRANSFORM_SOURCE, "bench");
			UnitKey key = store.store("qvto", new PackagedUnit(c));
			PreparedContext prepared = UnitPreparer.withDefaults(store, engine.unitBinder()).prepare(key);
			assertTrue(engine.execute(prepared, "bench", context(source)).isSuccess());
		}
	}

	@Test
	void compiledUnitPath() throws Exception {
		List<EObject> source = createSourceElements(MODEL_SIZE);
		jitWarmup(source);

		// 1. parse vs compile
		long parseStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			engine.parse(TRANSFORM_SOURCE, "bench.qvto");
		}
		long parseNanos = System.nanoTime() - parseStart;

		long compileStart = System.nanoTime();
		CompiledUnit compiled = null;
		for (int i = 0; i < ITERATIONS; i++) {
			compiled = engine.compile(TRANSFORM_SOURCE, "bench");
		}
		long compileNanos = System.nanoTime() - compileStart;

		// 2. store round trip (store + load, validation on)
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		long storeStart = System.nanoTime();
		UnitKey key = null;
		for (int i = 0; i < ITERATIONS; i++) {
			key = store.store("qvto", new PackagedUnit(compiled));
			store.load(key).orElseThrow();
		}
		long storeNanos = System.nanoTime() - storeStart;

		// 3. prepare once
		long prepareStart = System.nanoTime();
		PreparedContext prepared = UnitPreparer.withDefaults(store, engine.unitBinder()).prepare(key);
		long prepareNanos = System.nanoTime() - prepareStart;

		// 4. repeated execution: execute(ast) links on every call, execute(prepared) does not
		OperationalTransformation ast = engine.parse(TRANSFORM_SOURCE, "bench.qvto");
		long execAstStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			QvtoExecutionResult result = engine.execute(ast, context(source));
			assertTrue(result.isSuccess());
		}
		long execAstNanos = System.nanoTime() - execAstStart;

		long execPreparedStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			QvtoExecutionResult result = engine.execute(prepared, "bench", context(source));
			assertTrue(result.isSuccess());
		}
		long execPreparedNanos = System.nanoTime() - execPreparedStart;

		System.out.println();
		System.out.println("=== QVT-O COMPILED-UNIT PATH (" + MODEL_SIZE + " elements, " + ITERATIONS + " iterations) ===");
		System.out.printf("parse        %10.2f ms   compile      %10.2f ms   (compile/parse %.2fx)%n",
				ms(parseNanos), ms(compileNanos), (double) compileNanos / parseNanos);
		System.out.printf("store+load   %10.2f ms   (%.3f ms per round trip, validation on)%n",
				ms(storeNanos), ms(storeNanos) / ITERATIONS);
		System.out.printf("prepare      %10.3f ms   (once, one unit, no dependencies)%n", ms(prepareNanos));
		System.out.printf("execute(ast) %10.2f ms   execute(prepared) %10.2f ms   (prepared/ast %.2fx)%n",
				ms(execAstNanos), ms(execPreparedNanos), (double) execPreparedNanos / execAstNanos);
		long totalMappings = (long) ITERATIONS * MODEL_SIZE;
		System.out.printf("             %,.0f ns/mapping (ast)   %,.0f ns/mapping (prepared)%n",
				execAstNanos / (double) totalMappings, execPreparedNanos / (double) totalMappings);
		System.out.printf("whole run    parse once + %d exec: %10.2f ms   load+prepare once + %d exec: %10.2f ms%n",
				ITERATIONS, ms(parseNanos / ITERATIONS + execAstNanos),
				ITERATIONS, ms(storeNanos / (2L * ITERATIONS) + prepareNanos + execPreparedNanos));
	}

	private static double ms(long nanos) {
		return nanos / 1_000_000.0;
	}
}
