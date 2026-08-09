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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.eclipse.fennec.m2x.ocl.api.annotation.require.RequireOCL;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.annotation.require.RequireM2T;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceObjects;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.Property;
import org.osgi.test.common.annotation.Property.Scalar;
import org.osgi.test.common.annotation.config.WithConfiguration;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * M2T composes over the {@code OclEngine} service.
 *
 * <p>The component's configuration policy is optional, so a bundle that merely wants to generate text gets a working engine without configuring anything, and configuring one later does not mean registering a different service.
 *
 * <p>Before this, the engine built an OCL engine of its own in its constructor. Under OSGi
 * that meant the engine a consumer had configured — with its cache and its operation
 * providers — was not the one that ran; a second one was created next to it.
 */
@RequireOCL
@RequireM2T
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
class M2tEngineComponentOSGiTest {

	@Test
	@DisplayName("there is an engine without configuring anything")
	void engineIsThereByDefault(@InjectService M2tEngine engine) {
		assertNotNull(engine);
		assertNotNull(engine.getOclEngine(),
				"the engine has to run on the OCL service, not on one it built itself");
	}

	@Test
	@DisplayName("every consumer gets its own engine")
	void prototypeScope(@InjectBundleContext BundleContext context) {
		// Engines carry caches. Handing the same one to unrelated consumers would make
		// one consumer's warm-up and another's memory footprint the same thing.
		//
		// It takes ServiceObjects to see that: a plain getService hands out the one
		// bundle-scoped instance no matter how the component is scoped.
		ServiceObjects<M2tEngine> services =
				context.getServiceObjects(context.getServiceReference(M2tEngine.class));
		M2tEngine first = services.getService();
		M2tEngine second = services.getService();
		try {
			assertNotSame(first, second);
			assertNotSame(first.getOclEngine(), second.getOclEngine(),
					"the OCL engine is bound prototype-required, so it must not be shared either");
		} finally {
			services.ungetService(first);
			services.ungetService(second);
		}
	}

	@Test
	@DisplayName("configuring it does not replace it — the policy is optional")
	@WithConfiguration(pid = "DefaultM2tEngine", properties = {
			@Property(key = "m2t.maxTemplateDepth", value = "64", scalar = Scalar.Integer)
	})
	void configuredEngineIsStillTheEngine(@InjectService M2tEngine engine) {
		assertNotNull(engine);
		assertNotNull(engine.getOclEngine());
	}
}
