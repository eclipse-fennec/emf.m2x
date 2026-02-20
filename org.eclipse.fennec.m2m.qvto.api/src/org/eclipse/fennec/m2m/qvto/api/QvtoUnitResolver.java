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

import java.util.Optional;

import org.osgi.annotation.versioning.ConsumerType;

/**
 * Resolves QVT-O compilation units by qualified name.
 *
 * <p>In standalone mode, register resolvers via
 * {@link QvtoEngine#registerUnitResolver(QvtoUnitResolver)}. In OSGi,
 * publish implementations as whiteboard services (D27).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ConsumerType
public interface QvtoUnitResolver {

	/**
	 * Resolves a compilation unit by its qualified name.
	 *
	 * @param qualifiedName the dot-separated qualified name
	 * @return the resolved unit, or empty if not found
	 */
	Optional<QvtoUnit> resolveUnit(String qualifiedName);
}
