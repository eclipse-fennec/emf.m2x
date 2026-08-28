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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Regression guards for MOFM2T guards ({@code ? (expression)}) that read
 * something beyond the guarded element's own properties — a query defined at
 * module level in particular.
 *
 * <p>The QVT-O counterpart is {@code QvtoE2eGuardPropertyAccessTest}. The review
 * of PR #128 showed that a guard is a different containment context than a body
 * ({@code MappingOperation::when} is not an {@code OperationBody}), that a
 * satellite mechanism therefore takes a different path there, and that no test
 * noticed because the guards in the suites were too simple. In M2T the guards in
 * the suites all read a parameter directly ({@code ? (p.name = 'target')}); none
 * called a module-level query.
 *
 * <p>Since the satellite mechanism of #135/#137 is language-neutral by design,
 * M2T gets the same guard: a template or loop guard that resolves a module
 * element must keep working.
 *
 * <p><b>SPEC-FIRST:</b> Tests written against MOFM2T v1.0 §7.0 (template guard),
 * §8.2 (query). Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class M2tGuardExpressionRegressionTest {

	private M2tEngine engine;
	private EPackage pkg;
	private EClass withOps;
	private EClass withoutOps;

	@BeforeEach
	void setUp() {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		M2tConfiguration config = M2tConfiguration.builder(oclConfig).build();
		engine = M2tEngines.create(config);

		pkg = EcoreFactory.eINSTANCE.createEPackage();
		pkg.setName("company");
		pkg.setNsURI("http://company");

		withOps = EcoreFactory.eINSTANCE.createEClass();
		withOps.setName("Employee");
		var op = EcoreFactory.eINSTANCE.createEOperation();
		op.setName("hire");
		op.setEType(EcorePackage.Literals.ESTRING);
		withOps.getEOperations().add(op);
		pkg.getEClassifiers().add(withOps);

		withoutOps = EcoreFactory.eINSTANCE.createEClass();
		withoutOps.setName("Address");
		pkg.getEClassifiers().add(withoutOps);
	}

	// ==== Template guard calling a module-level query ====

	// §7.0 + §8.2: a template guard invoking a query sees the query's real result
	@Test
	void templateGuard_callsQuery_guardTrue() throws M2tParseException {
		String mtl = """
				[module m(Ecore)/]
				[query public hasOps(c : EClass) : Boolean = c.eOperations->size() > 0/]
				[template public main(c : EClass) ? (c.hasOps())]
				[file ('out', false)]
				guarded:[c.name/]
				[/file]
				[/template]
				""";
		M2tResult result = executeOn(mtl, withOps);
		assertEquals("guarded:Employee", getFile(result, "out"));
	}

	// §7.0 + §8.2: the same guard denies the element the query rejects
	@Test
	void templateGuard_callsQuery_guardFalse() throws M2tParseException {
		String mtl = """
				[module m(Ecore)/]
				[query public hasOps(c : EClass) : Boolean = c.eOperations->size() > 0/]
				[template public main(c : EClass) ? (c.hasOps())]
				[file ('out', false)]
				guarded:[c.name/]
				[/file]
				[/template]
				""";
		M2tResult result = executeOn(mtl, withoutOps);
		assertTrue(result.generatedFiles().isEmpty(),
				"guard must reject the element: " + result.generatedFiles().keySet());
	}

	// §8.2: a guard invoking a query that itself calls another query
	@Test
	void templateGuard_callsChainedQuery() throws M2tParseException {
		String mtl = """
				[module m(Ecore)/]
				[query public opCount(c : EClass) : Integer = c.eOperations->size()/]
				[query public hasOps(c : EClass) : Boolean = c.opCount() > 0/]
				[template public main(c : EClass) ? (c.hasOps())]
				[file ('out', false)]
				chained:[c.name/]
				[/file]
				[/template]
				""";
		M2tResult result = executeOn(mtl, withOps);
		assertEquals("chained:Employee", getFile(result, "out"));
	}

	// §8.2: a guard combining a query result with the parameter itself
	@Test
	void templateGuard_combinesQueryAndParameter() throws M2tParseException {
		String mtl = """
				[module m(Ecore)/]
				[query public opCount(c : EClass) : Integer = c.eOperations->size()/]
				[template public main(c : EClass) ? (c.opCount() > 0 and c.name.startsWith('Emp'))]
				[file ('out', false)]
				both:[c.name/]
				[/file]
				[/template]
				""";
		M2tResult result = executeOn(mtl, withOps);
		assertEquals("both:Employee", getFile(result, "out"));
	}

	// ==== Loop guard calling a query ====

	// §7.5: a for-loop guard invoking a query filters per iteration
	@Test
	void loopGuard_callsQuery_filtersPerIteration() throws M2tParseException {
		String mtl = """
				[module m(Ecore)/]
				[query public hasOps(c : EClass) : Boolean = c.eOperations->size() > 0/]
				[template public main(p : EPackage)]
				[file ('out', false)]
				[for (c : EClass | p.eClassifiers) ? (c.hasOps())][c.name/];[/for]
				[/file]
				[/template]
				""";
		M2tResult result = executeOn(mtl, pkg);
		assertEquals("Employee;", getFile(result, "out"),
				"only the EClass the query accepts may be emitted");
	}

	// §7.5: the loop guard is evaluated per element, not once for the collection
	@Test
	void loopGuard_evaluatedPerElement() throws M2tParseException {
		String mtl = """
				[module m(Ecore)/]
				[query public opCount(c : EClass) : Integer = c.eOperations->size()/]
				[template public main(p : EPackage)]
				[file ('out', false)]
				[for (c : EClass | p.eClassifiers) ? (c.opCount() = 0)][c.name/];[/for]
				[/file]
				[/template]
				""";
		M2tResult result = executeOn(mtl, pkg);
		assertEquals("Address;", getFile(result, "out"));
	}

	// ==== Guard on an overriding template ====

	// §7.0: a guard on an overriding template that calls a query — guard true,
	// the override wins
	@Test
	void overridingTemplateGuard_callsQuery() throws M2tParseException {
		String base = """
				[module base(Ecore)/]
				[template public render(c : EClass)]base:[c.name/][/template]
				""";
		String child = """
				[module child(Ecore) extends base/]
				[query public hasOps(c : EClass) : Boolean = c.eOperations->size() > 0/]
				[template public render(c : EClass) overrides render ? (c.hasOps())]override:[c.name/][/template]
				[template public main(c : EClass)]
				[file ('out', false)]
				[c.render()/]
				[/file]
				[/template]
				""";
		Module baseModule = engine.parse(base, "base");
		Module childModule = engine.parse(child, "child");
		engine.link(baseModule, childModule);
		M2tResult result = engine.execute(childModule, M2tContext.of(withOps));
		assertEquals("override:Employee", getFile(result, "out"));
	}

	// §7.0: the same override with a guard the query rejects — the base applies
	@Test
	@Disabled("#149 — a call resolving to the overriding template does not fall back "
			+ "to the overridden one when the guard declines; unrelated to the query visibility of #146")
	void overridingTemplateGuard_queryRejects_baseApplies() throws M2tParseException {
		String base = """
				[module base(Ecore)/]
				[template public render(c : EClass)]base:[c.name/][/template]
				""";
		String child = """
				[module child(Ecore) extends base/]
				[query public hasOps(c : EClass) : Boolean = c.eOperations->size() > 0/]
				[template public render(c : EClass) overrides render ? (c.hasOps())]override:[c.name/][/template]
				[template public main(c : EClass)]
				[file ('out', false)]
				[c.render()/]
				[/file]
				[/template]
				""";
		Module baseModule = engine.parse(base, "base");
		Module childModule = engine.parse(child, "child");
		engine.link(baseModule, childModule);
		M2tResult result = engine.execute(childModule, M2tContext.of(withoutOps));
		assertEquals("base:Address", getFile(result, "out"));
	}

	// ---- Helpers ----

	private M2tResult executeOn(String mtlSource, EObject target) throws M2tParseException {
		Module module = engine.parse(mtlSource, "guard_test");
		return engine.execute(module, M2tContext.of(target));
	}

	private static String getFile(M2tResult result, String name) {
		Map<String, String> files = result.generatedFiles();
		assertNotNull(files.get(name), "File '" + name + "' not in: " + files.keySet());
		return files.get(name).strip();
	}
}
