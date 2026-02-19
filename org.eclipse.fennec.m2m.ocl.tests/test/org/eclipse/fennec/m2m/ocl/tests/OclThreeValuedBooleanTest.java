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
 * Tests for OCL three-valued boolean logic per OCL v2.4 §11.3.1.
 *
 * <p>OCL defines three-valued logic where {@code invalid} acts as
 * "unknown" in boolean operations. Key short-circuit rules:
 * <ul>
 *   <li>{@code false and invalid = false}</li>
 *   <li>{@code invalid and false = false}</li>
 *   <li>{@code true or invalid = true}</li>
 *   <li>{@code invalid or true = true}</li>
 *   <li>{@code false implies invalid = true}</li>
 *   <li>{@code invalid implies true = true}</li>
 *   <li>{@code not invalid = invalid}</li>
 * </ul>
 */
class OclThreeValuedBooleanTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// === AND with invalid (§11.3.1) ===

	@Test
	void and_trueAndInvalid_isInvalid() throws OclParseException {
		// true and invalid = invalid
		assertInvalid("true and invalid", self);
	}

	@Test
	void and_invalidAndTrue_isInvalid() throws OclParseException {
		// invalid and true = invalid
		assertInvalid("invalid and true", self);
	}

	@Test
	void and_falseAndInvalid_isFalse() throws OclParseException {
		// false and invalid = false — short-circuit!
		assertEquals(false, eval("false and invalid", self));
	}

	@Test
	void and_invalidAndFalse_isFalse() throws OclParseException {
		// invalid and false = false — short-circuit!
		assertEquals(false, eval("invalid and false", self));
	}

	@Test
	void and_invalidAndInvalid_isInvalid() throws OclParseException {
		assertInvalid("invalid and invalid", self);
	}

	// === OR with invalid (§11.3.1) ===

	@Test
	void or_trueOrInvalid_isTrue() throws OclParseException {
		// true or invalid = true — short-circuit!
		assertEquals(true, eval("true or invalid", self));
	}

	@Test
	void or_invalidOrTrue_isTrue() throws OclParseException {
		// invalid or true = true — short-circuit!
		assertEquals(true, eval("invalid or true", self));
	}

	@Test
	void or_falseOrInvalid_isInvalid() throws OclParseException {
		// false or invalid = invalid
		assertInvalid("false or invalid", self);
	}

	@Test
	void or_invalidOrFalse_isInvalid() throws OclParseException {
		// invalid or false = invalid
		assertInvalid("invalid or false", self);
	}

	@Test
	void or_invalidOrInvalid_isInvalid() throws OclParseException {
		assertInvalid("invalid or invalid", self);
	}

	// === NOT with invalid (§11.3.1) ===

	@Test
	void not_invalid_isInvalid() throws OclParseException {
		assertInvalid("not invalid", self);
	}

	// === XOR with invalid (§11.3.1) ===
	// xor has no short-circuit — all combinations with invalid yield invalid

	@Test
	void xor_trueXorInvalid_isInvalid() throws OclParseException {
		assertInvalid("true xor invalid", self);
	}

	@Test
	void xor_invalidXorTrue_isInvalid() throws OclParseException {
		assertInvalid("invalid xor true", self);
	}

	@Test
	void xor_falseXorInvalid_isInvalid() throws OclParseException {
		assertInvalid("false xor invalid", self);
	}

	@Test
	void xor_invalidXorFalse_isInvalid() throws OclParseException {
		assertInvalid("invalid xor false", self);
	}

	@Test
	void xor_invalidXorInvalid_isInvalid() throws OclParseException {
		assertInvalid("invalid xor invalid", self);
	}

	// === IMPLIES with invalid (§11.3.1) ===

	@Test
	void implies_trueImpliesInvalid_isInvalid() throws OclParseException {
		// true implies invalid = invalid
		assertInvalid("true implies invalid", self);
	}

	@Test
	void implies_falseImpliesInvalid_isTrue() throws OclParseException {
		// false implies invalid = true — short-circuit!
		assertEquals(true, eval("false implies invalid", self));
	}

	@Test
	void implies_invalidImpliesTrue_isTrue() throws OclParseException {
		// invalid implies true = true — short-circuit!
		assertEquals(true, eval("invalid implies true", self));
	}

	@Test
	void implies_invalidImpliesFalse_isInvalid() throws OclParseException {
		// invalid implies false = invalid
		assertInvalid("invalid implies false", self);
	}

	@Test
	void implies_invalidImpliesInvalid_isInvalid() throws OclParseException {
		assertInvalid("invalid implies invalid", self);
	}

	// === Three-valued logic with expressions producing invalid ===

	@Test
	void and_falseAndDivByZero_isFalse() throws OclParseException {
		// false and (1/0 > 0) = false — using actual invalid expression
		assertEquals(false, eval("false and (1 / 0 > 0)", self));
	}

	@Test
	void or_trueOrDivByZero_isTrue() throws OclParseException {
		// true or (1/0 > 0) = true
		assertEquals(true, eval("true or (1 / 0 > 0)", self));
	}

	@Test
	void implies_falseImpliesDivByZero_isTrue() throws OclParseException {
		// false implies (1/0 > 0) = true
		assertEquals(true, eval("false implies (1 / 0 > 0)", self));
	}

	// === Three-valued logic with model expressions ===

	@Test
	void and_falsePropertyAndInvalid() throws OclParseException {
		// self.age > 100 is false, so false and invalid = false
		assertEquals(false, eval("self.age > 100 and invalid", self));
	}

	@Test
	void or_truePropertyOrInvalid() throws OclParseException {
		// self.age > 0 is true, so true or invalid = true
		assertEquals(true, eval("self.age > 0 or invalid", self));
	}

	@Test
	void implies_falsePropertyImpliesInvalid() throws OclParseException {
		// self.age > 100 is false, so false implies invalid = true
		assertEquals(true, eval("self.age > 100 implies invalid", self));
	}
}
