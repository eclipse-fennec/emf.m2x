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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Edge case tests for equality ({@code =} and {@code <>}) across
 * various types, null/invalid, and cross-type comparisons.
 */
class OclEqualityEdgeCaseTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 50000.0, true);
		bob = createPerson("Bob", 25, 40000.0, false);
		company = createCompany("ACME", alice, bob);
	}

	// --- Integer equality ---

	@Test
	void intEqual_same() throws OclParseException {
		assertEquals(true, eval("42 = 42", alice));
	}

	@Test
	void intEqual_different() throws OclParseException {
		assertEquals(false, eval("42 = 43", alice));
	}

	@Test
	void intNotEqual() throws OclParseException {
		assertEquals(true, eval("42 <> 43", alice));
	}

	@Test
	void intNotEqual_same() throws OclParseException {
		assertEquals(false, eval("42 <> 42", alice));
	}

	// --- Real equality ---

	@Test
	void realEqual() throws OclParseException {
		assertEquals(true, eval("3.14 = 3.14", alice));
	}

	@Test
	void realNotEqual() throws OclParseException {
		assertEquals(true, eval("3.14 <> 3.15", alice));
	}

	// --- String equality ---

	@Test
	void stringEqual() throws OclParseException {
		assertEquals(true, eval("'hello' = 'hello'", alice));
	}

	@Test
	void stringNotEqual() throws OclParseException {
		assertEquals(true, eval("'hello' <> 'world'", alice));
	}

	@Test
	void stringEqual_caseSensitive() throws OclParseException {
		assertEquals(false, eval("'Hello' = 'hello'", alice));
	}

	// --- Boolean equality ---

	@Test
	void boolEqual() throws OclParseException {
		assertEquals(true, eval("true = true", alice));
	}

	@Test
	void boolNotEqual() throws OclParseException {
		assertEquals(true, eval("true <> false", alice));
	}

	// --- Null equality ---

	@Test
	void nullEqualNull() throws OclParseException {
		assertEquals(true, eval("null = null", alice));
	}

	@Test
	void nullNotEqualValue() throws OclParseException {
		assertEquals(true, eval("null <> 42", alice));
	}

	@Test
	void valueNotEqualNull() throws OclParseException {
		assertEquals(true, eval("42 <> null", alice));
	}

	@Test
	void nullNotEqualString() throws OclParseException {
		assertEquals(true, eval("null <> 'hello'", alice));
	}

	// --- Invalid equality ---

	@Test
	void invalidEqualInvalid() throws OclParseException {
		// OCL v2.5: any operation on invalid (except oclIsInvalid/oclIsUndefined) yields invalid
		assertInvalid("invalid = invalid", alice);
	}

	@Test
	void invalidNotEqualNull() throws OclParseException {
		// OCL v2.5: any operation on invalid yields invalid
		assertInvalid("invalid <> null", alice);
	}

	@Test
	void invalidNotEqualValue() throws OclParseException {
		// OCL v2.5: any operation on invalid yields invalid
		assertInvalid("invalid <> 42", alice);
	}

	// --- EObject equality (identity) ---

	@Test
	void eObject_sameObject() throws OclParseException {
		assertEquals(true, eval("self = self", alice));
	}

	@Test
	void eObject_notEqual() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->at(1) <> self.employees->at(2)", company));
	}

	@Test
	void eObject_equalInCollection() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->exists(e | e = self.employees->first())", company));
	}

	// --- Collection equality ---

	@Test
	void sequenceEqual() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3} = Sequence{1, 2, 3}", alice));
	}

	@Test
	void sequenceNotEqual_differentOrder() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3} <> Sequence{3, 2, 1}", alice));
	}

	@Test
	void sequenceNotEqual_differentSize() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2} <> Sequence{1, 2, 3}", alice));
	}

	@Test
	void setEqual() throws OclParseException {
		assertEquals(true, eval("Set{1, 2, 3} = Set{3, 2, 1}", alice));
	}

	@Test
	void setNotEqual() throws OclParseException {
		assertEquals(true, eval("Set{1, 2} <> Set{1, 2, 3}", alice));
	}

	@Test
	void emptySequenceEqual() throws OclParseException {
		assertEquals(true, eval("Sequence{} = Sequence{}", alice));
	}

	@Test
	void emptySetEqual() throws OclParseException {
		assertEquals(true, eval("Set{} = Set{}", alice));
	}

	// --- Property equality ---

	@Test
	void propertyEqual_name() throws OclParseException {
		assertEquals(true, eval("self.name = 'Alice'", alice));
	}

	@Test
	void propertyNotEqual_name() throws OclParseException {
		assertEquals(true, eval("self.name <> 'Bob'", alice));
	}

	@Test
	void propertyEqual_age() throws OclParseException {
		assertEquals(true, eval("self.age = 30", alice));
	}

	@Test
	void propertyEqual_boolean() throws OclParseException {
		assertEquals(true, eval("self.isMarried = true", alice));
	}

	// --- Cross-type comparisons ---

	@Test
	void intNotEqualString() throws OclParseException {
		assertEquals(true, eval("42 <> '42'", alice));
	}

	@Test
	void boolNotEqualString() throws OclParseException {
		assertEquals(true, eval("true <> 'true'", alice));
	}
}
