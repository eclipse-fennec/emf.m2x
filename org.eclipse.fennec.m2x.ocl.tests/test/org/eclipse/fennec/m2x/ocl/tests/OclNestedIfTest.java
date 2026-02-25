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
import org.junit.jupiter.api.Test;

/**
 * Tests for nested and complex if-then-else expressions.
 */
class OclNestedIfTest extends AbstractOclTest {

	static EObject alice;
	static EObject bob;
	static EObject carol;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 80000.0, true);
		bob = createPerson("Bob", 17, 0.0, false);
		carol = createPerson("Carol", 65, 120000.0, true);
		company = createCompany("ACME", alice, bob, carol);
	}

	// --- Nested if in then-branch ---

	@Test
	void nestedIf_thenBranch() throws OclParseException {
		assertEquals("senior", eval(
				"if self.age >= 18 then " +
						"if self.age >= 30 then 'senior' else 'junior' endif " +
						"else 'minor' endif", alice));
	}

	@Test
	void nestedIf_thenBranch_junior() throws OclParseException {
		EObject young = createPerson("Young", 20, 30000.0, false);
		assertEquals("junior", eval(
				"if self.age >= 18 then " +
						"if self.age >= 30 then 'senior' else 'junior' endif " +
						"else 'minor' endif", young));
	}

	@Test
	void nestedIf_thenBranch_minor() throws OclParseException {
		assertEquals("minor", eval(
				"if self.age >= 18 then " +
						"if self.age >= 30 then 'senior' else 'junior' endif " +
						"else 'minor' endif", bob));
	}

	// --- Nested if in else-branch ---

	@Test
	void nestedIf_elseBranch() throws OclParseException {
		assertEquals("rich", eval(
				"if self.salary > 100000.0 then 'rich' " +
						"else if self.salary > 50000.0 then 'comfortable' " +
						"else 'modest' endif endif", carol));
	}

	@Test
	void nestedIf_elseBranch_middle() throws OclParseException {
		assertEquals("comfortable", eval(
				"if self.salary > 100000.0 then 'rich' " +
						"else if self.salary > 50000.0 then 'comfortable' " +
						"else 'modest' endif endif", alice));
	}

	@Test
	void nestedIf_elseBranch_last() throws OclParseException {
		assertEquals("modest", eval(
				"if self.salary > 100000.0 then 'rich' " +
						"else if self.salary > 50000.0 then 'comfortable' " +
						"else 'modest' endif endif", bob));
	}

	// --- Triple nested ---

	@Test
	void tripleNested() throws OclParseException {
		assertEquals("A", eval(
				"if self.age > 60 then 'A' " +
						"else if self.age > 40 then 'B' " +
						"else if self.age > 20 then 'C' " +
						"else 'D' endif endif endif", carol));
	}

	@Test
	void tripleNested_middleMatch() throws OclParseException {
		assertEquals("C", eval(
				"if self.age > 60 then 'A' " +
						"else if self.age > 40 then 'B' " +
						"else if self.age > 20 then 'C' " +
						"else 'D' endif endif endif", alice));
	}

	// --- If with arithmetic results ---

	@Test
	void if_arithmeticResult() throws OclParseException {
		assertEquals(60, eval(
				"if self.age >= 30 then self.age * 2 else self.age endif", alice));
	}

	@Test
	void if_arithmeticResult_elseBranch() throws OclParseException {
		assertEquals(17, eval(
				"if self.age >= 30 then self.age * 2 else self.age endif", bob));
	}

	// --- If inside iterator ---

	@Test
	void if_insideCollect() throws OclParseException {
		Object result = eval(
				"self.employees->collect(e | if e.age >= 18 then 'adult' else 'minor' endif)",
				company);
		assertEquals(List.of("adult", "minor", "adult"), result);
	}

	@Test
	void if_insideSelect() throws OclParseException {
		// Select employees who are adults
		assertEquals(2, eval(
				"self.employees->select(e | if e.age >= 18 then true else false endif)->size()",
				company));
	}

	@Test
	void if_insideIterate() throws OclParseException {
		// Count adults using iterate
		assertEquals(2, eval(
				"self.employees->iterate(e; acc : Integer = 0 | " +
						"if e.age >= 18 then acc + 1 else acc endif)", company));
	}

	// --- If with boolean combinations ---

	@Test
	void if_andCondition() throws OclParseException {
		assertEquals("qualified", eval(
				"if self.age >= 25 and self.salary > 50000.0 then 'qualified' else 'not yet' endif",
				alice));
	}

	@Test
	void if_orCondition() throws OclParseException {
		assertEquals("eligible", eval(
				"if self.age >= 60 or self.salary > 100000.0 then 'eligible' else 'not eligible' endif",
				carol));
	}

	@Test
	void if_notCondition() throws OclParseException {
		assertEquals("single", eval(
				"if not self.isMarried then 'single' else 'married' endif", bob));
	}

	// --- If with let ---

	@Test
	void let_thenIf() throws OclParseException {
		assertEquals("high", eval(
				"let threshold : Real = 50000.0 in " +
						"if self.salary > threshold then 'high' else 'low' endif", alice));
	}

	@Test
	void if_withLetInBranch() throws OclParseException {
		assertEquals(40, eval(
				"if self.age >= 30 then " +
						"let bonus : Integer = 10 in self.age + bonus " +
						"else self.age endif", alice));
	}

	// --- If with null ---

	@Test
	void if_nullCheck() throws OclParseException {
		assertEquals("has employer", eval(
				"if self.employer.oclIsUndefined() then 'no employer' else 'has employer' endif",
				alice));
	}

	@Test
	void if_nullCheck_noEmployer() throws OclParseException {
		EObject standalone = createPerson("Standalone", 20, 0.0, false);
		assertEquals("no employer", eval(
				"if self.employer.oclIsUndefined() then 'no employer' else 'has employer' endif",
				standalone));
	}

	// --- If returns collection ---

	@Test
	void if_returnsSequence() throws OclParseException {
		Object result = eval(
				"if true then Sequence{1, 2, 3} else Sequence{4, 5} endif", alice);
		assertEquals(List.of(1, 2, 3), result);
	}
}
