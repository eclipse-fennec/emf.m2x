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
package org.eclipse.fennec.m2m.qvto.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingBody;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingCallExp;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2m.model.qvtoperational.ResolveExp;
import org.eclipse.fennec.m2m.model.qvtoperational.ResolveInExp;
import org.eclipse.fennec.m2m.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Test;

/**
 * Tests for mapping calls (map/xmap) and resolve expressions.
 */
class QvtoMappingCallResolveParseTest extends AbstractQvtoParserTest {

	private OclExpression parseBodyExpr(String bodyStatement) throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping target() {}
				    mapping doIt() {
				        %s
				    }
				}
				""".formatted(bodyStatement));
		MappingOperation mapping = (MappingOperation) getOperation(t, "doIt");
		MappingBody body = (MappingBody) mapping.getBody();
		assertFalse(body.getContent().isEmpty(), "Expected at least one body statement");
		return body.getContent().get(0);
	}

	@Test
	void mapCall() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("map target();");
		MappingCallExp call = assertInstanceOf(MappingCallExp.class, expr);
		assertFalse(call.isIsStrict());
	}

	@Test
	void xmapCall() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("xmap target();");
		MappingCallExp call = assertInstanceOf(MappingCallExp.class, expr);
		assertTrue(call.isIsStrict());
	}

	@Test
	void mapCallWithArgs() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping target(x : Integer) {}
				    mapping doIt() {
				        map target(42);
				    }
				}
				""");
		MappingOperation mapping = (MappingOperation) getOperation(t, "doIt");
		MappingBody body = (MappingBody) mapping.getBody();
		MappingCallExp call = assertInstanceOf(MappingCallExp.class, body.getContent().get(0));
		assertEquals(1, call.getOwnedArguments().size(), "Should have 1 argument");
	}

	@Test
	void resolveAll() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("resolve();");
		ResolveExp resolveExp = assertInstanceOf(ResolveExp.class, expr);
		assertFalse(resolveExp.isOne());
		assertFalse(resolveExp.isIsInverse());
		assertFalse(resolveExp.isIsDeferred());
	}

	@Test
	void resolveOne() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("resolveone();");
		ResolveExp resolveExp = assertInstanceOf(ResolveExp.class, expr);
		assertTrue(resolveExp.isOne());
	}

	@Test
	void invResolve() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("invresolve();");
		ResolveExp resolveExp = assertInstanceOf(ResolveExp.class, expr);
		assertTrue(resolveExp.isIsInverse());
	}

	@Test
	void lateResolve() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("late resolve();");
		ResolveExp resolveExp = assertInstanceOf(ResolveExp.class, expr);
		assertTrue(resolveExp.isIsDeferred());
	}

	@Test
	void resolveWithTarget() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("resolve(t : String);");
		ResolveExp resolveExp = assertInstanceOf(ResolveExp.class, expr);
		assertNotNull(resolveExp.getTarget());
	}

	@Test
	void resolveWithCondition() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("resolve(t : String | t = 'x');");
		ResolveExp resolveExp = assertInstanceOf(ResolveExp.class, expr);
		assertNotNull(resolveExp.getTarget());
		assertNotNull(resolveExp.getCondition());
	}

	@Test
	void resolveInExp() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("resolveIn(target);");
		assertInstanceOf(ResolveInExp.class, expr);
	}
}
