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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.OclResult;
import org.junit.jupiter.api.Test;

/**
 * Security hardening tests for the OCL engine.
 *
 * <p>Tests configurable resource limits that protect against denial-of-service
 * attacks when evaluating untrusted OCL expressions.
 *
 * @see <a href="../../../../../docs/security-hardening.md">Security Hardening</a>
 */
class OclSecurityHardeningTest extends AbstractOclTest {

	/**
	 * Evaluates an OCL expression with custom options.
	 */
	private static Object evalWithOptions(String expression, EObject self,
			OclEvaluationOptions opts) throws OclParseException {
		OclExpression parsed = engine.parse(expression, self.eClass());
		OclResult result = engine.evaluateWithDiagnostics(parsed, OclContext.of(self), opts);
		return result.value();
	}

	// --- S-1: ReDoS Protection ---

	@Test
	void s1_matchesWithShortPattern_succeeds() throws OclParseException {
		EObject person = createPerson("hello", 30, 50000, false);
		OclEvaluationOptions opts = OclEvaluationOptions.strict().withMaxRegexLength(1000);
		Object result = evalWithOptions("'hello'.matches('h.*')", person, opts);
		assertEquals(true, result);
	}

	@Test
	void s1_matchesWithLongPattern_returnsInvalid() throws OclParseException {
		EObject person = createPerson("hello", 30, 50000, false);
		OclEvaluationOptions opts = OclEvaluationOptions.strict().withMaxRegexLength(10);
		// Pattern 'hello world!!' has length 14, exceeds limit of 10
		Object result = evalWithOptions("'hello'.matches('hello world!!')", person, opts);
		assertSame(OclInvalid.INSTANCE, result);
	}

	@Test
	void s1_replaceAllWithLongPattern_returnsInvalid() throws OclParseException {
		EObject person = createPerson("hello", 30, 50000, false);
		OclEvaluationOptions opts = OclEvaluationOptions.strict().withMaxRegexLength(5);
		// Pattern 'hello.' has length 6, exceeds limit of 5
		Object result = evalWithOptions("'hello world'.replaceAll('hello.', 'x')", person, opts);
		assertSame(OclInvalid.INSTANCE, result);
	}

	@Test
	void s1_replaceFirstWithLongPattern_returnsInvalid() throws OclParseException {
		EObject person = createPerson("hello", 30, 50000, false);
		OclEvaluationOptions opts = OclEvaluationOptions.strict().withMaxRegexLength(5);
		Object result = evalWithOptions("'hello world'.replaceFirst('hello.', 'x')", person, opts);
		assertSame(OclInvalid.INSTANCE, result);
	}

	// --- S-2: Collection Range Guard ---

	@Test
	void s2_smallRange_succeeds() throws OclParseException {
		EObject person = createPerson("test", 30, 50000, false);
		OclEvaluationOptions opts = OclEvaluationOptions.strict().withMaxCollectionSize(100);
		Object result = evalWithOptions("Sequence{1..10}->size()", person, opts);
		assertEquals(10L, result);
	}

	@Test
	void s2_rangeExceedingLimit_returnsInvalid() throws OclParseException {
		EObject person = createPerson("test", 30, 50000, false);
		OclEvaluationOptions opts = OclEvaluationOptions.strict().withMaxCollectionSize(100);
		Object result = evalWithOptions("Sequence{1..200}", person, opts);
		assertSame(OclInvalid.INSTANCE, result);
	}

	// --- S-3: Product Guard ---

	@Test
	void s3_smallProduct_succeeds() throws OclParseException {
		EObject person = createPerson("test", 30, 50000, false);
		OclEvaluationOptions opts = OclEvaluationOptions.strict().withMaxCollectionSize(100);
		Object result = evalWithOptions("Sequence{1..5}->product(Sequence{1..5})", person, opts);
		assertInstanceOf(Set.class, result);
		assertEquals(25, ((Collection<?>) result).size());
	}

	@Test
	void s3_largeProduct_returnsInvalid() throws OclParseException {
		EObject person = createPerson("test", 30, 50000, false);
		OclEvaluationOptions opts = OclEvaluationOptions.strict().withMaxCollectionSize(100);
		Object result = evalWithOptions(
				"Sequence{1..20}->product(Sequence{1..20})", person, opts);
		assertSame(OclInvalid.INSTANCE, result);
	}

	// --- S-4: Closure Iteration Limit ---

	@Test
	void s4_unboundedClosure_returnsInvalid() throws OclParseException {
		EObject person = createPerson("test", 30, 50000, false);
		OclEvaluationOptions opts = OclEvaluationOptions.strict()
				.withMaxClosureIterations(10);
		// This closure generates infinite non-repeating values
		Object result = evalWithOptions(
				"Sequence{1}->closure(i | i + 1)", person, opts);
		assertSame(OclInvalid.INSTANCE, result);
	}

	@Test
	void s4_cyclicClosure_terminatesNormally() throws OclParseException {
		EObject person = createPerson("test", 30, 50000, false);
		OclEvaluationOptions opts = OclEvaluationOptions.strict()
				.withMaxClosureIterations(100);
		// Cyclic closure: 1 -> 2 -> 3 -> 1 (cycle detected via visited set)
		Object result = evalWithOptions(
				"Sequence{1}->closure(i | if i < 3 then Sequence{i + 1} else Sequence{1} endif)",
				person, opts);
		// Should terminate naturally with {1, 2, 3}
		assertInstanceOf(Collection.class, result);
		assertEquals(3, ((Collection<?>) result).size());
	}

	// --- S-5: Safe Cast Helpers ---

	@Test
	void s5_typeMismatch_noClassCastException() throws OclParseException {
		// A type mismatch in a stdlib operation is OclInvalid, not a ClassCastException out of
		// evaluate(). This used to evaluate 1.div(0) — division by zero, which is a different
		// thing entirely and would pass with every safe cast removed (#173).
		EObject person = createPerson("test", 30, 50000, false);

		assertSame(OclInvalid.INSTANCE, eval("'abc'.abs()", person), "a String where a number goes");
		assertSame(OclInvalid.INSTANCE, eval("Sequence{'a','b'}->sum()", person),
				"summing what cannot be added");
		assertSame(OclInvalid.INSTANCE, eval("1.concat('x')", person), "a number where a String goes");
		assertSame(OclInvalid.INSTANCE, eval("Set{1}->at(1)", person), "an index into an unordered set");
	}

	@Test
	void s5_divisionByZero_isInvalid() throws OclParseException {
		// What the test above used to assert, kept because it is worth asserting
		EObject person = createPerson("test", 30, 50000, false);

		assertSame(OclInvalid.INSTANCE, eval("1.div(0)", person));
	}

	// --- S-9: Recursion Depth Limit ---

	@Test
	void s9_deeplyNestedLet_exceedingDepthLimit_returnsInvalid() throws OclParseException {
		EObject person = createPerson("test", 30, 50000, false);
		OclEvaluationOptions opts = OclEvaluationOptions.strict().withMaxDepth(5);
		// Build a deeply nested let: let a=1 in let b=2 in let c=3 in ...
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			sb.append("let v").append(i).append(" = ").append(i).append(" in ");
		}
		sb.append("1");
		Object result = evalWithOptions(sb.toString(), person, opts);
		assertSame(OclInvalid.INSTANCE, result);
	}

	@Test
	void s9_normalDepth_succeeds() throws OclParseException {
		EObject person = createPerson("test", 30, 50000, false);
		OclEvaluationOptions opts = OclEvaluationOptions.strict().withMaxDepth(50);
		Object result = evalWithOptions("let a = 1 in let b = 2 in a + b", person, opts);
		assertEquals(3L, result);
	}

	// --- S-12: allInstances Size Limit ---

	@Test
	@SuppressWarnings("unchecked")
	void s12_allInstances_exceedingLimit_returnsInvalid() throws OclParseException {
		// Create a company with many employees
		EObject[] employees = new EObject[10];
		for (int i = 0; i < 10; i++) {
			employees[i] = createPerson("person" + i, 20 + i, 30000 + i * 1000, false);
		}
		EObject company = createCompany("BigCorp", employees);
		EObject person = (EObject) ((java.util.List<EObject>) company.eGet(
				companyClass.getEStructuralFeature("employees"))).get(0);

		OclEvaluationOptions opts = OclEvaluationOptions.strict().withMaxCollectionSize(5);

		// allInstances requires a model extent — test that the limit is enforced
		// We test via the engine with a model extent that has more than 5 instances
		org.eclipse.fennec.m2x.ocl.api.OclModelExtent extent =
				new org.eclipse.fennec.m2x.ocl.api.OclModelExtent() {
					@Override
					public java.util.Collection<EObject> getAllInstances(
							org.eclipse.emf.ecore.EClass eClass) {
						java.util.List<EObject> instances = new java.util.ArrayList<>();
						for (EObject emp : employees) {
							if (eClass.isInstance(emp)) {
								instances.add(emp);
							}
						}
						return instances;
					}
				};

		OclExpression parsed = engine.parse("Person.allInstances()", personClass);
		OclContext context = OclContext.of(person, extent);
		OclResult result = engine.evaluateWithDiagnostics(parsed, context, opts);
		assertSame(OclInvalid.INSTANCE, result.value());
		assertTrue(result.diagnostics().size() > 0);
	}

	// --- S-13: Timeout Enforcement ---

	@Test
	void s13_timeout_exceedingDeadline_returnsInvalid() throws OclParseException {
		EObject person = createPerson("test", 30, 50000, false);
		// Nanosecond timeout — guaranteed to be expired by the time evaluation starts
		OclEvaluationOptions opts = OclEvaluationOptions.strict()
				.withTimeout(Duration.ofNanos(1));
		Object result = evalWithOptions("1 + 2", person, opts);
		assertSame(OclInvalid.INSTANCE, result);
	}

	@Test
	void s13_timeout_withinDeadline_succeeds() throws OclParseException {
		EObject person = createPerson("test", 30, 50000, false);
		// Generous timeout (5s) with simple expression
		OclEvaluationOptions opts = OclEvaluationOptions.strict()
				.withTimeout(Duration.ofSeconds(5));
		Object result = evalWithOptions("1 + 2", person, opts);
		assertEquals(3L, result);
	}

	// --- Options Default Values ---

	@Test
	void strictOptions_haveCorrectDefaults() {
		OclEvaluationOptions strict = OclEvaluationOptions.strict();
		assertEquals(1000, strict.maxDepth());
		assertEquals(1_000_000, strict.maxCollectionSize());
		assertEquals(100_000, strict.maxClosureIterations());
		assertEquals(1000, strict.maxRegexLength());
		assertEquals(OclEvaluationOptions.NullHandling.STRICT, strict.nullHandling());
		assertEquals(OclEvaluationOptions.ErrorRecovery.FAIL_FAST, strict.errorRecovery());
	}

	@Test
	void lenientOptions_haveCorrectDefaults() {
		OclEvaluationOptions lenient = OclEvaluationOptions.lenient();
		assertEquals(1000, lenient.maxDepth());
		assertEquals(1_000_000, lenient.maxCollectionSize());
		assertEquals(100_000, lenient.maxClosureIterations());
		assertEquals(1000, lenient.maxRegexLength());
		assertEquals(OclEvaluationOptions.NullHandling.LENIENT, lenient.nullHandling());
		assertEquals(OclEvaluationOptions.ErrorRecovery.COLLECT_ERRORS, lenient.errorRecovery());
	}

	@Test
	void withMethods_createNewInstancesWithUpdatedValues() {
		OclEvaluationOptions base = OclEvaluationOptions.strict();

		OclEvaluationOptions custom = base
				.withMaxDepth(500)
				.withMaxCollectionSize(50_000)
				.withMaxClosureIterations(5_000)
				.withMaxRegexLength(200);

		assertEquals(500, custom.maxDepth());
		assertEquals(50_000, custom.maxCollectionSize());
		assertEquals(5_000, custom.maxClosureIterations());
		assertEquals(200, custom.maxRegexLength());

		// Original unchanged
		assertEquals(1000, base.maxDepth());
		assertEquals(1_000_000, base.maxCollectionSize());
	}

	@Test
	void delegateOptions_defaultToStrict() {
		assertNotNull(engine.getDelegateOptions());
		assertEquals(OclEvaluationOptions.NullHandling.STRICT,
				engine.getDelegateOptions().nullHandling());
		assertEquals(OclEvaluationOptions.ErrorRecovery.FAIL_FAST,
				engine.getDelegateOptions().errorRecovery());
	}

	// --- D29: Extension Security Controls ---

	@Test
	void d29_customOpsEnabled_defaultsFalseInConfig() {
		var config = org.eclipse.fennec.m2x.ocl.api.OclConfiguration.builder(
				new org.eclipse.fennec.m2x.ocl.parser.OclParserSupport()).build();
		assertEquals(false, config.customOperationsEnabled());
	}

	@Test
	void d29_customOpsEnabled_defaultsFalseInOptions() {
		assertEquals(false, OclEvaluationOptions.strict().customOperationsEnabled());
		assertEquals(false, OclEvaluationOptions.lenient().customOperationsEnabled());
		assertEquals(List.of(), OclEvaluationOptions.strict().additionalProviders());
		assertEquals(List.of(), OclEvaluationOptions.lenient().additionalProviders());
	}

	@Test
	void d29_configProvider_requiresBothFlags() throws OclParseException {
		org.eclipse.fennec.m2x.model.ocl.AnyType anyType = org.eclipse.fennec.m2x.model.ocl.OclFactory.eINSTANCE.createAnyType();
		anyType.setName("OclAny");
		org.eclipse.fennec.m2x.model.ocl.PrimitiveType intType = org.eclipse.fennec.m2x.model.ocl.OclFactory.eINSTANCE.createPrimitiveType();
		intType.setName("Integer");

		org.eclipse.fennec.m2x.ocl.api.OclOperation op = org.eclipse.fennec.m2x.ocl.api.OclOperation.of(
				"d29test", anyType, intType, (source, args) -> 999L);
		org.eclipse.fennec.m2x.ocl.api.OclOperationProvider provider = () -> List.of(op);

		// Config enabled, options NOT enabled → provider not active
		var config1 = org.eclipse.fennec.m2x.ocl.api.OclConfiguration.builder(
				new org.eclipse.fennec.m2x.ocl.parser.OclParserSupport())
				.addOperationProvider(provider)
				.customOperationsEnabled(true)
				.build();
		var eng1 = org.eclipse.fennec.m2x.ocl.engine.OclEngines.create(config1);
		var providers1 = eng1.getOperationProviders(OclEvaluationOptions.strict());
		assertEquals(0, providers1.size(), "Options flag false → no config providers");

		// Config NOT enabled, options enabled → provider not active
		var config2 = org.eclipse.fennec.m2x.ocl.api.OclConfiguration.builder(
				new org.eclipse.fennec.m2x.ocl.parser.OclParserSupport())
				.addOperationProvider(provider)
				.build(); // customOperationsEnabled defaults to false
		var eng2 = org.eclipse.fennec.m2x.ocl.engine.OclEngines.create(config2);
		var providers2 = eng2.getOperationProviders(
				OclEvaluationOptions.strict().withCustomOperationsEnabled(true));
		assertEquals(0, providers2.size(), "Config flag false → no config providers");

		// Both enabled → provider active
		var providers3 = eng1.getOperationProviders(
				OclEvaluationOptions.strict().withCustomOperationsEnabled(true));
		assertEquals(1, providers3.size(), "Both flags true → config provider active");
	}
}
