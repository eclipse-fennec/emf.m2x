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
}
