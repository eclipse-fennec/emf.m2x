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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;

/**
 * A backend that keeps everything in memory — the one every test and every quick start can
 * use, and the reference for what a backend has to do.
 *
 * <p>Thread-safe by a single lock; a store is written rarely and read at prepare time, not in
 * the hot path.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class InMemoryUnitStoreBackend implements UnitStoreBackend {

	private final Map<UnitKey, byte[]> content = new LinkedHashMap<>();
	/** Versions per unit, newest last. */
	private final Map<UnitKey, List<UnitKey>> versions = new LinkedHashMap<>();

	@Override
	public synchronized void put(UnitKey key, byte[] bytes) {
		requirePinned(key);
		Objects.requireNonNull(bytes, "content must not be null");
		content.put(key, bytes.clone());
		List<UnitKey> known = versions.computeIfAbsent(nameKey(key), k -> new ArrayList<>());
		known.remove(key);
		known.add(key);
	}

	@Override
	public synchronized Optional<byte[]> get(UnitKey key) {
		requirePinned(key);
		byte[] bytes = content.get(key);
		return bytes == null ? Optional.empty() : Optional.of(bytes.clone());
	}

	@Override
	public synchronized List<UnitKey> list(String language, String qualifiedName, UnitKind kind) {
		List<UnitKey> known = versions.get(UnitKey.of(language, qualifiedName, kind));
		if (known == null) {
			return List.of();
		}
		List<UnitKey> newestFirst = new ArrayList<>(known);
		Collections.reverse(newestFirst);
		return List.copyOf(newestFirst);
	}

	@Override
	public synchronized boolean remove(UnitKey key) {
		requirePinned(key);
		boolean removed = content.remove(key) != null;
		List<UnitKey> known = versions.get(nameKey(key));
		if (known != null) {
			known.remove(key);
			if (known.isEmpty()) {
				versions.remove(nameKey(key));
			}
		}
		return removed;
	}

	private static UnitKey nameKey(UnitKey key) {
		return UnitKey.of(key.language(), key.qualifiedName(), key.kind());
	}

	private static void requirePinned(UnitKey key) {
		Objects.requireNonNull(key, "key must not be null");
		if (key.fingerprint().isEmpty()) {
			throw new IllegalArgumentException("a backend key carries a fingerprint: " + key);
		}
	}
}
