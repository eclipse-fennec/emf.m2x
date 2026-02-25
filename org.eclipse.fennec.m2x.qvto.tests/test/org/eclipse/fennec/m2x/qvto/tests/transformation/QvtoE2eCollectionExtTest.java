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
 * End-to-end tests for QVT-O Collection extensions.
 *
 * <p>QVT-O extends OCL collection operations with:
 * <ul>
 *   <li>{@code asList()} on all Collection types (§8.3.9.6 / Eclipse CollectionTypeOperations)
 *   <li>{@code reverse()} on OrderedSet (Eclipse OrderedSetTypeOperations)
 *   <li>List conversion operations: {@code asBag()}, {@code asSet()},
 *       {@code asOrderedSet()}, {@code asSequence()} (§8.3.9.5–9)
 * </ul>
 *
 * <p><b>SPEC-FIRST:</b> Tests written against QVT-O v1.3 §8.3.9
 * and Eclipse CollectionTypeOperations / OrderedSetTypeOperations.
 * Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eCollectionExtTest extends AbstractQvtoEngineTest {

	// ==== P8-08: Collection Extensions ====

	// ---- asList() on OCL Collection types ----

	@Test
	void collection_sequenceAsList() throws Exception {
		// Sequence->asList() returns a mutable List with same elements/order
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var seq := Sequence{1, 2, 3};
				        var lst := seq->asList();
				        log('size:' + lst->size().repr());
				        log('first:' + lst->first().repr());
				        log('last:' + lst->last().repr());
				        -- verify it's mutable (List supports add)
				        lst->add(4);
				        log('after:' + lst->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "size:3", "first:1", "last:3", "after:4");
	}

	@Test
	void collection_setAsList() throws Exception {
		// Set->asList() returns a mutable List
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var s := Set{3, 1, 2};
				        var lst := s->asList();
				        log('size:' + lst->size().repr());
				        -- Set has no guaranteed order, but size must match
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:3");
	}

	@Test
	void collection_orderedSetAsList() throws Exception {
		// OrderedSet->asList() returns a mutable List preserving order
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var os := OrderedSet{10, 20, 30};
				        var lst := os->asList();
				        log('size:' + lst->size().repr());
				        log('first:' + lst->first().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "size:3", "first:10");
	}

	@Test
	void collection_bagAsList() throws Exception {
		// Bag->asList() returns a mutable List (duplicates preserved)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var b := Bag{1, 2, 2, 3};
				        var lst := b->asList();
				        log('size:' + lst->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:4");
	}

	@Test
	void collection_listAsList() throws Exception {
		// §8.3.9.6: List->asList() returns a shallow clone
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var orig := List{1, 2, 3};
				        var copy := orig->asList();
				        copy->add(4);
				        log('orig:' + orig->size().repr());
				        log('copy:' + copy->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		// original unchanged, copy has extra element
		assertLoggedInOrder(result, "orig:3", "copy:4");
	}

	// ---- List conversion operations §8.3.9.5–9 ----

	@Test
	void list_asBag() throws Exception {
		// §8.3.9.5: List->asBag() preserves duplicates
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var lst := List{1, 2, 2, 3};
				        var b := lst->asBag();
				        log('size:' + b->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:4");
	}

	@Test
	void list_asSet() throws Exception {
		// §8.3.9.9: List->asSet() removes duplicates
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var lst := List{1, 2, 2, 3};
				        var s := lst->asSet();
				        log('size:' + s->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:3");
	}

	@Test
	void list_asOrderedSet() throws Exception {
		// §8.3.9.7: List->asOrderedSet() preserves order, removes duplicates
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var lst := List{1, 2, 2, 3};
				        var os := lst->asOrderedSet();
				        log('size:' + os->size().repr());
				        log('first:' + os->first().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "size:3", "first:1");
	}

	@Test
	void list_asSequence() throws Exception {
		// §8.3.9.8: List->asSequence() returns immutable Sequence copy
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var lst := List{1, 2, 3};
				        var seq := lst->asSequence();
				        log('size:' + seq->size().repr());
				        log('first:' + seq->first().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "size:3", "first:1");
	}

	// ---- reverse() on OrderedSet ----

	@Test
	void orderedSet_reverse() throws Exception {
		// Eclipse OrderedSetTypeOperations: reverse on OrderedSet
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var os := OrderedSet{1, 2, 3, 4};
				        var rev := os->reverse();
				        log('first:' + rev->first().repr());
				        log('last:' + rev->last().repr());
				        log('size:' + rev->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "first:4", "last:1", "size:4");
	}

	// ---- asList() mutability verification ----

	@Test
	void collection_asListMutable() throws Exception {
		// asList() result must be mutable — add/remove should work
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var seq := Sequence{10, 20, 30};
				        var lst := seq->asList();
				        lst->add(40);
				        lst->removeFirst();
				        log('size:' + lst->size().repr());
				        log('first:' + lst->first().repr());
				        log('last:' + lst->last().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "size:3", "first:20", "last:40");
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
