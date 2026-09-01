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
package org.eclipse.fennec.m2x.unit.registry.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.m2t.M2tFactory;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistryWriter;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;
import org.eclipse.fennec.m2x.unit.api.UnitResourceSet;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.eclipse.fennec.m2x.unit.materialize.UnitMaterializer;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.eclipse.fennec.m2x.unit.store.UnitDocuments;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.eclipse.fennec.m2x.unit.store.UnitXmi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * The whole delivery rail from configuration alone (#213): unit XMIs in a directory, a
 * {@code FileEObjectProvider} loading them, a gated {@code EObjectRegistry} holding them, and
 * the {@code M2xRegistryUnitStore} component publishing the {@code UnitStore} on top — the
 * simple, file-based stand-in for what a model atlas delivers.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
class RegistryUnitStoreFileOSGiTest {

	private static final String NS_URI = "http://example.org/m2x/registry-osgi/1.0";

	@InjectService
	ConfigurationAdmin configurations;

	@Test
	@DisplayName("units from files reach a consumer through nothing but configuration")
	void aFileFedRegistry_servesUnits(
			@InjectService(cardinality = 0) ServiceAware<UnitStore> stores,
			@InjectService(cardinality = 0, filter = "(emf.eobject.registry.name=units)")
			ServiceAware<EObjectRegistryWriter> writers) throws Exception {
		// A directory with one unit XMI — what a build or an atlas export drops there
		EPackage shelf = shelf();
		CompiledUnit compiled = compiledTemplate(shelf, "gen.Books");
		UnitDocuments.Sealed sealed = UnitDocuments.seal(compiled, DefaultUnitFingerprintService.INSTANCE);
		Path directory = Files.createTempDirectory("m2x-unit-files");
		Files.write(directory.resolve("gen.Books.xmi"), UnitXmi.write(sealed.document(), sealed.key()));

		Configuration provider = configurations.createFactoryConfiguration("FileEObjectProvider", "?");
		Configuration registry = configurations.createFactoryConfiguration("EObjectRegistry", "?");
		Configuration store = configurations.getConfiguration("M2xRegistryUnitStore", "?");
		try {
			Dictionary<String, Object> providerProperties = new Hashtable<>();
			providerProperties.put("emf.eobject.provider.name", "unit-files");
			providerProperties.put("locations", new String[] { directory.toString() });
			provider.update(providerProperties);

			Dictionary<String, Object> registryProperties = new Hashtable<>();
			registryProperties.put("name", "units");
			registryProperties.put("initialProvider.target", "(emf.eobject.provider.name=unit-files)");
			registry.update(registryProperties);

			// The registry publishes both faces in one registration, once the provider loaded.
			// Configuring the store only afterwards makes the writer present at its activation —
			// no reliance on a greedy re-bind racing the test's service lookup.
			assertNotNull(writers.waitForService(10_000), "the write face is published under the name");

			Dictionary<String, Object> storeProperties = new Hashtable<>();
			storeProperties.put("registry.target", "(emf.eobject.registry.name=units)");
			storeProperties.put("writer.target", "(emf.eobject.registry.name=units)");
			store.update(storeProperties);

			// The registry publishes only after the provider loaded, the store only after the
			// registry — a UnitStore that is there is a UnitStore whose content is complete
			UnitStore units = stores.waitForService(10_000);
			assertNotNull(units, "the UnitStore service came up over the file-fed registry");

			// The provider filed the entry under its own key, without unit.* properties —
			// it answers by what its manifest says
			UnitKey key = sealed.key();
			assertTrue(units.contains(key));
			assertEquals(List.of(key), units.versions("m2t", "gen.Books", UnitKind.COMPILED));

			PackagedUnit loaded = (PackagedUnit) units.get(key).orElseThrow();
			UnitResourceSet context = new UnitResourceSet();
			context.registerPackage(shelf);
			UnitMaterializer.defaults().materialize(loaded, context);
			assertEquals("gen.Books", loaded.qualifiedName());
			assertSame(shelf.getEClassifier("Book"), variableType(loaded.document()),
					"materialized in the consumer's context, the type is the consumer's instance");

			// The writer face is bound, so the store also takes new units — beside the
			// provider's entries, under its own source
			CompiledUnit second = compiledTemplate(shelf, "gen.Covers");
			UnitKey put = units.put(second);
			assertTrue(units.contains(put));
			assertEquals("gen.Covers", ((PackagedUnit) units.get(put).orElseThrow()).qualifiedName());
		} finally {
			store.delete();
			registry.delete();
			provider.delete();
		}
	}

	// --- helpers ---

	private static EPackage shelf() {
		EPackage shelf = EcoreFactory.eINSTANCE.createEPackage();
		shelf.setName("shelf");
		shelf.setNsURI(NS_URI);
		shelf.setNsPrefix("shelf");
		EClass book = EcoreFactory.eINSTANCE.createEClass();
		book.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		book.getEStructuralFeatures().add(title);
		shelf.getEClassifiers().add(book);
		return shelf;
	}

	private static CompiledUnit compiledTemplate(EPackage shelf, String name) {
		Module module = M2tFactory.eINSTANCE.createModule();
		module.setName(name);
		module.setNsURI("http://example.org/m2x/registry-osgi/module/" + name);
		Template template = M2tFactory.eINSTANCE.createTemplate();
		template.setName("book");
		Variable parameter = OclFactory.eINSTANCE.createVariable();
		parameter.setName("b");
		parameter.setType((EClass) shelf.getEClassifier("Book"));
		template.getParameter().add(parameter);
		module.getOwnedModuleElement().add(template);
		return UnitPackager.compile("m2t", name, module);
	}

	private static EClass variableType(CompiledUnit document) {
		Module module = (Module) document.getUnit();
		Template template = (Template) module.getOwnedModuleElement().get(0);
		return (EClass) template.getParameter().get(0).getType();
	}
}
