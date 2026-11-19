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
package org.eclipse.fennec.m2x.m2t.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * The MOFM2T §8.3 string library as the spec and the decisions of #373 read it: substitute and
 * substituteAll, null and invalid sources and arguments, negative lengths, strtok's flag, the
 * empty string for isAlpha, locale-independent case mapping, strcmp's sign, and the §8.3.2/§8.3.3
 * {@code toString} operations in a template.
 */
class M2tLibraryConformanceTest {

	private static final M2tEngine ENGINE = M2tEngines.create(M2tConfiguration.builder(
			OclConfiguration.builder(new OclParserSupport()).build()).build());

	/** {@code c}: a class Employee without instance class name, so c.instanceClassName is null. */
	private static M2tResult run(String body) throws M2tParseException {
		Module module = ENGINE.parse("[module doc(ecore)/]\n[template public main(c : EClass)]\n[file ('out', false)]"
				+ body + "[/file]\n[/template]\n", "doc");
		EClass employee = EcoreFactory.eINSTANCE.createEClass();
		employee.setName("Employee");
		EPackage company = EcoreFactory.eINSTANCE.createEPackage();
		company.setName("company");
		company.getEClassifiers().add(employee);
		return ENGINE.execute(module, M2tContext.of(employee));
	}

	private static String eval(String expression) throws M2tParseException {
		M2tResult result = run("[" + expression + "/]");
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		return result.generatedFiles().get("out");
	}

	/** Asserts the expression is invalid with an ERROR that says the given text, and no Java exception. */
	private static void assertRejected(String expression, String message) throws M2tParseException {
		M2tResult result = run("<[" + expression + "/]>");
		assertEquals("<>", result.generatedFiles().get("out"));
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getSeverity() == Diagnostic.ERROR
				&& d.getMessage().contains(message) && !d.getMessage().contains("Exception")),
				() -> "expected an ERROR with '" + message + "': " + result.diagnostics());
	}

	@Nested
	@DisplayName("substitute and substituteAll")
	class Substitute {

		@Test
		@DisplayName("substitute replaces the first occurrence")
		void firstOccurrence() throws M2tParseException {
			assertEquals("a-bXc", eval("'aXbXc'.substitute('X', '-')"));
		}

		@Test
		@DisplayName("substitute without an occurrence returns the original string")
		void noOccurrence() throws M2tParseException {
			assertEquals("abc", eval("'abc'.substitute('X', '-')"));
		}

		@Test
		@DisplayName("substituteAll — of the OCL standard library — replaces every occurrence")
		void allOccurrences() throws M2tParseException {
			assertEquals("a-b-c", eval("'aXbXc'.substituteAll('X', '-')"));
		}

		@Test
		@DisplayName("an empty search string is invalid")
		void emptySearchString() throws M2tParseException {
			assertRejected("'abc'.substitute('', '-')", "Empty search string for operation 'substitute'");
		}
	}

	@Nested
	@DisplayName("null, invalid and out of range")
	class Rejected {

		@Test
		@DisplayName("a null argument is invalid with an ERROR")
		void nullArgument() throws M2tParseException {
			assertRejected("'abc'.substitute(c.instanceClassName, 'x')", "Null argument for operation 'substitute'");
		}

		@Test
		@DisplayName("a null length is invalid with an ERROR, not a NullPointerException")
		void nullLength() throws M2tParseException {
			// no features: any finds nothing, its lowerBound navigates leniently to null
			assertRejected("'abc'.first(c.eStructuralFeatures->any(true).lowerBound)", "Null argument for operation 'first'");
		}

		@Test
		@DisplayName("a negative length is invalid with an ERROR")
		void negativeLength() throws M2tParseException {
			assertRejected("'abc'.first(-1)", "Negative length -1 for operation 'first'");
			assertRejected("'abc'.last(-1)", "Negative length -1 for operation 'last'");
		}

		@Test
		@DisplayName("a length beyond the string is the whole string")
		void longLength() throws M2tParseException {
			assertEquals("abc|abc", eval("'abc'.first(10) + '|' + 'abc'.last(10)"));
		}
	}

	@Nested
	@DisplayName("strtok")
	class Strtok {

		@Test
		@DisplayName("0 starts, 1 continues, and the end gives the empty string")
		void continuation() throws M2tParseException {
			assertEquals("a|b|c|", eval("'a,b;c'.strtok(',;', 0) + '|' + 'a,b;c'.strtok(',;', 1) + '|'"
					+ " + 'a,b;c'.strtok(',;', 1) + '|' + 'a,b;c'.strtok(',;', 1)"));
		}

		@Test
		@DisplayName("1 without a start starts")
		void continuationWithoutStart() throws M2tParseException {
			assertEquals("x", eval("'x y'.strtok(' ', 1)"));
		}

		@Test
		@DisplayName("a flag other than 0 or 1 is invalid")
		void otherFlag() throws M2tParseException {
			assertRejected("'x y'.strtok(' ', 2)", "strtok takes the flag 0 to start and 1 to continue, not 2");
		}
	}

	@Nested
	@DisplayName("comparison, character classes, case")
	class Characters {

		@Test
		@DisplayName("strcmp has the sign of C's strcmp(self, s1)")
		void strcmpSign() throws M2tParseException {
			assertEquals("true|true|0", eval("('b'.strcmp('a') > 0).toString() + '|' + ('a'.strcmp('b') < 0).toString()"
					+ " + '|' + 'a'.strcmp('a').toString()"));
		}

		@Test
		@DisplayName("the empty string consists only of letters, and only of letters and digits")
		void emptyStringIsAlpha() throws M2tParseException {
			assertEquals("true|true", eval("''.isAlpha().toString() + '|' + ''.isAlphanum().toString()"));
		}

		@Test
		@DisplayName("a digit is no letter")
		void digitIsNoLetter() throws M2tParseException {
			assertEquals("false|true", eval("'a1'.isAlpha().toString() + '|' + 'a1'.isAlphanum().toString()"));
		}

		@Test
		@DisplayName("case mapping does not depend on the default locale")
		void localeIndependentCase() throws M2tParseException {
			Locale before = Locale.getDefault();
			Locale.setDefault(Locale.forLanguageTag("tr-TR"));
			try {
				assertEquals("I|i|Item", eval("'i'.toUpper() + '|' + 'I'.toLower() + '|' + 'item'.toUpperFirst()"));
			} finally {
				Locale.setDefault(before);
			}
		}
	}

	@Nested
	@DisplayName("§8.3.2, §8.3.3: toString")
	class ToString {

		@Test
		@DisplayName("Integer::toString and Real::toString in a template")
		void integerAndReal() throws M2tParseException {
			assertEquals("42|1.5", eval("42.toString() + '|' + (1.5).toString()"));
		}
	}
}
