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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.emf.osgi.fingerprint.util.FingerprintHelper;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * What the compiled-unit path costs and saves for QVT-R: parse vs compile, a store round trip,
 * and repeated execution from a parsed transformation vs from a prepared context. Excluded from
 * the normal test run ({@code benchmark} tag); numbers are relative to the machine.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Tag("benchmark")
class QvtdCompiledUnitBenchmarkTest {

	private static final int JIT_WARMUP = 300;
	private static final int ITERATIONS = 200;
	private static final int MODEL_SIZE = 100;
	private static final String NS_URI = "http://example.org/m2x/qvtr-bench/1.0";
	private static final String TRANSFORMATION = """
			transformation copy(source : bookshelf, target : bookshelf) {
			    top relation CopyTitle {
			        t : String;
			        checkonly domain source b1 : Book { title = t };
			        enforce domain target b2 : Book { title = t };
			    }
			}
			""";

	@Test
	void compiledUnitPath() throws Exception {
		EPackage bookshelf = EcoreFactory.eINSTANCE.createEPackage();
		bookshelf.setName("bookshelf");
		bookshelf.setNsURI(NS_URI);
		bookshelf.setNsPrefix("bookshelf");
		EClass bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(title);
		bookshelf.getEClassifiers().add(bookClass);
		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(NS_URI, bookshelf);

		OclConfiguration ocl = OclConfiguration.builder(new OclParserSupport()).build();
		QvtdEngine engine = QvtdEngines.create(QvtdConfiguration.builder(ocl).packageRegistry(registry).build());
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		UnitPreparer preparer = new UnitPreparer(store, registry, FingerprintHelper.getDefaultFingerprintService(),
				List.of(engine.unitBinder()));
		List<EObject> books = books(bookClass, MODEL_SIZE);

		for (int i = 0; i < JIT_WARMUP; i++) {
			assertTrue(engine.execute(engine.parse(TRANSFORMATION, "copy"), context(books)).isSuccess());
			UnitKey key = store.put(engine.compile(TRANSFORMATION, "copy"));
			assertTrue(engine.execute(preparer.prepare(key), "copy", context(books)).isSuccess());
		}

		long parseStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			engine.parse(TRANSFORMATION, "copy");
		}
		long parseNanos = System.nanoTime() - parseStart;

		long compileStart = System.nanoTime();
		CompiledUnit compiled = null;
		for (int i = 0; i < ITERATIONS; i++) {
			compiled = engine.compile(TRANSFORMATION, "copy");
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
		PreparedContext prepared = preparer.prepare(key);
		long prepareNanos = System.nanoTime() - prepareStart;

		RelationalTransformation ast = engine.parse(TRANSFORMATION, "copy");
		long execStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			assertTrue(engine.execute(ast, context(books)).isSuccess());
		}
		long execNanos = System.nanoTime() - execStart;

		long execPreparedStart = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			assertTrue(engine.execute(prepared, "copy", context(books)).isSuccess());
		}
		long execPreparedNanos = System.nanoTime() - execPreparedStart;

		System.out.println();
		System.out.println("=== QVT-R COMPILED-UNIT PATH (" + MODEL_SIZE + " books, " + ITERATIONS + " iterations) ===");
		System.out.printf("parse        %10.2f ms   compile      %10.2f ms   (compile/parse %.2fx)%n",
				ms(parseNanos), ms(compileNanos), (double) compileNanos / parseNanos);
		System.out.printf("store+load   %10.2f ms   (%.3f ms per round trip, validation on)%n", ms(storeNanos), ms(storeNanos) / ITERATIONS);
		System.out.printf("prepare      %10.3f ms   (once)%n", ms(prepareNanos));
		System.out.printf("execute(ast) %10.2f ms   execute(prepared) %10.2f ms   (prepared/ast %.2fx)%n",
				ms(execNanos), ms(execPreparedNanos), (double) execPreparedNanos / execNanos);
	}

	private static QvtdExecutionContext context(List<EObject> books) {
		return QvtdExecutionContext.enforce("target",
				Map.of("source", QvtdModelExtent.of(books), "target", QvtdModelExtent.of()));
	}

	private static List<EObject> books(EClass bookClass, int count) {
		List<EObject> books = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			EObject book = EcoreUtil.create(bookClass);
			book.eSet(bookClass.getEStructuralFeature("title"), "Book " + i);
			books.add(book);
		}
		return books;
	}

	private static double ms(long nanos) {
		return nanos / 1_000_000.0;
	}
}
