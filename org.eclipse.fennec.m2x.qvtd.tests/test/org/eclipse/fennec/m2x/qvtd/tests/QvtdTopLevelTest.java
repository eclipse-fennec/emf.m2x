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
package org.eclipse.fennec.m2x.qvtd.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;
import org.eclipse.fennec.m2x.qvtd.parser.QvtrParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * The top level of a QVT-R unit (§7.1.1, §7.13.1, §7.13.4, §7.13.7, #360): imports of metamodels
 * by URI, with alias, subpackage and wildcard; imports of units; transformation names as path
 * names; and a unit holding several transformations.
 */
class QvtdTopLevelTest extends AbstractQvtdEngineTest {

	private static final String UML_URI = "http://test/simpleuml/1.0";
	private static final String RDBMS_URI = "http://test/simplerdbms/1.0";

	/** A transformation named {@code %s} over the models {@code %s} and {@code %s}. */
	private static String transformation(String name, String uml, String rdbms) {
		return """
				transformation %s(uml : %s, rdbms : %s) {
				    top relation R {
				        checkonly domain uml p : Package {};
				        enforce domain rdbms s : Schema {};
				    }
				}
				""".formatted(name, uml, rdbms);
	}

	@Nested
	@DisplayName("§7.1.1: import of a metamodel")
	class MetamodelImport {

		@Test
		@DisplayName("the spec's example: an alias for a URI, usable in a model declaration")
		void anAliasForAUri_isUsableInAModelDeclaration() throws QvtdParseException {
			RelationalTransformation t = parse("import UML : '" + UML_URI + "';\nimport RDB : '" + RDBMS_URI + "';\n"
					+ transformation("T", "UML", "RDB"));

			assertSame(umlPackage, t.getModelParameter().get(0).getUsedPackage().get(0));
			assertSame(rdbmsPackage, t.getModelParameter().get(1).getUsedPackage().get(0));
		}

		@Test
		@DisplayName("a URI without alias makes the metamodel available by its own name")
		void aUriWithoutAlias_isAvailableByItsName() throws QvtdParseException {
			RelationalTransformation t = parse("import '" + UML_URI + "';\n" + transformation("T", "simpleuml", "simplerdbms"));

			assertSame(umlPackage, t.getModelParameter().get(0).getUsedPackage().get(0));
		}

		@Test
		@DisplayName("a trailing wildcard imports all names below the URI")
		void aWildcard_isAccepted() throws QvtdParseException {
			RelationalTransformation t = parse("import '" + UML_URI + "'::*;\n" + transformation("T", "simpleuml", "simplerdbms"));

			assertSame(umlPackage, t.getModelParameter().get(0).getUsedPackage().get(0));
		}

		@Test
		@DisplayName("an alias and a wildcard together are an error")
		void anAliasWithAWildcard_isAnError() {
			QvtdParseException e = assertThrows(QvtdParseException.class,
					() -> parse("import UML : '" + UML_URI + "'::*;\n" + transformation("T", "simpleuml", "simplerdbms")));

			assertTrue(e.getErrors().stream().anyMatch(d -> d.getMessage().contains("An alias and a wildcard cannot be used together")
					&& d.getLine() == 1), () -> "" + e.getErrors());
		}

		@Test
		@DisplayName("'uri'::Sub names a subpackage")
		void aSubpackage_isSelectedByName() throws QvtdParseException {
			EPackage inner = subpackaged();
			QvtdEngine engine = engineFor(inner.getESuperPackage());

			RelationalTransformation t = engine.parse("""
					import In : 'http://example.org/m2x/outer'::inner;
					transformation T(a : In, b : In) {
					    top relation R {
					        checkonly domain a x : Book {};
					        enforce domain b y : Book {};
					    }
					}
					""", "test");

			assertSame(inner, t.getModelParameter().get(0).getUsedPackage().get(0));
		}

		@Test
		@DisplayName("a subpackage the metamodel does not have is a positioned error")
		void anUnknownSubpackage_isAPositionedError() {
			QvtdEngine engine = engineFor(subpackaged().getESuperPackage());

			QvtdParseException e = assertThrows(QvtdParseException.class, () -> engine.parse("""
					import In : 'http://example.org/m2x/outer'::nope;
					transformation T(a : In, b : In) {
					}
					""", "test"));

			assertTrue(e.getErrors().stream().anyMatch(d -> d.getMessage().contains("has no subpackage 'nope'")
					&& d.getLine() == 1), () -> "" + e.getErrors());
		}

		private EPackage subpackaged() {
			EPackage outer = EcoreFactory.eINSTANCE.createEPackage();
			outer.setName("outer");
			outer.setNsURI("http://example.org/m2x/outer");
			EPackage inner = EcoreFactory.eINSTANCE.createEPackage();
			inner.setName("inner");
			inner.setNsURI("http://example.org/m2x/outer/inner");
			EClass book = EcoreFactory.eINSTANCE.createEClass();
			book.setName("Book");
			inner.getEClassifiers().add(book);
			outer.getESubpackages().add(inner);
			return inner;
		}

		private QvtdEngine engineFor(EPackage root) {
			EPackage.Registry registry = new EPackageRegistryImpl();
			registry.put(root.getNsURI(), root);
			return QvtdEngines.create(QvtdConfiguration.builder(
					OclConfiguration.builder(new OclParserSupport()).build())
					.packageRegistry(registry).build());
		}
	}

	@Nested
	@DisplayName("§7.13.1: import of a unit")
	class UnitImport {

		@Test
		@DisplayName("a quoted URI the registry does not know names a unit document")
		void aDocumentUri_isAUnitImport() throws QvtdParseException {
			RelationalTransformation t = parse("import 'platform:/x.qvtr';\n" + transformation("T", "simpleuml", "simplerdbms"));

			assertEquals(List.of("platform:/x.qvtr"), List.copyOf(
					t.getEAnnotation(QvtrParserSupport.IMPORTS_ANNOTATION).getDetails().values()));
		}

		@Test
		@DisplayName("a document URI with a transformation and a wildcard behind it")
		void aDocumentUriWithPathAndWildcard_isAccepted() throws QvtdParseException {
			RelationalTransformation t = parse("import 'x.qvtr'::T::*;\n" + transformation("T", "simpleuml", "simplerdbms"));

			assertEquals(List.of("x.qvtr"), List.copyOf(
					t.getEAnnotation(QvtrParserSupport.IMPORTS_ANNOTATION).getDetails().values()));
		}

		@Test
		@DisplayName("an unquoted qualified name names a unit, as before")
		void aQualifiedName_isAUnitImport() throws QvtdParseException {
			RelationalTransformation t = parse("import a.b.c;\n" + transformation("T", "simpleuml", "simplerdbms"));

			assertEquals(List.of("a.b.c"), List.copyOf(
					t.getEAnnotation(QvtrParserSupport.IMPORTS_ANNOTATION).getDetails().values()));
		}
	}

	@Nested
	@DisplayName("§7.13.7 topLevelCS: a set of transformations")
	class SeveralTransformations {

		private static final String TWO = transformation("T", "simpleuml", "simplerdbms")
				+ transformation("U", "simpleuml", "simplerdbms");

		@Test
		@DisplayName("parseAll returns every transformation of the unit")
		void parseAll_returnsBoth() throws QvtdParseException {
			assertEquals(List.of("T", "U"), engine.parseAll(TWO, "test").stream()
					.map(RelationalTransformation::getName).toList());
		}

		@Test
		@DisplayName("parse with a name selects that transformation")
		void parseByName_selectsIt() throws QvtdParseException {
			assertEquals("T", engine.parse(TWO, "test", "T").getName());
			assertEquals("U", engine.parse(TWO, "test", "U").getName());
		}

		@Test
		@DisplayName("parse without a name, of two independent transformations, is an error, not a lost one")
		void parseWithoutName_twoIndependent_isAnError() {
			QvtdParseException e = assertThrows(QvtdParseException.class, () -> parse(TWO));

			assertTrue(e.getMessage().contains("holds the transformations [T, U]"), e.getMessage());
		}

		@Test
		@DisplayName("parse without a name returns the transformation no other extends")
		void parseWithoutName_returnsTheExtendingOne() throws QvtdParseException {
			assertEquals("U", parse(transformation("T", "simpleuml", "simplerdbms") + """
					transformation U(uml : simpleuml, rdbms : simplerdbms) extends T {
					}
					""").getName());
		}

		@Test
		@DisplayName("parse with a name no transformation has is an error")
		void parseByUnknownName_isAnError() {
			QvtdParseException e = assertThrows(QvtdParseException.class, () -> engine.parse(TWO, "test", "V"));

			assertTrue(e.getMessage().contains("holds no transformation 'V'"), e.getMessage());
		}

		@Test
		@DisplayName("two transformations of one name are an error")
		void aDuplicateTransformation_isAnError() {
			QvtdParseException e = assertThrows(QvtdParseException.class,
					() -> engine.parseAll(TWO + transformation("T", "simpleuml", "simplerdbms"), "test"));

			assertTrue(e.getErrors().stream().anyMatch(d -> d.getMessage().contains("Duplicate transformation 'T'")
					&& d.getLine() == 13), () -> "" + e.getErrors());
		}
	}

	@Nested
	@DisplayName("§7.13.4: <transformationId> ::= <pathNameCS>")
	class TransformationId {

		@Test
		@DisplayName("a transformation's name is the last segment of its path name")
		void aPathName_namesTheTransformationByItsLastSegment() throws QvtdParseException {
			assertEquals("T", parse(transformation("a::b::T", "simpleuml", "simplerdbms")).getName());
		}

		@Test
		@DisplayName("extends takes a path name too")
		void extendsAPathName_resolvesTheLastSegment() throws QvtdParseException {
			RelationalTransformation u = parse(transformation("a::T", "simpleuml", "simplerdbms") + """
					transformation a::U(uml : simpleuml, rdbms : simplerdbms) extends a::T {
					}
					""");

			assertEquals("U", u.getName());
			assertEquals("T", u.getExtends().getName());
		}
	}
}
