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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A diagnostic says where it came from.
 *
 * <p>Syntax errors always did — ANTLR reports the token. What the builders found afterwards, every
 * unresolved name and unknown type, was constructed with line 0, so an editor had nothing to put a
 * marker on and a multi-line document pointed all its problems at its first character (#110).
 */
class OclDiagnosticPositionTest extends AbstractOclTest {

	@Test
	@DisplayName("an unknown type is reported at the line it stands on")
	void unknownTypeCarriesItsLine() {
		OclParserSupport parser = new OclParserSupport();

		// The offending name is on line 3, and nothing before it is wrong.
		OclParseException failure = assertThrows(OclParseException.class,
				() -> parser.parse("""
						self.name = 'Alice'
						and self.age > 18
						and self.oclIsTypeOf(nosuch::Type)""", personClass));

		Resource.Diagnostic diagnostic = failure.getErrors().get(0);
		assertTrue(diagnostic.getMessage().contains("Unknown type"), diagnostic::getMessage);
		assertEquals(3, diagnostic.getLine(),
				() -> "expected line 3, got " + diagnostic.getLine() + ":" + diagnostic.getColumn()
						+ " for " + diagnostic.getMessage());
		// The column is where the offending name starts, counted from 0: 'and self.oclIsTypeOf('
		// is 21 characters, and the type follows.
		assertEquals(21, diagnostic.getColumn(),
				() -> "column was " + diagnostic.getColumn());
	}

	@Test
	@DisplayName("the column is where the offending name starts, not where the line does")
	void theColumnPointsAtTheName() {
		OclParserSupport parser = new OclParserSupport();

		OclParseException withPrefix = assertThrows(OclParseException.class,
				() -> parser.parse("self.name = 'x' and self.oclIsTypeOf(nosuch::Type)", personClass));
		OclParseException without = assertThrows(OclParseException.class,
				() -> parser.parse("self.oclIsTypeOf(nosuch::Type)", personClass));

		assertEquals(17, without.getErrors().get(0).getColumn(),
				"'self.oclIsTypeOf(' is 17 characters");
		assertEquals(37, withPrefix.getErrors().get(0).getColumn(),
				"and the same name moves with its prefix");
	}

	@Test
	@DisplayName("each unresolved name reports its own line, not the first one")
	void everyDiagnosticCarriesItsOwnLine() {
		OclParserSupport parser = new OclParserSupport();

		OclParseException failure = assertThrows(OclParseException.class,
				() -> parser.parse("""
						self.oclIsTypeOf(nosuch::First)
						or self.oclIsTypeOf(nosuch::Second)""", personClass));

		assertEquals(2, failure.getErrors().size(), () -> "diagnostics: " + failure.getErrors());
		assertEquals(1, failure.getErrors().get(0).getLine());
		assertEquals(2, failure.getErrors().get(1).getLine(),
				() -> "the second problem is on the second line: " + failure.getErrors());
	}

	@Test
	@DisplayName("several problems on one line are told apart by their column")
	void severalProblemsOnOneLine() {
		OclParserSupport parser = new OclParserSupport();

		// The case a per-node cursor could get wrong: if the position were only refreshed per
		// line, or left stale after the first problem, these would all report the same column.
		OclParseException failure = assertThrows(OclParseException.class,
				() -> parser.parse(
						"self.oclIsKindOf(NoA) or self.oclIsKindOf(NoB) or self.oclIsKindOf(NoC)",
						personClass));

		assertEquals(3, failure.getErrors().size(), () -> "diagnostics: " + failure.getErrors());
		assertEquals(List.of(17, 42, 67), failure.getErrors().stream()
				.map(Resource.Diagnostic::getColumn).toList(),
				() -> "each name reports where it starts: " + failure.getErrors());
		assertTrue(failure.getErrors().stream().allMatch(d -> d.getLine() == 1),
				"and all of them are on the one line there is");
	}

	@Test
	@DisplayName("a syntax error keeps the position it always had")
	void syntaxErrorsAreUnchanged() {
		OclParserSupport parser = new OclParserSupport();

		OclParseException failure = assertThrows(OclParseException.class,
				() -> parser.parse("self.name\nand +++ 42", personClass));

		assertTrue(failure.getErrors().stream().anyMatch(d -> d.getLine() == 2),
				() -> "diagnostics: " + failure.getErrors());
	}
}
