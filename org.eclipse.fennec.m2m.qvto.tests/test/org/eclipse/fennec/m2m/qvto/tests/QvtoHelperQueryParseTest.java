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

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.fennec.m2m.model.qvtoperational.Constructor;
import org.eclipse.fennec.m2m.model.qvtoperational.EntryOperation;
import org.eclipse.fennec.m2m.model.qvtoperational.Helper;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2m.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Test;

/**
 * Tests for helper, query, constructor, and entry operations.
 */
class QvtoHelperQueryParseTest extends AbstractQvtoParserTest {

	@Test
	void helperWithBlock() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    helper doWork() {
				        var x := 1;
				    }
				}
				""");
		EOperation op = getOperation(t, "doWork");
		Helper helper = assertInstanceOf(Helper.class, op);
		assertFalse(helper.isIsQuery());
		assertNotNull(helper.getBody());
		assertFalse(helper.getBody().getContent().isEmpty());
	}

	@Test
	void helperWithExpressionBody() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    helper getValue() : Integer = 42;
				}
				""");
		EOperation op = getOperation(t, "getValue");
		Helper helper = assertInstanceOf(Helper.class, op);
		assertNotNull(helper.getBody());
	}

	@Test
	void queryOperation() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    query isValid() : Boolean = true;
				}
				""");
		EOperation op = getOperation(t, "isValid");
		Helper helper = assertInstanceOf(Helper.class, op);
		assertTrue(helper.isIsQuery());
	}

	@Test
	void queryWithBlock() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    query compute() : Integer {
				        return 42;
				    }
				}
				""");
		EOperation op = getOperation(t, "compute");
		Helper helper = assertInstanceOf(Helper.class, op);
		assertTrue(helper.isIsQuery());
		assertNotNull(helper.getBody());
	}

	@Test
	void helperWithParameters() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    helper add(a : Integer, b : Integer) : Integer = a + b;
				}
				""");
		EOperation op = getOperation(t, "add");
		assertEquals(2, op.getEParameters().size());
		assertEquals("a", op.getEParameters().get(0).getName());
		assertEquals("b", op.getEParameters().get(1).getName());
	}

	@Test
	void scopedHelper() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC) {
				    helper SourceElement::getName() : SourceElement = self;
				}
				""");
		EOperation op = getOperation(t, "getName");
		Helper helper = assertInstanceOf(Helper.class, op);
		assertNotNull(helper.getContext());
	}

	@Test
	void blackboxHelper() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    blackbox helper format(s : String) : String;
				}
				""");
		EOperation op = getOperation(t, "format");
		Helper helper = assertInstanceOf(Helper.class, op);
		assertTrue(helper.isIsBlackbox());
	}

	@Test
	void constructorDef() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC) {
				    constructor SourceElement::create() {
				        var x := 'hello';
				    }
				}
				""");
		EOperation op = getOperation(t, "create");
		assertInstanceOf(Constructor.class, op);
	}

	@Test
	void entryMain() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    main() {
				        var x := 1;
				    }
				}
				""");
		assertNotNull(t.getEntry());
		assertInstanceOf(EntryOperation.class, t.getEntry());
	}

	@Test
	void entryWithBody() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    main() {
				        var x := 1;
				        var y := 2;
				    }
				}
				""");
		EntryOperation entry = (EntryOperation) t.getEntry();
		assertNotNull(entry.getBody());
		assertFalse(entry.getBody().getContent().isEmpty());
	}

	@Test
	void queryWithReturnType() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    query name() : String = 'hello';
				}
				""");
		EOperation op = getOperation(t, "name");
		Helper helper = assertInstanceOf(Helper.class, op);
		assertTrue(helper.isIsQuery());
		assertFalse(helper.getResult().isEmpty());
	}

	@Test
	void helperAbstractBody() throws QvtoParseException {
		// Abstract helper with no body (just semicolon)
		OperationalTransformation t = parse("""
				transformation T() {
				    abstract helper doAbstract() : String;
				}
				""");
		EOperation op = getOperation(t, "doAbstract");
		assertInstanceOf(Helper.class, op);
	}
}
