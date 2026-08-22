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
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import org.junit.jupiter.api.Test;

/**
 * What a standalone block does to the line it stands on — MOFM2T v1.0 §8.4, p. 25.
 *
 * <p>§8.4 gives the two block shapes different body rules, and the difference decides what
 * becomes of the newline that ends the tail line:
 *
 * <ul>
 *   <li><b>multi-line</b>: the body "starts at the beginning of the next line after the
 *       block head, and ends on the last character (excluding the new line) of the line
 *       previous to the block tail" — the construct has already spent the newline in front
 *       of its tail, so the one after the tail is a whitespace body element of the
 *       enclosing body and is "output as is"</li>
 *   <li><b>single-line</b>: the body "starts after the closing bracket of the block head
 *       and ends before the starting bracket of the block tail" — no newline was spent, so
 *       the tag line ends where the line ends and takes its newline with it</li>
 * </ul>
 *
 * <p>Acceleo 3.7 draws the same line, in its own engine tests
 * ({@code data/IfBlock/template_if.mtl} against {@code src-gen/IfBlock/ReferenceResult}):
 * {@code testingElseif}, whose branches are multi-line, keeps a newline after each branch,
 * while {@code testingIf}, whose {@code [if …][/if]} sits on one line inside a for block,
 * runs the iterations onto a single line.
 *
 * <p>Before #122 every standalone block was treated as the single-line shape, which glued
 * the line after a {@code [for]} or {@code [if]} to the block's last line. Before #123 the
 * {@code elseif} and {@code else} branches were not normalized at all, so a taken
 * {@code elseif} emitted a blank line the same body did not emit as the {@code if} branch.
 *
 * @author Data In Motion Consulting
 */
class M2tStandaloneBlockWhitespaceTest {

	private M2tEngine engine;
	private EClass employee;

	@BeforeEach
	void setUp() {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		engine = M2tEngines.create(M2tConfiguration.builder(oclConfig).build());

		EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
		pkg.setName("company");
		pkg.setNsURI("http://company");
		employee = EcoreFactory.eINSTANCE.createEClass();
		employee.setName("Employee");
		pkg.getEClassifiers().add(employee);
		employee.getEStructuralFeatures().add(attribute("name"));
		employee.getEStructuralFeatures().add(attribute("salary"));
	}

	private static org.eclipse.emf.ecore.EAttribute attribute(String name) {
		var attribute = EcoreFactory.eINSTANCE.createEAttribute();
		attribute.setName(name);
		attribute.setEType(EcorePackage.Literals.ESTRING);
		return attribute;
	}

	private String generate(String body) throws M2tParseException {
		String mtl = """
				[module m(Ecore)/]
				[template public main(c : EClass)]
				[file ('out', false)]
				""" + body + """
				[/file]
				[/template]
				""";
		Module module = engine.parse(mtl, "whitespace_test");
		M2tResult result = engine.execute(module, M2tContext.of(employee));
		String content = result.generatedFiles().get("out");
		assertNotNull(content, "no output: " + result.diagnostics());
		return content;
	}

	@Test
	@DisplayName("a multi-line for block keeps the newline after its tail (#122)")
	void forBlockKeepsTailNewline() throws M2tParseException {
		assertEquals("A\n  name;\n  salary;\nZ", generate("""
				A
				[for (a : EAttribute | c.eAttributes)]
				  [a.name/];
				[/for]
				Z
				"""));
	}

	@Test
	@DisplayName("a multi-line if block keeps the newline after its tail (#122)")
	void ifBlockKeepsTailNewline() throws M2tParseException {
		assertEquals("A\nIF-BRANCH\nZ", generate("""
				A
				[if (c.name = 'Employee')]
				IF-BRANCH
				[elseif (c.name = 'Other')]
				ELSEIF-BRANCH
				[/if]
				Z
				"""));
	}

	@Test
	@DisplayName("a taken elseif branch produces what the if branch would (#123)")
	void elseIfBranchIsSymmetric() throws M2tParseException {
		assertEquals("A\nELSEIF-BRANCH\nZ", generate("""
				A
				[if (c.name = 'Other')]
				IF-BRANCH
				[elseif (c.name = 'Employee')]
				ELSEIF-BRANCH
				[/if]
				Z
				"""));
	}

	@Test
	@DisplayName("a taken else branch produces what the if branch would (#123)")
	void elseBranchIsSymmetric() throws M2tParseException {
		assertEquals("A\nELSE-BRANCH\nZ", generate("""
				A
				[if (c.name = 'Other')]
				IF-BRANCH
				[else]
				ELSE-BRANCH
				[/if]
				Z
				"""));
	}

	@Test
	@DisplayName("a standalone for block inside an else branch is normalized too (#123)")
	void standaloneBlockInsideElseBranch() throws M2tParseException {
		// The default "\n" separator and the stripped indentation of the [for] tags are
		// standalone handling — before #123 nothing reached into an else branch to apply it.
		assertEquals("A\n  name;\n  salary;\nZ", generate("""
				A
				[if (c.name = 'Other')]
				never
				[else]
				[for (a : EAttribute | c.eAttributes)]
				  [a.name/];
				[/for]
				[/if]
				Z
				"""));
	}

	@Test
	@DisplayName("a single-line standalone block takes its line's newline with it")
	void singleLineBlockConsumesItsNewline() throws M2tParseException {
		// §8.4's other body rule, and Acceleo's testingIf: nothing of that line survives
		// but the text the block itself produced.
		assertEquals("A\nIN-LINEZ", generate("""
				A
				[if (c.name = 'Employee')]IN-LINE[/if]
				Z
				"""));
	}
}
