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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.api.UnresolvedReferenceMode;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A reference naming something that is not there ends the generation (#144).
 *
 * <p>M2T used to report an unresolved {@code extends}, {@code import}, {@code overrides} or
 * invocation as a warning and generate anyway, while QVT-O and QVT-R fail such a case outright
 * with {@code Cannot resolve import}. Generating anyway is the quieter and worse outcome: a
 * missing {@code extends} silently changes which templates are visible, so the document is not
 * absent but wrong.
 *
 * <p>{@link UnresolvedReferenceMode#WARN} keeps the older behaviour reachable for a caller who
 * knowingly generates from an incomplete module set.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class M2tUnresolvedReferenceTest {

	private EClass input;

	@BeforeEach
	void setUp() {
		input = EcoreFactory.eINSTANCE.createEClass();
		input.setName("Employee");
	}

	// ==== FAIL, the default ====

	@Test
	void unresolvedExtends_failsAndGeneratesNothing() throws M2tParseException {
		M2tEngine engine = engine(UnresolvedReferenceMode.FAIL);
		Module module = engine.parse("""
				[module child(Ecore) extends missing/]
				[template public main(c : EClass)][file ('out', false)]body:[c.name/][/file][/template]
				""", "child");
		M2tResult result = engine.execute(module, M2tContext.of(input));
		assertError(result, "Unresolved extends 'missing'");
		assertEquals(0, result.generatedFiles().size(),
				"nothing may be generated from an incomplete module set");
	}

	@Test
	void unresolvedImport_failsAndGeneratesNothing() throws M2tParseException {
		M2tEngine engine = engine(UnresolvedReferenceMode.FAIL);
		Module module = engine.parse("""
				[module child(Ecore)/]
				[import missing/]
				[template public main(c : EClass)][file ('out', false)]body:[c.name/][/file][/template]
				""", "child");
		M2tResult result = engine.execute(module, M2tContext.of(input));
		assertError(result, "Unresolved import 'missing'");
		assertEquals(0, result.generatedFiles().size());
	}

	@Test
	void unresolvedInvocation_failsAndGeneratesNothing() throws M2tParseException {
		M2tEngine engine = engine(UnresolvedReferenceMode.FAIL);
		Module module = engine.parse("""
				[module m(Ecore)/]
				[template public main(c : EClass)][file ('out', false)][missingTemplate(c)/][/file][/template]
				""", "m");
		M2tResult result = engine.execute(module, M2tContext.of(input));
		assertError(result, "Unresolved invocation 'missingTemplate'");
		assertEquals(0, result.generatedFiles().size());
	}

	@Test
	void unresolvedOverride_failsAndGeneratesNothing() throws M2tParseException {
		M2tEngine engine = engine(UnresolvedReferenceMode.FAIL);
		Module module = engine.parse("""
				[module m(Ecore)/]
				[template public render(c : EClass) overrides missingBase]x[/template]
				[template public main(c : EClass)][file ('out', false)][c.render()/][/file][/template]
				""", "m");
		M2tResult result = engine.execute(module, M2tContext.of(input));
		assertError(result, "Unresolved override 'missingBase'");
		assertEquals(0, result.generatedFiles().size());
	}

	// ==== WARN, the older behaviour ====

	@Test
	void warnMode_generatesDespiteUnresolvedExtends() throws M2tParseException {
		M2tEngine engine = engine(UnresolvedReferenceMode.WARN);
		Module module = engine.parse("""
				[module child(Ecore) extends missing/]
				[template public main(c : EClass)][file ('out', false)]body:[c.name/][/file][/template]
				""", "child");
		M2tResult result = engine.execute(module, M2tContext.of(input));
		assertEquals("body:Employee", result.generatedFiles().get("out").strip());
	}

	@Test
	void warnMode_stillReportsTheUnresolvedNameFromLink() throws M2tParseException {
		M2tEngine engine = engine(UnresolvedReferenceMode.WARN);
		Module module = engine.parse("""
				[module child(Ecore) extends missing/]
				[template public main(c : EClass)][file ('out', false)]x[/file][/template]
				""", "child");
		assertTrue(engine.link(module).stream()
				.anyMatch(warning -> warning.contains("Unresolved extends 'missing'")),
				"link() reports the name in either mode");
	}

	// ==== Nothing unresolved ====

	@Test
	void resolvedModuleSet_generatesAsBefore() throws M2tParseException {
		M2tEngine engine = engine(UnresolvedReferenceMode.FAIL);
		Module base = engine.parse("""
				[module base(Ecore)/]
				[template public render(c : EClass)]base:[c.name/][/template]
				""", "base");
		Module child = engine.parse("""
				[module child(Ecore) extends base/]
				[template public main(c : EClass)][file ('out', false)][c.render()/][/file][/template]
				""", "child");
		engine.link(base, child);
		M2tResult result = engine.execute(child, M2tContext.of(input));
		assertEquals("base:Employee", result.generatedFiles().get("out").strip());
	}

	// A broken module in the link set must not stop a sound one: the references are
	// attributed to the module they are written in.
	@Test
	void soundModuleInABrokenLinkSet_stillGenerates() throws M2tParseException {
		M2tEngine engine = engine(UnresolvedReferenceMode.FAIL);
		Module sound = engine.parse("""
				[module sound(Ecore)/]
				[template public main(c : EClass)][file ('out', false)]sound:[c.name/][/file][/template]
				""", "sound");
		Module broken = engine.parse("""
				[module broken(Ecore) extends missing/]
				[template public main(c : EClass)][file ('other', false)]broken[/file][/template]
				""", "broken");
		engine.link(sound, broken);

		M2tResult soundResult = engine.execute(sound, M2tContext.of(input));
		assertEquals("sound:Employee", soundResult.generatedFiles().get("out").strip());

		M2tResult brokenResult = engine.execute(broken, M2tContext.of(input));
		assertError(brokenResult, "Unresolved extends 'missing'");
	}

	// ---- Helpers ----

	private static M2tEngine engine(UnresolvedReferenceMode mode) {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		return M2tEngines.create(M2tConfiguration.builder(oclConfig)
				.unresolvedReferenceMode(mode)
				.build());
	}

	private static void assertError(M2tResult result, String expected) {
		boolean found = result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() == Diagnostic.ERROR
						&& d.getMessage().contains(expected));
		assertTrue(found, "Expected an ERROR containing '" + expected
				+ "' but diagnostics were: " + result.diagnostics());
		assertFalse(result.generatedFiles().containsKey("out"),
				"no file may be generated when a reference is unresolved");
	}
}
