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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@code oclAsType()} on collection types (OCL v2.4 §11.7).
 *
 * <p>{@code oclAsType()} on collections checks type conformance (collection kind
 * + element type). It does NOT convert between collection kinds.
 */
class OclAsTypeCollectionTest extends AbstractOclTest {

	static EObject person;
	static EObject company;

	@BeforeAll
	static void setUp() {
		person = createPerson("Alice", 30, 50000.0, true);
		company = createCompany("Acme", person,
				createPerson("Bob", 25, 40000.0, false));
	}

	// ==================== Same kind — success ====================

	@Test
	void set_oclAsType_set_sameElementType() throws OclParseException {
		// Set{1,2,3}.oclAsType(Set(Integer)) → same set
		assertEquals(3,
				eval("Set{1,2,3}.oclAsType(Set(Integer))->size()", person));
	}

	@Test
	void sequence_oclAsType_sequence() throws OclParseException {
		assertEquals(3,
				eval("Sequence{1,2,3}.oclAsType(Sequence(Integer))->size()", person));
	}

	@Test
	void bag_oclAsType_bag() throws OclParseException {
		assertEquals(3,
				eval("Bag{1,2,3}.oclAsType(Bag(Integer))->size()", person));
	}

	@Test
	void orderedSet_oclAsType_orderedSet() throws OclParseException {
		assertEquals(3,
				eval("OrderedSet{1,2,3}.oclAsType(OrderedSet(Integer))->size()", person));
	}

	// ==================== Collection(T) accepts any kind ====================

	@Test
	void set_oclAsType_collection() throws OclParseException {
		// Collection(T) is the supertype — accepts any collection kind
		assertEquals(3,
				eval("Set{1,2,3}.oclAsType(Collection(Integer))->size()", person));
	}

	@Test
	void sequence_oclAsType_collection() throws OclParseException {
		assertEquals(3,
				eval("Sequence{1,2,3}.oclAsType(Collection(Integer))->size()", person));
	}

	@Test
	void bag_oclAsType_collection() throws OclParseException {
		assertEquals(3,
				eval("Bag{1,2,3}.oclAsType(Collection(Integer))->size()", person));
	}

	@Test
	void orderedSet_oclAsType_collection() throws OclParseException {
		assertEquals(3,
				eval("OrderedSet{1,2,3}.oclAsType(Collection(Integer))->size()", person));
	}

	// ==================== Wrong kind — OclInvalid ====================

	@Test
	void set_oclAsType_sequence_invalid() throws OclParseException {
		// Set cannot be cast to Sequence — different kind
		assertInvalid("Set{1,2,3}.oclAsType(Sequence(Integer))", person);
	}

	@Test
	void sequence_oclAsType_set_invalid() throws OclParseException {
		assertInvalid("Sequence{1,2,3}.oclAsType(Set(Integer))", person);
	}

	@Test
	void bag_oclAsType_orderedSet_invalid() throws OclParseException {
		assertInvalid("Bag{1,2,3}.oclAsType(OrderedSet(Integer))", person);
	}

	@Test
	void set_oclAsType_bag_invalid() throws OclParseException {
		assertInvalid("Set{1,2,3}.oclAsType(Bag(Integer))", person);
	}

	// ==================== Element type conformance ====================

	@Test
	void set_oclAsType_oclAny_elements() throws OclParseException {
		// OclAny is supertype of all — should accept
		assertEquals(3,
				eval("Set{1,2,3}.oclAsType(Set(OclAny))->size()", person));
	}

	@Test
	void set_string_oclAsType_set_integer_invalid() throws OclParseException {
		// String elements don't conform to Integer
		assertInvalid("Set{'a','b'}.oclAsType(Set(Integer))", person);
	}

	@Test
	void set_string_oclAsType_set_string() throws OclParseException {
		assertEquals(2,
				eval("Set{'a','b'}.oclAsType(Set(String))->size()", person));
	}

	// ==================== Classifier element types ====================

	@Test
	void set_eobject_oclAsType_set_person() throws OclParseException {
		// self.employees is EList<Person> → asSet() makes Set(Person)
		assertEquals(2,
				eval("self.employees->asSet().oclAsType(Set(Person))->size()", company));
	}

	// ==================== Non-collection source — invalid ====================

	@Test
	void integer_oclAsType_set_invalid() throws OclParseException {
		// A non-collection value cannot be cast to a collection type
		assertInvalid("42.oclAsType(Set(Integer))", person);
	}

	@Test
	void string_oclAsType_sequence_invalid() throws OclParseException {
		assertInvalid("'hello'.oclAsType(Sequence(String))", person);
	}

	// ==================== Empty collections ====================

	@Test
	void emptySet_oclAsType_set() throws OclParseException {
		assertEquals(0,
				eval("Set{}.oclAsType(Set(Integer))->size()", person));
	}

	@Test
	void emptySequence_oclAsType_sequence() throws OclParseException {
		assertEquals(0,
				eval("Sequence{}.oclAsType(Sequence(String))->size()", person));
	}

	// ==================== Preserves identity ====================

	@Test
	void set_oclAsType_preserves_elements() throws OclParseException {
		// After oclAsType the collection should contain the same elements
		assertEquals(true,
				eval("Set{1,2,3}.oclAsType(Set(Integer)) = Set{1,2,3}", person));
	}
}
