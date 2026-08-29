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

import org.eclipse.emf.ecore.resource.Resource;

/**
 * A parse or resolution problem with the place it was found.
 *
 * <p>One implementation for all four languages. Each of them used to carry its own, differing
 * from the others only in the package it sat in — and each was constructed with line 0 wherever
 * the problem was semantic rather than syntactic, which is what {@code UNKNOWN_POSITION} now
 * names instead of leaving a reader to guess whether the first character was meant.
 *
 * @since 1.0
 */
public final class ParseDiagnostic implements Resource.Diagnostic {

	/**
	 * Line and column of a diagnostic whose position is not known — a problem found while
	 * building the AST rather than while reading tokens, and reported without a rule context.
	 */
	public static final int UNKNOWN_POSITION = 0;

	private final String message;
	private final int line;
	private final int column;

	/**
	 * @param message what is wrong
	 * @param line    1-based line, or {@link #UNKNOWN_POSITION}
	 * @param column  0-based column, or {@link #UNKNOWN_POSITION}
	 */
	public ParseDiagnostic(String message, int line, int column) {
		this.message = message;
		this.line = line;
		this.column = column;
	}

	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>Always {@code null}: a parse diagnostic says where in a unit the problem is, not
	 * which unit it was. The unit travels beside it — with the parse result, and in the
	 * {@link SourcePosition#unit()} of a runtime diagnostic. There used to be a field for it
	 * here that no caller ever filled and no caller ever read (#185).
	 */
	@Override
	public String getLocation() {
		return null;
	}

	@Override
	public int getLine() {
		return line;
	}

	@Override
	public int getColumn() {
		return column;
	}

	/** Whether this diagnostic knows where it came from. */
	public boolean hasPosition() {
		return line != UNKNOWN_POSITION;
	}

	@Override
	public String toString() {
		return hasPosition() ? "[" + line + ":" + column + "] " + message : message;
	}
}
