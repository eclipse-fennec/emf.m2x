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

import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.UnlimitedNaturalLiteralExp;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingBody;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Test;

/**
 * Tests for N3: 'unlimited' keyword as literal (§8.4.7).
 */
class QvtoUnlimitedLiteralParseTest extends AbstractQvtoParserTest {

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
	void unlimitedKeyword_parsesAsUnlimitedNaturalLiteral() throws QvtoParseException {
		OclExpression expr = parseBodyExpr("unlimited;");
		UnlimitedNaturalLiteralExp lit = assertInstanceOf(UnlimitedNaturalLiteralExp.class, expr);
		assertEquals(-1L, lit.getUnlimitedNaturalSymbol());
	}

	@Test
	void starLiteral_stillParsesAsUnlimitedNaturalLiteral() throws QvtoParseException {
		// Regression: '*' must still work
		OclExpression expr = parseBodyExpr("*;");
		UnlimitedNaturalLiteralExp lit = assertInstanceOf(UnlimitedNaturalLiteralExp.class, expr);
		assertEquals(-1L, lit.getUnlimitedNaturalSymbol());
	}
}
