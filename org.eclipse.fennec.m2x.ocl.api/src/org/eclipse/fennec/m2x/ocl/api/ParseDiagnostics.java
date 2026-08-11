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
package org.eclipse.fennec.m2x.ocl.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * Collects the problems found while building an AST, each with the place it was found.
 *
 * <p>Held rather than inherited: every builder in this workspace already extends the ANTLR
 * visitor generated for its grammar, so a common base class is not available to them. What they
 * share is this collaborator — one diagnostic list and one place where a {@link ParseDiagnostic}
 * is constructed.
 *
 * <p><b>Positions are plain numbers here, deliberately.</b> This is the API layer and carries no
 * parser dependency; taking a rule context would drag ANTLR into it and reach only the bundles
 * that depend on {@code ocl.parser}. As line and column, the same collaborator serves all four
 * languages — including QVT-O, whose parser has its own expression builder and no dependency on
 * the OCL parser. Each builder pulls the two numbers out of its own context, which is one line
 * where it stands.
 *
 * <p><b>The position cursor.</b> A visitor holds the rule context of the node it is on and can
 * pass it in. A helper called <em>by</em> a visitor — {@link AbstractExpressionBuilder}, which the
 * four languages share for OCL expressions — cannot: it is handed segments and types, not parse
 * trees. For those, the visitor sets the cursor as it descends, and a diagnostic without an
 * explicit context takes the position of the node last entered. That is the enclosing expression
 * rather than the exact token, which beats no position at all and is what an editor needs to put
 * a marker on the right line.
 *
 * @since 1.0
 */
public class ParseDiagnostics {

	private final List<Resource.Diagnostic> diagnostics = new ArrayList<>();
	private int cursorLine = ParseDiagnostic.UNKNOWN_POSITION;
	private int cursorColumn = ParseDiagnostic.UNKNOWN_POSITION;

	/**
	 * Remembers where the visitor currently is, for diagnostics reported without a position.
	 *
	 * @param line   1-based line
	 * @param column 0-based column
	 */
	public void positionAt(int line, int column) {
		cursorLine = line;
		cursorColumn = column;
	}

	/**
	 * Records a problem at the given place.
	 *
	 * @param message what is wrong
	 * @param line    1-based line
	 * @param column  0-based column
	 */
	public void addError(String message, int line, int column) {
		diagnostics.add(new ParseDiagnostic(message, line, column));
	}

	/**
	 * Records a problem at the cursor — the node the visitor last entered.
	 *
	 * @param message what is wrong
	 */
	public void addError(String message) {
		diagnostics.add(at(message));
	}

	/**
	 * Builds a diagnostic at the cursor without recording it here.
	 *
	 * <p>For a builder that already has a diagnostic list of its own — QVT-O's expression builder
	 * writes into the one its unit builder handed it — so that the position comes from here while
	 * the message still lands where its collector expects it.
	 *
	 * @param message what is wrong
	 * @return the diagnostic, positioned at the cursor
	 */
	public ParseDiagnostic at(String message) {
		return new ParseDiagnostic(message, cursorLine, cursorColumn);
	}

	/**
	 * Records an already-built diagnostic, for a collector that produced one itself.
	 *
	 * @param diagnostic the diagnostic, must not be {@code null}
	 */
	public void add(Resource.Diagnostic diagnostic) {
		diagnostics.add(diagnostic);
	}

	/**
	 * @return the diagnostics in the order they were found, unmodifiable
	 */
	public List<Resource.Diagnostic> getDiagnostics() {
		return Collections.unmodifiableList(diagnostics);
	}

	/**
	 * @return whether anything was reported
	 */
	public boolean hasErrors() {
		return !diagnostics.isEmpty();
	}

	/**
	 * Forgets everything reported so far, so one collector can serve several units.
	 */
	public void clear() {
		diagnostics.clear();
		cursorLine = ParseDiagnostic.UNKNOWN_POSITION;
		cursorColumn = ParseDiagnostic.UNKNOWN_POSITION;
	}
}
