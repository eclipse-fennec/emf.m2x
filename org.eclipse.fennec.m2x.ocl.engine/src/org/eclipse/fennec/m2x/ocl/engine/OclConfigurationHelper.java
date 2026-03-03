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

import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions.ErrorRecovery;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions.NullHandling;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionParser;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;

/**
 * Maps an {@link OclEngineConfiguration} (OSGi Metatype) to an
 * {@link OclConfiguration} instance.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class OclConfigurationHelper {

	private OclConfigurationHelper() {
		// utility class
	}

	/**
	 * Creates an {@link OclConfiguration} from the given OSGi configuration
	 * annotation, parser, and optional cache.
	 *
	 * @param config the OSGi configuration annotation
	 * @param parser the OCL expression parser
	 * @param cache  the expression cache, or {@code null}
	 * @return the configuration
	 */
	public static OclConfiguration from(OclEngineConfiguration config,
			OclExpressionParser parser, OclExpressionCache cache) {
		return buildCommon(config, parser, cache).build();
	}

	/**
	 * Creates an {@link OclConfiguration} from the given OSGi configuration
	 * annotation, parser, cache, and operation provider.
	 *
	 * @param config            the OSGi configuration annotation
	 * @param parser            the OCL expression parser
	 * @param cache             the expression cache, or {@code null}
	 * @param operationProvider the custom operation provider
	 * @return the configuration
	 */
	public static OclConfiguration from(OclEngineConfiguration config,
			OclExpressionParser parser, OclExpressionCache cache,
			OclOperationProvider operationProvider) {
		return buildCommon(config, parser, cache)
				.addOperationProvider(operationProvider)
				.build();
	}

	private static OclConfiguration.Builder buildCommon(OclEngineConfiguration config,
			OclExpressionParser parser, OclExpressionCache cache) {
		return OclConfiguration.builder(parser)
				.expressionCache(cache)
				.customOperationsEnabled(config.customOperationsEnabled())
				.nullHandling(NullHandling.valueOf(config.nullHandling()))
				.errorRecovery(ErrorRecovery.valueOf(config.errorRecovery()))
				.maxDepth(config.maxDepth())
				.timeoutMs(config.timeout())
				.maxCollectionSize(config.maxCollectionSize())
				.maxClosureIterations(config.maxClosureIterations())
				.maxRegexLength(config.maxRegexLength());
	}
}
