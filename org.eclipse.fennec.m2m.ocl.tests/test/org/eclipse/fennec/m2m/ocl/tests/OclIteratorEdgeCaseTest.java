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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Edge case tests for OCL iterators: sortedBy, isUnique, collect, any
 * with invalid/null body results.
 */
class OclIteratorEdgeCaseTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// === sortedBy with invalid body ===

	@Test
	void sortedBy_invalidBody_divByZero() throws OclParseException {
		// Body produces invalid for element 0 → sortedBy is invalid
		assertInvalid("Sequence{1, 0, 2}->sortedBy(x | 1.div(x))", self);
	}

	@Test
	void sortedBy_normalCase() throws OclParseException {
		Object result = eval("Sequence{3, 1, 2}->sortedBy(x | x)", self);
		assertTrue(result instanceof Collection<?>);
		assertEquals(3, ((Collection<?>) result).size());
	}

	@Test
	void sortedBy_strings() throws OclParseException {
		Object result = eval("Sequence{'banana', 'apple', 'cherry'}->sortedBy(x | x)", self);
		assertTrue(result instanceof Collection<?>);
		Collection<?> coll = (Collection<?>) result;
		assertEquals("apple", coll.iterator().next());
	}

	// === isUnique with invalid body ===

	@Test
	void isUnique_invalidBody_divByZero() throws OclParseException {
		// Body produces invalid for element 0 → isUnique should report invalid
		assertInvalid("Sequence{1, 0, 2}->isUnique(x | 1.div(x))", self);
	}

	@Test
	void isUnique_nullBody() throws OclParseException {
		// Multiple nulls → not unique
		assertEquals(false, eval("Sequence{1, 2, 3}->isUnique(x | null)", self));
	}

	// === collect with null body ===

	@Test
	void collect_nullBody() throws OclParseException {
		// Collect null body should produce a Bag with nulls
		Object result = eval("Sequence{1, 2, 3}->collect(x | null)->size()", self);
		assertEquals(3, result);
	}

	@Test
	void collect_invalidBody_propagates() throws OclParseException {
		// Collect on invalid body element: invalid is included in result
		Object result = eval("Sequence{1, 0, 2}->collect(x | 1.div(x))->size()", self);
		assertEquals(3, result);
	}

	// === any with invalid body ===

	@Test
	void any_invalidBody_noMatch() throws OclParseException {
		// All body evaluations are invalid → no element matched, result is null
		Object result = eval("Sequence{0}->any(x | 1.div(x) > 0)", self);
		// 1.div(0) is invalid, invalid > 0 is invalid, not TRUE → no match → null
		assertEquals(null, result);
	}

	@Test
	void any_invalidBody_afterMatch() throws OclParseException {
		// First element matches, rest may produce invalid — short-circuit returns first match
		assertEquals(2, eval("Sequence{2, 0, 1}->any(x | x > 1)", self));
	}

	// === Empty collection edge cases ===

	@Test
	void sortedBy_emptyCollection() throws OclParseException {
		Object result = eval("Sequence{}->sortedBy(x | x)", self);
		assertTrue(result instanceof Collection<?>);
		assertEquals(0, ((Collection<?>) result).size());
	}

	@Test
	void isUnique_emptyCollection() throws OclParseException {
		// Empty collection → vacuously unique
		assertEquals(true, eval("Sequence{}->isUnique(x | x)", self));
	}

	// === sortedBy preserves all elements ===

	@Test
	void sortedBy_descending() throws OclParseException {
		// sortedBy with negated key → descending order
		Object result = eval("Sequence{1, 3, 2}->sortedBy(x | 0 - x)", self);
		assertTrue(result instanceof Collection<?>);
		Collection<?> coll = (Collection<?>) result;
		assertEquals(3, coll.iterator().next());
	}
}
