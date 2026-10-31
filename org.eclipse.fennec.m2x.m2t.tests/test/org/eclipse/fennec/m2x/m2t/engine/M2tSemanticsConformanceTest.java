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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * What MOFM2T §7 and §8.1 say a module does when it runs (#363, #364, #365, #366, #369): the let
 * block as a type test, template invocation with its arguments, overrides and {@code [super/]},
 * guards and conditions, the for block, and the module level — unique names, the metamodel,
 * single inheritance, the entry point and visibility.
 */
class M2tSemanticsConformanceTest {

	private static final String HEADER = "[module doc(ecore)/]\n";

	/** A value of a statically unknown type that is a String at runtime: the package's name. */
	private static final String STRING_OF_UNKNOWN_TYPE = "c.oclContainer().name";

	/** An expression that evaluates to invalid: there is no 99th feature. */
	private static final String INVALID = "c.eStructuralFeatures->at(99)";

	/** An expression of type Boolean that evaluates to invalid. */
	private static final String INVALID_BOOLEAN = INVALID + ".name.size() > 0";

	private static final M2tEngine ENGINE = M2tEngines.create(M2tConfiguration.builder(
			OclConfiguration.builder(new OclParserSupport()).build()).build());

	/** A class Employee with an attribute title and a reference dept, in a package company. */
	private static EClass employee() {
		EClass employee = EcoreFactory.eINSTANCE.createEClass();
		employee.setName("Employee");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		employee.getEStructuralFeatures().add(title);
		EClass dept = EcoreFactory.eINSTANCE.createEClass();
		dept.setName("Dept");
		EReference reference = EcoreFactory.eINSTANCE.createEReference();
		reference.setName("dept");
		reference.setEType(dept);
		employee.getEStructuralFeatures().add(reference);
		EPackage company = EcoreFactory.eINSTANCE.createEPackage();
		company.setName("company");
		company.getEClassifiers().add(employee);
		company.getEClassifiers().add(dept);
		return employee;
	}

	/** Runs a module whose main template writes {@code body} to a file. */
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

	private static String output(String declarations, String body) throws M2tParseException {
		M2tResult result = run(declarations, body);
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		return text(result);
	}

	private static String output(String body) throws M2tParseException {
		return output("", body);
	}

	private static String text(M2tResult result) {
		return String.join("", result.generatedFiles().values()).strip();
	}

	/** Asserts an ERROR whose message contains the given text, and returns the output. */
	private static String withError(M2tResult result, String message) {
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() == Diagnostic.ERROR && d.getMessage().contains(message)),
				() -> "expected an ERROR with '" + message + "': " + result.diagnostics());
		return text(result);
	}

	private static M2tParseException parseError(String source) {
		return assertThrows(M2tParseException.class, () -> ENGINE.parse(source, "doc"));
	}

	private static boolean mentions(M2tParseException failure, String message) {
		return failure.getErrors().stream().anyMatch(d -> d.getMessage().contains(message));
	}

	/** Links the modules and runs the last one. */
	private static M2tResult runModules(String... sources) throws M2tParseException {
		Module[] modules = new Module[sources.length];
		for (int i = 0; i < sources.length; i++) {
			modules[i] = ENGINE.parse(sources[i], "unit" + i);
		}
		ENGINE.link(modules);
		return ENGINE.execute(modules[modules.length - 1], M2tContext.of(employee()));
	}

	private static final String BASE = """
			[module base(ecore)/]
			[template public t(c : EClass)]BASE[/template]
			""";

	@Nested
	@DisplayName("#363: the let block tests the type of its value (§7, §8.1.16)")
	class LetBlock {

		@Test
		@DisplayName("a value of the declared type runs the body")
		void subtypeMatches() throws M2tParseException {
			assertEquals("AC=Employee", output("[let ac : EClass = c]AC=[ac.name/][/let]"));
		}

		@Test
		@DisplayName("a value of another type takes the else branch")
		void wrongTypeTakesElse() throws M2tParseException {
			assertEquals("NOT-REF", output(
					"[let r : EReference = c.eStructuralFeatures->first()]IS-REF[else]NOT-REF[/let]"));
		}

		@Test
		@DisplayName("the elselet whose type fits is the one that runs")
		void elseletSecondTypeMatches() throws M2tParseException {
			assertEquals("ATTR title", output("[let r : EReference = c.eStructuralFeatures->first()]REF"
					+ "[elselet a : EAttribute = c.eStructuralFeatures->first()]ATTR [a.name/][else]NONE[/let]"));
		}

		@Test
		@DisplayName("a value of another type without else produces no text")
		void wrongTypeWithoutElseNoText() throws M2tParseException {
			assertEquals("AZ", output("A[let ac : EClass = c.ePackage]IN[/let]Z"));
		}

		@Test
		@DisplayName("null is no instance of any type: the else branch runs")
		void nullTakesElse() throws M2tParseException {
			assertEquals("NULL", output("[let s : String = c.instanceClassName]S[else]NULL[/let]"));
		}

		@Test
		@DisplayName("a primitive value of another type takes the else branch")
		void primitiveMismatchTakesElse() throws M2tParseException {
			assertEquals("E", output("[let s : String = " + STRING_OF_UNKNOWN_TYPE + ".size()]S[else]E[/let]"));
		}

		@Test
		@DisplayName("an invalid value is an ERROR, and the let block produces nothing, else included")
		void invalidValueIsAnError() throws M2tParseException {
			assertEquals("AZ", withError(run("", "A[let a : EAttribute = " + INVALID + "]L[else]E[/let]Z"),
					"is invalid — the let block produces nothing"));
		}

		@Test
		@DisplayName("a declaration without a type takes the type of its value")
		void untypedDeclaration() throws M2tParseException {
			assertEquals("Employee", output("[let n = c.name][n/][/let]"));
		}

		@Test
		@DisplayName("a declaration without a type and without a value is a parse error")
		void untypedWithoutInitIsAParseError() {
			assertTrue(mentions(parseError(HEADER + "[template public main(c : EClass)][let n]x[/let][/template]"),
					"needs a type or an initial value"));
		}
	}

	@Nested
	@DisplayName("#364: template invocation (§8.1.10, §8.2)")
	class Invocation {

		private static final String T = "[template public t(x : EClass)]T[/template]\n";

		@Test
		@DisplayName("a collection argument for a collection parameter is passed once")
		void collectionParameterInvokedOnce() throws M2tParseException {
			assertEquals("size=3", output("[template public t(s : Sequence(String))]size=[s->size()/][/template]\n",
					"[t(Sequence{'a', 'b', 'c'})/]"));
		}

		@Test
		@DisplayName("a single argument for a collection parameter is passed as a collection of one")
		void singletonForCollectionParameterIsWrapped() throws M2tParseException {
			assertEquals("1:a", output("[template public t(l : Sequence(String))][l->size()/]:[l->first()/][/template]\n",
					"[t('a')/]"));
		}

		@Test
		@DisplayName("a collection argument for a single parameter invokes the template per element")
		void collectionForSingleParameterFansOut() throws M2tParseException {
			assertEquals("<title,dept>", output("[template public t(f : EStructuralFeature)][f.name/][/template]\n",
					"[t(c.eStructuralFeatures) before ('<') separator (',') after ('>')/]"));
		}

		@Test
		@DisplayName("before and after go around a single invocation too")
		void singleInvocationBeforeAfter() throws M2tParseException {
			assertEquals("<T>", output(T, "[t(c) before ('<') after ('>')/]"));
		}

		@Test
		@DisplayName("a template its guard declines is not invoked: no before, no after")
		void guardDeclinesNoBeforeAfter() throws M2tParseException {
			assertEquals("AZ", output("[template public t(x : EClass) ? (false)]T[/template]\n",
					"A[t(c) before ('<') after ('>')/]Z"));
		}

		@Test
		@DisplayName("an invocation inside an OCL expression has the generated text as its value")
		void templateCallInsideOclExpression() throws M2tParseException {
			assertEquals("<T>", output(T, "['<' + t(c) + '>'/]"));
		}

		@Test
		@DisplayName("the receiver of the receiver form is the first argument")
		void receiverForm() throws M2tParseException {
			assertEquals("U=Employee", output("[template public u(x : EClass)]U=[x.name/][/template]\n", "[c.u()/]"));
		}

		@Test
		@DisplayName("an argument of another type is an ERROR, and the template is not invoked")
		void argumentTypeMismatch() throws M2tParseException {
			assertEquals("AZ", withError(run(T, "A[t(c.ePackage)/]Z"), "is a EPackage, which is no EClass"));
		}

		@Test
		@DisplayName("an invalid argument is an ERROR, and the template is not invoked")
		void invalidArgument() throws M2tParseException {
			assertEquals("AZ", withError(run(T, "A[t(" + INVALID + ")/]Z"), "is invalid — the template is not invoked"));
		}

		@Test
		@DisplayName("a null argument is an ERROR, and the template is not invoked")
		void nullArgument() throws M2tParseException {
			assertEquals("AZ", withError(run("[template public t(s : String)]T[/template]\n", "A[t(c.instanceClassName)/]Z"),
					"is null — the template is not invoked"));
		}

		@Test
		@DisplayName("too many arguments are a link error")
		void tooManyArguments() throws M2tParseException {
			withError(run(T, "[t(c, c)/]"), "'t' takes 1 argument(s), the call passes 2");
		}

		@Test
		@DisplayName("too few arguments are a link error")
		void tooFewArguments() throws M2tParseException {
			withError(run("[template public t(x : EClass, y : EClass)]T[/template]\n", "[t(c)/]"),
					"'t' takes 2 argument(s), the call passes 1");
		}

		@Test
		@DisplayName("before or after on a query call is a link error")
		void queryBeforeAfter() throws M2tParseException {
			withError(run("[query public q(x : EClass) : String = 'Q'/]\n", "[q(c) before ('<')/]"),
					"they belong to a template invocation");
		}
	}

	@Nested
	@DisplayName("#365: overrides and [super/] (§7, §8.1.3)")
	class Overrides {

		@Test
		@DisplayName("super points to the template whose invocation led here, else the first listed")
		void superPointsToTheInvokedTemplate() throws M2tParseException {
			M2tResult result = runModules("""
					[module base(ecore)/]
					[template public t1(c : EClass)]T1[/template]
					[template public t3(c : EClass)]T3[/template]
					""", """
					[module child(ecore) extends base/]
					[template public t2(c : EClass) overrides t1, t3]T2>[super/][/template]
					[template public main(c : EClass)]
					[file ('out.txt', false)][t3(c)/]|[t1(c)/]|[t2(c)/][/file]
					[/template]
					""");
			assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
			assertEquals("T2>T3|T2>T1|T2>T1", text(result));
		}

		@Test
		@DisplayName("a main in the child calling its own override reaches the base through super")
		void childMainCallsOwnOverride() throws M2tParseException {
			M2tResult result = runModules(BASE, """
					[module child(ecore) extends base/]
					[template public t(c : EClass) overrides t]CHILD+[super/][/template]
					[template public main(c : EClass)]
					[file ('out.txt', false)][t(c)/][/file]
					[/template]
					""");
			assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
			assertEquals("CHILD+BASE", text(result));
		}

		@Test
		@DisplayName("an override with a supertype parameter applies")
		void supertypeParameter() throws M2tParseException {
			M2tResult result = runModules(BASE, """
					[module child(ecore) extends base/]
					[template public t(c : EClassifier) overrides t]CHILD[/template]
					[template public main(c : EClass)]
					[file ('out.txt', false)][t(c)/][/file]
					[/template]
					""");
			assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
			assertEquals("CHILD", text(result));
		}

		private M2tResult child(String override) throws M2tParseException {
			return runModules(BASE, """
					[module child(ecore) extends base/]
					%s
					[template public main(c : EClass)][file ('out.txt', false)]x[/file][/template]
					""".formatted(override));
		}

		@Test
		@DisplayName("an override with another number of parameters is a link error")
		void parameterCountMismatch() throws M2tParseException {
			withError(child("[template public t(c : EClass, d : EClass) overrides t]C[/template]"),
					"has 2 parameter(s), the template 't' it overrides has 1");
		}

		@Test
		@DisplayName("an override with an unrelated parameter type is a link error")
		void unrelatedType() throws M2tParseException {
			withError(child("[template public t(p : EPackage) overrides t]C[/template]"),
					"neither the type EClass");
		}

		@Test
		@DisplayName("a private template cannot be overridden")
		void privateTemplate() throws M2tParseException {
			M2tResult result = runModules("""
					[module base(ecore)/]
					[template private t(c : EClass)]BASE[/template]
					""", """
					[module child(ecore) extends base/]
					[template public t(c : EClass) overrides t]C[/template]
					[template public main(c : EClass)][file ('out.txt', false)]x[/file][/template]
					""");
			withError(result, "is private and cannot be overridden");
		}

		@Test
		@DisplayName("a template cannot override itself")
		void selfOverride() throws M2tParseException {
			withError(run("[template public t(c : EClass) overrides t]T[/template]\n", "x"), "overrides itself");
		}

		@Test
		@DisplayName("only an extended module's template can be overridden, not an imported one's")
		void importedOnly() throws M2tParseException {
			M2tResult result = runModules(BASE, """
					[module child(ecore)/]
					[import base/]
					[template public t(c : EClass) overrides t]C[/template]
					[template public main(c : EClass)][file ('out.txt', false)]x[/file][/template]
					""");
			withError(result, "is only imported");
		}

		@Test
		@DisplayName("super outside an override is an ERROR")
		void superOutsideOverride() throws M2tParseException {
			withError(run("[template public t(x : EClass)][super/][/template]\n", "[t(c)/]"),
					"[super/] called outside of an override context");
		}
	}

	@Nested
	@DisplayName("#366: guards and conditions (§7, §8.1.21)")
	class Conditions {

		@Test
		@DisplayName("true and false decide as before")
		void booleanConditions() throws M2tParseException {
			assertEquals("B", output("[if (c.name = 'X')]A[else]B[/if]"));
			assertEquals("T", output("[template public t(x : EClass) ? (true)]T[/template]\n", "[t(c)/]"));
		}

		@Test
		@DisplayName("a lenient navigation in a comparison is still false, not an error")
		void lenientNavigationInAComparison() throws M2tParseException {
			// the package has no super package: name navigates from null, leniently, to null
			M2tResult result = run("", "[if (c.ePackage.eSuperPackage.name = 'X')]A[else]B[/if]");
			assertTrue(result.diagnostics().stream().noneMatch(d -> d.getSeverity() == Diagnostic.ERROR),
					() -> "diagnostics: " + result.diagnostics());
			assertEquals("B", text(result));
		}

		@Test
		@DisplayName("a template guard that is a String is an ERROR, and the template is not invoked")
		void templateGuardString() throws M2tParseException {
			assertEquals("AZ", withError(run("[template public t(c : EClass) ? (" + STRING_OF_UNKNOWN_TYPE + ")]T[/template]\n",
					"A[t(c)/]Z"), "guard of template 't' is a String"));
		}

		@Test
		@DisplayName("a template guard that is invalid is an ERROR")
		void templateGuardInvalid() throws M2tParseException {
			assertEquals("AZ", withError(run("[template public t(c : EClass) ? (" + INVALID_BOOLEAN + ")]T[/template]\n",
					"A[t(c)/]Z"), "guard of template 't' is invalid"));
		}

		@Test
		@DisplayName("a for guard that is a String is an ERROR, and the element is skipped")
		void forGuardString() throws M2tParseException {
			assertEquals("AZ", withError(run("", "A[for (f : EStructuralFeature | c.eStructuralFeatures) ? ("
					+ "f.oclContainer().name)][f.name/][/for]Z"), "guard of a for block is a String"));
		}

		@Test
		@DisplayName("an if condition that is a String takes no branch, else included")
		void ifConditionString() throws M2tParseException {
			assertEquals("AZ", withError(run("", "A[if (" + STRING_OF_UNKNOWN_TYPE + ")]1[else]2[/if]Z"),
					"condition of an if block is a String"));
		}

		@Test
		@DisplayName("an invalid if condition takes no branch, else included")
		void ifConditionInvalid() throws M2tParseException {
			assertEquals("AZ", withError(run("", "A[if (" + INVALID_BOOLEAN + ")]1[else]2[/if]Z"),
					"condition of an if block is invalid"));
		}

		@Test
		@DisplayName("an elseif condition that is not Boolean takes no branch")
		void elseifConditionNotBoolean() throws M2tParseException {
			assertEquals("AZ", withError(run("", "A[if (false)]1[elseif (" + STRING_OF_UNKNOWN_TYPE + ")]2[else]3[/if]Z"),
					"condition of an elseif is a String"));
		}
	}

	@Nested
	@DisplayName("#366: the for block (§8.1.4, §8.1.8)")
	class ForBlock {

		@Test
		@DisplayName("the init section is evaluated per iteration")
		void initPerIteration() throws M2tParseException {
			assertEquals("#title #dept", output(
					"[for (f : EStructuralFeature | c.eStructuralFeatures) { v : String = '#'; }][v/][f.name/] [/for]"));
		}

		@Test
		@DisplayName("the init section sees the loop variable")
		void initUsesLoopVariable() throws M2tParseException {
			assertEquals("TITLE DEPT", output(
					"[for (f : EStructuralFeature | c.eStructuralFeatures) { u : String = f.name.toUpper(); }][u/] [/for]"));
		}

		@Test
		@DisplayName("a single value is iterated once")
		void singleValueOneIteration() throws M2tParseException {
			assertEquals("Employee", output("[for (s : String | c.name)][s/][/for]"));
		}

		@Test
		@DisplayName("null is not iterated")
		void nullNoIteration() throws M2tParseException {
			assertEquals("END", output("[for (s : String | c.instanceClassName)]x[/for]END"));
		}

		@Test
		@DisplayName("an invalid iteration set is an ERROR")
		void invalidIterationSet() throws M2tParseException {
			assertEquals("END", withError(run("", "[for (f : EStructuralFeature | " + INVALID + ")]x[/for]END"),
					"iteration set of a for block is invalid"));
		}

		@Test
		@DisplayName("elements that are no instance of the loop variable's type are skipped with an ERROR")
		void loopVariableTypeMismatch() throws M2tParseException {
			assertEquals("title;", withError(run("", "[for (a : EAttribute | c.eStructuralFeatures)][a.name/];[/for]"),
					"skips the elements that are no EAttribute"));
		}

		@Test
		@DisplayName("an empty collection writes neither before nor after")
		void emptyCollectionNoBeforeAfter() throws M2tParseException {
			assertEquals("AZ", output("A[for (s : EClass | c.eSuperTypes) before ('<') after ('>')][s.name/][/for]Z"));
		}
	}

	@Nested
	@DisplayName("#369: the module level (§7, §8.1.1, §8.1.2, §8.2)")
	class ModuleLevel {

		@Test
		@DisplayName("two templates with the same parameter types are an error")
		void duplicateSignature() throws M2tParseException {
			withError(run("[template public t(x : EClass)]ONE[/template]\n[template public t(y : EClass)]TWO[/template]\n",
					"[t(c)/]"), "'t' is declared twice");
		}

		@Test
		@DisplayName("a template and a query of the same name are an error")
		void templateAndQuerySameName() throws M2tParseException {
			withError(run("[template public t(x : EClass)]T[/template]\n[query public t(x : EClass) : String = 'Q'/]\n",
					"x"), "'t' is declared twice");
		}

		@Test
		@DisplayName("same-named templates dispatch by the type of the argument")
		void overloadingByType() throws M2tParseException {
			assertEquals("CLASS|PACKAGE", output("[template public t(x : EClass)]CLASS[/template]\n"
					+ "[template public t(p : EPackage)]PACKAGE[/template]\n", "[t(c)/]|[t(c.ePackage)/]"));
		}

		@Test
		@DisplayName("the most specific of the fitting namesakes is chosen")
		void overloadingMostSpecific() throws M2tParseException {
			assertEquals("CLASS", output("[template public t(x : EClassifier)]CLASSIFIER[/template]\n"
					+ "[template public t(x : EClass)]CLASS[/template]\n", "[t(c.oclAsType(EClassifier))/]"));
		}

		@Test
		@DisplayName("an unknown metamodel in the header is a parse error")
		void unknownMetamodel() {
			assertTrue(mentions(parseError("[module m(NoSuchMetamodel)/]\n"), "Unknown metamodel 'NoSuchMetamodel'"));
		}

		@Test
		@DisplayName("extending two modules is a parse error")
		void twoExtends() {
			assertTrue(mentions(parseError("[module m(ecore) extends a, b/]\n"), "only single inheritance is supported"));
		}

		@Test
		@DisplayName("a template without visibility is a warning and taken as public")
		void missingVisibility() throws M2tParseException {
			M2tResult result = run("[template t(x : EClass)]T[/template]\n", "[t(c)/]");
			assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
			assertTrue(result.diagnostics().stream().anyMatch(d -> d.getSeverity() == Diagnostic.WARNING
					&& d.getMessage().contains("missing visibility (MOFM2T §8.2)")), () -> "diagnostics: " + result.diagnostics());
			assertEquals("T", text(result));
		}

		@Test
		@DisplayName("a public template is started by name")
		void executeNamedTemplate() throws M2tParseException {
			Module module = ENGINE.parse(HEADER + "[template public gen(c : EClass)][file ('out.txt', false)]G[/file][/template]\n", "doc");
			M2tResult result = ENGINE.execute(module, "gen", M2tContext.of(employee()));
			assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
			assertEquals("G", text(result));
		}

		@Test
		@DisplayName("the main template of an extended module is found")
		void inheritedMain() throws M2tParseException {
			M2tResult result = runModules("""
					[module base(ecore)/]
					[template public main(c : EClass)][file ('out.txt', false)]BASE-MAIN[/file][/template]
					""", """
					[module child(ecore) extends base/]
					[template public helper(c : EClass)]H[/template]
					""");
			assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
			assertEquals("BASE-MAIN", text(result));
		}

		@Test
		@DisplayName("a private template is no entry point")
		void privateEntryPoint() throws M2tParseException {
			Module module = ENGINE.parse(HEADER + "[template private gen(c : EClass)]G[/template]\n", "doc");
			M2tResult result = ENGINE.execute(module, "gen", M2tContext.of(employee()));
			withError(result, "'gen' is not public");
			assertTrue(result.generatedFiles().isEmpty());
		}

		@Test
		@DisplayName("an input of another type is an ERROR, and nothing is generated")
		void wrongInputType() throws M2tParseException {
			Module module = ENGINE.parse(HEADER + "[template public main(c : EClass)][file ('out.txt', false)]x[/file][/template]\n", "doc");
			M2tResult result = ENGINE.execute(module, M2tContext.of(employee().getEPackage()));
			withError(result, "is a EPackage, which is no EClass");
			assertTrue(result.generatedFiles().isEmpty());
		}

		@Test
		@DisplayName("too few inputs are an ERROR, and nothing is generated")
		void tooFewInputs() throws M2tParseException {
			Module module = ENGINE.parse(HEADER
					+ "[template public main(p : EPackage, c : EClass)][file ('out.txt', false)]x[/file][/template]\n", "doc");
			M2tResult result = ENGINE.execute(module, M2tContext.of(employee()));
			withError(result, "takes 2 input(s), the generation passes 1");
			assertFalse(result.isSuccess());
		}

		@Test
		@DisplayName("a module without an entry point is an ERROR in the result")
		void noEntryPoint() throws M2tParseException {
			Module module = ENGINE.parse(HEADER + "[template public helper(c : EClass)]H[/template]\n", "doc");
			withError(ENGINE.execute(module, M2tContext.of(employee())), "has no [template public main(...)] entry point");
		}

		@Test
		@DisplayName("an input object of the declared class is accepted")
		void inputOfDeclaredClass() throws M2tParseException {
			EObject input = employee();
			Module module = ENGINE.parse(HEADER + "[template public main(c : EClassifier)][file ('out.txt', false)][c.name/][/file][/template]\n", "doc");
			M2tResult result = ENGINE.execute(module, M2tContext.of(input));
			assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
			assertEquals("Employee", text(result));
		}
	}
}
