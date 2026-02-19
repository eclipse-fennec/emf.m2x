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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Advanced tests for collection type conversions:
 * asSequence, asSet, asBag, asOrderedSet, and chained conversions.
 */
class OclCollectionConversionAdvancedTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Sequence -> Set (removes duplicates) ---

	@Test
	void sequenceToSet_removesDuplicates() throws OclParseException {
		Object result = eval("Sequence{1, 2, 2, 3, 3}->asSet()->size()", self);
		assertEquals(3, result);
	}

	@Test
	void sequenceToSet_preservesElements() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3}->asSet()->includes(2)", self);
		assertEquals(true, result);
	}

	@Test
	void sequenceToSet_type() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3}->asSet()", self);
		assertInstanceOf(LinkedHashSet.class, result);
	}

	// --- Set -> Sequence ---

	@Test
	void setToSequence_type() throws OclParseException {
		Object result = eval("Set{1, 2, 3}->asSequence()", self);
		assertInstanceOf(List.class, result);
	}

	@Test
	void setToSequence_size() throws OclParseException {
		assertEquals(3, eval("Set{1, 2, 3}->asSequence()->size()", self));
	}

	// --- Set -> OrderedSet ---

	@Test
	void setToOrderedSet_size() throws OclParseException {
		assertEquals(3, eval("Set{3, 1, 2}->asOrderedSet()->size()", self));
	}

	// --- Sequence -> Bag ---

	@Test
	void sequenceToBag_keepsDuplicates() throws OclParseException {
		assertEquals(5, eval("Sequence{1, 2, 2, 3, 3}->asBag()->size()", self));
	}

	// --- Bag -> Set (removes duplicates) ---

	@Test
	void bagToSet_removesDuplicates() throws OclParseException {
		assertEquals(3, eval("Bag{1, 2, 2, 3, 3}->asSet()->size()", self));
	}

	// --- Bag -> Sequence ---

	@Test
	void bagToSequence_keepsDuplicates() throws OclParseException {
		assertEquals(5, eval("Bag{1, 2, 2, 3, 3}->asSequence()->size()", self));
	}

	// --- OrderedSet -> Sequence ---

	@Test
	void orderedSetToSequence_preservesOrder() throws OclParseException {
		Object result = eval("OrderedSet{1, 2, 3}->asSequence()", self);
		assertInstanceOf(List.class, result);
		assertEquals(List.of(1, 2, 3), result);
	}

	@Test
	void orderedSetToSequence_noDuplicates() throws OclParseException {
		assertEquals(3, eval("OrderedSet{1, 2, 3}->asSequence()->size()", self));
	}

	// --- OrderedSet -> Set ---

	@Test
	void orderedSetToSet_type() throws OclParseException {
		Object result = eval("OrderedSet{1, 2, 3}->asSet()", self);
		assertInstanceOf(Set.class, result);
	}

	// --- Sequence -> OrderedSet (removes duplicates, keeps order) ---

	@Test
	void sequenceToOrderedSet_removesDuplicates() throws OclParseException {
		assertEquals(3, eval("Sequence{1, 2, 2, 3, 3}->asOrderedSet()->size()", self));
	}

	// --- Empty collection conversions ---

	@Test
	void emptySequenceToSet() throws OclParseException {
		assertEquals(0, eval("Sequence{}->asSet()->size()", self));
	}

	@Test
	void emptySetToSequence() throws OclParseException {
		assertEquals(0, eval("Set{}->asSequence()->size()", self));
	}

	// --- Chained conversions ---

	@Test
	void chained_seqToSetToSeq() throws OclParseException {
		assertEquals(3, eval("Sequence{1, 2, 2, 3}->asSet()->asSequence()->size()", self));
	}

	@Test
	void chained_bagToSetToBag() throws OclParseException {
		assertEquals(3, eval("Bag{1, 1, 2, 3}->asSet()->asBag()->size()", self));
	}

	// --- Conversion then operation ---

	@Test
	void setToSequence_thenFirst() throws OclParseException {
		// Set iteration order is not guaranteed, but the conversion should work
		Object result = eval("Set{42}->asSequence()->first()", self);
		assertEquals(42, result);
	}

	@Test
	void sequenceToSet_thenIncludes() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3}->asSet()->includes(2)", self));
	}

	@Test
	void sequenceToSet_thenExcludes() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3}->asSet()->excludes(99)", self));
	}

	// --- String collection conversions ---

	@Test
	void stringSequenceToSet() throws OclParseException {
		assertEquals(2, eval("Sequence{'a', 'b', 'a'}->asSet()->size()", self));
	}

	// --- With model data ---

	@Test
	void employeeNames_asSet() throws OclParseException {
		EObject bob = createPerson("Bob", 25, 40000.0, false);
		EObject alice2 = createPerson("Alice", 35, 60000.0, true);
		EObject company = createCompany("ACME", self, bob, alice2);
		// Two Alices → Set has 2 distinct names
		assertEquals(2, eval("self.employees->collect(e | e.name)->asSet()->size()", company));
	}

	// --- Boolean collection ---

	@Test
	void booleanSequenceToSet() throws OclParseException {
		assertEquals(2, eval("Sequence{true, false, true}->asSet()->size()", self));
	}

	// --- Flatten after conversion ---

	@Test
	void sequenceToSet_thenUnion() throws OclParseException {
		assertEquals(true,
				eval("Sequence{1, 2}->asSet()->union(Set{3})->includes(3)", self));
	}
}
