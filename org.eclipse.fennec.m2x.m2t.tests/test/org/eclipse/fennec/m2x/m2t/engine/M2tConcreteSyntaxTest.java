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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.engine.internal.M2tProtectedAreaMerger;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The MOFM2T §8.2 grammar, rule by rule (#376): for every production of the text-explicit
 * grammar one source it accepts, with the text that source generates, and one it rejects. The
 * code-explicit productions ({@code <module_decl>}'s second form, {@code <query_defn_code>},
 * {@code <template_defn_code>}, the {@code *_code} commands and {@code <mode>}
 * {@code @code-explicit}) are a documented deviation (D15, #371) and are listed as rejected.
 *
 * <p>A source is one module, or several separated by {@code ---}, linked together; the last one
 * runs, with an EClass {@code Employee} as input, and its {@code main} writes to the file
 * {@code out}.
 */
class M2tConcreteSyntaxTest {

	private static final String HEADER = "[module doc(ecore)/]\n";
	private static final String BASE = "[module base(ecore)/]\n[template public t(c : EClass)]BASE[/template]\n---\n";

	private static final M2tEngine ENGINE = M2tEngines.create(M2tConfiguration.builder(
			OclConfiguration.builder(new OclParserSupport()).build()).build());

	/** A main template writing {@code body} to {@code out}, after the declarations. */
	private static String main(String declarations, String body) {
		return HEADER + declarations + "\n[template public main(c : EClass)][file ('out', false)]" + body
				+ "[/file][/template]\n";
	}

	private static String child(String header, String declarations, String body) {
		return BASE + header + "\n" + declarations + "\n[template public main(c : EClass)][file ('out', false)]" + body
				+ "[/file][/template]\n";
	}

	static Stream<Arguments> accepted() {
		return Stream.of(
				Arguments.of("<module_decl> by name", main("", "M"), "M"),
				Arguments.of("<module_decl> by nsURI", "[module doc(_'http://www.eclipse.org/emf/2002/Ecore')/]\n"
						+ "[template public main(c : EClass)][file ('out', false)]M[/file][/template]\n", "M"),
				Arguments.of("<extends_decl>", child("[module doc(ecore) extends base/]", "", "[t(c)/]"), "BASE"),
				Arguments.of("<import_decl>", child("[module doc(ecore)/]\n[import base/]", "", "[t(c)/]"), "BASE"),
				Arguments.of("<query_defn>", main("[query public q(c : EClass) : String = c.name/]", "[q(c)/]"), "Employee"),
				Arguments.of("<visibility> private", main("[template private p(c : EClass)]P[/template]", "[p(c)/]"), "P"),
				Arguments.of("<arglist> <arg_decl>", main("[template public two(a : EClass, b : String)][a.name/]-[b/][/template]",
						"[two(c, 'x')/]"), "Employee-x"),
				Arguments.of("<actualarglist> empty", main("[template public z()]Z[/template]", "[z()/]"), "Z"),
				Arguments.of("<macro_defn>", main("[macro m(c : EClass)]<[c.name/]>[/macro]", "[m(c)/]"), "<Employee>"),
				Arguments.of("<template_defn_text> <signature> <guard> <init>",
						main("[template public t(c : EClass) ? (true) { v : String = 'V'; }][v/][/template]", "[t(c)/]"), "V"),
				Arguments.of("<overrides>", child("[module doc(ecore) extends base/]",
						"[template public t(c : EClass) overrides t]CHILD[/template]", "[t(c)/]"), "CHILD"),
				Arguments.of("<literal>", main("", "plain text"), "plain text"),
				Arguments.of("<tracecmd>", main("", "[trace (c)]T[/trace]"), "T"),
				Arguments.of("'[' <OclExpressionCS> '/]'", main("", "[c.name.toUpper()/]"), "EMPLOYEE"),
				Arguments.of("<ifExp>", main("", "[c.name = 'Employee' ? 'yes' : 'no'/]"), "yes"),
				Arguments.of("<invocation> <before> <separator> <after>",
						main("[template public t(s : String)][s/][/template]",
								"[t(Sequence{'a', 'b'}) before ('<') separator (',') after ('>')/]"), "<a,b>"),
				Arguments.of("<invocation> 'super'", child("[module doc(ecore) extends base/]",
						"[template public t(c : EClass) overrides t]CHILD+[super/][/template]", "[t(c)/]"), "CHILD+BASE"),
				Arguments.of("<forcmd>", main("", "[for (s : String | Sequence{'a', 'b', 'c'}) before ('<') separator (',')"
						+ " after ('>') ? (s <> 'b') { u : String = s.toUpper(); }][u/][/for]"), "<A,C>"),
				Arguments.of("<ifcmd> <elseif> <else>", main("", "[if (false)]1[elseif (true)]2[/elseif][else]3[/else][/if]"), "2"),
				Arguments.of("<letcmd> <elselet> <else>", main("", "[let p : EPackage = c]P[elselet k : EClass = c]K[k.name/]"
						+ "[/elselet][else]E[/let]"), "KEmployee"),
				Arguments.of("<mode> @text-explicit", "@text-explicit\n" + main("", "M"), "M"),
				Arguments.of("comment [comment()]…[/comment]", main("", "a[comment()]x[/comment]b"), "ab"),
				Arguments.of("comment [comment]…[/comment]", main("", "a[comment]x[/comment]b"), "ab"),
				Arguments.of("comment [comment … /]", main("", "a[comment x /]b"), "ab"));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("accepted")
	void acceptsAndGenerates(String rule, String source, String expected) throws M2tParseException {
		M2tResult result = run(source);
		assertTrue(result.isSuccess(), () -> rule + ": " + result.diagnostics());
		assertEquals(expected, result.generatedFiles().get("out"), rule);
	}

	static Stream<Arguments> rejected() {
		return Stream.of(
				Arguments.of("<module_decl> missing", "[template public main(c : EClass)]x[/template]\n", ""),
				Arguments.of("<module_decl> unknown metamodel", "[module doc(NoSuchMetamodel)/]\n", "Unknown metamodel"),
				Arguments.of("<extends_decl> two modules", "[module doc(ecore) extends a, b/]\n", "only single inheritance"),
				Arguments.of("<import_decl> unterminated", HEADER + "[import base]\n", ""),
				Arguments.of("<query_defn> without return type", main("[query public q(c : EClass) = c.name/]", ""), ""),
				Arguments.of("<visibility> misspelt", main("[template publik t(c : EClass)]T[/template]", ""), ""),
				Arguments.of("<arg_decl> without type", main("[template public t(c)]T[/template]", ""), ""),
				Arguments.of("<macro_defn> without end tag", main("[macro m(c : EClass)]M", ""), ""),
				Arguments.of("<template_defn_text> without end tag", HEADER + "[template public t(c : EClass)]T\n", ""),
				Arguments.of("<overrides> without name", main("[template public t(c : EClass) overrides]T[/template]", ""), ""),
				Arguments.of("<guard> without parentheses", main("[template public t(c : EClass) ? true]T[/template]", ""), ""),
				Arguments.of("<init> without ';'", main("[template public t(c : EClass) { v : String = 'x' }]T[/template]", ""), ""),
				Arguments.of("<filecmd> without parentheses", main("", "[file 'x', false]X[/file]"), ""),
				Arguments.of("<protected> without id", main("", "[protected]P[/protected]"), ""),
				Arguments.of("<tracecmd> without parentheses", main("", "[trace c]T[/trace]"), ""),
				Arguments.of("<ifExp> without ':'", main("", "[true ? 'a'/]"), ""),
				Arguments.of("<before> without parentheses", main("[template public t(c : EClass)]T[/template]", "[t(c) before '<'/]"), ""),
				Arguments.of("<forcmd> without '|'", main("", "[for (s : String Sequence{'a'})][s/][/for]"), ""),
				Arguments.of("<ifcmd> without end tag", main("", "[if (true)]x"), ""),
				Arguments.of("<elseif> end tag without elseif", main("", "[if (true)]x[/elseif][/if]"), ""),
				Arguments.of("<letcmd> without type and value", main("", "[let n]x[/let]"), "needs a type or an initial value"),
				Arguments.of("<mode> @code-explicit", "@code-explicit\nmodule doc\n", "code-explicit mode is not supported"),
				Arguments.of("<module_decl> code form", "module doc\n", ""),
				Arguments.of("<template_defn_code>", HEADER + "template public t(c : EClass) 'x' /template\n", "outside a template"),
				Arguments.of("comment unterminated", main("", "a[comment]x"), ""));
	}

	/**
	 * A rejected source is a parse error, with the given text in a message when one is given;
	 * the code form of a template is not a parse error but literal text outside a template,
	 * which is reported as a warning, so it is checked there.
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("rejected")
	void rejects(String rule, String source, String message) throws M2tParseException {
		Module module;
		try {
			module = ENGINE.parse(source, "doc");
		} catch (M2tParseException expected) {
			assertTrue(message.isEmpty() || expected.getErrors().stream().anyMatch(d -> d.getMessage().contains(message)),
					() -> rule + ": " + expected.getErrors());
			return;
		}
		M2tResult result = ENGINE.execute(module, M2tContext.of(employee()));
		assertFalse(message.isEmpty(), () -> rule + " was accepted: " + result.diagnostics());
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains(message)),
				() -> rule + ": " + result.diagnostics());
	}

	/** {@code <filecmd>} with all three spec arguments, and {@code stdout}. */
	@ParameterizedTest(name = "{0}")
	@MethodSource("fileCommands")
	void fileCommand(String rule, String body, String file, String content, String standardOutput) throws M2tParseException {
		M2tResult result = run(HEADER + "[template public main(c : EClass)]" + body + "[/template]\n");
		assertTrue(result.isSuccess(), () -> rule + ": " + result.diagnostics());
		assertEquals(content, result.generatedFiles().get(file), rule);
		assertEquals(standardOutput, result.standardOutput(), rule);
	}

	static Stream<Arguments> fileCommands() {
		String hash = M2tProtectedAreaMerger.contentHash("P\n");
		return Stream.of(
				Arguments.of("<filecmd> url, append, uniqId", "[file ('x.txt', false, 'id-1')]X[/file]", "x.txt", "X", ""),
				Arguments.of("<filecmd> stdout", "[file (stdout)]S[/file]", "x.txt", null, "S"),
				Arguments.of("<protected>", "[file ('x.java', false)][protected ('p')]P[/protected][/file]", "x.java",
						"// [protected 'p' " + hash + "]\nP\n// [/protected]", ""));
	}

	private static M2tResult run(String source) throws M2tParseException {
		String[] units = source.split("---\n");
		Module[] modules = new Module[units.length];
		for (int i = 0; i < units.length; i++) {
			modules[i] = ENGINE.parse(units[i], "unit" + i);
		}
		if (modules.length > 1) {
			ENGINE.link(modules);
		}
		try {
			return ENGINE.execute(modules[modules.length - 1], M2tContext.of(employee()));
		} catch (RuntimeException e) {
			fail(e);
			return null;
		}
	}

	private static EClass employee() {
		EClass employee = EcoreFactory.eINSTANCE.createEClass();
		employee.setName("Employee");
		EPackage company = EcoreFactory.eINSTANCE.createEPackage();
		company.setName("company");
		company.getEClassifiers().add(employee);
		return employee;
	}
}
