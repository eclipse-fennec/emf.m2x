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
package org.eclipse.fennec.m2x.qvto.tests.intermediate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for GAP-22: Intermediate Class Features.
 *
 * <p>Tests multiplicity, opposites, EReference (references/composes), derived modifier,
 * generic stereotypes, and forward-references between intermediate classes.
 *
 * <p><b>SPEC-FIRST:</b> Tests written against QVT-O v1.3 §8.4 (p.167) concrete syntax
 * for intermediate class features. Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eIntermediateClassFeaturesTest extends AbstractQvtoEngineTest {

	// ==== GAP-22: Intermediate Class Features ====

	// ---- references (single-valued EReference) ----

	@Test
	void references_singleValued() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Node {
				        name : String;
				        references next : Node;
				    }
				    main() {
				        var a := object Node { name := 'A'; };
				        var b := object Node { name := 'B'; next := a; };
				        log('next:' + b.next.name);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "next:A");
	}

	// ---- composes (containment EReference) ----

	@Test
	void composes_containment() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Parent {
				        name : String;
				        composes children : Child[*];
				    }
				    intermediate class Child {
				        label : String;
				    }
				    main() {
				        var p := object Parent { name := 'root'; };
				        p.children += object Child { label := 'c1'; };
				        p.children += object Child { label := 'c2'; };
				        log('count:' + p.children->size().repr());
				        log('first:' + p.children->at(1).label);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "count:2");
		assertLogged(result, "first:c1");
	}

	// ---- multiplicity [*] (multi-valued) ----

	@Test
	void multiplicity_star() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Container {
				        references items : Item[*];
				    }
				    intermediate class Item {
				        name : String;
				    }
				    main() {
				        var c := object Container {};
				        c.items += object Item { name := 'x'; };
				        c.items += object Item { name := 'y'; };
				        log('size:' + c.items->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:2");
	}

	// ---- multiplicity [0..1] (optional single-valued) ----

	@Test
	void multiplicity_optional() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Node {
				        name : String;
				        references opt : Node[0..1];
				    }
				    main() {
				        var a := object Node { name := 'A'; };
				        var b := object Node { name := 'B'; opt := a; };
				        log('opt:' + b.opt.name);
				        log('null:' + (a.opt = null).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "opt:A");
		assertLogged(result, "null:true");
	}

	// ---- multiplicity [1] (exact) with <<id>> ----

	@Test
	void multiplicity_exact() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Entity {
				        <<id>> uuid : String[1];
				        name : String;
				    }
				    main() {
				        var e := object Entity { uuid := 'abc-123'; name := 'test'; };
				        log('uuid:' + e.uuid);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "uuid:abc-123");
	}

	// ---- opposites (bidirectional) ----

	@Test
	void opposites_bidirectional() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Parent {
				        name : String;
				        composes children : Child[*] opposites ~parent;
				    }
				    intermediate class Child {
				        label : String;
				    }
				    main() {
				        var p := object Parent { name := 'root'; };
				        var c := object Child { label := 'kid'; };
				        p.children += c;
				        log('parent:' + c.parent.name);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "parent:root");
	}

	// ---- derived modifier ----

	@Test
	void derived_modifier() throws Exception {
		// Just test that 'derived' parses and the feature is accessible
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Config {
				        derived x : String = 'computed';
				    }
				    main() {
				        var c := object Config {};
				        log('x:' + c.x);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "x:computed");
	}

	// ---- generic stereotypes <<id, readonly>> ----

	@Test
	void stereotype_generic() throws Exception {
		// Multiple stereotypes in a single <<...>> qualifier
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Entity {
				        <<id, readonly>> key : String;
				        name : String;
				    }
				    main() {
				        var e := object Entity { key := 'k1'; name := 'test'; };
				        log('key:' + e.key);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "key:k1");
	}

	// ---- ordered keyword ----

	@Test
	void ordered_keyword() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Container {
				        references items : Item[*] ordered;
				    }
				    intermediate class Item {
				        name : String;
				    }
				    main() {
				        var c := object Container {};
				        c.items += object Item { name := 'a'; };
				        c.items += object Item { name := 'b'; };
				        log('count:' + c.items->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "count:2");
	}

	// ---- forward reference (IC A → IC B, B defined after A) ----

	@Test
	void references_forwardRef() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class A {
				        name : String;
				        references ref : B;
				    }
				    intermediate class B {
				        label : String;
				    }
				    main() {
				        var b := object B { label := 'target'; };
				        var a := object A { name := 'src'; ref := b; };
				        log('ref:' + a.ref.label);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "ref:target");
	}

	// ---- classifier_operation (N9: method declaration in IC body) ----

	@Test
	void classifierOperation_parsesMethodDeclaration() throws Exception {
		// §8.4: <classifier_operation> ::= <feature_qualifier>? <declarator> <complete_signature>
		// Spec syntax: name ':' returnType '(' params ')' ';'
		// Declaration only — no body execution (like Eclipse).
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Shape {
				        name : String;
				        area : Real;
				        computeArea : Real (scale : Integer);
				    }
				    main() {
				        var s := object Shape { name := 'rect'; area := 42.0; };
				        log('name:' + s.name);
				        log('area:' + s.area.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "name:rect");
		assertLogged(result, "area:42");
	}

	@Test
	void classifierOperation_multipleOpsAndParams() throws Exception {
		// Multiple operations with multiple parameters
		// §8.4: <declarator> = name ':' returnType, <complete_signature> = '(' params ')'
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Calculator {
				        value : Integer = 0;
				        add : Integer (a : Integer, b : Integer);
				        reset : Calculator ();
				    }
				    main() {
				        var c := object Calculator {};
				        log('val:' + c.value.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "val:0");
	}

	// ---- Helpers ----

	private static void assertSuccess(QvtoExecutionResult result) {
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		boolean found = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains(expected));
		assertTrue(found, "Expected log containing '" + expected
				+ "' but diagnostics were: " + result.diagnostics());
	}
}
