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

import java.util.List;

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
 * <p>A provider serves the operation a QVT-R {@code implementedby} clause names
 * (QVT v1.3 §7.11.3.6 {@code RelationImplementation.impl : Operation}) — one operation, called
 * with the arguments the clause gives, which are the root objects of the relation's other
 * domains (§10, rule 4.5). A QVT-O engine serves it with the mapping of that name, which
 * "refines conceptually a relation where each relation domain corresponds to a parameter"
 * (§8.2.1.15). The QVT-R engine calls it only when the relation does not hold in the enforced
 * direction, and checks the relation again afterwards (#282).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ConsumerType
public interface RelationImplementationProvider {

	/**
	 * Returns whether this provider serves the given operation.
	 *
	 * @param operationName the name of the operation an {@code implementedby} clause names
	 * @return {@code true} if this provider can invoke it
	 */
	boolean canProvide(String operationName);

	/**
	 * Invokes the operation an {@code implementedby} clause names.
	 *
	 * <p>The operation is responsible for the changes that make the relation hold in the
	 * enforced direction; what it returns is the object of the enforced domain. Objects it
	 * creates go into the extents of the given context — the same extents the QVT-R run uses.
	 *
	 * @param operationName the operation, one this provider {@linkplain #canProvide(String) serves}
	 * @param arguments the evaluated arguments of the {@code implementedby} call, in order
	 * @param context the QVT-R execution context, whose extents the operation works on
	 * @return the operation's result and what it reported, never {@code null}
	 */
	RelationImplementationResult invoke(String operationName, List<Object> arguments,
			QvtdExecutionContext context);
}
