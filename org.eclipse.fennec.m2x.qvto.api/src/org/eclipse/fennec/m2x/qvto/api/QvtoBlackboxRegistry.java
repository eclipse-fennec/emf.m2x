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

import java.util.List;
import java.util.Optional;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Registry for blackbox libraries (§8.1.4).
 *
 * <p>A configuration holds one registry. Multiple registries can exist
 * in the system. In OSGi, a registry is typically a DS component using
 * the whiteboard pattern for library registration.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ProviderType
public interface QvtoBlackboxRegistry {

	/**
	 * Returns the library with the given qualified unit name.
	 *
	 * @param qualifiedName the fully qualified unit name
	 * @return the library, or empty if not found
	 */
	Optional<QvtoBlackboxLibrary> getLibrary(String qualifiedName);

	/**
	 * Returns all registered libraries.
	 *
	 * @return unmodifiable list of libraries, never {@code null}
	 */
	List<QvtoBlackboxLibrary> getLibraries();
}
