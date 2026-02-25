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
package org.eclipse.fennec.m2x.qvto.tests.transformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O transformation structure and model parameters.
 *
 * Spec references:
 * - §8.1.1 (p65): Operational transformations — signature, model parameters, main()
 * - §8.2.1.1 (p89-94): OperationalTransformation — parameters, execution semantics
 * - §8.2.1.5 (p97): ModelParameter — direction (in/out/inout)
 * - §8.2.1.11 (p101): EntryOperation — main() entry point
 */
class QvtoE2eTransformationTest extends AbstractQvtoEngineTest {

	// ---- Model parameter directions ----

	// §8.1.1: Transformation with in/out parameters transforms source to target
	@Test
	void transformation_inOutParameters() throws Exception {
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
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		assertEquals("elem", eGet(outExtent.getContents().get(0), "name"));
	}

	// §8.1.1: Three-parameter transformation (in/inout/out)
	@Test
	void transformation_threeParameters() throws Exception {
		EObject src = createSourceElement("a", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent inoutExtent = new BasicQvtoModelExtent(createSourceElement("b", 2));
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, inout m : SRC, out t : TGT) {
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            log('in:' + e.name);
				        };
				        m.objectsOfType(SourceElement)->forEach(e) {
				            log('inout:' + e.name);
				        };
				    }
				}
				""", inExtent, inoutExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "in:a");
		assertLogged(result, "inout:b");
	}

	// §8.1.1: inout parameter — input elements accessible and output can be added
	@Test
	void transformation_inoutModifiesModel() throws Exception {
		EObject src = createSourceElement("orig", 10);
		QvtoModelExtent inoutExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(inout m : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := 'copied-' + self.name;
				        value := self.value;
				    }
				    main() {
				        m.objectsOfType(SourceElement)->forEach(e) {
				            log('found:' + e.name);
				            e.map toTarget();
				        };
				    }
				}
				""", inoutExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "found:orig");
		assertEquals(1, outExtent.getContents().size());
		assertEquals("copied-orig", eGet(outExtent.getContents().get(0), "name"));
	}

	// ---- No main() entry ----

	// §8.2.1.11: Transformation without main() → error
	@Test
	void transformation_noMain_producesError() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper doSomething() : String {
				        return 'x';
				    }
				}
				""");
		assertNotNull(result);
		assertFalse(result.isSuccess(), "Transformation without main() should fail");
	}

	// ---- Empty transformation ----

	// §8.1.1: Empty main() executes successfully
	@Test
	void transformation_emptyMain() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				    }
				}
				""");
		assertSuccess(result);
	}

	// ---- Transformation with library access (inline) ----

	// §8.1.4 + §8.2.1.4: Transformation accesses inline library
	@Test
	void transformation_accessInlineLibrary() throws Exception {
		EObject src = createSourceElement("test", 99);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				library Utils {
				    helper tag(s : String) : String {
				        return '[' + s + ']';
				    }
				}
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := tag(self.name);
				        value := self.value;
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
		assertEquals("[test]", eGet(outExtent.getContents().get(0), "name"));
	}

	// ---- Multiple access on transformation ----

	// §8.2.1.4: Transformation with access moduleUsage
	@Test
	void transformation_withAccessModuleUsage() throws Exception {
		QvtoExecutionResult result = execute("""
				library Lib1 {
				    helper fromLib() : String {
				        return 'accessed';
				    }
				}
				transformation test() access Lib1 {
				    main() {
				        log(fromLib());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "accessed");
	}

	// ---- P2-10: Entry Operation (main) E2E Tests ----

	// §8.2.1.11: main() expressions execute in order
	@Test
	void main_expressionsInOrder() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('1:first');
				        log('2:second');
				        log('3:third');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "1:first");
		assertLogged(result, "2:second");
		assertLogged(result, "3:third");
	}

	// §8.2.1.11: main() can use variables, call helpers and mappings
	@Test
	void main_complexBody() throws Exception {
		EObject src = createSourceElement("item", 5);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    helper prefix() : String {
				        return 'pfx';
				    }
				    mapping SourceElement::toTarget() : TargetElement {
				        name := prefix() + ':' + self.name;
				    }
				    main() {
				        var elements := s.objectsOfType(SourceElement);
				        log('count:' + elements->size().toString());
				        elements->forEach(e) {
				            e.map toTarget();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "count:1");
		assertEquals(1, outExtent.getContents().size());
		assertEquals("pfx:item", eGet(outExtent.getContents().get(0), "name"));
	}

	// §8.2.1.11: main() with local variables
	@Test
	void main_localVariables() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : String := 'hello';
				        var y : String := 'world';
				        log(x + ':' + y);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "hello:world");
	}

	// ---- P2-09: ModelType E2E Tests ----

	// §8.2.1.6: objectsOfType filters by ModelType correctly
	@Test
	void modeltype_objectsOfTypeFilters() throws Exception {
		EObject src = createSourceElement("alpha", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var count := s.objectsOfType(SourceElement)->size();
				        log('count:' + count.toString());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "count:1");
	}

	// §8.2.1.6: Two modeltypes — in/out with different metamodels
	@Test
	void modeltype_twoModelTypes() throws Exception {
		EObject src = createSourceElement("mt", 7);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := 'mt:' + self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertFalse(outExtent.getContents().isEmpty());
		assertEquals("mt:mt", eGet(outExtent.getContents().get(0), "name"));
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
