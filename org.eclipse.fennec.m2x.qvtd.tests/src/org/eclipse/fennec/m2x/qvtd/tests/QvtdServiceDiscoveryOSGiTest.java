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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.fennec.m2x.ocl.api.annotation.require.RequireOCL;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnit;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnitResolver;
import org.eclipse.fennec.m2x.qvtd.api.annotation.require.RequireQVTD;
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
 * The QVT-R whiteboard resolver (#176).
 *
 * <p>It had no OSGi test at all, while it is where two fixes live that only show up in a
 * framework: the ranking of #141 — which of several services answers for one name — and the
 * filter escaping of #182, where a unit name taken from a transformation is put into an LDAP
 * filter. The QVT-O twin of this test is `QvtoServiceDiscoveryOSGiTest`.
 */
@RequireOCL
@RequireQVTD
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
class QvtdServiceDiscoveryOSGiTest {

	private static final String LIBRARY = """
			transformation ranked(source : ecore, target : ecore) {
			}
			""";

	@Test
	@DisplayName("with several services for one name, the highest ranking wins (#141)")
	void higherRankingWins(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(qvtd.resolver.kind=discovery)")
			QvtdUnitResolver discovery) {
		ServiceRegistration<QvtdUnitResolver> low =
				register(context, "ranked.library", 1, "mem:/low", LIBRARY);
		ServiceRegistration<QvtdUnitResolver> high =
				register(context, "ranked.library", 10, "mem:/high", LIBRARY);
		try {
			QvtdUnit unit = discovery.resolveUnit("ranked.library").orElseThrow();

			assertEquals(URI.createURI("mem:/high"), ((QvtdUnit.SourceUnit) unit).uri(),
					"same content from both, so the higher ranking decides");
		} finally {
			low.unregister();
			high.unregister();
		}
	}

	@Test
	@DisplayName("two services with different content for one name are a conflict, not a choice (#141)")
	void disagreeingServicesAreAConflict(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(qvtd.resolver.kind=discovery)")
			QvtdUnitResolver discovery) {
		ServiceRegistration<QvtdUnitResolver> one =
				register(context, "contested.library", 1, "mem:/one", LIBRARY);
		ServiceRegistration<QvtdUnitResolver> two = register(context, "contested.library", 10,
				"mem:/two", LIBRARY.replace("transformation ranked", "transformation other"));
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
			@InjectService(timeout = 5000, filter = "(qvtd.resolver.kind=discovery)")
			QvtdUnitResolver discovery) {
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put("qvtd.unit.name", "broken.library");
		QvtdUnitResolver broken = name -> {
			throw new IllegalStateException("disk on fire");
		};
		ServiceRegistration<QvtdUnitResolver> registration =
				context.registerService(QvtdUnitResolver.class, broken, properties);
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
			@InjectService(timeout = 5000, filter = "(qvtd.resolver.kind=discovery)")
			QvtdUnitResolver discovery) {
		// The name comes out of a transformation's import, so it can be anything the grammar
		// allows. Unescaped, this one turns a lookup for one unit into a lookup for every unit —
		// and it is well-formed, so the InvalidSyntaxException catch never sees it.
		ServiceRegistration<QvtdUnitResolver> secret =
				register(context, "secret.library", 1, "mem:/secret", LIBRARY);
		try {
			Optional<QvtdUnit> found = discovery.resolveUnit("foo)(qvtd.unit.name=*");

			assertTrue(found.isEmpty(),
					() -> "no unit is registered under that name: " + found);
		} finally {
			secret.unregister();
		}
	}

	private static ServiceRegistration<QvtdUnitResolver> register(BundleContext context,
			String unit, int ranking, String uri, String text) {
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put("qvtd.unit.name", unit);
		properties.put(Constants.SERVICE_RANKING, ranking);
		QvtdUnitResolver resolver = name -> unit.equals(name)
				? Optional.of(new QvtdUnit.SourceUnit(unit, URI.createURI(uri), text))
				: Optional.empty();
		return context.registerService(QvtdUnitResolver.class, resolver, properties);
	}
}
