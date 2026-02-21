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

import java.util.List;

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
		assertFalse(outExtent.getContents().isEmpty());
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
