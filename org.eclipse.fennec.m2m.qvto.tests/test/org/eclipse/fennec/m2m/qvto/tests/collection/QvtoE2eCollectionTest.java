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
package org.eclipse.fennec.m2m.qvto.tests.collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2m.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O collection operations in imperative context.
 * Tests cover Sequence, Set, OrderedSet, Bag, collection parameters,
 * collection results, += operator, and nested collection operations.
 */
class QvtoE2eCollectionTest extends AbstractQvtoEngineTest {

	// ---- Sequence literal + iteration ----

	@Test
	void sequence_literal_forEachIteration() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var sum : Integer := 0;
				        Sequence{10, 20, 30}->forEach(n) {
				            sum := sum + n;
				        };
				        log(sum.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "60");
	}

	@Test
	void sequence_size() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s := Sequence{1, 2, 3, 4, 5};
				        log(s->size().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "5");
	}

	@Test
	void sequence_includes() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s := Sequence{1, 2, 3};
				        log(s->includes(2).toString());
				        log(s->includes(5).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "true");
		assertLogged(result, "false");
	}

	// ---- Set operations ----

	@Test
	void set_literal_noDuplicates() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s := Set{1, 2, 2, 3, 3, 3};
				        log(s->size().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "3");
	}

	@Test
	void set_union() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var a := Set{1, 2, 3};
				        var b := Set{3, 4, 5};
				        var u := a->union(b);
				        log(u->size().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "5");
	}

	@Test
	void set_intersection() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var a := Set{1, 2, 3, 4};
				        var b := Set{3, 4, 5, 6};
				        var i := a->intersection(b);
				        log(i->size().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "2");
	}

	// ---- OrderedSet ----

	@Test
	void orderedSet_preservesOrder() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var os := OrderedSet{3, 1, 2};
				        os->forEach(n) { log(n.toString()); };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "3");
		assertLogged(result, "1");
		assertLogged(result, "2");
	}

	// ---- Bag from collect ----

	@Test
	void bag_fromCollect_allowsDuplicates() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var result := Sequence{1, 2, 3}->collect(n | n / 2);
				        log(result->size().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "3");
	}

	// ---- Collection as mapping parameter ----

	@Test
	void collection_asMappingInput() throws Exception {
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(
				createSourceElement("a", 1),
				createSourceElement("b", 2),
				createSourceElement("c", 3));
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(3, outExtent.getContents().size());
	}

	// ---- += operator on collection features ----

	@Test
	void plusEquals_addsToContainmentRef() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    main() {
				        var container := object TargetContainer {
				            name := 'c';
				        };
				        container.elements += object TargetElement { name := 'e1'; };
				        container.elements += object TargetElement { name := 'e2'; };
				        log(container.elements->size().toString());
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertLogged(result, "2");
	}

	// ---- forEach with collection ----

	@Test
	void forEach_overMappedCollection() throws Exception {
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(
				createSourceElement("x", 10),
				createSourceElement("y", 20));
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				        r.value := self.value;
				    }
				    main() {
				        var targets := s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				        targets->forEach(tgt) {
				            log(tgt.name + ':' + tgt.value.toString());
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "x:10");
		assertLogged(result, "y:20");
	}

	// ---- Nested collect/select/reject ----

	@Test
	void nested_select_reject() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var nums := Sequence{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
				        var even := nums->select(n | n.mod(2) = 0);
				        var large := even->reject(n | n < 6);
				        log(large->size().toString());
				    }
				}
				""");
		assertSuccess(result);
		// even: 2,4,6,8,10 → reject <6 → 6,8,10 → size=3
		assertLogged(result, "3");
	}

	@Test
	void nested_collect_select() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var result := Sequence{1, 2, 3, 4, 5}
				            ->collect(n | n * 2)
				            ->select(n | n > 6);
				        log(result->size().toString());
				    }
				}
				""");
		assertSuccess(result);
		// collect: 2,4,6,8,10 → select >6 → 8,10 → size=2
		assertLogged(result, "2");
	}

	// ---- Collection isEmpty/notEmpty ----

	@Test
	void collection_isEmpty_emptySequence_returnsTrue() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('isEmpty-empty:' + Sequence{}->isEmpty().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "isEmpty-empty:true");
	}

	@Test
	void collection_isEmpty_nonEmptySequence_returnsFalse() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('isEmpty-nonempty:' + Sequence{1}->isEmpty().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "isEmpty-nonempty:false");
	}

	@Test
	void collection_notEmpty_emptySequence_returnsFalse() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('notEmpty-empty:' + Sequence{}->notEmpty().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "notEmpty-empty:false");
	}

	@Test
	void collection_notEmpty_nonEmptySequence_returnsTrue() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('notEmpty-nonempty:' + Sequence{1}->notEmpty().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "notEmpty-nonempty:true");
	}

	// ---- Sequence first/last ----

	@Test
	void sequence_first_last() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s := Sequence{10, 20, 30};
				        log(s->first().toString());
				        log(s->last().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "10");
		assertLogged(result, "30");
	}

	// ---- Sequence append ----

	@Test
	void sequence_append() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s := Sequence{1, 2};
				        var s2 := s->append(99);
				        log('size:' + s2->size().toString());
				        log('last:' + s2->last().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:3");
		assertLogged(result, "last:99");
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
