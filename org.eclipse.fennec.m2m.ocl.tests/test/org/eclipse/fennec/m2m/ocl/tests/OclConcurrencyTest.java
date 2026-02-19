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
package org.eclipse.fennec.m2m.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.ocl.api.OclContext;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for thread safety of the OCL engine.
 * Verifies that concurrent evaluate() calls produce correct results
 * and do not corrupt shared state.
 */
class OclConcurrencyTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject carol;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 50000.0, true);
		bob = createPerson("Bob", 25, 40000.0, false);
		carol = createPerson("Carol", 35, 60000.0, true);
	}

	@Test
	void concurrent_sameExpression_differentContexts() throws Exception {
		int threadCount = 10;
		OclExpression expr = engine.parse("self.name", personClass);
		CountDownLatch latch = new CountDownLatch(1);
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);

		List<Future<Object>> futures = new ArrayList<>();
		for (int i = 0; i < threadCount; i++) {
			EObject self = (i % 3 == 0) ? alice : (i % 3 == 1) ? bob : carol;
			futures.add(executor.submit(() -> {
				latch.await();
				return engine.evaluate(expr, OclContext.of(self));
			}));
		}

		latch.countDown();
		for (int i = 0; i < threadCount; i++) {
			Object result = futures.get(i).get(5, TimeUnit.SECONDS);
			String expected = (i % 3 == 0) ? "Alice" : (i % 3 == 1) ? "Bob" : "Carol";
			assertEquals(expected, result);
		}

		executor.shutdown();
		assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
	}

	@Test
	void concurrent_differentExpressions() throws Exception {
		int threadCount = 9;
		OclExpression nameExpr = engine.parse("self.name", personClass);
		OclExpression ageExpr = engine.parse("self.age", personClass);
		OclExpression salaryExpr = engine.parse("self.salary", personClass);
		CountDownLatch latch = new CountDownLatch(1);
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);

		List<Future<Object>> futures = new ArrayList<>();
		for (int i = 0; i < threadCount; i++) {
			OclExpression expr = (i % 3 == 0) ? nameExpr : (i % 3 == 1) ? ageExpr : salaryExpr;
			futures.add(executor.submit(() -> {
				latch.await();
				return engine.evaluate(expr, OclContext.of(alice));
			}));
		}

		latch.countDown();
		for (int i = 0; i < threadCount; i++) {
			Object result = futures.get(i).get(5, TimeUnit.SECONDS);
			if (i % 3 == 0) {
				assertEquals("Alice", result);
			} else if (i % 3 == 1) {
				assertEquals(30, result);
			} else {
				assertEquals(50000.0, result);
			}
		}

		executor.shutdown();
		assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
	}

	@Test
	void concurrent_collectionOperations() throws Exception {
		int threadCount = 8;
		EObject company = createCompany("ACME", alice, bob, carol);
		OclExpression expr = engine.parse(
				"self.employees->select(e | e.age >= 30)->size()", companyClass);
		CountDownLatch latch = new CountDownLatch(1);
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);

		List<Future<Object>> futures = new ArrayList<>();
		for (int i = 0; i < threadCount; i++) {
			futures.add(executor.submit(() -> {
				latch.await();
				return engine.evaluate(expr, OclContext.of(company));
			}));
		}

		latch.countDown();
		for (Future<Object> f : futures) {
			assertEquals(2, f.get(5, TimeUnit.SECONDS));
		}

		executor.shutdown();
		assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
	}

	@Test
	void concurrent_stringParsing() throws Exception {
		int threadCount = 6;
		CountDownLatch latch = new CountDownLatch(1);
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);

		List<Future<Object>> futures = new ArrayList<>();
		for (int i = 0; i < threadCount; i++) {
			int idx = i;
			futures.add(executor.submit(() -> {
				latch.await();
				return engine.evaluate("self.name.size() + " + idx, OclContext.of(alice));
			}));
		}

		latch.countDown();
		for (int i = 0; i < threadCount; i++) {
			assertEquals(5 + i, futures.get(i).get(5, TimeUnit.SECONDS));
		}

		executor.shutdown();
		assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
	}

	@Test
	void concurrent_noSharedStateLeak() throws Exception {
		int threadCount = 10;
		CountDownLatch latch = new CountDownLatch(1);
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);

		// Each thread evaluates a let expression with a unique variable value
		List<Future<Object>> futures = new ArrayList<>();
		for (int i = 0; i < threadCount; i++) {
			int val = i * 10;
			futures.add(executor.submit(() -> {
				latch.await();
				return engine.evaluate("let x : Integer = " + val + " in x * 2",
						OclContext.of(alice));
			}));
		}

		latch.countDown();
		for (int i = 0; i < threadCount; i++) {
			assertEquals(i * 20, futures.get(i).get(5, TimeUnit.SECONDS));
		}

		executor.shutdown();
		assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
	}

	@Test
	void concurrent_errorRecovery() throws Exception {
		int threadCount = 4;
		CountDownLatch latch = new CountDownLatch(1);
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);

		List<Future<String>> futures = new ArrayList<>();
		for (int i = 0; i < threadCount; i++) {
			futures.add(executor.submit(() -> {
				latch.await();
				try {
					engine.evaluate("invalid syntax !!!", OclContext.of(alice));
					return "no-error";
				} catch (OclParseException e) {
					return "parse-error";
				}
			}));
		}

		latch.countDown();
		for (Future<String> f : futures) {
			assertEquals("parse-error", f.get(5, TimeUnit.SECONDS));
		}

		executor.shutdown();
		assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
	}
}
