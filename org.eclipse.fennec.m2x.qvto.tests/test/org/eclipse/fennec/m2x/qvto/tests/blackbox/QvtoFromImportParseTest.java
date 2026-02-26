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
package org.eclipse.fennec.m2x.qvto.tests.blackbox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.model.qvtoperational.ImportKind;
import org.eclipse.fennec.m2x.model.qvtoperational.ModuleImport;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.parser.QvtoParserSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Parser-level tests for {@code from ... import} syntax (§8.4).
 * Verifies AST structure: ModuleImport with importedNames populated correctly.
 */
class QvtoFromImportParseTest {

	private static QvtoParserSupport parser;

	@BeforeAll
	static void setUp() {
		parser = new QvtoParserSupport();
	}

	@Test
	void fromImport_singleName_parsesImportedNames() throws QvtoParseException {
		OperationalTransformation t = parse("""
				from mylib import trimAll;
				transformation T() {}
				""");

		ModuleImport mi = findImport(t, "mylib");
		assertNotNull(mi, "Import for 'mylib' not found");
		assertEquals(ImportKind.ACCESS, mi.getKind());
		assertEquals(List.of("trimAll"), mi.getImportedNames());
	}

	@Test
	void fromImport_multipleNames_parsesAllNames() throws QvtoParseException {
		OperationalTransformation t = parse("""
				from mylib import greet, farewell, hello;
				transformation T() {}
				""");

		ModuleImport mi = findImport(t, "mylib");
		assertNotNull(mi);
		assertEquals(List.of("greet", "farewell", "hello"), mi.getImportedNames());
	}

	@Test
	void fromImport_wildcard_emptyImportedNames() throws QvtoParseException {
		OperationalTransformation t = parse("""
				from mylib import *;
				transformation T() {}
				""");

		ModuleImport mi = findImport(t, "mylib");
		assertNotNull(mi);
		assertTrue(mi.getImportedNames().isEmpty(),
				"Wildcard import should have empty importedNames, but was: "
						+ mi.getImportedNames());
	}

	@Test
	void classicImport_emptyImportedNames() throws QvtoParseException {
		OperationalTransformation t = parse("""
				import mylib;
				transformation T() {}
				""");

		ModuleImport mi = findImport(t, "mylib");
		assertNotNull(mi);
		assertTrue(mi.getImportedNames().isEmpty(),
				"Classic import should have empty importedNames");
	}

	@Test
	void fromImport_qualifiedName() throws QvtoParseException {
		OperationalTransformation t = parse("""
				from com.example.mylib import doStuff;
				transformation T() {}
				""");

		ModuleImport mi = findImport(t, "com.example.mylib");
		assertNotNull(mi, "Import for 'com.example.mylib' not found");
		assertEquals(List.of("doStuff"), mi.getImportedNames());
	}

	@Test
	void fromImport_coexistsWithClassicImport() throws QvtoParseException {
		OperationalTransformation t = parse("""
				import libA;
				from libB import op1, op2;
				transformation T() {}
				""");

		assertEquals(2, t.getModuleImport().size());

		ModuleImport miA = findImport(t, "libA");
		assertNotNull(miA);
		assertTrue(miA.getImportedNames().isEmpty());

		ModuleImport miB = findImport(t, "libB");
		assertNotNull(miB);
		assertEquals(List.of("op1", "op2"), miB.getImportedNames());
	}

	@Test
	void fromImport_fromAsIdentifier_inTransformation() throws QvtoParseException {
		// 'from' is a soft keyword — should still work as identifier elsewhere
		OperationalTransformation t = parse("""
				from mylib import op1;
				transformation T() {
				    main() {
				        var from := 42;
				    }
				}
				""");

		assertNotNull(t);
		ModuleImport mi = findImport(t, "mylib");
		assertNotNull(mi);
		assertEquals(List.of("op1"), mi.getImportedNames());
	}

	// ==================== Helper ====================

	private static OperationalTransformation parse(String source) throws QvtoParseException {
		return parser.parse(source, "test", EPackage.Registry.INSTANCE);
	}

	private static ModuleImport findImport(OperationalTransformation t, String moduleName) {
		return t.getModuleImport().stream()
				.filter(mi -> mi.getImportedModule() != null
						&& moduleName.equals(mi.getImportedModule().getName()))
				.findFirst()
				.orElse(null);
	}
}
