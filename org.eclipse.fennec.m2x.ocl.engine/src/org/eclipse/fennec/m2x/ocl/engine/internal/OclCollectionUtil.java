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

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Shared utility methods for OCL collection operations.
 *
 * <p>These methods are used by both {@link OclEvaluator} and {@link OclStdlib}
 * to avoid code duplication.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class OclCollectionUtil {

	private OclCollectionUtil() {
	}

	/**
	 * Cross-type comparison for OCL: {@code Long} against {@code Double}, and anything
	 * {@link Comparable} against its own kind.
	 *
	 * <p>Values that cannot be compared are ordered by the name of their type, and only then
	 * by identity. Answering 0 for them, as this did, breaks the contract every sort in the JDK
	 * relies on: with three values where a and b are "equal", b and c are "equal" and a and c
	 * are not, {@code sortedBy} over a mixed collection can fail with "Comparison method
	 * violates its general contract" (#187). It is arbitrary but consistent, which is what an
	 * order over values OCL gives no order to has to be.
	 *
	 * @param a the first value
	 * @param b the second value
	 * @return a negative number, zero, or a positive number as a is less than, equal to, or
	 *         greater than b
	 */
	@SuppressWarnings("unchecked")
	static int compareOcl(Object a, Object b) {
		if (a instanceof Number na && b instanceof Number nb) {
			return Double.compare(na.doubleValue(), nb.doubleValue());
		}
		if (a instanceof Comparable<?> ca && b != null && a.getClass() == b.getClass()) {
			return ((Comparable<Object>) ca).compareTo(b);
		}
		if (a instanceof Comparable<?> ca && b != null) {
			try {
				return ((Comparable<Object>) ca).compareTo(b);
			} catch (ClassCastException incomparable) {
				return fallbackOrder(a, b);
			}
		}
		return fallbackOrder(a, b);
	}

	/**
	 * A total order over values OCL cannot compare: nulls first, then by type name, then by
	 * identity hash — stable within one run, and transitive, which is all a sort needs.
	 */
	private static int fallbackOrder(Object a, Object b) {
		if (a == b) {
			return 0;
		}
		if (a == null) {
			return -1;
		}
		if (b == null) {
			return 1;
		}
		int byType = a.getClass().getName().compareTo(b.getClass().getName());
		return byType != 0 ? byType : Integer.compare(System.identityHashCode(a), System.identityHashCode(b));
	}

	/**
	 * Returns a collection of the same kind as the source, filled with the given elements.
	 *
	 * <p>Preserves OCL collection semantics: OrderedSet, Bag, Set, or Sequence (List).
	 */
	static Collection<Object> preserveCollectionKind(Collection<?> source, List<Object> elements) {
		if (source instanceof OclOrderedSet<?>) return new OclOrderedSet<>(elements);
		if (source instanceof OclBag<?>) return new OclBag<>(elements);
		if (source instanceof Set<?>) return new OclSet<>(elements);
		return elements;
	}
}
