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
 * Null/invalid propagation tests for comparison and equality operators.
 *
 * <p>Per OCL v2.5:
 * <ul>
 *   <li>Equality with null: {@code null = null} = true, {@code null = x} = false,
 *       {@code null <> x} = true</li>
 *   <li>Equality with invalid: any use of {@code =} or {@code <>} where
 *       either operand is invalid yields invalid</li>
 *   <li>Comparison ({@code <, >, <=, >=}) with null or invalid: yields invalid</li>
 * </ul>
 *
 * <p>Extends coverage beyond {@link OclInvalidNullSystematicTest} which covers
 * the basic integer comparison and equality cases.
 */
class OclNullInvalidComparisonTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// === Null equality — defined per OCL v2.5 §11.2.3 ===

	@Nested
	class NullEquality {

		@Test
		void null_equals_null() throws OclParseException {
			assertEquals(true, eval("null = null", self));
		}

		@Test
		void null_notEquals_null() throws OclParseException {
			assertEquals(false, eval("null <> null", self));
		}

		@Test
		void null_equals_string() throws OclParseException {
			assertEquals(false, eval("null = 'hello'", self));
		}

		@Test
		void string_equals_null() throws OclParseException {
			assertEquals(false, eval("'hello' = null", self));
		}

		@Test
		void null_notEquals_string() throws OclParseException {
			assertEquals(true, eval("null <> 'hello'", self));
		}

		@Test
		void null_equals_boolean() throws OclParseException {
			assertEquals(false, eval("null = true", self));
		}

		@Test
		void boolean_equals_null() throws OclParseException {
			assertEquals(false, eval("false = null", self));
		}

		@Test
		void null_notEquals_boolean() throws OclParseException {
			assertEquals(true, eval("null <> true", self));
		}

		@Test
		void boolean_notEquals_null() throws OclParseException {
			assertEquals(true, eval("false <> null", self));
		}
	}

	// === Invalid equality — all yield invalid ===

	@Nested
	class InvalidEquality {

		@Test
		void invalid_equals_invalid() throws OclParseException {
			assertInvalid("invalid = invalid", self);
		}

		@Test
		void invalid_notEquals_invalid() throws OclParseException {
			assertInvalid("invalid <> invalid", self);
		}

		@Test
		void invalid_equals_string() throws OclParseException {
			assertInvalid("invalid = 'hello'", self);
		}

		@Test
		void string_equals_invalid() throws OclParseException {
			assertInvalid("'hello' = invalid", self);
		}

		@Test
		void invalid_notEquals_string() throws OclParseException {
			assertInvalid("invalid <> 'hello'", self);
		}

		@Test
		void string_notEquals_invalid() throws OclParseException {
			assertInvalid("'hello' <> invalid", self);
		}

		@Test
		void invalid_equals_boolean() throws OclParseException {
			assertInvalid("invalid = true", self);
		}

		@Test
		void boolean_equals_invalid() throws OclParseException {
			assertInvalid("false = invalid", self);
		}

		@Test
		void invalid_notEquals_boolean() throws OclParseException {
			assertInvalid("invalid <> true", self);
		}

		@Test
		void boolean_notEquals_invalid() throws OclParseException {
			assertInvalid("false <> invalid", self);
		}

		@Test
		void null_equals_invalid() throws OclParseException {
			assertInvalid("null = invalid", self);
		}

		@Test
		void invalid_equals_null() throws OclParseException {
			assertInvalid("invalid = null", self);
		}

		@Test
		void null_notEquals_invalid() throws OclParseException {
			assertInvalid("null <> invalid", self);
		}

		@Test
		void invalid_notEquals_null() throws OclParseException {
			assertInvalid("invalid <> null", self);
		}
	}

	// === Null comparison — all yield invalid ===

	@Nested
	class NullComparison {

		@Test
		void null_lessThan_integer() throws OclParseException {
			assertInvalid("null < 1", self);
		}

		@Test
		void integer_lessThan_null() throws OclParseException {
			assertInvalid("1 < null", self);
		}

		@Test
		void null_greaterThan_integer() throws OclParseException {
			assertInvalid("null > 1", self);
		}

		@Test
		void integer_greaterThan_null() throws OclParseException {
			assertInvalid("1 > null", self);
		}

		@Test
		void null_lessOrEqual_integer() throws OclParseException {
			assertInvalid("null <= 1", self);
		}

		@Test
		void integer_lessOrEqual_null() throws OclParseException {
			assertInvalid("1 <= null", self);
		}

		@Test
		void null_greaterOrEqual_integer() throws OclParseException {
			assertInvalid("null >= 1", self);
		}

		@Test
		void integer_greaterOrEqual_null() throws OclParseException {
			assertInvalid("1 >= null", self);
		}
	}

	// === Invalid comparison (remaining cases) ===

	@Nested
	class InvalidComparison {

		@Test
		void integer_greaterThan_invalid() throws OclParseException {
			assertInvalid("1 > invalid", self);
		}

		@Test
		void integer_lessOrEqual_invalid() throws OclParseException {
			assertInvalid("1 <= invalid", self);
		}

		@Test
		void integer_greaterOrEqual_invalid() throws OclParseException {
			assertInvalid("1 >= invalid", self);
		}

		@Test
		void real_lessThan_null() throws OclParseException {
			assertInvalid("1.0 < null", self);
		}

		@Test
		void real_greaterThan_null() throws OclParseException {
			assertInvalid("1.0 > null", self);
		}

		@Test
		void real_lessThan_invalid() throws OclParseException {
			assertInvalid("1.0 < invalid", self);
		}

		@Test
		void real_greaterThan_invalid() throws OclParseException {
			assertInvalid("1.0 > invalid", self);
		}
	}
}
