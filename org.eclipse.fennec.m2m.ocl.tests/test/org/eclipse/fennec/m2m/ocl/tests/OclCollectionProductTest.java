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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL collection product, cross-collection operations,
 * and complex multi-collection patterns.
 */
class OclCollectionProductTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject charlie;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 60000.0, true);
		bob = createPerson("Bob", 25, 45000.0, false);
		charlie = createPerson("Charlie", 35, 80000.0, true);
		company = createCompany("ACME", alice, bob, charlie);
	}

	// --- Cross-collection queries ---

	@Test
	void twoCollections_forAll_includes() throws OclParseException {
		// All elements of first are in second
		assertEquals(true, eval(
				"Set{1, 2}->forAll(i | Set{1, 2, 3}->includes(i))", alice));
	}

	@Test
	void twoCollections_exists_intersection() throws OclParseException {
		// There exists an element in both collections
		assertEquals(true, eval(
				"Set{1, 2, 3}->exists(i | Set{3, 4, 5}->includes(i))", alice));
	}

	@Test
	void twoCollections_noIntersection() throws OclParseException {
		assertEquals(false, eval(
				"Set{1, 2}->exists(i | Set{3, 4}->includes(i))", alice));
	}

	// --- Combining results from multiple operations ---

	@Test
	void combineResults_union() throws OclParseException {
		// Select even + select odd = all
		assertEquals(5, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | i.mod(2) = 0)" +
				"->union(Sequence{1, 2, 3, 4, 5}->select(i | i.mod(2) = 1))->size()",
				alice));
	}

	@Test
	void combineResults_intersection() throws OclParseException {
		// Elements > 2 AND elements < 5
		assertEquals(2, eval(
				"Set{1, 2, 3, 4, 5}->select(i | i > 2)" +
				"->intersection(Set{1, 2, 3, 4, 5}->select(i | i < 5))->size()",
				alice));
	}

	// --- Nested collection expressions ---

	@Test
	void nestedCollectionLiteral_inIterator() throws OclParseException {
		// For each element, check against a literal collection
		assertEquals(2, eval(
				"Sequence{1, 2, 3, 4, 5}->select(i | Set{2, 4}->includes(i))->size()",
				alice));
	}

	@Test
	void nestedCollectionLiteral_inLet() throws OclParseException {
		assertEquals(true, eval(
				"let allowed: Set(Integer) = Set{1, 2, 3} in allowed->includes(2)",
				alice));
	}

	// --- Model queries combining collections ---

	@Test
	void model_selectAndCount() throws OclParseException {
		// Number of married employees
		assertEquals(2, eval(
				"self.employees->select(e | e.isMarried)->size()", company));
	}

	@Test
	void model_selectAndCollect() throws OclParseException {
		// Names of unmarried employees
		Object result = eval(
				"self.employees->select(e | not e.isMarried)->collect(e | e.name)",
				company);
		assertInstanceOf(Collection.class, result);
		Collection<?> names = (Collection<?>) result;
		assertEquals(1, names.size());
	}

	@Test
	void model_collectAndSort() throws OclParseException {
		// Sorted salaries
		Object result = eval(
				"self.employees->collect(e | e.salary)->sortedBy(s | s)",
				company);
		assertInstanceOf(List.class, result);
		List<?> salaries = (List<?>) result;
		assertEquals(45000.0, salaries.get(0));
		assertEquals(60000.0, salaries.get(1));
		assertEquals(80000.0, salaries.get(2));
	}

	@Test
	void model_collectAndMax() throws OclParseException {
		assertEquals(80000.0, eval(
				"self.employees->collect(e | e.salary)->max()", company));
	}

	@Test
	void model_collectAndMin() throws OclParseException {
		assertEquals(45000.0, eval(
				"self.employees->collect(e | e.salary)->min()", company));
	}

	// --- Using let with model collections ---

	@Test
	void model_let_withSelect() throws OclParseException {
		assertEquals(true, eval(
				"let married: Integer = self.employees->select(e | e.isMarried)->size() " +
				"in married > 0",
				company));
	}

	@Test
	void model_let_withCollect() throws OclParseException {
		Object result = eval(
				"let names: Sequence(String) = self.employees->collect(e | e.name) " +
				"in names->sortedBy(n | n)->first()",
				company);
		assertEquals("Alice", result);
	}

	// --- Collection operations chain ---

	@Test
	void chain_selectRejectSize() throws OclParseException {
		// Start with 1..10, select even, reject > 6
		assertEquals(3, eval(
				"Sequence{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}" +
				"->select(i | i.mod(2) = 0)" +       // {2,4,6,8,10}
				"->reject(i | i > 6)" +               // {2,4,6}
				"->size()",
				alice));
	}

	@Test
	void chain_collectSelectForAll() throws OclParseException {
		assertEquals(true, eval(
				"Sequence{1, 2, 3, 4, 5}" +
				"->collect(i | i * 2)" +              // {2,4,6,8,10}
				"->select(i | i > 5)" +               // {6,8,10}
				"->forAll(i | i.mod(2) = 0)",         // all even → true
				alice));
	}

	// --- Collection iteration with arithmetic ---

	@Test
	void iterate_factorial() throws OclParseException {
		// 5! = 120
		assertEquals(120, eval(
				"Sequence{1, 2, 3, 4, 5}->iterate(i; acc: Integer = 1 | acc * i)",
				alice));
	}

	@Test
	void iterate_maxManual() throws OclParseException {
		// Manual max via iterate
		assertEquals(5, eval(
				"Sequence{3, 1, 5, 2, 4}->iterate(i; acc: Integer = 0 | if i > acc then i else acc endif)",
				alice));
	}

	// --- product() tests (OCL v2.4 §11.7.1) ---

	@Test
	@SuppressWarnings("unchecked")
	void product_basic_cartesianProduct() throws OclParseException {
		// Set{1,2}->product(Set{3,4}) → 4 tuples
		Object result = eval("Set{1, 2}->product(Set{3, 4})", alice);
		assertInstanceOf(Set.class, result);
		Set<Map<String, Object>> tuples = (Set<Map<String, Object>>) result;
		assertEquals(4, tuples.size());
		// Verify tuple structure: each has 'first' and 'second'
		for (Map<String, Object> tuple : tuples) {
			assertTrue(tuple.containsKey("first"));
			assertTrue(tuple.containsKey("second"));
		}
	}

	@Test
	void product_size_isMultiplication() throws OclParseException {
		// |source| × |argument| = result size
		assertEquals(6, eval("Set{1, 2, 3}->product(Set{4, 5})->size()", alice));
	}

	@Test
	void product_tupleAccess_first() throws OclParseException {
		// Access .first on product tuples
		assertEquals(true, eval(
				"Set{1, 2}->product(Set{3})->forAll(t | t.first > 0)", alice));
	}

	@Test
	void product_tupleAccess_second() throws OclParseException {
		// Single-element product → access .second
		assertEquals(3, eval(
				"Set{1}->product(Set{3})->any(true).second", alice));
	}

	@Test
	void product_resultIsAlwaysSet_fromSequence() throws OclParseException {
		// Sequence->product() still yields a Set (§11.7.1)
		Object result = eval("Sequence{1, 2}->product(Sequence{3, 4})", alice);
		assertInstanceOf(Set.class, result);
	}

	@Test
	void product_resultIsAlwaysSet_fromBag() throws OclParseException {
		Object result = eval("Bag{1, 2}->product(Bag{3, 4})", alice);
		assertInstanceOf(Set.class, result);
	}

	@Test
	void product_resultIsAlwaysSet_fromOrderedSet() throws OclParseException {
		Object result = eval("OrderedSet{1, 2}->product(OrderedSet{3, 4})", alice);
		assertInstanceOf(Set.class, result);
	}

	@Test
	void product_emptySource() throws OclParseException {
		// Set{}->product(Set{1,2}) → Set{} (empty)
		Object result = eval("Set{}->product(Set{1, 2})->size()", alice);
		assertEquals(0, result);
	}

	@Test
	void product_emptyArgument() throws OclParseException {
		// Set{1,2}->product(Set{}) → Set{} (empty)
		assertEquals(0, eval("Set{1, 2}->product(Set{})->size()", alice));
	}

	@Test
	void product_bothEmpty() throws OclParseException {
		assertEquals(0, eval("Set{}->product(Set{})->size()", alice));
	}

	@Test
	void product_invalidSource() throws OclParseException {
		assertInvalid("invalid->product(Set{1})", alice);
	}

	@Test
	void product_invalidArgument() throws OclParseException {
		assertInvalid("let x: Set(Integer) = invalid in Set{1}->product(x)", alice);
	}

	@Test
	void product_invalidElementInSource() throws OclParseException {
		// Well-formedness: collection with invalid element → invalid
		assertInvalid("Set{invalid, 1}->product(Set{2})", alice);
	}

	@Test
	void product_nullSource_arrowCall() throws OclParseException {
		// null->product(Set{1}) → arrow-call converts null to Set{} → empty product
		assertEquals(0, eval("null->product(Set{1})->size()", alice));
	}

	@Test
	@SuppressWarnings("unchecked")
	void product_nullElementInSource() throws OclParseException {
		// Set{null, 1}->product(Set{2}) → tuples with null as 'first'
		Object result = eval("Set{null, 1}->product(Set{2})", alice);
		assertInstanceOf(Set.class, result);
		Set<Map<String, Object>> tuples = (Set<Map<String, Object>>) result;
		assertEquals(2, tuples.size());
		boolean hasNullFirst = tuples.stream().anyMatch(t -> t.get("first") == null);
		assertTrue(hasNullFirst, "Expected a tuple with null as 'first'");
	}

	@Test
	@SuppressWarnings("unchecked")
	void product_nullElementInArgument() throws OclParseException {
		// Set{1}->product(Set{null}) → tuple with null as 'second'
		Object result = eval("Set{1}->product(Set{null})", alice);
		assertInstanceOf(Set.class, result);
		Set<Map<String, Object>> tuples = (Set<Map<String, Object>>) result;
		assertEquals(1, tuples.size());
		Map<String, Object> tuple = tuples.iterator().next();
		assertEquals(1, tuple.get("first"));
		assertEquals(null, tuple.get("second"));
	}

	@Test
	void product_forAll_onTuples() throws OclParseException {
		assertEquals(true, eval(
				"Set{1, 2}->product(Set{3})->forAll(t | t.first > 0 and t.second = 3)",
				alice));
	}

	@Test
	void product_mixedTypes() throws OclParseException {
		// product of integers with strings
		assertEquals(2, eval(
				"Set{1}->product(Set{'a', 'b'})->size()", alice));
	}
}
