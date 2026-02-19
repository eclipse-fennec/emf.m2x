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
package org.eclipse.fennec.m2m.ocl.api;

import java.util.List;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.fennec.m2m.model.ocl.Constraint;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.osgi.annotation.versioning.ProviderType;

/**
 * Abstraction for OCL parsing, decoupling the engine from a specific parser implementation.
 *
 * <p>The engine receives an {@code OclExpressionParser} via constructor injection. In OSGi,
 * the parser is injected as an {@code @Reference}; in plain Java, the caller instantiates the
 * concrete parser implementation (e.g., {@code OclParserSupport}) and passes it to the engine.
 *
 * <p>This interface is named {@code OclExpressionParser} (not {@code OclParser}) to avoid
 * a naming collision with the ANTLR-generated {@code OclParser} class in the parser bundle.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ProviderType
public interface OclExpressionParser {

	/**
	 * Parses an OCL expression string in the context of the given classifier.
	 *
	 * @param expression the OCL expression text
	 * @param contextType the {@code EClassifier} that defines the type of {@code self}
	 * @return the parsed expression AST with types resolved
	 * @throws OclParseException if the expression contains syntax or type errors
	 */
	OclExpression parse(String expression, EClassifier contextType) throws OclParseException;

	/**
	 * Parses a Complete OCL document, producing a list of constraints.
	 *
	 * @param oclDocument the Complete OCL document text
	 * @return the list of parsed constraints with context references populated
	 * @throws OclParseException if the document contains syntax or type errors
	 */
	List<Constraint> parseDocument(String oclDocument) throws OclParseException;

	/**
	 * Parses a Complete OCL document using the given resource set for package resolution.
	 *
	 * @param oclDocument the Complete OCL document text
	 * @param resourceSet the resource set whose package registry is used for classifier resolution
	 * @return the list of parsed constraints with context references populated
	 * @throws OclParseException if the document contains syntax or type errors
	 */
	default List<Constraint> parseDocument(String oclDocument, ResourceSet resourceSet)
			throws OclParseException {
		return parseDocument(oclDocument);
	}
}
