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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclOperation;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.OclResult;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;
import org.eclipse.fennec.m2x.ocl.engine.OclDelegateFactories;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * The limits that are enforced, held to it (#173, part of #188).
 *
 * <p>Each of these guards a mitigation the security analysis names, and each was chosen
 * because deleting the production check keeps every other test in the suite green. A limit
 * that nothing tests is a limit that comes back out in the next refactoring.
 *
 * <p>Two of them are about <em>when</em> a limit applies rather than whether: a range and a
 * regular expression have to be refused before the work is done, not after — a check that runs
 * afterwards reports the right thing while the allocation has already happened.
 */
class OclLimitEnforcementTest extends AbstractOclTest {

	private static Object evalWith(String expression, EObject self, OclEvaluationOptions options)
			throws OclParseException {
		OclExpression parsed = engine.parse(expression, self.eClass());
		OclResult result = engine.evaluateWithDiagnostics(parsed, OclContext.of(self), options);
		return result.value();
	}

	// ==== collection size ====

	@Test
	@DisplayName("a collection literal past maxCollectionSize is invalid")
	void literalExceedingTheLimitIsInvalid() throws OclParseException {
		EObject person = createPerson("Alice", 30, 1000.0, false);
		OclEvaluationOptions options = OclEvaluationOptions.strict().withMaxCollectionSize(3);

		assertSame(OclInvalid.INSTANCE, evalWith("Set{1, 2, 3, 4}", person, options));
		assertEquals(3, ((java.util.Collection<?>) evalWith("Set{1, 2, 3}", person, options)).size(),
				"a literal within the limit is untouched");
	}

	@Test
	@Timeout(value = 10, unit = TimeUnit.SECONDS)
	@DisplayName("a huge range is refused before it is allocated")
	void hugeRangeIsRefusedBeforeAllocation() throws OclParseException {
		// The size check after the loop reports the same thing — two billion boxed Longs later.
		// This test finishes only because the bounds are checked first.
		EObject person = createPerson("Alice", 30, 1000.0, false);
		OclEvaluationOptions options = OclEvaluationOptions.strict().withMaxCollectionSize(100);

		assertSame(OclInvalid.INSTANCE,
				evalWith("Sequence{1..2000000000}", person, options));
	}

	// ==== regular expressions ====

	@Test
	@DisplayName("QVT-O's match is bounded by maxRegexLength, as matches is")
	void matchWithALongPatternIsInvalid() throws OclParseException {
		// match is the QVT-O spelling (§8.3.16.15) and reaches the same regex engine, so it
		// needs the same bound — ReDoS does not care which operation name led to it
		EObject person = createPerson("hello", 30, 1000.0, false);
		OclEvaluationOptions options = OclEvaluationOptions.strict()
				.withMaxRegexLength(10)
				.withCustomOperationsEnabled(true);

		assertSame(OclInvalid.INSTANCE,
				evalWith("'hello'.match('hello world!!')", person, options));
		assertEquals(true, evalWith("'hello'.match('h.*')", person, options),
				"a pattern within the limit still works");
	}

	// ==== timeout ====

	@Test
	@Timeout(value = 10, unit = TimeUnit.SECONDS)
	@DisplayName("the timeout stops a runaway closure")
	void timeoutStopsARunawayClosure() throws OclParseException {
		// A closure that never exhausts its work list, with the iteration limit deliberately
		// out of reach: only the timeout can end this, and it has to end inside the loop.
		// Reaching the assertions at all is most of what this test says — without a check in
		// the loop the expression runs until the iteration limit and the @Timeout fires.
		EObject person = createPerson("Alice", 30, 1000.0, false);
		OclEvaluationOptions options = OclEvaluationOptions.strict()
				.withMaxClosureIterations(Integer.MAX_VALUE)
				.withTimeout(Duration.ofMillis(100));
		OclExpression parsed = engine.parse("Sequence{1}->closure(i | i + 1)", person.eClass());

		OclResult result = engine.evaluateWithDiagnostics(parsed, OclContext.of(person), options);

		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().toLowerCase().contains("timeout")),
				() -> "the timeout has to be reported: " + result.diagnostics());
		// Which of the two checks trips first depends on the machine — the one between the
		// iterations of the closure, or the one at the root of the nested evaluation of the
		// body, which ends the work list and used to yield what had been collected until then
		// as an ordinary value (#191). Either way there is no result.
		assertSame(OclInvalid.INSTANCE, result.value(),
				"an evaluation that ran out of time has no result, not a partial one");
	}

	// ==== the parser ====

	@Test
	@Timeout(value = 30, unit = TimeUnit.SECONDS)
	@DisplayName("an expression nested past what the stack can take is a parse exception")
	void deeplyNestedExpressionIsAParseException() {
		// ANTLR and the AST builder both recurse, and the evaluator's maxDepth applies only
		// after parsing. Since #181 every failure of parse — a StackOverflowError included —
		// comes out as an OclParseException, which is what makes this survivable (#173).
		String deep = "(".repeat(50_000) + "1" + ")".repeat(50_000);

		assertThrows(OclParseException.class, () -> engine.parse(deep, null));
	}

	@Test
	@DisplayName("an input past maxInputLength is refused before the parser sees it")
	void inputPastTheLengthLimitIsRefused() {
		OclParserSupport parser = new OclParserSupport().maxInputLength(100);

		OclParseException failure = assertThrows(OclParseException.class,
				() -> parser.parse("1 + ".repeat(100) + "1", null));

		assertTrue(failure.getMessage().contains("longer than 100 characters"), failure::getMessage);
	}

	// ==== the D29 gate, and what it deliberately does not cover ====

	@Test
	@DisplayName("additional providers are caller trust and are not gated")
	void additionalProvidersAreNotGated() {
		// The enable flag governs providers registered in the *configuration* — those come from
		// wherever the engine was configured. A provider handed in with the options for one
		// evaluation comes from the caller making that call, who is already trusted with the
		// expression itself. Written down because "not gated" reads like an oversight (#173).
		EObject person = createPerson("Alice", 30, 1000.0, false);
		OclEvaluationOptions options = OclEvaluationOptions.strict()
				.withCustomOperationsEnabled(false)
				.withAdditionalProviders(List.of(() -> List.of(new OclOperation(
						"shout", OclStandardLibrary.INSTANCE.string(), List.of(),
						OclStandardLibrary.INSTANCE.string(),
						(self, args) -> String.valueOf(self).toUpperCase()))));

		assertEquals("HELLO", assertDoesNotThrow(
				() -> evalWith("'hello'.shout()", person, options)),
				"the flag is off, and the caller's own provider still runs");
	}

	// ==== the options themselves ====

	@Test
	@DisplayName("a limit that is not positive is refused when the options are built")
	void nonPositiveLimitsAreRefused() {
		// Fail closed: a zero or negative limit would read as "no limit" everywhere it is used
		assertThrows(IllegalArgumentException.class, () -> OclEvaluationOptions.strict().withMaxDepth(0));
		assertThrows(IllegalArgumentException.class,
				() -> OclEvaluationOptions.strict().withMaxCollectionSize(-1));
		assertThrows(IllegalArgumentException.class,
				() -> OclEvaluationOptions.strict().withMaxClosureIterations(0));
		assertThrows(IllegalArgumentException.class,
				() -> OclEvaluationOptions.strict().withMaxRegexLength(-5));
	}

	// ==== EMF delegates ====

	@Test
	@DisplayName("a delegate refuses an engine it did not come from")
	void delegatesRefuseAForeignEngine() {
		// Delegates reach engine state the OclEngine interface does not expose, so they need
		// this implementation. Anything else would fail later, deeper, and less clearly.
		// A caller's own implementation of the interface — the fat interface (#186) makes a
		// hand-written stub eighteen methods long, and none of them matters here
		OclEngine foreign = (OclEngine) Proxy.newProxyInstance(
				OclLimitEnforcementTest.class.getClassLoader(), new Class<?>[] { OclEngine.class },
				(proxy, method, args) -> {
					throw new UnsupportedOperationException(method.getName());
				});

		IllegalArgumentException failure = assertThrows(IllegalArgumentException.class,
				() -> OclDelegateFactories.newValidationDelegate(foreign));

		assertTrue(failure.getMessage().contains("created by OclEngines"), failure::getMessage);
		assertThrows(IllegalArgumentException.class,
				() -> OclDelegateFactories.newSettingDelegateFactory(foreign));
		assertThrows(IllegalArgumentException.class,
				() -> OclDelegateFactories.newInvocationDelegateFactory(foreign));
	}

	@Test
	@DisplayName("an annotation under a URI this engine does not serve is not evaluated")
	void annotationUnderAnUnservedUriIsIgnored() {
		// The delegate answers for two URIs and no others. A model carrying an OCL body under
		// somebody else's delegate URI must not have it evaluated by this engine — that URI
		// belongs to another implementation, with other semantics.
		EClass type = EcoreFactory.eINSTANCE.createEClass();
		type.setName("Thing");
		EAnnotation foreign = EcoreFactory.eINSTANCE.createEAnnotation();
		foreign.setSource("http://example.org/not/our/delegate");
		foreign.getDetails().put("alwaysTrue", "true");
		type.getEAnnotations().add(foreign);

		EValidator.ValidationDelegate delegate =
				OclDelegateFactories.newValidationDelegate(engine);

		IllegalStateException failure = assertThrows(IllegalStateException.class,
				() -> delegate.validate(type, EcoreFactory.eINSTANCE.createEObject(), null,
						"alwaysTrue", null),
				"there is no expression under a served URI, so there is nothing to evaluate");
		assertTrue(failure.getMessage().toLowerCase().contains("no ocl"), failure::getMessage);
	}
}
