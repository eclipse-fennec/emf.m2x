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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.fennec.m2x.model.imperativeocl.AssertExp;
import org.eclipse.fennec.m2x.model.imperativeocl.CatchExp;
import org.eclipse.fennec.m2x.model.imperativeocl.LogExp;
import org.eclipse.fennec.m2x.model.imperativeocl.RaiseExp;
import org.eclipse.fennec.m2x.model.imperativeocl.SeverityKind;
import org.eclipse.fennec.m2x.model.imperativeocl.TryExp;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingBody;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Test;

/**
 * Tests for try/catch, raise, assert, and log expressions.
 */
class QvtoTryCatchParseTest extends AbstractQvtoParserTest {

	private OclExpression parseBodyExpr(String bodyStatement) throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
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
	void tryCatch() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("""
				try {
				    var x := 1;
				} catch (Exception) {
				    var y := 2;
				};""");
		TryExp tryExp = assertInstanceOf(TryExp.class, expr);
		assertFalse(tryExp.getTryBody().isEmpty());
		assertFalse(tryExp.getExceptClause().isEmpty());
	}

	@Test
	void catchWithExceptionType() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("""
				try {
				    var x := 1;
				} catch (Exception) {
				    var y := 2;
				};""");
		TryExp tryExp = assertInstanceOf(TryExp.class, expr);
		CatchExp catchExp = tryExp.getExceptClause().get(0);
		assertFalse(catchExp.getException().isEmpty());
	}

	@Test
	void catchWithoutType() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("""
				try {
				    var x := 1;
				} catch {
				    var y := 2;
				};""");
		TryExp tryExp = assertInstanceOf(TryExp.class, expr);
		CatchExp catchExp = tryExp.getExceptClause().get(0);
		// Catch without parenthesized type list
		assertNotNull(catchExp);
	}

	@Test
	void raiseWithString() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("raise 'something went wrong';");
		assertInstanceOf(RaiseExp.class, expr);
	}

	@Test
	void raiseWithType() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("raise Exception;");
		RaiseExp raiseExp = assertInstanceOf(RaiseExp.class, expr);
		assertNotNull(raiseExp.getException());
	}

	@Test
	void assertExpression() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("assert (true);");
		AssertExp assertExp = assertInstanceOf(AssertExp.class, expr);
		assertNotNull(assertExp.getAssertion());
	}

	@Test
	void assertWithSeverity() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("assert warning (true);");
		AssertExp assertExp = assertInstanceOf(AssertExp.class, expr);
		assertEquals(SeverityKind.WARNING, assertExp.getSeverity());
	}

	@Test
	void assertWithLog() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("assert (true) with log('check passed');");
		AssertExp assertExp = assertInstanceOf(AssertExp.class, expr);
		assertNotNull(assertExp.getLog());
	}

	@Test
	void logExpression() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("log('message');");
		LogExp logExp = assertInstanceOf(LogExp.class, expr);
		assertFalse(logExp.getOwnedArguments().isEmpty());
	}

	@Test
	void logWithWhen() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("log('debug', 42) when true;");
		LogExp logExp = assertInstanceOf(LogExp.class, expr);
		assertNotNull(logExp.getCondition());
	}
}
