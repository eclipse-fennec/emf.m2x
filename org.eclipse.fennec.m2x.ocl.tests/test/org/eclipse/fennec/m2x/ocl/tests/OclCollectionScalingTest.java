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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclOrderedSet;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * The unique collections stay usable on model-sized inputs.
 *
 * <p>{@code OclSet} and {@code OclOrderedSet} deduplicate with OCL equality (§11.5.1:
 * Integer is a subclass of Real, so {@code 4 = 4.0}), which ruled out {@link java.util.HashSet}
 * and left a linear scan per {@code add} — quadratic in the number of elements. A
 * {@code Person.allInstances()->select(...)} over 50 000 instances spent 10.9 s per
 * evaluation on ~1.25 billion equality checks.
 *
 * <p>The scaling tests below are the regression guard. Their time limits are two orders of
 * magnitude above what a hash-based lookup needs, so they say "not quadratic" rather than
 * "fast enough", and do not turn ordinary machine noise into a failure.
 *
 * <p>The semantic tests pin what the linear scan guaranteed, because any lookup structure
 * has to agree with {@code oclEquals} exactly — including the two cases where OCL numeric
 * equality departs from {@code equals}.
 */
class OclCollectionScalingTest extends AbstractOclTest {

	/** Enough that a quadratic build takes tens of seconds; a hashed one takes milliseconds. */
	private static final int MANY = 100_000;

	/**
	 * The time limit for building one collection of {@link #MANY} elements.
	 *
	 * <p>Measured on this workspace: quadratic needs 8 s (numbers) to 21 s (model elements),
	 * hash-based lookup needs well under 100 ms. Three seconds sits two orders of magnitude
	 * above the one and clearly below the other, so the tests fail on the complexity, not on
	 * a slow machine.
	 */
	private static final int SECONDS = 3;

	@Nested
	@DisplayName("scaling")
	class Scaling {

		/** Built once and outside the time limits — only the collection build is measured. */
		private final List<EObject> people = people(MANY);

		@Test
		@Timeout(value = SECONDS, unit = TimeUnit.SECONDS)
		@DisplayName("a Set of many distinct numbers builds without quadratic cost")
		void setOfManyNumbers() {
			OclSet<Integer> set = new OclSet<>();
			for (int i = 0; i < MANY; i++) {
				set.add(i);
			}
			assertEquals(MANY, set.size());
		}

		@Test
		@Timeout(value = SECONDS, unit = TimeUnit.SECONDS)
		@DisplayName("a Set of many distinct model elements builds without quadratic cost")
		void setOfManyEObjects() {
			OclSet<EObject> set = new OclSet<>(people);

			assertEquals(MANY, set.size());
			assertTrue(set.contains(people.get(MANY - 1)));
		}

		@Test
		@Timeout(value = SECONDS, unit = TimeUnit.SECONDS)
		@DisplayName("an OrderedSet of many distinct model elements builds without quadratic cost")
		void orderedSetOfManyEObjects() {
			OclOrderedSet<EObject> set = new OclOrderedSet<>(people);

			assertEquals(MANY, set.size());
			assertEquals(people.get(0), set.get(0), "insertion order must survive");
			assertTrue(set.contains(people.get(MANY - 1)));
		}

		@Test
		@Timeout(value = SECONDS, unit = TimeUnit.SECONDS)
		@DisplayName("a Set built from many duplicates collapses without quadratic cost")
		void setOfManyDuplicates() {
			// Cheap even when quadratic — the scan only ever sees the 100 distinct elements.
			// It is here so that a fix cannot buy its speed by dropping deduplication.
			List<EObject> repeated = new ArrayList<>(MANY);
			for (int i = 0; i < MANY; i++) {
				repeated.add(people.get(i % 100));
			}

			assertEquals(100, new OclSet<>(repeated).size());
		}
	}

	@Nested
	@DisplayName("equality semantics the lookup has to agree with")
	class Semantics {

		@Test
		@DisplayName("Integer and Real of the same value are one element (§11.5.1)")
		void numericCrossTypeEquality() {
			OclSet<Number> set = new OclSet<>();

			assertTrue(set.add(4));
			assertFalse(set.add(4.0), "4 = 4.0, so the Real must not be added again");
			assertFalse(set.add(4L));

			assertEquals(1, set.size());
			assertTrue(set.contains(4.0));
			assertEquals(Integer.valueOf(4), set.iterator().next(), "the first one wins");
		}

		@Test
		@DisplayName("NaN equals NaN, as Double.compare has it")
		void nanIsItsOwnEqual() {
			// oclEquals compares two Numbers with Double.compare, under which NaN == NaN.
			// A hash lookup has to reproduce that, not Double's == semantics.
			OclSet<Double> set = new OclSet<>();

			assertTrue(set.add(Double.NaN));
			assertFalse(set.add(Double.NaN));

			assertEquals(1, set.size());
			assertTrue(set.contains(Double.NaN));
		}

		@Test
		@DisplayName("negative zero and positive zero stay two elements")
		void signedZeroIsNotCollapsed() {
			// The other direction: Double.compare(-0.0, 0.0) is not 0, so they are two
			// distinct elements — even though -0.0 == 0.0 in plain Java arithmetic.
			OclSet<Double> set = new OclSet<>();

			assertTrue(set.add(-0.0));
			assertTrue(set.add(0.0));

			assertEquals(2, set.size());
		}

		@Test
		@DisplayName("null is an ordinary element")
		void nullIsAnElement() {
			OclSet<Object> set = new OclSet<>();

			assertTrue(set.add(null));
			assertFalse(set.add(null));

			assertEquals(1, set.size());
			assertTrue(set.contains(null));
			assertTrue(set.remove(null));
			assertEquals(0, set.size());
		}

		@Test
		@DisplayName("a number and a non-number never collide")
		void numbersDoNotCollideWithOtherTypes() {
			OclSet<Object> set = new OclSet<>();

			assertTrue(set.add(4));
			assertTrue(set.add("4"));

			assertEquals(2, set.size());
		}

		@Test
		@DisplayName("removing keeps the remaining order")
		void removalKeepsOrder() {
			OclSet<Integer> set = new OclSet<>(Arrays.asList(1, 2, 3, 4));

			assertTrue(set.remove(2.0), "2.0 removes the Integer 2");
			assertFalse(set.remove(99));

			assertIterableEquals(Arrays.asList(1, 3, 4), new ArrayList<>(set));
		}

		@Test
		@DisplayName("an OrderedSet stays deduplicated after a removal")
		void orderedSetRemainsConsistentAfterRemoval() {
			// Removals go through ArrayList directly, so whatever the OrderedSet keeps
			// alongside its elements has to notice.
			OclOrderedSet<Number> set = new OclOrderedSet<>(Arrays.asList(1, 2, 3));

			set.remove(Integer.valueOf(2));

			assertFalse(set.contains(2), "2 is gone");
			assertTrue(set.add(2), "and can be added again");
			assertFalse(set.add(2.0), "but only once");
			assertIterableEquals(Arrays.asList(1, 3, 2), set);
		}

		@Test
		@DisplayName("an OrderedSet stays deduplicated after clear")
		void orderedSetRemainsConsistentAfterClear() {
			OclOrderedSet<Integer> set = new OclOrderedSet<>(Arrays.asList(1, 2, 3));

			set.clear();

			assertTrue(set.add(1));
			assertFalse(set.add(1));
			assertEquals(1, set.size());
		}

		@Test
		@DisplayName("an OrderedSet stays deduplicated after set()")
		void orderedSetRemainsConsistentAfterReplacement() {
			// set() replaces in place and is not a structural modification, so it cannot
			// be caught by watching for those.
			OclOrderedSet<Integer> set = new OclOrderedSet<>(Arrays.asList(1, 2, 3));

			set.set(1, 7);

			assertFalse(set.contains(2), "2 was replaced");
			assertTrue(set.contains(7));
			assertFalse(set.add(7), "and 7 is now the one that must not be added twice");
			assertTrue(set.add(2), "while 2 is free again");
		}
	}

	// --- helpers ---

	private static List<EObject> people(int count) {
		List<EObject> people = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			people.add(createPerson("P" + i, 20 + (i % 50), 40000.0 + i, true));
		}
		return people;
	}
}
