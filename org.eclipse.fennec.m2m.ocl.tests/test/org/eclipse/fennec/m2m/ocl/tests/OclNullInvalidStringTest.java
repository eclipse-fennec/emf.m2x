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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Null/invalid propagation tests for String operations.
 *
 * <p>Per OCL v2.5: any operation on invalid yields invalid.
 * Any String operation on null (OclVoid) receiver yields invalid
 * because null is not a String.
 *
 * <p>Extends coverage beyond {@link OclInvalidNullSystematicTest} which
 * covers: invalid.concat, concat(invalid), invalid.size, invalid.toUpperCase,
 * invalid.toLowerCase, invalid.substring.
 */
class OclNullInvalidStringTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// === Null receiver on String operations ===

	@Nested
	class NullReceiver {

		@Test
		void null_size() throws OclParseException {
			assertInvalid("null.size()", self);
		}

		@Test
		void null_concat() throws OclParseException {
			assertInvalid("null.concat('x')", self);
		}

		@Test
		void null_toUpperCase() throws OclParseException {
			assertInvalid("null.toUpperCase()", self);
		}

		@Test
		void null_toLowerCase() throws OclParseException {
			assertInvalid("null.toLowerCase()", self);
		}

		@Test
		void null_substring() throws OclParseException {
			assertInvalid("null.substring(1, 2)", self);
		}

		@Test
		void null_indexOf() throws OclParseException {
			assertInvalid("null.indexOf('x')", self);
		}

		@Test
		void null_at() throws OclParseException {
			assertInvalid("null.at(1)", self);
		}

		@Test
		void null_characters() throws OclParseException {
			assertInvalid("null.characters()", self);
		}

		@Test
		void null_trim() throws OclParseException {
			assertInvalid("null.trim()", self);
		}

		@Test
		void null_matches() throws OclParseException {
			assertInvalid("null.matches('.*')", self);
		}

		@Test
		void null_toInteger() throws OclParseException {
			assertInvalid("null.toInteger()", self);
		}

		@Test
		void null_toReal() throws OclParseException {
			assertInvalid("null.toReal()", self);
		}

		@Test
		void null_toBoolean() throws OclParseException {
			assertInvalid("null.toBoolean()", self);
		}
	}

	// === Invalid receiver on remaining String operations ===

	@Nested
	class InvalidReceiver {

		@Test
		void invalid_indexOf() throws OclParseException {
			assertInvalid("invalid.indexOf('x')", self);
		}

		@Test
		void invalid_at() throws OclParseException {
			assertInvalid("invalid.at(1)", self);
		}

		@Test
		void invalid_characters() throws OclParseException {
			assertInvalid("invalid.characters()", self);
		}

		@Test
		void invalid_trim() throws OclParseException {
			assertInvalid("invalid.trim()", self);
		}

		@Test
		void invalid_matches() throws OclParseException {
			assertInvalid("invalid.matches('.*')", self);
		}

		@Test
		void invalid_toInteger() throws OclParseException {
			assertInvalid("invalid.toInteger()", self);
		}

		@Test
		void invalid_toReal() throws OclParseException {
			assertInvalid("invalid.toReal()", self);
		}

		@Test
		void invalid_toBoolean() throws OclParseException {
			assertInvalid("invalid.toBoolean()", self);
		}
	}

	// === Null/Invalid as argument ===

	@Nested
	class NullInvalidArgument {

		@Test
		void string_concat_null() throws OclParseException {
			assertInvalid("'hello'.concat(null)", self);
		}

		@Test
		void string_indexOf_null() throws OclParseException {
			assertInvalid("'hello'.indexOf(null)", self);
		}

		@Test
		void string_indexOf_invalid() throws OclParseException {
			assertInvalid("'hello'.indexOf(invalid)", self);
		}

		@Test
		void string_matches_null() throws OclParseException {
			assertInvalid("'hello'.matches(null)", self);
		}

		@Test
		void string_matches_invalid() throws OclParseException {
			assertInvalid("'hello'.matches(invalid)", self);
		}

		@Test
		void string_at_null() throws OclParseException {
			assertInvalid("'hello'.at(null)", self);
		}

		@Test
		void string_at_invalid() throws OclParseException {
			assertInvalid("'hello'.at(invalid)", self);
		}

		@Test
		void string_substring_null_args() throws OclParseException {
			assertInvalid("'hello'.substring(null, 2)", self);
		}
	}

	// === String comparison with null/invalid ===

	@Nested
	class StringComparisonNullInvalid {

		@Test
		void null_lessThan_string() throws OclParseException {
			assertInvalid("null < 'hello'", self);
		}

		@Test
		void string_lessThan_null() throws OclParseException {
			assertInvalid("'hello' < null", self);
		}

		@Test
		void null_greaterThan_string() throws OclParseException {
			assertInvalid("null > 'hello'", self);
		}

		@Test
		void invalid_lessThan_string() throws OclParseException {
			assertInvalid("invalid < 'hello'", self);
		}

		@Test
		void string_lessThan_invalid() throws OclParseException {
			assertInvalid("'hello' < invalid", self);
		}
	}

	// === String + operator with null/invalid ===

	@Nested
	class StringPlusNullInvalid {

		@Test
		void null_plus_string() throws OclParseException {
			assertInvalid("null + 'hello'", self);
		}

		@Test
		void string_plus_null() throws OclParseException {
			assertInvalid("'hello' + null", self);
		}

		@Test
		void invalid_plus_string() throws OclParseException {
			assertInvalid("invalid + 'hello'", self);
		}

		@Test
		void string_plus_invalid() throws OclParseException {
			assertInvalid("'hello' + invalid", self);
		}
	}
}
