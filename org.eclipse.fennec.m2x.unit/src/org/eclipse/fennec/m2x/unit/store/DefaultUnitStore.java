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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.fennec.m2x.model.compiled.CompiledFactory;
import org.eclipse.fennec.m2x.model.compiled.CompiledPackage;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.SourceUnit;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitFingerprintService;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.api.UnitStoreException;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.eclipse.fennec.m2x.unit.satellite.SatelliteCollector;

/**
 * The unit store: sources and compiled units over a {@link UnitStoreBackend}.
 *
 * <p>The store owns what goes over the wire. A compiled unit is stored as the XMI of its
 * document and comes back as an independent copy, loaded into a fresh resource set — a later
 * write cannot change what an earlier caller holds, and a caller's mutation cannot change the
 * store. A source is stored as a {@link SourceUnit} document around the text. The key of
 * either carries its fingerprint: the manifest's {@code unitFingerprint} for a compiled unit,
 * the source fingerprint of the m2x mechanism for a source.
 *
 * <p><b>What a compiled unit needs to be stored.</b> Only the document form,
 * {@link Unit.Packaged}: a bare AST has no manifest, no satellite container and no fingerprint
 * to be stored by — {@code compile()} first. The document is copied on the way in, so a unit
 * that already sits in a resource stays there.
 *
 * <p><b>Metamodels.</b> A document refers to its metamodels by URI. XMI can only write such a
 * reference for a package that lives in a resource, and a package built in memory does not;
 * the store gives it the resource EMF gives a generated package in {@code createResource}: one
 * named by its nsURI. On the way out the copies a document carries
 * ({@code CompiledUnit.packages}) are registered in the loading resource set for every nsURI
 * the store's package registry does not know, so a unit compiled against a dynamic metamodel
 * loads where that metamodel is absent — the compare-and-adopt against the runtime instance is
 * prepare's business (#140). A reference that still resolves to nothing afterwards is an error,
 * not a proxy handed on.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class DefaultUnitStore implements UnitStore {

	private final UnitStoreBackend backend;
	private final EPackage.Registry packages;
	private final UnitFingerprintService fingerprints;

	/**
	 * Creates a store over a backend, resolving metamodels through the global registry.
	 *
	 * @param backend what carries the content
	 */
	public DefaultUnitStore(UnitStoreBackend backend) {
		this(backend, EPackage.Registry.INSTANCE);
	}

	/**
	 * Creates a store over a backend, resolving metamodels through the given registry.
	 *
	 * @param backend what carries the content
	 * @param packages where a loaded unit's metamodels are looked up
	 */
	public DefaultUnitStore(UnitStoreBackend backend, EPackage.Registry packages) {
		this(backend, packages, DefaultUnitFingerprintService.INSTANCE);
	}

	/**
	 * Creates a store with every collaborator given.
	 *
	 * @param backend what carries the content
	 * @param packages where a loaded unit's metamodels are looked up
	 * @param fingerprints where a source's fingerprint comes from
	 */
	public DefaultUnitStore(UnitStoreBackend backend, EPackage.Registry packages,
			UnitFingerprintService fingerprints) {
		this.backend = Objects.requireNonNull(backend, "backend must not be null");
		this.packages = Objects.requireNonNull(packages, "packages must not be null");
		this.fingerprints = Objects.requireNonNull(fingerprints, "fingerprints must not be null");
	}

	@Override
	public UnitKey store(String language, Unit unit) throws UnitStoreException {
		Objects.requireNonNull(language, "language must not be null");
		Objects.requireNonNull(unit, "unit must not be null");
		return switch (unit) {
			case Unit.Packaged packaged -> storeCompiled(language, packaged);
			case Unit.Source source -> storeSource(language, source);
			case Unit.Compiled compiled -> throw new UnitStoreException("the unit '" + unit.qualifiedName()
					+ "' is a bare AST; a store takes the compiled document — compile() first");
			default -> throw new UnitStoreException("unknown kind of unit: " + unit.getClass().getName());
		};
	}

	private UnitKey storeCompiled(String language, Unit.Packaged packaged) throws UnitStoreException {
		CompiledUnit document = packaged.document();
		String declared = document.getManifest().getLanguage();
		if (declared != null && !declared.equals(language)) {
			throw new UnitStoreException("the unit '" + packaged.qualifiedName() + "' declares language '"
					+ declared + "', not '" + language + "'");
		}
		String fingerprint = document.getManifest().getUnitFingerprint();
		if (fingerprint == null) {
			fingerprint = fingerprints.fingerprint(document);
		}
		UnitKey key = UnitKey.pinned(language, packaged.qualifiedName(), UnitKind.COMPILED, fingerprint);
		backend.put(key, serialize(EcoreUtil.copy(document), key));
		return key;
	}

	private UnitKey storeSource(String language, Unit.Source source) throws UnitStoreException {
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
	public Optional<Unit> load(UnitKey key) throws UnitStoreException {
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

	private byte[] serialize(EObject document, UnitKey key) throws UnitStoreException {
		giveResourcelessMetamodelsAResource(document);
		ResourceSet resourceSet = freshResourceSet();
		Resource resource = resourceSet.createResource(uriOf(key));
		resource.getContents().add(document);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			resource.save(out, Map.of(XMLResource.OPTION_ENCODING, "UTF-8"));
		} catch (IOException | RuntimeException e) {
			throw new UnitStoreException("cannot serialize '" + key.qualifiedName() + "': " + e.getMessage(), e);
		}
		return out.toByteArray();
	}

	private EObject deserialize(byte[] bytes, UnitKey key) throws UnitStoreException {
		UnitResourceSet resourceSet = freshResourceSet();
		Resource resource = resourceSet.createResource(uriOf(key));
		try {
			resource.load(new ByteArrayInputStream(bytes), null);
		} catch (IOException | RuntimeException e) {
			throw new UnitStoreException("cannot read '" + key.qualifiedName() + "': " + e.getMessage(), e);
		}
		if (resource.getContents().isEmpty()) {
			throw new UnitStoreException("the content under " + key + " is empty");
		}
		EObject root = resource.getContents().get(0);
		if (root instanceof CompiledUnit document) {
			// A copy carried by the unit serves where the registry has nothing for the nsURI
			for (EPackage copy : document.getPackages()) {
				if (copy.getNsURI() != null && resourceSet.getPackageRegistry().getEPackage(copy.getNsURI()) == null) {
					resourceSet.serveFromCopy(copy);
				}
			}
		}
		EcoreUtil.resolveAll(resourceSet);
		Map<EObject, ?> unresolved = EcoreUtil.UnresolvedProxyCrossReferencer.find(resourceSet);
		if (!unresolved.isEmpty()) {
			EObject first = unresolved.keySet().iterator().next();
			throw new UnitStoreException("the unit '" + key.qualifiedName() + "' refers to " + unresolved.size()
					+ " object(s) that resolve to nothing here, first: " + EcoreUtil.getURI(first));
		}
		return root;
	}

	private UnitResourceSet freshResourceSet() {
		UnitResourceSet resourceSet = new UnitResourceSet();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
		// The store's registry answers for the user's metamodels; the languages' own metamodels —
		// the classes a document is an instance of — are generated and live in the global registry,
		// so that one is the last resort behind it
		EPackage.Registry registry = new EPackageRegistryImpl(packages) {
			private static final long serialVersionUID = 1L;

			@Override
			protected EPackage delegatedGetEPackage(String nsURI) {
				EPackage ePackage = super.delegatedGetEPackage(nsURI);
				return ePackage != null ? ePackage : EPackage.Registry.INSTANCE.getEPackage(nsURI);
			}

			@Override
			protected EFactory delegatedGetEFactory(String nsURI) {
				EFactory eFactory = super.delegatedGetEFactory(nsURI);
				return eFactory != null ? eFactory : EPackage.Registry.INSTANCE.getEFactory(nsURI);
			}
		};
		registry.put(CompiledPackage.eNS_URI, CompiledPackage.eINSTANCE);
		resourceSet.setPackageRegistry(registry);
		return resourceSet;
	}

	/**
	 * XMI writes a reference to another document as {@code <resource URI>#<fragment>}, so a
	 * referenced package has to live in a resource — a generated one does (EMF's
	 * {@code createResource(nsURI)}), a package built in memory does not. The store gives it the
	 * same: a resource named by its nsURI, which is exactly how the document refers to it.
	 */
	private static void giveResourcelessMetamodelsAResource(EObject document) {
		for (EObject target : EcoreUtil.ExternalCrossReferencer.find(document).keySet()) {
			if (target.eResource() == null && !target.eIsProxy() && SatelliteCollector.isMetamodelElement(target)) {
				EPackage root = (EPackage) EcoreUtil.getRootContainer(target);
				if (root.eResource() == null) {
					new ResourceImpl(URI.createURI(root.getNsURI())).getContents().add(root);
				}
			}
		}
	}

	/**
	 * A resource set that can resolve a metamodel reference against a package copy the document
	 * carries. Going through the registry would not do: EMF resolves {@code nsURI#//Book} by
	 * asking the registered package for its resource and that resource for the fragment — and
	 * the copy's resource is the document's, whose root is the {@code CompiledUnit}, not the
	 * package. So the fragment is walked from the copy itself, the way Ecore's own fragment
	 * convention ({@code //Classifier/feature}) reads.
	 */
	private static final class UnitResourceSet extends ResourceSetImpl {
		private final Map<String, EPackage> copies = new HashMap<>();

		void serveFromCopy(EPackage copy) {
			copies.put(copy.getNsURI(), copy);
		}

		@Override
		public EObject getEObject(URI uri, boolean loadOnDemand) {
			EPackage copy = copies.get(uri.trimFragment().toString());
			if (copy != null && uri.fragment() != null && uri.fragment().startsWith("//")) {
				EObject current = copy;
				for (String segment : uri.fragment().substring(2).split("/")) {
					if (current == null) {
						break;
					}
					current = ((InternalEObject) current).eObjectForURIFragmentSegment(segment);
				}
				if (current != null) {
					return current;
				}
			}
			return super.getEObject(uri, loadOnDemand);
		}
	}

	private static URI uriOf(UnitKey key) {
		return URI.createURI("unit:/" + key.language() + "/" + key.kind().tag() + "/"
				+ key.qualifiedName() + "/" + key.fingerprint().orElse("current") + ".xmi");
	}
}
