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
package org.eclipse.fennec.m2m.qvto.tests.regression;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2m.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for Eclipse QVT-O bug regressions (P9-09).
 *
 * <p>Adapted from Eclipse bug regression tests to our in-memory transformation
 * framework. Only behaviors testable without model I/O are included.
 *
 * <p>Eclipse reference tests:
 * <ul>
 *   <li>{@code bug449445/bug449445.qvto} — Collection += with invalid/null</li>
 *   <li>{@code bug415661/bug415661.qvto} — Integer range with invalid bound</li>
 *   <li>{@code bug561707/bug561707.qvto} — Object reparenting (deferred: needs model I/O)</li>
 *   <li>{@code scr878/scr878.qvto} — invresolveone read-only guard (deferred: needs model I/O)</li>
 * </ul>
 *
 * <p><b>SPEC-FIRST:</b> Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eEclipseBugRegressionTest extends AbstractQvtoEngineTest {

	// ==== bug449445: Collection += with invalid/null ====

	@Test
	void bug449445_collectionPlusEqualsInvalid_notAdded() throws Exception {
		// Eclipse bug449445: c += invalid; assert fatal (c = OrderedSet{'a', 'b', 'c'})
		// §8.2.2: invalid should NOT be added to a collection
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var c : Sequence(String) := Sequence {'a', 'b'};
				        c += invalid;
				        log('size:' + c->size().repr());
				        log('includes_a:' + c->includes('a').toString());
				        log('includes_b:' + c->includes('b').toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:2");
		assertLogged(result, "includes_a:true");
		assertLogged(result, "includes_b:true");
	}

	@Test
	void bug449445_collectionPlusEqualsNull_isAdded() throws Exception {
		// Eclipse bug449445: c += null; assert fatal (c->includes(null))
		// §8.2.2: null SHOULD be added to a collection
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var c : Sequence(String) := Sequence {'a'};
				        c += null;
				        log('size:' + c->size().repr());
				        log('includes_null:' + c->includes(null).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:2");
		assertLogged(result, "includes_null:true");
	}

	@Test
	void bug449445_collectionAssignInvalid_clearsCollection() throws Exception {
		// Eclipse bug449445: cls.eAnnotations := invalid; assert fatal (cls.eAnnotations->size() = 0)
		// Assigning invalid to a collection-typed property should clear it
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Box { items : Sequence(String); }
				    main() {
				        var b := object Box { items := Sequence {'x', 'y'}; };
				        b.items := invalid;
				        log('size:' + b.items->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:0");
	}

	@Test
	void bug449445_variableAssignInvalid_primitives() throws Exception {
		// Eclipse bug449445: var vb : Boolean := invalid; assert fatal (vb.oclIsInvalid())
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var vb : Boolean := invalid;
				        var vi : Integer := invalid;
				        var vr : Real := invalid;
				        var vs : String := invalid;
				        log('bool:' + vb.oclIsInvalid().toString());
				        log('int:' + vi.oclIsInvalid().toString());
				        log('real:' + vr.oclIsInvalid().toString());
				        log('str:' + vs.oclIsInvalid().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "bool:true");
		assertLogged(result, "int:true");
		assertLogged(result, "real:true");
		assertLogged(result, "str:true");
	}

	@Test
	void bug449445_invalidCollectionOperationsPropagate() throws Exception {
		// Eclipse bug449445: var cc1 : Collection(OclAny) := invalid;
		// assert fatal (cc1->size().oclIsInvalid())
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var cc : Sequence(String) := invalid;
				        log('size_invalid:' + cc->size().oclIsInvalid().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size_invalid:true");
	}

	@Test
	void bug449445_collectionReassignChangesKind() throws Exception {
		// Eclipse bug449445: c := Sequence {}; c += 'a'; c += 'a';
		// assert fatal (c->size() = 2)
		// Reassigning from OrderedSet to Sequence allows duplicates
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var c : Sequence(String) := Sequence {};
				        c += 'a';
				        c += 'a';
				        log('size:' + c->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:2");
	}

	@Test
	void bug449445_realSetWidening() throws Exception {
		// Eclipse bug449445: var reals : Set(Real) = Set{}; reals := Set{1};
		// assert fatal (reals->includes(1.0)); reals += 1.0;
		// assert fatal (reals = Set{1.0})
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var reals : Set(Real) := Set {1};
				        log('includes_1_0:' + reals->includes(1.0).toString());
				        reals += 1.0;
				        log('size_after:' + reals->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "includes_1_0:true");
		// Set should de-duplicate: 1 and 1.0 are equal for Real
		assertLogged(result, "size_after:1");
	}

	@Test
	void bug449445_nullAssignmentToObjectProperty() throws Exception {
		// Eclipse bug449445: cls.name := invalid; assert fatal (cls.name = null)
		// Assigning invalid to an object's String property should result in null
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Item { name : String; }
				    main() {
				        var item := object Item { name := 'foo'; };
				        item.name := invalid;
				        log('is_null:' + (item.name = null).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "is_null:true");
	}

	// ==== bug415661: Integer range with invalid bound ====

	@Test
	void bug415661_rangeWithInvalidBound() throws Exception {
		// Eclipse bug415661: var range : Set(Integer) = Set{1 .. bound};
		// assert fatal (range->size().oclIsInvalid())
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var bound : Integer := invalid;
				        var range := Sequence {1 .. bound};
				        log('invalid:' + range->size().oclIsInvalid().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "invalid:true");
	}

	@Test
	void bug415661_rangeWithNullBound() throws Exception {
		// Similar to bug415661 but with null bound
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var bound : Integer := null;
				        var range := Sequence {1 .. bound};
				        log('invalid:' + range->size().oclIsInvalid().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "invalid:true");
	}

	// ==== bug561707: Object reparenting via removeElement + mapping ====
	// Eclipse original: bug561707.qvto
	// - Creates EEnum parent + EEnumLiteral child as module properties
	// - Adds child to parent.eLiterals
	// - Calls output.removeElement(parent) on the out-extent
	// - Then re-adds parent/child via mappings with init { result := ... }
	// - Asserts parent.eLiterals still includes child after reparenting

	@Test
	void bug561707_objectReparenting() throws Exception {
		BasicQvtoModelExtent output = new BasicQvtoModelExtent();
		// Eclipse original: module-level untyped properties (property child = object ...;)
		// and semicolon-separated top-level syntax.
		// Adapted: (1) block syntax, (2) typed properties (our grammar requires : Type),
		// (3) use '=' instead of ':=' for property init.
		// Core behavior preserved: removeElement on out-extent, reparenting via mapping.
		QvtoExecutionResult result = executeWithExtents("""
				modeltype ecore uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation bug561707(out output : ecore) {

				    main() {
				        var child := object EEnumLiteral {};
				        var parent := object EEnum {};

				        parent.eLiterals += child;
				        assert fatal (parent.eLiterals->includes(child));

				        output.removeElement(parent);
				        var stolen := map steal(child);
				        assert fatal (parent.eLiterals->includes(child));

				        map outputMapping(parent);
				    }

				    mapping outputMapping(p : EEnum) : EEnum {
				        init {
				            result := p;
				        }
				        name := 'parent';
				    }

				    mapping steal(c : EEnumLiteral) : EEnumLiteral {
				        init {
				            result := c;
				        }
				        name := 'child';
				    }
				}
				""", output);
		assertSuccess(result);
	}

	// ==== scr878: invresolveone + write to in-model ====
	// Eclipse original: scr878.qvto
	// - transformation scr878(in model : ecore, out ecore)
	// - main(in inModel : EPackage) — entry with typed in-parameter
	// - Maps EPackage to EClass, then invresolveone() to get back original
	// - Tries to write orig.name := 'a' on the in-model object
	// - §8.1.3.2: in-model is read-only, write must fail

	@Test
	void scr878_writeToInModelFails() throws Exception {
		// Set up an in-extent with one EPackage (matching Eclipse original)
		EObject pkg = EcorePackage.eINSTANCE.getEFactoryInstance()
				.create(EcorePackage.Literals.EPACKAGE);
		pkg.eSet(EcorePackage.Literals.ENAMED_ELEMENT__NAME, "TestPkg");

		BasicQvtoModelExtent inModel = new BasicQvtoModelExtent(pkg);
		BasicQvtoModelExtent outModel = new BasicQvtoModelExtent();
		// Eclipse original uses main(in inModel : EPackage) — typed entry param.
		// We adapt to objectsOfType(EPackage) since we don't support typed main yet,
		// but keep the same mapping chain: EPackage→EClass, then testMapping with
		// invresolveone() back to original, then write attempt on in-model object.
		// Eclipse original: transformation scr878(in model : ecore, out ecore)
		// Adapted: (1) block syntax, (2) named out parameter (our grammar requires name).
		// Core behavior preserved: invresolveone() to get back in-model object,
		// then write attempt on in-model → should fail.
		QvtoExecutionResult result = executeWithExtents("""
				modeltype ecore uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation scr878(in model : ecore, out output : ecore) {

				    main() {
				        var cls := model.objectsOfType(EPackage)->any(true).map toEObject();
				        map testMapping(cls);
				    }

				    mapping EPackage::toEObject() : EClass {
				        name := self.name;
				    }

				    mapping testMapping(cls : EClass) {
				        init {
				            var orig : EPackage := cls.invresolveone().oclAsType(EPackage);
				            orig.name := 'a';
				        }
				    }
				}
				""", inModel, outModel);
		// §8.1.3.2: Transformation should fail due to write to read-only in-model
		assertFalse(result.isSuccess(),
				"Expected failure due to write to in-model, but diagnostics: "
						+ result.diagnostics());
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
