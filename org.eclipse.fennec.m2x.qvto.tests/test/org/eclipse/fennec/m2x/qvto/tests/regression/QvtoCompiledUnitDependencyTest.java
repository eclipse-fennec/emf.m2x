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
package org.eclipse.fennec.m2x.qvto.tests.regression;

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
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
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
import org.eclipse.fennec.m2x.model.qvtoperational.BlackboxOperationDescriptor;
import org.eclipse.fennec.m2x.model.qvtoperational.Module;
import org.eclipse.fennec.m2x.model.qvtoperational.ModuleImport;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalFactory;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoBlackboxRegistry;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxInvocationContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxLibrary;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.qvto.parser.QvtoParserSupport;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.eclipse.fennec.m2x.unit.satellite.SatelliteCollector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Compile binds the imports of a QVT-O unit the way the dependency mode says (#139, concept
 * §3.1/§4).
 *
 * <p>Three modes, three shapes of the same compile: under {@code embed} the library travels
 * inside the document and the unit runs on an engine that knows no resolver; under {@code pin}
 * the manifest names the library with its unit fingerprint and the import stays a stub; under
 * {@code rebind} the manifest names it and nothing more. In every mode an import nobody serves
 * fails the compile, and a blackbox becomes a requirement rather than a dependency: the runtime
 * has to bring the implementation, the unit only says which one.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoCompiledUnitDependencyTest {

	private static final String HELPER_LIB = """
			library HelperLib {
			    helper greet() : String {
			        return 'hello';
			    }
			}
			""";
	private static final String HELPER_LIB_V2 = """
			library HelperLib {
			    helper greet() : String {
			        return 'hello again';
			    }
			}
			""";
	private static final String MAIN = """
			modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
			import HelperLib;
			transformation Main(inout m : ECORE) {
			    main() {
			        m.objectsOfType(EPackage)->forEach(p) {
			            p.name := greet();
			        };
			    }
			}
			""";
	private static final String MAIN_BLACKBOX = """
			modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
			import mylib;
			transformation Main(inout m : ECORE) {
			    main() {
			        m.objectsOfType(EPackage)->forEach(p) {
			            p.name := trimAll('  boxed  ');
			        };
			    }
			}
			""";
	private static final String MAIN_UNKNOWN = """
			import nobody.Serves;
			transformation Main() {
			    main() { log('x'); }
			}
			""";

	/** Engine with a resolver for HelperLib and the blackbox library. */
	private static QvtoEngine engine;
	/** Engine that knows no resolver and no blackbox: what an embedded unit has to run on. */
	private static QvtoEngine bare;

	@BeforeAll
	static void setUp() {
		engine = engineWith(Map.of("HelperLib", HELPER_LIB));
		bare = QvtoEngines.create(QvtoConfiguration.builder(oclConfig()).build());
	}

	private static OclConfiguration oclConfig() {
		return OclConfiguration.builder(new OclParserSupport()).build();
	}

	private static QvtoEngine engineWith(Map<String, String> libraries) {
		QvtoUnitResolver resolver = name -> Optional.ofNullable(libraries.get(name))
				.map(source -> new QvtoUnit.SourceUnit(name, URI.createURI("inline:" + name), source));
		BasicQvtoBlackboxRegistry registry = new BasicQvtoBlackboxRegistry();
		registry.register(new TrimLibrary());
		return QvtoEngines.create(QvtoConfiguration.builder(oclConfig())
				.addUnitResolver(resolver).unitResolverEnabled(true)
				.blackboxRegistry(registry).blackboxEnabled(true)
				.build());
	}

	// ==== embed ====

	@Test
	void embed_carriesTheDependencyInsideTheDocument() throws Exception {
		CompiledUnit compiled = engine.compile(MAIN, "Main", UnitCompileOptions.of(DependencyMode.EMBED));

		assertEquals(DependencyMode.EMBED, compiled.getManifest().getDependencyMode());
		assertEquals(1, compiled.getEmbedded().size(), "the library is an embedded unit");
		CompiledUnit library = compiled.getEmbedded().get(0);
		assertEquals("HelperLib", library.getManifest().getQualifiedName());
		assertNotNull(library.getManifest().getUnitFingerprint());

		DependencyEntry entry = singleDependency(compiled);
		assertEquals("HelperLib", entry.getQualifiedName());
		assertEquals(DependencyMode.EMBED, entry.getMode());
		assertEquals(library.getManifest().getUnitFingerprint(), entry.getFingerprint(),
				"the entry names the very fingerprint the embedded unit was sealed with");

		Module imported = importOf(compiled).getImportedModule();
		assertNull(imported.getEAnnotation(QvtoParserSupport.LINKER_STUB_ANNOTATION),
				"the import is bound, not a stub");
		assertTrue(EcoreUtil.isAncestor(compiled, imported),
				"and bound to the module inside the document, so the reference stays in-tree");
		assertEquals(List.of(), SatelliteCollector.find(compiled));
	}

	@Test
	void embed_runsWithoutAnyResolver_alsoAfterAReload() throws Exception {
		CompiledUnit compiled = engine.compile(MAIN, "Main", UnitCompileOptions.of(DependencyMode.EMBED));
		CompiledUnit reloaded = reload(compiled);

		assertEquals("hello", runOn(bare, (OperationalTransformation) compiled.getUnit()));
		assertEquals("hello", runOn(bare, (OperationalTransformation) reloaded.getUnit()));
	}

	// ==== pin ====

	@Test
	void pin_recordsTheFingerprintAndKeepsTheStub() throws Exception {
		CompiledUnit compiled = engine.compile(MAIN, "Main", UnitCompileOptions.of(DependencyMode.PIN));
		CompiledUnit libraryAlone = engine.compile(HELPER_LIB, "HelperLib");

		assertEquals(List.of(), compiled.getEmbedded(), "a pin carries nothing of the library");
		DependencyEntry entry = singleDependency(compiled);
		assertEquals(DependencyMode.PIN, entry.getMode());
		assertEquals(libraryAlone.getManifest().getUnitFingerprint(), entry.getFingerprint(),
				"the pinned fingerprint is the library's own unit fingerprint");
		assertNotNull(importOf(compiled).getImportedModule()
				.getEAnnotation(QvtoParserSupport.LINKER_STUB_ANNOTATION), "the import stays a stub");
		assertEquals(List.of(), SatelliteCollector.find(compiled), "the stub is a satellite, the document saves");
	}

	@Test
	void pin_isBoundWhenExecuted_onAnEngineWithTheResolver() throws Exception {
		CompiledUnit compiled = engine.compile(MAIN, "Main", UnitCompileOptions.of(DependencyMode.PIN));
		CompiledUnit reloaded = reload(compiled);

		assertEquals("hello", runOn(engine, (OperationalTransformation) reloaded.getUnit()));
		QvtoExecutionResult withoutResolver = execute(bare, (OperationalTransformation) reload(compiled).getUnit());
		assertFalse(withoutResolver.isSuccess(), "without a resolver the pinned import cannot be bound");
	}

	@Test
	void pin_isTheDefault() throws Exception {
		CompiledUnit compiled = engine.compile(MAIN, "Main");
		assertEquals(DependencyMode.PIN, compiled.getManifest().getDependencyMode());
		assertEquals(DependencyMode.PIN, singleDependency(compiled).getMode());
	}

	@Test
	void pin_unitFingerprintFollowsTheDependency() throws Exception {
		QvtoEngine v2 = engineWith(Map.of("HelperLib", HELPER_LIB_V2));
		CompiledUnit withV1 = engine.compile(MAIN, "Main");
		CompiledUnit withV2 = v2.compile(MAIN, "Main");

		assertNotEquals(withV1.getManifest().getUnitFingerprint(), withV2.getManifest().getUnitFingerprint(),
				"same source, different library: the Merkle fold makes the unit fingerprint differ");
		assertNotEquals(singleDependency(withV1).getFingerprint(), singleDependency(withV2).getFingerprint());
	}

	// ==== rebind ====

	@Test
	void rebind_recordsTheNameOnly() throws Exception {
		CompiledUnit compiled = engine.compile(MAIN, "Main", UnitCompileOptions.of(DependencyMode.REBIND));

		DependencyEntry entry = singleDependency(compiled);
		assertEquals("HelperLib", entry.getQualifiedName());
		assertEquals(DependencyMode.REBIND, entry.getMode());
		assertNull(entry.getFingerprint(), "rebind decides at prepare time; nothing to pin");
		assertEquals(List.of(), compiled.getEmbedded());
		assertEquals("hello", runOn(engine, (OperationalTransformation) reload(compiled).getUnit()));
	}

	@Test
	void rebind_unitFingerprintIgnoresTheDependencyVersion() throws Exception {
		QvtoEngine v2 = engineWith(Map.of("HelperLib", HELPER_LIB_V2));
		UnitCompileOptions rebind = UnitCompileOptions.of(DependencyMode.REBIND);
		assertEquals(engine.compile(MAIN, "Main", rebind).getManifest().getUnitFingerprint(),
				v2.compile(MAIN, "Main", rebind).getManifest().getUnitFingerprint(),
				"under rebind the fingerprint describes the unit alone — that is why resolvedClosure exists");
	}

	// ==== every mode ====

	@Test
	void unresolvableImport_failsTheCompile_inEveryMode() {
		for (DependencyMode mode : DependencyMode.values()) {
			QvtoParseException failure = assertThrows(QvtoParseException.class,
					() -> engine.compile(MAIN_UNKNOWN, "Main", UnitCompileOptions.of(mode)), mode.getName());
			assertTrue(failure.getMessage().contains("Cannot resolve import: nobody.Serves"), failure.getMessage());
		}
	}

	// Under embed and pin the compiler follows the dependency and meets the cycle; under
	// rebind it records the name and stops — the closure is prepare's business, and so is
	// the cycle (the execute-time linker reports it today).
	@Test
	void circularImport_isReported_whereTheDependencyIsFollowed() {
		QvtoEngine cyclic = engineWith(Map.of(
				"A", "import B;\nlibrary A { helper a() : String { return b(); } }\n",
				"B", "import A;\nlibrary B { helper b() : String { return a(); } }\n"));
		String main = "import A;\ntransformation Main() { main() { log(a()); } }\n";
		for (DependencyMode mode : List.of(DependencyMode.EMBED, DependencyMode.PIN)) {
			QvtoParseException failure = assertThrows(QvtoParseException.class,
					() -> cyclic.compile(main, "Main", UnitCompileOptions.of(mode)), mode.getName());
			assertTrue(failure.getMessage().contains("Circular import"), failure.getMessage());
		}
		CompiledUnit rebound = assertDoesNotThrow(
				() -> cyclic.compile(main, "Main", UnitCompileOptions.of(DependencyMode.REBIND)));
		assertEquals("A", singleDependency(rebound).getQualifiedName());
	}

	@Test
	void blackbox_isARequirement_notADependency_inEveryMode() throws Exception {
		for (DependencyMode mode : DependencyMode.values()) {
			try {
				blackboxIsARequirement(mode);
			} catch (Exception | AssertionError e) {
				throw new AssertionError("under " + mode.getName() + ": " + e.getMessage(), e);
			}
		}
	}

	private void blackboxIsARequirement(DependencyMode mode) throws Exception {
		CompiledUnit compiled = engine.compile(MAIN_BLACKBOX, "Main", UnitCompileOptions.of(mode));

		assertEquals(List.of(), compiled.getEmbedded(), "an implementation is never embedded");
		assertEquals(List.of(), compiled.getManifest().getDependencyEntry(), "and it is not a unit dependency");
		assertEquals(1, compiled.getManifest().getBlackboxRequirement().size());
		BlackboxRequirement requirement = compiled.getManifest().getBlackboxRequirement().get(0);
		assertEquals("mylib", requirement.getName());
		assertEquals(TrimLibrary.class.getName(), requirement.getProvider());
		assertTrue(requirement.getSignatureFingerprint().startsWith("m2x1:"), requirement.getSignatureFingerprint());
		assertEquals(List.of(), SatelliteCollector.find(compiled));
		assertEquals("boxed", runOn(engine, (OperationalTransformation) reload(compiled).getUnit()));
	}

	@Test
	void blackbox_signatureFingerprint_isStableAcrossCompiles() throws Exception {
		BlackboxRequirement first = engine.compile(MAIN_BLACKBOX, "Main").getManifest().getBlackboxRequirement().get(0);
		BlackboxRequirement second = engine.compile(MAIN_BLACKBOX, "Main").getManifest().getBlackboxRequirement().get(0);
		assertEquals(first.getSignatureFingerprint(), second.getSignatureFingerprint());
	}

	// ==== the resolver's object is never ours ====

	@Test
	void embed_ofABorrowedAst_leavesTheOriginalUntouched() throws Exception {
		OperationalTransformation lent = engine.parse(HELPER_LIB, "HelperLib");
		int satellitesBefore = SatelliteCollector.find(lent).size();
		QvtoEngine lending = QvtoEngines.create(QvtoConfiguration.builder(oclConfig())
				.addUnitResolver(name -> "HelperLib".equals(name)
						? Optional.of(new QvtoUnit.CompiledUnit("HelperLib", lent)) : Optional.empty())
				.unitResolverEnabled(true).build());

		CompiledUnit compiled = lending.compile(MAIN, "Main", UnitCompileOptions.of(DependencyMode.EMBED));

		assertNull(lent.eContainer(), "the lent AST was not moved into the document");
		assertEquals(satellitesBefore, SatelliteCollector.find(lent).size(), "nor were its satellites taken");
		assertFalse(EcoreUtil.isAncestor(compiled, lent));
		assertEquals("hello", runOn(bare, (OperationalTransformation) compiled.getUnit()));
	}

	@Test
	void pin_ofAnAlreadyCompiledDependency_takesItsManifestFingerprint() throws Exception {
		CompiledUnit library = engine.compile(HELPER_LIB, "HelperLib");
		QvtoEngine fromDocument = QvtoEngines.create(QvtoConfiguration.builder(oclConfig())
				.addUnitResolver(name -> "HelperLib".equals(name)
						? Optional.of(new QvtoUnit.CompiledUnit("HelperLib", (OperationalTransformation) library.getUnit()))
						: Optional.empty())
				.unitResolverEnabled(true).build());

		CompiledUnit compiled = fromDocument.compile(MAIN, "Main");
		assertEquals(library.getManifest().getUnitFingerprint(), singleDependency(compiled).getFingerprint());
		assertNotNull(library.getUnit().eContainer(), "the library document is intact");
	}

	@Test
	void detach_copiesTheScriptWithItsSatellites() throws Exception {
		OperationalTransformation original = engine.parse(MAIN, "Main");
		List<EObject> originalSatellites = SatelliteCollector.find(original);
		assertFalse(originalSatellites.isEmpty());

		OperationalTransformation copy = (OperationalTransformation) UnitPackager.detach(original);
		List<EObject> copySatellites = SatelliteCollector.find(copy);
		assertEquals(originalSatellites.size(), copySatellites.size());
		for (EObject satellite : copySatellites) {
			assertFalse(originalSatellites.contains(satellite), "the copy references copies, not the originals");
		}
	}

	// ==== helpers ====

	private static DependencyEntry singleDependency(CompiledUnit compiled) {
		assertEquals(1, compiled.getManifest().getDependencyEntry().size(), "exactly one dependency");
		return compiled.getManifest().getDependencyEntry().get(0);
	}

	private static ModuleImport importOf(CompiledUnit compiled) {
		OperationalTransformation transformation = (OperationalTransformation) compiled.getUnit();
		for (ModuleImport imp : transformation.getModuleImport()) {
			if (imp.getImportedModule() != null && "HelperLib".equals(imp.getImportedModule().getName())) {
				return imp;
			}
		}
		throw new AssertionError("no import of HelperLib");
	}

	private static String runOn(QvtoEngine target, OperationalTransformation transformation) {
		EObject pkg = EcoreUtil.create(EcorePackage.Literals.EPACKAGE);
		pkg.eSet(EcorePackage.Literals.ENAMED_ELEMENT__NAME, "Original");
		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(pkg);
		QvtoExecutionResult result = target.execute(transformation, QvtoExecutionContext.of(extent));
		assertTrue(result.isSuccess(), () -> "execution failed: " + result.diagnostics());
		return (String) pkg.eGet(EcorePackage.Literals.ENAMED_ELEMENT__NAME);
	}

	private static QvtoExecutionResult execute(QvtoEngine target, OperationalTransformation transformation) {
		EObject pkg = EcoreUtil.create(EcorePackage.Literals.EPACKAGE);
		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(pkg);
		return target.execute(transformation, QvtoExecutionContext.of(extent));
	}

	private static CompiledUnit reload(CompiledUnit compiled) throws Exception {
		ResourceSet rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		rs.getPackageRegistry().put(CompiledPackage.eNS_URI, CompiledPackage.eINSTANCE);
		Resource out = rs.createResource(URI.createURI("mem:/unit.xmi"));
		out.getContents().add(compiled);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		out.save(bytes, null);
		ResourceSet fresh = new ResourceSetImpl();
		fresh.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		Resource in = fresh.createResource(URI.createURI("mem:/reloaded.xmi"));
		in.load(new ByteArrayInputStream(bytes.toByteArray()), null);
		EcoreUtil.resolveAll(in);
		return (CompiledUnit) in.getContents().get(0);
	}

	/** A blackbox library with one operation: {@code trimAll(s : String) : String}. */
	public static final class TrimLibrary implements QvtoBlackboxLibrary {
		@Override
		public String getModuleName() {
			return "mylib";
		}

		@Override
		public String getUnitQualifiedName() {
			return "mylib";
		}

		@Override
		public List<String> getUsedPackageURIs() {
			return List.of();
		}

		@Override
		public List<BlackboxOperationDescriptor> getOperationDescriptors() {
			BlackboxOperationDescriptor trimAll = QvtOperationalFactory.eINSTANCE.createBlackboxOperationDescriptor();
			trimAll.setName("trimAll");
			trimAll.setReturnType(EcorePackage.Literals.ESTRING);
			trimAll.getParameterTypes().add(EcorePackage.Literals.ESTRING);
			return List.of(trimAll);
		}

		@Override
		public Object invoke(String operationName, QvtoBlackboxInvocationContext context, Object[] args) {
			return args.length > 0 && args[0] instanceof String s ? s.strip() : null;
		}
	}
}
