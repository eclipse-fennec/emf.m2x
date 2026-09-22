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

import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoEvaluationOptions;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The OCL half of a transformation runs with the options the embedder configured (#258).
 *
 * <p>Until #258 the evaluator built {@code OclEvaluationOptions.lenient()} on the spot for every
 * OCL sub-expression: {@code QvtoEvaluationOptions.oclOptions()} was consumed nowhere, and the
 * OCL engine's own defaults — {@code OclConfiguration}, or {@code ocl.*} under ConfigAdmin —
 * did not reach QVT-O either. A tightened limit on either side changed nothing.
 */
class QvtoOclOptionsTest extends AbstractQvtoEngineTest {

	/** An OCL range of a thousand elements; a collection limit below that makes it invalid. */
	private static final String RANGE_OF_A_THOUSAND = """
			transformation T();
			main() {
				log(Sequence{1..1000}->size().repr());
			}
			""";

	@Test
	@DisplayName("a limit tightened via withOclOptions applies inside the transformation")
	void oclOptionsOfTheRun_areHonoured() throws QvtoParseException {
		QvtoExecutionResult result = execute(RANGE_OF_A_THOUSAND, QvtoExecutionContext.of(),
				QvtoEvaluationOptions.defaults().withOclOptions(
						OclEvaluationOptions.strict().withMaxCollectionSize(100)));

		assertTrue(hasDiagnosticContaining(result, "exceeds maximum allowed size"),
				() -> "diagnostics: " + result.diagnostics());
	}

	@Test
	@DisplayName("with no OCL options on the run, the OCL engine's configured defaults apply")
	void defaultsOfTheOclEngine_areHonoured() throws QvtoParseException {
		QvtoEngine tightened = QvtoEngines.create(QvtoConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).maxCollectionSize(100).build()).build());

		QvtoExecutionResult result = tightened.execute(tightened.parse(RANGE_OF_A_THOUSAND, "T"),
				QvtoExecutionContext.of(), QvtoEvaluationOptions.defaults());

		assertTrue(hasDiagnosticContaining(result, "exceeds maximum allowed size"),
				() -> "diagnostics: " + result.diagnostics());
	}

	@Test
	@DisplayName("OCL options on the run take precedence over the engine's defaults")
	void oclOptionsOfTheRun_replaceTheEngineDefaults() throws QvtoParseException {
		QvtoEngine tightened = QvtoEngines.create(QvtoConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).maxCollectionSize(100).build()).build());

		QvtoExecutionResult result = tightened.execute(tightened.parse(RANGE_OF_A_THOUSAND, "T"),
				QvtoExecutionContext.of(),
				QvtoEvaluationOptions.defaults().withOclOptions(OclEvaluationOptions.strict()));

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertFalse(hasDiagnosticContaining(result, "exceeds maximum allowed size"));
	}

	@Test
	@DisplayName("null handling stays LENIENT whatever the OCL options say: QVT-O reads null as empty")
	void nullHandling_staysLenient_evenWithStrictOclOptions() throws QvtoParseException {
		// Module-level operations have no self, and QVT-O reads a null operand as the empty
		// string (QvtoDispatchTest.nullConcatenatesAsEmpty); STRICT would answer invalid.
		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
					var missing : String := null;
					log('x' + missing);
				}
				""", QvtoExecutionContext.of(),
				QvtoEvaluationOptions.defaults().withOclOptions(OclEvaluationOptions.strict()));

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertTrue(hasDiagnosticContaining(result, "x"), () -> "diagnostics: " + result.diagnostics());
	}

	private static boolean hasDiagnosticContaining(QvtoExecutionResult result, String substring) {
		return result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains(substring));
	}
}
