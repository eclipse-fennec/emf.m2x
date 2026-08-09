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

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * OCL Set implementation that uses OCL equality for element comparison.
 *
 * <p>Unlike {@link java.util.LinkedHashSet}, this Set uses OCL numeric
 * cross-type equality (§11.5.1: Integer is subclass of Real, so
 * {@code 4 = 4.0} is {@code true}). This ensures that {@code Set{4, 4.0}}
 * correctly deduplicates to a single element.
 *
 * <p>Backed by a {@link LinkedHashMap} from
 * {@linkplain OclEqualityUtil#lookupKey(Object) lookup key} to element, which keeps
 * containment and insertion at constant cost while preserving encounter order. The
 * straightforward reading — a list with a linear scan per {@code add} — makes building a
 * set quadratic, which a {@code Person.allInstances()} over a real model runs into hard.
 *
 * @param <E> element type
 * @since 1.0
 */
public class OclSet<E> extends AbstractSet<E> {

	private final Map<Object, E> elements;

	public OclSet() {
		this.elements = new LinkedHashMap<>();
	}

	public OclSet(int initialCapacity) {
		this.elements = new LinkedHashMap<>(initialCapacity);
	}

	/**
	 * Creates an OclSet from the given collection, deduplicating
	 * elements using OCL equality.
	 */
	public OclSet(Collection<? extends E> c) {
		this.elements = new LinkedHashMap<>(c.size());
		addAll(c);
	}

	@Override
	public Iterator<E> iterator() {
		return elements.values().iterator();
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
		return elements.containsKey(OclEqualityUtil.lookupKey(o));
	}

	@Override
	public boolean add(E e) {
		Object key = OclEqualityUtil.lookupKey(e);
		// Not putIfAbsent: null is a legitimate element, and it would be indistinguishable
		// from an absent key.
		if (elements.containsKey(key)) {
			return false;
		}
		elements.put(key, e);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		Object key = OclEqualityUtil.lookupKey(o);
		if (!elements.containsKey(key)) {
			return false;
		}
		elements.remove(key);
		return true;
	}

	@Override
	public void clear() {
		elements.clear();
	}
}
