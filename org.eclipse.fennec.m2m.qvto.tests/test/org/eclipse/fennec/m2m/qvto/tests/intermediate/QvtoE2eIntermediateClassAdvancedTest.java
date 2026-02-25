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
package org.eclipse.fennec.m2m.qvto.tests.intermediate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for advanced intermediate class features.
 *
 * <p>Based on Eclipse reference tests:
 * <ul>
 *   <li>{@code intermProperties/intermProperties.qvto} — static, readonly, opposite, {@literal <<id>>}</li>
 *   <li>{@code intermWithCrossRefs/intermWithCrossRefs.qvto} — self-references, cross-refs</li>
 *   <li>{@code intermWithExtends/intermWithExtends.qvto} — extends intermediate + metamodel class</li>
 * </ul>
 *
 * <p><b>SPEC-FIRST:</b> Tests written against QVT-O v1.3 §8.1.10 (Intermediate Data),
 * §8.2.1.4 (OperationalTransformation::intermediateClass), and §8.4 concrete syntax.
 * Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eIntermediateClassAdvancedTest extends AbstractQvtoEngineTest {

	// ==== P9-02: Intermediate Class Advanced ====

	// ---- Default values in intermediate class properties ----

	@Test
	void intermediateClass_defaultPropertyValues() throws Exception {
		// Eclipse: intermediate class MyClass { a : Integer = 5; b : String = 'a'; }
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Config {
				        maxRetries : Integer = 3;
				        label : String = 'default';
				    }
				    main() {
				        var c := object Config {};
				        log('retries:' + c.maxRetries.repr());
				        log('label:' + c.label);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "retries:3");
		assertLogged(result, "label:default");
	}

	// ---- Default value overridden by object expression assignment ----

	@Test
	void intermediateClass_defaultOverriddenByAssignment() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Config {
				        maxRetries : Integer = 3;
				        label : String = 'default';
				    }
				    main() {
				        var c := object Config { maxRetries := 10; };
				        log('retries:' + c.maxRetries.repr());
				        log('label:' + c.label);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "retries:10");
		assertLogged(result, "label:default");
	}

	// ---- Intermediate class inheritance ----

	@Test
	void intermediateClass_inheritance() throws Exception {
		// Eclipse: intermediate class MyDerived extends MyBaseClass
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Base {
				        name : String = 'base';
				    }
				    intermediate class Derived extends Base {
				        extra : String = 'ext';
				    }
				    main() {
				        var d := object Derived {};
				        log('name:' + d.name);
				        log('extra:' + d.extra);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "name:base");
		assertLogged(result, "extra:ext");
	}

	// ---- Inherited property override in object expression ----

	@Test
	void intermediateClass_inheritedPropertyOverride() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Base {
				        name : String = 'base';
				    }
				    intermediate class Derived extends Base {
				        extra : Integer = 0;
				    }
				    main() {
				        var d := object Derived { name := 'overridden'; extra := 42; };
				        log('name:' + d.name);
				        log('extra:' + d.extra.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "name:overridden");
		assertLogged(result, "extra:42");
	}

	// ---- Collection-typed property ----

	@Test
	void intermediateClass_collectionProperty() throws Exception {
		// Eclipse: intermediate class MyClass2 { ab : Sequence(String) = Sequence{'1', '2'} }
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Tags {
				        items : Sequence(String) = Sequence{'a', 'b'};
				    }
				    main() {
				        var t := object Tags {};
				        log('size:' + t.items->size().repr());
				        log('first:' + t.items->at(1));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:2");
		assertLogged(result, "first:a");
	}

	// ---- oclIsTypeOf / oclIsKindOf on intermediate classes ----

	@Test
	void intermediateClass_oclIsTypeOf() throws Exception {
		// Eclipse: assert fatal (v0.oclIsTypeOf(MyBase))
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class MyBase {
				        n : String = 'n';
				    }
				    intermediate class MyChild extends MyBase {
				        extra : Integer = 0;
				    }
				    main() {
				        var b := object MyBase {};
				        var c := object MyChild {};
				        log('b-typeOf-Base:' + b.oclIsTypeOf(MyBase).toString());
				        log('c-typeOf-Child:' + c.oclIsTypeOf(MyChild).toString());
				        log('c-typeOf-Base:' + c.oclIsTypeOf(MyBase).toString());
				        log('c-kindOf-Base:' + c.oclIsKindOf(MyBase).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "b-typeOf-Base:true");
		assertLogged(result, "c-typeOf-Child:true");
		// oclIsTypeOf is exact — Child is NOT exactly Base
		assertLogged(result, "c-typeOf-Base:false");
		// oclIsKindOf includes supertypes — Child IS a kind of Base
		assertLogged(result, "c-kindOf-Base:true");
	}

	// ---- Static property (shared across all instances) ----

	@Test
	void intermediateClass_staticProperty() throws Exception {
		// Eclipse: static staticData : String = '1'; — shared across all instances
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Shared {
				        static counter : Integer = 0;
				        name : String;
				    }
				    main() {
				        var a := object Shared { name := 'a'; };
				        var b := object Shared { name := 'b'; };
				        a.counter := 42;
				        log('a:' + a.counter.repr());
				        log('b:' + b.counter.repr());
				    }
				}
				""");
		assertSuccess(result);
		// Static property is shared — setting on 'a' also visible on 'b'
		assertLogged(result, "a:42");
		assertLogged(result, "b:42");
	}

	// ---- Readonly property with default value ----

	@Test
	void intermediateClass_readonlyProperty() throws Exception {
		// Eclipse: readonly defStrings : Set(String)[0 .. 1] = Set{'1'};
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Config {
				        readonly version : String = '1.0';
				        name : String;
				    }
				    main() {
				        var c := object Config { name := 'test'; };
				        log('version:' + c.version);
				        log('name:' + c.name);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "version:1.0");
		assertLogged(result, "name:test");
	}

	// ---- Cross-references between intermediate class instances ----

	@Test
	void intermediateClass_crossReferences() throws Exception {
		// Eclipse intermWithCrossRefs: refBase property pointing to another instance
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Node {
				        name : String;
				        parent : Node;
				    }
				    main() {
				        var root := object Node { name := 'root'; };
				        var child := object Node { name := 'child'; parent := root; };
				        log('child-parent:' + child.parent.name);
				        log('root-parent-null:' + (root.parent = null).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "child-parent:root");
		assertLogged(result, "root-parent-null:true");
	}

	// ---- Self-reference (refSelf := result in mapping) ----

	@Test
	void intermediateClass_selfReferenceInMapping() throws Exception {
		// Eclipse intermWithCrossRefs: refSelf := result
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Node {
				        name : String;
				        selfRef : Node;
				    }
				    mapping Node::linkSelf() : Node {
				        name := self.name + '-linked';
				        selfRef := result;
				    }
				    main() {
				        var n := object Node { name := 'orig'; };
				        var linked := n.map linkSelf();
				        log('name:' + linked.name);
				        log('self-ref:' + (linked.selfRef = linked).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "name:orig-linked");
		assertLogged(result, "self-ref:true");
	}

	// ---- Intermediate class instance in collection ----

	@Test
	void intermediateClass_instancesInCollection() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Item { label : String; }
				    main() {
				        var items : Sequence(Item) := Sequence{};
				        items += object Item { label := 'x'; };
				        items += object Item { label := 'y'; };
				        items += object Item { label := 'z'; };
				        log('count:' + items->size().repr());
				        items->forEach(i) {
				            log(i.label);
				        };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "count:3");
		assertLoggedInOrder(result, "x", "y", "z");
	}

	// ---- Intermediate class with <<id>> stereotype ----

	@Test
	@org.junit.jupiter.api.Disabled("Parser gap: <<id>> stereotype syntax not yet supported")
	void intermediateClass_idStereotype() throws Exception {
		// Eclipse: <<id>> uuid:String [1]; — a mandatory qualifying uuid attribute
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Entity {
				        <<id>> uuid : String;
				        name : String;
				    }
				    main() {
				        var e := object Entity { uuid := 'abc-123'; name := 'test'; };
				        log('uuid:' + e.uuid);
				        log('name:' + e.name);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "uuid:abc-123");
		assertLogged(result, "name:test");
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

	private static void assertLoggedInOrder(QvtoExecutionResult result, String... expected) {
		var messages = result.diagnostics().stream()
				.map(d -> d.getMessage())
				.toList();
		int lastIdx = -1;
		for (String exp : expected) {
			boolean found = false;
			for (int i = lastIdx + 1; i < messages.size(); i++) {
				if (messages.get(i).contains(exp)) {
					lastIdx = i;
					found = true;
					break;
				}
			}
			assertTrue(found, "Expected log containing '" + exp
					+ "' after index " + lastIdx + " but diagnostics were: " + messages);
		}
	}
}
