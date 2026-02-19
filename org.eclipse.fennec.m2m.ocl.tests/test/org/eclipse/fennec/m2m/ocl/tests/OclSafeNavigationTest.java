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
package org.eclipse.fennec.m2m.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclInvalid;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL v2.5 safe navigation operators ({@code ?.} and {@code ?->}).
 *
 * <p>Covers branches in OclAstBuilder (isSafe flag on property/operation/iterator
 * calls) and OclEvaluator (null-safe short-circuit paths).
 */
class OclSafeNavigationTest extends AbstractOclTest {

	static EObject alice;
	static EObject lonely;
	static EObject company;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 60000.0, true);
		lonely = createPerson("Lonely", 20, 30000.0, false);
		company = createCompany("ACME", alice);
	}

	// === Safe property navigation (?.) ===

	@Test
	void safeNav_nonNull_property() throws OclParseException {
		assertEquals("ACME", eval("self.employer?.name", alice));
	}

	@Test
	void safeNav_null_property() throws OclParseException {
		// lonely has no employer → ?.name returns null
		assertNull(eval("self.employer?.name", lonely));
	}

	@Test
	void safeNav_null_chain() throws OclParseException {
		// null?.name?.size() — null propagates
		assertNull(eval("self.employer?.name?.size()", lonely));
	}

	@Test
	void safeNav_nonNull_chain() throws OclParseException {
		assertEquals(4, eval("self.employer?.name?.size()", alice));
	}

	// === Safe collection navigation (?->) ===

	@Test
	void safeCollNav_nonNull() throws OclParseException {
		assertEquals(1, eval("self.employees?->size()", company));
	}

	@Test
	void safeCollNav_null_iterator() throws OclParseException {
		// employer is null → ?.employees is null → ?->exists returns null
		assertNull(eval(
				"self.employer?.employees?->exists(e | e.name = 'Alice')", lonely));
	}

	@Test
	void safeCollNav_nonNull_iterator() throws OclParseException {
		assertEquals(true, eval(
				"self.employer?.employees?->exists(e | e.name = 'Alice')", alice));
	}

	@Test
	void safeCollNav_null_collectionOp() throws OclParseException {
		// null ?-> notEmpty returns null
		assertNull(eval(
				"self.employer?.employees?->notEmpty()", lonely));
	}

	@Test
	void safeCollNav_nonNull_collectionOp() throws OclParseException {
		assertEquals(true, eval(
				"self.employer?.employees?->notEmpty()", alice));
	}

	// === Safe navigation on null literal ===

	@Test
	void safeNav_nullLiteral() throws OclParseException {
		assertNull(eval("null?.oclIsUndefined()", alice));
	}

	// === Safe navigation on non-null (no effect) ===

	@Test
	void safeNav_onString() throws OclParseException {
		assertEquals(5, eval("'hello'?.size()", alice));
	}

	@Test
	void safeNav_onInteger() throws OclParseException {
		assertEquals(3, eval("3?.abs()", alice));
	}

	// === Non-safe navigation on null → OclInvalid (contrast) ===

	@Test
	void nonSafeNav_nullProperty_returnsInvalid() throws OclParseException {
		assertSame(OclInvalid.INSTANCE, eval("self.employer.name", lonely));
	}

	@Test
	void nonSafeArrow_nullCollection_returnsInvalid() throws OclParseException {
		assertSame(OclInvalid.INSTANCE,
				eval("self.employer.employees->size()", lonely));
	}

	// === Safe iterate ===

	@Test
	void safeArrow_iterate_nonNull() throws OclParseException {
		// iterate with safe arrow on non-null source
		Object result = eval(
				"self.employees?->iterate(e; acc : Integer = 0 | acc + 1)",
				company);
		assertEquals(1, result);
	}

	@Test
	void safeArrow_iterate_null() throws OclParseException {
		// iterate with safe arrow on null source
		assertNull(eval(
				"self.employer?.employees?->iterate(e; acc : Integer = 0 | acc + 1)",
				lonely));
	}
}
