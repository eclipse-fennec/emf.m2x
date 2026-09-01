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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Optional;
import java.util.Collection;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.ocl.api.annotation.require.RequireOCL;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;
import org.eclipse.fennec.m2x.unit.api.UnitResolutionException;
import org.eclipse.fennec.m2x.qvto.api.annotation.require.RequireQVTO;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.Property;
import org.osgi.test.common.annotation.Property.Scalar;
import org.osgi.test.common.annotation.config.WithConfiguration;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * A transformation reaches a unit that only the service registry knows about.
 *
 * <p>Every {@code @InjectService} here carries a filter naming the configuration the test
 * needs, and that is not decoration. The three configuration tests write the same
 * ConfigAdmin pid with different values; injecting a bare {@code QvtoEngine} waits only for
 * <em>an</em> engine, not for the one this test configured, so a test can be handed the
 * instance the previous test left behind. That is what turned the Java 21 leg red on
 * 2026-09-01: this class's first test ran right after the one that switches discovery off,
 * got its engine, and reported "Cannot resolve import: registered.library" while both
 * resolvers were registered. Because DS publishes configuration properties as service
 * properties, a filter on them waits for the instance that actually carries the setting.
 *
 * <p>{@link RegisteredTestUnitResolver} is a component nothing references. The engine finds
 * it because the name it looks up — taken from the {@code import} in the transformation —
 * matches the {@code qvto.unit.name} it publishes. That is the whole point of resolving at
 * link time rather than binding resolvers up front: a declarative reference would have to
 * bind every resolver in the framework, and a static one would restart the engine, dropping
 * its caches, each time one came or went.
 */
@RequireOCL
@RequireQVTO
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
class QvtoServiceDiscoveryOSGiTest {

	private static final String TRANSFORMATION = """
			modeltype ECORE uses '%s';
			import registered.library;
			transformation discovery(in source : ECORE, out target : ECORE) {
			    main() {
			    }
			}
			""".formatted(EcorePackage.eNS_URI);

	private static final String CLASSPATH_TRANSFORMATION = """
			modeltype ECORE uses '%s';
			import discovered.library;
			transformation classpath(in source : ECORE, out target : ECORE) {
			    main() {
			    }
			}
			""".formatted(EcorePackage.eNS_URI);

	@Test
	@DisplayName("an import resolves to a unit found in the service registry")
	@WithConfiguration(pid = "DefaultQvtoEngine", properties = {
			@Property(key = "qvto.unitResolverEnabled", value = "true", scalar = Scalar.Boolean),
			@Property(key = "qvto.discoverUnitResolvers", value = "true", scalar = Scalar.Boolean)
	})
	void registeredUnitResolves(
			@InjectService(timeout = 5000, filter = "(qvto.discoverUnitResolvers=true)") QvtoEngine engine)
			throws Exception {
		QvtoExecutionResult result = run(engine);
		assertTrue(result.isSuccess(), () -> whyItFailed(engine, result));
	}

	@Test
	@DisplayName("without the opt-in the same registered unit is out of reach")
	@WithConfiguration(pid = "DefaultQvtoEngine", properties = {
			@Property(key = "qvto.unitResolverEnabled", value = "true", scalar = Scalar.Boolean)
	})
	void withoutDiscoveryTheRegistryIsNotConsulted(
			@InjectService(timeout = 5000,
					filter = "(&(qvto.unitResolverEnabled=true)(!(qvto.discoverUnitResolvers=true)))") QvtoEngine engine)
			throws Exception {
		QvtoExecutionResult result = run(engine);
		assertFalse(result.isSuccess(),
				"without the opt-in the registered unit must stay out of reach, but the"
						+ " transformation succeeded");
	}

	@Test
	@DisplayName("the allow-list narrows what may be reached, discovery or not")
	@WithConfiguration(pid = "DefaultQvtoEngine", properties = {
			@Property(key = "qvto.unitResolverEnabled", value = "true", scalar = Scalar.Boolean),
			@Property(key = "qvto.discoverUnitResolvers", value = "true", scalar = Scalar.Boolean),
			@Property(key = "qvto.allowedUnitModules", value = "something.else")
	})
	void allowListStillNarrows(
			@InjectService(timeout = 5000, filter = "(qvto.allowedUnitModules=something.else)") QvtoEngine engine)
			throws Exception {
		QvtoExecutionResult result = run(engine);
		assertFalse(result.isSuccess(),
				"the allow-list names something.else, so registered.library must stay out of"
						+ " reach, but the transformation succeeded");
	}

	@Test
	@DisplayName("a Service Loader Mediator is running, so the isolation is tested against one")
	void mediatorIsInTheRuntime(@InjectBundleContext BundleContext context) {
		// Without this the isolation test below would pass in a framework that has no
		// mediator at all, which proves nothing about mediators. Aries SPI Fly weaves
		// through a WeavingHook, so it has to be ACTIVE, not merely installed.
		Bundle mediator = Arrays.stream(context.getBundles())
				.filter(b -> "org.apache.aries.spifly.dynamic.bundle".equals(b.getSymbolicName()))
				.findFirst()
				.orElseThrow(() -> new AssertionError("SPI Fly is not in this runtime"));

		assertEquals(Bundle.ACTIVE, mediator.getState(),
				() -> "SPI Fly has to be running to weave anything, state was "
						+ mediator.getState());
	}

	@Test
	@DisplayName("the class-path route finds nothing here — one mechanism per environment")
	void serviceLoaderRouteIsInert() throws Exception {
		// DiscoveredTestUnitResolver sits in this bundle's META-INF/services and answers for
		// discovered.library. ServiceLoaderQvtoUnitResolver looks through the class loader of
		// QvtoUnitResolver, which is the api bundle's and cannot see into this one — so the
		// class-path route is inert under OSGi by construction, and the service route is the
		// only one that answers. Two mechanisms racing for the same import is exactly what a
		// Service Loader Mediator such as Aries SPI Fly would create, which is why this
		// bundle declares no osgi.serviceloader requirement.
		QvtoEngine engine = QvtoEngines.create(QvtoConfiguration.builder(
				OclEngines.create(new OclParserSupport()))
				.unitResolverEnabled(true)
				.discoverUnitResolvers(true)
				.build());

		QvtoExecutionResult result = engine.execute(
				engine.parse(CLASSPATH_TRANSFORMATION, "classpath"),
				QvtoExecutionContext.of(new BasicQvtoModelExtent(), new BasicQvtoModelExtent()));

		assertFalse(result.isSuccess());
	}

	@Test
	@DisplayName("the engine bundle asks no Service Loader Mediator to weave it")
	void noServiceLoaderRequirement() {
		// A mediator such as Aries SPI Fly only processes bundles that declare an
		// osgi.serviceloader requirement, and weaving ServiceLoader.load would feed the
		// class-path route from the very service registry the other route already uses.
		// SPI Fly is not in this runtime, so the guard is the manifest rather than a
		// behavioural test — and the manifest is what would change, by someone adding a
		// @ServiceConsumer annotation to the engine bundle.
		Bundle engineBundle = FrameworkUtil.getBundle(QvtoEngines.class);
		String requirements = engineBundle.getHeaders().get(Constants.REQUIRE_CAPABILITY);

		assertFalse(requirements != null && requirements.contains("osgi.serviceloader"),
				() -> "Require-Capability: " + requirements);
	}

	@Test
	@DisplayName("with several services for one name, the highest ranking wins (#141)")
	void higherRankingWins(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(qvto.resolver.kind=discovery)") QvtoUnitResolver discovery) {
		String text = "library ranked.library;\n";
		ServiceRegistration<QvtoUnitResolver> low = register(context, "ranked.library", 1, "mem:/low", text);
		ServiceRegistration<QvtoUnitResolver> high = register(context, "ranked.library", 10, "mem:/high", text);
		try {
			QvtoUnit unit = discovery.resolveUnit("ranked.library").orElseThrow();
			assertEquals(URI.createURI("mem:/high"), ((QvtoUnit.SourceUnit) unit).uri(),
					"same content from both, the one with the higher ranking is returned");
		} finally {
			low.unregister();
			high.unregister();
		}
	}

	@Test
	@DisplayName("two services with different content for one name are a conflict, not a choice (#141)")
	void disagreeingServicesAreAConflict(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(qvto.resolver.kind=discovery)") QvtoUnitResolver discovery) {
		ServiceRegistration<QvtoUnitResolver> one = register(context, "contested.library", 1, "mem:/one",
				"library contested.library;\n");
		ServiceRegistration<QvtoUnitResolver> two = register(context, "contested.library", 10, "mem:/two",
				"library contested.library;\nhelper h() : String { return 'x'; }\n");
		try {
			UnitResolutionException failure = assertThrows(UnitResolutionException.class,
					() -> discovery.resolveUnit("contested.library"));
			assertTrue(failure.getMessage().contains("disagree"), failure.getMessage());
		} finally {
			one.unregister();
			two.unregister();
		}
	}

	@Test
	@DisplayName("a failing service is an error the caller sees, not 'not found' (#141)")
	void failingServiceIsAnError(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(qvto.resolver.kind=discovery)") QvtoUnitResolver discovery) {
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put("qvto.unit.name", "broken.library");
		QvtoUnitResolver broken = name -> {
			throw new IllegalStateException("disk on fire");
		};
		ServiceRegistration<QvtoUnitResolver> registration = context.registerService(QvtoUnitResolver.class, broken, properties);
		try {
			UnitResolutionException failure = assertThrows(UnitResolutionException.class,
					() -> discovery.resolveUnit("broken.library"));
			assertTrue(failure.getMessage().contains("disk on fire"), failure.getMessage());
		} finally {
			registration.unregister();
		}
	}

	private static ServiceRegistration<QvtoUnitResolver> register(BundleContext context, String unit, int ranking,
			String uri, String text) {
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put("qvto.unit.name", unit);
		properties.put(Constants.SERVICE_RANKING, ranking);
		QvtoUnitResolver resolver = name -> unit.equals(name)
				? Optional.of(new QvtoUnit.SourceUnit(name, URI.createURI(uri), text))
				: Optional.empty();
		return context.registerService(QvtoUnitResolver.class, resolver, properties);
	}

	private static QvtoExecutionResult run(QvtoEngine engine) throws Exception {
		return engine.execute(engine.parse(TRANSFORMATION, "discovery"),
				QvtoExecutionContext.of(new BasicQvtoModelExtent(), new BasicQvtoModelExtent()));
	}

	/**
	 * What to print when the import did not resolve.
	 *
	 * <p>A bare {@code assertTrue} says only "expected true but was false", which is exactly
	 * nothing when the failure happens on a CI runner and not here: whether the engine never
	 * saw the configuration, whether the resolver service was absent, or whether the import
	 * failed for a third reason all look the same. All three are printed.
	 */
	private static String whyItFailed(QvtoEngine engine, QvtoExecutionResult result) {
		BundleContext context = FrameworkUtil.getBundle(QvtoServiceDiscoveryOSGiTest.class)
				.getBundleContext();
		String resolvers;
		try {
			Collection<ServiceReference<QvtoUnitResolver>> references =
					context.getServiceReferences(QvtoUnitResolver.class, null);
			resolvers = references.stream()
					.map(reference -> reference.getBundle().getSymbolicName() + " "
							+ reference.getProperty("qvto.unit.name"))
					.collect(Collectors.joining(", ", "[", "]"));
		} catch (InvalidSyntaxException e) {
			resolvers = "<could not be listed: " + e.getMessage() + ">";
		}
		return "the import did not resolve."
				+ "\n  engine:      " + engine
				+ "\n  resolvers:   " + resolvers
				+ "\n  diagnostics: " + result.diagnostics();
	}
}
