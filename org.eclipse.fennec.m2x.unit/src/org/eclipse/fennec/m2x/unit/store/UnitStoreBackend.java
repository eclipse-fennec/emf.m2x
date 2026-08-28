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
package org.eclipse.fennec.m2x.unit.store;

import java.util.List;
import java.util.Optional;

import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;
import org.eclipse.fennec.m2x.unit.api.UnitStoreException;
import org.osgi.annotation.versioning.ConsumerType;

/**
 * What carries a {@link DefaultUnitStore}: content by key, and the keys by name.
 *
 * <p>A backend stores bytes and knows nothing about units — the store serializes and reads
 * back. Every key handed to a backend carries a fingerprint, so a key is content-addressed and
 * one unit exists in as many versions as it has fingerprints; the name index is what a
 * content-addressed store alone cannot answer, and what makes a fingerprint mismatch
 * diagnosable ({@link org.eclipse.fennec.m2x.unit.api.UnitStore#versions}). A file system, a
 * bundle, a model repository — each is a backend; emf.osgi's {@code ArtifactStore} can be one
 * with an index beside it. This bundle ships the in-memory one.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ConsumerType
public interface UnitStoreBackend {

	/**
	 * Stores content under a key. Storing the same key again replaces the content — same
	 * fingerprint, same content by definition.
	 *
	 * @param key the key, with fingerprint
	 * @param content the serialized unit
	 * @throws UnitStoreException if the content could not be stored
	 */
	void put(UnitKey key, byte[] content) throws UnitStoreException;

	/**
	 * Returns the content stored under a key.
	 *
	 * @param key the key, with fingerprint
	 * @return the content, or empty if nothing is stored under the key
	 * @throws UnitStoreException if the backend could not answer
	 */
	Optional<byte[]> get(UnitKey key) throws UnitStoreException;

	/**
	 * Lists the keys of every version of one unit, newest first where the backend can tell.
	 *
	 * @param language the language tag
	 * @param qualifiedName the qualified unit name
	 * @param kind source or compiled
	 * @return the keys, empty if there are none
	 * @throws UnitStoreException if the backend could not answer
	 */
	List<UnitKey> list(String language, String qualifiedName, UnitKind kind) throws UnitStoreException;

	/**
	 * Removes the content stored under a key.
	 *
	 * @param key the key, with fingerprint
	 * @return {@code true} if something was removed
	 * @throws UnitStoreException if the backend could not carry out the removal
	 */
	boolean remove(UnitKey key) throws UnitStoreException;
}
