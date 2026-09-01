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
package org.eclipse.fennec.m2x.qvtd.example;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;

/**
 * The compiled way: compile once, store, prepare, enforce — after compile no parser runs. The
 * dynamic metamodels travel as copies inside the unit; the preparer's contributions decide
 * which instances the prepared transformation binds to.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class CompiledUnitExample {

	private CompiledUnitExample() {}

	public static void main(String[] args) throws Exception {
		AdHocExample.print(run());
	}

	/**
	 * Compiles, stores, prepares and enforces the transformation over the example files.
	 *
	 * @return the produced inventory root
	 * @throws Exception if anything on the way refuses
	 */
	public static EObject run() throws Exception {
		Path base = ExampleFiles.baseDir();
		EPackage library = ExampleFiles.loadEcore(base.resolve("model/library.ecore"));
		EPackage inventory = ExampleFiles.loadEcore(base.resolve("model/inventory.ecore"));

		// Build side: compile and store — the document carries copies of the dynamic metamodels
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		QvtdEngine compiler = QvtdEngines.create(QvtdConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.registerPackage(library)
				.registerPackage(inventory)
				.build());
		UnitKey key = store.put(compiler.compile(
				ExampleFiles.read(base.resolve("transforms/Library2Inventory.qvtr")), "Library2Inventory"));
		System.out.println("stored: " + key);

		// Runtime side: no resolver, no parser — contributions bind the prepared unit to the
		// same metamodel instances the input model is built with
		QvtdEngine runner = QvtdEngines.create(QvtdConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.build());
		PreparedContext prepared = new UnitPreparer(store, List.of(runner.unitBinder()))
				.registerPackage(library)
				.registerPackage(inventory)
				.prepare(key);

		EObject city = ExampleFiles.loadInstance(base.resolve("model/library.xmi"), library, inventory);
		QvtdModelExtent targetExtent = QvtdModelExtent.of();
		QvtdExecutionResult result = runner.execute(prepared, "Library2Inventory",
				QvtdExecutionContext.enforce("target",
						Map.of("source", QvtdModelExtent.of(city), "target", targetExtent)));
		if (!result.isSuccess()) {
			throw new IllegalStateException("execution failed: " + result.diagnostics());
		}
		return targetExtent.getContents().get(0);
	}
}
