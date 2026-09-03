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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.engine.QvtoConfigurationHelper;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngineConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The OSGi configuration of the QVT-O component reaches the configuration the engine is
 * built from — every attribute, and each on its own setting.
 *
 * <p>Several numeric limits that all mean "some maximum" are exactly the shape where one
 * gets wired to the neighbour's setter and nothing notices, so each is given a value of its
 * own here rather than checked as a group.
 */
class QvtoConfigurationHelperTest {

	private static final OclEngine OCL = OclEngines.create(new OclParserSupport());

	@Test
	@DisplayName("the defaults of the OCD are the defaults of the configuration")
	void defaultsMatch() {
		QvtoConfiguration mapped = QvtoConfigurationHelper.from(config(Map.of()), OCL);
		QvtoConfiguration built = QvtoConfiguration.builder(OCL).build();

		assertEquals(built.blackboxEnabled(), mapped.blackboxEnabled());
		assertEquals(built.allowedBlackboxModules(), mapped.allowedBlackboxModules());
		assertEquals(built.unitResolverEnabled(), mapped.unitResolverEnabled());
		assertEquals(built.allowedUnitModules(), mapped.allowedUnitModules());
		assertEquals(built.maxBlackboxLibraries(), mapped.maxBlackboxLibraries());
		assertEquals(built.maxUnitResolvers(), mapped.maxUnitResolvers());
	}

	@Test
	@DisplayName("every attribute lands on its own setting")
	void everyAttributeIsCarriedOver() {
		QvtoConfiguration mapped = QvtoConfigurationHelper.from(config(Map.ofEntries(
				Map.entry("blackboxEnabled", true),
				Map.entry("allowedBlackboxModules", new String[] { "a.b" }),
				Map.entry("unitResolverEnabled", true),
				Map.entry("allowedUnitModules", new String[] { "c.d" }),
				Map.entry("maxBlackboxLibraries", 11),
				Map.entry("maxUnitResolvers", 22)
)), OCL);

		assertTrue(mapped.blackboxEnabled());
		assertEquals(Set.of("a.b"), mapped.allowedBlackboxModules());
		assertTrue(mapped.unitResolverEnabled());
		assertEquals(Set.of("c.d"), mapped.allowedUnitModules());
		assertEquals(11, mapped.maxBlackboxLibraries());
		assertEquals(22, mapped.maxUnitResolvers());
	}

	@Test
	@DisplayName("the engine it was given is the engine in the configuration")
	void theSuppliedEngineIsCarriedOver() {
		assertSame(OCL, QvtoConfigurationHelper.from(config(Map.of()), OCL).oclEngine());
	}

	/**
	 * A {@link QvtoEngineConfiguration} answering with the given overrides, and with the
	 * attribute's declared default everywhere else — the same thing SCR hands the component.
	 */
	@Test
	@DisplayName("the resource set it was given is what resolves metamodels")
	void theSuppliedResourceSetDecidesTheRegistry() {
		// The DS component hands in the emf.osgi resource set (#245). What has to hold is that
		// the configuration resolves through exactly that resource set's registry — not a copy,
		// not the global one — so a metamodel registered for the resource set only is seen.
		ResourceSet resourceSet = new ResourceSetImpl();
		QvtoConfiguration mapped = QvtoConfigurationHelper.from(config(Map.of()), OCL, null, resourceSet);
		assertSame(resourceSet, mapped.resourceSet());
		assertSame(resourceSet.getPackageRegistry(), mapped.packageRegistry());
	}

	@Test
	@DisplayName("without a resource set the plain-Java fallback stays in place")
	void withoutAResourceSetTheGlobalRegistryApplies() {
		QvtoConfiguration mapped = QvtoConfigurationHelper.from(config(Map.of()), OCL, null, null);
		assertNull(mapped.resourceSet());
		assertSame(EPackage.Registry.INSTANCE, mapped.packageRegistry());
	}

	private static QvtoEngineConfiguration config(Map<String, Object> overrides) {
		return (QvtoEngineConfiguration) Proxy.newProxyInstance(
				QvtoEngineConfiguration.class.getClassLoader(),
				new Class<?>[] { QvtoEngineConfiguration.class },
				(proxy, method, args) -> overrides.containsKey(method.getName())
						? overrides.get(method.getName())
						: method.getDefaultValue());
	}
}
