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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnit;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.eclipse.fennec.m2x.unit.store.UnitDocuments;
import org.eclipse.fennec.m2x.unit.store.UnitXmi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Units by reference for QVT-R (#214): a resolver answers with where the transformation lives;
 * the linker loads and binds it in the engine's context — the metamodel instance the merged
 * relations refer to is the engine's, not a resolver's.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtdResourceUnitTest {

	private static final String NS_URI = "http://example.org/m2x/qvtr-reference/1.0";
	private static final String LIBRARY = """
			transformation shared(source : bookshelf, target : bookshelf) {
			    top relation CopyTitle {
			        t : String;
			        checkonly domain source b1 : Book { title = t };
			        enforce domain target b2 : Book { title = t };
			    }
			}
			""";
	private static final String IMPORTER = """
			import shared.Library;
			transformation importer(source : bookshelf, target : bookshelf) {
			}
			""";

	@Test
	void aDocumentAtAUri_resolves_andTheMergedRelationsRun(@TempDir Path dir) throws Exception {
		EPackage bookshelf = EcoreFactory.eINSTANCE.createEPackage();
		bookshelf.setName("bookshelf");
		bookshelf.setNsURI(NS_URI);
		bookshelf.setNsPrefix("bookshelf");
		EClass bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(title);
		bookshelf.getEClassifiers().add(bookClass);

		OclConfiguration ocl = OclConfiguration.builder(new OclParserSupport()).build();
		QvtdEngine bare = QvtdEngines.create(QvtdConfiguration.builder(ocl)
				.registerPackage(bookshelf).build());
		CompiledUnit library = bare.compile(LIBRARY, "shared.Library");
		UnitDocuments.Sealed sealed = UnitDocuments.seal(library, DefaultUnitFingerprintService.INSTANCE);
		Path file = dir.resolve("shared.Library.xmi");
		Files.write(file, UnitXmi.write(sealed.document(), sealed.key()));

		QvtdEngine engine = QvtdEngines.create(QvtdConfiguration.builder(ocl)
				.registerPackage(bookshelf)
				.addUnitResolver(name -> "shared.Library".equals(name)
						? Optional.of(new QvtdUnit.ResourceUnit(name, URI.createFileURI(file.toString())))
						: Optional.empty())
				.unitResolverEnabled(true)
				.build());

		RelationalTransformation importer = engine.parse(IMPORTER, "importer");
		EObject book = EcoreUtil.create(bookClass);
		book.eSet(bookClass.getEStructuralFeature("title"), "Moby Dick");
		QvtdModelExtent targetExtent = QvtdModelExtent.of();
		QvtdExecutionResult result = engine.execute(importer, QvtdExecutionContext.enforce("target",
				Map.of("source", QvtdModelExtent.of(book), "target", targetExtent)));

		assertTrue(result.isSuccess(), () -> "execution failed: " + result.diagnostics());
		assertEquals(1, targetExtent.getContents().size(), "one Book produced");
		EObject produced = targetExtent.getContents().get(0);
		assertEquals("Moby Dick", produced.eGet(produced.eClass().getEStructuralFeature("title")));
	}
}
