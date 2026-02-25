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
package org.eclipse.fennec.m2x.qvto.tests.mapping;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for mapping result variations (P9-07).
 *
 * <p>Based on QVT-O v1.3 §8.1.6, §8.1.11.2 and Eclipse reference tests:
 * <ul>
 *   <li>{@code collectionMappingResult/collectionMappingResult.qvto}</li>
 *   <li>{@code multiresultpars/multiresultpars.qvto}</li>
 *   <li>{@code populationSection/populationSection.qvto}</li>
 * </ul>
 *
 * <p><b>SPEC-FIRST:</b> Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eMappingResultVariationsTest extends AbstractQvtoEngineTest {

	// ==== P9-07: Mapping Result Variations ====

	// ---- Explicit result assignment in init section ----

	@Test
	void result_explicitAssignmentInInit() throws Exception {
		// Eclipse collectionMappingResult: result assigned in init { ... }
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Item { name : String; }
				    mapping createItem(n : String) : Item {
				        init {
				            result := object Item { name := n; };
				        }
				    }
				    main() {
				        var item := map createItem('hello');
				        log('name:' + item.name);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "name:hello");
	}

	// ---- Result populated in body (implicit object construction) ----

	@Test
	void result_populatedInBody() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Item { name : String; value : Integer; }
				    mapping createItem() : Item {
				        name := 'body-item';
				        value := 42;
				    }
				    main() {
				        var item := map createItem();
				        log('name:' + item.name);
				        log('value:' + item.value.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "name:body-item");
		assertLogged(result, "value:42");
	}

	// ---- Result consistent between init and end ----

	@Test
	void result_consistentBetweenInitAndEnd() throws Exception {
		// Eclipse collectionMappingResult: fooInitEndConcordance
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Box { label : String; }
				    mapping makeBox() : Box {
				        init {
				            result := object Box { label := 'from-init'; };
				        }
				        end {
				            log('end-label:' + result.label);
				        }
				    }
				    main() {
				        var b := map makeBox();
				        log('main-label:' + b.label);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "end-label:from-init");
		assertLogged(result, "main-label:from-init");
	}

	// ---- Result modified in body and end ----

	@Test
	void result_modifiedInBodyAndEnd() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Trail { log : String; }
				    mapping buildTrail() : Trail {
				        init {
				            result := object Trail { log := 'init'; };
				        }
				        result.log := result.log + '>body';
				        end {
				            result.log := result.log + '>end';
				        }
				    }
				    main() {
				        var t := map buildTrail();
				        log('trail:' + t.log);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "trail:init>body>end");
	}

	// ---- Mapping result used in collection ----

	@Test
	void result_collectedFromMultipleMappings() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Tag { label : String; }
				    mapping makeTag(s : String) : Tag {
				        label := s;
				    }
				    main() {
				        var names := Sequence {'a', 'b', 'c'};
				        var tags : Sequence(Tag) := Sequence {};
				        names->forEach(n) {
				            tags += map makeTag(n);
				        };
				        log('count:' + tags->size().repr());
				        tags->forEach(t) { log(t.label); };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "count:3");
		assertLoggedInOrder(result, "a", "b", "c");
	}

	// ---- Mapping with context returning result ----

	@Test
	void result_contextMappingReturnsNewObject() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Node { name : String; }
				    intermediate class Copy { original : String; }
				    mapping Node::toCopy() : Copy {
				        original := 'copy-of-' + self.name;
				    }
				    main() {
				        var n := object Node { name := 'src'; };
				        var c := n.map toCopy();
				        log('original:' + c.original);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "original:copy-of-src");
	}

	// ---- Mapping with null result from init (body still populates) ----

	@Test
	void result_initDoesNotSetResult_bodyPopulates() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Item { name : String; }
				    mapping makeItem() : Item {
				        init {
				            -- do nothing — result remains auto-instantiated
				        }
				        name := 'auto';
				    }
				    main() {
				        var item := map makeItem();
				        log('name:' + item.name);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "name:auto");
	}

	// ---- Mapping with helper accessing result ----

	@Test
	void result_accessedViaHelper() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Data { value : Integer; }
				    helper computeValue() : Integer {
				        return 7 * 6;
				    }
				    mapping createData() : Data {
				        value := computeValue();
				    }
				    main() {
				        var d := map createData();
				        log('value:' + d.value.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "value:42");
	}

	// ---- Multiple mappings on same object — trace inhibition returns cached result ----

	@Test
	void result_traceInhibitionReturnsCachedResult() throws Exception {
		// §8.1.11.2: Second call with same context returns cached result
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Node { name : String; }
				    intermediate class Out { label : String; }
				    mapping Node::convert() : Out {
				        label := 'converted-' + self.name;
				    }
				    main() {
				        var n := object Node { name := 'x'; };
				        var r1 := n.map convert();
				        var r2 := n.map convert();
				        log('same:' + (r1 = r2).toString());
				        log('label:' + r2.label);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "same:true");
		assertLogged(result, "label:converted-x");
	}

	// ---- Multi-result query with named results ----

	@Test
	void result_multiResultQuery() throws Exception {
		// Eclipse multiresultpars: query fooQuery() : p : EPackage, id : Integer
		QvtoExecutionResult result = execute("""
				transformation test() {
				    query splitName(full : String) : first : String, last : String {
				        first := full.substringBefore(' ');
				        last := full.substringAfter(' ');
				    }
				    main() {
				        var r := splitName('John Doe');
				        log('first:' + r.first);
				        log('last:' + r.last);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "first:John");
		assertLogged(result, "last:Doe");
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
