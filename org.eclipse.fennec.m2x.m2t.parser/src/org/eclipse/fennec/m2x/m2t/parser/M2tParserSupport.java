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
package org.eclipse.fennec.m2x.m2t.parser;

import java.util.List;
import java.util.Objects;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.model.m2t.Module;

/**
 * Entry point for MOFM2T template parsing. Parses a MOFM2T template source
 * string into an ANTLR4 parse tree (CST).
 *
 * <p>The resulting {@link M2tParser.ModuleContext} can be traversed using
 * the generated {@link M2tParserVisitor} to build the EMF AST (M2T metamodel).
 *
 * <p>This class is designed for use as a plain Java object (OSGi-optional):
 * <pre>
 * M2tParserSupport support = new M2tParserSupport();
 * M2tParser.ModuleContext cst = support.parse(templateSource);
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class M2tParserSupport {

	static {
		// Ecore registers itself in the global registry when its package class initializes,
		// and nothing guarantees that happened before the first parse (see QvtoParserSupport).
		EcorePackage.eINSTANCE.getNsURI();
	}


	/**
	 * Parses a MOFM2T template source into a parse tree.
	 *
	 * @param source the MOFM2T template source text, must not be {@code null}
	 * @return the parse tree root (module context)
	 * @throws M2tParseException if the source contains syntax errors
	 */
	public M2tParser.ModuleContext parse(String source) throws M2tParseException {
		Objects.requireNonNull(source, "source must not be null");

		M2tLexer lexer = new M2tLexer(CharStreams.fromString(source));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		M2tParser parser = new M2tParser(tokens);

		M2tErrorListener errorListener = configureErrorHandling(lexer, parser);

		M2tParser.ModuleContext tree = parser.module();

		checkErrors(errorListener, source);

		return tree;
	}

	private M2tErrorListener configureErrorHandling(M2tLexer lexer, M2tParser parser) {
		M2tErrorListener errorListener = new M2tErrorListener();

		lexer.removeErrorListeners();
		lexer.addErrorListener(errorListener);

		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);

		return errorListener;
	}

	/**
	 * Parses a MOFM2T template source into an EMF Module AST.
	 *
	 * @param source the MOFM2T template source text, must not be {@code null}
	 * @param unitName the logical unit name for diagnostics
	 * @return the parsed Module
	 * @throws M2tParseException if the source contains syntax errors
	 */
	public Module buildModule(String source, String unitName) throws M2tParseException {
		return buildModule(source, unitName, EcorePackage.eINSTANCE.getEObject(), null);
	}

	/**
	 * Parses a MOFM2T template source into an EMF Module AST with the given context.
	 *
	 * @param source the MOFM2T template source text
	 * @param unitName the logical unit name
	 * @param contextType the default context type for OCL expressions
	 * @param packageRegistry optional package registry for type resolution
	 * @return the parsed Module
	 * @throws M2tParseException if the source contains syntax errors
	 */
	public Module buildModule(String source, String unitName, EClassifier contextType,
			EPackage.Registry packageRegistry) throws M2tParseException {
		Objects.requireNonNull(unitName, "unitName must not be null");
		Objects.requireNonNull(contextType, "contextType must not be null");
		M2tParser.ModuleContext cst = parse(source);
		M2tModuleBuilder builder = new M2tModuleBuilder(unitName, contextType, packageRegistry);
		Module module = builder.visitModule(cst);
		checkResolutionErrors(builder.getDiagnostics());
		return module;
	}

	/**
	 * Parses a MOFM2T template source into a {@link M2tParseResult} containing
	 * the Module AST plus unresolved name references for linking.
	 *
	 * @param source the MOFM2T template source text
	 * @param unitName the logical unit name
	 * @return the parse result with pending references
	 * @throws M2tParseException if the source contains syntax errors
	 */
	public M2tParseResult buildModuleWithPending(String source, String unitName)
			throws M2tParseException {
		return buildModuleWithPending(source, unitName,
				EcorePackage.eINSTANCE.getEObject(), null);
	}

	/**
	 * Parses a MOFM2T template source into a {@link M2tParseResult} with
	 * the given context type and package registry.
	 *
	 * @param source the MOFM2T template source text
	 * @param unitName the logical unit name
	 * @param contextType the default context type for OCL expressions
	 * @param packageRegistry optional package registry for type resolution
	 * @return the parse result with pending references
	 * @throws M2tParseException if the source contains syntax errors
	 */
	public M2tParseResult buildModuleWithPending(String source, String unitName,
			EClassifier contextType, EPackage.Registry packageRegistry)
			throws M2tParseException {
		Objects.requireNonNull(unitName, "unitName must not be null");
		Objects.requireNonNull(contextType, "contextType must not be null");
		M2tParser.ModuleContext cst = parse(source);
		M2tModuleBuilder builder = new M2tModuleBuilder(unitName, contextType, packageRegistry);
		builder.visitModule(cst);
		checkResolutionErrors(builder.getDiagnostics());
		return builder.getParseResult();
	}

	/**
	 * Rejects a module whose type names could not be resolved (#66).
	 *
	 * <p>Collected rather than thrown on first sight, so a template with several unknown
	 * names reports all of them at once — the contract syntax errors already have.
	 *
	 * @param diagnostics the diagnostics collected while building
	 * @throws M2tParseException if any were collected
	 */
	private void checkResolutionErrors(List<Resource.Diagnostic> diagnostics)
			throws M2tParseException {
		if (!diagnostics.isEmpty()) {
			throw new M2tParseException("M2T resolution error: "
					+ diagnostics.get(0).getMessage(), List.copyOf(diagnostics));
		}
	}

	private void checkErrors(M2tErrorListener errorListener, String input)
			throws M2tParseException {
		if (errorListener.hasErrors()) {
			List<Resource.Diagnostic> errors = errorListener.getErrors();
			String message = "M2T parse error: " + errors.get(0).getMessage();
			throw new M2tParseException(message, errors);
		}
	}
}
