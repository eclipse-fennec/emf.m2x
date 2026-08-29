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
package org.eclipse.fennec.m2x.m2t.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.fennec.m2x.ocl.api.annotation.require.RequireOCL;
import org.eclipse.fennec.m2x.m2t.api.M2tUnit;
import org.eclipse.fennec.m2x.m2t.api.M2tUnitResolver;
import org.eclipse.fennec.m2x.m2t.api.annotation.require.RequireM2T;
import org.eclipse.fennec.m2x.unit.api.UnitResolutionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * The MOFM2T whiteboard resolver (#177).
 *
 * <p>It had no OSGi test at all, while it is where two fixes live that only show up in a
 * framework: the ranking of #141 — which of several services answers for one name — and the
 * filter escaping of #182, where a unit name taken from a transformation is put into an LDAP
 * filter. The QVT-O twin of this test is `QvtoServiceDiscoveryOSGiTest`.
 */
@RequireOCL
@RequireM2T
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
class M2tServiceDiscoveryOSGiTest {

	private static final String LIBRARY = """
			[module ranked(_'http://www.eclipse.org/emf/2002/Ecore')/]
			[template public greet(c : EClass)]hello[/template]
			""";

	@Test
	@DisplayName("with several services for one name, the highest ranking wins (#141)")
	void higherRankingWins(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(m2t.resolver.kind=discovery)")
			M2tUnitResolver discovery) {
		ServiceRegistration<M2tUnitResolver> low =
				register(context, "ranked.library", 1, "mem:/low", LIBRARY);
		ServiceRegistration<M2tUnitResolver> high =
				register(context, "ranked.library", 10, "mem:/high", LIBRARY);
		try {
			M2tUnit unit = discovery.resolveUnit("ranked.library").orElseThrow();

			assertEquals(URI.createURI("mem:/high"), ((M2tUnit.SourceUnit) unit).uri(),
					"same content from both, so the higher ranking decides");
		} finally {
			low.unregister();
			high.unregister();
		}
	}

	@Test
	@DisplayName("two services with different content for one name are a conflict, not a choice (#141)")
	void disagreeingServicesAreAConflict(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(m2t.resolver.kind=discovery)")
			M2tUnitResolver discovery) {
		ServiceRegistration<M2tUnitResolver> one =
				register(context, "contested.library", 1, "mem:/one", LIBRARY);
		ServiceRegistration<M2tUnitResolver> two = register(context, "contested.library", 10,
				"mem:/two", LIBRARY.replace("hello", "different"));
		try {
			UnitResolutionException failure = assertThrows(UnitResolutionException.class,
					() -> discovery.resolveUnit("contested.library"));

			assertTrue(failure.getMessage().contains("disagree"), failure::getMessage);
		} finally {
			one.unregister();
			two.unregister();
		}
	}

	@Test
	@DisplayName("a failing service is an error the caller sees, not 'not found' (#141)")
	void failingServiceIsAnError(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(m2t.resolver.kind=discovery)")
			M2tUnitResolver discovery) {
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put("m2t.unit.name", "broken.library");
		M2tUnitResolver broken = name -> {
			throw new IllegalStateException("disk on fire");
		};
		ServiceRegistration<M2tUnitResolver> registration =
				context.registerService(M2tUnitResolver.class, broken, properties);
		try {
			UnitResolutionException failure = assertThrows(UnitResolutionException.class,
					() -> discovery.resolveUnit("broken.library"));

			assertTrue(failure.getMessage().contains("disk on fire"), failure::getMessage);
		} finally {
			registration.unregister();
		}
	}

	@Test
	@DisplayName("a name that reads as a filter finds nothing, rather than everything (#182)")
	void aNameThatLooksLikeAFilterDoesNotWiden(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(m2t.resolver.kind=discovery)")
			M2tUnitResolver discovery) {
		// The name comes out of a transformation's import, so it can be anything the grammar
		// allows. Unescaped, this one turns a lookup for one unit into a lookup for every unit —
		// and it is well-formed, so the InvalidSyntaxException catch never sees it.
		ServiceRegistration<M2tUnitResolver> secret =
				register(context, "secret.library", 1, "mem:/secret", LIBRARY);
		try {
			Optional<M2tUnit> found = discovery.resolveUnit("foo)(m2t.unit.name=*");

			assertTrue(found.isEmpty(),
					() -> "no unit is registered under that name: " + found);
		} finally {
			secret.unregister();
		}
	}

	private static ServiceRegistration<M2tUnitResolver> register(BundleContext context,
			String unit, int ranking, String uri, String text) {
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put("m2t.unit.name", unit);
		properties.put(Constants.SERVICE_RANKING, ranking);
		M2tUnitResolver resolver = name -> unit.equals(name)
				? Optional.of(new M2tUnit.SourceUnit(unit, URI.createURI(uri), text))
				: Optional.empty();
		return context.registerService(M2tUnitResolver.class, resolver, properties);
	}
}
