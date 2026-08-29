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
package org.eclipse.fennec.m2x.unit.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.fennec.m2x.unit.api.UnitNames;
import org.junit.jupiter.api.Test;

/**
 * A unit name goes into an OSGi filter, and a unit name comes out of a transformation — QVT-O
 * lets an identifier be written escaped ({@code _'…'}), so it can contain anything (#183).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class UnitNamesTest {

	@Test
	void anOrdinaryNameIsUnchanged() {
		assertEquals("lib.Strings", UnitNames.escapeFilterValue("lib.Strings"));
	}

	@Test
	void theFourCharactersAFilterReadsAreEscaped() {
		// OSGi Core §3.2.7
		assertEquals("\\*", UnitNames.escapeFilterValue("*"));
		assertEquals("\\(", UnitNames.escapeFilterValue("("));
		assertEquals("\\)", UnitNames.escapeFilterValue(")"));
		assertEquals("\\\\", UnitNames.escapeFilterValue("\\"));
	}

	@Test
	void aNameThatWouldReopenTheFilterIsNeutralised() {
		// Concatenated raw, this turns "the unit named X" into "every unit"
		assertEquals("foo\\)\\(qvto.unit.name=\\*",
				UnitNames.escapeFilterValue("foo)(qvto.unit.name=*"));
	}

	@Test
	void nullIsRefused() {
		assertThrows(NullPointerException.class, () -> UnitNames.escapeFilterValue(null));
	}
}
