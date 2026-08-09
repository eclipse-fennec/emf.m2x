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

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.engine.M2tEngines;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.parser.M2tParseResult;
import org.eclipse.fennec.m2x.m2t.parser.M2tParserSupport;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for P3-4: Module Composition, Template Resolution, Overrides, and Super.
 */
class M2tModuleCompositionTest {

	private M2tParserSupport parserSupport;
	private M2tEngine engine;
	private EPackage testPkg;
	private EClass elementClass;
	private EClass specialClass;

	@BeforeEach
	void setUp() {
		parserSupport = new M2tParserSupport();
		OclParserSupport oclParser = new OclParserSupport();
		OclConfiguration oclConfig = OclConfiguration.builder(oclParser).build();
		M2tConfiguration config = M2tConfiguration.builder(oclConfig).build();
		engine = M2tEngines.create(config);

		// Build test metamodel
		EcoreFactory ef = EcoreFactory.eINSTANCE;
		testPkg = ef.createEPackage();
		testPkg.setName("test");
		testPkg.setNsURI("http://test/1.0");
		testPkg.setNsPrefix("test");

		elementClass = ef.createEClass();
		elementClass.setName("Element");
		EAttribute nameAttr = ef.createEAttribute();
		nameAttr.setName("name");
		nameAttr.setEType(EcorePackage.eINSTANCE.getEString());
		elementClass.getEStructuralFeatures().add(nameAttr);
		testPkg.getEClassifiers().add(elementClass);

		specialClass = ef.createEClass();
		specialClass.setName("Special");
		specialClass.getESuperTypes().add(elementClass);
		testPkg.getEClassifiers().add(specialClass);

		EPackage.Registry.INSTANCE.put(testPkg.getNsURI(), testPkg);
		EPackage.Registry.INSTANCE.put("test", testPkg);
	}

	// ==================== Parser: Pending References ====================

	@Nested
	@DisplayName("Parser: Pending References")
	class ParserPendingRefs {

		@Test
		@DisplayName("Collects extends names from module declaration")
		void extendsNamesCollected() throws Exception {
			String source = "[module child(ecore) extends base/]\n"
					+ "[template public main(e : EClass)]hello[/template]\n";
			M2tParseResult result = parserSupport.buildModuleWithPending(source, "child");
			assertEquals(List.of("base"), result.extendsNames());
			assertEquals("child", result.module().getName());
		}

		@Test
		@DisplayName("Collects import names from import declarations")
		void importNamesCollected() throws Exception {
			String source = "[module main(ecore)/]\n"
					+ "[import util/]\n"
					+ "[import common::helpers/]\n"
					+ "[template public main(e : EClass)]hello[/template]\n";
			M2tParseResult result = parserSupport.buildModuleWithPending(source, "main");
			assertEquals(List.of("util", "common::helpers"), result.importNames());
		}

		@Test
		@DisplayName("Collects override names from template signature")
		void overrideNamesCollected() throws Exception {
			String source = "[module child(ecore)/]\n"
					+ "[template public render(e : EClass) overrides render]overridden[/template]\n";
			M2tParseResult result = parserSupport.buildModuleWithPending(source, "child");

			Map<Template, List<String>> overrides = result.overrideNames();
			assertEquals(1, overrides.size());

			Template template = overrides.keySet().iterator().next();
			assertEquals("render", template.getName());
			assertEquals(List.of("render"), overrides.get(template));
		}

		@Test
		@DisplayName("Inline expressions are parsed as OperationCallExp (resolved by linker)")
		void inlineExpressionsNotInPendingInvocations() throws Exception {
			// [helper(e)/] is parsed by ANTLR as inlineExpression → OperationCallExp,
			// not as templateInvocation. The linker's Phase 5 resolves these.
			String source = "[module main(ecore)/]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[helper(e)/]\n"
					+ "[/template]\n"
					+ "[template public helper(e : EClass)]help[/template]\n";
			M2tParseResult result = parserSupport.buildModuleWithPending(source, "main");

			// pendingInvocations is empty because grammar routes to inlineExpression
			assertTrue(result.invocationNames().isEmpty());
		}
	}

	// ==================== Intra-Module Linking ====================

	@Nested
	@DisplayName("Intra-Module Linking")
	class IntraModuleLinking {

		@Test
		@DisplayName("Template invocation resolves to local template")
		void templateInvocationResolvesLocally() throws Exception {
			String source = "[module main(ecore)/]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[helper(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n"
					+ "[template public helper(e : EClass)]HELLO[/template]\n";
			Module module = engine.parse(source, "main");
			EObject input = EcoreFactory.eINSTANCE.createEClass();
			M2tResult result = engine.execute(module, M2tContext.of(input));

			assertEquals("HELLO", result.generatedFiles().get("out.txt").strip());
		}

		@Test
		@DisplayName("Query invocation resolves to local query")
		void queryInvocationResolvesLocally() throws Exception {
			String source = "[module main(ecore)/]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[greet(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n"
					+ "[query public greet(e : EClass) : String = 'Hi'/]\n";
			Module module = engine.parse(source, "main");
			EObject input = EcoreFactory.eINSTANCE.createEClass();
			M2tResult result = engine.execute(module, M2tContext.of(input));

			assertEquals("Hi", result.generatedFiles().get("out.txt").strip());
		}

		@Test
		@DisplayName("Macro invocation resolves to local macro")
		void macroInvocationResolvesLocally() throws Exception {
			EClass testClass = EcoreFactory.eINSTANCE.createEClass();
			testClass.setName("Foo");
			String source = "[module main(ecore)/]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[wrap(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n"
					+ "[macro wrap(e : EClass)]<[e.name/]>[/macro]\n";
			Module module = engine.parse(source, "main");
			M2tResult result = engine.execute(module, M2tContext.of(testClass));

			assertEquals("<Foo>", result.generatedFiles().get("out.txt").strip());
		}
	}

	// ==================== Multi-Module Extends ====================

	@Nested
	@DisplayName("Multi-Module: Extends")
	class MultiModuleExtends {

		@Test
		@DisplayName("Child module can invoke templates from extended module")
		void extendsInheritsTemplates() throws Exception {
			String baseSource = "[module base(ecore)/]\n"
					+ "[template public greet(e : EClass)]Hello[/template]\n";
			String childSource = "[module child(ecore) extends base/]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[greet(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";

			Module baseModule = engine.parse(baseSource, "base");
			Module childModule = engine.parse(childSource, "child");
			engine.link(baseModule, childModule);

			EObject input = EcoreFactory.eINSTANCE.createEClass();
			M2tResult result = engine.execute(childModule, M2tContext.of(input));
			assertEquals("Hello", result.generatedFiles().get("out.txt").strip());
		}

		@Test
		@DisplayName("Private templates are NOT visible via extends")
		void extendsPrivateNotVisible() throws Exception {
			String baseSource = "[module base(ecore)/]\n"
					+ "[template private secret(e : EClass)]SECRET[/template]\n"
					+ "[template public greet(e : EClass)]Hello[/template]\n";
			String childSource = "[module child(ecore) extends base/]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[secret(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";

			Module baseModule = engine.parse(baseSource, "base");
			Module childModule = engine.parse(childSource, "child");
			List<String> warnings = engine.link(baseModule, childModule);

			assertTrue(warnings.stream().anyMatch(w -> w.contains("secret")),
					"Expected warning about unresolved 'secret': " + warnings);
		}

		@Test
		@DisplayName("Transitive extends: A extends B extends C")
		void transitiveExtends() throws Exception {
			String cSource = "[module c(ecore)/]\n"
					+ "[template public baseGreet(e : EClass)]BASE[/template]\n";
			String bSource = "[module b(ecore) extends c/]\n"
					+ "[template public midGreet(e : EClass)]MID[/template]\n";
			String aSource = "[module a(ecore) extends b/]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[baseGreet(e)/][midGreet(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";

			Module cMod = engine.parse(cSource, "c");
			Module bMod = engine.parse(bSource, "b");
			Module aMod = engine.parse(aSource, "a");
			List<String> warnings = engine.link(cMod, bMod, aMod);
			assertTrue(warnings.isEmpty(), "Unexpected warnings: " + warnings);

			EObject input = EcoreFactory.eINSTANCE.createEClass();
			M2tResult result = engine.execute(aMod, M2tContext.of(input));
			assertEquals("BASEMID", result.generatedFiles().get("out.txt").strip());
		}

		@Test
		@DisplayName("Override template in child module")
		void extendsWithOverride() throws Exception {
			String baseSource = "[module base(ecore)/]\n"
					+ "[template public greet(e : EClass)]Hello[/template]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[greet(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";
			String childSource = "[module child(ecore) extends base/]\n"
					+ "[template public greet(e : EClass) overrides greet]Hallo[/template]\n";

			Module baseModule = engine.parse(baseSource, "base");
			Module childModule = engine.parse(childSource, "child");
			engine.link(baseModule, childModule);

			EObject input = EcoreFactory.eINSTANCE.createEClass();
			M2tResult result = engine.execute(baseModule, M2tContext.of(input));
			assertEquals("Hallo", result.generatedFiles().get("out.txt").strip());
		}
	}

	// ==================== Multi-Module Imports ====================

	@Nested
	@DisplayName("Multi-Module: Imports")
	class MultiModuleImports {

		@Test
		@DisplayName("Imported module templates are accessible")
		void importPublicTemplates() throws Exception {
			String utilSource = "[module util(ecore)/]\n"
					+ "[template public render(e : EClass)]Rendered[/template]\n";
			String mainSource = "[module main(ecore)/]\n"
					+ "[import util/]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[render(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";

			Module utilModule = engine.parse(utilSource, "util");
			Module mainModule = engine.parse(mainSource, "main");
			engine.link(utilModule, mainModule);

			EObject input = EcoreFactory.eINSTANCE.createEClass();
			M2tResult result = engine.execute(mainModule, M2tContext.of(input));
			assertEquals("Rendered", result.generatedFiles().get("out.txt").strip());
		}

		@Test
		@DisplayName("Private templates are NOT accessible via import")
		void importPrivateNotVisible() throws Exception {
			String utilSource = "[module util(ecore)/]\n"
					+ "[template private internal(e : EClass)]INTERNAL[/template]\n";
			String mainSource = "[module main(ecore)/]\n"
					+ "[import util/]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[internal(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";

			Module utilModule = engine.parse(utilSource, "util");
			Module mainModule = engine.parse(mainSource, "main");
			List<String> warnings = engine.link(utilModule, mainModule);

			assertTrue(warnings.stream().anyMatch(w -> w.contains("internal")),
					"Expected warning about unresolved 'internal': " + warnings);
		}

		@Test
		@DisplayName("Query from imported module")
		void importQuery() throws Exception {
			String utilSource = "[module util(ecore)/]\n"
					+ "[query public prefix(e : EClass) : String = 'PREFIX'/]\n";
			String mainSource = "[module main(ecore)/]\n"
					+ "[import util/]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[prefix(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";

			Module utilModule = engine.parse(utilSource, "util");
			Module mainModule = engine.parse(mainSource, "main");
			engine.link(utilModule, mainModule);

			EObject input = EcoreFactory.eINSTANCE.createEClass();
			M2tResult result = engine.execute(mainModule, M2tContext.of(input));
			assertEquals("PREFIX", result.generatedFiles().get("out.txt").strip());
		}
	}

	// ==================== Override Resolution ====================

	@Nested
	@DisplayName("Override Resolution")
	class OverrideResolution {

		@Test
		@DisplayName("Simple override replaces base template")
		void simpleOverride() throws Exception {
			String baseSource = "[module base(ecore)/]\n"
					+ "[template public render(e : EClass)]Base[/template]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[render(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";
			String childSource = "[module child(ecore) extends base/]\n"
					+ "[template public render(e : EClass) overrides render]Child[/template]\n";

			Module baseMod = engine.parse(baseSource, "base");
			Module childMod = engine.parse(childSource, "child");
			engine.link(baseMod, childMod);

			EObject input = EcoreFactory.eINSTANCE.createEClass();
			M2tResult result = engine.execute(baseMod, M2tContext.of(input));
			assertEquals("Child", result.generatedFiles().get("out.txt").strip());
		}

		@Test
		@DisplayName("Override with guard — applied when guard is true")
		void overrideWithGuard() throws Exception {
			String baseSource = "[module base(ecore)/]\n"
					+ "[template public render(e : EClass)]Base[/template]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[render(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";
			String childSource = "[module child(ecore) extends base/]\n"
					+ "[template public render(e : EClass) overrides render ? (e.name = 'special')]Guarded[/template]\n";

			Module baseMod = engine.parse(baseSource, "base");
			Module childMod = engine.parse(childSource, "child");
			engine.link(baseMod, childMod);

			// With matching guard
			EClass special = EcoreFactory.eINSTANCE.createEClass();
			special.setName("special");
			M2tResult result1 = engine.execute(baseMod, M2tContext.of(special));
			assertEquals("Guarded", result1.generatedFiles().get("out.txt").strip());

			// With non-matching guard — falls back to base
			EClass normal = EcoreFactory.eINSTANCE.createEClass();
			normal.setName("normal");
			M2tResult result2 = engine.execute(baseMod, M2tContext.of(normal));
			assertEquals("Base", result2.generatedFiles().get("out.txt").strip());
		}

		@Test
		@DisplayName("No override when guard is false — original template executed")
		void noOverrideWhenGuardFails() throws Exception {
			String baseSource = "[module base(ecore)/]\n"
					+ "[template public render(e : EClass)]Original[/template]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[render(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";
			String childSource = "[module child(ecore) extends base/]\n"
					+ "[template public render(e : EClass) overrides render ? (false)]Never[/template]\n";

			Module baseMod = engine.parse(baseSource, "base");
			Module childMod = engine.parse(childSource, "child");
			engine.link(baseMod, childMod);

			EObject input = EcoreFactory.eINSTANCE.createEClass();
			M2tResult result = engine.execute(baseMod, M2tContext.of(input));
			assertEquals("Original", result.generatedFiles().get("out.txt").strip());
		}
	}

	// ==================== Super ====================

	@Nested
	@DisplayName("Super Invocation")
	class SuperInvocation {

		@Test
		@DisplayName("[super/] calls the overridden template")
		void superCallsOverridden() throws Exception {
			String baseSource = "[module base(ecore)/]\n"
					+ "[template public render(e : EClass)]Base[/template]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[render(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";
			String childSource = "[module child(ecore) extends base/]\n"
					+ "[template public render(e : EClass) overrides render]Before-[super/]-After[/template]\n";

			Module baseMod = engine.parse(baseSource, "base");
			Module childMod = engine.parse(childSource, "child");
			engine.link(baseMod, childMod);

			EObject input = EcoreFactory.eINSTANCE.createEClass();
			M2tResult result = engine.execute(baseMod, M2tContext.of(input));
			assertEquals("Before-Base-After", result.generatedFiles().get("out.txt").strip());
		}

		@Test
		@DisplayName("Nested super chain: C overrides B overrides A")
		void nestedSuperChain() throws Exception {
			String aSource = "[module a(ecore)/]\n"
					+ "[template public render(e : EClass)]A[/template]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[render(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";
			String bSource = "[module b(ecore) extends a/]\n"
					+ "[template public render(e : EClass) overrides render][super/]+B[/template]\n";
			String cSource = "[module c(ecore) extends b/]\n"
					+ "[template public render(e : EClass) overrides render][super/]+C[/template]\n";

			Module aMod = engine.parse(aSource, "a");
			Module bMod = engine.parse(bSource, "b");
			Module cMod = engine.parse(cSource, "c");
			engine.link(aMod, bMod, cMod);

			EObject input = EcoreFactory.eINSTANCE.createEClass();
			M2tResult result = engine.execute(aMod, M2tContext.of(input));
			assertEquals("A+B+C", result.generatedFiles().get("out.txt").strip());
		}
	}

	// ==================== Protected Visibility ====================

	@Nested
	@DisplayName("Protected Visibility")
	class ProtectedVisibility {

		@Test
		@DisplayName("Protected template visible via extends")
		void protectedVisibleViaExtends() throws Exception {
			String baseSource = "[module base(ecore)/]\n"
					+ "[template protected helper(e : EClass)]PROTECTED[/template]\n";
			String childSource = "[module child(ecore) extends base/]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[helper(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";

			Module baseMod = engine.parse(baseSource, "base");
			Module childMod = engine.parse(childSource, "child");
			List<String> warnings = engine.link(baseMod, childMod);
			assertTrue(warnings.isEmpty(), "Expected no warnings: " + warnings);

			EObject input = EcoreFactory.eINSTANCE.createEClass();
			M2tResult result = engine.execute(childMod, M2tContext.of(input));
			assertEquals("PROTECTED", result.generatedFiles().get("out.txt").strip());
		}

		@Test
		@DisplayName("Protected template NOT visible via imports")
		void protectedNotVisibleViaImports() throws Exception {
			String libSource = "[module lib(ecore)/]\n"
					+ "[template protected helper(e : EClass)]SECRET[/template]\n";
			String mainSource = "[module main(ecore)/]\n"
					+ "[import lib/]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[helper(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";

			Module libMod = engine.parse(libSource, "lib");
			Module mainMod = engine.parse(mainSource, "main");
			List<String> warnings = engine.link(libMod, mainMod);

			assertTrue(warnings.stream().anyMatch(w -> w.contains("helper")),
					"Expected warning about unresolved 'helper': " + warnings);
		}

		@Test
		@DisplayName("Protected template can be overridden via extends")
		void protectedOverrideViaExtends() throws Exception {
			String baseSource = "[module base(ecore)/]\n"
					+ "[template protected render(e : EClass)]Base[/template]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[render(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";
			String childSource = "[module child(ecore) extends base/]\n"
					+ "[template protected render(e : EClass) overrides render]Child[/template]\n";

			Module baseMod = engine.parse(baseSource, "base");
			Module childMod = engine.parse(childSource, "child");
			List<String> warnings = engine.link(baseMod, childMod);
			assertTrue(warnings.isEmpty(), "Expected no warnings: " + warnings);

			EObject input = EcoreFactory.eINSTANCE.createEClass();
			M2tResult result = engine.execute(baseMod, M2tContext.of(input));
			assertEquals("Child", result.generatedFiles().get("out.txt").strip());
		}
	}

	// ==================== Acceleo Simple Override Reference Tests ====================

	@Nested
	@DisplayName("Acceleo: Simple Override")
	class AcceleoSimpleOverride {

		@Test
		@DisplayName("Local overrides extended template")
		void acceleoSimpleOverrideUsesLocalOverExtended() throws Exception {
			String importedSource = "[module imported(ecore)/]\n"
					+ "[template public render(e : EClass)]Imported[/template]\n";
			String extendedSource = "[module extended(ecore) extends imported/]\n"
					+ "[template public render(e : EClass) overrides render]Extended[/template]\n";
			String localSource = "[module local(ecore) extends extended/]\n"
					+ "[template public render(e : EClass) overrides render]Local[/template]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[render(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";

			Module importedMod = engine.parse(importedSource, "imported");
			Module extendedMod = engine.parse(extendedSource, "extended");
			Module localMod = engine.parse(localSource, "local");
			engine.link(importedMod, extendedMod, localMod);

			EObject input = EcoreFactory.eINSTANCE.createEClass();
			M2tResult result = engine.execute(localMod, M2tContext.of(input));
			assertEquals("Local", result.generatedFiles().get("out.txt").strip());
		}

		@Test
		@DisplayName("Extended overrides imported template")
		void acceleoSimpleOverrideUsesExtendedOverImported() throws Exception {
			String importedSource = "[module imported(ecore)/]\n"
					+ "[template public render(e : EClass)]Imported[/template]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[render(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";
			String extendedSource = "[module extended(ecore) extends imported/]\n"
					+ "[template public render(e : EClass) overrides render]Extended[/template]\n";

			Module importedMod = engine.parse(importedSource, "imported");
			Module extendedMod = engine.parse(extendedSource, "extended");
			engine.link(importedMod, extendedMod);

			EObject input = EcoreFactory.eINSTANCE.createEClass();
			M2tResult result = engine.execute(importedMod, M2tContext.of(input));
			assertEquals("Extended", result.generatedFiles().get("out.txt").strip());
		}

		@Test
		@DisplayName("Full override chain: local → extended → imported with [super/]")
		void acceleoSimpleOverrideChain() throws Exception {
			String importedSource = "[module imported(ecore)/]\n"
					+ "[template public render(e : EClass)]I[/template]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[render(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";
			String extendedSource = "[module extended(ecore) extends imported/]\n"
					+ "[template public render(e : EClass) overrides render][super/]+E[/template]\n";
			String localSource = "[module local(ecore) extends extended/]\n"
					+ "[template public render(e : EClass) overrides render][super/]+L[/template]\n";

			Module importedMod = engine.parse(importedSource, "imported");
			Module extendedMod = engine.parse(extendedSource, "extended");
			Module localMod = engine.parse(localSource, "local");
			engine.link(importedMod, extendedMod, localMod);

			EObject input = EcoreFactory.eINSTANCE.createEClass();
			M2tResult result = engine.execute(importedMod, M2tContext.of(input));
			assertEquals("I+E+L", result.generatedFiles().get("out.txt").strip());
		}
	}

	// ==================== Acceleo Guard Override Reference Tests ====================

	@Nested
	@DisplayName("Acceleo: Guard Override")
	class AcceleoGuardOverride {

		@Test
		@DisplayName("Override with guard applied when guard is true")
		void acceleoGuardOverrideApplied() throws Exception {
			String baseSource = "[module base(ecore)/]\n"
					+ "[template public render(e : EClass)]Original[/template]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[render(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";
			String childSource = "[module child(ecore) extends base/]\n"
					+ "[template public render(e : EClass) overrides render ? (e.abstract)]Override[/template]\n";

			Module baseMod = engine.parse(baseSource, "base");
			Module childMod = engine.parse(childSource, "child");
			engine.link(baseMod, childMod);

			EClass abstractClass = EcoreFactory.eINSTANCE.createEClass();
			abstractClass.setAbstract(true);
			M2tResult result = engine.execute(baseMod, M2tContext.of(abstractClass));
			assertEquals("Override", result.generatedFiles().get("out.txt").strip());
		}

		@Test
		@DisplayName("Override with guard falls back when guard is false")
		void acceleoGuardOverrideFallback() throws Exception {
			String baseSource = "[module base(ecore)/]\n"
					+ "[template public render(e : EClass)]Original[/template]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[render(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";
			String childSource = "[module child(ecore) extends base/]\n"
					+ "[template public render(e : EClass) overrides render ? (e.abstract)]Override[/template]\n";

			Module baseMod = engine.parse(baseSource, "base");
			Module childMod = engine.parse(childSource, "child");
			engine.link(baseMod, childMod);

			EClass concreteClass = EcoreFactory.eINSTANCE.createEClass();
			concreteClass.setAbstract(false);
			M2tResult result = engine.execute(baseMod, M2tContext.of(concreteClass));
			assertEquals("Original", result.generatedFiles().get("out.txt").strip());
		}
	}

	// ==================== Parameter Type Narrowing Override ====================

	@Nested
	@DisplayName("Acceleo: Parameter Type Narrowing")
	class AcceleoParameterTypeNarrowing {

		@Test
		@DisplayName("Override with narrower param type applies when arg matches")
		void acceleoParameterTypeNarrowingOverride() throws Exception {
			// EDataType is a subtype of EClassifier — test covariant parameter type narrowing
			String baseSource = "[module base(ecore)/]\n"
					+ "[template public render(e : EClassifier)]Base[/template]\n"
					+ "[template public main(e : EClassifier)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[render(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";
			String childSource = "[module child(ecore) extends base/]\n"
					+ "[template public render(e : EDataType) overrides render]Narrowed[/template]\n";

			Module baseMod = engine.parse(baseSource, "base");
			Module childMod = engine.parse(childSource, "child");
			engine.link(baseMod, childMod);

			// Argument is EDataType → narrowed override applies
			EObject dataType = EcoreFactory.eINSTANCE.createEDataType();
			M2tResult result = engine.execute(baseMod, M2tContext.of(dataType));
			assertEquals("Narrowed", result.generatedFiles().get("out.txt").strip());
		}

		@Test
		@DisplayName("Override with narrower param type falls back when arg is too broad")
		void acceleoParameterTypeNarrowingFallback() throws Exception {
			// EDataType is a subtype of EClassifier — arg is EClass (not EDataType) → fallback
			String baseSource = "[module base(ecore)/]\n"
					+ "[template public render(e : EClassifier)]Base[/template]\n"
					+ "[template public main(e : EClassifier)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[render(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";
			String childSource = "[module child(ecore) extends base/]\n"
					+ "[template public render(e : EDataType) overrides render]Narrowed[/template]\n";

			Module baseMod = engine.parse(baseSource, "base");
			Module childMod = engine.parse(childSource, "child");
			engine.link(baseMod, childMod);

			// Argument is EClass (sibling of EDataType, not subtype) → fallback to base
			EObject eClass = EcoreFactory.eINSTANCE.createEClass();
			M2tResult result = engine.execute(baseMod, M2tContext.of(eClass));
			assertEquals("Base", result.generatedFiles().get("out.txt").strip());
		}
	}

	// ==================== Namesake Resolution ====================

	@Nested
	@DisplayName("Namesake Resolution")
	class NamesakeResolution {

		@Test
		@DisplayName("Local template preferred over extended namesake")
		void localTemplatePreferredOverExtended() throws Exception {
			String baseSource = "[module base(ecore)/]\n"
					+ "[template public render(e : EClass)]Base[/template]\n";
			String childSource = "[module child(ecore) extends base/]\n"
					+ "[template public render(e : EClass)]Local[/template]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[render(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";

			Module baseMod = engine.parse(baseSource, "base");
			Module childMod = engine.parse(childSource, "child");
			engine.link(baseMod, childMod);

			EObject input = EcoreFactory.eINSTANCE.createEClass();
			M2tResult result = engine.execute(childMod, M2tContext.of(input));
			assertEquals("Local", result.generatedFiles().get("out.txt").strip());
		}

		@Test
		@DisplayName("Extended template preferred over imported namesake")
		void extendedPreferredOverImported() throws Exception {
			String importedSource = "[module imported(ecore)/]\n"
					+ "[template public render(e : EClass)]Imported[/template]\n";
			String extendedSource = "[module extended(ecore)/]\n"
					+ "[template public render(e : EClass)]Extended[/template]\n";
			String mainSource = "[module main(ecore) extends extended/]\n"
					+ "[import imported/]\n"
					+ "[template public main(e : EClass)]\n"
					+ "[file ('out.txt', false)]\n"
					+ "[render(e)/]\n"
					+ "[/file]\n"
					+ "[/template]\n";

			Module importedMod = engine.parse(importedSource, "imported");
			Module extendedMod = engine.parse(extendedSource, "extended");
			Module mainMod = engine.parse(mainSource, "main");
			engine.link(importedMod, extendedMod, mainMod);

			EObject input = EcoreFactory.eINSTANCE.createEClass();
			M2tResult result = engine.execute(mainMod, M2tContext.of(input));
			assertEquals("Extended", result.generatedFiles().get("out.txt").strip());
		}
	}
}
