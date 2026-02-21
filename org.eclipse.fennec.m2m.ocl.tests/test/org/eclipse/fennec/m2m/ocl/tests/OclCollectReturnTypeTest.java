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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@code collect} and {@code collectNested} return types
 * (OCL v2.4 §11.9).
 *
 * <p>Spec references:
 * <ul>
 *   <li>§11.9.1: {@code collect = collectNested + flatten()}</li>
 *   <li>§11.9.2 Set: collectNested → <b>Bag</b></li>
 *   <li>§11.9.3 Bag: collectNested → <b>Bag</b></li>
 *   <li>§11.9.4 Sequence: collectNested → <b>Sequence</b></li>
 *   <li>§11.9.5 OrderedSet: collectNested → <b>Sequence</b></li>
 * </ul>
 *
 * <p>Eclipse OCL reference: {@code GenericIteratorsTest#test_collect_returns_490982}
 * and {@code test_collectNested_returns_490982}.
 *
 * <p>⚠️ SPEC-FIRST: Tests are written against the spec. If they fail,
 * the implementation has a gap — fix the implementation, NOT the test.
 */
class OclCollectReturnTypeTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// ========================================================================
	// collect() return type (§11.9: collect = collectNested + flatten)
	//   Set → Bag, Bag → Bag, Sequence → Sequence, OrderedSet → Sequence
	// ========================================================================

	@Test
	void collect_fromSet_returnsBag() throws OclParseException {
		// §11.9.2: Set->collectNested = Bag, flatten(Bag) = Bag → collect = Bag
		Object result = eval("Set{1, 2, 3}->collect(p | p * 2)", self);
		assertBag(result);
		assertEquals(3, ((Collection<?>) result).size());
	}

	@Test
	void collect_fromBag_returnsBag() throws OclParseException {
		// §11.9.3: Bag->collectNested = Bag, flatten(Bag) = Bag → collect = Bag
		Object result = eval("Bag{1, 2, 3}->collect(p | p * 2)", self);
		assertBag(result);
		assertEquals(3, ((Collection<?>) result).size());
	}

	@Test
	void collect_fromSequence_returnsSequence() throws OclParseException {
		// §11.9.4: Sequence->collectNested = Sequence → collect = Sequence
		Object result = eval("Sequence{1, 2, 3}->collect(p | p * 2)", self);
		assertSequence(result);
		assertEquals(3, ((Collection<?>) result).size());
	}

	@Test
	void collect_fromOrderedSet_returnsSequence() throws OclParseException {
		// §11.9.5: OrderedSet->collectNested = Sequence → collect = Sequence
		Object result = eval("OrderedSet{1, 2, 3}->collect(p | p * 2)", self);
		assertSequence(result);
		assertEquals(3, ((Collection<?>) result).size());
	}

	// ========================================================================
	// collectNested() return type (§11.9.2-§11.9.5)
	//   Set → Bag, Bag → Bag, Sequence → Sequence, OrderedSet → Sequence
	// ========================================================================

	@Test
	void collectNested_fromSet_returnsBag() throws OclParseException {
		// §11.9.2: Set->collectNested → Bag
		Object result = eval("Set{1, 2, 3}->collectNested(p | p * 2)", self);
		assertBag(result);
		assertEquals(3, ((Collection<?>) result).size());
	}

	@Test
	void collectNested_fromBag_returnsBag() throws OclParseException {
		// §11.9.3: Bag->collectNested → Bag
		Object result = eval("Bag{1, 2, 3}->collectNested(p | p * 2)", self);
		assertBag(result);
		assertEquals(3, ((Collection<?>) result).size());
	}

	@Test
	void collectNested_fromSequence_returnsSequence() throws OclParseException {
		// §11.9.4: Sequence->collectNested → Sequence
		Object result = eval("Sequence{1, 2, 3}->collectNested(p | p * 2)", self);
		assertSequence(result);
		assertEquals(3, ((Collection<?>) result).size());
	}

	@Test
	void collectNested_fromOrderedSet_returnsSequence() throws OclParseException {
		// §11.9.5: OrderedSet->collectNested → Sequence
		Object result = eval("OrderedSet{1, 2, 3}->collectNested(p | p * 2)", self);
		assertSequence(result);
		assertEquals(3, ((Collection<?>) result).size());
	}

	// ========================================================================
	// Eclipse reference: test_collect_returns_490982
	// Verifies exact content equality per source type
	// ========================================================================

	@Test
	void collect_fromBag_contentMatch() throws OclParseException {
		// Bag{1,2,3}->collect(p | (p.mod(2)) <> 0) = Bag{false, true, true}
		Object result = eval("Bag{1, 2, 3}->collect(p | (p.mod(2)) <> 0)", self);
		assertBag(result);
		@SuppressWarnings("unchecked")
		Collection<Boolean> coll = (Collection<Boolean>) result;
		assertEquals(3, coll.size());
		// contains false(1x) and true(2x)
		assertEquals(1, coll.stream().filter(b -> !b).count());
		assertEquals(2, coll.stream().filter(b -> b).count());
	}

	@Test
	void collect_fromSet_contentMatch() throws OclParseException {
		// Set{1,2,3}->collect(p | (p.mod(2)) <> 0) = Bag{...}
		Object result = eval("Set{1, 2, 3}->collect(p | (p.mod(2)) <> 0)", self);
		assertBag(result);
		assertEquals(3, ((Collection<?>) result).size());
	}

	@Test
	void collect_fromOrderedSet_contentMatch() throws OclParseException {
		// OrderedSet{1,2,3}->collect(p | (p.mod(2)) <> 0) = Sequence{true, false, true}
		Object result = eval("OrderedSet{1, 2, 3}->collect(p | (p.mod(2)) <> 0)", self);
		assertSequence(result);
		assertEquals(List.of(true, false, true), result);
	}

	@Test
	void collect_fromSequence_contentMatch() throws OclParseException {
		// Sequence{1,2,3}->collect(p | (p.mod(2)) <> 0) = Sequence{true, false, true}
		Object result = eval("Sequence{1, 2, 3}->collect(p | (p.mod(2)) <> 0)", self);
		assertSequence(result);
		assertEquals(List.of(true, false, true), result);
	}

	// ========================================================================
	// collectNested preserves nesting, collect flattens (§11.9.1)
	// ========================================================================

	@Test
	void collect_flattens_nestedCollections() throws OclParseException {
		// collect flattens one level: 3 * 2 = 6 elements
		assertEquals(6, eval(
				"Sequence{1, 2, 3}->collect(i | Sequence{i, i * 2})->size()", self));
	}

	@Test
	void collectNested_preserves_nestedCollections() throws OclParseException {
		// collectNested does NOT flatten: 3 nested sequences = size 3
		assertEquals(3, eval(
				"Sequence{1, 2, 3}->collectNested(i | Sequence{i, i * 2})->size()", self));
	}

	// ========================================================================
	// Ordered sources: at() works on Sequence results
	// ========================================================================

	@Test
	void collectNested_fromOrderedSet_supportsAt() throws OclParseException {
		// §11.9.5: OrderedSet->collectNested → Sequence, so at() should work
		assertEquals(false, eval(
				"OrderedSet{1, 2, 3}->collectNested(p | (p.mod(2)) <> 0)->at(2)", self));
	}

	@Test
	void collectNested_fromSequence_supportsAt() throws OclParseException {
		// §11.9.4: Sequence->collectNested → Sequence, so at() should work
		assertEquals(false, eval(
				"Sequence{1, 2, 3}->collectNested(p | (p.mod(2)) <> 0)->at(2)", self));
	}

	// ========================================================================
	// collect with implicit iterator (shorthand)
	// ========================================================================

	@Test
	void collect_shorthand_fromSet_returnsBag() throws OclParseException {
		Object result = eval("Set{1, 2, 3}->collect(true)", self);
		assertBag(result);
	}

	@Test
	void collect_shorthand_fromSequence_returnsSequence() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3}->collect(true)", self);
		assertSequence(result);
	}

	// ========================================================================
	// Helpers
	// ========================================================================

	/**
	 * Asserts that the result is a Bag (OclBag, not a plain List/Set).
	 * OclBag is a List subclass but NOT a Set, NOT an OrderedSet, and NOT a plain ArrayList.
	 */
	private static void assertBag(Object result) {
		assertInstanceOf(List.class, result, "Bag must be a List");
		// Must NOT be a Set
		assertTrue(!(result instanceof Set<?>), "Bag must not be a Set");
		// Must be OclBag (internal type), check via class name
		assertEquals("OclBag", result.getClass().getSimpleName(),
				"Expected OclBag but got " + result.getClass().getName());
	}

	/**
	 * Asserts that the result is a plain Sequence (ArrayList, not OclBag or OclOrderedSet).
	 */
	private static void assertSequence(Object result) {
		assertInstanceOf(List.class, result, "Sequence must be a List");
		// Must NOT be OclBag
		assertNotEquals("OclBag", result.getClass().getSimpleName(),
				"Sequence must not be OclBag");
		// Must NOT be OclOrderedSet
		assertNotEquals("OclOrderedSet", result.getClass().getSimpleName(),
				"Sequence must not be OclOrderedSet");
	}
}
