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
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Edge case tests for the OCL evaluator.
 * Covers corner cases, boundary values, and unusual but valid
 * OCL expressions.
 */
class OclEdgeCasesTest extends AbstractOclTest {

	static EObject self;
	static EObject company;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
		company = createCompany("ACME", self);
	}

	// --- Large numbers ---

	@Test
	void largeInteger() throws OclParseException {
		assertEquals(1000000000, eval("1000000000", self));
	}

	@Test
	void largeIntegerArithmetic() throws OclParseException {
		assertEquals(2000000000, eval("1000000000 + 1000000000", self));
	}

	// --- Zero ---

	@Test
	void zeroInteger() throws OclParseException {
		assertEquals(0, eval("0", self));
	}

	@Test
	void zeroReal() throws OclParseException {
		assertEquals(0.0, eval("0.0", self));
	}

	@Test
	void zeroMultiplication() throws OclParseException {
		assertEquals(0, eval("0 * 99999", self));
	}

	// --- Negative numbers ---

	@Test
	void negativeInteger() throws OclParseException {
		assertEquals(-42, eval("-42", self));
	}

	@Test
	void negativeReal() throws OclParseException {
		assertEquals(-3.14, eval("-3.14", self));
	}

	@Test
	void doubleNegation() throws OclParseException {
		assertEquals(42, eval("-(-42)", self));
	}

	// --- Empty string ---

	@Test
	void emptyString() throws OclParseException {
		assertEquals("", eval("''", self));
	}

	@Test
	void emptyStringSize() throws OclParseException {
		assertEquals(0, eval("''.size()", self));
	}

	@Test
	void emptyStringConcat() throws OclParseException {
		assertEquals("hello", eval("'' + 'hello'", self));
	}

	// --- Deeply nested expressions ---

	@Test
	void deeplyNestedParens() throws OclParseException {
		assertEquals(1, eval("((((1))))", self));
	}

	@Test
	void deeplyNestedIf() throws OclParseException {
		assertEquals("deep", eval(
				"if true then if true then if true then 'deep' else 'x' endif else 'y' endif else 'z' endif",
				self));
	}

	@Test
	void deeplyNestedLet() throws OclParseException {
		assertEquals(6, eval(
				"let a: Integer = 1 in let b: Integer = 2 in let c: Integer = 3 in a + b + c",
				self));
	}

	// --- Empty collections ---

	@Test
	void emptySet_size() throws OclParseException {
		assertEquals(0, eval("Set{}->size()", self));
	}

	@Test
	void emptySequence_size() throws OclParseException {
		assertEquals(0, eval("Sequence{}->size()", self));
	}

	@Test
	void emptySet_isEmpty() throws OclParseException {
		assertEquals(true, eval("Set{}->isEmpty()", self));
	}

	@Test
	void emptySet_forAll() throws OclParseException {
		// forAll on empty is vacuously true
		assertEquals(true, eval("Set{}->forAll(i | false)", self));
	}

	@Test
	void emptySet_exists() throws OclParseException {
		// exists on empty is false
		assertEquals(false, eval("Set{}->exists(i | true)", self));
	}

	// --- Single element collections ---

	@Test
	void singleton_set() throws OclParseException {
		assertEquals(1, eval("Set{42}->size()", self));
	}

	@Test
	void singleton_sequence_first() throws OclParseException {
		assertEquals(42, eval("Sequence{42}->first()", self));
	}

	@Test
	void singleton_sequence_last() throws OclParseException {
		assertEquals(42, eval("Sequence{42}->last()", self));
	}

	// --- Boolean edge cases ---

	@Test
	void notNot() throws OclParseException {
		assertEquals(true, eval("not not true", self));
	}

	@Test
	void complexBooleanChain() throws OclParseException {
		assertEquals(true, eval("true and true and true and true", self));
	}

	@Test
	void complexOrChain() throws OclParseException {
		assertEquals(true, eval("false or false or false or true", self));
	}

	// --- String with special characters ---

	@Test
	void stringWithDigits() throws OclParseException {
		assertEquals("abc123", eval("'abc123'", self));
	}

	@Test
	void stringWithSpaces() throws OclParseException {
		assertEquals("hello world", eval("'hello world'", self));
	}

	// --- Chained operations ---

	@Test
	void chainedStringOps() throws OclParseException {
		assertEquals("HELLO", eval("'  hello  '.trim().toUpperCase()", self));
	}

	@Test
	void chainedCollectionOps() throws OclParseException {
		assertEquals(3, eval("Sequence{3, 1, 2}->sortedBy(i | i)->reverse()->first()", self));
	}

	// --- Any with false condition on all ---

	@Test
	void any_noMatch_returnsNull() throws OclParseException {
		assertNull(eval("Sequence{1, 2, 3}->any(i | i > 10)", self));
	}

	// --- Collection including null ---

	@Test
	void sequenceWithNull() throws OclParseException {
		assertEquals(3, eval("Sequence{1, null, 3}->size()", self));
	}

	// --- Multiple equal elements ---

	@Test
	void sequenceDuplicates() throws OclParseException {
		assertEquals(5, eval("Sequence{1, 1, 1, 1, 1}->size()", self));
	}

	@Test
	void setDuplicates() throws OclParseException {
		assertEquals(1, eval("Set{1, 1, 1, 1, 1}->size()", self));
	}

	// --- Collection of booleans ---

	@Test
	void booleanSequence() throws OclParseException {
		assertEquals(true, eval("Sequence{true, false, true}->exists(b | b)", self));
	}

	@Test
	void booleanForAll() throws OclParseException {
		assertEquals(false, eval("Sequence{true, false, true}->forAll(b | b)", self));
	}

	// --- sortedBy returns correct first/last ---

	@Test
	void sortedBy_first() throws OclParseException {
		assertEquals(1, eval("Set{5, 3, 1, 4, 2}->sortedBy(i | i)->first()", self));
	}

	@Test
	void sortedBy_last() throws OclParseException {
		assertEquals(5, eval("Set{5, 3, 1, 4, 2}->sortedBy(i | i)->last()", self));
	}

	// --- Nested sorted + chain with reverse ---

	@Test
	void chainedSortReverse() throws OclParseException {
		Object result = eval("Sequence{3, 1, 2}->sortedBy(i | i)->reverse()", self);
		assertEquals(List.of(3, 2, 1), result);
	}
}
