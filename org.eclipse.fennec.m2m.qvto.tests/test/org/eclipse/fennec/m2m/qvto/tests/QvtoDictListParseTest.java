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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.fennec.m2m.model.imperativeocl.VariableInitExp;
import org.eclipse.fennec.m2m.model.ocl.CollectionType;
import org.eclipse.fennec.m2m.model.ocl.MapType;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.model.ocl.Variable;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingBody;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2m.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Test;

/**
 * Tests for D26: Dict/List as parser-level aliases for Map/Sequence.
 */
class QvtoDictListParseTest extends AbstractQvtoParserTest {

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
	void listTypeResolvesToSequence() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("var x : List(String);");
		VariableInitExp varInit = assertInstanceOf(VariableInitExp.class, expr);
		Variable v = varInit.getReferredVariable();
		assertNotNull(v.getType());
		assertInstanceOf(CollectionType.class, v.getType());
	}

	@Test
	void dictTypeResolvesToMap() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("var x : Dict(String, Integer);");
		VariableInitExp varInit = assertInstanceOf(VariableInitExp.class, expr);
		Variable v = varInit.getReferredVariable();
		assertNotNull(v.getType());
		assertInstanceOf(MapType.class, v.getType());
	}

	@Test
	void listInHelperParameter() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    helper process(items : List(String)) : Integer = 0;
				}
				""");
		assertNotNull(getOperation(t, "process"));
	}

	@Test
	void dictInHelperParameter() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    helper lookup(table : Dict(String, Integer)) : Integer = 0;
				}
				""");
		assertNotNull(getOperation(t, "lookup"));
	}

	@Test
	void listInReturnType() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    query getNames() : List(String) = Sequence{};
				}
				""");
		assertNotNull(getOperation(t, "getNames"));
	}

	@Test
	void dictInReturnType() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    query getMapping() : Dict(String, Integer) = Map{};
				}
				""");
		assertNotNull(getOperation(t, "getMapping"));
	}
}
