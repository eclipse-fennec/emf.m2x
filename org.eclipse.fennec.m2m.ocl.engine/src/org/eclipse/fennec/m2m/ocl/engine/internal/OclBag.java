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
 * Marker type for OCL Bag collections.
 *
 * <p>A Bag is an unordered collection that allows duplicates.
 * This class extends {@link ArrayList} for storage but signals
 * to the equality logic that order-insensitive, frequency-based
 * comparison should be used.
 *
 * @param <E> element type
 * @since 1.0
 */
class OclBag<E> extends ArrayList<E> {

	private static final long serialVersionUID = 1L;

	OclBag() {
		super();
	}

	OclBag(Collection<? extends E> c) {
		super(c);
	}

	OclBag(int initialCapacity) {
		super(initialCapacity);
	}
}
