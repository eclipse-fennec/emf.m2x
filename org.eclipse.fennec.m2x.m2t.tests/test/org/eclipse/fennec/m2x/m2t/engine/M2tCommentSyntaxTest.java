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
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
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
 *
 * <p>The body of a comment is literal text. The lexer drops the whole comment before any
 * '[' inside it can open a code block, so comment content is never tokenized as template
 * code, never parsed and never evaluated — as Acceleo 3.x reads it, as a raw substring up
 * to the next {@code [/comment]}.
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

	@Test
	@DisplayName("square brackets inside a comment are text, not a code block")
	void bracketsInsideCommentAreText() throws Exception {
		String output = generate("""
				[module doc(_'http://www.eclipse.org/emf/2002/Ecore')/]
				[comment]
				  // Das ist ein Kommentar [:tlsPort]
				[/comment]
				[template public main(c : EClass)]
				[comment]// Das ist ein Kommentar [:tlsPort][/comment]
				[file ('out.txt', false)]
				done
				[/file]
				[/template]
				""");

		assertTrue(output.contains("done"), output);
		assertEquals(-1, output.indexOf("tlsPort"), output);
	}

	@Test
	@DisplayName("template code inside a comment is neither parsed nor evaluated")
	void codeInsideCommentIsNotEvaluated() throws Exception {
		// c.noSuchFeature would fail to link, [if] without [/if] would fail to parse,
		// and [file] would write a second file — none of it may happen inside a comment.
		String output = generate("""
				[module doc(_'http://www.eclipse.org/emf/2002/Ecore')/]
				[template public main(c : EClass)]
				[comment]
				  [c.noSuchFeature/] [if (c.noSuchFeature)] [let x : Integer = 'text']
				  [file ('must-not-exist.txt', false)]leak[/file]
				  [template public other(c : EClass)]
				[/comment]
				[file ('out.txt', false)]
				done
				[/file]
				[/template]
				""");

		assertEquals("done", output.strip(), output);
	}

	@Test
	@DisplayName("whitespace inside the comment tags is accepted")
	void whitespaceInsideTags() throws Exception {
		String output = generate("""
				[module doc(_'http://www.eclipse.org/emf/2002/Ecore')/]
				[template public main(c : EClass)]
				[ comment ] [c.noSuchFeature/] [ / comment ]
				[file ('out.txt', false)]
				done
				[/file]
				[/template]
				""");

		assertTrue(output.contains("done"), output);
	}

	@Test
	@DisplayName("the first [/comment] closes the comment")
	void firstCloseTagEndsTheComment() {
		// The text after the first [/comment] is template code again, so the dangling
		// second [/comment] is a parse error — comments do not nest, as in Acceleo 3.x.
		assertThrows(M2tParseException.class, () -> generate("""
				[module doc(_'http://www.eclipse.org/emf/2002/Ecore')/]
				[template public main(c : EClass)]
				[comment] outer [comment] inner [/comment] still outer? [/comment]
				[/template]
				"""));
	}

	@Test
	@DisplayName("an unterminated comment is a parse error")
	void unterminatedCommentIsRejected() {
		assertThrows(M2tParseException.class, () -> generate("""
				[module doc(_'http://www.eclipse.org/emf/2002/Ecore')/]
				[template public main(c : EClass)]
				[comment] never closed [:tlsPort]
				[/template]
				"""));
	}

	private String generate(String template) throws Exception {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		M2tEngine engine = M2tEngines.create(M2tConfiguration.builder(oclConfig).build());

		Module module = engine.parse(template, "doc");
		engine.link(module);
		M2tResult result = engine.execute(module, M2tContext.of(EcoreFactory.eINSTANCE.createEClass()));
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		return String.join("\n", result.generatedFiles().values());
	}
}
