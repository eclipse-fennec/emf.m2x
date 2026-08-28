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

import java.util.Objects;

import org.eclipse.emf.common.util.URI;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
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

	// --- Compiling ---

	/**
	 * Compiles a QVT-R transformation from source text into a {@link CompiledUnit}: one document that holds the
	 * parsed script, everything the parser created for it, and a manifest.
	 *
	 * <p>{@code parse()} yields the in-memory graph and is what execution needs; {@code compile()}
	 * yields the storable form. The difference is ownership. A parser creates objects the script
	 * references but that no metamodel feature contains — {@code self}, a type instance for every
	 * {@code Integer}, the default expression of an intermediate class — and in memory that costs
	 * nothing. Saving or copying the result of {@code parse()} fails on exactly those objects. The
	 * compiled unit gives them a home beside the script, so the whole document saves, loads and
	 * copies, and every reference resolves within it (#137).
	 *
	 * <p>The script is reachable as {@link CompiledUnit#getUnit()} and is the same
	 * {@link RelationalTransformation} {@code parse()} would have returned; it can be executed as before.
	 *
	 * @param source the source text
	 * @param unitName the logical unit name — the name the unit is imported by
	 * @return the compiled unit, never {@code null}
	 * @throws QvtdParseException if parsing fails, or if the result cannot be made self-contained
	 * @since 1.0
	 */
	CompiledUnit compile(String source, String unitName) throws QvtdParseException;

	/**
	 * Compiles a QVT-R transformation read from the given URI — see {@link #compile(String, String)}.
	 *
	 * @param transformationUri the URI of the source
	 * @return the compiled unit, never {@code null}
	 * @throws QvtdParseException if the source cannot be read or parsed, or the result cannot be made
	 *             self-contained
	 * @since 1.0
	 */
	CompiledUnit compile(URI transformationUri) throws QvtdParseException;

	/**
	 * Compiles a QVT-R transformation with explicit options — chiefly how its imports are bound.
	 *
	 * <p>Compile resolves every import through the configured unit resolvers, under the same
	 * enable flag and allow-list as execution (D29). What happens with a resolved import is the
	 * {@link UnitCompileOptions#dependencyMode() dependency mode}. QVT-R binds an import by
	 * merging the imported relations into the importing transformation (§7.11.1.1): under
	 * {@code embed} that merge happens at compile time, the imported rules travel inside the unit
	 * and the import is struck from the transformation, which then runs without any resolver.
	 * Under {@code pin} the manifest records the name with the unit fingerprint of the imported
	 * transformation, under {@code rebind} the name alone; in both the import stays declared and
	 * is merged when the unit is prepared or executed. A blackbox query — one declared without a
	 * body — becomes a manifest requirement naming the operation and its signature. An import
	 * nobody can resolve fails the compile in every mode.
	 *
	 * <p>{@link #compile(String, String)} is this method with {@link UnitCompileOptions#defaults()}.
	 *
	 * @param source the source text
	 * @param unitName the logical unit name — the name the unit is imported by
	 * @param options how to bind the dependencies
	 * @return the compiled unit, never {@code null}
	 * @throws QvtdParseException if parsing fails, an import cannot be resolved, imports form a
	 *             cycle (under {@code embed} and {@code pin}, where the dependency is followed),
	 *             or the result cannot be made self-contained
	 * @since 1.0
	 */
	CompiledUnit compile(String source, String unitName, UnitCompileOptions options)
			throws QvtdParseException;

	/**
	 * Compiles a QVT-R transformation read from the given URI with explicit options — see
	 * {@link #compile(String, String, UnitCompileOptions)}.
	 *
	 * @param transformationUri the URI of the source
	 * @param options how to bind the dependencies
	 * @return the compiled unit, never {@code null}
	 * @throws QvtdParseException if the source cannot be read or compiled
	 * @since 1.0
	 */
	CompiledUnit compile(URI transformationUri, UnitCompileOptions options) throws QvtdParseException;

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

	/**
	 * Registers a {@link RelationImplementationProvider} for hybrid QVT-O ↔ QVT-R
	 * execution (D39).
	 *
	 * <p>When a QVT-R relation has an {@code implementedby} clause, the engine
	 * queries registered providers via {@link RelationImplementationProvider#canProvide}
	 * before falling back to the blackbox registry.
	 *
	 * @param provider the provider to register, must not be {@code null}
	 */
	default void registerImplementationProvider(RelationImplementationProvider provider) {
		Objects.requireNonNull(provider, "provider must not be null");
		// Default no-op for backward compatibility
	}

	/**
	 * Returns the OCL engine this engine evaluates expressions with.
	 *
	 * <p>Either the engine that was supplied through the configuration, or the one built
	 * from it. Callers use this to warm it up, inspect its cache, or install EMF
	 * delegates on the very engine that QVT-R runs on.
	 *
	 * @return the OCL engine, never {@code null}
	 */
	OclEngine getOclEngine();
}
