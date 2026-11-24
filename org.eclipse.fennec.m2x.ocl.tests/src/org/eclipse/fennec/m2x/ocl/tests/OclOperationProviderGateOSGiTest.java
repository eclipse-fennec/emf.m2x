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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

import org.eclipse.fennec.m2x.model.ocl.AnyType;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclOperation;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.ocl.api.annotation.require.RequireOCL;
import org.eclipse.fennec.m2x.ocl.engine.OclOperationProviderProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.runtime.ServiceComponentRuntime;
import org.osgi.service.component.runtime.dto.ComponentConfigurationDTO;
import org.osgi.service.component.runtime.dto.ComponentDescriptionDTO;
import org.osgi.service.component.runtime.dto.ReferenceDTO;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.Property;
import org.osgi.test.common.annotation.config.WithFactoryConfiguration;
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * The readiness gate of #431: {@link OclOperationProviderProvider} as a target for an engine
 * that must not be handed out before the operation providers it needs are registered.
 *
 * <p>A multiple reference is satisfied by nothing at all, so {@code operationProvider.target}
 * decides which providers bind but never holds the engine back — a consumer injecting
 * {@code OclEngine} can get one whose provider has not arrived, and every expression calling
 * that provider's operations evaluates to {@code invalid}.
 *
 * <p>The gate closes that window. It selects nothing:
 * {@link OclOperationProviderOSGiTest} covers what {@code operationProvider.target} binds,
 * and {@link OclLateOperationProviderOSGiTest} what happens when a provider arrives anyway.
 */
@RequireOCL
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
class OclOperationProviderGateOSGiTest {

	private static final String ENGINE_COMPONENT = "DefaultOclEngine";

	private static final String GATE_REFERENCE = "operationProvidersReady";

	private static final long TIMEOUT_MS = 5_000;

	@Test
	@DisplayName("the gate is a mandatory static reference — an optional one would not hold anything back")
	void theGateIsMandatoryAndStatic(@InjectService(timeout = TIMEOUT_MS) ServiceComponentRuntime scr) {
		ReferenceDTO gate = Arrays.stream(engineDescription(scr).references)
				.filter(candidate -> GATE_REFERENCE.equals(candidate.name))
				.findFirst()
				.orElseThrow(() -> new AssertionError("no reference named " + GATE_REFERENCE
						+ " in " + ENGINE_COMPONENT));

		assertEquals(OclOperationProviderProvider.class.getName(), gate.interfaceName);
		assertEquals("1..1", gate.cardinality, "an optional reference would never hold the engine back");
		assertEquals("static", gate.policy, "a dynamic reference binds late instead of waiting");
	}

	@Test
	@DisplayName("the gate names every registered operation provider, and follows them in and out")
	void theGateNamesTheRegisteredProviders(@InjectBundleContext BundleContext context,
			@InjectService(timeout = TIMEOUT_MS) OclOperationProviderProvider gate) throws Exception {

		// Two providers are DS components of this bundle, so they are there from the start.
		assertTrue(gate.getOperationProviderNames().containsAll(List.of("testProvider", "secondProvider")),
				"the providers registered as components have to be named: "
						+ gate.getOperationProviderNames());

		ServiceRegistration<OclOperationProvider> registration = registerProvider(context, "gatedProvider");
		try {
			assertTrue(await(() -> gate.getOperationProviderNames().contains("gatedProvider")),
					"a provider registered later has to show up in the names");
		} finally {
			registration.unregister();
		}

		assertTrue(await(() -> !gate.getOperationProviderNames().contains("gatedProvider")),
				"a provider that went away has to drop out of the names again");
	}

	@WithFactoryConfiguration(factoryPid = ENGINE_COMPONENT, location = "?", name = "gated-on-absent", properties = {
			@Property(key = "operationProvidersReady.target", value = "(operationProviderNames=notRegisteredAnywhere)"),
			@Property(key = "engine.tag", value = "gated-absent-eng")
	})
	@Test
	@DisplayName("an engine gated on a provider that is not there is not handed out")
	void anEngineGatedOnAnAbsentProviderStaysUnsatisfied(
			@InjectBundleContext BundleContext context,
			@InjectService(timeout = TIMEOUT_MS) ServiceComponentRuntime scr) throws Exception {

		// Asserted on the component configuration rather than on the absence of a service:
		// nothing can wait deterministically for something that never appears.
		assertTrue(await(() -> configurationOf(scr, "gated-absent-eng")
				.filter(dto -> dto.state == ComponentConfigurationDTO.UNSATISFIED_REFERENCE)
				.isPresent()),
				"the engine has to stay unsatisfied: " + configurationOf(scr, "gated-absent-eng")
						.map(dto -> "state " + dto.state).orElse("no component configuration at all"));

		assertTrue(context.getServiceReferences(OclEngine.class, "(engine.tag=gated-absent-eng)").isEmpty(),
				"no engine service while the gate is unsatisfied");
	}

	@WithFactoryConfiguration(factoryPid = ENGINE_COMPONENT, location = "?", name = "gated-on-late", properties = {
			@Property(key = "operationProvidersReady.target", value = "(operationProviderNames=lateGatedProvider)"),
			@Property(key = "engine.tag", value = "gated-late-eng")
	})
	@Test
	@DisplayName("the engine appears once the provider it waits for is registered")
	void anEngineAppearsWhenTheGateIsSatisfied(@InjectBundleContext BundleContext context,
			@InjectService(timeout = TIMEOUT_MS) ServiceComponentRuntime scr) throws Exception {

		// Not an injected ServiceAware: that resolves the service when the parameter is bound,
		// and the whole point here is that the service is not there yet.
		assertTrue(await(() -> configurationOf(scr, "gated-late-eng")
				.filter(dto -> dto.state == ComponentConfigurationDTO.UNSATISFIED_REFERENCE)
				.isPresent()),
				"control: the engine waits before the provider is registered");

		ServiceRegistration<OclOperationProvider> registration =
				registerProvider(context, "lateGatedProvider");
		try {
			assertTrue(await(() -> engineIsRegistered(context, "gated-late-eng")),
					"registering the awaited provider has to let the engine through");
		} finally {
			registration.unregister();
		}

		assertTrue(await(() -> !engineIsRegistered(context, "gated-late-eng")),
				"and withdrawing it takes the engine away again");
	}

	@WithFactoryConfiguration(factoryPid = ENGINE_COMPONENT, location = "?", name = "gated-on-both", properties = {
			@Property(key = "operationProvidersReady.target",
					value = "(&(operationProviderNames=testProvider)(operationProviderNames=secondProvider))"),
			@Property(key = "engine.tag", value = "gated-both-eng")
	})
	@Test
	@DisplayName("one filter can require several providers at once — what cardinality cannot express")
	void theGateCanRequireSeveralProvidersAtOnce(
			@InjectService(filter = "(engine.tag=gated-both-eng)", timeout = TIMEOUT_MS)
					ServiceAware<OclEngine> engineAware) throws Exception {

		// A reference with cardinality 1..n and target (|(provider.name=A)(provider.name=B))
		// is satisfied by either one. Conjunction over the names of a single service is the
		// only way to say "both".
		assertNotNull(engineAware.waitForService(TIMEOUT_MS),
				"both providers are registered, so the conjunction is satisfied");
	}

	// --- helpers ---

	private static ComponentDescriptionDTO engineDescription(ServiceComponentRuntime scr) {
		return scr.getComponentDescriptionDTOs().stream()
				.filter(candidate -> ENGINE_COMPONENT.equals(candidate.name))
				.findFirst()
				.orElseThrow(() -> new AssertionError("component " + ENGINE_COMPONENT + " is not there"));
	}

	private static Optional<ComponentConfigurationDTO> configurationOf(ServiceComponentRuntime scr, String tag) {
		return scr.getComponentConfigurationDTOs(engineDescription(scr)).stream()
				.filter(dto -> tag.equals(dto.properties.get("engine.tag")))
				.findFirst();
	}

	private static boolean engineIsRegistered(BundleContext context, String tag) {
		try {
			return !context.getServiceReferences(OclEngine.class, "(engine.tag=" + tag + ")").isEmpty();
		} catch (InvalidSyntaxException e) {
			throw new AssertionError(e);
		}
	}

	private static ServiceRegistration<OclOperationProvider> registerProvider(
			BundleContext context, String name) {
		Hashtable<String, Object> properties = new Hashtable<>();
		properties.put("provider.name", name);
		return context.registerService(OclOperationProvider.class, provider(name + "Op"), properties);
	}

	private static OclOperationProvider provider(String operationName) {
		AnyType anyType = OclFactory.eINSTANCE.createAnyType();
		anyType.setName("OclAny");
		PrimitiveType intType = OclFactory.eINSTANCE.createPrimitiveType();
		intType.setName("Integer");
		return () -> List.of(OclOperation.of(operationName, anyType, intType, (self, args) -> 1));
	}

	/**
	 * SCR reacts to a registration asynchronously, so a condition is polled rather than read
	 * once.
	 */
	private static boolean await(java.util.function.BooleanSupplier condition) throws Exception {
		long deadline = System.currentTimeMillis() + TIMEOUT_MS;
		do {
			if (condition.getAsBoolean()) {
				return true;
			}
			Thread.sleep(50);
		} while (System.currentTimeMillis() < deadline);
		return false;
	}
}
