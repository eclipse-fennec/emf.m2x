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
package org.eclipse.fennec.m2x.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.engine.OclLruExpressionCache;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OclLruExpressionCache} and cache integration in {@link OclEngine}.
 */
class OclExpressionCacheTest {

	static OclParserSupport parser;
	static EcoreHelper ecoreHelper;
	static EPackage companyPackage;
	static EClass companyClass;
	static EClass personClass;

	@BeforeAll
	static void setUp() throws IOException {
		parser = new OclParserSupport();
		ecoreHelper = new EcoreHelper(OclExpressionCacheTest.class);
		companyPackage = ecoreHelper.loadEcore("company.ecore");
		companyClass = ecoreHelper.getEClass(companyPackage, "Company");
		personClass = ecoreHelper.getEClass(companyPackage, "Person");
	}

	@AfterAll
	static void tearDown() {
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
	}

	@Test
	void cache_hit_returnsCachedExpression() throws OclParseException {
		OclExpressionCache cache = OclLruExpressionCache.ofSize(16);
		OclEngine engine = OclEngines.create(parser, cache);

		OclExpression first = engine.parse("self.name", personClass);
		OclExpression second = engine.parse("self.name", personClass);

		assertSame(first, second, "Second parse should return cached instance");
	}

	@Test
	void cache_miss_parsesAndStores() throws OclParseException {
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(16);
		OclEngine engine = OclEngines.create(parser, cache);

		assertEquals(0, cache.hitCount());
		assertEquals(0, cache.missCount());

		engine.parse("self.name", personClass);
		assertEquals(0, cache.hitCount(), "First access should be a miss");
		assertEquals(1, cache.missCount());

		engine.parse("self.name", personClass);
		assertEquals(1, cache.hitCount(), "Second access should be a hit");
		assertEquals(1, cache.missCount());
	}

	@Test
	void cache_differentContext_separateEntries() throws OclParseException {
		OclExpressionCache cache = OclLruExpressionCache.ofSize(16);
		OclEngine engine = OclEngines.create(parser, cache);

		OclExpression forPerson = engine.parse("self.name", personClass);
		OclExpression forCompany = engine.parse("self.name", companyClass);

		assertNotNull(forPerson);
		assertNotNull(forCompany);
		assertEquals(2, cache.size(), "Different context types should be separate entries");
	}

	@Test
	void cache_lruEviction_removesOldest() throws OclParseException {
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(2);
		OclEngine engine = OclEngines.create(parser, cache);

		engine.parse("self.name", personClass);       // entry 1
		engine.parse("self.age", personClass);         // entry 2
		assertEquals(2, cache.size());

		engine.parse("self.salary", personClass);      // entry 3 → evicts entry 1
		assertEquals(2, cache.size());

		// "self.name" should have been evicted
		assertNull(cache.get("self.name", personClass));
		// "self.age" should still be there
		assertNotNull(cache.get("self.age", personClass));
	}

	@Test
	void cache_sharedAcrossEngines() throws OclParseException {
		OclExpressionCache cache = OclLruExpressionCache.ofSize(16);
		OclEngine engine1 = OclEngines.create(parser, cache);
		OclEngine engine2 = OclEngines.create(parser, cache);

		OclExpression fromEngine1 = engine1.parse("self.name", personClass);
		OclExpression fromEngine2 = engine2.parse("self.name", personClass);

		assertSame(fromEngine1, fromEngine2, "Shared cache should return same instance");
	}

	@Test
	void cache_hitMissCounters() throws OclParseException {
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(16);
		OclEngine engine = OclEngines.create(parser, cache);

		engine.parse("self.name", personClass);   // miss
		engine.parse("self.age", personClass);    // miss
		engine.parse("self.name", personClass);   // hit
		engine.parse("self.name", personClass);   // hit
		engine.parse("self.salary", personClass); // miss

		assertEquals(2, cache.hitCount());
		assertEquals(3, cache.missCount());
	}

	@Test
	void cache_invalidate_removesEntry() throws OclParseException {
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(16);
		OclEngine engine = OclEngines.create(parser, cache);

		engine.parse("self.name", personClass);
		assertEquals(1, cache.size());

		cache.invalidate("self.name", personClass);
		assertEquals(0, cache.size());

		// Next access should be a miss again
		long missesBefore = cache.missCount();
		engine.parse("self.name", personClass);
		assertEquals(missesBefore + 1, cache.missCount());
	}

	@Test
	void cache_invalidateAll_clearsAll() throws OclParseException {
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(16);
		OclEngine engine = OclEngines.create(parser, cache);

		engine.parse("self.name", personClass);
		engine.parse("self.age", personClass);
		engine.parse("self.salary", personClass);
		assertEquals(3, cache.size());

		cache.invalidateAll();
		assertEquals(0, cache.size());
	}

	@Test
	void cache_null_engineWorksWithoutCache() throws OclParseException {
		OclEngine engine = OclEngines.create(parser);

		assertNull(engine.getExpressionCache());

		// Parsing and evaluation should work normally
		EObject person = companyPackage.getEFactoryInstance().create(personClass);
		person.eSet(personClass.getEStructuralFeature("name"), "Alice");
		person.eSet(personClass.getEStructuralFeature("age"), 30);
		person.eSet(personClass.getEStructuralFeature("salary"), 60000.0);
		person.eSet(personClass.getEStructuralFeature("isMarried"), false);

		Object result = engine.evaluate("self.name", OclContext.of(person));
		assertEquals("Alice", result);
	}

	@Test
	void cache_threadSafety_concurrentAccess() throws Exception {
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(256);
		int threadCount = 16;
		int engineCount = 4;
		int opsPerThread = 100;

		OclEngine[] engines = new OclEngine[engineCount];
		for (int i = 0; i < engineCount; i++) {
			engines[i] = OclEngines.create(parser, cache);
		}

		String[] expressions = {
			"self.name", "self.age", "self.salary", "self.isMarried",
			"self.name.size()", "self.age + 1", "self.salary * 2.0",
			"self.name.toUpperCase()"
		};

		CyclicBarrier barrier = new CyclicBarrier(threadCount);
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		List<Future<?>> futures = new ArrayList<>();

		for (int t = 0; t < threadCount; t++) {
			final int threadIndex = t;
			futures.add(executor.submit(() -> {
				try {
					barrier.await();
					OclEngine engine = engines[threadIndex % engineCount];
					for (int i = 0; i < opsPerThread; i++) {
						String expr = expressions[i % expressions.length];
						engine.parse(expr, personClass);
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				return null;
			}));
		}

		for (Future<?> f : futures) {
			f.get();
		}
		executor.shutdown();

		// Verify cache is consistent
		long totalAccesses = cache.hitCount() + cache.missCount();
		assertEquals(threadCount * opsPerThread, totalAccesses,
				"Total accesses should equal total operations");
		assertNotNull(cache.get("self.name", personClass),
				"Frequently accessed expression should be in cache");
	}
}
