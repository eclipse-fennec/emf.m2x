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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.UnaryOperator;

/**
 * OCL OrderedSet — an ordered collection with unique elements.
 *
 * <p>Uses OCL equality for uniqueness checks (§11.5.1: Integer is
 * subclass of Real, so {@code 4 = 4.0} are considered equal and
 * deduplicated).
 *
 * <p>Extends {@link ArrayList} for storage and order-sensitive equality.
 *
 * <p>Uniqueness is decided against a set of
 * {@linkplain OclEqualityUtil#lookupKey(Object) lookup keys} rather than by scanning the
 * elements, which is what keeps building an OrderedSet linear instead of quadratic. Because
 * this is a {@link java.util.List}, callers can also change it through inherited operations
 * that know nothing about that set, so it is rebuilt whenever the list has moved on: for
 * structural changes {@code modCount} says so, and the two operations that replace elements
 * without being structural drop it explicitly.
 *
 * @param <E> element type
 * @since 1.0
 */
public class OclOrderedSet<E> extends ArrayList<E> {

	private static final long serialVersionUID = 1L;

	private transient Set<Object> keys;
	private transient int keysModCount;

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
		return keys().contains(OclEqualityUtil.lookupKey(o));
	}

	@Override
	public boolean add(E e) {
		Object key = OclEqualityUtil.lookupKey(e);
		Set<Object> known = keys();
		if (known.contains(key)) {
			return false;
		}
		if (!super.add(e)) {
			return false;
		}
		known.add(key);
		keysModCount = modCount;
		return true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>Adds the elements that are not in this set yet, in their order, and answers whether
	 * anything was added. {@code ArrayList.addAll} copies straight into the backing array
	 * without asking {@link #add(Object)}, which let a duplicate into an OrderedSet through
	 * every caller that used it — {@code OrderedSet->union(...)} among them (#187).
	 */
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean changed = false;
		for (E element : c) {
			changed |= add(element);
		}
		return changed;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>Inserts the elements that are not in this set yet, in their order, at the index.
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		int at = index;
		boolean changed = false;
		for (E element : c) {
			int before = size();
			add(at, element);
			if (size() > before) {
				at++;
				changed = true;
			}
		}
		return changed;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>An element this set already holds is not inserted a second time: a set keeps the
	 * occurrence it has.
	 */
	@Override
	public void add(int index, E element) {
		Object key = OclEqualityUtil.lookupKey(element);
		Set<Object> known = keys();
		if (known.contains(key)) {
			return;
		}
		super.add(index, element);
		known.add(key);
		keysModCount = modCount;
	}

	@Override
	public E set(int index, E element) {
		keys = null; // replacing an element is not a structural change, so modCount stays put
		return super.set(index, element);
	}

	@Override
	public void replaceAll(UnaryOperator<E> operator) {
		keys = null;
		super.replaceAll(operator);
	}

	@Override
	public Object clone() {
		OclOrderedSet<?> copy = (OclOrderedSet<?>) super.clone();
		copy.keys = null; // the shallow copy would otherwise share this instance's key set
		return copy;
	}

	private Set<Object> keys() {
		if (keys == null || keysModCount != modCount) {
			Set<Object> rebuilt = new HashSet<>(Math.max(16, size() * 2));
			for (int i = 0; i < size(); i++) {
				rebuilt.add(OclEqualityUtil.lookupKey(get(i)));
			}
			keys = rebuilt;
			keysModCount = modCount;
		}
		return keys;
	}
}
