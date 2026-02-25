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
package org.eclipse.fennec.m2m.qvto.api;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * Mutable model extent for QVT-O transformations.
 *
 * <p>Each extent corresponds to a model parameter ({@code in}, {@code inout},
 * or {@code out}) of the transformation. The extent ordering in
 * {@link QvtoExecutionContext} must match the order of
 * {@code modelParameter} declarations in the {@code OperationalTransformation}.
 *
 * <p>Unlike OCL's read-only {@code OclModelExtent}, QVT-O extents are mutable
 * because output models are populated during transformation execution.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public interface QvtoModelExtent {

	/**
	 * Returns the current contents of this extent.
	 *
	 * @return the list of root-level objects, never {@code null}
	 */
	List<EObject> getContents();

	/**
	 * Replaces the contents of this extent.
	 *
	 * @param contents the new contents
	 */
	void setContents(List<? extends EObject> contents);

	/**
	 * Adds an object to this extent.
	 *
	 * @param object the object to add
	 */
	void add(EObject object);

	/**
	 * Returns whether this extent is read-only.
	 *
	 * <p>§8.1.3.2: {@code in} model parameters are immutable. Mutation
	 * operations ({@link #add}, {@link #setContents}) on a read-only extent
	 * must be rejected at runtime.
	 *
	 * @return {@code true} if this extent is read-only
	 */
	default boolean isReadOnly() {
		return false;
	}
}
