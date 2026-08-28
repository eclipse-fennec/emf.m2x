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
package org.eclipse.fennec.m2x.unit.api;

import java.util.List;
import java.util.Optional;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Durable storage for units — sources, compiled units, or both.
 *
 * <p>{@code UnitStore} is the m2x-level abstraction; what carries it underneath
 * is not its concern. An {@code ArtifactStore} from {@code emf.osgi} is one
 * possible backend, a file system or a bundle are others, and a store backed by
 * a model repository is a separate project — none of that is visible here (§5.5
 * and §8 of the compiled-unit concept).
 *
 * <p>Because a store may hold the source as well as the compiled unit, a
 * fingerprint mismatch does not have to be the end of the road: the source is
 * findable, so recompiling is an option the caller can choose.
 *
 * <p><b>Isolation.</b> An implementation returns units that are safe to keep: a
 * store that hands out a live object graph must copy it on the way out, so that
 * a later write cannot change what an earlier caller is holding. Loading happens
 * once per prepare, not once per execution, so a copy per load is affordable.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ProviderType
public interface UnitStore {

	/**
	 * Stores a unit and returns the key it can be read back with.
	 *
	 * @param language the language tag, e.g. {@code qvto}
	 * @param unit the unit to store
	 * @return the key of the stored unit, carrying its fingerprint, never {@code null}
	 * @throws UnitStoreException if the unit could not be stored
	 */
	UnitKey store(String language, Unit unit) throws UnitStoreException;

	/**
	 * Loads the unit the key addresses.
	 *
	 * <p>An empty result means the store does not have it. Anything else that went
	 * wrong — an unreachable store, unreadable content, a pinned fingerprint that is
	 * gone — is a {@link UnitStoreException}, never an empty result.
	 *
	 * @param key what to load
	 * @return the unit, or empty if the store does not have it
	 * @throws UnitStoreException if the store could not answer the question
	 */
	Optional<Unit> load(UnitKey key) throws UnitStoreException;

	/**
	 * Returns whether the store holds the unit the key addresses.
	 *
	 * @param key what to look for
	 * @return {@code true} if it is there
	 * @throws UnitStoreException if the store could not answer the question
	 */
	boolean contains(UnitKey key) throws UnitStoreException;

	/**
	 * Lists every version of one unit the store holds, newest first where the store
	 * can tell.
	 *
	 * <p>This is what makes a fingerprint mismatch diagnosable: it answers which
	 * versions are actually there, so the message can name them instead of stating
	 * that something is outdated.
	 *
	 * @param language the language tag
	 * @param qualifiedName the qualified unit name
	 * @param kind source or compiled
	 * @return the keys, empty if the store holds none
	 * @throws UnitStoreException if the store could not answer the question
	 */
	List<UnitKey> versions(String language, String qualifiedName, UnitKind kind)
			throws UnitStoreException;

	/**
	 * Removes the unit the key addresses.
	 *
	 * @param key what to remove
	 * @return {@code true} if something was removed
	 * @throws UnitStoreException if the store could not carry out the removal
	 */
	boolean remove(UnitKey key) throws UnitStoreException;
}
