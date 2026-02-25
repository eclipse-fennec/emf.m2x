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
 * Tests that OCL parsing is insensitive to whitespace variations.
 * The same expression should produce the same result regardless
 * of spacing, indentation, or newlines.
 */
class OclWhitespaceInsensitivityTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- Arithmetic ---

	@Test
	void arithmetic_noSpaces() throws OclParseException {
		assertEquals(5, eval("2+3", self));
	}

	@Test
	void arithmetic_extraSpaces() throws OclParseException {
		assertEquals(5, eval("2  +  3", self));
	}

	@Test
	void arithmetic_tabs() throws OclParseException {
		assertEquals(5, eval("2\t+\t3", self));
	}

	@Test
	void arithmetic_newlines() throws OclParseException {
		assertEquals(5, eval("2\n+\n3", self));
	}

	// --- Property access ---

	@Test
	void property_noSpaces() throws OclParseException {
		assertEquals("Alice", eval("self.name", self));
	}

	@Test
	void property_spacesAroundDot() throws OclParseException {
		// Dot navigation with spaces should still work
		assertEquals("Alice", eval("self . name", self));
	}

	// --- Collection arrow ---

	@Test
	void arrow_noSpaces() throws OclParseException {
		assertEquals(3, eval("Sequence{1,2,3}->size()", self));
	}

	@Test
	void arrow_spacesAroundArrow() throws OclParseException {
		assertEquals(3, eval("Sequence{1, 2, 3} -> size()", self));
	}

	@Test
	void arrow_spacesInCollection() throws OclParseException {
		assertEquals(3, eval("Sequence{ 1 , 2 , 3 }->size()", self));
	}

	// --- If-then-else ---

	@Test
	void ifThenElse_compact() throws OclParseException {
		assertEquals(1, eval("if true then 1 else 2 endif", self));
	}

	@Test
	void ifThenElse_multiline() throws OclParseException {
		assertEquals(1, eval("if true\nthen 1\nelse 2\nendif", self));
	}

	@Test
	void ifThenElse_extraSpaces() throws OclParseException {
		assertEquals(1, eval("if   true   then   1   else   2   endif", self));
	}

	// --- Let ---

	@Test
	void let_compact() throws OclParseException {
		assertEquals(42, eval("let x:Integer=42 in x", self));
	}

	@Test
	void let_spacious() throws OclParseException {
		assertEquals(42, eval("let  x : Integer  =  42  in  x", self));
	}

	@Test
	void let_multiline() throws OclParseException {
		assertEquals(42, eval("let x: Integer = 42\nin x", self));
	}

	// --- Iterator ---

	@Test
	void iterator_compact() throws OclParseException {
		assertEquals(true, eval("Sequence{1,2,3}->forAll(i|i>0)", self));
	}

	@Test
	void iterator_spacious() throws OclParseException {
		assertEquals(true, eval("Sequence{ 1 , 2 , 3 } -> forAll( i | i > 0 )", self));
	}

	// --- Comparison ---

	@Test
	void comparison_noSpaces() throws OclParseException {
		assertEquals(true, eval("1<2", self));
	}

	@Test
	void comparison_spaces() throws OclParseException {
		assertEquals(true, eval("1 < 2", self));
	}

	// --- Boolean ---

	@Test
	void boolean_compact() throws OclParseException {
		assertEquals(true, eval("true and true", self));
	}

	@Test
	void boolean_extraSpaces() throws OclParseException {
		assertEquals(true, eval("true   and   true", self));
	}

	// --- Parentheses ---

	@Test
	void parens_noSpaces() throws OclParseException {
		assertEquals(9, eval("(1+2)*3", self));
	}

	@Test
	void parens_spaces() throws OclParseException {
		assertEquals(9, eval("( 1 + 2 ) * 3", self));
	}

	// --- Complex multiline ---

	@Test
	void complexMultiline() throws OclParseException {
		assertEquals(true, eval(
				"let x: Integer = 5\n" +
				"in\n" +
				"  if x > 3\n" +
				"  then true\n" +
				"  else false\n" +
				"  endif", self));
	}
}
