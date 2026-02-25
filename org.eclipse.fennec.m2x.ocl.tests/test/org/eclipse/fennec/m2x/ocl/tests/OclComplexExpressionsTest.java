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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for complex OCL expressions combining multiple language features:
 * nested let/if, chained iterators, arithmetic in predicates,
 * boolean combinations, mixed collection operations.
 */
class OclComplexExpressionsTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject charlie;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 60000.0, true);
		bob = createPerson("Bob", 25, 45000.0, false);
		charlie = createPerson("Charlie", 35, 80000.0, true);
		company = createCompany("ACME", alice, bob, charlie);
	}

	// --- Nested let expressions ---

	@Test
	void nestedLet() throws OclParseException {
		assertEquals(15, eval("let a: Integer = 5 in let b: Integer = 10 in a + b", alice));
	}

	@Test
	void nestedLet_innerShadows() throws OclParseException {
		assertEquals(10, eval("let x: Integer = 5 in let x: Integer = 10 in x", alice));
	}

	@Test
	void let_withPropertyAccess() throws OclParseException {
		assertEquals("Hello Alice", eval(
				"let greeting: String = 'Hello' in greeting.concat(' ').concat(self.name)", alice));
	}

	// --- Nested if expressions ---

	@Test
	void nestedIf() throws OclParseException {
		assertEquals("young", eval(
				"if self.age > 40 then 'old' " +
				"else if self.age > 28 then 'middle' " +
				"else 'young' endif endif", bob));
	}

	@Test
	void nestedIf_middle() throws OclParseException {
		assertEquals("middle", eval(
				"if self.age > 40 then 'old' " +
				"else if self.age > 28 then 'middle' " +
				"else 'young' endif endif", alice));
	}

	// --- If with let ---

	@Test
	void ifWithLet() throws OclParseException {
		assertEquals(65000.0, eval(
				"let bonus: Real = 5000.0 in " +
				"if self.isMarried then self.salary + bonus " +
				"else self.salary endif", alice));
	}

	// --- Boolean short-circuit ---

	@Test
	void and_shortCircuit() throws OclParseException {
		// false and <anything> should be false without evaluating RHS
		assertEquals(false, eval("false and true", alice));
	}

	@Test
	void or_shortCircuit() throws OclParseException {
		// true or <anything> should be true without evaluating RHS
		assertEquals(true, eval("true or false", alice));
	}

	@Test
	void implies_falseAntecedent() throws OclParseException {
		// false implies <anything> is true
		assertEquals(true, eval("false implies false", alice));
	}

	@Test
	void implies_trueAntecedent() throws OclParseException {
		assertEquals(false, eval("true implies false", alice));
	}

	@Test
	void complexBoolean() throws OclParseException {
		assertEquals(true, eval(
				"(self.age > 25 and self.isMarried) or self.salary > 100000.0", alice));
	}

	// --- Arithmetic in predicates ---

	@Test
	void arithmeticInPredicate() throws OclParseException {
		// Select employees with salary above average (average ≈ 61666)
		assertEquals(1, eval(
				"self.employees->select(e | e.salary > 70000.0)->size()", company));
	}

	@Test
	void arithmeticWithLet() throws OclParseException {
		assertEquals(120000.0, eval(
				"let doubleSalary: Real = self.salary * 2.0 in doubleSalary", alice));
	}

	// --- Chained iterators ---

	@Test
	void selectThenCollect() throws OclParseException {
		Object result = eval(
				"self.employees->select(e | e.isMarried)->collect(e | e.name)", company);
		assertInstanceOf(List.class, result);
		assertEquals(List.of("Alice", "Charlie"), result);
	}

	@Test
	void rejectThenSize() throws OclParseException {
		assertEquals(2, eval(
				"self.employees->reject(e | e.age < 28)->size()", company));
	}

	@Test
	void selectThenForAll() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->select(e | e.isMarried)->forAll(e | e.salary > 50000.0)", company));
	}

	@Test
	void collectThenSelect() throws OclParseException {
		// Collect ages, then select those > 28
		Object result = eval(
				"self.employees->collect(e | e.age)->select(a | a > 28)", company);
		assertInstanceOf(Collection.class, result);
		assertEquals(2, ((Collection<?>) result).size());
	}

	// --- Collection operations combined ---

	@Test
	void union_thenSize() throws OclParseException {
		assertEquals(4, eval(
				"Sequence{1, 2}->union(Sequence{3, 4})->size()", alice));
	}

	@Test
	void flatten_nested() throws OclParseException {
		assertEquals(List.of(1, 2, 3, 4), eval(
				"Sequence{Sequence{1, 2}, Sequence{3, 4}}->flatten()", alice));
	}

	@Test
	void including_chain() throws OclParseException {
		assertEquals(3, eval(
				"Set{1, 2}->including(3)->size()", alice));
	}

	// --- Mixed number arithmetic ---

	@Test
	void intPlusReal() throws OclParseException {
		assertEquals(5.5, eval("3 + 2.5", alice));
	}

	@Test
	void intTimesReal() throws OclParseException {
		assertEquals(7.5, eval("3 * 2.5", alice));
	}

	@Test
	void intMinusReal() throws OclParseException {
		assertEquals(0.5, eval("3 - 2.5", alice));
	}

	// --- Parenthesized expressions ---

	@Test
	void parenthesized_addition() throws OclParseException {
		assertEquals(14, eval("(2 + 5) * 2", alice));
	}

	@Test
	void parenthesized_complex() throws OclParseException {
		assertEquals(true, eval("(3 + 4) > (2 * 3)", alice));
	}

	// --- Operator precedence ---

	@Test
	void precedence_multiplyBeforeAdd() throws OclParseException {
		assertEquals(11, eval("3 + 4 * 2", alice));
	}

	@Test
	void precedence_unaryMinus() throws OclParseException {
		assertEquals(-5, eval("-2 - 3", alice));
	}

	// --- Collect with expression ---

	@Test
	void collect_withComputation() throws OclParseException {
		// Collect salary * 1.1 (10% raise) for each employee
		Object result = eval(
				"self.employees->collect(e | e.salary * 1.1)", company);
		assertInstanceOf(List.class, result);
		List<?> salaries = (List<?>) result;
		assertEquals(3, salaries.size());
		assertEquals(66000.0, salaries.get(0)); // Alice: 60000 * 1.1
	}

	// --- Count in collections ---

	@Test
	void count_inSequence() throws OclParseException {
		assertEquals(2, eval("Sequence{1, 2, 3, 2, 1}->count(2)", alice));
	}

	@Test
	void count_notFound() throws OclParseException {
		assertEquals(0, eval("Sequence{1, 2, 3}->count(4)", alice));
	}

	// --- Product ---

	@Test
	void product_size() throws OclParseException {
		assertEquals(4, eval(
				"Set{1, 2}->product(Set{3, 4})->size()", alice));
	}
}
