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

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Spec cases the audit found correct but untested (#330).
 */
class OclSpecCoverageTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 1.0, true);
	}

	/** §7.4.8 precedence. */
	@Nested
	class Precedence {

		@Test
		void notBindsTighterThanEquality() throws OclParseException {
			// (not 1) = 1: 'not' on an Integer is invalid
			assertInvalid("not 1 = 1", self);
			assertEquals(false, eval("not Set{true}->includes(true)", self));
		}

		@Test
		void unaryMinusBindsLooserThanNavigation() throws OclParseException {
			// -(1.abs())
			assertEquals(-1, eval("-1.abs()", self));
		}

		@Test
		void letExtendsAsFarRightAsPossible() throws OclParseException {
			// §7.4.8: "a + let … in a + a" — the let body is "x + 3"
			assertEquals(6, eval("1 + let x : Integer = 2 in x + 3", self));
		}

		@Test
		void orXorImplies_leftToRightByLevel() throws OclParseException {
			assertEquals(false, eval("true or true xor true", self));
			assertEquals(true, eval("true xor true implies false", self));
		}
	}

	/** §7.4.7, §7.5.11, §7.5.15. */
	@Nested
	class LiteralsAndCasts {

		@Test
		void tupleEquality_ignoresPartOrder() throws OclParseException {
			assertEquals(true, eval("Tuple{a = 1, b = 2} = Tuple{b = 2, a = 1}", self));
		}

		@Test
		void rangeWithNonIntegerBound_isAnError() throws OclParseException {
			assertInvalid("Sequence{1..'a'}", self);
			assertHasError("Sequence{1..'a'}", self, "Integer bounds");
		}

		@Test
		void collectionCast_withArrow() throws OclParseException {
			assertInvalid("Set{1}->oclAsType(Set(String))", self);
		}
	}

	/** §11.7. */
	@Nested
	class Collections {

		@Test
		void setAndOrderedSet_areDifferentKinds() throws OclParseException {
			assertEquals(false, eval("Set{1, 2} = OrderedSet{1, 2}", self));
			assertEquals(false, eval("OrderedSet{1, 2} = Set{1, 2}", self));
		}

		@Test
		void selectByKind_dropsNull_andKeepsTheKind() throws OclParseException {
			// §11.7.1: "the non-null elements of self of type t"
			assertEquals(1, eval("Sequence{1, null, 'a'}->selectByKind(Integer)->size()", self));
			assertEquals(true, eval("Bag{1, 1, 'a'}->selectByKind(Integer) = Bag{1, 1}", self));
			assertEquals(true, eval("OrderedSet{2, 'a', 1}->selectByType(Integer) = OrderedSet{2, 1}", self));
		}

		@Test
		void iterate_withNullBodyAndInvalidInit() throws OclParseException {
			assertNull(eval("Sequence{1, 2}->iterate(x; acc : Integer = 0 | null)", self));
			assertInvalid("Sequence{}->iterate(x; acc : Integer = invalid | acc)", self);
		}

		@Test
		void orderedSetInsertAt() throws OclParseException {
			assertEquals(List.of(1, 2, 3), eval("OrderedSet{1, 2, 3}->insertAt(1, 3)", self));
			assertEquals(List.of(1, 2, 3), eval("OrderedSet{1, 2, 3}->insertAt(4, 3)", self));
			assertEquals(List.of(1, 2, 3, 9), eval("OrderedSet{1, 2, 3}->insertAt(4, 9)", self));
			assertInvalid("OrderedSet{1, 2, 3}->insertAt(5, 9)", self);
		}
	}

	/** §7.4.9: a model operation named like an infix operator is called with the infix syntax. */
	@Nested
	class UserDefinedInfixOperator {

		@Test
		void plusOperationOfTheModel() throws OclParseException {
			EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
			pkg.setName("money");
			pkg.setNsURI("http://test/money");
			pkg.setNsPrefix("money");
			EAnnotation delegates = EcoreFactory.eINSTANCE.createEAnnotation();
			delegates.setSource(EcorePackage.eNS_URI);
			delegates.getDetails().put("invocationDelegates", "http://www.eclipse.org/fennec/m2x/ocl/1.0");
			pkg.getEAnnotations().add(delegates);

			EClass money = EcoreFactory.eINSTANCE.createEClass();
			money.setName("Money");
			pkg.getEClassifiers().add(money);
			EAttribute amount = EcoreFactory.eINSTANCE.createEAttribute();
			amount.setName("amount");
			amount.setEType(EcorePackage.Literals.EINT);
			money.getEStructuralFeatures().add(amount);

			EOperation plus = EcoreFactory.eINSTANCE.createEOperation();
			plus.setName("+");
			plus.setEType(EcorePackage.Literals.EINT);
			EParameter other = EcoreFactory.eINSTANCE.createEParameter();
			other.setName("other");
			other.setEType(money);
			plus.getEParameters().add(other);
			EAnnotation body = EcoreFactory.eINSTANCE.createEAnnotation();
			body.setSource("http://www.eclipse.org/fennec/m2x/ocl/1.0");
			body.getDetails().put("body", "self.amount + other.amount");
			plus.getEAnnotations().add(body);
			money.getEOperations().add(plus);

			engine.installDelegates();
			try {
				EObject a = pkg.getEFactoryInstance().create(money);
				a.eSet(amount, 3);
				EObject b = pkg.getEFactoryInstance().create(money);
				b.eSet(amount, 4);
				assertEquals(7, engine.evaluate(engine.parse("self + other", money, Map.of("other", money)),
						OclContext.of(a, Map.of("other", b))));
				assertEquals(7, ((InternalEObject) a).eInvoke(plus, ECollections.asEList(b)));
			} catch (InvocationTargetException e) {
				throw new AssertionError(e);
			} finally {
				engine.uninstallDelegates();
			}
		}
	}
}
