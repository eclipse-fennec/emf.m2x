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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclOrderedSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@code flatten()} collection operation.
 *
 * <p>Spec references:
 * <ul>
 *   <li>§11.7.1: {@code flatten() : Collection(T2)} — If the element type is a
 *       collection type, the result is a collection containing all the elements
 *       of all the <b>recursively flattened</b> elements of self.</li>
 *   <li>§11.7.2 Set: {@code flatten()} returns Set</li>
 *   <li>§11.7.3 OrderedSet: {@code flatten()} returns OrderedSet</li>
 *   <li>§11.7.4 Bag: {@code flatten()} returns Bag</li>
 *   <li>§11.7.5 Sequence: {@code flatten()} returns Sequence</li>
 * </ul>
 *
 * <p>Eclipse reference: {@code CollectionsTest#test_flatten()},
 * {@code test_flatten_recursive_217461()},
 * {@code GenericEvaluationCollectionOperationTest#testCollectionFlatten()}
 *
 * <p>⚠️ SPEC-FIRST: Tests are written against the spec. If they fail,
 * the implementation has a gap — fix the implementation, NOT the test.
 */
class OclCollectionFlattenTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// ========================================================================
	// Single-level flatten (basic)
	// Eclipse: CollectionsTest#test_flatten
	// ========================================================================

	@Nested
	class SingleLevel {

		@Test
		void sequence_ofSequences() throws OclParseException {
			Object result = eval(
					"Sequence{Sequence{1, 2}, Sequence{3, 4}}->flatten()", self);
			assertInstanceOf(List.class, result);
			assertEquals(List.of(1, 2, 3, 4), result);
		}

		@Test
		void set_ofSequences_removesDuplicates() throws OclParseException {
			// Set{Sequence{'a', 'b'}, Sequence{'b', 'c', 'd'}}->flatten()
			// = Set{'a', 'b', 'c', 'd'} (duplicates removed)
			Object result = eval(
					"Set{Sequence{'a', 'b'}, Sequence{'b', 'c', 'd'}}->flatten()", self);
			assertInstanceOf(Set.class, result);
			@SuppressWarnings("unchecked")
			Set<Object> set = (Set<Object>) result;
			assertEquals(4, set.size());
			assertTrue(set.contains("a"));
			assertTrue(set.contains("b"));
			assertTrue(set.contains("c"));
			assertTrue(set.contains("d"));
		}

		@Test
		void bag_ofBags_preservesDuplicates() throws OclParseException {
			// Bag{Bag{1, 2, 1}, Bag{2, 3}}->flatten()
			// = Bag{1, 2, 1, 2, 3} (duplicates preserved)
			Object result = eval(
					"Bag{Bag{1, 2, 1}, Bag{2, 3}}->flatten()", self);
			assertInstanceOf(List.class, result);
			assertEquals(5, ((List<?>) result).size());
		}

		@Test
		void sequence_ofOrderedSets() throws OclParseException {
			// Sequence{OrderedSet{'a', 'b', 'd'}, OrderedSet{'b', 'c', 'd'}}->flatten()
			// = Sequence{'a', 'b', 'd', 'b', 'c', 'd'} (order + duplicates preserved)
			Object result = eval(
					"Sequence{OrderedSet{'a', 'b', 'd'}, OrderedSet{'b', 'c', 'd'}}->flatten()", self);
			assertInstanceOf(List.class, result);
			assertEquals(List.of("a", "b", "d", "b", "c", "d"), result);
		}
	}

	// ========================================================================
	// Return type preservation (§11.7.2–§11.7.5)
	// ========================================================================

	@Nested
	class ReturnType {

		@Test
		void set_flatten_returnsSet() throws OclParseException {
			Object result = eval("Set{Set{1, 2}, Set{3}}->flatten()", self);
			assertInstanceOf(Set.class, result);
		}

		@Test
		void bag_flatten_returnsBag() throws OclParseException {
			Object result = eval("Bag{Bag{1, 2}, Bag{3}}->flatten()", self);
			assertEquals("OclBag", result.getClass().getSimpleName());
		}

		@Test
		void sequence_flatten_returnsSequence() throws OclParseException {
			Object result = eval("Sequence{Sequence{1, 2}, Sequence{3}}->flatten()", self);
			assertInstanceOf(List.class, result);
			assertEquals("ArrayList", result.getClass().getSimpleName());
		}

		@Test
		void orderedSet_flatten_returnsOrderedSet() throws OclParseException {
			Object result = eval("OrderedSet{OrderedSet{1, 2}, OrderedSet{3}}->flatten()", self);
			assertInstanceOf(OclOrderedSet.class, result);
		}
	}

	// ========================================================================
	// Not nested — identity operation (§11.7.1)
	// Eclipse: CollectionsTest#test_flatten_notNested
	// ========================================================================

	@Nested
	class NotNested {

		@Test
		void set_alreadyFlat() throws OclParseException {
			Object result = eval("Set{1, 2, 3}->flatten()", self);
			assertInstanceOf(Set.class, result);
			assertEquals(3, ((Set<?>) result).size());
		}

		@Test
		void sequence_alreadyFlat() throws OclParseException {
			assertEquals(List.of(1, 2, 3), eval("Sequence{1, 2, 3}->flatten()", self));
		}

		@Test
		void bag_alreadyFlat() throws OclParseException {
			Object result = eval("Bag{1, 2, 2, 3}->flatten()", self);
			assertEquals("OclBag", result.getClass().getSimpleName());
			assertEquals(4, ((List<?>) result).size());
		}

		@Test
		void orderedSet_alreadyFlat() throws OclParseException {
			Object result = eval("OrderedSet{1, 2, 3}->flatten()", self);
			assertInstanceOf(OclOrderedSet.class, result);
			assertEquals(3, ((List<?>) result).size());
		}
	}

	// ========================================================================
	// Empty collections
	// Eclipse: CollectionsTest#test_flatten_emptySource_195252,
	//          test_flatten_emptyChildren
	// ========================================================================

	@Nested
	class EmptyCollections {

		@Test
		void emptySet() throws OclParseException {
			Object result = eval("Set{}->flatten()", self);
			assertInstanceOf(Set.class, result);
			assertTrue(((Set<?>) result).isEmpty());
		}

		@Test
		void emptySequence() throws OclParseException {
			Object result = eval("Sequence{}->flatten()", self);
			assertInstanceOf(List.class, result);
			assertTrue(((List<?>) result).isEmpty());
		}

		@Test
		void emptyBag() throws OclParseException {
			Object result = eval("Bag{}->flatten()", self);
			assertEquals("OclBag", result.getClass().getSimpleName());
			assertTrue(((List<?>) result).isEmpty());
		}

		@Test
		void emptyOrderedSet() throws OclParseException {
			Object result = eval("OrderedSet{}->flatten()", self);
			assertInstanceOf(OclOrderedSet.class, result);
			assertTrue(((List<?>) result).isEmpty());
		}

		@Test
		void set_ofEmptySets() throws OclParseException {
			Object result = eval("Set{Set{}, Set{}}->flatten()", self);
			assertInstanceOf(Set.class, result);
			assertTrue(((Set<?>) result).isEmpty());
		}

		@Test
		void sequence_ofEmptySequences() throws OclParseException {
			Object result = eval("Sequence{Sequence{}, Sequence{}}->flatten()", self);
			assertInstanceOf(List.class, result);
			assertTrue(((List<?>) result).isEmpty());
		}

		@Test
		void sequence_withEmptyInner() throws OclParseException {
			Object result = eval(
					"Sequence{Sequence{1}, Sequence{}, Sequence{2}}->flatten()", self);
			assertEquals(List.of(1, 2), result);
		}
	}

	// ========================================================================
	// Recursive/deep flatten (§11.7.1: "recursively flattened")
	// Eclipse: CollectionsTest#test_flatten_recursive_217461
	// ========================================================================

	@Nested
	class RecursiveFlatten {

		@Test
		void set_threeLevel_crossType() throws OclParseException {
			// Set{Sequence{Set{'a'}, Set{'b'}}, Sequence{Set{'b', 'c'}, Set{'d'}}}->flatten()
			// Recursive: Set → Sequence → Set → leaf elements
			// Result: Set{'a', 'b', 'c', 'd'} (deduplicated)
			Object result = eval(
					"Set{Sequence{Set{'a'}, Set{'b'}}, Sequence{Set{'b', 'c'}, Set{'d'}}}->flatten()", self);
			assertInstanceOf(Set.class, result);
			@SuppressWarnings("unchecked")
			Set<Object> set = (Set<Object>) result;
			assertEquals(4, set.size());
			assertTrue(set.contains("a"));
			assertTrue(set.contains("b"));
			assertTrue(set.contains("c"));
			assertTrue(set.contains("d"));
		}

		@Test
		void sequence_threeLevel_crossType() throws OclParseException {
			// Sequence{OrderedSet{Sequence{'a', 'b'}, Sequence{'d'}},
			//          OrderedSet{Sequence{'b', 'c'}, Sequence{'d'}}}->flatten()
			// = Sequence{'a', 'b', 'd', 'b', 'c', 'd'}
			Object result = eval(
					"Sequence{OrderedSet{Sequence{'a', 'b'}, Sequence{'d'}}, "
					+ "OrderedSet{Sequence{'b', 'c'}, Sequence{'d'}}}->flatten()", self);
			assertInstanceOf(List.class, result);
			assertEquals(List.of("a", "b", "d", "b", "c", "d"), result);
		}

		@Test
		void bag_threeLevel_crossType() throws OclParseException {
			// Bag{Bag{Set{1, 2}, Set{3}}, Bag{Set{2, 4}}}->flatten()
			// Recursive: all leaf elements, Bag preserves duplicates
			Object result = eval(
					"Bag{Bag{Set{1, 2}, Set{3}}, Bag{Set{2, 4}}}->flatten()", self);
			assertEquals("OclBag", result.getClass().getSimpleName());
			@SuppressWarnings("unchecked")
			List<Object> bag = (List<Object>) result;
			// 1, 2, 3, 2, 4 — five elements (2 appears twice)
			assertEquals(5, bag.size());
		}

		@Test
		void emptyRecursive_set() throws OclParseException {
			// Set{Set{Sequence{}}, Set{Sequence{}, Sequence{}}}->flatten() = Set{}
			Object result = eval(
					"Set{Set{Sequence{}}, Set{Sequence{}, Sequence{}}}->flatten()", self);
			assertInstanceOf(Set.class, result);
			assertTrue(((Set<?>) result).isEmpty());
		}

		@Test
		void emptyRecursive_sequence() throws OclParseException {
			// Sequence{Sequence{Sequence{}, Sequence{}}, Sequence{Sequence{}, Sequence{}}}->flatten()
			// = Sequence{} (all empty at every level)
			Object result = eval(
					"Sequence{Sequence{Sequence{}, Sequence{}}, Sequence{Sequence{}, Sequence{}}}->flatten()", self);
			assertInstanceOf(List.class, result);
			assertTrue(((List<?>) result).isEmpty());
		}
	}

	// ========================================================================
	// Cross-type nesting (mixed collection types)
	// Eclipse: GenericEvaluationCollectionOperationTest#testCollectionFlatten
	// ========================================================================

	@Nested
	class CrossTypeNesting {

		@Test
		void sequence_ofMixedTypes() throws OclParseException {
			// Sequence{Set{1, 2, 3}, Sequence{4, 5}, Bag{6}}->flatten()
			// = Sequence with all elements
			Object result = eval(
					"Sequence{Set{1, 2, 3}, Sequence{4, 5}, Bag{6}}->flatten()", self);
			assertInstanceOf(List.class, result);
			assertEquals(6, ((List<?>) result).size());
		}

		@Test
		void set_ofMixedTypes_deduplicates() throws OclParseException {
			// Set{Bag{1, 2}, Sequence{2, 3}}->flatten()
			// = Set{1, 2, 3} (deduplicated)
			Object result = eval(
					"Set{Bag{1, 2}, Sequence{2, 3}}->flatten()", self);
			assertInstanceOf(Set.class, result);
			assertEquals(3, ((Set<?>) result).size());
		}
	}

	// ========================================================================
	// Flatten then chained operations
	// ========================================================================

	@Nested
	class ChainedOps {

		@Test
		void flatten_thenSize() throws OclParseException {
			assertEquals(4, eval(
					"Sequence{Sequence{1, 2}, Sequence{3, 4}}->flatten()->size()", self));
		}

		@Test
		void flatten_thenSum() throws OclParseException {
			assertEquals(10, eval(
					"Sequence{Sequence{1, 2}, Sequence{3, 4}}->flatten()->sum()", self));
		}

		@Test
		void flatten_thenIncludes() throws OclParseException {
			assertEquals(true, eval(
					"Sequence{Sequence{1, 2}, Sequence{3, 4}}->flatten()->includes(3)", self));
		}

		@Test
		void flatten_thenSelect() throws OclParseException {
			assertEquals(2, eval(
					"Sequence{Sequence{1, 2}, Sequence{3, 4}}->flatten()->select(x | x > 2)->size()", self));
		}

		@Test
		void flatten_thenForAll() throws OclParseException {
			assertEquals(true, eval(
					"Sequence{Sequence{1, 2}, Sequence{3, 4}}->flatten()->forAll(x | x > 0)", self));
		}
	}

	// ========================================================================
	// Invalid/null source
	// Eclipse: testCollectionFlattenInvalid, testCollectionFlattenNull
	// ========================================================================

	@Nested
	class InvalidNull {

		@Test
		void invalid_source_isInvalid() throws OclParseException {
			assertInvalid("invalid->flatten()", self);
		}

		@Test
		void null_arrow_flatten_emptySet() throws OclParseException {
			// §9.3.35[B]: null->flatten() = null.oclAsSet()->flatten() = Set{}->flatten() = Set{}
			Object result = eval("null->flatten()", self);
			assertInstanceOf(Collection.class, result);
			assertTrue(((Collection<?>) result).isEmpty());
		}
	}

	// ========================================================================
	// Strings
	// ========================================================================

	@Test
	void flatten_stringSequences() throws OclParseException {
		Object result = eval(
				"Sequence{Sequence{'a', 'b'}, Sequence{'c'}}->flatten()", self);
		assertEquals(List.of("a", "b", "c"), result);
	}
}
