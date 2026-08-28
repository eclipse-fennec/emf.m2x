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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.api.M2tUnit;
import org.eclipse.fennec.m2x.m2t.api.M2tUnitResolver;
import org.eclipse.fennec.m2x.model.compiled.CompiledPackage;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.DependencyEntry;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.eclipse.fennec.m2x.unit.satellite.SatelliteCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Compile binds the {@code extends} and {@code imports} of a MOFM2T module the way the
 * dependency mode says (#139, concept §3.1/§4).
 *
 * <p>Under {@code embed} the library travels inside the document and the module is linked
 * against it, so it generates on an engine that knows no resolver — also after a reload. Under
 * {@code pin} and {@code rebind} the module stays unbound; what it needs to be bound later is
 * kept on it, so an engine that never parsed it can still link it from the resolvers it has.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class M2tCompiledUnitDependencyTest {

	private static final String LIBRARY = """
			[module base(_'http://www.eclipse.org/emf/2002/Ecore')/]
			[template public greet(c : EClass)]hello from the library[/template]
			""";
	private static final String LIBRARY_V2 = """
			[module base(_'http://www.eclipse.org/emf/2002/Ecore')/]
			[template public greet(c : EClass)]hello again from the library[/template]
			""";
	private static final String EXTENDING = """
			[module main(_'http://www.eclipse.org/emf/2002/Ecore') extends base/]
			[template public main(c : EClass)]
			[file ('out.txt', false)][greet(c)/][/file]
			[/template]
			""";
	private static final String IMPORTING = """
			[module main(_'http://www.eclipse.org/emf/2002/Ecore')/]
			[import base/]
			[template public main(c : EClass)]
			[file ('out.txt', false)][greet(c)/][/file]
			[/template]
			""";

	private EClass input;
	/** Engine with the library resolver. */
	private M2tEngine engine;
	/** Engine that knows no resolver: what an embedded unit has to run on. */
	private M2tEngine bare;

	@BeforeEach
	void setUp() {
		input = EcoreFactory.eINSTANCE.createEClass();
		input.setName("Book");
		engine = engine(builder -> builder.addUnitResolver(library(LIBRARY)).unitResolverEnabled(true));
		bare = engine(builder -> builder);
	}

	// ==== embed ====

	@Test
	void embed_carriesTheDependencyAndLinksAgainstIt() throws Exception {
		CompiledUnit compiled = engine.compile(EXTENDING, "main", UnitCompileOptions.of(DependencyMode.EMBED));
		Module module = (Module) compiled.getUnit();

		assertEquals(1, compiled.getEmbedded().size(), "the library is an embedded unit");
		CompiledUnit library = compiled.getEmbedded().get(0);
		assertEquals("base", library.getManifest().getQualifiedName());
		assertEquals(1, module.getExtends().size(), "extends is bound");
		assertTrue(EcoreUtil.isAncestor(compiled, module.getExtends().get(0)),
				"and bound to the module inside the document, so the reference stays in-tree");
		DependencyEntry entry = singleDependency(compiled);
		assertEquals("base", entry.getQualifiedName());
		assertEquals(DependencyMode.EMBED, entry.getMode());
		assertEquals(library.getManifest().getUnitFingerprint(), entry.getFingerprint());
		assertEquals(List.of(), SatelliteCollector.find(compiled));
		assertEquals(List.of(), module.getEAnnotations(), "a bound module carries no link information");
	}

	@Test
	void embed_generatesWithoutAnyResolver_alsoAfterAReload() throws Exception {
		CompiledUnit compiled = engine.compile(EXTENDING, "main", UnitCompileOptions.of(DependencyMode.EMBED));
		assertEquals("hello from the library", generate(bare, (Module) compiled.getUnit()));
		assertEquals("hello from the library", generate(bare, (Module) reload(compiled).getUnit()));
	}

	@Test
	void embed_worksForImportsToo() throws Exception {
		CompiledUnit compiled = engine.compile(IMPORTING, "main", UnitCompileOptions.of(DependencyMode.EMBED));
		Module module = (Module) compiled.getUnit();
		assertEquals(1, module.getImports().size(), "import is bound");
		assertTrue(EcoreUtil.isAncestor(compiled, module.getImports().get(0)));
		assertEquals("hello from the library", generate(bare, (Module) reload(compiled).getUnit()));
	}

	// ==== pin ====

	@Test
	void pin_recordsTheFingerprintAndKeepsTheModuleUnbound() throws Exception {
		CompiledUnit compiled = engine.compile(EXTENDING, "main", UnitCompileOptions.of(DependencyMode.PIN));
		Module module = (Module) compiled.getUnit();

		assertEquals(List.of(), compiled.getEmbedded(), "a pin carries nothing of the library");
		assertEquals(List.of(), module.getExtends(), "the module is not linked");
		assertNotNull(module.getEAnnotation("m2t.link.extends"), "what is still to bind is kept on the module");
		DependencyEntry entry = singleDependency(compiled);
		assertEquals(DependencyMode.PIN, entry.getMode());
		assertEquals(engine.compile(LIBRARY, "base").getManifest().getUnitFingerprint(), entry.getFingerprint(),
				"the pinned fingerprint is the library's own unit fingerprint");
		assertEquals(List.of(), SatelliteCollector.find(compiled));
	}

	@Test
	void pin_isBoundByAnEngineThatNeverParsedIt() throws Exception {
		CompiledUnit compiled = engine.compile(EXTENDING, "main", UnitCompileOptions.of(DependencyMode.PIN));
		Module reloaded = (Module) reload(compiled).getUnit();

		M2tEngine fresh = engine(builder -> builder.addUnitResolver(library(LIBRARY)).unitResolverEnabled(true));
		assertEquals("hello from the library", generate(fresh, reloaded),
				"the link information on the module lets a fresh engine bind it from its resolvers");
		M2tResult withoutResolver = bare.execute((Module) reload(compiled).getUnit(), M2tContext.of(input));
		assertFalse(withoutResolver.generatedFiles().containsKey("out.txt"),
				"without a resolver the pinned extends cannot be bound: " + withoutResolver.diagnostics());
	}

	@Test
	void pin_isTheDefault_andFollowsTheDependency() throws Exception {
		M2tEngine v2 = engine(builder -> builder.addUnitResolver(library(LIBRARY_V2)).unitResolverEnabled(true));
		CompiledUnit withV1 = engine.compile(EXTENDING, "main");
		CompiledUnit withV2 = v2.compile(EXTENDING, "main");
		assertEquals(DependencyMode.PIN, withV1.getManifest().getDependencyMode());
		assertNotEquals(withV1.getManifest().getUnitFingerprint(), withV2.getManifest().getUnitFingerprint(),
				"same source, different library: the Merkle fold makes the unit fingerprint differ");
	}

	// ==== rebind ====

	@Test
	void rebind_recordsTheNameOnly() throws Exception {
		CompiledUnit compiled = engine.compile(IMPORTING, "main", UnitCompileOptions.of(DependencyMode.REBIND));
		DependencyEntry entry = singleDependency(compiled);
		assertEquals(DependencyMode.REBIND, entry.getMode());
		assertNull(entry.getFingerprint());
		assertEquals(List.of(), ((Module) compiled.getUnit()).getImports());
		M2tEngine fresh = engine(builder -> builder.addUnitResolver(library(LIBRARY_V2)).unitResolverEnabled(true));
		assertEquals("hello again from the library", generate(fresh, (Module) reload(compiled).getUnit()),
				"rebind takes whatever the runtime serves under the name");
	}

	// ==== every mode ====

	@Test
	void unresolvableDependency_failsTheCompile_inEveryMode() {
		for (DependencyMode mode : DependencyMode.values()) {
			M2tParseException failure = assertThrows(M2tParseException.class,
					() -> bare.compile(EXTENDING, "main", UnitCompileOptions.of(mode)), mode.getName());
			assertTrue(failure.getMessage().contains("Cannot resolve import: base"), failure.getMessage());
		}
	}

	@Test
	void circularDependency_isReported_whereTheDependencyIsFollowed() {
		String a = "[module a(_'http://www.eclipse.org/emf/2002/Ecore') extends b/]\n";
		String b = "[module b(_'http://www.eclipse.org/emf/2002/Ecore') extends a/]\n";
		M2tEngine cyclic = engine(builder -> builder.addUnitResolver(name -> switch (name) {
			case "a" -> Optional.of(new M2tUnit.SourceUnit("a", URI.createURI("mem:/a.mtl"), a));
			case "b" -> Optional.of(new M2tUnit.SourceUnit("b", URI.createURI("mem:/b.mtl"), b));
			default -> Optional.empty();
		}).unitResolverEnabled(true));
		for (DependencyMode mode : List.of(DependencyMode.EMBED, DependencyMode.PIN)) {
			M2tParseException failure = assertThrows(M2tParseException.class,
					() -> cyclic.compile(a, "a", UnitCompileOptions.of(mode)), mode.getName());
			assertTrue(failure.getMessage().contains("Circular import"), failure.getMessage());
		}
		assertDoesNotThrow(() -> cyclic.compile(a, "a", UnitCompileOptions.of(DependencyMode.REBIND)));
	}

	@Test
	void ecoreAlone_needsNoPackageEntry() throws Exception {
		CompiledUnit compiled = bare.compile(LIBRARY, "base");
		assertEquals(List.of(), compiled.getManifest().getPackageEntry(), "Ecore is in every runtime by definition");
		assertEquals(List.of(), compiled.getPackages());
		assertEquals(List.of(), compiled.getManifest().getBlackboxRequirement(), "MOFM2T has no blackboxes");
	}

	// ==== helpers ====

	private static M2tUnitResolver library(String source) {
		return name -> "base".equals(name)
				? Optional.of(new M2tUnit.SourceUnit(name, URI.createURI("mem:/" + name + ".mtl"), source))
				: Optional.empty();
	}

	private static M2tEngine engine(UnaryOperator<M2tConfiguration.Builder> customizer) {
		return M2tEngines.create(customizer.apply(M2tConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build())).build());
	}

	private static DependencyEntry singleDependency(CompiledUnit compiled) {
		assertEquals(1, compiled.getManifest().getDependencyEntry().size(), "exactly one dependency");
		return compiled.getManifest().getDependencyEntry().get(0);
	}

	private String generate(M2tEngine target, Module module) {
		M2tResult result = target.execute(module, M2tContext.of(input));
		assertTrue(result.generatedFiles().containsKey("out.txt"),
				() -> "no file generated: " + result.diagnostics() + " files=" + result.generatedFiles().keySet());
		return result.generatedFiles().get("out.txt").strip();
	}

	private static CompiledUnit reload(CompiledUnit compiled) throws Exception {
		ResourceSet out = new ResourceSetImpl();
		out.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		out.getPackageRegistry().put(CompiledPackage.eNS_URI, CompiledPackage.eINSTANCE);
		Resource resource = out.createResource(URI.createURI("mem:/unit.xmi"));
		resource.getContents().add(compiled);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		resource.save(bytes, null);
		ResourceSet in = new ResourceSetImpl();
		in.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		Resource loaded = in.createResource(URI.createURI("mem:/reloaded.xmi"));
		loaded.load(new ByteArrayInputStream(bytes.toByteArray()), null);
		EcoreUtil.resolveAll(in);
		return (CompiledUnit) loaded.getContents().get(0);
	}
}
