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

import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for top-level module property syntax and
 * transformation declaration form.
 *
 * <p>Covers untyped properties, transformation semicolon declaration form,
 * property cross-references, null/collection semantics, copy semantics,
 * and Integer-to-Real conformance on module properties.
 *
 * <p>Eclipse references: {@code duplicatelocalproperty.qvto}, {@code props.qvto},
 * {@code traceLookup_287589.qvto}, {@code q1.qvto}, {@code moduleProperty.qvto}
 *
 * <p><b>SPEC-FIRST:</b> Tests written against QVT-O v1.3 §8.2.1.1
 * (OperationalTransformation::ownedVariable), Figure 8.2.
 * Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eTopLevelSyntaxTest extends AbstractQvtoEngineTest {

	// ==== Top-level module property syntax ====

	// ---- Untyped properties ----

	@Test
	void topLevel_untypedStringProperty() throws Exception {
		// Eclipse: duplicatelocalproperty.qvto, props.qvto — untyped property with String literal
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property foo = 'bar';
				    main() {
				        log('foo:' + foo);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "foo:bar");
	}

	@Test
	void topLevel_untypedCollectionProperty() throws Exception {
		// Eclipse: props.qvto:5 — untyped property with Collection init
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property prop = Sequence {};
				    main() {
				        prop += 'a';
				        prop += 'b';
				        log('size:' + prop->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:2");
	}

	// ---- Object property ----

	@Test
	void topLevel_objectProperty() throws Exception {
		// Eclipse: traceLookup_287589.qvto:7 — property with EObject initialization
		QvtoExecutionResult result = execute("""
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation test(out x : ECORE);
				property shared : EClass = object EClass { name := 'Shared'; };
				main() {
				    log('name:' + shared.name);
				}
				""");
		assertSuccess(result);
		assertLogged(result, "name:Shared");
	}

	// ---- Transformation declaration form ----

	@Test
	void topLevel_transformationDeclarationForm() throws Exception {
		// Eclipse: practically all deployed tests use semicolon form
		QvtoExecutionResult result = execute("""
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation test(out x : ECORE);
				main() {
				    var c := object EClass { name := 'Created'; };
				    log('name:' + c.name);
				}
				""");
		assertSuccess(result);
		assertLogged(result, "name:Created");
	}

	// ---- Property cross-reference ----

	@Test
	void topLevel_propertyCrossReference() throws Exception {
		// Eclipse: q1.qvto:7-8 — property references another property
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property a : String = 'x';
				    property b : String = a + 'y';
				    main() {
				        log('b:' + b);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "b:xy");
	}

	// ---- Null assignment on collection ----

	@Test
	void topLevel_nullAssignToCollection_doesNotClear() throws Exception {
		// Eclipse: moduleProperty.qvto:67-68 — := null on collection does NOT clear it
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property items : Sequence(String) = Sequence {'a', 'b'};
				    main() {
				        this.items := null;
				        log('is-null:' + (items = null).toString());
				        log('size:' + items->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "is-null:false");
		assertLogged(result, "size:0");
	}

	// ---- += with invalid element ----

	@Test
	void topLevel_plusEqualsInvalid_ignored() throws Exception {
		// Eclipse: moduleProperty.qvto:35 — += invalid on collection is ignored
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property items : Sequence(String) = Sequence {'a'};
				    main() {
				        this.items += invalid;
				        log('size:' + items->size().repr());
				        log('first:' + items->at(1));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:1");
		assertLogged(result, "first:a");
	}

	// ---- Collection reassign copy semantics ----

	@Test
	void topLevel_collectionReassign_copySemantics() throws Exception {
		// Eclipse: moduleProperty.qvto:70-74 — property := property creates a copy
		// this.propA := propB; this.propA += x; → propB unchanged
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Box { items : Sequence(String); }
				    main() {
				        var a := object Box { items := Sequence {'a', 'b'}; };
				        var b := object Box { items := Sequence {}; };
				        b.items := a.items;
				        b.items += 'c';
				        log('a-size:' + a.items->size().repr());
				        log('b-size:' + b.items->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "a-size:2");
		assertLogged(result, "b-size:3");
	}

	// ---- Integer conforms to Real ----

	@Test
	void topLevel_integerConformsToReal() throws Exception {
		// Eclipse: moduleProperty.qvto:98-100 — Integer value assigned to Real property
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property propInt : Integer = 42;
				    property propReal : Real = 0.0;
				    main() {
				        this.propReal := propInt;
				        log('real:' + propReal.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "real:42");
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
}
