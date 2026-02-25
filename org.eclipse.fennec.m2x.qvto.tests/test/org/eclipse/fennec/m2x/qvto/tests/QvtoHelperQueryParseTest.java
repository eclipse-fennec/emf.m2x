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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.Constructor;
import org.eclipse.fennec.m2x.model.qvtoperational.ConstructorBody;
import org.eclipse.fennec.m2x.model.qvtoperational.ContextualProperty;
import org.eclipse.fennec.m2x.model.qvtoperational.EntryOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.Helper;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
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

	// §8.2.1.13: Constructor with parameters
	@Test
	void constructorWithParameters() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC) {
				    constructor SourceElement::SourceElement(n : String, v : Integer) {
				        name := n;
				        value := v;
				    }
				}
				""");
		EOperation op = getOperation(t, "SourceElement");
		Constructor ctor = assertInstanceOf(Constructor.class, op);
		assertEquals(2, ctor.getEParameters().size());
		assertEquals("n", ctor.getEParameters().get(0).getName());
		assertEquals("v", ctor.getEParameters().get(1).getName());
	}

	// §8.2.1.13: Constructor body is ConstructorBody type
	@Test
	void constructorHasConstructorBody() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC) {
				    constructor SourceElement::SourceElement(n : String) {
				        name := n;
				    }
				}
				""");
		Constructor ctor = assertInstanceOf(Constructor.class, getOperation(t, "SourceElement"));
		assertNotNull(ctor.getBody());
		assertInstanceOf(ConstructorBody.class, ctor.getBody());
		assertFalse(ctor.getBody().getContent().isEmpty(), "Constructor body should have statements");
	}

	// §8.2.1.13: Constructor with no parameters (explicit default)
	@Test
	void constructorNoParameters() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC) {
				    constructor SourceElement::SourceElement() {
				        name := 'default';
				    }
				}
				""");
		Constructor ctor = assertInstanceOf(Constructor.class, getOperation(t, "SourceElement"));
		assertTrue(ctor.getEParameters().isEmpty());
	}

	// §8.2.1.13: Constructor with distinct name (not class name)
	@Test
	void constructorDistinctName() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC) {
				    constructor SourceElement::createWithName(n : String) {
				        name := n;
				    }
				}
				""");
		Constructor ctor = assertInstanceOf(Constructor.class, getOperation(t, "createWithName"));
		assertEquals(1, ctor.getEParameters().size());
	}

	// ---- Intermediate Property parse tests (§8.1.10, §8.2.1.14) ----

	// §8.1.10: intermediate property declaration with context type
	@Test
	void intermediatePropertyDecl() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC) {
				    intermediate property SourceElement::tag : String;
				    main() { }
				}
				""");
		assertFalse(t.getIntermediateProperty().isEmpty(), "Should have intermediate property");
		ContextualProperty prop = (ContextualProperty) t.getIntermediateProperty().get(0);
		assertEquals("tag", prop.getName());
		assertNotNull(prop.getContext(), "Context EClass should be set");
		assertEquals("SourceElement", prop.getContext().getName());
	}

	// §8.1.10: intermediate property with init expression
	@Test
	void intermediatePropertyWithInit() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC) {
				    intermediate property SourceElement::tag : String = 'default';
				    main() { }
				}
				""");
		ContextualProperty prop = (ContextualProperty) t.getIntermediateProperty().get(0);
		assertEquals("tag", prop.getName());
		assertNotNull(prop.getInitExpression(), "Init expression should be set");
	}

	// §8.1.10: multiple intermediate properties
	@Test
	void multipleIntermediateProperties() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC) {
				    intermediate property SourceElement::tag : String;
				    intermediate property SourceElement::processed : Boolean;
				    main() { }
				}
				""");
		assertEquals(2, t.getIntermediateProperty().size());
	}

	// §8.1.10: intermediate class declaration
	@Test
	void intermediateClassDecl() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    intermediate class Memo { text : String; priority : Integer; }
				    main() { }
				}
				""");
		assertFalse(t.getIntermediateClass().isEmpty());
		EClass memo = t.getIntermediateClass().get(0);
		assertEquals("Memo", memo.getName());
		assertEquals(2, memo.getEStructuralFeatures().size());
	}

	// ---- P3-04: Intermediate Class Parse Tests ----

	// §8.1.10: Intermediate class without properties
	@Test
	void intermediateClassEmpty() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    intermediate class Empty { }
				    main() { }
				}
				""");
		assertFalse(t.getIntermediateClass().isEmpty());
		EClass empty = t.getIntermediateClass().get(0);
		assertEquals("Empty", empty.getName());
		assertEquals(0, empty.getEStructuralFeatures().size());
	}

	// §8.1.10: Multiple intermediate classes in one transformation
	@Test
	void multipleIntermediateClasses() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    intermediate class First { a : String; }
				    intermediate class Second { b : Integer; c : Boolean; }
				    main() { }
				}
				""");
		assertEquals(2, t.getIntermediateClass().size());
		assertEquals("First", t.getIntermediateClass().get(0).getName());
		assertEquals("Second", t.getIntermediateClass().get(1).getName());
		assertEquals(1, t.getIntermediateClass().get(0).getEStructuralFeatures().size());
		assertEquals(2, t.getIntermediateClass().get(1).getEStructuralFeatures().size());
	}

	// ---- P3-01: Helper vertieft Parse Tests ----

	// §8.2.1.12: Helper without return type (void)
	@Test
	void helperVoidNoReturnType() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    helper doWork() {
				        log('side-effect');
				    }
				}
				""");
		EOperation op = getOperation(t, "doWork");
		Helper helper = assertInstanceOf(Helper.class, op);
		assertFalse(helper.isIsQuery());
		assertTrue(helper.getResult().isEmpty(), "Void helper should have no result parameter");
	}

	// §8.1.9 p73: Helper with inout parameter
	@Test
	void helperWithInoutParameter() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    helper inc(inout x : Integer) {
				        x := x + 1;
				    }
				}
				""");
		EOperation op = getOperation(t, "inc");
		Helper helper = assertInstanceOf(Helper.class, op);
		assertNotNull(helper);
		assertEquals(1, op.getEParameters().size());
		assertEquals("x", op.getEParameters().get(0).getName());
	}

	// §8.2.1.12: Helper with Tuple return type
	@Test
	void helperWithTupleReturnType() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    helper pair(a : String, b : Integer) : Tuple(name : String, value : Integer) {
				        return Tuple{name = a, value = b};
				    }
				}
				""");
		EOperation op = getOperation(t, "pair");
		Helper helper = assertInstanceOf(Helper.class, op);
		assertNotNull(helper.getBody());
	}

	// ---- P3-02: Query vertieft Parse Tests ----

	// §8.1.9 p73: Query on primitive type context (String)
	@Test
	void queryOnStringContext() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    query String::wrap() : String = '[' + self + ']';
				}
				""");
		Helper helper = assertInstanceOf(Helper.class, getOperation(t, "wrap"));
		assertTrue(helper.isIsQuery());
		assertNotNull(helper.getContext());
	}

	// §8.2.1.12 p102 + Eclipse virtualPredefinedTypeOpers: Query on Integer context
	@Test
	void queryOnIntegerContext() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    query Integer::doubled() : Integer = self * 2;
				}
				""");
		Helper helper = assertInstanceOf(Helper.class, getOperation(t, "doubled"));
		assertTrue(helper.isIsQuery());
		assertNotNull(helper.getContext());
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
