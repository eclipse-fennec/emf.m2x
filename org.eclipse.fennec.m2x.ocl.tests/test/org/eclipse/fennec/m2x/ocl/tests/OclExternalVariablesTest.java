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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OclContext} with external variables.
 * Verifies that variables passed via {@code OclContext.of(self, variables)}
 * are accessible in OCL expressions.
 */
class OclExternalVariablesTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	// --- OclContext factory methods ---

	@Test
	void context_ofSelf() {
		OclContext ctx = OclContext.of(self);
		assertEquals(self, ctx.self());
		assertTrue(ctx.variables().isEmpty());
	}

	@Test
	void context_ofSelfWithVariables() {
		OclContext ctx = OclContext.of(self, Map.of("x", 42));
		assertEquals(self, ctx.self());
		assertEquals(Map.of("x", 42), ctx.variables());
	}

	@Test
	void context_nullSelf_allowed() {
		OclContext ctx = OclContext.of((EObject) null);
		assertNull(ctx.self());
	}

	@Test
	void context_variablesImmutable() {
		OclContext ctx = OclContext.of(self, Map.of("x", 42));
		assertThrows(UnsupportedOperationException.class, () -> ctx.variables().put("y", 0));
	}

	// --- Self access ---

	@Test
	void selfAccess_name() throws OclParseException {
		OclContext ctx = OclContext.of(self);
		assertEquals("Alice", engine.evaluate("self.name", ctx));
	}

	@Test
	void selfAccess_age() throws OclParseException {
		OclContext ctx = OclContext.of(self);
		assertEquals(30, engine.evaluate("self.age", ctx));
	}

	// --- String expression with context ---

	@Test
	void stringEvaluate_withSelf() throws OclParseException {
		assertEquals("Alice", engine.evaluate("self.name", OclContext.of(self)));
	}

	@Test
	void stringEvaluate_literal() throws OclParseException {
		assertEquals(42, engine.evaluate("42", OclContext.of(self)));
	}

	@Test
	void stringEvaluate_complexExpression() throws OclParseException {
		assertEquals(31, engine.evaluate("self.age + 1", OclContext.of(self)));
	}

	// --- Parse and evaluate separately ---

	@Test
	void parseAndEvaluate_separate() throws OclParseException {
		OclExpression expr = engine.parse("self.name.size()", personClass);
		Object result = engine.evaluate(expr, OclContext.of(self));
		assertEquals(5, result);
	}

	@Test
	void parseAndEvaluate_reuseExpression() throws OclParseException {
		OclExpression expr = engine.parse("self.age * 2", personClass);
		EObject bob = createPerson("Bob", 25, 40000.0, false);

		assertEquals(60, engine.evaluate(expr, OclContext.of(self)));
		assertEquals(50, engine.evaluate(expr, OclContext.of(bob)));
	}

	// --- Multiple contexts ---

	@Test
	void differentSelf_sameExpression() throws OclParseException {
		EObject bob = createPerson("Bob", 25, 40000.0, false);
		assertEquals("Alice", engine.evaluate("self.name", OclContext.of(self)));
		assertEquals("Bob", engine.evaluate("self.name", OclContext.of(bob)));
	}

	@Test
	void differentSelf_booleanResult() throws OclParseException {
		EObject bob = createPerson("Bob", 25, 40000.0, false);
		assertEquals(true, engine.evaluate("self.isMarried", OclContext.of(self)));
		assertEquals(false, engine.evaluate("self.isMarried", OclContext.of(bob)));
	}
}
