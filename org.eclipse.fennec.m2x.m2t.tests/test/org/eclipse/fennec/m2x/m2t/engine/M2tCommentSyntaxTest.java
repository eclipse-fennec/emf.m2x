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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Comment syntax, as the user guide documents it.
 *
 * <p>MOFM2T v1.0 specifies comments as a macro — {@code [comment()] … [/comment]} — and
 * the grammar accepts the paired form only. The single-expression form
 * {@code [comment … /]} is an Acceleo 3.x extension; the guide used to show it in a
 * dozen examples, none of which would have parsed.
 */
class M2tCommentSyntaxTest {

	@Test
	@DisplayName("a paired comment is accepted wherever the guide shows one")
	void pairedCommentParsesEverywhere() throws Exception {
		String output = generate("""
				[module doc(_'http://www.eclipse.org/emf/2002/Ecore')/]
				[comment]
				  a comment between the header and the first template
				[/comment]
				[template public main(c : EClass)]
				[comment]inside a template body[/comment]
				[file ('out.txt', false)]
				[for (a : EAttribute | c.eAttributes)][comment]inside a for block[/comment][a.name/][/for]
				done
				[/file]
				[/template]
				""");

		assertTrue(output.contains("done"), output);
		assertEquals(-1, output.indexOf("comment"),
				() -> "comment text must not reach the output:\n" + output);
	}

	@Test
	@DisplayName("the single-expression form of Acceleo 3.x is not accepted")
	void singleExpressionCommentIsRejected() {
		assertThrows(M2tParseException.class, () -> generate("""
				[module doc(_'http://www.eclipse.org/emf/2002/Ecore')/]
				[template public main(c : EClass)]
				[comment this form does not exist /]
				[/template]
				"""));
	}

	private String generate(String template) throws Exception {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		M2tEngineImpl engine = new M2tEngineImpl(M2tConfiguration.builder(oclConfig).build());

		Module module = engine.parse(template, "doc");
		engine.link(module);
		M2tResult result = engine.execute(module, M2tContext.of(EcoreFactory.eINSTANCE.createEClass()));
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		return String.join("\n", result.generatedFiles().values());
	}
}
