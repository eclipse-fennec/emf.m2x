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
package org.eclipse.fennec.m2x.qvto.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A QVT-O diagnostic says where it came from (#110).
 *
 * <p>Syntax errors always carried the token ANTLR reported. What the builders found afterwards —
 * an unknown type, a metamodel that resolves nowhere — was constructed with line 0, so every
 * problem in a transformation of any length pointed at its first character.
 */
class QvtoDiagnosticPositionTest extends AbstractQvtoParserTest {

	@Test
	@DisplayName("an unknown type is reported at the line and column it stands on")
	void unknownTypeCarriesItsPosition() {
		QvtoParseException failure = assertThrows(QvtoParseException.class,
				() -> parse("""
						transformation T();
						main() {
						    var x : NoSuchType := null;
						}
						"""));

		assertTrue(failure.getErrors().stream().anyMatch(d -> d.getLine() == 3),
				() -> "expected a diagnostic on line 3: " + describe(failure));
	}

	@Test
	@DisplayName("a metamodel that resolves nowhere is reported where it is declared")
	void unresolvedMetamodelCarriesItsPosition() {
		QvtoParseException failure = assertThrows(QvtoParseException.class,
				() -> parse("""
						modeltype MM uses nosuch('http://example.org/nosuch/1.0');
						transformation T(in m : MM);
						main() {}
						"""));

		assertEquals(1, failure.getErrors().get(0).getLine(),
				() -> "the declaration is on line 1: " + describe(failure));
		assertTrue(failure.getErrors().get(0).getColumn() > 0,
				() -> "and not at the start of it: " + describe(failure));
	}

	@Test
	@DisplayName("the OCL inside a mapping body reports its own line and column")
	void unknownTypeInsideAnExpressionCarriesItsPosition() {
		QvtoParseException failure = assertThrows(QvtoParseException.class,
				() -> parse("""
						transformation T();
						main() {
						    var ok : Integer := 1;
						    log(ok.repr());
						    var bad := ok.oclIsKindOf(nosuch::Type);
						}
						"""));

		assertEquals(5, failure.getErrors().get(0).getLine(),
				() -> "the expression is on line 5: " + describe(failure));
		assertTrue(failure.getErrors().get(0).getColumn() > 0,
				() -> "and not at the start of the line: " + describe(failure));
	}

	private static String describe(QvtoParseException failure) {
		return failure.getErrors().stream()
				.map(d -> d.getLine() + ":" + d.getColumn() + " " + d.getMessage())
				.toList().toString();
	}
}
