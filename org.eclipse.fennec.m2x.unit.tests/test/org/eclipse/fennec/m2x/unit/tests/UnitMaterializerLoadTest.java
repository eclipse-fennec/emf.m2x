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

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.m2t.M2tFactory;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.unit.api.UnitMaterializeException;
import org.eclipse.fennec.m2x.unit.api.UnitResourceSet;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.eclipse.fennec.m2x.unit.materialize.UnitMaterializer;
import org.eclipse.fennec.m2x.unit.store.UnitDocuments;
import org.eclipse.fennec.m2x.unit.store.UnitXmi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Loading a unit named by reference (#214): the URI holds a compiled-unit document — a store
 * load without the store — or a bare AST, the pre-unit shape, which binds in the consumer's
 * context all the same. No proxy is handed on either way.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class UnitMaterializerLoadTest {

	private static final String NS_URI = "http://example.org/m2x/materializer-load/1.0";

	private EPackage metamodel;
	private EClass bookClass;

	@BeforeEach
	void setUp() {
		metamodel = EcoreFactory.eINSTANCE.createEPackage();
		metamodel.setName("shelf");
		metamodel.setNsURI(NS_URI);
		metamodel.setNsPrefix("shelf");
		bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(title);
		metamodel.getEClassifiers().add(bookClass);
	}

	@Test
	void aDocumentAtAUri_isAStoreLoadWithoutTheStore(@TempDir Path dir) throws Exception {
		UnitDocuments.Sealed sealed = UnitDocuments.seal(
				UnitPackager.compile("m2t", "gen.Books", module()), DefaultUnitFingerprintService.INSTANCE);
		Path file = dir.resolve("gen.Books.xmi");
		Files.write(file, UnitXmi.write(sealed.document(), sealed.key()));

		UnitResourceSet context = new UnitResourceSet().registerPackage(metamodel);
		EObject root = UnitMaterializer.defaults().load(URI.createFileURI(file.toString()), context);

		CompiledUnit document = assertInstanceOf(CompiledUnit.class, root);
		assertSame(bookClass, variableType(document),
				"materialized: the context's instance, not a copy and not a proxy");
	}

	@Test
	void aBareAstAtAUri_bindsInTheConsumersContext(@TempDir Path dir) throws Exception {
		// The pre-unit shape: a module saved as a plain resource, its metamodel by href
		Path file = dir.resolve("gen.Books.xmi");
		saveBareModule(file);

		UnitResourceSet context = new UnitResourceSet().registerPackage(metamodel);
		EObject root = UnitMaterializer.defaults().load(URI.createFileURI(file.toString()), context);

		Module module = assertInstanceOf(Module.class, root);
		Template template = (Template) module.getOwnedModuleElement().get(0);
		assertSame(bookClass, template.getParameter().get(0).getType(),
				"a bare AST binds in the consumer's context too");
	}

	@Test
	void aBareAstWhoseReferencesResolveToNothing_isRefused_neverAProxy(@TempDir Path dir) throws Exception {
		Path file = dir.resolve("gen.Books.xmi");
		saveBareModule(file);

		UnitMaterializeException failure = assertThrows(UnitMaterializeException.class,
				() -> UnitMaterializer.defaults().load(URI.createFileURI(file.toString()), new UnitResourceSet()));
		assertTrue(failure.getMessage().contains("resolve to nothing"), failure.getMessage());
	}

	@Test
	void garbageAtAUri_isAnError(@TempDir Path dir) throws Exception {
		Path file = dir.resolve("gen.Books.xmi");
		Files.writeString(file, "this is not XMI");
		UnitMaterializeException failure = assertThrows(UnitMaterializeException.class,
				() -> UnitMaterializer.defaults().load(URI.createFileURI(file.toString()), new UnitResourceSet()));
		assertTrue(failure.getMessage().contains("cannot read"), failure.getMessage());
	}

	// ==== helpers ====

	private Module module() {
		Module module = M2tFactory.eINSTANCE.createModule();
		module.setName("gen.Books");
		module.setNsURI("http://example.org/m2x/materializer-load/module/gen.Books");
		Template template = M2tFactory.eINSTANCE.createTemplate();
		template.setName("book");
		Variable parameter = OclFactory.eINSTANCE.createVariable();
		parameter.setName("b");
		parameter.setType(bookClass);
		template.getParameter().add(parameter);
		module.getOwnedModuleElement().add(template);
		return module;
	}

	/** Saves a bare module; the metamodel gets a resource named by its nsURI, so the href reads {@code nsURI#//Book}. */
	private void saveBareModule(Path file) throws Exception {
		ResourceSetImpl saveSet = new ResourceSetImpl();
		saveSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
		Resource metamodelResource = saveSet.createResource(URI.createURI(metamodel.getNsURI()));
		metamodelResource.getContents().add(metamodel);
		Resource resource = saveSet.createResource(URI.createFileURI(file.toString()));
		resource.getContents().add(module());
		resource.save(Map.of());
		metamodelResource.getContents().remove(metamodel);
	}

	private static EClass variableType(CompiledUnit document) {
		Module module = (Module) document.getUnit();
		Template template = (Template) module.getOwnedModuleElement().get(0);
		return (EClass) template.getParameter().get(0).getType();
	}
}
