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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * A cycle in what a module extends or a template overrides terminates (#177).
 *
 * <p>Both walks keep a visited set, and neither had a test. A cycle cannot be written in a
 * single source file — a module cannot extend itself by name before it exists — but it can
 * arrive: a compiled unit is loaded from a store, and what a store hands back is input. These
 * build the cycle on the AST, which is what a manipulated document looks like from the
 * engine's side.
 */
class M2tCycleTest {

	@Test
	@Timeout(value = 15, unit = TimeUnit.SECONDS)
	@DisplayName("a module that extends itself is linked once, not forever")
	void selfExtendingModuleTerminates() throws M2tParseException {
		M2tEngine engine = engine();
		Module module = engine.parse("""
				[module gen(_'http://www.eclipse.org/emf/2002/Ecore')/]
				[template public main(c : EClass)]
				[file ('out.txt', false)]class [c.name/][/file]
				[/template]
				""", "gen");
		module.getExtends().add(module);

		M2tResult result = engine.execute(module, M2tContext.of(namedClass()));

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertEquals("class Thing", result.generatedFiles().get("out.txt"));
	}

	@Test
	@Timeout(value = 15, unit = TimeUnit.SECONDS)
	@DisplayName("two modules that extend each other are linked once each")
	void mutualExtendsTerminates() throws M2tParseException {
		M2tEngine engine = engine();
		Module first = engine.parse("""
				[module first(_'http://www.eclipse.org/emf/2002/Ecore')/]
				[template public main(c : EClass)]
				[file ('out.txt', false)]class [c.name/][/file]
				[/template]
				""", "first");
		Module second = engine.parse("""
				[module second(_'http://www.eclipse.org/emf/2002/Ecore')/]
				[template public helper(c : EClass)]helping[/template]
				""", "second");
		first.getExtends().add(second);
		second.getExtends().add(first);

		M2tResult result = engine.execute(first, M2tContext.of(namedClass()));

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertEquals("class Thing", result.generatedFiles().get("out.txt"));
	}

	@Test
	@Timeout(value = 15, unit = TimeUnit.SECONDS)
	@DisplayName("a template that overrides itself still runs")
	void selfOverridingTemplateTerminates() throws M2tParseException {
		// overrideFamily walks up the chain and then down over every override; both directions
		// keep a visited set, and a template pointing at itself exercises both
		M2tEngine engine = engine();
		Module module = engine.parse("""
				[module gen(_'http://www.eclipse.org/emf/2002/Ecore')/]
				[template public greet(c : EClass)]hello[/template]
				[template public main(c : EClass)]
				[file ('out.txt', false)][greet(c)/][/file]
				[/template]
				""", "gen");
		Template greet = template(module, "greet");
		greet.getOverrides().add(greet);

		M2tResult result = engine.execute(module, M2tContext.of(namedClass()));

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertEquals("hello", result.generatedFiles().get("out.txt"));
	}

	// --- helpers ---

	private static M2tEngine engine() {
		return M2tEngines.create(M2tConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build()).build());
	}

	private static org.eclipse.emf.ecore.EClass namedClass() {
		org.eclipse.emf.ecore.EClass type = EcoreFactory.eINSTANCE.createEClass();
		type.setName("Thing");
		return type;
	}

	private static Template template(Module module, String name) {
		return module.getOwnedModuleElement().stream()
				.filter(Template.class::isInstance)
				.map(Template.class::cast)
				.filter(t -> name.equals(t.getName()))
				.findFirst()
				.orElseThrow(() -> new AssertionError("no template " + name));
	}
}
