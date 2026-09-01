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
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.api.M2tUnit;
import org.eclipse.fennec.m2x.m2t.engine.M2tEngines;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;

/**
 * The ad-hoc way: parse the {@code .mtl} files and generate — no units anywhere. The imported
 * format module arrives as <em>source</em> through a resolver at parse time. Generation goes to
 * {@code gen/}: run it twice and edit the protected area in between — the curator notes
 * survive, the generated part is regenerated.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class AdHocExample {

	private AdHocExample() {}

	public static void main(String[] args) throws Exception {
		M2tResult result = run(ExampleFiles.baseDir().resolve("gen"));
		System.out.println("generated: " + result.generatedFiles().keySet());
		System.out.println(Files.readString(ExampleFiles.baseDir().resolve("gen/catalog.md")));
	}

	/**
	 * Parses the modules and generates into the given directory.
	 *
	 * @param targetDirectory where the files go — protected areas merge against what is there
	 * @return the generation result
	 * @throws Exception if anything on the way refuses
	 */
	public static M2tResult run(Path targetDirectory) throws Exception {
		Path base = ExampleFiles.baseDir();
		EPackage library = ExampleFiles.loadEcore(base.resolve("model/library.ecore"));

		Path formatFile = base.resolve("templates/format.mtl");
		M2tEngine engine = M2tEngines.create(M2tConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.registerPackage(library)
				.addUnitResolver(name -> "format".equals(name)
						? Optional.of(new M2tUnit.SourceUnit(name,
								URI.createFileURI(formatFile.toAbsolutePath().toString()),
								ExampleFiles.read(formatFile)))
						: Optional.empty())
				.unitResolverEnabled(true)
				.build());

		Module module = engine.parse(ExampleFiles.read(base.resolve("templates/catalog.mtl")), "catalog");

		EObject city = ExampleFiles.loadInstance(base.resolve("model/library.xmi"), library);
		M2tResult result = engine.execute(module, M2tContext.of(city, targetDirectory));
		if (!result.diagnostics().isEmpty()) {
			System.out.println("diagnostics: " + result.diagnostics());
		}
		return result;
	}
}
