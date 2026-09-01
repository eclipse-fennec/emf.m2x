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

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * What the compiled-unit path costs and saves for MOFM2T: parse vs compile, a store round trip,
 * and repeated generation from a parsed module vs from a prepared context. Excluded from the
 * normal test run ({@code benchmark} tag); numbers are relative to the machine.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Tag("benchmark")
class M2tCompiledUnitBenchmarkTest {

	private static final int JIT_WARMUP = 300;
	private static final int ITERATIONS = 200;
	private static final int OPERATIONS = 50;

	private static final String MODULE = """
			[module bench(_'http://www.eclipse.org/emf/2002/Ecore')/]
			[template public main(c : EClass)]
			[file (c.name.concat('.txt'), false)]
			class [c.name/] {
			[for (op : EOperation | c.eOperations)]
			  [op.name/]() -> [op.eParameters->size()/] params
			[/for]
			}
			[/file]
			[/template]
			""";

	@Test
	void compiledUnitPath() throws Exception {
		M2tEngine engine = M2tEngines.create(
				M2tConfiguration.builder(OclConfiguration.builder(new OclParserSupport()).build()).build());
		EClass input = input();

		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		for (int i = 0; i < JIT_WARMUP; i++) {
			assertTrue(engine.execute(engine.parse(MODULE, "bench"), M2tContext.of(input)).generatedFiles().containsKey("Book.txt"));
			UnitKey key = store.put(engine.compile(MODULE, "bench"));
			PreparedContext prepared = UnitPreparer.withDefaults(store, engine.unitBinder()).prepare(key);
			assertTrue(engine.execute(prepared, "bench", M2tContext.of(input)).generatedFiles().containsKey("Book.txt"));
		}

		long parseStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			engine.parse(MODULE, "bench");
		}
		long parseNanos = System.nanoTime() - parseStart;

		long compileStart = System.nanoTime();
		CompiledUnit compiled = null;
		for (int i = 0; i < ITERATIONS; i++) {
			compiled = engine.compile(MODULE, "bench");
		}
		long compileNanos = System.nanoTime() - compileStart;

		long storeStart = System.nanoTime();
		UnitKey key = null;
		for (int i = 0; i < ITERATIONS; i++) {
			key = store.put(compiled);
			store.get(key).orElseThrow();
		}
		long storeNanos = System.nanoTime() - storeStart;

		long prepareStart = System.nanoTime();
		PreparedContext prepared = UnitPreparer.withDefaults(store, engine.unitBinder()).prepare(key);
		long prepareNanos = System.nanoTime() - prepareStart;

		Module module = engine.parse(MODULE, "bench");
		long execStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			M2tResult result = engine.execute(module, M2tContext.of(input));
			assertTrue(result.generatedFiles().containsKey("Book.txt"));
		}
		long execNanos = System.nanoTime() - execStart;

		long execPreparedStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			M2tResult result = engine.execute(prepared, "bench", M2tContext.of(input));
			assertTrue(result.generatedFiles().containsKey("Book.txt"));
		}
		long execPreparedNanos = System.nanoTime() - execPreparedStart;

		System.out.println();
		System.out.println("=== M2T COMPILED-UNIT PATH (" + OPERATIONS + " operations, " + ITERATIONS + " iterations) ===");
		System.out.printf("parse        %10.2f ms   compile      %10.2f ms   (compile/parse %.2fx)%n",
				ms(parseNanos), ms(compileNanos), (double) compileNanos / parseNanos);
		System.out.printf("store+load   %10.2f ms   (%.3f ms per round trip, validation on)%n", ms(storeNanos), ms(storeNanos) / ITERATIONS);
		System.out.printf("prepare      %10.3f ms   (once)%n", ms(prepareNanos));
		System.out.printf("execute(module) %7.2f ms   execute(prepared) %10.2f ms   (prepared/module %.2fx)%n",
				ms(execNanos), ms(execPreparedNanos), (double) execPreparedNanos / execNanos);
	}

	private static EClass input() {
		EClass book = EcoreFactory.eINSTANCE.createEClass();
		book.setName("Book");
		for (int i = 0; i < OPERATIONS; i++) {
			EOperation op = EcoreFactory.eINSTANCE.createEOperation();
			op.setName("op" + i);
			book.getEOperations().add(op);
		}
		return book;
	}

	private static double ms(long nanos) {
		return nanos / 1_000_000.0;
	}
}
