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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.model.ocl.AnyType;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclOperation;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.ocl.api.annotation.require.RequireOCL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.Property;
import org.osgi.test.common.annotation.Property.Scalar;
import org.osgi.test.common.annotation.config.WithFactoryConfiguration;
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * {@link OclOperationProvider}s that come and go while the engine runs (#431).
 *
 * <p>A bundle carrying domain operations is not guaranteed to start before the bundle that
 * starts an engine: start levels, lazy activation and a {@code Configurator} dropping a
 * configuration in later all make the other order ordinary. While the {@code operationProvider}
 * reference was static with the default {@code RELUCTANT} policy option, OSGi Compendium
 * §112.3.6 (Table 112.1) had one answer for that — a target service registered afterwards is
 * <em>ignored</em>, no bind and no reactivation. Every expression calling one of those
 * operations evaluated to {@code invalid}: not an error anybody can see the cause of, just a
 * result that is wrong.
 *
 * <p>The reference is dynamic, so both directions have to hold, and for each provider on its
 * own: one arriving is asked from then on, one leaving takes its operations with it and
 * leaves every other provider alone.
 *
 * <p>These tests hold the engine the way a consumer does — they look the service up again
 * each time, so an engine that was rebuilt would be found rather than a stale instance.
 *
 * @see OclOperationProviderOSGiTest for the providers that are there before the engine
 */
@RequireOCL
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
class OclLateOperationProviderOSGiTest {

	private static final String ARRIVAL_TAG = "late-provider-eng";

	private static final String ARRIVAL = "(engine.tag=" + ARRIVAL_TAG + ")";

	private static final String COMINGS_TAG = "providers-come-and-go-eng";

	private static final String COMINGS = "(engine.tag=" + COMINGS_TAG + ")";

	private static final long TIMEOUT_MS = 5_000;

	@WithFactoryConfiguration(factoryPid = "DefaultOclEngine", location = "?", name = "late-provider", properties = {
			@Property(key = "ocl.customOperationsEnabled", scalar = Scalar.Boolean, value = "true"),
			@Property(key = "engine.tag", value = ARRIVAL_TAG)
	})
	@Test
	@DisplayName("an operation provider registered after the engine is still asked")
	void aProviderArrivingAfterTheEngineIsPickedUp(@InjectBundleContext BundleContext context,
			@InjectService(filter = ARRIVAL, timeout = TIMEOUT_MS) ServiceAware<OclEngine> engineAware)
			throws Exception {

		assertNotNull(engineAware.waitForService(TIMEOUT_MS), "the engine has to be up first");

		// Nothing defines lateOp() yet, so this is what a wrong answer looks like.
		assertInstanceOf(OclInvalid.class, evaluate(context, ARRIVAL, "self.lateOp()"),
				"before the provider is registered the operation does not exist");

		ServiceRegistration<OclOperationProvider> registration = context.registerService(
				OclOperationProvider.class, provider("lateOp", 41), null);
		try {
			assertEquals(41, await(context, ARRIVAL, "self.lateOp()", 41),
					"the engine has to take an operation provider that comes up after it");
		} finally {
			registration.unregister();
		}

		assertEquals(OclInvalid.INSTANCE,
				await(context, ARRIVAL, "self.lateOp()", OclInvalid.INSTANCE),
				"after the provider is unregistered the operation does not exist");
	}

	@WithFactoryConfiguration(factoryPid = "DefaultOclEngine", location = "?", name = "providers-come-and-go", properties = {
			@Property(key = "ocl.customOperationsEnabled", scalar = Scalar.Boolean, value = "true"),
			@Property(key = "engine.tag", value = COMINGS_TAG)
	})
	@Test
	@DisplayName("several providers come and go one at a time, and each is followed on its own")
	void providersBindAndUnbindIndependently(@InjectBundleContext BundleContext context,
			@InjectService(filter = COMINGS, timeout = TIMEOUT_MS) ServiceAware<OclEngine> engineAware)
			throws Exception {

		assertNotNull(engineAware.waitForService(TIMEOUT_MS), "the engine has to be up first");

		// However many providers the runtime already holds — this bundle registers two as DS
		// components — what matters is how the count moves, so the assertions are deltas.
		int atRest = providerCount(context, COMINGS);
		assertInstanceOf(OclInvalid.class, evaluate(context, COMINGS, "self.alphaOp()"));
		assertInstanceOf(OclInvalid.class, evaluate(context, COMINGS, "self.betaOp()"));

		ServiceRegistration<OclOperationProvider> alpha = null;
		ServiceRegistration<OclOperationProvider> beta = null;
		try {
			alpha = context.registerService(OclOperationProvider.class, provider("alphaOp", 1), null);
			beta = context.registerService(OclOperationProvider.class, provider("betaOp", 2), null);

			// Both arrived after the engine, and the second one does not displace the first:
			// a multiple reference binds every matching service, so the engine ends up
			// holding both at once.
			assertEquals(1, await(context, COMINGS, "self.alphaOp()", 1));
			assertEquals(2, await(context, COMINGS, "self.betaOp()", 2));
			assertEquals(atRest + 2, providerCount(context, COMINGS),
					"both providers are reported as active");

			// One leaves. Its operations go with it and nothing else is disturbed — an
			// unbind has to reach exactly the provider that was unregistered, which is what
			// a list the engine edits in place can get wrong.
			alpha.unregister();
			alpha = null;

			assertEquals(OclInvalid.INSTANCE,
					await(context, COMINGS, "self.alphaOp()", OclInvalid.INSTANCE),
					"the operations of a provider that left are gone with it");
			assertEquals(2, evaluate(context, COMINGS, "self.betaOp()"),
					"the provider that stayed answers as before");
			assertEquals(atRest + 1, providerCount(context, COMINGS));

			beta.unregister();
			beta = null;

			assertEquals(OclInvalid.INSTANCE,
					await(context, COMINGS, "self.betaOp()", OclInvalid.INSTANCE),
					"the last provider to leave takes its operations with it too");
			assertEquals(atRest, providerCount(context, COMINGS),
					"the engine is back to the providers it started with");
		} finally {
			unregisterQuietly(alpha);
			unregisterQuietly(beta);
		}
	}

	// --- helpers ---

	/**
	 * Evaluates until the expected value shows up or the timeout runs out, and answers with
	 * whatever came last. Putting a provider into the registry and the engine taking notice
	 * are two different moments, whether the engine is rebuilt or bound to.
	 */
	private static Object await(BundleContext context, String filter, String expression,
			Object expected) throws Exception {
		long deadline = System.currentTimeMillis() + TIMEOUT_MS;
		Object result;
		do {
			result = evaluate(context, filter, expression);
			if (expected.equals(result)) {
				return result;
			}
			Thread.sleep(50);
		} while (System.currentTimeMillis() < deadline);
		return result;
	}

	private static Object evaluate(BundleContext context, String filter, String expression)
			throws Exception {
		return engineOf(context, filter).evaluate(expression,
				OclContext.of(EcoreFactory.eINSTANCE.createEClass()));
	}

	/** How many providers the engine would ask, with the gate held open. */
	private static int providerCount(BundleContext context, String filter) throws Exception {
		return engineOf(context, filter).getOperationProviders(
				OclEvaluationOptions.strict().withCustomOperationsEnabled(true)).size();
	}

	/**
	 * Looks the engine up afresh every time, the way a consumer with a static reference ends
	 * up doing: if the component was rebuilt, the old service is gone and this finds the new
	 * one; if it was not, this is the same instance as before.
	 */
	private static OclEngine engineOf(BundleContext context, String filter) throws Exception {
		Collection<ServiceReference<OclEngine>> references =
				context.getServiceReferences(OclEngine.class, filter);
		assertEquals(1, references.size(), "exactly one engine carries " + filter);
		OclEngine engine = context.getService(references.iterator().next());
		assertNotNull(engine);
		return engine;
	}

	private static OclOperationProvider provider(String operationName, int result) {
		AnyType anyType = OclFactory.eINSTANCE.createAnyType();
		anyType.setName("OclAny");
		PrimitiveType intType = OclFactory.eINSTANCE.createPrimitiveType();
		intType.setName("Integer");
		return () -> List.of(OclOperation.of(operationName, anyType, intType,
				(self, args) -> result));
	}

	private static void unregisterQuietly(ServiceRegistration<?> registration) {
		if (registration != null) {
			registration.unregister();
		}
	}
}
