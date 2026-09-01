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
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * compile → store → load → generate for MOFM2T (#139, acceptance). A pinned module comes back
 * unbound and is linked by an engine that never parsed it, from the store it resolves against.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class M2tStoreRoundTripTest {

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

	private EClass input;
	private UnitStore store;
	private M2tEngine engine;
	private M2tEngine bare;

	@BeforeEach
	void setUp() {
		input = EcoreFactory.eINSTANCE.createEClass();
		input.setName("Book");
		store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		engine = engineOver(store);
		bare = M2tEngines.create(M2tConfiguration.builder(OclConfiguration.builder(new OclParserSupport()).build()).build());
	}

	@Test
	void pinnedFromTheStore_roundTrips_andIsLinkedByAFreshEngineOverTheStore() throws Exception {
		UnitKey libraryKey = store.put(bare.compile(LIBRARY, "base"));
		CompiledUnit main = engine.compile(EXTENDING, "main");
		assertEquals(libraryKey.fingerprint().orElseThrow(),
				main.getManifest().getDependencyEntry().get(0).getFingerprint());

		PackagedUnit loaded = (PackagedUnit) store.get(store.put(main)).orElseThrow();
		assertEquals("hello from the library", generate(engineOver(store), (Module) loaded.document().getUnit()));
	}

	@Test
	void embeddedFromTheStore_generatesWithoutTheStore() throws Exception {
		store.put(bare.compile(LIBRARY, "base"));
		CompiledUnit main = engine.compile(EXTENDING, "main", UnitCompileOptions.of(DependencyMode.EMBED));
		PackagedUnit loaded = (PackagedUnit) store.get(store.put(main)).orElseThrow();
		assertEquals("hello from the library", generate(bare, (Module) loaded.document().getUnit()));
	}

	private static M2tEngine engineOver(UnitStore store) {
		return M2tEngines.create(M2tConfiguration.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.addUnitResolver(new M2tStoreUnitResolver(store)).unitResolverEnabled(true).build());
	}

	private String generate(M2tEngine target, Module module) {
		M2tResult result = target.execute(module, M2tContext.of(input));
		assertTrue(result.generatedFiles().containsKey("out.txt"),
				() -> "no file generated: " + result.diagnostics() + " files=" + result.generatedFiles().keySet());
		return result.generatedFiles().get("out.txt").strip();
	}
}
