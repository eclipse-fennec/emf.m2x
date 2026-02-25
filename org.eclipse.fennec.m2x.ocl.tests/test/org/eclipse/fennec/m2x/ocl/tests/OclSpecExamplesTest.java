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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests derived from examples in the OCL v2.4 specification.
 * Uses the company/person model as a stand-in for the spec's
 * UML examples (Person ≈ Employee, Company ≈ Department).
 *
 * <p>References are to OMG OCL v2.4 formal/14-02-03.
 */
class OclSpecExamplesTest extends AbstractOclTest {

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

	// --- Section 7.5.3: Property Call Expressions ---

	@Test
	void spec_selfPropertyAccess() throws OclParseException {
		// self.name (the name of the context object)
		assertEquals("Alice", eval("self.name", alice));
	}

	@Test
	void spec_navigationThroughReference() throws OclParseException {
		// Navigation through an association end
		assertEquals("ACME", eval("self.employer.name", alice));
	}

	// --- Section 7.5.4: Collection Operations ---

	@Test
	void spec_sizeOperation() throws OclParseException {
		// self.employees->size()
		assertEquals(3, eval("self.employees->size()", company));
	}

	@Test
	void spec_notEmpty() throws OclParseException {
		// self.employees->notEmpty()
		assertEquals(true, eval("self.employees->notEmpty()", company));
	}

	// --- Section 7.5.5: Iterators ---

	@Test
	void spec_forAll() throws OclParseException {
		// All employees must have age > 18
		assertEquals(true, eval(
				"self.employees->forAll(e | e.age > 18)", company));
	}

	@Test
	void spec_exists() throws OclParseException {
		// There exists an employee with salary > 70000
		assertEquals(true, eval(
				"self.employees->exists(e | e.salary > 70000.0)", company));
	}

	@Test
	void spec_select() throws OclParseException {
		// Select married employees
		Object result = eval(
				"self.employees->select(e | e.isMarried)", company);
		assertInstanceOf(Collection.class, result);
		assertEquals(2, ((Collection<?>) result).size());
	}

	@Test
	void spec_collect() throws OclParseException {
		// Collect names of all employees
		Object result = eval(
				"self.employees->collect(e | e.name)", company);
		assertInstanceOf(List.class, result);
		assertTrue(((List<?>) result).contains("Alice"));
		assertTrue(((List<?>) result).contains("Bob"));
		assertTrue(((List<?>) result).contains("Charlie"));
	}

	// --- Section 7.5.6: If Expressions ---

	@Test
	void spec_ifExpression() throws OclParseException {
		assertEquals("married", eval(
				"if self.isMarried then 'married' else 'single' endif", alice));
	}

	@Test
	void spec_ifExpression_else() throws OclParseException {
		assertEquals("single", eval(
				"if self.isMarried then 'married' else 'single' endif", bob));
	}

	// --- Section 7.5.7: Let Expressions ---

	@Test
	void spec_letExpression() throws OclParseException {
		// let income : Real = self.salary in income > 40000
		assertEquals(true, eval(
				"let income: Real = self.salary in income > 40000.0", alice));
	}

	// --- Section 11.5: Integer Operations ---

	@Test
	void spec_integerArithmetic() throws OclParseException {
		assertEquals(7, eval("3 + 4", alice));
	}

	@Test
	void spec_integerDivision() throws OclParseException {
		// OCL / always returns Real
		assertEquals(2.5, eval("5 / 2", alice));
	}

	@Test
	void spec_integerDiv() throws OclParseException {
		// div returns Integer
		assertEquals(2, eval("5.div(2)", alice));
	}

	@Test
	void spec_integerMod() throws OclParseException {
		assertEquals(1, eval("5.mod(2)", alice));
	}

	@Test
	void spec_integerAbs() throws OclParseException {
		assertEquals(5, eval("(-5).abs()", alice));
	}

	// --- Section 11.6: Real Operations ---

	@Test
	void spec_realFloor() throws OclParseException {
		assertEquals(3, eval("(3.7).floor()", alice));
	}

	@Test
	void spec_realCeiling() throws OclParseException {
		assertEquals(4, eval("(3.2).ceiling()", alice));
	}

	@Test
	void spec_realRound() throws OclParseException {
		assertEquals(4, eval("(3.5).round()", alice));
	}

	// --- Section 11.7: String Operations ---

	@Test
	void spec_stringSize() throws OclParseException {
		assertEquals(5, eval("'hello'.size()", alice));
	}

	@Test
	void spec_stringConcat() throws OclParseException {
		assertEquals("helloworld", eval("'hello'.concat('world')", alice));
	}

	@Test
	void spec_stringSubstring() throws OclParseException {
		// OCL substring is 1-based, inclusive on both ends
		assertEquals("ell", eval("'hello'.substring(2, 4)", alice));
	}

	@Test
	void spec_stringToUpperCase() throws OclParseException {
		assertEquals("HELLO", eval("'hello'.toUpperCase()", alice));
	}

	@Test
	void spec_stringToLowerCase() throws OclParseException {
		assertEquals("hello", eval("'HELLO'.toLowerCase()", alice));
	}

	// --- Section 11.7.3: Sequence Operations ---

	@Test
	void spec_sequenceFirst() throws OclParseException {
		assertEquals(1, eval("Sequence{1, 2, 3}->first()", alice));
	}

	@Test
	void spec_sequenceLast() throws OclParseException {
		assertEquals(3, eval("Sequence{1, 2, 3}->last()", alice));
	}

	@Test
	void spec_sequenceAt() throws OclParseException {
		// 1-based indexing
		assertEquals(2, eval("Sequence{1, 2, 3}->at(2)", alice));
	}

	@Test
	void spec_sequenceIncludes() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3}->includes(2)", alice));
	}

	@Test
	void spec_sequenceExcludes() throws OclParseException {
		assertEquals(true, eval("Sequence{1, 2, 3}->excludes(4)", alice));
	}

	// --- Section 11.7.1: Set Operations ---

	@Test
	void spec_setUnion() throws OclParseException {
		assertEquals(4, eval("Set{1, 2}->union(Set{3, 4})->size()", alice));
	}

	@Test
	void spec_setIntersection() throws OclParseException {
		assertEquals(2, eval("Set{1, 2, 3}->intersection(Set{2, 3, 4})->size()", alice));
	}

	@Test
	void spec_setMinus() throws OclParseException {
		assertEquals(1, eval("(Set{1, 2, 3} - Set{2, 3})->size()", alice));
	}

	// --- Section 11.2: OclAny Operations ---

	@Test
	void spec_oclIsUndefined() throws OclParseException {
		assertEquals(true, eval("null.oclIsUndefined()", alice));
	}

	@Test
	void spec_oclIsInvalid() throws OclParseException {
		assertEquals(true, eval("invalid.oclIsInvalid()", alice));
	}

	@Test
	void spec_oclAsSet() throws OclParseException {
		assertEquals(1, eval("42.oclAsSet()->size()", alice));
	}

	// --- Section 11.9: Iterate ---

	@Test
	void spec_iterate_sum() throws OclParseException {
		assertEquals(6, eval(
				"Sequence{1, 2, 3}->iterate(i; acc : Integer = 0 | acc + i)", alice));
	}

	// --- Invariant-style expressions ---

	@Test
	void spec_invariant_agePositive() throws OclParseException {
		assertEquals(true, eval("self.age > 0", alice));
	}

	@Test
	void spec_invariant_salaryNonNegative() throws OclParseException {
		assertEquals(true, eval("self.salary >= 0.0", alice));
	}

	@Test
	void spec_invariant_nameNotEmpty() throws OclParseException {
		assertEquals(true, eval("self.name.size() > 0", alice));
	}

	@Test
	void spec_invariant_employeesSalaryPositive() throws OclParseException {
		assertEquals(true, eval(
				"self.employees->forAll(e | e.salary > 0.0)", company));
	}
}
