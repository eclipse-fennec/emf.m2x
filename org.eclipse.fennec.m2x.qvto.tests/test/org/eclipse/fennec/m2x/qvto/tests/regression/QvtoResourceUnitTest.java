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
package org.eclipse.fennec.m2x.qvto.tests.regression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.eclipse.fennec.m2x.unit.store.UnitDocuments;
import org.eclipse.fennec.m2x.unit.store.UnitXmi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Units by reference (#214): a resolver answers with a URI, loading and binding happen at
 * consumption, in the consumer's context, through the materializer — its second consumer
 * beside the store path.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoResourceUnitTest {

	private static final String HELPER_LIB = """
			library HelperLib {
			    helper greet() : String {
			        return 'hello';
			    }
			}
			""";
	private static final String MAIN = """
			modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
			import HelperLib;
			transformation Main(inout m : ECORE) {
			    main() {
			        m.objectsOfType(EPackage)->forEach(p) {
			            p.name := greet();
			        };
			    }
			}
			""";

	@Test
	void aDocumentAtAUri_resolves_andAPinNamesItsFingerprint(@TempDir Path dir) throws Exception {
		CompiledUnit library = bare().compile(HELPER_LIB, "HelperLib");
		UnitDocuments.Sealed sealed = UnitDocuments.seal(library, DefaultUnitFingerprintService.INSTANCE);
		Path file = dir.resolve("HelperLib.xmi");
		Files.write(file, UnitXmi.write(sealed.document(), sealed.key()));

		QvtoEngine engine = overReference(file);
		assertEquals("hello", runOn(engine, engine.parse(MAIN, "Main")));

		CompiledUnit main = engine.compile(MAIN, "Main");   // pin is the default
		assertEquals(sealed.key().fingerprint().orElseThrow(),
				main.getManifest().getDependencyEntry().get(0).getFingerprint(),
				"the pin names the fingerprint of the document the URI holds");
	}

	@Test
	void garbageAtAUri_isALinkTimeError_namingTheImport(@TempDir Path dir) throws Exception {
		Path file = dir.resolve("HelperLib.xmi");
		Files.writeString(file, "this is not XMI");
		QvtoEngine engine = overReference(file);
		QvtoParseException failure = assertThrows(QvtoParseException.class, () -> engine.compile(MAIN, "Main"));
		assertTrue(failure.getMessage().contains("Cannot resolve import 'HelperLib'"), failure.getMessage());
	}

	@Test
	void aUriHoldingNoUnit_saysWhatItFoundInstead(@TempDir Path dir) throws Exception {
		Path file = dir.resolve("HelperLib.xmi");
		ResourceSetImpl set = new ResourceSetImpl();
		set.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		Resource resource = set.createResource(URI.createFileURI(file.toString()));
		resource.getContents().add(EcoreFactory.eINSTANCE.createEPackage());
		resource.save(Map.of());

		QvtoEngine engine = overReference(file);
		QvtoParseException failure = assertThrows(QvtoParseException.class, () -> engine.compile(MAIN, "Main"));
		assertTrue(failure.getMessage().contains("no QVT-O unit"), failure.getMessage());
	}

	// ==== helpers ====

	private static QvtoEngine bare() {
		return QvtoEngines.create(QvtoConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build()).build());
	}

	private static QvtoEngine overReference(Path file) {
		return QvtoEngines.create(QvtoConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.addUnitResolver(name -> "HelperLib".equals(name)
						? Optional.of(new QvtoUnit.ResourceUnit(name, URI.createFileURI(file.toString())))
						: Optional.empty())
				.unitResolverEnabled(true)
				.build());
	}

	private static String runOn(QvtoEngine engine, OperationalTransformation transformation) {
		EObject pkg = EcoreUtil.create(EcorePackage.Literals.EPACKAGE);
		pkg.eSet(EcorePackage.Literals.ENAMED_ELEMENT__NAME, "Original");
		QvtoExecutionResult result = engine.execute(transformation,
				QvtoExecutionContext.of(new BasicQvtoModelExtent(List.of(pkg))));
		assertTrue(result.isSuccess(), () -> "execution failed: " + result.diagnostics());
		return (String) pkg.eGet(EcorePackage.Literals.ENAMED_ELEMENT__NAME);
	}
}
