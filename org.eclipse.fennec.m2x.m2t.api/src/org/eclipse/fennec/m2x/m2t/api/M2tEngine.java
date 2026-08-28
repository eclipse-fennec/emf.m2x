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
package org.eclipse.fennec.m2x.m2t.api;

import org.eclipse.emf.common.util.URI;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import java.util.List;

import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.osgi.annotation.versioning.ProviderType;

/**
 * Main facade for parsing and executing MOFM2T v1.0 templates.
 *
 * <p>Implementations must be usable as plain Java objects (OSGi-optional).
 * Typical standalone usage:
 * <pre>
 * OclConfiguration oclConfig = OclConfiguration.builder(parser).build();
 * M2tConfiguration config = M2tConfiguration.builder(oclConfig).build();
 * M2tEngine engine = new M2tEngineImpl(config);
 *
 * Module module = engine.parse(uri);
 * M2tResult result = engine.execute(module, M2tContext.of(inputExtent));
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ProviderType
public interface M2tEngine {

	// --- Parsing ---

	/**
	 * Parses a MOFM2T module from the given URI.
	 *
	 * @param moduleUri the URI of the template source
	 * @return the parsed module AST
	 * @throws M2tParseException if parsing fails
	 */
	Module parse(URI moduleUri) throws M2tParseException;

	/**
	 * Parses a MOFM2T module from source text.
	 *
	 * @param source the template source text
	 * @param unitName the logical unit name (for diagnostics)
	 * @return the parsed module AST
	 * @throws M2tParseException if parsing fails
	 */
	Module parse(String source, String unitName) throws M2tParseException;

	// --- Compiling ---

	/**
	 * Compiles a MOFM2T module from source text into a {@link CompiledUnit}: one document that holds the
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
	 * {@link Module} {@code parse()} would have returned; it can be executed as before.
	 *
	 * @param source the source text
	 * @param unitName the logical unit name — the name the unit is imported by
	 * @return the compiled unit, never {@code null}
	 * @throws M2tParseException if parsing fails, or if the result cannot be made self-contained
	 * @since 1.0
	 */
	CompiledUnit compile(String source, String unitName) throws M2tParseException;

	/**
	 * Compiles a MOFM2T module read from the given URI — see {@link #compile(String, String)}.
	 *
	 * @param moduleUri the URI of the source
	 * @return the compiled unit, never {@code null}
	 * @throws M2tParseException if the source cannot be read or parsed, or the result cannot be made
	 *             self-contained
	 * @since 1.0
	 */
	CompiledUnit compile(URI moduleUri) throws M2tParseException;

	/**
	 * Compiles a MOFM2T module with explicit options — chiefly how its dependencies are bound.
	 *
	 * <p>A module depends on what it {@code extends} and {@code imports}. Compile resolves every
	 * such name through the configured unit resolvers, under the same enable flag and allow-list
	 * as generation (D29), and binds according to the {@link UnitCompileOptions#dependencyMode()
	 * dependency mode}: under {@code embed} the dependency is compiled in turn and carried inside
	 * the unit, which is linked against it and then generates without any resolver; under
	 * {@code pin} the manifest records the name with the dependency's unit fingerprint, under
	 * {@code rebind} the name alone, and the module stays unbound — with what it needs to be bound
	 * later kept on it — until it is prepared or linked. A name nobody can resolve fails the
	 * compile in every mode.
	 *
	 * <p>{@link #compile(String, String)} is this method with {@link UnitCompileOptions#defaults()}.
	 *
	 * @param source the source text
	 * @param unitName the logical unit name — the name the unit is imported by
	 * @param options how to bind the dependencies
	 * @return the compiled unit, never {@code null}
	 * @throws M2tParseException if parsing fails, a dependency cannot be resolved, dependencies
	 *             form a cycle (under {@code embed} and {@code pin}, where the dependency is
	 *             followed), or the result cannot be made self-contained
	 * @since 1.0
	 */
	CompiledUnit compile(String source, String unitName, UnitCompileOptions options)
			throws M2tParseException;

	/**
	 * Compiles a MOFM2T module read from the given URI with explicit options — see
	 * {@link #compile(String, String, UnitCompileOptions)}.
	 *
	 * @param moduleUri the URI of the source
	 * @param options how to bind the dependencies
	 * @return the compiled unit, never {@code null}
	 * @throws M2tParseException if the source cannot be read or compiled
	 * @since 1.0
	 */
	CompiledUnit compile(URI moduleUri, UnitCompileOptions options) throws M2tParseException;

	// --- Execution ---

	/**
	 * Executes a module with the given context.
	 *
	 * <p>The module's main template is located and invoked with the input
	 * model elements. Generated text is directed to the configured
	 * {@link M2tGenerationStrategy}.
	 *
	 * @param module the parsed module
	 * @param context the execution context with input model(s)
	 * @return the execution result with diagnostics and generated files
	 */
	M2tResult execute(Module module, M2tContext context);

	// --- Linking ---

	/**
	 * Links multiple parsed modules, resolving cross-module references
	 * (extends, imports, overrides, invocations).
	 *
	 * <p>Call this after parsing all modules and before executing.
	 * For single-module scenarios, linking happens automatically during
	 * {@link #execute(Module, M2tContext)}.
	 *
	 * @param modules the modules to link together
	 * @return warnings encountered during linking (empty if all references resolved)
	 */
	List<String> link(Module... modules);

	// --- Retention ---

	/**
	 * Drops everything this engine remembers about the given module.
	 *
	 * <p>The engine caches a module's parse result, its link and normalization state and
	 * the indentation of its template invocations, so that repeated executions do not
	 * re-parse and re-link. Those caches are keyed weakly and let go once the caller
	 * drops the module, but garbage collection is not a schedule: an engine that lives
	 * as long as the application and parses modules in a loop should say when it is done
	 * with one.
	 *
	 * @param module the module to forget, must not be {@code null}
	 */
	void release(Module module);

	/**
	 * Drops everything this engine remembers about every module.
	 *
	 * <p>After this call, previously parsed modules have to be parsed and linked again.
	 */
	void clearCaches();

	/**
	 * Returns the OCL engine this engine evaluates template expressions with.
	 *
	 * <p>Either the engine that was supplied through the configuration, or the one built
	 * from it. Callers use this to warm it up, inspect its cache, or install EMF delegates
	 * on the very engine that M2T runs on.
	 *
	 * @return the OCL engine, never {@code null}
	 */
	OclEngine getOclEngine();
}
