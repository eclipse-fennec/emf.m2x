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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@code toBoolean()} semantics — strict matching, no trim/lowercase.
 *
 * <p>Spec: OCL v2.4 §11.5.3:
 * <br>{@code toBoolean() : Boolean} — post: {@code result = (self = 'true')}
 *
 * <p>This means: only the exact string {@code "true"} yields {@code true},
 * everything else (including {@code "True"}, {@code " true"}, {@code "yes"}, etc.)
 * yields {@code false}. No trimming, no case-insensitive matching.
 *
 * <p><strong>Design decision:</strong> Eclipse Pivot (OCL 2.5) returns {@code null} for
 * strings that are neither {@code "true"} nor {@code "false"}. The spec postcondition
 * {@code result = (self = 'true')} always yields Boolean. Fennec follows the spec
 * (returns {@code false}), not Eclipse Pivot (returns {@code null}).
 *
 * <p>Eclipse reference: {@code GenericEvaluationStringOperationTest#testStringToBoolean()},
 * {@code EvaluateStringOperationsTest4#testStringToBoolean()}.
 */
class OclToBooleanSemanticsTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// ========================================================================
	// Valid cases
	// ========================================================================

	@Nested
	class ValidCases {

		@Test
		void true_string() throws OclParseException {
			assertEquals(true, eval("'true'.toBoolean()", self));
		}

		@Test
		void false_string() throws OclParseException {
			assertEquals(false, eval("'false'.toBoolean()", self));
		}
	}

	// ========================================================================
	// No trim — leading/trailing whitespace → false
	// ========================================================================

	@Nested
	class NoTrim {

		@Test
		void leadingSpace_false() throws OclParseException {
			// ' true'.toBoolean() → false (no trimming)
			assertEquals(false, eval("' true'.toBoolean()", self));
		}

		@Test
		void trailingSpace_false() throws OclParseException {
			assertEquals(false, eval("'true '.toBoolean()", self));
		}

		@Test
		void leadingAndTrailingSpace_false() throws OclParseException {
			assertEquals(false, eval("' true '.toBoolean()", self));
		}
	}

	// ========================================================================
	// Case-sensitive — uppercase/mixed case → false
	// ========================================================================

	@Nested
	class CaseSensitive {

		@Test
		void uppercase_True_false() throws OclParseException {
			assertEquals(false, eval("'True'.toBoolean()", self));
		}

		@Test
		void uppercase_TRUE_false() throws OclParseException {
			assertEquals(false, eval("'TRUE'.toBoolean()", self));
		}

		@Test
		void uppercase_FALSE_false() throws OclParseException {
			assertEquals(false, eval("'FALSE'.toBoolean()", self));
		}

		@Test
		void uppercase_False_false() throws OclParseException {
			assertEquals(false, eval("'False'.toBoolean()", self));
		}
	}

	// ========================================================================
	// Unrecognized strings → false (spec: result = (self = 'true'))
	// ========================================================================

	@Nested
	class UnrecognizedStrings {

		@Test
		void number_string_false() throws OclParseException {
			assertEquals(false, eval("'-4'.toBoolean()", self));
		}

		@Test
		void yes_string_false() throws OclParseException {
			assertEquals(false, eval("'yes'.toBoolean()", self));
		}

		@Test
		void empty_string_false() throws OclParseException {
			assertEquals(false, eval("''.toBoolean()", self));
		}

		@Test
		void arbitrary_string_false() throws OclParseException {
			assertEquals(false, eval("'maybe'.toBoolean()", self));
		}
	}

	// ========================================================================
	// null / invalid → invalid
	// ========================================================================

	@Nested
	class NullInvalid {

		@Test
		void null_toBoolean_invalid() throws OclParseException {
			assertInvalid("null.toBoolean()", self);
		}

		@Test
		void invalid_toBoolean_invalid() throws OclParseException {
			assertInvalid("invalid.toBoolean()", self);
		}
	}
}
