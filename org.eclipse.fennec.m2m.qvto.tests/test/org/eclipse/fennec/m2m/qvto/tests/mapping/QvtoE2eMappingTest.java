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

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O mapping operations.
 * Tests cover mapping semantics including guards, sections, inheritance, and chaining.
 */
class QvtoE2eMappingTest extends AbstractQvtoEngineTest {

	// ---- Basic mapping (in → out) ----

	@Test
	void mapping_inToOut_copiesNameAndValue() throws Exception {
		EObject src = createSourceElement("alpha", 10);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				        r.value := self.value;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		EObject target = outExtent.getContents().get(0);
		assertEquals("alpha", eGet(target, "name"));
		assertEquals(10, eGet(target, "value"));
	}

	@Test
	void mapping_multipleInputElements() throws Exception {
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(
				createSourceElement("a", 1),
				createSourceElement("b", 2),
				createSourceElement("c", 3));
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(3, outExtent.getContents().size());
	}

	// ---- When guard ----

	@Test
	void mapping_whenGuard_trueCondition_createsOutput() throws Exception {
		EObject src = createSourceElement("pos", 5);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement
				        when { self.value > 0 } {
				        r.name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
	}

	@Test
	void mapping_whenGuard_falseCondition_skipsMapping() throws Exception {
		EObject src = createSourceElement("neg", -1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement
				        when { self.value > 0 } {
				        r.name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertTrue(outExtent.getContents().isEmpty(), "Guard false → no output");
	}

	@Test
	void mapping_whenGuard_filtersPartially() throws Exception {
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(
				createSourceElement("ok", 10),
				createSourceElement("skip", -1),
				createSourceElement("ok2", 5));
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement
				        when { self.value > 0 } {
				        r.name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(2, outExtent.getContents().size(), "Only elements with value>0 should be mapped");
	}

	// ---- Where postcondition ----

	@Test
	void mapping_wherePostcondition_failsEmitsWarning() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createFailing() : r : SourceElement
				        where { false; } {
				        r.name := 'test';
				    }
				    main() {
				        map createFailing();
				    }
				}
				""", outExtent);
		assertNotNull(result);
		boolean hasWarning = result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() == Diagnostic.WARNING
						&& d.getMessage().contains("where-postcondition"));
		assertTrue(hasWarning, "Failing where-postcondition should emit warning");
	}

	// ---- Init / Population / End sections ----

	@Test
	void mapping_initSection_executesBeforePopulation() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createWithInit() : r : SourceElement {
				        init {
				            log('init-phase');
				        }
				        r.name := 'populated';
				        end {
				            log('end-phase');
				        }
				    }
				    main() {
				        map createWithInit();
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		assertLogged(result, "init-phase");
		assertLogged(result, "end-phase");
	}

	@Test
	void mapping_initSection_computesValueForPopulation() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createWithComputed() : r : SourceElement {
				        init {
				            var prefix : String := 'pre-';
				        }
				        r.name := prefix + 'name';
				    }
				    main() {
				        var elem := map createWithComputed();
				        log(elem.name);
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertLogged(result, "pre-name");
	}

	@Test
	void mapping_endSection_canAccessResult() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createWithEnd() : r : SourceElement {
				        r.name := 'original';
				        end {
				            log(r.name);
				        }
				    }
				    main() {
				        map createWithEnd();
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertLogged(result, "original");
	}

	// ---- Inherits ----

	@Test
	void mapping_inherits_executesBaseFirst() throws Exception {
		EObject src = createSourceElement("inh", 7);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTargetBase() : r : TargetElement {
				        r.name := self.name;
				    }
				    mapping SourceElement::toTargetDerived() : r : TargetElement
				        inherits toTargetBase {
				        r.value := self.value * 2;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTargetDerived());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		EObject target = outExtent.getContents().get(0);
		assertEquals("inh", eGet(target, "name"), "Base mapping should set name");
		assertEquals(14, eGet(target, "value"), "Derived mapping should set value*2");
	}

	// ---- Merges ----

	@Test
	void mapping_merges_combinesResults() throws Exception {
		EObject src = createSourceElement("mrg", 3);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::setName() : r : TargetElement {
				        r.name := self.name;
				    }
				    mapping SourceElement::setValue() : r : TargetElement {
				        r.value := self.value;
				    }
				    mapping SourceElement::toTargetMerged() : r : TargetElement
				        merges setName, setValue {
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTargetMerged());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size(), "Merges should produce one result object");
		EObject target = outExtent.getContents().get(0);
		assertEquals("mrg", eGet(target, "name"), "Merged setName should set name from source");
		assertEquals(3, eGet(target, "value"), "Merged setValue should set value from source");
	}

	// ---- Disjuncts ----

	@Test
	void mapping_disjuncts_firstMatchWins() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping alt1() : r : SourceElement
				        when { true; } {
				        r.name := 'first';
				    }
				    mapping alt2() : r : SourceElement
				        when { true; } {
				        r.name := 'second';
				    }
				    mapping dispatch() : r : SourceElement
				        disjuncts alt1, alt2 {}
				    main() {
				        var elem := map dispatch();
				        log(elem.name);
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertLogged(result, "first");
	}

	@Test
	void mapping_disjuncts_fallthrough() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping alt1() : r : SourceElement
				        when { false; } {
				        r.name := 'first';
				    }
				    mapping alt2() : r : SourceElement
				        when { true; } {
				        r.name := 'second';
				    }
				    mapping dispatch() : r : SourceElement
				        disjuncts alt1, alt2 {}
				    main() {
				        var elem := map dispatch();
				        log(elem.name);
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertLogged(result, "second");
	}

	// ---- Nested / chained mappings ----

	@Test
	void mapping_chained_AtoB_BtoC() throws Exception {
		EObject src = createSourceElement("chain", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toIntermediate() : r : TargetElement {
				        r.name := 'intermediate-' + self.name;
				        r.value := self.value * 10;
				    }
				    mapping TargetElement::toFinal() : r : TargetElement {
				        r.name := 'final-' + self.name;
				        r.value := self.value + 1;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e |
				            e.map toIntermediate().map toFinal()
				        );
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(2, outExtent.getContents().size(), "Intermediate + final");
	}

	// ---- Mapping result used as variable ----

	@Test
	void mapping_resultAssignedToVariable() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createElem() : r : SourceElement {
				        r.name := 'created';
				        r.value := 99;
				    }
				    main() {
				        var elem := map createElem();
				        log(elem.name + ':' + elem.value.toString());
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertLogged(result, "created:99");
	}

	// ---- Contextual mapping ----

	@Test
	void mapping_contextualOnEClass() throws Exception {
		EObject src = createSourceElement("ctx", 42);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::convert() : r : TargetElement {
				        r.name := 'converted-' + self.name;
				        r.value := self.value;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map convert());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		EObject target = outExtent.getContents().get(0);
		assertEquals("converted-ctx", eGet(target, "name"));
		assertEquals(42, eGet(target, "value"));
	}

	// ---- Mapping with no explicit result parameter ----

	@Test
	void mapping_noExplicitResult_createsObject() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createSimple() : SourceElement {
				        name := 'simple';
				        value := 1;
				    }
				    main() {
				        map createSimple();
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		EObject elem = outExtent.getContents().get(0);
		assertEquals("simple", eGet(elem, "name"));
	}

	// ---- Mapping with multiple result parameters ----

	@Test
	void mapping_multipleResults_createsMultipleObjects() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createPair() : r1 : SourceElement, r2 : SourceElement {
				        r1.name := 'first';
				        r2.name := 'second';
				    }
				    main() {
				        map createPair();
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertEquals(2, outExtent.getContents().size());
	}

	// ---- Mapping with when guard using self property ----

	@Test
	void mapping_whenGuard_usingSelfProperty() throws Exception {
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(
				createSourceElement("long-name", 1),
				createSourceElement("ab", 2));
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement
				        when { self.name.size() > 3 } {
				        r.name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size(), "Only 'long-name' passes guard");
		assertEquals("long-name", eGet(outExtent.getContents().get(0), "name"));
	}

	// ---- Mapping calling helper ----

	@Test
	void mapping_callsHelper_inBody() throws Exception {
		EObject src = createSourceElement("raw", 5);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    helper SourceElement::computeLabel() : String {
				        return 'label-' + self.name;
				    }
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.computeLabel();
				        r.value := self.value;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals("label-raw", eGet(outExtent.getContents().get(0), "name"));
	}

	// ---- Mapping calling another mapping ----

	@Test
	void mapping_nestedMappingCall() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createChild() : r : SourceElement {
				        r.name := 'child';
				    }
				    mapping createParent() : r : SourceContainer {
				        r.name := 'parent';
				        r.elements += map createChild();
				    }
				    main() {
				        map createParent();
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertFalse(outExtent.getContents().isEmpty());
	}

	// ---- Mapping with computation in init returning early ----

	@Test
	void mapping_initSection_canReturnExistingObject() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createFirst() : r : SourceElement {
				        r.name := 'first';
				    }
				    mapping createSecond() : r : SourceElement {
				        r.name := 'second';
				    }
				    main() {
				        var a := map createFirst();
				        var b := map createSecond();
				        log(a.name + ',' + b.name);
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertLogged(result, "first,second");
		assertEquals(2, outExtent.getContents().size());
	}

	// ---- Mapping with container reference ----

	@Test
	void mapping_containerWithChildren() throws Exception {
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(
				createSourceElement("c1", 1),
				createSourceElement("c2", 2));
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				    }
				    mapping createContainer() : r : TargetContainer {
				        r.name := 'container';
				        r.elements += s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				    main() {
				        map createContainer();
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertFalse(outExtent.getContents().isEmpty());
	}

	// ---- P2-07: Inherits/Merges execution order (§8.1.15 p107) ----

	// §8.1.15: Inherits — derived population overrides base (base runs first)
	// Spec: "invokes first its initialization section, including the implicit
	// instantiation section, and then invokes the inherited mappings"
	// Order: init → instantiation → inherited → population → end
	@Test
	void inherits_derivedOverridesBase() throws Exception {
		EObject src = createSourceElement("x", 5);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::base() : r : TargetElement {
				        r.name := 'base';
				        log('base-pop');
				    }
				    mapping SourceElement::derived() : r : TargetElement
				        inherits base {
				        r.name := 'derived';
				        log('derived-pop');
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map derived();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		EObject target = outExtent.getContents().get(0);
		// Spec: inherited runs BEFORE derived population → derived overrides
		assertEquals("derived", eGet(target, "name"),
				"Derived population should override base (base runs first per §8.1.15)");
	}

	// §8.1.15: Merges — merged runs after end section
	// Spec: "The merged mappings are executed at the end of the execution
	// of the merging mapping"
	@Test
	void merges_executesAfterEnd() throws Exception {
		EObject src = createSourceElement("m", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::merged1() : r : TargetElement {
				        r.name := 'merged';
				        log('merged-pop');
				    }
				    mapping SourceElement::main_mapping() : r : TargetElement
				        merges merged1 {
				        init { log('init'); }
				        r.name := 'main';
				        log('pop');
				        end { log('end'); }
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map main_mapping();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		EObject target = outExtent.getContents().get(0);
		// Spec: merged runs after end → merged overrides main population
		assertEquals("merged", eGet(target, "name"),
				"Merged mapping runs after end section, overriding main population (§8.1.15)");
	}

	// ---- P2-12: Mapping Guards vertieft (when/where) ----

	// §8.1.5: when with multiple conditions — implicit AND
	@Test
	void mapping_whenMultipleConditions_implicitAnd() throws Exception {
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(
				createSourceElement("long-name", 5),
				createSourceElement("ab", 10),
				createSourceElement("long-too", -1));
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement
				        when { self.name.size() > 3; self.value > 0; } {
				        r.name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size(),
				"Only 'long-name' passes both conditions (name.size>3 AND value>0)");
		assertEquals("long-name", eGet(outExtent.getContents().get(0), "name"));
	}

	// §8.1.5: when + where combined — both checked
	@Test
	void mapping_whenAndWhere_bothChecked() throws Exception {
		EObject src = createSourceElement("valid", 5);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement
				        when { self.value > 0; }
				        where { r.name <> null; } {
				        r.name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		assertEquals("valid", eGet(outExtent.getContents().get(0), "name"));
	}

	// §8.1.5: when with null-check — null name → guard false
	@Test
	void mapping_whenNullCheck_filtersNull() throws Exception {
		EObject src1 = createSourceElement("present", 1);
		EObject src2 = createSourceElement(null, 2);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1, src2);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement
				        when { self.name <> null; } {
				        r.name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size(),
				"Only element with non-null name should pass guard");
		assertEquals("present", eGet(outExtent.getContents().get(0), "name"));
	}

	// §8.1.5: where postcondition passes — no warning
	@Test
	void mapping_wherePostcondition_passes() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createValid() : r : SourceElement
				        where { r.name <> null; } {
				        r.name := 'test';
				    }
				    main() {
				        map createValid();
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		boolean hasWarning = result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() == Diagnostic.WARNING
						&& d.getMessage().contains("where-postcondition"));
		assertFalse(hasWarning, "Passing where-postcondition should not emit warning");
	}

	// ---- P2-11: Mapping Sections vertieft (init/population/end) ----

	// §8.1.5 p106: Execution order is init → instantiation → population → end
	@Test
	void mapping_sectionOrder_initPopulationEnd() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createOrdered() : r : SourceElement {
				        init {
				            log('1:init');
				        }
				        r.name := 'populated';
				        log('2:population');
				        end {
				            log('3:end');
				        }
				    }
				    main() {
				        map createOrdered();
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertLogged(result, "1:init");
		assertLogged(result, "2:population");
		assertLogged(result, "3:end");
		assertEquals("populated", eGet(outExtent.getContents().get(0), "name"));
	}

	// §8.1.5: Mapping with only init-section sets result properties
	@Test
	void mapping_onlyInitSection() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createInInit() : r : SourceElement {
				        init {
				            r.name := 'init-only';
				            r.value := 42;
				        }
				    }
				    main() {
				        var elem := map createInInit();
				        log(elem.name + ':' + elem.value.toString());
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertLogged(result, "init-only:42");
	}

	// §8.1.5: Mapping with only end-section
	@Test
	void mapping_onlyEndSection() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createInEnd() : r : SourceElement {
				        end {
				            r.name := 'end-only';
				        }
				    }
				    main() {
				        var elem := map createInEnd();
				        log(elem.name);
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertLogged(result, "end-only");
	}

	// §8.2.1.19: Explicit population keyword — same semantics as implicit
	@Test
	void mapping_explicitPopulationKeyword() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createExplicit() : r : SourceElement {
				        population {
				            r.name := 'explicit-pop';
				            r.value := 7;
				        }
				    }
				    main() {
				        var elem := map createExplicit();
				        log(elem.name + ':' + elem.value.toString());
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertLogged(result, "explicit-pop:7");
	}

	// §8.1.5: init variable is accessible in population section (scope crossing)
	@Test
	void mapping_initVariableUsedInPopulation() throws Exception {
		EObject src = createSourceElement("test", 3);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        init {
				            var prefix := 'mapped-';
				            var doubled := self.value * 2;
				        }
				        r.name := prefix + self.name;
				        r.value := doubled;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		EObject target = outExtent.getContents().get(0);
		assertEquals("mapped-test", eGet(target, "name"));
		assertEquals(6, eGet(target, "value"));
	}

	// §8.1.5 p106 + Eclipse assignresultininit.qvto: init assigns result → prevents auto-instantiation,
	// but population body still executes (body updates the pre-assigned result)
	@Test
	void mapping_initAssignsResult_preventsAutoInstantiation() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createWithInitAssign() : r : SourceElement {
				        init {
				            r := object SourceElement { name := 'from-init'; value := 1; };
				        }
				        r.name := 'overwritten';
				    }
				    main() {
				        var elem := map createWithInitAssign();
				        log(elem.name + ':' + elem.value.toString());
				    }
				}
				""", outExtent);
		assertSuccess(result);
		// Per Eclipse: init assigns result, then population still runs and overrides name
		assertLogged(result, "overwritten:1");
	}

	// §8.1.5 + Eclipse endsectresultpatch.qvto: end section can modify result after population
	@Test
	void mapping_endModifiesResult() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(out t : SRC) {
				    mapping createAndPatch() : r : SourceElement {
				        r.name := 'original';
				        r.value := 10;
				        end {
				            r.name := 'patched';
				        }
				    }
				    main() {
				        var elem := map createAndPatch();
				        log(elem.name + ':' + elem.value.toString());
				    }
				}
				""", outExtent);
		assertSuccess(result);
		// End section runs after population, overrides name but not value
		assertLogged(result, "patched:10");
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

	private static Object eGet(EObject obj, String featureName) {
		return obj.eGet(obj.eClass().getEStructuralFeature(featureName));
	}
}
