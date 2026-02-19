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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for the flatten() collection operation.
 * Flatten removes one level of nesting from collections of collections.
 */
class OclCollectionFlattenTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Basic flatten ---

	@Test
	void flatten_sequenceOfSequences() throws OclParseException {
		Object result = eval(
				"Sequence{Sequence{1, 2}, Sequence{3, 4}}->flatten()", self);
		assertInstanceOf(List.class, result);
		assertEquals(List.of(1, 2, 3, 4), result);
	}

	@Test
	void flatten_alreadyFlat() throws OclParseException {
		assertEquals(List.of(1, 2, 3), eval("Sequence{1, 2, 3}->flatten()", self));
	}

	@Test
	void flatten_emptyCollection() throws OclParseException {
		Object result = eval("Sequence{}->flatten()", self);
		assertInstanceOf(List.class, result);
		assertEquals(0, ((List<?>) result).size());
	}

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

	// --- Flatten with strings ---

	@Test
	void flatten_stringSequences() throws OclParseException {
		Object result = eval(
				"Sequence{Sequence{'a', 'b'}, Sequence{'c'}}->flatten()", self);
		assertEquals(List.of("a", "b", "c"), result);
	}

	// --- Flatten mixed with empty ---

	@Test
	void flatten_withEmptyInner() throws OclParseException {
		Object result = eval(
				"Sequence{Sequence{1}, Sequence{}, Sequence{2}}->flatten()", self);
		assertEquals(List.of(1, 2), result);
	}

	// --- Flatten then operations ---

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
