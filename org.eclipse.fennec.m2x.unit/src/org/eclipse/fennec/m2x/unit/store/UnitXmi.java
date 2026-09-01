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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitResourceSet;
import org.eclipse.fennec.m2x.unit.api.UnitStoreException;
import org.eclipse.fennec.m2x.unit.satellite.SatelliteCollector;

/**
 * The XMI form of a unit document — what the byte medium writes and reads, and what an object
 * medium uses once, on the way in, to normalize a live document into the transport state
 * (#211, #213).
 *
 * <p>Reading yields a document in a resource set of its own, references unresolved: proxies are
 * the transport state, and the {@code UnitMaterializer} is where they end. Writing serializes a
 * document that must not change under the writer's hands — callers pass a copy they own.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class UnitXmi {

	private UnitXmi() {}

	/**
	 * What a unit is parsed with: no doctype, no external entities.
	 *
	 * <p>A medium is a plugin — a file, a database, something remote — and the bytes it returns
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
	 * borrowed. The lock is global on purpose: stores are shared by construction, and two
	 * stores writing units of one caller borrow the same metamodel.
	 */
	private static final Object BORROWED_METAMODELS = new Object();

	/**
	 * Serializes a unit document.
	 *
	 * @param document the document — a copy the caller owns, not a borrowed graph
	 * @param key the key it is stored under, naming the resource
	 * @return the XMI bytes, never {@code null}
	 * @throws UnitStoreException if the document cannot be serialized
	 */
	public static byte[] write(EObject document, UnitKey key) throws UnitStoreException {
		synchronized (BORROWED_METAMODELS) {
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
	 *
	 * @param bytes the XMI bytes
	 * @param key the key they were stored under, for the resource URI and the messages
	 * @return the root, never {@code null}
	 * @throws UnitStoreException if the bytes cannot be read, or hold nothing
	 */
	public static EObject read(byte[] bytes, UnitKey key) throws UnitStoreException {
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
	 * {@code createResource(nsURI)}), a package built in memory does not. The write gives it the
	 * same: a resource named by its nsURI, which is exactly how the document refers to it.
	 */
	private static List<EPackage> giveResourcelessMetamodelsAResource(EObject document, ResourceSet resourceSet) {
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
