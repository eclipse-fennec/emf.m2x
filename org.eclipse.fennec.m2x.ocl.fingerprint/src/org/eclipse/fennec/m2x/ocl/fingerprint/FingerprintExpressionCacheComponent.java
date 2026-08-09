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

import org.eclipse.fennec.emf.osgi.fingerprint.FingerprintService;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;
import org.eclipse.fennec.m2x.ocl.engine.OclLruExpressionCache;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Publishes the fingerprint-keyed cache as an {@link OclExpressionCache} service.
 *
 * <p>It carries {@code cache.name=fingerprint} and no service ranking, so it does not take
 * the place of the default cache on its own. A deployment that wants it says so:
 *
 * <pre>
 * "DefaultOclEngine": { "expressionCache.target": "(cache.name=fingerprint)" }
 * </pre>
 *
 * <p>Not the default, because the cache that ships with the engine is right whenever one
 * version of an nsURI is live at a time, which is the ordinary case. Where two are — the
 * situation the fingerprint work exists for — this one is what keeps a compiled expression
 * from being handed to the version it was not compiled against.
 *
 * @since 1.0
 */
@Designate(ocd = FingerprintExpressionCacheConfiguration.class)
@Component(name = "FingerprintOclExpressionCache", service = OclExpressionCache.class,
		property = "cache.name=fingerprint")
public class FingerprintExpressionCacheComponent extends FingerprintExpressionCache {

	@Activate
	public FingerprintExpressionCacheComponent(FingerprintExpressionCacheConfiguration config,
			@Reference FingerprintService fingerprintService) {
		super(OclLruExpressionCache.ofSize(config.maxSize()), fingerprintService);
	}
}
