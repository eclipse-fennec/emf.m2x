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

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingBody;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2m.model.qvtoperational.VarParameter;
import org.eclipse.fennec.m2m.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Test;

/**
 * Tests for mapping operations: signature, parameters, when/where, init/end, scoped names.
 */
class QvtoMappingParseTest extends AbstractQvtoParserTest {

	@Test
	void simpleMapping() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping doIt() {}
				}
				""");
		EOperation op = getOperation(t, "doIt");
		assertInstanceOf(MappingOperation.class, op);
	}

	@Test
	void mappingName() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping convert() {}
				}
				""");
		EOperation op = getOperation(t, "convert");
		assertEquals("convert", op.getName());
	}

	@Test
	void mappingWithInputParams() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping doIt(name : String, count : Integer) {}
				}
				""");
		EOperation op = getOperation(t, "doIt");
		assertEquals(2, op.getEParameters().size());
		assertEquals("name", op.getEParameters().get(0).getName());
		assertEquals("count", op.getEParameters().get(1).getName());
	}

	@Test
	void mappingWithReturnType() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC) {
				    mapping doIt(x : SourceElement) : r : SourceElement {}
				}
				""");
		EOperation op = getOperation(t, "doIt");
		ImperativeOperation impOp = (ImperativeOperation) op;
		assertFalse(impOp.getResult().isEmpty());
		VarParameter result = impOp.getResult().get(0);
		assertEquals("r", result.getName());
	}

	@Test
	void mappingWithWhenClause() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping doIt() when { true; } {}
				}
				""");
		EOperation op = getOperation(t, "doIt");
		MappingOperation mapping = (MappingOperation) op;
		assertFalse(mapping.getWhen().isEmpty());
	}

	@Test
	void mappingWithWhereClause() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping doIt() where { true; } {}
				}
				""");
		EOperation op = getOperation(t, "doIt");
		MappingOperation mapping = (MappingOperation) op;
		assertNotNull(mapping.getWhere());
	}

	@Test
	void mappingWithInitSection() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping doIt() {
				        init { var x := 1; }
				    }
				}
				""");
		EOperation op = getOperation(t, "doIt");
		MappingOperation mapping = (MappingOperation) op;
		MappingBody body = (MappingBody) mapping.getBody();
		assertNotNull(body);
		assertFalse(body.getInitSection().isEmpty());
	}

	@Test
	void mappingWithEndSection() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping doIt() {
				        end { var y := 2; }
				    }
				}
				""");
		EOperation op = getOperation(t, "doIt");
		MappingOperation mapping = (MappingOperation) op;
		MappingBody body = (MappingBody) mapping.getBody();
		assertNotNull(body);
		assertFalse(body.getEndSection().isEmpty());
	}

	@Test
	void mappingWithInitAndEnd() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping doIt() {
				        init { var x := 1; }
				        end { var y := 2; }
				    }
				}
				""");
		EOperation op = getOperation(t, "doIt");
		MappingOperation mapping = (MappingOperation) op;
		MappingBody body = (MappingBody) mapping.getBody();
		assertFalse(body.getInitSection().isEmpty());
		assertFalse(body.getEndSection().isEmpty());
	}

	@Test
	void mappingBodyStatements() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping doIt() {
				        var x := 1;
				        var y := 2;
				    }
				}
				""");
		EOperation op = getOperation(t, "doIt");
		MappingOperation mapping = (MappingOperation) op;
		MappingBody body = (MappingBody) mapping.getBody();
		assertFalse(body.getContent().isEmpty());
	}

	@Test
	void scopedMapping() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC) {
				    mapping SourceElement::convert() {}
				}
				""");
		EOperation op = getOperation(t, "convert");
		ImperativeOperation impOp = (ImperativeOperation) op;
		assertNotNull(impOp.getContext());
	}

	@Test
	void mappingWithWhenAndWhere() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping doIt()
				        when { true; }
				        where { false; }
				    {}
				}
				""");
		EOperation op = getOperation(t, "doIt");
		MappingOperation mapping = (MappingOperation) op;
		assertFalse(mapping.getWhen().isEmpty());
		assertNotNull(mapping.getWhere());
	}

	@Test
	void abstractMapping_parsesWithoutError() throws QvtoParseException {
		// abstract modifier is accepted by the grammar but not yet stored in the model
		OperationalTransformation t = parse("""
				transformation T() {
				    abstract mapping doIt() {}
				}
				""");
		EOperation op = getOperation(t, "doIt");
		assertInstanceOf(MappingOperation.class, op);
	}

	@Test
	void mappingWithMultipleResults() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC) {
				    mapping doIt(x : SourceElement) : r1 : SourceElement, r2 : SourceContainer {}
				}
				""");
		EOperation op = getOperation(t, "doIt");
		ImperativeOperation impOp = (ImperativeOperation) op;
		assertEquals(2, impOp.getResult().size());
	}

	@Test
	void mappingInherits() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping base() {}
				    mapping derived() inherits base {}
				}
				""");
		MappingOperation derived = (MappingOperation) getOperation(t, "derived");
		assertFalse(derived.getInherited().isEmpty(), "Derived mapping should reference base via inherits");
		assertEquals("base", derived.getInherited().get(0).getName());
	}
}
