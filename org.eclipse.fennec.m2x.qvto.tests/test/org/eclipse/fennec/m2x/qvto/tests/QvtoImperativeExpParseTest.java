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
import static org.junit.jupiter.api.Assertions.assertNull;

import org.eclipse.fennec.m2x.model.imperativeocl.AltExp;
import org.eclipse.fennec.m2x.model.imperativeocl.BreakExp;
import org.eclipse.fennec.m2x.model.imperativeocl.ComputeExp;
import org.eclipse.fennec.m2x.model.imperativeocl.ContinueExp;
import org.eclipse.fennec.m2x.model.imperativeocl.ForExp;
import org.eclipse.fennec.m2x.model.imperativeocl.ReturnExp;
import org.eclipse.fennec.m2x.model.imperativeocl.SwitchExp;
import org.eclipse.fennec.m2x.model.imperativeocl.WhileExp;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingBody;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Test;

/**
 * Tests for imperative expressions: while, for, switch, compute, return, break, continue.
 */
class QvtoImperativeExpParseTest extends AbstractQvtoParserTest {

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
	void whileExpression() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("while (true) { var x := 1; };");
		WhileExp whileExp = assertInstanceOf(WhileExp.class, expr);
		assertNotNull(whileExp.getCondition());
		assertNotNull(whileExp.getBody());
	}

	@Test
	void forEachExpression() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("forEach (i; Integer) { var x := i; };");
		ForExp forExp = assertInstanceOf(ForExp.class, expr);
		assertFalse(forExp.getOwnedIterators().isEmpty());
		assertNotNull(forExp.getOwnedBody());
	}

	@Test
	void forOneExpression() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("forOne (i; Integer) { var x := i; };");
		ForExp forExp = assertInstanceOf(ForExp.class, expr);
		assertFalse(forExp.getOwnedIterators().isEmpty());
	}

	@Test
	void forEachWithCondition() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("forEach (i; Integer | i > 0) { var x := i; };");
		ForExp forExp = assertInstanceOf(ForExp.class, expr);
		assertNotNull(forExp.getCondition());
	}

	@Test
	void switchExpression() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("""
				switch {
				    case (true) 1;
				    case (false) 2;
				    else 0;
				};""");
		SwitchExp switchExp = assertInstanceOf(SwitchExp.class, expr);
		assertEquals(2, switchExp.getAlternativePart().size());
		assertNotNull(switchExp.getElsePart());
	}

	@Test
	void switchAlternative() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("""
				switch {
				    case (true) 42;
				};""");
		SwitchExp switchExp = assertInstanceOf(SwitchExp.class, expr);
		assertEquals(1, switchExp.getAlternativePart().size());
		AltExp alt = switchExp.getAlternativePart().get(0);
		assertNotNull(alt.getCondition());
		assertNotNull(alt.getBody());
	}

	@Test
	void switchWithoutElse() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("""
				switch {
				    case (true) 1;
				};""");
		SwitchExp switchExp = assertInstanceOf(SwitchExp.class, expr);
		assertNull(switchExp.getElsePart());
	}

	@Test
	void computeExpression() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("compute (r) { r := 1; };");
		ComputeExp computeExp = assertInstanceOf(ComputeExp.class, expr);
		assertNotNull(computeExp.getReturnedElement());
		assertNotNull(computeExp.getBody());
	}

	@Test
	void returnExpression() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("return 42;");
		ReturnExp returnExp = assertInstanceOf(ReturnExp.class, expr);
		assertNotNull(returnExp.getValue());
	}

	@Test
	void returnWithoutValue() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("return;");
		ReturnExp returnExp = assertInstanceOf(ReturnExp.class, expr);
		assertNull(returnExp.getValue());
	}

	@Test
	void breakExpression() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("break;");
		assertInstanceOf(BreakExp.class, expr);
	}

	@Test
	void continueExpression() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("continue;");
		assertInstanceOf(ContinueExp.class, expr);
	}

	@Test
	void whileWithBreak() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("while (true) { break; };");
		WhileExp whileExp = assertInstanceOf(WhileExp.class, expr);
		assertNotNull(whileExp.getBody());
	}

	@Test
	void switchWithComputation() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("""
				switch {
				    case (1 > 0) 'positive';
				    case (1 < 0) 'negative';
				    else 'zero';
				};""");
		SwitchExp switchExp = assertInstanceOf(SwitchExp.class, expr);
		assertEquals(2, switchExp.getAlternativePart().size());
		assertNotNull(switchExp.getElsePart());
	}
}
