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
package org.eclipse.fennec.m2x.unit.materialize;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitMaterializeException;
import org.eclipse.fennec.m2x.unit.api.UnitResourceSet;
import org.eclipse.fennec.m2x.unit.api.UnitStoreException;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.eclipse.fennec.m2x.unit.store.UnitXmi;
import org.eclipse.fennec.m2x.unit.validate.UnitValidator;

/**
 * Binds a unit document in a consumer's context — the one place a document's references are
 * resolved, checked and validated (#211).
 *
 * <p>A store hands out documents with their references unresolved: proxies are the transport
 * state. Materializing moves the document's resource into the consumer's
 * {@link UnitResourceSet}, lets the copies the document carries answer for the nsURIs the
 * context does not know ({@link UnitResourceSet#serveFromCopy}), resolves everything, and runs
 * the {@link UnitValidator} funnel (#142). "Never hand a proxy on" is this class's promise —
 * a reference that resolves to nothing in the context is an error naming it, never a proxy
 * handed on.
 *
 * <p>Which instance a reference binds to is decided by the context's tiers — its registry, the
 * global registry, the carried copies — so type identity is a property of the consumer's
 * context, not of the store the document came from.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class UnitMaterializer {

	private final UnitValidator validator;

	/**
	 * Creates a materializer with the given validation funnel.
	 *
	 * @param validator what every materialized document has to pass, or {@code null} for none
	 */
	public UnitMaterializer(UnitValidator validator) {
		this.validator = validator;
	}

	/**
	 * A materializer with the default validation funnel — the right one for documents from any
	 * medium: a stored document never passed the parser, this is where its shape is checked.
	 *
	 * @return the materializer, never {@code null}
	 */
	public static UnitMaterializer defaults() {
		return new UnitMaterializer(UnitValidator.defaults());
	}

	/**
	 * A materializer that skips validation — for a trusted, hot medium.
	 *
	 * @return the materializer, never {@code null}
	 */
	public static UnitMaterializer withoutValidation() {
		return new UnitMaterializer(null);
	}

	/**
	 * Materializes a compiled unit document in the given context.
	 *
	 * <p>The document's resource is moved into the context, so the unit afterwards belongs to
	 * it — one document is materialized in one context, once. The unit handed in is the unit
	 * returned.
	 *
	 * @param <U> the unit's type
	 * @param unit the unit in document form
	 * @param context the consumer's context
	 * @return the same unit, bound in the context
	 * @throws UnitMaterializeException if a reference resolves to nothing in the context, or
	 *             the document does not pass validation
	 */
	public <U extends Unit.Packaged> U materialize(U unit, UnitResourceSet context)
			throws UnitMaterializeException {
		Objects.requireNonNull(unit, "unit must not be null");
		Objects.requireNonNull(context, "context must not be null");
		CompiledUnit document = unit.document();
		Resource resource = document.eResource();
		if (resource == null) {
			resource = context.createResource(URI.createURI("unit:/materialized/" + unit.qualifiedName()));
			resource.getContents().add(document);
		} else if (resource.getResourceSet() != context) {
			context.getResources().add(resource);
		}
		for (EPackage copy : document.getPackages()) {
			context.serveFromCopy(copy);
		}
		EcoreUtil.resolveAll(resource);
		Map<EObject, ?> unresolved = EcoreUtil.UnresolvedProxyCrossReferencer.find(resource);
		if (!unresolved.isEmpty()) {
			EObject first = unresolved.keySet().iterator().next();
			throw new UnitMaterializeException("the unit '" + unit.qualifiedName() + "' refers to "
					+ unresolved.size() + " object(s) that resolve to nothing here, first: "
					+ EcoreUtil.getURI(first));
		}
		if (validator != null) {
			List<String> findings = validator.validate(document);
			if (!findings.isEmpty()) {
				throw new UnitMaterializeException("the unit '" + unit.qualifiedName() + "' is rejected: "
						+ String.join("; ", findings));
			}
		}
		return unit;
	}

	/**
	 * Loads a unit named by reference ({@code Unit.Referenced}, #214) and materializes it in the
	 * given context — the second consumer of this class beside the store path.
	 *
	 * <p>The URI is expected to hold a compiled-unit document; then this is a store load without
	 * the store — safe parse, carried copies served, everything resolved, the validation funnel
	 * run — and the returned root is the document. A bare AST — the pre-unit shape — loads too:
	 * it binds in this context, no proxy is handed on, but there is no manifest to check and no
	 * copy to serve.
	 *
	 * @param uri where the unit lives
	 * @param context the consumer's context
	 * @return the loaded root — a {@code CompiledUnit} document, or the bare AST
	 * @throws UnitMaterializeException if the content cannot be read, resolves to nothing here,
	 *             or a document does not pass validation
	 */
	public EObject load(URI uri, UnitResourceSet context) throws UnitMaterializeException {
		Objects.requireNonNull(uri, "uri must not be null");
		Objects.requireNonNull(context, "context must not be null");
		Resource resource = context.createResource(uri);
		try {
			UnitXmi.loadSafely(resource);
		} catch (UnitStoreException e) {
			throw new UnitMaterializeException(e.getMessage(), e);
		}
		EObject root = resource.getContents().get(0);
		if (root instanceof CompiledUnit document) {
			materialize(new PackagedUnit(document), context);
			return document;
		}
		EcoreUtil.resolveAll(resource);
		Map<EObject, ?> unresolved = EcoreUtil.UnresolvedProxyCrossReferencer.find(resource);
		if (!unresolved.isEmpty()) {
			EObject first = unresolved.keySet().iterator().next();
			throw new UnitMaterializeException("the unit at " + uri + " refers to " + unresolved.size()
					+ " object(s) that resolve to nothing here, first: " + EcoreUtil.getURI(first));
		}
		return root;
	}
}
