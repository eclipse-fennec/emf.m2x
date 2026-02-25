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
package org.eclipse.fennec.m2x.qvto.tests.constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O constructor operations.
 *
 * Spec references:
 * - §8.1.8 (p72): Constructor overview — specialized operation creating instances
 * - §8.2.1.13 (p103): Constructor — no result params, name usually = class name
 * - §8.2.1.18 (p109): ConstructorBody — implicit property assignment on created object
 * - §8.2.2.22 (p135): InstantiationExp — `new` keyword invokes constructor
 * - §8.4 (p168): Concrete syntax for constructor declaration
 */
class QvtoE2eConstructorTest extends AbstractQvtoEngineTest {

	// ---- Default constructor (implicit, no explicit constructor defined) ----

	// §8.2.1.13: "For each class, if not defined explicitly, there is an implicit
	// default constructor that has no parameter and that has the name of the class."
	@Test
	void new_defaultConstructor_createsObject() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    main() {
				        new TargetElement();
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size(),
				"Default constructor should create one object in extent");
	}

	// ---- Explicit constructor with parameters ----

	// §8.1.8 (p72): "constructor Column::Column(n:String, t:String) { name:=n; type:=t; }"
	// §8.2.1.13: Constructor body populates properties of the created object
	@Test
	void new_withConstructor_setsProperties() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    constructor TargetElement::TargetElement(n : String, v : Integer) {
				        name := n;
				        value := v;
				    }
				    main() {
				        new TargetElement('constructed', 42);
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		EObject elem = outExtent.getContents().get(0);
		assertEquals("constructed", eGet(elem, "name"));
		assertEquals(42, eGet(elem, "value"));
	}

	// §8.2.1.13: Constructor with single parameter
	@Test
	void new_withSingleParam_setsName() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    constructor TargetElement::TargetElement(n : String) {
				        name := n;
				    }
				    main() {
				        new TargetElement('hello');
				    }
				}
				""", outExtent);
		assertSuccess(result);
		EObject elem = outExtent.getContents().get(0);
		assertEquals("hello", eGet(elem, "name"));
	}

	// ---- Constructor return value ----

	// §8.2.2.22: "creates an instance... and returns the created object"
	@Test
	void new_returnValue_isCreatedObject() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    constructor TargetElement::TargetElement(n : String) {
				        name := n;
				    }
				    main() {
				        var elem := new TargetElement('returned');
				        log(elem.name);
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertLogged(result, "returned");
	}

	// ---- Constructor with computed values ----

	// §8.2.1.18: Constructor body can contain arbitrary expressions
	@Test
	void constructor_withComputedValues() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    constructor TargetElement::TargetElement(n : String, v : Integer) {
				        name := 'prefix-' + n;
				        value := v * 2;
				    }
				    main() {
				        new TargetElement('test', 21);
				    }
				}
				""", outExtent);
		assertSuccess(result);
		EObject elem = outExtent.getContents().get(0);
		assertEquals("prefix-test", eGet(elem, "name"));
		assertEquals(42, eGet(elem, "value"));
	}

	// ---- Constructor invoked in mapping ----

	// §8.1.8 (p72): Constructors used in mapping bodies
	@Test
	void constructor_calledFromMapping() throws Exception {
		EObject src = createSourceElement("src", 10);
		QvtoModelExtent inExtent = new org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    constructor TargetElement::TargetElement(n : String, v : Integer) {
				        name := n;
				        value := v;
				    }
				    helper createTarget(n : String, v : Integer) : TargetElement {
				        return new TargetElement(n, v);
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            var elem := createTarget(e.name, e.value);
				            log(elem.name + ':' + elem.value.toString());
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "src:10");
		// new TargetElement creates it in extent
		EObject elem = outExtent.getContents().stream()
				.filter(e -> "src".equals(eGet(e, "name")))
				.findFirst().orElse(null);
		assertNotNull(elem, "Constructor-created element should be in extent");
		assertEquals(10, eGet(elem, "value"));
	}

	// ---- Multiple new calls ----

	// §8.2.2.22: Each `new` creates a separate instance
	@Test
	void new_multipleCalls_createsSeparateObjects() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    constructor TargetElement::TargetElement(n : String) {
				        name := n;
				    }
				    main() {
				        new TargetElement('first');
				        new TargetElement('second');
				        new TargetElement('third');
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertEquals(3, outExtent.getContents().size());
	}

	// ---- Constructor with distinct name (not matching class name) ----

	// §8.2.1.13: "Giving distinct names allows having more than one constructor."
	// Two constructors for same class: TargetElement (1 param) and createFull (2 params)
	@Test
	void constructor_multipleConstructors_dispatchByArgCount() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    constructor TargetElement::TargetElement(n : String) {
				        name := n;
				        value := 0;
				    }
				    constructor TargetElement::TargetElement(n : String, v : Integer) {
				        name := n;
				        value := v;
				    }
				    main() {
				        new TargetElement('one-param');
				        new TargetElement('two-params', 77);
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertEquals(2, outExtent.getContents().size());
		EObject first = outExtent.getContents().get(0);
		assertEquals("one-param", eGet(first, "name"));
		assertEquals(0, eGet(first, "value"));
		EObject second = outExtent.getContents().get(1);
		assertEquals("two-params", eGet(second, "name"));
		assertEquals(77, eGet(second, "value"));
	}

	// ---- Constructor body uses result variable explicitly ----

	// §8.2.1.18: "name := messageName; // same as result.name := messageName"
	@Test
	void constructor_explicitResultAccess() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    constructor TargetElement::TargetElement(n : String) {
				        result.name := n;
				        result.value := 100;
				    }
				    main() {
				        new TargetElement('explicit');
				    }
				}
				""", outExtent);
		assertSuccess(result);
		EObject elem = outExtent.getContents().get(0);
		assertEquals("explicit", eGet(elem, "name"));
		assertEquals(100, eGet(elem, "value"));
	}

	// ---- Constructor with helper call ----

	@Test
	void constructor_callsHelper() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    helper formatName(n : String) : String {
				        return 'fmt-' + n;
				    }
				    constructor TargetElement::TargetElement(n : String) {
				        name := formatName(n);
				    }
				    main() {
				        new TargetElement('test');
				    }
				}
				""", outExtent);
		assertSuccess(result);
		EObject elem = outExtent.getContents().get(0);
		assertEquals("fmt-test", eGet(elem, "name"));
	}

	// ---- new in loop ----

	@Test
	void new_inLoop_createsMultipleObjects() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    constructor TargetElement::TargetElement(n : String, v : Integer) {
				        name := n;
				        value := v;
				    }
				    main() {
				        Sequence{1, 2, 3}->forEach(i) {
				            new TargetElement('item-' + i.toString(), i);
				        };
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertEquals(3, outExtent.getContents().size());
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
