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
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe implementation of {@link QvtoBlackboxRegistry} for standalone use.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class BasicQvtoBlackboxRegistry implements QvtoBlackboxRegistry {

	private final ConcurrentHashMap<String, QvtoBlackboxLibrary> libraries = new ConcurrentHashMap<>();

	/**
	 * Registers a blackbox library.
	 *
	 * @param library the library to register
	 */
	public void register(QvtoBlackboxLibrary library) {
		Objects.requireNonNull(library, "library must not be null");
		libraries.put(library.getUnitQualifiedName(), library);
	}

	/**
	 * Unregisters a blackbox library.
	 *
	 * @param library the library to unregister
	 */
	public void unregister(QvtoBlackboxLibrary library) {
		Objects.requireNonNull(library, "library must not be null");
		libraries.remove(library.getUnitQualifiedName());
	}

	@Override
	public Optional<QvtoBlackboxLibrary> getLibrary(String qualifiedName) {
		return Optional.ofNullable(libraries.get(qualifiedName));
	}

	@Override
	public List<QvtoBlackboxLibrary> getLibraries() {
		return List.copyOf(libraries.values());
	}
}
