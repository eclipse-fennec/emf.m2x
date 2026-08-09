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
package org.eclipse.fennec.m2x.ocl.fingerprint;

import java.util.Objects;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.emf.osgi.fingerprint.FingerprintService;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;

/**
 * An expression cache that tells two versions of one model apart.
 *
 * <p>The cache that ships with the engine keys by nsURI, which names a model but not a model
 * <em>version</em>. Where two versions of one nsURI are live at once — the situation the
 * fingerprint work exists for — they share an entry, and whichever was compiled first decides
 * what the other one gets. The failure is quiet: property access falls back to resolving a
 * feature by name on the runtime class, so navigation survives by accident, while an
 * expression naming a type answers about the wrong {@code EClass}. A perfectly valid object
 * is then reported as invalid, with nothing said about why.
 *
 * <p>This one derives its key with
 * {@link FingerprintService#fingerprint(EPackage, String...)} — the "can I reuse this
 * artifact?" key of the fingerprint guide, computed from the package content rather than from
 * its name. Two versions produce two fingerprints and therefore two entries; two loads of the
 * same content produce one, which is what an identity-based key could not do.
 *
 * <p>It is a decorator rather than a cache of its own: what to store and how long to keep it
 * is the wrapped cache's business, and only the key changes.
 *
 * <p><b>Model mutation after registration is out of contract.</b> A fingerprint is computed
 * once per package instance; changing a package after expressions have been compiled against
 * it leaves entries that no longer describe it, and this cache does not try to notice.
 *
 * @since 1.0
 */
public class FingerprintExpressionCache implements OclExpressionCache {

	/** Names what the fingerprint is derived for, so other artifacts of one model differ. */
	private static final String PURPOSE = "ocl";

	private final OclExpressionCache delegate;
	private final FingerprintService fingerprintService;

	/**
	 * @param delegate           the cache that stores the entries, must not be {@code null}
	 * @param fingerprintService the service that computes model identity, must not be
	 *                           {@code null} — injected in OSGi, and
	 *                           {@code FingerprintHelper.getDefaultFingerprintService()}
	 *                           outside it, so that this bundle compiles against the API
	 *                           alone and never against an implementation
	 */
	public FingerprintExpressionCache(OclExpressionCache delegate,
			FingerprintService fingerprintService) {
		this.delegate = Objects.requireNonNull(delegate, "delegate must not be null");
		this.fingerprintService =
				Objects.requireNonNull(fingerprintService, "fingerprintService must not be null");
	}

	@Override
	public OclExpression get(String expression, EClassifier contextType) {
		return delegate.get(key(expression, contextType), contextType);
	}

	@Override
	public void put(String expression, EClassifier contextType, OclExpression parsed) {
		delegate.put(key(expression, contextType), contextType, parsed);
	}

	@Override
	public void invalidate(String expression, EClassifier contextType) {
		delegate.invalidate(key(expression, contextType), contextType);
	}

	@Override
	public void invalidateAll() {
		delegate.invalidateAll();
	}

	@Override
	public long size() {
		return delegate.size();
	}

	@Override
	public long hitCount() {
		return delegate.hitCount();
	}

	@Override
	public long missCount() {
		return delegate.missCount();
	}

	/**
	 * The expression text, prefixed with the fingerprint of the model it is compiled against.
	 *
	 * <p>The prefix is what the wrapped cache then combines with the context type, so its own
	 * key stays well-formed and this one only makes it version-aware.
	 *
	 * <p>Without a package there is nothing to fingerprint — a classifier of Ecore itself, or
	 * a type built at runtime — and the expression is passed through unchanged. That leaves
	 * the wrapped cache's behaviour, which is the honest answer when there is no version to
	 * distinguish.
	 */
	private String key(String expression, EClassifier contextType) {
		EPackage ePackage = contextType != null ? contextType.getEPackage() : null;
		if (ePackage == null) {
			return expression;
		}
		String fingerprint = fingerprintService.fingerprint(ePackage, PURPOSE);
		return fingerprint == null ? expression : fingerprint + "#" + expression;
	}
}
