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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Spec conformance of the OclAny type operations (OCL v2.4 §11.3, §7.5.13, §11.4.5) found by the
 * section-by-section audit (#321).
 */
class OclTypeOperationSpecConformanceTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	/** §7.5.13: kind and element type both decide collection conformance. */
	@Nested
	class CollectionTypes {

		@Test
		void oclIsKindOf_checksKind() throws OclParseException {
			assertEquals(true, eval("Set{1}->oclIsKindOf(Set(Integer))", self));
			assertEquals(true, eval("Set{1}->oclIsKindOf(Collection(Integer))", self));
			assertEquals(false, eval("Set{1}->oclIsKindOf(Bag(Integer))", self));
			assertEquals(false, eval("Set{1}.oclIsKindOf(Sequence(Integer))", self));
			assertEquals(false, eval("Bag{1}->oclIsKindOf(Set(Integer))", self));
		}

		@Test
		void oclIsKindOf_checksElementConformance() throws OclParseException {
			assertEquals(false, eval("Set{1}->oclIsKindOf(Set(String))", self));
			assertEquals(false, eval("Set{'a'}.oclIsKindOf(Set(Integer))", self));
			// Integer conforms to Real (§11.4.2)
			assertEquals(true, eval("Set{1}->oclIsKindOf(Collection(Real))", self));
		}

		@Test
		void oclIsTypeOf_isExact() throws OclParseException {
			assertEquals(true, eval("Set{1}->oclIsTypeOf(Set(Integer))", self));
			assertEquals(false, eval("Set{1}->oclIsTypeOf(Collection(Integer))", self));
			assertEquals(false, eval("Set{1}->oclIsTypeOf(Set(Real))", self));
			assertEquals(true, eval("Set{1, 2.5}->oclIsTypeOf(Set(Real))", self));
			assertEquals(false, eval("Sequence{1}->oclIsTypeOf(Set(Integer))", self));
		}

		@Test
		void oclType_ofCollection_isNamedAfterItsKind() throws OclParseException {
			assertEquals("Set", eval("Set{1}.oclType().name", self));
			assertEquals("Sequence", eval("Sequence{1}.oclType().name", self));
		}
	}

	/** §11.3.1 oclAsType: "result.oclIsKindOf(type)" — conformance, not exact type. */
	@Nested
	class PrimitiveCasts {

		@Test
		void integer_asReal_isTheRealValue() throws OclParseException {
			assertEquals(5.0, eval("5.oclAsType(Real)", self));
			assertEquals(5.5, eval("5.oclAsType(Real) + 0.5", self));
			assertEquals(5, eval("5.oclAsType(Integer)", self));
		}

		@Test
		void real_asInteger_andUnlimited_asNumber_areInvalid() throws OclParseException {
			assertInvalid("5.5.oclAsType(Integer)", self);
			assertInvalid("*.oclAsType(Integer)", self);
			assertInvalid("*.oclAsType(Real)", self);
		}
	}

	/** §11.4.5: UnlimitedNatural conforms to Integer, not the reverse. */
	@Nested
	class UnlimitedNatural {

		@Test
		void finiteInteger_isNotOfTypeUnlimitedNatural() throws OclParseException {
			assertEquals(false, eval("5.oclIsTypeOf(UnlimitedNatural)", self));
			assertEquals(false, eval("5.oclIsKindOf(UnlimitedNatural)", self));
			assertEquals(false, eval("(-5).oclIsKindOf(UnlimitedNatural)", self));
			assertEquals(true, eval("5.oclType() = Integer", self));
		}

		@Test
		void unlimited_isUnlimitedNatural_andConformsToInteger() throws OclParseException {
			assertEquals(true, eval("*.oclIsTypeOf(UnlimitedNatural)", self));
			assertEquals(true, eval("*.oclIsKindOf(Integer)", self));
		}

		@Test
		void cast_toUnlimitedNatural_acceptsOnlyNonNegativeValues() throws OclParseException {
			assertEquals(5, eval("5.oclAsType(UnlimitedNatural)", self));
			assertInvalid("(-5).oclAsType(UnlimitedNatural)", self);
		}
	}

	/** §11.2.4 / §11.3.2 / §11.3.3: operations on invalid and null. */
	@Nested
	class InvalidAndNull {

		@Test
		void invalid_toString_isInvalid() throws OclParseException {
			assertInvalid("invalid.toString()", self);
			assertInvalid("(1 / 0).toString()", self);
		}

		@Test
		void null_oclIsNew_isFalse() throws OclParseException {
			assertEquals(false, eval("null.oclIsNew()", self));
			assertInvalid("invalid.oclIsNew()", self);
		}

		@Test
		void oclIsInState_reportsThatItIsUnsupported() throws OclParseException {
			assertInvalid("self.oclIsInState(Active)", self);
			assertHasError("self.oclIsInState(Active)", self, "oclIsInState is not supported");
		}
	}

	/** §8.2.2 / A.2.3.1: allInstances of types whose instances need no model (#328). */
	@Nested
	class AllInstancesWithoutModel {

		@Test
		void enumeration_allInstances_areItsLiterals() throws OclParseException {
			assertEquals(3, eval("Status.allInstances()->size()", self));
			assertEquals(true, eval("Status.allInstances()->includes(Status::SENIOR)", self));
		}

		@Test
		void boolean_allInstances_areTrueAndFalse() throws OclParseException {
			assertEquals(true, eval("Boolean.allInstances() = Set{true, false}", self));
		}

		@Test
		void oclVoid_allInstances_isNull() throws OclParseException {
			assertEquals(true, eval("OclVoid.allInstances()->includes(null)", self));
			assertEquals(1, eval("OclVoid.allInstances()->size()", self));
		}

		@Test
		void infiniteAndInvalidTypes_haveNoInstanceSet() throws OclParseException {
			assertInvalid("OclInvalid.allInstances()", self);
			assertInvalid("Integer.allInstances()", self);
			assertInvalid("String.allInstances()", self);
		}
	}
}
