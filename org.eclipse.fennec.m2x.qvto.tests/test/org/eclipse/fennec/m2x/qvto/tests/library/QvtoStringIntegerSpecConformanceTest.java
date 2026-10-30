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
package org.eclipse.fennec.m2x.qvto.tests.library;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * String and Integer operations of QVT v1.3 §8.3.16 and §8.3.17 (#344).
 */
class QvtoStringIntegerSpecConformanceTest extends AbstractQvtoEngineTest {

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
	void format_fillsSeveralHolesFromATuple() throws Exception {
		// §8.3.16.1: "The value, necessarily a tuple if more than a hole"
		QvtoExecutionResult result = run("log('t=' + '%s-%d'.format(Tuple{a = 'x', b = 3}));");
		assertSuccess(result);
		assertLogged(result, "t=x-3");
	}

	@Test
	void format_fillsKeyedHolesFromADict() throws Exception {
		// §8.3.16.1: "A dictionary can be passed … holes have the syntax %(key)s"
		QvtoExecutionResult result = run("""
				var d : Dict(String, String) := Dict{};
				d->put('k', 'v');
				log('d=' + '%(k)s!'.format(d));
				""");
		assertSuccess(result);
		assertLogged(result, "d=v!");
	}

	@Test
	void format_isIndependentOfTheJvmLocale() throws Exception {
		Locale saved = Locale.getDefault();
		Locale.setDefault(Locale.GERMANY);
		try {
			QvtoExecutionResult result = run("log('f=' + '%.2f'.format(1.5));");
			assertSuccess(result);
			assertLogged(result, "f=1.50");
		} finally {
			Locale.setDefault(saved);
		}
	}

	@Test
	void format_takesACollectionAsOneValue() throws Exception {
		QvtoExecutionResult result = run("log('v=' + 'v=%s'.format(Sequence{1, 2}));");
		assertSuccess(result);
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains("v=v=") && d.getMessage().contains("2")),
				() -> result.diagnostics().toString());
	}

	@Test
	void format_withFewerPartsThanHoles_isInvalid() throws Exception {
		QvtoExecutionResult result = run("log('i=' + '%s %s %s'.format(Tuple{a = 1, b = 2}).oclIsInvalid().repr());");
		assertLogged(result, "i=true");
	}

	@Test
	void stringPositions_countCodePoints() throws Exception {
		// §8.3.16.2: length is "a synonym of the OCL String::size()", which counts code points (#329)
		QvtoExecutionResult result = run("""
				log('len=' + 'a😀b'.length().repr() + ' size=' + 'a😀b'.size().repr());
				log('find=' + 'a😀b'.find('b').repr() + ' rfind=' + 'ba😀b'.rfind('b').repr());
				log('missing=' + 'abc'.find('x').repr());
				""");
		assertSuccess(result);
		assertLogged(result, "len=3 size=3");
		assertLogged(result, "find=3 rfind=4");
		assertLogged(result, "missing=-1");
	}

	@Test
	void firstToUpper_titlecasesACodePoint() throws Exception {
		QvtoExecutionResult result = run("log('t=' + 'ǆx'.firstToUpper() + ' l=' + 'x𐐨'.lastToUpper());");
		assertSuccess(result);
		assertLogged(result, "t=ǅx l=x𐐀");
	}

	@Test
	void trim_removesUnicodeWhiteSpace() throws Exception {
		QvtoExecutionResult result = run("""
				log('[' + ' x '.trim() + ']');
				log('[' + ' a    b '.normalizeSpace() + ']');
				""");
		assertSuccess(result);
		assertLogged(result, "[x]");
		assertLogged(result, "[a b]");
	}

	@Test
	void range_isCallableOnTheType_withIntegersOfAnySize() throws Exception {
		// §8.3.17: Integer::range(start, end)
		QvtoExecutionResult result = run("""
				log('r=' + Integer.range(1, 3)->size().repr());
				log('big=' + Integer.range(9223372036854775807, 9223372036854775807 + 1)->size().repr());
				""");
		assertSuccess(result);
		assertLogged(result, "r=3");
		assertLogged(result, "big=2");
	}

	@Test
	void anIntegerLiteralBeyond64Bit_isAParseError() {
		// As in the OCL parser (#322): the literal is held as an ELong
		QvtoParseException failure = assertThrows(QvtoParseException.class,
				() -> run("log(9223372036854775808.repr());"));
		assertTrue(failure.getMessage().contains("Integer literal out of range (64 bit): 9223372036854775808"),
				failure::getMessage);
	}

	@Test
	void aStringCounter_calledOnAString_counts() throws Exception {
		QvtoExecutionResult result = run("""
				'c'.incrStrCounter('k');
				log('n=' + String.getStrCounter('k').repr());
				""");
		assertSuccess(result);
		assertLogged(result, "n=1");
	}

	private static void assertSuccess(QvtoExecutionResult result) {
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains(expected)),
				() -> "Expected log containing '" + expected + "' but diagnostics were: " + result.diagnostics());
	}
}
