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
 * End-to-end tests for QVT-O String extensions (§8.3.16).
 *
 * <p>QVT-O extends OCL's standard String operations with additional methods
 * for substring extraction, case conversion, search, regex, quoting,
 * type parsing, and type matching.
 *
 * <p>Eclipse reference: {@code stdlibString/stdlibString.qvto}
 *
 * <p><b>SPEC-FIRST:</b> Tests written against QVT-O v1.3 §8.3.16.
 * Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eStringExtTest extends AbstractQvtoEngineTest {

	// ==== P8-03: String Extensions §8.3.16 ====

	// ---- §8.3.16.2: length() : Integer (deprecated synonym for size) ----

	@Test
	void string_length() throws Exception {
		// §8.3.16.2: length is synonym for size (deprecated)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('hello'.length().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "5");
	}

	// ---- §8.3.16.3: substringBefore(match) : String ----

	@Test
	void string_substringBefore() throws Exception {
		// §8.3.16.3: Eclipse: '12345'.substringBefore('34') = '12'
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('12345'.substringBefore('34'));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "12");
	}

	@Test
	void string_substringBeforeNotFound() throws Exception {
		// §8.3.16.3: returns null if match not found
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('12345'.substringBefore('99').oclIsUndefined().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "true");
	}

	// ---- §8.3.16.4: substringAfter(match) : String ----

	@Test
	void string_substringAfter() throws Exception {
		// §8.3.16.4: Eclipse: '12345'.substringAfter('12') = '345'
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('12345'.substringAfter('12'));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "345");
	}

	@Test
	void string_substringAfterNotFound() throws Exception {
		// §8.3.16.4: returns null if match not found
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('12345'.substringAfter('99').oclIsUndefined().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "true");
	}

	// ---- §8.3.16.7: firstToUpper() : String ----

	@Test
	void string_firstToUpper() throws Exception {
		// §8.3.16.7: Eclipse: 'abc'.firstToUpper() = 'Abc'
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('abc'.firstToUpper());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "Abc");
	}

	@Test
	void string_firstToUpperEmpty() throws Exception {
		// §8.3.16.7: empty string returns empty string
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log(''.firstToUpper().size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "0");
	}

	// ---- §8.3.16.8: lastToUpper() : String ----

	@Test
	void string_lastToUpper() throws Exception {
		// §8.3.16.8: Eclipse: 'abc'.lastToUpper() = 'abC'
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('abc'.lastToUpper());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "abC");
	}

	// ---- §8.3.16.13: normalizeSpace() : String ----

	@Test
	void string_normalizeSpace() throws Exception {
		// §8.3.16.13: Eclipse: ' x vh  y '.normalizeSpace() = 'x vh y'
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log(' x vh  y '.normalizeSpace());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "x vh y");
	}

	// ---- §8.3.16.17: find(match) : Integer (1-indexed) ----

	@Test
	void string_find() throws Exception {
		// §8.3.16.17: Eclipse: 'a1212c'.find('12') = 2 (1-indexed)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('a1212c'.find('12').repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "2");
	}

	@Test
	void string_findNotFound() throws Exception {
		// §8.3.16.17: returns -1 if not found
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('abc'.find('xyz').repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "-1");
	}

	// ---- §8.3.16.18: rfind(match) : Integer (1-indexed, from right) ----

	@Test
	void string_rfind() throws Exception {
		// §8.3.16.18: Eclipse: 'a1212c'.rfind('12') = 4 (1-indexed, last occurrence)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('a1212c'.rfind('12').repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "4");
	}

	// ---- §8.3.16.15: match(pattern) : Boolean (regex) ----

	@Test
	void string_match() throws Exception {
		// §8.3.16.15: Eclipse: 'aaaab'.match('a*b') = true
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('aaaab'.match('a*b').repr());
				        log('xyz'.match('a*b').repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "true", "false");
	}

	// ---- §8.3.16.14: replace(m1, m2) : String (literal replacement) ----

	@Test
	void string_replace() throws Exception {
		// §8.3.16.14: all occurrences of m1 replaced by m2 (literal, not regex)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('aXbXc'.replace('X', '-'));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "a-b-c");
	}

	// ---- §8.3.16.19-21: isQuoted, quotify, unquotify ----

	@Test
	void string_isQuoted() throws Exception {
		// §8.3.16.19: Eclipse: 'aa1234aa'.isQuoted('aa') = true
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('aa1234aa'.isQuoted('aa').repr());
				        log('aa1234'.isQuoted('aa').repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "true", "false");
	}

	@Test
	void string_quotify() throws Exception {
		// §8.3.16.20: Eclipse: '1234'.quotify('aa') = 'aa1234aa'
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('1234'.quotify('aa'));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "aa1234aa");
	}

	@Test
	void string_unquotify() throws Exception {
		// §8.3.16.21: Eclipse: 'aa1234aa'.unquotify('aa') = '1234'
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('aa1234aa'.unquotify('aa'));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "1234");
	}

	// ---- §8.3.16.27-30: asBoolean, asInteger, asReal ----

	@Test
	void string_asBoolean() throws Exception {
		// §8.3.16.27: Eclipse: 'true'.asBoolean() = true, '1'.asBoolean() = true
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('true'.asBoolean().repr());
				        log('1'.asBoolean().repr());
				        log('false'.asBoolean().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "true", "true", "false");
	}

	@Test
	void string_asBooleanInvalid() throws Exception {
		// §8.3.16.27: returns null for unparseable strings
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('ddd'.asBoolean().oclIsUndefined().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "true");
	}

	@Test
	void string_asInteger() throws Exception {
		// §8.3.16.28: Eclipse: '123'.asInteger() = 123
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('123'.asInteger().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "123");
	}

	@Test
	void string_asIntegerInvalid() throws Exception {
		// §8.3.16.28: returns null for non-integer strings
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('abc'.asInteger().oclIsUndefined().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "true");
	}

	@Test
	void string_asReal() throws Exception {
		// §8.3.16.30: Eclipse: '123'.asFloat() = 123.0
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('3.14'.asReal().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "3.14");
	}

	// ---- §8.3.16.22-26: matchBoolean, matchInteger, matchReal, matchIdentifier ----

	@Test
	void string_matchBoolean() throws Exception {
		// §8.3.16.22: returns true if string is "true"/"false"/"0"/"1" (case-insensitive)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('true'.matchBoolean().repr());
				        log('FALSE'.matchBoolean().repr());
				        log('1'.matchBoolean().repr());
				        log('xyz'.matchBoolean().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "true", "true", "true", "false");
	}

	@Test
	void string_matchInteger() throws Exception {
		// §8.3.16.23: returns true if string represents an integer
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('123'.matchInteger().repr());
				        log('abc'.matchInteger().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "true", "false");
	}

	@Test
	void string_matchReal() throws Exception {
		// §8.3.16.25: returns true if string represents a real
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('3.14'.matchReal().repr());
				        log('abc'.matchReal().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "true", "false");
	}

	@Test
	void string_matchIdentifier() throws Exception {
		// §8.3.16.26: returns true if string is alphanumeric word (letter first)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('abc123'.matchIdentifier().repr());
				        log('1abc'.matchIdentifier().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLoggedInOrder(result, "true", "false");
	}

	// ---- §8.3.16.1: format(value) : String ----

	@Test
	void string_format() throws Exception {
		// §8.3.16.1: format with %s placeholder
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        log('Hello %s!'.format('World'));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "Hello World!");
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
