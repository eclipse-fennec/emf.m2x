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
package org.eclipse.fennec.m2x.unit.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.fennec.m2x.m2t.api.M2tUnit;
import org.eclipse.fennec.m2x.model.m2t.M2tFactory;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalFactory;
import org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationFactory;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnit;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitKind;
import org.junit.jupiter.api.Test;

/**
 * The three language unit types are {@link Unit}s.
 *
 * <p>This is what #136 buys: a store, a fingerprint mechanism and a prepared
 * context can be written once against {@link Unit} instead of three times
 * against three unrelated sealed interfaces. The dependency points from the
 * language APIs to this bundle and never back, so no cycle arises (D39) — the
 * language APIs are on the test path here, not on the build path.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class UnitContractTest {

	private static final URI SOURCE_URI = URI.createURI("test://unit.qvto");

	@Test
	void qvtoSourceUnit_isANeutralSourceUnit() {
		QvtoUnit.SourceUnit unit = new QvtoUnit.SourceUnit("lib.Strings", SOURCE_URI, "transformation t() {}");
		Unit.Source source = assertInstanceOf(Unit.Source.class, unit);
		assertEquals("lib.Strings", source.qualifiedName());
		assertEquals(SOURCE_URI, source.uri());
		assertEquals("transformation t() {}", source.source());
		assertEquals(UnitKind.SOURCE, source.kind());
	}

	@Test
	void qvtoCompiledUnit_isANeutralCompiledUnit() {
		OperationalTransformation transformation =
				QvtOperationalFactory.eINSTANCE.createOperationalTransformation();
		QvtoUnit.CompiledUnit unit = new QvtoUnit.CompiledUnit("lib.Strings", transformation);
		Unit.Compiled compiled = assertInstanceOf(Unit.Compiled.class, unit);
		assertEquals("lib.Strings", compiled.qualifiedName());
		assertSame(transformation, compiled.root());
		assertEquals(UnitKind.COMPILED, compiled.kind());
	}

	@Test
	void qvtdSourceUnit_isANeutralSourceUnit() {
		QvtdUnit.SourceUnit unit = new QvtdUnit.SourceUnit("uml.ToRdbms", SOURCE_URI, "transformation t() {}");
		Unit.Source source = assertInstanceOf(Unit.Source.class, unit);
		assertEquals("uml.ToRdbms", source.qualifiedName());
		assertEquals(UnitKind.SOURCE, source.kind());
	}

	@Test
	void qvtdCompiledUnit_isANeutralCompiledUnit() {
		RelationalTransformation transformation =
				QvtrelationFactory.eINSTANCE.createRelationalTransformation();
		QvtdUnit.CompiledUnit unit = new QvtdUnit.CompiledUnit("uml.ToRdbms", transformation);
		Unit.Compiled compiled = assertInstanceOf(Unit.Compiled.class, unit);
		assertSame(transformation, compiled.root());
		assertEquals(UnitKind.COMPILED, compiled.kind());
	}

	@Test
	void m2tSourceUnit_isANeutralSourceUnit() {
		M2tUnit.SourceUnit unit = new M2tUnit.SourceUnit("gen.Java", SOURCE_URI, "[module m(Ecore)/]");
		Unit.Source source = assertInstanceOf(Unit.Source.class, unit);
		assertEquals("gen.Java", source.qualifiedName());
		assertEquals(UnitKind.SOURCE, source.kind());
	}

	@Test
	void m2tCompiledUnit_isANeutralCompiledUnit() {
		Module module = M2tFactory.eINSTANCE.createModule();
		M2tUnit.CompiledUnit unit = new M2tUnit.CompiledUnit("gen.Java", module);
		Unit.Compiled compiled = assertInstanceOf(Unit.Compiled.class, unit);
		assertSame(module, compiled.root());
		assertEquals(UnitKind.COMPILED, compiled.kind());
	}

	// All three at once — the point of the exercise: one list, one loop, no
	// instanceof cascade over three languages.
	@Test
	void allThreeLanguages_shareTheBaseType() {
		List<Unit> units = List.of(
				new QvtoUnit.SourceUnit("a", SOURCE_URI, "x"),
				new QvtdUnit.SourceUnit("b", SOURCE_URI, "y"),
				new M2tUnit.SourceUnit("c", SOURCE_URI, "z"),
				new QvtoUnit.CompiledUnit("d",
						QvtOperationalFactory.eINSTANCE.createOperationalTransformation()),
				new QvtdUnit.CompiledUnit("e",
						QvtrelationFactory.eINSTANCE.createRelationalTransformation()),
				new M2tUnit.CompiledUnit("f", M2tFactory.eINSTANCE.createModule()));
		assertEquals(List.of("a", "b", "c", "d", "e", "f"),
				units.stream().map(Unit::qualifiedName).toList());
		assertEquals(List.of(UnitKind.SOURCE, UnitKind.SOURCE, UnitKind.SOURCE,
				UnitKind.COMPILED, UnitKind.COMPILED, UnitKind.COMPILED),
				units.stream().map(Unit::kind).toList());
	}

	// The language records keep their own null checks — extending Unit changes
	// nothing about them.
	@Test
	void nullArguments_stillRejected() {
		assertThrows(NullPointerException.class,
				() -> new QvtoUnit.SourceUnit(null, SOURCE_URI, "x"));
		assertThrows(NullPointerException.class,
				() -> new QvtdUnit.CompiledUnit("a", null));
		assertThrows(NullPointerException.class,
				() -> new M2tUnit.SourceUnit("a", SOURCE_URI, null));
	}
}
