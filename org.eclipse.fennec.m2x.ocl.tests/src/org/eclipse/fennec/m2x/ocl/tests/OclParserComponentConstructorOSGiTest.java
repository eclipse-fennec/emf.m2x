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
package org.eclipse.fennec.m2x.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.fennec.m2x.ocl.api.OclExpressionParser;
import org.eclipse.fennec.m2x.ocl.api.annotation.require.RequireOCL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.runtime.ServiceComponentRuntime;
import org.osgi.service.component.runtime.dto.ComponentConfigurationDTO;
import org.osgi.service.component.runtime.dto.ComponentDescriptionDTO;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * SCR selects a constructor by parameter count, and {@link Class#getConstructors()} has no defined
 * order, so a second public constructor of the same arity on the component class leaves the choice
 * to the JVM (#437).
 */
@RequireOCL
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
class OclParserComponentConstructorOSGiTest {

	private static final String COMPONENT = "org.eclipse.fennec.m2x.ocl.parser.OclParserSupport";

	@Test
	@DisplayName("the component class has exactly one public constructor at the init arity")
	void constructorChoiceIsUnambiguous(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000) ServiceComponentRuntime scr) throws Exception {
		ComponentDescriptionDTO description = description(scr);
		Class<?> implementation = context.getBundle(description.bundle.id)
				.loadClass(description.implementationClass);

		List<Constructor<?>> candidates = Arrays.stream(implementation.getConstructors())
				.filter(constructor -> constructor.getParameterCount() == description.init)
				.toList();

		assertEquals(1, candidates.size(),
				() -> description.implementationClass + " must leave SCR a single candidate at init="
						+ description.init + ", found: " + candidates);
	}

	@Test
	@DisplayName("the parser component activates and provides OclExpressionParser")
	void componentActivates(@InjectService(timeout = 5000) ServiceComponentRuntime scr,
			@InjectService(timeout = 5000, filter = "(parser.type=DEFAULT)") OclExpressionParser parser) {
		assertNotNull(parser);

		Collection<ComponentConfigurationDTO> configurations =
				scr.getComponentConfigurationDTOs(description(scr));
		assertFalse(configurations.isEmpty(), COMPONENT + " has no component configuration");
		for (ComponentConfigurationDTO configuration : configurations) {
			assertNull(configuration.failure, COMPONENT + " failed to activate");
			assertEquals(0, configuration.unsatisfiedReferences.length,
					() -> COMPONENT + " has unsatisfied references: "
							+ Arrays.toString(configuration.unsatisfiedReferences));
			assertEquals(ComponentConfigurationDTO.ACTIVE, configuration.state,
					COMPONENT + " is not active although its service was obtained");
		}
	}

	private static ComponentDescriptionDTO description(ServiceComponentRuntime scr) {
		return scr.getComponentDescriptionDTOs().stream()
				.filter(candidate -> COMPONENT.equals(candidate.name))
				.findFirst()
				.orElseThrow(() -> new AssertionError("component " + COMPONENT + " is not there"));
	}
}
