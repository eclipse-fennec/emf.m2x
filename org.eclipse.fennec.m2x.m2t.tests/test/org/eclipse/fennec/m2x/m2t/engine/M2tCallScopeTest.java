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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A called template or query sees its parameters and the module, not the locals of its caller:
 * MOFM2T scopes are lexical, like QVT-O's (§8.2.1.17, #383).
 */
class M2tCallScopeTest {

	private M2tEngine engine;
	private EClass testClass;

	@BeforeEach
	void setUp() {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		engine = M2tEngines.create(M2tConfiguration.builder(oclConfig).build());
		EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
		pkg.setName("test");
		pkg.setNsURI("http://test");
		testClass = EcoreFactory.eINSTANCE.createEClass();
		testClass.setName("TestClass");
		pkg.getEClassifiers().add(testClass);
	}

	private String run(String src) throws M2tParseException {
		Module m = engine.parse(src, "test");
		M2tResult result = engine.execute(m, M2tContext.of(testClass));
		return String.join("|", result.generatedFiles().values());
	}

	@Test
	void aCallersLetVariable_isNotVisibleInTheCalledTemplate() throws M2tParseException {
		String out = run("[module m(ecore)/]\n"
				+ "[template public main(c : EClass)][file ('o', false)][let x : String = 'CALLER'][t(c)/][/let][/file][/template]\n"
				+ "[template public t(c : EClass)][if (x.oclIsUndefined())]none[else][x/][/if][/template]\n");
		assertEquals("none", out);
	}

	@Test
	void aCallersForIterator_isNotVisibleInTheCalledQuery() throws M2tParseException {
		String out = run("[module m(ecore)/]\n"
				+ "[template public main(c : EClass)][file ('o', false)][for (s : String | Sequence{'it'})][q(c)/][/for][/file][/template]\n"
				+ "[query public q(c : EClass) : String = if s.oclIsUndefined() then 'none' else s endif/]\n");
		assertEquals("none", out);
	}

	@Test
	void theReceiverOfATemplateCall_isItsFirstArgument() throws M2tParseException {
		String out = run("[module m(ecore)/]\n"
				+ "[template public main(c : EClass)][file ('o', false)][c.t()/][/file][/template]\n"
				+ "[template public t(k : EClass)][k.name/][/template]\n");
		assertEquals("TestClass", out);
	}

	@Test
	void theReceiverOfAQueryCall_isItsFirstArgument() throws M2tParseException {
		String out = run("[module m(ecore)/]\n"
				+ "[template public main(c : EClass)][file ('o', false)][c.q()/][/file][/template]\n"
				+ "[query public q(k : EClass) : String = k.name/]\n");
		assertEquals("TestClass", out);
	}

	@Test
	void aCallersLocal_reachesTheCalleeAsAnArgument() throws M2tParseException {
		String out = run("[module m(ecore)/]\n"
				+ "[template public main(c : EClass)][file ('o', false)][let x : String = 'L'][t(c, x)/][/let][/file][/template]\n"
				+ "[template public t(k : EClass, y : String)][k.name/] [y/][/template]\n");
		assertEquals("TestClass L", out);
	}

	@Test
	void aForVariable_shadowsAPropertyOfSelf() throws M2tParseException {
		assertEquals("ab", run("[module m(ecore)/]\n"
				+ "[template public main(c : EClass)][file ('o', false)][for (name : String | Sequence{'a','b'})][name/][/for][/file][/template]\n"));
	}

	@Test
	void aLetVariable_shadowsAPropertyOfSelf() throws M2tParseException {
		assertEquals("L", run("[module m(ecore)/]\n"
				+ "[template public main(c : EClass)][file ('o', false)][let name : String = 'L'][name/][/let][/file][/template]\n"));
	}
}
