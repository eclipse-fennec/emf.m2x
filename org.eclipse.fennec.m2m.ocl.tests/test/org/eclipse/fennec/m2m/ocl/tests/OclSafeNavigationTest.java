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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for OCL v2.5 safe navigation operators (?. and ?->).
 *
 * <p>Safe navigation returns null instead of OclInvalid when
 * the source is null.
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

	// --- Safe property navigation (?.) ---

	@Test
	void safeNav_nonNull_property() throws OclParseException {
		assertEquals("ACME", eval("self.employer?.name", alice));
	}

	@Test
	void safeNav_null_property() throws OclParseException {
		// lonely has no employer, so employer is null; ?.name returns null
		assertNull(eval("self.employer?.name", lonely));
	}

	@Test
	void safeNav_null_chain() throws OclParseException {
		// null?.name?.size() — null propagates
		assertNull(eval("self.employer?.name?.size()", lonely));
	}

	@Test
	void safeNav_nonNull_chain() throws OclParseException {
		// alice.employer?.name?.size() — ACME has 4 chars
		assertEquals(4L, eval("self.employer?.name?.size()", alice));
	}

	// --- Safe collection navigation (?->) ---

	@Test
	void safeCollNav_nonNull() throws OclParseException {
		assertEquals(1L, eval("self.employees?->size()", company));
	}

	// --- null literal with safe navigation ---

	@Test
	void safeNav_nullLiteral() throws OclParseException {
		assertNull(eval("null?.oclIsUndefined()", alice));
	}

	// --- Safe navigation on non-null value (no effect) ---

	@Test
	void safeNav_onString() throws OclParseException {
		assertEquals(5L, eval("'hello'?.size()", alice));
	}

	@Test
	void safeNav_onInteger() throws OclParseException {
		assertEquals(3L, eval("3?.abs()", alice));
	}
}
