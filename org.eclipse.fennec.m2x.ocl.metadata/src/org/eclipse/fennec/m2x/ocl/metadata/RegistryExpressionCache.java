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

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.emf.osgi.fingerprint.FingerprintService;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistry;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistryWriter;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;

/**
 * Keeps compiled expressions in a named {@link EObjectRegistry}, anchored to the model
 * version they were compiled against.
 *
 * <p>An LRU inside the engine knows how many entries it may keep, but not what a model
 * version is: an expression compiled against a model long retired survives because it was
 * used recently, and one belonging to a live model is dropped because it was not. A registry
 * entry carries {@code emf.nsURI} and {@code emf.fingerprint}, which is what ties it to the
 * version it belongs to — and what lets the {@code RegistryMetadataBridge} put it onto the
 * tree of every live version, including one that registers later.
 *
 * <p><b>The registry is the read face here, not the aspect.</b> The metadata bridge files at
 * most one aspect per anchor class and type id, so a domain with many entries per class —
 * which every OCL context type is, since each expression is an entry of its own — keeps the
 * registry as its query face. That is the boundary the registry guide draws, and this cache
 * stays on the right side of it.
 *
 * <p>The key carries the fingerprint, so two versions of one nsURI never answer for each
 * other. Where a context type has no package to fingerprint — a classifier of Ecore itself,
 * or a type built at runtime — there is no version to anchor to and the delegate takes it.
 *
 * <p><b>Model mutation after registration is out of contract.</b> A fingerprint describes the
 * package as it was; changing it afterwards leaves entries that no longer describe it, and
 * nothing here tries to notice.
 *
 * @since 1.0
 */
public class RegistryExpressionCache implements OclExpressionCache {

	/** Entry property naming the model an entry belongs to. */
	public static final String PROP_NS_URI = "emf.nsURI";

	/** Entry property naming the model <em>version</em> an entry belongs to. */
	public static final String PROP_FINGERPRINT = "emf.fingerprint";

	/** What this cache derives fingerprints for, so other artifacts of a model differ. */
	private static final String PURPOSE = "ocl";

	/** Technical origin of the entries, which is what {@code sync} is scoped by. */
	private static final String SOURCE = "ocl-compiled";

	private final EObjectRegistryWriter writer;
	private final EObjectRegistry registry;
	private final FingerprintService fingerprintService;
	private final OclExpressionCache delegate;
	private final AtomicLong hits = new AtomicLong();
	private final AtomicLong misses = new AtomicLong();

	/**
	 * @param writer             the registry to file compiled expressions in, must not be
	 *                           {@code null}
	 * @param fingerprintService computes the model version an entry belongs to, must not be
	 *                           {@code null}
	 * @param delegate           takes what has no model version to anchor to, must not be
	 *                           {@code null}
	 */
	public RegistryExpressionCache(EObjectRegistryWriter writer,
			FingerprintService fingerprintService, OclExpressionCache delegate) {
		this.writer = Objects.requireNonNull(writer, "writer must not be null");
		this.registry = writer.getRegistry();
		this.fingerprintService =
				Objects.requireNonNull(fingerprintService, "fingerprintService must not be null");
		this.delegate = Objects.requireNonNull(delegate, "delegate must not be null");
	}

	@Override
	public OclExpression get(String expression, EClassifier contextType) {
		String fingerprint = fingerprintOf(contextType);
		if (fingerprint == null) {
			return delegate.get(expression, contextType);
		}
		Optional<EObject> found = registry.get(key(fingerprint, expression, contextType));
		if (found.isPresent() && found.get() instanceof OclExpression parsed) {
			hits.incrementAndGet();
			return parsed;
		}
		misses.incrementAndGet();
		return null;
	}

	@Override
	public void put(String expression, EClassifier contextType, OclExpression parsed) {
		String fingerprint = fingerprintOf(contextType);
		if (fingerprint == null) {
			delegate.put(expression, contextType, parsed);
			return;
		}
		writer.put(SOURCE, key(fingerprint, expression, contextType), parsed,
				Map.of(PROP_NS_URI, contextType.getEPackage().getNsURI(),
						PROP_FINGERPRINT, fingerprint));
	}

	@Override
	public void invalidate(String expression, EClassifier contextType) {
		String fingerprint = fingerprintOf(contextType);
		if (fingerprint == null) {
			delegate.invalidate(expression, contextType);
			return;
		}
		writer.remove(SOURCE, key(fingerprint, expression, contextType));
	}

	@Override
	public void invalidateAll() {
		// Only what this cache wrote: a registry is shared, and other sources' entries are
		// none of its business — which is what scoping by source is for.
		writer.sync(SOURCE, java.util.List.of());
		delegate.invalidateAll();
	}

	@Override
	public long size() {
		return registry.entries().stream().filter(e -> SOURCE.equals(e.source())).count()
				+ delegate.size();
	}

	@Override
	public long hitCount() {
		return hits.get() + delegate.hitCount();
	}

	@Override
	public long missCount() {
		return misses.get() + delegate.missCount();
	}

	/**
	 * The fingerprint of the model the context type belongs to, or {@code null} when there is
	 * no package and therefore no version to anchor to.
	 */
	private String fingerprintOf(EClassifier contextType) {
		EPackage ePackage = contextType != null ? contextType.getEPackage() : null;
		return ePackage == null ? null : fingerprintService.fingerprint(ePackage, PURPOSE);
	}

	/**
	 * Registry keys are unique per registry and the convention is the domain's own. Ours is
	 * version, then context type, then expression — so that two versions of one model never
	 * answer for each other, and two classes of one model do not either.
	 */
	private static String key(String fingerprint, String expression, EClassifier contextType) {
		return fingerprint + "#" + contextType.getName() + "#" + expression;
	}
}
