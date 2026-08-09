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
package org.eclipse.fennec.m2x.m2t.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.WhitespaceMode;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The OSGi configuration of the M2T component reaches the configuration the engine is built
 * from — every attribute, and each on its own setting.
 *
 * <p>Eight numeric limits that all mean "some maximum" is exactly the shape where one gets
 * wired to the neighbour's setter and nothing notices, so each is given a value of its own
 * here rather than checked as a group.
 */
class M2tConfigurationHelperTest {

	private static final OclEngine OCL = OclEngines.create(new OclParserSupport());

	@Test
	@DisplayName("the defaults of the OCD are the defaults of the configuration")
	void defaultsMatch() {
		M2tConfiguration mapped = M2tConfigurationHelper.from(config(Map.of()), OCL);
		M2tConfiguration built = M2tConfiguration.builder(OCL).build();

		assertEquals(built.defaultCharset(), mapped.defaultCharset());
		assertEquals(built.whitespaceMode(), mapped.whitespaceMode());
		assertEquals(built.maxDiagnostics(), mapped.maxDiagnostics());
		assertEquals(built.maxTemplateDepth(), mapped.maxTemplateDepth());
		assertEquals(built.maxForIterations(), mapped.maxForIterations());
		assertEquals(built.maxCrossProductSize(), mapped.maxCrossProductSize());
		assertEquals(built.maxOutputSize(), mapped.maxOutputSize());
		assertEquals(built.protectedAreaEnabled(), mapped.protectedAreaEnabled());
	}

	@Test
	@DisplayName("every attribute lands on its own setting")
	void everyAttributeIsCarriedOver() {
		M2tConfiguration mapped = M2tConfigurationHelper.from(config(Map.ofEntries(
				Map.entry("defaultCharset", "ISO-8859-1"),
				Map.entry("whitespaceMode", "SPEC"),
				Map.entry("maxDiagnostics", 11),
				Map.entry("maxTemplateDepth", 22),
				Map.entry("maxForIterations", 33),
				Map.entry("maxCrossProductSize", 44),
				Map.entry("maxOutputSize", 55L),
				Map.entry("protectedAreaEnabled", false))), OCL);

		assertEquals(StandardCharsets.ISO_8859_1, mapped.defaultCharset());
		assertEquals(WhitespaceMode.SPEC, mapped.whitespaceMode());
		assertEquals(11, mapped.maxDiagnostics());
		assertEquals(22, mapped.maxTemplateDepth());
		assertEquals(33, mapped.maxForIterations());
		assertEquals(44, mapped.maxCrossProductSize());
		assertEquals(55L, mapped.maxOutputSize());
		assertFalse(mapped.protectedAreaEnabled());
	}

	@Test
	@DisplayName("the engine it was given is the engine in the configuration")
	void theSuppliedEngineIsCarriedOver() {
		assertSame(OCL, M2tConfigurationHelper.from(config(Map.of()), OCL).oclEngine());
	}

	/**
	 * An {@link M2tEngineConfiguration} answering with the given overrides, and with the
	 * attribute's declared default everywhere else — the same thing SCR hands the component.
	 */
	private static M2tEngineConfiguration config(Map<String, Object> overrides) {
		return (M2tEngineConfiguration) Proxy.newProxyInstance(
				M2tEngineConfiguration.class.getClassLoader(),
				new Class<?>[] { M2tEngineConfiguration.class },
				(proxy, method, args) -> overrides.containsKey(method.getName())
						? overrides.get(method.getName())
						: method.getDefaultValue());
	}
}
