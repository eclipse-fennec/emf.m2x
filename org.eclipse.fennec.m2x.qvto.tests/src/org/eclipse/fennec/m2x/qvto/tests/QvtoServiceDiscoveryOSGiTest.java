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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.ocl.api.annotation.require.RequireOCL;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.annotation.require.RequireQVTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

	@Test
	@DisplayName("an import resolves to a unit found in the service registry")
	@WithConfiguration(pid = "DefaultQvtoEngine", properties = {
			@Property(key = "qvto.unitResolverEnabled", value = "true", scalar = Scalar.Boolean),
			@Property(key = "qvto.discoverUnitResolvers", value = "true", scalar = Scalar.Boolean)
	})
	void registeredUnitResolves(@InjectService QvtoEngine engine) throws Exception {
		assertTrue(run(engine).isSuccess());
	}

	@Test
	@DisplayName("without the opt-in the same registered unit is out of reach")
	@WithConfiguration(pid = "DefaultQvtoEngine", properties = {
			@Property(key = "qvto.unitResolverEnabled", value = "true", scalar = Scalar.Boolean)
	})
	void withoutDiscoveryTheRegistryIsNotConsulted(@InjectService QvtoEngine engine)
			throws Exception {
		assertFalse(run(engine).isSuccess());
	}

	@Test
	@DisplayName("the allow-list narrows what may be reached, discovery or not")
	@WithConfiguration(pid = "DefaultQvtoEngine", properties = {
			@Property(key = "qvto.unitResolverEnabled", value = "true", scalar = Scalar.Boolean),
			@Property(key = "qvto.discoverUnitResolvers", value = "true", scalar = Scalar.Boolean),
			@Property(key = "qvto.allowedUnitModules", value = "something.else")
	})
	void allowListStillNarrows(@InjectService QvtoEngine engine) throws Exception {
		assertFalse(run(engine).isSuccess());
	}

	private static QvtoExecutionResult run(QvtoEngine engine) throws Exception {
		return engine.execute(engine.parse(TRANSFORMATION, "discovery"),
				QvtoExecutionContext.of(new BasicQvtoModelExtent(), new BasicQvtoModelExtent()));
	}
}
