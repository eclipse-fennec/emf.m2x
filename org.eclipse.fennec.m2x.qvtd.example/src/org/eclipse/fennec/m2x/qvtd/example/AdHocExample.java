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
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;

/**
 * The ad-hoc way: parse the {@code .qvtr} and enforce towards the target — no units anywhere.
 * The metamodels arrive as contributions, the models as named extents.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class AdHocExample {

	private AdHocExample() {}

	public static void main(String[] args) throws Exception {
		print(run());
	}

	static void print(EObject inventory) {
		System.out.println("inventory: " + inventory.eGet(inventory.eClass().getEStructuralFeature("name")));
		for (Object record : (Iterable<?>) inventory.eGet(inventory.eClass().getEStructuralFeature("records"))) {
			EObject eRecord = (EObject) record;
			System.out.println("  " + eRecord.eGet(eRecord.eClass().getEStructuralFeature("since"))
					+ "  " + eRecord.eGet(eRecord.eClass().getEStructuralFeature("label")));
		}
	}

	/**
	 * Parses and enforces the transformation over the example files.
	 *
	 * @return the produced inventory root
	 * @throws Exception if anything on the way refuses
	 */
	public static EObject run() throws Exception {
		Path base = ExampleFiles.baseDir();
		EPackage library = ExampleFiles.loadEcore(base.resolve("model/library.ecore"));
		EPackage inventory = ExampleFiles.loadEcore(base.resolve("model/inventory.ecore"));

		QvtdEngine engine = QvtdEngines.create(QvtdConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.registerPackage(library)
				.registerPackage(inventory)
				.build());

		RelationalTransformation transformation = engine.parse(
				ExampleFiles.read(base.resolve("transforms/Library2Inventory.qvtr")), "Library2Inventory");

		EObject city = ExampleFiles.loadInstance(base.resolve("model/library.xmi"), library, inventory);
		QvtdModelExtent targetExtent = QvtdModelExtent.of();
		QvtdExecutionResult result = engine.execute(transformation, QvtdExecutionContext.enforce("target",
				Map.of("source", QvtdModelExtent.of(city), "target", targetExtent)));
		if (!result.isSuccess()) {
			throw new IllegalStateException("execution failed: " + result.diagnostics());
		}
		return targetExtent.getContents().get(0);
	}
}
