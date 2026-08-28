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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.ocl.CollectionKind;
import org.eclipse.fennec.m2x.model.ocl.CollectionType;
import org.eclipse.fennec.m2x.model.ocl.InvalidType;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.model.ocl.VoidType;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for oclType() returning the correct type descriptor:
 * EClass for EObjects, PrimitiveType for Integer/Real/String/Boolean,
 * OclInvalid for null and invalid.
 */
class OclOclTypeTest extends AbstractOclTest {

	static EObject person;
	static EObject company;

	@BeforeAll
	static void setUp() {
		person = createPerson("Alice", 30, 80000.0, true);
		company = createCompany("TechCorp", person);
	}

	// --- EObject returns EClass ---

	@Test
	void oclType_person_returnsEClass() throws OclParseException {
		Object result = eval("self.oclType()", person);
		assertInstanceOf(EClass.class, result);
		assertEquals("Person", ((EClass) result).getName());
	}

	@Test
	void oclType_company_returnsEClass() throws OclParseException {
		Object result = eval("self.oclType()", company);
		assertInstanceOf(EClass.class, result);
		assertEquals("Company", ((EClass) result).getName());
	}

	@Test
	void oclType_person_isSameAsPersonClass() throws OclParseException {
		Object result = eval("self.oclType()", person);
		assertSame(personClass, result);
	}

	// --- Primitive types return PrimitiveType ---

	@Test
	void oclType_integer() throws OclParseException {
		Object result = eval("42.oclType()", person);
		assertInstanceOf(PrimitiveType.class, result);
		assertEquals("Integer", ((PrimitiveType) result).getName());
	}

	@Test
	void oclType_real() throws OclParseException {
		Object result = eval("3.14.oclType()", person);
		assertInstanceOf(PrimitiveType.class, result);
		assertEquals("Real", ((PrimitiveType) result).getName());
	}

	@Test
	void oclType_string() throws OclParseException {
		Object result = eval("'hello'.oclType()", person);
		assertInstanceOf(PrimitiveType.class, result);
		assertEquals("String", ((PrimitiveType) result).getName());
	}

	@Test
	void oclType_boolean() throws OclParseException {
		Object result = eval("true.oclType()", person);
		assertInstanceOf(PrimitiveType.class, result);
		assertEquals("Boolean", ((PrimitiveType) result).getName());
	}

	// --- null and invalid ---

	@Test
	void oclType_null_returnsOclVoid() throws OclParseException {
		// OCL v2.5 §11.2.1: null.oclType() = OclVoid — and OclVoid is its own type, VoidType,
		// not a PrimitiveType (OCL v2.4 §8.2: PrimitiveType covers Integer, Real, String,
		// Boolean and UnlimitedNatural). Since #154 the value is the standard library's instance.
		Object result = eval("null.oclType()", person);
		assertInstanceOf(VoidType.class, result);
		assertEquals("OclVoid", ((VoidType) result).getName());
		assertSame(OclStandardLibrary.INSTANCE.oclVoid(), result);
	}

	@Test
	void oclType_invalid_returnsOclInvalid() throws OclParseException {
		// OCL v2.5 §11.2.1: invalid.oclType() = OclInvalid — an InvalidType (OCL v2.4 §8.2),
		// the standard library's instance since #154
		Object result = eval("invalid.oclType()", person);
		assertInstanceOf(InvalidType.class, result);
		assertEquals("OclInvalid", ((InvalidType) result).getName());
		assertSame(OclStandardLibrary.INSTANCE.oclInvalid(), result);
	}

	// --- oclType on property results ---

	@Test
	void oclType_stringProperty() throws OclParseException {
		Object result = eval("self.name.oclType()", person);
		assertInstanceOf(PrimitiveType.class, result);
		assertEquals("String", ((PrimitiveType) result).getName());
	}

	@Test
	void oclType_intProperty() throws OclParseException {
		Object result = eval("self.age.oclType()", person);
		assertInstanceOf(PrimitiveType.class, result);
		assertEquals("Integer", ((PrimitiveType) result).getName());
	}

	@Test
	void oclType_realProperty() throws OclParseException {
		Object result = eval("self.salary.oclType()", person);
		assertInstanceOf(PrimitiveType.class, result);
		assertEquals("Real", ((PrimitiveType) result).getName());
	}

	@Test
	void oclType_booleanProperty() throws OclParseException {
		Object result = eval("self.isMarried.oclType()", person);
		assertInstanceOf(PrimitiveType.class, result);
		assertEquals("Boolean", ((PrimitiveType) result).getName());
	}

	// --- oclType in expressions ---

	@Test
	void oclType_inLetExpression() {
		// OCL v2.4 §11.2.1: oclType() : Classifier. There is no type named OclType in the
		// language, so declaring a let variable of that type is a resolution error. Until #158 the
		// name resolved anyway: to the OCL metamodel's own EClass OclType, found by scanning
		// every package in the global registry, and only when that metamodel happened to be
		// registered before the parse.
		OclParseException error = assertThrows(OclParseException.class,
				() -> eval("let t : OclType = self.oclType() in t.oclIsKindOf(Person)", person));
		assertTrue(error.getMessage().contains("Unknown type (OclType)"), error.getMessage());
	}

	@Test
	void oclType_equalsSelf_oclType() throws OclParseException {
		// Two calls on the same object should return the same EClass
		assertEquals(true, eval("self.oclType() = self.oclType()", person));
	}

	// --- Collection and Map oclType ---

	@Test
	void oclType_collection() throws OclParseException {
		Object result = eval("Sequence{1, 2, 3}.oclType()", person);
		assertInstanceOf(CollectionType.class, result);
		assertEquals(CollectionKind.SEQUENCE, ((CollectionType) result).getKind());
	}
}
