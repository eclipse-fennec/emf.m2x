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

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * OCL Set implementation that uses OCL equality for element comparison.
 *
 * <p>Unlike {@link java.util.LinkedHashSet}, this Set uses OCL numeric
 * cross-type equality (§11.5.1: Integer is subclass of Real, so
 * {@code 4 = 4.0} is {@code true}). This ensures that {@code Set{4, 4.0}}
 * correctly deduplicates to a single element.
 *
 * <p>Internally backed by an {@link ArrayList} with linear-scan containment
 * checks using {@link OclEqualityUtil#oclEquals(Object, Object)}.
 * Performance is O(n) for contains/add, which is acceptable for typical
 * OCL collection sizes.
 *
 * @param <E> element type
 * @since 1.0
 */
public class OclSet<E> extends AbstractSet<E> {

	private final List<E> elements;

	public OclSet() {
		this.elements = new ArrayList<>();
	}

	public OclSet(int initialCapacity) {
		this.elements = new ArrayList<>(initialCapacity);
	}

	/**
	 * Creates an OclSet from the given collection, deduplicating
	 * elements using OCL equality.
	 */
	public OclSet(Collection<? extends E> c) {
		this.elements = new ArrayList<>(c.size());
		addAll(c);
	}

	@Override
	public Iterator<E> iterator() {
		return elements.iterator();
	}

	@Override
	public int size() {
		return elements.size();
	}

	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return indexOf(o) >= 0;
	}

	@Override
	public boolean add(E e) {
		if (contains(e)) {
			return false;
		}
		return elements.add(e);
	}

	@Override
	public boolean remove(Object o) {
		int idx = indexOf(o);
		if (idx >= 0) {
			elements.remove(idx);
			return true;
		}
		return false;
	}

	@Override
	public void clear() {
		elements.clear();
	}

	private int indexOf(Object o) {
		for (int i = 0; i < elements.size(); i++) {
			if (OclEqualityUtil.oclEquals(elements.get(i), o)) {
				return i;
			}
		}
		return -1;
	}
}
