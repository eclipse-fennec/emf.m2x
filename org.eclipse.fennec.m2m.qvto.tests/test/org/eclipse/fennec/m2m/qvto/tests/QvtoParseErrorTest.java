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
package org.eclipse.fennec.m2m.qvto.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.fennec.m2m.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Test;

/**
 * Negative tests: syntax errors should produce QvtoParseException with diagnostics.
 */
class QvtoParseErrorTest extends AbstractQvtoParserTest {

	@Test
	void missingClosingBrace() {
		QvtoParseException ex = assertThrows(QvtoParseException.class, () ->
				parse("transformation T() {"));
		assertNotNull(ex.getMessage());
		assertFalse(ex.getErrors().isEmpty());
	}

	@Test
	void missingParentheses() {
		QvtoParseException ex = assertThrows(QvtoParseException.class, () ->
				parse("transformation T {"));
		assertNotNull(ex.getMessage());
		assertFalse(ex.getErrors().isEmpty());
	}

	@Test
	void invalidKeyword() {
		QvtoParseException ex = assertThrows(QvtoParseException.class, () ->
				parse("foobar T() {}"));
		assertNotNull(ex.getMessage());
	}

	@Test
	void incompleteMappingDef() {
		QvtoParseException ex = assertThrows(QvtoParseException.class, () ->
				parse("transformation T() { mapping }"));
		assertNotNull(ex.getMessage());
	}

	@Test
	void missingSemicolon() {
		QvtoParseException ex = assertThrows(QvtoParseException.class, () ->
				parse("""
						transformation T() {
						    mapping doIt() {
						        var x := 1
						        var y := 2;
						    }
						}
						"""));
		assertNotNull(ex.getMessage());
	}

	@Test
	void errorHasDiagnostics() {
		QvtoParseException ex = assertThrows(QvtoParseException.class, () ->
				parse("transformation T() {"));
		assertFalse(ex.getErrors().isEmpty());
	}

	@Test
	void emptyInputParsesAsEmptyUnit() throws QvtoParseException {
		// Empty input should parse without error — the parser returns an unnamed transformation
		// based on unit name
		var result = parse("");
		assertNotNull(result);
	}

	@Test
	void unterminatedString() {
		QvtoParseException ex = assertThrows(QvtoParseException.class, () ->
				parse("""
						transformation T() {
						    helper h() : String = 'unterminated;
						}
						"""));
		assertNotNull(ex.getMessage());
	}
}
