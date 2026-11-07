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
package org.eclipse.fennec.m2x.ocl.api;

import java.util.Optional;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclParseResult.Parsed;
import org.osgi.annotation.versioning.ProviderType;

/**
 * Cache for parsed OCL expression ASTs, keyed by expression text and context type.
 *
 * <p>Implementations must be thread-safe. A cache instance can be shared across
 * multiple {@link OclEngine} instances to avoid redundant parsing of the same
 * expression.
 *
 * <p>The cache key is derived from the expression text, the context type name,
 * and the containing package's namespace URI, so identically named classifiers
 * from different packages are cached separately.
 *
 * <p>The engine works through {@link #lookup(OclCacheKey)} and
 * {@link #store(OclCacheKey, OclParseResult)}, which also cover parses with declared
 * variables and parse failures (a negative cache: a text that does not parse is not parsed
 * again on every call). Their default implementations map onto {@link #get}/{@link #put}
 * and cache only successful parses without variables, so an implementation written against
 * the original methods keeps working unchanged; it just does not remember failures or
 * parses with variables. An implementation that does keep failures should bound them
 * separately, so that a flood of broken expressions cannot displace the parses that work.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ProviderType
public interface OclExpressionCache {

	/**
	 * Retrieves a cached expression, or {@code null} if not present.
	 *
	 * @param expression the OCL expression text
	 * @param contextType the context classifier
	 * @return the cached AST, or {@code null} on cache miss
	 */
	OclExpression get(String expression, EClassifier contextType);

	/**
	 * Stores a parsed expression in the cache.
	 *
	 * @param expression the OCL expression text
	 * @param contextType the context classifier
	 * @param parsed the parsed AST to cache
	 */
	void put(String expression, EClassifier contextType, OclExpression parsed);

	/**
	 * Removes a single entry from the cache, a remembered failure included.
	 *
	 * @param expression the OCL expression text
	 * @param contextType the context classifier
	 */
	void invalidate(String expression, EClassifier contextType);

	/**
	 * Looks up the outcome of an earlier parse: the AST, or the failure when the text did not
	 * parse.
	 *
	 * <p>The default delegates to {@link #get(String, EClassifier)} for keys without
	 * variables and reports a miss for all others.
	 *
	 * @param key what the outcome is cached under
	 * @return the cached outcome, or empty on a cache miss
	 */
	default Optional<OclParseResult> lookup(OclCacheKey key) {
		if (key.hasVariables()) {
			return Optional.empty();
		}
		return Optional.ofNullable(get(key.expression(), key.contextType())).map(Parsed::new);
	}

	/**
	 * Stores the outcome of a parse, success or failure.
	 *
	 * <p>The default delegates successful parses without variables to
	 * {@link #put(String, EClassifier, OclExpression)} and ignores everything else.
	 *
	 * @param key what the outcome is cached under
	 * @param result the outcome of the parse
	 */
	default void store(OclCacheKey key, OclParseResult result) {
		if (!key.hasVariables() && result instanceof Parsed parsed) {
			put(key.expression(), key.contextType(), parsed.expression());
		}
	}

	/**
	 * Removes the entry for a key, success or failure.
	 *
	 * <p>The default delegates keys without variables to
	 * {@link #invalidate(String, EClassifier)} and ignores all others.
	 *
	 * @param key what the entry is cached under
	 */
	default void invalidate(OclCacheKey key) {
		if (!key.hasVariables()) {
			invalidate(key.expression(), key.contextType());
		}
	}

	/**
	 * Removes all entries from the cache, failures included.
	 */
	void invalidateAll();

	/**
	 * Returns the number of entries currently in the cache.
	 *
	 * @return the cache size
	 */
	long size();

	/**
	 * Returns the number of cache hits since creation.
	 *
	 * @return the hit count
	 */
	long hitCount();

	/**
	 * Returns the number of cache misses since creation.
	 *
	 * @return the miss count
	 */
	long missCount();
}
