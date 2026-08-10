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
package org.eclipse.fennec.m2x.ocl.metadata;

import java.util.Optional;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistryWriter;
import org.eclipse.fennec.emf.osgi.fingerprint.FingerprintService;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;
import org.eclipse.fennec.m2x.ocl.engine.OclLruExpressionCache;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Publishes the registry-backed cache as an {@link OclExpressionCache} service.
 *
 * <p>It composes the cache rather than extending it: a component is wiring, and what it
 * publishes should be replaceable without inheriting anything.
 *
 * <p>Two caches in one: what has a model version to anchor to goes into the registry, where
 * it is filed under that version and goes with it; what has none — a classifier of Ecore
 * itself, a type built at runtime — goes into an LRU, which is the right structure for
 * something with no lifetime of its own.
 *
 * <p>It carries {@code cache.name=metadata} and no service ranking, so it does not displace
 * the default cache by existing. A deployment that wants it says so:
 *
 * <pre>
 * "DefaultOclEngine": { "expressionCache.target": "(cache.name=metadata)" }
 * </pre>
 *
 * <p>The registry is selected by name, as registries always are:
 *
 * <pre>
 * "OclMetadataExpressionCache": {
 *     "writer.target": "(emf.eobject.registry.name=ocl-compiled)"
 * }
 * </pre>
 *
 * @since 1.0
 */
@Designate(ocd = OclMetadataCacheConfiguration.class)
@Component(name = "OclMetadataExpressionCache",
		service = { OclExpressionCache.class, OclVersionedExpressions.class },
		property = "cache.name=metadata")
public class OclMetadataCacheComponent implements OclExpressionCache, OclVersionedExpressions {

	private final RegistryExpressionCache cache;

	@Activate
	public OclMetadataCacheComponent(OclMetadataCacheConfiguration config,
			@Reference(name = "writer") EObjectRegistryWriter writer,
			@Reference FingerprintService fingerprintService) {
		this.cache = new RegistryExpressionCache(writer, fingerprintService,
				OclLruExpressionCache.ofSize(config.lruSize()));
	}

	@Override
	public OclExpression get(String expression, EClassifier contextType) {
		return cache.get(expression, contextType);
	}

	@Override
	public void put(String expression, EClassifier contextType, OclExpression parsed) {
		cache.put(expression, contextType, parsed);
	}

	@Override
	public void invalidate(String expression, EClassifier contextType) {
		cache.invalidate(expression, contextType);
	}

	@Override
	public void invalidateAll() {
		cache.invalidateAll();
	}

	@Override
	public long size() {
		return cache.size();
	}

	@Override
	public long hitCount() {
		return cache.hitCount();
	}

	@Override
	public long missCount() {
		return cache.missCount();
	}

	@Override
	public Optional<EClass> anchorOf(String registryKey) {
		return cache.anchorOf(registryKey);
	}

	@Override
	public int release(EPackage ePackage) {
		return cache.release(ePackage);
	}
}
