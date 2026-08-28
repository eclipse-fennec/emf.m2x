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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement;
import org.eclipse.fennec.m2x.model.compiled.CompiledPackage;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.DependencyEntry;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.compiled.PackageEntry;
import org.eclipse.fennec.m2x.model.compiled.PackageRole;
import org.eclipse.fennec.m2x.model.qvtbase.Rule;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnit;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnitResolver;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;
import org.eclipse.fennec.m2x.qvtd.parser.QvtrParserSupport;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.eclipse.fennec.m2x.unit.satellite.SatelliteCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Compile binds the imports of a QVT-R unit the way the dependency mode says (#139, concept
 * §3.1/§4).
 *
 * <p>QVT-R binds an import by merging the imported relations into the importer (§7.11.1.1).
 * Under {@code embed} that merge is done at compile time and the import struck, so the unit
 * runs on an engine that knows no resolver; the merged rules are the embedding — no embedded
 * unit is attached. Under {@code pin} and {@code rebind} nothing is merged: the manifest names
 * the library (with its fingerprint under pin) and the import stays declared for the runtime.
 * A blackbox query becomes a requirement, the metamodel a package entry — with a copy, because
 * {@code bookshelf} here is a dynamic package no runtime can supply from generated code.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtdCompiledUnitDependencyTest {

	private static final String NS_URI = "http://example.org/m2x/qvtr-compiled/1.0";
	private static final String LIBRARY = """
			transformation shared(source : bookshelf, target : bookshelf) {
			    top relation CopyTitle {
			        t : String;
			        checkonly domain source b1 : Book { title = t };
			        enforce domain target b2 : Book { title = t };
			    }
			}
			""";
	private static final String LIBRARY_V2 = """
			transformation shared(source : bookshelf, target : bookshelf) {
			    top relation CopyTitle {
			        t : String;
			        checkonly domain source b1 : Book { title = t };
			        enforce domain target b2 : Book { title = t + '!' };
			    }
			}
			""";
	private static final String IMPORTER = """
			import shared.Library;
			transformation importer(source : bookshelf, target : bookshelf) {
			}
			""";
	private static final String WITH_BLACKBOX = """
			transformation boxed(source : bookshelf, target : bookshelf) {
			    query Shout(s : String) : String;
			    top relation CopyTitle {
			        t : String;
			        checkonly domain source b1 : Book { title = t };
			        enforce domain target b2 : Book { title = Shout(t) };
			    }
			}
			""";

	private EPackage bookshelf;
	private EClass bookClass;
	private EPackage.Registry registry;
	/** Engine with the library resolver. */
	private QvtdEngine engine;
	/** Engine that knows no resolver: what an embedded unit has to run on. */
	private QvtdEngine bare;

	@BeforeEach
	void setUp() {
		bookshelf = EcoreFactory.eINSTANCE.createEPackage();
		bookshelf.setName("bookshelf");
		bookshelf.setNsURI(NS_URI);
		bookshelf.setNsPrefix("bookshelf");
		bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(title);
		bookshelf.getEClassifiers().add(bookClass);
		registry = new EPackageRegistryImpl();
		registry.put(NS_URI, bookshelf);
		engine = engine(builder -> builder.addUnitResolver(library(LIBRARY)).unitResolverEnabled(true));
		bare = engine(builder -> builder);
	}

	// ==== embed ====

	@Test
	void embed_mergesTheRulesAndStrikesTheImport() throws Exception {
		CompiledUnit compiled = engine.compile(IMPORTER, "importer", UnitCompileOptions.of(DependencyMode.EMBED));
		RelationalTransformation transformation = (RelationalTransformation) compiled.getUnit();

		assertEquals(List.of("CopyTitle"), transformation.getRule().stream().map(Rule::getName).toList(),
				"the imported relation is merged in at compile time");
		assertNull(transformation.getEAnnotation(QvtrParserSupport.IMPORTS_ANNOTATION),
				"the import is bound and struck — nothing asks for it again");
		assertEquals(List.of(), compiled.getEmbedded(),
				"the merged rules are the embedding; an embedded unit would be dead weight");
		DependencyEntry entry = singleDependency(compiled);
		assertEquals("shared.Library", entry.getQualifiedName());
		assertEquals(DependencyMode.EMBED, entry.getMode());
		assertEquals(engine.compile(LIBRARY, "shared.Library").getManifest().getUnitFingerprint(),
				entry.getFingerprint(), "the entry names what was merged");
		assertEquals(List.of(), SatelliteCollector.find(compiled));
	}

	@Test
	void embed_runsWithoutAnyResolver_alsoAfterAReload() throws Exception {
		CompiledUnit compiled = engine.compile(IMPORTER, "importer", UnitCompileOptions.of(DependencyMode.EMBED));
		assertEquals("Moby Dick", runOn(bare, (RelationalTransformation) compiled.getUnit()));
		assertEquals("Moby Dick", runOn(bare, (RelationalTransformation) reload(compiled).getUnit()));
	}

	// ==== pin ====

	@Test
	void pin_recordsTheFingerprintAndLeavesTheImportDeclared() throws Exception {
		CompiledUnit compiled = engine.compile(IMPORTER, "importer", UnitCompileOptions.of(DependencyMode.PIN));
		RelationalTransformation transformation = (RelationalTransformation) compiled.getUnit();

		assertEquals(List.of(), transformation.getRule(), "nothing is merged under pin");
		assertNotNull(transformation.getEAnnotation(QvtrParserSupport.IMPORTS_ANNOTATION), "the import stays declared");
		DependencyEntry entry = singleDependency(compiled);
		assertEquals(DependencyMode.PIN, entry.getMode());
		assertEquals(engine.compile(LIBRARY, "shared.Library").getManifest().getUnitFingerprint(), entry.getFingerprint());
		assertEquals("Moby Dick", runOn(engine, (RelationalTransformation) reload(compiled).getUnit()),
				"executed on an engine with the resolver, the import is bound as before");
		assertFalse(execute(bare, (RelationalTransformation) reload(compiled).getUnit()).isSuccess(),
				"without a resolver the pinned import cannot be bound");
	}

	@Test
	void pin_isTheDefault_andFollowsTheDependency() throws Exception {
		QvtdEngine v2 = engine(builder -> builder.addUnitResolver(library(LIBRARY_V2)).unitResolverEnabled(true));
		CompiledUnit withV1 = engine.compile(IMPORTER, "importer");
		CompiledUnit withV2 = v2.compile(IMPORTER, "importer");

		assertEquals(DependencyMode.PIN, withV1.getManifest().getDependencyMode());
		assertNotEquals(withV1.getManifest().getUnitFingerprint(), withV2.getManifest().getUnitFingerprint(),
				"same source, different library: the Merkle fold makes the unit fingerprint differ");
	}

	// ==== rebind ====

	@Test
	void rebind_recordsTheNameOnly() throws Exception {
		CompiledUnit compiled = engine.compile(IMPORTER, "importer", UnitCompileOptions.of(DependencyMode.REBIND));
		DependencyEntry entry = singleDependency(compiled);
		assertEquals(DependencyMode.REBIND, entry.getMode());
		assertNull(entry.getFingerprint());
		assertEquals(List.of(), ((RelationalTransformation) compiled.getUnit()).getRule());
		assertEquals("Moby Dick", runOn(engine, (RelationalTransformation) reload(compiled).getUnit()));
	}

	// ==== every mode ====

	@Test
	void unresolvableImport_failsTheCompile_inEveryMode() {
		for (DependencyMode mode : DependencyMode.values()) {
			QvtdParseException failure = assertThrows(QvtdParseException.class,
					() -> bare.compile(IMPORTER, "importer", UnitCompileOptions.of(mode)), mode.getName());
			assertTrue(failure.getMessage().contains("Cannot resolve import: shared.Library"), failure.getMessage());
		}
	}

	@Test
	void circularImport_isReported_whereTheDependencyIsFollowed() {
		String a = "import B;\ntransformation A(source : bookshelf, target : bookshelf) { }\n";
		String b = "import A;\ntransformation B(source : bookshelf, target : bookshelf) { }\n";
		QvtdEngine cyclic = engine(builder -> builder.addUnitResolver(name -> switch (name) {
			case "A" -> Optional.of(new QvtdUnit.SourceUnit("A", URI.createURI("mem:/A.qvtr"), a));
			case "B" -> Optional.of(new QvtdUnit.SourceUnit("B", URI.createURI("mem:/B.qvtr"), b));
			default -> Optional.empty();
		}).unitResolverEnabled(true));
		for (DependencyMode mode : List.of(DependencyMode.EMBED, DependencyMode.PIN)) {
			QvtdParseException failure = assertThrows(QvtdParseException.class,
					() -> cyclic.compile(a, "A", UnitCompileOptions.of(mode)), mode.getName());
			assertTrue(failure.getMessage().contains("Circular import"), failure.getMessage());
		}
		assertDoesNotThrow(() -> cyclic.compile(a, "A", UnitCompileOptions.of(DependencyMode.REBIND)));
	}

	// ==== blackbox ====

	@Test
	void blackboxQuery_isARequirement() throws Exception {
		CompiledUnit compiled = bare.compile(WITH_BLACKBOX, "boxed");

		assertEquals(List.of(), compiled.getManifest().getDependencyEntry(), "a blackbox is not a unit dependency");
		assertEquals(1, compiled.getManifest().getBlackboxRequirement().size());
		BlackboxRequirement requirement = compiled.getManifest().getBlackboxRequirement().get(0);
		assertEquals("Shout", requirement.getName());
		assertTrue(requirement.getSignatureFingerprint().startsWith("m2x1:"), requirement.getSignatureFingerprint());
		assertNull(requirement.getProvider(), "QVT-R addresses blackbox operations by name, not by library");
		assertEquals(requirement.getSignatureFingerprint(),
				bare.compile(WITH_BLACKBOX, "boxed").getManifest().getBlackboxRequirement().get(0).getSignatureFingerprint(),
				"the signature fingerprint is stable");
	}

	// ==== packages ====

	@Test
	void dynamicMetamodel_isRecordedAndCopied() throws Exception {
		CompiledUnit compiled = bare.compile(LIBRARY, "shared");

		PackageEntry entry = compiled.getManifest().getPackageEntry().stream()
				.filter(e -> NS_URI.equals(e.getNsURI())).findFirst()
				.orElseThrow(() -> new AssertionError("no entry for bookshelf: " + compiled.getManifest().getPackageEntry()));
		assertEquals(PackageRole.EMBEDDED, entry.getRole(), "bookshelf has no generated code");
		assertEquals("fp1", entry.getScheme(), "package fingerprints come from emf.osgi");
		assertTrue(entry.getFingerprint().startsWith("fp1:"), entry.getFingerprint());
		assertEquals(1, compiled.getPackages().size());
		EPackage copy = compiled.getPackages().get(0);
		assertEquals(NS_URI, copy.getNsURI());
		assertNotSame(bookshelf, copy, "a copy, the original stays where it is");
		assertTrue(EcoreUtil.isAncestor(compiled, copy));
		assertEquals(List.of(), SatelliteCollector.find(compiled), "the copy is self-contained too");
	}

	@Test
	void manifest_carriesFormatAndSourceFingerprint() throws Exception {
		CompiledUnit compiled = bare.compile(LIBRARY, "shared");
		assertEquals(UnitPackager.FORMAT_VERSION, compiled.getManifest().getFormatVersion());
		assertTrue(compiled.getManifest().getProducedBy().startsWith("org.eclipse.fennec.m2x.unit"));
		assertTrue(compiled.getManifest().getSourceFingerprint().startsWith("m2x1:"));
		assertEquals(compiled.getManifest().getSourceFingerprint(),
				bare.compile(LIBRARY.replace("\n", "\r\n"), "shared").getManifest().getSourceFingerprint(),
				"line endings do not change the source fingerprint");
	}

	// ==== helpers ====

	private QvtdUnitResolver library(String source) {
		return name -> "shared.Library".equals(name)
				? Optional.of(new QvtdUnit.SourceUnit(name, URI.createURI("mem:/" + name + ".qvtr"), source))
				: Optional.empty();
	}

	private QvtdEngine engine(UnaryOperator<QvtdConfiguration.Builder> customizer) {
		return QvtdEngines.create(customizer.apply(QvtdConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build())
				.packageRegistry(registry)).build());
	}

	private static DependencyEntry singleDependency(CompiledUnit compiled) {
		assertEquals(1, compiled.getManifest().getDependencyEntry().size(), "exactly one dependency");
		return compiled.getManifest().getDependencyEntry().get(0);
	}

	private QvtdExecutionResult execute(QvtdEngine target, RelationalTransformation transformation) {
		EObject book = EcoreUtil.create(bookClass);
		book.eSet(bookClass.getEStructuralFeature("title"), "Moby Dick");
		return target.execute(transformation, QvtdExecutionContext.enforce("target",
				Map.of("source", QvtdModelExtent.of(book), "target", QvtdModelExtent.of())));
	}

	private String runOn(QvtdEngine target, RelationalTransformation transformation) {
		EObject book = EcoreUtil.create(bookClass);
		book.eSet(bookClass.getEStructuralFeature("title"), "Moby Dick");
		QvtdModelExtent targetExtent = QvtdModelExtent.of();
		QvtdExecutionResult result = target.execute(transformation, QvtdExecutionContext.enforce("target",
				Map.of("source", QvtdModelExtent.of(book), "target", targetExtent)));
		assertTrue(result.isSuccess(), () -> "execution failed: " + result.diagnostics());
		assertEquals(1, targetExtent.getContents().size(), "one Book produced");
		EObject produced = targetExtent.getContents().get(0);
		return (String) produced.eGet(produced.eClass().getEStructuralFeature("title"));
	}

	/** Saves and loads the document in a resource set that knows the (dynamic) metamodel. */
	private CompiledUnit reload(CompiledUnit compiled) throws Exception {
		ResourceSet out = new ResourceSetImpl();
		out.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		out.getPackageRegistry().put(CompiledPackage.eNS_URI, CompiledPackage.eINSTANCE);
		// XMI can only refer to what has a URI: a package built in memory gets the resource
		// EMF gives a generated package in createResource(nsURI) — the store does the same
		if (bookshelf.eResource() == null) {
			out.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
			out.createResource(URI.createURI(NS_URI)).getContents().add(bookshelf);
		}
		Resource resource = out.createResource(URI.createURI("mem:/unit.xmi"));
		resource.getContents().add(compiled);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		resource.save(bytes, null);
		ResourceSet in = new ResourceSetImpl();
		in.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		in.getPackageRegistry().put(NS_URI, bookshelf);
		Resource loaded = in.createResource(URI.createURI("mem:/reloaded.xmi"));
		loaded.load(new ByteArrayInputStream(bytes.toByteArray()), null);
		EcoreUtil.resolveAll(in);
		return (CompiledUnit) loaded.getContents().get(0);
	}
}
