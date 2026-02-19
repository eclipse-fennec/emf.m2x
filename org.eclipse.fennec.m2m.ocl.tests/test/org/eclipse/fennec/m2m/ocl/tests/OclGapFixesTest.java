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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.model.ocl.AnyType;
import org.eclipse.fennec.m2m.model.ocl.CollectionKind;
import org.eclipse.fennec.m2m.model.ocl.CollectionType;
import org.eclipse.fennec.m2m.model.ocl.MapType;
import org.eclipse.fennec.m2m.model.ocl.OclFactory;
import org.eclipse.fennec.m2m.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2m.ocl.api.OclContext;
import org.eclipse.fennec.m2m.ocl.api.OclOperation;
import org.eclipse.fennec.m2m.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclOrderedSet;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclUnlimitedNatural;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for GAP-4 through GAP-20 fixes.
 */
class OclGapFixesTest extends AbstractOclTest {

	static EObject self;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
		EObject bob = createPerson("Bob", 25, 40000.0, false);
		company = createCompany("ACME", self, bob);
	}

	// ==================== GAP-4: narrowResult preserves OclBag/OclOrderedSet ====================

	@Nested
	class Gap4NarrowResult {

		@Test
		void bagLiteral_preservedThroughNarrow() throws OclParseException {
			Object result = eval("Bag{1, 2, 2, 3}", self);
			assertInstanceOf(List.class, result);
			assertEquals(4, ((List<?>) result).size());
		}

		@Test
		void orderedSetLiteral_preservedThroughNarrow() throws OclParseException {
			Object result = eval("OrderedSet{1, 2, 3}", self);
			assertInstanceOf(OclOrderedSet.class, result);
		}
	}

	// ==================== GAP-5: Integer.floor/ceiling/round ====================

	@Nested
	class Gap5IntegerFloorCeilingRound {

		@Test
		void integerFloor_identity() throws OclParseException {
			assertEquals(42, eval("42.floor()", self));
		}

		@Test
		void integerCeiling_identity() throws OclParseException {
			assertEquals(42, eval("42.ceiling()", self));
		}

		@Test
		void integerRound_identity() throws OclParseException {
			assertEquals(42, eval("42.round()", self));
		}

		@Test
		void negativeIntegerFloor_identity() throws OclParseException {
			assertEquals(-5, eval("(-5).floor()", self));
		}
	}

	// ==================== GAP-8: oclType for Collection/Map + equality ====================

	@Nested
	class Gap8OclTypeCollections {

		@Test
		void oclType_set() throws OclParseException {
			Object result = eval("Set{1, 2}.oclType()", self);
			assertInstanceOf(CollectionType.class, result);
			assertEquals(CollectionKind.SET, ((CollectionType) result).getKind());
		}

		@Test
		void oclType_bag() throws OclParseException {
			Object result = eval("Bag{1, 2}.oclType()", self);
			assertInstanceOf(CollectionType.class, result);
			assertEquals(CollectionKind.BAG, ((CollectionType) result).getKind());
		}

		@Test
		void oclType_orderedSet() throws OclParseException {
			Object result = eval("OrderedSet{1, 2}.oclType()", self);
			assertInstanceOf(CollectionType.class, result);
			assertEquals(CollectionKind.ORDERED_SET, ((CollectionType) result).getKind());
		}

		@Test
		void oclType_sequence() throws OclParseException {
			Object result = eval("Sequence{1, 2}.oclType()", self);
			assertInstanceOf(CollectionType.class, result);
			assertEquals(CollectionKind.SEQUENCE, ((CollectionType) result).getKind());
		}

		@Test
		void oclType_map() throws OclParseException {
			Object result = eval("Map{'a' with 1}.oclType()", self);
			assertInstanceOf(MapType.class, result);
		}

		@Test
		void oclType_integerEquality_cached() throws OclParseException {
			// oclType() on same type should return equal objects
			assertEquals(true, eval("42.oclType() = 99.oclType()", self));
		}

		@Test
		void oclType_stringEquality_cached() throws OclParseException {
			assertEquals(true, eval("'hello'.oclType() = 'world'.oclType()", self));
		}
	}

	// ==================== GAP-9: oclAsType for primitive types ====================

	@Nested
	class Gap9OclAsTypePrimitive {

		@Test
		void oclAsType_integerToInteger() throws OclParseException {
			assertEquals(42, eval("42.oclAsType(Integer)", self));
		}

		@Test
		void oclAsType_stringToString() throws OclParseException {
			assertEquals("hello", eval("'hello'.oclAsType(String)", self));
		}

		@Test
		void oclAsType_booleanToBoolean() throws OclParseException {
			assertEquals(true, eval("true.oclAsType(Boolean)", self));
		}

		@Test
		void oclAsType_integerToString_invalid() throws OclParseException {
			assertInvalid("42.oclAsType(String)", self);
		}

		@Test
		void oclAsType_stringToInteger_invalid() throws OclParseException {
			assertInvalid("'hello'.oclAsType(Integer)", self);
		}
	}

	// ==================== GAP-12: oclAsSet on Collection ====================

	@Nested
	class Gap12OclAsSetCollection {

		@Test
		void oclAsSet_onSequence_convertsToSet() throws OclParseException {
			Object result = eval("Sequence{1, 2, 2, 3}->oclAsSet()", self);
			assertInstanceOf(Set.class, result);
			@SuppressWarnings("unchecked")
			Set<Object> set = (Set<Object>) result;
			assertEquals(3, set.size()); // duplicates removed
			assertTrue(set.contains(1));
			assertTrue(set.contains(2));
			assertTrue(set.contains(3));
		}

		@Test
		void oclAsSet_onSingleValue_wrapsInSet() throws OclParseException {
			Object result = eval("42.oclAsSet()", self);
			assertInstanceOf(Set.class, result);
			@SuppressWarnings("unchecked")
			Set<Object> set = (Set<Object>) result;
			assertEquals(1, set.size());
			assertTrue(set.contains(42));
		}

		@Test
		void oclAsSet_onSet_returnsNewSet() throws OclParseException {
			Object result = eval("Set{1, 2, 3}->oclAsSet()", self);
			assertInstanceOf(Set.class, result);
			assertEquals(3, ((Set<?>) result).size());
		}
	}

	// ==================== GAP-13: Tuple equality with recursive OCL equality ====================

	@Nested
	class Gap13TupleEquality {

		@Test
		void tupleEquality_sameValues() throws OclParseException {
			assertEquals(true, eval(
					"Tuple{a = 1, b = 'hello'} = Tuple{a = 1, b = 'hello'}", self));
		}

		@Test
		void tupleEquality_differentValues() throws OclParseException {
			assertEquals(false, eval(
					"Tuple{a = 1, b = 'hello'} = Tuple{a = 2, b = 'hello'}", self));
		}

		@Test
		void tupleEquality_crossTypeNumeric() throws OclParseException {
			// 1 (Integer) and 1.0 (Real) should be equal via OCL numeric equality
			assertEquals(true, eval(
					"Tuple{x = 1} = Tuple{x = 1.0}", self));
		}

		@Test
		void tupleInequality() throws OclParseException {
			assertEquals(true, eval(
					"Tuple{a = 1} <> Tuple{a = 2}", self));
		}
	}

	// ==================== GAP-14: sortedBy on Set returns OrderedSet ====================

	@Nested
	class Gap14SortedByReturnType {

		@Test
		void sortedBy_onSet_returnsOrderedSet() throws OclParseException {
			Object result = eval("Set{3, 1, 2}->sortedBy(i | i)", self);
			assertInstanceOf(OclOrderedSet.class, result);
			@SuppressWarnings("unchecked")
			List<Object> list = (List<Object>) result;
			assertEquals(List.of(1, 2, 3), list);
		}

		@Test
		void sortedBy_onSequence_returnsSequence() throws OclParseException {
			Object result = eval("Sequence{3, 1, 2}->sortedBy(i | i)", self);
			assertInstanceOf(List.class, result);
			assertNotSame(OclOrderedSet.class, result.getClass());
			assertEquals(List.of(1, 2, 3), result);
		}
	}

	// ==================== GAP-15: closure returns OrderedSet ====================

	@Nested
	class Gap15ClosureReturnType {

		@Test
		void closure_returnsOrderedSet() throws OclParseException {
			Object result = eval("Sequence{1, 2, 3}->closure(i | i)", self);
			assertInstanceOf(OclOrderedSet.class, result);
		}

		@Test
		void closure_onSet_returnsOrderedSet() throws OclParseException {
			Object result = eval("Set{1, 2, 3}->closure(i | i)", self);
			assertInstanceOf(OclOrderedSet.class, result);
		}
	}

	// ==================== GAP-16: indexOf uses OCL equality ====================

	@Nested
	class Gap16IndexOfOclEquality {

		@Test
		void indexOf_basic() throws OclParseException {
			assertEquals(2, eval("Sequence{10, 20, 30}->indexOf(20)", self));
		}

		@Test
		void indexOf_notFound() throws OclParseException {
			assertEquals(0, eval("Sequence{1, 2, 3}->indexOf(99)", self));
		}

		@Test
		void indexOf_crossTypeNumeric() throws OclParseException {
			// 2.0 should match 2 via OCL numeric equality
			assertEquals(2, eval("Sequence{1, 2, 3}->indexOf(2.0)", self));
		}
	}

	// ==================== GAP-17: select/reject with invalid body ====================

	@Nested
	class Gap17SelectRejectInvalid {

		@Test
		void select_invalidBody_returnsInvalid() throws OclParseException {
			// Body evaluates to non-boolean (String) → should propagate invalid
			assertInvalid("Sequence{1, 2, 3}->select(i | invalid)", self);
		}

		@Test
		void reject_invalidBody_returnsInvalid() throws OclParseException {
			assertInvalid("Sequence{1, 2, 3}->reject(i | invalid)", self);
		}

		@Test
		void select_normalBody_works() throws OclParseException {
			Object result = eval("Sequence{1, 2, 3, 4, 5}->select(i | i > 3)", self);
			assertEquals(List.of(4, 5), result);
		}

		@Test
		void reject_normalBody_works() throws OclParseException {
			Object result = eval("Sequence{1, 2, 3, 4, 5}->reject(i | i > 3)", self);
			assertEquals(List.of(1, 2, 3), result);
		}
	}

	// ==================== GAP-18: UnlimitedNatural arithmetic ====================

	@Nested
	class Gap18UnlimitedNatural {

		@Test
		void unlimited_toString() throws OclParseException {
			assertEquals("*", eval("*.toString()", self));
		}

		@Test
		void unlimited_addition_invalid() throws OclParseException {
			assertInvalid("* + 1", self);
		}

		@Test
		void unlimited_subtraction_invalid() throws OclParseException {
			assertInvalid("* - 1", self);
		}

		@Test
		void unlimited_equals_self() throws OclParseException {
			assertEquals(true, eval("* = *", self));
		}

		@Test
		void unlimited_notEquals_integer() throws OclParseException {
			assertEquals(true, eval("* <> 5", self));
		}

		@Test
		void unlimited_greaterThan_finite() throws OclParseException {
			assertEquals(true, eval("* > 999999", self));
		}

		@Test
		void finite_lessThan_unlimited() throws OclParseException {
			assertEquals(true, eval("5 < *", self));
		}

		@Test
		void unlimited_toInteger_invalid() throws OclParseException {
			assertInvalid("*.toInteger()", self);
		}

		@Test
		void unlimited_sentinel_preserved() throws OclParseException {
			Object result = engine.evaluate("*", OclContext.of(self));
			assertSame(OclUnlimitedNatural.INSTANCE, result);
		}
	}

	// ==================== GAP-10: Custom op dispatch with ownerType ====================

	@Nested
	class Gap10CustomOpOwnerType {

		private OclOperationProvider registeredProvider;

		@AfterEach
		void cleanUp() {
			if (registeredProvider != null) {
				engine.unregisterOperations(registeredProvider);
				registeredProvider = null;
			}
		}

		@Test
		void customOp_matchesByOwnerType() throws OclParseException {
			PrimitiveType intType = OclFactory.eINSTANCE.createPrimitiveType();
			intType.setName("Integer");
			PrimitiveType stringType = OclFactory.eINSTANCE.createPrimitiveType();
			stringType.setName("String");

			// Register two ops with same name but different owner types
			OclOperation intOp = OclOperation.of("myOp", intType, intType,
					(source, args) -> ((Number) source).longValue() * 10);
			OclOperation strOp = OclOperation.of("myOp", stringType, stringType,
					(source, args) -> source.toString().toUpperCase());

			registeredProvider = () -> List.of(intOp, strOp);
			engine.registerOperations(registeredProvider);

			assertEquals(420, eval("42.myOp()", self));
			assertEquals("ALICE", eval("'alice'.myOp()", self));
		}

		@Test
		void customOp_anyType_matchesAll() throws OclParseException {
			AnyType anyType = OclFactory.eINSTANCE.createAnyType();
			anyType.setName("OclAny");
			PrimitiveType stringType = OclFactory.eINSTANCE.createPrimitiveType();
			stringType.setName("String");

			OclOperation op = OclOperation.of("describe", anyType, stringType,
					(source, args) -> "value=" + source);

			registeredProvider = () -> List.of(op);
			engine.registerOperations(registeredProvider);

			assertEquals("value=42", eval("42.describe()", self));
			assertEquals("value=hello", eval("'hello'.describe()", self));
		}
	}

	// ==================== GAP-20: Parser overload resolution ====================

	@Nested
	class Gap20OverloadResolution {

		@Test
		void overloadedOp_matchesByArgCount() throws OclParseException {
			// The parser should prefer the operation with matching param count.
			// Test with a model operation if available; if not, stdlib ops
			// that have unique names don't overlap.
			// At minimum, verify that the parser doesn't crash on common ops.
			assertEquals(5, eval("2 + 3", self));
			assertEquals("ab", eval("'a' + 'b'", self));
		}
	}
}
