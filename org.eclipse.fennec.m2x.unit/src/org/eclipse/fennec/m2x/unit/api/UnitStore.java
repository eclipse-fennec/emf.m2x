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

import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.osgi.annotation.versioning.ProviderType;

/**
 * Durable storage for units — sources, compiled units, or both: key ↔ document, nothing else
 * (#210, #211).
 *
 * <p>A store is dumb by design. How a document is carried underneath — bytes in a backend, live
 * objects in a registry, a model repository — is the medium's business; what a document means in
 * a runtime context is not decided here at all. A compiled unit comes back with its references
 * unresolved: proxies are the transport state, and the
 * {@link org.eclipse.fennec.m2x.unit.materialize.UnitMaterializer} is where a consumer binds,
 * checks and validates the document in its own context. Nothing a store hands out is ready to
 * execute.
 *
 * <p>Because a store may hold the source as well as the compiled unit, a fingerprint mismatch
 * does not have to be the end of the road: the source is findable, so recompiling is an option
 * the caller can choose.
 *
 * <p><b>Isolation.</b> A store hands out documents that are safe to keep: a later write cannot
 * change what an earlier caller is holding, and a caller's mutation cannot change the store.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ProviderType
public interface UnitStore {

	/**
	 * Puts a compiled unit document into the store and returns the key it can be read back with.
	 *
	 * <p>Language and qualified name are the manifest's; a document whose manifest carries no
	 * unit fingerprint is stamped on the way in. The caller's document stays as it was — what is
	 * stored is a copy.
	 *
	 * @param document the compiled unit document
	 * @return the key of the stored unit, carrying its fingerprint, never {@code null}
	 * @throws UnitStoreException if the document could not be stored
	 */
	UnitKey put(CompiledUnit document) throws UnitStoreException;

	/**
	 * Puts a source into the store and returns the key it can be read back with.
	 *
	 * @param language the language tag, e.g. {@code qvto} — a source does not name its own
	 * @param source the source unit
	 * @return the key of the stored source, carrying its source fingerprint, never {@code null}
	 * @throws UnitStoreException if the source could not be stored
	 */
	UnitKey put(String language, Unit.Source source) throws UnitStoreException;

	/**
	 * Returns the unit the key addresses.
	 *
	 * <p>An empty result means the store does not have it. Anything else that went wrong — an
	 * unreachable medium, unreadable content, a pinned fingerprint that is gone — is a
	 * {@link UnitStoreException}, never an empty result.
	 *
	 * <p>A compiled unit comes back as a document whose references are not resolved — materialize
	 * it in a context before use.
	 *
	 * @param key what to read
	 * @return the unit, or empty if the store does not have it
	 * @throws UnitStoreException if the store could not answer the question
	 */
	Optional<Unit> get(UnitKey key) throws UnitStoreException;

	/**
	 * Returns whether the store holds the unit the key addresses.
	 *
	 * @param key what to look for
	 * @return {@code true} if it is there
	 * @throws UnitStoreException if the store could not answer the question
	 */
	boolean contains(UnitKey key) throws UnitStoreException;

	/**
	 * Lists every version of one unit the store holds, newest first where the store can tell.
	 *
	 * <p>This is what makes a fingerprint mismatch diagnosable: it answers which versions are
	 * actually there, so the message can name them instead of stating that something is outdated.
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
