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
package org.eclipse.fennec.m2x.m2t.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * Loads the metamodels and models of one generation from files, and makes them
 * resolvable the way MOFM2T needs them.
 *
 * <p>Metamodels arrive as {@code .ecore} files somewhere in a bnd project — a location,
 * not an identity. Everything downstream addresses them by their namespace URI instead:
 * a template declares {@code [module vcard('http://.../addressbook/1.0')/]}, an instance
 * model names the nsURI of its metamodel, and the M2T parser resolves the type names in
 * {@code [template public main(b : AddressBook)]} against a {@link EPackage.Registry}.
 * So a file that was loaded from a {@code file:} URI has to be registered under its
 * nsURI before anything else happens — which is what {@link #loadMetamodel(File)} does,
 * for the root package and every subpackage below it.
 *
 * <p>Everything lives in one {@link ResourceSet} whose package registry is
 * <em>isolated</em>: it has no delegate to {@link EPackage.Registry#INSTANCE} (D42). The
 * generator runs inside the build tool, where the global registry is neither ours nor
 * predictable — under bndtools an Eclipse-hosted EMF is on the parent class loader.
 * Only what was configured is visible here, plus Ecore and XMLType themselves.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class M2tModelLoader {

	private final ResourceSet resourceSet;
	private final Map<String, File> registeredPackages = new LinkedHashMap<>();
	private final List<String> warnings = new ArrayList<>();

	/**
	 * Creates a loader with its own resource set and its own package registry.
	 */
	public M2tModelLoader() {
		ResourceSetImpl rs = new ResourceSetImpl();
		// Set before anything asks for it: the lazily created default would delegate to
		// EPackage.Registry.INSTANCE.
		rs.setPackageRegistry(new EPackageRegistryImpl());
		rs.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		rs.getPackageRegistry().put(XMLTypePackage.eNS_URI, XMLTypePackage.eINSTANCE);

		Map<String, Object> factories = rs.getResourceFactoryRegistry().getExtensionToFactoryMap();
		factories.put("ecore", new EcoreResourceFactoryImpl());
		// Instance models are not required to be called .xmi — an .addressbook or a
		// .model file is just as common — so XMI is the default for everything else.
		factories.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

		this.resourceSet = rs;
	}

	/**
	 * Loads an {@code .ecore} file and registers every {@link EPackage} it contains
	 * under its namespace URI.
	 *
	 * <p>The resource keeps its {@code file:} URI, so that cross references between two
	 * ecore files written as relative paths resolve as they stand. In addition the nsURI
	 * of each root package is mapped to that file in the
	 * {@linkplain ResourceSet#getURIConverter() URI converter}, which is what makes a
	 * cross reference written as {@code http://other/ns#//Type} resolve without the
	 * other metamodel being installed anywhere.
	 *
	 * @param file the ecore file, must not be {@code null} and must exist
	 * @return the root packages that were loaded, in file order
	 * @throws IOException if the file cannot be read or contains no package
	 */
	public List<EPackage> loadMetamodel(File file) throws IOException {
		Objects.requireNonNull(file, "file must not be null");
		URI fileUri = toUri(file);
		Resource resource = load(fileUri);

		List<EPackage> roots = new ArrayList<>();
		for (EObject content : resource.getContents()) {
			if (content instanceof EPackage ePackage) {
				register(ePackage, file, fileUri, true);
				roots.add(ePackage);
			} else {
				warnings.add("Ignored a " + content.eClass().getName() + " at the root of metamodel "
						+ file + " — only EPackage is registered");
			}
		}
		if (roots.isEmpty()) {
			throw new IOException("Metamodel contains no EPackage: " + file);
		}
		return roots;
	}

	/**
	 * Loads an instance model. Its metamodel has to be registered already, which is why
	 * every {@code ecore} of a generation is loaded before its {@code model}s.
	 *
	 * @param file the model file, must not be {@code null} and must exist
	 * @return the root elements of the model, in file order
	 * @throws IOException if the file cannot be read
	 */
	public List<EObject> loadModel(File file) throws IOException {
		Objects.requireNonNull(file, "file must not be null");
		Resource resource = load(toUri(file));
		if (resource.getContents().isEmpty()) {
			warnings.add("Model has no content: " + file);
		}
		return new ArrayList<>(resource.getContents());
	}

	/**
	 * Returns the resource set every metamodel and model of this generation was loaded
	 * into. Its package registry is what the M2T engine resolves type names against.
	 *
	 * @return the resource set, never {@code null}
	 */
	public ResourceSet getResourceSet() {
		return resourceSet;
	}

	/**
	 * Returns what was odd but not fatal while loading, such as an empty model or a
	 * namespace URI registered twice.
	 *
	 * @return the warnings, never {@code null}
	 */
	public List<String> getWarnings() {
		return List.copyOf(warnings);
	}

	private Resource load(URI uri) throws IOException {
		try {
			return resourceSet.getResource(uri, true);
		} catch (RuntimeException e) {
			// EMF wraps the IOException of a missing or broken file
			throw new IOException("Cannot load " + uri + ": " + e.getMessage(), e);
		}
	}

	private void register(EPackage ePackage, File file, URI fileUri, boolean root) {
		String nsURI = ePackage.getNsURI();
		if (nsURI == null || nsURI.isBlank()) {
			warnings.add("Package '" + ePackage.getName() + "' in " + file
					+ " has no nsURI and cannot be registered");
		} else {
			File previous = registeredPackages.put(nsURI, file);
			if (previous != null && !previous.equals(file)) {
				warnings.add("nsURI '" + nsURI + "' is declared by " + previous + " and by " + file
						+ " — the latter wins");
			}
			resourceSet.getPackageRegistry().put(nsURI, ePackage);
			if (root) {
				// Only for a root package: a fragment behind a subpackage nsURI would be
				// resolved from the file's root anyway, so mapping it would mislead.
				resourceSet.getURIConverter().getURIMap().putIfAbsent(URI.createURI(nsURI), fileUri);
			}
		}
		for (EPackage subPackage : ePackage.getESubpackages()) {
			register(subPackage, file, fileUri, false);
		}
	}

	private static URI toUri(File file) {
		return URI.createFileURI(file.getAbsolutePath());
	}
}
