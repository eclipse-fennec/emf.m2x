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
package org.eclipse.fennec.m2m.ocl.api;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
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
	 * Removes a single entry from the cache.
	 *
	 * @param expression the OCL expression text
	 * @param contextType the context classifier
	 */
	void invalidate(String expression, EClassifier contextType);

	/**
	 * Removes all entries from the cache.
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
