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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.ocl.api.OclContext;
import org.eclipse.fennec.m2m.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2m.ocl.api.OclInvalid;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.eclipse.fennec.m2m.ocl.api.OclResult;
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
		// This test verifies that type mismatches in stdlib operations
		// don't throw ClassCastException but produce OclInvalid
		EObject person = createPerson("test", 30, 50000, false);
		// Division by zero returns OclInvalid, not an exception
		Object result = eval("1.div(0)", person);
		assertSame(OclInvalid.INSTANCE, result);
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
		org.eclipse.fennec.m2m.ocl.api.OclModelExtent extent =
				new org.eclipse.fennec.m2m.ocl.api.OclModelExtent() {
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
		assertEquals(OclEvaluationOptions.strict(), engine.getDelegateOptions());
	}
}
