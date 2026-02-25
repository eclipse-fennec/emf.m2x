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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.model.imperativeocl.AssignExp;
import org.eclipse.fennec.m2x.model.imperativeocl.VariableInitExp;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingBody;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Test;

/**
 * Tests for assignment expressions and variable declarations.
 */
class QvtoAssignVarParseTest extends AbstractQvtoParserTest {

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
	void assignReset() throws QvtoParseException {
		// Need a var first, then assign
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping doIt() {
				        var x := 0;
				        x := 42;
				    }
				}
				""");
		MappingOperation mapping = (MappingOperation) getOperation(t, "doIt");
		MappingBody body = (MappingBody) mapping.getBody();
		// Second statement should be AssignExp
		OclExpression expr = body.getContent().get(1);
		AssignExp assignExp = assertInstanceOf(AssignExp.class, expr);
		assertTrue(assignExp.isIsReset());
	}

	@Test
	void assignAppend() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping doIt() {
				        var x := 0;
				        x += 1;
				    }
				}
				""");
		MappingOperation mapping = (MappingOperation) getOperation(t, "doIt");
		MappingBody body = (MappingBody) mapping.getBody();
		OclExpression expr = body.getContent().get(1);
		AssignExp assignExp = assertInstanceOf(AssignExp.class, expr);
		assertFalse(assignExp.isIsReset());
	}

	@Test
	void assignOrderedCopy() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping doIt() {
				        var x := 0;
				        x ::= 1;
				    }
				}
				""");
		MappingOperation mapping = (MappingOperation) getOperation(t, "doIt");
		MappingBody body = (MappingBody) mapping.getBody();
		OclExpression expr = body.getContent().get(1);
		AssignExp assignExp = assertInstanceOf(AssignExp.class, expr);
		assertTrue(assignExp.isIsReset());
	}

	@Test
	void varDeclWithTypeAndInit() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("var x : Integer := 42;");
		VariableInitExp varInit = assertInstanceOf(VariableInitExp.class, expr);
		Variable v = varInit.getReferredVariable();
		assertNotNull(v);
		assertNotNull(v.getType());
		assertNotNull(v.getOwnedInit());
	}

	@Test
	void varDeclWithResetAssignInit() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("var x := 42;");
		VariableInitExp varInit = assertInstanceOf(VariableInitExp.class, expr);
		Variable v = varInit.getReferredVariable();
		assertNotNull(v);
		assertNotNull(v.getOwnedInit());
	}

	@Test
	void varDeclWithEqualsInit() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("var x = 42;");
		VariableInitExp varInit = assertInstanceOf(VariableInitExp.class, expr);
		Variable v = varInit.getReferredVariable();
		assertNotNull(v);
		assertNotNull(v.getOwnedInit());
	}

	@Test
	void varDeclWithoutInit() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("var x : String;");
		VariableInitExp varInit = assertInstanceOf(VariableInitExp.class, expr);
		Variable v = varInit.getReferredVariable();
		assertNotNull(v);
		assertNull(v.getOwnedInit());
	}

	@Test
	void varDeclNameOnly() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("var x;");
		VariableInitExp varInit = assertInstanceOf(VariableInitExp.class, expr);
		Variable v = varInit.getReferredVariable();
		assertNotNull(v);
		assertNull(v.getType());
		assertNull(v.getOwnedInit());
	}

	@Test
	void assignHasLeftAndValue() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping doIt() {
				        var x := 0;
				        x := 1;
				    }
				}
				""");
		MappingOperation mapping = (MappingOperation) getOperation(t, "doIt");
		MappingBody body = (MappingBody) mapping.getBody();
		AssignExp assignExp = (AssignExp) body.getContent().get(1);
		assertNotNull(assignExp.getLeft());
		assertFalse(assignExp.getValue().isEmpty());
	}

	@Test
	void varWithOrderedCopyInit() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("var x ::= 42;");
		VariableInitExp varInit = assertInstanceOf(VariableInitExp.class, expr);
		assertNotNull(varInit.getReferredVariable().getOwnedInit());
	}
}
