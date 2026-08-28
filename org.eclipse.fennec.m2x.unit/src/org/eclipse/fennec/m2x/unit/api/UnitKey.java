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

import java.util.Objects;
import java.util.Optional;

/**
 * Identifies one unit in a {@link UnitStore}.
 *
 * <p>A store holds sources and compiled units side by side, so the {@link UnitKind}
 * belongs in the key rather than in the value (§5.5 of the compiled-unit concept).
 * The {@link #language()} tag separates the three languages, and {@link #typeId()}
 * folds both into the single string a backing store keyed by type id expects —
 * {@code qvto-source}, {@code qvto-compiled}, and so on.
 *
 * <p>The fingerprint is optional and expresses the difference between the two ways
 * of asking for a unit: without one the store answers with what it currently holds
 * under that name, with one it answers with exactly that version or not at all —
 * the {@code pin} dependency mode of §3.1.
 *
 * @param language the language tag, e.g. {@code qvto}, {@code qvtr}, {@code m2t}
 * @param qualifiedName the dot-separated qualified unit name
 * @param kind whether source or compiled unit is meant
 * @param fingerprint the exact version asked for, or empty for the current one
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record UnitKey(String language, String qualifiedName, UnitKind kind,
		Optional<String> fingerprint) {

	public UnitKey {
		Objects.requireNonNull(language, "language must not be null");
		Objects.requireNonNull(qualifiedName, "qualifiedName must not be null");
		Objects.requireNonNull(kind, "kind must not be null");
		Objects.requireNonNull(fingerprint, "fingerprint must not be null");
	}

	/**
	 * Creates a key for whichever version of the unit the store currently holds.
	 *
	 * @param language the language tag
	 * @param qualifiedName the qualified unit name
	 * @param kind source or compiled
	 * @return the key, never {@code null}
	 */
	public static UnitKey of(String language, String qualifiedName, UnitKind kind) {
		return new UnitKey(language, qualifiedName, kind, Optional.empty());
	}

	/**
	 * Creates a key for exactly one version of the unit.
	 *
	 * @param language the language tag
	 * @param qualifiedName the qualified unit name
	 * @param kind source or compiled
	 * @param fingerprint the fingerprint the stored unit must carry, in the
	 *            {@code <scheme>:<digest>} form of {@link UnitFingerprintService}
	 * @return the key, never {@code null}
	 */
	public static UnitKey pinned(String language, String qualifiedName, UnitKind kind,
			String fingerprint) {
		Objects.requireNonNull(fingerprint, "fingerprint must not be null");
		return new UnitKey(language, qualifiedName, kind, Optional.of(fingerprint));
	}

	/**
	 * Returns the type id a backing store keyed by type can use, {@code <language>-<kind>}.
	 *
	 * @return the type id, never {@code null}
	 */
	public String typeId() {
		return language + "-" + kind.tag();
	}
}
