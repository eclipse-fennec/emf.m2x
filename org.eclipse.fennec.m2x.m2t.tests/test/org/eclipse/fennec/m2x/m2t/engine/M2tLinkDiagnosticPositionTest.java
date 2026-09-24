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
package org.eclipse.fennec.m2x.m2t.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.api.M2tUnit;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.junit.jupiter.api.Test;

/**
 * An {@code extends} or {@code import} nothing can resolve arrives as a {@link Resource.Diagnostic}
 * at the declaration, in {@link M2tParseException#getErrors()} — every one of them, not only the
 * first (#264).
 */
class M2tLinkDiagnosticPositionTest {

	private static final String LIBRARY = """
			[module base(_'http://www.eclipse.org/emf/2002/Ecore')/]
			[template public greet(c : EClass)]hello[/template]
			""";

	private static final String TWO_MISSING = """
			[module main(_'http://www.eclipse.org/emf/2002/Ecore') extends missing::Parent/]
			[import base/]
			[import missing::Helpers/]
			[template public main(c : EClass)]
			[file ('out.txt', false)][greet(c)/][/file]
			[/template]
			""";

	@Test
	void everyUnresolvableReference_isADiagnosticAtItsDeclaration_inEveryMode() {
		M2tEngine engine = engineWith(Map.of("base", LIBRARY));
		for (DependencyMode mode : DependencyMode.values()) {
			M2tParseException failure = assertThrows(M2tParseException.class,
					() -> engine.compile(TWO_MISSING, "main", UnitCompileOptions.of(mode)), mode.getName());
			List<Resource.Diagnostic> errors = failure.getErrors();
			assertEquals(2, errors.size(), mode.getName() + ": " + errors);
			assertAt(errors.get(0), TWO_MISSING, "missing::Parent");
			assertEquals("Cannot resolve import: missing::Parent", errors.get(0).getMessage());
			assertAt(errors.get(1), TWO_MISSING, "[import missing::Helpers/]");
			assertEquals("Cannot resolve import: missing::Helpers", errors.get(1).getMessage());
		}
	}

	@Test
	void failureInsideADependency_isReportedAtTheReferenceToThatDependency() {
		String mid = """
				[module mid(_'http://www.eclipse.org/emf/2002/Ecore')/]
				[import deep::Missing/]
				""";
		String main = """
				[module main(_'http://www.eclipse.org/emf/2002/Ecore')/]

				[import mid/]
				""";
		M2tParseException failure = assertThrows(M2tParseException.class,
				() -> engineWith(Map.of("mid", mid)).compile(main, "main"));
		assertEquals(1, failure.getErrors().size(), String.valueOf(failure.getErrors()));
		Resource.Diagnostic error = failure.getErrors().get(0);
		assertAt(error, main, "[import mid/]");
		assertTrue(error.getMessage().contains("In import 'mid'")
				&& error.getMessage().contains("Cannot resolve import: deep::Missing"), error.getMessage());
	}

	@Test
	void circularDependency_isPlacedAtTheReferenceThatClosesTheCycle() {
		String a = "[module a(_'http://www.eclipse.org/emf/2002/Ecore') extends b/]\n";
		String b = "[module b(_'http://www.eclipse.org/emf/2002/Ecore') extends a/]\n";
		M2tParseException failure = assertThrows(M2tParseException.class,
				() -> engineWith(Map.of("a", a, "b", b)).compile(a, "a"));
		assertEquals(1, failure.getErrors().size(), String.valueOf(failure.getErrors()));
		assertAt(failure.getErrors().get(0), a, "b/]");
		assertTrue(failure.getMessage().contains("Circular import"), failure.getMessage());
	}

	@Test
	void unresolvedInvocation_underEmbed_isADiagnosticOfItsOwn() {
		String source = """
				[module main(_'http://www.eclipse.org/emf/2002/Ecore')/]
				[template public main(c : EClass)]
				[nothere(c)/]
				[/template]
				""";
		M2tParseException failure = assertThrows(M2tParseException.class,
				() -> engineWith(Map.of()).compile(source, "main", UnitCompileOptions.of(DependencyMode.EMBED)));
		assertEquals(1, failure.getErrors().size(), String.valueOf(failure.getErrors()));
		Resource.Diagnostic error = failure.getErrors().get(0);
		assertTrue(error.getMessage().contains("Unresolved invocation 'nothere'"), error.getMessage());
		assertEquals(3, error.getLine(), "on the line of the invocation: " + error);
	}

	private static M2tEngine engineWith(Map<String, String> modules) {
		return M2tEngines.create(M2tConfiguration.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.addUnitResolver(name -> Optional.ofNullable(modules.get(name))
						.map(source -> new M2tUnit.SourceUnit(name, URI.createURI("mem:/" + name + ".mtl"), source)))
				.unitResolverEnabled(true)
				.build());
	}

	/** The diagnostic stands where {@code needle} first occurs in {@code source}. */
	private static void assertAt(Resource.Diagnostic diagnostic, String source, String needle) {
		int offset = source.indexOf(needle);
		assertTrue(offset >= 0, needle);
		String before = source.substring(0, offset);
		int line = (int) before.chars().filter(c -> c == '\n').count() + 1;
		int column = offset - (before.lastIndexOf('\n') + 1);
		assertEquals(line + ":" + column, diagnostic.getLine() + ":" + diagnostic.getColumn(),
				"position of '" + needle + "' for " + diagnostic.getMessage());
	}
}
