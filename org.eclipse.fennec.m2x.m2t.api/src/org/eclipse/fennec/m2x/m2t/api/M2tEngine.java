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
import java.util.List;

import org.eclipse.fennec.m2x.model.m2t.Module;
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
}
