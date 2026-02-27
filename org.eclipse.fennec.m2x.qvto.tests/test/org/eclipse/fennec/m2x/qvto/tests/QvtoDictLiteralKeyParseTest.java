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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Test;

/**
 * Tests for N10: Dict literal key restriction to literal_simple (§8.4.7).
 * Spec EBNF: {@code <dict_item> ::= <literal_simple> '=' <expression>}
 * Eclipse: {@code dictLiteralPartCS ::= literalSimpleCS '=' OclExpressionCS}
 */
class QvtoDictLiteralKeyParseTest extends AbstractQvtoParserTest {

	@Test
	void dictLiteral_stringKey_parsesSuccessfully() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation test();
				main() {
				    var d : Dict(String, Integer) := Dict{'hello' = 42};
				    log(d->get('hello').repr());
				}
				""");
		assertNotNull(t);
	}

	@Test
	void dictLiteral_integerKey_parsesSuccessfully() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation test();
				main() {
				    var d : Dict(Integer, String) := Dict{1 = 'one', 2 = 'two'};
				    log(d->get(1).repr());
				}
				""");
		assertNotNull(t);
	}

	@Test
	void dictLiteral_booleanKey_parsesSuccessfully() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation test();
				main() {
				    var d : Dict(Boolean, String) := Dict{true = 'yes', false = 'no'};
				    log(d->get(true).repr());
				}
				""");
		assertNotNull(t);
	}

	@Test
	void dictLiteral_nullKey_parsesSuccessfully() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation test();
				main() {
				    var d : Dict(String, Integer) := Dict{null = 0};
				    log(d->size().repr());
				}
				""");
		assertNotNull(t);
	}

	@Test
	void dictLiteral_expressionKey_rejectsParseError() {
		// §8.4.7: Dict keys must be literal_simple, NOT arbitrary expressions
		assertThrows(QvtoParseException.class, () -> parse("""
				transformation test();
				main() {
				    var k := 'hello';
				    var d : Dict(String, Integer) := Dict{k = 42};
				}
				"""));
	}
}
