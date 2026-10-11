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
package org.eclipse.fennec.m2x.m2t.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@code invalid} is not text (#270).
 *
 * <p>An expression that cannot be evaluated answers {@code invalid}, and until now the word
 * {@code OclInvalid} went into the document in its place — as a value, as a separator, even as a
 * file name. The spot stays empty now, and the run reports the expression at its line. Where the
 * OCL engine already said why, that report is the one the author sees; where the value turned
 * invalid in silence, the generation names the expression itself.
 */
class M2tInvalidOutputTest {

	private static final String NS_URI = "http://example.org/m2x/invalid/1.0";

	private EPackage.Registry registry;
	private EClass bookClass;
	private EAttribute bookTitle;

	@BeforeEach
	void setUp() {
		EPackage library = EcoreFactory.eINSTANCE.createEPackage();
		library.setName("invalid");
		library.setNsPrefix("invalid");
		library.setNsURI(NS_URI);
		bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		bookTitle = EcoreFactory.eINSTANCE.createEAttribute();
		bookTitle.setName("title");
		bookTitle.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(bookTitle);
		library.getEClassifiers().add(bookClass);
		registry = new EPackageRegistryImpl();
		registry.put(NS_URI, library);
	}

	@Test
	@DisplayName("a null argument leaves the spot empty and is reported once, at its line")
	void nullArgumentInAStatement() throws Exception {
		M2tResult result = generate("""
				[module t(_'http://example.org/m2x/invalid/1.0')/]
				[template public main(b : Book)]
				[file ('out.txt', false)]
				X=['a' + b.title/]
				[/file]
				[/template]
				""");

		assertEquals("X=", result.generatedFiles().get("out.txt").strip());
		assertFalse(result.isSuccess(), "an invalid value is a failed generation");
		List<Diagnostic> errors = errors(result);
		assertEquals(1, errors.size(), () -> "one error, the engine's: " + result.diagnostics());
		// Line 4 of the template, column 3: "X=[" is three characters
		assertTrue(errors.get(0).getMessage().startsWith("t:4:3 "), errors.get(0).getMessage());
		assertTrue(errors.get(0).getMessage().contains("Null argument for operation '+'"),
				errors.get(0).getMessage());
	}

	@Test
	@DisplayName("a query that answers invalid writes nothing")
	void invalidQueryResult() throws Exception {
		M2tResult result = generate("""
				[module t(_'http://example.org/m2x/invalid/1.0')/]
				[query public label(s : String) : String = 'a' + s /]
				[template public main(b : Book)]
				[file ('out.txt', false)]
				X=[label(b.title)/]
				[/file]
				[/template]
				""");

		assertEquals("X=", result.generatedFiles().get("out.txt").strip());
		assertFalse(result.isSuccess());
		assertFalse(result.generatedFiles().get("out.txt").contains("OclInvalid"));
	}

	@Test
	@DisplayName("a value that turned invalid without a word is named by the generation")
	void silentInvalidIsReportedByTheTemplate() throws Exception {
		M2tResult result = generate("""
				[module t(_'http://example.org/m2x/invalid/1.0')/]
				[template public main(b : Book)]
				[file ('out.txt', false)]
				X=[invalid/]
				[/file]
				[/template]
				""");

		assertEquals("X=", result.generatedFiles().get("out.txt").strip());
		List<Diagnostic> errors = errors(result);
		assertEquals(1, errors.size(), () -> result.diagnostics().toString());
		assertTrue(errors.get(0).getMessage().startsWith("t:4:3 "), errors.get(0).getMessage());
		assertTrue(errors.get(0).getMessage().contains("invalid"), errors.get(0).getMessage());
	}

	@Test
	@DisplayName("an invalid separator is left out and reported")
	void invalidSeparator() throws Exception {
		M2tResult result = generate("""
				[module t(_'http://example.org/m2x/invalid/1.0')/]
				[template public main(b : Book)]
				[file ('out.txt', false)]
				[for (s : String | Sequence{'x', 'y'}) separator('a' + b.title)][s/][/for]
				[/file]
				[/template]
				""");

		assertEquals("xy", result.generatedFiles().get("out.txt").strip());
		assertFalse(result.isSuccess());
		assertFalse(result.diagnostics().isEmpty());
	}

	@Test
	@DisplayName("an invalid file URL skips the block instead of naming a file OclInvalid")
	void invalidFileUrl() throws Exception {
		M2tResult result = generate("""
				[module t(_'http://example.org/m2x/invalid/1.0')/]
				[template public main(b : Book)]
				[file ('a' + b.title, false)]
				content
				[/file]
				[/template]
				""");

		assertTrue(result.generatedFiles().isEmpty(), () -> "files: " + result.generatedFiles());
		assertFalse(result.isSuccess());
		assertTrue(errors(result).stream().anyMatch(d -> d.getMessage().contains("[file]")),
				() -> result.diagnostics().toString());
	}

	private static List<Diagnostic> errors(M2tResult result) {
		return result.diagnostics().stream()
				.filter(d -> d.getSeverity() >= Diagnostic.ERROR)
				.toList();
	}

	private M2tResult generate(String template) throws Exception {
		OclConfiguration ocl = OclConfiguration.builder(new OclParserSupport(registry)).build();
		M2tEngine engine = M2tEngines.create(M2tConfiguration.builder(ocl)
				.packageRegistry(registry)
				.build());
		Module module = engine.parse(template, "t");
		engine.link(module);
		EObject book = EcoreUtil.create(bookClass);
		return engine.execute(module, M2tContext.of(book));
	}
}
