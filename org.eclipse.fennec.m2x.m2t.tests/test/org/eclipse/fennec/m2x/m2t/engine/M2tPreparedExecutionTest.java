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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.api.M2tUnitResolver;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.junit.jupiter.api.Test;

/**
 * Generate from a prepared MOFM2T unit: the pinned {@code extends} is linked at prepare time from
 * the link information the compiler kept on the module, no resolver is asked at execute (#140).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class M2tPreparedExecutionTest {

	private static final String LIBRARY = """
			[module base(_'http://www.eclipse.org/emf/2002/Ecore')/]
			[template public greet(c : EClass)]hello from the library[/template]
			""";
	private static final String EXTENDING = """
			[module main(_'http://www.eclipse.org/emf/2002/Ecore') extends base/]
			[template public main(c : EClass)]
			[file ('out.txt', false)][greet(c)/][/file]
			[/template]
			""";

	@Test
	void preparedUnit_generatesWithoutAskingAnyResolver() throws Exception {
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		OclConfiguration ocl = OclConfiguration.builder(new OclParserSupport()).build();
		M2tEngine compiler = M2tEngines.create(M2tConfiguration.builder(ocl)
				.addUnitResolver(new M2tStoreUnitResolver(store)).unitResolverEnabled(true).build());
		M2tUnitResolver forbidden = name -> {
			throw new AssertionError("Execute asked a resolver for '" + name + "'");
		};
		M2tEngine runner = M2tEngines.create(M2tConfiguration.builder(ocl)
				.addUnitResolver(forbidden).unitResolverEnabled(true).build());

		store.store("m2t", new PackagedUnit(compiler.compile(LIBRARY, "base")));
		UnitKey key = store.store("m2t", new PackagedUnit(compiler.compile(EXTENDING, "main")));
		PreparedContext prepared = UnitPreparer.withDefaults(store, runner.unitBinder()).prepare(key);

		EClass input = EcoreFactory.eINSTANCE.createEClass();
		input.setName("Book");
		M2tResult result = runner.execute(prepared, "main", M2tContext.of(input));
		assertTrue(result.generatedFiles().containsKey("out.txt"),
				() -> "no file generated: " + result.diagnostics() + " files=" + result.generatedFiles().keySet());
		assertEquals("hello from the library", result.generatedFiles().get("out.txt").strip());
	}
}
