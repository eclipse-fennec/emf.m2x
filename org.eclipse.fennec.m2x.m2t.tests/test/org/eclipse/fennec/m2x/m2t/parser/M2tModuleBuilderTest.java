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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.model.m2t.FileBlock;
import org.eclipse.fennec.m2x.model.m2t.ForBlock;
import org.eclipse.fennec.m2x.model.m2t.OpenModeKind;
import org.eclipse.fennec.m2x.model.m2t.IfBlock;
import org.eclipse.fennec.m2x.model.m2t.LetBlock;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.ProtectedAreaBlock;
import org.eclipse.fennec.m2x.model.m2t.Query;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.m2t.TemplateExpression;
import org.eclipse.fennec.m2x.model.m2t.TextExpression;
import org.eclipse.fennec.m2x.model.m2t.VisibilityKind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link M2tModuleBuilder} — CST to AST transformation.
 *
 * <p>Verifies that MOFM2T source text parsed via {@link M2tParserSupport}
 * produces the correct EMF Module AST structure.
 */
class M2tModuleBuilderTest {

	private M2tParserSupport parser;

	@BeforeEach
	void setUp() {
		parser = new M2tParserSupport();
	}

	private Module buildModule(String source) throws M2tParseException {
		return parser.buildModule(source, "test");
	}

	// ==================== Module ====================

	@Nested
	@DisplayName("Module")
	class ModuleTests {

		@Test
		@DisplayName("module name is set")
		void moduleName() throws M2tParseException {
			Module m = buildModule("[module myGen(UML)/]\n");
			assertEquals("myGen", m.getName());
		}

		@Test
		@DisplayName("module with template has one owned element")
		void moduleWithTemplate() throws M2tParseException {
			Module m = buildModule(
					"[module m(UML)/]\n" +
					"[template public gen(c : EObject)]\n" +
					"Hello\n" +
					"[/template]\n");
			assertEquals(1, m.getOwnedModuleElement().size());
			assertInstanceOf(Template.class, m.getOwnedModuleElement().get(0));
		}
	}

	// ==================== Template ====================

	@Nested
	@DisplayName("Template")
	class TemplateTests {

		@Test
		@DisplayName("template name and visibility")
		void templateNameAndVisibility() throws M2tParseException {
			Module m = buildModule(
					"[module m(UML)/]\n" +
					"[template public gen(c : EObject)]\n" +
					"text\n" +
					"[/template]\n");
			Template t = (Template) m.getOwnedModuleElement().get(0);
			assertEquals("gen", t.getName());
			assertEquals(VisibilityKind.PUBLIC, t.getVisibility());
		}

		@Test
		@DisplayName("private template")
		void privateTemplate() throws M2tParseException {
			Module m = buildModule(
					"[module m(UML)/]\n" +
					"[template private helper(e : EObject)]\n" +
					"text\n" +
					"[/template]\n");
			Template t = (Template) m.getOwnedModuleElement().get(0);
			assertEquals(VisibilityKind.PRIVATE, t.getVisibility());
		}

		@Test
		@DisplayName("template with one parameter")
		void templateParameter() throws M2tParseException {
			Module m = buildModule(
					"[module m(UML)/]\n" +
					"[template public gen(c : EObject)]\n" +
					"text\n" +
					"[/template]\n");
			Template t = (Template) m.getOwnedModuleElement().get(0);
			assertEquals(1, t.getParameter().size());
			assertEquals("c", t.getParameter().get(0).getName());
		}

		@Test
		@DisplayName("template body contains text")
		void templateBodyText() throws M2tParseException {
			Module m = buildModule(
					"[module m(UML)/]\n" +
					"[template public gen(c : EObject)]\n" +
					"Hello World\n" +
					"[/template]\n");
			Template t = (Template) m.getOwnedModuleElement().get(0);
			assertFalse(t.getBody().isEmpty());
			// First body element should be text
			boolean hasText = t.getBody().stream()
					.anyMatch(e -> e instanceof TextExpression);
			assertTrue(hasText);
		}

		@Test
		@DisplayName("template with guard expression")
		void templateWithGuard() throws M2tParseException {
			Module m = buildModule(
					"[module m(UML)/]\n" +
					"[template public gen(c : EObject) ? (not c.oclIsUndefined())]\n" +
					"text\n" +
					"[/template]\n");
			Template t = (Template) m.getOwnedModuleElement().get(0);
			assertNotNull(t.getGuard());
		}
	}

	// ==================== Query ====================

	@Nested
	@DisplayName("Query")
	class QueryTests {

		@Test
		@DisplayName("query name and return type")
		void queryNameAndReturnType() throws M2tParseException {
			Module m = buildModule(
					"[module m(UML)/]\n" +
					"[query public myQuery(e : EObject) : String = e.toString()/]\n");
			assertEquals(1, m.getOwnedModuleElement().size());
			Query q = (Query) m.getOwnedModuleElement().get(0);
			assertEquals("myQuery", q.getName());
			assertNotNull(q.getExpression());
		}
	}

	// ==================== ForBlock ====================

	@Nested
	@DisplayName("ForBlock")
	class ForBlockTests {

		@Test
		@DisplayName("for block with loop variable and iter set")
		void forBlock() throws M2tParseException {
			Module m = buildModule(
					"[module m(UML)/]\n" +
					"[template public gen(c : EObject)]\n" +
					"[for (child : EObject | c.eContents())]\n" +
					"  item\n" +
					"[/for]\n" +
					"[/template]\n");
			Template t = (Template) m.getOwnedModuleElement().get(0);
			ForBlock forBlock = findFirst(t, ForBlock.class);
			assertNotNull(forBlock, "ForBlock should exist in template body");
			assertNotNull(forBlock.getLoopVariable());
			assertEquals("child", forBlock.getLoopVariable().getName());
			assertNotNull(forBlock.getIterSet());
		}

		@Test
		@DisplayName("for block with separator")
		void forBlockWithSeparator() throws M2tParseException {
			Module m = buildModule(
					"[module m(UML)/]\n" +
					"[template public gen(c : EObject)]\n" +
					"[for (child : EObject | c.eContents()) separator(', ')]\n" +
					"item\n" +
					"[/for]\n" +
					"[/template]\n");
			Template t = (Template) m.getOwnedModuleElement().get(0);
			ForBlock forBlock = findFirst(t, ForBlock.class);
			assertNotNull(forBlock);
			assertNotNull(forBlock.getEach(), "separator should map to 'each'");
		}
	}

	// ==================== IfBlock ====================

	@Nested
	@DisplayName("IfBlock")
	class IfBlockTests {

		@Test
		@DisplayName("simple if block")
		void simpleIf() throws M2tParseException {
			Module m = buildModule(
					"[module m(UML)/]\n" +
					"[template public gen(c : EObject)]\n" +
					"[if (c.oclIsKindOf(EObject))]\n" +
					"yes\n" +
					"[/if]\n" +
					"[/template]\n");
			Template t = (Template) m.getOwnedModuleElement().get(0);
			IfBlock ifBlock = findFirst(t, IfBlock.class);
			assertNotNull(ifBlock, "IfBlock should exist in template body");
			assertNotNull(ifBlock.getIfExpr());
		}

		@Test
		@DisplayName("if-else block")
		void ifElse() throws M2tParseException {
			Module m = buildModule(
					"[module m(UML)/]\n" +
					"[template public gen(c : EObject)]\n" +
					"[if (true)]\n" +
					"yes\n" +
					"[else]\n" +
					"no\n" +
					"[/if]\n" +
					"[/template]\n");
			Template t = (Template) m.getOwnedModuleElement().get(0);
			IfBlock ifBlock = findFirst(t, IfBlock.class);
			assertNotNull(ifBlock);
			assertNotNull(ifBlock.getElse(), "else branch should be present");
		}
	}

	// ==================== LetBlock ====================

	@Nested
	@DisplayName("LetBlock")
	class LetBlockTests {

		@Test
		@DisplayName("let block with variable")
		void letBlock() throws M2tParseException {
			Module m = buildModule(
					"[module m(UML)/]\n" +
					"[template public gen(c : EObject)]\n" +
					"[let name : String = c.toString()]\n" +
					"[name/]\n" +
					"[/let]\n" +
					"[/template]\n");
			Template t = (Template) m.getOwnedModuleElement().get(0);
			LetBlock letBlock = findFirst(t, LetBlock.class);
			assertNotNull(letBlock, "LetBlock should exist in template body");
			assertNotNull(letBlock.getLetVariable());
			assertEquals("name", letBlock.getLetVariable().getName());
		}
	}

	// ==================== FileBlock ====================

	@Nested
	@DisplayName("FileBlock")
	class FileBlockTests {

		@Test
		@DisplayName("file block with URL expression")
		void fileBlock() throws M2tParseException {
			Module m = buildModule(
					"[module m(UML)/]\n" +
					"[template public gen(c : EObject)]\n" +
					"[file ('output.txt', false, 'UTF-8')]\n" +
					"content\n" +
					"[/file]\n" +
					"[/template]\n");
			Template t = (Template) m.getOwnedModuleElement().get(0);
			FileBlock fileBlock = findFirst(t, FileBlock.class);
			assertNotNull(fileBlock, "FileBlock should exist in template body");
			assertNotNull(fileBlock.getFileUrl());
		}

		@Test
		@DisplayName("file block with append=true sets APPEND mode (MOFM2T §7.3)")
		void fileBlockAppendTrue() throws M2tParseException {
			Module m = buildModule(
					"[module m(UML)/]\n" +
					"[template public gen(c : EObject)]\n" +
					"[file ('log.log', true)]\n" +
					"content\n" +
					"[/file]\n" +
					"[/template]\n");
			Template t = (Template) m.getOwnedModuleElement().get(0);
			FileBlock fileBlock = findFirst(t, FileBlock.class);
			assertNotNull(fileBlock);
			assertEquals(OpenModeKind.APPEND, fileBlock.getOpenMode(),
					"append=true must set openMode to APPEND");
		}

		@Test
		@DisplayName("file block with append=false sets OVERWRITE mode (MOFM2T §7.3)")
		void fileBlockAppendFalse() throws M2tParseException {
			Module m = buildModule(
					"[module m(UML)/]\n" +
					"[template public gen(c : EObject)]\n" +
					"[file ('output.txt', false)]\n" +
					"content\n" +
					"[/file]\n" +
					"[/template]\n");
			Template t = (Template) m.getOwnedModuleElement().get(0);
			FileBlock fileBlock = findFirst(t, FileBlock.class);
			assertNotNull(fileBlock);
			assertEquals(OpenModeKind.OVERWRITE, fileBlock.getOpenMode(),
					"append=false must set openMode to OVERWRITE");
		}

		@Test
		@DisplayName("file block third param is uniqId, not charset (MOFM2T §8.1.17)")
		void fileBlockUniqId() throws M2tParseException {
			Module m = buildModule(
					"[module m(UML)/]\n" +
					"[template public gen(c : EObject)]\n" +
					"[file ('output.txt', false, c.toString())]\n" +
					"content\n" +
					"[/file]\n" +
					"[/template]\n");
			Template t = (Template) m.getOwnedModuleElement().get(0);
			FileBlock fileBlock = findFirst(t, FileBlock.class);
			assertNotNull(fileBlock);
			assertNotNull(fileBlock.getUniqId(),
					"third param must be uniqId (MOFM2T §8.1.17)");
		}
	}

	// ==================== ProtectedAreaBlock ====================

	@Nested
	@DisplayName("ProtectedAreaBlock")
	class ProtectedAreaBlockTests {

		@Test
		@DisplayName("protected area block")
		void protectedArea() throws M2tParseException {
			Module m = buildModule(
					"[module m(UML)/]\n" +
					"[template public gen(c : EObject)]\n" +
					"[protected ('my-id')]\n" +
					"default content\n" +
					"[/protected]\n" +
					"[/template]\n");
			Template t = (Template) m.getOwnedModuleElement().get(0);
			ProtectedAreaBlock pab = findFirst(t, ProtectedAreaBlock.class);
			assertNotNull(pab, "ProtectedAreaBlock should exist in template body");
			assertNotNull(pab.getMarker());
		}
	}

	// ==================== Helpers ====================

	@SuppressWarnings("unchecked")
	private <T> T findFirst(Template template, Class<T> type) {
		for (TemplateExpression expr : template.getBody()) {
			if (type.isInstance(expr)) {
				return (T) expr;
			}
		}
		return null;
	}
}
