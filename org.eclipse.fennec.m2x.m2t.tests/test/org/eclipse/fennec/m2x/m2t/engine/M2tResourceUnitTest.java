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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.api.M2tUnit;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.eclipse.fennec.m2x.unit.store.UnitDocuments;
import org.eclipse.fennec.m2x.unit.store.UnitXmi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Units by reference for MOFM2T (#214): a resolver answers with where the module lives.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class M2tResourceUnitTest {

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
	void aDocumentAtAUri_resolves_andGenerates(@TempDir Path dir) throws Exception {
		M2tEngine bare = M2tEngines.create(M2tConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build()).build());
		CompiledUnit library = bare.compile(LIBRARY, "base");
		UnitDocuments.Sealed sealed = UnitDocuments.seal(library, DefaultUnitFingerprintService.INSTANCE);
		Path file = dir.resolve("base.xmi");
		Files.write(file, UnitXmi.write(sealed.document(), sealed.key()));

		M2tEngine engine = M2tEngines.create(M2tConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.addUnitResolver(name -> "base".equals(name)
						? Optional.of(new M2tUnit.ResourceUnit(name, URI.createFileURI(file.toString())))
						: Optional.empty())
				.unitResolverEnabled(true)
				.build());

		CompiledUnit main = engine.compile(EXTENDING, "main");   // pin is the default
		assertEquals(sealed.key().fingerprint().orElseThrow(),
				main.getManifest().getDependencyEntry().get(0).getFingerprint(),
				"the pin names the fingerprint of the document the URI holds");

		EClass input = EcoreFactory.eINSTANCE.createEClass();
		input.setName("Book");
		M2tResult result = engine.execute(engine.parse(EXTENDING, "main"), M2tContext.of(input));
		assertTrue(result.generatedFiles().containsKey("out.txt"),
				() -> "no file generated: " + result.diagnostics());
		assertEquals("hello from the library", result.generatedFiles().get("out.txt").strip());
	}
}
