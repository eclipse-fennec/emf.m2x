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

import java.util.Objects;

import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionParser;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclEngineImpl;

/**
 * Creates {@link OclEngine} instances outside OSGi.
 *
 * <p>In OSGi the engine arrives as a service and Declarative Services wires parser,
 * cache and operation providers; nothing here is needed. On a flat classpath that
 * wiring has to happen by hand, and this factory is where the knowledge lives — the
 * implementation class sits in a private package, so a caller cannot depend on it by
 * accident.
 *
 * <pre>
 * OclEngine engine = OclEngines.create(new OclParserSupport());
 *
 * OclEngine configured = OclEngines.create(OclConfiguration.builder(parser)
 *         .expressionCache(OclLruExpressionCache.ofSize(2048))
 *         .addOperationProvider(myOperations)
 *         .build());
 * </pre>
 *
 * <p>Engines that compose on top of OCL — M2T, QVT-O, QVT-R — take an {@link OclEngine}
 * rather than building one, so the engine they use is the one that was configured,
 * with its cache and its providers.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class OclEngines {

	private OclEngines() {
		// static factory
	}

	/**
	 * Creates an engine from a full configuration.
	 *
	 * @param configuration the configuration, must not be {@code null}
	 * @return a new engine
	 */
	public static OclEngine create(OclConfiguration configuration) {
		Objects.requireNonNull(configuration, "configuration must not be null");
		return new OclEngineImpl(configuration);
	}

	/**
	 * Creates an engine with default options and no expression cache.
	 *
	 * @param parser the parser the engine builds its expressions with, must not be {@code null}
	 * @return a new engine
	 */
	public static OclEngine create(OclExpressionParser parser) {
		Objects.requireNonNull(parser, "parser must not be null");
		return new OclEngineImpl(parser);
	}

	/**
	 * Creates an engine with default options and the given expression cache.
	 *
	 * @param parser the parser the engine builds its expressions with, must not be {@code null}
	 * @param cache the cache parsed expressions are kept in, must not be {@code null}
	 * @return a new engine
	 */
	public static OclEngine create(OclExpressionParser parser, OclExpressionCache cache) {
		Objects.requireNonNull(parser, "parser must not be null");
		Objects.requireNonNull(cache, "cache must not be null");
		return new OclEngineImpl(parser, cache);
	}
}
