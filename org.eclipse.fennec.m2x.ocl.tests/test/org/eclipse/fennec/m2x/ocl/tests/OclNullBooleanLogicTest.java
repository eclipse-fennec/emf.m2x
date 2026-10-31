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

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.OclResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL three-valued boolean logic with null (OclVoid).
 *
 * <p>Per OCL v2.4 §11.5.4, a boolean operation whose result is not decided by a
 * short-circuit is invalid if either operand is invalid, otherwise null if either operand
 * is null (#313). Eclipse Pivot ({@code BooleanAndOperation} and siblings) answers the same.
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
		void true_and_null_isNull() throws OclParseException {
			// Spec §11.5.4 and(b): "Otherwise null if either self or b is null"
			assertNull(eval("true and null", self));
		}

		@Test
		void null_and_true_isNull() throws OclParseException {
			// Spec §11.5.4 and(b): "Otherwise null if either self or b is null"
			assertNull(eval("null and true", self));
		}

		@Test
		void null_and_null_isNull() throws OclParseException {
			// Spec §11.5.4 and(b): "Otherwise null if either self or b is null"
			assertNull(eval("null and null", self));
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
		void false_or_null_isNull() throws OclParseException {
			// Spec §11.5.4 or(b): "Otherwise null if either self or b is null"
			assertNull(eval("false or null", self));
		}

		@Test
		void null_or_false_isNull() throws OclParseException {
			// Spec §11.5.4 or(b): "Otherwise null if either self or b is null"
			assertNull(eval("null or false", self));
		}

		@Test
		void null_or_null_isNull() throws OclParseException {
			// Spec §11.5.4 or(b): "Otherwise null if either self or b is null"
			assertNull(eval("null or null", self));
		}
	}

	@Nested
	class NotWithNull {

		@Test
		void not_null_isNull() throws OclParseException {
			// Spec §11.5.4 not: "null if self is null"
			assertNull(eval("not null", self));
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
		void true_implies_null_isNull() throws OclParseException {
			// Spec §11.5.4 implies(b): "Otherwise null if either self or b is null"
			assertNull(eval("true implies null", self));
		}

		@Test
		void null_implies_true_isTrue() throws OclParseException {
			// Short-circuit: anything implies true = true
			assertEquals(true, eval("null implies true", self));
		}

		@Test
		void null_implies_false_isNull() throws OclParseException {
			// Spec §11.5.4 implies(b): "Otherwise null if either self or b is null"
			assertNull(eval("null implies false", self));
		}

		@Test
		void null_implies_null_isNull() throws OclParseException {
			// Spec §11.5.4 implies(b): "Otherwise null if either self or b is null"
			assertNull(eval("null implies null", self));
		}
	}

	@Nested
	class XorWithNull {

		@Test
		void true_xor_null_isNull() throws OclParseException {
			// Spec §11.5.4 xor(b): "null if either self or b is null"
			assertNull(eval("true xor null", self));
		}

		@Test
		void null_xor_true_isNull() throws OclParseException {
			// Spec §11.5.4 xor(b): "null if either self or b is null"
			assertNull(eval("null xor true", self));
		}

		@Test
		void false_xor_null_isNull() throws OclParseException {
			// Spec §11.5.4 xor(b): "null if either self or b is null"
			assertNull(eval("false xor null", self));
		}

		@Test
		void null_xor_false_isNull() throws OclParseException {
			// Spec §11.5.4 xor(b): "null if either self or b is null"
			assertNull(eval("null xor false", self));
		}

		@Test
		void null_xor_null_isNull() throws OclParseException {
			// Spec §11.5.4 xor(b): "null if either self or b is null"
			assertNull(eval("null xor null", self));
		}
	}

	@Nested
	class InvalidAndNonBoolean {

		@Test
		void invalid_winsOverNull() throws OclParseException {
			// §11.5.4: "Otherwise invalid if either self or b is invalid. Otherwise null …"
			assertInvalid("null and invalid", self);
			assertInvalid("invalid or null", self);
			assertInvalid("true implies invalid", self);
			assertInvalid("null xor invalid", self);
		}

		@Test
		void nonBooleanOperand_isATypeError() {
			// §11.5.4: and takes a Boolean — known not to be one, a parse error (#378)
			assertTypeError("true and 'x'", self, "No operation Boolean::and(String)");
		}

		@Test
		void nonBooleanOperand_atEvaluation_isInvalidWithError() throws OclParseException {
			// null has no Boolean operations to check against, and v is typed only at evaluation
			assertInvalid("null or 1", self);
			assertSame(OclInvalid.INSTANCE, evalWith("true and v", self, Map.of("v", "x")));
			OclResult result = evalWithDiagnosticsWith("true and v", self, Map.of("v", "x"));
			assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains("must be Boolean")),
					() -> "" + result.diagnostics());
		}
	}
}
