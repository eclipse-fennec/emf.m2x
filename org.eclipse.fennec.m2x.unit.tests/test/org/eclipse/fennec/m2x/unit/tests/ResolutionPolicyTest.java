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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.m2t.M2tFactory;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.m2t.api.M2tUnit;
import org.eclipse.fennec.m2x.unit.api.UnitResolutionException;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.eclipse.fennec.m2x.unit.resolve.ResolutionPolicy;
import org.eclipse.fennec.m2x.unit.resolve.ResolutionPolicy.Source;
import org.junit.jupiter.api.Test;

/**
 * Every source is asked, a failing source is an error, answers have to agree (#141, concept §7).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class ResolutionPolicyTest {

	private static final String TEXT = "[module base(Ecore)/]\n";
	private static final String OTHER_TEXT = "[module base(Ecore)/]\n[template public t(c : EClass)][/template]\n";

	@Test
	void noSource_hasIt_isEmpty() {
		assertEquals(Optional.empty(), ResolutionPolicy.resolve("base", List.of(none("a"), none("b"))));
	}

	@Test
	void oneAnswer_isReturned() {
		M2tUnit unit = source("base", TEXT);
		assertSame(unit, ResolutionPolicy.resolve("base", List.of(none("a"), has("b", unit), none("c"))).orElseThrow());
	}

	@Test
	void everySource_isAsked_andTheFirstAnswerWins() {
		M2tUnit first = source("base", TEXT);
		M2tUnit second = source("base", TEXT);
		boolean[] asked = new boolean[3];
		List<Source<M2tUnit>> sources = List.of(
				Source.of("a", name -> {
					asked[0] = true;
					return Optional.of(first);
				}),
				Source.of("b", name -> {
					asked[1] = true;
					return Optional.of(second);
				}),
				Source.of("c", name -> {
					asked[2] = true;
					return Optional.empty();
				}));
		assertSame(first, ResolutionPolicy.resolve("base", sources).orElseThrow());
		assertTrue(asked[0] && asked[1] && asked[2], "all three were asked, not only until the first answer");
	}

	@Test
	void aFailingSource_isAnError_namingIt_notNotFound() {
		List<Source<M2tUnit>> sources = List.of(
				Source.of("the store", name -> {
					throw new IllegalStateException("disk on fire");
				}),
				has("a stale copy", source("base", TEXT)));
		UnitResolutionException failure = assertThrows(UnitResolutionException.class,
				() -> ResolutionPolicy.resolve("base", sources));
		assertTrue(failure.getMessage().contains("the store"), failure.getMessage());
		assertTrue(failure.getMessage().contains("disk on fire"), failure.getMessage());
	}

	@Test
	void twoSources_withDifferentText_disagree() {
		UnitResolutionException failure = assertThrows(UnitResolutionException.class,
				() -> ResolutionPolicy.resolve("base", List.of(has("a", source("base", TEXT)), has("b", source("base", OTHER_TEXT)))));
		assertTrue(failure.getMessage().contains("a") && failure.getMessage().contains("b"), failure.getMessage());
		assertTrue(failure.getMessage().contains("source fingerprint"), failure.getMessage());
	}

	@Test
	void twoSources_withTheSameText_agree() {
		assertTrue(ResolutionPolicy.resolve("base", List.of(has("a", source("base", TEXT)), has("b", source("base", TEXT)))).isPresent());
	}

	@Test
	void compiledDocuments_areComparedByUnitFingerprint() {
		CompiledUnit one = compiled("base", 1);
		CompiledUnit same = compiled("base", 1);
		CompiledUnit different = compiled("base", 2);
		assertTrue(ResolutionPolicy.resolve("base", List.of(has("a", ofDocument(one)), has("b", ofDocument(same)))).isPresent());
		UnitResolutionException failure = assertThrows(UnitResolutionException.class,
				() -> ResolutionPolicy.resolve("base", List.of(has("a", ofDocument(one)), has("b", ofDocument(different)))));
		assertTrue(failure.getMessage().contains("unit fingerprint"), failure.getMessage());
	}

	@Test
	void aSourceAndACompiledDocument_areComparedByTheDocumentsSourceFingerprint() {
		CompiledUnit fromText = compiledFrom("base", TEXT);
		assertTrue(ResolutionPolicy.resolve("base", List.of(has("text", source("base", TEXT)), has("doc", ofDocument(fromText)))).isPresent(),
				"the document records the fingerprint of the text it was compiled from");
		UnitResolutionException failure = assertThrows(UnitResolutionException.class,
				() -> ResolutionPolicy.resolve("base", List.of(has("text", source("base", OTHER_TEXT)), has("doc", ofDocument(fromText)))));
		assertTrue(failure.getMessage().contains("source fingerprint"), failure.getMessage());
	}

	@Test
	void aBareAst_againstASource_cannotBeCompared_andTheFirstWins() {
		M2tUnit bare = new M2tUnit.CompiledUnit("base", module("base", 1));
		M2tUnit text = source("base", OTHER_TEXT);
		assertSame(bare, ResolutionPolicy.resolve("base", List.of(has("ast", bare), has("text", text))).orElseThrow());
	}

	// ==== helpers ====

	private static Source<M2tUnit> none(String name) {
		return Source.of(name, n -> Optional.empty());
	}

	private static Source<M2tUnit> has(String name, M2tUnit unit) {
		return Source.of(name, n -> Optional.of(unit));
	}

	private static M2tUnit source(String name, String text) {
		return new M2tUnit.SourceUnit(name, URI.createURI("mem:/" + name + ".mtl"), text);
	}

	private static M2tUnit ofDocument(CompiledUnit document) {
		return new M2tUnit.CompiledUnit(document.getManifest().getQualifiedName(), (Module) document.getUnit());
	}

	private static Module module(String name, int templates) {
		Module module = M2tFactory.eINSTANCE.createModule();
		module.setName(name);
		module.setNsURI("http://example.org/m2x/policy-test/" + name);
		for (int i = 0; i < templates; i++) {
			var template = M2tFactory.eINSTANCE.createTemplate();
			template.setName("t" + i);
			module.getOwnedModuleElement().add(template);
		}
		return module;
	}

	private static CompiledUnit compiled(String name, int templates) {
		return UnitPackager.compile("m2t", name, module(name, templates));
	}

	private static CompiledUnit compiledFrom(String name, String text) {
		UnitPackager packager = UnitPackager.withDefaults();
		return packager.seal(packager.begin("m2t", name, module(name, 1), DependencyMode.PIN, text));
	}
}
