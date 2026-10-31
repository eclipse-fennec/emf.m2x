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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A reference to a local variable is a reference to its declaration, not to a free variable of
 * the same name that only the evaluator's name lookup connects to it (#384).
 */
class M2tLocalVariableLinkTest {

	private M2tEngine engine;

	@BeforeEach
	void setUp() {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		engine = M2tEngines.create(M2tConfiguration.builder(oclConfig).build());
	}

	/** Asserts every reference to {@code name} in the module refers to its one declaration. */
	private void assertLinked(String src, String name) throws M2tParseException {
		Module module = engine.parse(src, "test");
		Variable declaration = null;
		List<VariableExp> references = new ArrayList<>();
		for (var it = module.eAllContents(); it.hasNext();) {
			EObject o = it.next();
			if (o instanceof Variable v && name.equals(v.getName())) {
				declaration = v;
			} else if (o instanceof VariableExp ve && ve.getReferredVariable() != null
					&& name.equals(ve.getReferredVariable().getName())) {
				references.add(ve);
			}
		}
		assertFalse(references.isEmpty(), "no reference to " + name);
		for (VariableExp reference : references) {
			assertSame(declaration, reference.getReferredVariable(),
					"a reference to '" + name + "' is a free variable");
		}
	}

	@Test
	void theLoopVariable_inTheBody_separatorAndGuard() throws M2tParseException {
		assertLinked("[module m(ecore)/]\n"
				+ "[template public main(c : EClass)]"
				+ "[for (a : EAttribute | c.eAttributes) separator(a.name) ? (a.name <> '')][a.name/][/for]"
				+ "[/template]\n", "a");
	}

	@Test
	void theLetVariable_inTheBody() throws M2tParseException {
		assertLinked("[module m(ecore)/]\n"
				+ "[template public main(c : EClass)][let n : String = c.name][n/][/let][/template]\n", "n");
	}

	@Test
	void theElseLetVariable_inItsBody() throws M2tParseException {
		assertLinked("[module m(ecore)/]\n"
				+ "[template public main(c : EClass)][let n : Integer = c.name][n/][elselet k : String = c.name][k/][/let][/template]\n",
				"k");
	}

	@Test
	void aTemplateInitVariable_inTheBodyAndTheNextInit() throws M2tParseException {
		assertLinked("[module m(ecore)/]\n"
				+ "[template public main(c : EClass) {x : String = c.name; y : String = x;}][x/][y/][/template]\n", "x");
	}

	@Test
	void aForInitVariable_inTheBody() throws M2tParseException {
		assertLinked("[module m(ecore)/]\n"
				+ "[template public main(c : EClass)][for (a : EAttribute | c.eAttributes) {u : String = a.name.toUpper();}][u/][/for][/template]\n",
				"u");
	}
}
