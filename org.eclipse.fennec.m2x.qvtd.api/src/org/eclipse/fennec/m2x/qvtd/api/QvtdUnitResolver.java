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

import java.util.Optional;

import org.osgi.annotation.versioning.ConsumerType;

/**
 * Resolves QVT-R compilation units by qualified name (D37).
 *
 * <p>Implementations are registered via {@link QvtdConfiguration.Builder#addUnitResolver}
 * or as OSGi whiteboard services. The engine queries resolvers in order until one
 * returns a result.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ConsumerType
public interface QvtdUnitResolver {

	/**
	 * Resolves a QVT-R unit by its qualified name.
	 *
	 * @param qualifiedName the fully qualified unit name
	 * @return the resolved unit, or empty if not found
	 */
	Optional<QvtdUnit> resolveUnit(String qualifiedName);
}
