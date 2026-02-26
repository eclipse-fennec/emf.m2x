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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.ImperativeOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingBody;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.model.qvtoperational.VarParameter;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
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

	// ---- P2-07: Parse-level tests for merges, disjuncts, combined ----

	// §8.2.1.15: mapping merges → merged reference in AST
	@Test
	void mappingMerges() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping setName() {}
				    mapping setValue() {}
				    mapping combined() merges setName, setValue {}
				}
				""");
		MappingOperation combined = (MappingOperation) getOperation(t, "combined");
		assertEquals(2, combined.getMerged().size(), "Should have 2 merged mappings");
		assertEquals("setName", combined.getMerged().get(0).getName());
		assertEquals("setValue", combined.getMerged().get(1).getName());
	}

	// §8.2.1.15: mapping disjuncts → disjunct list in AST
	@Test
	void mappingDisjuncts() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping alt1() {}
				    mapping alt2() {}
				    mapping dispatch() disjuncts alt1, alt2 {}
				}
				""");
		MappingOperation dispatch = (MappingOperation) getOperation(t, "dispatch");
		assertEquals(2, dispatch.getDisjunct().size(), "Should have 2 disjunct mappings");
		assertEquals("alt1", dispatch.getDisjunct().get(0).getName());
		assertEquals("alt2", dispatch.getDisjunct().get(1).getName());
	}

	// §8.2.1.15: mapping inherits + merges combined
	@Test
	void mappingInheritsAndMerges() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping base() {}
				    mapping extra() {}
				    mapping derived() inherits base merges extra {}
				}
				""");
		MappingOperation derived = (MappingOperation) getOperation(t, "derived");
		assertEquals(1, derived.getInherited().size());
		assertEquals("base", derived.getInherited().get(0).getName());
		assertEquals(1, derived.getMerged().size());
		assertEquals("extra", derived.getMerged().get(0).getName());
	}

	// §8.2.1.15 Constraint: disjuncts mapping has empty body
	@Test
	void mappingDisjuncts_emptyBody() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping a() {}
				    mapping b() {}
				    mapping dispatch() disjuncts a, b {}
				}
				""");
		MappingOperation dispatch = (MappingOperation) getOperation(t, "dispatch");
		assertFalse(dispatch.getDisjunct().isEmpty());
		// Body should be empty (no population content)
		if (dispatch.getBody() instanceof MappingBody mb) {
			assertTrue(mb.getContent().isEmpty(), "Disjuncting mapping body should be empty");
		}
	}

	// §8.1.15: mapping merges with 3 targets
	@Test
	void mappingMerges_threeTargets() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping m1() {}
				    mapping m2() {}
				    mapping m3() {}
				    mapping combined() merges m1, m2, m3 {}
				}
				""");
		MappingOperation combined = (MappingOperation) getOperation(t, "combined");
		assertEquals(3, combined.getMerged().size());
		assertEquals("m1", combined.getMerged().get(0).getName());
		assertEquals("m2", combined.getMerged().get(1).getName());
		assertEquals("m3", combined.getMerged().get(2).getName());
	}

	// ---- P2-11: Mapping Sections vertieft (init/population/end) ----

	// §8.2.1.19: explicit population keyword in MappingBody
	@Test
	void mappingWithPopulationKeyword() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC) {
				    mapping createElem() : r : SourceElement {
				        population {
				            r.name := 'test';
				        }
				    }
				}
				""");
		EOperation op = getOperation(t, "createElem");
		MappingOperation mapping = (MappingOperation) op;
		MappingBody body = (MappingBody) mapping.getBody();
		assertNotNull(body);
		assertFalse(body.getContent().isEmpty(), "Population section should have content");
	}

	// §8.2.1.19: all three sections (init + population + end)
	@Test
	void mappingWithAllThreeSections() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC) {
				    mapping createElem() : r : SourceElement {
				        init {
				            var x := 'init';
				        }
				        population {
				            r.name := x;
				        }
				        end {
				            log(r.name);
				        }
				    }
				}
				""");
		EOperation op = getOperation(t, "createElem");
		MappingOperation mapping = (MappingOperation) op;
		MappingBody body = (MappingBody) mapping.getBody();
		assertFalse(body.getInitSection().isEmpty(), "Init section should be present");
		assertFalse(body.getContent().isEmpty(), "Population section should be present");
		assertFalse(body.getEndSection().isEmpty(), "End section should be present");
	}

	// §8.4 S.168: mapping declaration without body (abstract)
	@Test
	void mappingDecl_bodyless() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    abstract mapping createElem() : SourceElement;
				}
				""");
		MappingOperation mapping = (MappingOperation) getOperation(t, "createElem");
		assertNull(mapping.getBody(), "Bodyless mapping should have no body");
	}

	// §8.4 S.168: blackbox mapping declaration without body
	@Test
	void mappingDecl_blackbox_bodyless() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    blackbox mapping createElem() : SourceElement;
				}
				""");
		MappingOperation mapping = (MappingOperation) getOperation(t, "createElem");
		assertTrue(mapping.isIsBlackbox());
		assertNull(mapping.getBody(), "Bodyless mapping should have no body");
	}

	// §8.4 S.168: bodyless mapping with extension
	@Test
	void mappingDecl_bodyless_with_extensions() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping base() {}
				    abstract mapping derived() inherits base;
				}
				""");
		MappingOperation derived = (MappingOperation) getOperation(t, "derived");
		assertNull(derived.getBody(), "Bodyless mapping should have no body");
		assertEquals(1, derived.getInherited().size());
		assertEquals("base", derived.getInherited().get(0).getName());
	}

	// §8.1.15: mapping inherits with 2 bases
	@Test
	void mappingInherits_twoBases() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping base1() {}
				    mapping base2() {}
				    mapping derived() inherits base1, base2 {}
				}
				""");
		MappingOperation derived = (MappingOperation) getOperation(t, "derived");
		assertEquals(2, derived.getInherited().size());
		assertEquals("base1", derived.getInherited().get(0).getName());
		assertEquals("base2", derived.getInherited().get(1).getName());
	}
}
