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
package org.eclipse.fennec.m2x.unit.prepare;

import java.util.Collection;
import java.util.LinkedHashMap;
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
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitPrepareException;
import org.eclipse.fennec.m2x.unit.api.UnitResourceSet;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;

/**
 * The context a {@link UnitPreparer} produces: one resource set, the verified registry, the
 * bound units.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class DefaultPreparedContext implements PreparedContext {

	private final UnitResourceSet resourceSet;
	private final Map<String, PackagedUnit> units;

	DefaultPreparedContext(UnitResourceSet resourceSet, Map<String, PackagedUnit> units) {
		this.resourceSet = Objects.requireNonNull(resourceSet, "resourceSet must not be null");
		this.units = new LinkedHashMap<>(units);
	}

	@Override
	public ResourceSet resourceSet() {
		return resourceSet;
	}

	@Override
	public EPackage.Registry packageRegistry() {
		return resourceSet.getPackageRegistry();
	}

	@Override
	public Optional<Unit> unit(String qualifiedName) {
		return Optional.ofNullable(units.get(qualifiedName));
	}

	@Override
	public Collection<Unit> units() {
		return List.copyOf(units.values());
	}

	@Override
	public List<EObject> contents(URI uri) throws UnitPrepareException {
		Objects.requireNonNull(uri, "uri must not be null");
		try {
			Resource resource = resourceSet.getResource(uri, true);
			EcoreUtil.resolveAll(resource);
			Map<EObject, ?> unresolved = EcoreUtil.UnresolvedProxyCrossReferencer.find(resource);
			if (!unresolved.isEmpty()) {
				EObject first = unresolved.keySet().iterator().next();
				throw new UnitPrepareException("the model " + uri + " refers to " + unresolved.size()
						+ " object(s) this context cannot resolve, first: " + EcoreUtil.getURI(first));
			}
			return resource.getContents();
		} catch (RuntimeException e) {
			throw new UnitPrepareException("cannot load " + uri + " in this context: " + e.getMessage(), e);
		}
	}
}
