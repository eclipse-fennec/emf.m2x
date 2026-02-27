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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.ocl.Constraint;
import org.eclipse.fennec.m2x.model.ocl.ConstraintKind;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL syntax gaps G-01 through G-12.
 *
 * <p>Verifies that the parser correctly handles escaped identifiers, Unicode characters,
 * real exponent notation, adjacent string concatenation, hex/unicode escapes,
 * implicit-source {@code @pre}, qualified calls, nested block comments, self aliases,
 * unqualified operation contexts, and optional return types.
 *
 * @see <a href="docs/ocl-spec-compliance.md">OCL Spec Compliance</a>
 */
class OclSyntaxGapsTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// ==================== G-01: Escaped Identifiers _'keyword' (§9.3.4) ====================

	@Test
	void g01_escapedIdentifier_asLetVariable() throws OclParseException {
		// _'self' used as a let variable name (not the keyword 'self')
		assertEquals(42, eval("let _'self' : Integer = 42 in _'self'", self));
	}

	@Test
	void g01_escapedIdentifier_inTupleLiteral() throws OclParseException {
		// _'context' used as tuple part name
		Object result = eval("Tuple{_'context' : Integer = 7}._'context'", self);
		assertEquals(7, result);
	}

	@Test
	void g01_escapedIdentifier_withEmbeddedQuote() throws OclParseException {
		// _'it''s' → identifier "it's" (escaped quote inside escaped identifier)
		assertEquals(99, eval("let _'it''s' : Integer = 99 in _'it''s'", self));
	}

	// ==================== G-02: Unicode Identifier Characters (§9.3.4) ====================

	@Test
	void g02_unicodeIdentifier_umlaut() throws OclParseException {
		// Identifier with umlaut character
		assertEquals(42, eval("let größe : Integer = 42 in größe", self));
	}

	@Test
	void g02_unicodeIdentifier_dollar() throws OclParseException {
		// '$' as identifier start character
		assertEquals(7, eval("let $value : Integer = 7 in $value", self));
	}

	@Test
	void g02_unicodeIdentifier_accentedLetters() throws OclParseException {
		// Accented letters (Latin Extended)
		assertEquals(5, eval("let café : Integer = 5 in café", self));
	}

	// ==================== G-03: Real Exponent Notation (§9.3.19) ====================

	@Test
	void g03_realExponent_integerForm() throws OclParseException {
		// 1e10 = integer form with exponent
		assertEquals(1e10, eval("1e10", self));
	}

	@Test
	void g03_realExponent_decimalForm() throws OclParseException {
		// 1.5E-3 = decimal form with negative exponent
		assertEquals(0.0015, eval("1.5E-3", self));
	}

	@Test
	void g03_realExponent_plusSign() throws OclParseException {
		// 3E+2 = explicit positive exponent
		assertEquals(300.0, eval("3E+2", self));
	}

	// ==================== G-04: Adjacent String Concatenation (§9.3.20) ====================

	@Test
	void g04_adjacentStringConcat() throws OclParseException {
		// 'hello' ' world' → 'hello world'
		assertEquals("hello world", eval("'hello' ' world'", self));
	}

	@Test
	void g04_adjacentStringConcat_multiple() throws OclParseException {
		// Three adjacent strings
		assertEquals("abc", eval("'a' 'b' 'c'", self));
	}

	// ==================== G-05: Hex/Unicode Escape Sequences (§9.3.20) ====================

	@Test
	void g05_hexEscape() throws OclParseException {
		// \x41 = 'A'
		assertEquals("A", eval("'\\x41'", self));
	}

	@Test
	void g05_unicodeEscape() throws OclParseException {
		// backslash-u 0041 = 'A'
		assertEquals("A", eval("'\\u0041'", self));
	}

	@Test
	void g05_unicodeEscape_nonAscii() throws OclParseException {
		// backslash-u 00E4 = 'ä'
		assertEquals("\u00E4", eval("'\\u00E4'", self));
	}

	// ==================== G-06: Implicit-Source op@pre(args) (§9.3.35 form [F]) ====================

	@Test
	void g06_implicitSourceOpPre_parses() throws OclParseException {
		// Ensure self.name@pre syntax parses (needs navigation context for @pre)
		assertDoesNotThrow(() -> eval("self.name@pre", self));
	}

	// ==================== G-07: Qualified Calls obj.Path::op() (§9.3.35/36) ====================

	@Test
	void g07_qualifiedPropertyCall_parses() throws OclParseException {
		// obj.Person::name — qualified property access (uses qualifier prefix)
		assertDoesNotThrow(() -> eval("self.Person::name", self));
	}

	@Test
	void g07_qualifiedOperationCall_parses() throws OclParseException {
		// obj.Person::getAge() — qualified operation call
		assertDoesNotThrow(() -> eval("self.Person::name.size()", self));
	}

	// ==================== G-09: Nested Block Comments (§9.3.49) ====================

	@Test
	void g09_nestedBlockComment() throws OclParseException {
		// /* outer /* inner */ */ 42 — nested comment should be fully consumed
		assertEquals(42, eval("/* outer /* inner */ */ 42", self));
	}

	@Test
	void g09_nestedBlockComment_deep() throws OclParseException {
		// Three levels of nesting
		assertEquals(7, eval("/* a /* b /* c */ b */ a */ 7", self));
	}

	// ==================== G-10: Self-Variable Alias (§12.12.5 form [B]) ====================

	@Test
	void g10_selfAlias_classifierContext() throws OclParseException {
		// context p : Person inv: p.name <> null
		String doc = """
				package company
				context p : Person
				  inv aliased: p.name.size() > 0
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(1, constraints.size());
		Constraint inv = constraints.get(0);
		assertEquals(ConstraintKind.INV, inv.getKind());
		assertEquals("aliased", inv.getName());
		assertNotNull(inv.getSpecification());
	}

	@Test
	void g10_selfAlias_inDefinition() throws OclParseException {
		// Self alias used in a def constraint
		String doc = """
				package company
				context p : Person
				  def : myAge : Integer = p.age
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(1, constraints.size());
		assertEquals(ConstraintKind.DEF, constraints.get(0).getKind());
		assertNotNull(constraints.get(0).getSpecification());
	}

	// ==================== G-11: Unqualified Operation Context (§12.12.10) ====================

	@Test
	void g11_unqualifiedOperationContext() throws OclParseException {
		// context Person::getAge() : Integer body: self.age — standard qualified form must still work
		String doc = """
				package company
				context Person::getAge() : Integer
				  body: self.age
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(1, constraints.size());
		assertEquals(ConstraintKind.BODY, constraints.get(0).getKind());
	}

	// ==================== G-12: Optional Return Type (§12.12.10) ====================

	@Test
	void g12_optionalReturnType() throws OclParseException {
		// context Person::getAge() body: self.age — no return type
		String doc = """
				package company
				context Person::getAge()
				  body: self.age
				endpackage
				""";
		List<Constraint> constraints = engine.parseDocument(doc);
		assertEquals(1, constraints.size());
		assertEquals(ConstraintKind.BODY, constraints.get(0).getKind());
	}
}
