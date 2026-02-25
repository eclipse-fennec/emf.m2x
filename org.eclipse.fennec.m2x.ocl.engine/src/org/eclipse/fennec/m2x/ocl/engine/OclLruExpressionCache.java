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
package org.eclipse.fennec.m2x.ocl.engine;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;

/**
 * LRU (Least Recently Used) implementation of {@link OclExpressionCache}.
 *
 * <p>Based on {@link LinkedHashMap} with {@code accessOrder=true} and a
 * synchronized wrapper. All map operations are synchronized; hit/miss
 * counters use {@link AtomicLong} for lock-free reads.
 *
 * <p>Thread-safe: the synchronized block around map operations is sufficient
 * because parsing (the expensive part) happens outside the lock. Duplicate
 * parsing under race conditions is harmless and idempotent.
 *
 * <p>Usage:
 * <pre>
 * OclExpressionCache cache = OclLruExpressionCache.ofSize(1024);
 * OclEngine engine = new OclEngineImpl(parser, cache);
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class OclLruExpressionCache implements OclExpressionCache {

	private final int maxSize;
	private final Map<String, OclExpression> cache;
	private final AtomicLong hits = new AtomicLong();
	private final AtomicLong misses = new AtomicLong();

	/**
	 * Creates a new LRU cache with the given maximum size.
	 *
	 * @param maxSize the maximum number of entries before eviction
	 * @throws IllegalArgumentException if maxSize is less than 1
	 */
	public OclLruExpressionCache(int maxSize) {
		if (maxSize < 1) {
			throw new IllegalArgumentException("maxSize must be >= 1, was: " + maxSize);
		}
		this.maxSize = maxSize;
		this.cache = new LinkedHashMap<>(16, 0.75f, true) {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(Map.Entry<String, OclExpression> eldest) {
				return size() > maxSize;
			}
		};
	}

	/**
	 * Creates a new LRU cache with the given maximum size.
	 *
	 * @param maxSize the maximum number of entries, e.g. 1024
	 * @return a new cache instance
	 * @throws IllegalArgumentException if maxSize is less than 1
	 */
	public static OclLruExpressionCache ofSize(int maxSize) {
		return new OclLruExpressionCache(maxSize);
	}

	@Override
	public OclExpression get(String expression, EClassifier contextType) {
		String key = cacheKey(expression, contextType);
		OclExpression result;
		synchronized (cache) {
			result = cache.get(key);
		}
		if (result != null) {
			hits.incrementAndGet();
		} else {
			misses.incrementAndGet();
		}
		return result;
	}

	@Override
	public void put(String expression, EClassifier contextType, OclExpression parsed) {
		Objects.requireNonNull(parsed, "parsed must not be null");
		String key = cacheKey(expression, contextType);
		synchronized (cache) {
			cache.put(key, parsed);
		}
	}

	@Override
	public void invalidate(String expression, EClassifier contextType) {
		String key = cacheKey(expression, contextType);
		synchronized (cache) {
			cache.remove(key);
		}
	}

	@Override
	public void invalidateAll() {
		synchronized (cache) {
			cache.clear();
		}
	}

	@Override
	public long size() {
		synchronized (cache) {
			return cache.size();
		}
	}

	@Override
	public long hitCount() {
		return hits.get();
	}

	@Override
	public long missCount() {
		return misses.get();
	}

	/**
	 * Returns the maximum number of entries this cache can hold.
	 *
	 * @return the max size
	 */
	public int getMaxSize() {
		return maxSize;
	}

	/**
	 * Builds the cache key from expression text and context type.
	 * Uses nsURI to distinguish identically named classifiers from different packages.
	 */
	private static String cacheKey(String expression, EClassifier contextType) {
		EPackage ePackage = contextType.getEPackage();
		String nsURI = ePackage != null ? ePackage.getNsURI() : "";
		return nsURI + "#" + contextType.getName() + "#" + expression;
	}
}
