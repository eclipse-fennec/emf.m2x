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
package org.eclipse.fennec.m2x.unit.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistry;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistryEntry;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistryWriter;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.SourceUnit;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitFingerprintService;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.api.UnitStoreException;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.eclipse.fennec.m2x.unit.store.StoredSource;
import org.eclipse.fennec.m2x.unit.store.UnitDocuments;
import org.eclipse.fennec.m2x.unit.store.UnitXmi;

/**
 * The unit store over the object medium: key ↔ document, carried by an emf.osgi
 * {@link EObjectRegistry} as live {@link CompiledUnit} documents (#213).
 *
 * <p>The store contract does not change with the medium. What goes in is sealed and
 * <em>normalized into the transport state</em> — one XMI round trip on the way in, so a document
 * whose references were bound in the producer's context arrives with them unresolved, exactly as
 * the byte medium delivers; which instance a reference binds to stays a property of the
 * consumer's context, never of the producer's. What comes out is an independent copy: the
 * registry's live instance never leaves, a caller's mutation cannot reach it, and the copy passes
 * the {@code UnitMaterializer} funnel like any other document.
 *
 * <p><b>Entries of other sources.</b> A registry fed by a provider — unit XMIs on disk behind a
 * {@code FileEObjectProvider}, a model atlas — holds entries this store never wrote. They are
 * served all the same: an entry whose object is a compiled-unit or source document answers under
 * the key its manifest names, whether or not the {@code unit.*} entry properties are present.
 * Removing is limited to what a writer's source owns — foreign entries are the provider's.
 *
 * <p><b>No {@code emf.fingerprint} on unit entries.</b> That property carries a plain model
 * fingerprint — the metadata-bridge join key — and a unit references several packages; leaving it
 * out beats filling it wrongly (#210).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class RegistryUnitStore implements UnitStore {

	/** Entry property: the language tag, e.g. {@code qvto}. */
	public static final String PROP_LANGUAGE = "unit.language";
	/** Entry property: the kind tag, {@code source} or {@code compiled}. */
	public static final String PROP_KIND = "unit.kind";
	/** Entry property: the qualified unit name. */
	public static final String PROP_QUALIFIED_NAME = "unit.qualifiedName";
	/** Entry property: the unit or source fingerprint the key pins. */
	public static final String PROP_FINGERPRINT = "unit.fingerprint";
	/** Entry property: the unit's nature, {@code transformation} or {@code library} (#224). */
	public static final String PROP_NATURE = "unit.nature";

	/** The default source name this store writes under. */
	public static final String DEFAULT_SOURCE = "m2x-unit-store";

	private final EObjectRegistry registry;
	private final EObjectRegistryWriter writer;
	private final String source;
	private final UnitFingerprintService fingerprints;

	/**
	 * Creates a read-only store over a registry: {@code get}, {@code contains} and
	 * {@code versions} answer, {@code put} and {@code remove} refuse.
	 *
	 * @param registry the registry to read from
	 */
	public RegistryUnitStore(EObjectRegistry registry) {
		this(registry, null, DEFAULT_SOURCE, DefaultUnitFingerprintService.INSTANCE);
	}

	/**
	 * Creates a read-write store over a registry's write face.
	 *
	 * @param writer the write face; the read face is its registry
	 */
	public RegistryUnitStore(EObjectRegistryWriter writer) {
		this(Objects.requireNonNull(writer, "writer must not be null").getRegistry(), writer,
				DEFAULT_SOURCE, DefaultUnitFingerprintService.INSTANCE);
	}

	/**
	 * Creates a store with every collaborator given.
	 *
	 * @param registry the registry to read from
	 * @param writer the write face, or {@code null} for a read-only store
	 * @param source the source name written entries belong to
	 * @param fingerprints where an unstamped document's fingerprint comes from
	 */
	public RegistryUnitStore(EObjectRegistry registry, EObjectRegistryWriter writer, String source,
			UnitFingerprintService fingerprints) {
		this.registry = Objects.requireNonNull(registry, "registry must not be null");
		this.writer = writer;
		this.source = Objects.requireNonNull(source, "source must not be null");
		this.fingerprints = Objects.requireNonNull(fingerprints, "fingerprints must not be null");
	}

	@Override
	public UnitKey put(CompiledUnit document) throws UnitStoreException {
		UnitDocuments.Sealed sealed = UnitDocuments.seal(document, fingerprints);
		// One round trip normalizes into the transport state: references the producer's context
		// had bound come back unresolved, so the registry never carries the producer's bindings
		EObject detached = UnitXmi.read(UnitXmi.write(sealed.document(), sealed.key()), sealed.key());
		Map<String, Object> properties = new HashMap<>(propertiesOf(sealed.key()));
		if (detached instanceof CompiledUnit stored && stored.getManifest() != null
				&& stored.getManifest().getNature() != null) {
			// The whole point of the manifest attribute: a registry consumer — an atlas UI —
			// filters startable transformations from libraries without opening a document (#224)
			properties.put(PROP_NATURE, stored.getManifest().getNature().getLiteral());
		}
		requireWriter().put(source, entryKey(sealed.key()), detached, properties);
		return sealed.key();
	}

	@Override
	public UnitKey put(String language, Unit.Source unitSource) throws UnitStoreException {
		UnitDocuments.Sealed sealed = UnitDocuments.sourceForm(language, unitSource, fingerprints);
		requireWriter().put(source, entryKey(sealed.key()), sealed.document(), propertiesOf(sealed.key()));
		return sealed.key();
	}

	@Override
	public Optional<Unit> get(UnitKey key) throws UnitStoreException {
		Objects.requireNonNull(key, "key must not be null");
		UnitKey pinned;
		if (key.fingerprint().isPresent()) {
			pinned = key;
			if (entryOf(pinned).isEmpty() && !versions(key.language(), key.qualifiedName(), key.kind()).isEmpty()) {
				// The name is there, this version is not: say so, never "not found"
				throw new UnitStoreException("the store holds '" + key.qualifiedName() + "' (" + key.kind().tag()
						+ ") but not with fingerprint " + key.fingerprint().get() + "; it has: "
						+ versions(key.language(), key.qualifiedName(), key.kind()).stream()
								.map(k -> k.fingerprint().orElse("?")).toList());
			}
		} else {
			List<UnitKey> versions = versions(key.language(), key.qualifiedName(), key.kind());
			if (versions.isEmpty()) {
				return Optional.empty();
			}
			pinned = versions.get(0);
		}
		Optional<EObjectRegistryEntry> entry = entryOf(pinned);
		if (entry.isEmpty()) {
			return Optional.empty();
		}
		EObject root = copyOf(entry.get().object());
		return Optional.of(switch (root) {
			case CompiledUnit document -> new PackagedUnit(document);
			case SourceUnit sourceUnit -> new StoredSource(sourceUnit.getQualifiedName(),
					URI.createURI(sourceUnit.getUri() == null ? "unit:/" + sourceUnit.getQualifiedName()
							: sourceUnit.getUri()),
					sourceUnit.getSource());
			default -> throw new UnitStoreException("the content under " + pinned + " is not a unit but a "
					+ root.eClass().getName());
		});
	}

	@Override
	public boolean contains(UnitKey key) throws UnitStoreException {
		Objects.requireNonNull(key, "key must not be null");
		if (key.fingerprint().isPresent()) {
			return entryOf(key).isPresent();
		}
		return !versions(key.language(), key.qualifiedName(), key.kind()).isEmpty();
	}

	@Override
	public List<UnitKey> versions(String language, String qualifiedName, UnitKind kind) throws UnitStoreException {
		List<UnitKey> keys = new ArrayList<>();
		for (EObjectRegistryEntry entry : registry.entries()) {
			UnitKey key = keyOf(entry);
			if (key != null && key.language().equals(language) && key.qualifiedName().equals(qualifiedName)
					&& key.kind() == kind) {
				keys.add(key);
			}
		}
		// entries() is insertion order; newest first is what the contract asks for
		List<UnitKey> newestFirst = new ArrayList<>(keys.size());
		for (int i = keys.size() - 1; i >= 0; i--) {
			newestFirst.add(keys.get(i));
		}
		return newestFirst;
	}

	@Override
	public boolean remove(UnitKey key) throws UnitStoreException {
		Objects.requireNonNull(key, "key must not be null");
		if (key.fingerprint().isPresent()) {
			boolean there = entryOf(key).isPresent();
			requireWriter().remove(source, entryKey(key));
			return there && entryOf(key).isEmpty();
		}
		boolean removed = false;
		for (UnitKey version : versions(key.language(), key.qualifiedName(), key.kind())) {
			removed |= remove(version);
		}
		return removed;
	}

	// --- the medium ---

	private EObjectRegistryWriter requireWriter() throws UnitStoreException {
		if (writer == null) {
			throw new UnitStoreException("this store reads registry '" + registry.getName()
					+ "' and has no write face; writing is the content source's business");
		}
		return writer;
	}

	/**
	 * The independent copy a caller gets: nothing of the registry's live instance leaves, and
	 * nothing is resolved on the way — an unresolved reference stays the transport state the
	 * materializer ends, in the consumer's context, not here.
	 */
	private static EObject copyOf(EObject object) {
		EcoreUtil.Copier copier = new EcoreUtil.Copier(false, true);
		EObject copy = copier.copy(object);
		copier.copyReferences();
		return copy;
	}

	private Optional<EObjectRegistryEntry> entryOf(UnitKey pinned) {
		Optional<EObjectRegistryEntry> direct = registry.getEntry(entryKey(pinned));
		if (direct.isPresent()) {
			return direct;
		}
		// An entry another source filed — a provider loading unit XMIs from disk — answers by
		// what its document says, whatever its entry key looks like
		for (EObjectRegistryEntry entry : registry.entries()) {
			if (pinned.equals(keyOf(entry))) {
				return Optional.of(entry);
			}
		}
		return Optional.empty();
	}

	/**
	 * The unit key an entry answers under: the {@code unit.*} properties where the writer set
	 * them, the document's own manifest where a foreign source did not.
	 */
	private static UnitKey keyOf(EObjectRegistryEntry entry) {
		Map<String, Object> properties = entry.properties();
		Object language = properties.get(PROP_LANGUAGE);
		Object kind = properties.get(PROP_KIND);
		Object name = properties.get(PROP_QUALIFIED_NAME);
		Object fingerprint = properties.get(PROP_FINGERPRINT);
		if (language instanceof String l && kind instanceof String k && name instanceof String n
				&& fingerprint instanceof String f) {
			UnitKind unitKind = UnitKind.SOURCE.tag().equals(k) ? UnitKind.SOURCE : UnitKind.COMPILED;
			return UnitKey.pinned(l, n, unitKind, f);
		}
		if (entry.object() instanceof CompiledUnit document && document.getManifest() != null
				&& document.getManifest().getLanguage() != null
				&& document.getManifest().getQualifiedName() != null
				&& document.getManifest().getUnitFingerprint() != null) {
			return UnitKey.pinned(document.getManifest().getLanguage(),
					document.getManifest().getQualifiedName(), UnitKind.COMPILED,
					document.getManifest().getUnitFingerprint());
		}
		if (entry.object() instanceof SourceUnit sourceUnit && sourceUnit.getLanguage() != null
				&& sourceUnit.getQualifiedName() != null && sourceUnit.getFingerprint() != null) {
			return UnitKey.pinned(sourceUnit.getLanguage(), sourceUnit.getQualifiedName(),
					UnitKind.SOURCE, sourceUnit.getFingerprint());
		}
		return null;
	}

	private static String entryKey(UnitKey key) {
		return key.language() + "/" + key.kind().tag() + "/" + key.qualifiedName() + "/"
				+ key.fingerprint().orElseThrow();
	}

	private static Map<String, Object> propertiesOf(UnitKey key) {
		return Map.of(
				PROP_LANGUAGE, key.language(),
				PROP_KIND, key.kind().tag(),
				PROP_QUALIFIED_NAME, key.qualifiedName(),
				PROP_FINGERPRINT, key.fingerprint().orElseThrow());
	}
}
