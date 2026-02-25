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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclOperation;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.ocl.engine.OclEngineImpl;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Thread-safety tests for the OCL engine (P8–P12).
 *
 * <p>Verifies that concurrent evaluation, parsing, delegate invocation,
 * and provider mutation are safe under the engine's thread-safety design:
 * per-evaluation {@code OclEvaluator}, {@code CopyOnWriteArrayList} for
 * providers, {@code ConcurrentHashMap} for expression caches.
 *
 * @see <a href="../../../../../docs/architecture.md">§15 Thread-Safety Architecture</a>
 */
@Tag("perf")
class OclThreadSafetyTest {

	private static final int THREAD_COUNT = 16;
	private static final int ITERATIONS_PER_THREAD = 1000;

	static OclEngineImpl engine;
	static EcoreHelper ecoreHelper;
	static EPackage companyPackage;
	static EClass companyClass;
	static EClass personClass;
	static EPackage prestatePackage;
	static EClass accountClass;

	@BeforeAll
	static void setUp() throws IOException {
		engine = new OclEngineImpl(new OclParserSupport());
		ecoreHelper = new EcoreHelper(OclThreadSafetyTest.class);
		companyPackage = ecoreHelper.loadEcore("company.ecore");
		companyClass = ecoreHelper.getEClass(companyPackage, "Company");
		personClass = ecoreHelper.getEClass(companyPackage, "Person");
		prestatePackage = ecoreHelper.loadEcore("prestate-test.ecore");
		accountClass = ecoreHelper.getEClass(prestatePackage, "Account");
	}

	@AfterAll
	static void tearDown() {
		engine.uninstallDelegates();
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
	}

	// --- P8: Concurrent eval (same expression) ---

	@Test
	void p8_concurrentEval_sameExpression() throws Exception {
		OclExpression parsed = engine.parse("self.name.size() + self.age", personClass);
		List<Throwable> errors = new CopyOnWriteArrayList<>();
		CountDownLatch startLatch = new CountDownLatch(1);

		try (ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT)) {
			List<Future<?>> futures = new ArrayList<>();
			for (int t = 0; t < THREAD_COUNT; t++) {
				final int threadId = t;
				futures.add(pool.submit(() -> {
					try {
						startLatch.await();
						for (int i = 0; i < ITERATIONS_PER_THREAD; i++) {
							EObject person = createPerson("T" + threadId, 20 + threadId);
							Object result = engine.evaluate(parsed, OclContext.of(person));
							assertNotNull(result);
						}
					} catch (Throwable e) {
						errors.add(e);
					}
				}));
			}
			startLatch.countDown();
			for (Future<?> f : futures) {
				f.get(30, TimeUnit.SECONDS);
			}
		}

		if (!errors.isEmpty()) {
			fail("Concurrent eval failed with " + errors.size() + " errors: " + errors.get(0));
		}
	}

	// --- P9: Concurrent eval (different expressions) ---

	@Test
	void p9_concurrentEval_differentExpressions() throws Exception {
		String[] expressions = {
				"self.name", "self.age", "self.salary", "self.isMarried",
				"self.name.size()", "self.age + 1", "self.salary * 2.0",
				"self.name.toUpperCase()", "self.age > 18", "self.salary > 0.0",
				"if self.isMarried then 'yes' else 'no' endif",
				"Sequence{1, 2, 3}->size()", "self.name.concat(' test')",
				"let x = self.age in x * 2", "self.age.max(18)",
				"Set{self.name, 'other'}->size()"
		};

		OclExpression[] parsed = new OclExpression[expressions.length];
		for (int i = 0; i < expressions.length; i++) {
			parsed[i] = engine.parse(expressions[i], personClass);
		}

		List<Throwable> errors = new CopyOnWriteArrayList<>();
		CountDownLatch startLatch = new CountDownLatch(1);

		try (ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT)) {
			List<Future<?>> futures = new ArrayList<>();
			for (int t = 0; t < THREAD_COUNT; t++) {
				final int threadId = t;
				futures.add(pool.submit(() -> {
					try {
						startLatch.await();
						for (int i = 0; i < ITERATIONS_PER_THREAD; i++) {
							EObject person = createPerson("Thread" + threadId, 25 + threadId);
							// Each thread evaluates its "own" expression from the pool
							OclExpression expr = parsed[(threadId + i) % parsed.length];
							engine.evaluate(expr, OclContext.of(person));
							// Just verify no exception — result varies by expression
						}
					} catch (Throwable e) {
						errors.add(e);
					}
				}));
			}
			startLatch.countDown();
			for (Future<?> f : futures) {
				f.get(30, TimeUnit.SECONDS);
			}
		}

		if (!errors.isEmpty()) {
			fail("Concurrent eval (different) failed with " + errors.size()
					+ " errors: " + errors.get(0));
		}
	}

	// --- P10: Concurrent parse + eval ---

	@Test
	void p10_concurrentParseAndEval() throws Exception {
		String[] expressions = {
				"self.name.size()", "self.age + 10", "self.salary / 2.0",
				"self.name.toUpperCase()", "Sequence{1..5}->size()",
				"self.age * self.age", "self.name.substring(1, 2)",
				"if self.age > 30 then 'senior' else 'junior' endif"
		};

		List<Throwable> errors = new CopyOnWriteArrayList<>();
		CountDownLatch startLatch = new CountDownLatch(1);

		try (ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT)) {
			List<Future<?>> futures = new ArrayList<>();
			for (int t = 0; t < THREAD_COUNT; t++) {
				final int threadId = t;
				futures.add(pool.submit(() -> {
					try {
						startLatch.await();
						for (int i = 0; i < ITERATIONS_PER_THREAD / 10; i++) {
							String expr = expressions[(threadId + i) % expressions.length];
							// Parse AND evaluate in the same thread — tests parser thread safety
							OclExpression parsed = engine.parse(expr, personClass);
							EObject person = createPerson("P" + threadId, 20 + (i % 50));
							Object result = engine.evaluate(parsed, OclContext.of(person));
							assertNotNull(result);
						}
					} catch (Throwable e) {
						errors.add(e);
					}
				}));
			}
			startLatch.countDown();
			for (Future<?> f : futures) {
				f.get(60, TimeUnit.SECONDS);
			}
		}

		if (!errors.isEmpty()) {
			fail("Concurrent parse+eval failed with " + errors.size()
					+ " errors: " + errors.get(0));
		}
	}

	// --- P11: Concurrent delegates ---

	@Test
	void p11_concurrentDelegates() throws Exception {
		engine.installDelegates();

		EOperation computeBonus = accountClass.getEOperations().stream()
				.filter(op -> "computeBonus".equals(op.getName()))
				.findFirst().orElseThrow();

		EOperation nameLength = accountClass.getEOperations().stream()
				.filter(op -> "nameLength".equals(op.getName()))
				.findFirst().orElseThrow();

		List<Throwable> errors = new CopyOnWriteArrayList<>();
		CountDownLatch startLatch = new CountDownLatch(1);

		try (ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT)) {
			List<Future<?>> futures = new ArrayList<>();
			for (int t = 0; t < THREAD_COUNT; t++) {
				final int threadId = t;
				futures.add(pool.submit(() -> {
					try {
						startLatch.await();
						for (int i = 0; i < ITERATIONS_PER_THREAD / 10; i++) {
							EObject account = createAccount(
									"Acct" + threadId, 1000.0 + threadId * 100);
							// Alternate between two delegates
							if (i % 2 == 0) {
								Object bonus = ((InternalEObject) account)
										.eInvoke(computeBonus, null);
								assertNotNull(bonus);
							} else {
								Object len = ((InternalEObject) account)
										.eInvoke(nameLength, null);
								assertNotNull(len);
							}
						}
					} catch (Throwable e) {
						// Unwrap InvocationTargetException
						if (e instanceof InvocationTargetException ite) {
							errors.add(ite.getCause() != null ? ite.getCause() : ite);
						} else {
							errors.add(e);
						}
					}
				}));
			}
			startLatch.countDown();
			for (Future<?> f : futures) {
				f.get(60, TimeUnit.SECONDS);
			}
		}

		if (!errors.isEmpty()) {
			fail("Concurrent delegates failed with " + errors.size()
					+ " errors: " + errors.get(0));
		}
	}

	// --- P12: Concurrent register/unregister providers ---

	@Test
	void p12_concurrentProviderMutation() throws Exception {
		OclExpression parsed = engine.parse("self.name", personClass);

		AtomicInteger evalCount = new AtomicInteger(0);
		List<Throwable> errors = new CopyOnWriteArrayList<>();
		CountDownLatch startLatch = new CountDownLatch(1);

		try (ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT)) {
			List<Future<?>> futures = new ArrayList<>();

			// Half the threads evaluate
			for (int t = 0; t < THREAD_COUNT / 2; t++) {
				final int threadId = t;
				futures.add(pool.submit(() -> {
					try {
						startLatch.await();
						for (int i = 0; i < ITERATIONS_PER_THREAD; i++) {
							EObject person = createPerson("Eval" + threadId, 30);
							engine.evaluate(parsed, OclContext.of(person));
							evalCount.incrementAndGet();
						}
					} catch (Throwable e) {
						errors.add(e);
					}
				}));
			}

			// Other half registers/unregisters providers
			for (int t = THREAD_COUNT / 2; t < THREAD_COUNT; t++) {
				futures.add(pool.submit(() -> {
					try {
						startLatch.await();
						for (int i = 0; i < ITERATIONS_PER_THREAD / 10; i++) {
							OclOperationProvider provider = new NoOpProvider();
							engine.registerOperations(provider);
							Thread.yield(); // give evaluators a chance to see it
							engine.unregisterOperations(provider);
						}
					} catch (Throwable e) {
						errors.add(e);
					}
				}));
			}

			startLatch.countDown();
			for (Future<?> f : futures) {
				f.get(30, TimeUnit.SECONDS);
			}
		}

		assertTrue(evalCount.get() > 0, "At least some evaluations should have completed");

		if (!errors.isEmpty()) {
			fail("Concurrent provider mutation failed with " + errors.size()
					+ " errors: " + errors.get(0));
		}
	}

	// --- Helpers ---

	private static EObject createPerson(String name, int age) {
		EObject person = companyPackage.getEFactoryInstance().create(personClass);
		person.eSet(personClass.getEStructuralFeature("name"), name);
		person.eSet(personClass.getEStructuralFeature("age"), age);
		person.eSet(personClass.getEStructuralFeature("salary"), 50000.0);
		person.eSet(personClass.getEStructuralFeature("isMarried"), false);
		return person;
	}

	private static EObject createAccount(String name, double balance) {
		EObject account = prestatePackage.getEFactoryInstance().create(accountClass);
		account.eSet(accountClass.getEStructuralFeature("name"), name);
		account.eSet(accountClass.getEStructuralFeature("balance"), balance);
		return account;
	}

	/**
	 * A no-op provider for testing concurrent register/unregister.
	 */
	private static class NoOpProvider implements OclOperationProvider {
		@Override
		public List<OclOperation> getOperations() {
			return List.of();
		}
	}
}
