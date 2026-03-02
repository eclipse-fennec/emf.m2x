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
package org.eclipse.fennec.m2x.ocl.parser;

import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.fennec.m2x.model.ocl.Constraint;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionParser;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * Entry point for OCL parsing. Provides methods to parse single expressions
 * and Complete OCL documents into EMF AST nodes.
 *
 * <p>Implements the {@link OclExpressionParser} interface from {@code ocl.api},
 * allowing the engine to depend on the parser abstraction rather than the concrete class.
 *
 * <p>This class is designed for use by the {@code OclEngine} implementation
 * and can be instantiated as a plain Java object (OSGi-optional):
 * <pre>
 * OclExpressionParser parser = new OclParserSupport();
 * OclExpression expr = parser.parse("self.name", employeeEClass);
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Component(scope = ServiceScope.PROTOTYPE, property = "parser.type=DEFAULT")
public class OclParserSupport implements OclExpressionParser {

	/**
	 * Parses an OCL expression string in the context of the given classifier.
	 *
	 * @param expression the OCL expression text
	 * @param contextType the classifier that defines the type of {@code self}
	 * @return the parsed expression AST with types resolved
	 * @throws OclParseException if the expression contains syntax or type errors
	 */
	public OclExpression parse(String expression, EClassifier contextType) throws OclParseException {
		OclParser parser = createParser(expression);
		OclErrorListener errorListener = configureErrorHandling(parser);

		OclParser.ExpressionEntryContext tree = parser.expressionEntry();

		checkErrors(errorListener, expression);

		try {
			OclAstBuilder builder = new OclAstBuilder(contextType);
			return builder.visitExpressionEntry(tree);
		} catch (IllegalArgumentException e) {
			throw new OclParseException(e.getMessage());
		}
	}

	/**
	 * Parses a Complete OCL document, producing a list of constraints.
	 * Uses the global {@link EPackage.Registry#INSTANCE} for classifier resolution.
	 *
	 * @param oclDocument the Complete OCL document text
	 * @return the list of parsed constraints
	 * @throws OclParseException if the document contains syntax or type errors
	 */
	@Override
	public List<Constraint> parseDocument(String oclDocument) throws OclParseException {
		return parseDocument(oclDocument, EPackage.Registry.INSTANCE);
	}

	/**
	 * Parses a Complete OCL document using the given resource set for package resolution.
	 *
	 * @param oclDocument the Complete OCL document text
	 * @param resourceSet the resource set whose package registry is used
	 * @return the list of parsed constraints
	 * @throws OclParseException if the document contains syntax or type errors
	 */
	@Override
	public List<Constraint> parseDocument(String oclDocument, ResourceSet resourceSet)
			throws OclParseException {
		return parseDocument(oclDocument, resourceSet.getPackageRegistry());
	}

	private List<Constraint> parseDocument(String oclDocument, EPackage.Registry registry)
			throws OclParseException {
		OclParser parser = createParser(oclDocument);
		OclErrorListener errorListener = configureErrorHandling(parser);

		OclParser.CompleteOclDocumentEntryContext tree = parser.completeOclDocumentEntry();

		checkErrors(errorListener, oclDocument);

		return new OclDocumentBuilder(registry).buildDocument(tree);
	}

	private OclParser createParser(String input) {
		OclLexer lexer = new OclLexer(CharStreams.fromString(input));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		return new OclParser(tokens);
	}

	private OclErrorListener configureErrorHandling(OclParser parser) {
		OclErrorListener errorListener = new OclErrorListener();

		// Remove default console error listener
		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);

		// Also configure the lexer
		OclLexer lexer = (OclLexer) parser.getTokenStream().getTokenSource();
		lexer.removeErrorListeners();
		lexer.addErrorListener(errorListener);

		return errorListener;
	}

	private void checkErrors(OclErrorListener errorListener, String input)
			throws OclParseException {
		if (errorListener.hasErrors()) {
			List<Resource.Diagnostic> errors = errorListener.getErrors();
			String message = "OCL parse error: " + errors.get(0).getMessage();
			throw new OclParseException(message, errors);
		}
	}
}
