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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Implicit sources (OCL v2.4 §7.6.2, §7.8, §9.3.25 [A], §9.3.35 [D], §9.4.1 [10]–[12], #320):
 * implicit collect of operations, the implicit source of an unqualified operation call, every
 * implicit iterator from the innermost outward, and named iterator variables as implicit source.
 */
class OclImplicitSourceTest extends AbstractOclTest {

	static EObject company;

	@BeforeAll
	static void setUp() {
		company = createCompany("ACME", createPerson("Alice", 30, 1.0, true), createPerson("Bob", 25, 1.0, false));
	}

	/** §7.6.2: collection.op(args) = collection->collect(op(args)). */
	@Nested
	class ImplicitCollectOfOperations {

		@Test
		void dotOperation_onCollectionLiteral_collects() throws OclParseException {
			assertEquals(true, eval("Set{'ab', 'c'}.size() = Bag{2, 1}", company));
			assertEquals(true, eval("Sequence{'a', 'b'}.toUpperCase() = Sequence{'A', 'B'}", company));
		}

		@Test
		void dotOperation_onNavigatedCollection_collects() throws OclParseException {
			assertEquals(List.of("ALICE", "BOB"), eval("self.employees.name.toUpperCase()", company));
			assertEquals(List.of("A", "B"), eval("self.employees.name.substring(1, 1)", company));
		}

		@Test
		void arrowOperation_staysOnTheCollection() throws OclParseException {
			assertEquals(2, eval("Set{'ab', 'c'}->size()", company));
			assertEquals(1, eval("Set{1}->size()", company));
		}

		@Test
		void argumentsSeeTheEnclosingScope() throws OclParseException {
			assertEquals(true, eval("let s : String = '!' in Sequence{'a'}.concat(s) = Sequence{'a!'}", company));
		}
	}

	/** §9.3.35 [D]: an unqualified operation call applies to the innermost implicit source that has it. */
	@Nested
	class ImplicitOperationSource {

		@Test
		void implicitIterator_isTheSourceOfAStandardOperation() throws OclParseException {
			assertEquals(true, eval("Set{'ab', 'c'}->collect(size()) = Bag{2, 1}", company));
			assertEquals(true, eval("Set{'ab'}->collect(toUpperCase()) = Bag{'AB'}", company));
		}

		@Test
		void oclAnyOperation_appliesToTheIterator_notToSelf() throws OclParseException {
			// §7.4.7: aCollection->collect(oclAsType(String))
			assertEquals(true, eval("Set{'ab'}->collect(oclAsType(String)) = Bag{'ab'}", company));
			assertEquals(2, eval("self.employees->select(oclIsKindOf(Person))->size()", company));
			assertEquals(false, eval("self.employees->exists(oclIsUndefined())", company));
			assertEquals(true, eval("self.employees->forAll(oclIsTypeOf(Person))", company));
		}
	}

	/** §7.8, §9.4.1 [10]: implicit sources from the innermost scope outward, then self. */
	@Nested
	class NestedImplicitSources {

		@Test
		void outerImplicitIterator_isFoundBehindAnInnerOne() throws OclParseException {
			assertEquals(true, eval("self.employees->forAll(Set{1}->forAll(age > 0))", company));
		}

		@Test
		void innermostSourceWins() throws OclParseException {
			// both the person (implicit) and self (company) have a name: the innermost wins
			assertEquals(true, eval("self.employees->forAll(name <> self.name)", company));
		}

		@Test
		void nestedImplicitIterators_reachTheOuterOne() throws OclParseException {
			assertEquals(true, eval("self.employees->forAll(Set{'x'}->forAll(name.size() > 2))", company));
		}
	}

	/** §9.3.25 [A]: a named iterator variable is an implicit source too — stricter than Eclipse. */
	@Nested
	class NamedIteratorAsImplicitSource {

		@Test
		void propertyResolvesAgainstTheNamedIterator() throws OclParseException {
			assertEquals(List.of("Alice", "Bob"), eval("self.employees->collect(p | name)", company));
			assertEquals(true, eval("self.employees->forAll(p | age > 0)", company));
		}

		@Test
		void iterateVariables_areImplicitSources() throws OclParseException {
			assertEquals(55, eval("self.employees->iterate(p; acc : Integer = 0 | acc + age)", company));
		}

		@Test
		void letVariable_isNoImplicitSource() throws OclParseException {
			// §9.3.41 adds let variables with mayBeImplicit = false: name stays self.name
			assertEquals("ACME", eval("let p : Person = self.employees->first() in name", company));
		}
	}
}
