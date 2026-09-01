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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.fennec.m2x.model.compiled.CompiledFactory;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.SourceUnit;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitFingerprintService;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;
import org.eclipse.fennec.m2x.unit.api.UnitResourceSet;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.api.UnitStoreException;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.eclipse.fennec.m2x.unit.satellite.SatelliteCollector;

/**
 * The unit store over the byte medium: key ↔ document, carried by a {@link UnitStoreBackend}.
 *
 * <p>This store is dumb by design (#210, #211). It serializes on the way in and parses on the
 * way out, and that is all: a compiled unit comes back as an independent document in a resource
 * set of its own, its references <em>unresolved</em> — proxies are the transport state. Binding
 * the document in a consumer's context, checking the metamodels and running the validation
 * funnel is the {@link org.eclipse.fennec.m2x.unit.materialize.UnitMaterializer}'s job, not
 * this class's. No registry is consulted here and none is taken: which metamodel instance a
 * reference resolves to is a property of the consumer's context.
 *
 * <p><b>What a compiled unit needs to be stored.</b> Only the document form: a bare AST has no
 * manifest, no satellite container and no fingerprint to be stored by — {@code compile()}
 * first. The document is copied on the way in, so a unit that already sits in a resource stays
 * there, and a document that reaches the store unsealed is stamped on the copy (#183).
 *
 * <p><b>Metamodels.</b> A document refers to its metamodels by URI. XMI can only write such a
 * reference for a package that lives in a resource, and a package built in memory does not; the
 * store lends it one for the length of the save — the resource EMF gives a generated package in
 * {@code createResource}: one named by its nsURI.
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
		Objects.requireNonNull(document, "document must not be null");
		if (document.getManifest() == null) {
			throw new UnitStoreException("the document carries no manifest; a store takes the compiled"
					+ " document — compile() first");
		}
		String language = document.getManifest().getLanguage();
		String qualifiedName = document.getManifest().getQualifiedName();
		if (language == null || language.isBlank()) {
			throw new UnitStoreException("the unit '" + qualifiedName + "' declares no language");
		}
		if (qualifiedName == null || qualifiedName.isBlank()) {
			throw new UnitStoreException("the document declares no qualified name");
		}
		// The copy is what is stored, so the copy is what is stamped: a document that reaches the
		// store unsealed used to be written without a fingerprint and rejected by its own load()
		// as "carries no unit fingerprint" (#183). The caller's document stays as it was.
		CompiledUnit stored = EcoreUtil.copy(document);
		String fingerprint = stored.getManifest().getUnitFingerprint();
		if (fingerprint == null || fingerprint.isBlank()) {
			fingerprint = fingerprints.fingerprint(stored);
			stored.getManifest().setUnitFingerprint(fingerprint);
		}
		UnitKey key = UnitKey.pinned(language, qualifiedName, UnitKind.COMPILED, fingerprint);
		backend.put(key, serialize(stored, key));
		return key;
	}

	@Override
	public UnitKey put(String language, Unit.Source source) throws UnitStoreException {
		Objects.requireNonNull(language, "language must not be null");
		Objects.requireNonNull(source, "source must not be null");
		String fingerprint = fingerprints.fingerprint(source);
		SourceUnit document = CompiledFactory.eINSTANCE.createSourceUnit();
		document.setLanguage(language);
		document.setQualifiedName(source.qualifiedName());
		document.setUri(source.uri().toString());
		document.setSource(source.source());
		document.setFingerprint(fingerprint);
		UnitKey key = UnitKey.pinned(language, source.qualifiedName(), UnitKind.SOURCE, fingerprint);
		backend.put(key, serialize(document, key));
		return key;
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
		EObject root = deserialize(bytes.get(), pinned);
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

	// --- serialization ---

	/**
	 * What a unit is parsed with: no doctype, no external entities.
	 *
	 * <p>A backend is a plugin — a file, a database, something remote — and the bytes it returns
	 * are the least trusted input this bundle has. The validator and the fingerprint inspect the
	 * <em>result</em> of parsing, so a {@code <!DOCTYPE … SYSTEM "file:///etc/passwd">} would
	 * already have been resolved by the time they run (#183). The three features below are the
	 * standard XXE defence; refusing a doctype outright makes the other two redundant and is kept
	 * for readers who look for them.
	 */
	private static final Map<Object, Object> SAFE_PARSER_OPTIONS = Map.of(
			XMLResource.OPTION_PARSER_FEATURES, Map.of(
					"http://apache.org/xml/features/disallow-doctype-decl", Boolean.TRUE,
					"http://xml.org/sax/features/external-general-entities", Boolean.FALSE,
					"http://xml.org/sax/features/external-parameter-entities", Boolean.FALSE));

	/**
	 * Guards the metamodels a save borrows.
	 *
	 * <p>Writing a unit that refers to a metamodel with no resource of its own has to put that
	 * metamodel into a resource for the length of the save — XMI writes an href by asking the
	 * target for its resource, and a target without one cannot be referenced. The metamodel
	 * belongs to the caller, though, and there is one of it: two saves at once each moved it
	 * into their own resource, and the removal from the other's contents raced
	 * ({@code ArrayIndexOutOfBoundsException} out of {@code BasicEList.remove}, #174).
	 *
	 * <p>Saves that borrow are serialized against each other, and each puts back what it
	 * borrowed. A store is shared by construction — every engine reaches for it on every
	 * import — so this is not an exotic case.
	 */
	private final Object borrowedMetamodels = new Object();

	private byte[] serialize(EObject document, UnitKey key) throws UnitStoreException {
		synchronized (borrowedMetamodels) {
			ResourceSet resourceSet = new UnitResourceSet();
			List<EPackage> borrowed = giveResourcelessMetamodelsAResource(document, resourceSet);
			Resource resource = resourceSet.createResource(uriOf(key));
			resource.getContents().add(document);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				resource.save(out, Map.of(XMLResource.OPTION_ENCODING, "UTF-8"));
			} catch (IOException | RuntimeException e) {
				throw new UnitStoreException("cannot serialize '" + key.qualifiedName() + "': " + e.getMessage(), e);
			} finally {
				// Put the caller's metamodels back the way they were: a package left in this
				// save's resource keeps an eResource() pointing at a set that is gone, and the
				// next save skips it for exactly that reason and writes an href to nowhere
				for (EPackage metamodel : borrowed) {
					metamodel.eResource().getContents().remove(metamodel);
				}
				resource.getContents().remove(document);
			}
			return out.toByteArray();
		}
	}

	/**
	 * Parses the bytes into a resource set of the document's own — no registry beyond the global
	 * one answers there, nothing is resolved, nothing is validated. The proxies the document
	 * comes back with are the transport state the materializer ends.
	 */
	private EObject deserialize(byte[] bytes, UnitKey key) throws UnitStoreException {
		UnitResourceSet resourceSet = new UnitResourceSet();
		Resource resource = resourceSet.createResource(uriOf(key));
		try {
			resource.load(new ByteArrayInputStream(bytes), SAFE_PARSER_OPTIONS);
		} catch (IOException | RuntimeException e) {
			throw new UnitStoreException("cannot read '" + key.qualifiedName() + "': " + e.getMessage(), e);
		}
		if (resource.getContents().isEmpty()) {
			throw new UnitStoreException("the content under " + key + " is empty");
		}
		return resource.getContents().get(0);
	}

	/**
	 * XMI writes a reference to another document as {@code <resource URI>#<fragment>}, so a
	 * referenced package has to live in a resource — a generated one does (EMF's
	 * {@code createResource(nsURI)}), a package built in memory does not. The store gives it the
	 * same: a resource named by its nsURI, which is exactly how the document refers to it.
	 */
	private List<EPackage> giveResourcelessMetamodelsAResource(EObject document, ResourceSet resourceSet) {
		List<EPackage> borrowed = new ArrayList<>();
		for (EObject target : EcoreUtil.ExternalCrossReferencer.find(document).keySet()) {
			if (target.eResource() != null || target.eIsProxy() || !SatelliteCollector.isMetamodelElement(target)) {
				continue;
			}
			EPackage root = SatelliteCollector.metamodelOf(target);
			if (root.eResource() != null || root.eContainer() != null) {
				continue;
			}
			// The resource belongs to this save, not to the caller's package: putting the
			// caller's EPackage into a ResourceImpl changed its eResource() for good, a side
			// effect of store() that nobody asked for (#183). A resource in the set being
			// written gives XMI the href it needs and is discarded with the set.
			Resource metamodel = resourceSet.createResource(URI.createURI(root.getNsURI()));
			metamodel.getContents().add(root);
			resourceSet.getPackageRegistry().put(root.getNsURI(), root);
			borrowed.add(root);
		}
		return borrowed;
	}

	private static URI uriOf(UnitKey key) {
		return URI.createURI("unit:/" + key.language() + "/" + key.kind().tag() + "/"
				+ key.qualifiedName() + "/" + key.fingerprint().orElse("current") + ".xmi");
	}
}
