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
 * Tests for Arrow ({@code ->}) vs Dot ({@code .}) semantics on non-collection values.
 *
 * <p>Spec references:
 * <ul>
 *   <li>§9.3.35 Rule [B]: Arrow on non-collection inserts implicit {@code oclAsSet()}</li>
 *   <li>§11.2.3 OclVoid: null property/operation → invalid, except listed ops in §11.3.2</li>
 *   <li>§11.2.4 OclInvalid: invalid property/operation → invalid, except listed ops in §11.3.3</li>
 *   <li>§11.3.1 OclAny: {@code oclAsSet()} → {@code Set{self}}</li>
 *   <li>§11.3.2 OclVoid: {@code oclAsSet()} → {@code Set{}}</li>
 *   <li>§11.3.3 OclInvalid: {@code oclAsSet()} → invalid</li>
 * </ul>
 *
 * <p>Eclipse reference: {@code GenericEvaluationOclAnyOperationTest#test_oclAsSet_implicit()}
 *
 * <p>⚠️ SPEC-FIRST: Tests are written against the spec. If they fail,
 * the implementation has a gap — fix the implementation, NOT the test.
 */
class OclArrowDotSemanticsTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// ========================================================================
	// oclIsUndefined: Dot vs Arrow on null (§11.2.3, §11.3.2, §9.3.35[B])
	// ========================================================================

	@Test
	void null_dot_oclIsUndefined_isTrue() throws OclParseException {
		// §11.3.2: oclIsUndefined is an exception operation on OclVoid → true
		assertEquals(true, eval("null.oclIsUndefined()", self));
	}

	@Test
	void null_arrow_oclIsUndefined_isFalse() throws OclParseException {
		// §9.3.35[B]: null->X = null.oclAsSet()->X = Set{}->X
		// §11.3.2: null.oclAsSet() = Set{}
		// Set{}.oclIsUndefined() = false (a Set is not undefined)
		assertEquals(false, eval("null->oclIsUndefined()", self));
	}

	// ========================================================================
	// oclIsUndefined: Dot vs Arrow on invalid (§11.2.4, §11.3.3)
	// ========================================================================

	@Test
	void invalid_dot_oclIsUndefined_isTrue() throws OclParseException {
		// §11.3.3: oclIsUndefined is an exception operation on OclInvalid → true
		assertEquals(true, eval("invalid.oclIsUndefined()", self));
	}

	@Test
	void invalid_arrow_oclIsUndefined_isTrue() throws OclParseException {
		// §9.3.35[B]: invalid->X = invalid.oclAsSet()->X
		// §11.3.3: invalid.oclAsSet() = invalid
		// invalid.oclIsUndefined() = true
		assertEquals(true, eval("invalid->oclIsUndefined()", self));
	}

	// ========================================================================
	// oclIsUndefined: Dot vs Arrow on normal values
	// ========================================================================

	@Test
	void true_dot_oclIsUndefined_isFalse() throws OclParseException {
		assertEquals(false, eval("true.oclIsUndefined()", self));
	}

	@Test
	void true_arrow_oclIsUndefined_isFalse() throws OclParseException {
		// true->oclIsUndefined() = Set{true}.oclIsUndefined() = false
		assertEquals(false, eval("true->oclIsUndefined()", self));
	}

	// ========================================================================
	// oclIsInvalid: Dot vs Arrow on null
	// ========================================================================

	@Test
	void null_dot_oclIsInvalid_isFalse() throws OclParseException {
		// §11.3.2: oclIsInvalid is an exception operation on OclVoid → false
		assertEquals(false, eval("null.oclIsInvalid()", self));
	}

	@Test
	void null_arrow_oclIsInvalid_isFalse() throws OclParseException {
		// null->oclIsInvalid() = Set{}.oclIsInvalid() = false
		assertEquals(false, eval("null->oclIsInvalid()", self));
	}

	// ========================================================================
	// oclIsInvalid: Dot vs Arrow on invalid
	// ========================================================================

	@Test
	void invalid_dot_oclIsInvalid_isTrue() throws OclParseException {
		assertEquals(true, eval("invalid.oclIsInvalid()", self));
	}

	@Test
	void invalid_arrow_oclIsInvalid_isTrue() throws OclParseException {
		// invalid->oclIsInvalid() = invalid.oclAsSet()->oclIsInvalid()
		// §11.3.3: invalid.oclAsSet() = invalid → invalid.oclIsInvalid() = true
		assertEquals(true, eval("invalid->oclIsInvalid()", self));
	}

	// ========================================================================
	// Arrow implicit oclAsSet: null->select, null->isEmpty (§11.2.3)
	// Eclipse: GenericEvaluationOclAnyOperationTest#test_oclAsSet_implicit
	// ========================================================================

	@Test
	void null_arrow_select_true_returnsEmptySet() throws OclParseException {
		// null->select(true) = Set{}->select(true) = Set{}
		assertEquals(0, eval("null->select(true)->size()", self));
	}

	@Test
	void null_arrow_isEmpty_isTrue() throws OclParseException {
		// null->isEmpty() = Set{}->isEmpty() = true
		assertEquals(true, eval("null->isEmpty()", self));
	}

	@Test
	void null_arrow_notEmpty_isFalse() throws OclParseException {
		// null->notEmpty() = Set{}->notEmpty() = false
		assertEquals(false, eval("null->notEmpty()", self));
	}

	@Test
	void null_arrow_size_isZero() throws OclParseException {
		// null->size() = Set{}->size() = 0
		assertEquals(0, eval("null->size()", self));
	}

	// ========================================================================
	// Arrow implicit oclAsSet: invalid->X propagates invalid (§11.2.4)
	// ========================================================================

	@Test
	void invalid_arrow_select_isInvalid() throws OclParseException {
		// invalid->select(true) = invalid.oclAsSet()->select(true)
		// §11.3.3: invalid.oclAsSet() = invalid → propagates
		assertInvalid("invalid->select(true)", self);
	}

	@Test
	void invalid_arrow_isEmpty_isInvalid() throws OclParseException {
		// invalid->isEmpty() = invalid
		assertInvalid("invalid->isEmpty()", self);
	}

	// ========================================================================
	// Arrow on normal values: implicit Set wrapping (§11.3.1)
	// ========================================================================

	@Test
	void true_arrow_select_true_returnsSetTrue() throws OclParseException {
		// true->select(true) = Set{true}->select(true) = Set{true}
		assertEquals(1, eval("true->select(true)->size()", self));
	}

	@Test
	void string_dot_size_returnsLength() throws OclParseException {
		// '1234'.size() = 4 (String operation)
		assertEquals(4, eval("'1234'.size()", self));
	}

	@Test
	void string_arrow_size_returnsOne() throws OclParseException {
		// '1234'->size() = Set{'1234'}->size() = 1 (Collection operation)
		assertEquals(1, eval("'1234'->size()", self));
	}

	// ========================================================================
	// Set{null} vs null (they are different!)
	// ========================================================================

	@Test
	void setNull_arrow_select_true_containsNull() throws OclParseException {
		// Set{null}->select(true) = Set{null} (one element: null)
		assertEquals(1, eval("Set{null}->select(true)->size()", self));
	}

	@Test
	void setEmpty_arrow_select_true_isEmpty() throws OclParseException {
		// Set{}->select(true) = Set{} (empty)
		assertEquals(0, eval("Set{}->select(true)->size()", self));
	}
}
