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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.OclResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Static well-formedness rules of OCL v2.4 §8.3.7 and §9.3 that the parser enforces (#326).
 */
class OclWellFormednessTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	static void assertParseError(String expression, String messageFragment) {
		OclParseException failure = assertThrows(OclParseException.class,
				() -> engine.parse(expression, personClass), expression);
		assertTrue(failure.getMessage().contains(messageFragment), failure::getMessage);
	}

	static OclResult evalStrict(String expression) throws OclParseException {
		return engine.evaluateWithDiagnostics(engine.parse(expression, personClass), OclContext.of(self),
				engine.getDefaultOptions().withSpecStrict(true));
	}

	/** §8.3.7 CollectionLiteralExp [1] / §9.3.10 [1]. */
	@Nested
	class CollectionLiteral {

		@Test
		void collectionLiteral_isAParseError() {
			assertParseError("Collection{1, 2}", "'Collection' is abstract");
		}

		@Test
		void collectionType_staysValid() throws OclParseException {
			assertEquals(true, eval("Set{1}->oclIsKindOf(Collection(Integer))", self));
		}
	}

	/** §8.3.7 TupleLiteralExp [2], §8.2.2 TupleType [2]. */
	@Nested
	class TupleParts {

		@Test
		void duplicatePartName_isAParseError() {
			assertParseError("Tuple{a = 1, a = 2}", "Duplicate tuple part 'a'");
			assertParseError("Tuple{a = 1}.oclAsType(Tuple(a : Integer, a : String))", "Duplicate tuple part 'a'");
		}

		@Test
		void distinctPartNames_areFine() throws OclParseException {
			assertEquals(2, eval("Tuple{a = 1, b = 2}.b", self));
		}
	}

	/** §8.3.7 Variable [1], TupleLiteralPart [1], IterateExp [2]: literal initial values. */
	@Nested
	class DeclaredTypes {

		@Test
		void literalNotConformingToDeclaredType_isAParseError() {
			assertParseError("let x : Integer = 'abc' in x", "declared as Integer but initialized with a String");
			assertParseError("let x : String = 42 in x", "declared as String but initialized with a Integer");
			assertParseError("Tuple{a : Integer = 'x'}.a", "declared as Integer");
			assertParseError("Set{1, 2}->iterate(x; acc : String = 0 | acc)", "declared as String");
		}

		@Test
		void conformingLiterals_areFine() throws OclParseException {
			assertEquals(true, eval("let x : Real = 1 in x = 1.0", self));
			assertEquals("a", eval("let x : OclAny = 'a' in x", self));
			assertEquals(3, eval("Set{1, 2}->iterate(x; acc : Integer = 0 | acc + x)", self));
		}
	}

	/** §9.3.41 [1], §9.3.25 [A]: a variable may not hide one in scope — stricter than Eclipse. */
	@Nested
	class Shadowing {

		@Test
		void nestedLet_withSameName_isAParseError() {
			assertParseError("let x : Integer = 1 in let x : Integer = 2 in x", "Variable name already used: 'x'");
		}

		@Test
		void iterator_hidingLetVariable_isAParseError() {
			assertParseError("let x : Integer = 1 in Set{1}->collect(x | x)", "Variable name already used: 'x'");
		}

		@Test
		void nestedIterators_withSameName_areAParseError() {
			assertParseError("Set{1}->forAll(x | Set{2}->exists(x | x > 0))", "Variable name already used: 'x'");
			assertParseError("Set{1}->forAll(x, x | true)", "Variable name already used: 'x'");
			assertParseError("Set{1}->iterate(x; x : Integer = 0 | x)", "Variable name already used: 'x'");
		}

		@Test
		void variableNamedSelf_isAParseError() {
			// 'self' is a keyword; the escaped name _'self' is the variable self
			assertThrows(OclParseException.class, () -> engine.parse("let self : Integer = 1 in self", personClass));
			assertParseError("let _'self' : Integer = 1 in 2", "Variable name already used: 'self'");
		}

		@Test
		void siblingScopes_mayReuseNames() throws OclParseException {
			assertEquals(true, eval("Set{1}->forAll(x | x > 0) and Set{2}->exists(x | x > 1)", self));
			assertEquals(3, eval("(let x : Integer = 1 in x) + (let x : Integer = 2 in x)", self));
		}

		@Test
		void implicitIterators_mayNest() throws OclParseException {
			assertEquals(true, eval("Set{1}->forAll(Set{2}->exists(true))", self));
		}
	}

	/** §7.4.3, §9.3.26: let and iterate result need a declared type — enforced under specStrict. */
	@Nested
	class UntypedDeclarations {

		@Test
		void untypedLet_isAcceptedByDefault() throws OclParseException {
			assertEquals(2, eval("let x = 1 in x + 1", self));
			assertEquals(6, eval("Sequence{1, 2, 3}->iterate(i; acc = 0 | acc + i)", self));
		}

		@Test
		void untypedLet_isAnErrorUnderSpecStrict() throws OclParseException {
			OclResult let = evalStrict("let x = 1 in x + 1");
			assertSame(OclInvalid.INSTANCE, let.value());
			assertTrue(let.diagnostics().stream().anyMatch(d -> d.getMessage().contains("no declared type")));
			assertSame(OclInvalid.INSTANCE, evalStrict("Sequence{1, 2}->iterate(i; acc = 0 | acc + i)").value());
		}

		@Test
		void typedLet_isFineUnderSpecStrict() throws OclParseException {
			assertEquals(2L, evalStrict("let x : Integer = 1 in x + 1").value());
		}
	}
}
