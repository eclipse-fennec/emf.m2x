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
package org.eclipse.fennec.m2m.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.ocl.api.OclContext;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for the OclContext property interceptor mechanism.
 *
 * <p>The property interceptor allows QVT-O (and other clients) to inject custom
 * property resolution logic without modifying the OCL engine. This is used for
 * QVT-O intermediate properties (§8.1.10) which exist only at transformation
 * scope and are not real EStructuralFeatures on the EClass.
 */
class OclPropertyInterceptorTest extends AbstractOclTest {

	static EObject alice;

	@BeforeAll
	static void setUp() {
		alice = createPerson("Alice", 30, 50000.0, true);
	}

	// ---- Interceptor not set: normal behavior ----

	@Test
	void noInterceptor_normalPropertyAccess() throws OclParseException {
		assertEquals("Alice", engine.evaluate("self.name", OclContext.of(alice)));
	}

	// ---- Interceptor set but returns NOT_HANDLED: falls through to eGet ----

	@Test
	void interceptor_notHandled_fallsThrough() throws OclParseException {
		OclContext ctx = new OclContext(alice, null, Map.of(), null,
				(target, propName) -> OclContext.PROPERTY_NOT_HANDLED);
		assertEquals("Alice", engine.evaluate("self.name", ctx));
	}

	// ---- Interceptor intercepts a known property ----

	@Test
	void interceptor_overridesExistingProperty() throws OclParseException {
		OclContext ctx = new OclContext(alice, null, Map.of(), null,
				(target, propName) -> {
					if ("name".equals(propName)) {
						return "Intercepted";
					}
					return OclContext.PROPERTY_NOT_HANDLED;
				});
		assertEquals("Intercepted", engine.evaluate("self.name", ctx));
	}

	// ---- Interceptor returns null ----

	@Test
	void interceptor_returnsNull() throws OclParseException {
		OclContext ctx = new OclContext(alice, null, Map.of(), null,
				(target, propName) -> {
					if ("name".equals(propName)) {
						return null;
					}
					return OclContext.PROPERTY_NOT_HANDLED;
				});
		// OCL wraps null to OclNull, which toString() = null
		assertNull(engine.evaluate("self.name", ctx));
	}

	// ---- Interceptor works in expressions (string concat) ----

	@Test
	void interceptor_worksInConcatenation() throws OclParseException {
		OclContext ctx = new OclContext(alice, null, Map.of(), null,
				(target, propName) -> {
					if ("name".equals(propName)) {
						return "Virtual";
					}
					return OclContext.PROPERTY_NOT_HANDLED;
				});
		assertEquals("Hello Virtual", engine.evaluate("'Hello ' + self.name", ctx));
	}

	// ---- Interceptor works with non-intercepted properties in same expression ----

	@Test
	void interceptor_mixedProperties() throws OclParseException {
		OclContext ctx = new OclContext(alice, null, Map.of(), null,
				(target, propName) -> {
					if ("name".equals(propName)) {
						return "Custom";
					}
					return OclContext.PROPERTY_NOT_HANDLED;
				});
		// 'name' is intercepted, 'age' falls through to normal eGet
		assertEquals("Custom:30", engine.evaluate("self.name + ':' + self.age.toString()", ctx));
	}

	// ---- Interceptor with collection: implicit collect ----

	@Test
	void interceptor_implicitCollect() throws OclParseException {
		EObject bob = createPerson("Bob", 25, 40000.0, false);
		EObject company = createCompany("ACME", alice, bob);
		OclContext ctx = new OclContext(company, null, Map.of(), null,
				(target, propName) -> {
					if ("name".equals(propName) && personClass.isInstance(target)) {
						return "X-" + target.eGet(personClass.getEStructuralFeature("name"));
					}
					return OclContext.PROPERTY_NOT_HANDLED;
				});
		// employees->collect(name) should use interceptor for each Person
		Object result = engine.evaluate("self.employees.name", ctx);
		assertEquals(java.util.List.of("X-Alice", "X-Bob"), result);
	}
}
