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

import org.eclipse.fennec.m2x.model.imperativeocl.InstantiationExp;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingBody;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.ObjectExp;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Test;

/**
 * Tests for ObjectExp and InstantiationExp (new).
 */
class QvtoObjectNewParseTest extends AbstractQvtoParserTest {

	private OclExpression parseBodyExpr(String bodyStatement) throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC) {
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
	void objectExpWithType() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("object SourceElement {};");
		ObjectExp objectExp = assertInstanceOf(ObjectExp.class, expr);
		assertNotNull(objectExp.getInstantiatedClass());
	}

	@Test
	void objectExpWithVarName() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("object elem : SourceElement {};");
		ObjectExp objectExp = assertInstanceOf(ObjectExp.class, expr);
		assertNotNull(objectExp.getReferredObject());
	}

	@Test
	void objectExpWithBody() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("object SourceElement { name := 'test'; };");
		ObjectExp objectExp = assertInstanceOf(ObjectExp.class, expr);
		assertNotNull(objectExp.getBody());
		assertFalse(objectExp.getBody().getContent().isEmpty());
	}

	@Test
	void objectExpEmptyBody() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("object SourceElement {};");
		ObjectExp objectExp = assertInstanceOf(ObjectExp.class, expr);
		assertNotNull(objectExp.getBody());
	}

	@Test
	void newExpression() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("new SourceElement();");
		InstantiationExp newExp = assertInstanceOf(InstantiationExp.class, expr);
		assertNotNull(newExp.getInstantiatedClass());
	}

	@Test
	void newExpressionWithArgs() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("new SourceElement('test');");
		InstantiationExp newExp = assertInstanceOf(InstantiationExp.class, expr);
		assertFalse(newExp.getArgument().isEmpty());
	}

	@Test
	void objectExpWithNamedVarAndBody() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("""
				object elem : SourceElement {
				    name := 'test';
				    value := 42;
				};""");
		ObjectExp objectExp = assertInstanceOf(ObjectExp.class, expr);
		assertNotNull(objectExp.getReferredObject());
		assertNotNull(objectExp.getBody());
	}

	@Test
	void newExpressionNoArgs() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("new SourceElement();");
		InstantiationExp newExp = assertInstanceOf(InstantiationExp.class, expr);
		assertNotNull(newExp.getInstantiatedClass());
	}
}
