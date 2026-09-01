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
package org.eclipse.fennec.m2x.m2t.example;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.engine.M2tEngines;
import org.eclipse.fennec.m2x.m2t.engine.M2tStoreUnitResolver;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;

/**
 * The compiled way: compile once, store, prepare, generate — after compile no resolver is asked
 * and no parser runs. The format module is compiled and stored first; the catalog module pins
 * its fingerprint.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class CompiledUnitExample {

	private CompiledUnitExample() {}

	public static void main(String[] args) throws Exception {
		M2tResult result = run(ExampleFiles.baseDir().resolve("gen"));
		System.out.println("generated: " + result.generatedFiles().keySet());
		System.out.println(Files.readString(ExampleFiles.baseDir().resolve("gen/catalog.md")));
	}

	/**
	 * Compiles, stores, prepares and generates into the given directory.
	 *
	 * @param targetDirectory where the files go — protected areas merge against what is there
	 * @return the generation result
	 * @throws Exception if anything on the way refuses
	 */
	public static M2tResult run(Path targetDirectory) throws Exception {
		Path base = ExampleFiles.baseDir();
		EPackage library = ExampleFiles.loadEcore(base.resolve("model/library.ecore"));

		// Build side: the format module goes in first, compiled — the catalog's compile
		// resolves the import from the store and pins the fingerprint
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		M2tEngine compiler = M2tEngines.create(M2tConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.registerPackage(library)
				.addUnitResolver(new M2tStoreUnitResolver(store))
				.unitResolverEnabled(true)
				.build());
		store.put(compiler.compile(ExampleFiles.read(base.resolve("templates/format.mtl")), "format"));
		UnitKey key = store.put(compiler.compile(ExampleFiles.read(base.resolve("templates/catalog.mtl")), "catalog"));
		System.out.println("stored: " + key);

		// Runtime side: no resolver, no parser
		M2tEngine runner = M2tEngines.create(M2tConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.build());
		PreparedContext prepared = new UnitPreparer(store, List.of(runner.unitBinder()))
				.registerPackage(library)
				.prepare(key);

		EObject city = ExampleFiles.loadInstance(base.resolve("model/library.xmi"), library);
		M2tResult result = runner.execute(prepared, "catalog", M2tContext.of(city, targetDirectory));
		if (!result.diagnostics().isEmpty()) {
			System.out.println("diagnostics: " + result.diagnostics());
		}
		return result;
	}
}
