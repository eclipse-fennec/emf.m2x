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

/**
 * Where a node stood in the text it was parsed from.
 *
 * <p>Kept beside the AST rather than in it: a compiled expression is shared by every occurrence of
 * the same expression text — that is what the expression cache is for — so an AST node cannot
 * carry one position without lying to the second occurrence. A map from node to position belongs
 * to the unit that was parsed, and is only as valid as that unit (#116).
 *
 * <p>The unit belongs here rather than beside it: with imports, several units contribute nodes to
 * one execution, and a line without the file it belongs to points confidently at the wrong place.
 *
 * @param unit   the unit the node was parsed from, or {@code null} when there is none to name
 * @param line   1-based line
 * @param column 0-based column
 * @since 1.0
 */
public record SourcePosition(String unit, int line, int column) {

	/**
	 * A position with no unit to name.
	 *
	 * @param line   1-based line
	 * @param column 0-based column
	 */
	public SourcePosition(int line, int column) {
		this(null, line, column);
	}

	/**
	 * The same position, attributed to a unit.
	 *
	 * @param unit the unit name
	 * @return a copy naming that unit
	 */
	public SourcePosition inUnit(String unit) {
		return new SourcePosition(unit, line, column);
	}

	@Override
	public String toString() {
		return unit == null ? line + ":" + column : unit + ":" + line + ":" + column;
	}
}
