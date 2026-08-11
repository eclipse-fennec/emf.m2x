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
 * @param line   1-based line
 * @param column 0-based column
 * @since 1.0
 */
public record SourcePosition(int line, int column) {

	@Override
	public String toString() {
		return line + ":" + column;
	}
}
