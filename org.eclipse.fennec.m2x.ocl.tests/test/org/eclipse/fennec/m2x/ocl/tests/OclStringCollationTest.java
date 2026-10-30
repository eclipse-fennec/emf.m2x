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
package org.eclipse.fennec.m2x.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Strings compare by Unicode collation in the oclLocale and count Unicode characters
 * (OCL v2.4 §11.1, §11.4.3, §11.5.3, #329).
 */
class OclStringCollationTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 1.0, true);
	}

	@Nested
	class Collation {

		@Test
		void caseDoesNotDecideBeforeTheLetter() throws OclParseException {
			// code-point order put every capital before every small letter: 'B' < 'a'
			assertEquals(false, eval("'B' < 'a'", self));
			assertEquals(true, eval("'a' < 'B'", self));
		}

		@Test
		void accentedLetters_sortWithTheirBaseLetter() throws OclParseException {
			assertEquals(true, eval("let oclLocale : String = 'de_DE' in 'ä' < 'b'", self));
			assertEquals(true, eval("'ä' < 'b'", self));
		}

		@Test
		void sortedBy_usesCollation() throws OclParseException {
			assertEquals(List.of("apple", "Banana", "cherry"),
					eval("Sequence{'cherry', 'Banana', 'apple'}->sortedBy(s | s)", self));
		}

		@Test
		void maxMin_useCollation() throws OclParseException {
			assertEquals("Banana", eval("Sequence{'apple', 'Banana'}->max()", self));
			assertEquals("apple", eval("Sequence{'apple', 'Banana'}->min()", self));
		}

		@Test
		void equalStrings_stayEqual() throws OclParseException {
			assertEquals(true, eval("'abc' <= 'abc'", self));
			assertEquals(false, eval("'abc' < 'abc'", self));
		}
	}

	/** §11.5.3 equalsIgnoreCase: self.toUpperCase() = s.toUpperCase(), in the locale. */
	@Nested
	class EqualsIgnoreCase {

		@Test
		void followsToUpperCase() throws OclParseException {
			assertEquals(true, eval("'ß'.equalsIgnoreCase('SS')", self));
			assertEquals(true, eval("'Hello'.equalsIgnoreCase('hELLO')", self));
		}

		@Test
		void turkishLocale_tellsTheDottedIApart() throws OclParseException {
			assertEquals(false, eval("let oclLocale : String = 'tr_TR' in 'i'.equalsIgnoreCase('I')", self));
		}
	}

	/** §11.4.3: a String is a sequence of characters — code points, not UTF-16 units. */
	@Nested
	class CodePoints {

		@Test
		void supplementaryCharacter_countsOnce() throws OclParseException {
			assertEquals(1, eval("'😀'.size()", self));
			assertEquals(3, eval("'a😀b'.size()", self));
			assertEquals(3, eval("'a😀b'.characters()->size()", self));
		}

		@Test
		void positions_areCharacterPositions() throws OclParseException {
			assertEquals("b", eval("'a😀b'.at(3)", self));
			assertEquals("😀", eval("'a😀b'.at(2)", self));
			assertEquals("😀b", eval("'a😀b'.substring(2, 3)", self));
			assertEquals(3, eval("'a😀b'.indexOf('b')", self));
			assertInvalid("'a😀b'.at(4)", self);
		}
	}
}
