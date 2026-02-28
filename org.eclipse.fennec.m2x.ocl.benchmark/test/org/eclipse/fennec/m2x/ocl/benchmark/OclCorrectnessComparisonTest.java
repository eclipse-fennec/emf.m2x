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
package org.eclipse.fennec.m2x.ocl.benchmark;

import org.eclipse.emf.ecore.EObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Correctness comparison: same OCL expressions evaluated on Fennec and Eclipse OCL Classic.
 *
 * <p>Each test asserts that both engines produce identical results for the same expression.
 */
class OclCorrectnessComparisonTest extends AbstractComparisonTest {

	static EObject alice;
	static EObject bob;
	static EObject carol;
	static EObject acme;

	@BeforeAll
	static void setUpData() {
		alice = createPerson("Alice", 30, 50000.0, true);
		bob = createPerson("Bob", 25, 35000.0, false);
		carol = createPerson("Carol", 45, 80000.0, true);
		acme = createCompany("ACME", alice, bob, carol);
	}

	// --- Primitive Operations ---

	@Nested
	@DisplayName("Primitive Operations")
	class PrimitiveOps {

		@Test
		void integerAddition() {
			assertSameResult("1 + 2", alice);
		}

		@Test
		void integerMultiplication() {
			assertSameResult("3 * 7", alice);
		}

		@Test
		void integerDivision() {
			assertSameResult("10.div(3)", alice);
		}

		@Test
		void integerMod() {
			assertSameResult("10.mod(3)", alice);
		}

		@Test
		void integerNegate() {
			assertSameResult("-(5)", alice);
		}

		@Test
		void integerAbs() {
			assertSameResult("(-5).abs()", alice);
		}

		@Test
		void integerMax() {
			assertSameResult("3.max(7)", alice);
		}

		@Test
		void integerMin() {
			assertSameResult("3.min(7)", alice);
		}

		@Test
		void realDivision() {
			assertSameResult("10.0 / 3.0", alice);
		}

		@Test
		void realFloor() {
			assertSameResult("3.7.floor()", alice);
		}

		@Test
		void realRound() {
			assertSameResult("3.5.round()", alice);
		}

		@Test
		void stringSize() {
			assertSameResult("'hello'.size()", alice);
		}

		@Test
		void stringConcat() {
			assertSameResult("'hello'.concat(' world')", alice);
		}

		@Test
		void stringSubstring() {
			assertSameResult("'hello'.substring(2, 4)", alice);
		}

		@Test
		void stringToUpper() {
			assertSameResult("'hello'.toUpperCase()", alice);
		}

		@Test
		void stringToLower() {
			assertSameResult("'HELLO'.toLowerCase()", alice);
		}

		@Test
		void booleanAnd() {
			assertSameResult("true and false", alice);
		}

		@Test
		void booleanOr() {
			assertSameResult("true or false", alice);
		}

		@Test
		void booleanNot() {
			assertSameResult("not true", alice);
		}

		@Test
		void booleanImplies() {
			assertSameResult("false implies true", alice);
		}
	}

	// --- Navigation ---

	@Nested
	@DisplayName("Navigation")
	class Navigation {

		@Test
		void selfName() {
			assertSameResult("self.name", alice);
		}

		@Test
		void selfAge() {
			assertSameResult("self.age", alice);
		}

		@Test
		void selfSalary() {
			assertSameResult("self.salary", alice);
		}

		@Test
		void selfIsMarried() {
			assertSameResult("self.isMarried", alice);
		}

		@Test
		void companyName() {
			assertSameResult("self.name", acme);
		}

		@Test
		void employeesSize() {
			assertSameResult("self.employees->size()", acme);
		}
	}

	// --- Collection Operations ---

	@Nested
	@DisplayName("Collection Operations")
	class CollectionOps {

		@Test
		void select() {
			assertSameResult("self.employees->select(e | e.age > 28)->size()", acme);
		}

		@Test
		void reject() {
			assertSameResult("self.employees->reject(e | e.isMarried)->size()", acme);
		}

		@Test
		void collect() {
			assertSameResult("self.employees->collect(e | e.name)->size()", acme);
		}

		@Test
		void forAll() {
			assertSameResult("self.employees->forAll(e | e.age > 20)", acme);
		}

		@Test
		void exists() {
			assertSameResult("self.employees->exists(e | e.name = 'Alice')", acme);
		}

		@Test
		void isEmpty() {
			assertSameResult("self.employees->isEmpty()", acme);
		}

		@Test
		void notEmpty() {
			assertSameResult("self.employees->notEmpty()", acme);
		}

		@Test
		void any() {
			assertSameResult("self.employees->any(e | e.name = 'Bob').age", acme);
		}

		@Test
		void isUnique() {
			assertSameResult("self.employees->isUnique(e | e.name)", acme);
		}

		@Test
		void one() {
			assertSameResult("self.employees->one(e | e.name = 'Alice')", acme);
		}
	}

	// --- Type Operations ---

	@Nested
	@DisplayName("Type Operations")
	class TypeOps {

		@Test
		void oclIsKindOf() {
			assertSameResult("self.oclIsKindOf(Person)", alice);
		}

		@Test
		void oclIsTypeOf() {
			assertSameResult("self.oclIsTypeOf(Person)", alice);
		}

		@Test
		void oclIsUndefinedOnNonNull() {
			assertSameResult("self.name.oclIsUndefined()", alice);
		}

		@Test
		void oclAsType() {
			assertSameResult("self.oclAsType(Person).name", alice);
		}
	}

	// --- Let and If ---

	@Nested
	@DisplayName("Let and If Expressions")
	class LetAndIf {

		@Test
		void letExpression() {
			assertSameResult("let x : Integer = self.age in x * 2", alice);
		}

		@Test
		void ifThenElse() {
			assertSameResult("if self.age > 25 then 'senior' else 'junior' endif", alice);
		}

		@Test
		void nestedIf() {
			assertSameResult(
					"if self.age > 40 then 'manager' else if self.age > 25 then 'senior' else 'junior' endif endif",
					alice);
		}

		@Test
		void letWithNavigation() {
			assertSameResult("let n : String = self.name in n.size()", alice);
		}
	}

	// --- Tuple and Set Literals ---

	@Nested
	@DisplayName("Tuple and Collection Literals")
	class TupleAndCollections {

		@Test
		void setIncludes() {
			assertSameResult("Set{1, 2, 3}->includes(2)", alice);
		}

		@Test
		void setExcludes() {
			assertSameResult("Set{1, 2, 3}->excludes(5)", alice);
		}

		@Test
		void sequenceFirst() {
			assertSameResult("Sequence{1, 2, 3}->first()", alice);
		}

		@Test
		void sequenceLast() {
			assertSameResult("Sequence{1, 2, 3}->last()", alice);
		}

		@Test
		void orderedSetAt() {
			assertSameResult("OrderedSet{10, 20, 30}->at(2)", alice);
		}

		@Test
		void setSize() {
			assertSameResult("Set{1, 2, 3, 2, 1}->size()", alice);
		}

		@Test
		void sequenceSize() {
			assertSameResult("Sequence{1, 2, 3, 2, 1}->size()", alice);
		}
	}

	// --- Comparison and Equality ---

	@Nested
	@DisplayName("Comparison and Equality")
	class Comparison {

		@Test
		void integerEquals() {
			assertSameResult("1 = 1", alice);
		}

		@Test
		void integerNotEquals() {
			assertSameResult("1 <> 2", alice);
		}

		@Test
		void integerLessThan() {
			assertSameResult("1 < 2", alice);
		}

		@Test
		void integerGreaterThan() {
			assertSameResult("2 > 1", alice);
		}

		@Test
		void stringEquals() {
			assertSameResult("'hello' = 'hello'", alice);
		}

		@Test
		void stringNotEquals() {
			assertSameResult("'hello' <> 'world'", alice);
		}
	}
}
