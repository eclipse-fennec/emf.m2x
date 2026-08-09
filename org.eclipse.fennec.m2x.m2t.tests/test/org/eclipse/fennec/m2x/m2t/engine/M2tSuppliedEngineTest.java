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

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclOperation;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * M2T evaluates with the OCL engine it was given.
 *
 * <p>It used to rebuild the OCL configuration — copying nine settings by hand — only to
 * add its standard library as a provider, and then construct a second engine next to the
 * one the caller had configured. Under OSGi that meant the engine with the configured
 * cache and the provider whiteboard was not the one that ran.
 */
class M2tSuppliedEngineTest {

	private static final String TEMPLATE = """
			[module doc(_'http://www.eclipse.org/emf/2002/Ecore')/]
			[template public main(c : EClass)]
			[file ('out.txt', false)]
			%s
			[/file]
			[/template]
			""";

	@Test
	@DisplayName("the operations of the supplied engine are available in a template")
	void suppliedEngineIsUsed() throws Exception {
		// An operation only this engine knows. If M2T built its own engine next to it,
		// the template could not call it.
		PrimitiveType string = OclFactory.eINSTANCE.createPrimitiveType();
		string.setName("String");
		OclOperationProvider provider = () -> List.of(
				OclOperation.of("shout", string, string, (self, args) -> self + "!"));

		OclEngine ocl = OclEngines.create(OclConfiguration.builder(new OclParserSupport())
				.customOperationsEnabled(true)
				.addOperationProvider(provider)
				.build());

		String output = generate(M2tEngines.create(M2tConfiguration.builder(ocl).build()),
				"[c.name.shout()/]");

		assertEquals("book!", output.strip());
	}

	@Test
	@DisplayName("the MOFM2T standard library works on a supplied engine")
	void standardLibraryTravelsWithTheEvaluation() throws Exception {
		// toUpperFirst is a MOFM2T §8.3 operation, not an OCL one. It has to be
		// available even though the engine was configured entirely without M2T.
		OclEngine ocl = OclEngines.create(new OclParserSupport());

		String output = generate(M2tEngines.create(M2tConfiguration.builder(ocl).build()),
				"[c.name.toUpperFirst()/]");

		assertEquals("Book", output.strip());
	}

	@Test
	@DisplayName("without a supplied engine the configuration still builds one")
	void configurationStillWorks() throws Exception {
		M2tEngine engine = M2tEngines.create(M2tConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build()).build());

		assertEquals("Book", generate(engine, "[c.name.toUpperFirst()/]").strip());
	}

	private String generate(M2tEngine engine, String body) throws Exception {
		Module module = engine.parse(TEMPLATE.formatted(body), "doc");
		engine.link(module);
		EClass input = EcoreFactory.eINSTANCE.createEClass();
		input.setName("book");
		M2tResult result = engine.execute(module, M2tContext.of(input));
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		return result.generatedFiles().get("out.txt");
	}
}
