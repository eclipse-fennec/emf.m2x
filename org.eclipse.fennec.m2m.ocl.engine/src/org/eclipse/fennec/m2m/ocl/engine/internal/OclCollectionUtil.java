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
package org.eclipse.fennec.m2m.ocl.engine.internal;

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
	 * Cross-type numeric comparison for OCL: Long vs Double etc.
	 *
	 * <p>Numbers are compared by their double value. Other comparable values
	 * are compared naturally. Non-comparable values compare as equal (0).
	 */
	@SuppressWarnings("unchecked")
	static int compareOcl(Object a, Object b) {
		if (a instanceof Number na && b instanceof Number nb) {
			return Double.compare(na.doubleValue(), nb.doubleValue());
		}
		if (a instanceof Comparable<?> ca) {
			try {
				return ((Comparable<Object>) ca).compareTo(b);
			} catch (ClassCastException e) {
				return 0;
			}
		}
		return 0;
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
