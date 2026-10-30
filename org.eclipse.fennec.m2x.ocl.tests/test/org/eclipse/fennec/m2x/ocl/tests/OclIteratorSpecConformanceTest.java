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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.OclResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Spec conformance of the predefined iterators (OCL v2.4 §8.3.7, §11.7.1, §11.9) found by the
 * section-by-section audit: non-Boolean condition bodies (#315), invalid inside collections and
 * the {@code specStrict} cases (#316), OCL equality in {@code isUnique} (#317).
 */
class OclIteratorSpecConformanceTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	static OclResult evalStrict(String expression) throws OclParseException {
		return engine.evaluateWithDiagnostics(engine.parse(expression, self.eClass()), OclContext.of(self),
				engine.getDefaultOptions().withSpecStrict(true));
	}

	static boolean hasWarning(OclResult result, String fragment) {
		return result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() == Diagnostic.WARNING && d.getMessage().contains(fragment));
	}

	/** §8.3.7 IteratorExp [3]: "The type of the body must be Boolean" (#315). */
	@Nested
	class NonBooleanBody {

		@Test
		void select_reject_withNumberBody_areInvalidWithError() throws OclParseException {
			assertInvalid("Set{1, 2}->select(x | x + 1)", self);
			assertInvalid("Set{1, 2}->reject(x | x + 1)", self);
			assertHasError("Set{1, 2}->select(x | x + 1)", self, "Body of 'select' must be Boolean");
		}

		@Test
		void forAll_exists_withNumberBody_areInvalidWithError() throws OclParseException {
			assertInvalid("Set{1, 2}->forAll(x | x + 1)", self);
			assertInvalid("Set{1, 2}->exists(x | x.toString())", self);
			assertInvalid("Set{1, 2}->forAll(x, y | x + y)", self);
			assertHasError("Set{1, 2}->exists(x | x + 1)", self, "Body of 'exists' must be Boolean");
		}

		@Test
		void any_one_withNumberBody_areInvalidWithError() throws OclParseException {
			assertInvalid("Set{1, 2}->any(x | x + 1)", self);
			assertInvalid("Set{1, 2}->one(x | x + 1)", self);
			assertHasError("Set{1, 2}->one(x | x + 1)", self, "Body of 'one' must be Boolean");
		}

		@Test
		void booleanBody_isStillFine() throws OclParseException {
			assertEquals(List.of(2), eval("Sequence{1, 2}->select(x | x > 1)", self));
			assertEquals(true, eval("Set{1, 2}->forAll(x | x > 0)", self));
			assertEquals(true, eval("Set{1, 2}->exists(x | x > 1)", self));
		}

		@Test
		void if_withNonBooleanCondition_isInvalidWithError() throws OclParseException {
			// §8.3.7 IfExp [1] / §10.3.2 IfExpEval [1]
			assertInvalid("if null then 1 else 2 endif", self);
			assertInvalid("if 1 then 1 else 2 endif", self);
			assertInvalid("if 'x' then 1 else 2 endif", self);
			assertHasError("if 1 then 1 else 2 endif", self, "must be Boolean");
		}
	}

	/** §11.7.1 [1]: "A collection cannot contain invalid values" (#316). */
	@Nested
	class InvalidInCollections {

		@Test
		void literal_withInvalidPart_isInvalid() throws OclParseException {
			assertInvalid("Set{1, invalid, 3}", self);
			assertInvalid("Sequence{1, invalid}->size()", self);
			assertInvalid("Bag{1 / 0}", self);
		}

		@Test
		void literal_withNullPart_keepsNull() throws OclParseException {
			assertEquals(2, eval("Set{1, null}->size()", self));
			assertEquals(true, eval("Sequence{null}->includes(null)", self));
		}

		@Test
		void collect_withInvalidBody_isInvalid() throws OclParseException {
			assertInvalid("Sequence{1, 2}->collect(x | 1 / 0)", self);
			assertInvalid("Set{1, 0}->collect(x | 1.div(x))", self);
		}

		@Test
		void collectNested_withInvalidBody_isInvalid_nullStays() throws OclParseException {
			assertInvalid("Sequence{1, 2}->collectNested(x | invalid)", self);
			assertEquals(3, eval("Bag{1, 2, 3}->collectNested(x | if x = 2 then null else Set{x} endif)->size()", self));
		}

		@Test
		void closure_withInvalidBody_isInvalid_nullAddsNothing() throws OclParseException {
			assertInvalid("Set{1}->closure(x | invalid)", self);
			assertInvalid("Set{1}->closure(x | 1 / 0)", self);
			assertEquals(1, eval("Set{1}->closure(x | null)->size()", self));
		}

		@Test
		void collect_flattensRecursively() throws OclParseException {
			// §11.9.1: collect = collectNested(…)->flatten(), and flatten is recursive (§11.7.1)
			assertEquals(List.of(1, 2), eval("Sequence{1}->collect(x | Sequence{Sequence{x, 2}})", self));
			assertEquals(1, eval("Set{1}->collect(x | Set{Set{x}})->size()", self));
			// collectNested keeps the nesting
			assertEquals(1, eval("Sequence{1}->collectNested(x | Sequence{Sequence{x, 2}})->size()", self));
		}
	}

	/** {@code any}, {@code one}, {@code isUnique}, {@code sortedBy} (§11.9.1, §11.9.2, #316). */
	@Nested
	class AnyOneIsUniqueSortedBy {

		@Test
		void any_withoutMatch_isNullByDefault_invalidWhenSpecStrict() throws OclParseException {
			assertNull(eval("Sequence{1, 2}->any(x | x > 5)", self));
			assertSame(OclInvalid.INSTANCE, evalStrict("Sequence{1, 2}->any(x | x > 5)").value());
			assertSame(OclInvalid.INSTANCE, evalStrict("Sequence{}->any(x | true)").value());
		}

		@Test
		void any_withInvalidBodyBeforeMatch_isInvalid() throws OclParseException {
			// "Returns invalid if any body evaluates to invalid for any element"
			assertInvalid("Sequence{0, 1}->any(x | 1.div(x) = 1)", self);
		}

		@Test
		void any_withInvalidBodyAfterMatch_isInvalidOnlyWhenSpecStrict() throws OclParseException {
			assertEquals(1, eval("Sequence{1, 0}->any(x | 1.div(x) = 1)", self));
			assertSame(OclInvalid.INSTANCE, evalStrict("Sequence{1, 0}->any(x | 1.div(x) = 1)").value());
		}

		@Test
		void any_withNullBody_isInvalid() throws OclParseException {
			// mapping select(…)->asSequence()->first(): select with a null body is invalid
			assertInvalid("Sequence{1, 2}->any(x | null)", self);
		}

		@Test
		void any_withMatch_returnsElement() throws OclParseException {
			assertEquals(2, eval("Sequence{1, 2, 3}->any(x | x = 2)", self));
			assertEquals(2L, evalStrict("Sequence{1, 2, 3}->any(x | x = 2)").value());
		}

		@Test
		void one_shortCircuitsByDefault_evaluatesAllWhenSpecStrict() throws OclParseException {
			assertEquals(false, eval("Sequence{1, 1, 0}->one(x | 1.div(x) = 1)", self));
			assertSame(OclInvalid.INSTANCE, evalStrict("Sequence{1, 1, 0}->one(x | 1.div(x) = 1)").value());
			assertEquals(false, evalStrict("Sequence{1, 1, 2}->one(x | x = 1)").value());
			assertEquals(true, evalStrict("Sequence{1, 2, 3}->one(x | x = 1)").value());
		}

		@Test
		void isUnique_usesOclEquality() throws OclParseException {
			// §11.5.1: 1 = 1.0, so the two values are not different (#317)
			assertEquals(false, eval("Sequence{1, 1.0}->isUnique(x | x)", self));
			assertEquals(false, eval("Bag{1, 1.0}->isUnique(x | x)", self));
			assertEquals(true, eval("Sequence{1, 2}->isUnique(x | x)", self));
			assertEquals(false, eval("Sequence{1, 2}->isUnique(x | 1.0)", self));
		}

		@Test
		void isUnique_shortCircuitsByDefault_evaluatesAllWhenSpecStrict() throws OclParseException {
			String expression = "Sequence{1, 1, 0}->isUnique(x | if x = 0 then 1.div(0) else 5 endif)";
			assertEquals(false, eval(expression, self));
			assertSame(OclInvalid.INSTANCE, evalStrict(expression).value());
		}

		@Test
		void sortedBy_withNullKey_isInvalid() throws OclParseException {
			assertInvalid("Sequence{3, 1, 2}->sortedBy(x | if x = 1 then null else x endif)", self);
			assertInvalid("Sequence{1}->sortedBy(x | null)", self);
			assertTrue(hasWarning(evalWithDiagnostics("Sequence{1}->sortedBy(x | null)", self), "sortedBy key is null"));
		}
	}

	/** §8.3.7 CollectionRange [2]: first <= last (#326). */
	@Nested
	class DecreasingRange {

		@Test
		void decreasingRange_isEmptyWithWarning() throws OclParseException {
			OclResult result = evalWithDiagnostics("Sequence{5..3}", self);
			assertEquals(List.of(), result.value());
			assertTrue(hasWarning(result, "is decreasing"));
		}

		@Test
		void decreasingRange_isInvalidWhenSpecStrict() throws OclParseException {
			OclResult result = evalStrict("Sequence{5..3}");
			assertSame(OclInvalid.INSTANCE, result.value());
			assertTrue(result.diagnostics().stream().anyMatch(d -> d.getSeverity() == Diagnostic.ERROR));
		}

		@Test
		void singleElementRange_isFine() throws OclParseException {
			OclResult result = evalStrict("Sequence{3..3}");
			assertEquals(List.of(3L), result.value());
		}
	}

	@Test
	void specStrict_isOffByDefault() {
		assertEquals(false, OclEvaluationOptions.strict().specStrict());
		assertEquals(false, OclEvaluationOptions.lenient().specStrict());
		assertEquals(true, OclEvaluationOptions.strict().withSpecStrict(true).specStrict());
	}
}
