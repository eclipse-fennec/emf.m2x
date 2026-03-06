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

import org.osgi.annotation.versioning.ConsumerType;

/**
 * Hybrid bridge interface for QVT-O ↔ QVT-R integration (D39).
 *
 * <p>This interface breaks the dependency cycle between QVT-O and QVT-R engines:
 * <ul>
 *   <li>{@code qvto.engine} → {@code qvtd.api} (implements this provider)
 *   <li>{@code qvtd.engine} → {@code qvtd.api} (consumes this provider)
 * </ul>
 *
 * <p>A QVT-O engine can provide an implementation that executes QVT-R
 * {@code RelationImplementation} bodies using QVT-O mappings.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ConsumerType
public interface RelationImplementationProvider {

	/**
	 * Returns whether this provider can execute the given relation.
	 *
	 * @param relationQualifiedName the fully qualified relation name
	 * @return {@code true} if this provider can handle the relation
	 */
	boolean canProvide(String relationQualifiedName);

	/**
	 * Executes a relation implementation.
	 *
	 * @param relationQualifiedName the fully qualified relation name
	 * @param context the execution context with model extents
	 * @return the execution result
	 */
	QvtdExecutionResult executeRelation(String relationQualifiedName, QvtdExecutionContext context);
}
