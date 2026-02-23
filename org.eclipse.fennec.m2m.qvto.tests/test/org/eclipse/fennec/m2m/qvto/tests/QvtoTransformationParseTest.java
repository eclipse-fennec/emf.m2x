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
package org.eclipse.fennec.m2m.qvto.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2m.model.qvtoperational.DirectionKind;
import org.eclipse.fennec.m2m.model.qvtoperational.EntryOperation;
import org.eclipse.fennec.m2m.model.qvtoperational.ImportKind;
import org.eclipse.fennec.m2m.model.qvtoperational.ModelParameter;
import org.eclipse.fennec.m2m.model.qvtoperational.ModelType;
import org.eclipse.fennec.m2m.model.qvtoperational.ModuleImport;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2m.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Test;

/**
 * Tests for transformation declarations, model parameters, modeltype, imports, and qualifiers.
 */
class QvtoTransformationParseTest extends AbstractQvtoParserTest {

	@Test
	void emptyTransformation() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation EmptyT() {}
				""");
		assertEquals("EmptyT", t.getName());
		assertTrue(t.getModelParameter().isEmpty());
	}

	@Test
	void transformationName() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation MyTransform() {}
				""");
		assertEquals("MyTransform", t.getName());
	}

	@Test
	void inParameter() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in src : SRC) {}
				""");
		assertEquals(1, t.getModelParameter().size());
		ModelParameter p = t.getModelParameter().get(0);
		assertEquals("src", p.getName());
		assertEquals(DirectionKind.IN, p.getKind());
	}

	@Test
	void outParameter() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(out tgt : SRC) {}
				""");
		ModelParameter p = t.getModelParameter().get(0);
		assertEquals("tgt", p.getName());
		assertEquals(DirectionKind.OUT, p.getKind());
	}

	@Test
	void inoutParameter() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(inout m : SRC) {}
				""");
		ModelParameter p = t.getModelParameter().get(0);
		assertEquals("m", p.getName());
		assertEquals(DirectionKind.INOUT, p.getKind());
	}

	@Test
	void multipleParameters() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC, out t : SRC) {}
				""");
		assertEquals(2, t.getModelParameter().size());
		assertEquals(DirectionKind.IN, t.getModelParameter().get(0).getKind());
		assertEquals(DirectionKind.OUT, t.getModelParameter().get(1).getKind());
	}

	@Test
	void modeltypeDeclaration() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC) {}
				""");
		assertFalse(t.getUsedModelType().isEmpty());
		ModelType mt = t.getUsedModelType().get(0);
		assertEquals("SRC", mt.getName());
		assertFalse(mt.getMetamodel().isEmpty());
	}

	@Test
	void modeltypeResolvesPackage() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC) {}
				""");
		assertEquals(sourcePackage, t.getUsedModelType().get(0).getMetamodel().get(0));
	}

	@Test
	void importDeclaration() throws QvtoParseException {
		OperationalTransformation t = parse("""
				import some.library;
				transformation T() {}
				""");
		assertFalse(t.getModuleImport().isEmpty());
		ModuleImport mi = t.getModuleImport().get(0);
		assertEquals(ImportKind.ACCESS, mi.getKind());
	}

	@Test
	void abstractQualifier() throws QvtoParseException {
		OperationalTransformation t = parse("""
				abstract transformation T() {}
				""");
		assertNotNull(t);
		assertEquals("T", t.getName());
	}

	@Test
	void blackboxQualifier() throws QvtoParseException {
		OperationalTransformation t = parse("""
				blackbox transformation T() {}
				""");
		assertTrue(t.isIsBlackbox());
	}

	@Test
	void modeltypeWithConformanceKind() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC 'strict' uses 'http://test/source/1.0';
				transformation T(in s : SRC) {}
				""");
		assertFalse(t.getUsedModelType().isEmpty());
		assertEquals("strict", t.getUsedModelType().get(0).getConformanceKind());
	}

	@Test
	void modeltypeWithPackageName() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses source('http://test/source/1.0');
				transformation T(in s : SRC) {}
				""");
		assertFalse(t.getUsedModelType().isEmpty());
		assertFalse(t.getUsedModelType().get(0).getMetamodel().isEmpty());
	}

	@Test
	void transformationWithSemicolon() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {};
				""");
		assertEquals("T", t.getName());
	}

	@Test
	void libraryDef() throws QvtoParseException {
		OperationalTransformation t = parse("""
				library MyLib {}
				""");
		assertNotNull(t);
	}

	// ---- Library Parse Tests (P2-03) ----

	// §8.2.1.2: Library with helpers and queries
	@Test
	void libraryWithOperations() throws QvtoParseException {
		OperationalTransformation t = parse("""
				library StringUtils {
				    helper toUpper(s : String) : String {
				        return s;
				    }
				    query greet(name : String) : String = 'Hello ' + name;
				}
				""");
		assertNotNull(t);
		assertEquals("StringUtils", t.getName());
	}

	// §8.2.1.2: Library with multiple operations
	@Test
	void libraryWithMultipleOperations() throws QvtoParseException {
		OperationalTransformation t = parse("""
				library MathLib {
				    helper add(a : Integer, b : Integer) : Integer {
				        return a + b;
				    }
				    query multiply(a : Integer, b : Integer) : Integer = a * b;
				}
				""");
		assertNotNull(t);
		assertEquals("MathLib", t.getName());
	}

	// §8.2.1.4: Import with access keyword (moduleUsage on transformation)
	@Test
	void transformationWithAccessUsage() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() access MyLib {}
				""");
		assertFalse(t.getModuleImport().isEmpty());
		ModuleImport mi = t.getModuleImport().get(0);
		assertEquals(ImportKind.ACCESS, mi.getKind());
		assertNotNull(mi.getImportedModule());
		assertEquals("MyLib", mi.getImportedModule().getName());
	}

	// §8.2.1.4: Import with extends keyword (moduleUsage on transformation)
	@Test
	void transformationWithExtendsUsage() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() extends BaseLib {}
				""");
		assertFalse(t.getModuleImport().isEmpty());
		ModuleImport mi = t.getModuleImport().get(0);
		assertEquals(ImportKind.EXTENSION, mi.getKind());
		assertNotNull(mi.getImportedModule());
		assertEquals("BaseLib", mi.getImportedModule().getName());
	}

	// §8.2.1.4: import declaration stores qualified name
	@Test
	void importDeclarationStoresName() throws QvtoParseException {
		OperationalTransformation t = parse("""
				import some.library;
				transformation T() {}
				""");
		assertFalse(t.getModuleImport().isEmpty());
		ModuleImport mi = t.getModuleImport().get(0);
		assertEquals(ImportKind.ACCESS, mi.getKind());
		assertNotNull(mi.getImportedModule());
		assertEquals("some.library", mi.getImportedModule().getName());
	}

	// ---- P2-09: ModelType Parse Tests ----

	// §8.2.1.6: ModelType without compliance kind defaults to "effective"
	@Test
	void modeltypeDefaultCompliance() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC) {}
				""");
		ModelType mt = t.getUsedModelType().get(0);
		// Default: conformanceKind is null (= "effective" by spec default)
		assertTrue(mt.getConformanceKind() == null || "effective".equals(mt.getConformanceKind()));
	}

	// §8.2.1.6: ModelType with explicit "effective" compliance kind
	@Test
	void modeltypeEffectiveCompliance() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC 'effective' uses 'http://test/source/1.0';
				transformation T(in s : SRC) {}
				""");
		assertEquals("effective", t.getUsedModelType().get(0).getConformanceKind());
	}

	// §8.2.1.6: ModelType with two packages
	@Test
	void modeltypeMultiplePackages() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype M uses 'http://test/source/1.0', 'http://test/target/1.0';
				transformation T(in s : M) {}
				""");
		ModelType mt = t.getUsedModelType().get(0);
		assertEquals(2, mt.getMetamodel().size());
	}

	// §8.4 p167: ModelType with where clause
	@Test
	void modeltypeWithWhereClause() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0' where { self.objectsOfType(SourceElement)->notEmpty() };
				transformation T(in s : SRC) {}
				""");
		ModelType mt = t.getUsedModelType().get(0);
		assertNotNull(mt);
		assertFalse(mt.getAdditionalCondition().isEmpty());
	}

	// §8.2.1.6 + §8.1.1: Two modeltypes in transformation
	@Test
	void twoModeltypesInTransformation() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT) {}
				""");
		assertEquals(2, t.getUsedModelType().size());
		assertEquals("SRC", t.getUsedModelType().get(0).getName());
		assertEquals("TGT", t.getUsedModelType().get(1).getName());
	}

	// ---- P2-10: Entry Operation (main) Parse Tests ----

	// §8.2.1.11: main() is parsed as EntryOperation
	@Test
	void mainParsedAsEntryOperation() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    main() {
				        log('hello');
				    }
				}
				""");
		EntryOperation entry = t.getEntry();
		assertNotNull(entry, "Entry operation should be set on transformation");
		assertEquals("main", entry.getName());
	}

	// §8.2.1.11: main() body contains ordered expressions
	@Test
	void mainBodyOrderedExpressions() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    main() {
				        log('first');
				        log('second');
				        log('third');
				    }
				}
				""");
		EntryOperation entry = t.getEntry();
		assertNotNull(entry);
		assertNotNull(entry.getBody());
		assertEquals(3, entry.getBody().getContent().size());
	}

	// §8.2.1.11: main() has no parameters
	@Test
	void mainHasNoParameters() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    main() {
				    }
				}
				""");
		EntryOperation entry = t.getEntry();
		assertNotNull(entry);
		assertTrue(entry.getEParameters().isEmpty());
	}

	// §8.1.4: Inline library creates ModuleImport on transformation
	@Test
	void inlineLibraryCreatesImport() throws QvtoParseException {
		OperationalTransformation t = parse("""
				library MyLib {
				    helper greet() : String {
				        return 'hello';
				    }
				}
				transformation T() {
				    main() {
				        log(greet());
				    }
				}
				""");
		assertEquals("T", t.getName());
		assertFalse(t.getModuleImport().isEmpty());
		ModuleImport mi = t.getModuleImport().get(0);
		assertEquals(ImportKind.ACCESS, mi.getKind());
		assertNotNull(mi.getImportedModule());
		assertEquals("MyLib", mi.getImportedModule().getName());
	}

	// ---- P2-13: Configuration Properties Parse Tests ----

	// §8.2.1.1 + §8.4 p94: configuration property declaration → configProperty in AST
	@Test
	void configPropertyDeclaration() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    configuration property maxSize : Integer;
				}
				""");
		assertFalse(t.getConfigProperty().isEmpty());
		assertEquals("maxSize", t.getConfigProperty().get(0).getName());
	}

	// §8.2.1.1: multiple configuration properties of different types
	@Test
	void configPropertyMultipleTypes() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    configuration property name : String;
				    configuration property count : Integer;
				    configuration property flag : Boolean;
				}
				""");
		assertEquals(3, t.getConfigProperty().size());
		assertEquals("name", t.getConfigProperty().get(0).getName());
		assertEquals("count", t.getConfigProperty().get(1).getName());
		assertEquals("flag", t.getConfigProperty().get(2).getName());
	}

	// §8.4 p167: configuration property with default value expression
	@Test
	void configPropertyWithDefaultValue() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    configuration property name : String = 'default';
				}
				""");
		assertFalse(t.getConfigProperty().isEmpty());
		assertEquals("name", t.getConfigProperty().get(0).getName());
	}
}
