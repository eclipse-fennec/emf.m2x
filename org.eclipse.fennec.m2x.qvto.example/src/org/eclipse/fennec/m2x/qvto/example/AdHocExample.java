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
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;

/**
 * The ad-hoc way: parse the {@code .qvto} files and execute — no units anywhere. The imported
 * helper library arrives as <em>source</em> through a resolver at parse time; the models are
 * extents, exactly as they would be with compiled units.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class AdHocExample {

	private AdHocExample() {}

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
	 * Parses and runs the transformation over the example files.
	 *
	 * @return the produced catalog root
	 * @throws Exception if anything on the way refuses
	 */
	public static EObject run() throws Exception {
		Path base = ExampleFiles.baseDir();
		EPackage library = ExampleFiles.loadEcore(base.resolve("model/library.ecore"));
		EPackage catalog = ExampleFiles.loadEcore(base.resolve("model/catalog.ecore"));

		// The engine gets the metamodels as contributions, and the helper library as a source
		// resolver — the resolver decides nothing, it only says where the text is
		Path libraryFile = base.resolve("transforms/CatalogFormat.qvto");
		QvtoEngine engine = QvtoEngines.create(QvtoConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.registerPackage(library)
				.registerPackage(catalog)
				.addUnitResolver(name -> "catalog.Format".equals(name)
						? Optional.of(new QvtoUnit.SourceUnit(name,
								URI.createFileURI(libraryFile.toAbsolutePath().toString()),
								ExampleFiles.read(libraryFile)))
						: Optional.empty())
				.unitResolverEnabled(true)
				.build());

		OperationalTransformation transformation = engine.parse(
				ExampleFiles.read(base.resolve("transforms/Library2Catalog.qvto")), "Library2Catalog");

		EObject city = ExampleFiles.loadInstance(base.resolve("model/library.xmi"), library, catalog);
		BasicQvtoModelExtent in = new BasicQvtoModelExtent(city);
		BasicQvtoModelExtent out = new BasicQvtoModelExtent();
		QvtoExecutionResult result = engine.execute(transformation, QvtoExecutionContext.of(in, out));
		if (!result.isSuccess()) {
			throw new IllegalStateException("execution failed: " + result.diagnostics());
		}
		return out.getContents().get(0);
	}
}
