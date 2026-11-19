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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Blocks inside blocks — MOFM2T v1.0 §8.4 makes every block body a sequence of body
 * elements, and a block is itself a body element, so {@code if}, {@code for}, {@code let},
 * {@code file} and {@code trace} nest to any depth and in any combination.
 *
 * <p>The suite already exercises a handful of pairs as a side effect of other concerns
 * (a {@code for} inside a {@code file}, an {@code if} inside a {@code for}, …). This test
 * walks the combinations on purpose, three and four levels deep, and pins down the three
 * things that go wrong when nesting is handled carelessly:
 *
 * <ul>
 *   <li><b>scoping</b>: an inner block sees the variables of every enclosing block —
 *       the {@code let} variable, the loop variable, the template parameter — and the
 *       loop counter {@code i} belongs to the innermost {@code for}</li>
 *   <li><b>whitespace</b>: the standalone-line rule of §8.4 (see
 *       {@link M2tStandaloneBlockWhitespaceTest}) applies at every depth, including the
 *       indented tag lines of an inner block and the {@code else} branch of an inner
 *       {@code if}</li>
 *   <li><b>diagnostics</b>: an unbalanced inner block is reported as a parse error with a
 *       position, instead of being silently closed by the outer tail</li>
 * </ul>
 *
 * @author Data In Motion Consulting
 */
class M2tNestedBlockTest {

	private M2tEngine engine;
	private EPackage company;

	@BeforeEach
	void setUp() {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		engine = M2tEngines.create(M2tConfiguration.builder(oclConfig).build());

		company = EcoreFactory.eINSTANCE.createEPackage();
		company.setName("company");
		company.setNsURI("http://company");
		company.setNsPrefix("co");

		EClass employee = eClass("Employee", false, "name", "salary");
		EClass manager = eClass("Manager", true, "level");
		EClass marker = eClass("Marker", false);
		company.getEClassifiers().add(employee);
		company.getEClassifiers().add(manager);
		company.getEClassifiers().add(marker);
	}

	@Nested
	@DisplayName("if inside if")
	class IfInsideIf {

		@Test
		@DisplayName("a multi-line if inside the taken branch of a multi-line if")
		void blockForm() throws M2tParseException {
			assertEquals("outer\ninner-else", generate("""
					[if (p.name = 'company')]
					outer
					[if (p.nsPrefix = 'other')]
					inner-then
					[else]
					inner-else
					[/if]
					[/if]
					"""));
		}

		@Test
		@DisplayName("inline ifs nest on a single line, in the then and in the else branch")
		void inlineForm() throws M2tParseException {
			assertEquals("<a2><3>", generate("""
					<[if (p.name = 'company')]a[if (p.nsPrefix = 'x')]1[else]2[/if][else]b[/if]>\
					<[if (p.nsPrefix = 'x')]a[else][if (p.name = 'y')]1[elseif (p.name = 'company')]3[else]4[/if][/if]>
					"""));
		}

		@Test
		@DisplayName("three levels of if, the innermost taking an elseif")
		void threeLevels() throws M2tParseException {
			assertEquals("1\n2\n3b", generate("""
					[if (p.name = 'company')]
					1
					[if (p.nsPrefix = 'co')]
					2
					[if (p.nsURI = 'x')]
					3a
					[elseif (p.nsURI = 'http://company')]
					3b
					[else]
					3c
					[/if]
					[/if]
					[/if]
					"""));
		}

		@Test
		@DisplayName("an inner if that is not taken leaves only the newline after its tail")
		void innerNotTaken() throws M2tParseException {
			// §8.4: the body of a multi-line block ends before the newline in front of its
			// tail, so that newline is spent by the block — but the one after the tail is a
			// whitespace body element of the enclosing body and is "output as is", taken
			// branch or not (#122). Acceleo 3.7 leaves the same blank line.
			assertEquals("A\n\nZ", generate("""
					[if (p.name = 'company')]
					A
					[if (p.nsPrefix = 'x')]
					never
					[/if]
					Z
					[/if]
					"""));
		}
	}

	@Nested
	@DisplayName("for inside for")
	class ForInsideFor {

		@Test
		@DisplayName("the inner loop sees the outer loop variable")
		void innerSeesOuterVariable() throws M2tParseException {
			// The trailing newline is the outer loop's default separator in front of the
			// Marker iteration, whose inner loop runs over nothing: §8.4 puts the separator
			// "between the outputs produced by successive iterations", empty or not, and
			// M2tAcceleoReferenceTest.ifBlockMatchesOne pins the same thing down.
			assertEquals("""
					Employee.name
					Employee.salary
					Manager.level
					""", generate("""
					[for (c : EClass | p.eClassifiers)]
					[for (a : EAttribute | c.eAttributes)]
					[c.name/].[a.name/]
					[/for]
					[/for]
					"""));
		}

		@Test
		@DisplayName("indented inner tag lines are standalone lines too")
		void indentedInnerTags() throws M2tParseException {
			assertEquals("""
					class Employee
					  - name
					  - salary
					class Manager
					  - level
					class Marker
					""", generate("""
					[for (c : EClass | p.eClassifiers)]
					class [c.name/]
					  [for (a : EAttribute | c.eAttributes)]
					  - [a.name/]
					  [/for]
					[/for]
					"""));
		}

		@Test
		@DisplayName("the loop counter i belongs to the innermost for")
		void loopCounterIsInnermost() throws M2tParseException {
			// Every block here is embedded (§8.4): the head lines carry other body elements,
			// so no block is standalone and no default separator is injected.
			assertEquals("<1:1 1:2 2:1 >", generate("""
					<[for (c : EClass | p.eClassifiers->select(x | not x.eAttributes->isEmpty()))][let outer : Integer = i][for (a : EAttribute | c.eAttributes)][outer/]:[i/] [/for][/let][/for]>
					"""));
		}

		@Test
		@DisplayName("a for whose head follows another block head on the same line is embedded")
		void embeddedAfterBlockHead() throws M2tParseException {
			// §8.4: an <embedded-block-expression> is "surrounded by other non-whitespace
			// body elements on the lines where block head or tail occur" and its text is
			// "output as is" — being the first element of the enclosing body does not make
			// the inner for standalone when the enclosing head sits on the same line.
			assertEquals("<Employee Manager Marker >", generate("""
					<[if (p.name = 'company')][for (c : EClass | p.eClassifiers)][c.name/] [/for][/if]>
					"""));
		}

		@Test
		@DisplayName("separator and before/after clauses apply per loop level")
		void separatorsPerLevel() throws M2tParseException {
			assertEquals("[Employee(name,salary); Manager(level); Marker()]", generate("""
					[for (c : EClass | p.eClassifiers) before ('[') separator ('; ') after (']')][c.name/]([for (a : EAttribute | c.eAttributes) separator (',')][a.name/][/for])[/for]\
					"""));
		}
	}

	@Nested
	@DisplayName("let inside and around other blocks")
	class LetNesting {

		@Test
		@DisplayName("a let inside a let sees the outer variable")
		void letInsideLet() throws M2tParseException {
			assertEquals("co:company", generate("""
					[let prefix : String = p.nsPrefix]
					[let qualified : String = prefix + ':' + p.name]
					[qualified/]
					[/let]
					[/let]
					"""));
		}

		@Test
		@DisplayName("a let inside a for binds per iteration and sees the loop variable")
		void letInsideFor() throws M2tParseException {
			assertEquals("""
					Employee has 2 attribute(s)
					Manager has 1 attribute(s)
					Marker has 0 attribute(s)""", generate("""
					[for (c : EClass | p.eClassifiers)]
					[let n : Integer = c.eAttributes->size()]
					[c.name/] has [n/] attribute(s)
					[/let]
					[/for]
					"""));
		}

		@Test
		@DisplayName("a for inside a let sees the let variable in every iteration")
		void forInsideLet() throws M2tParseException {
			assertEquals("co.Employee co.Manager co.Marker ", generate("""
					[let prefix : String = p.nsPrefix][for (c : EClass | p.eClassifiers)][prefix/].[c.name/] [/for][/let]
					"""));
		}

		@Test
		@DisplayName("an if inside a let decides on the let variable")
		void ifInsideLet() throws M2tParseException {
			assertEquals("three classes", generate("""
					[let n : Integer = p.eClassifiers->size()]
					[if (n = 3)]
					three classes
					[else]
					[n/] classes
					[/if]
					[/let]
					"""));
		}

		@Test
		@DisplayName("a let inside an if branch")
		void letInsideIf() throws M2tParseException {
			assertEquals("COMPANY", generate("""
					[if (p.nsPrefix = 'co')]
					[let upper : String = p.name.toUpperCase()]
					[upper/]
					[/let]
					[/if]
					"""));
		}
	}

	@Nested
	@DisplayName("deep combinations")
	class DeepCombinations {

		@Test
		@DisplayName("for → if → for → if, four levels with indentation")
		void fourLevels() throws M2tParseException {
			// Marker has no attributes: the inner for produces nothing, and the newline after
			// its tail stays (§8.4, see innerNotTaken) — hence the blank line in its body.
			assertEquals("""
					class Employee {
					  name: String
					  salary: String
					}
					abstract class Manager {
					  level: String
					}
					class Marker {

					}""", generate("""
					[for (c : EClass | p.eClassifiers)]
					[if (c.abstract)]
					abstract class [c.name/] {
					[else]
					class [c.name/] {
					[/if]
					[for (a : EAttribute | c.eAttributes)]
					  [if (a.eType.name = 'EString')]
					  [a.name/]: String
					  [else]
					  [a.name/]: [a.eType.name/]
					  [/if]
					[/for]
					}
					[/for]
					"""));
		}

		@Test
		@DisplayName("a for inside an if inside a for, the if deciding per iteration")
		void forInsideIfInsideFor() throws M2tParseException {
			assertEquals("""
					Employee: name salary
					Manager: level
					Marker: none""", generate("""
					[for (c : EClass | p.eClassifiers)]
					[if (c.eAttributes->isEmpty())]
					[c.name/]: none
					[else]
					[c.name/]:[for (a : EAttribute | c.eAttributes)] [a.name/][/for]
					[/if]
					[/for]
					"""));
		}

		@Test
		@DisplayName("all variables of the enclosing blocks are visible at the innermost level")
		void allEnclosingVariablesVisible() throws M2tParseException {
			// Trailing newline: the outer loop's default separator before the Marker iteration,
			// whose if is not taken (see innerSeesOuterVariable).
			assertEquals("co/company/Employee/name\nco/company/Employee/salary\nco/company/Manager/level\n", generate("""
					[let prefix : String = p.nsPrefix]
					[for (c : EClass | p.eClassifiers)]
					[if (not c.eAttributes->isEmpty())]
					[for (a : EAttribute | c.eAttributes)]
					[prefix/]/[p.name/]/[c.name/]/[a.name/]
					[/for]
					[/if]
					[/for]
					[/let]
					"""));
		}
	}

	@Nested
	@DisplayName("file and trace blocks")
	class FileAndTrace {

		@Test
		@DisplayName("a file per iteration, with nested if and for inside each file")
		void filePerIteration() throws M2tParseException {
			M2tResult result = execute("""
					[for (c : EClass | p.eClassifiers)]
					[file (c.name + '.txt', false)]
					[if (c.abstract)]
					abstract
					[/if]
					[for (a : EAttribute | c.eAttributes) separator (',')][a.name/][/for]
					[/file]
					[/for]
					""");
			assertEquals(3, result.generatedFiles().size(), () -> result.generatedFiles().keySet().toString());
			// The untaken standalone if leaves the newline after its tail (§8.4, see
			// innerNotTaken); the single-line for takes its line's newline with it (#122).
			assertEquals("\nname,salary", result.generatedFiles().get("Employee.txt"));
			assertEquals("abstract\nlevel", result.generatedFiles().get("Manager.txt"));
			assertEquals("\n", result.generatedFiles().get("Marker.txt"));
		}

		@Test
		@DisplayName("a trace block is transparent for its body")
		void traceBlock() throws M2tParseException {
			assertEquals("<company>", generate("""
					[trace (p)]
					<[p.name/]>
					[/trace]
					"""));
		}

		@Test
		@DisplayName("a trace block inside a for inside an if, its body seeing the loop variable")
		void traceInsideForInsideIf() throws M2tParseException {
			assertEquals("Employee\nManager\nMarker", generate("""
					[if (p.name = 'company')]
					[for (c : EClass | p.eClassifiers)]
					[trace (c)]
					[c.name/]
					[/trace]
					[/for]
					[/if]
					"""));
		}
	}

	@Nested
	@DisplayName("unbalanced nesting")
	class Unbalanced {

		@Test
		@DisplayName("a missing inner [/if] is a parse error with a position, not a silently closed block")
		void missingInnerTail() {
			M2tParseException failure = assertThrows(M2tParseException.class, () -> generate("""
					[for (c : EClass | p.eClassifiers)]
					[if (c.abstract)]
					abstract
					[/for]
					"""));
			assertFalse(failure.getErrors().isEmpty(), failure.getMessage());
			assertTrue(failure.getErrors().stream().anyMatch(d -> d.getLine() > 0),
					() -> "parse error without a position: " + failure.getErrors());
		}

		@Test
		@DisplayName("a surplus inner tail is a parse error")
		void surplusInnerTail() {
			M2tParseException failure = assertThrows(M2tParseException.class, () -> generate("""
					[for (c : EClass | p.eClassifiers)]
					[if (c.abstract)]
					abstract
					[/if]
					[/if]
					[/for]
					"""));
			assertFalse(failure.getErrors().isEmpty(), failure.getMessage());
		}

		@Test
		@DisplayName("crossed tails are a parse error")
		void crossedTails() {
			assertThrows(M2tParseException.class, () -> generate("""
					[for (c : EClass | p.eClassifiers)]
					[if (c.abstract)]
					abstract
					[/for]
					[/if]
					"""));
		}
	}

	// ---- helpers --------------------------------------------------------------------

	private static EClass eClass(String name, boolean isAbstract, String... attributes) {
		EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		eClass.setName(name);
		eClass.setAbstract(isAbstract);
		for (String attribute : attributes) {
			EAttribute eAttribute = EcoreFactory.eINSTANCE.createEAttribute();
			eAttribute.setName(attribute);
			eAttribute.setEType(EcorePackage.Literals.ESTRING);
			eClass.getEStructuralFeatures().add(eAttribute);
		}
		return eClass;
	}

	private M2tResult execute(String body) throws M2tParseException {
		String mtl = """
				[module nested(ecore)/]
				[template public main(p : EPackage)]
				""" + body + """
				[/template]
				""";
		Module module = engine.parse(mtl, "nested_test");
		return engine.execute(module, M2tContext.of(company));
	}

	private String generate(String body) throws M2tParseException {
		M2tResult result = execute("[file ('out', false)]\n" + body + "[/file]\n");
		String content = result.generatedFiles().get("out");
		assertNotNull(content, () -> "no output: " + result.diagnostics());
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		return content;
	}
}
