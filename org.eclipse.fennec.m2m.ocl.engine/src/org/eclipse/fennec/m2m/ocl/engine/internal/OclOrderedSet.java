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

/**
 * OCL OrderedSet — an ordered collection with unique elements.
 *
 * <p>Uses OCL equality for uniqueness checks (§11.5.1: Integer is
 * subclass of Real, so {@code 4 = 4.0} are considered equal and
 * deduplicated).
 *
 * <p>Extends {@link ArrayList} for storage and order-sensitive equality.
 *
 * @param <E> element type
 * @since 1.0
 */
public class OclOrderedSet<E> extends ArrayList<E> {

	private static final long serialVersionUID = 1L;

	public OclOrderedSet() {
		super();
	}

	/**
	 * Creates an OrderedSet from the given collection, removing duplicates
	 * using OCL equality while preserving insertion order.
	 */
	public OclOrderedSet(Collection<? extends E> c) {
		super(c.size());
		for (E e : c) {
			add(e); // add uses OCL-equality contains check
		}
	}

	public OclOrderedSet(int initialCapacity) {
		super(initialCapacity);
	}

	@Override
	public boolean contains(Object o) {
		for (int i = 0; i < size(); i++) {
			if (OclEqualityUtil.oclEquals(get(i), o)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean add(E e) {
		if (contains(e)) {
			return false;
		}
		return super.add(e);
	}
}
