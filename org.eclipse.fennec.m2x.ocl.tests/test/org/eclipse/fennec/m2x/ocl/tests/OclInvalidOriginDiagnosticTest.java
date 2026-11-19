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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
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
 * An {@code invalid} a standard-library operation produces from valid values is reported where
 * it originates (#304).
 *
 * <p>{@code Sequence{}->first()} is {@code invalid} (OCL v2.4 §11.7.2: {@code first()} is
 * {@code at(1)}, whose precondition the empty sequence violates). The value is right; what was
 * missing is a word about it, so that an {@code OclInvalid} in a log or a generated file can be
 * traced to its expression. A warning, once, at the origin — not while it propagates, and not
 * where the expression tests for it on the spot.
 */
class OclInvalidOriginDiagnosticTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	private static OclResult evalResult(String expression) throws OclParseException {
		OclExpression parsed = engine.parse(expression, self.eClass());
		return engine.evaluateWithDiagnostics(parsed, OclContext.of(self),
				OclEvaluationOptions.lenient());
	}

	private static void assertWarned(OclResult result, String message) {
		assertSame(OclInvalid.INSTANCE, result.value());
		assertTrue(result.isSuccess(), "a warning, not an error: " + result.diagnostics());
		assertEquals(1, result.diagnostics().size(), () -> "one diagnostic: " + result.diagnostics());
		Diagnostic diagnostic = result.diagnostics().get(0);
		assertEquals(Diagnostic.WARNING, diagnostic.getSeverity());
		assertEquals(message, diagnostic.getMessage());
	}

	private static void assertQuiet(OclResult result) {
		assertEquals(List.of(), result.diagnostics());
	}

	@Test
	@DisplayName("first() of an empty sequence is invalid, and says so")
	void firstOfAnEmptySequence() throws OclParseException {
		assertWarned(evalResult("Sequence{}->first()"), "'first' on an empty Sequence is invalid");
	}

	@Test
	@DisplayName("at() out of range names the collection and the index")
	void atOutOfRange() throws OclParseException {
		assertWarned(evalResult("OrderedSet{1, 2}->at(3)"), "'at' on an OrderedSet of 2 with 3 is invalid");
	}

	@Test
	@DisplayName("a division by zero names its operands")
	void divisionByZero() throws OclParseException {
		assertWarned(evalResult("1 / 0"), "'/' on 1 with 0 is invalid");
	}

	@Test
	@DisplayName("invalid propagating further is reported once, at its origin")
	void reportedOnceWhilePropagating() throws OclParseException {
		assertWarned(evalResult("(Sequence{}->first() + 1) * 2"),
				"'first' on an empty Sequence is invalid");
	}

	@Test
	@DisplayName("tested on the spot, invalid is expected and nothing is said")
	void testedOnTheSpot() throws OclParseException {
		OclResult undefined = evalResult("Sequence{}->first().oclIsUndefined()");
		assertEquals(true, undefined.value());
		assertQuiet(undefined);

		OclResult invalid = evalResult("(1 / 0).oclIsInvalid()");
		assertEquals(true, invalid.value());
		assertQuiet(invalid);
	}

	@Test
	@DisplayName("a defined result is quiet")
	void definedResultIsQuiet() throws OclParseException {
		OclResult result = evalResult("Sequence{1, 2}->first()");
		assertEquals(1L, result.value());
		assertQuiet(result);
	}
}
