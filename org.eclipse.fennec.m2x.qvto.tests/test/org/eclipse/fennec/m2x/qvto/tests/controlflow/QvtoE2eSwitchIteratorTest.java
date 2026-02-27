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
package org.eclipse.fennec.m2x.qvto.tests.controlflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * E2E tests for N6: switch with iterator declarator (§8.4.7 / §8.2.2.8).
 * Arrow-form based on Eclipse QVT-O bug219075_1.qvto test pattern.
 * Standalone declarator: spec-conformant extension beyond Eclipse.
 */
class QvtoE2eSwitchIteratorTest extends AbstractQvtoEngineTest {

	// ==================== Standalone switch with iterator ====================

	@Test
	void switchWithIterator_bindsVariableInCaseConditions() throws QvtoParseException {
		// §8.4.7: switch (x := expr) { case (x > 0) ... } — x bound as local variable
		QvtoExecutionResult result = execute("""
				transformation test();
				main() {
				    var label := switch (x := 5) {
				        case (x > 10) 'big';
				        case (x > 0) 'small';
				        else 'zero';
				    };
				    assert fatal (label = 'small') with log('expected small, got: ' + label.repr());
				}
				""");
		assertTrue(result.isSuccess(), () -> "Transformation should succeed: " + result.diagnostics());
	}

	@Test
	void switchWithIterator_elseMatchesWhenNoCaseHits() throws QvtoParseException {
		QvtoExecutionResult result = execute("""
				transformation test();
				main() {
				    var label := switch (x := 0) {
				        case (x > 10) 'big';
				        case (x > 0) 'small';
				        else 'zero';
				    };
				    assert fatal (label = 'zero') with log('expected zero, got: ' + label.repr());
				}
				""");
		assertTrue(result.isSuccess(), () -> "Transformation should succeed: " + result.diagnostics());
	}

	// ==================== Arrow-switch form ====================

	@Test
	void arrowSwitch_iteratesAndMatchesCases() throws QvtoParseException {
		// Eclipse bug219075_1 pattern: coll->switch(cls) { case (cls.name = 'a') ...; else ...; }
		String qvto = """
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				main() {
				    var elems := s.objectsOfType(source::SourceElement);
				    elems->switch (x) {
				        case (x.name = 'A') object target::TargetElement { name := x.name + ' -> 1'; };
				        case (x.name = 'B') object target::TargetElement { name := x.name + ' -> 2'; };
				        else object target::TargetElement { name := 'else part'; };
				    };
				}
				""";
		QvtoModelExtent srcExtent = new BasicQvtoModelExtent();
		srcExtent.add(createSourceElement("A", 1));
		srcExtent.add(createSourceElement("B", 2));
		srcExtent.add(createSourceElement("C", 3));
		QvtoModelExtent tgtExtent = new BasicQvtoModelExtent();

		QvtoExecutionResult result = execute(qvto, QvtoExecutionContext.of(srcExtent, tgtExtent));
		assertTrue(result.isSuccess(), () -> "Transformation should succeed: " + result.diagnostics());

		List<EObject> targets = tgtExtent.getContents();
		assertEquals(3, targets.size(), "Should create one target per source element");
		assertEquals("A -> 1", targets.get(0).eGet(targets.get(0).eClass().getEStructuralFeature("name")));
		assertEquals("B -> 2", targets.get(1).eGet(targets.get(1).eClass().getEStructuralFeature("name")));
		assertEquals("else part", targets.get(2).eGet(targets.get(2).eClass().getEStructuralFeature("name")));
	}

	@Test
	void arrowSwitch_returnsCollectionOfResults() throws QvtoParseException {
		// Arrow-switch returns a collection (xcollect result) — verify it is usable
		String qvto = """
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				main() {
				    var elems := s.objectsOfType(source::SourceElement);
				    var labels := elems->switch (x) {
				        case (x.value > 10) 'big';
				        case (x.value > 0) 'small';
				        else 'zero';
				    };
				    labels->forEach(lbl) {
				        object target::TargetElement {
				            name := lbl.oclAsType(String);
				        };
				    };
				}
				""";
		QvtoModelExtent srcExtent = new BasicQvtoModelExtent();
		srcExtent.add(createSourceElement("A", 20));
		srcExtent.add(createSourceElement("B", 5));
		srcExtent.add(createSourceElement("C", 0));
		QvtoModelExtent tgtExtent = new BasicQvtoModelExtent();

		QvtoExecutionResult result = execute(qvto, QvtoExecutionContext.of(srcExtent, tgtExtent));
		assertTrue(result.isSuccess(), () -> "Transformation should succeed: " + result.diagnostics());

		List<EObject> targets = tgtExtent.getContents();
		assertEquals(3, targets.size(), "Should create one target per label");
		assertEquals("big", targets.get(0).eGet(targets.get(0).eClass().getEStructuralFeature("name")));
		assertEquals("small", targets.get(1).eGet(targets.get(1).eClass().getEStructuralFeature("name")));
		assertEquals("zero", targets.get(2).eGet(targets.get(2).eClass().getEStructuralFeature("name")));
	}
}
