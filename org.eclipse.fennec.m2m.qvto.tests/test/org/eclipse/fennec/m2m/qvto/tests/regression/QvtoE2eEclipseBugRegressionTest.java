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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

	// ==== bug561707: Object reparenting (deferred) ====

	@Test
	@org.junit.jupiter.api.Disabled("Deferred: bug561707 needs model I/O "
			+ "(removeElement on output model extent).")
	void bug561707_objectReparenting() throws Exception {
		// Eclipse bug561707: Tests reparenting with removeElement and +=
		// Requires model I/O infrastructure
	}

	// ==== scr878: invresolveone read-only guard (deferred) ====

	@Test
	@org.junit.jupiter.api.Disabled("Deferred: scr878 needs model I/O "
			+ "(in/out model parameters with read-only guard).")
	void scr878_invresolveoneReadOnlyGuard() throws Exception {
		// Eclipse scr878: Tests invresolveone() in init section
		// Requires model I/O with read-only enforcement
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
