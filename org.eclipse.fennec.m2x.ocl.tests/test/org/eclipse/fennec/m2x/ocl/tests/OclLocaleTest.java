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
import org.junit.jupiter.api.Test;

/**
 * Tests for {@code oclLocale} property (OCL v2.4 §11.2.1, S-04).
 *
 * <p>The {@code oclLocale} property is defined on OclAny and controls
 * locale-sensitive operations like {@code toUpperCase()} and {@code toLowerCase()}.
 * Default value is {@code 'en_us'}. Can be overridden via {@code let oclLocale = ...}.
 */
class OclLocaleTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// ==================== oclLocale Property Access ====================

	@Test
	void oclLocale_defaultValue() throws OclParseException {
		// OCL v2.4 §11.2.1: default locale is 'en_us'
		assertEquals("en_us", eval("oclLocale", self));
	}

	@Test
	void oclLocale_viaSelfDot() throws OclParseException {
		// self.oclLocale returns the same as bare oclLocale
		assertEquals("en_us", eval("self.oclLocale", self));
	}

	// ==================== let override ====================

	@Test
	void oclLocale_letOverride() throws OclParseException {
		// OCL v2.4 §11.2.1: "may be changed temporarily by using a Let expression"
		assertEquals("fr_CA", eval("let oclLocale : String = 'fr_CA' in oclLocale", self));
	}

	// ==================== toUpperCase with locale ====================

	@Test
	void toUpperCase_defaultLocale() throws OclParseException {
		assertEquals("HELLO", eval("'hello'.toUpperCase()", self));
	}

	@Test
	void toLowerCase_defaultLocale() throws OclParseException {
		assertEquals("hello", eval("'HELLO'.toLowerCase()", self));
	}

	@Test
	void toUpperCase_turkishLocale_dotlessI() throws OclParseException {
		// Turkish locale: lowercase 'i' → uppercase 'İ' (dotted I, U+0130)
		// This is the classic locale-sensitive case conversion example
		assertEquals("\u0130", eval("let oclLocale : String = 'tr_TR' in 'i'.toUpperCase()", self));
	}

	@Test
	void toLowerCase_turkishLocale_dottedI() throws OclParseException {
		// Turkish locale: uppercase 'I' → lowercase 'ı' (dotless i, U+0131)
		assertEquals("\u0131", eval("let oclLocale : String = 'tr_TR' in 'I'.toLowerCase()", self));
	}

	@Test
	void toUpper_shortForm() throws OclParseException {
		// toUpper() is the short form of toUpperCase()
		assertEquals("HELLO", eval("'hello'.toUpper()", self));
	}

	@Test
	void toLower_shortForm() throws OclParseException {
		// toLower() is the short form of toLowerCase()
		assertEquals("hello", eval("'HELLO'.toLower()", self));
	}

	// ==================== let scope ====================

	@Test
	void oclLocale_letScopeResets() throws OclParseException {
		// Outside let scope, default locale should apply again
		// 'i'.toUpperCase() with default en_us → 'I' (not 'İ')
		assertEquals("I", eval("'i'.toUpperCase()", self));
	}

	@Test
	void toUpperCase_germanLocale_eszett() throws OclParseException {
		// German locale: 'ß'.toUpperCase() → 'SS' (in most JDK versions)
		assertEquals("SS", eval("let oclLocale : String = 'de_DE' in '\u00DF'.toUpperCase()", self));
	}
}
