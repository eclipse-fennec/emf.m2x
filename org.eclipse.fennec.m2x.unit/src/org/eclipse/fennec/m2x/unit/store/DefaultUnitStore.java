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
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.SourceUnit;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitFingerprintService;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.api.UnitStoreException;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;

/**
 * The unit store over the byte medium: key ↔ document, carried by a {@link UnitStoreBackend}.
 *
 * <p>This store is dumb by design (#210, #211). It serializes on the way in and parses on the
 * way out ({@link UnitXmi}), and that is all: a compiled unit comes back as an independent
 * document in a resource set of its own, its references <em>unresolved</em> — proxies are the
 * transport state. Binding the document in a consumer's context, checking the metamodels and
 * running the validation funnel is the
 * {@link org.eclipse.fennec.m2x.unit.materialize.UnitMaterializer}'s job, not this class's. No
 * registry is consulted here and none is taken: which metamodel instance a reference resolves to
 * is a property of the consumer's context.
 *
 * <p><b>What a compiled unit needs to be stored.</b> Only the document form: a bare AST has no
 * manifest, no satellite container and no fingerprint to be stored by — {@code compile()}
 * first. The document is copied on the way in, so a unit that already sits in a resource stays
 * there, and a document that reaches the store unsealed is stamped on the copy
 * ({@link UnitDocuments}, #183).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class DefaultUnitStore implements UnitStore {

	private final UnitStoreBackend backend;
	private final UnitFingerprintService fingerprints;

	/**
	 * Creates a store over a backend.
	 *
	 * @param backend what carries the content
	 */
	public DefaultUnitStore(UnitStoreBackend backend) {
		this(backend, DefaultUnitFingerprintService.INSTANCE);
	}

	/**
	 * Creates a store over a backend, with the service that stamps an unsealed document.
	 *
	 * @param backend what carries the content
	 * @param fingerprints where an unstamped document's fingerprint comes from
	 */
	public DefaultUnitStore(UnitStoreBackend backend, UnitFingerprintService fingerprints) {
		this.backend = Objects.requireNonNull(backend, "backend must not be null");
		this.fingerprints = Objects.requireNonNull(fingerprints, "fingerprints must not be null");
	}

	@Override
	public UnitKey put(CompiledUnit document) throws UnitStoreException {
		UnitDocuments.Sealed sealed = UnitDocuments.seal(document, fingerprints);
		backend.put(sealed.key(), UnitXmi.write(sealed.document(), sealed.key()));
		return sealed.key();
	}

	@Override
	public UnitKey put(String language, Unit.Source source) throws UnitStoreException {
		UnitDocuments.Sealed sealed = UnitDocuments.sourceForm(language, source, fingerprints);
		backend.put(sealed.key(), UnitXmi.write(sealed.document(), sealed.key()));
		return sealed.key();
	}

	@Override
	public Optional<Unit> get(UnitKey key) throws UnitStoreException {
		Objects.requireNonNull(key, "key must not be null");
		UnitKey pinned;
		if (key.fingerprint().isPresent()) {
			pinned = key;
			if (backend.get(pinned).isEmpty() && !backend.list(key.language(), key.qualifiedName(), key.kind()).isEmpty()) {
				// The name is there, this version is not: say so, never "not found"
				throw new UnitStoreException("the store holds '" + key.qualifiedName() + "' (" + key.kind().tag()
						+ ") but not with fingerprint " + key.fingerprint().get() + "; it has: "
						+ backend.list(key.language(), key.qualifiedName(), key.kind()).stream()
								.map(k -> k.fingerprint().orElse("?")).toList());
			}
		} else {
			List<UnitKey> versions = backend.list(key.language(), key.qualifiedName(), key.kind());
			if (versions.isEmpty()) {
				return Optional.empty();
			}
			pinned = versions.get(0);
		}
		Optional<byte[]> bytes = backend.get(pinned);
		if (bytes.isEmpty()) {
			return Optional.empty();
		}
		EObject root = UnitXmi.read(bytes.get(), pinned);
		return Optional.of(switch (root) {
			case CompiledUnit document -> new PackagedUnit(document);
			case SourceUnit source -> new StoredSource(source.getQualifiedName(),
					URI.createURI(source.getUri() == null ? "unit:/" + source.getQualifiedName() : source.getUri()),
					source.getSource());
			default -> throw new UnitStoreException("the content under " + pinned + " is not a unit but a "
					+ root.eClass().getName());
		});
	}

	@Override
	public boolean contains(UnitKey key) throws UnitStoreException {
		Objects.requireNonNull(key, "key must not be null");
		if (key.fingerprint().isPresent()) {
			return backend.get(key).isPresent();
		}
		return !backend.list(key.language(), key.qualifiedName(), key.kind()).isEmpty();
	}

	@Override
	public List<UnitKey> versions(String language, String qualifiedName, UnitKind kind) throws UnitStoreException {
		return backend.list(language, qualifiedName, kind);
	}

	@Override
	public boolean remove(UnitKey key) throws UnitStoreException {
		Objects.requireNonNull(key, "key must not be null");
		if (key.fingerprint().isPresent()) {
			return backend.remove(key);
		}
		boolean removed = false;
		for (UnitKey version : backend.list(key.language(), key.qualifiedName(), key.kind())) {
			removed |= backend.remove(version);
		}
		return removed;
	}
}
