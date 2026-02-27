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
package org.eclipse.fennec.m2x.qvto.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.fennec.m2x.model.qvtoperational.ImportKind;
import org.eclipse.fennec.m2x.model.qvtoperational.ModuleImport;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Test;

/**
 * Tests for standalone access declarations (§8.4.7) and moduleref with signature (N2).
 */
class QvtoAccessDeclParseTest extends AbstractQvtoParserTest {

	@Test
	void accessDecl_atUnitLevel_createsModuleImport() throws QvtoParseException {
		OperationalTransformation t = parse("""
				access MyLib;
				transformation T(inout m : ecore::EPackage) {}
				""");
		assertEquals("T", t.getName());
		// inline library creates implicit access + standalone access = need to find the standalone one
		ModuleImport imp = t.getModuleImport().stream()
				.filter(mi -> "MyLib".equals(mi.getImportedModule().getName()))
				.findFirst()
				.orElse(null);
		assertNotNull(imp, "Expected ModuleImport for MyLib");
		assertEquals(ImportKind.ACCESS, imp.getKind());
	}

	@Test
	void accessDecl_insideTransformationBody_createsModuleImport() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T(inout m : ecore::EPackage) {
				    access MyLib;
				    query hello() : String = 'hi';
				}
				""");
		ModuleImport imp = t.getModuleImport().stream()
				.filter(mi -> "MyLib".equals(mi.getImportedModule().getName()))
				.findFirst()
				.orElse(null);
		assertNotNull(imp, "Expected ModuleImport for MyLib");
		assertEquals(ImportKind.ACCESS, imp.getKind());
	}

	@Test
	void accessDecl_withModuleKind_parsesCorrectly() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T(inout m : ecore::EPackage) {
				    access library MyLib;
				}
				""");
		ModuleImport imp = t.getModuleImport().stream()
				.filter(mi -> "MyLib".equals(mi.getImportedModule().getName()))
				.findFirst()
				.orElse(null);
		assertNotNull(imp, "Expected ModuleImport for MyLib");
		assertEquals(ImportKind.ACCESS, imp.getKind());
	}

	@Test
	void accessDecl_multipleModules_createsMultipleImports() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T(inout m : ecore::EPackage) {
				    access Lib1, Lib2;
				}
				""");
		ModuleImport imp1 = t.getModuleImport().stream()
				.filter(mi -> "Lib1".equals(mi.getImportedModule().getName()))
				.findFirst()
				.orElse(null);
		ModuleImport imp2 = t.getModuleImport().stream()
				.filter(mi -> "Lib2".equals(mi.getImportedModule().getName()))
				.findFirst()
				.orElse(null);
		assertNotNull(imp1, "Expected ModuleImport for Lib1");
		assertNotNull(imp2, "Expected ModuleImport for Lib2");
		assertEquals(ImportKind.ACCESS, imp1.getKind());
		assertEquals(ImportKind.ACCESS, imp2.getKind());
	}

	// ==================== N2: moduleref with optional signature ====================

	@Test
	void moduleRef_withSignature_inHeaderAccess_parsesCorrectly() throws QvtoParseException {
		// §8.4.7: <moduleref> ::= <scoped_identifier> <simple_signature>?
		OperationalTransformation t = parse("""
				transformation T(inout m : ecore::EPackage)
				    access MyLib(in x : String) {}
				""");
		ModuleImport imp = t.getModuleImport().stream()
				.filter(mi -> "MyLib".equals(mi.getImportedModule().getName()))
				.findFirst()
				.orElse(null);
		assertNotNull(imp, "Expected ModuleImport for MyLib");
		assertEquals(ImportKind.ACCESS, imp.getKind());
	}

	@Test
	void moduleRef_withSignature_inStandaloneAccess_parsesCorrectly() throws QvtoParseException {
		// §8.4.7: standalone access_decl with parameterized moduleref
		OperationalTransformation t = parse("""
				transformation T(inout m : ecore::EPackage) {
				    access MyLib(in x : String);
				}
				""");
		ModuleImport imp = t.getModuleImport().stream()
				.filter(mi -> "MyLib".equals(mi.getImportedModule().getName()))
				.findFirst()
				.orElse(null);
		assertNotNull(imp, "Expected ModuleImport for MyLib");
		assertEquals(ImportKind.ACCESS, imp.getKind());
	}

	@Test
	void moduleRef_withSignature_inExtends_parsesCorrectly() throws QvtoParseException {
		// §8.4.7: extends with parameterized moduleref
		OperationalTransformation t = parse("""
				transformation T(inout m : ecore::EPackage)
				    extends Base(in x : Integer) {}
				""");
		ModuleImport imp = t.getModuleImport().stream()
				.filter(mi -> "Base".equals(mi.getImportedModule().getName()))
				.findFirst()
				.orElse(null);
		assertNotNull(imp, "Expected ModuleImport for Base");
		assertEquals(ImportKind.EXTENSION, imp.getKind());
	}

	@Test
	void moduleRef_withoutSignature_stillWorks() throws QvtoParseException {
		// Regression: moduleref without signature must still work
		OperationalTransformation t = parse("""
				transformation T(inout m : ecore::EPackage)
				    access MyLib {}
				""");
		ModuleImport imp = t.getModuleImport().stream()
				.filter(mi -> "MyLib".equals(mi.getImportedModule().getName()))
				.findFirst()
				.orElse(null);
		assertNotNull(imp, "Expected ModuleImport for MyLib");
	}
}
