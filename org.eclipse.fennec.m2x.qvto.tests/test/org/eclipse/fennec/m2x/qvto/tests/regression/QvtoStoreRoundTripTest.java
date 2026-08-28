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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.qvto.engine.QvtoStoreUnitResolver;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.api.UnitStoreException;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.eclipse.fennec.m2x.unit.store.UnitStoreBackend;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * compile → store → load → execute yields what executing the fresh unit yields (#139, acceptance).
 *
 * <p>The store is the in-memory one; a {@link QvtoStoreUnitResolver} lets the compiler pin or
 * embed a library that lives in the store, as a compiled unit or as a source.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoStoreRoundTripTest {

	private static final String HELPER_LIB = """
			library HelperLib {
			    helper greet() : String {
			        return 'hello';
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

	private UnitStore store;
	/** Engine that resolves from the store. */
	private QvtoEngine engine;
	/** Engine that knows no resolver. */
	private QvtoEngine bare;

	@BeforeEach
	void setUp() {
		store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		engine = engineOver(store);
		bare = QvtoEngines.create(QvtoConfiguration.builder(oclConfig()).build());
	}

	@Test
	void compiledLibraryInTheStore_isPinnedByItsStoredFingerprint_andTheUnitRoundTrips() throws Exception {
		UnitKey libraryKey = store.store("qvto", new PackagedUnit(bare.compile(HELPER_LIB, "HelperLib")));

		CompiledUnit main = engine.compile(MAIN, "Main");
		assertEquals(libraryKey.fingerprint().orElseThrow(),
				main.getManifest().getDependencyEntry().get(0).getFingerprint(),
				"the pin names the fingerprint the store holds the library under");

		UnitKey mainKey = store.store("qvto", new PackagedUnit(main));
		PackagedUnit loaded = (PackagedUnit) store.load(mainKey).orElseThrow();
		assertEquals(runOn(engine, (OperationalTransformation) main.getUnit()),
				runOn(engine, (OperationalTransformation) loaded.document().getUnit()));
		assertEquals("hello", runOn(engine, (OperationalTransformation) loaded.document().getUnit()));
	}

	@Test
	void embeddedFromTheStore_runsWithoutTheStore() throws Exception {
		store.store("qvto", new PackagedUnit(bare.compile(HELPER_LIB, "HelperLib")));
		CompiledUnit main = engine.compile(MAIN, "Main", UnitCompileOptions.of(DependencyMode.EMBED));
		PackagedUnit loaded = (PackagedUnit) store.load(store.store("qvto", new PackagedUnit(main))).orElseThrow();
		assertEquals("hello", runOn(bare, (OperationalTransformation) loaded.document().getUnit()));
	}

	@Test
	void sourceInTheStore_isResolvedToo() throws Exception {
		store.store("qvto", new QvtoUnit.SourceUnit("HelperLib", URI.createURI("mem:/HelperLib.qvto"), HELPER_LIB));
		CompiledUnit main = engine.compile(MAIN, "Main");
		assertEquals(bare.compile(HELPER_LIB, "HelperLib").getManifest().getUnitFingerprint(),
				main.getManifest().getDependencyEntry().get(0).getFingerprint(),
				"a source is compiled on the way, and pinned by what that yields");
		assertEquals("hello", runOn(engine, (OperationalTransformation) main.getUnit()));
	}

	@Test
	void aCompiledUnit_isPreferredOverASourceOfTheSameName() throws Exception {
		store.store("qvto", new QvtoUnit.SourceUnit("HelperLib", URI.createURI("mem:/HelperLib.qvto"),
				HELPER_LIB.replace("'hello'", "'from source'")));
		store.store("qvto", new PackagedUnit(bare.compile(HELPER_LIB, "HelperLib")));
		assertEquals("hello", runOn(engine, (OperationalTransformation) engine.compile(MAIN, "Main").getUnit()));
	}

	@Test
	void aBrokenStore_isNotNotFound() {
		UnitStoreBackend broken = new UnitStoreBackend() {
			@Override
			public void put(UnitKey key, byte[] content) throws UnitStoreException {
				throw new UnitStoreException("disk on fire");
			}

			@Override
			public Optional<byte[]> get(UnitKey key) throws UnitStoreException {
				throw new UnitStoreException("disk on fire");
			}

			@Override
			public List<UnitKey> list(String language, String qualifiedName, UnitKind kind) throws UnitStoreException {
				throw new UnitStoreException("disk on fire");
			}

			@Override
			public boolean remove(UnitKey key) throws UnitStoreException {
				throw new UnitStoreException("disk on fire");
			}
		};
		QvtoEngine overBroken = engineOver(new DefaultUnitStore(broken));
		// #141: a failing source travels to the caller as the compile's own failure, naming source and cause
		QvtoParseException failure = assertThrows(QvtoParseException.class, () -> overBroken.compile(MAIN, "Main"));
		assertTrue(failure.getMessage().contains("disk on fire"), failure.getMessage());
		assertTrue(failure.getMessage().contains("QvtoStoreUnitResolver"), failure.getMessage());
	}

	// ==== helpers ====

	private static OclConfiguration oclConfig() {
		return OclConfiguration.builder(new OclParserSupport()).build();
	}

	private static QvtoEngine engineOver(UnitStore store) {
		return QvtoEngines.create(QvtoConfiguration.builder(oclConfig())
				.addUnitResolver(new QvtoStoreUnitResolver(store)).unitResolverEnabled(true).build());
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
}
