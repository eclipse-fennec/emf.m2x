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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Additional tests targeting remaining uncovered branches in OclAstBuilder:
 * <ul>
 *   <li>let without type annotation (L394 false branch)</li>
 *   <li>iterate without typed accumulator (L647 false branch)</li>
 *   <li>typed iterator variables in iterator expressions (L592)</li>
 *   <li>resolveCollectionType switch: OrderedSet, Bag (L741-746)</li>
 *   <li>map type expression in type context (L696 resolveMapType)</li>
 *   <li>implicit operation call without source (L443 visitOperationCallExp)</li>
 *   <li>2-segment pathName where enum is NOT found → resolveQualifiedName (L168 false, L174)</li>
 *   <li>collection literal "Collection" kind → default branch (L935 resolveCollectionKind)</li>
 * </ul>
 */
class OclAstBuilderBranchTest2 extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject charlie;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 65000.0, true);
		bob = createPerson("Bob", 22, 42000.0, false);
		charlie = createPerson("Charlie", 45, 95000.0, true);
		company = createCompany("TestCorp", alice, bob, charlie);
	}

	// === Let without type annotation (L394 false branch) ===

	@Test
	void let_withoutTypeAnnotation() throws OclParseException {
		assertEquals(42, eval("let x = 42 in x", alice));
	}

	@Test
	void let_multipleWithoutType() throws OclParseException {
		assertEquals(30, eval("let a = 10, b = 20 in a + b", alice));
	}

	@Test
	void let_mixedTypedAndUntyped() throws OclParseException {
		// First binding typed, second untyped
		assertEquals(30, eval("let x : Integer = 10, y = 20 in x + y", alice));
	}

	// === Iterate without typed accumulator (L647 false branch) ===

	@Test
	void iterate_untypedAccumulator() throws OclParseException {
		// No type annotation on accumulator
		Object result = eval(
				"self.employees->iterate(e; acc = 0 | acc + e.age)",
				company);
		assertEquals(97, result);
	}

	@Test
	void iterate_untypedIteratorAndAccumulator() throws OclParseException {
		// Neither iterator nor accumulator have type annotations
		Object result = eval(
				"Sequence{1, 2, 3}->iterate(x; acc = 0 | acc + x)",
				alice);
		assertEquals(6, result);
	}

	// === Typed iterator variable in iterator expression (L592) ===

	@Test
	void iterator_typedVariable_select() throws OclParseException {
		// Explicitly typed iterator variable: select(e : Person | ...)
		Object result = eval(
				"self.employees->select(e : Person | e.age > 25)",
				company);
		assertTrue(result instanceof Collection<?>);
		assertEquals(2, ((Collection<?>) result).size());
	}

	@Test
	void iterator_typedVariable_forAll() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e : Person | e.age > 0)", company));
	}

	@Test
	void iterator_typedVariable_exists() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->exists(e : Person | e.name = 'Alice')", company));
	}

	// === resolveCollectionType switch: OrderedSet and Bag type expressions ===

	@Test
	void collectionType_orderedSetExpression() throws OclParseException {
		// Force parsing of OrderedSet(Integer) type expression via iterate
		Object result = eval(
				"self.employees->iterate(e : Person; acc : OrderedSet(String) = OrderedSet{} | acc->including(e.name))",
				company);
		assertNotNull(result);
	}

	@Test
	void collectionType_bagExpression() throws OclParseException {
		// Force parsing of Bag(Integer) type expression via iterate
		Object result = eval(
				"self.employees->iterate(e : Person; acc : Bag(Integer) = Bag{} | acc->including(e.age))",
				company);
		assertNotNull(result);
	}

	@Test
	void collectionType_sequenceExpression() throws OclParseException {
		Object result = eval(
				"self.employees->iterate(e; acc : Sequence(String) = Sequence{} | acc->including(e.name))",
				company);
		assertNotNull(result);
	}

	// === Map type expression (L696 resolveMapType path) ===

	@Test
	void mapTypeExpression_inIterate() throws OclParseException {
		// Forces map type resolution during parsing
		Object result = eval(
				"self.employees->iterate(e; acc : Map(String, Integer) = Map{} | acc->including(e.name, e.age))",
				company);
		assertNotNull(result);
	}

	// === Implicit operation call (L443 visitOperationCallExp) ===
	// This is an operation called without a source: opName(args) — implicit self
	// In practice, this would be an EOperation defined on the context class.
	// Our test model doesn't have EOperations on Person/Company, so this path
	// is harder to trigger through the standard tests.

	// === Empty collection literal with null parts (ANTLR may never produce null) ===
	// These are defensive branches that ANTLR grammar always produces non-null lists.
	// The empty collection tests in Batch 25 already cover the empty-but-non-null path.

	// === Chained implicit collect on nested collections ===
	// This exercises the copyType CollectionType branch (L546-552)

	@Test
	void implicitCollect_chainedProperty() throws OclParseException {
		// self.employees.name is implicit collect → Sequence of name
		Object result = eval("self.employees.name", company);
		assertTrue(result instanceof Collection<?>);
		Collection<?> names = (Collection<?>) result;
		assertEquals(3, names.size());
		assertTrue(names.contains("Alice"));
	}

	@Test
	void implicitCollect_chainedNavigation() throws OclParseException {
		// self.employees.employer.name — chained implicit collect
		Object result = eval("self.employees.employer.name", company);
		assertTrue(result instanceof Collection<?>);
	}

	// === Enum literal with non-existent literal name ===

	@Test
	void enumLiteral_nonExistentLiteral() {
		// Status::UNKNOWN — the enumeration exists, the literal does not.
		//
		// OCL v2.4 §9.3.9 resolves the literal via
		//   type.oclAsType(Enumeration).literal->select(l | l.name = ...)->any(true)
		// which is undefined for an empty selection, so the §8.3 invariant
		//   self.type = referredEnumLiteral.enumeration
		// cannot hold — the expression is not a well-formed AS instance. Eclipse OCL
		// reports "Unknown enumeration literal" after exhausting its alternatives.
		OclParseException failure = assertThrows(OclParseException.class,
				() -> eval("Status::UNKNOWN", alice));

		assertTrue(failure.getErrors().stream()
				.anyMatch(d -> d.getMessage().contains("Unknown enumeration literal")),
				() -> "diagnostics: " + failure.getErrors());
	}

	// === @pre marker (L492, L519, L562 — isMarkedPre branches) ===
	// @pre is a parser feature for pre/post conditions.
	// We can't test it in expression context without Complete OCL support.
	// The branches will remain uncovered until Complete OCL is implemented.
}
