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
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The unit fingerprint of a MOFM2T module (#138).
 *
 * <p>In a template the text <em>is</em> the program, so "reformatted" means comment blocks and
 * whitespace inside expressions — not whitespace in the template body.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class M2tUnitFingerprintTest {

	private static final String MODULE = """
			[module m(Ecore)/]
			[query public hasOps(c : EClass) : Boolean = c.eOperations->size() > 0/]
			[template public main(c : EClass) ? (c.hasOps())]
			[file ('out', false)]
			[for (op : EOperation | c.eOperations) separator(',')][op.name/][/for]
			[/file]
			[/template]
			""";

	private static final String REFORMATTED = """
			[module m(Ecore)/]
			[comment]a comment says nothing about the program[/comment]
			[query public hasOps(c : EClass) : Boolean =   c.eOperations -> size()   >   0 /]
			[template public main(c : EClass) ? ( c.hasOps() )]
			[file ('out', false)]
			[for (op : EOperation | c.eOperations) separator(',')][op.name/][/for]
			[/file]
			[/template]
			""";

	private static final String OTHER_LOGIC = """
			[module m(Ecore)/]
			[query public hasOps(c : EClass) : Boolean = c.eOperations->size() > 1/]
			[template public main(c : EClass) ? (c.hasOps())]
			[file ('out', false)]
			[for (op : EOperation | c.eOperations) separator(';')][op.name/][/for]
			[/file]
			[/template]
			""";

	private M2tEngine engine;

	@BeforeEach
	void setUp() {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		engine = M2tEngines.create(M2tConfiguration.builder(oclConfig).build());
	}

	@Test
	void reformattedExpressions_sameFingerprint() throws Exception {
		assertEquals(fingerprint(MODULE), fingerprint(REFORMATTED));
	}

	@Test
	void differentLogic_differentFingerprint() throws Exception {
		assertNotEquals(fingerprint(MODULE), fingerprint(OTHER_LOGIC));
	}

	@Test
	void compiledTwice_sameFingerprint() throws Exception {
		CompiledUnit first = engine.compile(MODULE, "m");
		CompiledUnit second = engine.compile(MODULE, "m");
		assertNotEquals(first.getId(), second.getId());
		assertEquals(first.getManifest().getUnitFingerprint(), second.getManifest().getUnitFingerprint());
	}

	private String fingerprint(String source) throws Exception {
		return engine.compile(source, "m").getManifest().getUnitFingerprint();
	}
}
