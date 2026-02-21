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
 * Comprehensive null/invalid propagation tests for arithmetic operations.
 *
 * <p>Per OCL v2.5 §11.2.4: any operation on invalid (except oclIsInvalid,
 * oclIsUndefined) yields invalid. Per §11.2.3: null as operand in
 * arithmetic context yields invalid (null is not a number).
 *
 * <p>Extends coverage beyond {@link OclInvalidNullSystematicTest} which
 * covers the basic cases. This class adds: null subtraction/division,
 * null unary ops, null/invalid floor/round/ceiling, missing invalid arg
 * variants, and Real mixed-type propagation.
 */
class OclNullInvalidArithmeticTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// === Null propagation through remaining arithmetic ops ===

	@Nested
	class NullArithmetic {

		@Test
		void null_minus_integer() throws OclParseException {
			assertInvalid("null - 1", self);
		}

		@Test
		void integer_minus_null() throws OclParseException {
			assertInvalid("1 - null", self);
		}

		@Test
		void null_divide_integer() throws OclParseException {
			assertInvalid("null / 2", self);
		}

		@Test
		void integer_divide_null() throws OclParseException {
			assertInvalid("2 / null", self);
		}

		@Test
		void negate_null() throws OclParseException {
			assertInvalid("-null", self);
		}

		@Test
		void null_abs() throws OclParseException {
			assertInvalid("null.abs()", self);
		}

		@Test
		void null_floor() throws OclParseException {
			assertInvalid("null.floor()", self);
		}

		@Test
		void null_round() throws OclParseException {
			assertInvalid("null.round()", self);
		}

		@Test
		void null_ceiling() throws OclParseException {
			assertInvalid("null.ceiling()", self);
		}

		@Test
		void null_max_integer() throws OclParseException {
			assertInvalid("null.max(1)", self);
		}

		@Test
		void integer_max_null() throws OclParseException {
			assertInvalid("1.max(null)", self);
		}

		@Test
		void null_min_integer() throws OclParseException {
			assertInvalid("null.min(1)", self);
		}

		@Test
		void integer_min_null() throws OclParseException {
			assertInvalid("1.min(null)", self);
		}

		@Test
		void null_div_integer() throws OclParseException {
			assertInvalid("null.div(2)", self);
		}

		@Test
		void integer_div_null() throws OclParseException {
			assertInvalid("2.div(null)", self);
		}

		@Test
		void null_mod_integer() throws OclParseException {
			assertInvalid("null.mod(2)", self);
		}

		@Test
		void integer_mod_null() throws OclParseException {
			assertInvalid("2.mod(null)", self);
		}
	}

	// === Missing invalid arg variants ===

	@Nested
	class InvalidArgVariants {

		@Test
		void integer_min_invalid() throws OclParseException {
			assertInvalid("1.min(invalid)", self);
		}

		@Test
		void integer_div_invalid() throws OclParseException {
			assertInvalid("2.div(invalid)", self);
		}

		@Test
		void integer_mod_invalid() throws OclParseException {
			assertInvalid("2.mod(invalid)", self);
		}

		@Test
		void invalid_floor() throws OclParseException {
			assertInvalid("invalid.floor()", self);
		}

		@Test
		void invalid_round() throws OclParseException {
			assertInvalid("invalid.round()", self);
		}

		@Test
		void invalid_ceiling() throws OclParseException {
			assertInvalid("invalid.ceiling()", self);
		}
	}

	// === Real with null/invalid ===

	@Nested
	class RealNullInvalid {

		@Test
		void null_plus_real() throws OclParseException {
			assertInvalid("null + 1.0", self);
		}

		@Test
		void real_plus_null() throws OclParseException {
			assertInvalid("1.0 + null", self);
		}

		@Test
		void null_minus_real() throws OclParseException {
			assertInvalid("null - 1.0", self);
		}

		@Test
		void real_minus_null() throws OclParseException {
			assertInvalid("1.0 - null", self);
		}

		@Test
		void null_times_real() throws OclParseException {
			assertInvalid("null * 1.0", self);
		}

		@Test
		void real_times_null() throws OclParseException {
			assertInvalid("1.0 * null", self);
		}

		@Test
		void null_divide_real() throws OclParseException {
			assertInvalid("null / 1.0", self);
		}

		@Test
		void real_divide_null() throws OclParseException {
			assertInvalid("1.0 / null", self);
		}

		@Test
		void invalid_plus_real() throws OclParseException {
			assertInvalid("invalid + 1.0", self);
		}

		@Test
		void real_plus_invalid() throws OclParseException {
			assertInvalid("1.0 + invalid", self);
		}

		@Test
		void invalid_minus_real() throws OclParseException {
			assertInvalid("invalid - 1.0", self);
		}

		@Test
		void real_minus_invalid() throws OclParseException {
			assertInvalid("1.0 - invalid", self);
		}

		@Test
		void invalid_times_real() throws OclParseException {
			assertInvalid("invalid * 1.0", self);
		}

		@Test
		void real_times_invalid() throws OclParseException {
			assertInvalid("1.0 * invalid", self);
		}

		@Test
		void invalid_divide_real() throws OclParseException {
			assertInvalid("invalid / 1.0", self);
		}

		@Test
		void real_divide_invalid() throws OclParseException {
			assertInvalid("1.0 / invalid", self);
		}

		@Test
		void real_max_null() throws OclParseException {
			assertInvalid("1.0.max(null)", self);
		}

		@Test
		void real_min_null() throws OclParseException {
			assertInvalid("1.0.min(null)", self);
		}

		@Test
		void real_max_invalid() throws OclParseException {
			assertInvalid("1.0.max(invalid)", self);
		}

		@Test
		void real_min_invalid() throws OclParseException {
			assertInvalid("1.0.min(invalid)", self);
		}
	}

	// === Conversion with null/invalid ===

	@Nested
	class ConversionNullInvalid {

		@Test
		void null_toInteger() throws OclParseException {
			assertInvalid("null.toInteger()", self);
		}

		@Test
		void null_toReal() throws OclParseException {
			assertInvalid("null.toReal()", self);
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
		void null_toString() throws OclParseException {
			// Spec §11.2.3: toString() not defined on OclVoid → invalid (general rule).
			assertInvalid("null.toString()", self);
		}
	}
}
