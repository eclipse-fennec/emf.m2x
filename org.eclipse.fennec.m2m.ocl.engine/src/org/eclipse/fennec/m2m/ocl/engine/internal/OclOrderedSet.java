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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Marker type for OCL OrderedSet collections.
 *
 * <p>An OrderedSet is an ordered collection with unique elements.
 * This class extends {@link ArrayList} for storage and order-sensitive
 * equality, while ensuring uniqueness by filtering duplicates on construction.
 *
 * @param <E> element type
 * @since 1.0
 */
class OclOrderedSet<E> extends ArrayList<E> {

	private static final long serialVersionUID = 1L;

	OclOrderedSet() {
		super();
	}

	/**
	 * Creates an OrderedSet from the given collection, removing duplicates
	 * while preserving insertion order.
	 */
	OclOrderedSet(Collection<? extends E> c) {
		super(new LinkedHashSet<>(c));
	}

	OclOrderedSet(int initialCapacity) {
		super(initialCapacity);
	}

	@Override
	public boolean add(E e) {
		if (contains(e)) {
			return false;
		}
		return super.add(e);
	}
}
