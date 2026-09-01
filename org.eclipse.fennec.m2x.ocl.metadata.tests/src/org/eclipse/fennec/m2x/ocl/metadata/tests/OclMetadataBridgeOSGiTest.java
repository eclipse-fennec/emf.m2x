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
package org.eclipse.fennec.m2x.ocl.metadata.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.emf.osgi.metadata.MetadataService;
import org.eclipse.fennec.emf.osgi.model.metadata.AspectEntry;
import org.eclipse.fennec.emf.osgi.model.metadata.PackageMetadata;
import org.eclipse.fennec.m2x.ocl.api.OclDelegates;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.Property;
import org.osgi.test.common.annotation.config.WithConfiguration;
import org.osgi.test.common.annotation.config.WithFactoryConfiguration;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * Compiled OCL reaching the metadata tree of the model version it belongs to.
 *
 * <p>What the plain-Java tests cannot show is the part that only a framework has: a model that
 * registers as a service, a second version of it alongside the first, and a bridge that reacts
 * to both. The models here are registered by the test itself — an {@code EPackage} service is
 * all the metadata whiteboard binds, and doing it by hand is what makes the <em>moment</em> of
 * registration something a test can choose.
 *
 * <p>Three situations, from the registry guide's use cases:
 * <ul>
 *   <li><b>the model arrives late</b> — an expression is compiled before any version of its
 *       model is registered; the handler replay puts it on the fresh tree</li>
 *   <li><b>the version bump</b> — a second, diverging version registers while the first stays
 *       live; each tree answers with what was compiled against it, and with nothing else</li>
 *   <li><b>the late bridge</b> — content and model are long up when the bridge is configured;
 *       the listener replay makes late wiring indistinguishable from early wiring</li>
 * </ul>
 *
 * <p>The third one is what the aspect face is worth: an expression compiled against v1 must not
 * appear on v2's tree, because its references point into v1 and a consumer of the aspect has no
 * way to notice (eclipse-fennec/emf.osgi#81).
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
@WithFactoryConfiguration(factoryPid = "EObjectRegistry", location = "?", name = "ocl", properties = {
		@Property(key = "name", value = "ocl-compiled"),
		@Property(key = "initialProvider.target", value = "(emf.eobject.provider.name=ocl-empty-provider)")
})
@WithFactoryConfiguration(factoryPid = "EObjectRegistryMetadataBridge", location = "?", name = "ocl", properties = {
		@Property(key = "emf.eobject.registry.name", value = "ocl-compiled"),
		@Property(key = "aspect.type.id", value = "ocl.compiled"),
		@Property(key = "anchorResolver.target", value = "(anchor.resolver.name=ocl)")
})
@WithConfiguration(pid = "OclConstraintPrecompiler", location = "?", properties = {
		@Property(key = "cache.target", value = "(cache.name=metadata)"),
		@Property(key = "versioned.target", value = "(cache.name=metadata)")
})
class OclMetadataBridgeOSGiTest {

	private static final String ASPECT_TYPE_ID = "ocl.compiled";

	private static final String LATE_ASPECT_TYPE_ID = "ocl.compiled.late";

	private static final String NS_URI = "http://example.org/m2x/bridge-osgi/1.0";

	private static final String INVARIANT = "self.pages > 0";

	private final List<ServiceRegistration<?>> registrations = new ArrayList<>();

	private final List<Configuration> lateConfigurations = new ArrayList<>();

	@InjectBundleContext
	BundleContext context;

	@AfterEach
	void unregisterModels() throws Exception {
		registrations.forEach(registration -> {
			try {
				registration.unregister();
			} catch (IllegalStateException alreadyGone) {
				// the test itself unregistered it — that is one of the things being tested
			}
		});
		registrations.clear();
		for (Configuration configuration : lateConfigurations) {
			configuration.delete();
		}
		lateConfigurations.clear();
	}

	@Test
	@DisplayName("a model that registers late finds its compiled OCL waiting")
	void theLateModelGetsItsAspectOnRegistration(
			@InjectService(timeout = 5000) MetadataService metadata) {
		EPackage ePackage = bookPackage(false);
		EClass book = bookOf(ePackage);

		// nothing is registered yet, so there is no tree anything could be placed on
		assertTrue(metadata.getClassAspect(book, ASPECT_TYPE_ID).isEmpty());

		register(ePackage);

		await(() -> metadata.getClassAspect(book, ASPECT_TYPE_ID).isPresent(),
				"the aspect appears once the model's tree exists");
	}

	@Test
	@DisplayName("each version answers with what was compiled against it, and with nothing else")
	void aVersionBumpKeepsTheVersionsApart(@InjectService(timeout = 5000) MetadataService metadata) {
		EPackage v1 = bookPackage(false);
		EPackage v2 = bookPackage(true);
		register(v1);
		await(() -> metadata.getClassAspect(bookOf(v1), ASPECT_TYPE_ID).isPresent(),
				"the first version is answered for");

		register(v2);

		await(() -> metadata.getClassAspect(bookOf(v2), ASPECT_TYPE_ID).isPresent(),
				"the second version gets its own compiled invariant");
		assertTrue(metadata.getClassAspect(bookOf(v1), ASPECT_TYPE_ID).isPresent(),
				"and the first keeps its own");

		String firstFingerprint = fingerprintOf(metadata, v1);
		String secondFingerprint = fingerprintOf(metadata, v2);
		assertNotEquals(firstFingerprint, secondFingerprint,
				"two versions of one nsURI, or this test proves nothing");

		// One aspect per class and type id: if v1's expression had been copied onto v2's tree
		// as well, the two trees would answer with the same object — the failure of #81.
		AspectEntry first = metadata.getClassAspect(bookOf(v1), ASPECT_TYPE_ID).orElseThrow();
		AspectEntry second = metadata.getClassAspect(bookOf(v2), ASPECT_TYPE_ID).orElseThrow();
		assertNotEquals(first, second);
		assertEquals(firstFingerprint, versionOf(first),
				"the aspect sits on the version its expression was compiled against");
		assertEquals(secondFingerprint, versionOf(second));
	}

	@Test
	@DisplayName("a bridge configured after the fact gets the content that is already there")
	void theLateBridgeReplayFindsExistingContent(
			@InjectService(timeout = 5000) MetadataService metadata,
			@InjectService(timeout = 5000) ConfigurationAdmin configurationAdmin) throws Exception {
		EPackage ePackage = bookPackage(false);
		register(ePackage);
		await(() -> metadata.getClassAspect(bookOf(ePackage), ASPECT_TYPE_ID).isPresent(),
				"the configured bridge answers first, so there is content to be found");

		// a second bridge over the same registry, with its own type id — the shape use case 6
		// of the registry guide describes: late wiring must be indistinguishable from early
		Configuration late = configurationAdmin.createFactoryConfiguration(
				"EObjectRegistryMetadataBridge", "?");
		lateConfigurations.add(late);
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put("emf.eobject.registry.name", "ocl-compiled");
		properties.put("aspect.type.id", LATE_ASPECT_TYPE_ID);
		properties.put("anchorResolver.target", "(anchor.resolver.name=ocl)");
		late.update(properties);

		await(() -> metadata.getClassAspect(bookOf(ePackage), LATE_ASPECT_TYPE_ID).isPresent(),
				"the listener replay hands the late bridge what the registry already holds");
	}

	@Test
	@DisplayName("what a version leaves behind goes with it")
	void aDepartingVersionTakesItsCompiledOclWithIt(
			@InjectService(timeout = 5000) MetadataService metadata) {
		EPackage ePackage = bookPackage(false);
		ServiceRegistration<?> registration = register(ePackage);
		await(() -> metadata.getClassAspect(bookOf(ePackage), ASPECT_TYPE_ID).isPresent(),
				"the model is answered for before it goes");

		registration.unregister();

		await(() -> metadata.getPackageMetadataVersions(NS_URI).isEmpty(),
				"the version is gone, and with it its tree");
	}

	// --- helpers ---

	private ServiceRegistration<?> register(EPackage ePackage) {
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put("emf.name", ePackage.getName());
		properties.put("emf.nsURI", ePackage.getNsURI());
		ServiceRegistration<?> registration =
				context.registerService(EPackage.class, ePackage, properties);
		registrations.add(registration);
		return registration;
	}

	private static String fingerprintOf(MetadataService metadata, EPackage ePackage) {
		return metadata.getPackageMetadata(ePackage).map(PackageMetadata::getModelFingerprint)
				.orElseThrow();
	}

	/** The version of the tree an aspect ended up on. */
	private static String versionOf(AspectEntry aspect) {
		return Optional.ofNullable(aspect.eContainer())
				.map(classMetadata -> (PackageMetadata) classMetadata.eContainer())
				.map(PackageMetadata::getModelFingerprint)
				.orElseThrow();
	}

	private static void await(BooleanSupplier condition, String what) {
		// 30s, not 5: the CI runner executes the whole workspace build in parallel around this
		// framework, and under Java 25 the bridge pipeline's one-time startup exceeded 5s there
		// while every local run stayed far below it (#219). A passing test is as fast as before —
		// the poll returns the moment the condition holds; only a genuine failure waits this long.
		long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(30);
		while (System.nanoTime() < deadline) {
			if (condition.getAsBoolean()) {
				return;
			}
			try {
				Thread.sleep(25);
			} catch (InterruptedException interrupted) {
				Thread.currentThread().interrupt();
				break;
			}
		}
		throw new AssertionError("timed out waiting: " + what);
	}

	private static EClass bookOf(EPackage ePackage) {
		return (EClass) ePackage.getEClassifier("Book");
	}

	/** A model that states an invariant, so the precompiler has something to compile. */
	private static EPackage bookPackage(boolean withChapters) {
		EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
		ePackage.setName("bookshelf");
		ePackage.setNsURI(NS_URI);
		ePackage.setNsPrefix("bookshelf");

		EClass book = EcoreFactory.eINSTANCE.createEClass();
		book.setName("Book");
		book.getEStructuralFeatures().add(attribute("pages"));
		if (withChapters) {
			book.getEStructuralFeatures().add(attribute("chapters"));
		}
		invariant(book, "PagesArePositive", INVARIANT);
		ePackage.getEClassifiers().add(book);
		return ePackage;
	}

	private static EAttribute attribute(String name) {
		EAttribute attribute = EcoreFactory.eINSTANCE.createEAttribute();
		attribute.setName(name);
		attribute.setEType(EcorePackage.Literals.EINT);
		return attribute;
	}

	/** An invariant the way an .ecore carries one: named by Ecore, bodied by the delegate. */
	private static void invariant(EClass eClass, String name, String body) {
		EAnnotation names = EcoreFactory.eINSTANCE.createEAnnotation();
		names.setSource(OclDelegates.ECORE_URI);
		names.getDetails().put(OclDelegates.CONSTRAINTS_KEY, name);
		eClass.getEAnnotations().add(names);

		EAnnotation bodies = EcoreFactory.eINSTANCE.createEAnnotation();
		bodies.setSource(OclDelegates.DELEGATE_URI);
		bodies.getDetails().put(name, body);
		eClass.getEAnnotations().add(bodies);
	}
}
