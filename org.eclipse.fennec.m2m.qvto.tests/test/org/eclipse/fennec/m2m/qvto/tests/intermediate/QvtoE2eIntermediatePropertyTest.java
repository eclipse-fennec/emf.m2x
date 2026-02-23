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
 * End-to-end tests for QVT-O intermediate properties.
 *
 * Spec references:
 * - §8.1.10 (p78): Intermediate Data — properties attached to model types within transformation scope
 * - §8.2.1.14 (p104): ContextualProperty — property owned by module, conceptually extends context class
 * - §8.2.1.1 (p96): Module — owns contextual properties
 * - §8.4 (p168): Concrete syntax: 'intermediate' 'property' scopedName ':' type ('=' initExpr)? ';'
 */
class QvtoE2eIntermediatePropertyTest extends AbstractQvtoEngineTest {

	// ---- Basic read/write of intermediate property ----

	// §8.1.10: Intermediate property can be written and read on model elements
	@Test
	void intermediateProperty_writeAndRead() throws Exception {
		EObject src = createSourceElement("elem", 10);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    intermediate property SourceElement::tag : String;
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.tag := 'marked';
				            log(e.tag);
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "marked");
	}

	// §8.1.10: Multiple intermediate properties on same type
	@Test
	void intermediateProperty_multipleProperties() throws Exception {
		EObject src = createSourceElement("elem", 10);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    intermediate property SourceElement::tag : String;
				    intermediate property SourceElement::processed : Boolean;
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.tag := 'done';
				            e.processed := true;
				            log(e.tag + ':' + e.processed.toString());
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "done:true");
	}

	// §8.1.10: Each instance has its own intermediate property value
	@Test
	void intermediateProperty_perInstanceValues() throws Exception {
		EObject src1 = createSourceElement("first", 1);
		EObject src2 = createSourceElement("second", 2);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1, src2);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    intermediate property SourceElement::tag : String;
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.tag := 'tagged-' + e.name;
				        };
				        s.objectsOfType(SourceElement)->forEach(e) {
				            log(e.tag);
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "tagged-first");
		assertLogged(result, "tagged-second");
	}

	// ---- Intermediate property with computed value ----

	// §8.1.10: Intermediate property used in mapping context
	@Test
	void intermediateProperty_usedInMapping() throws Exception {
		EObject src = createSourceElement("elem", 42);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    intermediate property SourceElement::tag : String;
				    mapping SourceElement::toTarget() : TargetElement {
				        name := self.tag;
				        value := self.value;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.tag := 'mapped-' + e.name;
				            e.map toTarget();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		EObject target = outExtent.getContents().get(0);
		assertEquals("mapped-elem", eGet(target, "name"));
	}

	// ---- Intermediate property default value (uninitialized) ----

	// §8.1.10: Reading uninitialized intermediate property returns null/default
	@Test
	void intermediateProperty_uninitializedReturnsNull() throws Exception {
		EObject src = createSourceElement("elem", 10);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    intermediate property SourceElement::tag : String;
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            if e.tag = null then
				                log('null-ok')
				            endif;
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "null-ok");
	}

	// ---- Intermediate property in helper ----

	@Test
	void intermediateProperty_readInHelper() throws Exception {
		EObject src = createSourceElement("elem", 10);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    intermediate property SourceElement::tag : String;
				    helper SourceElement::getTag() : String {
				        return self.tag;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.tag := 'helper-read';
				            log(e.getTag());
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "helper-read");
	}

	// ---- Intermediate property on target type ----

	@Test
	void intermediateProperty_onTargetType() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    intermediate property TargetElement::origin : String;
				    main() {
				        var elem := object TargetElement { name := 'test'; };
				        elem.origin := 'created';
				        log(elem.origin);
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertLogged(result, "created");
	}

	// ---- P3-05: Spec/Eclipse-derived additional tests ----

	// §8.1.10 + Eclipse CheckMemoryLeak.qvto: Property persists across mapping invocations
	@Test
	void intermediateProperty_persistsAcrossMappings() throws Exception {
		EObject src = createSourceElement("elem", 10);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    intermediate property SourceElement::tag : String;
				    mapping SourceElement::toTarget() : TargetElement {
				        name := self.tag;
				        value := self.value;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.tag := 'pre-' + e.name;
				        };
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		EObject target = outExtent.getContents().get(0);
		assertEquals("pre-elem", eGet(target, "name"));
	}

	// §8.2.1.14 + Eclipse IntermediateData.qvto: Init expression provides default value
	@Test
	void intermediateProperty_initExpression() throws Exception {
		EObject src = createSourceElement("elem", 10);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    intermediate property SourceElement::label : String = 'default-label';
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            log(e.label);
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "default-label");
	}

	// §8.1.10: Intermediate property on intermediate class (combined usage)
	@Test
	void intermediateProperty_onIntermediateClass() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Node { name : String; }
				    intermediate property Node::visited : Boolean;
				    main() {
				        var n := object Node { name := 'root'; };
				        n.visited := true;
				        log(n.name + ':' + n.visited.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "root:true");
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
