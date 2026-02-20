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
package org.eclipse.fennec.m2m.ocl.engine;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.ocl.api.OclExpressionCache;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * Default {@link OclExpressionCache} service component.
 *
 * <p>Provides an LRU expression cache with 1024 entries as the default
 * cache service in OSGi. This component is automatically picked up by
 * the {@link OclEngineComponent} unless a custom cache is targeted
 * via configuration.
 *
 * <p>To override with a custom cache, register your own
 * {@code OclExpressionCache} service and target it in the engine
 * configuration:
 * <pre>
 * // OSGi Configurator JSON
 * "org.eclipse.fennec.m2m.ocl.engine.OclEngineComponent": {
 *     "expressionCache.target": "(cache.name=myCustomCache)"
 * }
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Component(service = OclExpressionCache.class)
public class DefaultOclExpressionCacheComponent implements OclExpressionCache {

	/** Default cache size: 1024 entries. */
	private static final int DEFAULT_SIZE = 1024;

	private final OclLruExpressionCache delegate;

	@Activate
	public DefaultOclExpressionCacheComponent() {
		this.delegate = new OclLruExpressionCache(DEFAULT_SIZE);
	}

	@Override
	public OclExpression get(String expression, EClassifier contextType) {
		return delegate.get(expression, contextType);
	}

	@Override
	public void put(String expression, EClassifier contextType, OclExpression parsed) {
		delegate.put(expression, contextType, parsed);
	}

	@Override
	public void invalidate(String expression, EClassifier contextType) {
		delegate.invalidate(expression, contextType);
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
}
