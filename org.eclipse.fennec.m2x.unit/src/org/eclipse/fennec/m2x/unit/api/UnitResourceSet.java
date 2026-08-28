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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.fennec.m2x.model.compiled.CompiledPackage;

/**
 * The resource set a unit is loaded into — by a store for one unit, by prepare for a whole
 * pipeline.
 *
 * <p>Its package registry answers in three tiers: the registry it was given (the user's
 * metamodels — a store's, a prepare run's runtime registry), then the global registry (the
 * languages' own metamodels, which are generated), then the copies a loaded document carries
 * for the nsURIs neither knows ({@link #serveFromCopy}). A copy cannot be served through the
 * registry: EMF resolves {@code nsURI#//Book} by asking the registered package for its resource
 * and that resource for the fragment, and the copy's resource is the document's, whose root is
 * the {@code CompiledUnit}. So the fragment is walked from the copy itself, the way Ecore's own
 * fragment convention ({@code //Classifier/feature}) reads — and the same walk serves a registry
 * instance that lives in no resource, which EMF's route through the resource cannot reach.
 *
 * <p>Which instance a document's type references resolve to is decided here, once, at load
 * time — that is what makes a prepared context's type identity a property of the context.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class UnitResourceSet extends ResourceSetImpl {

	private final Map<String, EPackage> copies = new HashMap<>();

	/**
	 * Creates a resource set whose registry falls back from the given one to the global one.
	 *
	 * @param packages the registry that answers first, e.g. the user's metamodels
	 */
	public UnitResourceSet(EPackage.Registry packages) {
		Objects.requireNonNull(packages, "packages must not be null");
		getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
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
		setPackageRegistry(registry);
	}

	/**
	 * Lets a package copy a document carries answer for its nsURI, for as long as the registry
	 * has nothing of its own. The first copy registered for an nsURI stays — every unit of one
	 * context sees the same instance.
	 *
	 * @param copy the package copy, contained in a loaded document
	 * @return {@code true} if the copy now serves the nsURI, {@code false} if the nsURI was
	 *         already answered for
	 */
	public boolean serveFromCopy(EPackage copy) {
		Objects.requireNonNull(copy, "copy must not be null");
		if (copy.getNsURI() == null || getPackageRegistry().getEPackage(copy.getNsURI()) != null
				|| copies.containsKey(copy.getNsURI())) {
			return false;
		}
		copies.put(copy.getNsURI(), copy);
		return true;
	}

	/**
	 * Returns the package that answers for an nsURI in this resource set: the registry's
	 * instance, or the copy that serves it.
	 *
	 * @param nsURI the namespace URI
	 * @return the package, or {@code null} if nothing answers
	 */
	public EPackage packageFor(String nsURI) {
		EPackage ePackage = getPackageRegistry().getEPackage(nsURI);
		return ePackage != null ? ePackage : copies.get(nsURI);
	}

	/**
	 * A package-rooted reference ({@code nsURI#//Classifier/feature}) is resolved against the
	 * package that answers for the nsURI — a copy, or a registry instance. EMF's own route goes
	 * through the package's resource, and a package built in memory has none; walking the
	 * fragment from the package is what that route does for a package that has one.
	 */
	@Override
	public EObject getEObject(URI uri, boolean loadOnDemand) {
		EPackage ePackage = uri.fragment() != null && uri.fragment().startsWith("/")
				? packageFor(uri.trimFragment().toString()) : null;
		if (ePackage != null && "/".equals(uri.fragment())) {
			return ePackage; // the package itself, e.g. a model type's metamodel
		}
		if (ePackage != null && uri.fragment().startsWith("//")) {
			EObject current = ePackage;
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
