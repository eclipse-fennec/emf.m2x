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

import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * The concrete syntax of MOFM2T §8.2 and what it builds (#370, #371, #372, #375): the OCL rules
 * the template grammar copies, the spec's end tags and ternary, comments and {@code @main}, the
 * mode marker and text outside templates, trace blocks, macros, and queries.
 */
class M2tSyntaxConformanceTest {

	private static final String HEADER = "[module doc(_'http://www.eclipse.org/emf/2002/Ecore')/]\n";

	private static final M2tEngine ENGINE = M2tEngines.create(M2tConfiguration.builder(
			OclConfiguration.builder(new OclParserSupport()).build()).build());

	/** A class named Employee with one attribute, the input of every generation here. */
	private static EClass employee() {
		EClass employee = EcoreFactory.eINSTANCE.createEClass();
		employee.setName("Employee");
		EAttribute name = EcoreFactory.eINSTANCE.createEAttribute();
		name.setName("title");
		name.setEType(EcorePackage.Literals.ESTRING);
		employee.getEStructuralFeatures().add(name);
		EPackage company = EcoreFactory.eINSTANCE.createEPackage();
		company.setName("company");
		company.getEClassifiers().add(employee);
		return employee;
	}

	/** Runs a module whose main template writes {@code %s} to a file, and returns that file. */
	private static M2tResult run(String declarations, String body) throws M2tParseException {
		Module module = ENGINE.parse(HEADER + declarations + """
				[template public main(c : EClass)]
				[file ('out.txt', false)]
				%s
				[/file]
				[/template]
				""".formatted(body), "doc");
		ENGINE.link(module);
		return ENGINE.execute(module, M2tContext.of(employee()));
	}

	private static String output(String body) throws M2tParseException {
		return output("", body);
	}

	private static String output(String declarations, String body) throws M2tParseException {
		M2tResult result = run(declarations, body);
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		return String.join("", result.generatedFiles().values()).strip();
	}

	private static M2tParseException parseError(String source) {
		return assertThrows(M2tParseException.class, () -> ENGINE.parse(source, "doc"));
	}

	private static List<String> messages(M2tParseException failure) {
		return failure.getErrors().stream().map(Resource.Diagnostic::getMessage).toList();
	}

	@Nested
	@DisplayName("#375: the OCL rules of the template grammar are those of OCL")
	class OclRules {

		@Test
		@DisplayName("an escaped identifier takes the escapes of a string literal (§9.3.4)")
		void escapedIdentifierBackslash() throws M2tParseException {
			assertEquals("v", output("[let _'it\\'s' : String = 'v'][_'it\\'s'/][/let]"));
		}

		@Test
		@DisplayName("a letter of a supplementary plane is a letter (§9.3.4)")
		void supplementaryPlaneIdentifier() throws M2tParseException {
			assertEquals("x", output("[let \uD835\uDCB3 : String = 'x'][\uD835\uDCB3/][/let]"));
		}

		@Test
		@DisplayName("a restricted word after '::' is a name (§7.4.11)")
		void restrictedWordAfterColons() {
			// parses; that ecore has no class Set is an ordinary resolution error
			M2tParseException failure = parseError(HEADER + "[template public main(c : EClass)][c.oclIsKindOf(ecore::Set)/][/template]");
			assertTrue(messages(failure).stream().anyMatch(m -> m.contains("Set") && !m.contains("no viable")),
					() -> "" + messages(failure));
		}

		@Test
		@DisplayName("iterate without an iterator variable (§9.3.26)")
		void iterateWithoutIterator() throws M2tParseException {
			assertEquals("3", output("[Sequence{1, 2, 3}->iterate(acc : Integer = 0 | acc + 1)/]"));
		}

		@Test
		@DisplayName("block comments nest (§9.3.49)")
		void nestedBlockComment() throws M2tParseException {
			assertEquals("Employee", output("[c.name /* a /* b */ c */ /]"));
		}

		@Test
		@DisplayName("the qualifier of self.T::feature must name a type the source is (§7.4.11, §9.3.36)")
		void qualifiersAreChecked() {
			assertTrue(messages(parseError(HEADER + "[template public main(c : EClass)][c.Nonsense::name/][/template]"))
					.stream().anyMatch(m -> m.contains("Nonsense")));
			assertTrue(!messages(parseError(HEADER + "[template public main(c : EClass)][c.EPackage::name/][/template]"))
					.isEmpty());
			assertTrue(messages(parseError(HEADER + "[template public main(c : EClass)][Nonsense::oclIsUndefined()/][/template]"))
					.stream().anyMatch(m -> m.contains("Nonsense")));
		}

		@Test
		@DisplayName("an '@' says that templates have no @pre")
		void atPre_clearMessage() {
			assertTrue(messages(parseError(HEADER + "[template public main(c : EClass)][c.name@pre/][/template]"))
					.stream().anyMatch(m -> m.contains("'@pre' is not allowed in templates")));
		}
	}

	@Nested
	@DisplayName("#375: end tags and the ternary of §8.2")
	class EndTagsAndTernary {

		@Test
		@DisplayName("[/elseif] and [/else] are optional end tags")
		void optionalElseifElseEndTags() throws M2tParseException {
			assertEquals("C", output("[if (c.name = 'X')]A[elseif (c.name = 'Y')]B[/elseif][else]C[/else][/if]"));
			assertEquals("C", output("[if (c.name = 'X')]A[elseif (c.name = 'Y')]B[else]C[/if]"));
		}

		@Test
		@DisplayName("[/elselet] is accepted, and so is its omission")
		void elseletEndTag() throws M2tParseException {
			ENGINE.parse(HEADER + "[template public main(c : EClass)][let n : String = c.name]A[elselet m : String = 'x']B[/elselet][/let][/template]", "doc");
			ENGINE.parse(HEADER + "[template public main(c : EClass)][let n : String = c.name]A[elselet m : String = 'x']B[/let][/template]", "doc");
		}

		@Test
		@DisplayName("an end tag without its block is a parse error")
		void strayEndTags() {
			parseError(HEADER + "[template public main(c : EClass)][if (true)]A[/elseif][/if][/template]");
			parseError(HEADER + "[template public main(c : EClass)][if (true)]A[else]B[/else][/else][/if][/template]");
		}

		@Test
		@DisplayName("a ? b : c is a conditional expression (§8.2 <ifExp>)")
		void ternaryIfExp() throws M2tParseException {
			assertEquals("C", output("[c.abstract ? 'A' : 'C'/]"));
			assertEquals("B", output("[c.abstract ? 'A' : c.interface ? 'I' : 'B'/]"));
			parseError(HEADER + "[template public main(c : EClass)][c.abstract ? 'A'/][/template]");
		}

		@Test
		@DisplayName("a guard stays a guard")
		void guardStaysGuard() throws M2tParseException {
			assertEquals("Employee", output("[template public named(c : EClass) ? (c.name <> '')][c.name/][/template]\n",
					"[named(c)/]"));
		}
	}

	@Nested
	@DisplayName("#372: comments and @main")
	class Comments {

		@Test
		@DisplayName("the spec's [comment()] … [/comment], Acceleo's [comment] … [/comment] and [comment … /]")
		void commentForms() throws M2tParseException {
			assertEquals("ab", output("a[comment()]x[/comment]b"));
			assertEquals("ab", output("a[comment]x[/comment]b"));
			assertEquals("ab", output("a[comment hello /]b"));
		}

		@Test
		@DisplayName("'//' in text-explicit mode is text")
		void doubleSlashIsLiteralText() throws M2tParseException {
			assertEquals("a // not a comment", output("a // not a comment"));
		}

		@Test
		@DisplayName("[comment @main/] makes the template that follows the entry point")
		void mainAnnotationSetsMain() throws M2tParseException {
			Module module = ENGINE.parse(HEADER + """
					[comment @main/]
					[template public generate(c : EClass)]
					[file ('out.txt', false)]generated[/file]
					[/template]
					""", "doc");
			Template generate = (Template) module.getOwnedModuleElement().get(0);
			assertTrue(generate.isMain());
			ENGINE.link(module);
			M2tResult result = ENGINE.execute(module, M2tContext.of(employee()));
			assertTrue(result.isSuccess(), () -> "" + result.diagnostics());
			assertEquals("generated", String.join("", result.generatedFiles().values()).strip());
		}

		@Test
		@DisplayName("@main on two templates is an error")
		void mainAnnotationOnTwoTemplates() {
			M2tParseException failure = parseError(HEADER + """
					[comment @main/]
					[template public a(c : EClass)][/template]
					[comment @main/]
					[template public b(c : EClass)][/template]
					""");
			assertTrue(messages(failure).stream().anyMatch(m -> m.contains("a module has one entry point")),
					() -> "" + messages(failure));
		}

		@Test
		@DisplayName("an unclosed [comment()] is a parse error")
		void specCommentUnclosed() {
			parseError(HEADER + "[template public main(c : EClass)]a[comment()]x[/template]");
		}
	}

	@Nested
	@DisplayName("#371: mode, text outside templates, trace, macros")
	class D15 {

		@Test
		@DisplayName("@code-explicit is a clear parse error at its place")
		void codeExplicit() {
			M2tParseException failure = parseError("\n@code-explicit\n" + HEADER);
			assertTrue(failure.getErrors().stream().anyMatch(d -> d.getMessage().contains("code-explicit mode is not supported")
					&& d.getLine() == 2 && d.getColumn() == 0), () -> "" + failure.getErrors());
		}

		@Test
		@DisplayName("@text-explicit is the default mode")
		void textExplicit() throws M2tParseException {
			Module module = ENGINE.parse("@text-explicit\n" + HEADER + "[template public main(c : EClass)][/template]", "doc");
			ENGINE.link(module);
			M2tResult result = ENGINE.execute(module, M2tContext.of(employee()));
			assertTrue(result.diagnostics().stream().noneMatch(d -> d.getSeverity() == Diagnostic.WARNING), () -> "" + result.diagnostics());
		}

		@Test
		@DisplayName("text between module elements is reported, not dropped silently")
		void textOutsideTemplates() throws M2tParseException {
			M2tResult result = run("template public other(c : EClass)]\n", "x");
			assertTrue(result.diagnostics().stream().anyMatch(d -> d.getSeverity() == Diagnostic.WARNING
					&& d.getMessage().contains("doc:2:0: text outside a template is not generated: 'template public other(c : EClass)]'")),
					() -> "" + result.diagnostics());
		}

		@Test
		@DisplayName("a trace block writes its body; an invalid model element is an error")
		void traceBlock() throws M2tParseException {
			assertEquals("class Employee", output("[trace (c)]class [c.name/][/trace]"));
			M2tResult result = run("", "[trace (invalid)]x[/trace]");
			assertTrue(result.diagnostics().stream().anyMatch(d -> d.getSeverity() == Diagnostic.ERROR
					&& d.getMessage().contains("The model element of a trace block is invalid")), () -> "" + result.diagnostics());
		}

		@Test
		@DisplayName("a macro calling itself ends at the template depth limit")
		void macroDepthLimit() throws M2tParseException {
			M2tResult result = run("[macro loop(c : EClass)][loop(c)/][/macro]\n", "[loop(c)/]");
			assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains("Maximum template depth exceeded")
					&& d.getMessage().contains("macro 'loop'")), () -> "" + result.diagnostics());
		}

		@Test
		@DisplayName("a macro body is a block in the sense of §8.4: its head and tail lines go")
		void macroBodyNormalized() throws M2tParseException {
			assertEquals("A<Employee>Z", output("[macro wrap(c : EClass)]\n<[c.name/]>\n[/macro]\n", "A[wrap(c)/]Z"));
		}
	}

	@Nested
	@DisplayName("#370: the module and its queries")
	class ModuleAndQueries {

		@Test
		@DisplayName("a module has a namespace URI and prefix")
		void moduleNamespace() throws M2tParseException {
			Module module = ENGINE.parse(HEADER + "[template public main(c : EClass)][/template]", "doc");
			assertEquals("m2t:/doc", module.getNsURI());
			assertEquals("doc", module.getNsPrefix());
		}

		@Test
		@DisplayName("a query body known not to be of the return type is a type error")
		void queryReturnTypeMismatch() {
			M2tParseException failure = parseError(HEADER + "[query public q(c : EClass) : Integer = 'abc'/]\n[template public main(c : EClass)][/template]");
			assertTrue(messages(failure).stream().anyMatch(m -> m.contains("Query 'q' declares Integer but its body is a String")),
					() -> "" + messages(failure));
		}

		@Test
		@DisplayName("a subtype is a fine return value")
		void queryReturnSubtype() throws M2tParseException {
			assertEquals("Employee", output("[query public q(c : EClass) : EClassifier = c/]\n", "[q(c).name/]"));
		}

		@Test
		@DisplayName("a value of unknown static type that is not of the return type is a warning at run time")
		void queryReturnTypeAtRunTime() throws M2tParseException {
			// oclContainer() says nothing about its value; here it is the EPackage of the class
			M2tResult result = run("[query public q(c : EClass) : Integer = c.oclContainer()/]\n", "[q(c).toString()/]");
			assertTrue(result.diagnostics().stream().anyMatch(d -> d.getSeverity() == Diagnostic.WARNING
					&& d.getMessage().contains("Query 'q' returned a EPackage, which is no Integer")), () -> "" + result.diagnostics());
		}
	}
}
