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
package org.eclipse.fennec.m2x.m2t.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.engine.M2tEngines;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * An M2T diagnostic says where it came from (#110), from both sides of the language.
 *
 * <p>A template has two kinds of name to resolve: its own — the metamodel types of template
 * parameters and {@code overrides} declarations — and the OCL inside its expressions, which the
 * shared expression builder handles. Both used to report line 0, and both are checked here.
 */
class M2tDiagnosticPositionTest {

	@Test
	@DisplayName("its own domain: a template parameter type is reported where it is written")
	void unknownParameterTypeCarriesItsPosition() {
		M2tParseException failure = assertThrows(M2tParseException.class,
				() -> engine().parse("""
						[module doc(_'http://www.eclipse.org/emf/2002/Ecore')/]
						[template public main(c : EClass)]
						[/template]
						[template public other(x : NoSuchType)]
						[/template]
						""", "doc"));

		assertTrue(failure.getErrors().stream()
				.anyMatch(d -> d.getMessage().contains("NoSuchType") && d.getLine() == 4),
				() -> "the parameter is on line 4: " + describe(failure));
	}

	@Test
	@DisplayName("the OCL inside a template expression reports its own line and column")
	void unknownTypeInsideAnExpressionCarriesItsPosition() {
		M2tParseException failure = assertThrows(M2tParseException.class,
				() -> engine().parse("""
						[module doc(_'http://www.eclipse.org/emf/2002/Ecore')/]
						[template public main(c : EClass)]
						[c.name/]
						[c.oclIsKindOf(nosuch::Type)/]
						[/template]
						""", "doc"));

		assertEquals(4, failure.getErrors().get(0).getLine(),
				() -> "the expression is on line 4: " + describe(failure));
		assertTrue(failure.getErrors().get(0).getColumn() > 0,
				() -> "and not at the start of the line: " + describe(failure));
	}

	private static M2tEngine engine() {
		return M2tEngines.create(M2tConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build()).build());
	}

	private static String describe(M2tParseException failure) {
		return failure.getErrors().stream()
				.map(d -> d.getLine() + ":" + d.getColumn() + " " + d.getMessage())
				.toList().toString();
	}
}
