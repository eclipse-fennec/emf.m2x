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
package org.eclipse.fennec.m2x.ocl.engine.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The order OCL sorts by has to be an order (#187).
 *
 * <p>{@code sortedBy}, {@code max} and {@code min} all go through one comparator, and it is
 * handed whatever the expression produced — values of different types included, which OCL
 * gives no order to. Answering "equal" for those looks harmless and is not: it makes equality
 * intransitive, and a sort that notices refuses to run at all with "Comparison method violates
 * its general contract" — from inside the JDK, in the middle of a user's transformation.
 */
class OclCollectionUtilTest {

	@Test
	@DisplayName("values it cannot compare are ordered, not called equal")
	void incomparableValuesAreOrdered() {
		assertNotEquals(0, OclCollectionUtil.compareOcl(1L, "a"),
				"a number and a string are not the same value");
		assertNotEquals(0, OclCollectionUtil.compareOcl("a", true));
	}

	@Test
	@DisplayName("equality stays transitive across types")
	void equalityIsTransitive() {
		// The shape that breaks a sort: 1 and 2 differ, but if both are "equal" to "a", then
		// equality is intransitive and no consistent order exists
		int oneVsTwo = OclCollectionUtil.compareOcl(1L, 2L);
		int oneVsText = OclCollectionUtil.compareOcl(1L, "a");
		int twoVsText = OclCollectionUtil.compareOcl(2L, "a");

		assertTrue(oneVsTwo < 0, "1 is less than 2");
		assertTrue(oneVsText != 0 || twoVsText != 0,
				"two values that differ cannot both be equal to a third");
	}

	@Test
	@DisplayName("the order is antisymmetric")
	void comparisonIsAntisymmetric() {
		List<Object> values = List.of(1L, 2.5d, "a", "b", true, List.of());
		for (Object a : values) {
			for (Object b : values) {
				int forward = OclCollectionUtil.compareOcl(a, b);
				int backward = OclCollectionUtil.compareOcl(b, a);
				assertEquals(Integer.signum(forward), -Integer.signum(backward),
						() -> "compare(" + a + ", " + b + ") and its reverse disagree");
			}
		}
	}

	@Test
	@DisplayName("numbers compare across Long and Double, as OCL says")
	void numbersCompareAcrossTypes() {
		assertTrue(OclCollectionUtil.compareOcl(1L, 1.5d) < 0);
		assertEquals(0, OclCollectionUtil.compareOcl(2L, 2.0d));
	}
}
