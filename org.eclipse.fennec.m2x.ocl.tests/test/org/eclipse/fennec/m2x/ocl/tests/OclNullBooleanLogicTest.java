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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL three-valued boolean logic with null (OclVoid).
 *
 * <p>Per OCL v2.4 §11.5.4, null in boolean operations should return null
 * (not invalid) when the result is indeterminate. Eclipse OCL treats null
 * identically to invalid in boolean contexts (D28 Eclipse compatibility).
 *
 * <p>Tests marked {@code @Tag("spec-deviation")} document cases where
 * the Spec says null but Eclipse/Fennec return invalid.
 *
 * <p>Companion to {@link OclThreeValuedBooleanTest} which covers
 * the invalid cases. This class covers null.
 */
class OclNullBooleanLogicTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	@Nested
	class AndWithNull {

		@Test
		void false_and_null_isFalse() throws OclParseException {
			// Short-circuit: false and anything = false
			assertEquals(false, eval("false and null", self));
		}

		@Test
		void null_and_false_isFalse() throws OclParseException {
			assertEquals(false, eval("null and false", self));
		}

		@Test
		@Tag("spec-deviation") // Spec §11.5.4: result = null. Eclipse/Fennec: invalid.
		void true_and_null_isInvalid() throws OclParseException {
			// Spec §11.5.4 and(b): "Otherwise null if either self or b is null"
			// → true and null = null. Eclipse returns invalid. D28.
			assertInvalid("true and null", self);
		}

		@Test
		@Tag("spec-deviation") // Spec §11.5.4: result = null. Eclipse/Fennec: invalid.
		void null_and_true_isInvalid() throws OclParseException {
			// Spec §11.5.4 and(b): "Otherwise null if either self or b is null"
			// → null and true = null. Eclipse returns invalid. D28.
			assertInvalid("null and true", self);
		}

		@Test
		@Tag("spec-deviation") // Spec §11.5.4: result = null. Eclipse/Fennec: invalid.
		void null_and_null_isInvalid() throws OclParseException {
			// Spec §11.5.4 and(b): "Otherwise null if either self or b is null"
			// → null and null = null. Eclipse returns invalid. D28.
			assertInvalid("null and null", self);
		}
	}

	@Nested
	class OrWithNull {

		@Test
		void true_or_null_isTrue() throws OclParseException {
			// Short-circuit: true or anything = true
			assertEquals(true, eval("true or null", self));
		}

		@Test
		void null_or_true_isTrue() throws OclParseException {
			assertEquals(true, eval("null or true", self));
		}

		@Test
		@Tag("spec-deviation") // Spec §11.5.4: result = null. Eclipse/Fennec: invalid.
		void false_or_null_isInvalid() throws OclParseException {
			// Spec §11.5.4 or(b): "Otherwise null if either self or b is null"
			// → false or null = null. Eclipse returns invalid. D28.
			assertInvalid("false or null", self);
		}

		@Test
		@Tag("spec-deviation") // Spec §11.5.4: result = null. Eclipse/Fennec: invalid.
		void null_or_false_isInvalid() throws OclParseException {
			// Spec §11.5.4 or(b): "Otherwise null if either self or b is null"
			// → null or false = null. Eclipse returns invalid. D28.
			assertInvalid("null or false", self);
		}

		@Test
		@Tag("spec-deviation") // Spec §11.5.4: result = null. Eclipse/Fennec: invalid.
		void null_or_null_isInvalid() throws OclParseException {
			// Spec §11.5.4 or(b): "Otherwise null if either self or b is null"
			// → null or null = null. Eclipse returns invalid. D28.
			assertInvalid("null or null", self);
		}
	}

	@Nested
	class NotWithNull {

		@Test
		@Tag("spec-deviation") // Spec §11.5.4: result = null. Eclipse/Fennec: invalid.
		void not_null_isInvalid() throws OclParseException {
			// Spec §11.5.4 not: "null if self is null"
			// → not null = null. Eclipse returns invalid. D28.
			assertInvalid("not null", self);
		}
	}

	@Nested
	class ImpliesWithNull {

		@Test
		void false_implies_null_isTrue() throws OclParseException {
			// Short-circuit: false implies anything = true
			assertEquals(true, eval("false implies null", self));
		}

		@Test
		@Tag("spec-deviation") // Spec §11.5.4: result = null. Eclipse/Fennec: invalid.
		void true_implies_null_isInvalid() throws OclParseException {
			// Spec §11.5.4 implies(b): "Otherwise null if either self or b is null"
			// → true implies null = null. Eclipse returns invalid. D28.
			assertInvalid("true implies null", self);
		}

		@Test
		void null_implies_true_isTrue() throws OclParseException {
			// Short-circuit: anything implies true = true
			assertEquals(true, eval("null implies true", self));
		}

		@Test
		@Tag("spec-deviation") // Spec §11.5.4: result = null. Eclipse/Fennec: invalid.
		void null_implies_false_isInvalid() throws OclParseException {
			// Spec §11.5.4 implies(b): "Otherwise null if either self or b is null"
			// → null implies false = null. Eclipse returns invalid. D28.
			assertInvalid("null implies false", self);
		}

		@Test
		@Tag("spec-deviation") // Spec §11.5.4: result = null. Eclipse/Fennec: invalid.
		void null_implies_null_isInvalid() throws OclParseException {
			// Spec §11.5.4 implies(b): "Otherwise null if either self or b is null"
			// → null implies null = null. Eclipse returns invalid. D28.
			assertInvalid("null implies null", self);
		}
	}

	@Nested
	class XorWithNull {

		@Test
		@Tag("spec-deviation") // Spec §11.5.4: result = null. Eclipse/Fennec: invalid.
		void true_xor_null_isInvalid() throws OclParseException {
			// Spec §11.5.4 xor(b): "null if either self or b is null"
			// → true xor null = null. Eclipse returns invalid. D28.
			assertInvalid("true xor null", self);
		}

		@Test
		@Tag("spec-deviation") // Spec §11.5.4: result = null. Eclipse/Fennec: invalid.
		void null_xor_true_isInvalid() throws OclParseException {
			// Spec §11.5.4 xor(b): "null if either self or b is null"
			// → null xor true = null. Eclipse returns invalid. D28.
			assertInvalid("null xor true", self);
		}

		@Test
		@Tag("spec-deviation") // Spec §11.5.4: result = null. Eclipse/Fennec: invalid.
		void false_xor_null_isInvalid() throws OclParseException {
			// Spec §11.5.4 xor(b): "null if either self or b is null"
			// → false xor null = null. Eclipse returns invalid. D28.
			assertInvalid("false xor null", self);
		}

		@Test
		@Tag("spec-deviation") // Spec §11.5.4: result = null. Eclipse/Fennec: invalid.
		void null_xor_false_isInvalid() throws OclParseException {
			// Spec §11.5.4 xor(b): "null if either self or b is null"
			// → null xor false = null. Eclipse returns invalid. D28.
			assertInvalid("null xor false", self);
		}

		@Test
		@Tag("spec-deviation") // Spec §11.5.4: result = null. Eclipse/Fennec: invalid.
		void null_xor_null_isInvalid() throws OclParseException {
			// Spec §11.5.4 xor(b): "null if either self or b is null"
			// → null xor null = null. Eclipse returns invalid. D28.
			assertInvalid("null xor null", self);
		}
	}
}
