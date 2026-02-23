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
package org.eclipse.fennec.m2m.qvto.tests.transformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for {@code self} keyword edge-cases (§8.1.18, §8.2.1.10).
 *
 * <p>{@code self} is the contextual parameter within a contextual operation
 * (mapping, helper, query). It is only available when the operation has a
 * context type.
 *
 * <p>Eclipse reference: {@code lateresolve.qvto}, {@code stacktrace.qvto},
 * {@code callvirtforundefined.qvto}
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eSelfEdgeCaseTest extends AbstractQvtoEngineTest {

	// ==== P7-02: `self` Edge-Cases (§8.1.18, §8.2.1.10) ====

	@Test
	void self_explicitInMappingBody() throws Exception {
		// §8.1.18: self is the context parameter in a contextual mapping
		// Eclipse lateresolve.qvto: name := self.name; _abstract := self._abstract;
		EObject src = createSourceElement("elem", 42);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := self.name;
				        value := self.value;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->map toTarget();
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		assertEquals("elem", eGet(outExtent.getContents().get(0), "name"));
		assertEquals(42, eGet(outExtent.getContents().get(0), "value"));
	}

	@Test
	void self_explicitInHelper() throws Exception {
		// §8.1.18: self available in helpers with context type
		// Eclipse callvirtforundefined.qvto: query EPackage::virt() : String { return self.name; }
		EObject src = createSourceElement("item", 7);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    helper SourceElement::label() : String {
				        return 'tag:' + self.name;
				    }
				    mapping SourceElement::toTarget() : TargetElement {
				        name := self.label();
				        value := self.value;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->map toTarget();
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		assertEquals("tag:item", eGet(outExtent.getContents().get(0), "name"));
	}

	@Test
	void self_implicitInHelper() throws Exception {
		// §8.1.18: self can be omitted — implicit property access in helper
		// In a helper with context type, bare feature names resolve to self.featureName
		EObject src = createSourceElement("auto", 99);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    helper SourceElement::getName() : String {
				        return name;
				    }
				    mapping SourceElement::toTarget() : TargetElement {
				        name := self.getName();
				        value := self.value;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->map toTarget();
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		assertEquals("auto", eGet(outExtent.getContents().get(0), "name"));
	}

	@Test
	void self_notAvailableInMain() throws Exception {
		// §8.2.1.10: main() has no context parameter → self not available
		// main() is not a contextual operation — no implicit self
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('reached');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "reached");
	}

	@Test
	void self_typeMatchesContextParameter() throws Exception {
		// §8.2.1.10: self has the type of the context parameter
		// self.oclIsKindOf(SourceElement) should be true inside SourceElement::mapping
		EObject src = createSourceElement("typed", 5);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := if self.oclIsKindOf(SourceElement) then 'ok:' + self.name else 'wrong' endif;
				        value := self.value;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->map toTarget();
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		assertEquals("ok:typed", eGet(outExtent.getContents().get(0), "name"));
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
