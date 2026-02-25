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
package org.eclipse.fennec.m2x.qvto.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EObject;

/**
 * In-memory implementation of {@link QvtoModelExtent} backed by an {@code ArrayList}.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class BasicQvtoModelExtent implements QvtoModelExtent {

	private final List<EObject> contents;
	private boolean readOnly;

	/**
	 * Creates an empty model extent.
	 */
	public BasicQvtoModelExtent() {
		this.contents = new ArrayList<>();
	}

	/**
	 * Creates a model extent initialized with the given objects.
	 *
	 * @param initial the initial contents
	 */
	public BasicQvtoModelExtent(List<? extends EObject> initial) {
		Objects.requireNonNull(initial, "initial must not be null");
		this.contents = new ArrayList<>(initial);
	}

	/**
	 * Creates a model extent initialized with the given objects.
	 *
	 * @param initial the initial contents
	 */
	public BasicQvtoModelExtent(EObject... initial) {
		Objects.requireNonNull(initial, "initial must not be null");
		this.contents = new ArrayList<>(Arrays.asList(initial));
	}

	@Override
	public List<EObject> getContents() {
		return contents;
	}

	@Override
	public void setContents(List<? extends EObject> contents) {
		Objects.requireNonNull(contents, "contents must not be null");
		checkWritable();
		this.contents.clear();
		this.contents.addAll(contents);
	}

	@Override
	public void add(EObject object) {
		Objects.requireNonNull(object, "object must not be null");
		checkWritable();
		contents.add(object);
	}

	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * Sets the read-only flag on this extent.
	 *
	 * <p>§8.1.3.2: {@code in} model extents are immutable at runtime.
	 *
	 * @param readOnly {@code true} to mark this extent as read-only
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	private void checkWritable() {
		if (readOnly) {
			throw new UnsupportedOperationException(
					"Cannot modify read-only model extent (in-parameter)");
		}
	}
}
