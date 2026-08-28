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
package org.eclipse.fennec.m2x.qvto.api;

import org.eclipse.emf.common.util.URI;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;

import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.UnitBinder;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
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

	// --- Compiling ---

	/**
	 * Compiles a QVT-O transformation from source text into a {@link CompiledUnit}: one document that holds the
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
	 * {@link OperationalTransformation} {@code parse()} would have returned; it can be executed as before.
	 *
	 * @param source the source text
	 * @param unitName the logical unit name — the name the unit is imported by
	 * @return the compiled unit, never {@code null}
	 * @throws QvtoParseException if parsing fails, or if the result cannot be made self-contained
	 * @since 1.0
	 */
	CompiledUnit compile(String source, String unitName) throws QvtoParseException;

	/**
	 * Compiles a QVT-O transformation read from the given URI — see {@link #compile(String, String)}.
	 *
	 * @param transformationUri the URI of the source
	 * @return the compiled unit, never {@code null}
	 * @throws QvtoParseException if the source cannot be read or parsed, or the result cannot be made
	 *             self-contained
	 * @since 1.0
	 */
	CompiledUnit compile(URI transformationUri) throws QvtoParseException;

	/**
	 * Compiles a QVT-O transformation with explicit options — chiefly how its imports are bound.
	 *
	 * <p>Compile resolves every import through the configured unit resolvers and the blackbox
	 * registry, under the same enable flags and allow-lists as execution (D29). What happens with
	 * a resolved import is the {@link UnitCompileOptions#dependencyMode() dependency mode}:
	 * under {@code embed} the dependency is compiled in turn and carried inside the unit, which
	 * then runs without any resolver; under {@code pin} the manifest records its name and unit
	 * fingerprint; under {@code rebind} the name alone. Under {@code pin} and {@code rebind} the
	 * import stays unbound in the AST — a stub, contained as a satellite — and is bound when the
	 * unit is prepared or executed. A blackbox import is never embedded: its declaration becomes
	 * a manifest requirement, its implementation stays with the runtime. An import nobody can
	 * resolve fails the compile in every mode.
	 *
	 * <p>{@link #compile(String, String)} is this method with {@link UnitCompileOptions#defaults()}.
	 *
	 * @param source the source text
	 * @param unitName the logical unit name — the name the unit is imported by
	 * @param options how to bind the dependencies
	 * @return the compiled unit, never {@code null}
	 * @throws QvtoParseException if parsing fails, an import cannot be resolved, imports form a
	 *             cycle (under {@code embed} and {@code pin}, where the dependency is followed),
	 *             or the result cannot be made self-contained
	 * @since 1.0
	 */
	CompiledUnit compile(String source, String unitName, UnitCompileOptions options)
			throws QvtoParseException;

	/**
	 * Compiles a QVT-O transformation read from the given URI with explicit options — see
	 * {@link #compile(String, String, UnitCompileOptions)}.
	 *
	 * @param transformationUri the URI of the source
	 * @param options how to bind the dependencies
	 * @return the compiled unit, never {@code null}
	 * @throws QvtoParseException if the source cannot be read or compiled
	 * @since 1.0
	 */
	CompiledUnit compile(URI transformationUri, UnitCompileOptions options) throws QvtoParseException;

	// --- Execution ---

	/**
	 * Executes a transformation with the given context.
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

	/**
	 * Executes a prepared unit — the Execute phase proper: nothing is linked, no resolver and no
	 * registry is consulted, the unit runs as the prepare run bound it (#140).
	 *
	 * @param prepared the context a {@code UnitPreparer} produced, with this engine's
	 *            {@link #unitBinder()} among its binders
	 * @param qualifiedName the unit to run, one the context holds
	 * @param context the model extents and configuration properties
	 * @return the result, never {@code null}
	 * @throws IllegalArgumentException if the context does not hold the unit, or holds it with an
	 *             import still unbound
	 * @since 1.0
	 */
	QvtoExecutionResult execute(PreparedContext prepared, String qualifiedName, QvtoExecutionContext context);

	/**
	 * Executes a prepared unit with evaluation options — see
	 * {@link #execute(PreparedContext, String, QvtoExecutionContext)}.
	 *
	 * @param prepared the prepared context
	 * @param qualifiedName the unit to run
	 * @param context the model extents and configuration properties
	 * @param options the evaluation options
	 * @return the result, never {@code null}
	 * @since 1.0
	 */
	QvtoExecutionResult execute(PreparedContext prepared, String qualifiedName, QvtoExecutionContext context,
			QvtoEvaluationOptions options);

	/**
	 * Returns the binder a {@code UnitPreparer} needs to bind QVT-O units with this engine's
	 * blackbox registry and limits (D29).
	 *
	 * @return the binder, never {@code null}
	 * @since 1.0
	 */
	UnitBinder unitBinder();


	/**
	 * Loads a transformation so that its mappings can serve relation implementations
	 * (§7.8, D39 hybrid QVT-R / QVT-O).
	 *
	 * <p>The engine acts as a {@code RelationImplementationProvider} for a QVT-R engine
	 * once a transformation is loaded. Under OSGi that role arrives as its own service
	 * registration; in plain Java the engine created by {@code QvtoEngines} implements
	 * it and can be handed to {@code QvtdEngine.registerImplementationProvider}.
	 *
	 * @param transformation the transformation whose mappings implement relations
	 */
	void loadTransformation(OperationalTransformation transformation);

	/**
	 * Returns the OCL engine this engine evaluates expressions with.
	 *
	 * <p>Either the engine that was supplied through the configuration, or the one built
	 * from it. Callers use this to warm it up, inspect its cache, or install EMF
	 * delegates on the very engine that QVT-O runs on.
	 *
	 * @return the OCL engine, never {@code null}
	 */
	OclEngine getOclEngine();
}
