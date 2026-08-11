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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.api.M2tUnit;
import org.eclipse.fennec.m2x.m2t.api.M2tUnitResolver;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A template reaches a module the engine never parsed.
 *
 * <p>{@code extends} and {@code import} used to resolve only against the modules handed to
 * {@code link} in the same call, so a template could not use a library that lives anywhere
 * else — in another bundle, or next to it on disk — no matter how the engine was configured.
 * There was no resolver SPI at all, while QVT-O has had one from the start.
 *
 * <p>The library here is never parsed by the caller and never passed to {@code link}: the
 * only way {@code greet} can produce its text is through a resolver.
 */
class M2tUnitResolverTest {

	private static final String LIBRARY = """
			[module base(_'http://www.eclipse.org/emf/2002/Ecore')/]
			[template public greet(c : EClass)]hello from the library[/template]
			""";

	private static final String TEMPLATE = """
			[module main(_'http://www.eclipse.org/emf/2002/Ecore') extends base/]
			[template public main(c : EClass)]
			[file ('out.txt', false)][greet(c)/][/file]
			[/template]
			""";

	@Test
	@DisplayName("an extended module is fetched by name")
	void extendedModuleIsResolved() throws Exception {
		M2tEngine engine = engine(builder -> builder
				.addUnitResolver(library())
				.unitResolverEnabled(true));

		assertEquals("hello from the library", generate(engine));
	}

	@Test
	@DisplayName("the enable flag is the gate, and it is shut by default")
	void resolversAreUnreachableUntilEnabled() throws Exception {
		M2tEngine engine = engine(builder -> builder.addUnitResolver(library()));

		assertTrue(warnings(engine).stream().anyMatch(w -> w.contains("Unresolved extends")),
				"the resolver is configured but the gate is shut");
	}

	@Test
	@DisplayName("a name not on a non-empty allow-list is refused")
	void allowListNarrows() throws Exception {
		M2tEngine engine = engine(builder -> builder
				.addUnitResolver(library())
				.unitResolverEnabled(true)
				.allowedUnitModules(Set.of("something.else")));

		assertFalse(warnings(engine).isEmpty());
	}

	@Test
	@DisplayName("an empty allow-list restricts nothing")
	void emptyAllowListMeansNoRestriction() throws Exception {
		M2tEngine engine = engine(builder -> builder
				.addUnitResolver(library())
				.unitResolverEnabled(true)
				.allowedUnitModules(Set.of()));

		assertEquals("hello from the library", generate(engine));
	}

	@Test
	@DisplayName("no more resolvers are consulted than the limit allows")
	void limitCapsHowManyResolversAreAsked() throws Exception {
		AtomicInteger asked = new AtomicInteger();
		M2tEngine engine = engine(builder -> {
			for (int i = 0; i < 10; i++) {
				builder.addUnitResolver(name -> {
					asked.incrementAndGet();
					return Optional.empty();
				});
			}
			return builder.unitResolverEnabled(true).maxUnitResolvers(3);
		});

		warnings(engine);

		assertEquals(3, asked.get(), "the fourth resolver is past the limit");
	}

	@Test
	@DisplayName("a problem in the imported module names that module, not the importing one")
	void aProblemInTheImportedModuleNamesIt() throws Exception {
		// The question this answers: with several units in one generation, whose line 3 is it?
		// Before #116 the message said neither the unit nor the template that was running — it
		// named the outermost template and a bare line, which reads as the main file's line 3.
		M2tEngine engine = engine(builder -> builder
				.addUnitResolver(name -> "broken".equals(name)
						? Optional.of(new M2tUnit.SourceUnit(name,
								URI.createURI("mem:/broken.mtl"), BROKEN_LIBRARY))
						: Optional.empty())
				.unitResolverEnabled(true));

		Module module = engine.parse(IMPORTING, "main");
		assertTrue(engine.link(module).isEmpty(), "the extends has to resolve");
		EClass input = EcoreFactory.eINSTANCE.createEClass();
		input.setName("Book");

		M2tResult result = engine.execute(module, M2tContext.of(input));

		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().startsWith("broken:3:")),
				() -> "the position has to name the imported unit: " + messages(result));
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("[template greet")),
				() -> "and the template that was running, not the one that started: "
						+ messages(result));
	}

	private static List<String> messages(M2tResult result) {
		return result.diagnostics().stream().map(d -> d.getMessage()).toList();
	}

	/** Line 3 navigates from something unset — a runtime problem inside the imported unit. */
	private static final String BROKEN_LIBRARY = """
			[module broken(_'http://www.eclipse.org/emf/2002/Ecore')/]
			[template public greet(c : EClass)]
			name=[c.ePackage.name/]
			[/template]
			""";

	private static final String IMPORTING = """
			[module main(_'http://www.eclipse.org/emf/2002/Ecore') extends broken/]
			[template public main(c : EClass)]
			[file ('out.txt', false)][greet(c)/][/file]
			[/template]
			""";

	// --- helpers ---

	private static M2tUnitResolver library() {
		return name -> "base".equals(name)
				? Optional.of(new M2tUnit.SourceUnit(name,
						URI.createURI("mem:/" + name + ".mtl"), LIBRARY))
				: Optional.empty();
	}

	private static M2tEngine engine(UnaryOperator<M2tConfiguration.Builder> customizer) {
		return M2tEngines.create(customizer.apply(M2tConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build())).build());
	}

	private static List<String> warnings(M2tEngine engine) throws Exception {
		return engine.link(engine.parse(TEMPLATE, "main"));
	}

	private static String generate(M2tEngine engine) throws Exception {
		Module module = engine.parse(TEMPLATE, "main");
		assertTrue(engine.link(module).isEmpty(),
				"the extends has to resolve, otherwise nothing was fetched");

		EClass input = EcoreFactory.eINSTANCE.createEClass();
		input.setName("Book");
		M2tResult result = engine.execute(module, M2tContext.of(input));
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		return result.generatedFiles().get("out.txt").strip();
	}
}
