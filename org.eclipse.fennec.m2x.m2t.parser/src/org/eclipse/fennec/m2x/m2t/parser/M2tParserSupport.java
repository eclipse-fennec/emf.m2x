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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;

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

	private void checkErrors(M2tErrorListener errorListener, String input)
			throws M2tParseException {
		if (errorListener.hasErrors()) {
			List<Resource.Diagnostic> errors = errorListener.getErrors();
			String message = "M2T parse error: " + errors.get(0).getMessage();
			throw new M2tParseException(message, errors);
		}
	}
}
