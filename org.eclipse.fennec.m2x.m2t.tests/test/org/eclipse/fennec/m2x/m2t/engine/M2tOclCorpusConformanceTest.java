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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The template grammar copies the OCL rules (a combined grammar cannot be imported into a mode
 * lexer, §8.2), and the copy drifted (#375). This runs one corpus of expressions through the OCL
 * parser and, as {@code [expr/]}, through the template parser: the same value or a failure in the
 * next OCL grammar change that the template grammar misses.
 */
class M2tOclCorpusConformanceTest {

	/**
	 * Expressions over self — in a template the first input element — an EClass Employee with an
	 * attribute title, each with a String or Integer value.
	 */
	static Stream<String> corpus() {
		return Stream.of(
				"1 + 2 * 3", "7.div(2)", "-1", "'a' + 'b'", "'it''s'", "'\\\\x41'",
				"self.name", "self.name.size()", "self.name.toUpper()", "self?.name",
				"self.eStructuralFeatures->size()", "self.eStructuralFeatures.name->first()",
				"self.eStructuralFeatures->select(f | f.name = 'title')->size()",
				"self.eStructuralFeatures->collect(name)->first()",
				"Sequence{1, 2, 3}->iterate(i; acc : Integer = 0 | acc + i)",
				"Sequence{1, 2, 3}->iterate(acc : Integer = 0 | acc + 1)",
				"Sequence{1, 2, 3}->any(i | i > 1)", "Set{1, 2, 2}->size()", "Sequence{1..3}->last()",
				"let x : Integer = 2 in x * x", "let init : Integer = 2 in init",
				"if self.abstract then 'A' elseif self.interface then 'I' else 'C' endif",
				"Tuple{a = 1, b = 'x'}.b", "self.oclAsType(EClassifier).name",
				"self.EClassifier::name", "self.oclIsKindOf(ecore::EClass).toString()",
				"_'self'.name", "self.name /* a /* nested */ comment */",
				"self.eStructuralFeatures->forAll(f | f.name <> '').toString()",
				"Sequence{'b', 'a'}->sortedBy(s | s)->first()");
	}

	private static EClass employee() {
		EPackage company = EcoreFactory.eINSTANCE.createEPackage();
		company.setName("company");
		EClass employee = EcoreFactory.eINSTANCE.createEClass();
		employee.setName("Employee");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		employee.getEStructuralFeatures().add(title);
		company.getEClassifiers().add(employee);
		return employee;
	}

	private static final OclEngine OCL = OclEngines.create(new OclParserSupport());
	private static final M2tEngine M2T = M2tEngines.create(M2tConfiguration.builder(
			OclConfiguration.builder(new OclParserSupport()).build()).build());

	@ParameterizedTest
	@MethodSource("corpus")
	void sameValueInOclAndInATemplate(String expression) throws Exception {
		EClass self = employee();
		Object oclValue = OCL.evaluate(OCL.parse(expression, EcorePackage.Literals.ECLASS), OclContext.of(self));

		Module module = M2T.parse("""
				[module doc(_'http://www.eclipse.org/emf/2002/Ecore')/]
				[template public main(c : EClass)]
				[file ('out.txt', false)][%s/][/file]
				[/template]
				""".formatted(expression), "doc");
		M2T.link(module);
		M2tResult result = M2T.execute(module, M2tContext.of(self));
		assertTrue(result.isSuccess(), () -> expression + ": " + result.diagnostics());
		assertEquals(String.valueOf(oclValue), String.join("", result.generatedFiles().values()).strip(), expression);
	}
}
