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
package org.eclipse.fennec.m2x.qvto.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.qvto.api.QvtoEvaluationOptions;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * {@code +=} with a collection operand on a tuple part is bounded by {@code maxCollectionSize}
 * (#260). The operand is a navigation result, which the standard-library check of #257 never
 * sees, so {@code t.items += t.items} in a loop doubled on every step — thirty steps to a
 * gigabyte, while {@code maxLoopIterations} counted thirty.
 */
class QvtoTuplePartGrowthTest extends AbstractQvtoEngineTest {

	private static final String DOUBLING = """
			transformation T();
			main() {
				var t := Tuple { items = Sequence{1} };
				var i := 0;
				while (i < 40) {
					t.items += t.items;
					i := i + 1;
				};
				log(t.items->size().repr());
			}
			""";

	@Test
	@Timeout(30)
	@DisplayName("doubling a tuple part stops at the collection limit with a diagnostic")
	void doublingATuplePart_stopsAtTheCollectionLimit() throws QvtoParseException {
		QvtoExecutionResult result = execute(DOUBLING, QvtoExecutionContext.of(),
				QvtoEvaluationOptions.defaults().withOclOptions(
						OclEvaluationOptions.strict().withMaxCollectionSize(1000)));

		assertTrue(hasDiagnosticContaining(result, "exceeds maximum allowed size"),
				() -> "diagnostics: " + result.diagnostics());
		// The part kept the last size within the limit: 512, the doubling before 1024
		assertTrue(hasDiagnosticContaining(result, "512"), () -> "diagnostics: " + result.diagnostics());
	}

	@Test
	@DisplayName("+= with a collection operand within the limit still appends all of it")
	void appendingWithinTheLimit_stillWorks() throws QvtoParseException {
		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
					var t := Tuple { items = Sequence{1, 2} };
					t.items += Sequence{3, 4, 5};
					log(t.items->size().repr());
				}
				""", QvtoExecutionContext.of(),
				QvtoEvaluationOptions.defaults().withOclOptions(
						OclEvaluationOptions.strict().withMaxCollectionSize(5)));

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertTrue(hasDiagnosticContaining(result, "5"), () -> "diagnostics: " + result.diagnostics());
		assertFalse(hasDiagnosticContaining(result, "exceeds maximum allowed size"));
	}

	private static boolean hasDiagnosticContaining(QvtoExecutionResult result, String substring) {
		return result.diagnostics().stream().anyMatch(d -> d.getMessage().contains(substring));
	}
}
