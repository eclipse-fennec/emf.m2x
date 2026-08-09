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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.api.WhitespaceMode;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The three ways to get an M2T engine.
 *
 * <p>A caller should only have to know the door that fits, and generating text from a
 * template should not require knowing that OCL is involved at all. That is door 1: the
 * configuration carries no OCL side, and the factory supplies a default engine. Door 2 runs
 * on an engine that already exists — the injected service under OSGi. Door 3 configures the
 * OCL side as well.
 */
class M2tEngineDoorsTest {

	private static final String TEMPLATE = """
			[module doors(_'http://www.eclipse.org/emf/2002/Ecore')/]
			[template public main(c : EClass)]
			[file ('out.txt', false)]
			[c.name.toUpperFirst()/]
			[/file]
			[/template]
			""";

	@Test
	@DisplayName("door 1: no OCL knowledge — M2T settings alone are enough")
	void withoutAnyOclKnowledge() throws Exception {
		M2tEngine engine = M2tEngines.create(M2tConfiguration.builder()
				.whitespaceMode(WhitespaceMode.ACCELEO)
				.defaultCharset(StandardCharsets.UTF_8)
				.build());

		assertEquals("Book", generate(engine));
		assertNotNull(engine.getOclEngine(), "the factory has to supply one");
	}

	@Test
	@DisplayName("door 2: the engine it was given is the engine that runs")
	void withASuppliedEngine() throws Exception {
		OclEngine ocl = OclEngines.create(new OclParserSupport());

		M2tEngine engine = M2tEngines.create(M2tConfiguration.builder(ocl).build());

		assertSame(ocl, engine.getOclEngine(),
				"a second engine next to the configured one is the bug this closes");
		assertEquals("Book", generate(engine));
	}

	@Test
	@DisplayName("door 3: the OCL side configured too")
	void withAnOclConfiguration() throws Exception {
		M2tEngine engine = M2tEngines.create(M2tConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).maxDepth(64).build()).build());

		assertEquals("Book", generate(engine));
		assertNotNull(engine.getOclEngine());
	}

	@Test
	@DisplayName("each engine gets its own OCL engine unless one is handed in")
	void defaultEnginesAreNotShared() {
		M2tEngine first = M2tEngines.create(M2tConfiguration.builder().build());
		M2tEngine second = M2tEngines.create(M2tConfiguration.builder().build());

		assertNotNull(first.getOclEngine());
		assertNotNull(second.getOclEngine());
		assertTrue(first.getOclEngine() != second.getOclEngine(),
				"sharing one would share its caches between unrelated callers");
	}

	private String generate(M2tEngine engine) throws Exception {
		Module module = engine.parse(TEMPLATE, "doors");
		engine.link(module);
		EClass input = EcoreFactory.eINSTANCE.createEClass();
		input.setName("book");
		M2tResult result = engine.execute(module, M2tContext.of(input));
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		return result.generatedFiles().get("out.txt").strip();
	}
}
