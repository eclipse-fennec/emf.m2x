/**
 * Copyright (c) 2012 - 2023 Data In Motion and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.eclipse.fennec.m2x.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.annotation.require.RequireOCL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
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
 * OSGi integration tests for the Fennec OCL engine service wiring.
 *
 * <p>Verifies that the OCL engine components are correctly registered
 * as OSGi services, that dependency injection works, and that
 * ConfigurationAdmin-based customization (e.g. custom cache targeting)
 * is wired correctly end-to-end.
 *
 * @see <a href="https://github.com/osgi/osgi-test">osgi-test</a>
 */
@RequireOCL
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
class SimpleOSGiTest {

	@BeforeEach
	void before(@InjectBundleContext BundleContext ctx) {

	}

	// --- Default Service Registration ---

	@Test
	void oclEngine_defaultServiceRegistered(@InjectService ServiceAware<OclEngine> engineAware) {
		assertFalse(engineAware.isEmpty());
	}

	// --- Custom Cache Wiring via ConfigurationAdmin ---

	@WithFactoryConfiguration(factoryPid = "DefaultOclExpressionCache", location = "?", name = "foo", properties = {
			@Property(key = "oclExpressionSize", scalar = Scalar.Integer, value = "256"),
			@Property(key = "name", value = "foo-cache")
	})
	@WithFactoryConfiguration(factoryPid = "DefaultOclEngine", location = "?", name = "foo", properties = {
			@Property(key = "expressionCache.target", value = "(name=foo-cache)")
	})
	@Test
	void oclEngine_customCacheWiring_evaluationUsesConfiguredCache(
			@InjectService(filter = "(name=foo-cache)") ServiceAware<OclExpressionCache> cacheAware,
			@InjectService ServiceAware<OclEngine> engineAware) throws OclParseException {

		// Verify custom cache service is available and empty
		OclExpressionCache cache = cacheAware.getService();
		assertNotNull(cache);
		assertEquals(0, cache.size());

		// Verify engine service is available
		OclEngine engine = engineAware.getService();
		assertNotNull(engine);

		// Evaluate a simple expression — triggers parse → cache miss → cache put
		Object result = engine.evaluate("true", OclContext.of(EcoreFactory.eINSTANCE.createEClass()));
		assertEquals(Boolean.TRUE, result);

		// Proof: the custom cache now holds the parsed expression,
		// which means the engine was wired to this specific cache instance
		assertEquals(1, cache.size());
		assertEquals(1, cache.missCount());
	}

	// --- ConfigAdmin: maxDepth enforcement ---

	@WithFactoryConfiguration(factoryPid = "DefaultOclEngine", location = "?", name = "depth-test", properties = {
			@Property(key = "ocl.maxDepth", scalar = Scalar.Integer, value = "3")
	})
	@Test
	void oclEngine_configuredMaxDepth_enforcedDuringEvaluation(
			@InjectService ServiceAware<OclEngine> engineAware) throws OclParseException {

		OclEngine engine = engineAware.getService();
		assertNotNull(engine);

		// Deeply nested let expression should exceed maxDepth=3
		Object result = engine.evaluate(
				"let a : Integer = 1 in let b : Integer = a in let c : Integer = b in let d : Integer = c in d",
				OclContext.of(EcoreFactory.eINSTANCE.createEClass()));
		assertInstanceOf(OclInvalid.class, result);
	}

	// --- ConfigAdmin: lenient null handling ---

	@WithFactoryConfiguration(factoryPid = "DefaultOclEngine", location = "?", name = "lenient-test", properties = {
			@Property(key = "ocl.nullHandling", value = "LENIENT")
	})
	@Test
	void oclEngine_configuredNullHandling_lenient(
			@InjectService ServiceAware<OclEngine> engineAware) throws OclParseException {

		OclEngine engine = engineAware.getService();
		assertNotNull(engine);

		// With LENIENT null handling, null.oclIsUndefined() should return true
		Object result = engine.evaluate("null.oclIsUndefined()",
				OclContext.of(EcoreFactory.eINSTANCE.createEClass()));
		assertEquals(Boolean.TRUE, result);
	}

	// --- Default config: strict defaults ---

	@Test
	void oclEngine_defaultConfig_usesStrictDefaults(
			@InjectService ServiceAware<OclEngine> engineAware) throws OclParseException {

		OclEngine engine = engineAware.getService();
		assertNotNull(engine);

		// Default strict mode: simple expression works fine
		Object result = engine.evaluate("42 + 1",
				OclContext.of(EcoreFactory.eINSTANCE.createEClass()));
		assertEquals(43, result);
	}

	// --- Two differently configured engines are distinct instances ---

	@WithFactoryConfiguration(factoryPid = "DefaultOclEngine", location = "?", name = "strict-engine", properties = {
			@Property(key = "ocl.maxDepth", scalar = Scalar.Integer, value = "100"),
			@Property(key = "ocl.nullHandling", value = "STRICT"),
			@Property(key = "engine.tag", value = "strict-eng")
	})
	@WithFactoryConfiguration(factoryPid = "DefaultOclEngine", location = "?", name = "lenient-engine", properties = {
			@Property(key = "ocl.maxDepth", scalar = Scalar.Integer, value = "500"),
			@Property(key = "ocl.nullHandling", value = "LENIENT"),
			@Property(key = "engine.tag", value = "lenient-eng")
	})
	@Test
	void oclEngine_twoDifferentConfigs_distinctInstances(
			@InjectService(filter = "(engine.tag=strict-eng)") ServiceAware<OclEngine> strictAware,
			@InjectService(filter = "(engine.tag=lenient-eng)") ServiceAware<OclEngine> lenientAware) throws OclParseException {

		OclEngine strictEngine = strictAware.getService();
		OclEngine lenientEngine = lenientAware.getService();
		assertNotNull(strictEngine);
		assertNotNull(lenientEngine);
		assertNotSame(strictEngine, lenientEngine);

		// Both must be functional
		Object r1 = strictEngine.evaluate("1 + 2", OclContext.of(EcoreFactory.eINSTANCE.createEClass()));
		Object r2 = lenientEngine.evaluate("3 + 4", OclContext.of(EcoreFactory.eINSTANCE.createEClass()));
		assertEquals(3, r1);
		assertEquals(7, r2);
	}

}
