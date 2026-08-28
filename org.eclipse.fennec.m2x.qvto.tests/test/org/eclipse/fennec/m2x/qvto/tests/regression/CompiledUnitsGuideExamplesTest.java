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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.qvto.engine.QvtoStoreUnitResolver;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.junit.jupiter.api.Test;

/**
 * The examples of {@code docs/compiled-units-guide.md}, executed. A guide whose code does not
 * compile is worse than none, and the shapes it shows — compile with a mode, store, prepare,
 * execute, the manifest accessors, resolving from a store — are exactly what breaks silently when
 * an API is renamed.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class CompiledUnitsGuideExamplesTest {

	private static final String LIB_SOURCE = """
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

	/** §3 — compile() beside parse(), and what the document holds. */
	@Test
	void section3_compileBesideParse() throws Exception {
		QvtoEngine engine = engineWithLibrary();

		OperationalTransformation ast = engine.parse(MAIN, "Main");
		CompiledUnit compiled = engine.compile(MAIN, "Main");
		OperationalTransformation script = (OperationalTransformation) compiled.getUnit();

		assertNotNull(ast);
		assertNotNull(compiled.getId());
		CompiledUnitManifest m = compiled.getManifest();
		assertEquals("qvto", m.getLanguage());
		assertEquals("Main", m.getQualifiedName());
		assertTrue(m.getUnitFingerprint().startsWith("m2x1:"));
		assertTrue(m.getSourceFingerprint().startsWith("m2x1:"));
		assertEquals(DependencyMode.PIN, m.getDependencyMode());
		assertEquals("hello", runOn(engine, script));
	}

	/** §4 — embed carries the library; the unit runs where no resolver exists. */
	@Test
	void section4_embedRunsWithoutAResolver() throws Exception {
		CompiledUnit unit = engineWithLibrary()
				.compile(MAIN, "Main", UnitCompileOptions.of(DependencyMode.EMBED));

		assertEquals("HelperLib", unit.getEmbedded().get(0).getManifest().getQualifiedName());
		QvtoEngine bare = QvtoEngines.create(QvtoConfiguration.builder(oclConfig()).build());
		assertEquals("hello", runOn(bare, (OperationalTransformation) unit.getUnit()));
	}

	/**
	 * §5 — store, versions, kinds; §6 — prepare and execute. Note what prepare needs: the pinned
	 * dependency has to be in the store as a <em>compiled</em> unit. A source there serves the
	 * compiler, which parses it; prepare loads documents by key and has nothing to parse with.
	 */
	@Test
	void section5and6_storePrepareExecute() throws Exception {
		QvtoEngine engine = engineWithLibrary();
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());

		UnitKey key = store.store("qvto", new PackagedUnit(engine.compile(MAIN, "Main")));
		PackagedUnit loaded = (PackagedUnit) store.load(key).orElseThrow();
		assertEquals("Main", loaded.qualifiedName());

		// a source and a compiled unit of one name live side by side, told apart by kind
		store.store("qvto", new QvtoUnit.SourceUnit("HelperLib", URI.createURI("mem:/lib.qvto"), LIB_SOURCE));
		store.store("qvto", new PackagedUnit(engine.compile(LIB_SOURCE, "HelperLib")));
		assertEquals(1, store.versions("qvto", "HelperLib", UnitKind.SOURCE).size());
		assertEquals(1, store.versions("qvto", "HelperLib", UnitKind.COMPILED).size());
		assertTrue(store.load(UnitKey.of("qvto", "HelperLib", UnitKind.SOURCE)).isPresent());

		QvtoEngine overStore = QvtoEngines.create(QvtoConfiguration.builder(oclConfig())
				.addUnitResolver(new QvtoStoreUnitResolver(store))
				.unitResolverEnabled(true).build());
		UnitKey pinned = store.store("qvto", new PackagedUnit(overStore.compile(MAIN, "Main")));

		PreparedContext prepared = UnitPreparer.withDefaults(store, overStore.unitBinder()).prepare(pinned);
		EObject pkg = ecorePackage("Original");
		QvtoExecutionResult result = overStore.execute(prepared, "Main",
				QvtoExecutionContext.of(new BasicQvtoModelExtent(List.of(pkg))));
		assertTrue(result.isSuccess(), () -> "execution failed: " + result.diagnostics());
		assertEquals("hello", name(pkg));
	}

	/** §6 — one context, many executions. */
	@Test
	void section6_oneContextManyExecutions() throws Exception {
		QvtoEngine engine = engineWithLibrary();
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		store.store("qvto", new PackagedUnit(engine.compile(LIB_SOURCE, "HelperLib")));
		QvtoEngine overStore = QvtoEngines.create(QvtoConfiguration.builder(oclConfig())
				.addUnitResolver(new QvtoStoreUnitResolver(store)).unitResolverEnabled(true).build());
		UnitKey key = store.store("qvto", new PackagedUnit(overStore.compile(MAIN, "Main")));

		PreparedContext prepared = UnitPreparer.withDefaults(store, overStore.unitBinder()).prepare(key);
		for (int i = 0; i < 3; i++) {
			EObject pkg = ecorePackage("run " + i);
			assertTrue(overStore.execute(prepared, "Main",
					QvtoExecutionContext.of(new BasicQvtoModelExtent(List.of(pkg)))).isSuccess());
			assertEquals("hello", name(pkg));
		}
	}

	/** §6 — under rebind, prepare records what it bound. */
	@Test
	void section6_rebindRecordsTheClosure() throws Exception {
		QvtoEngine engine = engineWithLibrary();
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		store.store("qvto", new PackagedUnit(engine.compile(LIB_SOURCE, "HelperLib")));
		QvtoEngine overStore = QvtoEngines.create(QvtoConfiguration.builder(oclConfig())
				.addUnitResolver(new QvtoStoreUnitResolver(store)).unitResolverEnabled(true).build());
		UnitKey key = store.store("qvto", new PackagedUnit(
				overStore.compile(MAIN, "Main", UnitCompileOptions.of(DependencyMode.REBIND))));

		PreparedContext prepared = UnitPreparer.withDefaults(store, overStore.unitBinder()).prepare(key);
		CompiledUnit ran = ((PackagedUnit) prepared.unit("Main").orElseThrow()).document();
		assertEquals("HelperLib", ran.getManifest().getResolvedClosure().get(0).getQualifiedName());
		assertEquals("store", ran.getManifest().getResolvedClosure().get(0).getSource());
	}

	// ==== helpers ====

	private static OclConfiguration oclConfig() {
		return OclConfiguration.builder(new OclParserSupport()).build();
	}

	private static QvtoEngine engineWithLibrary() {
		QvtoUnitResolver lib = name -> "HelperLib".equals(name)
				? Optional.of(new QvtoUnit.SourceUnit(name, URI.createURI("mem:/HelperLib.qvto"), LIB_SOURCE))
				: Optional.empty();
		return QvtoEngines.create(QvtoConfiguration.builder(oclConfig())
				.addUnitResolver(lib).unitResolverEnabled(true).build());
	}

	private static EObject ecorePackage(String name) {
		EObject pkg = EcoreUtil.create(EcorePackage.Literals.EPACKAGE);
		pkg.eSet(EcorePackage.Literals.ENAMED_ELEMENT__NAME, name);
		return pkg;
	}

	private static String name(EObject pkg) {
		return (String) pkg.eGet(EcorePackage.Literals.ENAMED_ELEMENT__NAME);
	}

	private static String runOn(QvtoEngine engine, OperationalTransformation transformation) {
		EObject pkg = ecorePackage("Original");
		QvtoExecutionResult result = engine.execute(transformation,
				QvtoExecutionContext.of(new BasicQvtoModelExtent(List.of(pkg))));
		assertTrue(result.isSuccess(), () -> "execution failed: " + result.diagnostics());
		return name(pkg);
	}
}
