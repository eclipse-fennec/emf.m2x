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
package org.eclipse.fennec.m2x.qvtd.tests;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A QVT-R diagnostic says where it came from (#110), from both sides of the language.
 *
 * <p>A transformation has two kinds of name to resolve: its own — the metamodels a
 * {@code transformation} declares — and the OCL inside its domain patterns, which the shared
 * expression builder handles. Both used to report line 0, and both are checked here, because a
 * cursor that only follows one of the two visitors would fix half of it and look complete.
 */
class QvtdDiagnosticPositionTest extends AbstractQvtdEngineTest {

	@Test
	@DisplayName("its own domain: an unknown metamodel is reported where it is declared")
	void unknownMetamodelCarriesItsPosition() {
		QvtdParseException failure = assertThrows(QvtdParseException.class,
				() -> parse("""
						transformation T(uml : simpleuml, nowhere : nosuchmetamodel) {
							top relation R {
								checkonly domain uml p : Package {};
								enforce domain nowhere s : Schema {};
							}
						}
						"""));

		assertTrue(failure.getErrors().stream()
				.anyMatch(d -> d.getMessage().contains("nosuchmetamodel") && d.getLine() == 1),
				() -> "expected the declaration's line: " + describe(failure));
	}

	@Test
	@DisplayName("the OCL inside a domain pattern reports its own line")
	void unknownTypeInsideAPatternCarriesItsPosition() {
		QvtdParseException failure = assertThrows(QvtdParseException.class,
				() -> parse("""
						transformation T(uml : simpleuml, rdbms : simplerdbms) {
							top relation R {
								pn : String;
								checkonly domain uml p : Package {
									name = pn
								};
								enforce domain rdbms s : Schema {
									name = pn
								};
								when {
									p.oclIsKindOf(nosuch::Type);
								}
							}
						}
						"""));

		assertTrue(failure.getErrors().stream().anyMatch(d -> d.getLine() == 11),
				() -> "the when clause is on line 11: " + describe(failure));
	}

	private static String describe(QvtdParseException failure) {
		return failure.getErrors().stream()
				.map(d -> d.getLine() + ":" + d.getColumn() + " " + d.getMessage())
				.toList().toString();
	}
}
