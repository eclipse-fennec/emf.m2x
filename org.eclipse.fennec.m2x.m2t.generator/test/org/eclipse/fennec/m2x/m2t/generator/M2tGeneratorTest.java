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
package org.eclipse.fennec.m2x.m2t.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import aQute.bnd.build.Project;
import aQute.bnd.build.Workspace;
import aQute.bnd.service.generate.BuildContext;
import aQute.service.reporter.Report.Location;

/**
 * The generator as bnd runs it: a {@code Project} in a workspace, the attributes of the
 * {@code -generate} instruction as the context's local properties, and what comes back on the
 * context — errors with a location, warnings — is what the build shows (#270).
 */
class M2tGeneratorTest {

	private static final String NS_URI = "http://example.org/m2x/generator/1.0";

	@TempDir
	Path workspaceDir;

	private Project project;

	@BeforeEach
	void setUp() throws Exception {
		Files.createDirectories(workspaceDir.resolve("cnf"));
		Files.writeString(workspaceDir.resolve("cnf/build.bnd"), "");
		Path projectDir = Files.createDirectories(workspaceDir.resolve("gen"));
		Files.writeString(projectDir.resolve("bnd.bnd"), "");
		Files.createDirectories(projectDir.resolve("model"));
		Files.createDirectories(projectDir.resolve("templates"));
		writeMetamodelAndModel(projectDir);

		Workspace workspace = Workspace.getWorkspace(workspaceDir.toFile());
		project = workspace.getProject("gen");
		assertNotNull(project, "bnd found the project");
	}

	@Test
	@DisplayName("a parse error is placed in the template file, at its line")
	void parseErrorsArePositioned() throws Exception {
		BuildContext context = context("""
				[module t(_'http://example.org/m2x/generator/1.0')/]
				[comment short form[/comment]
				[template public main(b : Book)]
				[/template]
				""");

		Optional<String> failure = new M2tGenerator().generate(context, options());

		assertTrue(failure.isPresent(), "the generation failed");
		assertTrue(failure.get().contains("parse error"), failure.get());
		// ANTLR reports the short form twice — the stray text and the stray bracket — and both
		// arrive, each placed in the file at its line, with the column in the message
		List<String> errors = context.getErrors();
		assertEquals(2, errors.size(), () -> "one error per parse error: " + errors);
		for (String error : errors) {
			assertTrue(error.contains("t.mtl:2:"), error);
			Location location = context.getLocation(error);
			assertNotNull(location, "the error has a location");
			assertTrue(location.file.endsWith("t.mtl"), location.file);
			assertEquals(2, location.line);
		}
	}

	@Test
	@DisplayName("a main that writes no file is a warning naming the model")
	void fileLessExecutionWarns() throws Exception {
		BuildContext context = context("""
				[module t(_'http://example.org/m2x/generator/1.0')/]
				[template public main(b : Book)]
				nothing goes into a file here
				[/template]
				""");

		Optional<String> failure = new M2tGenerator().generate(context, options());

		assertTrue(failure.isEmpty(), () -> failure.orElse(""));
		assertTrue(context.getWarnings().stream()
				.anyMatch(w -> w.contains("'main' wrote no file for Book from book.xmi")),
				() -> "warnings: " + context.getWarnings());
	}

	@Test
	@DisplayName("an invalid value fails the generation and never reaches the file")
	void invalidValueFailsTheBuild() throws Exception {
		BuildContext context = context("""
				[module t(_'http://example.org/m2x/generator/1.0')/]
				[template public main(b : Book)]
				[file ('out.txt', false)]
				X=['a' + b.title/]
				[/file]
				[/template]
				""");

		Optional<String> failure = new M2tGenerator().generate(context, options());

		assertTrue(failure.isPresent(), "the generation failed");
		assertTrue(failure.get().contains("Null argument for operation '+'"), failure.get());
		assertTrue(failure.get().contains("t.mtl:4:3"), failure.get());
		Path written = project.getBase().toPath().resolve("out/out.txt");
		assertTrue(Files.exists(written), "what was generated is still written");
		assertFalse(Files.readString(written).contains("OclInvalid"));
	}

	@Test
	@DisplayName("a generation that writes its file reports nothing")
	void aCleanGenerationIsQuiet() throws Exception {
		BuildContext context = context("""
				[module t(_'http://example.org/m2x/generator/1.0')/]
				[template public main(b : Book)]
				[file ('out.txt', false)]
				X=[b.title.oclIsUndefined()/]
				[/file]
				[/template]
				""");

		Optional<String> failure = new M2tGenerator().generate(context, options());

		assertTrue(failure.isEmpty(), () -> failure.orElse(""));
		assertTrue(context.getErrors().isEmpty(), () -> context.getErrors().toString());
		assertTrue(context.getWarnings().isEmpty(), () -> context.getWarnings().toString());
		assertEquals("X=true", Files.readString(project.getBase().toPath().resolve("out/out.txt")).strip());
	}

	private BuildContext context(String template) throws IOException {
		Files.writeString(project.getBase().toPath().resolve("templates/t.mtl"), template);
		Map<String, String> attributes = Map.of(
				M2tGenerator.ATTR_TEMPLATE, "templates/t.mtl",
				M2tGenerator.ATTR_ECORE, "model/book.ecore",
				M2tGenerator.ATTR_MODEL, "model/book.xmi",
				M2tGenerator.ATTR_OUTPUT, "out/");
		return new BuildContext(project, attributes, List.of(), InputStream.nullInputStream(),
				OutputStream.nullOutputStream(), OutputStream.nullOutputStream());
	}

	/** bnd would build this from the command line; here nothing is set. */
	private static M2tGeneratorOptions options() {
		return (M2tGeneratorOptions) Proxy.newProxyInstance(M2tGeneratorTest.class.getClassLoader(),
				new Class<?>[] { M2tGeneratorOptions.class }, (proxy, method, args) -> switch (method.getName()) {
					case "output" -> Optional.empty();
					case "toString" -> "options";
					case "hashCode" -> 0;
					case "equals" -> proxy == args[0];
					default -> method.getReturnType() == boolean.class ? false
							: method.getReturnType() == List.class ? List.of() : null;
				});
	}

	/** A Book with an unset title, and the metamodel it is an instance of, as files. */
	private static void writeMetamodelAndModel(Path projectDir) throws IOException {
		EPackage library = EcoreFactory.eINSTANCE.createEPackage();
		library.setName("generator");
		library.setNsPrefix("generator");
		library.setNsURI(NS_URI);
		EClass bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(title);
		library.getEClassifiers().add(bookClass);

		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("ecore", new EcoreResourceFactoryImpl());
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("xmi", new XMIResourceFactoryImpl());
		Resource ecore = resourceSet.createResource(URI.createFileURI(file(projectDir, "model/book.ecore")));
		ecore.getContents().add(library);
		ecore.save(null);

		EObject book = EcoreUtil.create(bookClass);
		Resource model = resourceSet.createResource(URI.createFileURI(file(projectDir, "model/book.xmi")));
		model.getContents().add(book);
		model.save(null);
	}

	private static String file(Path projectDir, String relative) {
		return new File(projectDir.toFile(), relative).getAbsolutePath();
	}
}
