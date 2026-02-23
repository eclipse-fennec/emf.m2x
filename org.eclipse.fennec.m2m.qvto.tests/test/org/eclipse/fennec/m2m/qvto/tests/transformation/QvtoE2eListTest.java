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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for {@code List} (§8.3.9, §8.2.2.24).
 *
 * <p>{@code List(T)} is a mutable ordered collection (alias for Sequence per D26)
 * with additional mutating operations: add, remove, removeAt, removeFirst,
 * removeLast, removeAll, and joinfields.
 *
 * <p>Eclipse reference: {@code stdlibList/stdlibList.qvto},
 * {@code bug467600_List/bug467600_List.qvto}
 *
 * <p><b>SPEC-FIRST:</b> Tests written against QVT-O v1.3 §8.3.9.
 * Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eListTest extends AbstractQvtoEngineTest {

	// ==== P8-02: List (MutableList) §8.3.9 ====

	// ---- List Literal Syntax ----

	@Test
	void list_emptyLiteral() throws Exception {
		// List{} creates empty list (D26: desugars to Sequence)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(String) := List{};
				        log(l->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "0");
	}

	@Test
	void list_literalWithElements() throws Exception {
		// List{...} creates list with elements
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(Integer) := List{10, 20, 30};
				        log(l->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "3");
	}

	// ---- §8.3.9.3: add(object:T) : Void (mutating) ----

	@Test
	void list_addAppendsElement() throws Exception {
		// §8.3.9.3: add modifies list in-place, appends at end
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(String) := List{};
				        l->add('a');
				        l->add('b');
				        log(l->size().repr());
				        log(l->at(1));
				        log(l->at(2));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "2");
		assertLogged(result, "a");
		assertLogged(result, "b");
	}

	// ---- §8.3.9.4: append(object:T) : List(T) (non-mutating) ----

	@Test
	void list_appendReturnsNewList() throws Exception {
		// §8.3.9.4: append returns NEW list, does not modify original
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(String) := List{'a', 'b'};
				        var l2 := l->append('c');
				        log(l->size().repr());
				        log(l2->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "2");
		assertLogged(result, "3");
	}

	// ---- §8.3.9.33: prepend(object:T) : List(T) (non-mutating) ----

	@Test
	void list_prependReturnsNewList() throws Exception {
		// §8.3.9.33: prepend returns NEW list with object at beginning
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(String) := List{'b', 'c'};
				        var l2 := l->prepend('a');
				        log(l2->at(1));
				        log(l2->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "a");
		assertLogged(result, "3");
	}

	// ---- §8.3.9.25: insertAt(index:Integer, object:T) : List(T) (non-mutating) ----

	@Test
	void list_insertAtNonMutating() throws Exception {
		// §8.3.9.25: insertAt(index, object) returns NEW list
		// Eclipse: var insertAt : List(String) := c2->insertAt(1, "0");
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(String) := List{'a', 'b'};
				        var l2 := l->insertAt(1, 'z');
				        log(l2->at(1));
				        log(l2->size().repr());
				        log(l->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "z");
		assertLogged(result, "3");
		assertLogged(result, "2");
	}

	// ---- §8.3.9.35: remove(element:T) : Void (mutating) ----

	@Test
	void list_removeAllOccurrences() throws Exception {
		// §8.3.9.35: remove removes ALL equal elements from self
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(String) := List{'a', 'b', 'a', 'c'};
				        l->remove('a');
				        log(l->size().repr());
				        log(l->at(1));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "2");
		assertLogged(result, "b");
	}

	// ---- §8.3.9.38: removeAt(index:Integer) : T (mutating) ----

	@Test
	void list_removeAtReturnsElement() throws Exception {
		// §8.3.9.38: removeAt removes and returns element at 1-based index
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(String) := List{'a', 'b', 'c'};
				        var removed := l->removeAt(2);
				        log(removed);
				        log(l->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "b");
		assertLogged(result, "2");
	}

	// ---- §8.3.9.39: removeFirst() : T (mutating) ----

	@Test
	void list_removeFirstReturnsElement() throws Exception {
		// §8.3.9.39: removeFirst removes and returns first element
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(String) := List{'x', 'y', 'z'};
				        var removed := l->removeFirst();
				        log(removed);
				        log(l->size().repr());
				        log(l->at(1));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "x");
		assertLogged(result, "2");
		assertLogged(result, "y");
	}

	// ---- §8.3.9.40: removeLast() : T (mutating) ----

	@Test
	void list_removeLastReturnsElement() throws Exception {
		// §8.3.9.40: removeLast removes and returns last element
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(String) := List{'x', 'y', 'z'};
				        var removed := l->removeLast();
				        log(removed);
				        log(l->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "z");
		assertLogged(result, "2");
	}

	// ---- §8.3.9.36: removeAll(elements:Collection(T)) : Void (mutating) ----

	@Test
	void list_removeAllElements() throws Exception {
		// §8.3.9.36: removeAll removes all elements equal to any in given collection
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(String) := List{'a', 'b', 'c', 'a'};
				        l->removeAll(Set{'a', 'c'});
				        log(l->size().repr());
				        log(l->at(1));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "1");
		assertLogged(result, "b");
	}

	// ---- §8.3.9.28: joinfields(sep, begin, end) : String ----

	@Test
	void list_joinfields() throws Exception {
		// §8.3.9.28: joinfields creates string with sep, begin, end
		// Eclipse: list->joinfields(',', '<', '>')
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(String) := List{'a', 'b', 'c'};
				        log(l->joinfields(',', '<', '>'));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "<a,b,c>");
	}

	@Test
	void list_joinfieldsEmpty() throws Exception {
		// §8.3.9.28: joinfields on empty list → begin+end only
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(String) := List{};
				        log(l->joinfields(',', '[', ']'));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "[]");
	}

	// ---- Sequence-inherited operations (verify on List) ----

	@Test
	void list_at() throws Exception {
		// §8.3.9.10: 1-based indexing (inherited from Sequence)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(String) := List{'x', 'y', 'z'};
				        log(l->at(2));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "y");
	}

	@Test
	void list_first() throws Exception {
		// §8.3.9.18: first element (inherited from Sequence)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(String) := List{'alpha', 'beta'};
				        log(l->first());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "alpha");
	}

	@Test
	void list_last() throws Exception {
		// §8.3.9.29: last element (inherited from Sequence)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(String) := List{'alpha', 'beta'};
				        log(l->last());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "beta");
	}

	@Test
	void list_indexOf() throws Exception {
		// §8.3.9.24: 1-based indexOf (inherited from Sequence)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(String) := List{'a', 'b', 'c'};
				        log(l->indexOf('b').repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "2");
	}

	@Test
	void list_reverse() throws Exception {
		// §8.3.9.41: reverse returns new list in opposite order
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(Integer) := List{1, 2, 3};
				        var r := l->reverse();
				        log(r->at(1).repr());
				        log(r->at(3).repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "3");
		assertLogged(result, "1");
	}

	@Test
	void list_includes() throws Exception {
		// §8.3.9.20: includes membership check
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(String) := List{'a', 'b'};
				        log(l->includes('a').repr());
				        log(l->includes('z').repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "true", "false");
	}

	@Test
	void list_excluding() throws Exception {
		// §8.3.9.17: excluding returns new list without element
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(String) := List{'a', 'b', 'a', 'c'};
				        var l2 := l->excluding('a');
				        log(l2->size().repr());
				        log(l->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "2");
		assertLogged(result, "4");
	}

	// ---- Integration / Edge Cases ----

	@Test
	void list_mutationVisibleThroughReference() throws Exception {
		// List is mutable — mutation visible through all references
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l1 : List(String) := List{'a'};
				        var l2 := l1;
				        l2->add('b');
				        log(l1->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "2");
	}

	@Test
	void list_usedAsHelperParameter() throws Exception {
		// List passed as helper parameter
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper describeList(l : List(String)) : String {
				        return 'items=' + l->size().repr();
				    }
				    main() {
				        var l : List(String) := List{'a', 'b', 'c'};
				        log(describeList(l));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "items=3");
	}

	@Test
	void list_addThenRemoveRoundtrip() throws Exception {
		// Round-trip: add elements, then remove them
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var l : List(String) := List{};
				        l->add('x');
				        l->add('y');
				        l->add('z');
				        l->remove('y');
				        log(l->size().repr());
				        log(l->at(1));
				        log(l->at(2));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "2");
		assertLogged(result, "x");
		assertLogged(result, "z");
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
