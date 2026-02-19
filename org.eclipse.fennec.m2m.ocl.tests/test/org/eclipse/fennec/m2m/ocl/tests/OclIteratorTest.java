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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL iterator expressions (OCL v2.4 Section 11.9).
 *
 * <p>Ported from Eclipse OCL {@code IteratorsTest4}.
 */
class OclIteratorTest extends AbstractOclTest {

	static EObject self;
	static EObject alice;
	static EObject bob;
	static EObject charlie;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
		alice = createPerson("Alice", 30, 60000.0, true);
		bob = createPerson("Bob", 25, 45000.0, false);
		charlie = createPerson("Charlie", 40, 80000.0, true);
		company = createCompany("ACME", alice, bob, charlie);
	}

	// ==================== select ====================

	@Test
	void select_integers() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3, 4, 5}->select(i | i > 3)", self);
		assertEquals(List.of(4L, 5L), result);
	}

	@Test
	void select_emptyResult() throws OclParseException {
		Object result = eval("Set{1, 2, 3}->select(i | i > 10)", self);
		assertInstanceOf(Set.class, result);
		assertTrue(((Collection<?>) result).isEmpty());
	}

	@Test
	void select_all() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3}->select(i | i > 0)", self);
		assertEquals(List.of(1L, 2L, 3L), result);
	}

	@Test
	void select_preservesSetType() throws OclParseException {
		Object result = eval("Set{1, 2, 3, 4}->select(i | i > 2)", self);
		assertInstanceOf(LinkedHashSet.class, result);
	}

	@Test
	void select_strings() throws OclParseException {
		Object result = eval("Sequence{'abc', 'de', 'fghi'}->select(s | s.size() > 2)", self);
		assertEquals(List.of("abc", "fghi"), result);
	}

	// ==================== reject ====================

	@Test
	void reject_integers() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3, 4, 5}->reject(i | i > 3)", self);
		assertEquals(List.of(1L, 2L, 3L), result);
	}

	@Test
	void reject_none() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3}->reject(i | i > 10)", self);
		assertEquals(List.of(1L, 2L, 3L), result);
	}

	@Test
	void reject_all() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3}->reject(i | i > 0)", self);
		assertTrue(((Collection<?>) result).isEmpty());
	}

	// ==================== collect ====================

	@Test
	void collect_integers() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3}->collect(i | i * 2)", self);
		assertEquals(List.of(2L, 4L, 6L), result);
	}

	@Test
	void collect_strings() throws OclParseException {
		Object result = eval("Sequence{'a', 'bb', 'ccc'}->collect(s | s.size())", self);
		assertEquals(List.of(1L, 2L, 3L), result);
	}

	@Test
	void collect_flattensOneLevel() throws OclParseException {
		// collect flattens one level: Sequence{Sequence{1,2}, Sequence{3}} -> Sequence{1,2,3}
		Object result = eval("Sequence{Sequence{1, 2}, Sequence{3}}->collect(s | s)", self);
		assertEquals(List.of(1L, 2L, 3L), result);
	}

	@Test
	void collect_empty() throws OclParseException {
		Object result = eval("Sequence{}->collect(i | i)", self);
		assertInstanceOf(List.class, result);
		assertTrue(((Collection<?>) result).isEmpty());
	}

	// ==================== collectNested ====================

	@Test
	void collectNested_doesNotFlatten() throws OclParseException {
		Object result = eval("Sequence{Sequence{1, 2}, Sequence{3}}->collectNested(s | s)", self);
		assertInstanceOf(List.class, result);
		@SuppressWarnings("unchecked")
		List<Object> list = (List<Object>) result;
		assertEquals(2, list.size());
		// Each element should still be a collection
		assertInstanceOf(List.class, list.get(0));
		assertInstanceOf(List.class, list.get(1));
	}

	// ==================== forAll ====================

	@Test
	void forAll_true() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3}->forAll(i | i > 0)", self));
	}

	@Test
	void forAll_false() throws OclParseException {
		assertEquals(false, eval("Sequence{1, 2, 3}->forAll(i | i > 1)", self));
	}

	@Test
	void forAll_empty() throws OclParseException {
		// forAll on empty collection is true (vacuous truth)
		assertEquals(true, eval("Sequence{}->forAll(i | i > 0)", self));
	}

	@Test
	void forAll_shortCircuit() throws OclParseException {
		// Should short-circuit: first element fails
		assertEquals(false, eval("Sequence{0, 1, 2}->forAll(i | i > 0)", self));
	}

	// ==================== exists ====================

	@Test
	void exists_true() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3}->exists(i | i = 2)", self));
	}

	@Test
	void exists_false() throws OclParseException {
		assertEquals(false, eval("Sequence{1, 2, 3}->exists(i | i = 4)", self));
	}

	@Test
	void exists_empty() throws OclParseException {
		// exists on empty collection is false
		assertEquals(false, eval("Sequence{}->exists(i | i = 1)", self));
	}

	@Test
	void exists_shortCircuit() throws OclParseException {
		// Should short-circuit: first element matches
		assertEquals(true, eval("Sequence{1, 2, 3}->exists(i | i = 1)", self));
	}

	// ==================== any ====================

	@Test
	void any_found() throws OclParseException {
		assertEquals(2L, eval("Sequence{1, 2, 3}->any(i | i = 2)", self));
	}

	@Test
	void any_notFound() throws OclParseException {
		// any returns null when no element matches
		assertNull(eval("Sequence{1, 2, 3}->any(i | i = 4)", self));
	}

	@Test
	void any_multiple() throws OclParseException {
		// any returns the first match (for ordered collections)
		Object result = eval("Sequence{1, 2, 3}->any(i | i > 1)", self);
		assertEquals(2L, result);
	}

	@Test
	void any_booleans() throws OclParseException {
		assertEquals(false, eval("Sequence{false}->any(s | s = false)", self));
	}

	// ==================== one ====================

	@Test
	void one_exactlyOne() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3}->one(i | i = 2)", self));
	}

	@Test
	void one_moreThanOne() throws OclParseException {
		assertEquals(false, eval("Sequence{1, 2, 3}->one(i | i > 1)", self));
	}

	@Test
	void one_none() throws OclParseException {
		assertEquals(false, eval("Sequence{1, 2, 3}->one(i | i = 4)", self));
	}

	@Test
	void one_empty() throws OclParseException {
		assertEquals(false, eval("Sequence{}->one(i | i = 1)", self));
	}

	// ==================== isUnique ====================

	@Test
	void isUnique_true() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3}->isUnique(i | i)", self));
	}

	@Test
	void isUnique_false() throws OclParseException {
		// All map to the same value (> 0)
		assertEquals(false, eval("Sequence{1, 2, 3}->isUnique(i | i > 0)", self));
	}

	@Test
	void isUnique_withTransform() throws OclParseException {
		assertEquals(true, eval("Sequence{'a', 'bb', 'ccc'}->isUnique(s | s.size())", self));
	}

	@Test
	void isUnique_withDuplicateTransform() throws OclParseException {
		assertEquals(false, eval("Sequence{'ab', 'cd', 'ef'}->isUnique(s | s.size())", self));
	}

	// ==================== sortedBy ====================

	@Test
	void sortedBy_integers() throws OclParseException {
		Object result = eval("Sequence{3, 1, 2}->sortedBy(i | i)", self);
		assertEquals(List.of(1L, 2L, 3L), result);
	}

	@Test
	void sortedBy_strings() throws OclParseException {
		Object result = eval("Sequence{'banana', 'apple', 'cherry'}->sortedBy(s | s)", self);
		assertEquals(List.of("apple", "banana", "cherry"), result);
	}

	@Test
	void sortedBy_byLength() throws OclParseException {
		Object result = eval("Sequence{'bb', 'a', 'ccc'}->sortedBy(s | s.size())", self);
		assertEquals(List.of("a", "bb", "ccc"), result);
	}

	@Test
	void sortedBy_empty() throws OclParseException {
		Object result = eval("Sequence{}->sortedBy(i | i)", self);
		assertInstanceOf(List.class, result);
		assertTrue(((Collection<?>) result).isEmpty());
	}

	// ==================== iterate ====================

	@Test
	void iterate_sum() throws OclParseException {
		assertEquals(6L, eval("Sequence{1, 2, 3}->iterate(i; acc : Integer = 0 | acc + i)", self));
	}

	@Test
	void iterate_count() throws OclParseException {
		assertEquals(3L, eval("Sequence{1, 2, 3}->iterate(i; acc : Integer = 0 | acc + 1)", self));
	}

	@Test
	void iterate_concat() throws OclParseException {
		assertEquals("abc", eval("Sequence{'a', 'b', 'c'}->iterate(s; acc : String = '' | acc.concat(s))", self));
	}

	@Test
	void iterate_empty() throws OclParseException {
		// iterate on empty returns initial accumulator value
		assertEquals(0L, eval("Sequence{}->iterate(i; acc : Integer = 0 | acc + i)", self));
	}

	// ==================== Model-based iterators ====================

	@Test
	void select_employees() throws OclParseException {
		Object result = eval("self.employees->select(e | e.age > 25)", company);
		assertInstanceOf(Collection.class, result);
		assertEquals(2, ((Collection<?>) result).size());
	}

	@Test
	void collect_employeeNames() throws OclParseException {
		Object result = eval("self.employees->collect(e | e.name)", company);
		assertInstanceOf(List.class, result);
		@SuppressWarnings("unchecked")
		List<Object> names = (List<Object>) result;
		assertEquals(3, names.size());
		assertTrue(names.contains("Alice"));
		assertTrue(names.contains("Bob"));
		assertTrue(names.contains("Charlie"));
	}

	@Test
	void exists_highSalary() throws OclParseException {
		assertEquals(true, eval("self.employees->exists(e | e.salary > 70000.0)", company));
	}

	@Test
	void forAll_hasName() throws OclParseException {
		assertEquals(true, eval("self.employees->forAll(e | e.name.size() > 0)", company));
	}

	@Test
	void select_thenSize() throws OclParseException {
		assertEquals(2L, eval("self.employees->select(e | e.isMarried)->size()", company));
	}

	@Test
	void collect_thenSum() throws OclParseException {
		// Sum of all salaries
		Object result = eval("self.employees->collect(e | e.salary)->sum()", company);
		assertEquals(185000.0, result);
	}

	@Test
	void reject_thenForAll() throws OclParseException {
		// Reject married, then check all remaining are not married
		assertEquals(true, eval("self.employees->reject(e | e.isMarried)->forAll(e | not e.isMarried)", company));
	}

	// ==================== Chained iterators ====================

	@Test
	void select_thenCollect() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3, 4, 5}->select(i | i > 2)->collect(i | i * 10)", self);
		assertEquals(List.of(30L, 40L, 50L), result);
	}

	@Test
	void collect_thenSelect() throws OclParseException {
		Object result = eval("Sequence{'a', 'bb', 'ccc'}->collect(s | s.size())->select(n | n > 1)", self);
		assertEquals(List.of(2L, 3L), result);
	}

	@Test
	void sortedBy_thenFirst() throws OclParseException {
		assertEquals(1L, eval("Sequence{3, 1, 2}->sortedBy(i | i)->first()", self));
	}
}
