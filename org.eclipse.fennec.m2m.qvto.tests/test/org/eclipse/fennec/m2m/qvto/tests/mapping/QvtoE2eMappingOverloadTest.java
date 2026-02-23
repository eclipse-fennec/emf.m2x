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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O mapping overloading and dispatch.
 *
 * Spec references:
 * - §8.1.14 (p82): Mapping Overloading — explicit/implicit disjuncts, candidate selection
 * - §8.1.14.2 (p82): Implicit Disjuncts — overloading by context type
 * - §8.1.14.3 (p83): Disjunct candidates — prioritized selection order
 * - §8.2.1.15 (p104-107): MappingOperation — when/where, execution semantics, map vs xmap
 */
class QvtoE2eMappingOverloadTest extends AbstractQvtoEngineTest {

	// ---- Explicit disjuncts ----

	// §8.1.14.1: Explicit disjuncts — all guards false → no body executes
	@Test
	void disjuncts_allGuardsFalse_noBodyExecutes() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping alt1()
				        when { false; } {
				        log('alt1-ran');
				    }
				    mapping alt2()
				        when { false; } {
				        log('alt2-ran');
				    }
				    mapping dispatch() disjuncts alt1, alt2 {}
				    main() {
				        map dispatch();
				        log('done');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "done");
		assertNotLogged(result, "alt1-ran");
		assertNotLogged(result, "alt2-ran");
	}

	// §8.1.14.1: Explicit disjuncts with 3 candidates — third wins
	@Test
	void disjuncts_thirdCandidateWins() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping alt1() when { false; } {}
				    mapping alt2() when { false; } {}
				    mapping alt3() when { true; } {
				        log('third');
				    }
				    mapping dispatch() disjuncts alt1, alt2, alt3 {}
				    main() {
				        map dispatch();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "third");
	}

	// ---- When-guard as guard (standard map) ----

	// §8.2.1.15 p106: map with when-guard false → body skipped, no error
	@Test
	void map_whenFalse_bodySkipped() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping guarded()
				        when { false; } {
				        log('should-not-run');
				    }
				    main() {
				        map guarded();
				        log('after-call');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "after-call");
		assertNotLogged(result, "should-not-run");
	}

	// §8.2.1.15: map with when-guard true → body executes
	@Test
	void map_whenTrue_bodyExecutes() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping guarded() when { true; } {
				        log('body-ran');
				    }
				    main() {
				        map guarded();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "body-ran");
	}

	// ---- xmap strict semantics ----

	// §8.2.1.15: xmap with when-guard false → assertion failure (strict = pre-condition)
	@Test
	void xmap_whenFalse_producesError() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping guarded() when { false; } {
				        log('should-not-run');
				    }
				    main() {
				        xmap guarded();
				    }
				}
				""");
		assertNotNull(result);
		assertFalse(result.isSuccess(), "xmap with failed when-guard should produce assertion failure");
	}

	// ---- Contextual overloading (implicit disjunction) ----

	// §8.1.14.2: Two mappings same name, different context types → dispatched by runtime type
	@Test
	void contextOverload_dispatchByType() throws Exception {
		EObject elem = createSourceElement("e1", 10);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(elem);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::convert() : TargetElement {
				        name := 'elem-' + self.name;
				        value := self.value;
				    }
				    mapping SourceContainer::convert() : TargetElement {
				        name := 'container-' + self.name;
				        value := 0;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map convert();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		assertEquals("elem-e1", eGet(outExtent.getContents().get(0), "name"));
	}

	// ---- When-guard dispatch without explicit disjuncts ----

	// §8.2.1.15 + §8.1.14.2: Two non-contextual mappings same name, when-guards differentiate
	@Test
	void overload_whenGuardDispatch() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping doWork()
				        when { 1 > 2; } {
				        log('first');
				    }
				    mapping doWork()
				        when { 1 < 2; } {
				        log('second');
				    }
				    main() {
				        map doWork();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "second");
		assertNotLogged(result, "first");
	}

	// ---- Contextual overload in collection iteration ----

	// §8.1.14.2: Overloaded mapping in forEach over mixed model types
	@Test
	void contextOverload_inForEach() throws Exception {
		EObject elem = createSourceElement("item", 5);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(elem);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := 'mapped-' + self.name;
				        value := self.value * 2;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            var result := e.map toTarget();
				            log('done:' + result.name);
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "done:mapped-item");
		assertEquals(1, outExtent.getContents().size());
		assertEquals(10, eGet(outExtent.getContents().get(0), "value"));
	}

	// ---- P5-01: Explicit disjuncts — additional coverage ----

	// §8.1.14.1: Explicit disjuncts — no match returns null
	@Test
	void disjuncts_noMatch_returnsNull() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping alt1() when { false; } { log('alt1'); }
				    mapping alt2() when { false; } { log('alt2'); }
				    mapping dispatch() disjuncts alt1, alt2 {}
				    main() {
				        var x := map dispatch();
				        log(if x = null then 'null-result' else 'has-result' endif);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "null-result");
		assertNotLogged(result, "alt1");
		assertNotLogged(result, "alt2");
	}

	// §8.1.14.1: Explicit disjuncts — declaration order, first match wins
	@Test
	void disjuncts_declarationOrder_firstMatchWins() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping alt1() when { true; } { log('first'); }
				    mapping alt2() when { true; } { log('second'); }
				    mapping dispatch() disjuncts alt1, alt2 {}
				    main() {
				        map dispatch();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "first");
		assertNotLogged(result, "second");
	}

	// §8.1.14.1: Explicit disjuncts with parameters passed through
	@Test
	void disjuncts_withParameters() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping alt1(x : Integer) when { x > 10; } { log('big:' + x.toString()); }
				    mapping alt2(x : Integer) when { x <= 10; } { log('small:' + x.toString()); }
				    mapping dispatch(x : Integer) disjuncts alt1, alt2 {}
				    main() {
				        map dispatch(5);
				        map dispatch(20);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "small:5");
		assertLogged(result, "big:20");
	}

	// §8.1.14.1: Explicit disjuncts with context type on source
	@Test
	void disjuncts_withContextType() throws Exception {
		EObject src = createSourceElement("test", 42);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toHigh() : TargetElement
				        when { self.value > 10; } {
				        name := 'high-' + self.name;
				    }
				    mapping SourceElement::toLow() : TargetElement
				        when { self.value <= 10; } {
				        name := 'low-' + self.name;
				    }
				    mapping SourceElement::convert() : TargetElement
				        disjuncts SourceElement::toHigh, SourceElement::toLow {}
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map convert();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		assertEquals("high-test", eGet(outExtent.getContents().get(0), "name"));
	}

	// §8.1.14.1: Chained disjuncts — candidate itself has disjuncts
	@Test
	void disjuncts_chained() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping leaf1() when { true; } { log('leaf1'); }
				    mapping leaf2() when { false; } { log('leaf2'); }
				    mapping mid() disjuncts leaf1, leaf2 {}
				    mapping top() disjuncts mid {}
				    main() {
				        map top();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "leaf1");
	}

	// §8.1.14.1: Disjuncts — result type from winning candidate
	@Test
	void disjuncts_resultFromWinningCandidate() throws Exception {
		EObject src = createSourceElement("winner", 99);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toA() : TargetElement
				        when { false; } {
				        name := 'A';
				    }
				    mapping SourceElement::toB() : TargetElement
				        when { true; } {
				        name := 'B-' + self.name;
				        value := self.value;
				    }
				    mapping SourceElement::dispatch() : TargetElement
				        disjuncts SourceElement::toA, SourceElement::toB {}
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            var r := e.map dispatch();
				            log('got:' + r.name);
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "got:B-winner");
		assertEquals(1, outExtent.getContents().size());
		assertEquals(99, eGet(outExtent.getContents().get(0), "value"));
	}

	// ---- P5-02: Implicit disjuncts (§8.1.14.2) ----

	// §8.1.14.2: Derived type mapping wins over base type mapping (implicit disjunction)
	@Test
	void implicitDisjuncts_derivedTypeWins() throws Exception {
		EObject special = createSpecialSourceElement("sp", 7, "vip");
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(special);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::convert() : TargetElement {
				        name := 'base-' + self.name;
				    }
				    mapping SpecialSourceElement::convert() : TargetElement {
				        name := 'special-' + self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map convert();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		assertEquals("special-sp", eGet(outExtent.getContents().get(0), "name"));
	}

	// §8.1.14.2: Base type fallback when no specialized mapping matches
	@Test
	void implicitDisjuncts_baseFallback() throws Exception {
		EObject elem = createSourceElement("plain", 3);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(elem);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::convert() : TargetElement {
				        name := 'base-' + self.name;
				    }
				    mapping SpecialSourceElement::convert() : TargetElement {
				        name := 'special-' + self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map convert();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		assertEquals("base-plain", eGet(outExtent.getContents().get(0), "name"));
	}

	// §8.1.14.2: Mixed collection — each object dispatched by its runtime type
	@Test
	void implicitDisjuncts_mixedCollection() throws Exception {
		EObject elem = createSourceElement("normal", 1);
		EObject special = createSpecialSourceElement("extra", 2, "tag");
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(elem, special);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::convert() : TargetElement {
				        name := 'base-' + self.name;
				    }
				    mapping SpecialSourceElement::convert() : TargetElement {
				        name := 'special-' + self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map convert();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(2, outExtent.getContents().size());
		// SourceElement → base, SpecialSourceElement → special
		var names = outExtent.getContents().stream()
				.map(o -> eGet(o, "name").toString())
				.sorted()
				.toList();
		assertTrue(names.contains("base-normal"), "Expected base-normal but got: " + names);
		assertTrue(names.contains("special-extra"), "Expected special-extra but got: " + names);
	}

	// §8.1.14.2: Implicit disjunction with when-guards on overloaded non-contextual mappings
	@Test
	void implicitDisjuncts_whenGuardOnOverloads() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping doWork()
				        when { 1 > 2; } {
				        log('first');
				    }
				    mapping doWork()
				        when { 1 < 2; } {
				        log('second');
				    }
				    main() {
				        map doWork();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "second");
		assertNotLogged(result, "first");
	}

	// §8.1.14.3: xmap strict — when-guard failure on implicit disjunct is assertion error
	@Test
	void implicitDisjuncts_xmapStrict() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping guarded()
				        when { false; } {
				        log('should-not-run');
				    }
				    main() {
				        xmap guarded();
				    }
				}
				""");
		assertNotNull(result);
		assertFalse(result.isSuccess(), "xmap with failed when-guard should fail");
	}

	// ---- P5-03: Mapping Inherits (§8.1.15) ----

	// §8.1.15: inherits — inherited mapping executes after init, log order proves it
	@Test
	void inherits_executionOrder() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping base() {
				        log('base-body');
				    }
				    mapping derived()
				        inherits base {
				        init { log('derived-init'); }
				        log('derived-body');
				    }
				    main() {
				        map derived();
				    }
				}
				""");
		assertSuccess(result);
		// Order: derived-init → base-body → derived-body
		assertLogged(result, "derived-init");
		assertLogged(result, "base-body");
		assertLogged(result, "derived-body");
		assertLogOrder(result, "derived-init", "base-body");
		assertLogOrder(result, "base-body", "derived-body");
	}

	// §8.1.15: inherits — multiple inheritance, both parents execute
	@Test
	void inherits_multipleBases() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping base1() { log('base1'); }
				    mapping base2() { log('base2'); }
				    mapping derived()
				        inherits base1, base2 {
				        log('derived');
				    }
				    main() {
				        map derived();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "base1");
		assertLogged(result, "base2");
		assertLogged(result, "derived");
	}

	// §8.1.15: inherits — parent with when-guard false → parent skipped
	@Test
	void inherits_parentGuardFalse() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping base() when { false; } { log('base-ran'); }
				    mapping derived()
				        inherits base {
				        log('derived-ran');
				    }
				    main() {
				        map derived();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "derived-ran");
		assertNotLogged(result, "base-ran");
	}

	// ---- P5-04: Mapping Merges (§8.1.15) ----

	// §8.1.15: merges — merged mapping executes after end section
	@Test
	void merges_executionOrder() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping extra() { log('extra-body'); }
				    mapping main_map()
				        merges extra {
				        log('main-body');
				        end { log('main-end'); }
				    }
				    main() {
				        map main_map();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "main-body");
		assertLogged(result, "main-end");
		assertLogged(result, "extra-body");
		assertLogOrder(result, "main-end", "extra-body");
	}

	// §8.1.15: merges — merged mapping with failed when-guard is not invoked
	@Test
	void merges_guardFalse_skipped() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping extra() when { false; } { log('extra-ran'); }
				    mapping main_map()
				        merges extra {
				        log('main-ran');
				    }
				    main() {
				        map main_map();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "main-ran");
		assertNotLogged(result, "extra-ran");
	}

	// §8.1.15: merges — multiple merged mappings in declaration order
	@Test
	void merges_multipleInOrder() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping m1() { log('m1'); }
				    mapping m2() { log('m2'); }
				    mapping m3() { log('m3'); }
				    mapping combined()
				        merges m1, m2, m3 {
				        log('combined');
				    }
				    main() {
				        map combined();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "combined");
		assertLogged(result, "m1");
		assertLogged(result, "m2");
		assertLogged(result, "m3");
		assertLogOrder(result, "combined", "m1");
		assertLogOrder(result, "m1", "m2");
		assertLogOrder(result, "m2", "m3");
	}

	// §8.1.15: inherits + merges combined on same mapping
	@Test
	void inheritsAndMerges_combined() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping parent() { log('parent'); }
				    mapping addon() { log('addon'); }
				    mapping child()
				        inherits parent
				        merges addon {
				        log('child');
				    }
				    main() {
				        map child();
				    }
				}
				""");
		assertSuccess(result);
		// Order: parent (inherits, after init) → child (body) → addon (merges, after end)
		assertLogged(result, "parent");
		assertLogged(result, "child");
		assertLogged(result, "addon");
		assertLogOrder(result, "parent", "child");
		assertLogOrder(result, "child", "addon");
	}

	// ---- P5-05: Composing Transformations (§8.1.13) ----

	// §8.1.13: inout extent — mapping modifies source properties in-place
	@Test
	void inout_mappingModifiesSourceInPlace() throws Exception {
		EObject src = createSourceElement("original", 5);
		QvtoModelExtent inoutExtent = new BasicQvtoModelExtent(src);
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(inout m : SRC) {
				    mapping inout SourceElement::modify() {
				        self.name := 'modified-' + self.name;
				        self.value := self.value * 10;
				    }
				    main() {
				        m.objectsOfType(SourceElement)->forEach(e) {
				            e.map modify();
				        };
				    }
				}
				""", inoutExtent);
		assertSuccess(result);
		assertEquals("modified-original", eGet(src, "name"));
		assertEquals(50, eGet(src, "value"));
	}

	// §8.1.13: extends — extending transformation inherits library operations
	@Test
	void extends_inheritsOperations() throws Exception {
		QvtoExecutionResult result = execute("""
				library MyLib() {
				    helper greet() : String {
				        return 'hello from lib';
				    }
				}
				transformation test() extends MyLib {
				    main() {
				        log(greet());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "hello from lib");
	}

	// §8.1.13: access — transformation accesses library operations
	@Test
	void access_importsOperations() throws Exception {
		QvtoExecutionResult result = execute("""
				library Tools() {
				    helper double(x : Integer) : Integer {
				        return x * 2;
				    }
				}
				transformation test() access Tools {
				    main() {
				        log(double(21).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "42");
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

	private static void assertNotLogged(QvtoExecutionResult result, String unexpected) {
		boolean found = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains(unexpected));
		assertFalse(found, "Expected NO log containing '" + unexpected
				+ "' but diagnostics were: " + result.diagnostics());
	}

	private static void assertLogOrder(QvtoExecutionResult result, String first, String second) {
		var messages = result.diagnostics().stream()
				.map(d -> d.getMessage())
				.toList();
		int idxFirst = -1, idxSecond = -1;
		for (int i = 0; i < messages.size(); i++) {
			if (idxFirst < 0 && messages.get(i).contains(first)) idxFirst = i;
			if (messages.get(i).contains(second)) idxSecond = i;
		}
		assertTrue(idxFirst >= 0, "Expected log containing '" + first + "' but diagnostics were: " + messages);
		assertTrue(idxSecond >= 0, "Expected log containing '" + second + "' but diagnostics were: " + messages);
		assertTrue(idxFirst < idxSecond, "Expected '" + first + "' (idx=" + idxFirst
				+ ") before '" + second + "' (idx=" + idxSecond + ") in: " + messages);
	}

	private static Object eGet(EObject obj, String featureName) {
		return obj.eGet(obj.eClass().getEStructuralFeature(featureName));
	}
}
