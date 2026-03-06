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
package org.eclipse.fennec.m2x.qvtd.api;

import org.eclipse.emf.common.util.URI;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.osgi.annotation.versioning.ProviderType;

/**
 * Main facade for parsing and executing QVT-R (Relations) transformations.
 *
 * <p>Obtain an instance via OSGi service lookup or programmatic construction
 * from the engine bundle. The engine is configured via {@link QvtdConfiguration}.
 *
 * <p>Usage:
 * <pre>
 * RelationalTransformation t = engine.parse(uri);
 * QvtdExecutionResult result = engine.execute(t, context);
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ProviderType
public interface QvtdEngine {

	/**
	 * Parses a QVT-R transformation from the given URI.
	 *
	 * @param transformationUri the URI of the transformation source
	 * @return the parsed relational transformation
	 * @throws QvtdParseException if the source contains syntax or semantic errors
	 */
	RelationalTransformation parse(URI transformationUri) throws QvtdParseException;

	/**
	 * Parses a QVT-R transformation from the given source string.
	 *
	 * @param source the transformation source text
	 * @param unitName a logical unit name for error reporting
	 * @return the parsed relational transformation
	 * @throws QvtdParseException if the source contains syntax or semantic errors
	 */
	RelationalTransformation parse(String source, String unitName) throws QvtdParseException;

	/**
	 * Executes a parsed QVT-R transformation in the given context.
	 *
	 * <p>The execution direction is determined by
	 * {@link QvtdExecutionContext#targetModelName()} (enforce mode) or runs in
	 * check-only mode when {@link QvtdExecutionContext#checkOnly()} is {@code true}.
	 *
	 * @param transformation the parsed transformation to execute
	 * @param context the execution context with model extents and direction
	 * @return the execution result with diagnostics
	 * @throws QvtdExecutionException if a fatal runtime error occurs
	 */
	QvtdExecutionResult execute(RelationalTransformation transformation,
			QvtdExecutionContext context);
}
