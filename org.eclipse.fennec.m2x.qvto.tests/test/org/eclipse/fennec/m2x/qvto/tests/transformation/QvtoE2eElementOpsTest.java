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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O Element operations (§8.3.4).
 *
 * <p>QVT-O defines operations on Element (EObject):
 * metaClassName, subobjects, allSubobjects, subobjectsOfType/Kind,
 * allSubobjectsOfType/Kind, clone, deepclone, container.
 *
 * <p><b>SPEC-FIRST:</b> Tests written against QVT-O v1.3 §8.3.4.
 * Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eElementOpsTest extends AbstractQvtoEngineTest {

	// ==== P8-05: Element Operations §8.3.4 ====

	// ---- §8.3.4.3: metaClassName() : String ----

	@Test
	void element_metaClassName() throws Exception {
		// §8.3.4.3: returns the name of the metaclass
		EObject src = createSourceElement("a", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var e := s.objectsOfKind(SourceElement)->asSequence()->first();
				        log(e.metaClassName());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "SourceElement");
	}

	@Test
	void element_metaClassNameSubtype() throws Exception {
		// §8.3.4.3: returns exact metaclass name for subtypes
		EObject src = createSpecialSourceElement("b", 2, "tag");
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var e := s.objectsOfKind(SourceElement)->asSequence()->first();
				        log(e.metaClassName());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "SpecialSourceElement");
	}

	// ---- §8.3.4.4: subobjects() : Set(Element) ----

	@Test
	void element_subobjects() throws Exception {
		// §8.3.4.4: returns all immediate sub objects
		EObject container = createSourceContainer("parent",
				createSourceElement("child1", 1),
				createSourceElement("child2", 2));
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(container);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var c := s.objectsOfKind(SourceContainer)->asSequence()->first();
				        log('subs:' + c.subobjects()->size().repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "subs:2");
	}

	// ---- §8.3.4.5: allSubobjects() : Set(Element) ----

	@Test
	void element_allSubobjects() throws Exception {
		// §8.3.4.5: returns iteratively all sub objects (recursive)
		EObject container = createSourceContainer("parent",
				createSourceElement("child1", 1),
				createSourceElement("child2", 2));
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(container);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var c := s.objectsOfKind(SourceContainer)->asSequence()->first();
				        log('all:' + c.allSubobjects()->size().repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "all:2");
	}

	// ---- §8.3.4.6: subobjectsOfType(type) : Set(T) ----

	@Test
	void element_subobjectsOfType() throws Exception {
		// §8.3.4.6: filters immediate sub objects by exact type
		EObject container = createSourceContainer("parent",
				createSourceElement("child1", 1),
				createSpecialSourceElement("child2", 2, "tag"));
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(container);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var c := s.objectsOfKind(SourceContainer)->asSequence()->first();
				        log('type:' + c.subobjectsOfType(SourceElement)->size().repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		// Only exact SourceElement, not SpecialSourceElement
		assertLogged(result, "type:1");
	}

	// ---- §8.3.4.8: subobjectsOfKind(type) : Set(T) ----

	@Test
	void element_subobjectsOfKind() throws Exception {
		// §8.3.4.8: filters immediate sub objects by kind (includes subtypes)
		EObject container = createSourceContainer("parent",
				createSourceElement("child1", 1),
				createSpecialSourceElement("child2", 2, "tag"));
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(container);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var c := s.objectsOfKind(SourceContainer)->asSequence()->first();
				        log('kind:' + c.subobjectsOfKind(SourceElement)->size().repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		// Both SourceElement and SpecialSourceElement match
		assertLogged(result, "kind:2");
	}

	// ---- §8.3.4.7: allSubobjectsOfType(type) : Set(T) ----

	@Test
	void element_allSubobjectsOfType() throws Exception {
		// §8.3.4.7: filters all descendants by exact type
		EObject container = createSourceContainer("parent",
				createSourceElement("child1", 1),
				createSpecialSourceElement("child2", 2, "tag"));
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(container);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var c := s.objectsOfKind(SourceContainer)->asSequence()->first();
				        log('allType:' + c.allSubobjectsOfType(SourceElement)->size().repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "allType:1");
	}

	// ---- §8.3.4.9: allSubobjectsOfKind(type) : Set(T) ----

	@Test
	void element_allSubobjectsOfKind() throws Exception {
		// §8.3.4.9: filters all descendants by kind (includes subtypes)
		EObject container = createSourceContainer("parent",
				createSourceElement("child1", 1),
				createSpecialSourceElement("child2", 2, "tag"));
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(container);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var c := s.objectsOfKind(SourceContainer)->asSequence()->first();
				        log('allKind:' + c.allSubobjectsOfKind(SourceElement)->size().repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "allKind:2");
	}

	// ---- §8.3.4.10: clone() : T ----

	@Test
	void element_clone() throws Exception {
		// §8.3.4.10: shallow copy (sub objects are NOT cloned)
		EObject src = createSourceElement("original", 42);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var e := s.objectsOfKind(SourceElement)->asSequence()->first();
				        var c := e.clone().oclAsType(SourceElement);
				        log('name:' + c.name);
				        log('value:' + c.value.repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLoggedInOrder(result, "name:original", "value:42");
	}

	// ---- §8.3.4.11: deepclone() : T ----

	@Test
	void element_deepclone() throws Exception {
		// §8.3.4.11: deep copy (sub objects ARE cloned recursively)
		EObject container = createSourceContainer("parent",
				createSourceElement("child1", 1));
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(container);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var c := s.objectsOfKind(SourceContainer)->asSequence()->first();
				        var copy := c.deepclone().oclAsType(SourceContainer);
				        log('name:' + copy.name);
				        log('children:' + copy.elements->size().repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLoggedInOrder(result, "name:parent", "children:1");
	}

	// ---- container() (MOF reflective) ----

	@Test
	void element_container() throws Exception {
		// MOF reflective: Element::container() returns the containing object
		EObject container = createSourceContainer("parent",
				createSourceElement("child1", 1));
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(container);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var c := s.objectsOfKind(SourceContainer)->asSequence()->first();
				        var child := c.elements->first();
				        log('parent:' + child.container().oclAsType(SourceContainer).name);
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "parent:parent");
	}

	@Test
	void element_containerRoot() throws Exception {
		// container() on root object returns null
		EObject src = createSourceElement("root", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var e := s.objectsOfKind(SourceElement)->asSequence()->first();
				        log('null:' + e.container().oclIsUndefined().repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "null:true");
	}

	// ---- Chaining vs Variable Regression (triple-evaluation bug) ----

	@Test
	void element_subobjects_chained_vs_variable() throws Exception {
		// Regression: chaining element op result must match variable-based approach
		EObject container = createSourceContainer("parent",
				createSourceElement("child1", 1),
				createSourceElement("child2", 2));
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(container);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var c := s.objectsOfKind(SourceContainer)->asSequence()->first();
				        log('chained:' + c.subobjects()->size().repr());
				        var subs := c.subobjects();
				        log('variable:' + subs->size().repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLoggedInOrder(result, "chained:2", "variable:2");
	}

	@Test
	void element_clone_chained_vs_variable() throws Exception {
		// Regression: chaining clone + property access must match variable-based approach
		EObject src = createSourceElement("original", 42);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        var e := s.objectsOfKind(SourceElement)->asSequence()->first();
				        log('chained:' + e.clone().oclAsType(SourceElement).name);
				        var c := e.clone().oclAsType(SourceElement);
				        log('variable:' + c.name);
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLoggedInOrder(result, "chained:original", "variable:original");
	}

	@Test
	void element_extent_to_element_chained_vs_variable() throws Exception {
		// Regression: cross-path chain (extent op → element op) must match variable-based approach
		EObject src = createSourceElement("testElem", 99);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        log('chained:' + s.objectsOfKind(SourceElement)->asSequence()->first().metaClassName());
				        var e := s.objectsOfKind(SourceElement)->asSequence()->first();
				        log('variable:' + e.metaClassName());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLoggedInOrder(result, "chained:SourceElement", "variable:SourceElement");
	}

	// ---- Helpers ----

	/**
	 * Creates a SourceContainer with child elements.
	 */
	@SuppressWarnings("unchecked")
	protected static EObject createSourceContainer(String name, EObject... children) {
		EClass containerClass = ecoreHelper.getEClass(sourcePackage, "SourceContainer");
		EObject container = EcoreUtil.create(containerClass);
		container.eSet(containerClass.getEStructuralFeature("name"), name);
		var elements = (java.util.List<EObject>) container.eGet(containerClass.getEStructuralFeature("elements"));
		for (EObject child : children) {
			elements.add(child);
		}
		return container;
	}

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
