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
package org.eclipse.fennec.m2x.qvto.example;

import java.nio.file.Path;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.qvto.engine.QvtoStoreUnitResolver;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;

/**
 * The compiled way: compile once, store, prepare, execute — after compile no resolver is asked
 * and no parser runs. The helper library is compiled and stored first; the transformation pins
 * it by fingerprint, and prepare loads the closure into one context.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class CompiledUnitExample {

	private CompiledUnitExample() {}

	public static void main(String[] args) throws Exception {
		EObject catalog = run();
		System.out.println("catalog: " + catalog.eGet(catalog.eClass().getEStructuralFeature("title")));
		for (Object entry : (Iterable<?>) catalog.eGet(catalog.eClass().getEStructuralFeature("entries"))) {
			EObject eEntry = (EObject) entry;
			System.out.println("  " + eEntry.eGet(eEntry.eClass().getEStructuralFeature("year"))
					+ "  " + eEntry.eGet(eEntry.eClass().getEStructuralFeature("label")));
		}
	}

	/**
	 * Compiles, stores, prepares and runs the transformation over the example files.
	 *
	 * @return the produced catalog root
	 * @throws Exception if anything on the way refuses
	 */
	public static EObject run() throws Exception {
		Path base = ExampleFiles.baseDir();
		EPackage library = ExampleFiles.loadEcore(base.resolve("model/library.ecore"));
		EPackage catalog = ExampleFiles.loadEcore(base.resolve("model/catalog.ecore"));

		// Build side: the store is dumb (key <-> document); the helper library goes in first,
		// compiled — the transformation's compile resolves the import from the store and pins
		// the library's fingerprint in its manifest
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		QvtoEngine compiler = QvtoEngines.create(QvtoConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.registerPackage(library)
				.registerPackage(catalog)
				.addUnitResolver(new QvtoStoreUnitResolver(store))
				.unitResolverEnabled(true)
				.build());
		store.put(compiler.compile(
				ExampleFiles.read(base.resolve("transforms/CatalogFormat.qvto")), "catalog.Format"));
		UnitKey key = store.put(compiler.compile(
				ExampleFiles.read(base.resolve("transforms/Library2Catalog.qvto")), "Library2Catalog"));
		System.out.println("stored: " + key);

		// Runtime side: a fresh engine with no resolver at all — the prepared context is
		// complete, and the metamodels arrive as contributions to the preparer, once
		QvtoEngine runner = QvtoEngines.create(QvtoConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.build());
		PreparedContext prepared = new UnitPreparer(store, List.of(runner.unitBinder()))
				.registerPackage(library)
				.registerPackage(catalog)
				.prepare(key);

		EObject city = ExampleFiles.loadInstance(base.resolve("model/library.xmi"), library, catalog);
		BasicQvtoModelExtent in = new BasicQvtoModelExtent(city);
		BasicQvtoModelExtent out = new BasicQvtoModelExtent();
		QvtoExecutionResult result = runner.execute(prepared, "Library2Catalog",
				QvtoExecutionContext.of(in, out));
		if (!result.isSuccess()) {
			throw new IllegalStateException("execution failed: " + result.diagnostics());
		}
		return out.getContents().get(0);
	}
}
