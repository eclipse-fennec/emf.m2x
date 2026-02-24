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
package org.eclipse.fennec.m2m.qvto.tests.mapping;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for inout mapping semantics (P9-06).
 *
 * <p>Based on QVT-O v1.3 §8.1.6 (Mapping Operations), §8.1.11.2
 * (inhibition/instantiation) and Eclipse reference tests:
 * <ul>
 *   <li>{@code inoutMapping/inoutMapping.qvto} — inout self, no result, with result</li>
 *   <li>{@code inoutcontextparam/inoutcontextparam.qvto} — result := self pattern</li>
 *   <li>{@code inoutcontextparamnoresult/inoutcontextparamnoresult.qvto} — void inout</li>
 * </ul>
 *
 * <p><b>SPEC-FIRST:</b> Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eInoutMappingTest extends AbstractQvtoEngineTest {

	// ==== P9-06: inout Mapping Semantics ====

	// ---- inout mapping modifies self, no result type (void) ----

	@Test
	void inout_modifiesSelfNoResult() throws Exception {
		// Eclipse inoutcontextparamnoresult: mapping inout EPackage::foo() { self.name := 'right'; }
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Node { name : String; value : Integer; }
				    mapping inout Node::increment() {
				        self.value := self.value + 1;
				    }
				    main() {
				        var n := object Node { name := 'a'; value := 10; };
				        n.map increment();
				        log('value:' + n.value.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "value:11");
	}

	// ---- inout mapping with result type ----

	@Test
	void inout_withResultType() throws Exception {
		// Eclipse inoutMapping: mapping inout EPackage::updateSelfWithResult() : EClass
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Container { name : String; }
				    intermediate class Item { label : String; }
				    mapping inout Container::addItem() : Item {
				        result := object Item { label := 'added-to-' + self.name; };
				    }
				    main() {
				        var c := object Container { name := 'box'; };
				        var item := c.map addItem();
				        log('item:' + item.label);
				        log('container:' + c.name);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "item:added-to-box");
		assertLogged(result, "container:box");
	}

	// ---- inout mapping called multiple times on same object ----

	@Test
	void inout_calledMultipleTimes() throws Exception {
		// inout mappings modify self — calling multiple times accumulates changes
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Counter { count : Integer; }
				    mapping inout Counter::bump() {
				        self.count := self.count + 1;
				    }
				    main() {
				        var c := object Counter { count := 0; };
				        c.map bump();
				        c.map bump();
				        c.map bump();
				        log('count:' + c.count.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "count:3");
	}

	// ---- inout parameter (non-context) ----

	@Test
	void inout_parameterModified() throws Exception {
		// Eclipse inoutMapping: mapping inout EPackage::passedNullInout(inout eClass : EClass)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Data { value : Integer; }
				    helper modify(inout d : Data) : Void {
				        d.value := d.value * 2;
				    }
				    main() {
				        var d := object Data { value := 5; };
				        modify(d);
				        log('value:' + d.value.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "value:10");
	}

	// ---- inout mapping: self properties accessible without self. prefix ----

	@Test
	void inout_implicitSelfAccess() throws Exception {
		// In inout mapping body, self properties can be accessed implicitly
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Node { name : String; }
				    mapping inout Node::rename() {
				        name := 'renamed';
				    }
				    main() {
				        var n := object Node { name := 'original'; };
				        n.map rename();
				        log('name:' + n.name);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "name:renamed");
	}

	// ---- inout mapping in init section ----

	@Test
	void inout_modifySelfInInit() throws Exception {
		// Eclipse inoutMapping: mapping inout EPackage::updateSelfWithNoResult()
		// uses init { object self: { ... } }
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Box { label : String; count : Integer; }
				    mapping inout Box::setup() {
				        init {
				            self.label := 'initialized';
				            self.count := 42;
				        }
				    }
				    main() {
				        var b := object Box { label := 'empty'; count := 0; };
				        b.map setup();
				        log('label:' + b.label);
				        log('count:' + b.count.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "label:initialized");
		assertLogged(result, "count:42");
	}

	// ---- inout mapping with end section ----

	@Test
	void inout_endSectionExecuted() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Tracker { steps : Sequence(String); }
				    mapping inout Tracker::process() {
				        init {
				            self.steps += 'init';
				        }
				        self.steps += 'body';
				        end {
				            self.steps += 'end';
				        }
				    }
				    main() {
				        var t := object Tracker { steps := Sequence {}; };
				        t.map process();
				        t.steps->forEach(s) { log(s); };
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "init", "body", "end");
	}

	// ---- inout mapping: result := self pattern ----

	@Test
	void inout_resultEqualsSelf() throws Exception {
		// Eclipse inoutcontextparam: result := self; — returns same object
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Node { name : String; }
				    mapping inout Node::transform() : Node {
				        self.name := 'transformed';
				        result := self;
				    }
				    main() {
				        var n := object Node { name := 'orig'; };
				        var r := n.map transform();
				        log('same:' + (r = n).toString());
				        log('name:' + n.name);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "same:true");
		assertLogged(result, "name:transformed");
	}

	// ---- inout mapping: chained calls ----

	@Test
	void inout_chainedMappings() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Pipeline { stage : String; }
				    mapping inout Pipeline::step1() {
				        self.stage := self.stage + '>step1';
				    }
				    mapping inout Pipeline::step2() {
				        self.stage := self.stage + '>step2';
				    }
				    main() {
				        var p := object Pipeline { stage := 'start'; };
				        p.map step1();
				        p.map step2();
				        log('stage:' + p.stage);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "stage:start>step1>step2");
	}

	// ---- inout mapping: self unchanged when body is empty ----

	@Test
	void inout_emptyBodyNoChange() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Thing { val : Integer; }
				    mapping inout Thing::noop() {
				    }
				    main() {
				        var t := object Thing { val := 99; };
				        t.map noop();
				        log('val:' + t.val.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "val:99");
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
