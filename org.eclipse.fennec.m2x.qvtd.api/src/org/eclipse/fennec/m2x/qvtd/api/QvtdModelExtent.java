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
package org.eclipse.fennec.m2x.qvtd.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.osgi.annotation.versioning.ConsumerType;

/**
 * Mutable model extent for QVT-R transformations.
 *
 * <p>Each extent corresponds to a {@code TypedModel} of the transformation.
 * Unlike QVT-O (where read-only is an explicit flag), QVT-R determines
 * mutability via the checkonly/enforce semantics per domain.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ConsumerType
public interface QvtdModelExtent {

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
	 * Creates a model extent initialized with the given root objects.
	 *
	 * @param roots the initial root objects
	 * @return a new model extent
	 */
	static QvtdModelExtent of(EObject... roots) {
		Objects.requireNonNull(roots, "roots must not be null");
		return new BasicQvtdModelExtent(new ArrayList<>(Arrays.asList(roots)));
	}

	/**
	 * Creates a model extent initialized with the given root objects.
	 *
	 * @param roots the initial root objects
	 * @return a new model extent
	 */
	static QvtdModelExtent of(List<? extends EObject> roots) {
		Objects.requireNonNull(roots, "roots must not be null");
		return new BasicQvtdModelExtent(new ArrayList<>(roots));
	}
}
