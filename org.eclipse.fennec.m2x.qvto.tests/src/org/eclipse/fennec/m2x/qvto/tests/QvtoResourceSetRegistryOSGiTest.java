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
package org.eclipse.fennec.m2x.qvto.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.fennec.emf.osgi.configurator.EPackageConfigurator;
import org.eclipse.fennec.emf.osgi.constants.EMFNamespaces;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.annotation.require.RequireOCL;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.annotation.require.RequireQVTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.runtime.ServiceComponentRuntime;
import org.osgi.service.component.runtime.dto.ComponentDescriptionDTO;
import org.osgi.service.component.runtime.dto.ReferenceDTO;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * The #245 situation: {@code emf.osgi} registers a metamodel with
 * {@code emf.model.scope=resourceset}, which puts it into the resource set's package registry and
 * deliberately not into the global one. An engine that fell back to the global registry could not
 * see it and would fail the modeltype declaration — or, for a loaded unit, bind the wrong package instance and match nothing. The component binds the resource set as a mandatory
 * reference, so the engine has to resolve the metamodel from the first parse on.
 */
@RequireOCL
@RequireQVTO
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
class QvtoResourceSetRegistryOSGiTest {

	private static final String NS_URI = "http://example.org/m2x/osgi-resourceset-only/qvto/1.0";

	@Test
	@DisplayName("a metamodel registered for the resource set only is what the engine resolves")
	void resourceSetOnlyMetamodelIsResolved(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000) QvtoEngine engine) throws Exception {
		EPackage shelf = shelfPackage();
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put(EMFNamespaces.EMF_MODEL_SCOPE, EMFNamespaces.EMF_MODEL_SCOPE_RESOURCE_SET);
		properties.put(EMFNamespaces.EMF_NAME, "rsshelf");
		properties.put(EMFNamespaces.EMF_MODEL_NSURI, NS_URI);
		ServiceRegistration<EPackageConfigurator> registration = context.registerService(
				EPackageConfigurator.class, new ShelfConfigurator(shelf), properties);
		try {
			assertNull(EPackage.Registry.INSTANCE.getEPackage(NS_URI),
					"control: a resource-set-scoped metamodel must not reach the global registry");
			assertNotNull(awaitParse(engine), "the modeltype has to resolve through the bound resource set");
		} finally {
			registration.unregister();
		}
	}

	/**
	 * The configurator reaches the registry through a dynamic reference of emf.osgi's registry
	 * component, so the metamodel may become visible a moment after {@code registerService}
	 * returns. Retried until the parse succeeds; the last failure is what a timeout reports.
	 */
	private static OperationalTransformation awaitParse(QvtoEngine engine) throws Exception {
		long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
		Exception last = null;
		do {
			try {
				return engine.parse("""
						modeltype SHELF uses '%s';
						transformation RsOnly(in source : SHELF);
						main() { }
						""".formatted(NS_URI), "RsOnly");
			} catch (Exception failure) {
				last = failure;
				Thread.sleep(100);
			}
		} while (System.nanoTime() < deadline);
		fail("the engine never resolved the resource-set-only metamodel: " + last);
		return null;
	}

	@Test
	@DisplayName("the resource set is a mandatory, prototype-required reference — there is no fallback engine")
	void resourceSetReferenceIsMandatory(@InjectService(timeout = 5000) ServiceComponentRuntime scr) {
		// The 'without a resource set' case: it does not exist, by construction. A mandatory
		// reference means the component waits for the service instead of activating on the
		// global registry — which is the whole point (#245), so the contract is asserted on the
		// component description rather than on the absence of a service, which nothing could
		// wait for deterministically.
		ComponentDescriptionDTO description = scr.getComponentDescriptionDTOs().stream()
				.filter(candidate -> "DefaultQvtoEngine".equals(candidate.name))
				.findFirst()
				.orElseThrow(() -> new AssertionError("component " + "DefaultQvtoEngine" + " is not there"));
		ReferenceDTO reference = Arrays.stream(description.references)
				.filter(candidate -> "resourceSet".equals(candidate.name))
				.findFirst()
				.orElseThrow(() -> new AssertionError("no reference named resourceSet in " + "DefaultQvtoEngine"));
		assertEquals(ResourceSet.class.getName(), reference.interfaceName);
		assertEquals("1..1", reference.cardinality, "mandatory — no optional fallback");
		assertEquals("static", reference.policy);
		assertEquals("prototype_required", reference.scope, "every engine gets its own resource set");
	}

	private static EPackage shelfPackage() {
		EPackage shelf = EcoreFactory.eINSTANCE.createEPackage();
		shelf.setName("rsshelf");
		shelf.setNsURI(NS_URI);
		shelf.setNsPrefix("rsshelf");
		EClass book = EcoreFactory.eINSTANCE.createEClass();
		book.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		book.getEStructuralFeatures().add(title);
		shelf.getEClassifiers().add(book);
		return shelf;
	}

	/** Puts the package into whichever registry emf.osgi hands over — here the resource set's. */
	private record ShelfConfigurator(EPackage shelf) implements EPackageConfigurator {
		@Override
		public void configureEPackage(EPackage.Registry registry) {
			registry.put(shelf.getNsURI(), shelf);
		}

		@Override
		public void unconfigureEPackage(EPackage.Registry registry) {
			registry.remove(shelf.getNsURI());
		}
	}
}
