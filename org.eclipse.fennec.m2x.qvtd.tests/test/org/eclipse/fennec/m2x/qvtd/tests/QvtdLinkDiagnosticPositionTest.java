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
package org.eclipse.fennec.m2x.qvtd.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnit;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnitResolver;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;
import org.eclipse.fennec.m2x.qvtd.engine.internal.QvtdLinker;
import org.eclipse.fennec.m2x.qvtd.parser.QvtrParserSupport;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * An {@code import} nothing can resolve arrives as a {@link Resource.Diagnostic} at the
 * declaration, in {@link QvtdParseException#getErrors()} — every one of them, not only the first
 * (#264).
 */
class QvtdLinkDiagnosticPositionTest {

	private static final String NS_URI = "http://example.org/m2x/qvtr-link-diagnostics/1.0";

	private static final String LIBRARY = """
			transformation shared(source : bookshelf, target : bookshelf) {
			}
			""";

	private static final String TWO_MISSING = """
			import first.Missing;
			import shared.Library;
			import second.Missing;
			transformation importer(source : bookshelf, target : bookshelf) {
			}
			""";

	private EPackage.Registry registry;

	@BeforeEach
	void setUp() {
		EPackage bookshelf = EcoreFactory.eINSTANCE.createEPackage();
		bookshelf.setName("bookshelf");
		bookshelf.setNsURI(NS_URI);
		bookshelf.setNsPrefix("bookshelf");
		EClass book = EcoreFactory.eINSTANCE.createEClass();
		book.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		book.getEStructuralFeatures().add(title);
		bookshelf.getEClassifiers().add(book);
		registry = new EPackageRegistryImpl();
		registry.put(NS_URI, bookshelf);
	}

	@Test
	void everyUnresolvableImport_isADiagnosticAtItsDeclaration_inEveryMode() {
		QvtdEngine engine = engineWith(Map.of("shared.Library", LIBRARY));
		for (DependencyMode mode : DependencyMode.values()) {
			QvtdParseException failure = assertThrows(QvtdParseException.class,
					() -> engine.compile(TWO_MISSING, "importer", UnitCompileOptions.of(mode)), mode.getName());
			List<Resource.Diagnostic> errors = failure.getErrors();
			assertEquals(2, errors.size(), mode.getName() + ": " + errors);
			assertAt(errors.get(0), TWO_MISSING, "first.Missing");
			assertEquals("Cannot resolve import: first.Missing", errors.get(0).getMessage());
			assertAt(errors.get(1), TWO_MISSING, "second.Missing");
			assertEquals("Cannot resolve import: second.Missing", errors.get(1).getMessage());
		}
	}

	@Test
	void failureInsideADependency_isReportedAtTheImportOfThatDependency() {
		String mid = """
				import deep.Missing;
				transformation mid(source : bookshelf, target : bookshelf) {
				}
				""";
		String main = """

				import shared.Mid;
				transformation importer(source : bookshelf, target : bookshelf) {
				}
				""";
		QvtdParseException failure = assertThrows(QvtdParseException.class,
				() -> engineWith(Map.of("shared.Mid", mid)).compile(main, "importer"));
		assertEquals(1, failure.getErrors().size(), String.valueOf(failure.getErrors()));
		Resource.Diagnostic error = failure.getErrors().get(0);
		assertAt(error, main, "shared.Mid");
		assertTrue(error.getMessage().contains("In import 'shared.Mid'")
				&& error.getMessage().contains("Cannot resolve import: deep.Missing"), error.getMessage());
	}

	@Test
	void linker_reportsEveryUnresolvableImport_atItsDeclaration() throws Exception {
		QvtrParserSupport parserSupport = new QvtrParserSupport();
		RelationalTransformation transformation = parserSupport.parse(TWO_MISSING, "importer", registry);
		QvtdLinker linker = new QvtdLinker(parserSupport, registry,
				List.of(resolver(Map.of("shared.Library", LIBRARY))), Set.of());

		QvtdParseException failure = assertThrows(QvtdParseException.class, () -> linker.link(transformation));
		List<Resource.Diagnostic> errors = failure.getErrors();
		assertEquals(2, errors.size(), String.valueOf(errors));
		assertAt(errors.get(0), TWO_MISSING, "first.Missing");
		assertAt(errors.get(1), TWO_MISSING, "second.Missing");
	}

	private QvtdUnitResolver resolver(Map<String, String> units) {
		return name -> Optional.ofNullable(units.get(name))
				.map(source -> new QvtdUnit.SourceUnit(name, URI.createURI("mem:/" + name + ".qvtr"), source));
	}

	private QvtdEngine engineWith(Map<String, String> units) {
		return QvtdEngines.create(QvtdConfiguration.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.packageRegistry(registry)
				.addUnitResolver(resolver(units)).unitResolverEnabled(true)
				.build());
	}

	/** The diagnostic stands where {@code needle} first occurs in {@code source}. */
	private static void assertAt(Resource.Diagnostic diagnostic, String source, String needle) {
		int offset = source.indexOf(needle);
		assertTrue(offset >= 0, needle);
		String before = source.substring(0, offset);
		int line = (int) before.chars().filter(c -> c == '\n').count() + 1;
		int column = offset - (before.lastIndexOf('\n') + 1);
		assertEquals(line + ":" + column, diagnostic.getLine() + ":" + diagnostic.getColumn(),
				"position of '" + needle + "' for " + diagnostic.getMessage());
	}
}
