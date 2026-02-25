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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.Collection;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@code oclAsSet()} semantics on invalid, null, plain values, and collections.
 *
 * <p>Spec: OCL v2.4:
 * <ul>
 *   <li>§11.3.1 OclAny: {@code oclAsSet() : Set(T)} — "Returns a Set with the object as
 *       the sole content"</li>
 *   <li>§11.3.2 OclVoid: {@code null.oclAsSet()} → {@code Set{}} (empty Set)</li>
 *   <li>§11.3.3 OclInvalid: {@code invalid.oclAsSet()} → {@code invalid}</li>
 * </ul>
 *
 * <p>Eclipse reference: {@code GenericEvaluationOclAnyOperationTest#test_oclAsSet_explicit()},
 * {@code test_oclAsSet_implicit()}, {@code EvaluateOclAnyOperationsTest4#test_oclAsSet_explicit()}.
 */
class OclAsSetSemanticsTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// ========================================================================
	// §11.3.3: invalid.oclAsSet() → invalid
	// ========================================================================

	@Nested
	class InvalidOclAsSet {

		@Test
		void invalid_oclAsSet_isInvalid() throws OclParseException {
			assertInvalid("invalid.oclAsSet()", self);
		}

		@Test
		void invalid_oclAsSet_oclIsInvalid() throws OclParseException {
			assertEquals(true, eval("invalid.oclAsSet().oclIsInvalid()", self));
		}
	}

	// ========================================================================
	// §11.3.2: null.oclAsSet() → Set{} (empty)
	// ========================================================================

	@Nested
	class NullOclAsSet {

		@Test
		void null_oclAsSet_isEmpty() throws OclParseException {
			assertEquals(true, eval("null.oclAsSet()->isEmpty()", self));
		}

		@Test
		void null_oclAsSet_size() throws OclParseException {
			assertEquals(0, eval("null.oclAsSet()->size()", self));
		}

		@Test
		void null_oclAsSet_returnsSet() throws OclParseException {
			Object result = eval("null.oclAsSet()", self);
			assertInstanceOf(OclSet.class, result);
			assertEquals(0, ((Collection<?>) result).size());
		}
	}

	// ========================================================================
	// §11.3.1: value.oclAsSet() → Set{value}
	// ========================================================================

	@Nested
	class ValueOclAsSet {

		@Test
		void integer_oclAsSet() throws OclParseException {
			Object result = eval("42.oclAsSet()", self);
			assertInstanceOf(OclSet.class, result);
			assertEquals(1, ((Collection<?>) result).size());
		}

		@Test
		void integer_oclAsSet_includes() throws OclParseException {
			assertEquals(true, eval("42.oclAsSet()->includes(42)", self));
		}

		@Test
		void string_oclAsSet() throws OclParseException {
			Object result = eval("'hello'.oclAsSet()", self);
			assertInstanceOf(OclSet.class, result);
			assertEquals(1, ((Collection<?>) result).size());
		}

		@Test
		void boolean_oclAsSet() throws OclParseException {
			assertEquals(true, eval("true.oclAsSet()->includes(true)", self));
		}

		@Test
		void eobject_oclAsSet() throws OclParseException {
			Object result = eval("self.oclAsSet()", self);
			assertInstanceOf(OclSet.class, result);
			assertEquals(1, ((Collection<?>) result).size());
		}

		@Test
		void eobject_oclAsSet_includes() throws OclParseException {
			// self.oclAsSet()->includes(self) = true
			assertEquals(true, eval("self.oclAsSet()->includes(self)", self));
		}
	}

	// ========================================================================
	// Collection.oclAsSet() — Eclipse: wraps Collection as element
	// Set{1,2,3}.oclAsSet() → Set{Set{1,2,3}} (not flat copy!)
	// ========================================================================

	@Nested
	class CollectionOclAsSet {

		@Test
		void set_oclAsSet_wrapsAsElement() throws OclParseException {
			// Eclipse: Set{1..4}->oclAsSet() → Set{Set{1..4}} — size 1
			// The original Set becomes a single element of the outer Set
			Object result = eval("Set{1, 2, 3}->oclAsSet()", self);
			assertInstanceOf(Set.class, result);
			assertEquals(1, ((Collection<?>) result).size());
		}

		@Test
		void sequence_oclAsSet_wrapsAsElement() throws OclParseException {
			// Sequence{1,2,3}->oclAsSet() → Set{Sequence{1,2,3}} — size 1
			Object result = eval("Sequence{1, 2, 3}->oclAsSet()", self);
			assertInstanceOf(Set.class, result);
			assertEquals(1, ((Collection<?>) result).size());
		}

		@Test
		void bag_oclAsSet_wrapsAsElement() throws OclParseException {
			Object result = eval("Bag{1, 2, 3}->oclAsSet()", self);
			assertInstanceOf(Set.class, result);
			assertEquals(1, ((Collection<?>) result).size());
		}

		@Test
		void emptySet_oclAsSet_wrapsAsElement() throws OclParseException {
			// Set{}->oclAsSet() → Set{Set{}} — size 1
			Object result = eval("Set{}->oclAsSet()", self);
			assertInstanceOf(Set.class, result);
			assertEquals(1, ((Collection<?>) result).size());
		}
	}

	// ========================================================================
	// Implicit oclAsSet via arrow (->)
	// §9.3.35[B]: non-collection -> op ≡ value.oclAsSet() -> op
	// ========================================================================

	@Nested
	class ImplicitOclAsSet {

		@Test
		void true_arrow_select() throws OclParseException {
			// true->select(true) → Set{true}
			Object result = eval("true->select(true)", self);
			assertInstanceOf(Set.class, result);
			assertEquals(1, ((Collection<?>) result).size());
		}

		@Test
		void null_arrow_select() throws OclParseException {
			// null->select(true) → Set{} (null.oclAsSet()=Set{}, select keeps nothing)
			Object result = eval("null->select(true)", self);
			assertInstanceOf(Set.class, result);
			assertEquals(0, ((Collection<?>) result).size());
		}

		@Test
		void null_arrow_isEmpty() throws OclParseException {
			assertEquals(true, eval("null->isEmpty()", self));
		}

		@Test
		void null_arrow_size() throws OclParseException {
			assertEquals(0, eval("null->size()", self));
		}

		@Test
		void invalid_arrow_select() throws OclParseException {
			// invalid->select(true) → invalid (invalid.oclAsSet()=invalid → propagates)
			assertInvalid("invalid->select(true)", self);
		}

		@Test
		void invalid_arrow_isEmpty() throws OclParseException {
			// invalid->isEmpty() → invalid
			assertInvalid("invalid->isEmpty()", self);
		}

		@Test
		void integer_arrow_size() throws OclParseException {
			// 42->size() → 1 (42.oclAsSet()=Set{42})
			assertEquals(1, eval("42->size()", self));
		}

		@Test
		void integer_arrow_includes() throws OclParseException {
			assertEquals(true, eval("42->includes(42)", self));
		}

		@Test
		void integer_arrow_excludes() throws OclParseException {
			assertEquals(true, eval("42->excludes(99)", self));
		}
	}
}
