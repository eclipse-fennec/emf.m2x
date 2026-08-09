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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclModelExtent;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.engine.OclLruExpressionCache;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * Performance benchmarks for the Fennec OCL engine (P1–P7, P16–P17).
 *
 * <p>Measures parse throughput, evaluation throughput, collection pipeline
 * performance, large model navigation, memory footprint, and engine startup.
 *
 * <p>Results are printed to stdout for comparison. A future version will add
 * Eclipse OCL standalone baselines under identical conditions (DR-D24).
 *
 * <p>Run via: {@code ./gradlew org.eclipse.fennec.m2x.ocl.tests:perfTest}
 *
 * @see <a href="../../../../../docs/implementation-plan.md">§14.1.8 Performance Test Plan</a>
 * @see <a href="../../../../../docs/design-decisions.md">DR-D24 Performance Benchmark Design</a>
 */
@Tag("perf")
class OclPerformanceTest {

	private static final int WARMUP = 100;
	private static final int PARSE_ITERATIONS = 10_000;
	private static final int EVAL_ITERATIONS = 100_000;
	private static final int COLLECTION_ITERATIONS = 10_000;
	private static final int LARGE_MODEL_SIZE = 50_000;

	static OclEngine engine;
	static EcoreHelper ecoreHelper;
	static EPackage companyPackage;
	static EClass companyClass;
	static EClass personClass;
	static EStructuralFeature nameFeature;
	static EStructuralFeature ageFeature;
	static EStructuralFeature salaryFeature;
	static EStructuralFeature marriedFeature;
	static EStructuralFeature employeesFeature;
	static EStructuralFeature employerFeature;

	@BeforeAll
	static void setUp() throws IOException {
		engine = OclEngines.create(new OclParserSupport());
		ecoreHelper = new EcoreHelper(OclPerformanceTest.class);
		companyPackage = ecoreHelper.loadEcore("company.ecore");
		companyClass = ecoreHelper.getEClass(companyPackage, "Company");
		personClass = ecoreHelper.getEClass(companyPackage, "Person");
		nameFeature = personClass.getEStructuralFeature("name");
		ageFeature = personClass.getEStructuralFeature("age");
		salaryFeature = personClass.getEStructuralFeature("salary");
		marriedFeature = personClass.getEStructuralFeature("isMarried");
		employeesFeature = companyClass.getEStructuralFeature("employees");
		employerFeature = personClass.getEStructuralFeature("employer");
	}

	@AfterAll
	static void tearDown() {
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
	}

	// ==================== P1: Parse Throughput ====================

	@Test
	void p1_parseThroughput(TestInfo info) throws OclParseException {
		String expr = "self.employees->select(e | e.salary > 50000)->collect(e | e.name)->size()";

		// Warmup
		for (int i = 0; i < WARMUP; i++) {
			engine.parse(expr, companyClass);
		}

		long start = System.nanoTime();
		for (int i = 0; i < PARSE_ITERATIONS; i++) {
			engine.parse(expr, companyClass);
		}
		long elapsed = System.nanoTime() - start;

		long nsPerParse = elapsed / PARSE_ITERATIONS;
		printResult(info, PARSE_ITERATIONS, nsPerParse);
		assertTrue(nsPerParse > 0);
	}

	// ==================== P2: Simple Property Eval ====================

	@Test
	void p2_simplePropertyEval(TestInfo info) throws OclParseException {
		OclExpression parsed = engine.parse("self.name", personClass);
		EObject person = createPerson("Alice", 30, 60000.0, true);

		// Warmup
		for (int i = 0; i < WARMUP; i++) {
			engine.evaluate(parsed, OclContext.of(person));
		}

		long start = System.nanoTime();
		for (int i = 0; i < EVAL_ITERATIONS; i++) {
			engine.evaluate(parsed, OclContext.of(person));
		}
		long elapsed = System.nanoTime() - start;

		long nsPerEval = elapsed / EVAL_ITERATIONS;
		printResult(info, EVAL_ITERATIONS, nsPerEval);
		assertTrue(nsPerEval > 0);
	}

	// ==================== P3: Collection Pipeline ====================

	@Test
	void p3_collectionPipeline(TestInfo info) throws OclParseException {
		// Build a company with 100 employees
		EObject company = createCompanyWithEmployees(100);
		OclExpression parsed = engine.parse(
				"self.employees->select(e | e.salary > 50000)->collect(e | e.name)->size()",
				companyClass);

		// Warmup
		for (int i = 0; i < WARMUP; i++) {
			engine.evaluate(parsed, OclContext.of(company));
		}

		long start = System.nanoTime();
		for (int i = 0; i < COLLECTION_ITERATIONS; i++) {
			engine.evaluate(parsed, OclContext.of(company));
		}
		long elapsed = System.nanoTime() - start;

		long nsPerEval = elapsed / COLLECTION_ITERATIONS;
		printResult(info, COLLECTION_ITERATIONS, nsPerEval);
		assertTrue(nsPerEval > 0);
	}

	// ==================== P4: Nested Iterators ====================

	@Test
	void p4_nestedIterators(TestInfo info) throws OclParseException {
		EObject company = createCompanyWithEmployees(50);
		OclExpression parsed = engine.parse(
				"self.employees->forAll(e | e.salary > 0.0 and e.name.size() > 0)",
				companyClass);

		for (int i = 0; i < WARMUP; i++) {
			engine.evaluate(parsed, OclContext.of(company));
		}

		int iterations = 1_000;
		long start = System.nanoTime();
		for (int i = 0; i < iterations; i++) {
			engine.evaluate(parsed, OclContext.of(company));
		}
		long elapsed = System.nanoTime() - start;

		long nsPerEval = elapsed / iterations;
		printResult(info, iterations, nsPerEval);
		assertTrue(nsPerEval > 0);
	}

	// ==================== P5: Large Model allInstances ====================

	@Test
	void p5_largeModelAllInstances(TestInfo info) throws OclParseException {
		EObject company = createCompanyWithEmployees(LARGE_MODEL_SIZE);
		EObject[] employees = getEmployees(company);
		EObject firstEmployee = employees[0];

		// Create a model extent that returns all employees
		OclModelExtent extent = new OclModelExtent() {
			@Override
			public Collection<EObject> getAllInstances(EClass eClass) {
				List<EObject> instances = new ArrayList<>();
				if (personClass.isSuperTypeOf(eClass) || eClass.isSuperTypeOf(personClass)) {
					for (EObject emp : employees) {
						instances.add(emp);
					}
				}
				return instances;
			}
		};

		OclExpression parsed = engine.parse(
				"Person.allInstances()->select(p | p.salary > 50000)->size()",
				personClass);

		OclContext context = OclContext.of(firstEmployee, extent);

		// Warmup
		for (int i = 0; i < 10; i++) {
			engine.evaluate(parsed, context);
		}

		int iterations = 100;
		long start = System.nanoTime();
		for (int i = 0; i < iterations; i++) {
			engine.evaluate(parsed, context);
		}
		long elapsed = System.nanoTime() - start;

		long nsPerEval = elapsed / iterations;
		printResult(info, iterations, nsPerEval);
		assertTrue(nsPerEval > 0);
	}

	// ==================== P6: Deep Navigation ====================

	@Test
	void p6_deepNavigation(TestInfo info) throws OclParseException {
		// Build a 10-level deep containment: Company → employees → employer → ...
		// Since company.ecore has Person.employer→Company and Company.employees→Person,
		// we test a chain: self.employer.employees->first().employer.name
		EObject company = createCompanyWithEmployees(10);
		EObject employee = getEmployees(company)[0];

		OclExpression parsed = engine.parse(
				"self.employer.employees->at(1).employer.employees->at(1).name",
				personClass);

		for (int i = 0; i < WARMUP; i++) {
			engine.evaluate(parsed, OclContext.of(employee));
		}

		int iterations = 10_000;
		long start = System.nanoTime();
		for (int i = 0; i < iterations; i++) {
			engine.evaluate(parsed, OclContext.of(employee));
		}
		long elapsed = System.nanoTime() - start;

		long nsPerEval = elapsed / iterations;
		printResult(info, iterations, nsPerEval);
		assertTrue(nsPerEval > 0);
	}

	// ==================== P7: Closure on Graph ====================

	@Test
	void p7_closureTraversal(TestInfo info) throws OclParseException {
		// Use closure to traverse employees→employer chain
		EObject company = createCompanyWithEmployees(100);
		EObject employee = getEmployees(company)[0];

		// Closure: collect all "colleague" names via employer→employees
		OclExpression parsed = engine.parse(
				"self.employer.employees->closure(e | e.employer.employees)->asSet()->size()",
				personClass);

		for (int i = 0; i < WARMUP; i++) {
			engine.evaluate(parsed, OclContext.of(employee));
		}

		int iterations = 1_000;
		long start = System.nanoTime();
		for (int i = 0; i < iterations; i++) {
			engine.evaluate(parsed, OclContext.of(employee));
		}
		long elapsed = System.nanoTime() - start;

		long nsPerEval = elapsed / iterations;
		printResult(info, iterations, nsPerEval);
		assertTrue(nsPerEval > 0);
	}

	// ==================== P16: Memory Footprint ====================

	@Test
	void p16_memoryFootprint(TestInfo info) throws OclParseException {
		// Force GC, measure baseline, parse 1000 expressions, measure delta
		System.gc();
		try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

		long baselineUsed = usedHeap();

		OclExpression[] expressions = new OclExpression[1000];
		String[] sources = {
				"self.name", "self.age + 1", "self.salary * 2.0",
				"self.employees->size()", "self.name.toUpperCase()",
				"if self.age > 30 then 'senior' else 'junior' endif",
				"Sequence{1..100}->size()", "self.name.concat(' test')",
				"self.age.max(18)", "Set{1, 2, 3}->including(4)->size()"
		};

		for (int i = 0; i < expressions.length; i++) {
			expressions[i] = engine.parse(sources[i % sources.length], personClass);
		}

		System.gc();
		try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

		long afterUsed = usedHeap();
		long deltaKb = (afterUsed - baselineUsed) / 1024;

		System.out.printf("[%s] 1000 parsed ASTs: ~%,d KB heap delta%n",
				info.getDisplayName(), deltaKb);

		// Ensure expressions are not GC'd before measurement
		assertNotNull(expressions[0]);
	}

	// ==================== P17: Engine Startup Time ====================

	@Test
	void p17_engineStartupTime(TestInfo info) {
		// Warmup: create + discard a few engines
		for (int i = 0; i < 10; i++) {
			OclEngines.create(new OclParserSupport());
		}

		int iterations = 1_000;
		long start = System.nanoTime();
		for (int i = 0; i < iterations; i++) {
			OclEngine e = OclEngines.create(new OclParserSupport());
			assertNotNull(e);
		}
		long elapsed = System.nanoTime() - start;

		long nsPerStartup = elapsed / iterations;
		printResult(info, iterations, nsPerStartup);
		assertTrue(nsPerStartup > 0);
	}

	// ==================== P13-P15: Security Limit Stress ====================

	@Test
	void p13_rapidFireRangeLimit(TestInfo info) throws OclParseException {
		EObject person = createPerson("Test", 30, 50000.0, false);
		var opts = org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions.strict()
				.withMaxCollectionSize(100);
		OclExpression parsed = engine.parse("Sequence{1..200}", personClass);

		for (int i = 0; i < WARMUP; i++) {
			engine.evaluateWithDiagnostics(parsed, OclContext.of(person), opts);
		}

		int iterations = 10_000;
		long start = System.nanoTime();
		for (int i = 0; i < iterations; i++) {
			engine.evaluateWithDiagnostics(parsed, OclContext.of(person), opts);
		}
		long elapsed = System.nanoTime() - start;

		long nsPerEval = elapsed / iterations;
		printResult(info, iterations, nsPerEval);
		assertTrue(nsPerEval > 0);
	}

	@Test
	void p14_rapidFireClosureLimit(TestInfo info) throws OclParseException {
		EObject person = createPerson("Test", 30, 50000.0, false);
		var opts = org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions.strict()
				.withMaxClosureIterations(10);
		OclExpression parsed = engine.parse(
				"Sequence{1}->closure(i | i + 1)", personClass);

		for (int i = 0; i < WARMUP; i++) {
			engine.evaluateWithDiagnostics(parsed, OclContext.of(person), opts);
		}

		int iterations = 10_000;
		long start = System.nanoTime();
		for (int i = 0; i < iterations; i++) {
			engine.evaluateWithDiagnostics(parsed, OclContext.of(person), opts);
		}
		long elapsed = System.nanoTime() - start;

		long nsPerEval = elapsed / iterations;
		printResult(info, iterations, nsPerEval);
		assertTrue(nsPerEval > 0);
	}

	@Test
	void p15_rapidFireRegexLimit(TestInfo info) throws OclParseException {
		EObject person = createPerson("Test", 30, 50000.0, false);
		var opts = org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions.strict()
				.withMaxRegexLength(5);
		OclExpression parsed = engine.parse(
				"'hello'.matches('hello world!!')", personClass);

		for (int i = 0; i < WARMUP; i++) {
			engine.evaluateWithDiagnostics(parsed, OclContext.of(person), opts);
		}

		int iterations = 10_000;
		long start = System.nanoTime();
		for (int i = 0; i < iterations; i++) {
			engine.evaluateWithDiagnostics(parsed, OclContext.of(person), opts);
		}
		long elapsed = System.nanoTime() - start;

		long nsPerEval = elapsed / iterations;
		printResult(info, iterations, nsPerEval);
		assertTrue(nsPerEval > 0);
	}

	// ==================== P18: Cache Hit Throughput ====================

	@Test
	void p18_cacheHitThroughput(TestInfo info) throws OclParseException {
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(1024);
		OclEngine cachedEngine = OclEngines.create(new OclParserSupport(), cache);
		String expr = "self.employees->select(e | e.salary > 50000)->collect(e | e.name)->size()";

		// Prime the cache
		cachedEngine.parse(expr, companyClass);

		// Warmup
		for (int i = 0; i < WARMUP; i++) {
			cachedEngine.parse(expr, companyClass);
		}

		int iterations = 100_000;
		long start = System.nanoTime();
		for (int i = 0; i < iterations; i++) {
			cachedEngine.parse(expr, companyClass);
		}
		long elapsed = System.nanoTime() - start;
		long nsPerHit = elapsed / iterations;

		// Also measure uncached for comparison
		long startUncached = System.nanoTime();
		for (int i = 0; i < PARSE_ITERATIONS; i++) {
			engine.parse(expr, companyClass);
		}
		long elapsedUncached = System.nanoTime() - startUncached;
		long nsPerParse = elapsedUncached / PARSE_ITERATIONS;

		System.out.printf("[%s] cached: %,d iterations: %,.0f ns/op (%.1f µs/op, %,.0f ops/sec)%n",
				info.getDisplayName(), iterations, (double) nsPerHit,
				nsPerHit / 1_000.0, 1_000_000_000.0 / nsPerHit);
		System.out.printf("[%s] uncached: %,d iterations: %,.0f ns/op (%.1f µs/op, %,.0f ops/sec)%n",
				info.getDisplayName(), PARSE_ITERATIONS, (double) nsPerParse,
				nsPerParse / 1_000.0, 1_000_000_000.0 / nsPerParse);
		System.out.printf("[%s] speedup: %.1fx%n",
				info.getDisplayName(), (double) nsPerParse / nsPerHit);

		assertTrue(nsPerHit < nsPerParse, "Cache hit should be faster than parsing");
	}

	// ==================== P19: Property Accessor Cache ====================

	@Test
	void p19_propertyAccessorSpeedup(TestInfo info) throws OclParseException {
		OclExpression parsed = engine.parse("self.name", personClass);
		EObject person = createPerson("Alice", 30, 60000.0, true);

		// Warmup
		for (int i = 0; i < WARMUP; i++) {
			engine.evaluate(parsed, OclContext.of(person));
		}

		long start = System.nanoTime();
		for (int i = 0; i < EVAL_ITERATIONS; i++) {
			engine.evaluate(parsed, OclContext.of(person));
		}
		long elapsed = System.nanoTime() - start;
		long nsPerEval = elapsed / EVAL_ITERATIONS;

		printResult(info, EVAL_ITERATIONS, nsPerEval);
		assertTrue(nsPerEval > 0);
		// Compare with P2 baseline — accessor cache should show improvement
		System.out.printf("[%s] (compare with P2 for accessor speedup)%n",
				info.getDisplayName());
	}

	// ==================== P20: Warm-Up Time ====================

	@Test
	void p20_warmUpTime(TestInfo info) {
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(1024);
		OclEngine warmEngine = OclEngines.create(new OclParserSupport(), cache);

		// Warmup the JIT
		for (int i = 0; i < 10; i++) {
			cache.invalidateAll();
			warmEngine.warmUp(companyPackage);
		}

		int iterations = 1_000;
		long start = System.nanoTime();
		for (int i = 0; i < iterations; i++) {
			cache.invalidateAll();
			warmEngine.warmUp(companyPackage);
		}
		long elapsed = System.nanoTime() - start;

		long nsPerWarmup = elapsed / iterations;
		printResult(info, iterations, nsPerWarmup);
		assertTrue(nsPerWarmup > 0);
	}

	// ==================== Helpers ====================

	private static EObject createPerson(String name, int age, double salary, boolean married) {
		EObject person = companyPackage.getEFactoryInstance().create(personClass);
		person.eSet(nameFeature, name);
		person.eSet(ageFeature, age);
		person.eSet(salaryFeature, salary);
		person.eSet(marriedFeature, married);
		return person;
	}

	@SuppressWarnings("unchecked")
	private static EObject createCompanyWithEmployees(int count) {
		EObject company = companyPackage.getEFactoryInstance().create(companyClass);
		company.eSet(companyClass.getEStructuralFeature("name"), "TestCorp");
		List<EObject> empList = (List<EObject>) company.eGet(employeesFeature);
		for (int i = 0; i < count; i++) {
			EObject person = createPerson("Emp" + i, 20 + (i % 40),
					30000.0 + (i * 1000.0), i % 2 == 0);
			empList.add(person);
		}
		return company;
	}

	@SuppressWarnings("unchecked")
	private static EObject[] getEmployees(EObject company) {
		List<EObject> list = (List<EObject>) company.eGet(employeesFeature);
		return list.toArray(new EObject[0]);
	}

	private static long usedHeap() {
		Runtime rt = Runtime.getRuntime();
		return rt.totalMemory() - rt.freeMemory();
	}

	private static void printResult(TestInfo info, int iterations, long nsPerOp) {
		double usPerOp = nsPerOp / 1_000.0;
		double opsPerSec = 1_000_000_000.0 / nsPerOp;
		System.out.printf("[%s] %,d iterations: %,.0f ns/op (%.1f µs/op, %,.0f ops/sec)%n",
				info.getDisplayName(), iterations, (double) nsPerOp, usPerOp, opsPerSec);
		// TODO: Add Eclipse OCL baseline comparison (DR-D24)
		// Eclipse OCL standalone setup:
		//   org.eclipse.ocl.xtext.essentialocl.EssentialOCLStandaloneSetup.doSetup();
		//   OCL ocl = OCL.newInstance();
		//   ExpressionInOCL expr = ocl.createQuery(contextType, expressionText);
		//   Object result = ocl.evaluate(contextObject, expr);
	}
}
