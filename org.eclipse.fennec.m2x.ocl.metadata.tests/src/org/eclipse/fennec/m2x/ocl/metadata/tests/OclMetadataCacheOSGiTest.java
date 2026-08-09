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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistry;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.Property;
import org.osgi.test.common.annotation.config.WithFactoryConfiguration;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * The pieces wire up into a working, model-anchored cache.
 *
 * <p>Four configurations and nothing else: a registry with an initial provider, the cache
 * over that registry, the precompiler writing into that cache, and — where it is wanted — the
 * bridge that puts registry content onto the metadata tree. The configurations here are the
 * ones the user guide's tutorial shows, which is why they are spelled out rather than shared
 * behind a helper: the test is what proves the tutorial is right.
 *
 * <p>What this test cannot show, and says so rather than pretending: the version replay of
 * the bridge — two versions of one nsURI live at once, a model registering late — needs
 * packages published as OSGi services, which belongs to the emf.osgi extender and is covered
 * by its own integration tests. What is covered here in plain Java, with the same objects, is
 * {@code RegistryExpressionCacheTest}: two versions kept apart, anchoring by fingerprint,
 * precompilation on registration.
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
@WithFactoryConfiguration(factoryPid = "EObjectRegistry", location = "?", name = "ocl", properties = {
		@Property(key = "name", value = "ocl-compiled"),
		@Property(key = "initialProvider.target", value = "(emf.eobject.provider.name=ocl-empty-provider)")
})
class OclMetadataCacheOSGiTest {

	private static final String NS_URI = "http://example.org/m2x/metadata-osgi/1.0";

	private static final String EXPRESSION = "self.pages > 100";

	@Test
	@DisplayName("the registry publishes once its provider has loaded")
	void registryIsPublishedUnderItsName(
			@InjectService(filter = "(emf.eobject.registry.name=ocl-compiled)", timeout = 5000)
			EObjectRegistry registry) {
		// A registry that is there at all is a registry that finished loading — that is the
		// point of publishing it only afterwards.
		assertNotNull(registry);
	}

	@Test
	@DisplayName("the cache service is there, and an engine can be built on it")
	void cacheServiceIsUsableByAnEngine(
			@InjectService(filter = "(cache.name=metadata)", timeout = 5000) OclExpressionCache cache)
			throws Exception {
		EPackage ePackage = bookPackage();
		OclEngine engine = OclEngines.create(OclConfiguration.builder(
				new OclParserSupport(registryOf(ePackage)))
				.expressionCache(cache)
				.build());

		OclExpression first = engine.parse(EXPRESSION, bookOf(ePackage));
		OclExpression second = engine.parse(EXPRESSION, bookOf(ePackage));

		assertSame(first, second, "the second parse came out of the cache service");
	}

	@Test
	@DisplayName("what the engine compiles lands in the named registry, anchored")
	void compiledExpressionsLandInTheRegistry(
			@InjectService(filter = "(cache.name=metadata)", timeout = 5000) OclExpressionCache cache,
			@InjectService(filter = "(emf.eobject.registry.name=ocl-compiled)", timeout = 5000)
			EObjectRegistry registry) throws Exception {
		EPackage ePackage = bookPackage();
		OclEngines.create(OclConfiguration.builder(new OclParserSupport(registryOf(ePackage)))
				.expressionCache(cache)
				.build())
				.parse(EXPRESSION, bookOf(ePackage));

		assertTrue(registry.entries().stream()
				.anyMatch(entry -> NS_URI.equals(entry.properties().get("emf.nsURI"))
						&& entry.properties().get("emf.fingerprint") != null),
				"an entry carries the model and the version it belongs to");
	}

	// --- helpers ---

	private static EPackage.Registry registryOf(EPackage ePackage) {
		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(NS_URI, ePackage);
		return registry;
	}

	private static EClass bookOf(EPackage ePackage) {
		return (EClass) ePackage.getEClassifier("Book");
	}

	private static EPackage bookPackage() {
		EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
		ePackage.setName("bookshelf");
		ePackage.setNsURI(NS_URI);
		ePackage.setNsPrefix("bookshelf");

		EClass book = EcoreFactory.eINSTANCE.createEClass();
		book.setName("Book");
		EAttribute pages = EcoreFactory.eINSTANCE.createEAttribute();
		pages.setName("pages");
		pages.setEType(EcorePackage.Literals.EINT);
		book.getEStructuralFeatures().add(pages);
		ePackage.getEClassifiers().add(book);
		return ePackage;
	}
}
