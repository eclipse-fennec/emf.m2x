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
import java.util.Optional;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Registry for QVT-R blackbox libraries (§7.8, D24).
 *
 * <p>In OSGi, this is typically provided as a whiteboard-collected service.
 * Programmatically, use {@link BasicQvtdBlackboxRegistry}.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ProviderType
public interface QvtdBlackboxRegistry {

	/**
	 * Looks up a blackbox library by its qualified name.
	 *
	 * @param qualifiedName the fully qualified library name
	 * @return the library, or empty if not found
	 */
	Optional<QvtdBlackboxLibrary> getLibrary(String qualifiedName);

	/**
	 * Returns all registered libraries.
	 *
	 * @return unmodifiable list of libraries, never {@code null}
	 */
	List<QvtdBlackboxLibrary> getLibraries();
}
