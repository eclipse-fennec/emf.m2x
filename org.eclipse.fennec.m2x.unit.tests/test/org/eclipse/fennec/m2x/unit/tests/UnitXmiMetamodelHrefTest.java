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
package org.eclipse.fennec.m2x.unit.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.m2t.M2tFactory;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.unit.api.UnitResourceSet;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.eclipse.fennec.m2x.unit.materialize.UnitMaterializer;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.eclipse.fennec.m2x.unit.store.UnitDocuments;
import org.eclipse.fennec.m2x.unit.store.UnitXmi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * A metamodel loaded from an {@code .ecore} file is referenced by its nsURI, never by the file's
 * location (#231): a file path in a unit is a machine detail that would make the reader
 * demand-load a fresh instance past the consumer context — and make the unit unportable.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class UnitXmiMetamodelHrefTest {

	private static final String NS_URI = "http://example.org/m2x/href-test/1.0";

	@Test
	void aFileLoadedMetamodel_travelsByItsNsURI(@TempDir Path dir) throws Exception {
		// A metamodel that lives in a file — the normal user case
		Path ecore = dir.resolve("shelf.ecore");
		Files.writeString(ecore, """
				<?xml version="1.0" encoding="UTF-8"?>
				<ecore:EPackage xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI"
				    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				    xmlns:ecore="http://www.eclipse.org/emf/2002/Ecore"
				    name="shelf" nsURI="%s" nsPrefix="shelf">
				  <eClassifiers xsi:type="ecore:EClass" name="Book"/>
				</ecore:EPackage>
				""".formatted(NS_URI));
		ResourceSetImpl set = new ResourceSetImpl();
		set.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
		EPackage shelf = (EPackage) set
				.getResource(URI.createFileURI(ecore.toAbsolutePath().toString()), true).getContents().get(0);

		Module module = M2tFactory.eINSTANCE.createModule();
		module.setName("gen.Books");
		module.setNsURI("http://example.org/m2x/href-test/module/1.0");
		Template template = M2tFactory.eINSTANCE.createTemplate();
		template.setName("book");
		Variable parameter = OclFactory.eINSTANCE.createVariable();
		parameter.setName("b");
		parameter.setType(shelf.getEClassifier("Book"));
		template.getParameter().add(parameter);
		module.getOwnedModuleElement().add(template);

		UnitDocuments.Sealed sealed = UnitDocuments.seal(
				UnitPackager.compile("m2t", "gen.Books", module), DefaultUnitFingerprintService.INSTANCE);
		byte[] bytes = UnitXmi.write(sealed.document(), sealed.key());

		assertFalse(new String(bytes, StandardCharsets.UTF_8).contains("file:"),
				"no machine path travels inside a unit");
		assertTrue(new String(bytes, StandardCharsets.UTF_8).contains(NS_URI + "#"),
				"the metamodel href is the nsURI");

		// And the consumer's contribution decides the instance — not a demand-load of the file
		EObject root = UnitXmi.read(bytes, sealed.key());
		UnitResourceSet context = new UnitResourceSet().registerPackage(shelf);
		UnitMaterializer.defaults().materialize(new PackagedUnit((CompiledUnit) root), context);
		Module loaded = (Module) ((CompiledUnit) root).getUnit();
		EClass type = (EClass) ((Template) loaded.getOwnedModuleElement().get(0)).getParameter().get(0).getType();
		assertSame(shelf.getEClassifier("Book"), type, "the contributed instance answers");
	}
}
