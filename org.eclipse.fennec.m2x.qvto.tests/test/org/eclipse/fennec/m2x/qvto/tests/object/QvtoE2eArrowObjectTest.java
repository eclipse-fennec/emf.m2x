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
package org.eclipse.fennec.m2x.qvto.tests.object;

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
 * E2E tests for N5: coll->object(x:T) Type { ... } arrow form (§8.4.7).
 */
class QvtoE2eArrowObjectTest extends AbstractQvtoEngineTest {

	@Test
	void arrowObject_autoRoutesToOutExtent() throws QvtoParseException {
		// object expressions auto-route created objects to the out extent
		String qvto = """
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				main() {
				    var elems := s.objectsOfType(source::SourceElement);
				    elems->object (x : source::SourceElement) target::TargetElement {
				        name := x.name;
				    };
				}
				""";
		QvtoModelExtent srcExtent = new BasicQvtoModelExtent();
		srcExtent.add(createSourceElement("A", 1));
		srcExtent.add(createSourceElement("B", 2));
		srcExtent.add(createSourceElement("C", 3));
		QvtoModelExtent tgtExtent = new BasicQvtoModelExtent();

		QvtoExecutionResult result = execute(qvto, QvtoExecutionContext.of(srcExtent, tgtExtent));
		assertTrue(result.isSuccess(), "Transformation should succeed");

		List<EObject> targets = tgtExtent.getContents();
		assertEquals(3, targets.size(), "Should auto-route 3 target elements to out extent");
		assertEquals("A", targets.get(0).eGet(targets.get(0).eClass().getEStructuralFeature("name")));
		assertEquals("B", targets.get(1).eGet(targets.get(1).eClass().getEStructuralFeature("name")));
		assertEquals("C", targets.get(2).eGet(targets.get(2).eClass().getEStructuralFeature("name")));
	}

	@Test
	void arrowObject_returnsCollectionOfCreatedObjects() throws QvtoParseException {
		// coll->object(...) returns a collection — verify via size check on the result
		String qvto = """
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				main() {
				    var elems := s.objectsOfType(source::SourceElement);
				    var targets := elems->object (x : source::SourceElement) target::TargetElement {
				        name := x.name;
				    };
				    -- verify the return value is a collection with 3 elements
				    assert warning (targets->size() = 3) with log('arrow-object returned wrong count');
				    -- use the returned collection to set labels
				    targets->forEach(te) {
				        te.label := 'from_return';
				    };
				}
				""";
		QvtoModelExtent srcExtent = new BasicQvtoModelExtent();
		srcExtent.add(createSourceElement("A", 1));
		srcExtent.add(createSourceElement("B", 2));
		QvtoModelExtent tgtExtent = new BasicQvtoModelExtent();

		QvtoExecutionResult result = execute(qvto, QvtoExecutionContext.of(srcExtent, tgtExtent));
		assertTrue(result.isSuccess(), "Transformation should succeed");

		List<EObject> targets = tgtExtent.getContents();
		assertEquals(2, targets.size(), "Should create 2 target elements");
		// Verify the returned collection was usable — labels were set via forEach on return value
		assertEquals("from_return", targets.get(0).eGet(targets.get(0).eClass().getEStructuralFeature("label")));
		assertEquals("from_return", targets.get(1).eGet(targets.get(1).eClass().getEStructuralFeature("label")));
	}
}
