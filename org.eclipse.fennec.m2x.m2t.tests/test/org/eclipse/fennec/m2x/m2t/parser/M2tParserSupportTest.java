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
package org.eclipse.fennec.m2x.m2t.parser;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link M2tParserSupport} — MOFM2T v1.0 concrete syntax (§8.2).
 *
 * <p>Tests verify that valid MOFM2T source parses without errors and produces
 * the expected CST structure. Invalid source must produce {@link M2tParseException}.
 */
class M2tParserSupportTest {

	private M2tParserSupport parser;

	@BeforeEach
	void setUp() {
		parser = new M2tParserSupport();
	}

	// ==================== Module Declaration ====================

	@Nested
	@DisplayName("Module Declaration (§8.2)")
	class ModuleDeclarationTests {

		@Test
		@DisplayName("minimal module with single metamodel")
		void minimalModule() throws M2tParseException {
			String source = "[module myModule(UML)/]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			assertNotNull(ctx);
			assertNotNull(ctx.moduleDecl());
			assertEquals("myModule", ctx.moduleDecl().pathName().getText());
			assertEquals(1, ctx.moduleDecl().metamodelList().pathName().size());
			assertEquals("UML", ctx.moduleDecl().metamodelList().pathName(0).getText());
		}

		@Test
		@DisplayName("module with multiple metamodels")
		void multipleMetamodels() throws M2tParseException {
			String source = "[module gen(UML, Types)/]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			assertEquals(2, ctx.moduleDecl().metamodelList().pathName().size());
			assertEquals("UML", ctx.moduleDecl().metamodelList().pathName(0).getText());
			assertEquals("Types", ctx.moduleDecl().metamodelList().pathName(1).getText());
		}

		@Test
		@DisplayName("module with qualified metamodel name")
		void qualifiedMetamodelName() throws M2tParseException {
			String source = "[module gen(org::eclipse::uml)/]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			assertEquals("org::eclipse::uml",
					ctx.moduleDecl().metamodelList().pathName(0).getText());
		}

		@Test
		@DisplayName("module with extends clause")
		void moduleWithExtends() throws M2tParseException {
			String source = "[module child(UML) extends parent/]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			assertNotNull(ctx.moduleDecl().extendsDecl());
			assertEquals("parent", ctx.moduleDecl().extendsDecl().pathName(0).getText());
		}
	}

	// ==================== Imports ====================

	@Nested
	@DisplayName("Import Declaration (§8.2)")
	class ImportTests {

		@Test
		@DisplayName("single import")
		void singleImport() throws M2tParseException {
			String source = "[module m(UML)/]\n[import other::module/]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			assertEquals(1, ctx.importDecl().size());
			assertEquals("other::module", ctx.importDecl(0).pathName().getText());
		}

		@Test
		@DisplayName("multiple imports")
		void multipleImports() throws M2tParseException {
			String source = "[module m(UML)/]\n[import a/]\n[import b::c/]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			assertEquals(2, ctx.importDecl().size());
		}
	}

	// ==================== Templates ====================

	@Nested
	@DisplayName("Template Definition (§8.2)")
	class TemplateTests {

		@Test
		@DisplayName("minimal public template")
		void minimalTemplate() throws M2tParseException {
			String source = "[module m(UML)/]\n" +
					"[template public gen(c : Class)]\n" +
					"Hello\n" +
					"[/template]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			assertEquals(1, ctx.moduleElement().size());
			assertNotNull(ctx.moduleElement(0).templateDef());

			M2tParser.SignatureContext sig = ctx.moduleElement(0).templateDef().signature();
			assertNotNull(sig.visibility());
			assertEquals("public", sig.visibility().getText());
			assertEquals("gen", sig.pathName().getText());
			assertEquals(1, sig.argList().argDecl().size());
			assertEquals("c", sig.argList().argDecl(0).identifier().getText());
			assertEquals("Class", sig.argList().argDecl(0).typeExpression().getText());
		}

		@Test
		@DisplayName("template with guard")
		void templateWithGuard() throws M2tParseException {
			String source = "[module m(UML)/]\n" +
					"[template public gen(c : Class) ? (c.isAbstract = false)]\n" +
					"concrete\n" +
					"[/template]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			M2tParser.SignatureContext sig = ctx.moduleElement(0).templateDef().signature();
			assertNotNull(sig.guard());
		}

		@Test
		@DisplayName("template with overrides")
		void templateWithOverrides() throws M2tParseException {
			String source = "[module m(UML)/]\n" +
					"[template public gen(c : Class) overrides baseGen]\n" +
					"text\n" +
					"[/template]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			M2tParser.SignatureContext sig = ctx.moduleElement(0).templateDef().signature();
			assertNotNull(sig.overridesDecl());
			assertEquals("baseGen", sig.overridesDecl().pathName(0).getText());
		}

		@Test
		@DisplayName("private template")
		void privateTemplate() throws M2tParseException {
			String source = "[module m(UML)/]\n" +
					"[template private helper(e : Element)]\n" +
					"helper text\n" +
					"[/template]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			assertEquals("private",
					ctx.moduleElement(0).templateDef().signature().visibility().getText());
		}

		@Test
		@DisplayName("template with init section")
		void templateWithInit() throws M2tParseException {
			String source = "[module m(UML)/]\n" +
					"[template public gen(c : Class) { count : Integer = 0; }]\n" +
					"text\n" +
					"[/template]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			M2tParser.SignatureContext sig = ctx.moduleElement(0).templateDef().signature();
			assertNotNull(sig.initSection());
			assertEquals(1, sig.initSection().variableDeclaration().size());
			assertEquals("count", sig.initSection().variableDeclaration(0).identifier().getText());
		}

		@Test
		@DisplayName("template body with text and inline expression")
		void templateBodyWithExpression() throws M2tParseException {
			String source = "[module m(UML)/]\n" +
					"[template public gen(c : Class)]\n" +
					"class [c.name/] {\n" +
					"}\n" +
					"[/template]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			M2tParser.BodyContext body = ctx.moduleElement(0).templateDef().body();
			assertFalse(body.bodyElement().isEmpty());
		}
	}

	// ==================== Query ====================

	@Nested
	@DisplayName("Query Definition (§8.2)")
	class QueryTests {

		@Test
		@DisplayName("simple query")
		void simpleQuery() throws M2tParseException {
			String source = "[module m(UML)/]\n" +
					"[query public fullName(c : Class) : String = c.name/]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			assertNotNull(ctx.moduleElement(0).queryDef());
			assertEquals("fullName", ctx.moduleElement(0).queryDef().pathName().getText());
		}

		@Test
		@DisplayName("query with complex OCL expression")
		void queryWithComplexExpression() throws M2tParseException {
			String source = "[module m(UML)/]\n" +
					"[query public allOps(c : Class) : Set(Operation) = " +
					"c.ownedOperation->union(c.superClass->collect(allOps()))/]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			assertNotNull(ctx.moduleElement(0).queryDef());
		}
	}

	// ==================== For Block ====================

	@Nested
	@DisplayName("For Block (§8.2)")
	class ForBlockTests {

		@Test
		@DisplayName("simple for loop")
		void simpleForLoop() throws M2tParseException {
			String source = "[module m(UML)/]\n" +
					"[template public gen(c : Class)]\n" +
					"[for (a : Property | c.ownedAttribute)]\n" +
					"  [a.name/]\n" +
					"[/for]\n" +
					"[/template]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			M2tParser.BodyContext body = ctx.moduleElement(0).templateDef().body();
			// Find the for block in body elements
			boolean hasFor = body.bodyElement().stream()
					.anyMatch(e -> e.forBlock() != null);
			assertTrue(hasFor);
		}

		@Test
		@DisplayName("for with separator")
		void forWithSeparator() throws M2tParseException {
			String source = "[module m(UML)/]\n" +
					"[template public gen(c : Class)]\n" +
					"[for (a : Property | c.ownedAttribute) separator(', ')]\n" +
					"[a.name/]\n" +
					"[/for]\n" +
					"[/template]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			M2tParser.ForBlockContext forBlock = ctx.moduleElement(0).templateDef()
					.body().bodyElement().stream()
					.filter(e -> e.forBlock() != null)
					.findFirst().get().forBlock();
			assertNotNull(forBlock.separatorClause());
		}

		@Test
		@DisplayName("for with before, separator, and after")
		void forWithBeforeSeparatorAfter() throws M2tParseException {
			String source = "[module m(UML)/]\n" +
					"[template public gen(c : Class)]\n" +
					"[for (a : Property | c.ownedAttribute) before('(') separator(', ') after(')')]\n" +
					"[a.name/]\n" +
					"[/for]\n" +
					"[/template]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			M2tParser.ForBlockContext forBlock = ctx.moduleElement(0).templateDef()
					.body().bodyElement().stream()
					.filter(e -> e.forBlock() != null)
					.findFirst().get().forBlock();
			assertNotNull(forBlock.beforeClause());
			assertNotNull(forBlock.separatorClause());
			assertNotNull(forBlock.afterClause());
		}
	}

	// ==================== If Block ====================

	@Nested
	@DisplayName("If Block (§8.2)")
	class IfBlockTests {

		@Test
		@DisplayName("simple if")
		void simpleIf() throws M2tParseException {
			String source = "[module m(UML)/]\n" +
					"[template public gen(c : Class)]\n" +
					"[if (c.isAbstract)]\n" +
					"abstract\n" +
					"[/if]\n" +
					"[/template]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			boolean hasIf = ctx.moduleElement(0).templateDef().body().bodyElement().stream()
					.anyMatch(e -> e.ifBlock() != null);
			assertTrue(hasIf);
		}

		@Test
		@DisplayName("if-else")
		void ifElse() throws M2tParseException {
			String source = "[module m(UML)/]\n" +
					"[template public gen(c : Class)]\n" +
					"[if (c.isAbstract)]\n" +
					"abstract\n" +
					"[else]\n" +
					"concrete\n" +
					"[/if]\n" +
					"[/template]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			M2tParser.IfBlockContext ifBlock = ctx.moduleElement(0).templateDef()
					.body().bodyElement().stream()
					.filter(e -> e.ifBlock() != null)
					.findFirst().get().ifBlock();
			assertNotNull(ifBlock.elseBlock());
		}

		@Test
		@DisplayName("if-elseif-else")
		void ifElseIfElse() throws M2tParseException {
			String source = "[module m(UML)/]\n" +
					"[template public gen(c : Class)]\n" +
					"[if (c.isAbstract)]\n" +
					"abstract\n" +
					"[elseif (c.isFinal)]\n" +
					"final\n" +
					"[else]\n" +
					"normal\n" +
					"[/if]\n" +
					"[/template]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			M2tParser.IfBlockContext ifBlock = ctx.moduleElement(0).templateDef()
					.body().bodyElement().stream()
					.filter(e -> e.ifBlock() != null)
					.findFirst().get().ifBlock();
			assertEquals(1, ifBlock.elseIfBlock().size());
			assertNotNull(ifBlock.elseBlock());
		}
	}

	// ==================== Let Block ====================

	@Nested
	@DisplayName("Let Block (§8.2)")
	class LetBlockTests {

		@Test
		@DisplayName("simple let")
		void simpleLet() throws M2tParseException {
			String source = "[module m(UML)/]\n" +
					"[template public gen(c : Class)]\n" +
					"[let name : String = c.name]\n" +
					"[name/]\n" +
					"[/let]\n" +
					"[/template]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			boolean hasLet = ctx.moduleElement(0).templateDef().body().bodyElement().stream()
					.anyMatch(e -> e.letBlock() != null);
			assertTrue(hasLet);
		}
	}

	// ==================== File Block ====================

	@Nested
	@DisplayName("File Block (§8.2)")
	class FileBlockTests {

		@Test
		@DisplayName("file block with URL expression")
		void fileBlock() throws M2tParseException {
			String source = "[module m(UML)/]\n" +
					"[template public gen(c : Class)]\n" +
					"[file (c.name + '.java', false, c.name)]\n" +
					"class [c.name/] {}\n" +
					"[/file]\n" +
					"[/template]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			boolean hasFile = ctx.moduleElement(0).templateDef().body().bodyElement().stream()
					.anyMatch(e -> e.fileBlock() != null);
			assertTrue(hasFile);
		}
	}

	// ==================== Template Invocation ====================

	@Nested
	@DisplayName("Template Invocation (§8.2)")
	class TemplateInvocationTests {

		@Test
		@DisplayName("simple template invocation")
		void simpleInvocation() throws M2tParseException {
			String source = "[module m(UML)/]\n" +
					"[template public gen(c : Class)]\n" +
					"[genBody(c)/]\n" +
					"[/template]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			// Template invocations are inline expressions: [expr/]
			// The parser sees genBody(c) as an OperationCallExp in an inlineExpression
			boolean hasInline = ctx.moduleElement(0).templateDef().body().bodyElement().stream()
					.anyMatch(e -> e.inlineExpression() != null);
			assertTrue(hasInline);
		}
	}

	// ==================== Spec Examples ====================

	@Nested
	@DisplayName("Spec Examples (MOFM2T §7, Annex A)")
	class SpecExampleTests {

		@Test
		@DisplayName("§7 classToJava example")
		void classToJavaExample() throws M2tParseException {
			String source = "[module classGen(UML)/]\n" +
					"[template public classToJava(c : Class)]\n" +
					"class [c.name/]\n" +
					"{\n" +
					"    [for (a : Property | c.ownedAttribute)]\n" +
					"    [a.type.name/] [a.name/];\n" +
					"    [/for]\n" +
					"}\n" +
					"[/template]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			assertNotNull(ctx);
			assertEquals(1, ctx.moduleElement().size());
		}

		@Test
		@DisplayName("Annex A DDL example structure")
		void annexDdlExample() throws M2tParseException {
			String source = "[module DDLgen(RDBMS)/]\n" +
					"[template public SchemaToDDL(s : Schema)]\n" +
					"[for (t : Table | s.table)]\n" +
					"[TableToDDL(t)/]\n" +
					"[/for]\n" +
					"[/template]\n" +
					"[template public TableToDDL(t : Table)]\n" +
					"CREATE TABLE [t.name/] (\n" +
					"[for (c : Column | t.column) separator(', ')]\n" +
					"[c.name/] [c.type/]\n" +
					"[/for]\n" +
					");\n" +
					"[/template]\n";
			M2tParser.ModuleContext ctx = parser.parse(source);

			assertEquals(2, ctx.moduleElement().size());
		}
	}

	// ==================== Error Handling ====================

	@Nested
	@DisplayName("Error Handling")
	class ErrorTests {

		@Test
		@DisplayName("null source throws NullPointerException")
		void nullSource() {
			assertThrows(NullPointerException.class, () -> parser.parse(null));
		}

		@Test
		@DisplayName("empty source throws M2tParseException")
		void emptySource() {
			assertThrows(M2tParseException.class, () -> parser.parse(""));
		}

		@Test
		@DisplayName("malformed module throws M2tParseException")
		void malformedModule() {
			assertThrows(M2tParseException.class, () -> parser.parse("[module/]"));
		}
	}
}
