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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.OclResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A {@code null} argument is invalid, and the evaluation says so (#270).
 *
 * <p>{@code 'a' + null} and {@code 1 + null} have no reading (OCL v2.4 §11.2.3); the value was
 * always {@code invalid}, but it was the one path on which an expression became invalid without
 * a diagnostic. A generator then wrote the word {@code OclInvalid} into its document while the
 * run counted as successful. The null-safe operations ({@code =}, {@code <>},
 * {@code oclIsUndefined()}, …) and the collection operations take {@code null} by definition and
 * stay quiet.
 */
class OclNullArgumentDiagnosticTest extends AbstractOclTest {

	private static final OclEvaluationOptions STRICT = OclEvaluationOptions.strict();
	private static final OclEvaluationOptions LENIENT = OclEvaluationOptions.lenient();

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	private static OclResult evalResult(String expression, OclEvaluationOptions options)
			throws OclParseException {
		OclExpression parsed = engine.parse(expression, self.eClass());
		return engine.evaluateWithDiagnostics(parsed, OclContext.of(self), options);
	}

	private static void assertNullArgumentReported(OclResult result, String operation) {
		assertSame(OclInvalid.INSTANCE, result.value());
		assertFalse(result.isSuccess(), "a null argument is an error, not a quiet invalid");
		assertEquals(1, result.diagnostics().size(), () -> "one diagnostic: " + result.diagnostics());
		Diagnostic diagnostic = result.diagnostics().get(0);
		assertEquals(Diagnostic.ERROR, diagnostic.getSeverity());
		assertEquals("Null argument for operation '" + operation + "'", diagnostic.getMessage());
	}

	@Test
	@DisplayName("'a' + null is invalid and names the operation")
	void stringConcatenationWithNull() throws OclParseException {
		assertNullArgumentReported(evalResult("'hello' + null", STRICT), "+");
	}

	@Test
	@DisplayName("1 + null is invalid and names the operation")
	void arithmeticWithNull() throws OclParseException {
		assertNullArgumentReported(evalResult("1 + null", STRICT), "+");
		assertNullArgumentReported(evalResult("2 * null", STRICT), "*");
	}

	@Test
	@DisplayName("a null that came out of a variable is reported the same way")
	void nullFromAVariable() throws OclParseException {
		// The generator case: the value is not written as a literal, it was simply never set
		assertNullArgumentReported(evalResult("let s : String = null in 'a' + s", STRICT), "+");
	}

	@Test
	@DisplayName("leniency is about null sources, a null argument stays an error")
	void lenientReportsItToo() throws OclParseException {
		assertNullArgumentReported(evalResult("'hello' + null", LENIENT), "+");
	}

	@Test
	@DisplayName("the null-safe operations take null and say nothing")
	void nullSafeOperationsStayQuiet() throws OclParseException {
		OclResult equals = evalResult("self.name = null", STRICT);
		assertEquals(Boolean.FALSE, equals.value());
		assertTrue(equals.diagnostics().isEmpty(), () -> "diagnostics: " + equals.diagnostics());

		OclResult differs = evalResult("self.name <> null", STRICT);
		assertEquals(Boolean.TRUE, differs.value());
		assertTrue(differs.diagnostics().isEmpty(), () -> "diagnostics: " + differs.diagnostics());
	}

	@Test
	@DisplayName("collection operations take null as an element and say nothing")
	void collectionOperationsStayQuiet() throws OclParseException {
		OclResult includes = evalResult("Sequence{1, 2}->includes(null)", STRICT);
		assertEquals(Boolean.FALSE, includes.value());
		assertTrue(includes.diagnostics().isEmpty(), () -> "diagnostics: " + includes.diagnostics());

		OclResult including = evalResult("Sequence{1, 2}->including(null)->size()", STRICT);
		assertEquals(3, ((Number) including.value()).intValue());
		assertTrue(including.diagnostics().isEmpty(), () -> "diagnostics: " + including.diagnostics());
	}

	@Test
	@DisplayName("a model operation takes null, and its implementation decides (#306)")
	void modelOperationTakesNull() throws OclParseException {
		// null conforms to every type (OCL v2.4 §8.2); the refusal of #270 is the standard
		// library's, which has no reading for 'a' + null — not the model's
		OclExpression parsed = engine.parse("p.getEClassifier(null)", self.eClass());
		OclResult result = engine.evaluateWithDiagnostics(parsed,
				OclContext.of(self, Map.of("p", EcorePackage.eINSTANCE)), STRICT);
		assertEquals(null, result.value());
		assertTrue(result.diagnostics().isEmpty(), () -> "diagnostics: " + result.diagnostics());
	}
}
