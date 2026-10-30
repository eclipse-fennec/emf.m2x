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
package org.eclipse.fennec.m2x.qvto.tests.controlflow;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * raise and its StringException shorthand, QVT v1.3 §8.2.2.15 and §8.4 (#340).
 */
class QvtoRaiseSpecConformanceTest extends AbstractQvtoEngineTest {

	private static QvtoExecutionResult run(String main) throws Exception {
		return execute("""
				transformation test() {
				    main() {
				%s
				    }
				}
				""".formatted(main));
	}

	@Test
	void theStringShorthand_raisesAStringException_inEitherQuoteStyle() throws Exception {
		// §8.2.2.15: "a shorthand for raising a StringException with the string as the constructor argument"
		QvtoExecutionResult result = run("""
				try { raise "double"; } except (StringException) { log('caught-double'); };
				try { raise 'single'; } except (StringException) { log('caught-single'); };
				""");
		assertTrue(result.isSuccess(), () -> result.diagnostics().toString());
		assertLogged(result, "caught-double");
		assertLogged(result, "caught-single");
	}

	@Test
	void theStringShorthand_isNoAssertionFailure() throws Exception {
		QvtoExecutionResult result = run("""
				try { raise 'x'; } except (AssertionFailed) { log('wrong'); };
				log('after');
				""");
		assertFalse(result.isSuccess());
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains("Uncaught exception: StringException - x")),
				() -> result.diagnostics().toString());
		assertTrue(result.diagnostics().stream().noneMatch(d -> d.getMessage().contains("wrong")), () -> result.diagnostics().toString());
	}

	@Test
	void raiseNew_raisesTheInstantiatedException() throws Exception {
		QvtoExecutionResult result = run("""
				try { raise new StringException('x'); } except (StringException) { log('caught-new'); };
				""");
		assertTrue(result.isSuccess(), () -> result.diagnostics().toString());
		assertLogged(result, "caught-new");
	}

	@Test
	void anUnknownExceptionType_isAParseError() {
		assertThrows(QvtoParseException.class, () -> run("raise NoSuchException('x');"));
	}

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains(expected)),
				() -> "Expected log containing '" + expected + "' but diagnostics were: " + result.diagnostics());
	}
}
