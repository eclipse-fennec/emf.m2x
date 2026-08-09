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

import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.annotation.require.RequireOCL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.Property;
import org.osgi.test.common.annotation.Property.Scalar;
import org.osgi.test.common.annotation.config.WithFactoryConfiguration;
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * OSGi integration tests for {@link OclOperationProvider} wiring via
 * {@code @Reference} in {@code OclEngineComponent}.
 *
 * <p>Verifies that:
 * <ul>
 *   <li>An engine with no provider registered has no custom operations</li>
 *   <li>A custom provider with {@code customOperationsEnabled=true} makes operations available</li>
 *   <li>A custom provider with {@code customOperationsEnabled=false} (default) blocks operations</li>
 * </ul>
 *
 * <p>Uses {@link TestOclOperationProvider} — a DS component registered with
 * {@code provider.name=testProvider} that provides a {@code testOp()} operation
 * returning {@code 42}.
 */
@RequireOCL
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
class OclOperationProviderOSGiTest {

	// --- Test 1: Default engine has NoOpProvider → no custom ops ---

	@Test
	void oclEngine_defaultProvider_noCustomOps(
			@InjectService ServiceAware<OclEngine> engineAware) throws OclParseException {

		OclEngine engine = engineAware.getService();
		assertNotNull(engine);

		// 'self.testOp()' is not a standard OCL operation → should produce OclInvalid
		Object result = engine.evaluate("self.testOp()",
				OclContext.of(EcoreFactory.eINSTANCE.createEClass()));
		assertInstanceOf(OclInvalid.class, result);
	}

	// --- Test 2: Custom provider + gate enabled → operation available ---

	@WithFactoryConfiguration(factoryPid = "DefaultOclEngine", location = "?", name = "custom-ops-enabled", properties = {
			@Property(key = "ocl.customOperationsEnabled", scalar = Scalar.Boolean, value = "true"),
			@Property(key = "operationProvider.target", value = "(provider.name=testProvider)"),
			@Property(key = "engine.tag", value = "custom-ops-eng")
	})
	@Test
	void oclEngine_customProvider_withGateEnabled_operationAvailable(
			@InjectService(filter = "(engine.tag=custom-ops-eng)", timeout = 5000) ServiceAware<OclEngine> engineAware) throws OclParseException {

		OclEngine engine = engineAware.getService();
		assertNotNull(engine);

		Object result = engine.evaluate("self.testOp()",
				OclContext.of(EcoreFactory.eINSTANCE.createEClass()));
		assertEquals(42, result);
	}

	// --- Test 2b: Several providers coexist ---

	@WithFactoryConfiguration(factoryPid = "DefaultOclEngine", location = "?", name = "two-providers", properties = {
			@Property(key = "ocl.customOperationsEnabled", scalar = Scalar.Boolean, value = "true"),
			@Property(key = "engine.tag", value = "two-providers-eng")
	})
	@Test
	void oclEngine_severalProviders_allOperationsAvailable(
			@InjectService(filter = "(engine.tag=two-providers-eng)", timeout = 5000) ServiceAware<OclEngine> engineAware)
			throws OclParseException {
		// No operationProvider.target here: every registered provider is bound. While the
		// engine took exactly one, these two could not both be reached — which is the
		// situation a real runtime is in as soon as the MOFM2T standard library and any set
		// of domain operations are both wanted. The OCL specification has no notion of one
		// source of additional operations: §11.8.1 says the standard library is extensible,
		// and a document may carry any number of def: declarations.
		OclEngine engine = engineAware.getService();
		assertNotNull(engine);

		assertEquals(42, engine.evaluate("self.testOp()",
				OclContext.of(EcoreFactory.eINSTANCE.createEClass())));
		assertEquals(7, engine.evaluate("self.secondOp()",
				OclContext.of(EcoreFactory.eINSTANCE.createEClass())));
	}

	// --- Test 3: Custom provider + gate disabled (default) → operation not available ---

	@WithFactoryConfiguration(factoryPid = "DefaultOclEngine", location = "?", name = "custom-ops-disabled", properties = {
			@Property(key = "operationProvider.target", value = "(provider.name=testProvider)"),
			@Property(key = "engine.tag", value = "gate-disabled-eng")
	})
	@Test
	void oclEngine_customProvider_withGateDisabled_operationNotAvailable(
			@InjectService(filter = "(engine.tag=gate-disabled-eng)", timeout = 5000) ServiceAware<OclEngine> engineAware) throws OclParseException {

		OclEngine engine = engineAware.getService();
		assertNotNull(engine);

		// customOperationsEnabled defaults to false → operation not resolved
		Object result = engine.evaluate("self.testOp()",
				OclContext.of(EcoreFactory.eINSTANCE.createEClass()));
		assertInstanceOf(OclInvalid.class, result);
	}
}
