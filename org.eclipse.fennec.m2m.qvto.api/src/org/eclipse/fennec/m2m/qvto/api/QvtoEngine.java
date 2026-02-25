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
package org.eclipse.fennec.m2m.qvto.api;

import java.net.URI;

import org.eclipse.fennec.m2m.model.qvtoperational.OperationalTransformation;
import org.osgi.annotation.versioning.ProviderType;

/**
 * Main facade for parsing and executing QVT Operational transformations.
 *
 * <p>Implementations must be usable as plain Java objects (OSGi-optional).
 * Typical standalone usage:
 * <pre>
 * OclConfiguration oclConfig = OclConfiguration.builder(parser).build();
 * QvtoConfiguration config = QvtoConfiguration.builder(oclConfig).build();
 * QvtoEngine engine = new QvtoEngineImpl(config);
 *
 * OperationalTransformation t = engine.parse(uri);
 * QvtoExecutionResult result = engine.execute(t, QvtoExecutionContext.of(inExtent, outExtent));
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ProviderType
public interface QvtoEngine {

	// --- Parsing ---

	/**
	 * Parses a QVT-O transformation from the given URI.
	 *
	 * @param transformationUri the URI of the transformation source
	 * @return the parsed transformation AST
	 * @throws QvtoParseException if parsing fails
	 */
	OperationalTransformation parse(URI transformationUri) throws QvtoParseException;

	/**
	 * Parses a QVT-O transformation from source text.
	 *
	 * @param source the transformation source text
	 * @param unitName the logical unit name (for diagnostics)
	 * @return the parsed transformation AST
	 * @throws QvtoParseException if parsing fails
	 */
	OperationalTransformation parse(String source, String unitName) throws QvtoParseException;

	// --- Execution ---

	/**
	 * Executes a transformation with the given context.
	 *
	 * <p>The transformation can originate from different sources:
	 * <ul>
	 *   <li>{@link #parse(URI)} or {@link #parse(String, String)} — parsed from QVT-O syntax</li>
	 *   <li>Programmatically built via the EMF API against the QVT-O metamodel
	 *       ({@code qvtoperational.ecore}), e.g. from a UI editor or model transformation</li>
	 * </ul>
	 *
	 * <p>Example (programmatic, no parser required):
	 * <pre>
	 * OperationalTransformation t = QVTOFactory.eINSTANCE.createOperationalTransformation();
	 * t.setName("MyTransformation");
	 * // ... build AST programmatically ...
	 * QvtoExecutionResult result = engine.execute(t, context);
	 * </pre>
	 *
	 * @param transformation the transformation (parsed or programmatically built)
	 * @param context the execution context with model extents and properties
	 * @return the execution result with diagnostics and optional trace
	 */
	QvtoExecutionResult execute(OperationalTransformation transformation,
			QvtoExecutionContext context);

	/**
	 * Executes a parsed transformation with the given context and options.
	 *
	 * @param transformation the parsed transformation
	 * @param context the execution context with model extents and properties
	 * @param options evaluation options (stack depth, timeout, tracing)
	 * @return the execution result with diagnostics and optional trace
	 */
	QvtoExecutionResult execute(OperationalTransformation transformation,
			QvtoExecutionContext context, QvtoEvaluationOptions options);

	// --- Extension Registration (standalone mode) ---

	/**
	 * Registers a blackbox library for use in transformations.
	 *
	 * @param library the blackbox library to register
	 */
	void registerBlackbox(QvtoBlackboxLibrary library);

	/**
	 * Unregisters a previously registered blackbox library.
	 *
	 * @param library the blackbox library to unregister
	 */
	void unregisterBlackbox(QvtoBlackboxLibrary library);

	/**
	 * Registers a unit resolver for resolving imported units.
	 *
	 * @param resolver the unit resolver to register
	 */
	void registerUnitResolver(QvtoUnitResolver resolver);

	/**
	 * Unregisters a previously registered unit resolver.
	 *
	 * @param resolver the unit resolver to unregister
	 */
	void unregisterUnitResolver(QvtoUnitResolver resolver);
}
