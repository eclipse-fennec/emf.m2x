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
package org.eclipse.fennec.m2x.ocl.example;

import java.nio.file.Path;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.example.AdHocExample.Session;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;

/**
 * The document path, as a compiled unit: one side compiles the {@code .ocl} file and puts it
 * into a {@link UnitStore}; the other side prepares it from the store and <em>registers</em> it
 * — a document is installed into an engine, never executed, so for OCL the unit lifecycle ends
 * at {@code registerCompleteOclDocument(prepared, name)}. The registering engine never sees the
 * document text and runs no parser for it.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class CompiledUnitExample {

	private CompiledUnitExample() {}

	public static void main(String[] args) throws Exception {
		Session session = run();
		System.out.println("def label via compiled unit: "
				+ session.engine().evaluate("self.books->first().label",
						OclContext.of(session.city())));
	}

	/**
	 * Compiles, stores, prepares and registers the document, then opens the same session
	 * {@link AdHocExample} opens — everything the document puts into effect is identical.
	 *
	 * @return the session
	 * @throws Exception if anything on the way refuses
	 */
	public static Session run() throws Exception {
		Path base = ExampleFiles.baseDir();
		EPackage library = ExampleFiles.loadEcore(base.resolve("model/library.ecore"));

		// The compile side: parse once, seal, store. This is the last time the source is read.
		OclEngine compiler = OclEngines.create(new OclParserSupport(ExampleFiles.registryOf(library)));
		CompiledUnit unit = compiler.compileDocument("library.rules",
				ExampleFiles.read(base.resolve("documents/library.ocl")));
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		UnitKey key = store.put(unit);

		// The runtime side: its own engine, the store, the metamodel as a contribution —
		// and no document text anywhere
		OclEngine runtime = OclEngines.create(new OclParserSupport(ExampleFiles.registryOf(library)));
		PreparedContext prepared = new UnitPreparer(store, List.of(runtime.unitBinder()))
				.registerPackage(library).prepare(key);
		runtime.registerCompleteOclDocument(prepared, "library.rules");

		EObject city = ExampleFiles.loadInstance(base.resolve("model/library.xmi"), library);
		return new Session(runtime, city);
	}
}
