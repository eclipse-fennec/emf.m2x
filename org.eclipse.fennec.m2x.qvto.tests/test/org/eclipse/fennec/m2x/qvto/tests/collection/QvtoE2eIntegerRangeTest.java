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
package org.eclipse.fennec.m2x.qvto.tests.collection;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * E2E tests for QVT-O v1.3 §8.3.17 — {@code Integer::range(start, end)}.
 *
 * <p>Returns a list of integers from start to end (inclusive).
 *
 * <p><b>SPEC-FIRST:</b> Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eIntegerRangeTest extends AbstractQvtoEngineTest {

	// ---- §8.3.17: Integer::range ----

	@Test
	void range_oneArg() throws Exception {
		// source.range(end) — list from source to end
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var r := 1.range(5);
				        log('size:' + r->size().repr());
				        log('first:' + r->at(1).repr());
				        log('last:' + r->at(5).repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:5");
		assertLogged(result, "first:1");
		assertLogged(result, "last:5");
	}

	@Test
	void range_twoArgs() throws Exception {
		// source.range(start, end) — source ignored, list from start to end
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var r := 42.range(1, 5);
				        log('size:' + r->size().repr());
				        log('first:' + r->at(1).repr());
				        log('last:' + r->at(5).repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:5");
		assertLogged(result, "first:1");
		assertLogged(result, "last:5");
	}

	@Test
	void range_singleElement() throws Exception {
		// range where start == end → single-element list
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var r := 3.range(3);
				        log('size:' + r->size().repr());
				        log('elem:' + r->at(1).repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:1");
		assertLogged(result, "elem:3");
	}

	@Test
	void range_emptyDescending() throws Exception {
		// range where start > end → empty list
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var r := 5.range(3);
				        log('size:' + r->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:0");
	}

	@Test
	void range_negative() throws Exception {
		// range crossing zero
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var r := (-2).range(2);
				        log('size:' + r->size().repr());
				        log('first:' + r->at(1).repr());
				        log('last:' + r->at(5).repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:5");
		assertLogged(result, "first:-2");
		assertLogged(result, "last:2");
	}

	@Test
	void range_resultIsList() throws Exception {
		// Result is a List — supports at() indexing
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var r := 1.range(3);
				        var sum := r->at(1) + r->at(3);
				        log('sum:' + sum.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "sum:4");
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
