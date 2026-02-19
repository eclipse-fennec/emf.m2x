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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Systematic tests for invalid and null propagation through all major
 * OCL operation categories: arithmetic, string, comparison, type operations.
 *
 * <p>Per OCL spec, invalid propagates through most operations (except
 * oclIsInvalid/oclIsUndefined). Null (OclVoid) propagates differently:
 * arithmetic/string ops on null produce invalid, but equality with null is defined.
 */
class OclInvalidNullSystematicTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// === Invalid propagation through arithmetic ===

	@Test
	void invalid_plus_integer() throws OclParseException {
		assertInvalid("invalid + 1", self);
	}

	@Test
	void integer_plus_invalid() throws OclParseException {
		assertInvalid("1 + invalid", self);
	}

	@Test
	void invalid_minus_integer() throws OclParseException {
		assertInvalid("invalid - 1", self);
	}

	@Test
	void integer_minus_invalid() throws OclParseException {
		assertInvalid("1 - invalid", self);
	}

	@Test
	void invalid_times_integer() throws OclParseException {
		assertInvalid("invalid * 2", self);
	}

	@Test
	void integer_times_invalid() throws OclParseException {
		assertInvalid("2 * invalid", self);
	}

	@Test
	void invalid_div_integer() throws OclParseException {
		assertInvalid("invalid / 2", self);
	}

	@Test
	void integer_div_invalid() throws OclParseException {
		assertInvalid("2 / invalid", self);
	}

	@Test
	void invalid_unaryMinus() throws OclParseException {
		assertInvalid("-invalid", self);
	}

	@Test
	void invalid_abs() throws OclParseException {
		assertInvalid("invalid.abs()", self);
	}

	@Test
	void invalid_max_integer() throws OclParseException {
		assertInvalid("invalid.max(1)", self);
	}

	@Test
	void integer_max_invalid() throws OclParseException {
		assertInvalid("1.max(invalid)", self);
	}

	@Test
	void invalid_min_integer() throws OclParseException {
		assertInvalid("invalid.min(1)", self);
	}

	@Test
	void invalid_mod() throws OclParseException {
		assertInvalid("invalid.mod(2)", self);
	}

	@Test
	void invalid_integerDiv() throws OclParseException {
		assertInvalid("invalid.div(2)", self);
	}

	// === Invalid propagation through string operations ===

	@Test
	void invalid_concat_string() throws OclParseException {
		assertInvalid("invalid.concat('x')", self);
	}

	@Test
	void string_concat_invalid() throws OclParseException {
		assertInvalid("'x'.concat(invalid)", self);
	}

	@Test
	void invalid_size() throws OclParseException {
		assertInvalid("invalid.size()", self);
	}

	@Test
	void invalid_toUpperCase() throws OclParseException {
		assertInvalid("invalid.toUpperCase()", self);
	}

	@Test
	void invalid_toLowerCase() throws OclParseException {
		assertInvalid("invalid.toLowerCase()", self);
	}

	@Test
	void invalid_substring() throws OclParseException {
		assertInvalid("invalid.substring(1, 2)", self);
	}

	// === Invalid propagation through comparison ===

	@Test
	void invalid_lessThan_integer() throws OclParseException {
		assertInvalid("invalid < 1", self);
	}

	@Test
	void integer_lessThan_invalid() throws OclParseException {
		assertInvalid("1 < invalid", self);
	}

	@Test
	void invalid_greaterThan_integer() throws OclParseException {
		assertInvalid("invalid > 1", self);
	}

	@Test
	void invalid_lessOrEqual() throws OclParseException {
		assertInvalid("invalid <= 1", self);
	}

	@Test
	void invalid_greaterOrEqual() throws OclParseException {
		assertInvalid("invalid >= 1", self);
	}

	// === Invalid equality (special: = and <> ARE defined for invalid) ===

	@Test
	void invalid_equals_invalid() throws OclParseException {
		assertEquals(true, eval("invalid = invalid", self));
	}

	@Test
	void invalid_equals_integer() throws OclParseException {
		// invalid = 1 should be false (different values) or could be invalid
		// Per OCL spec §11.2.2: = is strict, so invalid = anything-non-invalid = false?
		// Actually, Eclipse OCL returns invalid for invalid = 1
		// But our existing test (OclInvalidPropagationTest) says invalid <> 42 = true
		// So we expect: invalid = 1 → false, invalid <> 1 → true
		assertEquals(false, eval("invalid = 1", self));
	}

	@Test
	void integer_equals_invalid() throws OclParseException {
		assertEquals(false, eval("1 = invalid", self));
	}

	@Test
	void invalid_notEquals_integer() throws OclParseException {
		assertEquals(true, eval("invalid <> 1", self));
	}

	@Test
	void invalid_notEquals_invalid() throws OclParseException {
		assertEquals(false, eval("invalid <> invalid", self));
	}

	// === Invalid propagation through collection operations ===

	@Test
	void invalid_source_size() throws OclParseException {
		assertInvalid("invalid->size()", self);
	}

	@Test
	void invalid_source_isEmpty() throws OclParseException {
		assertInvalid("invalid->isEmpty()", self);
	}

	@Test
	void invalid_source_includes() throws OclParseException {
		assertInvalid("invalid->includes(1)", self);
	}

	// === Invalid in type operations ===

	@Test
	void invalid_oclIsKindOf() throws OclParseException {
		// oclIsKindOf on invalid — should propagate invalid
		assertInvalid("(1 / 0).oclIsKindOf(Integer)", self);
	}

	@Test
	void invalid_oclIsTypeOf() throws OclParseException {
		assertInvalid("(1 / 0).oclIsTypeOf(Integer)", self);
	}

	@Test
	void valid_oclIsKindOf_integer() throws OclParseException {
		assertEquals(true, eval("42.oclIsKindOf(Integer)", self));
	}

	@Test
	void valid_oclIsKindOf_string() throws OclParseException {
		assertEquals(true, eval("'hello'.oclIsKindOf(String)", self));
	}

	@Test
	void valid_oclIsKindOf_boolean() throws OclParseException {
		assertEquals(true, eval("true.oclIsKindOf(Boolean)", self));
	}

	@Test
	void valid_oclIsKindOf_wrong_type() throws OclParseException {
		assertEquals(false, eval("42.oclIsKindOf(String)", self));
	}

	// === Null propagation through arithmetic ===

	@Test
	void null_plus_integer() throws OclParseException {
		// null + 1 → invalid (null is not a number)
		assertInvalid("null + 1", self);
	}

	@Test
	void integer_plus_null() throws OclParseException {
		assertInvalid("1 + null", self);
	}

	@Test
	void null_times_integer() throws OclParseException {
		assertInvalid("null * 2", self);
	}

	// === Null equality (special: = and <> ARE defined for null) ===

	@Test
	void null_equals_null() throws OclParseException {
		assertEquals(true, eval("null = null", self));
	}

	@Test
	void null_equals_integer() throws OclParseException {
		assertEquals(false, eval("null = 1", self));
	}

	@Test
	void integer_equals_null() throws OclParseException {
		assertEquals(false, eval("1 = null", self));
	}

	@Test
	void null_notEquals_integer() throws OclParseException {
		assertEquals(true, eval("null <> 1", self));
	}

	@Test
	void null_notEquals_null() throws OclParseException {
		assertEquals(false, eval("null <> null", self));
	}

	// === Null vs Invalid distinction ===

	@Test
	void null_notEquals_invalid() throws OclParseException {
		assertEquals(true, eval("null <> invalid", self));
	}

	@Test
	void invalid_notEquals_null() throws OclParseException {
		assertEquals(true, eval("invalid <> null", self));
	}

	@Test
	void null_equals_invalid() throws OclParseException {
		assertEquals(false, eval("null = invalid", self));
	}

	// === Null oclIsInvalid / oclIsUndefined ===

	@Test
	void null_oclIsInvalid_false() throws OclParseException {
		assertEquals(false, eval("null.oclIsInvalid()", self));
	}

	@Test
	void null_oclIsUndefined_true() throws OclParseException {
		assertEquals(true, eval("null.oclIsUndefined()", self));
	}

	@Test
	void invalid_oclIsInvalid_true() throws OclParseException {
		assertEquals(true, eval("invalid.oclIsInvalid()", self));
	}

	@Test
	void invalid_oclIsUndefined_true() throws OclParseException {
		assertEquals(true, eval("invalid.oclIsUndefined()", self));
	}

	// === Chained invalid propagation ===

	@Test
	void chainedInvalid_arithmetic() throws OclParseException {
		// (1/0) + 2 * 3 → invalid + 6 → invalid
		assertInvalid("(1 / 0) + 2 * 3", self);
	}

	@Test
	void chainedInvalid_stringOps() throws OclParseException {
		assertInvalid("invalid.toUpperCase().size()", self);
	}

	@Test
	void chainedInvalid_collectionOps() throws OclParseException {
		assertInvalid("invalid->select(x | true)->size()", self);
	}
}
