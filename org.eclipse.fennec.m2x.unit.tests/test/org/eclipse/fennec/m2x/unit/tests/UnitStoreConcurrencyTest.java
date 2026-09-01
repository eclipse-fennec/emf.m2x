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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.m2t.M2tFactory;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * One store, several threads (#174).
 *
 * <p>A store is what an engine reaches for every import of every run, so it is shared by
 * construction, and the suite never used one from more than one thread. Loading is the
 * interesting half: each load parses into a resource set, and a store that shared one across
 * threads would hand back half-built documents.
 *
 * <p>A concurrency test cannot prove the absence of a race; it can make a present one show up.
 */
class UnitStoreConcurrencyTest {

	private static final String NS_URI = "http://example.org/m2x/store-concurrency/1.0";
	private static final int THREADS = 8;
	private static final int RUNS_PER_THREAD = 20;

	private EClass bookClass;
	private UnitStore store;

	@BeforeEach
	void setUp() {
		EPackage shelf = EcoreFactory.eINSTANCE.createEPackage();
		shelf.setName("shelf");
		shelf.setNsURI(NS_URI);
		shelf.setNsPrefix("shelf");
		bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(title);
		shelf.getEClassifiers().add(bookClass);
		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(NS_URI, shelf);
		store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
	}

	@Test
	@Timeout(value = 60, unit = TimeUnit.SECONDS)
	@DisplayName("loading one unit from several threads gives every thread the same unit")
	void loadingFromSeveralThreads() throws Exception {
		UnitKey key = store.put(compiled("gen.Books"));

		List<String> results = inParallel(() -> {
			CompiledUnit loaded = ((PackagedUnit) store.get(key).orElseThrow()).document();
			return loaded.getManifest().getQualifiedName() + "/"
					+ loaded.getManifest().getUnitFingerprint() + "/"
					+ ((Module) loaded.getUnit()).getOwnedModuleElement().size();
		});

		assertEquals(THREADS * RUNS_PER_THREAD, results.size());
		assertEquals(1, results.stream().distinct().count(),
				() -> "every thread has to see the same unit: " + results.stream().distinct().toList());
		assertTrue(results.get(0).startsWith("gen.Books/m2x1:"), results.get(0));
	}

	@Test
	@Timeout(value = 60, unit = TimeUnit.SECONDS)
	@DisplayName("storing different units from several threads keeps all of them")
	void storingFromSeveralThreads() throws Exception {
		List<String> names = inParallelIndexed(index -> {
			UnitKey key = store.put(compiled("gen.Unit" + index));
			return key.qualifiedName();
		});

		assertEquals(THREADS * RUNS_PER_THREAD, names.size());
		for (String name : names.stream().distinct().toList() ) {
			assertTrue(store.get(UnitKey.of("m2t", name,
					org.eclipse.fennec.m2x.unit.api.UnitKind.COMPILED)).isPresent(),
					() -> name + " was stored and has to be there");
		}
	}

	// --- helpers ---

	private CompiledUnit compiled(String name) {
		Module module = M2tFactory.eINSTANCE.createModule();
		module.setName(name);
		module.setNsURI("http://example.org/m2x/store-concurrency/module/" + name);
		Template template = M2tFactory.eINSTANCE.createTemplate();
		template.setName("book");
		Variable parameter = OclFactory.eINSTANCE.createVariable();
		parameter.setName("b");
		parameter.setType(bookClass);
		template.getParameter().add(parameter);
		module.getOwnedModuleElement().add(template);
		return UnitPackager.compile("m2t", name, module);
	}

	private static List<String> inParallel(Callable<String> work) throws Exception {
		return run(index -> work.call());
	}

	private static List<String> inParallelIndexed(IndexedWork work) throws Exception {
		return run(work);
	}

	@FunctionalInterface
	private interface IndexedWork {
		String run(int index) throws Exception;
	}

	private static List<String> run(IndexedWork work) throws Exception {
		ExecutorService pool = Executors.newFixedThreadPool(THREADS);
		try {
			List<Callable<String>> jobs = new ArrayList<>();
			for (int i = 0; i < THREADS * RUNS_PER_THREAD; i++) {
				int index = i;
				jobs.add(() -> work.run(index));
			}
			List<String> results = new ArrayList<>();
			for (Future<String> future : pool.invokeAll(jobs)) {
				results.add(future.get(30, TimeUnit.SECONDS));
			}
			return results;
		} finally {
			pool.shutdownNow();
		}
	}
}
