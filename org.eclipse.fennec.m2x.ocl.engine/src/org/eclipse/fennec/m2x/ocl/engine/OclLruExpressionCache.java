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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclCacheKey;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;
import org.eclipse.fennec.m2x.ocl.api.OclParseResult;
import org.eclipse.fennec.m2x.ocl.api.OclParseResult.Failed;
import org.eclipse.fennec.m2x.ocl.api.OclParseResult.Parsed;

/**
 * LRU (Least Recently Used) implementation of {@link OclExpressionCache}.
 *
 * <p>Based on {@link LinkedHashMap} with {@code accessOrder=true} and a
 * synchronized wrapper. All map operations are synchronized; hit/miss
 * counters use {@link AtomicLong} for lock-free reads.
 *
 * <p>Keeps parses with declared variables (keyed by the variables' names and types as
 * well) and parse failures. Failures live in a second LRU with a limit of their own, so a
 * flood of broken expressions evicts only other failures, never a parse that works.
 * {@link #size()} counts both.
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

	/** Failures kept when no limit is given: 256 entries. */
	public static final int DEFAULT_MAX_FAILURES = 256;

	private final int maxSize;
	private final int maxFailures;
	private final Map<String, OclExpression> cache;
	private final Map<String, Failed> failures;
	private final AtomicLong hits = new AtomicLong();
	private final AtomicLong misses = new AtomicLong();

	/**
	 * Creates a new LRU cache with the given maximum size and {@link #DEFAULT_MAX_FAILURES}
	 * failures.
	 *
	 * @param maxSize the maximum number of entries before eviction
	 * @throws IllegalArgumentException if maxSize is less than 1
	 */
	public OclLruExpressionCache(int maxSize) {
		this(maxSize, DEFAULT_MAX_FAILURES);
	}

	/**
	 * Creates a new LRU cache with the given maximum sizes.
	 *
	 * @param maxSize the maximum number of parsed expressions before eviction
	 * @param maxFailures the maximum number of parse failures before eviction; {@code 0}
	 *        keeps none
	 * @throws IllegalArgumentException if maxSize is less than 1 or maxFailures is negative
	 */
	public OclLruExpressionCache(int maxSize, int maxFailures) {
		if (maxSize < 1) {
			throw new IllegalArgumentException("maxSize must be >= 1, was: " + maxSize);
		}
		if (maxFailures < 0) {
			throw new IllegalArgumentException("maxFailures must be >= 0, was: " + maxFailures);
		}
		this.maxSize = maxSize;
		this.maxFailures = maxFailures;
		this.cache = boundedMap(maxSize);
		this.failures = boundedMap(maxFailures);
	}

	/**
	 * Creates a new LRU cache with the given maximum size and {@link #DEFAULT_MAX_FAILURES}
	 * failures.
	 *
	 * @param maxSize the maximum number of entries, e.g. 1024
	 * @return a new cache instance
	 * @throws IllegalArgumentException if maxSize is less than 1
	 */
	public static OclLruExpressionCache ofSize(int maxSize) {
		return new OclLruExpressionCache(maxSize);
	}

	/**
	 * Creates a new LRU cache with the given maximum sizes.
	 *
	 * @param maxSize the maximum number of parsed expressions, e.g. 1024
	 * @param maxFailures the maximum number of parse failures, e.g. 256; {@code 0} keeps none
	 * @return a new cache instance
	 * @throws IllegalArgumentException if maxSize is less than 1 or maxFailures is negative
	 */
	public static OclLruExpressionCache ofSize(int maxSize, int maxFailures) {
		return new OclLruExpressionCache(maxSize, maxFailures);
	}

	@Override
	public OclExpression get(String expression, EClassifier contextType) {
		return lookup(OclCacheKey.of(expression, contextType))
				.filter(Parsed.class::isInstance)
				.map(result -> ((Parsed) result).expression())
				.orElse(null);
	}

	@Override
	public void put(String expression, EClassifier contextType, OclExpression parsed) {
		Objects.requireNonNull(parsed, "parsed must not be null");
		store(OclCacheKey.of(expression, contextType), new Parsed(parsed));
	}

	@Override
	public void invalidate(String expression, EClassifier contextType) {
		invalidate(OclCacheKey.of(expression, contextType));
	}

	@Override
	public Optional<OclParseResult> lookup(OclCacheKey key) {
		String cacheKey = cacheKey(key);
		OclParseResult result;
		synchronized (cache) {
			OclExpression parsed = cache.get(cacheKey);
			result = parsed != null ? new Parsed(parsed) : failures.get(cacheKey);
		}
		if (result != null) {
			hits.incrementAndGet();
		} else {
			misses.incrementAndGet();
		}
		return Optional.ofNullable(result);
	}

	@Override
	public void store(OclCacheKey key, OclParseResult result) {
		Objects.requireNonNull(result, "result must not be null");
		String cacheKey = cacheKey(key);
		synchronized (cache) {
			switch (result) {
				case Parsed parsed -> {
					failures.remove(cacheKey);
					cache.put(cacheKey, parsed.expression());
				}
				case Failed failed -> {
					cache.remove(cacheKey);
					if (maxFailures > 0) {
						failures.put(cacheKey, failed);
					}
				}
			}
		}
	}

	@Override
	public void invalidate(OclCacheKey key) {
		String cacheKey = cacheKey(key);
		synchronized (cache) {
			cache.remove(cacheKey);
			failures.remove(cacheKey);
		}
	}

	@Override
	public void invalidateAll() {
		synchronized (cache) {
			cache.clear();
			failures.clear();
		}
	}

	/**
	 * Returns the number of parsed expressions plus the number of parse failures.
	 *
	 * @return the cache size
	 */
	@Override
	public long size() {
		synchronized (cache) {
			return (long) cache.size() + failures.size();
		}
	}

	/**
	 * Returns the number of parse failures currently kept.
	 *
	 * @return the number of failures
	 */
	public long failureCount() {
		synchronized (cache) {
			return failures.size();
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
	 * Returns the configured maximum number of parsed expressions.
	 *
	 * @return the maximum cache size
	 */
	public int getMaxSize() {
		return maxSize;
	}

	/**
	 * Returns the configured maximum number of parse failures.
	 *
	 * @return the maximum number of failures
	 */
	public int getMaxFailures() {
		return maxFailures;
	}

	private static <V> Map<String, V> boundedMap(int limit) {
		return new LinkedHashMap<>(16, 0.75f, true) {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(Map.Entry<String, V> eldest) {
				return size() > limit;
			}
		};
	}

	private static String cacheKey(OclCacheKey key) {
		EClassifier contextType = key.contextType();
		EPackage ePackage = contextType.getEPackage();
		String nsURI = ePackage != null ? ePackage.getNsURI() : "";
		String variables = key.hasVariables() ? "(" + key.variablesText() + ")" : "";
		return nsURI + "#" + contextType.getName() + variables + "#" + key.expression();
	}
}
